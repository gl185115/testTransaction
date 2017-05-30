package ncr.res.mobilepos.customerclass.resource.test;

import java.io.IOException;
import java.lang.reflect.Field;

import javax.servlet.ServletContext;

import junit.framework.Assert;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.customerclass.model.CustomerClassInfoList;
import ncr.res.mobilepos.customerclass.resource.CustomerClassInfoResource;
import ncr.res.mobilepos.webserviceif.model.JSONData;
import ncr.res.pointserver.helper.JsonMarshaller;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import org.json.JSONException;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class GetCustomerClassInfoResourceSteps extends Steps {

	private CustomerClassInfoResource customerClassInfoResource;
	private CustomerClassInfoList customerClassInfoList;

	@BeforeScenario
	public final void setUp() throws Exception {
		Requirements.SetUp();

	}

	@AfterScenario
	public final void tearDown() {
		Requirements.TearDown();
	}

	@Given("a table $dataset")
	public final void givenDataset(String file) {
		new DBInitiator("CustomerClassInfoResourceSteps",
				"test/ncr/res/mobilepos/customerclass/resource/test/" + file,
				DATABASE.RESMaster);
	}

	@Given("that customerclassinfo service")
	public final void givenCustomerClassInfoService() {
		ServletContext context = Requirements.getMockServletContext();
		customerClassInfoResource = new CustomerClassInfoResource();
		try {
			Field customerClassInfoContext = customerClassInfoResource.getClass()
					.getDeclaredField("servletContext");
			customerClassInfoContext.setAccessible(true);
			customerClassInfoContext.set(customerClassInfoResource, context);
		} catch (Exception e) {
			Assert.fail("Cant load Mock Servlet Context.");
		}
	}

	@When("I get customerclassinfo of CompanyId:$1 StoreId:$2")
	public final void whenGetCustomerClassInfo(final String companyId, final String storeId) {
		customerClassInfoList = customerClassInfoResource.getCustomerClassInfo(companyId, storeId);
	}

	@Then("I should get the following json : $1")
	public final void thenIShouldGetJson(final String expectedJson)
			throws JSONException, IOException {
		JsonMarshaller<CustomerClassInfoList> jsonMarshaller = new JsonMarshaller<CustomerClassInfoList>();
		String actualJson = jsonMarshaller.marshall(customerClassInfoList);
		System.out.println(actualJson);
		// compare to json strings regardless of property ordering
		JSONAssert.assertEquals(expectedJson, actualJson,
				JSONCompareMode.NON_EXTENSIBLE);
	}
}
