package ncr.res.mobilepos.deviceinfo.resource.test;

import java.io.IOException;
import java.util.Map;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.JsonMarshaller;
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
import org.junit.Assert;

public class DeleteCreditAuthLinkSteps extends Steps {
    private DBInitiator dbInit;
    private DeviceInfoResource deviceInfoTest;
    private ResultBase actualResultBase;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();        
    }

    @AfterScenario
    public final void TearDown() {
        Requirements.TearDown();
    }
    
    @Given("a PRM_AUTHORIZATION_LINK database table")
    public final void anEmptyPRM_AUTHORIZATION_LINKDatabaseTable(){
        dbInit = new DBInitiator("prm_queuebuster", 
                "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/PRM_AUTHORIZATION_LINK_ForDelete.xml", DATABASE.RESMaster);
    }
    
    @Given("a Device Information Web Service")
    public final void aDeviceInformationWebService() {
        deviceInfoTest = new DeviceInfoResource();
    }
    
    @When("I delete a link with storeid: $storeid, linkid: ($linkid)")
    public final void deleteLink(String storeid, String linkid) {
        if (storeid.equals("null")) {
            storeid = null;
        }
        if (linkid.equals("null")) {
            linkid = null;
        }
        
        actualResultBase = deviceInfoTest.deleteLink("creditauthorization", storeid, linkid);
    }
    
    @When("I delete a link with invalid type")
    public final void createInvalidLink() {
        actualResultBase = deviceInfoTest.deleteLink("creditauthorization1", "0", "0");
    }
    
    @Then("the PRM_AUTHORIZATION_LINK database table should have the following row(s): $deviceTables")
    public final void thePRM_AUTHORIZATION_LINKDatabaseTableShouldHaveTheFollowingRow(ExamplesTable expectedDeviceInfoTable) {
        try {
            ITable actualDeviceInfoTable = 
                dbInit.getTableSnapshot("PRM_AUTHORIZATION_LINK");
            
            int i = 0;
            for(Map<String, String> expectedDeviceInfo : expectedDeviceInfoTable.getRows()) {
                String storeid = (String)actualDeviceInfoTable.getValue(i, "storeid");
                Assert.assertEquals("Compare the STOREID in PRM_AUTHORIZATION_LINK row" + i,
                        expectedDeviceInfo.get("storeid"),
                        storeid.trim());
                String id = (String)actualDeviceInfoTable.getValue(i, "id");
                Assert.assertEquals("Compare the ID in PRM_AUTHORIZATION_LINK row" + i,
                        expectedDeviceInfo.get("id"),
                        id.trim());
                String displayname = (String)actualDeviceInfoTable.getValue(i, "displayname");
                Assert.assertEquals("Compare the DISPLAYNAME in PRM_AUTHORIZATION_LINK row" + i,
                        expectedDeviceInfo.get("displayname"),
                        (displayname==null)?"":displayname.trim());
                String status = (String)actualDeviceInfoTable.getValue(i, "status");
                Assert.assertEquals("Compare the Status in PRM_AUTHORIZATION_LINK row" + i,
                        expectedDeviceInfo.get("status"),
                        status.trim());
                i++;
            }
            
            Assert.assertEquals("Compare the number of rows in PRM_AUTHORIZATION_LINK",
                    expectedDeviceInfoTable.getRowCount(),
                    actualDeviceInfoTable.getRowCount());
            
            
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in PRM_AUTHORIZATION_LINK.");
        }
    }
    
    @Then("the actual ResultBase would be $jsonResultBase")
    public final void theActualResultBaseWouldBe(final String jsonResultBase) {
        JsonMarshaller<ResultBase> resultbaseMarshaller =
            new JsonMarshaller<ResultBase>();
        try {
            ResultBase expectedResultBase =
                resultbaseMarshaller.unMarshall(jsonResultBase, ResultBase.class);
            
            Assert.assertEquals("Compare the ResultCode",
                    expectedResultBase.getNCRWSSResultCode(),
                    actualResultBase.getNCRWSSResultCode());
            Assert.assertEquals("Compare the ExtendedResultCode",
                    expectedResultBase.getNCRWSSExtendedResultCode(),
                    actualResultBase.getNCRWSSExtendedResultCode());
        } catch (IOException e) {
            Assert.fail("Failed to compare the Expected"
                    + " ResultBase from the Actual ResultBase");
        }
    }

}
