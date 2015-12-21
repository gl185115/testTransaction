package ncr.res.mobilepos.creditpayment.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.creditpayment.model.CreditPayment;
import ncr.res.mobilepos.creditpayment.model.CreditPaymentDataList;
import ncr.res.mobilepos.creditpayment.resource.CreditPaymentResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.pricing.model.ReasonData;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

public class CreditPaymentResourceTestSteps extends Steps{
	private CreditPaymentDataList creditPaymentList;
	CreditPaymentResource creditPaymentRsc;
	List<CreditPayment> creditPayment;
	
	
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
        dbInitiator = new DBInitiator("CreateCreditPaymentSteps", DATABASE.RESMaster);
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/creditpayment/resource/test/"
                + "PRM_CREDIT_PAYMENTMETHOD.xml");
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
     * Given Step: Set the table definition for Credit Payment to empty.
     */
    @Given("a CreditPayment resource")
    public final void aCreditPaymentResource() {
        try {
        	creditPaymentRsc = new CreditPaymentResource();
        	creditPaymentList = creditPaymentRsc.getCreditPayment();
        	
        } catch (Exception e) {
            Assert.fail("Cant set the database for Credit Payment.");
        }
    }
    
    @Then("Credit Payment should have the following values $creditPaymentTbl")
    public final void iMustHaveTxFollowingData(final ExamplesTable creditPaymentTbl) {
    	creditPayment = new ArrayList<CreditPayment>();
    	creditPayment = creditPaymentList.getCreditPayment();
    	
    	int i = 0;
        for(Map<String, String> txCreditPymnt : creditPaymentTbl.getRows()){
            assertThat("Compare the Button Number", creditPayment.get(i).getButtonNo(),
                    is(equalTo(txCreditPymnt.get("buttonnum"))));
            assertThat("Compare the Code", creditPayment.get(i).getCode(),
                    is(equalTo(txCreditPymnt.get("code"))));
            assertThat("Compare the Display Name", creditPayment.get(i).getDisplayName(),
                    is(equalTo(txCreditPymnt.get("displayname"))));
            i++;
        }        	
    }   
}
