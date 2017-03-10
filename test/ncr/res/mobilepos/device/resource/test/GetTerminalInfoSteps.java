package ncr.res.mobilepos.device.resource.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.department.resource.DepartmentResource;
import ncr.res.mobilepos.deviceinfo.model.TerminalInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewTerminalInfo;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.ExceptionHelper;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.ResultBaseHelper;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.pricing.resource.ItemResource;
import ncr.res.mobilepos.promotion.resource.PromotionResource;
import ncr.res.mobilepos.helper.StringUtility;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import junit.framework.Assert;

public class GetTerminalInfoSteps extends Steps {
	private DBInitiator dbInitiator;
	@InjectMocks
	private DeviceInfoResource deviceInfo;
	@Mock
	private DAOFactory daoFactory;
	private TerminalInfo terminalInfo;
	private ViewTerminalInfo viewTerminalInfo;
	
	/**
     *Connects to the database.
     */
    @BeforeScenario
    public final void setUp() {
    	Requirements.SetUp();
        dbInitiator =  new DBInitiator("DeviceInfoResourceTest", DATABASE.RESMaster);
    }
    
    /**
     * Destroys the database connection.
     */
    @AfterScenario
    public final void tearDown() {
        Requirements.TearDown();
    }
    
    @Given("I have a DeviceInfo resource and other resources")
    public final void givenIHaveDeviceInfo() throws Exception {
    	terminalInfo = new TerminalInfo();
    	viewTerminalInfo = new ViewTerminalInfo();
        
    	
    	ServletContext servletContext = null;
		try {
			servletContext = Requirements.getMockServletContext();
			deviceInfo = new DeviceInfoResource();
			try {
				Field fieldContext = deviceInfo.getClass()
						.getDeclaredField("context");
				fieldContext.setAccessible(true);
				fieldContext.set(deviceInfo, servletContext);
				
				dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
		    			"test/ncr/res/mobilepos/device/resource/test/MST_TERMINALINFO.xml");
			} catch (Exception ex) {
				ex.printStackTrace();
				Assert.fail("Can't Retrieve Servlet context from promotion.");
			}
		} catch (/* RuntimeError */Exception ex) {
			Assert.fail("Can't Start Up WebStoreServer");
		}
    }
    
    @Given("that database is throwing an unexpected $exception")
   	public final void givenThrownException(String exception) {
    	MockitoAnnotations.initMocks(this);
        Exception ex = ExceptionHelper.getException(exception);  			
        try {
        	Mockito.stub(daoFactory.getDeviceInfoDAO()).toThrow(ex);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
          
    @When("I request to get terminal info of companyId $companyId, storeId $storeId and terminalId $terminalId")
    public final void whenIGetTerminalInfo(String companyId, String storeId, String terminalId) {
    	companyId = StringUtility.convNullStringToNull(companyId);
    	storeId = StringUtility.convNullStringToNull(storeId);
    	terminalId = StringUtility.convNullStringToNull(terminalId);
    	terminalId = terminalId.equalsIgnoreCase("empty") ? "": terminalId;
    	viewTerminalInfo = deviceInfo.getTerminalInfo(companyId, storeId, terminalId);
    	terminalInfo = viewTerminalInfo.getTerminalInfo();
    }
    
    @Then("I should have the following data: $examplesTable")
    public final void thenIShouldHaveTheFollowingData (ExamplesTable examplesTable) 
    		throws Exception  {
    	Class<?> fieldType  = null;
    	String expectedValue;
    	String fieldName;
    	
    	for (Map<String, String> data : examplesTable.getRows()) {
    		fieldName = data.get("Key");
    		Field field = terminalInfo.getClass().getDeclaredField(fieldName);
    		field.setAccessible(true);
    		fieldType = field.getType();
    		expectedValue = data.get("Value");
    		
    		if (fieldType.equals(String.class)) {
    			assertEquals("Compare " + fieldName + ": ", expectedValue,
    	    			(String) field.get(terminalInfo));
    		} else if (fieldType.equals(int.class)) {
    			assertEquals("Compare " + fieldName + ": ", Integer.parseInt(expectedValue),
    	    			field.getInt(terminalInfo));
    		}
    	}
    }
    
    @Then("TerminalInfo should be null")
    public final void terminalInfoShouldBeNull() {
    	assertNull(terminalInfo);
    }
    
    @Then("the result should be $result")
    public final void thenResultShouldBe(String result) {
    	 assertEquals(ResultBaseHelper.getErrorCode(result), viewTerminalInfo.getNCRWSSResultCode());
    }

}
