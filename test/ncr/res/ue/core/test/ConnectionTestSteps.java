package ncr.res.ue.core.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.ue.TestServer;
import ncr.res.ue.core.Connection;
import ncr.res.ue.message.response.ConnectionInitializationResponse;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;


public class ConnectionTestSteps extends Steps{
    private Connection connection = null;
    private ServletContext context = null;
    private ConnectionInitializationResponse response = null;
    private TestServer testServer;
    
    @BeforeScenario
    public final void setUp() throws Exception {
        Requirements.SetUp();
        GlobalConstant.setCorpid("000031");
        testServer = new TestServer();
        testServer.runTestServer();
    }
    
    @AfterScenario
    public final void TearDown() {
        Requirements.TearDown();
    }
    
    @When("I create a connection")
    public final void createConnection() throws Exception {
        context = TestServer.getMockServletForUeParameters(2000);
        System.out.println((String) context.getAttribute("UeIoServerAddress"));
        connection = new Connection(context);
    }
    
    @Then("I should be able to do connection initialization")
    public final void connectionInitialization() throws Exception {
        response = connection.initializeUeConnection();
    }
    
    @Then("response flag should be {$code}")
    public final void validateResponseCode(int code) {
        assertThat(code,is(equalTo(this.response.getResponseFlag())));
    }
    
    @Then("location code should be {$locationCode}")
    public final void validateLocationCode(String locationCode) {
        assertThat(locationCode,is(equalTo(this.response.getLocationCode())));
    }
    
    @Then("protocol version should be {$protocolVersion}")
    public final void validateProtocolVersion(String protocolVersion) {
        assertThat(protocolVersion,is(equalTo(this.response.getProtocolVersion())));
    }
    
    @Then("protocol build should be {$protocolBuild}")
    public final void validateProtocolBuild(String protocolBuild) {
        assertThat(protocolBuild,is(equalTo(this.response.getProtocolBuild())));
    }
    
    @Then("close the connection")
    public final void closeConnection() {
        connection.closeConnection();
        TestServer.stop = true;
    }
}
