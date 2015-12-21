package ncr.res.mobilepos.deviceinfo.resource.test;

import java.io.IOException;
import java.util.Map;

import ncr.res.mobilepos.deviceinfo.model.DeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewDeviceInfo;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

public class GetDeviceInfoSteps extends Steps {
    
    DeviceInfoResource pdc = null;
    
    ResultBase resBase = null;
    DeviceInfo resPDI = null;
    ViewDeviceInfo viewInfo = null;
    private DBInitiator dbInitMST_PRINTERINFO;
    private DBInitiator dbInitMST_DEVICEINFO;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("an initial deviceinfo entry in database")
    public final void addDeviceInfoInDB() throws Exception{
        dbInitMST_DEVICEINFO = new DBInitiator("Mst_DeviceInfo",
                "test/ncr/res/mobilepos/deviceinfo/"
                + "datasets/MST_DEVICE_INFO_9999_000031_9999_1_9876.xml", DATABASE.RESMaster);
    }
    
    @Given("an empty MST_PRINTERINFO database table")
    public final void emptyPrinterInfoDB() throws Exception{
        new DBInitiator("Mst_PrinterInfo", "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/MST_PRINTERINFO_EMPTY.xml", DATABASE.RESMaster);
    }
    
    @Given("entries in MST_PRINTERINFO database table")
    public final void entriesPrinterInfoDB() throws Exception{
        dbInitMST_PRINTERINFO = new DBInitiator("Mst_PrinterInfo",
                "test/ncr/res/mobilepos/deviceinfo/"
                + "datasets/MST_PRINTERINFO.xml", DATABASE.RESMaster);
    }
    
    @Given("NORMAL pricing System Configuration (PricingType = 0)")
    public final void normalPricingSystemConfigDB() throws Exception{
        dbInitMST_PRINTERINFO = new DBInitiator("Mst_PrinterInfo",
                "test/ncr/res/mobilepos/deviceinfo/"
                + "datasets/PRM_SYSTEM_CONFIG_NORMAL_PRICING.xml", DATABASE.RESMaster);        
    }
    
    @Given("NOT NORMAL pricing System Configuration (PricingType = 4)")
    public final void notNormalPricingSystemConfigDB() throws Exception{
        dbInitMST_PRINTERINFO = new DBInitiator("Mst_PrinterInfo",
                "test/ncr/res/mobilepos/deviceinfo/"
                + "datasets/PRM_SYSTEM_CONFIG_NOT_NORMAL_PRICING.xml", DATABASE.RESMaster);        
    }
    
    @Given("a DeviceInfo service")
    public final void getPeripheralDeviceService()
    {
        pdc = new DeviceInfoResource();
        pdc.setContext(Requirements.getMockServletContext());
        Assert.assertNotNull(pdc);
    }
    
    @When("I get DeviceInfo for"
            + " storeid{$storeid} and deviceid{$deviceid}")
    public final void getPeripheralDeviceInfo(String storeid, String deviceid) {
        if(pdc == null){
            pdc = new DeviceInfoResource();
        }
        
        storeid = "null".equals(storeid) ? null : storeid;
        deviceid = "null".equals(deviceid) ? null : deviceid;
        
        viewInfo = pdc.getDeviceInfo(null, deviceid, storeid, 0);
        resPDI = viewInfo.getDeviceInfo();
    }
    
    @Then ("ResultCode should be $result")
    public final void checkResultBaseCode(final int result)
    {
        Assert.assertEquals(result, viewInfo.getNCRWSSResultCode());
    }
    
    @Then ("printer should be notnull")
    public final void checkPrinter()
    {
        Assert.assertNotNull(resPDI.getPrinterInfo());
    }
    
    @Then ("printerid should be $result")
    public final void checkPrinterId(final String result)
    {
        Assert.assertEquals(result, (resPDI.getPrinterInfo().getPrinterId() == null ) ? "null" : resPDI.getPrinterInfo().getPrinterId());
    }
    
    @Then ("linkposterminalid should be $result")
    public final void checkLinkPosTerminalId(String result)
    {
        if(result.equals("{empty}")) {
            result = "";
        }
        Assert.assertEquals(result, resPDI.getLinkPOSTerminalId());
    }
    
    @Then ("printername should be {$result}")
    public final void checkPrinterName(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result, resPDI.getPrinterInfo().getPrinterName());
    }
    
    @Then ("description should be {$result}")
    public final void checkPrinterDescription(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPDI.getPrinterInfo().getPrinterDescription());
    }
    
    @Then ("deviceid should be {$result}")
    public final void checkDevId(String result) {
        if("empty".equals(result) || "null".equals(result)) {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPDI.getDeviceId());
    }
    
    @Then ("devicename should be {$result}")
    public final void checkDevName(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPDI.getDeviceName());
    }
    
    @Then ("logautoupload should be {$result}")
    public final void checkLogAutoUpload(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPDI.getLogAutoUpload());
    }
    
    @Then ("logsize should be {$result}")
    public final void checkLogSize(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPDI.getLogSize());
    }
    
    @Then ("pricingtype should be {$result}")
    public final void checkPricingType(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPDI.getPricingType());
    }
    
    @Then ("retailstoreid should be {$result}")
    public final void checkRetailStoreId(String result) {
    	 if("empty".equals(result) || "null".equals(result)) {
             result = null;
         }
        
        Assert.assertEquals(result,
                resPDI.getRetailStoreId());
    }
    
    @Then ("savelogfile should be {$result}")
    public final void checkSaveLogFile(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPDI.getSaveLogFile());
    }
    
    @Then ("signaturelink should be {$result}")
    public final void checkSigLink(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPDI.getSignaturelink());
    }
    
    @Then ("queuebusterlink should be {$result}")
    public final void checkQueueBusterLink(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPDI.getQueuebusterlink());
    }
    
    @Then ("authorizationlink should be {$result}")
    public final void checkAuthLink(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPDI.getAuthorizationlink());
    }
    
    @Then ("the DeviceInfo should have the following JSON format: $jsonDeviceInfo")
    public final void theDeviceInfoShouldHaveTheFollowingJSONFormat(String expectedDeviceInfoJson) {
        try {
            JsonMarshaller<ViewDeviceInfo> deviceInfoJsonMarshaller =
                new JsonMarshaller<ViewDeviceInfo>();
            
            String actualDeviceInfoJson =
                deviceInfoJsonMarshaller.marshall(viewInfo);
            
            String resCode = "\"NCRWSSResultCode\":"+ viewInfo.getNCRWSSResultCode(); 
            System.out.println("rescode: "+resCode);
            if(actualDeviceInfoJson.contains(resCode)){
                actualDeviceInfoJson = actualDeviceInfoJson.replaceAll(resCode, "");
                actualDeviceInfoJson = actualDeviceInfoJson.replaceAll("\"NCRWSSExtendedResultCode\":0", "");
            }
            if(expectedDeviceInfoJson.contains(resCode)){
                expectedDeviceInfoJson = expectedDeviceInfoJson.replaceAll(resCode, "");
                expectedDeviceInfoJson = expectedDeviceInfoJson.replaceAll("\"NCRWSSExtendedResultCode\":0", "");
            }
            
            Assert.assertEquals("Verify the DeviceInfo JSON Format",
                    expectedDeviceInfoJson, actualDeviceInfoJson);
        } catch (IOException e) {
            Assert.fail("Failed to verify the DeviceInfo JSON format");
            e.printStackTrace();
        }
    }
    
    @Then("I should have the following rows in MST_PRINTERINFO: $expectedPrinters")
    public void iShouldHaveTheFollowingRowsInMST_PRINTERINFO(ExamplesTable expectedPrinters) {
        ITable actualPrinters = dbInitMST_PRINTERINFO.getTableSnapshot("MST_PRINTERINFO");
        
        Assert.assertEquals("Compare the actual rows in the MST_PRINTERINFO",
                expectedPrinters.getRowCount(), actualPrinters.getRowCount());
        try {
            int i = 0;
            for(Map<String, String> expectedPrinter : expectedPrinters.getRows()) {
                    Assert.assertEquals("Compare the STOREID in row"+ i,
                            expectedPrinter.get("storeid"),
                            actualPrinters.getValue(i, "storeid"));
                    Assert.assertEquals("Compare the PRINTERID in row"+ i,
                            expectedPrinter.get("printerid"),
                            actualPrinters.getValue(i, "printerid"));
                    Assert.assertEquals("Compare the PRINTERNAME in row"+ i,
                            expectedPrinter.get("printername"),
                            actualPrinters.getValue(i, "printername"));
                    Assert.assertEquals("Compare the DESCRIPTION in row"+ i,
                            expectedPrinter.get("description"),
                            actualPrinters.getValue(i, "description"));
                    Assert.assertEquals("Compare the IPADDRESS in row"+ i,
                            expectedPrinter.get("ipaddress"),
                            actualPrinters.getValue(i, "ipaddress"));
                    //Assert.assertEquals("Compare the PORTNUMTCP in row"+ i,
                    //        expectedPrinter.get("portnumtcp"),
                    //        actualPrinters.getValue(i, "portnumtcp"));
                    //Assert.assertEquals("Compare the PORTNUMUDP in row"+ i,
                      //      expectedPrinter.get("portnumudp"),
                        //    actualPrinters.getValue(i, "portnumudp"));
                    i++;
                } 
            }
        catch (DataSetException e) {
            e.printStackTrace();
            Assert.fail("Fail to verifiy the MST_PRINTERINFO database.");
        }
    }
    
    @Then("I should have the following rows in MST_DEVICEINFO: $expectedDeviceInfoTable")
    public final void shouldHaveTheFollowingRowsInMST_DEVICEINFO(final ExamplesTable expectedDeviceInfoTable) {
        try {
            ITable actualDeviceInfoTable = dbInitMST_DEVICEINFO.getTableSnapshot("MST_DEVICEINFO");
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
                Assert.assertEquals("Compare the TILLID in MST_DEVICEINFO row" + i,
                        (expectedDeviceInfo.get("tillid")),
                        (String)actualDeviceInfoTable.getValue(i, "tillid"));
                Assert.assertEquals("Compare the TERMINALID in MST_DEVICEINFO row" + i,
                        expectedDeviceInfo.get("terminalid"),
                        ((String)actualDeviceInfoTable.getValue(i, "terminalid")).trim());
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
                String linkqueuebuster = expectedDeviceInfo.get("linkqueuebuster");
                linkqueuebuster = (linkqueuebuster.equals("null")) ? null : linkqueuebuster;
                Assert.assertEquals("Compare the POSTERMINALID in MST_DEVICEINFO row" + i,
                        linkqueuebuster,
                        (String)actualDeviceInfoTable.getValue(i, "linkqueuebuster"));
                String linkauthorization = expectedDeviceInfo.get("linkauthorization");
                linkauthorization = (linkauthorization.equals("null")) ? null : linkauthorization;
                Assert.assertEquals("Compare the POSTERMINALID in MST_DEVICEINFO row" + i,
                        linkauthorization,
                        (String)actualDeviceInfoTable.getValue(i, "linkauthorization"));
                String linksignature = expectedDeviceInfo.get("linksignature");
                linksignature = (linksignature.equals("null")) ? null : linksignature;
                Assert.assertEquals("Compare the POSTERMINALID in MST_DEVICEINFO row" + i,
                        linksignature,
                        (String)actualDeviceInfoTable.getValue(i, "linksignature"));
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in MST_DEVICEINFO.");
        }
    }
}
