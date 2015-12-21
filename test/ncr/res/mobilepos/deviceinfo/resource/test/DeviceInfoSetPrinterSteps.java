package ncr.res.mobilepos.deviceinfo.resource.test;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.model.DeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewDeviceInfo;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;


public class DeviceInfoSetPrinterSteps extends Steps {
    
    DeviceInfoResource pdc = null;
    DeviceInfo resPDI = null;
    ViewDeviceInfo viewInfo = null;
    
    ResultBase resBase = null;
    DBInitiator dbInitiator;
    
    String storeId;
    String terminalId;
    
    
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
        new DBInitiator("Mst_DeviceInfo", "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/"
                + "MST_DEVICE_INFO_9999_000031_9999_1_9876.xml", DATABASE.RESMaster);
    }
    
    @Given("an empty MST_PRINTERINFO database table")
    public final void emptyPrinterInfoDB() throws Exception{
        new DBInitiator("Mst_PrinterInfo", "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/MST_PRINTERINFO_EMPTY.xml", DATABASE.RESMaster);
    }
    
    @Given("entries in MST_PRINTERINFO database table")
    public final void entriesPrinterInfoDB() throws Exception{
        new DBInitiator("Mst_PrinterInfo", "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/MST_PRINTERINFO.xml", DATABASE.RESMaster);
    }
    
    @Given("a PeripheralDeviceControl service")
    public final void getPeripheralDeviceService()
    {
        pdc = new DeviceInfoResource();
        pdc.setContext(Requirements.getMockServletContext());
        Assert.assertNotNull(pdc);
    }
    
    @Then ("ResultCode should be $result")
    public final void checkResultBaseCode(final int result)
    {
        Assert.assertEquals(result, resBase.getNCRWSSResultCode());
    }
    
    @Then ("result should be $result")
    public final void checkResultCode(final int result)
    {
        Assert.assertEquals(result, viewInfo.getNCRWSSResultCode());
    }
    
    @When ("I set the printer of device{$corpid}{$storeid}{$deviceno}"
            + " to {$printerid}")
    public final void setPrinter(final String corpid, String storeid,
            String deviceno, String printerid) {
        if(null == pdc) {
            pdc = new DeviceInfoResource();
        }
        
        storeId = storeid;
        terminalId = deviceno;
        storeid = "null".equals(storeid) ? null : storeid;
        deviceno = "null".equals(deviceno) ? null : deviceno;
        printerid = "null".equals(printerid) ? null : printerid;
        
        GlobalConstant.setCorpid(corpid);
        resBase = pdc.setPrinterId(storeid, deviceno, printerid);
        
        viewInfo = pdc.getDeviceInfo(null, deviceno, storeid, 0);
        resPDI = viewInfo.getDeviceInfo();
    }
    
    @When("I get PeripheralDeviceInfo for corpid{$corpid},"
            + " storeid{$storeid} and deviceid{$deviceid}")
    public final void getPeripheralDeviceInfo(final String corpid,
            final String storeid, final String deviceid)
    {
        if(pdc == null){
            pdc = new DeviceInfoResource();
        }
        GlobalConstant.setCorpid(corpid);
        viewInfo = pdc.getDeviceInfo(null, deviceid, storeid, 0);
        resPDI = viewInfo.getDeviceInfo();
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
    
    @Then ("printer should be notnull")
    public final void checkPrinter()
    {
        Assert.assertNotNull(resPDI.getPrinterInfo());
    }
    @Then ("printerid should be $result")
    public final void checkPrinterId(final String result)
    {    	
        Assert.assertEquals(result, resPDI.getPrinterInfo().getPrinterId() == null ? "null" :  resPDI.getPrinterInfo().getPrinterId());
    }
    
    @Then ("linkposterminalid should be $result")
    public final void checkLinkId(String result)
    {
        if(result.equals("{empty}")) {
            result = "";
        }
        Assert.assertEquals(result, resPDI.getLinkPOSTerminalId().trim());
    }
    
    @Then("printerid in PeripheralDeviceInfo should  be $result")
    public final void checkPrinterIdInPDI(final String result) {
    	Assert.assertEquals(result, resPDI.getPrinterId());
    }
    
    @Then("value of printerid in PeripheralDeviceInfo should  be a null value")
    public final void checkPrinterIdInPDI() {
    	Assert.assertNull(resPDI.getPrinterId());
    }
}
