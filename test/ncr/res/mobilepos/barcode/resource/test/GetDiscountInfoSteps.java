package ncr.res.mobilepos.barcode.resource.test;

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

import ncr.res.mobilepos.barcode.resource.BarCodeResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.webserviceif.model.JSONData;

public class GetDiscountInfoSteps extends Steps {

	private DBInitiator dbInitRESMaster = null;
	private BarCodeResource barCodeResource = null;
	private JSONData jsonData = null;
	
	@BeforeScenario
	public final void SetUpClass() {
		Requirements.SetUp();
		Requirements.getMockServletContext();
		dbInitRESMaster = new DBInitiator("GetDiscountInfoSteps", DATABASE.RESMaster);
		barCodeResource = new BarCodeResource();
	}

	@AfterScenario
	public final void TearDownClass() {
		Requirements.TearDown();
	}
	
	@Given("an empty table in MST_PRICE_DISCOUNT")
	public final void initPriceDiscountTable() {
		try {
			dbInitRESMaster
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/barcode/resource/test/mst_price_discount_(nodata).xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Given("data in table MST_PRICE_DISCOUNT")
	public final void initPriceDiscountTableWithData() {
		try {
			dbInitRESMaster
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/barcode/resource/test/mst_price_discount_(withdata).xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@When("I get discount information of companyid:$1 storeid:$2 cardtype:$3 seqno:$4 discounttype:$5")
	public final void getDiscountInfo(final String companyId, final String storeId, final String cardType, final String seqNo, final String discountType) {
		jsonData = barCodeResource.getDiscountInfo(companyId, storeId, cardType, seqNo, discountType);
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
