package ncr.res.mobilepos.credential.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import junit.framework.Assert;
import ncr.res.mobilepos.credential.model.UserGroup;
import ncr.res.mobilepos.credential.model.ViewUserGroup;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

@SuppressWarnings("deprecation")
public class UpdateGroupTestSteps extends Steps{
    CredentialResource credentialRes = null;
    ResultBase resultBase = null;
    ViewUserGroup viewUserGroup = null;
    UserGroup userGroup = null;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("an initial group entry in database")
    public final void addGroupInDB() throws Exception{
        new DBInitiator("CredentialResourceSteps",
                "test/ncr/res/mobilepos/credential/resource/test/"
                        + "PRM_GROUP_FUNCTION_updateGroup.xml", DATABASE.RESMaster);
    }
    
    /**
     * Creates CredentialResource instance.
     */
    @Given("a CredentialResource")
    public final void givenCredentialResource() {
        credentialRes = new CredentialResource();
        Assert.assertNotNull(credentialRes);
    }
    
    @When("I update Group with Group Code {$groupCode} to $jsonGroup")
    public final void updateGroup(final int groupCode, final String jsonGroup){
        viewUserGroup = credentialRes.updateGroup(groupCode, jsonGroup);
        userGroup = viewUserGroup.getUserGroup();
        System.out.println("USER GROUP: " + userGroup);
    }
    
    /**
     * Tests actual and expected group data.
     *
     * @param newGroupDetails
     *            The expected group data.
     */
    @Then("I should get updated Group details: $newGroupDetails")
    public final void testResult(final ExamplesTable newGroupDetails) {
        Assert.assertNotNull(userGroup);
        Assert.assertEquals(0, viewUserGroup.getNCRWSSResultCode());
        
        Assert.assertEquals(newGroupDetails.getRow(0).get("groupcode"),
                String.valueOf(userGroup.getGroupCode()));
        Assert.assertEquals(newGroupDetails.getRow(0).get("groupname").trim(),
                String.valueOf(userGroup.getGroupName()));
        Assert.assertEquals(Boolean.parseBoolean(newGroupDetails.getRow(0).get("transactions")),
                userGroup.isTransaction());
        Assert.assertEquals(Boolean.parseBoolean(newGroupDetails.getRow(0).get("reports")),
                userGroup.isReports());
        Assert.assertEquals(Boolean.parseBoolean(newGroupDetails.getRow(0).get("settings")),
                userGroup.isSettings());
        Assert.assertEquals(Boolean.parseBoolean(newGroupDetails.getRow(0).get("merchandise")),
                userGroup.isMerchandise());
        Assert.assertEquals(Boolean.parseBoolean(newGroupDetails.getRow(0).get("administration")),
                userGroup.isAdministration());
        Assert.assertEquals(Boolean.parseBoolean(newGroupDetails.getRow(0).get("drawer")),
                userGroup.isDrawer());
    }
    
    @Then("Result code should be: {$resultCode}")
    public final void checkResultCode(final int resultCode) {
        System.out.println("RESULT CODE: " + viewUserGroup.getNCRWSSResultCode());
        assertThat(viewUserGroup.getNCRWSSResultCode(), is(equalTo(resultCode)));
    }
}
