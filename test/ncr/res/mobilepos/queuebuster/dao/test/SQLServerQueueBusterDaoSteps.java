package ncr.res.mobilepos.queuebuster.dao.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import ncr.realgate.util.Snap;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.queuebuster.dao.SQLServerQueueBusterDao;
import ncr.res.mobilepos.queuebuster.model.BusteredTransaction;
import ncr.res.mobilepos.queuebuster.model.CashDrawer;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

public class SQLServerQueueBusterDaoSteps extends Steps{
    private DBInitiator dbInit;
    private String datasetPath = "test/ncr/res/mobilepos/queuebuster/";
    private SQLServerQueueBusterDao testQueueBusterDao;
    private String actualSelectedPOSLogXml = null;
    private List<BusteredTransaction> actualBustedList = null;
    private CashDrawer cashDrawer;
    
    @BeforeScenario
    public final void setUpClass() throws Exception {
         Requirements.SetUp();
         dbInit = new DBInitiator("SQLServerQueueBusterDaoSteps", DATABASE.RESTransaction);
         testQueueBusterDao = new SQLServerQueueBusterDao();
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("a table entry named {$dataset} in the database")
    public final void aTableEntryNamedInTheDatabase(final String dataset){
        try {
            dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    datasetPath + dataset);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Assert.fail("Setting the Database table for QueueBuster failed");
        }
    }
    
    /*
    @When("a request to save an incomplete transaction for QueueBustering with"
            + " RetailStoreId{$storeid}, Queue{$queue},"
            + "Workstationid{$workstationid}, Sequencenumber{$sequencenumber},"
            + " Txdate{$txdate}, Poslogxml: $poslogxml")
    public final void aRequestToSaveAnIncompleteTransactionForQueueBustering(
            final String storeid, final String queue,
            final String workstationid, final String sequencenumber,
            final String txdate, final String poslogxml){
        try {
        	// unmarshall poslog xml
            XmlSerializer<PosLog> poslogSerializer = 
            		new XmlSerializer<PosLog>();
            PosLog posLog = poslogSerializer.unMarshallXml(poslogxml, 
            		PosLog.class);
            
            testQueueBusterDao.saveTransactionToQueue(posLog, poslogxml, queue, 0);
        } catch (DaoException e) {
            Assert.fail("[QueueBuster]Fail to save the transaction"
                    + " in TXL_FORWARD_ITEM " + e.getMessage());
        } catch (JAXBException e) {
            Assert.fail("[QueueBuster]Fail to unmarshall posLogXml: " 
            		+ e.getMessage());
        }
    }
    */
    
    @When("a request to select the oldest transaction in the queue"
            + " given that it has Queue{$queue}"
            + " and RetailStoreid{$retailStoreId}")
    public final void aRequestToSelectTheOldestTransactionInTheQueue(
            final String queue, final String retailStoreId){
        try {
            actualSelectedPOSLogXml = testQueueBusterDao
                    .selectOldestTransactionFromQueue(queue, retailStoreId);
        } catch (DaoException e) {
            Assert.fail("Fail to select the oldest Transaction"
                    + " in the Queue to be bustered");
        }
    }
    
//    @When("a request to select the transaction in the queue given that"
//            + " it has Queue{$queue}, RetailStoreid{$retailstoreid},"
//            + " WorkstationID{$workstationid}, "
//            + "and SequenceNumber{$sequencenumber}")
//    public final void aRequestToSelectTheOldestTransactionInTheQueue(
//            final String queue, final String retailStoreId,
//            final String workstationid, final String sequencenumber){
//        try {
//            actualSelectedPOSLogXml = testQueueBusterDao
//                .selectTransactionFromQueue(retailStoreId, queue,
//                        workstationid, sequencenumber);
//        } catch (DaoException e) {
//            Assert.fail("Fail to select the Transaction"
//                    + " in the Queue to be bustered");
//        }
//    }
    
    @When("a request to list all transactions given that "
            + "Queue{$queue} and RetailStoreID{$retailstoreid and TrainingFlag{$trainingflag}")
    public final void aRequestToListAllTransactions(
            final String queue, final String companyid, final String retailstoreid, final String workstationid, final int trainingflag){
        try {
            actualBustedList = testQueueBusterDao
                .listTransactionFromQueue(queue, companyid, retailstoreid, workstationid, trainingflag);
        } catch (DaoException e) {
            Assert.fail("Fail to list all Transactions from the Queue");
        }
    }
    
    @Then("I should have the following transaction: $expectedbustedList")
    public final void iShouldHaveTheFollowingTransactionReadyToBeBustered(
            final ExamplesTable expectedbustedList) throws DataSetException{
        ITable actualTable =  dbInit.getTableSnapshot("TXL_FORWARD_ITEM");
        
        assertThat("Compare the actual number of rows in TXL_FORWARD_ITEM",
                actualTable.getRowCount(), 
                is(equalTo(expectedbustedList.getRowCount())));
        
        int i = 0;        
        for (Map<String, String> transaction : expectedbustedList.getRows()) {
            assertThat("TXL_FORWARD_ITEM (row"+i+" - storeid",
                    (String)actualTable.getValue(i, "RetailStoreId"), 
                    is(equalTo(transaction.get("storeid"))));
            assertThat("TXL_FORWARD_ITEM (row"+i+" - termid",
                    ((String)actualTable.getValue(i, "WorkstationId")).trim(), 
                    is(equalTo(transaction.get("termid"))));
            assertThat("TXL_FORWARD_ITEM (row"+i+" - txid",
                    (String)actualTable.getValue(i, "SequenceNumber"), 
                    is(equalTo(transaction.get("txid"))));
            assertThat("TXL_FORWARD_ITEM (row"+i+" - txdate",
                    (Date)actualTable.getValue(i, "BusinessDayDate"), 
                    is(equalTo(Date.valueOf(transaction.get("txdate")))));
            assertThat("TXL_FORWARD_ITEM (row"+i+" - queue",
                    (String)actualTable.getValue(i, "Queue"), 
                    is(equalTo(transaction.get("queue"))));
            assertThat("TXL_FORWARD_ITEM (row"+i+" - status",
                    (Integer)actualTable.getValue(i, "Status"), 
                    is(equalTo(Integer.parseInt(transaction.get("status")))));
            
            i++;
        }
    }
    
    @Then("I should have the selected transaction: $expectedSelectedPoslogXml")
    public final void iShouldHaveTheSelectedTransaction(
            String expectedSelectedPoslogXml){
        if (expectedSelectedPoslogXml.equals("NULL")){
            expectedSelectedPoslogXml = null;
        }
        
        Assert.assertEquals("Compare the selected POSLog xml",
                expectedSelectedPoslogXml, actualSelectedPOSLogXml);
    }
    
    @Then("I should have the listed POSLog XMLs: $expectedbustedList")
    public final void iShouldHaveTheListedPOSLogXMLs(
            final ExamplesTable expectedbustedList){    
        int i = 0;
        for (Map<String, String> transaction : expectedbustedList.getRows()) {
            Assert.assertEquals("Compare All the list of POSLog XML"
                    + " being listed from the Queue",  
                    transaction.get("POSLogXml"), actualBustedList.get(i));
            i++;
        }
    }
    
    @Given("I have a queue buster dao controller")
    public void getQueueBusterDao() {
    	 try {
			testQueueBusterDao = new SQLServerQueueBusterDao();
		} catch (DaoException e) {
		    e.printStackTrace();
		}
    }
    
   @When("getting the previous amount for compayId{$companyId} and storeId{$storeId}")
    public final void getPreviousAmount(String companyId, String storeId) {
    	try {
			cashDrawer = testQueueBusterDao.getPreviousAmount(companyId, storeId);
		} catch (DaoException e) {
			e.printStackTrace();
		}
    }
   
    @When("inserting previous amount data {$companyId} {$storeId} {$amount} {$operatorId} {$terminalId}")
    public final void itShouldInsertData(String companyId, String storeId, String amount, String operatorId, String terminalId){
    	CashDrawer cashDrawer = new CashDrawer();
    	cashDrawer.setCompanyId(companyId);
    	cashDrawer.setStoreId(storeId);
    	cashDrawer.setCashOnHand(amount);
    	cashDrawer.setOperatorId(operatorId);
    	cashDrawer.setTerminalId(terminalId);
    	cashDrawer.setBusinessDayDate("2015-07-14");
    	try {
    		testQueueBusterDao.updatePreviousAmount(null, cashDrawer);
		} catch (DaoException e) {
			e.printStackTrace();
		}
    }
    
    @Then("It should retreive the new data {$companyId} {$storeId}")
    public final void itShouldRetrieveData(String companyId, String storeId) {
    	try {
			cashDrawer = testQueueBusterDao.getPreviousAmount(companyId, storeId);
			Assert.assertNotNull(cashDrawer);
    	} catch (DaoException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    	
    }
    
    @Then("cash drawer should not be null")
	public final void getCashDrawer(){
		Assert.assertNotNull(cashDrawer);
	}

}
