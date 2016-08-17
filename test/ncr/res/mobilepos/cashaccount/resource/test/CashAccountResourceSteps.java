package ncr.res.mobilepos.cashaccount.resource.test;

import static org.junit.Assert.assertEquals;

import ncr.res.mobilepos.cashaccount.model.GetCashBalance;
import ncr.res.mobilepos.cashaccount.resource.CashAccountResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

public class CashAccountResourceSteps extends Steps {
	
	private CashAccountResource cashAccountResource;
	private GetCashBalance getCashBalance;
	private DBInitiator dbInitiator = null;
	
	@BeforeScenario
	public final void setUp() {
		Requirements.SetUp();
		dbInitiator = new DBInitiator("CashAccountResourceSteps", DATABASE.RESTransaction);
	}
	
	@AfterScenario
	public final void TearDown() {
		Requirements.TearDown();
	}
	
	@Given("a CashAccount Resource")
	public final void createResource() {
		cashAccountResource = new CashAccountResource();
	}
	
	@Given("a table data {$data}")
	public final void createTableData(final String data) {
		try {
            dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
            		"test/ncr/res/mobilepos/cashaccount/resource/test/" + data + 
            		".xml");
		} catch (Exception e) {
			Assert.fail("Cannot set the table data.");
		}
	}
	
	@When("getting the cash balance for tillId{$tillId} and storeId{$storeId} and businessDayDate{$businessDayDate}")
	public final void getCashBalance(final String tillId, final String storeId, 
			final String businessDayDate) {
		getCashBalance = cashAccountResource.getCashBalance(tillId, storeId, 
				businessDayDate);
	}
	
	@Then("tillId should be {$tillId}")
	public final void validateTillId(final String tillId) {
		assertEquals(tillId, getCashBalance.getCashBalance().getTillId());
	}
	
	@Then("cashOnHand should be {$cashOnHand}")
	public final void validateCashOnHand(final String cashOnHand) {
		assertEquals(cashOnHand, getCashBalance.getCashBalance().getCashOnHand());
	}
	
	@Then("resultCode should be {$resultCode}")
	public final void validateResultCode(final String resultCode) {
		assertEquals(Integer.parseInt(resultCode), getCashBalance.getNCRWSSResultCode());
	}
	
		
}
