package ncr.res.mobilepos.deviceinfo.resource.test;

import java.io.IOException;
import java.util.Map;

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

public class CreateCreditAuthorizationLinkSteps extends Steps {
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
    
    @Given("an empty PRM_AUTHORIZATION_LINK database table")
    public final void anEmptyPRM_AUTHORIZATION_LINKDatabaseTable(){
        dbInit = new DBInitiator("prm_queuebuster", 
                "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/PRM_CREDITAUTHORIZATION_LINK_EMPTY.xml", DATABASE.RESMaster);
    }
    

    @Given("the store information")
    public final void updateStoreInfoTable(){
        dbInit = new DBInitiator("mst_storeinfo", 
                "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/MST_STOREINFO.xml", DATABASE.RESMaster);
    }
    @Given("a Device Information Web Service")
    public final void aDeviceInformationWebService() {
        deviceInfoTest = new DeviceInfoResource();
    }
    
    @When("I create a link with storeid: $storeid, linkid: $linkid, linkinfo: ($linkinfo)")
    public final void createLink(String storeid, String linkid, String linkinfo) {
    	linkid = "null".equals(linkid) ? null : linkid;
        actualResultBase = deviceInfoTest.createLink("creditauthorization", storeid, linkid, linkinfo);
    }
    
    @When("I create a link with invalid type")
    public final void createInvalidLink() {
        actualResultBase = deviceInfoTest.createLink("authorization1", "000031", "0", "{}");
    }
    
    @Then("the PRM_AUTHORIZATION_LINK database table should have the following row(s): $deviceTables")
    public final void thePRM_AUTHORIZATION_LINKDatabaseTableShouldHaveTheFollowingRow(ExamplesTable expectedDeviceInfoTable) {
        try {
            ITable actualDeviceInfoTable = 
                dbInit.getTableSnapshot("PRM_AUTHORIZATION_LINK");
            Assert.assertEquals("Compare the number of rows in PRM_AUTHORIZATION_LINK",
                    expectedDeviceInfoTable.getRowCount(),
                    actualDeviceInfoTable.getRowCount());
            
            int i = 0;
            for(Map<String, String> expectedDeviceInfo : expectedDeviceInfoTable.getRows()) {
                Assert.assertEquals("Compare the STOREID in PRM_AUTHORIZATION_LINK row" + i,
                        expectedDeviceInfo.get("storeid"),
                        (String)actualDeviceInfoTable.getValue(i, "storeid"));
                Assert.assertEquals("Compare the ID in PRM_AUTHORIZATION_LINK row" + i,
                        expectedDeviceInfo.get("id"),
                        (String)actualDeviceInfoTable.getValue(i, "id"));
                Assert.assertEquals("Compare the DISPLAYNAME in PRM_AUTHORIZATION_LINK row" + i,
                        expectedDeviceInfo.get("displayname"),
                        (String)actualDeviceInfoTable.getValue(i, "displayname"));
                i++;
            }
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
