package ncr.res.ue.message.response.test;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.response.SuspendTransactionResponse;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class SuspendTransactionResponseTestSteps extends Steps {
    private SuspendTransactionResponse resp = null;

    @BeforeScenario
    public final void SetUpClass() {
        GlobalConstant.setCorpid("");
        Requirements.SetUp();
    }

    @AfterScenario
    public final void TearDownClass() {
        Requirements.TearDown();
    }

    @When("I create suspend transaction response with $messageType and ($message) expect ($expected)")
    public void createEndTransactionResponse(int messageType, String message,
            String expected) throws MessageException {
        try {
            resp = new SuspendTransactionResponse(messageType, message);
            if (!expected.equals("success")) {
                throw new MessageException("test fail - expected fail");
            }
        } catch (MessageException e) {
            e.printStackTrace();
            System.out.println(e.getMessage());
            if (expected.equals("success")) {
                throw new MessageException("test fail - expected success");
            } else {
                String errorMessage = e.getMessage();
                assertThat(errorMessage, is(equalTo(expected)));
            }
        }
    }

    @Then("The return should contain ($messageType, $responseFlag)")
    public void validateMessage(int messageType, int responseFlag) {
        assertThat(resp.getMessageType(), is(equalTo(messageType)));
        assertThat(resp.getResponseFlag(), is(equalTo(responseFlag)));
    }
}
