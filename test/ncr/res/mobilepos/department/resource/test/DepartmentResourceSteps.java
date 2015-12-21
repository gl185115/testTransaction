package ncr.res.mobilepos.department.resource.test;

import java.sql.SQLException;
import java.util.Map;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.department.resource.DepartmentResource;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import org.dbunit.operation.DatabaseOperation;
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

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.equalTo;

/**
 * Resource Steps for Department for Department Table Maintenance.
 * @author rd185102
 */
public class DepartmentResourceSteps extends Steps {

    /**
     * Holds the Department Resource Object.
    */
	@InjectMocks
    private DepartmentResource dptResource;	
	@Mock
	private DAOFactory daoFactory;
    /**
     * Holds the DBInitiator object.
     */
    private DBInitiator dbInit;

    /**
     * Holds the DepartmentModel object.
     */
     private ViewDepartment dptModel;

     /**
      * Method to test the database connection.
      */
    @BeforeScenario
    public final void setUpClass() {
        Requirements.SetUp();
        dbInit = new DBInitiator("DepartmentResourceSteps", DATABASE.RESMaster);
    }

    /**
     * Method to test the execution of Database operation
     * with the given xml file.
     * @param department - name of the xml file
     * @throws Exception - Exception
     */
    @Given("entries for {$department} in database")
   public final void initDptDatasets(final String department) throws Exception {
      dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
        "test/ncr/res/mobilepos/department/resource/test/"
           + department + ".xml");
       }

    /**
     * Method to destroy the subcontext of InitialContext.
     */
    @AfterScenario
    public final void tearDownClass() {
       Requirements.TearDown();
    }

    /**
    * Given Step : I have Department Resource.
    */
    @Given("I have Department Resource")
    public final void iHaveDepartmentResource() {
    	dptResource = new DepartmentResource();
    	dptResource.setContext(Requirements.getMockServletContext());
    }

    /**
     * Given Then : The Department Resource should not be null.
     */
    @Then("Department Resource should not be null")
    public final void dptResourceNotNull() {
       assertNotNull(dptResource);
    }

    /**
     * Scenario when getting the Department Details.
     * @param storeid - store number
     * @param dptid - departmentnumber
     */
    @When("I get Department with storeid{$storeid} and dptid{$dptid}")
    public final void selectDepartmentDetail(final String companyid, final String storeid,
         final String dptid) {
        dptModel = new ViewDepartment();
        dptModel = dptResource.selectDepartmentDetail(companyid, storeid, dptid);
        
    }

    /**
     * Then it should return DepartmentModel with correct properties.
     * @param expected - expected properties
     */
     @Then("I should get department model with properties: $expected")
     public final void assertGetDptModel(final ExamplesTable expected) {
        for (Map<String, String> dptDetails : expected.getRows()) {
          assertEquals(dptDetails.get("RETAILSTOREID"),
                       dptModel.getRetailStoreID().trim());
          assertEquals(dptDetails.get("DPT"),
                  dptModel.getDepartment().getDepartmentID());
          assertEquals(dptDetails.get("DPTNAME"),
                  dptModel.getDepartment().getDepartmentName().getEn());
          assertEquals(dptDetails.get("DPTNAMELOCAL"),
                  dptModel.getDepartment().getDepartmentName().getJa());
          assertEquals(dptDetails.get("INHERITFLAG"),
                  dptModel.getDepartment().getInheritFlag());
          assertEquals(dptDetails.get("NONSALES"),
                  dptModel.getDepartment().getNonSales()+"");
          assertEquals(dptDetails.get("SUBSMALLINT5"),
                  dptModel.getDepartment().getSubSmallInt5()+"");
          assertEquals(dptDetails.get("DISCOUNTAMT"),
                  dptModel.getDepartment().getDiscountAmt()+"");
          assertEquals(dptDetails.get("DISCOUNTTYPE"),
                  dptModel.getDepartment().getDiscountType());
          assertEquals(dptDetails.get("DISCOUNTRATE"),
                  dptModel.getDepartment().getDiscounRate()+"");
          assertEquals(dptDetails.get("DISCOUNTFLAG"),
                  dptModel.getDepartment().getDiscountFlag());
          assertEquals(dptDetails.get("TAXRATE"),
                  dptModel.getDepartment().getTaxRate());
          assertEquals(dptDetails.get("TAXTYPE"),
                  dptModel.getDepartment().getTaxType());
        }
     }

     /**
      * Method to test the DepartmentModel that should not be null.
      */
    @Then("The DepartmentModel should not be null")
    public final void dptModelNotNull() {
       assertNotNull(dptModel);
    }

    /**
     * Method to test the value of resultCode and extendedResultCode.
     * @param resultCode - the result code
     * @param extendedResultCode - the extended result code
     */
     @Then("the resultcode should be $resultCode and "
          + "extendedResultCode should be $extendedResultCode")
     public final void resultCodeShouldBe(final String resultCode,
            final String extendedResultCode) {
        int res = dptModel.getNCRWSSResultCode();
        int extendedResCode = dptModel.getNCRWSSExtendedResultCode();
        assertThat(Integer.valueOf(resultCode), is(equalTo(res)));
        assertThat(Integer.valueOf(extendedResultCode),
              is(equalTo(extendedResCode)));
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
     
     @Then("the result should be {$Result}")
     public final void checkResult(final int Result){
         assertThat(dptModel.getNCRWSSResultCode(), is(equalTo(Result)));
     }
     
}
