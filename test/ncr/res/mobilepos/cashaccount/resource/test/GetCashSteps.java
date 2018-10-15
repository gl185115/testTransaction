package ncr.res.mobilepos.cashaccount.resource.test;

import static org.junit.Assert.assertEquals;

import ncr.res.mobilepos.cashaccount.model.GetCashBalance;
import ncr.res.mobilepos.cashaccount.resource.CashAccountResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import org.junit.Assert;

public class GetCashSteps extends Steps {
	
	private CashAccountResource cashAccountResource;
	private GetCashBalance getCashBalance;
	private DBInitiator dbInitiatorMaster = null;
	private DBInitiator dbInitiatorRESTransaction = null;
	
	@BeforeScenario
	public final void setUp() {
		Requirements.SetUp();
	}
	
	@AfterScenario
	public final void TearDown() {
		Requirements.TearDown();
	}
	
	@Given("a CashAccount Resource")
	public final void createResource() {
		Requirements.getMockServletContext();
		cashAccountResource = new CashAccountResource();
	}
	
	@Given("a RESMaster table data $data")
	public final void resMasterInitData(final String data) {
		try {
			dbInitiatorMaster = new DBInitiator("CashAccountResourceSteps", DATABASE.RESMaster);
			dbInitiatorMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
            		"test/ncr/res/mobilepos/cashaccount/resource/test/" + data + 
            		".xml");
		} catch (Exception e) {
			Assert.fail("Cannot set the table data.");
		}
	}
	
	@Given("a RESTransaction table data $data")
	public final void resTransactionInitData(final String data) {
		try {
			dbInitiatorRESTransaction = new DBInitiator("CashAccountResourceSteps", DATABASE.RESTransaction);
			dbInitiatorRESTransaction.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
            		"test/ncr/res/mobilepos/cashaccount/resource/test/" + data + 
            		".xml");
		} catch (Exception e) {
			Assert.fail("Cannot set the table data.");
		}
	}
	
	@When("getting the cash balance for $companyid $storeid $tillid $terminalid $businessdate $trainingflag $datatype $itemlevel1 $itemlevel2")
	public final void getCashBalance(final String companyId,
			final String storeId, final String tillId, final String terminalId,
			final String businessDate, final int trainingFlag,
			final String dataType, final String itemLevel1,
			final String itemLevel2) {
		String tillIdParam = tillId.equalsIgnoreCase("null") ? null: tillId;
		getCashBalance = cashAccountResource.getReportItems(companyId, storeId, tillIdParam, terminalId, businessDate, trainingFlag, dataType, itemLevel1, itemLevel2);
	}
	
	@Then("tillId should be $tillId")
	public final void validateTillId(final String tillId) {
		assertEquals(tillId, getCashBalance.getCashBalance().getTillId());
	}
	
	@Then("cashOnHand should be $cashOnHand")
	public final void validateCashOnHand(final String cashOnHand) {
		assertEquals(cashOnHand, getCashBalance.getCashBalance().getCashOnHand());
	}
	
	@Then("resultCode should be $resultCode")
	public final void validateResultCode(final String resultCode) {
		assertEquals(Integer.parseInt(resultCode), getCashBalance.getNCRWSSResultCode());
	}
	
		
}
