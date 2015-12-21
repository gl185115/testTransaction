package ncr.res.mobilepos.pricing.resource.test;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.pricing.model.ReasonDataList;
import ncr.res.mobilepos.pricing.model.ItemReasonDataList;
import ncr.res.mobilepos.pricing.model.OverrideReasonDataList;
import ncr.res.mobilepos.pricing.model.TransactionReasonDataList;
import ncr.res.mobilepos.pricing.resource.ItemResource;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

/**
 * The Test Steps for Creating Reason Discount Resource.
 *
 * @author Vener Rosales
 */
public class GetDiscountReasonSteps  extends Steps {
    /**
     * ItemResource instance.
     */
    private ItemResource itemResource = null;
    private TransactionReasonDataList txnDiscountReasonList;
    private ItemReasonDataList itemDiscountReasonList;
    private OverrideReasonDataList overrideDiscountReasonList;
    private ReasonDataList dscntRsnList;
    /**
     * The database unit test initiator.
     */
    private DBInitiator dbInitiator = null;
    /**
     * Executed before start of the scenario.
     *
     * @throws Exception
     *             thrown when set-up fail.
     */
    @BeforeScenario
    public final void setUpClass() throws Exception {
        Requirements.SetUp();
        dbInitiator = new DBInitiator("CreateReasonDiscountSteps", DATABASE.RESMaster);
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/pricing/resource/test/"
                + "reason_discount.xml");
        GlobalConstant.setCorpid("000000000000");
    }

    /**
     * Executed after end of the scenario.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }
    
    /**
     * Given Step: Set the table definition for Item(s) to empty.
     */
    @Given("an initial data for reason discount")
    public final void anInitialDataForReasonDiscount() {
        try {
            dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    "test/ncr/res/mobilepos/pricing/resource/test/"
                    + "reason_discount.xml");
        } catch (Exception e) {
            Assert.fail("Cant set the database for item.");
        }
    }

    /**
     * Given Step: Set the table definition for Item(s) to empty.
     */
    @Given("an item resource for reason discount")
    public final void anItemResourceForTransactionReasonDiscount() {
        try {
        	itemResource = new ItemResource();
        	dscntRsnList = itemResource.getDiscountReason();
        	        	
        	System.out.println("DISCOUNT REASON: " + dscntRsnList.toString());
        	
        } catch (Exception e) {
            Assert.fail("Cant set the database for item.");
        }
    }
    
    @When("i get transaction discount reason")
    public final void iGetTransactionDiscountReason() {
        try {
        	txnDiscountReasonList = new TransactionReasonDataList();
        	txnDiscountReasonList = dscntRsnList.getTxReasonDataList();        	
        } catch (Exception e) {
            Assert.fail("Cant set the database for transaction.");
        }
    }
   
    @SuppressWarnings("deprecation")
	@Then("i should have object for transaction discount reason")
    public final void iShouldHaveObjectForTransactionDiscountReason() {  	
    	assertThat(txnDiscountReasonList, is(TransactionReasonDataList.class));
    }    
    
    @When("i get item discount reason")
    public final void iGetItemDiscountReason() {
        try {
        	itemDiscountReasonList = new ItemReasonDataList();
        	itemDiscountReasonList = dscntRsnList.getItemReasonDataList();        	
        } catch (Exception e) {
            Assert.fail("Cant set the database for item.");
        }
    }
   
    @SuppressWarnings("deprecation")
	@Then("i should have object for item discount reason")
    public final void IShouldHaveObjectForTransactionDiscountReason() {  	
    	assertThat(itemDiscountReasonList, is(ItemReasonDataList.class));
    } 
    
    @When("i get price override discount reason")
    public final void iGetOverrideDiscountReason() {
        try {
        	overrideDiscountReasonList = new OverrideReasonDataList();
        	overrideDiscountReasonList = dscntRsnList.getOverrideReasonDataList();        	
        } catch (Exception e) {
            Assert.fail("Cant set the database for price override.");
        }
    }
   
    @SuppressWarnings("deprecation")
	@Then("i should have object for price override discount reason")
    public final void IShouldHaveObjectForOverrideDiscountReason() {  	
    	assertThat(overrideDiscountReasonList, is(OverrideReasonDataList.class));
    }    
    
    /**
     * Then Step: Compare the result code.
     * @param expectedresult The expected result.
     */
    @Then("the result code should be {$expectedresult}")
    public final void theResultCodeShouldBe(final int expectedresult) {
        Assert.assertEquals("Compare the actual result",
                expectedresult, dscntRsnList.getNCRWSSResultCode());
    }
}
