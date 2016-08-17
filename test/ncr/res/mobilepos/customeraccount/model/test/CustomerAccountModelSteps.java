package ncr.res.mobilepos.customeraccount.model.test;

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
 * Model Test Steps for Customer Account.
 */
public class CustomerAccountModelSteps extends Steps {
    /**
     * Holds Customer Account Resource object.
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
        testCustomerAccount.getClass();
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
        customerResult = testCustomerAccount
                .getCustomerByID(customerNumber, deviceNumber, operatorNumber);
    }

    /**
     * Given Then : Display Customer Information.
     */
    @Then("Display Customer Info")
    public final void customerSearch() {
        customerResult.getCustomerid();
        customerResult.getCustomername();
        customerResult.getDiscountrate();
        customerResult.getGrade();
        customerResult.getMailaddress();
        customerResult.getPoints();
        customerResult.getUpddate();

        noCust = 1;
    }

    /**
     * Given Then : It should succeed.
     */
    @Then("it should succeed")
    public final void customerSearchSuccess() {
        assertThat(1, is(equalTo(noCust)));
    }
}