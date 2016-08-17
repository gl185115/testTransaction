package ncr.res.mobilepos.deviceinfo.resource.test;

import java.util.Map;

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
import org.junit.Assert;

public class CreatePrinterSteps extends Steps {
    private DBInitiator dbInit;
    private DeviceInfoResource deviceInfoTest;
    private ResultBase actualResultBase;
    private String printerInfoDataSetPath = "test/ncr/res/mobilepos/deviceinfo/datasets/";

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }

    @AfterScenario
    public final void TearDown() {
        Requirements.TearDown();
    }

    @Given("$fileName table")
    public final void aTable(final String fileName) {
        dbInit = new DBInitiator("MST_PRINTERINFO", printerInfoDataSetPath
                + fileName + ".xml", DATABASE.RESMaster);
    }

    @Then("MST_PRINTERINFO table should have the following row(s): $deviceTables")
    public final void showTableInfo(ExamplesTable expectedDataTable) {
        try {
            ITable actualDataTable = dbInit.getTableSnapshot("MST_PRINTERINFO");
            Assert.assertEquals("Compare the number of rows in MST_PRINTERINFO",
                    expectedDataTable.getRowCount(),
                    actualDataTable.getRowCount());

            int i = 0;
            for(Map<String, String> expectedDeviceInfo : expectedDataTable.getRows()) {
                Assert.assertEquals("Compare the STOREID in MST_PRINTERINFO row" + i,
                        expectedDeviceInfo.get("storeid"),
                        actualDataTable.getValue(i, "storeid"));
                Assert.assertEquals(
                        "Compare the PRINTERID in MST_PRINTERINFO row" + i,
                        expectedDeviceInfo.get("printerid"),
                        String.valueOf(actualDataTable.getValue(i, "printerid")));
                Assert.assertEquals(
                        "Compare the PRINTERNAME in MST_PRINTERINFO row" + i,
                        expectedDeviceInfo.get("printername"),
                        String.valueOf(actualDataTable.getValue(i, "printername")));
                Assert.assertEquals(
                        "Compare the IPADDRESS in MST_PRINTERINFO row" + i,
                        expectedDeviceInfo.get("ipaddress"),
                        String.valueOf(actualDataTable.getValue(i, "ipaddress")));
                Assert.assertEquals(
                        "Compare the PORTNUMTCP in MST_PRINTERINFO row" + i,
                        expectedDeviceInfo.get("portnumtcp"),
                        String.valueOf(actualDataTable.getValue(i, "portnumtcp")));
                Assert.assertEquals(
                        "Compare the PORTNUMUDP in MST_PRINTERINFO row" + i,
                        expectedDeviceInfo.get("portnumudp"),
                        String.valueOf(actualDataTable.getValue(i, "portnumudp")));
                Assert.assertEquals(
                        "Compare the DESCRIPTION in MST_PRINTERINFO row" + i,
                        expectedDeviceInfo.get("description"),
                        String.valueOf(actualDataTable.getValue(i, "description")));

                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in MST_PRINTERINFO.");
        }
    }

    

    @When("I create printer with storeid:$storeID, printerid:$printerID, printerinfojson:$jsonPrinter")
    public final void createPrinter(String storeID, String printerID, String jsonPrinter){
        storeID = storeID.equalsIgnoreCase("null") ? null : storeID;
        printerID = printerID.equalsIgnoreCase("null") ? null : printerID;
        jsonPrinter = jsonPrinter.equalsIgnoreCase("null") ? null : jsonPrinter;
        actualResultBase = deviceInfoTest.createPrinter(storeID, printerID, jsonPrinter);
    }

    @Then("I should get $type code {$errorCode}")
    public final void testResultCode(String type, String code){
        Assert.assertEquals(code, String.valueOf(actualResultBase.getNCRWSSResultCode()));
    }

    @Given("a Device Information Web Service")
    public final void aDeviceInformationWebService() {
        deviceInfoTest = new DeviceInfoResource();
    }
}
