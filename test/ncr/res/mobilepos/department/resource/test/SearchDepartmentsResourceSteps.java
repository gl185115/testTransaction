package ncr.res.mobilepos.department.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.department.model.Department;
import ncr.res.mobilepos.department.model.DepartmentList;
import ncr.res.mobilepos.department.resource.DepartmentResource;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * Test for search departments service in DepartmentResource class.
 */
@SuppressWarnings("deprecation")
public class SearchDepartmentsResourceSteps extends Steps {
    /**
     * DepartmentResource instance.
     */
	@InjectMocks
    private DepartmentResource departmentResource = null;
	@Mock
	private DAOFactory daoFactory;
    /**
     * DepartmentList instance.
     */
    private DepartmentList dptList = null;
    /**
     * Initializes database data.
     */
    private DBInitiator dbInit = null;
    /**
     * Method executed before running scenario.
     */
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        dbInit = new DBInitiator("DepartmentResource",
                "test/ncr/res/mobilepos/department/resource/test/"
                        + "MST_DPTINFO_forSearch.xml", DATABASE.RESMaster);
        GlobalConstant.setMaxSearchResults(3);
    }

    /**
     * Method executed after executing scenario.
     */
    @AfterScenario
    public final void tearDown() {
        Requirements.TearDown();
    }

    /**
     * Initialize DepartmentResource.
     */
    @Given("a Department Resource")
    public final void givenDepartmentResource() {
		departmentResource = new DepartmentResource();
		departmentResource.setContext(Requirements.getMockServletContext());
    }
    
    @Given("a database table with special characters")
    public final void aDatabaseTableForListOfClassInfos() {
    	 dbInit = new DBInitiator("DepartmentResource",
                 "test/ncr/res/mobilepos/department/resource/test/"
                         + "MST_DPTINFO_forSearchSpChars.xml", DATABASE.RESMaster);
    }

    /**
     * List all departments of given storeid.
     *
     * @param retailStoreID The Retail Store ID.
     * @param key The Department Key to be search.
     */
    @When("I list all departments of storeid {$retailStoreID} with key {$key} and name {$name}")
    public final void listDepartmentOfStoreIDWithKey(final String retailStoreID,
            String key, String name) {
        key = key.equals("null") ? null: key;
        name = name.equals("null")? null: name; 
      
        dptList = departmentResource.listDepartments(retailStoreID, key, name, 0);

       
    }
    
    /**
     * List all departments of given storeid.
     *
     * @param retailStoreID The Retail Store ID.
     * @param key The Department Key to be search.
     * @param limit Limit use to search department 
     */
    @When("I list all departments of storeid {$retailStoreID} with limit $searchLimit and key {$key} and name {$name}")
    public final void listDepartmentOfStoreIDWithKeyAndLimit(final String retailStoreID,
            int searchLimit, String key, String name) {        
        key = key.equals("null") ? null: key;
        name = name.equals("null")? null: name;        
        dptList = departmentResource.listDepartments(retailStoreID, key, null, searchLimit);
    }
    

    /**
     * Tests return resultcode and size of department list.
     *
     * @param resultCode
     *            NCRWSSResultCode.
     * @param length
     *            size of list.
     */
    @Then("I should get resultcode {$resultCode} and {$length} department list")
    public final void shouldGetResultCode(final int resultCode,
            final int length) {
        Assert.assertEquals(resultCode, dptList.getNCRWSSResultCode());
        Assert.assertEquals(length, dptList.getDepartments().size());
    }
    @Then("I should get resultcode {$resultCode}")
    public final void shouldGetResultCode(final int resultCode) {
        Assert.assertEquals(resultCode, dptList.getNCRWSSResultCode());
    }
    /**
     * test.
     * @param limit - the limit
     */
    @When("I set the search limit to $limit")
    public final void setLimit(final int limit) {
        GlobalConstant.setMaxSearchResults(limit);
    }

    /**
     * Tests each department information in a list.
     *
     * @param expectedDepartments
     *            The ExamplesTable instance.
     */
    @Then("I should get list: $departments")
    public final void shouldGetDepartments(
            final ExamplesTable expectedDepartments) {
        Assert.assertEquals("Must exact number of Department from the List",
                expectedDepartments.getRowCount(), dptList.getDepartments()
                        .size());

        int i = 0;
        for (Map<String, String> tempDpt : expectedDepartments.getRows()) {
            Department actualDept = dptList.getDepartments().get(i);

            Assert.assertEquals("Compare the Deparments's ID " + i,
                    tempDpt.get("DPT"), actualDept.getDepartmentID());
            Assert.assertEquals("Compare the Department's EN Name " + i,
                    tempDpt.get("DPTNAME"), actualDept.getDepartmentName().getEn());
            Assert.assertEquals("Compare the Department's JP Name " + i,
                    tempDpt.get("DPTNAMELOCAL"), actualDept.getDepartmentName().getJa());
            i++;
        }
    }
    
    @Then("I should have the following departments: $expecteddepartments")
    public final void iShouldHaveTheFollowingDepartments(
            final ExamplesTable expecteddepartments) throws DataSetException{
        ITable actualTable =  dbInit.getTableSnapshot("MST_DPTINFO");
        
        assertThat("Compare the actual number of rows in MST_DPTINFO",
                actualTable.getRowCount(), 
                is(equalTo(expecteddepartments.getRowCount())));
        
        int i = 0;        
        for (Map<String, String> deparments : expecteddepartments.getRows()) {
            String storeid = (String)actualTable.getValue(i, "StoreId");
            String dpt = (String)actualTable.getValue(i, "Dpt");
            String status = (String)actualTable.getValue(i, "Status");
            
            assertThat("MST_DPTINFO (row"+i+" - STOREID",
                    storeid.trim(), 
                    is(equalTo(deparments.get("STOREID").trim())));
            assertThat("MST_DPTINFO (row"+i+" - DPT",
                    dpt.trim(), 
                    is(equalTo(deparments.get("DPT").trim())));
			assertThat(
					"MST_DPTINFO (row" + i + " - STATUS",
					status == null ? status : status.trim(),
					is(equalTo(deparments.get("STATUS").trim().equals("NULL") ? null
							: deparments.get("STATUS").trim())));
            
            i++;
        }
    }
    
    @Given("that database is throwing an unexpected {$exception}")
	public final void givenThrownException(String exception) {
		MockitoAnnotations.initMocks(this);
		Exception ex = new Exception();
		if (exception.equalsIgnoreCase("DaoException"))
			ex = new DaoException();
		if (exception.equalsIgnoreCase("Exception"))
			ex = new Exception();
		if (exception.equalsIgnoreCase("SQLException"))
			ex = new DaoException(new SQLException());
		if (exception.equalsIgnoreCase("SQLStatementException"))
			ex = new DaoException(new SQLStatementException());
		
		try {
			Mockito.stub(daoFactory.getDepartmentDAO()).toThrow(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
