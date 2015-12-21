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

public class DeletePrinterSteps extends Steps {

    DeviceInfoResource deviceRes = null;
    private ResultBase resultBase = null;
    private DBInitiator dbInit = null;

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        dbInit = new DBInitiator("MST_PRINTERINFO", "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/" + "MST_PRINTERINFO_FOR_DELETE.xml", DATABASE.RESMaster);
        new DBInitiator("MST_STOREINFO", "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/" + "MST_STOREINFO.xml", DATABASE.RESMaster);
        
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

    @When("I delete printer{$printerid} of store{$storeid}")
    public final void deleteDevice(String printerID, String storeID) {
        storeID = storeID.equals("null") ? null: storeID;
        printerID = printerID.equals("null") ? null: printerID;
       
        resultBase = deviceRes.deletePrinter(storeID, printerID);
    }

    @Then("I should get resultCode $expectedResult")
    public final void testResultCode(final int expectedResult) {
        Assert.assertEquals(expectedResult, resultBase.getNCRWSSResultCode());
    }

    @Then("the MST_PRINTERINFO database table should have the following row(s): $printerTables")
    public final void theMST_PRINTERINFODatabaseTableShouldHaveTheFollowingRow(ExamplesTable expectedPrinterInfoTable) {
        try {
            ITable actualPrinterInfoTable = dbInit.getTableSnapshot("MST_PRINTERINFO");
            Assert.assertEquals("Compare the number of rows in MST_PRINTERINFO",
                    expectedPrinterInfoTable.getRowCount(),
                    actualPrinterInfoTable.getRowCount());
            
            int i = 0;
            for(Map<String, String> expectedPrinterInfo : expectedPrinterInfoTable.getRows()) {
                Assert.assertEquals("Compare the STOREID in MST_PRINTERINFO row" + i,
                        expectedPrinterInfo.get("storeid"),
                        (String)actualPrinterInfoTable.getValue(i, "storeid"));
                Assert.assertEquals("Compare the PRINTERID in MST_PRINTERINFO row" + i,
                        expectedPrinterInfo.get("printerid"),
                        (actualPrinterInfoTable.getValue(i, "printerid")));
                Assert.assertEquals("Compare the DESCRIPTION in MST_PRINTERINFO row" + i,
                        (expectedPrinterInfo.get("description")),
                        actualPrinterInfoTable.getValue(i, "description"));
                Assert.assertEquals("Compare the PRINTERNAME in MST_PRINTERINFO row" + i,
                		expectedPrinterInfo.get("printername"),
                        (String)actualPrinterInfoTable.getValue(i, "printername"));
                Assert.assertEquals("Compare the IPADDRESS in MST_PRINTERINFO row" + i,
                        (expectedPrinterInfo.get("ipaddress")),
                        actualPrinterInfoTable.getValue(i, "ipaddress"));
                Assert.assertEquals("Compare the PORTNUMTCP in MST_PRINTERINFO row" + i,
                        Integer.valueOf(expectedPrinterInfo.get("portnumtcp")),
                        (Integer)actualPrinterInfoTable.getValue(i, "portnumtcp"));
                Assert.assertEquals("Compare the PORTNUMUDP in MST_PRINTERINFO row" + i,
                        Integer.valueOf(expectedPrinterInfo.get("portnumudp")),
                        (Integer)actualPrinterInfoTable.getValue(i, "portnumudp"));
                Assert.assertEquals("Compare the STATUS in MST_PRINTERINFO row" + i,
                        (expectedPrinterInfo.get("status")),
                        actualPrinterInfoTable.getValue(i, "status"));
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in MST_PRINTERINFO.");
        }
    }
}
