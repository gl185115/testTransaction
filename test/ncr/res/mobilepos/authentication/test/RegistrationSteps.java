package ncr.res.mobilepos.authentication.test;

import java.lang.reflect.Field;

import javax.servlet.ServletContext;

import junit.framework.Assert;
import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.authentication.resource.AuthenticationResource;
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
				deviceContext = deviceInfoResource.getClass().getDeclaredField(
						"context");
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
	
	@When("register companyid{$companyid} storeid{$storeid} terminalid{$terminalid}")
	public final void whenIRegisterDevice(String companyId, String storeId, String terminalId){
		deviceAttribute = deviceInfoResource.getDeviceAttribute(companyId, storeId, terminalId);
	}
	
	@Then("I should get the resultcode resultCode{$resultCode}")
	public final void registerDeviceFailed(String resultCode){
		int result = Integer.parseInt(resultCode);
		Assert.assertEquals(result, deviceAttribute.getNCRWSSResultCode());
	}
	
	
}
