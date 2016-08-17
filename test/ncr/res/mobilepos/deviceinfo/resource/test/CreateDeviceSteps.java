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

public class CreateDeviceSteps extends Steps {
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
    
    @Given("an empty MST_DEVICEINFO database table")
    public final void anEmptyMST_DEVICEINFODatabaseTable(){
        dbInit = new DBInitiator("Mst_DeviceInfo", 
                "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/MST_DEVICEINFO_EMPTY.xml", DATABASE.RESMaster);
    }
    
    @Given("entries in MST_PRINTERINFO database table")
    public final void entriesPrinterInfoDB() throws Exception{
        new DBInitiator("Mst_PrinterInfo",
                "test/ncr/res/mobilepos/deviceinfo/"
                + "datasets/MST_PRINTERINFO.xml", DATABASE.RESMaster);
    }
    
    @Given("an MST_STOREINFO database table")
    public final void anMST_STOREINFODatabaseTable(){
        dbInit = new DBInitiator("Mst_StoreInfo", 
                "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/MST_STOREINFO.xml", DATABASE.RESMaster);
    }
    
    @Given("a Device Information Web Service")
    public final void aDeviceInformationWebService() {
        deviceInfoTest = new DeviceInfoResource();
    }
    
    @When("a request to add a device in the service given the Device information in JSON format: $jsonDeviceInfo")
    public final void aRequestToAddDeviceInTheServiceGivenTheDeviceInformationInJSONFormat(String jsonDeviceInfo) {
        actualResultBase = deviceInfoTest.createDevice(jsonDeviceInfo);
    }
    
    @When("I delete device{$deviceid} of store{$storeid}")
    public final void deleteDevice(String deviceID, String storeID) {
        storeID = storeID.equals("null") ? null: storeID;
        deviceID = deviceID.equals("null") ? null: deviceID;
       
        actualResultBase = deviceInfoTest.deleteDevice(deviceID, storeID, 0, null);
    }
    
    @Then("the MST_DEVICEINFO database table should have the following row(s): $deviceTables")
    public final void theMST_DEVICEINFODatabaseTableShouldHaveTheFollowingRow(ExamplesTable expectedDeviceInfoTable) {
        try {
        	Assert.assertNotNull(dbInit);
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
                String linkqueuebuster = expectedDeviceInfo.get("queuebusterlink");
                linkqueuebuster = (linkqueuebuster.equals("null")) ? null : linkqueuebuster;
                Assert.assertEquals("Compare the QUEUEBUSTERLINK in MST_DEVICEINFO row" + i,
                        linkqueuebuster,
                        (String)actualDeviceInfoTable.getValue(i, "linkqueuebuster"));
                String linkauthorization = expectedDeviceInfo.get("authorizationlink");
                linkauthorization = (linkauthorization.equals("null")) ? null : linkauthorization;
                Assert.assertEquals("Compare the AUTHORIZATIONLINK in MST_DEVICEINFO row" + i,
                        linkauthorization,
                        (String)actualDeviceInfoTable.getValue(i, "linkauthorization"));
                String linksignature = expectedDeviceInfo.get("signaturelink");
                linksignature = (linksignature.equals("null")) ? null : linksignature;
                Assert.assertEquals("Compare the SIGNATURELINK in MST_DEVICEINFO row" + i,
                        linksignature,
                        (String)actualDeviceInfoTable.getValue(i, "linksignature"));
                Assert.assertEquals("Compare the STATUS in MST_DEVICEINFO row" + i,
                        expectedDeviceInfo.get("status"),
                        (String)actualDeviceInfoTable.getValue(i, "status"));
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in MST_DEVICEINFO.");
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
