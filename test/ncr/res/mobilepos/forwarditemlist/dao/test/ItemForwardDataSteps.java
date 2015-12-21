package ncr.res.mobilepos.forwarditemlist.dao.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.Date;
import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.forwarditemlist.dao.SQLServerForwardItemListDAO;
import ncr.res.mobilepos.forwarditemlist.model.ForwardCountData;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.journalization.model.PosLogResp;

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

public class ItemForwardDataSteps extends Steps{
    private DBInitiator dbInit = null;
    private String datasetPath = "test/ncr/res/mobilepos/forwarditemlist/";
    private SQLServerForwardItemListDAO sqlServerForward = null;
    private PosLogResp poslogresponse;
    private ForwardCountData forwardCountData;
    private String forwardedXML;

    @SuppressWarnings("deprecation")
	@BeforeScenario
    public final void SetUpClass()
    {
        try {
            //By default Company ID is empty
            Requirements.SetUp();
            dbInit = new DBInitiator("ItemForwardDataSteps", DATABASE.RESTransaction);
            sqlServerForward = new SQLServerForwardItemListDAO();
        } catch (DaoException e) {
            Assert.fail("Setting up the Item Forward Data.");
        }
    }
    
    @AfterScenario
    public final void TearDownClass(){
        Requirements.TearDown();
        GlobalConstant.setCorpid("");
    }
    
    @SuppressWarnings("deprecation")
	@Given("a table entry named {$dataset} in the database")
    public final void ATableEntryNamedInTheDatabase(final String dataset){
        try {
            dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    datasetPath + dataset);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Fail to CLean Insert on TXL_FORWARD_ITEM");
        }    
    }
    
    @When("executing Upload ForwardTransactionToList from a MobilePOS"
            + " having $deviceNo and $terminalNo with xml: $poslogxml")
    public final void executingUploadForwardTransactionWithXml(
            final String deviceNo,
            final String terminalNo, final String poslogxml){
        try {
            poslogresponse = sqlServerForward.uploadItemForwardData(
                    deviceNo, terminalNo, poslogxml);
        } catch (DaoException e) {
            Assert.fail("Fail to upload For a given Transaction");
        }
    }
    
    @SuppressWarnings("deprecation")
	@When("a request to get the total number of Forwarded Item given that"
            + " storeid{$storeid}, terminalid{$termid}"
            + " and businessdate{$txdate}")
    public final void aRequestToGetTheTotalNumberOfForwardedItem(
            final String storeid,
            final String terminalid, final String txdate){
        try {
            forwardCountData = sqlServerForward.getForwardCountData(
                                            storeid, terminalid, txdate);
        } catch (DaoException e) {
            Assert.fail("The Request for Forward Count Data was unsuccessful");
        }
    }
    
    @SuppressWarnings("deprecation")
	@When("there is a request to get a forwarded item given that"
            + " storeid{$storeid}, terminalid{$termid}"
            + " and businessdate{$txdate}")
    public final void ARequestToGetAForwardedItem(final String storeid,
            final String terminalid, final String txdate){
        try {
            forwardedXML = sqlServerForward.getShoppingCartData(
                    storeid, terminalid, txdate);
        } catch (DaoException e) {
            Assert.fail("Failed to get Shopping Cart Data with storeid "
                    + storeid + 
                        "terminalid " + terminalid +
                        "txdate" + txdate);
        }
    }
    
    @Then("I should get the Forwarded POSLOG XML: $expectedForwardedXML")
    public final void IShouldGetTheForwardedPOSLOGXML(
            String expectedForwardedXML){
        if(expectedForwardedXML.equals("NULL")) {
            expectedForwardedXML = null;
        }
        assertThat(forwardedXML, is(equalTo(expectedForwardedXML)));
    }
    
    @Then("I should get Forwarded Count Results of"
            + " status{$status} and count{$count}")
    public final void IShouldGetResults(
            final String status, final String count){
        assertThat("Compare the forwarded count data",
                forwardCountData.getForwardCount(), is(equalTo(count)));
        assertThat("Compare the forwared status data",
                forwardCountData.getStatus(), is(equalTo(status)));
    }
    
    
    @Then("it have a successful POSLOg Response")
    public final void theFollowingPOSLogResponseWillBeRetrieved(){
        assertThat(poslogresponse.getStatus(), is(equalTo("0")));        
    }
    
    @Then("database have the following values in the TXL_FORWARD_ITEM:"
            + " $expectedForwardedItem")
    public final void databaseHaveTheFollowingValuesInTheTXL_FORWARD_ITEM(
            final ExamplesTable expectedForwardedItem) throws DataSetException{
        ITable actualTable = dbInit.getTableSnapshot("TXL_FORWARD_ITEM");
        
        assertThat("The number of rows in TXL_FORWARD_ITEM",
                expectedForwardedItem.getRowCount(),
                is(equalTo(actualTable.getRowCount())));
        
        int i = 0;        
        for (Map<String, String> item : expectedForwardedItem.getRows()) {
            assertThat("TXL_FORWARD_ITEM (row"+i
                    +" - storeid", item.get("storeid"), 
                    is(equalTo((String)actualTable
                            .getValue(i, "RetailStoreId"))));
            assertThat("TXL_FORWARD_ITEM (row"+i
                    +" - termid", item.get("termid"), 
                    is(equalTo(((String)actualTable
                            .getValue(i, "WorkstationId")).trim())));
            assertThat("TXL_FORWARD_ITEM (row"+i
                    +" - txid", item.get("txid"), 
                    is(equalTo(String.valueOf(actualTable.getValue(i, "SequenceNumber")))));
            assertThat("TXL_FORWARD_ITEM (row"+i
                    +" - txdate", Date.valueOf(item.get("txdate")), 
                    is(equalTo((Date)actualTable.getValue(i, "BusinessDayDate"))));
            assertThat("TXL_FORWARD_ITEM (row"+i
                    +" - postermid", item.get("postermid"), 
                    is(equalTo((String)actualTable.getValue(i, "PosTermId"))));
            assertThat("TXL_FORWARD_ITEM (row"+i
                    +" - status", Integer.parseInt(item.get("status")), 
                    is(equalTo(actualTable.getValue(i, "Status"))));
            i++;
        }        
    }
}
