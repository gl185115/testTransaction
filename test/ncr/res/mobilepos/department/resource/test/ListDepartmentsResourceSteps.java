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
 * Test for list departments service in DepartmentResource class.
 */
@SuppressWarnings("deprecation")
public class ListDepartmentsResourceSteps extends Steps {
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
                        + "MST_DPTINFO_forList.xml", DATABASE.RESMaster);
        GlobalConstant.setMaxSearchResults(50);
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
    public final void aDatabaseTableForListOfClassInfos(String filename) {
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
    @When("I list all departments of storeid {$retailStoreID} with key {$key}")
    public final void listDepartmentOfStoreIDWithKey(final String retailStoreID,
            String key, String name) {
        if (key.equals("null")) {
            key = null;
        } else if (key.equals("empty")) {
            key = "";
        }
        dptList = departmentResource.listDepartments(retailStoreID, key, null, 0);
        
    }

    /**
     * List all departments of given storeid.
     *
     * @param retailStoreID The Retail Store ID.
     */
    @When("I list all departments of storeid {$retailStoreID}")
    public final void listDepartmentOfStoreID(final String retailStoreID) {
            dptList = departmentResource.listDepartments(retailStoreID, null, null, 0);
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
    public final void shouldGetResults(final int resultCode,
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
            Assert.assertEquals("Compare the Department's Inherit Flag " + i,
                    tempDpt.get("INHERITFLAG"), actualDept.getInheritFlag());
            Assert.assertEquals("Compare the Department's NonSales Flag " + i,
                    tempDpt.get("EXCEPTIONFLAG"), actualDept.getNonSales()+"");
            Assert.assertEquals("Compare the Department's Allow Yen Flag " + i,
                    tempDpt.get("SUBSMALLINT5"), actualDept.getSubSmallInt5()+"");
            Assert.assertEquals("Compare the Department's Discount Type " + i,
                    tempDpt.get("DISCOUNTTYPE"), actualDept.getDiscountType());
            
            if (tempDpt.get("DISCOUNTFLAG").equals("null")) {
                Assert.assertNull("Assume that Discount Flag is "
                        + "null for row" + i + ":",
                        actualDept.getDiscountFlag());
            } else {
                Assert.assertEquals("Compare the Discount Flag row" + i
                        + ": ", Integer.parseInt(tempDpt.get("DISCOUNTFLAG")),
                          actualDept.getDiscountFlag());
            }
            
            if (tempDpt.get("DISCOUNTAMT").equals("null")) {
                Assert.assertNull("Assume that Discountamount is "
                        + "null for row" + i + ":",
                        actualDept.getDiscountAmt());
            } else {
            	Assert.assertEquals("Compare the Discountamount row" + i
                        + ": ", Integer.parseInt(tempDpt.get("DISCOUNTAMT")),
                          java.math.BigDecimal.valueOf(actualDept.getDiscountAmt()).intValue());
            }
            
            
            if (tempDpt.get("DISCOUNTRATE").equals("null")) {
                Assert.assertNull("Assume that Discount Rate is "
                        + "null for row" + i + ":",
                        actualDept.getDiscounRate());
            } else {
            	Assert.assertEquals("Compare the Discount Rate row" + i
                        + ": ", Integer.parseInt(tempDpt.get("DISCOUNTRATE")),
                          java.math.BigDecimal.valueOf(actualDept.getDiscounRate()).intValue());
            }
            
            Assert.assertEquals("Compare the Department's Tax Rate " + i,
                    tempDpt.get("TAXRATE"), actualDept.getTaxRate());
            Assert.assertEquals("Compare the Department's TaxType " + i,
                    tempDpt.get("TAXTYPE"), actualDept.getTaxType());
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
            assertThat("MST_DPTINFO (row"+i+" - STATUS",
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
