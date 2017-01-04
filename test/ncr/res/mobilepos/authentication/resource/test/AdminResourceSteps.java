package ncr.res.mobilepos.authentication.resource.test;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import ncr.res.mobilepos.authentication.resource.AdminResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

public class AdminResourceSteps extends Steps {
	private AdminResource adminresource;
	private ResultBase result;

	@BeforeScenario
	public final void setUp() {
		Requirements.SetUp();
	}

	@AfterScenario
	public final void TearDown() {
		Requirements.TearDown();
	}

	@Given("an Admin Resource")
	public final void createResource() throws Exception {
		adminresource = new AdminResource();
		adminresource.setContext(Requirements.getMockServletContext());
		result = new ResultBase();
		new DBInitiator("AdminResourceSteps",
				"test/ncr/res/mobilepos/authentication/resource/test/forAdminResourceTest.xml", DATABASE.RESMaster);
	}

	@Given("the adminkey is set to $adminkey")
	public final void setAdminKey(final String adminkey) throws Exception {
		new DBInitiator("AdminResourceSteps",
				"test/ncr/res/mobilepos/authentication/resource/datasets/PRM_SYSTEM_CONFIG_AdminKey_" 
				+ adminkey + ".xml", DATABASE.RESMaster);
	}

	@Given("the passcode is set to $passcode")
	public final void setPasscode(final String passcode) throws Exception {
		new DBInitiator("AdminResourceSteps",
				"test/ncr/res/mobilepos/authentication/resource/datasets/PRM_SYSTEM_CONFIG_Passcode_" + passcode + ".xml",
				DATABASE.RESMaster);
	}

	@When("I set the adminKey to $newKey using $currentKey")
	public final void setAdminKey(String newKey, String currentKey) {
		if (newKey.equals("null")) {
			newKey = null;
		}
		if (currentKey.equals("null")) {
			currentKey = null;
		}
		result = adminresource.setAdminKey(currentKey, newKey);
	}

	@When("I set the passcode to $newpasscode using $adminKey")
	public final void setPasscode(String newpasscode, String adminKey) {
		if (newpasscode.equals("null")) {
			newpasscode = null;
		}
		if (adminKey.equals("null")) {
			adminKey = null;
		}
		result = adminresource.setPasscode(newpasscode, "20", adminKey);
	}

	@Then("the result should be $expected")
	public final void checkResult(final int expected) {
		assertThat(result.getNCRWSSResultCode(), is(equalTo(expected)));
	}

}
