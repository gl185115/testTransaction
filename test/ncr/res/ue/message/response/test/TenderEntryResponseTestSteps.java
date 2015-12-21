package ncr.res.ue.message.response.test;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.response.ConnectionInitializationResponse;
import ncr.res.ue.message.response.ItemEntryResponse;
import ncr.res.ue.message.response.TenderEntryResponse;

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

public class TenderEntryResponseTestSteps extends Steps{
    private TenderEntryResponse resp = null;
    private String message = "";
    private String [] multiple = {"002200000031000000009300",
                                        "002200000031000000000300"};
    
    @When("I create tender entry response with $messageType and ($message) expect ($expected)")
    public void createTenderEntryResponse(int messageType, String message, String expected) throws MessageException{
        try {
            resp = new TenderEntryResponse(messageType, message);
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

    
    @Then("The return should contain ($messageType, $responseFlag)")
    public void validateMessage(int messageType, int responseFlag){
        assertThat(resp.getMessageType(), is(equalTo(messageType)));
        assertThat(resp.getResponseFlag(), is(equalTo(responseFlag)));
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
