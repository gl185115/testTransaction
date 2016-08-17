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

public class DeviceInfoSteps extends Steps {
    
    DeviceInfoResource pdc = null;
    
    ResultBase resBase = null;
    DeviceInfo resPDI = null;
    ViewDeviceInfo viewInfo = null;
    
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
    	new DBInitiator("Mst_DeviceInfo",
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
        new DBInitiator("Mst_PrinterInfo",
                "test/ncr/res/mobilepos/deviceinfo/"
                + "datasets/MST_PRINTERINFO.xml", DATABASE.RESMaster);
    }
    
    @Given("a PeripheralDeviceControl service")
    public final void getPeripheralDeviceService()
    {
        pdc = new DeviceInfoResource();
        pdc.setContext(Requirements.getMockServletContext());
        GlobalConstant.setCorpid("9999");
        Assert.assertNotNull(pdc);
    }
    
    @When("I get PeripheralDeviceInfo for"
            + " storeid{$storeid} and deviceid{$deviceid}")
    public final void getPeripheralDeviceInfo(
            final String storeid, final String deviceid)
    {
        if(pdc == null){
            pdc = new DeviceInfoResource();
        }
        
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
        Assert.assertEquals(result, resPDI.getPrinterInfo().getPrinterId() == null ? "null" : resPDI.getPrinterInfo().getPrinterId());
    }
    
    @Then ("linkposterminalid should be $result")
    public final void checkLinkPosTerminalId(String result) {
        if("{empty}".equals(result)) {
            result = "";
        }
        
        Assert.assertEquals(result, resPDI.getLinkPOSTerminalId());
    }
    
    @When ("I set posterminal link of corpid{$corpid}, storeid{$storeid}"
            + " and deviceid{$deviceid} to {$linkposterminalid}")
    public final void setposterminalid(
            final String corpid, String storeid,
            String deviceid, String linkposterminalid) {
        if(pdc == null) {
            pdc = new DeviceInfoResource();
        }
        
        storeid = "null".equals(storeid) ? null : storeid;
        deviceid = "null".equals(deviceid) ? null : deviceid;
        linkposterminalid = "null".equals(linkposterminalid) ? null : linkposterminalid;
        
        resBase = pdc.setLinkPosTerminalId(storeid,
                deviceid, linkposterminalid);
    }
    
    @Then ("the result base should be $result")
    public final void checkResultBase(final int result)
    {
        Assert.assertEquals(result, resBase.getNCRWSSResultCode());
    }
    
    @Then ("printername should be {$result}")
    public final void checkPrinterName(String result) {
        if("empty".equals(result) || "null".equals(result) ) {
            result = null;
        }
        
        Assert.assertEquals(result, resPDI.getPrinterInfo().getPrinterName());
    }
    
    @Then ("description should be {$result}")
    public final void checkPrinterDescription(String result) {
    	 if("empty".equals(result) || "null".equals(result) ) {
             result = null;
         }
        
        Assert.assertEquals(result,
                resPDI.getPrinterInfo().getPrinterDescription());
    }

}
