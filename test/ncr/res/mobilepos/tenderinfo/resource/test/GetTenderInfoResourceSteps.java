package ncr.res.mobilepos.tenderinfo.resource.test;

import java.io.IOException;
import java.lang.reflect.Field;
import javax.servlet.ServletContext;
import junit.framework.Assert;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.tenderinfo.resource.TenderInfoResource;
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

public class GetTenderInfoResourceSteps extends Steps {

	private TenderInfoResource tenderInfoResource;
	private JSONData jsonData;

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
		new DBInitiator("TenderInfoResourceSteps",
				"test/ncr/res/mobilepos/tenderinfo/resource/test/" + file,
				DATABASE.RESMaster);
	}

	@Given("that tenderinfo service")
	public final void givenTenderInfoService() {
		ServletContext context = Requirements.getMockServletContext();
		tenderInfoResource = new TenderInfoResource();
		try {
			Field tenderInfoContext = tenderInfoResource.getClass()
					.getDeclaredField("servletContext");
			tenderInfoContext.setAccessible(true);
			tenderInfoContext.set(tenderInfoResource, context);
		} catch (Exception e) {
			Assert.fail("Cant load Mock Servlet Context.");
		}
	}

	@When("I get tenderinfo of CompanyId:$1 StoreId:$2 TenderType:$3")
	public final void whenGetTenderInfo(final String companyId,
			final String storeId, final String tenderType) {
		jsonData = tenderInfoResource.getTenderInfo(companyId, storeId,
				tenderType);
	}
	
    @When("I get all tenderinfo of CompanyId:$1 StoreId:$2")
    public final void whenGetAllTenderInfo(final String companyId,
            final String storeId) {
        jsonData = tenderInfoResource.getAllTenderInfo(companyId, storeId);
    }
    
	@Then("I should get the following json : $1")
	public final void thenIShouldGetJson(final String expectedJson)
			throws JSONException, IOException {
		JsonMarshaller<JSONData> jsonMarshaller = new JsonMarshaller<JSONData>();
		String actualJson = jsonMarshaller.marshall(jsonData);
		// compare to json strings regardless of property ordering
		JSONAssert.assertEquals(expectedJson, actualJson,
				JSONCompareMode.NON_EXTENSIBLE);
	}
	

}
