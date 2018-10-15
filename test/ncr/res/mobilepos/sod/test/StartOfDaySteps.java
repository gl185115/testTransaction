package ncr.res.mobilepos.sod.test;

import java.lang.reflect.Field;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.ServletContext;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.resource.JournalizationResource;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.queuebuster.model.CashDrawer;
import ncr.res.mobilepos.queuebuster.resource.QueueBusterResource;
import ncr.res.mobilepos.tillinfo.resource.TillInfoResource;

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

import static org.mockito.Mockito.*;

/**
 * Steps class for component testing of operator signon.
 */
@SuppressWarnings("deprecation")
public class StartOfDaySteps extends Steps {
	
	private TillInfoResource tillInfoResource;
	private DBInitiator dbinitRESMaster = null;
	private DBInitiator dbinitRESTransaction = null;
	private String companyId, storeId, terminalId;
	private ResultBase resultBase;
    private CashDrawer cashOnDrawer;
    private PosLogResp poslogResp;
	/**
     * Invokes before execution of each scenario.
     */
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        mock(Calendar.class);
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
        tillInfoResource = new TillInfoResource();
        try {
            Field tillContext = tillInfoResource.getClass().getDeclaredField("servletContext");
            tillContext.setAccessible(true);
            tillContext.set(tillInfoResource, context);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Cant load Mock Servlet Context.");
        } 
    }

    /**
     * Initialize till table.
     *
     * @param filename the xml file.
     */
	@Given("a RESMaster initial data from $filename")
	public final void givenInitialDataForRESMaster(final String fileName) {
		dbinitRESMaster = new DBInitiator("TillInfoResourceSteps", DATABASE.RESMaster);
		try {
			dbinitRESMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/sod/test/" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
     * Initialize journal table.
     *
     * @param filename the xml file.
     */
	@Given("a RESTransaction initial data from $filename")
	public final void givenInitialDataForRESTransaction(final String fileName) {
		dbinitRESTransaction = new DBInitiator("TillInfoResourceSteps", DATABASE.RESTransaction);
		try {
			dbinitRESTransaction.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/sod/test/" + fileName);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /**
     * Initialize company store and terminal.
     * @param companyId
     * @param storeId
     * @param terminalId
     */
    @Given("a companyid $companyid storeid $storeid terminalid $terminalid")
    public final void givenTheFollowing(final String companyId, final String storeId, final String terminalId){
    	this.companyId = companyId;
    	this.storeId = storeId;
    	this.terminalId = terminalId;
    }
    
    @Given("that multiple SOD is $isMultiSOD")
    public final void givenThatMultiSOD(final String isMultiSODTemp) {
    	boolean isMultiSOD = false;
    	if(null != isMultiSODTemp) {
    		isMultiSOD = (isMultiSODTemp.equals("allowed")) ? true: false;
    	}
    	GlobalConstant.setMultiSOD(isMultiSOD);
    }

    @When("operator $operatorid starts SOD at till $tillid")
    public final void whenOperatorStartSOD(final String operatorNo, final String tillId) {
    	resultBase = tillInfoResource.getExecuteAuthority(companyId, storeId, tillId, terminalId, operatorNo, "SOD", "false");  
    	QueueBusterResource queueBusterResource = new QueueBusterResource();
    	cashOnDrawer = queueBusterResource.getPreviousAmount(companyId, storeId);
    }
    
    @When("completed SOD with poslog [$poslogxml]")
    public final void whenCompletedSOD(final String poslogXml) {
    	JournalizationResource journalizationResource = new JournalizationResource();
    	poslogResp = journalizationResource.journalize(poslogXml.trim(), 0);
    }
    
    @Then("it should get journal response $ncrwssresultcode")
    public final void thenShouldGetResultCode(final int resultCode) {
    	Assert.assertEquals("Compare journalize resultcode.", resultCode, poslogResp.getNCRWSSResultCode());
    }
    
    @Then("it should have an authorized code $resultcode")
    public final void thenItShouldBeAuthorized(final int resultCode) {
    	Assert.assertEquals("Compare getexecuteauthority resultcode.", resultCode, resultBase.getNCRWSSResultCode());
    }
    
    @Then("it should get cashonhand $cashonhand")
    public final void thenItShouldGetCashOnHand(final String cashOnHand) {
    	Assert.assertEquals("Compare cashonhand.", cashOnHand.trim(), cashOnDrawer.getCashOnHand().trim());
    }
    
	/**
     * Then Step: Verify the tillinfo in the database.
     * @param expected The Expected tills
     * @throws DataSetException 
     */
    @Then("MST_TILLIDINFO should have the following: $expected")
    public final void theMstTillIdInfoShouldHaveTheFollowing(
            final ExamplesTable expected) throws DataSetException {
        ITable actual = dbinitRESMaster.getTableSnapshot("MST_TILLIDINFO");
        Assert.assertEquals("Compare the number of tills:",
        		expected.getRowCount(),
                actual.getRowCount());
        int i = 0;
        for (Map<String, String> expectedRow: expected.getRows()) {
        	Assert.assertEquals("Compare the CompanyId in MST_TILLIDINFO row" + i,
        			expectedRow.get("CompanyId").trim(),
                    actual.getValue(i, "CompanyId"));
        	Assert.assertEquals("Compare the StoreId in MST_TILLIDINFO row" + i,
        			expectedRow.get("StoreId").trim(),
                    actual.getValue(i, "StoreId"));
        	Assert.assertEquals("Compare the TillId in MST_TILLIDINFO row" + i,
        			expectedRow.get("TillId").trim(),
                    actual.getValue(i, "TillId"));
        	Assert.assertEquals("Compare the TerminalId in MST_TILLIDINFO row" + i,
        			expectedRow.get("TerminalId").trim(),
                    actual.getValue(i, "TerminalId"));
        	Assert.assertEquals("Compare the BusinessDayDate in MST_TILLIDINFO row" + i,
        			expectedRow.get("BusinessDayDate").trim(),
                    String.valueOf(actual.getValue(i, "BusinessDayDate")));
        	Assert.assertEquals("Compare the SodFlag in MST_TILLIDINFO row" + i,
        			expectedRow.get("SodFlag").trim(),
                    String.valueOf(actual.getValue(i, "SodFlag")));
        	Assert.assertEquals("Compare the EodFlag in MST_TILLIDINFO row" + i,
        			expectedRow.get("EodFlag").trim(),
                    String.valueOf(actual.getValue(i, "EodFlag")));
        	Assert.assertEquals("Compare the UpdOpeCode in MST_TILLIDINFO row" + i,
        			expectedRow.get("UpdOpeCode").trim(),
                    actual.getValue(i, "UpdOpeCode"));
            i++;
        }
    }
	
    /**
     * Then Step: Verify the journal in the database.
     * @param expected The Expected journals
     * @throws DataSetException 
     */
    @Then("TXL_SALES_JOURNAL should have the following: $expected")
    public final void theTxlSalesJournalShouldHaveTheFollowing(
            final ExamplesTable expected) throws DataSetException {
        ITable actual = dbinitRESTransaction.getTableSnapshot("TXL_SALES_JOURNAL");
        Assert.assertEquals("Compare the number of journal:",
        		expected.getRowCount(),
                actual.getRowCount());
        int i = 0;
        for (Map<String, String> expectedRow: expected.getRows()) {
        	Assert.assertEquals("Compare the CompanyId in TXL_SALES_JOURNAL row" + i,
        			expectedRow.get("CompanyId").trim(),
                    actual.getValue(i, "CompanyId"));
        	Assert.assertEquals("Compare the RetailStoreId in TXL_SALES_JOURNAL row" + i,
        			expectedRow.get("RetailStoreId").trim(),
                    actual.getValue(i, "RetailStoreId"));
        	Assert.assertEquals("Compare the WorkstationId in TXL_SALES_JOURNAL row" + i,
        			expectedRow.get("WorkstationId").trim(),
                    actual.getValue(i, "WorkstationId"));
        	Assert.assertEquals("Compare the SequenceNumber in TXL_SALES_JOURNAL row" + i,
        			expectedRow.get("SequenceNumber").trim(),
                    String.valueOf(actual.getValue(i, "SequenceNumber")));
        	Assert.assertEquals("Compare the BusinessDayDate in TXL_SALES_JOURNAL row" + i,
        			expectedRow.get("BusinessDayDate").trim(),
                    String.valueOf(actual.getValue(i, "BusinessDayDate")));
        	Assert.assertEquals("Compare the TrainingFlag in TXL_SALES_JOURNAL row" + i,
        			expectedRow.get("TrainingFlag").trim(),
                    String.valueOf(actual.getValue(i, "TrainingFlag")));
        	Assert.assertEquals("Compare the TxType in TXL_SALES_JOURNAL row" + i,
        			expectedRow.get("TxType").trim(),
                    String.valueOf(actual.getValue(i, "TxType")));
        	Assert.assertEquals("Compare the Status in TXL_SALES_JOURNAL row" + i,
        			expectedRow.get("Status").trim(),
                    actual.getValue(i, "Status"));
        	Assert.assertEquals("Compare the SendStatus1 in TXL_SALES_JOURNAL row" + i,
        			expectedRow.get("SendStatus1").trim(),
                    String.valueOf(actual.getValue(i, "SendStatus1")));
        	Assert.assertEquals("Compare the SendStatus2 in TXL_SALES_JOURNAL row" + i,
        			expectedRow.get("SendStatus2").trim(),
                    String.valueOf(actual.getValue(i, "SendStatus2")));
        	Assert.assertEquals("Compare the Tx in TXL_SALES_JOURNAL row" + i,
        			expectedRow.get("Tx").trim(),
                    String.valueOf(actual.getValue(i, "Tx")));
            i++;
        }
    }
}
