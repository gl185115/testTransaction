package ncr.res.mobilepos.department.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.department.resource.DepartmentResource;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

public class CreateDepartmentResourceSteps extends Steps {
	@InjectMocks
    private DepartmentResource dptResource;	
	@Mock
	private DAOFactory daoFactory;
    private ResultBase resultBase;

    /**
     * The database unit test initiator.
     */
    private DBInitiator dbInitiator = null;
    
    @BeforeScenario
    public final void setUp() throws DaoException {
        Requirements.SetUp();
        dbInitiator = new DBInitiator("CreateDepartmentResourceSteps", DATABASE.RESMaster);
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
    public final void IHaveADepartmentResource(){
        dptResource = new DepartmentResource();
        dptResource.setContext(Requirements.getMockServletContext());
    }
    
    @Then("the result should be {$Result}")
    public final void checkResult(final int Result){
        assertThat(resultBase.getNCRWSSResultCode(), is(equalTo(Result)));
    }
    
    @Given ("an empty Department Table")
    public final void emptyTable(){
        new DBInitiator("AuthenticationResource",
                "test/ncr/res/mobilepos/department/resource/test/"
                +  "MST_DPTINFO_Empty.xml", DATABASE.RESMaster);
    }
    
     
        
    @When ("I add a department ($storeId, $dptId, $dptName, $dptNameLocal, $inheritFlag, $nonSales, $allowYen, $discType, $discFlag, $discAmt, $discRate, $taxRate, $taxType)")
    public final void addDept(final String storeId, final String dptId,
            final String dptName, final String dptNameLocal, final String inheritFlag, final String nonSales, final String allowYen, final String discType, final String discFlag, final String discAmt, final String discRate, final String taxRate, final String taxType) throws DaoException {
        String jsonString = "{DepartmentName : {en : '" + dptName + "', "
        		+ "jp : '" + dptNameLocal + "'},InheritFlag : '"+ inheritFlag+ "', NonSales : '"+nonSales+"', SubSmallInt5 : '"+allowYen+"', DiscountType : '"+discType+"', DiscountFlag : '"+discFlag+"', DiscountAmt : '"+discAmt+"', DiscountRate : '"+discRate+"', TaxRate : '"+taxRate+"', TaxType : '"+taxType+"'}";
        String storeID = storeId.equalsIgnoreCase("null") ? null : storeId;
        
        resultBase = dptResource.createDepartment(storeID, dptId, jsonString);
    }
    
    @When ("I add a department without TaxRate ($storeId, $dptId, $dptName, $dptNameLocal, $inheritFlag, $nonSales, $allowYen, $discType, $discFlag, $discAmt, $discRate, $taxType)")
    public final void addDeptWithoutTaxRate(final String storeId, final String dptId,
            final String dptName, final String dptNameLocal, final String inheritFlag, final String nonSales, final String allowYen, final String discType, final String discFlag, final String discAmt, final String discRate, final String taxType) {
        String jsonString = "{DepartmentName : {en : '" + dptName + "', "
        		+ "jp : '" + dptNameLocal + "'},InheritFlag : '"+ inheritFlag+ "', NonSales : '"+nonSales+"', SubSmallInt5 : '"+allowYen+"', DiscountType : '"+discType+"', DiscountFlag : '"+discFlag+"', DiscountAmt : '"+discAmt+"', DiscountRate : '"+discRate+"', TaxType : '"+taxType+"'}";
        String storeID = storeId.equalsIgnoreCase("null") ? null : storeId;
        
        resultBase = dptResource.createDepartment(storeID, dptId, jsonString);
    }
    
	@Given("an inactive Department Table")
	public final void givenDeletedTable() {
		new DBInitiator(
				"DepartmentResource",
				"test/ncr/res/mobilepos/department/resource/test/MST_DPTINFO_NotDeletedOrActive.xml",
				DATABASE.RESMaster);
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
