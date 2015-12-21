package ncr.res.ue.message.response.test;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.response.ItemSVResponse;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class ItemStoreValueResponseSteps extends Steps {
    private ItemSVResponse resp = null;

    @When("I create store value response with $messageType and ($message) expect ($expected)")
    public void createItemStoreValueResponse(int messageType, String message, String expected) throws MessageException{
        try {
            resp = new ItemSVResponse(messageType, message);
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
