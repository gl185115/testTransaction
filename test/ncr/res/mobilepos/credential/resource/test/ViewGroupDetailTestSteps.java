package ncr.res.mobilepos.credential.resource.test;

import java.io.IOException;

import junit.framework.Assert;
import ncr.res.mobilepos.credential.model.UserGroup;
import ncr.res.mobilepos.credential.model.ViewUserGroup;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

@SuppressWarnings("deprecation")
public class ViewGroupDetailTestSteps extends Steps{
    /**
     * Credential Resource instance. Web Service to call viewUserGroup.
     */
    private CredentialResource credentialRsrc = null;
    /**
     * ViewUserGroup instance. Contains User Group Details.
     */
    private ViewUserGroup viewUserGrp = null;
    /**
     * Method executed before each scenario.
     */
    @BeforeScenario
    public void setUpClass() {
        Requirements.SetUp();
        new DBInitiator("CredentialResourceSteps",
                "test/ncr/res/mobilepos/credential/resource/test/"
                        + "PRM_GROUP_FUNCTION_viewGroupDetail.xml", DATABASE.RESMaster);
    }
    
    /**
     * A method to be executed after each scenario.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }
    
    /**
     * Creates CredentialResource instance.
     */
    @Given("a CredentialResource")
    public final void givenCredentialResource() {
        credentialRsrc = new CredentialResource();
    }
    
    /**
     * Invokes view group detail in CredentialResource.
     *
     * @param groupCode
     *            The Group code (Identifier).
     */
    @When("I view group details of {$groupCode}")
    public final void viewGroupDetail(final int groupCode) {
        viewUserGrp = credentialRsrc.viewGroupDetail(groupCode);
    }
    
    /**
     * Tests expected and actual result code.
     * @param result
     *            The expected result.
     */
    @Then("I should get resultcode equal to {$result}")
    public final void testResultCode(final int result) {
        Assert.assertEquals(result, viewUserGrp.getNCRWSSResultCode());
    }
    
    /**
     * Tests actual and expected group details.
     * @param expectedGroup
     *            The ExamplesTable instance.
     */
    @Then("I should get group details: $groupData")
    public final void testEmployeeData(final ExamplesTable expectedGroup) {
        UserGroup actualGroup = viewUserGrp.getUserGroup();

        Assert.assertNotNull(viewUserGrp.getUserGroup());
        Assert.assertEquals("Compare the Group Code ", expectedGroup
                .getRow(0).get("groupcode"), String.valueOf(actualGroup
                        .getGroupCode()));
        Assert.assertEquals("Compare the Group Name ", expectedGroup
                .getRow(0).get("groupname"), String.valueOf(actualGroup
                .getGroupName()));
        Assert.assertEquals("Compare the Group's Transaction detail", Boolean.parseBoolean(expectedGroup
                .getRow(0).get("transactions")), actualGroup
                .isTransaction());        
        Assert.assertEquals("Compare the Group's Reports detail", Boolean.parseBoolean(expectedGroup
                .getRow(0).get("reports")), actualGroup
                .isReports());
        Assert.assertEquals("Compare the Group's Setting detail", Boolean.parseBoolean(expectedGroup
                .getRow(0).get("settings")), actualGroup
                .isSettings());
        Assert.assertEquals("Compare the Group's Merchandise detail", Boolean.parseBoolean(expectedGroup
                .getRow(0).get("merchandise")), actualGroup
                .isMerchandise());
        Assert.assertEquals("Compare the Group's Administration detail", Boolean.parseBoolean(expectedGroup
                .getRow(0).get("administration")), actualGroup
                .isAdministration());
        Assert.assertEquals("Compare the Group's Drawer detail", Boolean.parseBoolean(expectedGroup
                .getRow(0).get("drawer")), actualGroup
                .isDrawer());
    }

    @Then ("the JSON should have the following format : $expectedJson")
    public final void theJsonShouldHaveTheFollowingJSONFormat(
            String expectedJson) {
        try {
            JsonMarshaller<ViewUserGroup> deviceInfoJsonMarshaller =
                new JsonMarshaller<ViewUserGroup>();
            
            String actualJson =
                deviceInfoJsonMarshaller.marshall(viewUserGrp);
            
            int code = viewUserGrp.getNCRWSSResultCode();
            String resCode = "NCRWSSResultCode\":"+code; 
            if(actualJson.contains(resCode)){
            	actualJson = actualJson.replaceAll(resCode, "");
            	actualJson = actualJson.replaceAll("NCRWSSExtendedResultCode\":0", "");
            }
            if(expectedJson.contains(resCode)){
            	expectedJson = expectedJson.replaceAll(resCode, "");
            	expectedJson = expectedJson.replaceAll("NCRWSSExtendedResultCode\":0", "");
            }
            
            System.out.println(actualJson);
            Assert.assertEquals("Verify the DeviceInfo JSON Format",
                    expectedJson, actualJson);
        } catch (IOException e) {
            Assert.fail("Failed to verify the DeviceInfo JSON format");
            e.printStackTrace();
        }
    }
}