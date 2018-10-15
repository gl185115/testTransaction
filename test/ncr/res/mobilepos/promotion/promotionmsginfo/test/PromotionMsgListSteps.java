package ncr.res.mobilepos.promotion.promotionmsginfo.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
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
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.promotion.factory.PromotionMsgInfoFactory;
import ncr.res.mobilepos.promotion.model.Promotion;
import ncr.res.mobilepos.promotion.resource.PromotionResource;

public class PromotionMsgListSteps extends Steps {
	private PromotionResource promotionResource = null;
	private Promotion response = null;
	private DBInitiator dbInit = null;

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
			promotionResource = new PromotionResource();
			Field contextField = promotionResource.getClass().getDeclaredField("context");
			contextField.setAccessible(true);
			contextField.set(promotionResource, context);
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

    /**
    * Given Step : I have PromotionMsg Resource.
    */
    @Given("I have PromotionMsg Resource companyid $companyid storeid $storeid")
    public final void iHavePromotionMsgResource(final String companyId, final String storeId) {
    	try {
    		PromotionMsgInfoFactory.initialize(companyId, storeId);
    		promotionResource = new PromotionResource();
		} catch (Exception e) {
			// TODO é©ìÆê∂ê¨Ç≥ÇÍÇΩ catch ÉuÉçÉbÉN
			e.printStackTrace();
			Assert.fail("Cant load Mock Servlet Context.");
		}
    }

	@When("check the list with companyId $companyId retailstoreid $retailstoreid workstationid $workstationid sequencenumber $sequencenumber businessDate $businessDate transaction $transaction")
	public final void getPromotionMsgInfo(String companyId, String retailStoreId, String workstationid, String sequencenumber, String businessDate, String transaction) throws Exception {
		response = promotionResource.getPromotionMessageList(companyId, retailStoreId, workstationid, sequencenumber, businessDate, transaction);
	}


	@Then("I should get the PromotionMsgInfoList : $expectedJson")
	public final void PromotionMsgInfoListShouldBe(final ExamplesTable expectedItems) {
		int i = 0;
		for (Map<String, String> expectedItem : expectedItems.getRows()) {
			assertThat("Compare the ItemCode at row", ""
					+ response.getPromotionMsgInfoMap().get(i).getItemCode(),
					is(equalTo(expectedItem.get("itemCode"))));
			assertThat("Compare the MessageBody at row", ""
					+ response.getPromotionMsgInfoMap().get(i).getMessageBody(),
					is(equalTo(expectedItem.get("MessageBody"))));
			assertThat("Compare the MdName at row", ""
					+ response.getPromotionMsgInfoMap().get(i).getMdName(),
					is(equalTo(expectedItem.get("MdName"))));
			i++;
		}
	}

	@Then("the Result Code is $resultcode")
	public final void theResultCodeIs(int expectedResultCode) {
		Assert.assertEquals("Expect the result code", expectedResultCode,
				response.getNCRWSSResultCode());
	}

	@Then("the emptyResult is $expectedResult")
	public final void PromotionMsgInfoIsNull(String expectedResult) {
		assertThat("Expect the result ",
				"" + response,
				is(equalTo(expectedResult)));
	}
}