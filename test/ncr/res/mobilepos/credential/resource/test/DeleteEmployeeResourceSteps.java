package ncr.res.mobilepos.credential.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

/**
 * Delete employee steps class.
 *
 */
public class DeleteEmployeeResourceSteps extends Steps {
    /**
     * CredentialResource instance.
     */
    private CredentialResource credResource;
    /**
     * ResultBase instance.
     */
    private ResultBase resultBase;

    /**
     * Invokes before execution of each scenario.
     */
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }

    /**
     * Invokes after execution of each scenario.
     */
    @AfterScenario
    public final void tearDown() {
        Requirements.TearDown();
    }

    /**
     * A Given step, instantiating CredentialResource class.
     */
    @Given("I have a Credential Resource")
    public final void iHaveACredentialResource() {
        credResource = new CredentialResource();      
    }

    /**
     * A Then step, tests actual and expected result.
     *
     * @param result The expected result.
     */
    @Then("the result should be {$Result}")
    public final void checkResults(final int result) {
        assertThat(resultBase.getNCRWSSResultCode(), is(equalTo(result)));
    }

    /**
     * A Given step, initiating DBInitiator class with new data.
     */
    @Given("a Credential Table")
    public final void emptyTable() {
        @SuppressWarnings("unused")
        DBInitiator dbinit = new DBInitiator("AuthenticationResource",
                "test/ncr/res/mobilepos/credential/resource/test/"
                        + "MST_USER_CREDENTIALS_forDelete.xml", DATABASE.RESMaster);
    }

    /**
     * A When step, invokes deleteEmployee method.
     *
     * @param storeid   The store number where the operator belong.
     * @param opid      The operator number.
     */
    @When("I delete an employee ($storeid, $opid)")
    public final void deleteEmployee(final String storeid, final String opid) {
        resultBase = credResource.deleteEmployee(storeid, opid);
    }

}
