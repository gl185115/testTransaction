package ncr.res.mobilepos.credential.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.credential.model.Employee;
import ncr.res.mobilepos.credential.model.Employees;
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
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

/**
 * Steps class for createEmployee.
 */
@SuppressWarnings("deprecation")
public class CreateEmployeeResourceSteps extends Steps {
    /**
     * CredentialResource instance.
     */
    private CredentialResource credResource;
    /**
     * ResultBase instance.
     */
    private ResultBase resultBase;

    /**
     * List of all employees.
     */
    private Employees employees = null;

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
    @Given("I have a Credential Resource")
    public final void iHaveACredentialResource() {
        credResource = new CredentialResource();

    }

    /**
     * A Then step, tests actual and expect result code.
     *
     * @param result
     *            The expected result code.
     */
    @Then("the result should be {$Result}")
    public final void checkResult(final int result) {
        assertThat(resultBase.getNCRWSSResultCode(), is(equalTo(result)));
    }

    /**
     * A Given step, initializes new dataset.
     */
    @Given("an empty Credential Table")
    public final void emptyTable() {
        @SuppressWarnings("unused")
        DBInitiator dbinit = new DBInitiator("AuthenticationResource",
                "test/ncr/res/mobilepos/credential/resource/test/"
                        + "MST_USER_CREDENTIALS_Empty.xml", DATABASE.RESMaster);
    }

    /**
     * A When step, invokes createEmployee method.
     *
     * @param storeid
     *            The storeid of the employee.
     * @param operid
     *            The operator number.
     * @param jsonString
     *            The json string of employee.
     */
    @When("I add an employee ($storeid, $operid) ($jsonString)")
    public final void addEmployee(final String storeid, final String operid,
            final String jsonString) {
        String storeIDTemp = storeid;
        if (storeIDTemp.equals("empty")) {
            storeIDTemp = "";
        }
        if (storeIDTemp.equals("null")) {
            storeIDTemp = null;
        }

        String operatorIDTemp = operid;
        if (operatorIDTemp.equals("empty")) {
            operatorIDTemp = "";
        }
        if (operatorIDTemp.equals("null")) {
            operatorIDTemp = null;
        }

        String jsonStringTemp = jsonString;
        if (jsonStringTemp.equals("empty")) {
            jsonStringTemp = "";
        }
        if (jsonStringTemp.equals("null")) {
            jsonStringTemp = null;
        }

        resultBase = credResource.createEmployee(storeIDTemp, operatorIDTemp,
                jsonStringTemp);
    }

    /**
     * List employees.
     *
     * @param retailStoreID
     *            the retail store id
     */
    @When("I list all the employees of retailstoreid{$retailStoreID}")
    public final void listEmployees(final String retailStoreID) {
    	employees = credResource.listOperators(retailStoreID, null, null, -1); //-1 means list All        
    }
    
  
    /**
     * Test employee data.
     *
     * @param expectedEmployees
     *            the expected employees
     */
    @Then("I should get employees: [$expectedEmployees]")
    public final void testEmployeeData(final ExamplesTable expectedEmployees) {
        List<Employee> actualEmployees = employees.getEmployeeList();
        Assert.assertNotNull(actualEmployees);
        Assert.assertEquals("Must exact number of Employees from the List",
                expectedEmployees.getRowCount(), actualEmployees.size());

        if (actualEmployees.size() == expectedEmployees.getRowCount()
                && actualEmployees.size() > 0) {
            int i = 0;
            for (Map<String, String> expectedEmployee : expectedEmployees
                    .getRows()) {
                Employee actualEmployee = employees.getEmployeeList().get(i);
                Assert.assertEquals("Compare the Operator's Number ",
                        expectedEmployee.get("operatorno"),
                        actualEmployee.getNumber());
                Assert.assertEquals("Compare the Operator's Name ",
                        expectedEmployee.get("operatorname"),
                        actualEmployee.getName());
                i++;
            }
        }
    }
    
    /**
     *  A When step, invokes deleteEmployee method.
     *  
     *  @param storeid
     *          StoreId of the employee
     *  @param opid
     *          OperatorNo of the employee
     * */
    @When("I delete an employee ($storeid, $opid)")
    public final void deleteEmployee(final String storeid, final String opid) {
        resultBase = credResource.deleteEmployee(storeid, opid);
    }

}
