package ncr.res.mobilepos.credential.resource.test;

import junit.framework.Assert;
import ncr.res.mobilepos.credential.model.Employee;
import ncr.res.mobilepos.credential.model.ViewEmployee;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

/**
 * view employee steps class.
 */
@SuppressWarnings("deprecation")
public class ViewEmployeeResourceSteps extends Steps {
    /**
     * CredentialResource instance. WebService to call viewEmployee.
     */
    private CredentialResource credentialResource = null;
    /**
     * ViewEmployee instance. Contains Employee data.
     */
    private ViewEmployee viewEmployee = null;

    /**
     * A method to be executed before each scenario.
     */
    @BeforeScenario
    public final void setUpClass() {
        Requirements.SetUp();
        new DBInitiator("CredentialResourceSteps",
                "test/ncr/res/mobilepos/credential/resource/test/"
                        + "MST_USER_CREDENTIALS_forViewEmployee.xml", DATABASE.RESMaster);
    }

    /**
     * A method to be executed after each scenario.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }

    /**
     * Creates CredentialResource instance.
     */
    @Given("a CredentialResource")
    public final void givenCredentialResource() {
        credentialResource = new CredentialResource();
    }

    /**
     * Invokes view employee in CredentialResource.
     *
     * @param operatorID
     *            The operator ID.
     * @param storeID
     *            The retail store ID.
     */
    @When("I view employee details of {$operatorID} in {$storeID}")
    public final void viewEmployeeDetails(final String operatorID,
            final String storeID) {
        viewEmployee = credentialResource.viewEmployee(storeID, operatorID);
    }

    /**
     * Tests expected and actual resultcode.
     *
     * @param result
     *            The expected result.
     */
    @Then("I should get resultcode equal to {$result}")
    public final void testResultCode(final int result) {
        Assert.assertEquals(result, viewEmployee.getNCRWSSResultCode());
    }


    /**
     * Tests actual and expected employee details.
     *
     * @param expectedEmployee
     *            The ExamplesTable instance.
     */
    @Then("I should get employee details: $employeeData")
    public final void testEmployeeData(final ExamplesTable expectedEmployee) {
        Employee actualEmployee = viewEmployee.getEmployee();

        Assert.assertNotNull(viewEmployee.getEmployee());
        Assert.assertEquals("Compare the Operator's Number ", expectedEmployee
                .getRow(0).get("operatorno"), String.valueOf(actualEmployee
                .getNumber()));
        Assert.assertEquals("Compare the Operator's Name ", expectedEmployee
                .getRow(0).get("operatorname"), String.valueOf(actualEmployee
                .getName()));
        Assert.assertEquals("Compare the Operator's Type ", expectedEmployee
                .getRow(0).get("operatortype"), String.valueOf(actualEmployee
                .getOperatorType()));
        Assert.assertEquals("Compare the Operator's WorkstationID ",
                expectedEmployee.getRow(0).get("terminalid"),
                String.valueOf(actualEmployee.getWorkStationID()));
        Assert.assertEquals("Compare the Operator's RetailStoreID ",
                expectedEmployee.getRow(0).get("storeid"),
                String.valueOf(actualEmployee.getRetailStoreID()));
    }
}