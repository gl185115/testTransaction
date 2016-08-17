package ncr.res.mobilepos.deviceinfo.resource.test;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.model.PrinterInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewPrinterInfo;
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

public class UpdatePrinterInfoSteps extends Steps {
    
    DeviceInfoResource pdc = null;
    
    ResultBase resBase = null;
    PrinterInfo resPPI = null;
    ViewPrinterInfo viewInfo= null;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
       
    @Given("entries in MST_PRINTERINFO database table")
    public final void entriesPrinterInfoDB() throws Exception{
        new DBInitiator("Mst_PrinterInfo",
                "test/ncr/res/mobilepos/deviceinfo/"
                + "datasets/MST_PRINTERINFO.xml", DATABASE.RESMaster);
    }
    
    @Given("entries in MST_STOREINFO database table")
    public final void entriesStoreInfoDB() throws Exception{
        new DBInitiator("Mst_PrinterInfo",
                "test/ncr/res/mobilepos/deviceinfo/"
                + "datasets/MST_STOREINFO.xml", DATABASE.RESMaster);
    }
    
    @Given("a DeviceInfo service")
    public final void getPeripheralDeviceService()
    {
        pdc = new DeviceInfoResource();
        pdc.setContext(Requirements.getMockServletContext());
        Assert.assertNotNull(pdc);
    }
    
    @When("I update PrinterInfo for"
            + " storeid{$storeid} and printerid{$printerid} with json{$jsonObject}")
    public final void getPrinterInfo(
            String storeid, String printerid, String jsonObject)
    {
        if(pdc == null){
            pdc = new DeviceInfoResource();
        }
        
        if(storeid.equals("empty")) {
            storeid = "";
        }
        if(printerid.equals("empty")) {
            printerid = "";
        }
        if(jsonObject.equals("empty")) {
            jsonObject = "";
        }
        
        if(storeid.equals("null")) {
            storeid = null;
        }
        if(printerid.equals("null")) {
            printerid = null;
        }
        if(jsonObject.equals("null")) {
            jsonObject = null;
        }
        
        viewInfo = pdc.updatePrinterInfo(storeid, printerid, jsonObject);
        resPPI = viewInfo.getPrinterInfo();
    }
    
    @Then ("ResultCode should be $result")
    public final void checkResultBaseCode(final int result)
    {
        Assert.assertEquals(result, viewInfo.getNCRWSSResultCode());
    }
    
    
    
    @Then ("printerid should be $result")
    public final void checkPrinterId(final String result)
    {
        Assert.assertEquals(result, resPPI.getPrinterId());
    }
    
  
    @Then ("printername should be {$result}")
    public final void checkPrinterName(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result, resPPI.getPrinterName());
    }
    
    @Then ("description should be {$result}")
    public final void checkPrinterDescription(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPPI.getPrinterDescription());
    }
    
    @Then ("storeid should be {$result}")
    public final void checkDevId(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPPI.getRetailStoreId());
    }
    
    @Then ("ipaddress should be {$result}")
    public final void checkIpAddress(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPPI.getIpAddress());
    }
    
    @Then ("portnumtcp should be {$result}")
    public final void checkPortNumTcp(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPPI.getPortNumTcp());
    }
    
    @Then ("portnumudp should be {$result}")
    public final void checkPortnumUdp(String result)
    {
        if(result.equals("empty"))
        {
            result = null;
        }
        
        Assert.assertEquals(result,
                resPPI.getPortNumUdp());
    }
    
   
    
    
}
