package ncr.res.mobilepos.authentication.test;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;

import junit.framework.Assert;
import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.authentication.resource.AuthenticationResource;
import ncr.res.mobilepos.department.resource.DepartmentResource;
import ncr.res.mobilepos.deviceinfo.model.DeviceAttribute;
import ncr.res.mobilepos.deviceinfo.model.ViewDeviceInfo;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.promotion.resource.PromotionResource;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

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
				Assert.fail("Can't Retrieve Servlet context from promotion.");
			}
		} catch (/* RuntimeError */Exception ex) {
			Assert.fail("Can't Start Up WebStoreServer");
		}
	}
			
	//Authentication
	@When("I authenticate device companyid{$companyid} storeid{$storeid} deviceid{$terminalid} training{$training} udid{$udid} uuid{$uuid}")
	public final void whenAuthenticateDeviceFailed(String companyId, String storeId, String deviceId, String training, String udid, String uuid){
		int trainingMode = Integer.parseInt(training);
	
		viewInfo = deviceInfoResource.getDeviceInfo(companyId, storeId, deviceId, trainingMode);	
		deviceStatus = authenticationResource.authenticateDevice(companyId, storeId, deviceId, udid, uuid);
		resultbase = deviceInfoResource.getAttribute(storeId, uuid, companyId, training);
		
	}
	
	@Then("I should get the NCRWSSResultCode{$resultCode}")
	public final void thenAuthenticateDeviceFailed(String resultCode){
		int result = Integer.parseInt(resultCode);
		Assert.assertEquals(result, deviceStatus.getNCRWSSResultCode());
		
//		int i = 0;
//		
//		for(Map<String, String> data : table.getRows()){
//			Assert.assertNull(viewInfo.getDeviceInfo().getCompanyId());
//			Assert.assertNull(viewInfo.getDeviceInfo().getRetailStoreId());
//			Assert.assertNull(viewInfo.getDeviceInfo().getDeviceId());
//			Assert.assertNull(viewInfo.getDeviceInfo().getDeviceName());
//			Assert.assertEquals(0,viewInfo.getNCRWSSExtendedResultCode());
//			Assert.assertEquals(data.get("NCRWSSResultCode"), String.valueOf(viewInfo.getNCRWSSResultCode()));
//			Assert.assertNull(viewInfo.getDeviceInfo().getTillId());
//			Assert.assertNull(viewInfo.getDeviceInfo().getPrinterInfo());
//			
//			Assert.assertEquals(data.get("NCRWSSResultCode2"),String.valueOf(deviceStatus.getNCRWSSResultCode()));
//			Assert.assertEquals(data.get("resultCode"), String.valueOf(resultbase.getNCRWSSResultCode()));
//		
//		}
	}
	

	
	
    
	
	
	
	
	
	
	
}
