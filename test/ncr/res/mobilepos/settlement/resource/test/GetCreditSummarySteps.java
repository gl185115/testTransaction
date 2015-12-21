package ncr.res.mobilepos.settlement.resource.test;

import java.util.Map;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.ExceptionHelper;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.ResultBaseHelper;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.settlement.model.CreditInfo;
import ncr.res.mobilepos.settlement.model.SettlementInfo;
import ncr.res.mobilepos.settlement.resource.SettlementResource;

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

import static org.junit.Assert.*;

public class GetCreditSummarySteps extends Steps {
	private DBInitiator dbInitiator;
	@InjectMocks
    private SettlementResource settlementResource;
    @Mock
    private DAOFactory daoFactory;
    private SettlementInfo settlementInfo;
    private CreditInfo creditInfo;
	
    /**
     *Connects to the database.
     */
    @BeforeScenario
    public final void setUp() {
    	Requirements.SetUp();
        dbInitiator =  new DBInitiator("SettlementResourceTest", DATABASE.RESTransaction);
    }
    
    /**
     * Destroys the database connection.
     */
    @AfterScenario
    public final void tearDown() {
        Requirements.TearDown();
    }
    
    @Given("I have a Settlement resource and other resources")
    public final void givenSettlementResource() throws Exception {
    	settlementResource = new SettlementResource();
    	settlementInfo = new SettlementInfo();
    	creditInfo = new CreditInfo();
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
    			"test/ncr/res/mobilepos/settlement/resource/datasets/TXU_TOTAL_CREDITSTORE_SUMDAY.xml");
    }
    
    @Given("that database is throwing an unexpected {$exception}")
   	public final void givenThrownException(String exception) {
    	MockitoAnnotations.initMocks(this);
        Exception ex = ExceptionHelper.getException(exception);  			
        try {
        	Mockito.stub(daoFactory.getSettlementInfoDAO()).toThrow(ex);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    @When("I request to get credit summary with the following data: $examplesTable")
    public final void whenIGetCreditSummary(ExamplesTable examplesTable) {
    	Map<String, String> data = examplesTable.getRow(0);
    	String companyId = data.get("companyId");
    	String storeId = data.get("storeId");
    	String businessDayDate = data.get("businessDayDate");
    	int trainingFlag = Integer.parseInt(data.get("trainingFlag"));
    	companyId = StringUtility.convNullStringToNull(companyId);
    	storeId = StringUtility.convNullStringToNull(storeId);
    	businessDayDate = StringUtility.convNullStringToNull(businessDayDate);
    	settlementInfo = settlementResource.getCreditSummary(companyId, storeId, businessDayDate, trainingFlag);
    	creditInfo = settlementInfo.getCreditInfo();
    }
    
    @Then("the sales count sum should be {$countSum}")
    public final void thenTheSalesCountSumShouldBe(int countSum) {
    	assertEquals(countSum, creditInfo.getSalesCntSum());
    }
    
    @Then("the sales amt sum should be {$amtSum}")
    public final void thenTheSalesAmtSumShouldBe(double amtSum) {
    	assertEquals(amtSum, creditInfo.getSalesAmtSum(), 0);
    }
    
    @Then("the result should be {$result}")
    public final void thenResultShouldBe(String result) {
    	 assertEquals(ResultBaseHelper.getErrorCode(result), settlementInfo.getNCRWSSResultCode());
    }
}
