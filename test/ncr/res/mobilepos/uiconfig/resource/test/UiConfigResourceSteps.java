package ncr.res.mobilepos.uiconfig.resource.test;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.naming.NamingException;
import javax.ws.rs.core.Response;

import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.uiconfig.resource.UiConfigResource;

import static org.junit.Assert.assertEquals;

public class UiConfigResourceSteps extends Steps {
        private UiConfigResource uiConfigResource = null;
        private Response response = null;

        @BeforeScenario
        public void setUp() throws NamingException {
			Requirements.SetUp();
			Requirements.SetUpLocalUiConfig();
			Requirements.getMockServletContext();
			uiConfigResource = new UiConfigResource();
        }

        @AfterScenario
        public void tearDown() throws NamingException {
        }

        @Given("a table $dataset")
		public final void givenDataset(String file) {
			new DBInitiator("UiConfigResourceSteps",
				"test/ncr/res/mobilepos/uiconfig/resource/test/" + file,
				DBInitiator.DATABASE.RESMaster);
        }

        @When("I make a request with $typeParam, $companyID, $storeID, $workstationID")
		public void makeRequest(String typeParam, String companyID, String storeID, String workstationID) {
			response = uiConfigResource.requestConfigFile(typeParam, companyID, storeID, workstationID);
        }

        @Then("Response status was $statusCode")
		public void expectSuccessful(int statusCode) {
			assertEquals(statusCode, response.getStatus());
        }

}