package ncr.res.mobilepos.credential.resource.test;

import java.io.IOException;
import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.credential.model.UserGroupList;
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

/**
 * Steps class for CredentialResource.
 *
 */
@SuppressWarnings("deprecation")
public class ListGroupResourceSteps extends Steps {
    /**
     * CredentialResource instance.
     */
    private CredentialResource testCredentialResource;
    /**
     * ResultBase instance.
     */
    private UserGroupList actualResultBase;

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        GlobalConstant.setCorpid("000000000000"); // Set corpid
        testCredentialResource = new CredentialResource();
        GlobalConstant.setMaxSearchResults(3);
    }

    @AfterScenario
    public final void TearDown() {
        Requirements.TearDown();
    }
    
    @Given("a PRM_GROUP_FUNCTION database table")
    public final void anEmptyPRM_AUTHORIZATION_LINKDatabaseTable(){
        new DBInitiator("prm_queuebuster", 
                "test/ncr/res/mobilepos/credential/resource/test/"
                + "PRM_GROUP_FUNCTION_viewGroupDetail.xml", DATABASE.RESMaster);
    }

    
    @When("I list groups with key: ($key)")
    public final void listGroup(String key) {
        if (key.equals("null")) {
            key = null;
        }
        actualResultBase = testCredentialResource.listGroups(key);
    }
    
    @Then("the result should be $result")
    public void checkResults(int result) {
        Assert.assertEquals(result, actualResultBase.getNCRWSSResultCode());
    }
    
    @Then("the PRM_GROUP_FUNCTION database table should have the following row(s): $tables")
    public final void thePRM_GROUP_FUNCTIONDatabaseTableShouldHaveTheFollowingRow(ExamplesTable expectedDeviceInfoTable) {
        try {
            Assert.assertEquals("Compare the number of rows in PRM_GROUP_FUNCTION",
                    expectedDeviceInfoTable.getRowCount(),
                    actualResultBase.getGroupList().size());
            
            int i = 0;
            for(Map<String, String> expectedDeviceInfo : expectedDeviceInfoTable.getRows()) {
                Assert.assertEquals("Compare the CORPID in PRM_GROUP_FUNCTION row" + i,
                        Integer.parseInt(expectedDeviceInfo.get("groupcode")),
                        actualResultBase.getGroupList().get(i).getGroupCode());
                Assert.assertEquals("Compare the STOREID in PRM_GROUP_FUNCTION row" + i,
                        expectedDeviceInfo.get("groupname"),
                        actualResultBase.getGroupList().get(i).getGroupName());
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in PRM_AUTHORIZATION_LINK.");
        }
    }
    
    @When("I set the limit to $limit")
    public void setLimit(int limit) {
        GlobalConstant.setMaxSearchResults(limit);
    }

    @Then ("the JSON should have the following format : $expectedJson")
    public final void theJsonShouldHaveTheFollowingJSONFormat(
            String expectedJson) {
        try {
            JsonMarshaller<UserGroupList> deviceInfoJsonMarshaller =
                new JsonMarshaller<UserGroupList>();
            
            String actualJson =
                    deviceInfoJsonMarshaller.marshall(actualResultBase);
            
            int code = actualResultBase.getNCRWSSResultCode();
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
