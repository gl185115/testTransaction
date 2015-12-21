package ncr.res.mobilepos.consolidation.dao.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;
import ncr.res.mobilepos.consolidation.dao.SQLServerConsolidationDAO;
import ncr.res.mobilepos.consolidation.dao.TransactionInfo;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;

import org.dbunit.DatabaseUnitException;
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

public class SQLServerConsolidationDAOSteps extends Steps {
    private DBInitiator dbInit = null;
    private final String nUll = "NULL";
    private final String datasetPath =
        "test/ncr/res/mobilepos/consolidation/";    
    private SQLServerConsolidationDAO testSqlServerConsolidationDao = null;
    List<TransactionInfo> actualTransactionInfoList = null;
    XmlSerializer<PosLog> posLogSerializer = new XmlSerializer<PosLog>();
    
    @BeforeScenario
    public final void setUpClass() throws DatabaseUnitException,
    SQLException, Exception
    {        
        Requirements.SetUp();
        dbInit = new DBInitiator("SQLServerConsolidationDAOSteps", DATABASE.RESTransaction);
        try{
        dbInit.ExecuteOperation( DatabaseOperation.DELETE_ALL,
                datasetPath + "CONSOLIDATION_TABLES_EMPTY.xml");
        }catch(DatabaseUnitException db){
            
        }catch(SQLException sql){
            
        }catch (Exception e) {
            
        }
        
        //Set the Company ID as 000000000000
        GlobalConstant.setCorpid("000000000000");
    }
    
    @AfterScenario
    public final void tearDownClass(){
        Requirements.TearDown();
        //Set the Company ID as 000000000000
        GlobalConstant.setCorpid("");
    }
    
    @Given("a {$adj} batch of values for POSLOGs named $dataset")
    public final void aBatchOfValuesForPOSLOGsNamed(
            final String adj, final String dataset)
    throws DatabaseUnitException, SQLException, Exception{
        
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                datasetPath + dataset);                
    }
    
    @Given("a sample {$dataxml} poslog xml entry in database")
    public final void insertSampleData(final String dataxml)
    throws Exception{
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                datasetPath + dataxml + ".xml");
    }
    
    @Given("a batch of values for $dataset")
    public final void aBatchOfValuesFor(final String dataset)
    throws DatabaseUnitException, SQLException, Exception{
        aBatchOfValuesForPOSLOGsNamed("", dataset);
    }
    
    @Given("an instance of SQLServerConsolidationDAO")
    public final void  anInstanceOfSQLServerConsolidationDAO()
    throws DaoException {
        testSqlServerConsolidationDao = new SQLServerConsolidationDAO();
    }
    
    @Then("its Database Manager must have been settled")
    public final void itsDatabaseManagerMustHaveBeenSettled()
    throws SecurityException, NoSuchFieldException{
        assertNotNull(testSqlServerConsolidationDao);
        assertNotNull(testSqlServerConsolidationDao.getClass()
                .getDeclaredField("dbManager"));
    }
    
    @When("I select a non-consolidated transaction information")
    public final void ISelectANonConsolidatedTransactionInformation() {
        try {
            actualTransactionInfoList = testSqlServerConsolidationDao
                .selectPOSLogXML("0");
        } catch (DaoException e) {
            e.printStackTrace();
            Assert.fail("Failed to get Transaction Informatiuon"
                    + " from POSLog Select");
            
        }
    }
    
    @When("I consolidate top {$count} items")
    public final void  IConsolidateTopItems(final int count)
    throws JAXBException{
        TransactionInfo nonConsTrans = null;
        
        
        for(int i = 0; i < count; i++){
            
            if(actualTransactionInfoList.size() != 1){
                nonConsTrans = actualTransactionInfoList.remove(0);
            }else{
                nonConsTrans = actualTransactionInfoList.get(0);
            }
            
            PosLog poslog = posLogSerializer.unMarshallXml(nonConsTrans.getTx(),
                    PosLog.class);
            
            try {
                testSqlServerConsolidationDao.doPOSLogJournalization(poslog,
                        nonConsTrans);
            } catch (DaoException e) {
                Assert.fail("Unable to consolidate the POSLog XML: \n"
                        +poslog.getTransaction().getSequenceNo() + " " 
                        + poslog.getTransaction().getWorkStationID());
                e.printStackTrace();
            }
        }
    }
    
    @Then("I should get {$count} list of transaction informations")
    public final void IShouldGetListOfTransactionInformations(final int count) {
        assertThat(actualTransactionInfoList.size(), is(equalTo(count)));
    }
    
    @Then("I should get no list transaction information")
    public final void IShouldGetNoListTransactionInformation(){
        assertThat("Transction Information", actualTransactionInfoList, 
                is(equalTo((List<TransactionInfo>)
                        (new ArrayList<TransactionInfo>()))));
    }
    
    @SuppressWarnings("deprecation")
    @Then("I should get the following TXU_TOTAL_ITEMDAY results:"
            + " $expecteditems")
    public final void  IShouldGetTheFollowingTXU_TOTAL_ITEMDAYResults(
            final ExamplesTable expecteditems) throws DataSetException{ 
        ITable actualTable = dbInit.getTableSnapshot("TXU_TOTAL_ITEMDAY");
        
        assertThat("The number of rows in TXU_TOTAL_ITEMDAY",
                actualTable.getRowCount(),
                is(equalTo(expecteditems.getRowCount())));
        
        int i = 0;        
        for (Map<String, String> item : expecteditems.getRows()) {
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - corpid",
                    item.get("corpid"), 
                    is(equalTo((String)actualTable.getValue(i, "corpid"))));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - storeid",
                    item.get("storeid"), 
                    is(equalTo((String)actualTable.getValue(i, "storeid"))));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - summarydateid",
                    Date.valueOf(item.get("summarydateid")),
                    is(equalTo((Date)actualTable
                            .getValue(i, "summarydateid"))));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - salesprice",
                    Double.parseDouble(item.get("salesprice")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "salesprice")).doubleValue())));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - line", item.get("line"), 
                    is(equalTo((String)actualTable.getValue(i, "line"))));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - class", item.get("class"),
                    is(equalTo((String)actualTable.getValue(i, "class"))));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - SalesItemCnt",
                    Double.parseDouble(item.get("SalesItemCnt")),
                    is(equalTo(((BigDecimal)actualTable.getValue(i,
                            "SalesItemCnt")).doubleValue())));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - SalesCostAmt",
                    Double.parseDouble(item.get("SalesCostAmt")), 
                    is(equalTo(((BigDecimal)actualTable.getValue(i,
                            "SalesCostAmt")).doubleValue())));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - SalesSalesAmt",
                    Double.parseDouble(item.get("SalesSalesAmt")), 
                    is(equalTo(((BigDecimal)actualTable.getValue(i,
                            "SalesSalesAmt")).doubleValue())));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - ReturnItemCnt",
                    Double.parseDouble(item.get("ReturnItemCnt")), 
                    is(equalTo(((BigDecimal)actualTable.getValue(i,
                            "ReturnItemCnt")).doubleValue())));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - ReturnCostAmt",
                    Double.parseDouble(item.get("ReturnCostAmt")), 
                    is(equalTo(((BigDecimal)actualTable.getValue(i,
                            "ReturnCostAmt")).doubleValue())));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - ReturnSalesAmt",
                    Double.parseDouble(item.get("ReturnSalesAmt")), 
                    is(equalTo(((BigDecimal)actualTable.getValue(i,
                            "ReturnSalesAmt")).doubleValue())));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - VoidItemCnt",
                    Double.parseDouble(item.get("VoidItemCnt")), 
                    is(equalTo(((BigDecimal)actualTable.getValue(i,
                            "VoidItemCnt")).doubleValue())));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - VoidCostAmt",
                    Double.parseDouble(item.get("VoidCostAmt")), 
                    is(equalTo(((BigDecimal)actualTable.getValue(i,
                            "VoidCostAmt")).doubleValue())));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - VoidSalesAmt",
                    Double.parseDouble(item.get("VoidSalesAmt")), 
                    is(equalTo(((BigDecimal)actualTable.getValue(i,
                            "VoidSalesAmt")).doubleValue())));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - DcItemCnt",
                    Double.parseDouble(item.get("DcItemCnt")), 
                    is(equalTo(((BigDecimal)actualTable.getValue(i,
                            "DcItemCnt")).doubleValue())));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - DcCostAmt",
                    Double.parseDouble(item.get("DcCostAmt")), 
                    is(equalTo(((BigDecimal)actualTable.getValue(i,
                            "DcCostAmt")).doubleValue())));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - DcCostAmt",
                    Double.parseDouble(item.get("DcSalesAmt")), 
                    is(equalTo(((BigDecimal)actualTable.getValue(i,
                            "DcSalesAmt")).doubleValue())));
            i++;
        }        
    }
    
    @Then("I should get the following TXU_TOTAL_ACNTDAY results:"
            + " $expecteditems")
    public final void IShouldGetTheFollowingTXU_TOTAL_ACNTDAYResults(
            final ExamplesTable expecteditems) throws DataSetException{
        ITable actualTable = dbInit.getTableSnapshot("TXU_TOTAL_ACNTDAY");
        
        assertThat("The number of rows in TXU_TOTAL_ACNTDAY",
                expecteditems.getRowCount(),
                is(equalTo(actualTable.getRowCount())));
        
        int i = 0;        
        for (Map<String, String> item : expecteditems.getRows()) {
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - corpid",
                    item.get("corpid"), 
                    is(equalTo((String)actualTable.getValue(i, "corpid"))));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - storeid",
                    item.get("storeid"), 
                    is(equalTo((String)actualTable.getValue(i, "storeid"))));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - terminalid",
                    item.get("terminalid"), 
                    is(equalTo((String)actualTable.getValue(i, "terminalid"))));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - summarydateid",
                    Date.valueOf(item.get("summarydateid")),
                    is(equalTo((Date)actualTable.getValue(i,
                            "summarydateid"))));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - timezonecode",
                    Double.parseDouble(item.get("timezonecode")), 
                    is(equalTo(((BigDecimal)actualTable.getValue(i,
                            "timezonecode")).doubleValue())));        
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - dpt",
                    item.get("dpt"), 
                    is(equalTo((String)actualTable.getValue(i, "dpt"))));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - line", item.get("line"), 
                    is(equalTo((String)actualTable.getValue(i, "line"))));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - class", item.get("class"),
                    is(equalTo((String)actualTable.getValue(i, "class"))));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - Operatorcode",
                    item.get("Operatorcode"), 
                    is(equalTo((String)actualTable
                            .getValue(i, "Operatorcode"))));            
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - SalesItemCnt",
                    Double.parseDouble(item.get("SalesItemCnt")),
                    is(equalTo(((BigDecimal)actualTable.getValue(i,
                            "SalesItemCnt")).doubleValue())));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - SalesCostAmt",
                    Double.parseDouble(item.get("SalesCostAmt")), 
                    is(equalTo(((BigDecimal)actualTable.getValue(i,
                            "SalesCostAmt")).doubleValue())));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - SalesSalesAmt",
                    Double.parseDouble(item.get("SalesSalesAmt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "SalesSalesAmt")).doubleValue())));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - ReturnItemCnt",
                    Double.parseDouble(item.get("ReturnItemCnt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "ReturnItemCnt")).doubleValue())));
            assertThat("TXU_TOTAL_ITEMDAY (row"+i+" - ReturnCostAmt",
                    Double.parseDouble(item.get("ReturnCostAmt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "ReturnCostAmt")).doubleValue())));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - ReturnSalesAmt",
                    Double.parseDouble(item.get("ReturnSalesAmt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "ReturnSalesAmt")).doubleValue())));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - VoidItemCnt",
                    Double.parseDouble(item.get("VoidItemCnt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "VoidItemCnt")).doubleValue())));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - VoidCostAmt",
                    Double.parseDouble(item.get("VoidCostAmt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "VoidCostAmt")).doubleValue())));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - VoidSalesAmt",
                    Double.parseDouble(item.get("VoidSalesAmt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "VoidSalesAmt")).doubleValue())));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - DcItemCnt",
                    Double.parseDouble(item.get("DcItemCnt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "DcItemCnt")).doubleValue())));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - DcCostAmt",
                    Double.parseDouble(item.get("DcCostAmt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "DcCostAmt")).doubleValue())));
            assertThat("TXU_TOTAL_ACNTDAY (row"+i+" - DcCostAmt",
                    Double.parseDouble(item.get("DcSalesAmt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "DcSalesAmt")).doubleValue())));
            i++;
        }    
    }
    
    @Then("I should get the following TXU_TOTAL_DPTSUMDAY results:"
            + " $expecteditems")
    public final void IShouldGetTheFollowingTXU_TOTAL_DPTSUMDAYResults(
            final ExamplesTable expecteditems) throws DataSetException{
        ITable actualTable = dbInit.getTableSnapshot("TXU_TOTAL_DPTSUMDAY");
        
        assertThat("The number of rows in TXU_TOTAL_DPTSUMDAY",
                expecteditems.getRowCount(),
                is(equalTo(actualTable.getRowCount())));
        
        int i = 0;        
        for (Map<String, String> item : expecteditems.getRows()) {
            assertThat("TXU_TOTAL_DPTSUMDAY (row"+i+" - corpid",
                    item.get("corpid"), 
                    is(equalTo((String)actualTable.getValue(i, "corpid"))));
            assertThat("TXU_TOTAL_DPTSUMDAY (row"+i+" - storeid",
                    item.get("storeid"), 
                    is(equalTo((String)actualTable.getValue(i, "storeid"))));
            assertThat("TXU_TOTAL_DPTSUMDAY (row"+i+" - dpt", item.get("dpt"),
                    is(equalTo((String)actualTable.getValue(i, "dpt"))));
            assertThat("TXU_TOTAL_DPTSUMDAY (row"+i+" - summarydateid",
                    Date.valueOf(item.get("summarydateid")),
                    is(equalTo((Date)actualTable
                            .getValue(i, "summarydateid"))));
            assertThat("TXU_TOTAL_DPTSUMDAY (row"+i+" - timezonecode",
                    Double.parseDouble(item.get("timezonecode")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "timezonecode")).doubleValue())));
            assertThat("TXU_TOTAL_DPTSUMDAY (row"+i+" - SalesItemCnt",
                    Double.parseDouble(item.get("SalesItemCnt")),
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "SalesItemCnt")).doubleValue())));
            assertThat("TXU_TOTAL_DPTSUMDAY (row"+i+" - SalesSalesAmt",
                    Double.parseDouble(item.get("SalesSalesAmt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "SalesSalesAmt")).doubleValue())));
            assertThat("TXU_TOTAL_DPTSUMDAY (row"+i+" - ReturnItemCnt",
                    Double.parseDouble(item.get("ReturnItemCnt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "ReturnItemCnt")).doubleValue())));
            assertThat("TXU_TOTAL_DPTSUMDAY (row"+i+" - ReturnSalesAmt",
                    Double.parseDouble(item.get("ReturnSalesAmt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "ReturnSalesAmt")).doubleValue())));
            assertThat("TXU_TOTAL_DPTSUMDAY (row"+i+" - VoidItemCnt",
                    Double.parseDouble(item.get("VoidItemCnt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "VoidItemCnt")).doubleValue())));
            assertThat("TXU_TOTAL_DPTSUMDAY (row"+i+" - VoidSalesAmt",
                    Double.parseDouble(item.get("VoidSalesAmt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "VoidSalesAmt")).doubleValue())));
            assertThat("TXU_TOTAL_DPTSUMDAY (row"+i+" - DcItemCnt",
                    Double.parseDouble(item.get("DcItemCnt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "DcItemCnt")).doubleValue())));
            assertThat("TXU_TOTAL_DPTSUMDAY (row"+i+" - DcCostAmt",
                    Double.parseDouble(item.get("DcSalesAmt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "DcSalesAmt")).doubleValue())));
            i++;
        }    
    }
    
    @Then("I should get the following TXU_TOTAL_FINANCIAL results:"
            + " $expecteditems")
    public final void IShouldGetTheFollowingTXU_TOTAL_FINANCIAL(
            final ExamplesTable expecteditems) throws DataSetException{
        ITable actualTable = dbInit.getTableSnapshot("TXU_TOTAL_FINANCIAL");
        
        assertThat("The number of rows in TXU_TOTAL_FINANCIAL",
                actualTable.getRowCount(),
                is(equalTo(expecteditems.getRowCount())));
        
        int i = 0;        
        for (Map<String, String> item : expecteditems.getRows()) {
            assertThat("TXU_TOTAL_FINANCIAL (row"+i+" - corpid",
                    item.get("corpid"), 
                    is(equalTo((String)actualTable.getValue(i, "corpid"))));
            assertThat("TXU_TOTAL_FINANCIAL (row"+i+" - storeid",
                    item.get("storeid"), 
                    is(equalTo((String)actualTable.getValue(i, "storeid"))));
            assertThat("TXU_TOTAL_FINANCIAL (row"+i+" - terminalid",
                    item.get("terminalid"), 
                    is(equalTo((String)actualTable.getValue(i, "terminalid"))));
            assertThat("TXU_TOTAL_FINANCIAL (row"+i+" - accountcode",
                    item.get("accountcode"), 
                    is(equalTo((String)actualTable
                            .getValue(i, "accountcode"))));
            assertThat("TXU_TOTAL_FINANCIAL (row"+i+" - accountsubcode",
                    item.get("accountsubcode"), 
                    is(equalTo((String)actualTable
                            .getValue(i, "accountsubcode"))));
            assertThat("TXU_TOTAL_FINANCIAL (row"+i+" - summarydateid",
                    Date.valueOf(item.get("summarydateid")),
                    is(equalTo((Date)actualTable
                            .getValue(i, "summarydateid"))));
            assertThat("TXU_TOTAL_FINANCIAL (row"+i+" - debtor",
                    Double.parseDouble(item.get("debtor")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "debtor")).doubleValue())));
            assertThat("TXU_TOTAL_FINANCIAL (row"+i+" - creditor",
                    Double.parseDouble(item.get("creditor")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "creditor")).doubleValue())));

            i++;
        }    
    }
    
    @Then("I should get the following TXU_TOTAL_DRAWER_FINANCIAL results:"
            + " $expecteditems")
    public final void IShouldGetTheFollowingTXU_TOTAL_DRAWER_FINANCIAL(
            final ExamplesTable expecteditems) throws DataSetException{
        ITable actualTable =
            dbInit.getTableSnapshot("TXU_TOTAL_DRAWER_FINANCIAL");
        
        assertThat("The number of rows in TXU_TOTAL_DRAWER_FINANCIAL",
                actualTable.getRowCount(),
                is(equalTo(expecteditems.getRowCount())));
        
        int i = 0;        
        for (Map<String, String> item : expecteditems.getRows()) {
            assertThat("TXU_TOTAL_DRAWER_FINANCIAL (row"+i+" - corpid",
                    item.get("corpid"), 
                    is(equalTo(((String)actualTable
                            .getValue(i, "corpid")).trim())));
            assertThat("TXU_TOTAL_DRAWER_FINANCIAL (row"+i+" - storeid",
                    item.get("storeid"), 
                    is(equalTo(((String)actualTable
                            .getValue(i, "storeid")).trim())));    
            assertThat("TXU_TOTAL_DRAWER_FINANCIAL (row"+i+" - printerid",
                    item.get("printerid"), 
                    is(equalTo(actualTable
                            .getValue(i, "printerid").toString().trim())));
            assertThat("TXU_TOTAL_DRAWER_FINANCIAL (row"+i+" - accountcode",
                    item.get("accountcode"), 
                    is(equalTo(((String)actualTable
                            .getValue(i, "accountcode")).trim())));
            assertThat("TXU_TOTAL_DRAWER_FINANCIAL (row"+i+" - accountsubcode",
                    item.get("accountsubcode"), 
                    is(equalTo(((String)actualTable
                            .getValue(i, "accountsubcode")).trim())));
            assertThat("TXU_TOTAL_DRAWER_FINANCIAL (row"+i+" - summarydateid",
                    Date.valueOf(item.get("summarydateid")),
                    is(equalTo((Date)actualTable
                            .getValue(i, "summarydateid"))));
            assertThat("TXU_TOTAL_DRAWER_FINANCIAL (row"+i+" - debtor",
                    Double.parseDouble(item.get("debtor")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "debtor")).doubleValue())));
            assertThat("TXU_TOTAL_DRAWER_FINANCIAL (row"+i+" - creditor",
                    Double.parseDouble(item.get("creditor")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "creditor")).doubleValue())));
            i++;
        }    
    }
    
    @Then("I should get the following TXU_TOTAL_GUESTDPTDAY results:"
            + " $expecteditems")
    public final void IShouldGetTheFollowingTXU_TOTAL_GUESTDPTDAYResults(
            final ExamplesTable expecteditems) throws DataSetException{
        ITable actualTable = dbInit.getTableSnapshot("TXU_TOTAL_GUESTDPTDAY");
        
        assertThat("The number of rows in TXU_TOTAL_GUESTDPTDAY",
                expecteditems.getRowCount(),
                is(equalTo(actualTable.getRowCount())));
        
        int i = 0;        
        for (Map<String, String> item : expecteditems.getRows()) {
            assertThat("TXU_TOTAL_GUESTDPTDAY (row"+i+" - corpid",
                    item.get("corpid"), 
                    is(equalTo((String)actualTable.getValue(i, "corpid"))));
            assertThat("TXU_TOTAL_GUESTDPTDAY (row"+i+" - storeid",
                    item.get("storeid"), 
                    is(equalTo((String)actualTable.getValue(i, "storeid"))));
            assertThat("TXU_TOTAL_GUESTDPTDAY (row"+i+" - terminalid",
                    item.get("terminalid"), 
                    is(equalTo((String)actualTable.getValue(i, "terminalid"))));
            assertThat("TXU_TOTAL_GUESTDPTDAY (row"+i+" - deviceno",
                    item.get("deviceno"), 
                    is(equalTo(((String)actualTable
                            .getValue(i, "deviceno")).trim())));    
            assertThat("TXU_TOTAL_GUESTDPTDAY (row"+i+" - Operatorcode",
                    item.get("Operatorcode"), 
                    is(equalTo((String)actualTable
                            .getValue(i, "Operatorcode"))));    
            assertThat("TXU_TOTAL_GUESTDPTDAY (row"+i+" - summarydateid",
                    Date.valueOf(item.get("summarydateid")),
                    is(equalTo((Date)actualTable
                            .getValue(i, "summarydateid"))));
            assertThat("TXU_TOTAL_GUESTDPTDAY (row"+i+" - timezonecode",
                    Double.parseDouble(item.get("timezonecode")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "timezonecode")).doubleValue())));
            assertThat("TXU_TOTAL_GUESTDPTDAY (row"+i+" - dpt", item.get("dpt"),
                    is(equalTo((String)actualTable.getValue(i, "dpt"))));
            assertThat("TXU_TOTAL_GUESTDPTDAY (row"+i+" - SalesGuestCnt",
                    Double.parseDouble(item.get("SalesGuestCnt")),
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "SalesGuestCnt")).doubleValue())));
            assertThat("TXU_TOTAL_GUESTDPTDAY (row"+i+" - ReturnGuestCnt",
                    Double.parseDouble(item.get("ReturnGuestCnt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "ReturnGuestCnt")).doubleValue())));
            assertThat("TXU_TOTAL_GUESTDPTDAY (row"+i+" - VoidGuestCnt",
                    Double.parseDouble(item.get("VoidGuestCnt")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "VoidGuestCnt")).doubleValue())));
            i++;
        }    
    }
    
    @Then("I should get the following TXL_JOURNAL results: $expecteditems")
    public final void IShouldGetTheFollowingTXL_JOURNALResults(
            final ExamplesTable expecteditems) throws DataSetException{
        ITable actualTable = dbInit.getTableSnapshot("TXL_JOURNAL");
        
        assertThat("The number of rows in TXL_JOURNAL",
                expecteditems.getRowCount(),
                is(equalTo(actualTable.getRowCount())));
        
          String expectedplu = null;
          String expecteddescription = null;
          String expectedaccountcode= null;
          
          Double expecteditemcnt= null;
          Double expectedactsalesamount= null;
          Double expectedreturnflag= null;
          Double expectedvoidflag= null;
          Double expectedcancelflag= null;
          Double expectedDebtor= null;
          Double expectedcreditor= null;
          String expectedcustomerid= null;
          Double expectedoriginaltranno= null;
          
          Double actualitemcnt= null;
          Double actualactsalesamount= null;
          Double actualreturnflag= null;
          Double actualvoidflag= null;
          Double actualcancelflag= null;
          Double actualDebtor= null;
          Double actualcreditor= null;
          String actualcustomerid= null;
          Double actualoriginaltranno= null;
        
        int i = 0;        
        for (Map<String, String> item : expecteditems.getRows()) {
            
            assertThat("TXL_JOURNAL (row"+i+" - corpid", item.get("corpid"), 
                    is(equalTo((String)actualTable.getValue(i, "corpid"))));
            assertThat("TXL_JOURNAL (row"+i+" - storeid", item.get("storeid"), 
                    is(equalTo((String)actualTable.getValue(i, "storeid"))));
            assertThat("TXL_JOURNAL (row"+i+" - termid", item.get("termid"), 
                    is(equalTo((String)actualTable.getValue(i, "termid"))));
            assertThat("TXL_JOURNAL (row"+i+" - tranno",
                    Double.parseDouble(item.get("tranno")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "tranno")).doubleValue())));    
            assertThat("TXL_JOURNAL (row"+i+" - tranlineno",
                    Double.parseDouble(item.get("tranlineno")), 
                    is(equalTo(((BigDecimal)actualTable
                            .getValue(i, "tranlineno")).doubleValue())));
            assertThat("TXL_JOURNAL (row"+i+" - operatorcode",
                    item.get("operatorcode"), 
                    is(equalTo((String)actualTable
                            .getValue(i, "operatorcode"))));                
            assertThat("TXL_JOURNAL (row"+i+" - summarydateid",
                    Date.valueOf(item.get("summarydateid")),
                    is(equalTo((Date)actualTable
                            .getValue(i, "summarydateid"))));
            
            /////////////////////////////////////////////////////////          
            expectedplu = (item.get("plu").equals(nUll))
                              ? null : item.get("plu");
            expecteddescription = (item.get("description").equals(nUll))
                              ? null : item.get("description");
            expectedaccountcode = (item.get("accountcode").equals(nUll))
                              ? null : item.get("accountcode");
            expecteditemcnt =  (item.get("itemcnt").equals(nUll))
                              ? null :  Double.parseDouble(item.get("itemcnt"));
            expectedactsalesamount = (item.get("actsalesamount").equals(nUll))
                              ? null : Double.parseDouble(
                                      item.get("actsalesamount"));
            expectedreturnflag = (item.get("returnflag").equals(nUll))
                        ? null : Double.parseDouble(item.get("returnflag"));
            expectedvoidflag = (item.get("voidflag").equals(nUll))
                        ? null : Double.parseDouble(item.get("voidflag"));
            expectedcancelflag = (item.get("cancelflag").equals(nUll))
                        ? null : Double.parseDouble(item.get("cancelflag"));
            expectedDebtor = (item.get("debtor").equals(nUll))
                        ? null : Double.parseDouble(item.get("debtor"));
            expectedcreditor = (item.get("creditor").equals(nUll))
                        ? null : Double.parseDouble(item.get("creditor"));
            expectedcustomerid = (item.get("customerid").equals(nUll))
                        ? null : item.get("customerid");
            expectedoriginaltranno = (item.get("originaltranno").equals(nUll))
                        ? null : Double.parseDouble(item.get("originaltranno"));
            
            actualactsalesamount =
                (actualTable.getValue(i, "actsalesamount") == null) ? null : 
                ((BigDecimal)actualTable
                        .getValue(i, "actsalesamount")).doubleValue();
            actualitemcnt= (actualTable.getValue(i, "itemcnt") == null) ? null :
                ((BigDecimal)actualTable.getValue(i, "itemcnt")).doubleValue();
            actualactsalesamount = (actualTable
                    .getValue(i, "actsalesamount") == null) ? null : 
                ((BigDecimal)actualTable
                            .getValue(i, "actsalesamount")).doubleValue();
            actualreturnflag = (actualTable
                            .getValue(i, "returnflag") == null) ? null : 
                ((BigDecimal)actualTable
                        .getValue(i, "returnflag")).doubleValue();
            actualvoidflag = (actualTable
                    .getValue(i, "voidflag") == null) ? null : 
                ((BigDecimal)actualTable
                        .getValue(i, "voidflag")).doubleValue();
            actualcancelflag = (actualTable
                    .getValue(i, "cancelflag") == null) ? null : 
                ((BigDecimal)actualTable.getValue(i, "cancelflag"))
                                        .doubleValue();
            actualDebtor = (actualTable.getValue(i, "Debtor") == null) ? null : 
                ((BigDecimal)actualTable.getValue(i, "Debtor")).doubleValue();
            actualcreditor = (actualTable
                    .getValue(i, "creditor") == null) ? null : 
                ((BigDecimal)actualTable.getValue(i, "creditor")).doubleValue();
            actualcustomerid = (String)actualTable.getValue(i, "customerid");
            actualoriginaltranno = (actualTable
                    .getValue(i, "originaltranno") == null) ? null : 
                ((BigDecimal)actualTable
                        .getValue(i, "originaltranno")).doubleValue();

            
            assertThat("TXL_JOURNAL (row"+i+" - plu", expectedplu, 
                    is(equalTo((String)actualTable.getValue(i, "plu"))));
            assertThat("TXL_JOURNAL (row"+i+" - description",
                    expecteddescription, 
                    is(equalTo((String)actualTable
                            .getValue(i, "description"))));
            assertThat("TXL_JOURNAL (row"+i+" - accountcode",
                    expectedaccountcode, 
                    is(equalTo((String)actualTable
                            .getValue(i, "accountcode"))));
            
            assertThat("TXL_JOURNAL (row"+i+" - itemcnt", expecteditemcnt, 
                    is(equalTo(actualitemcnt)));
            assertThat("TXL_JOURNAL (row"+i+" - actsalesamount",
                    expectedactsalesamount,
                    is(equalTo(actualactsalesamount)));
            assertThat("TXL_JOURNAL (row"+i+" - returnflag",
                    expectedreturnflag, 
                    is(equalTo(actualreturnflag)));
            assertThat("TXL_JOURNAL (row"+i+" - voidflag",
                    expectedvoidflag, 
                    is(equalTo(actualvoidflag)));
            assertThat("TXL_JOURNAL (row"+i+" - cancelflag",
                    expectedcancelflag, 
                    is(equalTo(actualcancelflag)));
            assertThat("TXL_JOURNAL (row"+i+" - Debtor", expectedDebtor, 
                    is(equalTo(actualDebtor)));
            assertThat("TXL_JOURNAL (row"+i+" - creditor", expectedcreditor, 
                    is(equalTo(actualcreditor)));
            assertThat("TXL_JOURNAL (row"+i+" - customerid", expectedcustomerid,
                    is(equalTo(actualcustomerid)));
            assertThat("TXL_JOURNAL (row"+i+" - originaltranno",
                    expectedoriginaltranno, 
                    is(equalTo(actualoriginaltranno)));
        
            i++;
        }    
    }
}

