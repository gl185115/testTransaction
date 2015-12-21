package ncr.res.mobilepos.deviceinfo.resource.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Map;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.deviceinfo.model.TerminalInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewTerminalInfo;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.ExceptionHelper;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.ResultBaseHelper;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.StringUtility;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

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
    	deviceInfo = new DeviceInfoResource();
    	terminalInfo = new TerminalInfo();
    	viewTerminalInfo = new ViewTerminalInfo();
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
    			"test/ncr/res/mobilepos/deviceinfo/datasets/MST_TERMINALINFO.xml");
    }
    
    @Given("that database is throwing an unexpected {$exception}")
   	public final void givenThrownException(String exception) {
    	MockitoAnnotations.initMocks(this);
        Exception ex = ExceptionHelper.getException(exception);  			
        try {
        	Mockito.stub(daoFactory.getDeviceInfoDAO()).toThrow(ex);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    @When("I request to get terminal info of companyId{$companyId}, storeId{$storeId} and terminalId{$terminalId}")
    public final void whenIGetTerminalInfo(String companyId, String storeId, String terminalId) {
    	companyId = StringUtility.convNullStringToNull(companyId);
    	storeId = StringUtility.convNullStringToNull(storeId);
    	terminalId = StringUtility.convNullStringToNull(terminalId);
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
    
    @Then("the result should be {$result}")
    public final void thenResultShouldBe(String result) {
    	 assertEquals(ResultBaseHelper.getErrorCode(result), viewTerminalInfo.getNCRWSSResultCode());
    }

}
