package ncr.res.mobilepos.journalization.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.ServletContext;

import junit.framework.Assert;

import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.POSLogHandler;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.model.SearchedPosLog;
import ncr.res.mobilepos.journalization.model.SearchedPosLogs;
import ncr.res.mobilepos.journalization.resource.JournalizationResource;
import ncr.res.mobilepos.model.ResultBase;

@SuppressWarnings("deprecation")
public class JournalizationResourceSteps extends Steps{
    private JournalizationResource journalizationResource;
    private SearchedPosLog poslog;
    private PosLogResp journalResp;
    private ResultBase result;
    private String businessDate;

    private SearchedPosLogs searchedPosLogs;
    private DBInitiator dbInitMaster;
    private DBInitiator dbInitTransaction;

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        poslog = null;
    }

    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
        GlobalConstant.setCorpid("");
        System.setProperty("SERVERTYPE", "ENTERPRISE");
    }

    /**
     * A helper function to get the a given result Code
     * @param condition
     * @return
     */
    private int getResult(final String condition) {
        int res = -1; //Fail
        if ("OK".equals(condition)){
            res = 0;
        } else if ("TX_NOT_FOUND".equals(condition)){
            res = ResultBase.RES_ERROR_TXNOTFOUND;
        } else if ("DATABASE_ERROR".equals(condition)){
            res = ResultBase.RES_ERROR_DB;
        } else if ("ALREADY_PROCESSED".equals(condition)){
            res = ResultBase.RES_ERROR_TXALREADY;
        } else if ("ERROR_END".equals(condition)){
        	res = 1;
        } else if ("GENERAL_ERROR".equals(condition)){
            res = ResultBase.RES_ERROR_GENERAL;
        } else if ("INVALID_TRANSACTION".equals(condition)){
            res = ResultBase.RES_ERROR_TXINVALID;
        } else if ("TRANSACTION_ALREADY_VOIDED".equals(condition)){
            res = ResultBase.RES_ERROR_TXALREADYVOIDED;
        } else if ("JAXB_ERROR".equals(condition)){
            res = ResultBase.RES_ERROR_JAXB;
        } else if ("TILL_NO_UPDATE".equals(condition)) {
        	res = ResultBase.RES_TILL_NO_UPDATE;
        } else if ("TILL_NOT_EXIST".equals(condition)) {
        	res = ResultBase.RES_TILL_NOT_EXIST;
        } else if ("TILL_INVALID_BIZDATE".equals(condition)) {
        	res = ResultBase.RES_TILL_INVALID_BIZDATE;
        } else if ("TILL_SOD_FINISHED".equals(condition)) {
        	res = ResultBase.RES_TILL_SOD_FINISHED;
        } else if ("TILL_EOD_FINISHED".equals(condition)) {
        	res = ResultBase.RES_TILL_EOD_FINISHED;
        }
        return res;
    }

    @Given("an {$pathname} file path is {$path}")
    public final void setIOWLog(String pathName, String path){
    	Requirements.updateEnvironmentEntry("java:comp/env/" + pathName, path);
    }
    
    @Given("a Journalization Resource")
    public final void createResource() {
        ServletContext context = Requirements.getMockServletContext();
        journalizationResource = new JournalizationResource();
        try {
            Field journalizationContext = journalizationResource.getClass().getDeclaredField("context");
            journalizationContext.setAccessible(true);
            journalizationContext.set(journalizationResource, context);                        
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Cant load Mock Servlet Context.");
        } 
    }

    @Given("a RESMaster DBInitiator")
    public final void createDBInitiatorRESMaster() {
        dbInitMaster = new DBInitiator("RESMaster", DATABASE.RESMaster);
    }

    @Given("a RESTransaction DBInitiator")
    public final void createDBInitiatorRESTransaction() {
        dbInitTransaction = new DBInitiator("RESTransaction", DATABASE.RESTransaction);
    }

    @Given("a corpid{$corpid}")
    public final void givenCorpid(final String corpid) {
    	GlobalConstant.setCorpid(corpid);
    }
    
    @Given("TXL_SALES_JOURNAL is empty")
    public final void emptyDatabase() throws Exception {
    	dbInitTransaction.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/"
    			+ "TXL_SALES_JOURNAL.xml");
    }
    
    @Given("a TXL_POSLOG_COMPLETE_Search table database")
    public final void comnpleteDatabase() throws Exception {
    	dbInitTransaction.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/"
    			+ "TXL_POSLOG_COMPLETE_Search.xml");
    }
    
    @Given("TXL_SALES_VOIDED is empty")
    public final void emptyDatabaseSalesVoided() throws Exception {
    	dbInitTransaction.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/"
    			+ "TXL_SALES_VOIDED.xml");
    }
    
    @Given("TXL_SUMMARYRECEIPT_HISTORY is empty")
    public final void emptyDatabaseSummaryReceiptHistory() throws Exception {
    	dbInitTransaction.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/"
    	    			+ "TXL_SUMMARYRECEIPT_HISTORY.xml");
    }

    @Given("a batch of values for POSLOGs named $dataset")
    public final void setPoslogDatabase(final String dataset) throws Exception {
    	dbInitTransaction.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/"
    			+ dataset);
    }

    @Given("RESMaster $dataset table")
    public final void insertRESMasterDatabase(String dataset) throws Exception {
    	dbInitMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/"
    			+ dataset + ".xml");
    }

    @Given("$device is registered")
    public final void insertDeviceToDatabase(final String device) throws Exception {
    	dbInitMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/"
    			+ "AUT_DEVICES_" + device + ".xml");
    }

    @Given("device $device is $status")
    public final void updateDeviceOnline(final String device, final String status) 
    		throws Exception {
    	dbInitMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/"
    			+ "AUT_DEVICES_" + device + "_" + status + ".xml");
    }

    @Given("MST_DEVICEINFO table")
    public final void insertDeviceInfoToDatabase() throws Exception {
    	dbInitMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/"
    			+ "MST_DEVICEINFO.xml");
    }

    @Given("operator $operator is $status")
    public final void updateOperatorOnline(final String operator, final String status) 
    		throws Exception {
    	dbInitMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/"
    			+ "MST_USER_CREDENTIALS_" + operator + "_" + status + ".xml");
    }

    @Given("compute business date flag is $state")
    public final void updateBusinessDateFlag(final String state) throws Exception {
        dbInitMaster.ExecuteOperationNoKey(DatabaseOperation.UPDATE, "keyid",
                "test/ncr/res/mobilepos/journalization/resource/datasets/"
                + "PRM_SYSTEM_CONFIG_BusinessDateComputeFlag_" + state + ".xml");
    }

    @Given("manual business date is $bizdate")
    public final void updateBusinessDate(final String bizdate) throws Exception {
    	dbInitMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/"
    			+ "MST_BIZDAY_" + bizdate + ".xml");
    }

    @Given("normal transaction $txid is inserted")
    public final void normTxInsert(final String txid) throws Exception {
    	dbInitTransaction.ExecuteIdentityInsertOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/"
    			+ "TXL_POSLOG_Normal_" + txid + ".xml");
    }

    @Given("returned transaction $txid is inserted")
    public final void returnedTxInsert(final String txid) throws Exception {
    	dbInitTransaction.ExecuteIdentityInsertOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/"
    			+ "TXL_POSLOG_Returned_" + txid + ".xml");
    }

    @Given("voided transaction $txid is inserted")
    public final void voidedTxInsert(final String txid) throws Exception {
    	dbInitTransaction.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/"
    			+ "TXL_POSLOG_Voided_" + txid + ".xml");
    }
    
    @Given("a $filename table database")
    public final void givenDSFile(final String fileName) throws Exception {
    	dbInitTransaction.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/"
    			+ fileName + ".xml");
        GlobalConstant.setMaxSearchResults(5);
    }

    @Given("a system configuration has {$limit} key limit")
    public final void setConfigurationLimit(String limit) {
        if (limit.equals("null")) {
            limit = "0";
        }
        GlobalConstant.setMaxSearchResults(Integer.parseInt(limit));
    }
    
    @Given("MST_TILLIDINFO is empty")
    public final void tillInfoCleared() throws Exception {
    	dbInitMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/journalization/resource/datasets/MST_TILLIDINFO_EMPTY.xml");    	
    }
    
    @Given("a MST_TILLIDINFO with OK data")
    public final void tillInfoWithOKData() throws Exception {
    	dbInitMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
         		"test/ncr/res/mobilepos/journalization/resource/datasets/MST_TILLIDINFO_OK.xml");
    }
    
    @Given("a MST_TILLIDINFO where storeId doesn't exist")
    public final void tillInfoWithNGStoreIdData() throws Exception {
    	dbInitMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
         		"test/ncr/res/mobilepos/journalization/resource/datasets/MST_TILLIDINFO_NG_STORE_ID.xml");
    }
    
    @Given("a MST_TILLIDINFO where sod and eod flags are invalid for SOD")
    public final void tillInfoWithNGFlagsData() throws Exception {
    	dbInitMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
         		"test/ncr/res/mobilepos/journalization/resource/datasets/MST_TILLIDINFO_NG_FLAGS.xml");
    }
    
    @Given("that SERVERTYPE is set to STORE")
    public final void setStore() throws Exception {   	
    	System.setProperty("SERVERTYPE", "STORE");
    }
    
    @Given("that SERVERTYPE is set to ENTERPRISE")
    public final void setEnterprise() throws Exception {   	
    	System.setProperty("SERVERTYPE", "ENTERPRISE");
    }

    @When("querying transaction POSLOG with"
            + " terminalid{$terminalid} storeid{$storeid} txid{$txid}")
    public final void queryTransactionPOSLog(final String companyId, final int trainingMode, final String terminalid,
            final String storeid, final String txid) {
        poslog = journalizationResource.getPOSLogTransactionByNumber(companyId, terminalid, storeid, txid, txid, trainingMode, txid);
        result = poslog;
    }

    @When("executing journal log with xml{$poslogxml}"
            + " and trainingmode{$trainingmode}")
    public final void executeJournalLog(final String poslogXml, final int trainingMode) {
        journalResp = journalizationResource.journalize(poslogXml, trainingMode);
        result = journalResp;
    }

    @When("business date is retrieved")
    public final void getBusinessDate(String companyid, String storeid) {
        businessDate = journalizationResource.getBussinessDate(companyid, storeid);
    }

    /*
    @When("I search transactions with limit:{$limit}, from:{$from}, line:{$line}, storeId:{$storeId}, terminalId:{$deviceid}, itemName:{$itemName}, subcode4:{$subcode4}, itemNumber:{$itemNumber}, transactionNumberFrom:{$transactionNumberFrom}, transactionNumberTo:{$transactionNumberTo}, businessDate:{$businessDate}, fromDate:{$fromDate}, fromTime:{$fromTime}, toDate:{$toDate}, toTime:{$toTime}, type:{$type}, lang:{$language}, companyid:{$companyid}, trainingmode:{$traningmode}")
    public final void searchTransactions(
    		final String limit,
    		final String from,
    		final String line,
    		final String storeId, 
    		final String deviceid, 
    		final String itemName, 
    		final String subcode4,
    		final String itemid,
    		final String transactionNumberFrom, 
    		final String transactionNumberTo, 
    		final String businessDate, 
    		final String fromDate, 
    		final String fromTime, 
    		final String toDate, 
    		final String toTime, 
    		final String type,
    		final String language,
    		final String companyid,
    		final int trainingmode) {
        String limitTemp = limit.equalsIgnoreCase("null") ? null : limit;
        String storeIdTemp = storeId.equalsIgnoreCase("null") ? null : storeId;
        String terminalIdTemp = deviceid.equalsIgnoreCase("null") ? null : deviceid;
        String itemNameTemp = itemName.equalsIgnoreCase("null") ? null : itemName;
        String itemNumberTemp = itemid.equalsIgnoreCase("null") ? null : itemid;
        String transactionNumberFromTemp = transactionNumberFrom.equalsIgnoreCase("null") ? null : transactionNumberFrom;
        String transactionNumberToTemp = transactionNumberTo.equalsIgnoreCase("null") ? null : transactionNumberTo;
        String businessDateTemp = businessDate.equalsIgnoreCase("null") ? null : businessDate;
        String fromDateTemp = fromDate.equalsIgnoreCase("null") ? null : fromDate;
        String fromTimeTemp = fromTime.equalsIgnoreCase("null") ? null : fromTime;
        String toDateTemp = toDate.equalsIgnoreCase("null") ? null : toDate;
        String toTimeTemp = toTime.equalsIgnoreCase("null") ? null : toTime;
        String typeTemp = type.equalsIgnoreCase("null") ? null : type;
        String fromTemp = "null".equalsIgnoreCase(from) ? null : from;
        searchedPosLogs = journalizationResource.searchTransactions(
        		limitTemp,
        		fromTemp,
        		line,
        		storeIdTemp,
        		terminalIdTemp,
        		itemNameTemp,
        		subcode4,
          		itemNumberTemp, 
        		transactionNumberFromTemp, 
        		transactionNumberToTemp, 
        		businessDateTemp, 
        		fromDateTemp, 
        		fromTimeTemp, 
        		toDateTemp, 
        		toTimeTemp, 
        		typeTemp,
        		language,
        		companyid,
        		trainingmode);
    }
    */

    @Then("result should be {$condition}")
    public final void validateResult(final String condition) {
        int res = getResult(condition);
        assertThat("Assert Transaction was "
                + condition, result.getNCRWSSResultCode(), is(equalTo(res)));
    }

    @Then("extended result should be $exresult")
    public final void validateExResult(final int exres) {
        assertEquals(exres, result.getNCRWSSExtendedResultCode());
    }

    @Then("export the table please")
    public final void export() {
        try {
        	dbInitTransaction.exportTable("TXL_SALES_JOURNAL");
        } catch(Exception ex) {
            System.out.println(ex.getMessage());
        }
    }

    @Then("the poslog xml should be not empty")
    public final void validateXmlData() {
        Assert.assertTrue("the psolog xml should not be empty", poslog != null);
    }

    @Then("the POSLOG should be empty")
    public final void validatePOSLOGEmpty() {
        String actualdatetime = poslog.getTransaction().getBeginDateTime();

        assertThat("POSLOg should be empty", actualdatetime, is(equalTo(null)));
    }

    @Then("the POSLOG should should have BeginDateTime{$beginDateTime}")
    public final void validatePOSLogBeginDateTime(final String beginDateTime) {
        String actualdatetime = poslog.getTransaction().getBeginDateTime();

        assertThat("Compare POSLog Begin Date and Time",
                actualdatetime, is(equalTo(beginDateTime)));
    }

    @Then("the POSLOG should should have Transaction ID{$txid}")
    public final void validatePOSLogTxid(final String txid) {
        String actualSeqNum = poslog.getTransaction().getSequenceNo();

        assertThat("Compare POSLog transaction id",
                actualSeqNum, is(equalTo(txid)));
    }

    @Then("txid should be $txid")
    public final void checkTxid(final String txid) {
        assertEquals(txid, journalResp.getTxID());
    }

    @Then("status should be {$condition}")
    public final void checkStatus(final String condition) {
        int res = this.getResult(condition);
        assertThat("The Journal Response",
                journalResp.getStatus(), is(equalTo(String.valueOf(res))));
    }

    @Then("business date should be $bizdate")
    public final void validateBizDate(final String bizdate) {
        if(!bizdate.equals("now")) {
            assertEquals("Business date from MST_BIZDAY", bizdate, businessDate);
        } else {
            Calendar currentDate = Calendar.getInstance();
            SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
            String dateNow = formatter.format(currentDate.getTime());
            assertEquals("Calculated Business date ", dateNow, businessDate);
        }
    }
    
    @Then("I should have an empty business date")
    public final void emptyBizDate() {
    	assertThat("Bussiness date should be empty ", businessDate, is(equalTo(null)));       
    }

    @Then("the $dbTableName database table should have the following row(s): "
    		+ "$deviceTables")
    public final void showTableValues(String dbTableName, 
    		ExamplesTable expectedDataTable) {
        try {
            ITable actualDataTable = dbInitTransaction.getTableSnapshot(dbTableName);
            assertEquals("Compare the number of rows in " + dbTableName, 
            		expectedDataTable.getRowCount(),
                    actualDataTable.getRowCount());

            if (dbTableName.equals("TXL_SALES_JOURNAL")) {
                int i = 0;
                for (Map<String, String> expectedInfo : expectedDataTable.getRows()) {
                    assertEquals("Compare the RetailStoreId in TXL_SALES_JOURNAL row " 
                    		+ i, expectedInfo.get("RetailStoreId"), 
                    		String.valueOf(actualDataTable.getValue(i, "RetailStoreId"))
                    		.trim());
                    assertEquals("Compare the WorkstationId in TXL_SALES_JOURNAL row "
                            + i, expectedInfo.get("WorkstationId"), 
                            String.valueOf(actualDataTable.getValue(i, "WorkstationId")));
                    assertEquals("Compare the SequenceNumber in TXL_SALES_JOURNAL row "
                            + i, expectedInfo.get("SequenceNumber"),
                            String.valueOf(actualDataTable.getValue(i, "SequenceNumber")));
                    assertEquals("Compare the BusinessDayDate in TXL_SALES_JOURNAL row" 
                    		+ i, expectedInfo.get("BusinessDayDate"), 
                    		String.valueOf(actualDataTable.getValue(i, "BusinessDayDate")));
                    assertEquals("Compare the SystemDateTime in TXL_SALES_JOURNAL row" 
                    		+ i, expectedInfo.get("SystemDateTime"), 
                    		String.valueOf(actualDataTable.getValue(i, "SystemDateTime")));
                    assertEquals("Compare the TxType in TXL_SALES_JOURNAL row " + i, 
                    		expectedInfo.get("TxType"), 
                            String.valueOf(actualDataTable.getValue(i, "TxType")));
                    assertEquals("Compare the ServerId in TXL_SALES_JOURNAL row" + i,
                            expectedInfo.get("ServerId"),
                            String.valueOf(actualDataTable.getValue(i, "ServerId")));
                    assertEquals("Compare the Tx in TXL_SALES_JOURNAL row" + i,
                            expectedInfo.get("Tx"),
                            String.valueOf(actualDataTable.getValue(i, "Tx")));
                    i++;
                }
            } else if (dbTableName.equals("TXL_SALES_VOIDED")) {
            	int i  = 0;
            	for (Map<String, String> expectedInfo : expectedDataTable.getRows()) {
            		assertEquals("Compare RetailStoreId in TXL_SALES_VOIDED row " + i, 
            				expectedInfo.get("RetailStoreId"), 
            				String.valueOf(actualDataTable.getValue(i, "RetailStoreId"))
            				.trim());
            		assertEquals("Compare WorkstationId in TXL_SALES_VOIDED row " + i, 
            				expectedInfo.get("WorkstationId"), 
            				String.valueOf(actualDataTable.getValue(i, "WorkstationId"))
            				.trim());
            		assertEquals("Compare SequenceNumber in TXL_SALES_VOIDED row " + i, 
            				expectedInfo.get("SequenceNumber"), 
            				String.valueOf(actualDataTable.getValue(i, "SequenceNumber"))
            				.trim());
            		assertEquals("Compare BusinessDayDate in TXL_SALES_VOIDED row " + i, 
            				expectedInfo.get("BusinessDayDate"), 
            				String.valueOf(actualDataTable.getValue(i, "BusinessDayDate"))
            				.trim());
            		assertEquals("Compare TxType in TXL_SALES_VOIDED row " + i, 
            				expectedInfo.get("TxType"), 
            				String.valueOf(actualDataTable.getValue(i, "TxType"))
            				.trim());
            		i++;
            	}
            } else if (dbTableName.equals("TXL_SUMMARYRECEIPT_HISTORY")) {
            	int i  = 0;
            	for (Map<String, String> expectedInfo : expectedDataTable.getRows()) {
            		assertEquals("Compare RetailStoreId in TXL_SUMMARYRECEIPT_HISTORY row " + i, 
            				expectedInfo.get("RetailStoreId"), 
            				String.valueOf(actualDataTable.getValue(i, "RetailStoreId"))
            				.trim());
            		assertEquals("Compare WorkstationId in TXL_SUMMARYRECEIPT_HISTORY row " + i, 
            				expectedInfo.get("WorkstationId"), 
            				String.valueOf(actualDataTable.getValue(i, "WorkstationId"))
            				.trim());
            		assertEquals("Compare SequenceNumber in TXL_SUMMARYRECEIPT_HISTORY row " + i, 
            				expectedInfo.get("SequenceNumber"), 
            				String.valueOf(actualDataTable.getValue(i, "SequenceNumber"))
            				.trim());
            		assertEquals("Compare BusinessDayDate in TXL_SUMMARYRECEIPT_HISTORY row " + i, 
            				expectedInfo.get("BusinessDayDate"), 
            				String.valueOf(actualDataTable.getValue(i, "BusinessDayDate"))
            				.trim());
            		assertEquals("Compare RetailStoreId in TXL_SUMMARYRECEIPT_HISTORY row " + i, 
            				expectedInfo.get("IssuedRetailStoreId"), 
            				String.valueOf(actualDataTable.getValue(i, "IssuedRetailStoreId"))
            				.trim());
            		assertEquals("Compare WorkstationId in TXL_SUMMARYRECEIPT_HISTORY row " + i, 
            				expectedInfo.get("IssuedWorkstationId"), 
            				String.valueOf(actualDataTable.getValue(i, "IssuedWorkstationId"))
            				.trim());
            		assertEquals("Compare SequenceNumber in TXL_SUMMARYRECEIPT_HISTORY row " + i, 
            				expectedInfo.get("IssuedSequenceNumber"), 
            				String.valueOf(actualDataTable.getValue(i, "IssuedSequenceNumber"))
            				.trim());
            		assertEquals("Compare BusinessDayDate in TXL_SUMMARYRECEIPT_HISTORY row " + i, 
            				expectedInfo.get("IssuedBusinessDayDate"), 
            				String.valueOf(actualDataTable.getValue(i, "IssuedBusinessDayDate"))
            				.trim());
            		i++;
            	}
            } else if (dbTableName.equals("TXU_POS_CTRL")) {
            	int i  = 0;
            	// |StoreId|TerminalNo|OpeCode|
            	for (Map<String, String> expectedInfo : expectedDataTable.getRows()) {
            		assertEquals("Compare StoreId in TXU_POS_CTRL row " + i, 
            				expectedInfo.get("StoreId"), 
            				String.valueOf(actualDataTable.getValue(i, "StoreId"))
            				.trim());
            		assertEquals("Compare TerminalNo in TXU_POS_CTRL row " + i, 
            				expectedInfo.get("TerminalNo"), 
            				String.valueOf(actualDataTable.getValue(i, "TerminalNo"))
            				.trim());
            		assertEquals("Compare OpeCode in TXU_POS_CTRL row " + i, 
            				expectedInfo.get("OpeCode"), 
            				String.valueOf(actualDataTable.getValue(i, "OpeCode")).toUpperCase()
            				.trim());
            		i++;
            	}
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in " + dbTableName + ".");
        }
    }

    @Then("I should get the following: $expected")
    public final void testPOSLogs(ExamplesTable expectedDataTable) throws IOException {
        try {
            assertEquals("Compare the number of rows in TXL_SALES_JOURNAL", 
            		expectedDataTable.getRowCount(), 
            		searchedPosLogs.getPosLogs().size());

            int i = 0;
            for (Map<String, String> expectedInfo : expectedDataTable.getRows()) {
                assertEquals("Compare the WorkstationId in TXL_SALES_JOURNAL row" + i, 
                		expectedInfo.get("WorkstationId"), 
                		searchedPosLogs.getPosLogs().get(i).getPosLog().getTransaction()
                		.getWorkStationID());
                assertEquals("Compare the SequenceNumber in TXL_SALES_JOURNAL row" + i, 
                		expectedInfo.get("SequenceNumber"), 
                		searchedPosLogs.getPosLogs().get(i).getPosLog().getTransaction()
                		.getSequenceNo());
                assertEquals("Compare the BusinessDayDate in TXL_SALES_JOURNAL row" + i, 
                		expectedInfo.get("BusinessDayDate"), 
                		searchedPosLogs.getPosLogs().get(i).getPosLog().getTransaction()
                		.getBusinessDayDate());
                assertEquals("Compare the TxType in TXL_SALES_JOURNAL row" + i, 
                		expectedInfo.get("TxType"), 
                		POSLogHandler.getTransactionType(searchedPosLogs.getPosLogs().get(i).getPosLog()));                
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in TXL_SALES_JOURNAL.");
        }
    }

    @Then ("the JSON should have the following format : $expectedJson")
    public final void theJsonShouldHaveTheFollowingJSONFormat(
            String expectedJson) {
        try {
            JsonMarshaller<ResultBase> jsonMarshaller = 
            		new JsonMarshaller<ResultBase>();

            String actualJson = jsonMarshaller.marshall(searchedPosLogs);
            
            System.out.println(actualJson);
            
            // compare to json strings regardless of property ordering
            JSONAssert.assertEquals(expectedJson, actualJson, JSONCompareMode.NON_EXTENSIBLE);
        } catch (Exception e) {
            Assert.fail("Failed to verify the PromotionResponse JSON format");
            e.printStackTrace();
        }
    }

    @Then("I should get {$noOfResult} list")
    public final void testListSize(int expected){
        assertEquals(expected, searchedPosLogs.getPosLogs().size());
    }

    @Then("I should get {$code} resultcode")
    public final void testResultCode(int expected){
        assertEquals(expected, searchedPosLogs.getNCRWSSResultCode());
    }

    @Then("table {$tableName} will have {$rowCount} row(s)")
    public final void getTableRowCount(String tableName, String rowCount) {
    	ITable table = dbInitTransaction.getTableSnapshot(tableName);
    	
    	int rows = table.getRowCount();
    	
    	assertEquals(tableName + " rowCount", Integer.parseInt(rowCount), rows);
    }
}
