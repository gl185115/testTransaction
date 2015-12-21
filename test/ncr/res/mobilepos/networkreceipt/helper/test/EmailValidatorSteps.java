package ncr.res.mobilepos.networkreceipt.helper.test;

import junit.framework.Assert;

import ncr.res.mobilepos.networkreceipt.helper.EmailValidator;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class EmailValidatorSteps extends Steps{
    private EmailValidator emailValidator = null;
    private boolean isValidEmail = false;

    @BeforeScenario
    public final void SetUpClass(){
    }

    @AfterScenario
    public final void TearDown(){
    }

    @Given("an EmailValidator class")
    public final void emailValidtor(){
        emailValidator = new EmailValidator();
    }
    @When("I validate email address {$email}")
    public final void validateEmail(final String email){
        isValidEmail = emailValidator.validate(email);
    }
    @Then("It should be {$valid}")
    public final void testEmailValidation(String expectedStr){
        boolean expected = expectedStr.equals("valid") ? true: false;
        Assert.assertEquals(expected, isValidEmail);
    }
}
