package ncr.res.mobilepos.authentication.test;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;

import junit.framework.Assert;
import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.authentication.resource.AuthenticationResource;
import ncr.res.mobilepos.authentication.resource.RegistrationResource;
import ncr.res.mobilepos.deviceinfo.model.DeviceAttribute;
import ncr.res.mobilepos.deviceinfo.model.ViewDeviceInfo;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class RegistrationSteps extends Steps {
	private DBInitiator dbInit = null;
	private ServletContext servletContext = null;
	private String companyId = null;
	private String storeId = null;
	private String terminalId = null;
	private String businessDate = null;
	private String seqNo = null;
	private AuthenticationResource authenticationResource = null;
	private DeviceInfoResource deviceInfoResource = null;
	private ViewDeviceInfo viewInfo = null;
	private DeviceAttribute deviceAttribute = null;
	private DeviceStatus deviceStatus = null;
	private ResultBase resultbase = null;
	private RegistrationResource registerResource = null;
	
	@BeforeScenario
	public final void SetUpClass() {
		dbInit = new DBInitiator("SQLServerItemDAOSteps", DATABASE.RESMaster);
		Requirements.SetUp();
	}

	@AfterScenario
	public final void TearDownClass() {
		Requirements.TearDown();
	}

	@Given("a deviceinfo{$dataset} in database")
	public final void initdatasetsDpt(final String dataset) {
		try {
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, dataset);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Given("the Web API Starts Up with {$systemConDataSet} System Configuration")
	public final void GivenThatTheWebAPIStartsUpSytstemConfiguration(
			String systemConDataSet) {
		servletContext = null;

		try {
			servletContext = Requirements.getMockServletContext();
			deviceInfoResource = new DeviceInfoResource();
			authenticationResource = new AuthenticationResource();
			registerResource = new RegistrationResource();
			Field deviceContext;
			try {
				deviceContext = deviceInfoResource.getClass().getDeclaredField(
						"context");
				deviceContext.setAccessible(true);
				deviceContext.set(deviceInfoResource, servletContext);

				deviceContext = authenticationResource.getClass()
						.getDeclaredField("context");
				deviceContext.setAccessible(true);
				deviceContext.set(authenticationResource, servletContext);
				
				deviceContext = registerResource.getClass()
						.getDeclaredField("context");
				deviceContext.setAccessible(true);
				deviceContext.set(registerResource, servletContext);

				dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
						systemConDataSet);
			} catch (Exception ex) {
				Assert.fail("Can't Retrieve Servlet context from promotion.");
			}
		} catch (/* RuntimeError */Exception ex) {
			Assert.fail("Can't Start Up WebStoreServer");
		}
	}
	
	@When("I get device attribute companyid{$companyid} storeid{$storeid} terminalid{$terminalid}")
	public final void whenIRegisterDevice(String companyId, String storeId, String terminalId){
		deviceAttribute = deviceInfoResource.getDeviceAttribute(companyId, storeId, terminalId);
	}
	
	@Then("I should get the resultcode resultCode{$resultCode}")
	public final void registerDeviceFailed(String resultCode){
		int result = Integer.parseInt(resultCode);
		Assert.assertEquals(result, deviceAttribute.getNCRWSSResultCode());
	}
	
	@Then("I should get the following: $data")
	public final void thenIShouldGetData(ExamplesTable expectedData) {
		for (Map<String, String> data : expectedData.getRows()) {
			Assert.assertEquals(data.get("Message").trim(), deviceAttribute.getMessage());
			Assert.assertEquals(data.get("CompanyId").trim(), deviceAttribute.getCompanyId());
			Assert.assertEquals(data.get("StoreId").trim(), deviceAttribute.getStoreId());
			Assert.assertEquals(data.get("TerminalId").trim(), deviceAttribute.getTerminalId());
			Assert.assertEquals(data.get("DeviceName").trim(), deviceAttribute.getDeviceName());
			Assert.assertEquals(data.get("NCRWSSResultCode").trim(), String.valueOf(deviceAttribute.getNCRWSSResultCode()));
		
		}
	}
	
	@When("I register device companyid{$companyid} storeid{$storeid} terminalid{$terminalid} devicename{$devicename} passcode{$passcode} uuid{$uuid} udid{$udid} signstatus{$signstatus}")
	public final void whenIRegisterDevice(String companyId, String storeId, String terminalId, String deviceName, String passCode, String uuid, String udid, String signstatus){
		int signin = Integer.parseInt(signstatus);
		deviceStatus = registerResource.registerDevice(companyId, storeId, terminalId, deviceName, passCode, udid, uuid, signin, "", "");
	}
	
	@Then("I should get the following registration response: $data")
	public final void thenIShouldGetResponse(ExamplesTable expectedData) {
		for (Map<String, String> data : expectedData.getRows()) {
			Assert.assertEquals(data.get("Message").trim(), deviceStatus.getMessage());
			Assert.assertEquals(data.get("StoreID").trim(), deviceStatus.getStoreID());
			Assert.assertEquals(data.get("CorpID").trim(), deviceStatus.getCorpID());
			Assert.assertEquals(data.get("TerminalId").trim(), deviceStatus.getTerminalID());
			Assert.assertEquals(data.get("NCRWSSResultCode").trim(), String.valueOf(deviceStatus.getNCRWSSResultCode()));
		
		}
	}
	
	
	
}
