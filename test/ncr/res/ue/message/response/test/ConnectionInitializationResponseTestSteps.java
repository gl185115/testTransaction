package ncr.res.ue.message.response.test;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.response.ConnectionInitializationResponse;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ConnectionInitializationResponseTestSteps extends Steps{
    private ConnectionInitializationResponse resp = null;
    private String message = "";
    private String [] multiple = {"004600000031000000009300Store000031      1.0  20",
                                        "004600000031000000009200Store000031      1.0  20"};
    
    @When("I create connection initialization response with $messageType and ($message) expect ($expected)")
    public void createConnectionInitializationResponse(int messageType, String message, String expected) throws MessageException{
        
        try {
            resp = new ConnectionInitializationResponse(messageType, message);
            if(!expected.equals("success")){
                throw new MessageException("test fail - expected fail");
            }
        } catch (MessageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e.getMessage());
            if(expected.equals("success")){
                throw new MessageException("test fail - expected success");
            } else {
                String errorMessage = e.getMessage();
                assertThat(errorMessage, is(equalTo(expected)));
            }
        }
    }
    
    @Then("The return should contain ($messageType, $responseFlag, $locationCode, $protocolVersion, $protocolBuild)")
    public void validateMessage(int messageType, int responseFlag, String locationCode, String protocolVersion, String protocolBuild){
        assertThat(resp.getMessageType(), is(equalTo(messageType)));
        assertThat(resp.getResponseFlag(), is(equalTo(responseFlag)));
        assertThat(resp.getLocationCode(), is(equalTo(locationCode)));
        assertThat(resp.getProtocolVersion(), is(equalTo(protocolVersion)));
        assertThat(resp.getProtocolBuild(), is(equalTo(protocolBuild)));
    }
    
    @BeforeScenario
    public final void SetUpClass()
    {
        //By default Company ID is empty
        GlobalConstant.setCorpid("");
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDownClass(){
        Requirements.TearDown();
    }
}
