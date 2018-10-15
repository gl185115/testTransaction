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
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.pricing.factory.PricePromInfoFactory;
import ncr.res.mobilepos.pricing.model.PricePromInfo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PromDetailSteps extends Steps {
	private PricePromInfoFactory pricePromInfoFactory = null;
	private List<PricePromInfo> pricePromList = null;
	private DBInitiator dbInit = null;
	private static int status = 0;
	public static final int ERROR_DAOEXCEPTION = 2;

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
			pricePromInfoFactory = new PricePromInfoFactory();
			Field contextField = pricePromInfoFactory.getClass().getDeclaredField("context");
			contextField.setAccessible(true);
			contextField.set(pricePromInfoFactory, context);
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
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("static-access")
	@When("the tomcat startUp with companyId $companyId and storeId $storeId")
	public final void getPricePromInfo(String companyId, String storeId) throws Exception {
		try{
			status = 0;
			pricePromList = pricePromInfoFactory.initialize(
					StringUtility.convNullOrEmptryString(companyId),
					StringUtility.convNullOrEmptryString(storeId));
		} catch (DaoException e) {
			status = ERROR_DAOEXCEPTION;
		}
	}


	@Then("I should get the PricePromInfo : $expectedJson")
	public final void pricePromInfoShouldBe(final ExamplesTable expectedItems) {
		int i = 0;
		for (Map<String, String> expectedItem : expectedItems.getRows()) {
			assertThat("Compare the PromotionNo at row ",
					"" + pricePromList.get(i).getPromotionNo(),
					is(equalTo(expectedItem.get("PromotionNo"))));
			assertThat("Compare the PromotionName row ",
					"" + pricePromList.get(i).getPromotionName(),
					is(equalTo(expectedItem.get("PromotionName"))));
			assertThat("Compare the PromotionType at row ",
					"" + pricePromList.get(i).getPromotionType(),
					is(equalTo(expectedItem.get("PromotionType"))));
			assertThat("Compare the BrandFlag at row ",
					"" + pricePromList.get(i).getBrandFlag(),
					is(equalTo(expectedItem.get("BrandFlag"))));
			assertThat("Compare the Dpt at row",
					"" + String.valueOf(pricePromList.get(i).getDpt()),
					is(equalTo(expectedItem.get("Dpt"))));
			assertThat("Compare the Line at row", ""
					+ pricePromList.get(i).getLine(),
					is(equalTo(expectedItem.get("Line"))));
			assertThat("Compare the Class at row ", ""
					+ pricePromList.get(i).getClas(),
					is(equalTo(expectedItem.get("Class"))));
			assertThat("Compare the Sku row ",
					"" + pricePromList.get(i).getSku(),
					is(equalTo(expectedItem.get("Sku"))));
			assertThat("Compare the DiscountClass row ",
					"" + pricePromList.get(i).getDiscountClass(),
					is(equalTo(expectedItem.get("DiscountClass"))));
			assertThat("Compare the DiscountRate row ",
					"" + pricePromList.get(i).getDiscountRate(),
					is(equalTo(expectedItem.get("DiscountRate"))));
			assertThat("Compare the DiscountAmt row ",
					"" + pricePromList.get(i).getDiscountAmt(),
					is(equalTo(expectedItem.get("DiscountAmt"))));
			assertThat("Compare the BrandId row ",
					"" + pricePromList.get(i).getBrandId(),
					is(equalTo(expectedItem.get("BrandId"))));
			i++;
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
				"" + pricePromList,
				is(equalTo(expectedResult)));
	}
}