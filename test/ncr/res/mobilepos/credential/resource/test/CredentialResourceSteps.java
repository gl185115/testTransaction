package ncr.res.mobilepos.credential.resource.test;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.credential.model.Employee;
import ncr.res.mobilepos.credential.model.Employees;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

/**
 * Steps class for CredentialResource.
 *
 */
@SuppressWarnings("deprecation")
public class CredentialResourceSteps extends Steps {
    /**
     * CredentialResource instance.
     */
    private CredentialResource testCredentialResource;
    /**
     * DBInitiator instance.
     */
    private DBInitiator dbInit;
    /**
     * ResultBase instance.
     */
    private ResultBase actualRsBase;
    /**
     * List of Employee.
     */
    private Employees actualEmployeeTransactionList;
    /**
     * Xml filename that contains database data.
     */
    private String datasetpath = "test/ncr/res/mobilepos/credential/";

    /**
     * Invokes before execution of each scenario.
     */
    @BeforeScenario
    public final void setUpClass() {
        Requirements.SetUp();
        dbInit = new DBInitiator("CredentialResourceSteps", DATABASE.RESMaster);
        testCredentialResource = new CredentialResource();
    }

    /**
     * Invokes after execution of each scenario.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }

    /**
     * A Given step, initialize database raw data.
     *
     * @param dataset
     *            The xml filename.
     */
    @Given("a table entry named {$dataset} in the database")
    public final void aTableEntryNamedInTheDatabase(final String dataset) {
        try {
            dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, datasetpath
                    + dataset);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Setting the Database"
                    + " table for MST_USER CREDENTIALS failed");
        }
    }

    /**
     * A When step, invokes listAllOperators method.
     *
     * @param retailStoreid
     *            The retail storeid where the employees belong.
     */
    @When("a request to list all the operators with"
            + " RetailStoreID{$retailStoreid}")
    public final void aRequestToListAllTheOperatorsWithRetailStoreID(
            final String retailStoreid) {
        actualEmployeeTransactionList = testCredentialResource        		
                .listOperators(retailStoreid, null, null, -1);//-1 means list All

        actualRsBase = actualEmployeeTransactionList;
    }

    /**
     * A When step, invokes resetOperator method.
     *
     * @param operatorno
     *            The operator's number.
     * @param retailstoreid
     *            The retail storeid where the operator belong.
     */
    @When("a request to reset the passcode of the Operator{$operatorno}"
            + " with retailstoreid{$retailstoreid}")
    public final void requestToResetThePasscodeOfTheOperatorWithRetailstoreid(
            final String operatorno, final String retailstoreid) {
        actualRsBase = testCredentialResource.resetOperator(retailstoreid,
                operatorno);
    }

    /**
     * A Then step, tests actual and expected employees.
     *
     * @param expectedEmployees
     *            List of Employee.
     */
    @Then("I should have the following employees: $expectedEmployees")
    public final void iShouldHaveTheFollowingOperators(
            final ExamplesTable expectedEmployees) {
        List<Employee> actualEmployees = actualEmployeeTransactionList
                .getEmployeeList();

        Assert.assertEquals("Must exact number of Employee from the List",
                expectedEmployees.getRowCount(), actualEmployees.size());

        int i = 0;
        for (Map<String, String> expEmp : expectedEmployees.getRows()) {
            Employee actualOperator = actualEmployees.get(i);

            Assert.assertEquals(
                    "Compare the Operator's Number of Operator" + i, expEmp
                            .get("operatorno").trim(), actualOperator
                            .getNumber());
            Assert.assertEquals("Compare the Operator's Name of Operator" + i,
                    expEmp.get("operatorname").trim(),
                    actualOperator.getName());
            i++;
        }
    }

    /**
     * A Then step, tests actual and expected result code.
     *
     * @param code
     *            The expected result code.
     */
    @Then("the Result Code returned should be ($code)")
    public final void theResultCodeReturnedShouldBe(final String code) {
        int expectedResultCode = 0; // 0 means OK

        if (code.equals("FAIL_PASSCODE_RESET")) {
            expectedResultCode = ResultBase.RESCREDL_RESET_FAIL;
        } else if (code.equals("OPERATOR_NOT_FOUND")) {
            expectedResultCode = ResultBase.RESCREDL_ERROR_OPERATOR_NOTFOUND;
        }

        Assert.assertEquals("Compare the Result Code Returned",
                expectedResultCode, actualRsBase.getNCRWSSResultCode());
    }

    /**
     * A Then step, tests actual and expected list of employee.
     *
     * @param expectedListEmployee
     *            The list of expected employee.
     * @throws DataSetException
     *             Thrown when exception occurs.
     */
    @Then("I should have the following operator's passcode:"
            + "$expectedListOperator")
    public final void iShouldHaveTheFollowingOperatorsPasscode(
            final ExamplesTable expectedListEmployee) throws DataSetException {
        ITable actualListEmployee = dbInit
                .getTableSnapshot("MST_USER_CREDENTIALS");
        Assert.assertEquals("Must have exact Number of Employee",
                expectedListEmployee.getRowCount(),
                actualListEmployee.getRowCount());

        int i = 0;
        for (Map<String, String> expectedOperator : expectedListEmployee
                .getRows()) {
            String operatorno = checkEmptyValue(expectedOperator
                    .get("operatorno"));
            String passcode = checkEmptyValue(expectedOperator.get("passcode"));

            Assert.assertEquals(
                    "Compare the Operator's Number of Employee" + i,
                    operatorno,
                    String.valueOf(actualListEmployee.getValue(i, "operatorno"))
                            .trim());
            Assert.assertEquals("Compare the Operator's Passcode of Employee"
                    + i, passcode,
                    String.valueOf(actualListEmployee.getValue(i, "passcode"))
                            .trim());
            i++;
        }
    }

    /**
     * Helper method for checking if value is empty.
     *
     * @param str
     *            String to check.
     * @return empty string if parameter str has "empty" value.
     */
    private String checkEmptyValue(final String str) {
        String strTemp = str;
        if (strTemp.equals("empty")) {
            strTemp = "";
        }
        return strTemp;
    }

    /**
     * A Then step, tests actual and expected Employee object.
     *
     * @param xml
     *            The expected xml string representation of employee.
     * @throws Exception
     *             Thrown when exception occurs.
     */
    @Then("xml string should be {$xml}")
    public final void seriallize(final String xml) throws Exception {
        XmlSerializer<Employees> posLogRespSrlzr =
            new XmlSerializer<Employees>();
        System.out.println(actualEmployeeTransactionList);
        String actual = posLogRespSrlzr.marshallObj(Employees.class,
                actualEmployeeTransactionList, "UTF-8");
        System.out.println(actual);
        assertEquals(xml, actual);
    }

    /**
     * A Then step, tests actual and expected retail store id.
     *
     * @param expectedRetailStoreID
     *            The expected retail store id.
     */
    @Then("TransactionList should have RetailStoreID"
            + " of {$expectedRetailStoreID}")
    public final void transactionListShouldHaveRetailStoreIDOf(
            final String expectedRetailStoreID) {
        Assert.assertEquals("Compare the Retail Store ID",
                expectedRetailStoreID,
                actualEmployeeTransactionList.getRetailtStoreID());
    }

    /**
     * A Then step, tests actual and expected of TransactionList's result code.
     *
     * @param code
     *            The expected resultcode.
     */
    @Then("TransactionList Result Code value returned should be ($code)")
    public final void transactionListResultCode(final int code) {
        Assert.assertEquals("Compare the Retail Store ID", code,
                actualEmployeeTransactionList.getNCRWSSResultCode());
    }

    /**
     * A Then step, tests actual and expected result code.
     *
     * @param code
     *            The expected resultcode.
     */
    @Then("the Result Code value returned should be ($code)")
    public final void theResultCodeValueReturnedShouldBe(final int code) {
        Assert.assertEquals("Compare the returned result code", code,
                actualRsBase.getNCRWSSResultCode());
    }
}
