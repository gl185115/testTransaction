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
 * Test class for update employee service in CredentialResource.
 *
 */
@SuppressWarnings("deprecation")
public class UpdateEmployeeResourceSteps extends Steps {
    /**
     * CredentialResource instance to invoke updateEmployee method.
     */
    private CredentialResource credResource = null;
    /**
     * ViewEmployee instance that holds employee data and resultcode.
     */
    private ViewEmployee viewEmployee = null;

    /**
     * A method to execute before each scenario.
     */
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        new DBInitiator("AuthenticationResource",
                "test/ncr/res/mobilepos/credential/resource/test/"
                        + "MST_USER_CREDENTIALS_forUpdateEmployee.xml", DATABASE.RESMaster);
    }

    /**
     * A method to execute after each scenario.
     */
    @AfterScenario
    public final void tearDown() {
        Requirements.TearDown();
    }

    /**
     * Given a credential resource.
     */
    @Given("a CredentialResource")
    public final void givenCredentialResource() {
        credResource = new CredentialResource();
    }

    /**
     * Update employee details.
     *
     * @param storeID
     *            The storeid where the employee belongs.
     * @param employee
     *            The id of the operator requesting the update.
     * @param employeeID
     *            The operator number.
     * @param emmployeeJSON
     *            The JSON object of Employee.
     */
    @When("$employee updates employee details in {$storeID} of "
            + "{$empID} with [$employeeJSON]")
    public final void updateEmployee(final String employee,
            final String storeID,
            final String employeeID, final String emmployeeJSON) {
        viewEmployee = credResource.updateEmployee(storeID, employee,
                employeeID, emmployeeJSON);
    }

    /**
     * Tests actual and expected employee data.
     *
     * @param newEmpInfo
     *            The expected employee data.
     */
    @Then("I should get new employee details: $newEmpInfo")
    public final void testResult(final ExamplesTable newEmpInfo) {

        Employee employee = viewEmployee.getEmployee();   
        Assert.assertNotNull(employee);
        Assert.assertEquals(0, viewEmployee.getNCRWSSResultCode());
        Assert.assertEquals(newEmpInfo.getRow(0).get("operatorno").trim(),
                String.valueOf(employee.getNumber().trim()));
        Assert.assertEquals(employee.toString(), newEmpInfo.getRow(0).get("passcode"),
        		((employee.getPasscode()== null) ? "null" : String.valueOf(employee.getPasscode().trim())));
        Assert.assertEquals(newEmpInfo.getRow(0).get("name").trim(),
        		((employee.getName()== null) ? "null" : String.valueOf(employee.getName().trim())));
        Assert.assertEquals(newEmpInfo.getRow(0).get("type").trim(),
        		((employee.getOperatorType() == null) ? "null" : String.valueOf(employee.getOperatorType().trim())));
                
    }

    /**
     * Tests actual and expected resultcode.
     *
     * @param result
     *            The expected resultcode.
     */
    @Then("I should get resultcode equal to {$resultCode}")
    public final void testResultCode(final int result) {
        Assert.assertEquals(result, viewEmployee.getNCRWSSResultCode());
    }
    /**
     * Sign on.
     *
     * @param operator
     *            The operator
     * @param passcode
     *            The passcode
     * @param terminal
     *            The terminal id
     */
    @When("signing on operator:{$operator} passcode:{$passcode} terminal:{$terminal}")
    public final void signOn(String operator, String companyid,
            String passcode,
            String terminal) {
                credResource.requestSignOn(operator, companyid,
                        passcode, terminal, false);
    }
}
