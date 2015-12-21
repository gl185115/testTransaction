package ncr.res.mobilepos.deviceinfo.resource.test;

import java.math.BigDecimal;
import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.model.DeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewDeviceInfo;
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

public class UpdateDeviceInfoSteps extends Steps {
    
    DeviceInfoResource pdc = null;
    
    ResultBase resBase = null;
    DeviceInfo resPDI = null;
    ViewDeviceInfo viewInfo= null;
    private DBInitiator dbInit;
    
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
    	 dbInit = new DBInitiator("Mst_DeviceInfo",
                "test/ncr/res/mobilepos/deviceinfo/"
                + "datasets/MST_DEVICE_INFO_9999_000031_9999_1_9876.xml", DATABASE.RESMaster);
    }
    
    @Given("an empty MST_PRINTERINFO database table")
    public final void emptyPrinterInfoDB() throws Exception{
    	dbInit = new DBInitiator("Mst_PrinterInfo", "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/MST_PRINTERINFO_EMPTY.xml", DATABASE.RESMaster);
    }
    
    @Given("entries in MST_PRINTERINFO database table")
    public final void entriesPrinterInfoDB() throws Exception{
    	dbInit = new DBInitiator("Mst_PrinterInfo",
                "test/ncr/res/mobilepos/deviceinfo/"
                + "datasets/MST_PRINTERINFO.xml", DATABASE.RESMaster);
    }
    
    @Given("a DeviceInfo service")
    public final void getPeripheralDeviceService()
    {
        pdc = new DeviceInfoResource();
        pdc.setContext(Requirements.getMockServletContext());
        Assert.assertNotNull(pdc);
    }
    
    @When("I update DeviceInfo for"
            + " storeid{$storeid} and deviceid{$deviceid} with json{$jsonObject}")
    public final void getPeripheralDeviceInfo(String companyid,
            String storeid, String deviceid, String jsonObject)
    {
        if(pdc == null){
            pdc = new DeviceInfoResource();
        }
        
        if(storeid.equals("empty")) {
            storeid = "";
        }
        if(deviceid.equals("empty")) {
            deviceid = "";
        }
        if(jsonObject.equals("empty")) {
            jsonObject = "";
        }
        
        if(storeid.equals("null")) {
            storeid = null;
        }
        if(deviceid.equals("null")) {
            deviceid = null;
        }
        if(jsonObject.equals("null")) {
            jsonObject = null;
        }
        
        viewInfo = pdc.updateDevice(companyid, deviceid, storeid, jsonObject, 0);
        resPDI = viewInfo.getDeviceInfo();
        System.out.println("Deviceinfo: "+resPDI);
    }
    
    @When("I delete device{$deviceid} of store{$storeid}")
    public final void deleteDevice(String deviceID, String storeID) {
    	if(pdc == null){
            pdc = new DeviceInfoResource();
        }
 
    	storeID = storeID.equals("null") ? null: storeID;
        deviceID = deviceID.equals("null") ? null: deviceID;
       
        resBase = pdc.deleteDevice(deviceID, storeID, 0, null);
        //viewInfo = (ViewDeviceInfo) actualResultBase;
    }
    
    @Then ("ResultCode after delete should be $result")
    public final void checkResultBaseCodeAfterDelete(final int result)
    {
        Assert.assertEquals(result, resBase.getNCRWSSResultCode());
    }

    
    @Then ("ResultCode should be $result")
    public final void checkResultBaseCode(final int result)
    {
        Assert.assertEquals(result, viewInfo.getNCRWSSResultCode());
    }
    
    @Then ("printerid assigned to device with id $deviceid in store $storeid should be $result")
    public final void checkPrinterIdFromDeviceInfo(String deviceid, String storeid, String result){
    	//ViewDeviceInfo viewDeviceInfo = pdc.getDeviceInfo(deviceid, storeid);
    	//System.out.println("New DeviceInfo: "+viewDeviceInfo.getDeviceInfo());
    	Assert.assertEquals(result, (viewInfo.getDeviceInfo().getPrinterId() == null) ? "null" : viewInfo.getDeviceInfo().getPrinterId());
    }
    
    @Then ("printer should be notnull")
    public final void checkPrinter()
    {
        Assert.assertNotNull(resPDI.getPrinterInfo() == null ? "null" : resPDI.getPrinterInfo());
    }
    
    @Then ("printerid should be $result")
    public final void checkPrinterIdAsString(final String result)
    {
        Assert.assertEquals(result, (resPDI.getPrinterInfo() == null || resPDI.getPrinterInfo().getPrinterId() == null) ? "null" : resPDI.getPrinterInfo().getPrinterId());
    }
    
    @Then ("tillid should be {$result}")
    public final void checkTillId(final String result)
    {
        Assert.assertEquals(result, resPDI.getTillId());
    }
    
    @Then ("linkposterminalid should be $result")
    public final void checkLinkPosTerminalId(String result)
    {
        if(result.equals("{empty}")) {
            result = "";
        }
        if(result.equals("empty"))
        {
            result = "null";
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
        
        Assert.assertEquals(result, (resPDI.getPrinterInfo() == null)? null : resPDI.getPrinterInfo().getPrinterName());
    }
    
    @Then ("description should be {$result}")
    public final void checkPrinterDescription(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result, (resPDI.getPrinterInfo() == null)? null : resPDI.getPrinterInfo().getPrinterDescription());
    }
    
    @Then ("deviceid should be {$result}")
    public final void checkDevId(String result)
    {
        if(result.equals("empty"))
        {
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
    
    @Then ("printerid string should be {$result}")
    public final void checkPrinterId(String result)
    {
        if(result == null || result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPDI.getPrinterId());
    }
    
    @Then ("retailstoreid should be {$result}")
    public final void checkRetailStoreId(String result)
    {
        if(result.equals("empty"))
        {
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
    
    @Then ("Txid should be {$result}")
    public final void checkTxtId(String result)
    {
    	
        if(result == null || "empty".equals(result))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPDI.getTxid());
    }
    
    @Then ("LastSuspendTxId should be {$result}")
    public final void checkLastSuspendTxId(String result)
    {
    	if(result == null || "empty".equals(result))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPDI.getSuspendtxid());
    }
    
    @Then("the MST_DEVICEINFO database table should have the following row(s): $deviceTables")
    public final void theMST_DEVICEINFODatabaseTableShouldHaveTheFollowingRow(ExamplesTable expectedDeviceInfoTable) {
		try {
			Assert.assertNotNull(dbInit);
			ITable actualDeviceInfoTable = dbInit
					.getTableSnapshot("MST_DEVICEINFO");
			Assert.assertEquals("Compare the number of rows in MST_DEVICEINFO",
					expectedDeviceInfoTable.getRowCount(),
					actualDeviceInfoTable.getRowCount());

			int i = 0;
			for (Map<String, String> expectedDeviceInfo : expectedDeviceInfoTable
					.getRows()) {
				Assert.assertEquals("Compare the STOREID in MST_DEVICEINFO row"
						+ i, expectedDeviceInfo.get("storeid"),
						(String) actualDeviceInfoTable.getValue(i, "storeid"));
				Assert.assertEquals(
						"Compare the TERMINALID in MST_DEVICEINFO row" + i,
						expectedDeviceInfo.get("terminalid"),
						((String) actualDeviceInfoTable.getValue(i,
								"terminalid")).trim());				
				Assert.assertEquals(
						"Compare the LastTxId in MST_DEVICEINFO row" + i,
						expectedDeviceInfo.get("lasttxId"),
						"" + ((BigDecimal) actualDeviceInfoTable.getValue(i, "LastTxId")) );
				Assert.assertEquals(
						"Compare the LastSuspendTxId in MST_DEVICEINFO row" + i,
						expectedDeviceInfo.get("lastsuspendtxId"),
						"" + ((BigDecimal) actualDeviceInfoTable.getValue(i, "LastSuspendTxId")) );				
				i++;
			}
		} catch (Exception ex) {
			ex.printStackTrace();
			Assert.fail("Fail to test the entry in MST_DEVICEINFO.");
		}
    }
    
    
    
}
