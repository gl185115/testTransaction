package ncr.res.mobilepos.credential.resource.test;

import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.dbunit.dataset.ITable;
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
public class CreateGroupResourceSteps extends Steps {
    /**
     * CredentialResource instance.
     */
    private CredentialResource testCredentialResource;
    /**
     * DBInitiator instance.
     */
    private DBInitiator dbInit;
    /**
     * ResultBase instance.
     */
    private ResultBase actualResultBase;
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        GlobalConstant.setCorpid("000000000000"); // Set corpid
        testCredentialResource = new CredentialResource();
    }

    @AfterScenario
    public final void TearDown() {
        Requirements.TearDown();
    }
    
    @Given("an empty PRM_GROUP_FUNCTION database table")
    public final void anEmptyPRM_AUTHORIZATION_LINKDatabaseTable(){
        dbInit = new DBInitiator("prm_queuebuster", 
                "test/ncr/res/mobilepos/credential/resource/test/"
                + "PRM_GROUP_FUNCTION_EMPTY.xml", DATABASE.RESMaster);
    }

    
    @When("I create a group with groupcode: $groupcode, group: ($group)")
    public final void createGroup(int groupcode, String group) {
        if (group.equals("null")) {
            group = null;
        }
        actualResultBase = testCredentialResource.createGroup(groupcode, group);
    }
    
    @Then("the result should be $result")
    public void checkResults(int result) {
        Assert.assertEquals(result, actualResultBase.getNCRWSSResultCode());
    }
    
    @Then("the PRM_GROUP_FUNCTION database table should have the following row(s): $tables")
    public final void thePRM_GROUP_FUNCTIONDatabaseTableShouldHaveTheFollowingRow(ExamplesTable expectedDeviceInfoTable) {
        try {
            ITable actualDeviceInfoTable = 
                dbInit.getTableSnapshot("PRM_GROUP_FUNCTION");
            Assert.assertEquals("Compare the number of rows in PRM_GROUP_FUNCTION",
                    expectedDeviceInfoTable.getRowCount(),
                    actualDeviceInfoTable.getRowCount());
            
            int i = 0;
            for(Map<String, String> expectedDeviceInfo : expectedDeviceInfoTable.getRows()) {
                Assert.assertEquals("Compare the CORPID in PRM_GROUP_FUNCTION row" + i,
                        (Integer)Integer.parseInt(expectedDeviceInfo.get("groupcode")),
                        (Integer)actualDeviceInfoTable.getValue(i, "groupcode"));
                Assert.assertEquals("Compare the STOREID in PRM_GROUP_FUNCTION row" + i,
                        expectedDeviceInfo.get("groupname"),
                        (String)actualDeviceInfoTable.getValue(i, "groupname"));
                Assert.assertEquals("Compare the transactions in PRM_GROUP_FUNCTION row" + i,
                        (expectedDeviceInfo.get("transactions").equals("true")) ? Boolean.TRUE : Boolean.FALSE,
                        (Boolean)actualDeviceInfoTable.getValue(i, "transactions"));
                Assert.assertEquals("Compare the reports in PRM_GROUP_FUNCTION row" + i,
                        (expectedDeviceInfo.get("reports").equals("true")) ? Boolean.TRUE : Boolean.FALSE,
                        (Boolean)actualDeviceInfoTable.getValue(i, "reports"));
                Assert.assertEquals("Compare the settings in PRM_GROUP_FUNCTION row" + i,
                        (expectedDeviceInfo.get("settings").equals("true")) ? Boolean.TRUE : Boolean.FALSE,
                        (Boolean)actualDeviceInfoTable.getValue(i, "settings"));
                Assert.assertEquals("Compare the merchandise in PRM_GROUP_FUNCTION row" + i,
                        (expectedDeviceInfo.get("merchandise").equals("true")) ? Boolean.TRUE : Boolean.FALSE,
                        (Boolean)actualDeviceInfoTable.getValue(i, "merchandise"));
                Assert.assertEquals("Compare the administration in PRM_GROUP_FUNCTION  row" + i,
                        (expectedDeviceInfo.get("administration") == null) ? null :
                        (expectedDeviceInfo.get("administration").equals("true")) ? Boolean.TRUE : Boolean.FALSE,
                        (Boolean)actualDeviceInfoTable.getValue(i, "administration"));
                Assert.assertEquals("Compare the drawer in PRM_GROUP_FUNCTION row" + i,
                        (expectedDeviceInfo.get("drawer") == null) ? null :
                        (expectedDeviceInfo.get("drawer").equals("true")) ? Boolean.TRUE : Boolean.FALSE,
                        (Boolean)actualDeviceInfoTable.getValue(i, "drawer"));
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in PRM_AUTHORIZATION_LINK.");
        }
    }

}
