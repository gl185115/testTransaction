package ncr.res.mobilepos.department.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Map;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.department.resource.DepartmentResource;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

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



public class DeleteDepartmentResourceSteps extends Steps {
	@InjectMocks
    private DepartmentResource dptResource;
	@Mock
	private DAOFactory daoFactory;
    private ResultBase resultBase;
    private int noCust;
    private DBInitiator dbInit;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        dbInit = new DBInitiator("DeleteDepartmentResourceSteps", DATABASE.RESMaster);
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("I have a Department Resource")
    public final void IHaveACustomerResource(){
    	dptResource = new DepartmentResource();
        try {
			Field field = DepartmentResource.class.getDeclaredField("context");
			field.setAccessible(true);
	        field.set(dptResource, Requirements.getMockServletContext());
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    
    @Then("the result should be {$Result}")
    public final void customerSearch(final int Result){
        assertThat(resultBase.getNCRWSSResultCode(), is(equalTo(Result)));
    }
    
    @Then("Customer exists")
    public final void hasCustomer(){
        assertThat(1, is(equalTo(noCust)));
    }
    
    @Given ("a Department Table")
    public final void emptyTable(){
        @SuppressWarnings("unused")
		DBInitiator dbinit = new DBInitiator("AuthenticationResource",
                "test/ncr/res/mobilepos/department/resource/test/"
                + "MST_DPTINFO_forDelete.xml", DATABASE.RESMaster);
    }
    
    @When ("I delete a department ($storeid, $dptid)")
    public final void addDept(final String storeid, final String dptid)
    {
        resultBase = dptResource.deleteDepartment(storeid,dptid);
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
                    status.trim(), 
                    is(equalTo(deparments.get("STATUS").trim())));
            
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
