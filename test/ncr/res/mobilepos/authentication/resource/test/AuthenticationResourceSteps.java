package ncr.res.mobilepos.authentication.resource.test;

import junit.framework.Assert;

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
import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.authentication.resource.AuthenticationResource;
import ncr.res.mobilepos.authentication.resource.RegistrationResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

public class AuthenticationResourceSteps extends Steps{
    private AuthenticationResource authresource;
    private DeviceStatus result;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("an Authentication Resource")
    public final void createResource(){
        authresource = new AuthenticationResource();
        DBInitiator dbinit = new DBInitiator("AuthenticationResource",
                "test/ncr/res/mobilepos/authentication/resource/test/"
                + "insertAuthResourceDevices.xml", DATABASE.RESMaster);
        authresource.setContext(Requirements.getMockServletContext());
    }
    
    @When ("I check the device status of $deviceid")
    public final void checkStatus(String deviceid)
    {
        if(deviceid.equals("null")) {
            deviceid = null;
        }
        
        result = authresource.getDeviceStatus(deviceid);
    }
    
    @Then ("the result should be $expected")
    public final void checkResult(final int expected)
    {
        //result.get
        assertThat(result.getNCRWSSResultCode(),is(equalTo(expected)));
    }
    @Then ("the devicename should be {$expected}")
    public final void checkResult( String expected)
    {
        if (expected.equals("null")) {
            expected = null;
        }
        //result.get
        assertThat(result.getDeviceName(),is(equalTo(expected)));
    }
    
    @When ("I authenticate the device: $corpid, $storeid,"
            + " $deviceid, $uuid, $udid")
    public final void authenticateDevice(
            final String corpid, final String storeid,
            String deviceid, String uuid, String udid)
    {
        if(deviceid.equals("null")) {
            deviceid = null;
        }
        if(uuid.equals("null")) {
            uuid = null;
        }
        if(udid.equals("null")) {
            udid = null;
        }
        
        result = authresource.authenticateDevice(corpid, storeid, deviceid, udid, uuid);
    }
    
    
   
    
    @When ("I deauthenticate the device: $corpid, $storeid,"
            + " $deviceid, $uuid, $udid")
    public final void deauthenticateDevice(
            final String corpid, final String storeid,
            String deviceid, String uuid, String udid)
    {
        if(deviceid.equals("null")) {
            deviceid = null;
        }
        if(uuid.equals("null")) {
            uuid = null;
        }
        if(udid.equals("null")) {
            udid = null;
        }
        
        ResultBase resbase = authresource.deauthenticateDevice(corpid,
                storeid,deviceid, udid, uuid);
        result.setNCRWSSResultCode(resbase.getNCRWSSResultCode());
    }
    
    @Given("I set up for restriction")
    public final void setUpRestriction()
    {
        DBInitiator dbinit = new DBInitiator("AuthenticationResource",
                "test/ncr/res/mobilepos/authentication/resource/test/"
                + "forRestrictionTest.xml", DATABASE.RESMaster);
    }
    
    @When("I get restriction code of $deviceno and $operator")
    public final void getRestrictioncode(String deviceno, String operator)
    {
        if(deviceno.equals("null")) {
            deviceno = null;
        }
        if(operator.equals("null")) {
            operator = null;
        }
        
        String res = authresource.getRestrictionCode(deviceno, operator);
        
        result.setNCRWSSResultCode(Integer.parseInt(res));        
    }
    
    @Then ("I should have an Authentication Resource")
    public final void checkResource()
    {
        Assert.assertNotNull(authresource);
    }
}
