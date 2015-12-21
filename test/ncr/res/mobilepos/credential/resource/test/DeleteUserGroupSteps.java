package ncr.res.mobilepos.credential.resource.test;

import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

@SuppressWarnings("deprecation")
public class DeleteUserGroupSteps extends Steps {
    private DBInitiator dbInit;
    private CredentialResource credentialResourceTest;
    private ResultBase actualResultBase;
    /**
     * Xml filename that contains database data.
     */
    private String datasetpath = "test/ncr/res/mobilepos/credential/resource/test/";
    /**
     * Invokes before execution of each scenario.
     */
    @BeforeScenario
    public final void setUpClass() {
        Requirements.SetUp();
        dbInit = new DBInitiator("DeleteUserGroupSteps", DATABASE.RESMaster);
        this.credentialResourceTest = new CredentialResource();
    }

    /**
     * Invokes after execution of each scenario.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }
    
    @Given("a PRM_GROUP_FUNCTION for delete database table")
    public final void aPrmGroupFunctionForDeleteDatabaseTable() {
        try {
            dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, datasetpath
                    + "PRM_GROUP_FUNCTION_for_delete.xml");
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Setting the Database"
                    + " table for PRM_GROUP_FUNCTION failed");
        }
    }
    
    @When("User Group with groupcode{$groupcode} will be deleted")
    public final void userGroupWithGroupcodeWillBeDeleted(final int groupcode) {
        actualResultBase = credentialResourceTest.deleteGroup(groupcode);
    }
    
    @Then("the PRM_GROUP_FUNCTION database table should have the following row(s): $expectedtable")
    public final void thePrmGroupFunctionDatabaseTableShouldHaveTheFollowingRow(ExamplesTable expectedTable) {
        try {
            ITable actualTable = 
                dbInit.getTableSnapshot("PRM_GROUP_FUNCTION");
            Assert.assertEquals("Compare the number of rows in PRM_GROUP_FUNCTION",
                    expectedTable.getRowCount(),
                    actualTable.getRowCount());
            //|group|groupname|transaction|reports|settings|merchandise|administration|drawer|
            int i = 0;
            for(Map<String, String> expectedDeviceInfo : expectedTable.getRows()) {
                Assert.assertEquals("Compare the GROUP in PRM_GROUP_FUNCTION row" + i,
                        Integer.parseInt(expectedDeviceInfo.get("groupcode")),
                        ((Integer)actualTable.getValue(i, "groupcode")).intValue());
                Assert.assertEquals("Compare the GROUPNAME in PRM_GROUP_FUNCTION row" + i,
                        expectedDeviceInfo.get("groupname"),
                        (String)actualTable.getValue(i, "groupname"));
                Assert.assertEquals("Compare the TRANSACTION in PRM_GROUP_FUNCTION row" + i,
                        Boolean.parseBoolean(expectedDeviceInfo.get("transactions")),
                        ((Boolean)actualTable.getValue(i, "transactions")).booleanValue());
                Assert.assertEquals("Compare the REPORTS in PRM_GROUP_FUNCTION row" + i,
                        Boolean.parseBoolean(expectedDeviceInfo.get("reports")),
                        ((Boolean)actualTable.getValue(i, "reports")).booleanValue());
                Assert.assertEquals("Compare the SETTINGS in PRM_GROUP_FUNCTION row" + i,
                        Boolean.parseBoolean(expectedDeviceInfo.get("settings")),
                        ((Boolean)actualTable.getValue(i, "settings")).booleanValue());
                Assert.assertEquals("Compare the MERCHANDISE in PRM_GROUP_FUNCTION row" + i,
                        Boolean.parseBoolean(expectedDeviceInfo.get("merchandise")),
                        ((Boolean)actualTable.getValue(i, "merchandise")).booleanValue());
                Assert.assertEquals("Compare the ADMINISTRATION in PRM_GROUP_FUNCTION row" + i,
                        Boolean.parseBoolean(expectedDeviceInfo.get("administration")),
                        ((Boolean)actualTable.getValue(i, "administration")).booleanValue());
                Assert.assertEquals("Compare the DRAWER in PRM_GROUP_FUNCTION row" + i,
                        Boolean.parseBoolean(expectedDeviceInfo.get("drawer")),
                        ((Boolean)actualTable.getValue(i, "drawer")).booleanValue());
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in PRM_GROUP_FUNCTION.");
        }
    }
    
    @Then("the resulbase code is {$expectedResulCode}")
    public final void theResulBaseCodeIs(final int expectedResulCode) {
        Assert.assertEquals("Compare the ResultBase Code", expectedResulCode, actualResultBase.getNCRWSSResultCode());
    }
}
