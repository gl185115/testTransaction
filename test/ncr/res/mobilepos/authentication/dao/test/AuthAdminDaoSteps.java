package ncr.res.mobilepos.authentication.dao.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ncr.res.mobilepos.authentication.dao.AuthAdminDao;
import ncr.res.mobilepos.authentication.dao.AuthDeviceDao;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.dbunit.DatabaseUnitException;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

import java.sql.SQLException;
import java.util.Map;

import junit.framework.Assert;


public class AuthAdminDaoSteps extends Steps {
    private AuthAdminDao testAAD;
    private String result = "";
    private int res;
    private DBInitiator dbinit;
    
    
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
    
    @Given ("an AuthAdminDao")
    public final void givenAuthDeviceDao() throws DaoException
    {
        testAAD = new AuthAdminDao();  
        dbinit = new DBInitiator("AuthAdminDaoSteps", DATABASE.RESMaster);
    }
    
    @Then ("I should have an AuthAdminDao")
    public final void thenHaveAuthAdminDao()
    {
        Assert.assertNotNull(testAAD);
    }
    
    @Given("entries for in AUT_DEVICES")
    public final void initdatasetsDpt()
    throws Exception {
        dbinit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/authentication/dao/test/AUT_DEVICES_forRemove.xml");
    }
    
    @Given ("I set the AuthExpiry to 20")
    public final void setValue()
    throws DatabaseUnitException, SQLException, Exception
    {
        dbinit.ExecuteOperationNoKey(DatabaseOperation.UPDATE, "keyid",
            "test/ncr/res/mobilepos/authentication/dao/test/setValidDura.xml");
    }
    
    @When ("I set the passcode to $passcode using $adminkey")
    public final void setPasscode(final String passcode, final String adminKey)
    throws DaoException
    {
        res = testAAD.setPasscode(passcode, adminKey, 20);
    }
    
    @When ("I validate passcode $passcode")
    public final void checkPasscode(final String passcode) throws DaoException
    {
        res = testAAD.validatePasscode(passcode);
    }
    
    @When ("I set the adminKey to $adminKey using $oldKey")
    public final void setAdminKey(final String adminKey, final String oldKey)
    throws DaoException
    {
        res = testAAD.setAdminKey(oldKey, adminKey);
    }
    
    @Then ("the result should be OK")
    public final void checkOK()
    {
    	System.out.println("Result " + res);
        assertThat(0, is(equalTo(res)));
    }
    
    @Then ("the result should not be OK")
    public final void checkNOTOK()
    {
    	System.out.println("Result " + res);
        assertThat(false, is(equalTo(res == 0)));
    }
    
    @Then ("the AuthExpiry should be 20")
    public final void getValue() throws DaoException
    {
        int reslt = testAAD.getActiveValidityDuration();
        
        assertThat(20,is(equalTo(reslt)));
    }
    
    @When ("I register a device: $storeid, $terminalid," +
            " $devicename, $uuid, $udid, $validity")
    public final void whenRegisterDevice(
            final String companyid, final String storeid,
            final String terminalid, final String devicename,
            final String uuid, final String udid, final int validity,
            final int signstatus, final String signtid, final String signactivationkey)
    throws DaoException
    {
        AuthDeviceDao device = new AuthDeviceDao();
        res = device.registerTerminal(companyid, storeid, terminalid, devicename, 
                        udid, uuid, signstatus, signtid, signactivationkey);
    }
    
    @When ("I remove a device $deviceno using $adminKey")
    public final void removeDevice(final String deviceno, final String adminKey)
    throws DaoException
    {
        res = testAAD.removeDevice(deviceno, adminKey);
    }
    
    @Then ("Device: $storeid, $terminalid, $udid, $uuid should exist")
    public final void thenDeviceExisting(
           final String companyid, 
           final String storeid,
           final String terminalid, final String udid,
           final String uuid) throws DaoException
    {
        AuthDeviceDao device = new AuthDeviceDao();
        assertThat(true, is(equalTo(device.isDeviceExisting(companyid, storeid, terminalid, true))));
    }
    
    @Then ("Device: $storeid, $terminalid, $udid," +
            " $uuid should not exist")
    public final void thenDeviceNotExisting(final String companyid,
            final String storeid, final String terminalid,
            final String udid, final String uuid) throws DaoException
    {
        AuthDeviceDao device = new AuthDeviceDao();
		assertThat(false,
				is(equalTo(device.isDeviceExisting(companyid, storeid, terminalid, true))));
    }
    
    @Given("that AdminKey is $key")
    public final void updateBusinessDateFlag(final String key) throws Exception{
        DBInitiator db = new DBInitiator("Prm_System_Config", DATABASE.RESMaster);
        db.ExecuteOperationNoKey(DatabaseOperation.UPDATE, "KeyId",
                "test/ncr/res/mobilepos/authentication/resource/datasets/"
                + "PRM_SYSTEM_CONFIG_AdminKey_" + key + ".xml");
    }

}
