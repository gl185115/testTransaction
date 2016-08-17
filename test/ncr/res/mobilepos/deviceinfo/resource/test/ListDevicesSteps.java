package ncr.res.mobilepos.deviceinfo.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.StringWriter;
import java.sql.SQLException;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.model.SearchedDevice;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.dbunit.DatabaseUnitException;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class ListDevicesSteps extends Steps {

    private DeviceInfoResource deviceRes = null;
    private SearchedDevice searchedDevice = null;
    /**
     * Initializes database data.
     */
    private DBInitiator dbInit;
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();  
        
        dbInit = new DBInitiator("ListDeviceSteps", DATABASE.RESMaster);
        try {
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/deviceinfo/datasets/MST_DEVICEINFO_for_list.xml");
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/deviceinfo/datasets/MST_STOREINFO.xml");
		
		} catch (Exception e) {} 
      
    }

    @AfterScenario
    public final void TearDown() {
        Requirements.TearDown();
    } 
    
    @Given("a database table with special characters")
    public final void aDeviceDatabaseTable() {
    	try {
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/deviceinfo/datasets/MST_DEVICEINFO_listWithSpChars.xml");
		} catch (Exception e) {}
   
    }

    @Given("a Device service")
    public final void deviceService() {
        deviceRes = new DeviceInfoResource();
        deviceRes.setContext(Requirements.getMockServletContext());
        Assert.assertNotNull(deviceRes);
        GlobalConstant.setMaxSearchResults(2);
    }
    
    /**
     * test.
     * @param limit - the limit
     */
    @When("I set the search limit to $limit")
    public final void setLimit(final int limit) {
        GlobalConstant.setMaxSearchResults(limit);
    }

    @When("I search device with key{$key}, storeid{$storeid}, name{$name}, limit{$limit}")
    public final void searchDevice(String key, String storeid, String name, int limit) {
        key = key.equals("null") ? null: key;
        storeid = storeid.equals("null") ? null: storeid;
        
        searchedDevice = deviceRes.listDevices(storeid, key, name, limit);
    }
    
    @Then("I should get $expected resultcode")
    public final void testResultCode(final int expected){
        Assert.assertEquals(expected, searchedDevice.getNCRWSSResultCode());
    }

    @Then("I should get $expectedNoOfDevices devices")
    public final void testListSize(final int expectedNoOfDevices) {
        Assert.assertEquals(expectedNoOfDevices, searchedDevice.getDevices().size());
    }
    
    @Then("I should have an xml $expectedXml")
    public final void testXml(final String expectedXml) {
        try {
            StringWriter writer = new StringWriter();
            
            JAXBContext jaxbcontext = JAXBContext.newInstance(SearchedDevice.class);
            Marshaller m = jaxbcontext.createMarshaller();
            m.marshal(searchedDevice, writer);
            Assert.assertEquals("Expect the device Listed", expectedXml, writer.toString());
            
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

}
