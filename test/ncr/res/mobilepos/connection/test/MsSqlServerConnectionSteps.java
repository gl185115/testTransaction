package ncr.res.mobilepos.connection.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.department.dao.SQLServerDepartmentDAO;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.department.resource.DepartmentResource;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.forwarditemlist.dao.SQLServerForwardItemListDAO;
import ncr.res.mobilepos.forwarditemlist.resource.ForwardItemListResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.journalization.model.PosLogResp;

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
import org.junit.Assert;
import org.mockito.Mockito;

/**
 * Steps for Database instances and connections.
 */
public class MsSqlServerConnectionSteps extends Steps {
	/**
	 * DBManager instance for RESMaster & RESTransaction.
	 */
	private DBManager msSqlServerDBManager = null;
	
	/**
	 * Temporary dbManager used for testing only.
	 */
    private DBManager msSqlServerDBManagerTemp = null;
    
    /**
     * Instance properties.
     */
	private DBInitiator dbInit = null;
	private PosLogResp poslogresponse = null;
	private ForwardItemListResource testForwardItemResource = null;
	private ServletContext servletContext = null;
	
    @BeforeScenario
    public final void setUpClass() {
    	Requirements.SetUp();
    	servletContext = Requirements.getMockServletContext();
    }
    @AfterScenario
    public final void tearDownClass() {
    	Requirements.TearDown();
    }
    @Given("an MSSQLServer DBManager")
    public final void givenDBManager() throws DaoException {
    	msSqlServerDBManager = JndiDBManagerMSSqlServer.getInstance();
    }
    @When("I create another MSSQLServer DBManager")
    public final void whenCreateDBManager() throws DaoException {
    	msSqlServerDBManagerTemp = JndiDBManagerMSSqlServer.getInstance();
    }
    @Then("I should get the same MSSQLServer DBManager")
    public final void testTheSameInstance() {
    	Assert.assertEquals("The same instance of DBManager", msSqlServerDBManager, msSqlServerDBManagerTemp);
    }
    @Then("I should get MSSQLServer connection")
    public final void testConnection() throws DaoException, SQLException {
    	Connection connection = msSqlServerDBManager.getConnection();
    	Assert.assertNotNull("Get connection", connection);
    }
    
    /*********************************** Invoke SQLServerForwardItemListDAO ***********************************/
    @Given("entries for {$table} in database")
    public final void initForwardDatasets(final String forwardItemTable) {
    	dbInit = new DBInitiator("TxlForwardItem", DATABASE.RESTransaction);
    	try {
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/connection/test/"
							+ forwardItemTable + ".xml");
		} catch (DatabaseUnitException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
    }
    
    /*********************************** Invoke SQLServerDepartment  ***********************************/
    @Given("entries for {} in master database")
    public final void initMasterDatasets(final String resMasterTable) {
        dbInit = new DBInitiator("Department", DATABASE.RESMaster);
        try {
                        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                                        "test/ncr/res/mobilepos/connection/test/"
                                                        + resMasterTable + ".xml");
                } catch (DatabaseUnitException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (SQLException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                }
    }
    @When("executing Upload ForwardTransactionToList from a MobilePOS"
            + " having $deviceNo and $terminalNo with xml: $poslogxml")
    public final void executingUploadForwardTransactionWithXml(
            final String deviceNo,
            final String terminalNo, final String poslogxml) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, DaoException{
    	SQLServerForwardItemListDAO forwardItemDAO = Mockito.mock(SQLServerForwardItemListDAO.class);
    	Field field = SQLServerForwardItemListDAO.class.getDeclaredField("dbManager");
    	field.setAccessible(true);
    	field.set(forwardItemDAO, msSqlServerDBManager);
    	
    	Trace.Printer tp1 = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    	Field field2 = SQLServerForwardItemListDAO.class.getDeclaredField("tp");
    	field2.setAccessible(true);
    	field2.set(forwardItemDAO, tp1);
    	    	
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
                    is(equalTo(actualTable.getValue(i, "SequenceNumber").toString())));
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
    
    /*********************************** Invoke ForwardItemListResource ***********************************/
    @Given("a ForwardItem Service")
    public void aPromotionService() {
		testForwardItemResource = new ForwardItemListResource();
		Field forwardItemContext;
		Field itemcontext;
		try {
			ForwardItemListResource forwardItemListResourceObj = new ForwardItemListResource();
			forwardItemContext = testForwardItemResource.getClass()
					.getDeclaredField("context");
			itemcontext = forwardItemListResourceObj.getClass()
					.getDeclaredField("context");
			forwardItemContext.setAccessible(true);
			itemcontext.setAccessible(true);
			forwardItemContext.set(testForwardItemResource, servletContext);
			itemcontext.set(forwardItemListResourceObj, servletContext);
		} catch (Exception ex) {
			Assert.fail("Can't Retrieve Servlet context from promotion.");
		}
    }

    @When("I request service to Upload ForwardTransactionToList from a MobilePOS"
            + " having $deviceNo and $terminalNo with xml: $poslogxml")
    public final void servicetoUploadForwardTransactionWithXml(final String deviceNo, final String terminalNo, final String poslogxml) {
    	poslogresponse = testForwardItemResource.uploadForwardData(poslogxml, deviceNo, terminalNo);        
    }
    
    private DepartmentResource testDepartmentResource = null;
    private ViewDepartment dptModel = null;
    @Given("a Department Service")
    public final void givenDepartmentService() throws DaoException {
    	testDepartmentResource = new DepartmentResource();
		Field deptResourceContext;
		Field itemcontext;
		try {
			DepartmentResource deptResourceObj = new DepartmentResource();
			deptResourceContext = testDepartmentResource.getClass()
					.getDeclaredField("context");
			itemcontext = deptResourceObj.getClass()
					.getDeclaredField("context");
			deptResourceContext.setAccessible(true);
			itemcontext.setAccessible(true);
			deptResourceContext.set(testDepartmentResource, servletContext);
			itemcontext.set(deptResourceObj, servletContext);
		} catch (Exception ex) {
			Assert.fail("Can't Retrieve Servlet context from promotion.");
		}
    }
    @When("I request service to get department details of storeid{$storeid} and dptid{$dptid}")
    public final void selectDepartmentDetailService(final String companyid, final String storeid, final String dptid) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, DaoException {
    	dptModel =  testDepartmentResource.selectDepartmentDetail(companyid, storeid, dptid);
    }
    @Then("I should get department model with properties: $expected")
    public final void assertGetDptModel(final ExamplesTable expected) {
       for (Map<String, String> dptDetails : expected.getRows()) {
         assertEquals(dptDetails.get("RETAILSTOREID"),
                      dptModel.getRetailStoreID().trim());
         assertEquals(dptDetails.get("DPT"),
                 dptModel.getDepartment().getDepartmentID());
         assertEquals(dptDetails.get("DPTNAME"),
                 dptModel.getDepartment().getDepartmentName());
       }
    }
    @When("I get Department with storeid{$storeid} and dptid{$dptid}")
    public final void selectDepartmentDetailDAO(final String companyid, final String storeid, final String dptid) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, DaoException {
    	SQLServerDepartmentDAO deptDAO = Mockito.mock(SQLServerDepartmentDAO.class);
    	Field field = SQLServerDepartmentDAO.class.getDeclaredField("dbManager");
    	field.setAccessible(true);
    	field.set(deptDAO, msSqlServerDBManager);
    	
    	Trace.Printer tp1 = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    	Field field2 = SQLServerDepartmentDAO.class.getDeclaredField("tp");
    	field2.setAccessible(true);
    	field2.set(deptDAO, tp1);
    	    	
    	GlobalConstant.setCorpid("0");
    	dptModel = deptDAO.selectDepartmentDetail(companyid, storeid, dptid);    	
    }
}
