package ncr.res.mobilepos.connection.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Map;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.forwarditemlist.dao.SQLServerForwardItemListDAO;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Requirements;
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
import org.junit.Assert;
import org.mockito.Mockito;

/**
 * Steps for Database instances and connections.
 */
public class ResTransactionConnectionSteps extends Steps {
	/**
	 * DBManager instance for RESMaster & RESTransaction.
	 */
	private DBManager resTransactionDBManager = null;
	
	/**
	 * Temporary dbManager used for testing only.
	 */
    private DBManager resTransactionDBManagerTemp = null;
    
    /**
     * Instance properties.
     */
	private DBInitiator dbInit = null;
	private PosLogResp poslogresponse = null;
	
	
    @BeforeScenario
    public final void setUpClass() {
    	Requirements.SetUp();
    }
    @AfterScenario
    public final void tearDownClass() {
    	Requirements.TearDown();
    }
    @Given("an RESTransaction DBManager")
    public final void givenDBManager() throws DaoException {
    	resTransactionDBManager = JndiDBManagerMSSqlServer.getInstance();
    }
    @When("I create another RESTransaction DBManager")
    public final void whenCreateDBManager() throws DaoException {
    	resTransactionDBManagerTemp = JndiDBManagerMSSqlServer.getInstance();
    }
    @Then("I should get the same RESTransaction DBManager")
    public final void testTheSameInstance() {
    	Assert.assertEquals("The same instance of DBManager", resTransactionDBManager, resTransactionDBManagerTemp);
    }
    @Then("I should get RESTransaction connection")
    public final void testConnection() throws DaoException, SQLException {
    	Connection connection = resTransactionDBManager.getConnection();
    	Assert.assertNotNull("Get connection", connection);
    }
    
    /*********************************** Invoke SQLServerForwardItemListDAO ***********************************/
    @Given("entries for {$table} in database")
    public final void initDptDatasets(final String forwardItemTable) throws Exception {
    	dbInit = new DBInitiator("DepartmentResourceSteps", DATABASE.RESTransaction);
    	dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
    			"test/ncr/res/mobilepos/connection/test/"
    					+ forwardItemTable + ".xml");
    }
    @When("executing Upload ForwardTransactionToList from a MobilePOS"
            + " having $deviceNo and $terminalNo with xml: $poslogxml")
    public final void executingUploadForwardTransactionWithXml(
            final String deviceNo,
            final String terminalNo, final String poslogxml) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, DaoException{
    	SQLServerForwardItemListDAO forwardItemDAO = Mockito.mock(SQLServerForwardItemListDAO.class);
    	Field field = SQLServerForwardItemListDAO.class.getDeclaredField("dbManager");
    	field.setAccessible(true);
    	field.set(forwardItemDAO, resTransactionDBManager);
    	
    	Trace.Printer tp1 = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    	Field field2 = SQLServerForwardItemListDAO.class.getDeclaredField("tp");
    	field2.setAccessible(true);
    	field2.set(forwardItemDAO, tp1);
    	    	
    	GlobalConstant.setCorpid("000000000000");
    	poslogresponse = forwardItemDAO.uploadItemForwardData(
                deviceNo, terminalNo, poslogxml);        
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
                    +" - RetailStoreId", item.get("RetailStoreId"), 
                    is(equalTo((String)actualTable
                            .getValue(i, "RetailStoreId"))));
            assertThat("TXL_FORWARD_ITEM (row"+i
                    +" - WorkstationId", item.get("WorkstationId"), 
                    is(equalTo(((String)actualTable
                            .getValue(i, "WorkstationId")).trim())));
            assertThat("TXL_FORWARD_ITEM (row"+i
                    +" - SequenceNumber", item.get("SequenceNumber"), 
                    is(equalTo((String)actualTable.getValue(i, "SequenceNumber"))));
            assertThat("TXL_FORWARD_ITEM (row"+i
                    +" - BusinessDayDate", Date.valueOf(item.get("BusinessDayDate")), 
                    is(equalTo((Date)actualTable.getValue(i, "BusinessDayDate"))));
            assertThat("TXL_FORWARD_ITEM (row"+i
                    +" - PosTermId", item.get("PosTermId"), 
                    is(equalTo((String)actualTable.getValue(i, "PosTermId"))));
            assertThat("TXL_FORWARD_ITEM (row"+i
                    +" - Status", Integer.parseInt(item.get("Status")), 
                    is(equalTo(actualTable.getValue(i, "Status"))));
            i++;
        }        
    }
}
