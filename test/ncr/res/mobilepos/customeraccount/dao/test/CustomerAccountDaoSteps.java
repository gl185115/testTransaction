package ncr.res.mobilepos.customeraccount.dao.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertThat;

import ncr.res.mobilepos.customeraccount.dao.ICustomerDAO;
import ncr.res.mobilepos.customeraccount.model.Customer;
import ncr.res.mobilepos.daofactory.SQLServerDAOFactory;
import ncr.res.mobilepos.helper.Requirements;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.steps.Steps;
/*
Test Steps:
-Given a Customer with Customer Number {$customerNumber}
-When the Operator searchers for Customer ID {$customerNumber}
-Then the system should return result {$result}
-Then it should succeed
-Then it should fail
*/

/**
 * DAO Test Steps for Customer Account.
 */
public class CustomerAccountDaoSteps extends Steps {

    /**
     * Holds the Customer DAO.
     */
    private ICustomerDAO testICustomerDAO;
    /**
     * Holds the Customer information.
     */
    private Customer testCustomer;
    /**
     * Result Base's result code.
     */
    private int resultCode;
    /**
     * Holds status if customer is found.
     */
    private int customerFound;

    /**
     * Method to test the database connection.
     */
    @BeforeScenario
    public final void setUpClass() {
        Requirements.SetUp();
    }
    /**
     * Method to destroy the subcontext of InitialContext.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }

    /**
     * Given Step : I have a customer access.
     * @param customerNumber    Customer's number
     */
    @Given("a Customer with Customer Number {$customerNumber}")
    public final void iHaveACustomerAccess(final String customerNumber) {
        try {
            SQLServerDAOFactory daoFactory = new SQLServerDAOFactory();
            testICustomerDAO = daoFactory.getCustomerDAO();
            testCustomer = testICustomerDAO.getCustomerByID(customerNumber);
            resultCode = 1;
        } catch (Exception e) {
            resultCode = 0;
        }
    }
    /**
     * Scenario when the operator searches for Customer ID.
     * @param customerNumber    Customer's number.
     */
    @When("the Operator searchers for Customer ID {$customerNumber}")
    public final void customerSearch(final String customerNumber) {
        try {
            testCustomer = testICustomerDAO.getCustomerByID(customerNumber);
            customerFound = 1;
            resultCode = 1;
        } catch (Exception e) {
            customerFound = 0;
            resultCode = 0;
        }
    }
    /**
     * Given Then : System should return result.
     * @param result    Result
     */
    @Then("system should return result {$result}")
    public final void customerFound(final int result) {
            int currentStatus = customerFound;
            assertThat(result, is(equalTo(customerFound)));
            resultCode = currentStatus;
    }
    /**
     * Given Then : It should succeed.
     */
    @Then("it should succeed")
    public final void shouldSucceed() {
        assertThat(1, is(equalTo(resultCode)));
    }

    /**
     * Given When : The operator searches for non-customer ID.
     * @param noncustomerNumber Non-customer number
     */
    @When("the Operator searchers for non-Customer ID {$noncustomerNumber}")
    public final void customerSearchNonCust(final String noncustomerNumber) {
        try {
            testCustomer = testICustomerDAO.getCustomerByID(noncustomerNumber);
            customerFound = 0;
            resultCode = 0;
        } catch (Exception e) {
            customerFound = 1;
            resultCode = 1;
        }
    }

    /**
     * Given Then : System should return result for non-customer number.
     * @param result    Result for customer search.
     */
    @Then("system should return result {$resultNone}")
    public final void customerNotFound(final int result) {
            int currentStatus = customerFound;
            assertThat(result, is(equalTo(customerFound)));
            resultCode = currentStatus;
    }
    /**
     * Given Then : It should fail.
     */
    @Then("it should fail")
    public final void shouldFail() {
        assertThat(1, is(not(equalTo(resultCode))));
    }
}
