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
import ncr.res.mobilepos.helper.XmlSerializer;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class AuthenticationResourceActivationKeySteps extends Steps{
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
        new DBInitiator("AuthenticationResource",
                "test/ncr/res/mobilepos/authentication/resource/test/"
                + "insertAuthResourceDevices.xml", DATABASE.RESMaster);
        authresource.setContext(servletContext);
    }
    
    @When("I authenticate the device: corpid $corpid, storeid $storeid, terminalid $terminalid, uuid $uuid, udid $udid")
    public final void authenticateDevice(final String corpid, final String storeid, String terminalid, String uuid, String udid) {
        if(terminalid.equals("null")) {
            terminalid = null;
        }
        if(uuid.equals("null")) {
            uuid = null;
        }
        if(udid.equals("null")) {
            udid = null;
        }
        result = authresource.authenticateDevice(corpid, storeid, terminalid, udid, uuid);
    }
    
    @Then("the result should be $expected")
    public final void checkResult(final int expected) {
        assertThat(result.getNCRWSSResultCode(),is(equalTo(expected)));
    }
    

    @Then("the signStatus should be $expected")
    public final void checkSignStatus(final int expected) {
        assertThat(result.getSignStatus(),is(equalTo(expected)));
    }

    @Then("the activationKey.signActivationKey should be $expected")
    public final void checkSignActivationKey(String expected) {
    	if (expected.equals("empty")) {
			expected = null;
		}
        assertThat(null == result.getActivationKey() ? result.getActivationKey():
        	result.getActivationKey().getActivationKey(), is(equalTo(expected)));
    }
    
    @Then ("the activationKey.signTid should be $expected")
    public final void checkSignTid(String expected) {
        if(expected.equals("empty")){
            expected = null;
        }
        assertThat(null == result.getActivationKey() ? result.getActivationKey():
        	result.getActivationKey().getSignatureTid(), is(equalTo(expected)));
    }
    
    @Then("xml string should be $xml")
    public final void seriallize(final String xml) throws Exception {
        XmlSerializer<DeviceStatus> posLogRespSrlzr =
            new XmlSerializer<DeviceStatus>();
        String actual = posLogRespSrlzr.marshallObj(DeviceStatus.class,
                result, "UTF-8");
        assertEquals(xml, actual);
    }
}
