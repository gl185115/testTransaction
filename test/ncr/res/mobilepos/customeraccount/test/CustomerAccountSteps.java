package ncr.res.mobilepos.customeraccount.test;

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
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import junit.framework.Assert;
import ncr.res.mobilepos.customeraccount.model.LoyaltyAccountInfo;
import ncr.res.mobilepos.customeraccount.resource.CustomerAccountResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

public class CustomerAccountSteps extends Steps {
	private ServletContext servletContext = null;
	private DBInitiator dbInit = null;
	private CustomerAccountResource customerAccountResource = null;
	private LoyaltyAccountInfo loyaltyAccountInfo = null;
	
	@BeforeScenario
	public final void SetUpClass() {
		dbInit = new DBInitiator("CustomeraccountSteps", DATABASE.RESMaster);
		Requirements.SetUp();
		initResources();
	}
	
	@AfterScenario
	public final void TearDownClass() {
		Requirements.TearDown();
	}
	
	private void initResources() {
		servletContext = Requirements.getMockServletContext();
		customerAccountResource = new CustomerAccountResource();
		Field customerAccountText;
		try {
			customerAccountText = customerAccountResource.getClass()
					.getDeclaredField("servletContext");
			customerAccountText.setAccessible(true);
			customerAccountText.set(customerAccountResource, servletContext);
		} catch (Exception ex) {
			Assert.fail("Can't Retrieve Servlet context from customer.");
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
	
	@When("I get loyaltyaccountinfo attribute companyId $companyId storeId $storeId connName $connName connKanaName $connKanaName connTel $connTel")
	public final void whenIRegister(String companyId, String storeId, String connName,String connKanaName,String connTel){
		loyaltyAccountInfo = customerAccountResource.getLoyaltyAccountInfo(companyId, storeId, connName, connKanaName, connTel);
	}
	
	@Then("I should get the following: $data")
	public final void thenIShouldGet(ExamplesTable expectedData) {
		int i = 0;
		for (Map<String, String> data : expectedData.getRows()) {
			Assert.assertEquals(data.get("ConnCode").trim(), loyaltyAccountInfo.getLoyaltyAccountList().get(i).getConnCode());
			Assert.assertEquals(data.get("ConnName").trim(), loyaltyAccountInfo.getLoyaltyAccountList().get(i).getConnName());
			Assert.assertEquals(data.get("ConnKanaName").trim(), loyaltyAccountInfo.getLoyaltyAccountList().get(i).getConnKanaName());
			Assert.assertEquals(data.get("ConnTel").trim(), loyaltyAccountInfo.getLoyaltyAccountList().get(i).getConnTel());
			i++;
		}
	}
	
	@Then("I should get the Result size is $resultsize")
	public final void registerDeviceFailed(String resultsize){
		int result = Integer.parseInt(resultsize);
		Assert.assertEquals(result, loyaltyAccountInfo.getLoyaltyAccountList().size());
	}
	
	@Then("the Result Code is $resultcode")
	public final void theResultCodeIs(int expectedResultCode) {
		Assert.assertEquals("Expect the  result code", expectedResultCode,
				loyaltyAccountInfo.getNCRWSSResultCode());
	}
	
	@Then ("the JSON should have the following format : $expectedJson")
	public final void theJsonShouldHaveTheFollowingJSONFormat(
			String expectedJson) {
		try {
			JsonMarshaller<ResultBase> jsonMarshaller =
					new JsonMarshaller<ResultBase>();

			String actualJson = jsonMarshaller.marshall(loyaltyAccountInfo);

			System.out.println(actualJson);

			// compare to json strings regardless of property ordering
			JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.NON_EXTENSIBLE);
		} catch (Exception e) {
			Assert.fail("Failed to verify the PromotionResponse JSON format");
			e.printStackTrace();
		}
	}
}
