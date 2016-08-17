package ncr.res.mobilepos.customeraccount.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import ncr.res.mobilepos.customeraccount.model.Customer;
import ncr.res.mobilepos.customeraccount.resource.CustomerAccountResource;
import ncr.res.mobilepos.helper.Requirements;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

/*
Test Steps:
-Given I have a Customer Resource
-When I Search for customerNumber {$customerNumber}
-and deviceNumber {$deviceNumber}
-and operatorNumber {$operatorNumber}
-Then Search must return result ($searchResult)
-Then Customer exists
-Then Customer does not exists
*/

/**
 * Resource Test Steps for Customer Account.
 */
public class CustomerAccountResourceSteps extends Steps {
    /**
     * Holds Customer Account resource.
     */
    private CustomerAccountResource testCustomerAccount;
    /**
     * Holds customer account result.
     */
    private Customer customerResult;
    /**
     * No customer.
     */
    private int noCust;
    /**
     * Method to test the database connection.
     */
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }
    /**
     * Method to destroy the subcontext of InitialContext.
     */
    @AfterScenario
    public final void tearDown() {
        Requirements.TearDown();
    }

    /**
     * Given Step: I have a Customer Resource.
     */
    @Given("I have a Customer Resource")
    public final void iHaveACustomerResource() {
        testCustomerAccount = new CustomerAccountResource();
    }
    /**
     * Given When : I search for customer with customer number, device number
     *              and operator number.
     * @param customerNumber    Customer Number
     * @param deviceNumber      Device Number
     * @param operatorNumber    Operator Number
     */
    @When("I Search for customerNumber {$customerNumber} and deviceNumber"
            + " {$deviceNumber} and operatorNumber {$operatorNumber}")
    public final void customerAccount(final String customerNumber,
            final String deviceNumber, final String operatorNumber) {
        customerResult = testCustomerAccount.getCustomerByID(customerNumber,
                deviceNumber, operatorNumber);
    }

    /**
     * Given Then : Search should return result.
     * @param searchResult  Search result for customer.
     */
    @Then("search should return result {$searchResult}")
    public final void customerSearch(final int searchResult) {
        noCust = searchResult;
    }

    /**
     * Given Then : Customer Exists.
     */
    @Then("Customer exists")
    public final void hasCustomer() {
        assertThat(1, is(equalTo(noCust)));
    }

}
