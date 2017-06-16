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
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.pricing.factory.PriceMMInfoFactory;
import ncr.res.mobilepos.pricing.model.PriceMMInfo;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class PriceMMSteps extends Steps {
	private PriceMMInfoFactory priceMMInfoFactory = null;
	private List<PriceMMInfo> priceMMList = null;
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
			priceMMInfoFactory = new PriceMMInfoFactory();
			Field contextField = priceMMInfoFactory.getClass().getDeclaredField("context");
			contextField.setAccessible(true);
			contextField.set(priceMMInfoFactory, context);
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/pricing/pricemminfo/test/mst_bizday.xml");
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
	public final void getPriceMMInfo(String companyId, String storeId) throws Exception {
		try{
			status = 0;
			priceMMList = priceMMInfoFactory.initialize(
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
			assertThat("Compare the MMNo at row ",
					"" + priceMMList.get(i).getMMNo(),
					is(equalTo(expectedItem.get("MMNo"))));
			assertThat("Compare the ConditionCount1 row ",
					"" + priceMMList.get(i).getConditionCount1(),
					is(equalTo(expectedItem.get("ConditionCount1"))));
			assertThat("Compare the ConditionCount2 at row ",
					"" + priceMMList.get(i).getConditionCount2(),
					is(equalTo(expectedItem.get("ConditionCount2"))));
			assertThat("Compare the ConditionCount3 at row ",
					"" + priceMMList.get(i).getConditionCount3(),
					is(equalTo(expectedItem.get("ConditionCount3"))));
			assertThat("Compare the ConditionPrice1 at row",
					"" + String.valueOf(priceMMList.get(i).getConditionPrice1()),
					is(equalTo(expectedItem.get("ConditionPrice1"))));
			assertThat("Compare the ConditionPrice2 at row", ""
					+ priceMMList.get(i).getConditionPrice2(),
					is(equalTo(expectedItem.get("ConditionPrice2"))));
			assertThat("Compare the ConditionPrice3 at row ", ""
					+ priceMMList.get(i).getConditionPrice3(),
					is(equalTo(expectedItem.get("ConditionPrice3"))));
			assertThat("Compare the DecisionPrice1 row ",
					"" + priceMMList.get(i).getDecisionPrice1(),
					is(equalTo(expectedItem.get("DecisionPrice1"))));
			assertThat("Compare the DecisionPrice2 row ",
					"" + priceMMList.get(i).getDecisionPrice2(),
					is(equalTo(expectedItem.get("DecisionPrice2"))));
			assertThat("Compare the DecisionPrice3 row ",
					"" + priceMMList.get(i).getDecisionPrice3(),
					is(equalTo(expectedItem.get("DecisionPrice3"))));
			assertThat("Compare the AveragePrice1 row ",
					"" + priceMMList.get(i).getAveragePrice1(),
					is(equalTo(expectedItem.get("AveragePrice1"))));
			assertThat("Compare the AveragePrice2 row ",
					"" + priceMMList.get(i).getAveragePrice2(),
					is(equalTo(expectedItem.get("AveragePrice2"))));
			assertThat("Compare the AveragePrice3 row ",
					"" + priceMMList.get(i).getAveragePrice3(),
					is(equalTo(expectedItem.get("AveragePrice3"))));
			assertThat("Compare the Note row ",
					"" + priceMMList.get(i).getNote(),
					is(equalTo(expectedItem.get("Note"))));
			assertThat("Compare the Sku row ",
					"" + priceMMList.get(i).getSku(),
					is(equalTo(expectedItem.get("Sku"))));
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
				"" + priceMMList,
				is(equalTo(expectedResult)));
	}
}