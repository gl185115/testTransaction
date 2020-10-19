package ncr.res.mobilepos.pricing.promdetailinfo.test;

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
import ncr.res.mobilepos.pricing.factory.PricePromInfoFactory;
import ncr.res.mobilepos.pricing.model.PricePromInfo;
import ncr.res.mobilepos.pricing.resource.ItemResource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PromDetailListSteps extends Steps {
	private ItemResource itemResource = null;
	private PricePromInfo pricePromInfo = null;
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
			
			SystemFileConfig sys = SystemFileConfig.initInstance(WindowsEnvironmentVariables.getInstance().getSystemPath());
			String companyId = sys.getCompanyId();
			String storeId = sys.getStoreId();

			PricePromInfoFactory.initialize(companyId,storeId, null, null, null, null);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@When("get the pricePromInfo with sku $sku dpt $dpt line $line")
	public final void getPricePromInfo(final String sku, final String dpt, final String line) throws Exception{
		pricePromInfo = itemResource.getPricePromInfo(sku, dpt, line);
	}


	@Then("I should get the PricePromInfo : $expectedItems")
	public final void pricePromInfoShouldBe(final ExamplesTable expectedItems) {
		for (Map<String, String> expectedItem : expectedItems.getRows()) {
			assertThat("Compare the PromotionNo at row ",
					"" + pricePromInfo.getPromotionNo(),
					is(equalTo(expectedItem.get("PromotionNo"))));
			assertThat("Compare the DiscountClass row ",
					"" + pricePromInfo.getDiscountClass(),
					is(equalTo(expectedItem.get("DiscountClass"))));
			assertThat("Compare the DiscountRate row ",
					"" + pricePromInfo.getDiscountRate(),
					is(equalTo(expectedItem.get("DiscountRate"))));
			assertThat("Compare the DiscountAmt row ",
					"" + pricePromInfo.getDiscountAmt(),
					is(equalTo(expectedItem.get("DiscountAmt"))));
		}
	}

	@Then("the Result Code is $resultcode")
	public final void theResultCodeIs(int expectedResultCode) {
		Assert.assertEquals("Expect the result code", expectedResultCode,
				status);
	}

	@Then("the emptyResult is $expectedResult")
	public final void pricePromInfoIsNull(String expectedResult) {
		assertThat("Expect the result ",
				"" + pricePromInfo,
				is(equalTo(expectedResult)));
	}
}