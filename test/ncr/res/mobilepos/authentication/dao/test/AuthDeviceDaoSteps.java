package ncr.res.mobilepos.authentication.dao.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ncr.res.mobilepos.authentication.dao.AuthDeviceDao;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

import java.util.Map;

import junit.framework.Assert;


public class AuthDeviceDaoSteps extends Steps {
    private AuthDeviceDao testADD;
    private String result = "";
    
    
    @BeforeScenario
    public final void SetUpClass()
    {        
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown()
    {
        Requirements.TearDown();
    }
    
    @Given ("an AuthDeviceDao")
    public final void givenAuthDeviceDao() throws DaoException
    {
        testADD = new AuthDeviceDao();        
        
        DBInitiator dbinit = new DBInitiator("AuthDeviceDaoSteps",
                "test/ncr/res/mobilepos/authentication/resource/"
                + "datasets/AUTH_DEVICES_Empty.xml", DATABASE.RESMaster);
        DBInitiator dbinit2 = new DBInitiator("Mst_DeviceInfo", 
                "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/MST_DEVICEINFO_EMPTY.xml", DATABASE.RESMaster);
    }
    
    @When ("I test the udid : $udid")
    public final void whenIsValidUDID(final String udid)
    {
        result = "FAIL";
        if(AuthDeviceDao.isValidUDID(udid) == true) {
            result = "OK";        
        }
    }
    
    @When ("I test the uuid : $uuid")
    public final void whenIsValidUUID(final String uuid)
    {
        result = "FAIL";
        if(AuthDeviceDao.isValidUUID(uuid) == true) {
            result = "OK";        
        }
    }
    
    @When ("I register a device: $storeid, $terminalid,"
            + " $devicename, $uuid, $udid, $validity")
    public final void whenRegisterDevice(
            final String companyid,
            final String storeid, final String terminalid, 
            final String devicename, final String uuid,
            final String udid, final int validity,
            final int signstatus, final String signtid,
            final String signactivationkey)
    throws DaoException
    {
		testADD.registerTerminal(companyid, storeid, terminalid, devicename, 
		        udid, uuid, signstatus, signtid, signactivationkey);
    }
    
    @When ("I deregister a device: $storeid, $terminalid,"
            + " $uuid, $udid")
    public final void whenDeregisterDevice(
            final String storeid, final String terminalid,
            final String uuid, final String udid) throws DaoException
    {
		testADD.deregisterTerminal(storeid, terminalid);
    }
    
    @When ("I deauthenticate device: $storeid, $terminalid,"
            + " $uuid, $udid")
    public final void whenDeauthenticateDevice(final String storeid, final String terminalid,
            final String uuid, final String udid) throws DaoException
    {
        testADD.deauthenticateUser(storeid, terminalid, uuid, udid);
    }
    
    @When ("I authenticate device: $storeid,"
            + " $terminalid, $uuid, $udid")
    public final void whenAuthenticateDevice(final String storeid, final String terminalid, final String uuid,
            final String udid) throws DaoException
    {
        testADD.authenticateUser(storeid, terminalid);
    }
    
    @Then("I validate device: $storeid, $terminalid, $udid,"
            + " $uuid result should be $result")
    public final void whenValidateDevice(
            final String storeid,
            final String terminalid, final String udid, final String uuid,
            final int expectedresult)
    throws DaoException
    {
        int res = testADD.validateDevice(storeid, terminalid);
        
        assertThat(expectedresult, is(equalTo(res)));
    }
    
    @Then ("Device: $storeid, $terminalid, $udid, $uuid should exist")
    public final void thenDeviceExisting(final String companyid, final String storeid,
            final String terminalid, final String udid, final String uuid)
    throws DaoException
    {
		boolean res = testADD.isDeviceExisting(companyid, storeid, terminalid, true);        
        assertThat(true, is(equalTo(res)));
    }
    
    @Then ("Device: $deviceno status should be $status")
    public final void thenUserStatus(final String deviceno, final int status)
    throws DaoException
    {
        int res = testADD.getUserStatus(deviceno);
        
        assertThat(status, is(equalTo(res)));
    }
    
    @Then ("Device: $storeid, $terminalid, $udid,"
            + " $uuid should not exist")
    public final void thenDeviceNotExisting(final String companyid, final String storeid,
            final String terminalid, final String udid, final String uuid)
    throws DaoException
    {
		boolean res = testADD.isDeviceExisting(companyid, storeid, terminalid, true);
        assertThat(false, is(equalTo(res)));
    }
    
    @Then ("result should be $expected")
    public final void thenCheckResult(final String expected)
    {
        assertThat(expected, is(equalTo(result)));
    }
    
    @Then ("I should have an AuthDeviceDao")
    public final void thenHaveAuthDeviceDao()
    {
        Assert.assertNotNull(testADD);
    }
    

}
