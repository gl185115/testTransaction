package ncr.res.mobilepos.userloginlogout.test;

import junit.framework.Assert;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.resource.JournalizationResource;
import ncr.res.mobilepos.model.ResultBase;

/**
 * Steps class for component testing of operator signon.
 */
@SuppressWarnings("deprecation")
public class UserLogoutSteps extends Steps {
	private CredentialResource credentialResource;
	private ResultBase resultBase = null;
	private JournalizationResource journalizationResource;
	private int resultCode;
	private DBInitiator dbRESTransactionInitiator;
	
	/**
     * Invokes before execution of each scenario.
     */
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        initResources();
    }

    /**
     * Invokes after execution of each scenario.
     */
    @AfterScenario
    public final void tearDown() {
        Requirements.TearDown();
    }

    public final void initResources() {
        ServletContext context = Requirements.getMockServletContext();
        credentialResource = new CredentialResource();
        journalizationResource = new JournalizationResource(); 
        try {
            Field credentialContext = credentialResource.getClass().getDeclaredField("context");
            credentialContext.setAccessible(true);
            credentialContext.set(credentialResource, context);
            Field journalizationContext = journalizationResource.getClass()
					.getDeclaredField("context");
			journalizationContext.setAccessible(true);
			journalizationContext.set(journalizationResource, context);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Cant load Mock Servlet Context.");
        } 
    }	
    @Then("TXL_SALES_JOURNAL table should have: $expected")
	public final void checkTxlSalesJournal(final ExamplesTable expecteditems) throws DataSetException {
		List<Map<String, String>> expectedItemRows = expecteditems.getRows();
		ITable actualItemRows = dbRESTransactionInitiator.getTableSnapshot("TXL_SALES_JOURNAL");
		Assert.assertEquals("Compare that the number of rows in Items are exact: ", expecteditems.getRowCount(),
				actualItemRows.getRowCount());
		int i = 0;
		for (Map<String, String> expItem : expectedItemRows) {
			Assert.assertEquals("Compare the CompanyId row " + i + ": ", expItem.get("CompanyId"), actualItemRows.getValue(i, "CompanyId").toString());
			Assert.assertEquals("Compare the RetailStoreId row " + i + ": ", expItem.get("RetailStoreId"), actualItemRows.getValue(i, "RetailStoreId").toString());
			Assert.assertEquals("Compare the WorkstationId row " + i + ": ", expItem.get("WorkstationId"), actualItemRows.getValue(i, "WorkstationId").toString());
			Assert.assertEquals("Compare the SequenceNumber row " + i + ": ", expItem.get("SequenceNumber"), actualItemRows.getValue(i, "SequenceNumber").toString());
			Assert.assertEquals("Compare the BusinessDayDate row " + i + ": ", expItem.get("BusinessDayDate"), actualItemRows.getValue(i, "BusinessDayDate").toString());
			Assert.assertEquals("Compare the TrainingFlag row " + i + ": ", expItem.get("TrainingFlag"), actualItemRows.getValue(i, "TrainingFlag").toString());
			Assert.assertEquals("Compare the TxType row " + i + ": ", expItem.get("TxType"), actualItemRows.getValue(i, "TxType").toString());			
			Assert.assertEquals("Compare the ServerId row " + i + ": ", expItem.get("ServerId"), actualItemRows.getValue(i, "ServerId").toString());			
			Assert.assertEquals("Compare the Status row " + i + ": ", expItem.get("Status"), actualItemRows.getValue(i, "Status").toString());			
			Assert.assertEquals("Compare the SendStatus1 row " + i + ": ", expItem.get("SendStatus1"), actualItemRows.getValue(i, "SendStatus1").toString());			
			Assert.assertEquals("Compare the SendStatus2 row " + i + ": ", expItem.get("SendStatus2"), actualItemRows.getValue(i, "SendStatus2").toString());	
			Assert.assertEquals("Compare the EjStatus row " + i + ": ", expItem.get("EjStatus"), actualItemRows.getValue(i, "EjStatus").toString());	
			Assert.assertEquals("Compare the Tx row " + i + ": ", expItem.get("Tx"), actualItemRows.getValue(i, "Tx").toString());	
			i++;
		}
	}
    @Given("that no transactions")
	public final void givenNoTransactions() {
		try {
			dbRESTransactionInitiator = new DBInitiator("UserLoginSteps",
					DATABASE.RESTransaction);
			dbRESTransactionInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/userloginlogout/test/TXL_SALES_JOURNAL_EMPTY.xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
    @When("saving poslogxml:$1, trainingmode:$2")
	public final void whenSavingPOSlog(final String posLogXml, final int trainingMode){
		PosLogResp poslogResp = journalizationResource.journalize(posLogXml, trainingMode);
		resultCode = poslogResp.getNCRWSSResultCode();
	}
    @When("credential signoff with operatorid:$1")
    public final void whenCredentialSignOff(final String operatorId) {
    	resultBase = credentialResource.requestSignOff(operatorId);
    	resultCode = resultBase.getNCRWSSResultCode();
    }
    
    @Then("it should get NCRWSSResultCode:$1")
    public final void thenGetResult(final int resultCode) {
    	Assert.assertEquals("Compare resultcode", resultCode, resultCode);
    }
    private DBInitiator dbInitiator = null;
    @Given("that user is existing and signon")
    public final void givenThatUserExist() {
		dbInitiator = new DBInitiator("UserLogoutSteps",
				DATABASE.RESMaster);
		try {
			dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/userloginlogout/test/data_users.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    @Then("MST_USER_CREDENTIALS should have the following: $expected")
    public final void testCredentialTable(ExamplesTable expected) throws DataSetException {
    	ITable actualItemRows = dbInitiator.getTableSnapshot("MST_USER_CREDENTIALS");
    	
		int i = 0;
		for (Map<String, String> expItem : expected.getRows()) {
			Assert.assertEquals("Compare TerminalId", expItem.get("TerminalId"), String.valueOf(actualItemRows.getValue(i, "TerminalId")));
		}
    }
}