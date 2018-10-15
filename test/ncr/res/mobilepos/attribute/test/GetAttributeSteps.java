package ncr.res.mobilepos.attribute.test;

import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.smartcardio.ATR;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.deviceinfo.model.AttributeInfo;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.ExceptionHelper;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.ResultBaseHelper;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.helper.StringUtility;

import org.dbunit.operation.DatabaseOperation;
import org.hamcrest.core.IsInstanceOf;
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

public class GetAttributeSteps extends Steps {
	private DBInitiator dbInitiator;
	@InjectMocks
	private DeviceInfoResource deviceInfoResource;
	@Mock
	private DAOFactory daoFactory;
	private AttributeInfo attributeInfo = null;
	private ResultBase resultBase = null;

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
			Field fieldContext = deviceInfoResource.getClass().getDeclaredField("context");
			fieldContext.setAccessible(true);
			fieldContext.set(deviceInfoResource, servletContext);
			dbInitiator = new DBInitiator("DeviceInfoResourceTest",
					DATABASE.RESMaster);
			dbInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/attribute/test/MST_DEVICEINFO.xml");
			dbInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/attribute/test/PRM_DEVICE_ATTRIBUTE.xml");

		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Can't Retrieve Servlet context from promotion.");
		}
	}
	
	@When("I request to get attribute info of companyId $companyId, storeId $storeId, terminalId $terminalId and training $training")
	public final void whenIGetAttributeInfo(String companyId, String storeId,
			String terminalId, String training) {
		companyId = companyId.equalsIgnoreCase("empty") ? "": StringUtility.convNullStringToNull(companyId);
		storeId = StringUtility.convNullStringToNull(storeId);
		terminalId = StringUtility.convNullStringToNull(terminalId);
		training = StringUtility.convNullStringToNull(training);
		resultBase = deviceInfoResource.getAttribute(storeId, terminalId, companyId, training);		
		if(resultBase instanceof AttributeInfo){
			attributeInfo = (AttributeInfo) resultBase;
		}
	}

	@Then("I should have the following data: $examplesTable")
	public final void thenIShouldHaveTheFollowingData(ExamplesTable expected)
			throws Exception {
		for (Map<String, String> data : expected.getRows()) {
			Assert.assertEquals("Compare attributeId", data.get("attributeId"),
					attributeInfo.getAttributeId());
			Assert.assertEquals("Compare printer", data.get("printer"),
					attributeInfo.getPrinter());
			Assert.assertEquals("Compare till", data.get("till"),
					attributeInfo.getTill());
			Assert.assertEquals("Compare creditTerminal",
					data.get("creditTerminal"),
					attributeInfo.getCreditTerminal());
			Assert.assertEquals("Compare MSR", data.get("MSR"),
					attributeInfo.getAttributeId());
			Assert.assertEquals("Compare cashChanger", data.get("cashChanger"),
					attributeInfo.getCashChanger());
			Assert.assertEquals("Compare attribute1", data.get("attribute1"),
					attributeInfo.getAttribute1());
			Assert.assertEquals("Compare attribute2", data.get("attribute2"),
					attributeInfo.getAttribute2());
			Assert.assertEquals("Compare attribute3", data.get("attribute3"),
					attributeInfo.getAttribute3());
			Assert.assertEquals("Compare attribute4", data.get("attribute4"),
					attributeInfo.getAttribute4());
			Assert.assertEquals("Compare attribute5", data.get("attribute5"),
					attributeInfo.getAttribute5());
			Assert.assertEquals("Compare attribute6", data.get("attribute6"),
					attributeInfo.getAttribute6());
			Assert.assertEquals("Compare attribute7", data.get("attribute7"),
					attributeInfo.getAttribute7());
			Assert.assertEquals("Compare attribute8", data.get("attribute8"),
					attributeInfo.getAttribute8());
			Assert.assertEquals("Compare training mode", data.get("training")
					.trim(), String.valueOf(attributeInfo.getTrainingMode()));

		}

	}
	@Then("the result should be $result")
	public final void thenResultShouldBe(String result) {
		assertEquals(ResultBaseHelper.getErrorCode(result), resultBase.getNCRWSSResultCode());
	}

}
