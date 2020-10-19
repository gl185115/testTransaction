package ncr.res.mobilepos.pricing.pricemminfo.test;

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

import ncr.res.mobilepos.constant.SystemFileConfig;
import ncr.res.mobilepos.constant.WindowsEnvironmentVariables;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.pricing.factory.PriceMMInfoFactory;
import ncr.res.mobilepos.pricing.model.PriceMMInfo;
import ncr.res.mobilepos.pricing.resource.ItemResource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PriceMMListSteps extends Steps {
	private ItemResource itemResource = null;
	private PriceMMInfo priceMMInfo = null;
	private DBInitiator dbInit = null;
	private static int status = 0;
	public static final int ERROR_EXCEPTION = 2;

	@BeforeScenario
	public final void setUpClass() {
		dbInit = new DBInitiator("SQLServerItemDAOSteps", DATABASE.RESMaster);
		Requirements.SetUp();
		initResources();
	}

	@AfterScenario
	public final void tearDownClass() {
		Requirements.TearDown();
	}

	private void initResources() {
		ServletContext context = Requirements.getMockServletContext();
		try {
			itemResource = new ItemResource();
			Field contextField = itemResource.getClass().getDeclaredField("context");
			contextField.setAccessible(true);
			contextField.set(itemResource, context);
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/pricing/promdetailinfo/test/mst_bizday.xml");
			
			SystemFileConfig sys = SystemFileConfig.initInstance(WindowsEnvironmentVariables.getInstance().getSystemPath());
			String companyId = sys.getCompanyId();
			String storeId = sys.getStoreId();

			PriceMMInfoFactory.initialize(companyId,storeId, null);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail("Cant load Mock Servlet Context.");
		}
	}

	@Given("a loaded dataset $dataset")
	public final void initdatasetsDpt(final String dataset) {
		try {
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, dataset);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@When("get the priceMMInfo with sku $sku")
	public final void getPriceMMInfo(final String sku) throws Exception{
		priceMMInfo = itemResource.getPriceMMInfo(sku);
	}


	@Then("I should get the PriceMMInfo : $expectedItems")
	public final void priceMMInfoShouldBe(final ExamplesTable expectedItems) {
		for (Map<String, String> expectedItem : expectedItems.getRows()) {
			assertThat("Compare the MMNo at row ",
					"" + priceMMInfo.getMMNo(),
					is(equalTo(expectedItem.get("MMNo"))));
			assertThat("Compare the ConditionCount1 row ",
					"" + priceMMInfo.getConditionCount1(),
					is(equalTo(expectedItem.get("ConditionCount1"))));
			assertThat("Compare the ConditionCount2 row ",
					"" + priceMMInfo.getConditionCount2(),
					is(equalTo(expectedItem.get("ConditionCount2"))));
			assertThat("Compare the ConditionCount3 row ",
					"" + priceMMInfo.getConditionCount3(),
					is(equalTo(expectedItem.get("ConditionCount3"))));
			assertThat("Compare the ConditionPrice1 row ",
					"" + priceMMInfo.getConditionPrice1(),
					is(equalTo(expectedItem.get("ConditionPrice1"))));
			assertThat("Compare the ConditionPrice2 row ",
					"" + priceMMInfo.getConditionPrice2(),
					is(equalTo(expectedItem.get("ConditionPrice2"))));
			assertThat("Compare the ConditionPrice3 row ",
					"" + priceMMInfo.getConditionPrice3(),
					is(equalTo(expectedItem.get("ConditionPrice3"))));
			assertThat("Compare the DecisionPrice1 row ",
					"" + priceMMInfo.getDecisionPrice1(),
					is(equalTo(expectedItem.get("DecisionPrice1"))));
			assertThat("Compare the DecisionPrice2 row ",
					"" + priceMMInfo.getDecisionPrice2(),
					is(equalTo(expectedItem.get("DecisionPrice2"))));
			assertThat("Compare the DecisionPrice3 row ",
					"" + priceMMInfo.getDecisionPrice3(),
					is(equalTo(expectedItem.get("DecisionPrice3"))));
			assertThat("Compare the AveragePrice1 row ",
					"" + priceMMInfo.getAveragePrice1(),
					is(equalTo(expectedItem.get("AveragePrice1"))));
			assertThat("Compare the AveragePrice2 row ",
					"" + priceMMInfo.getAveragePrice2(),
					is(equalTo(expectedItem.get("AveragePrice2"))));
			assertThat("Compare the AveragePrice3 row ",
					"" + priceMMInfo.getAveragePrice3(),
					is(equalTo(expectedItem.get("AveragePrice3"))));
			assertThat("Compare the Note row ",
					"" + priceMMInfo.getNote(),
					is(equalTo(expectedItem.get("Note"))));
			assertThat("Compare the Sku row ",
					"" + priceMMInfo.getSku(),
					is(equalTo(expectedItem.get("Sku"))));
		}
	}

	@Then("the Result Code is $resultcode")
	public final void theResultCodeIs(int expectedResultCode) {
		Assert.assertEquals("Expect the result code", expectedResultCode,
				status);
	}

	@Then("the emptyResult is $expectedResult")
	public final void priceMMInfoIsNull(String expectedResult) {
		assertThat("Expect the result ",
				"" + priceMMInfo,
				is(equalTo(expectedResult)));
	}
}