package ncr.res.mobilepos.authentication.test;

import junit.framework.Assert;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.authentication.resource.AuthenticationResource;
import ncr.res.mobilepos.deviceinfo.model.DeviceAttribute;
import ncr.res.mobilepos.deviceinfo.model.ViewDeviceInfo;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.model.ResultBase;

public class AuthenticationSteps extends Steps {
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
	
	@BeforeScenario
	public final void SetUpClass() {
		dbInit = new DBInitiator("AuthenticationSteps", DATABASE.RESMaster);
		Requirements.SetUp();
	}

	@AfterScenario
	public final void TearDownClass() {
		Requirements.TearDown();
	}
	
	
	@Given("a deviceinfo $dataset in database")
	public final void initdatasetsDpt(final String dataset) {
		try {
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, dataset);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Given("the Web API Starts Up with $systemConDataSet System Configuration")
	public final void GivenThatTheWebAPIStartsUpSytstemConfiguration(
			String systemConDataSet) {
		servletContext = null;
		
		try {
			servletContext = Requirements.getMockServletContext();
			deviceInfoResource = new DeviceInfoResource();
			authenticationResource = new AuthenticationResource();
			Field deviceContext;
			try {
				deviceContext = deviceInfoResource.getClass()
						.getDeclaredField("context");
				deviceContext.setAccessible(true);
				deviceContext.set(deviceInfoResource, servletContext);
				
				deviceContext = authenticationResource.getClass()
						.getDeclaredField("context");
				deviceContext.setAccessible(true);
				deviceContext.set(authenticationResource, servletContext);
				
				dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
						systemConDataSet);
			} catch (Exception ex) {
				Assert.fail("Can't Retrieve Servlet context from deviceinfo.");
			}
		} catch (/* RuntimeError */Exception ex) {
			Assert.fail("Can't Start Up WebStoreServer");
		}
	}
			

	@When("I authenticate device companyid $companyid storeid $storeid deviceid $terminalid training $training udid $udid uuid $uuid")
	public final void whenAuthenticateDeviceFailed(String companyId, String storeId , String deviceId, String training, String udid, String uuid){
		int trainingMode = Integer.parseInt(training);
	
		viewInfo = deviceInfoResource.getDeviceInfo(companyId, storeId, deviceId, trainingMode);	
		deviceStatus = authenticationResource.authenticateDevice(companyId, storeId, deviceId, udid, uuid);
		resultbase = deviceInfoResource.getAttribute(storeId, deviceId,  companyId, training);
		
	}
	
	@Then("I should get the NCRWSSResultCode $resultCode")
	public final void thenAuthenticateDeviceFailed(String resultCode){
		int result = Integer.parseInt(resultCode);
		Assert.assertEquals(result, deviceStatus.getNCRWSSResultCode());
		
	}
	
	
	@Then("I should get the following: $data")
	public final void thenAuthenticateDeviceSuccess(ExamplesTable expectedTable){
		for(Map<String, String> data: expectedTable.getRows()){
			Assert.assertEquals(data.get("Message").trim(), deviceStatus.getMessage());
			Assert.assertEquals(data.get("StoreID").trim(), deviceStatus.getStoreID());
			Assert.assertEquals(data.get("TerminalId").trim(), deviceStatus.getTerminalID());
			Assert.assertEquals(data.get("signStatus").trim(), String.valueOf(deviceStatus.getSignStatus()));
			Assert.assertEquals(data.get("NCRWSSResultCode").trim(), String.valueOf(deviceStatus.getNCRWSSResultCode()));		
		}
	}

	@Then("I should get the device info: $data")
	public final void thenIshouldGetDeviceInfo(ExamplesTable expectedTable){
		for(Map<String, String> data: expectedTable.getRows()){
			Assert.assertEquals(data.get("CompanyId").trim(), viewInfo.getDeviceInfo().getCompanyId());
			Assert.assertEquals(data.get("RetailStoreID").trim(), viewInfo.getDeviceInfo().getRetailStoreId());
			Assert.assertEquals(data.get("DeviceID").trim(), viewInfo.getDeviceInfo().getDeviceId());
			Assert.assertEquals(data.get("Name").trim(), String.valueOf(viewInfo.getDeviceInfo().getDeviceName()));
			Assert.assertEquals(data.get("NCRWSSResultCode").trim(), String.valueOf(viewInfo.getNCRWSSResultCode()));
		}
	}
	
	@Then("I should get attributes: $data")
	public final void thenIshouldGetAttribute(ExamplesTable expectedTable){
		for(Map<String, String> data: expectedTable.getRows()){
			
		Assert.assertEquals(data.get("message").trim(), resultbase.getMessage());
		Assert.assertEquals(data.get("NCRWSSResultCode").trim(), String.valueOf(resultbase.getNCRWSSResultCode()));
	
		}

	}
	
	
    
	
	
	
	
	
	
	
}
