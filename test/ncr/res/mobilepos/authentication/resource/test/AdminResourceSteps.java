package ncr.res.mobilepos.authentication.resource.test;

import java.sql.SQLException;

import junit.framework.Assert;

import org.dbunit.DatabaseUnitException;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import ncr.res.mobilepos.authentication.dao.AuthAdminDao;
import ncr.res.mobilepos.authentication.dao.AuthDeviceDao;
import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.authentication.resource.AdminResource;
import ncr.res.mobilepos.authentication.resource.AuthenticationResource;
import ncr.res.mobilepos.authentication.resource.RegistrationResource;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

public class AdminResourceSteps extends Steps{
    private AdminResource adminresource;
    private ResultBase result;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("an Admin Resource")
    public final void createResource() throws Exception{
        adminresource = new AdminResource();        
        adminresource.setContext(Requirements.getMockServletContext());
        result = new ResultBase();
        
        DBInitiator dbinit = new DBInitiator("AdminResourceSteps",
                "test/ncr/res/mobilepos/authentication/resource/"
                + "test/forAdminResourceTest.xml", DATABASE.RESMaster);
    }
    
    @Given("the adminkey is set to $adminkey")
    public final void setAdminKey(final String adminkey) throws Exception {
        DBInitiator dbinit = new DBInitiator("AdminResourceSteps", "test/ncr/res/mobilepos/authentication/resource/"
              + "datasets/PRM_SYSTEM_CONFIG_AdminKey_" + adminkey + ".xml", DATABASE.RESMaster);
//        dbinit.ExecuteOperationNoKey(DatabaseOperation.UPDATE, "keyid",
//                "test/ncr/res/mobilepos/authentication/resource/"
//                + "datasets/PRM_SYSTEM_CONFIG_AdminKey_" + adminkey + ".xml");
    }
    
    @Given("the passcode is set to $passcode")
    public final void setPasscode(final String passcode) throws Exception {
        DBInitiator dbinit = new DBInitiator("AdminResourceSteps", "test/ncr/res/mobilepos/authentication/resource/datasets/"
                + "PRM_SYSTEM_CONFIG_Passcode_" + passcode + ".xml", DATABASE.RESMaster);
//        dbinit.ExecuteOperationNoKey(DatabaseOperation.UPDATE, "keyid",
//                "test/ncr/res/mobilepos/authentication/resource/datasets/"
//                + "PRM_SYSTEM_CONFIG_Passcode_" + passcode + ".xml");
    }
    
    @Then ("I should have an Admin Resource")
    public final void checkResource()
    {
        Assert.assertNotNull(adminresource);
    }
    
    @When ("I set the adminKey to $newKey using $currentKey")
    public final void setAdminKey(String newKey, String currentKey)
    {
        if(newKey.equals("null")) {
            newKey = null;
        }
        if(currentKey.equals("null")) {
            currentKey = null;
        }
        
        result = adminresource.setAdminKey(currentKey, newKey);
    }
    
    @When ("I set the passcode to $newpasscode using $adminKey")
    public final void setPasscode(String newpasscode, String adminKey)
    {
        if(newpasscode.equals("null")) {
            newpasscode = null;
        }
        if(adminKey.equals("null")) {
            adminKey = null;
        }
        
        result = adminresource.setPasscode(newpasscode, "20", adminKey);
    }
    
    @When ("I remove a device $deviceid using $adminKey")
    public final void removeDevice(String deviceid, String adminKey)
    {
        if(deviceid.equals("null")) {
            deviceid = null;
        }
        if(adminKey.equals("null")) {
            adminKey = null;
        }
        
        result = adminresource.removeDevice(deviceid, adminKey);
    }
    
    @When ("I check if device $corpid, $storeid, $deviceid, $uuid,"
            + " $udid exists")
    public final void isDeviceExist(final String corpid, final String storeid,
            final String deviceid, final String uuid,
            final String udid) throws DaoException
    {
        result.setNCRWSSResultCode(1);
        AuthDeviceDao add = new AuthDeviceDao();
        if(add.isDeviceExisting(corpid, storeid, deviceid, true)) {
            result.setNCRWSSResultCode(0);
        }
    }
    
    @When ("I validate the passcode $passcode")
    public final void checkPasscode(final String passcode) throws DaoException
    {
        result.setNCRWSSResultCode(1);
        
        AuthAdminDao aad = new AuthAdminDao();
        if(0 == aad.validatePasscode(passcode)) {
            result.setNCRWSSResultCode(0);
        }
    }
    
    @Then ("the result should be $expected")
    public final void checkResult(final int expected)
    {
        assertThat(result.getNCRWSSResultCode(),is(equalTo(expected)));
    }
    
    
    

    
}
