package ncr.res.mobilepos.deviceinfo.resource.test;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.model.DeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewDeviceInfo;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import java.lang.reflect.Field;

import javax.servlet.ServletContext;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;
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
    	new DBInitiator("MST_DEVICEINFO", "test/ncr/res/mobilepos/deviceinfo/resource/test/MST_DEVICE_INFO_9999_000031_9999_1_9876.xml", DATABASE.RESMaster);
    }
    
    @Given("an empty MST_PRINTERINFO database table")
    public final void emptyPrinterInfoDB() throws Exception{
        new DBInitiator("MST_PRINTERINFO", "test/ncr/res/mobilepos/device/resource/test/MST_PRINTERINFO_EMPTY.xml", DATABASE.RESMaster);
    }
    
    @Given("entries in MST_PRINTERINFO database table")
    public final void entriesPrinterInfoDB() throws Exception{
        new DBInitiator("MST_PRINTERINFO", "test/ncr/res/mobilepos/deviceinfo/resource/test/MST_PRINTERINFO.xml", DATABASE.RESMaster);
    }
    
    @Given("a PeripheralDeviceControl service")
    public final void getPeripheralDeviceService()
    {
 
        ServletContext context = Requirements.getMockServletContext();
        pdc = new DeviceInfoResource();
        
        try {
           Field Context = pdc.getClass().getDeclaredField("context");
            Context.setAccessible(true);
           Context.set(pdc, context);
           GlobalConstant.setCorpid("9999");
           Assert.assertNotNull(pdc);
           
           
       } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Cant load Mock Servlet Context.");
        }
    	
    }

    @When("I get PeripheralDeviceInfo for companyid $companyid, storeid $storeid, deviceid $deviceid and trainingmode $trainingmode")
    public final void getPeripheralDeviceInfo(
    		final String companyid, final String storeid, final String deviceid,final int trainingmode)
    {
        if(pdc == null){
        	
            pdc = new DeviceInfoResource();
           
        }
        
        viewInfo = pdc.getDeviceInfo(companyid, storeid, deviceid, trainingmode);
          resPDI = viewInfo.getDeviceInfo();
    }
    
    @Then ("ResultCode should be $result")
    public final void checkResultBaseCode(final int result)
    {
        Assert.assertEquals(result, viewInfo.getNCRWSSResultCode());
    }
    
    @Then ("printer should be not null")
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
        if("empty".equals(result)) {
            result = "";
        }
        
        Assert.assertEquals(result, resPDI.getLinkPOSTerminalId());
    }
    
    @When ("I set posterminal link of storeid $storeid and deviceid $deviceid to linkposterminalid $linkposterminalid")
    public final void setposterminalid(String storeid, String deviceid, String linkposterminalid) {
           if(pdc == null) {
            pdc = new DeviceInfoResource();
               }
        
        storeid = "null".equals(storeid) ? null : storeid;
        deviceid = "null".equals(deviceid) ? null : deviceid;
        linkposterminalid = "null".equals(linkposterminalid) ? null : linkposterminalid;
//        if(linkposterminalid == null){
//        	
//        }
//        else if(linkposterminalid.equals("")){
//        	linkposterminalid = "";
//   	   linkposterminalid = "".equals(linkposterminalid) ? "" : linkposterminalid;
//       }
        
        
        resBase = pdc.setLinkPosTerminalId(storeid,
                deviceid, linkposterminalid);
    }
    
    @Then ("the result base should be $result")
    public final void checkResultBase(final int result)
    {
        Assert.assertEquals(result, resBase.getNCRWSSResultCode());
    }
    
    @Then ("printername should be  $result")
    public final void checkPrinterName(String result) {
        if("empty".equals(result) || "null".equals(result) ) {
            result = null;
        }
        
        Assert.assertEquals(result, resPDI.getPrinterInfo().getPrinterName());
    }
    
    @Then ("description should be $result")
    public final void checkPrinterDescription(String result) {
    	 if("empty".equals(result) || "null".equals(result) ) {
             result = null;
         }
        
        Assert.assertEquals(result,
                resPDI.getPrinterInfo().getPrinterDescription());
    }

}
