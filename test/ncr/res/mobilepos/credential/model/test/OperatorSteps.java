package ncr.res.mobilepos.credential.model.test;

import static org.junit.Assert.assertEquals;

import ncr.res.mobilepos.credential.model.Operator;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.steps.Steps;

/**
 * Model Test Steps for Operator.
 *
 */
public class OperatorSteps extends Steps {
    /**
     * Holds operator.
     */
    private Operator operator;

    /**
     * Given Step : an operator model.
     */
    @Given("an operator model")
    public final void createOperator() {
        operator = new Operator();
    }

    /**
     * Given Then : Its Operator number.
     * @param operNum   Operator number
     */
    @Then("its operator number is {$operNum}")
    public final void itsOperNum(final String operNum) {
        operator.setOperatorNo(operNum);
        assertEquals(operNum, operator.getOperatorNo());
    }

    /**
     * Given Then : Its name.
     * @param name  Operator's name.
     */
    @Then("its name is {$name}")
    public final void itsName(final String name) {
        operator.setName(name);
        assertEquals(name, operator.getName());
    }

    /**
     * Given Then : Its Response.
     * @param response  Response code.
     */
    @Then("its response is {$response}")
    public final void itsResponse(final String response) {
        operator.setResponse(response);
        assertEquals(response, operator.getResponse());
    }

    /**
     * Given Then : Its Sign On At.
     * @param signOnAt  The Date time the operator sign on.
     */
    @Then("its Sign On At is {$signOnAt}")
    public final void itsSignOnAt(final String signOnAt) {
        operator.setSignOnAt(signOnAt);
        assertEquals(signOnAt, operator.getSignOnAt());
    }

    /**
     * Given Then : Its Date.
     * @param date  The Date time the operator sign on.
     */
    @Then("its date is {$date}")
    public final void itsDate(final String date) {
        operator.setDate(date);
        assertEquals(date, operator.getDate());
    }

    /**
     * Given Then : Its passcode.
     * @param passcode  Operator's passcode.
     */
    public final void itsPasscode(final String passcode) {
        operator.setPasscode(passcode);
        assertEquals(passcode, operator.getPasscode());
    }
}