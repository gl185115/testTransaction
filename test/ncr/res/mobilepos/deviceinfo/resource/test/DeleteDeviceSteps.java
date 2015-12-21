package ncr.res.mobilepos.deviceinfo.resource.test;

import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
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

public class DeleteDeviceSteps extends Steps {

    DeviceInfoResource deviceRes = null;
    private ResultBase resultBase = null;
    private DBInitiator dbInit = null;

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        dbInit = new DBInitiator("Mst_DeviceInfo", "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/" + "MST_DEVICEINFO_for_delete.xml", DATABASE.RESMaster);
        
    }

    @AfterScenario
    public final void TearDown() {
        Requirements.TearDown();
    }

    @Given("a Device service")
    public final void deviceService() {
        deviceRes = new DeviceInfoResource();
        deviceRes.setContext(Requirements.getMockServletContext());
        Assert.assertNotNull(deviceRes);
    }

    @When("I delete device{$deviceid} of store{$storeid}")
    public final void deleteDevice(String deviceID, String storeID) {
        storeID = storeID.equals("null") ? null: storeID;
        deviceID = deviceID.equals("null") ? null: deviceID;
       
        resultBase = deviceRes.deleteDevice(deviceID, storeID, 0, null);
    }

    @Then("I should get resultCode $expectedResult")
    public final void testResultCode(final int expectedResult) {
        Assert.assertEquals(expectedResult, resultBase.getNCRWSSResultCode());
    }

    @Then("the MST_DEVICEINFO database table should have the following row(s): $deviceTables")
    public final void theMST_DEVICEINFODatabaseTableShouldHaveTheFollowingRow(ExamplesTable expectedDeviceInfoTable) {
        try {
            ITable actualDeviceInfoTable = dbInit.getTableSnapshot("MST_DEVICEINFO");
            Assert.assertEquals("Compare the number of rows in MST_DEVICEINFO",
                    expectedDeviceInfoTable.getRowCount(),
                    actualDeviceInfoTable.getRowCount());
            
            int i = 0;
            for(Map<String, String> expectedDeviceInfo : expectedDeviceInfoTable.getRows()) {
                Assert.assertEquals("Compare the STOREID in MST_DEVICEINFO row" + i,
                        expectedDeviceInfo.get("storeid"),
                        (String)actualDeviceInfoTable.getValue(i, "storeid"));
                Assert.assertEquals("Compare the TERMINALID in MST_DEVICEINFO row" + i,
                        expectedDeviceInfo.get("terminalid"),
                        ((String)actualDeviceInfoTable.getValue(i, "terminalid")).trim());
                Assert.assertEquals("Compare the PRINTERID in MST_DEVICEINFO row" + i,
                        expectedDeviceInfo.get("printerid"),
                        actualDeviceInfoTable.getValue(i, "printerid"));
                String expPOStermID = expectedDeviceInfo.get("linkposterminalid");
                expPOStermID = (expPOStermID.equals("null")) ? null : expPOStermID;
                Assert.assertEquals("Compare the POSTERMINALID in MST_DEVICEINFO row" + i,
                        expPOStermID,
                        (String)actualDeviceInfoTable.getValue(i, "linkposterminalid"));
                Assert.assertEquals("Compare the SENDLOGFILE in MST_DEVICEINFO row" + i,
                        Integer.valueOf(expectedDeviceInfo.get("sendlogfile")),
                        (Integer)actualDeviceInfoTable.getValue(i, "sendlogfile"));
                Assert.assertEquals("Compare the SAVELOGFILE in MST_DEVICEINFO row" + i,
                        Integer.valueOf(expectedDeviceInfo.get("savelogfile")),
                        (Integer)actualDeviceInfoTable.getValue(i, "savelogfile"));
                Assert.assertEquals("Compare the AUTOUPLOAD in MST_DEVICEINFO row" + i,
                        Integer.valueOf(expectedDeviceInfo.get("autoupload")),
                        (Integer)actualDeviceInfoTable.getValue(i, "autoupload"));
                Assert.assertEquals("Compare the Status in MST_DEVICEINFO row" + i,
                        (expectedDeviceInfo.get("status")),
                        actualDeviceInfoTable.getValue(i, "status"));
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in MST_DEVICEINFO.");
        }
    }
}
