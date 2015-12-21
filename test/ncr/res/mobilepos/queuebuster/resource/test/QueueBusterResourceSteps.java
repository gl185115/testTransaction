package ncr.res.mobilepos.queuebuster.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.sql.Date;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.queuebuster.model.BusteredTransaction;
import ncr.res.mobilepos.queuebuster.model.BusteredTransactionList;
import ncr.res.mobilepos.queuebuster.model.ResumedTransaction;
import ncr.res.mobilepos.queuebuster.model.SuspendData;
import ncr.res.mobilepos.queuebuster.resource.QueueBusterResource;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

public class QueueBusterResourceSteps extends Steps{
    private DBInitiator dbInitRESTransaction = null;
    private DBInitiator dbInitRESMaster = null;
    private QueueBusterResource testQueueBustResource = null;
    private String datasetPath = "test/ncr/res/mobilepos/queuebuster/";
    private ResultBase actualRsbase = null;
    private ResumedTransaction actuaResumedTransaction = null; 
    private BusteredTransactionList actualBustTranslist = null;
    private SuspendData suspendData = null;
    private ServletContext servletContext = null;
    
    @BeforeScenario
    public final void setUpClass() throws Exception {
         Requirements.SetUp();
         servletContext = Requirements.getMockServletContext();
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("a QueueBuster Service")
    public void aPromotionService() {
		testQueueBustResource = new QueueBusterResource();
		Field queueBusterContext;
		Field itemcontext;
		try {
			QueueBusterResource queueBusterResourceObj = new QueueBusterResource();
			queueBusterContext = testQueueBustResource.getClass()
					.getDeclaredField("context");
			itemcontext = queueBusterResourceObj.getClass().getDeclaredField(
					"context");
			queueBusterContext.setAccessible(true);
			itemcontext.setAccessible(true);
			queueBusterContext.set(testQueueBustResource, servletContext);
			itemcontext.set(queueBusterResourceObj, servletContext);
		} catch (Exception ex) {
			Assert.fail("Can't Retrieve Servlet context from promotion.");
		}
    }
        @Given("a table entry named {$dataset} in the {$database} database")
    public final void aTableEntryNamedInTheDatabase(final String dataset, final String dbName){
        try {
        	if(dbName.equals("RESMaster")){
        		dbInitRESMaster = new DBInitiator("DBInitRESMaster", datasetPath + dataset, DATABASE.RESMaster);
        	}else if(dbName.equals("RESTransaction")){
        		dbInitRESTransaction = new DBInitiator("DBInitRESTransation", datasetPath + dataset, DATABASE.RESTransaction);
        	}
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Assert.fail("Setting the Database table for QueueBuster failed");
        }
    }
    
    @When("a request to save an incomplete transaction for QueueBustering with"
            + " RetailStoreId{$retailstoreid}, Queue{$queue},"
            + "Workstationid{$workstationid}, Sequencenumber{$sequencenumber},"
            + " Poslogxml: $poslogxml")
    public final void aRequestToSaveAnIncompleteTransactionForQueueBustering(
            final String retailstoreid, final String queue,
            final String workstationid,
            final String sequencenumber, final String poslogxml){
        actualRsbase =
            testQueueBustResource.suspend(retailstoreid, queue,
                    workstationid, sequencenumber, poslogxml);
    }
    
    @SuppressWarnings("deprecation")
	@When("a request to forward an incomplete transaction for QueueBustering with"
            + " Poslogxml: $poslogxml")
    public final void aRequestToForwardAnIncompleteTransactionForQueueBustering(
            final String poslogxml){
        actualRsbase =
            testQueueBustResource.suspendTransactionToQueue(poslogxml);
    }
    
    @When("I resume transaction with Queue{$queue}, StoreId{$storeId}, TermId{$termId}, TxId{$SeqNo} and TxDate{$businessDate}")
    public final void aRequestToSelectTheTransactionInTheQueue(
            final String queue, final String retailStoreId,
            final String workstationid, final String sequencenumber, final String businessDayDate){
        /* temporary comment out, hotfix. cannot build jenkins (ea185055)
        actuaResumedTransaction = testQueueBustResource.resume(retailStoreId,
                queue, workstationid, sequencenumber, businessDayDate);
        */
    }
    
    @When("a request to list all transactions given that Queue{$queue}"
            + " and RetailStoreID{$retailstoreid} and TrainingFlag{$trainingflag}")
    public final void aRequestToListAllTransactions(final String companyid, final String queue,
            final String retailstoreid, final String workstationid, final int trainingflag){
        actualBustTranslist = testQueueBustResource.list(companyid, retailstoreid, workstationid, queue, trainingflag);
        if(actualBustTranslist.getBusteredTransactionList().size()> 0){
        	System.out.println(actualBustTranslist.getBusteredTransactionList().toString());
        }
    }
    
    @When("I cancel the transaction with RetailStoreId{$retailstoreid}, txdate{$txdate}, Workstationid{$workstationid}, txid{$txid}")
     public final void requestCancel(String retailstoreid, String txdate, String workstationid, String txid){
        suspendData = testQueueBustResource.requestToQueue(
                "cancel", retailstoreid, workstationid, txid, txdate);
    }
    
    @When("I send an unknown method with RetailStoreId{$retailstoreid}, txdate{$txdate}, Workstationid{$workstationid}, txid{$txid}")
    public final void requestUnknown(String retailstoreid, String txdate, String workstationid, String txid){
       suspendData = testQueueBustResource.requestToQueue(
               "unknown", retailstoreid, workstationid, txid, txdate);
   }
    
    @When("I complete the transaction with RetailStoreId{$retailstoreid}, txdate{$txdate}, Workstationid{$workstationid}, txid{$txid}")
    public final void requestComplete(String retailstoreid, String txdate, String workstationid, String txid){
       suspendData = testQueueBustResource.requestToQueue(
               "complete", retailstoreid, workstationid, txid, txdate);
   }
    
    @When("a request to select a transaction from queue given that it has RetailStoreID{$storeID}, WorkstationID{$workstationID}, BusinessDate{$txDate}, SequenceNumber{$txID}")
    public final void aRequestToSelectATransactionFromQueue(
            final String storeid, final String workstationid,
            final String txdate, final String txid) {
        suspendData = testQueueBustResource.requestToQueue(
                "get", storeid, workstationid, txid, txdate);
    }
    
    @Then("I should have the following transaction: $expectedbustedList")
    public final void iShouldHaveTheFollowingTransactionReadyToBeBustered(
            final ExamplesTable expectedbustedList) throws DataSetException{
        ITable actualTable =  dbInitRESTransaction.getTableSnapshot("TXL_FORWARD_ITEM");
        
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
                    String.valueOf(actualTable.getValue(i, "SequenceNumber")), 
                    is(equalTo(transaction.get("txid"))));
            assertThat("TXL_FORWARD_ITEM (row"+i+" - txdate",
                    (Date)actualTable.getValue(i, "BusinessDayDate"), 
                    is(equalTo(Date.valueOf(transaction.get("txdate")))));
            String queue = transaction.get("queue");
            if(queue.equals("null")) {
                queue = null;
            }
            String actualQueue = (String)actualTable.getValue(i, "Queue");
            if(actualQueue != null){
                actualQueue = actualQueue.trim();
            }
            assertThat("TXL_FORWARD_ITEM (row"+i+" - queue",
                    actualQueue, 
                    is(equalTo(queue)));
            assertThat("TXL_FORWARD_ITEM (row"+i+" - status",
                    (Integer)actualTable.getValue(i, "Status"), 
                    is(equalTo(Integer.parseInt(transaction.get("status")))));
            i++;
        }
    }
    
    @Then("I should have the following deviceinfo: $expectedbustedList")
    public final void iShouldHaveTheFollowingDeviceInfo(
            final ExamplesTable expectedDeviceInfo) throws DataSetException{
        ITable actualTable =  dbInitRESMaster.getTableSnapshot("MST_DEVICEINFO");
        
        assertThat("Compare the actual number of rows in MST_DEVICEINFO",
                actualTable.getRowCount(), 
                is(equalTo(expectedDeviceInfo.getRowCount())));
        
        int i = 0;        
        for (Map<String, String> transaction : expectedDeviceInfo.getRows()) {
            assertThat("MST_DEVICEINFO (row"+i+" - storeid",
                    (String)actualTable.getValue(i, "StoreId"), 
                    is(equalTo(transaction.get("storeid"))));
            assertThat("MST_DEVICEINFO (row"+i+" - termid",
                    ((String)actualTable.getValue(i, "TerminalId")).trim(), 
                    is(equalTo(transaction.get("termid"))));
            String txId = "null";
            if (actualTable.getValue(i, "LastTxId") != null) {
            	txId = actualTable.getValue(i, "LastTxId").toString();
            }
            assertThat("MST_DEVICEINFO (row"+i+" - txid",
            		txId, 
                    is(equalTo(transaction.get("lasttxid"))));            
            String suspendtxid = "null";
            if (actualTable.getValue(i, "LastSuspendTxId") != null) {
            	suspendtxid = actualTable.getValue(i, "LastSuspendTxId").toString();
            }
            assertThat("MST_DEVICEINFO (row"+i+" - suspendtxid",
            		suspendtxid, 
                    is(equalTo(transaction.get("lastsuspendtxid"))));
            i++;
        }
    }
    
    @Then("returned Result Code should be {$code}")
    public final void returnedResultCodeShouldbe(final String code){
        int expectedResultCode = 0; //0 means OK
        int expectedExtendedResultCode = 0; //0 means OK
        
        if(code.equals("TRANSACTION_ALREADY_SAVED")){
            expectedResultCode = ResultBase.RES_ERROR_TXALREADY;
        }
        
        if(code.equals("DB_ERROR")){
            expectedResultCode = ResultBase.RES_ERROR_DB;
        }
        
        Assert.assertEquals(expectedResultCode,
                actualRsbase.getNCRWSSResultCode());
        Assert.assertEquals(expectedExtendedResultCode,
                actualRsbase.getNCRWSSExtendedResultCode());
    }
    
    @Then("I should have the resumed transaction: $resumedTransaction")
    public final void iShouldHaveTheResumedTransaction(
            final ExamplesTable resumedTrasaction){        
        Map<String, String> expectedResumedTransaction =
            resumedTrasaction.getRow(0);

        Assert.assertEquals("Compare the resumed transaction ResultCode", 
                Integer.parseInt(expectedResumedTransaction.get("ResultCode")), 
                actuaResumedTransaction.getNCRWSSResultCode());
        Assert.assertEquals("Compare the resumed transaction "
                + "ExtendedResultCode", 
                Integer.parseInt(
                        expectedResumedTransaction.get("ExtendedResultCode")), 
                actuaResumedTransaction.getNCRWSSExtendedResultCode());
        Assert.assertEquals("Compare the resumed transaction RetailStoreID",
                expectedResumedTransaction.get("RetailStoreID"), 
                actuaResumedTransaction.getRetailStoreID());
        Assert.assertEquals("Compare the resumed transaction Queue",
                expectedResumedTransaction.get("Queue"), 
                actuaResumedTransaction.getQueue());
        Assert.assertEquals("Compare the resumed transaction WorkstationID",
                expectedResumedTransaction.get("WorkstationID"), 
                actuaResumedTransaction.getWorkstationID());
        Assert.assertEquals("Compare the resumed transaction SequenceNumber",
                expectedResumedTransaction.get("SequenceNumber"), 
                actuaResumedTransaction.getSequenceNumber());
        String poslog = expectedResumedTransaction.get("POSLog").trim();
        if(!poslog.equals("")){
	        Assert.assertEquals("Compare the resumed transaction POSLog",
	                expectedResumedTransaction.get("POSLog"), 
	                actuaResumedTransaction.getPoslog());
        }
    }
    
    @Then("I should have the listed Transaction to be busted:"
            + " $expecetedBustTransList")
    public final void IShouldHaveTheListedTransactionToBeBusted(
            final ExamplesTable expecetedBustTransList){
        
        List<BusteredTransaction> actualBustList
            = actualBustTranslist.getBusteredTransactionList();
        
        Assert.assertEquals("Must have the exact number of"
                + " Transactions to be Busted", 
                expecetedBustTransList.getRowCount(), actualBustList.size());
        
        int i = 0;
        for (Map<String, String> transaction :
            expecetedBustTransList.getRows()) {
            BusteredTransaction bustTrans = actualBustList.get(i);
            assertThat("BusteredTransaction of (list"+i+" - WorkstationID",
                    bustTrans.getWorkstationid(), 
                    is(equalTo(transaction.get("WorkstationID"))));
            assertThat("BusteredTransaction of (list"+i+" - SequenceNumber",
                    bustTrans.getSequencenumber(), 
                    is(equalTo(transaction.get("SequenceNumber")))); 
            assertThat("BusteredTransaction of (list"+i+" - Total",
                    bustTrans.getTotal(), 
                            is(equalTo(Double.parseDouble(
                                    transaction.get("Total")))));
            
            assertThat("BusteredTransaction of (list"+1+" - BusinessDayDate",
            		bustTrans.getBusinessDayDate(),
            		is(equalTo(transaction.get("BusinessDayDate"))));
            assertThat("BusteredTransaction of (list"+1+" - ReceiptDateTime",
            		bustTrans.getReceiptDateTime(),
            		is(equalTo(transaction.get("ReceiptDateTime"))));
            if(transaction.get("OperatorID") != null){
				if (transaction.get("OperatorID").trim().equals("null")) {
					Assert.assertNull("BusteredTransaction of (list" + 1
							+ " - OperatorID", bustTrans.getEmployee());
				} else {
					assertThat("BusteredTransaction of (list" + 1
							+ " - OperatorID", bustTrans.getEmployee()
							.getNumber(),
							is(equalTo(transaction.get("OperatorID"))));
				}
            }
            if(transaction.get("OperatorName") != null){
				if (transaction.get("OperatorName").trim().equals("null")) {
					Assert.assertNull("BusteredTransaction of (list" + 1
							+ " - OperatorName", bustTrans.getEmployee());
				} else {
					assertThat("BusteredTransaction of (list" + 1
							+ " - OperatorName", bustTrans.getEmployee()
							.getName(),
							is(equalTo(transaction.get("OperatorName"))));
				}
            }
            
            i++;
        }
    }
     
    @Then("resultcode should be $code")
    public final void checkResultcode(final int code){
        assertEquals(code, actualRsbase.getNCRWSSResultCode());
    }
    
    @Then("status should be $code")
    public final void checkStatus(final String code){
        PosLogResp posResp = (PosLogResp) actualRsbase;
        assertEquals(code, posResp.getStatus());
    }
    
    @Then("status of suspendData should be status($status)")
    public final void checkSuspendStatus(final int status) {
        assertEquals(status, suspendData.getStatus());
    }
    
    @Then("error message should be ($message)")
    public final void checkErrMsg(final String message) {
        assertEquals(message, suspendData.getErrormessage());
    }
    
    
    @Then("List resultcode should be $code")
    public final void checkListResultcode(final int code){
        assertEquals(code, actualBustTranslist.getNCRWSSResultCode());
    }
    
    @Then("Suspend Xml retrieved should be: $expectedsuspendXml")
    public final void suspendXmlRetrievedShouldBe(final String expectedSuspendXml) {
        try {
            XmlSerializer<SuspendData> suspendDataSerializer = 
                new XmlSerializer<SuspendData>();
            String actualSuspendXml = suspendDataSerializer.marshallObj(SuspendData.class, suspendData, "UTF-8");
            System.out.println(actualSuspendXml);
            Assert.assertEquals("Compare the Suspend Data Xml retrieved",
                    expectedSuspendXml, actualSuspendXml);
        } catch (JAXBException e) {
           Assert.fail("Fail to compare the actual SuspendData Xml from its expected.");
        }
    }
 }
