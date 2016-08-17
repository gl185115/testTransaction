package ncr.res.mobilepos.credential.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import junit.framework.Assert;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.model.ResultBase;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

/**
 * Steps class for operator's changePasscode.
 */
@SuppressWarnings("deprecation")
public class ChangePasscodeResourceSteps extends Steps {
    /**
     * CredentialResource instance.
     */
    private CredentialResource credResource;
    /**
     * ResultBase instance.
     */
    private ResultBase resultBase;
    /** The dbinit. */
    private DBInitiator dbinit = null;

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
     * A Given step, initializes CredentialResource.
     */
    @Given("a Credential Resource")
    public final void aCredentialResource() {
        credResource = new CredentialResource();
    }

    /**
     * A Then step, tests actual and expect result code.
     *
     * @param result
     *            The expected result code.
     */
    @Then("I should get resultcode {$Result}")
    public final void checkResult(final int result) {
        assertThat(resultBase.getNCRWSSResultCode(), is(equalTo(result)));
    }

    /**
     * A Given step, initializes new dataset.
     *
     * @param filename
     *            the filename
     */
    @Given("an initial data from {$filename}")
    public final void emptyTable(final String filename) {
        dbinit = new DBInitiator("CredentialResource",
                "test/ncr/res/mobilepos/credential/resource/test/" + filename, DATABASE.RESMaster);
    }

    /**
     * Change old passcode.
     *
     * @param oldPasscode
     *            the old passcode
     * @param storeID
     *            the store id
     * @param operatorID
     *            the operator id
     * @param newPasscode
     *            the new passcode
     */
    @When("I change the oldpasscode{$oldPasscode} of operator{$operatorID} to newpasscode{$newPasscode}")
    public final void changeOldPasscode(String oldPasscode,
            String operatorID, String newPasscode) {
        operatorID = operatorID.equalsIgnoreCase("null") ? null : operatorID;
        newPasscode = newPasscode.equalsIgnoreCase("null") ? null : newPasscode;
        oldPasscode = oldPasscode.equalsIgnoreCase("null") ? null : oldPasscode;
        resultBase = credResource.changePasscode(operatorID,
                oldPasscode, newPasscode);
    }

    /**
     * Test updated data in MST_USER_CREDENTIAL table.
     *
     * @param expectedEmployees
     *            the expected employees
     * @throws DataSetException
     *             the exception thrown
     */
    @Then("I should have an updated table: $expectedEmployees")
    public final void testEmployeeData(final ExamplesTable expectedEmployees)
            throws DataSetException {
        ITable actualListEmployee = dbinit
                .getTableSnapshot("MST_USER_CREDENTIALS");
        for(int i=0; i < actualListEmployee.getRowCount(); i++){
            Assert.assertEquals(
                    String.valueOf(
                            expectedEmployees.getRows().get(i).get("operatorno"))
                            .trim(),
                    String.valueOf(actualListEmployee.getValue(i, "operatorno"))
                            .trim());
            Assert.assertEquals(
                    String.valueOf(expectedEmployees.getRows().get(i)
                            .get("passcode")),
                    String.valueOf(actualListEmployee.getValue(i, "passcode")));
            Assert.assertEquals(
                    String.valueOf(expectedEmployees.getRows().get(i).get("role"))
                            .trim(),
                    String.valueOf(actualListEmployee.getValue(i, "role")).trim());
            Assert.assertEquals(
                    String.valueOf(
                            expectedEmployees.getRows().get(i).get("operatorname"))
                            .trim(),
                    String.valueOf(actualListEmployee.getValue(i, "operatorname"))
                            .trim());
            Assert.assertEquals(
                    String.valueOf(
                            expectedEmployees.getRows().get(i).get("operatortype"))
                            .trim(),
                    String.valueOf(actualListEmployee.getValue(i, "operatortype"))
                            .trim());
            Assert.assertEquals(
                    String.valueOf(
                            expectedEmployees.getRows().get(i).get("terminalid"))
                            .trim(),
                    String.valueOf(actualListEmployee.getValue(i, "terminalid"))
                            .trim());
            Assert.assertEquals(
                    String.valueOf(
                            expectedEmployees.getRows().get(i).get("storeid"))
                            .trim(),
                    String.valueOf(actualListEmployee.getValue(i, "storeid"))
                            .trim());
        }
    }
}
