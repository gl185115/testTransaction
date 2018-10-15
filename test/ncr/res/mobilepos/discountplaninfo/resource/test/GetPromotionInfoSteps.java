package ncr.res.mobilepos.discountplaninfo.resource.test;

import junit.framework.Assert;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import ncr.res.mobilepos.discountplaninfo.resource.PromotionInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.webserviceif.model.JSONData;

public class GetPromotionInfoSteps extends Steps {

	private DBInitiator dbInitRESMaster = null;
	private PromotionInfoResource promotionInfoResource = null;
	private JSONData jsonData = null;
	
	@BeforeScenario
	public final void SetUpClass() {
		Requirements.SetUp();
		Requirements.getMockServletContext();
		dbInitRESMaster = new DBInitiator("GetPromotionInfoSteps", DATABASE.RESMaster);
		promotionInfoResource = new PromotionInfoResource();
	}

	@AfterScenario
	public final void TearDownClass() {
		Requirements.TearDown();
	}
	
	@Given("an empty table in MST_PRICE_PROMOTION")
	public final void initPricePromotionTable() {
		try {
			dbInitRESMaster
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/discountplaninfo/resource/test/mst_price_promotion_(nodata).xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Given("data in table MST_PRICE_PROMOTION")
	public final void initPricePromotionTableWithData() {
		try {
			dbInitRESMaster
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/discountplaninfo/resource/test/mst_price_promotion_(withdata).xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@When("I get promotion information of companyid:$1 storeid:$2 discreason:$3 discbarcodetype:$4 partialflag:$5 pricediscflag:$6 ratedisc:$7")
	public final void getPromotionInfo(final String companyId,
			final String storeId, final String discountReason,
			final String discountBarcodeType, final String partialFlag,
			final String priceDiscountFlag, final String rateDiscountFlag) {
		jsonData = promotionInfoResource.getPromotionInfo(companyId, storeId,
				discountReason, discountBarcodeType, partialFlag,
				priceDiscountFlag, rateDiscountFlag);
	}
	
	@Then("I should get NCRWSSResultCode:$expectedCode")
	public final void thenIShouldHaveCode(String expectedCode) {
		Assert.assertEquals("Compare NCRWSSResultCode", expectedCode, String.valueOf(jsonData.getNCRWSSResultCode()));
	}
	
	@Then("the JSON should have the following format: $expectedJson")
	public final void theJsonShouldHaveTheFollowingJSONFormat(
			String expectedJson) {
		try {
			JsonMarshaller<JSONData> jsonMarshaller = new JsonMarshaller<JSONData>();
			String actualJson = jsonMarshaller.marshall(jsonData);
			// compare to json strings regardless of property ordering
			JSONAssert.assertEquals(expectedJson, actualJson,
					JSONCompareMode.NON_EXTENSIBLE);
		} catch (Exception e) {
			Assert.fail("Failed to verify the JSONData");
			e.printStackTrace();
		}
	}

}
