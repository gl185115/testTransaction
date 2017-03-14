package ncr.res.mobilepos.device.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.model.DeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.PrinterInfo;
import ncr.res.mobilepos.deviceinfo.model.Printers;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.journalization.resource.JournalizationResource;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.tillinfo.resource.TillInfoResource;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;

public class DeviceInfoGetAllPrinterSteps extends Steps {
    
    DeviceInfoResource pdc = null;
    
    ResultBase resBase = null;
    Printers printers = null;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("an empty MST_PRINTERINFO database table")
    public final void emptyPrinterInfoDB() throws Exception{
        new DBInitiator("Mst_PrinterInfo", "test/resource/test/MST_PRINTERINFO_EMPTY.xml", DATABASE.RESMaster);
    }
    
    @Given("entries in MST_PRINTERINFO_GET_ALL_PRINTER database table")
    public final void entriesPrinterInfoDB() throws Exception{
        new DBInitiator("Mst_PrinterInfo", "test/resource/test/MST_PRINTERINFO_GET_ALL_PRINTER.xml", DATABASE.RESMaster);
    }
         // entries in printers database table with special characters
    @Given("entries in printers database table with special characters")
    public final void entriesPrinterInfoSpCharsDB() throws Exception{
        new DBInitiator("Mst_PrinterInfo", "test/resource/test/MST_PRINTERINFO_withSpChars.xml", DATABASE.RESMaster);
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
           Assert.assertNotNull(pdc);
       } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Cant load Mock Servlet Context.");
        }
    	
    }
    
    @Then ("ResultCode should be $result")
    public final void checkResultBaseCode(final int result)
    {
        Assert.assertEquals(result, printers.getNCRWSSResultCode());
    }
    
    @Then ("printers should be not null")
    public final void checkPrinter()
    {
        Assert.assertNotNull(printers.getPrinters());
    }    
    /**
     * test.
     * @param limit - the limit
     */
    @When("I set the search limit to $limit")
    public final void setLimit(final int limit) {
        GlobalConstant.setMaxSearchResults(limit);
    }  
    
    @When ("I get all printers for storeid $storeid and key $key and name $name and limit $limit")
    public final void getPrinters(final String storeid,final String key, final String name, final int limit)
    {
    	
        if(pdc == null){
        	
        	
            pdc = new DeviceInfoResource();
            
            
        }
        printers = pdc.getAllPrinters(storeid,key,name,limit); 
        
    }
    
    @Then ("printers should be null")
    public final void nullprinter()
    {    	   	
    	if(printers != null && printers.getPrinters() != null && printers.getPrinters().length == 0){
    		Assert.assertNull(null);
    	} 
    }
    
    @Then ("printers length should be $length")
    public final void checkCount(final int length)
    {
        Assert.assertEquals(length, printers.getPrinters().length);
    }
    

	@Then("the printers should have values $expecteditems")
    public final void  testPrinterValues(final ExamplesTable expecteditems)
    throws DataSetException{ 
        
        int i = 0;
        Assert.assertEquals("Compare the expected number "
                + "of Devices against the actual",
                expecteditems.getRowCount(),
                printers.getPrinters().length);
        boolean firstItem = true;
        for (Map<String, String> item : expecteditems.getRows()) {

            Assert.assertEquals(printers.getPrinters()[i].getRetailStoreId(),
                    item.get("storeid"));
            Assert.assertEquals(printers.getPrinters()[i].getPrinterId(),
                    item.get("printerid"));
            Assert.assertEquals(printers.getPrinters()[i].getPrinterName(),
                    item.get("printername"));
            Assert.assertEquals(
                    printers.getPrinters()[i].getPrinterDescription(),
                    item.get("description"));
            Assert.assertEquals(
                    printers.getPrinters()[i].getIpAddress(),
                    item.get("ipaddress"));
            Assert.assertEquals(
                    printers.getPrinters()[i].getPortNumTcp(),
                    item.get("portnumtcp").equalsIgnoreCase("null")? null: item.get("portnumtcp"));
            Assert.assertEquals(
                    printers.getPrinters()[i].getPortNumUdp(),
                    item.get("portnumudp").equalsIgnoreCase("null")? null: item.get("portnumudp"));
            i++;
        }
    }
    
}
