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

public class DeleteRegisteredDeviceSteps extends Steps {

    DeviceInfoResource deviceRes = null;
    private ResultBase resultBase = null;
    private DBInitiator dbInit = null;

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        dbInit = new DBInitiator("Mst_DeviceInfo", "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/" + "AUT_DEVICES_for_delete.xml", DATABASE.RESMaster);
        GlobalConstant.setCorpid("9999"); // Set corpid
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

    @When("I delete a registered device{$deviceid} of store{$storeid}")
    public final void deleteDevice(String deviceID, String storeID) {
        storeID = storeID.equals("null") ? null: storeID;
        deviceID = deviceID.equals("null") ? null: deviceID;
        
        resultBase = deviceRes.deleteRegisteredDevice(deviceID, storeID);
    }

    @Then("I should get resultCode $expectedResult")
    public final void testResultCode(final int expectedResult) {
        Assert.assertEquals(expectedResult, resultBase.getNCRWSSResultCode());
    }
    
    @Then("the AUT_DEVICES database table should have the following row(s): $deviceTables")
    public final void theAUT_DEVICESDatabaseTableShouldHaveTheFollowingRow(ExamplesTable expectedDeviceInfoTable) {
        try {
            ITable actualDeviceInfoTable = dbInit.getTableSnapshot("AUT_DEVICES");
            Assert.assertEquals("Compare the number of rows in AUT_DEVICES",
                    expectedDeviceInfoTable.getRowCount(),
                    actualDeviceInfoTable.getRowCount());
            
            int i = 0;
            for(Map<String, String> expectedDeviceInfo : expectedDeviceInfoTable.getRows()) {
                Assert.assertEquals("Compare the STOREID in AUT_DEVICES row" + i,
                        expectedDeviceInfo.get("storeid"),
                        (String)actualDeviceInfoTable.getValue(i, "storeid"));
                Assert.assertEquals("Compare the TERMINALID in AUT_DEVICES row" + i,
                        expectedDeviceInfo.get("terminalid"),
                        ((String)actualDeviceInfoTable.getValue(i, "terminalid")).trim());
                /*Assert.assertEquals("Compare the uuid in AUT_DEVICES row" + i,
                        Integer.valueOf(expectedDeviceInfo.get("devicename")),
                        (String)actualDeviceInfoTable.getValue(i, "printerid"));
                Assert.assertEquals("Compare the udid in AUT_DEVICES row" + i,
                        Integer.valueOf(expectedDeviceInfo.get("sendlogfile")),
                        (Integer)actualDeviceInfoTable.getValue(i, "sendlogfile"));
                Assert.assertEquals("Compare the state in AUT_DEVICES row" + i,
                        Integer.valueOf(expectedDeviceInfo.get("savelogfile")),
                        (Integer)actualDeviceInfoTable.getValue(i, "savelogfile"));
                Assert.assertEquals("Compare the lastactivetime in AUT_DEVICES row" + i,
                        Integer.valueOf(expectedDeviceInfo.get("lastactivetime")),
                        (Integer)actualDeviceInfoTable.getValue(i, "lastactivetime"));
                Assert.assertEquals("Compare the signstatus in AUT_DEVICES row" + i,
                        Integer.valueOf(expectedDeviceInfo.get("signstatus")),
                        (Integer)actualDeviceInfoTable.getValue(i, "signstatus"));
                Assert.assertEquals("Compare the signtid in AUT_DEVICES row" + i,
                        Integer.valueOf(expectedDeviceInfo.get("signtid")),
                        (Integer)actualDeviceInfoTable.getValue(i, "signtid"));
                Assert.assertEquals("Compare the signactivationkey in AUT_DEVICES row" + i,
                        Integer.valueOf(expectedDeviceInfo.get("signactivationkey")),
                        (Integer)actualDeviceInfoTable.getValue(i, "signactivationkey"));
                        */
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in AUT_DEVICES.");
        }
    }

}
