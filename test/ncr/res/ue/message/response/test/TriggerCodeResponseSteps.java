package ncr.res.ue.message.response.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.response.TriggerCodeResponse;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class TriggerCodeResponseSteps extends Steps {
    private TriggerCodeResponse resp = null;
    
    @When("I create trigger code response with $messageType and ($message) expect ($expected)")
    public void createTenderEntryResponse(int messageType, String message, String expected) throws MessageException{
        try {
            resp = new TriggerCodeResponse(messageType, message);
            
            /*
             * When the block of code pass here. It means a success creation of 
             * the class is implemented. If the expected is not "success",
             * then constructor is expected to fail.
             */
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
                Assert.assertEquals("Compare the Error Message", expected, errorMessage);
            }
        }
    }

    
    @Then("The return should contain ($messageType, $responseFlag)")
    public void validateMessage(int messageType, int responseFlag){
        Assert.assertEquals("Expect the message Type", messageType, resp.getMessageType());
        Assert.assertEquals("Expect the Response Flag", responseFlag, resp.getResponseFlag());
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
