package ncr.res.ue.message.response.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.response.GS1CouponResponse;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class GsOneCouponResponseTestSteps extends Steps{
    private GS1CouponResponse couponResp = null;

    @BeforeScenario
    public final void SetUpClass() {
        GlobalConstant.setCorpid("");
        Requirements.SetUp();
    }

    @AfterScenario
    public final void TearDownClass() {
        Requirements.TearDown();
    }

    @When("I create gs1 coupon response with ($messageType) and ($message) expect ($expected)")
    public void createResumeResponse(int messageType, String message,
            String expected) throws MessageException {
        try {
            couponResp = new GS1CouponResponse(messageType, message);
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
        assertThat(couponResp.getMessageType(), is(equalTo(messageType)));
        assertThat(couponResp.getResponseFlag(), is(equalTo(responseFlag)));
    }
}


