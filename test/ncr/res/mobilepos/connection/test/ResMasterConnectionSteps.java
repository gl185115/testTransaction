package ncr.res.mobilepos.connection.test;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.department.dao.SQLServerDepartmentDAO;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Requirements;

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

public class ResMasterConnectionSteps extends Steps {
	
	private DBManager resMasterDBManager;
	private DBManager resMasterDBManagerTemp;
	private DBInitiator dbInit = null;
	private ViewDepartment dptModel = null;
	
	@BeforeScenario
    public final void setUpClass() {
    	Requirements.SetUp();
    }
	@AfterScenario
    public final void tearDownClass() {
    	Requirements.TearDown();
    }
	@Given("an RESMaster DBManager")
    public final void givenDBManager() throws DaoException {
		resMasterDBManager = JndiDBManagerMSSqlServer.getInstance();
    }
	@When("I create another RESMaster DBManager")
    public final void whenCreateDBManager() throws DaoException {
		resMasterDBManagerTemp = JndiDBManagerMSSqlServer.getInstance();
    }
	@Then("I should get the same RESMaster DBManager")
    public final void testTheSameInstance() {
    	Assert.assertEquals("The same instance of DBManager", resMasterDBManager, resMasterDBManagerTemp);    	
    }
	@Then("I should get RESMaster connection")
    public final void testConnection() throws DaoException, SQLException {
    	Connection connection = resMasterDBManager.getConnection();
    	Assert.assertNotNull("Get connection", connection);
    } 
	
	/*********************************** Invoke SQLServerDepartmentDAO ***********************************/
	@Given("entries for {$department} in database")
    public final void initDptDatasets(final String department) throws Exception {
    	dbInit = new DBInitiator("DepartmentResourceSteps", DATABASE.RESMaster);
    	dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
    			"test/ncr/res/mobilepos/connection/test/"
    					+ department + ".xml");
    }
    @When("I get Department with storeid{$storeid} and dptid{$dptid}")
    public final void selectDepartmentDetail(final String companyid, final String storeid, final String dptid) throws NoSuchFieldException, SecurityException, IllegalArgumentException, IllegalAccessException, DaoException {
    	SQLServerDepartmentDAO deptDAO = Mockito.mock(SQLServerDepartmentDAO.class);
    	Field field = SQLServerDepartmentDAO.class.getDeclaredField("dbManager");
    	field.setAccessible(true);
    	field.set(deptDAO, resMasterDBManager);
    	
    	Trace.Printer tp1 = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    	Field field2 = SQLServerDepartmentDAO.class.getDeclaredField("tp");
    	field2.setAccessible(true);
    	field2.set(deptDAO, tp1);
    	    	
    	GlobalConstant.setCorpid("0");
    	dptModel = deptDAO.selectDepartmentDetail(companyid, storeid, dptid);    	
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
}
