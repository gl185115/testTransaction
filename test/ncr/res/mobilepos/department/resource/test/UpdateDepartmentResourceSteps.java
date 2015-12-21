package ncr.res.mobilepos.department.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

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

/*
Test Steps:
-Given I have a Customer Resource
-When I Search for customerNumber {$customerNumber}
-and deviceNumber {$deviceNumber}
-and operatorNumber {$operatorNumber}
-Then Search must return result ($searchResult)
-Then Customer exists
-Then Customer does not exists
*/

public class UpdateDepartmentResourceSteps extends Steps {
	@InjectMocks
    private DepartmentResource departmentResource = null;
	@Mock
	private DAOFactory daoFactory;
    private ViewDepartment resultBase;   
    private int noCust;
    private DBInitiator dbInitiator = null;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        dbInitiator = new DBInitiator("UpdateDepartmentResourceSteps", DATABASE.RESMaster);
        try {
			dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
			        "test/ncr/res/mobilepos/department/resource/test/"
			        + "prm_system_config_with_tax_rate.xml");		
		} catch (Exception e) {} 
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("I have a Department Resource")
    public final void IHaveACustomerResource(){
    	departmentResource = new DepartmentResource();      
    	departmentResource.setContext(Requirements.getMockServletContext());
    }
    
    @Then("the result should be {$Result, $departmentid, $expected}")
    public final void checkresult(final int Result, String departmentid,
            final ExamplesTable expected){
              
        assertThat(resultBase.getNCRWSSResultCode(), is(equalTo(Result)));
        assertThat(resultBase.getDepartment().getDepartmentID().trim(),
                is(equalTo(departmentid.trim())));
        
        for (Map<String, String> dptDetails : expected.getRows()) {
            assertEquals(dptDetails.get("DPT"),
                    resultBase.getDepartment().getDepartmentID());            
            assertEquals(dptDetails.get("DPTNAME"),
                    resultBase.getDepartment().getDepartmentName().getEn());                  
            assertEquals(dptDetails.get("DPTNAMELOCAL"),
                    resultBase.getDepartment().getDepartmentName().getJa()); 
            assertEquals(dptDetails.get("INHERITFLAG"),
                    resultBase.getDepartment().getInheritFlag()+""); 
            assertEquals(dptDetails.get("NONSALES"),
                    resultBase.getDepartment().getNonSales()+""); 
            assertEquals(dptDetails.get("SUBSMALLINT5"),
                    resultBase.getDepartment().getSubSmallInt5()+""); 
            assertEquals(dptDetails.get("DISCOUNTTYPE"),
                    resultBase.getDepartment().getDiscountType()+""); 
            assertEquals(dptDetails.get("DISCOUNTAMT"),
                    resultBase.getDepartment().getDiscountAmt()+""); 
            assertEquals(dptDetails.get("DISCOUNTFLAG"),
                    resultBase.getDepartment().getInheritFlag()+""); 
            assertEquals(dptDetails.get("DISCOUNTRATE"),
                    resultBase.getDepartment().getDiscounRate()+""); 
            assertEquals(dptDetails.get("TAXRATE"),
                    resultBase.getDepartment().getTaxRate()+""); 
            assertEquals(dptDetails.get("TAXTYPE"),
                    resultBase.getDepartment().getTaxType()+""); 
          }
    }
    
    @Then("the error result should be {$Result}")
    public final void checkerror(final int Result){
        assertThat(resultBase.getNCRWSSResultCode(), is(equalTo(Result)));
    }
    
    @Then("the extended result should be {$Result}")
    public final void checkerrorextended(final int Result){
        assertThat(resultBase.getNCRWSSExtendedResultCode(),
                is(equalTo(Result)));
    }
    
    @Then("Customer exists")
    public final void hasCustomer(){
        assertThat(1, is(equalTo(noCust)));
    }
    
    @Given ("a Department Table")
    public final void emptyTable(){
        new DBInitiator("AuthenticationResource",
                "test/ncr/res/mobilepos/department/resource/test/"
                + "MST_DPTINFO_forUpdate.xml", DATABASE.RESMaster);
    }
    
    @When ("I update a department ($storeid, $dptid, $jsonString)")
    public final void addDept(
            final String storeid, final String dptid, String jsonString)
    {   
        resultBase = departmentResource.updateDepartment(storeid,dptid,jsonString);            
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
