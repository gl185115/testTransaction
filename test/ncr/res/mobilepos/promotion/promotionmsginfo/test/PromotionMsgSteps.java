package ncr.res.mobilepos.promotion.promotionmsginfo.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;

import junit.framework.Assert;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.promotion.factory.PromotionMsgInfoFactory;
import ncr.res.mobilepos.promotion.model.PromotionMsgInfo;

public class PromotionMsgSteps extends Steps {
	private PromotionMsgInfoFactory PromotionMsgInfoFactory = null;
	private List<PromotionMsgInfo> promotionMsgList = null;
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
			PromotionMsgInfoFactory = new PromotionMsgInfoFactory();
			Field contextField = PromotionMsgInfoFactory.getClass().getDeclaredField("context");
			contextField.setAccessible(true);
			contextField.set(PromotionMsgInfoFactory, context);
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

	@When("the tomcat startUp with companyId $companyId and storeId $storeId")
	public final void getPromotionMsgInfo(String companyId, String storeId) throws Exception {
		try{
			status = 0;
			promotionMsgList = PromotionMsgInfoFactory.initialize(
					StringUtility.convNullOrEmptryString(companyId),
					StringUtility.convNullOrEmptryString(storeId));
		} catch (DaoException e) {
			status = ERROR_DAOEXCEPTION;
		}
	}


	@Then("I should get the PromotionMsgInfo : $expectedJson")
	public final void PromotionMsgInfoShouldBe(final ExamplesTable expectedItems) {
		int i = 0;

		for (Map<String, String> expectedItem : expectedItems.getRows()) {
			assertThat("Compare the RecordId at row ",
					"" + promotionMsgList.get(i).getRecordId(),
					is(equalTo(expectedItem.get("RecordId"))));
			assertThat("Compare the Subject at row ",
					"" + promotionMsgList.get(i).getSubject(),
					is(equalTo(expectedItem.get("Subject"))));
			assertThat("Compare the MinimunPrice at row ",
					"" + promotionMsgList.get(i).getMinimunPrice(),
					is(equalTo(expectedItem.get("MinimunPrice"))));
			assertThat("Compare the MessageBody at row ",
					"" + promotionMsgList.get(i).getMessageBody(),
					is(equalTo(expectedItem.get("MessageBody"))));
			assertThat("Compare the MdInternal at row ",
					"" + promotionMsgList.get(i).getItemCode(),
					is(equalTo(expectedItem.get("MdInternal"))));
			i++;
		}
	}

	@Then("the Result Code is $resultcode")
	public final void theResultCodeIs(int expectedResultCode) {
		Assert.assertEquals("Expect the result code", expectedResultCode,
				status);
	}

	@Then("the emptyResult is $expectedResult")
	public final void PromotionMsgInfoIsNull(String expectedResult) {
		assertThat("Expect the result ",
				"" + promotionMsgList,
				is(equalTo(expectedResult)));
	}
}