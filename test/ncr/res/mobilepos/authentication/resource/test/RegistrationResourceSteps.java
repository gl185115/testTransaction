package ncr.res.mobilepos.authentication.resource.test;

import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import junit.framework.Assert;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.authentication.resource.RegistrationResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

@SuppressWarnings("deprecation")
public class RegistrationResourceSteps extends Steps {
    private RegistrationResource registrationresource;
    private ResultBase deregistrationresult;
    private DeviceStatus result;
    private DBInitiator db;
    private boolean isDeregistrationResult = false;

    @BeforeScenario
    public final void setUp() throws Exception {
        Requirements.SetUp();
    }

    @AfterScenario
    public final void TearDown() {
        Requirements.TearDown();
    }

    @Given("a Registration Resource")
    public final void createResource() {
    	ServletContext servletContext = Requirements.getMockServletContext();
        registrationresource = new RegistrationResource();
        registrationresource.setContext(servletContext);
    }

    @Given("dataset {$xml} is loaded")
    public final void loadXml(final String xml) throws Exception {
		db = new DBInitiator("DeviceReg",
				"test/ncr/res/mobilepos/authentication/resource/datasets/"
						+ xml, DATABASE.RESMaster);
    }

	@Given("AUT_DEVICES is empty")
	public final void emptyAutDevices() throws Exception {
		new DBInitiator("DeviceReg", "test/ncr/res/mobilepos/authentication/"
				+ "resource/datasets/AUTH_DEVICES_Empty.xml",
				DATABASE.RESMaster);
	}

	@When("I register a device of companyid{$companyid} storeid{$storeid} deviceid{$deviceid} devicename{$devicename} passcode{$passcode} udid{$udid} uuid{$uuid} signstatus{$signstatus} signtid{$signtid} signactivationkey{$signactivationkey}")
	public final void registerDevice(final String companyid,
			final String storeid, final String deviceid,
			final String devicename, final String passcode, final String udid,
			final String uuid, final int signstatus, final String signtid,
			final String signactivationkey) {
		result = registrationresource.registerDevice(companyid, storeid,
				deviceid, devicename, passcode, udid, uuid, signstatus,
				signtid, signactivationkey);
		isDeregistrationResult = false;
	}

    @Then("result should be $result")
    public final void validateResult(final int result) {
    	if(isDeregistrationResult)	assertEquals(result, this.deregistrationresult.getNCRWSSResultCode());
    	else assertEquals(result, this.result.getNCRWSSResultCode());
    }

    @Then("the device data returned should be: $expected")
    public final void checkDeviceData(ExamplesTable expected) {
        Map<String, String> expectedDevice = expected.getRow(0);
        assertThat("DeviceStatus data : storeid",
                result.getStoreID(), 
                is(equalTo(expectedDevice.get("storeid"))));
        assertThat("DeviceStatus data : storename",
                result.getStoreName(), 
                is(equalTo(expectedDevice.get("storename"))));
        assertThat("DeviceStatus data : deviceid",
                result.getTerminalID(), 
                is(equalTo(expectedDevice.get("deviceid"))));
        assertThat("DeviceStatus data : devicename",
                result.getDeviceName(), 
                is(equalTo(expectedDevice.get("devicename"))));        
    }
    
    /**
     * Then Step: Compare the existing data in the database.
     * @param expecteditems The expected items in the database.
     * @throws DataSetException The Exception thrown when test fail.
     */
	@Then("the following authenticated devices in the database should be: $expecteditems")
	public final void theFollowingItemsInTheDatabaseShouldBe(
			final ExamplesTable expecteditems) throws DataSetException {
		List<Map<String, String>> expectedItemRows = expecteditems.getRows();
		ITable actualItemRows = db.getTableSnapshot("AUT_DEVICES");

		Assert.assertEquals("Compare that the number "
				+ "of rows in Items are exact: ", expecteditems.getRowCount(),
				actualItemRows.getRowCount());
		int i = 0;
		for (Map<String, String> expItem : expectedItemRows) {
			Assert.assertEquals("Compare the storeid row " + i + ": ",
					expItem.get("storeid"),
					actualItemRows.getValue(i, "StoreId").toString());
			Assert.assertEquals("Compare the terminalid row " + i + ": ",
					expItem.get("terminalid"),
					actualItemRows.getValue(i, "TerminalId").toString());
			Assert.assertEquals("Compare the state row " + i + ": ", expItem
					.get("state"), actualItemRows.getValue(i, "State")
					.toString());
			i++;
		}
	}
    
}
