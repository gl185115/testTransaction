package ncr.res.mobilepos.authentication.resource.test;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.authentication.resource.AuthenticationResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class AuthenticationResourceSteps extends Steps {
    private AuthenticationResource authresource;
    private DeviceStatus result;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown() {
        Requirements.TearDown();
    }
    
    @Given("an Authentication Resource")
    public final void createResource() {
    	ServletContext servletContext = Requirements.getMockServletContext();
        authresource = new AuthenticationResource();
		new DBInitiator(
				"AuthenticationResource",
				"test/ncr/res/mobilepos/authentication/resource/test/insertAuthResourceDevices.xml",
				DATABASE.RESMaster);
        authresource.setContext(servletContext);
    }
    
    @Then ("the result should be $expected")
    public final void checkResult(final int expected) {
        assertThat(result.getNCRWSSResultCode(),is(equalTo(expected)));
    }
    
    @When ("I authenticate the device: $corpid, $storeid, $deviceid, $uuid, $udid")
    public final void authenticateDevice(final String corpid, final String storeid, String deviceid, String uuid, String udid) {
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
     
}
