package ncr.res.mobilepos.deviceattribute.resource.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.deviceinfo.model.AttributeInfo;
import ncr.res.mobilepos.deviceinfo.model.DeviceAttribute;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.ResultBaseHelper;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
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

import junit.framework.Assert;

public class GetDeviceAttributeSteps extends Steps {
	private DBInitiator dbInitiator;
	@InjectMocks
	private DeviceInfoResource deviceInfoResource;
	@Mock
	private DAOFactory daoFactory;
	private DeviceAttribute deviceattribute;

	/**
	 * Connects to the database.
	 */
	@BeforeScenario
	public final void setUp() {
		Requirements.SetUp();
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
		ServletContext servletContext = Requirements.getMockServletContext();
		deviceInfoResource = new DeviceInfoResource();
		try {
			Field fieldContext = deviceInfoResource.getClass()
					.getDeclaredField("context");
			fieldContext.setAccessible(true);
			fieldContext.set(deviceInfoResource, servletContext);
			dbInitiator = new DBInitiator("DeviceInfoResourceTest",
					DATABASE.RESMaster);
			dbInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/deviceattribute/resource/test/MST_DEVICEINFO.xml");
			dbInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/deviceattribute/resource/test/PRM_DEVICE_ATTRIBUTE.xml");
			dbInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/deviceattribute/resource/test/MST_TILLIDINFO.xml");
			dbInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/deviceattribute/resource/test/PRM_QUEUEBUSTER_LINK.xml");

		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Can't Retrieve Servlet context from promotion.");
		}
	}

	@When("I request to get device attribute info of companyId $companyId, storeId $storeId and terminalId $terminalId")
	public final void whenIGetDeviceAttributeInfo(String companyId,
			String storeId, String terminalId) {
		companyId = companyId.equalsIgnoreCase("empty") ? "" : StringUtility
				.convNullStringToNull(companyId);
		storeId = storeId.equalsIgnoreCase("empty") ? "" : StringUtility
				.convNullStringToNull(storeId);
		terminalId = terminalId.equalsIgnoreCase("empty") ? "" : StringUtility
				.convNullStringToNull(terminalId);
		deviceattribute = deviceInfoResource.getDeviceAttribute(companyId,
				storeId, terminalId);
	}

	@Then("I should have the following data: $examplesTable")
	public final void thenIShouldHaveTheFollowingData(
			final ExamplesTable expected) {
		for (Map<String, String> data : expected.getRows()) {

			Assert.assertEquals("Compare CompanyId", data.get("companyId"),
					deviceattribute.getCompanyId());
			Assert.assertEquals("Compare StoreId", data.get("storeId"),
					deviceattribute.getStoreId());
			Assert.assertEquals("Compare TerminalId", data.get("terminalId"),
					deviceattribute.getTerminalId());
			Assert.assertEquals("Compare Training",
					data.get("training").trim(),
					String.valueOf(deviceattribute.getTrainingMode()));
			Assert.assertEquals("Compare DeviceName", data.get("deviceName"),
					deviceattribute.getDeviceName());
			Assert.assertEquals("Compare AttributeId", data.get("attributeId"),
					deviceattribute.getAttributeId());
			Assert.assertEquals("Compare PrinterId", data.get("printerId"),
					deviceattribute.getPrinterId());
			Assert.assertEquals("Compare TillId", data.get("tillId"),
					deviceattribute.getTillId());
			Assert.assertEquals("Compare LinkQueueBuster",
					data.get("linkQueueBuster"),
					deviceattribute.getLinkQueueBuster());
			Assert.assertEquals("Compare PrintDes", data.get("printDes"),
					deviceattribute.getPrintDes());
			Assert.assertEquals("Compare DrawerId", data.get("drawerId"),
					deviceattribute.getDrawerId());
			Assert.assertEquals("Compare DisplayName", data.get("displayName"),
					deviceattribute.getDisplayName());
			Assert.assertEquals("Compare AttributeDes",
					data.get("attributeDes"), deviceattribute.getAttributeDes());
		}
	}

	@Then("the result should be $result")
	public final void thenTheResultShouldBe(String result) {
		assertEquals(ResultBaseHelper.getErrorCode(result),
				deviceattribute.getNCRWSSResultCode());
	}
}
