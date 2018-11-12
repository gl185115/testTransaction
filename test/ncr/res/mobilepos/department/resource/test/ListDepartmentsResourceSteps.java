package ncr.res.mobilepos.department.resource.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.sql.SQLException;
import java.util.Map;

import javax.servlet.ServletContext;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.department.model.Department;
import ncr.res.mobilepos.department.model.DepartmentList;
import ncr.res.mobilepos.department.resource.DepartmentResource;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;

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
    	ServletContext servletContext = Requirements.getMockServletContext();
        departmentResource = new DepartmentResource();
        departmentResource.setContext(servletContext);
    }

    /**
     * List all departments of given storeid.
     *
     * @param retailStoreID The Retail Store ID.
     * @param key The Department Key to be search.
     */
    @When("I list all departments of storeid $retailStoreID with key $key")
    public final void listDepartmentOfStoreIDWithKey(final String retailStoreID,
            String key, String name) {
        if (key.equals("null")) {
            key = null;
        } else if (key.equals("empty")) {
            key = "";
        }
        dptList = departmentResource.listDepartments(null,retailStoreID, key, null, 0);

    }

    /**
     * List all departments of given storeid.
     *
     * @param retailStoreID The Retail Store ID.
     */
    @When("I list all departments of storeid $retailStoreID")
    public final void listDepartmentOfStoreID(final String retailStoreID) {
    	dptList = departmentResource.listDepartments(null,retailStoreID, null, null, 0);
    }

    /**
     * List all departments of given companyid and storeid.
     *
     * @param companyID The Company ID.
     * @param retailStoreID The Retail Store ID.
     */
    @When("I list all departments of companyid $companyid storeid $retailStoreID")
    public final void listDepartmentOfCompanyIDAndStoreID(final String companyID,final String retailStoreID) {
    	dptList = departmentResource.listDepartments(companyID,retailStoreID, null, null, 0);
    }

    /**
     * List all departments of given storeid.
     *
     * @param retailStoreID The Retail Store ID.
     * @param key The Department Key to be search.
     */
    @When("I list all departments of companyid $companyid storeid $retailStoreID with key $key")
    public final void listDepartmentOfCompanyIDAndStoreIDWithKey(final String companyID,final String retailStoreID,
            String key, String name) {
        if (key.equals("null")) {
            key = null;
        } else if (key.equals("empty")) {
            key = "";
        }
        dptList = departmentResource.listDepartments(companyID,retailStoreID, key, null, 0);

    }

    /**
     * Tests return resultcode and size of department list.
     *
     * @param resultCode
     *            NCRWSSResultCode.
     * @param length
     *            size of list.
     */
    @Then("I should get resultcode $resultCode and $length department list")
    public final void shouldGetResults(final int resultCode,
            final int length) {
        Assert.assertEquals(resultCode, dptList.getNCRWSSResultCode());
        Assert.assertEquals(length, dptList.getDepartments().size());
    }
    @Then("I should get resultcode $resultCode")
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
                        actualDept.getDiscountRate());
            } else {
            	Assert.assertEquals("Compare the Discount Rate row" + i
                        + ": ", Integer.parseInt(tempDpt.get("DISCOUNTRATE")),
                          java.math.BigDecimal.valueOf(actualDept.getDiscountRate()).intValue());
            }

            Assert.assertEquals("Compare the Department's TaxType " + i,
                    tempDpt.get("TAXTYPE"), actualDept.getTaxType());
            Assert.assertEquals("Compare the Department's SubNum1 " + i,
                    tempDpt.get("SUBNUM1"), actualDept.getSubNum1());
            Assert.assertEquals("Compare the Department's SubNum2 " + i,
                    tempDpt.get("SUBNUM2"), actualDept.getSubNum2());
            Assert.assertEquals("Compare the Department's SubNum3 " + i,
                    tempDpt.get("SUBNUM3"), actualDept.getSubNum3());
            Assert.assertEquals("Compare the Department's SubNum4 " + i,
                    tempDpt.get("SUBNUM4"), actualDept.getSubNum4());
            Assert.assertEquals("Compare the Department's SubCode1 " + i,
                    tempDpt.get("SUBCODE1"), actualDept.getSubCode1());
            Assert.assertEquals("Compare the Department's InputSubCode1 " + i,
                    tempDpt.get("INPUTTYPE"), actualDept.getInputType());
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

    @Given("that database is throwing an unexpected $exception")
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
