package ncr.res.mobilepos.device.resource.test;


import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;

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

import com.thoughtworks.xstream.mapper.Mapper.Null;

import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.deviceinfo.model.PosControlOpenCloseStatus;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.ResultBaseHelper;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import junit.framework.Assert;

public class GetDeviceStatusSteps extends Steps {
	private DBInitiator dbInitiator;
	@InjectMocks
	 DeviceInfoResource pdc = null;
	@Mock
	private DAOFactory daoFactory;
	private DeviceStatus getdevicestatus;
	
	private DeviceInfoResource deviceInfoResource = null;
	private ResultBase resultBase = null;
	private PosControlOpenCloseStatus posControlOpenCloseStatus = null;

	/**
	 * Connects to the database.
	 */
	@BeforeScenario
	public final void setUp() {
		Requirements.SetUp();
		ServletContext mockContext = Requirements.getMockServletContext();
		deviceInfoResource = new DeviceInfoResource();
		try {
			Field contextField = deviceInfoResource.getClass().getDeclaredField("context");
			contextField.setAccessible(true);
			contextField.set(deviceInfoResource, mockContext);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Destroys the database connection.
	 */
	@AfterScenario
	public final void tearDown() {
		Requirements.TearDown();
	}

	@Given("that no businessdate")
	public final void givenThatNoBusinessDate(){
		DBInitiator resMasterDbInitiator = new DBInitiator("GetDeviceStatusSteps", DATABASE.RESMaster);
		try {
			resMasterDbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/device/resource/test/MST_BIZDAY_Empty.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Given("that has businessdate")
	public final void givenThatHasBusinessDate() {
		DBInitiator resMasterDbInitiator = new DBInitiator("GetDeviceStatusSteps", DATABASE.RESMaster);
		try {
			resMasterDbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/device/resource/test/MST_BIZDAY_2016-08-16.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@When("getting device status companyId:$1 storeId:$2 terminalId:$3")
	public final void getDeviceStatus(final String companyIdParam, final String storeIdParam, final String terminalIdParam) {
		String companyId = (companyIdParam.equalsIgnoreCase("null")) ? null: 
			((companyIdParam.equalsIgnoreCase("empty"))? "": companyIdParam);
		String storeId = (storeIdParam.equalsIgnoreCase("null")) ? null: 
			((storeIdParam.equalsIgnoreCase("empty"))? "": storeIdParam);
		String terminalId = (terminalIdParam.equalsIgnoreCase("null")) ? null: 
			((terminalIdParam.equalsIgnoreCase("empty"))? "": terminalIdParam);
		resultBase = deviceInfoResource.getDeviceStatus(companyId, storeId, terminalId);
		if(resultBase instanceof PosControlOpenCloseStatus){
			posControlOpenCloseStatus = (PosControlOpenCloseStatus) resultBase;
		}
	}
	@Given("that has no data in txu_pos_ctrl")
	public final void noDataInTxuPosCtrl(){
		DBInitiator resTransactionDbInitiator = new DBInitiator("GetDeviceStatusSteps", DATABASE.RESTransaction);
		try {
			resTransactionDbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/device/resource/test/TXU_POS_CTRL_Empty.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Given("that has data in txu_pos_ctrl")
	public final void hasDataInTxuPosCtrl(){
		DBInitiator resTransactionDbInitiator = new DBInitiator("GetDeviceStatusSteps", DATABASE.RESTransaction);
		try {
			resTransactionDbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/device/resource/test/TXU_POS_CTRL_1.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Then("it should get OpenCloseStat:$1")
	public final void getOpenCloseStat(final String expected) {
		Assert.assertEquals("Compare OpenCloseStat", Short.parseShort(expected), posControlOpenCloseStatus.getOpenCloseStat());
	}
	@Then("it should get NCRWSSResultCode:$1")
	public final void getResultCode(final String expected) {
		Assert.assertEquals("Compare ncrwssresultcode", Integer.parseInt(expected), resultBase.getNCRWSSResultCode());
	}
	@Given("I have a DeviceInfo resource and other resources")
	public final void givenIHaveDeviceInfo() throws Exception {
		 ServletContext context = Requirements.getMockServletContext();
	        pdc = new DeviceInfoResource();
	        try {
	           Field Context = pdc.getClass().getDeclaredField("context");
	            Context.setAccessible(true);
	           Context.set(pdc, context);
	           Assert.assertNotNull(pdc);
	       } catch (Exception e) {
	            e.printStackTrace();
	            Assert.fail("Cant load Mock Servlet Context.");
	        }
	}

	@When("I request to get device attribute info of companyId $companyId, storeId $storeId and terminalId $terminalId")
	public final void whenIGetDeviceStatusInfo(String companyId,
			String storeId, String terminalId) {
		
	}

	@Then("I should have the following data: $examplesTable")
    public final void thenIShouldHaveTheFollowingData (ExamplesTable examplesTable) 
    		throws Exception  {
    	for (Map<String, String> data : examplesTable.getRows()) {
    		
    	}
    }
	

	@Then("the result should be $result")
	public final void thenTheResultShouldBe(String result) {
		assertEquals(ResultBaseHelper.getErrorCode(result),
				getdevicestatus.getNCRWSSResultCode());
	}
}
