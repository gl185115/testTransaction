package ncr.res.mobilepos.credential.resource.test;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.credential.model.Employee;
import ncr.res.mobilepos.credential.model.Employees;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

/**
 * Steps class for listOperators in CredentialResource class.
 *
 */
@SuppressWarnings("deprecation")
public class ListOperatorsSteps extends Steps {
    /**
     * CredentialResource instance.
     */
    private CredentialResource testCredentialResource;
    /**
     * DBInitiator instance.
     */
    private DBInitiator dbInit;
    /**
     * List of Employee.
     */
    private Employees actualEmployeeTransactionList;
    /**
     * Xml filename that contains database data.
     */
    private String datasetpath = "test/ncr/res/mobilepos/credential/resource/test/";

    /**
     * Invokes before execution of each scenario.
     */
    @BeforeScenario
    public final void setUpClass() {
        Requirements.SetUp();
        dbInit = new DBInitiator("CredentialResourceSteps", DATABASE.RESMaster);
        initResources();
    }

    /**
     * Invokes after execution of each scenario.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }
    
	private void initResources() {
		ServletContext context = Requirements.getMockServletContext();
		testCredentialResource = new CredentialResource();
		try {
			Field contextField = testCredentialResource.getClass()
					.getDeclaredField("context");
			contextField.setAccessible(true);
			contextField.set(testCredentialResource, context);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			Assert.fail("Cant load Mock Servlet Context.");
		}
	}

    /**
     * A Given step, initialize database raw data.
     *
     * @param dataset   The xml filename.
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
     * A When step to set GlobalConstant for Max search result
     * 
     * @param limit
     */
    @When("I set the search limit to {$limit}")
    public final void setMaxLimit(String limit){
        limit = (limit.equals("null"))?"0":limit;
        GlobalConstant.setMaxSearchResults(Integer.parseInt(limit));
    }

    /**
     * A When step, invokes listOperators method.
     *
     * @param retailStoreid The retail storeid where the employees belong.
     * @param key           The key to search.
     */
    @When("a request to list all the operators with"
            + " RetailStoreID{$retailStoreid} and Key{$key} and Name{$name} and limit{$limit}")
    public final void aRequestToListAllTheOperatorsWithRetailStoreID(
            final String retailStoreid, final String key, final String name, final int limit) {
        actualEmployeeTransactionList = testCredentialResource
                .listOperators(retailStoreid, key, name, limit);        
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
                    "Compare the Operator's Number of Operator" + i,
                    expEmp.get("operatorno"), actualOperator.getNumber());
            Assert.assertEquals("Compare the Operator's Name of Operator" + i,
                    expEmp.get("operatorname"), actualOperator.getName());
            Assert.assertEquals("Compare the Operator's Type of Operator" + i,
                    expEmp.get("operatortype"),
                    actualOperator.getOperatorType());
            Assert.assertEquals("Compare the Operator's WorkstationID of Operator" + i,
                    expEmp.get("workstationid"),
                    actualOperator.getWorkStationID());
            i++;
        }
    }

    /**
     * A Then step, tests actual and expected result code.
     *
     * @param code  The expected result code.
     */
    @Then("the Result Code returned should be ($code)")
    public final void theResultCodeReturnedShouldBe(final String code) {
        if (code.equals("FAIL_PASSCODE_RESET")) {
        } else if (code.equals("OPERATOR_NOT_FOUND")) {
        }
    }

    /**
     * A Then step, tests actual and expected Employee object.
     *
     * @param xml           The expected xml string representation of employee.
     * @throws Exception    Thrown when exception occurs.
     */
    @Then("xml string should be {$xml}")
    public final void seriallize(final String xml) throws Exception {
        XmlSerializer<Employees> posLogRespSrlzr =
            new XmlSerializer<Employees>();
        String actual = posLogRespSrlzr.marshallObj(Employees.class,
                actualEmployeeTransactionList, "UTF-8");
        System.out.println(actual);
        assertEquals(xml, actual);
    }

    /**
     * A Then step, tests actual and expected retail store id.
     *
     * @param expectedRetailStoreID
     *          The expected retail store id.
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
     * A Then step, tests actual and expected result code.
     *
     * @param code  The expected resultcode.
     */
    @Then("the Result Code value returned should be ($code)")
    public final void theResultCodeValueReturnedShouldBe(final int code) {
        Assert.assertEquals("Compare the returned result code", code,
                actualEmployeeTransactionList.getNCRWSSResultCode());
    }
}
