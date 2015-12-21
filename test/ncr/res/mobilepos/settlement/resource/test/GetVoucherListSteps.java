package ncr.res.mobilepos.settlement.resource.test;

import static org.junit.Assert.*;

import java.util.List;
import java.util.Map;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.ExceptionHelper;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.ResultBaseHelper;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.settlement.model.SettlementInfo;
import ncr.res.mobilepos.settlement.model.VoucherInfo;
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

public class GetVoucherListSteps extends Steps {
    private DBInitiator dbInitiator;
    @InjectMocks
    private SettlementResource settlementResource;
    @Mock
    private DAOFactory daoFactory;
    private SettlementInfo settlementInfo;
    
    /**
     *Connects to the database.
     */
    @BeforeScenario
    public final void setUp() {
    	Requirements.SetUp();
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
    	dbInitiator =  new DBInitiator("SettlementResourceTest", DATABASE.RESTransaction); 
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
    			"test/ncr/res/mobilepos/settlement/resource/datasets/TXU_TOTAL_VOUCHER_SUMDAY.xml");
    	dbInitiator =  new DBInitiator("SettlementResourceTest", DATABASE.RESMaster); 
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
    			"test/ncr/res/mobilepos/settlement/resource/datasets/MST_TENDERINFO.xml");
    }

    @Given("that database is throwing an unexpected {$exception}")
   	public final void givenThrownException(String exception) {
    	MockitoAnnotations.initMocks(this);
        Exception ex = new Exception();
        ex = ExceptionHelper.getException(exception);  			
        try {
        	Mockito.stub(daoFactory.getSettlementInfoDAO()).toThrow(ex);
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    @When("I request to get voucher list with the following data: $examplesTable")
    public final void whenIGetVoucherList(ExamplesTable examplesTable) {
    	Map<String, String> data = examplesTable.getRow(0);
    	String companyId = data.get("companyId");
    	String storeId = data.get("storeId");
    	String businessDayDate = data.get("businessDayDate");
    	int trainingFlag = Integer.parseInt(data.get("trainingFlag"));
    	companyId = StringUtility.convNullStringToNull(companyId);
    	storeId = StringUtility.convNullStringToNull(storeId);
    	businessDayDate = StringUtility.convNullStringToNull(businessDayDate);
    	settlementInfo = settlementResource.getVoucherList(companyId, storeId, businessDayDate, trainingFlag);
    }
    
    @Then("I should have the following data: $examplesTable")
    public final void thenIShouldHaveTheData(ExamplesTable examplesTable) {
    	List<VoucherInfo> voucherList = settlementInfo.getVoucherList();
    	int i = 0;
    	
    	assertEquals("Compare the expected count of voucher items",
    			examplesTable.getRowCount(), voucherList.size());
    	
    	for(Map<String, String> data : examplesTable.getRows()) {
            assertEquals("Compare companyId: ", data.get("companyId"),
            		voucherList.get(i).getCompanyId());
            assertEquals("Compare storeId: ", data.get("storeId"),
            		voucherList.get(i).getStoreId());
            assertEquals("Compare voucherId: ", data.get("voucherId"),
            		voucherList.get(i).getVoucherCompanyId());
            assertEquals("Compare voucherName: ", data.get("voucherName"),
            		voucherList.get(i).getVoucherName());
            assertEquals("Compare voucherKanaName: ", data.get("voucherKanaName"),
            		voucherList.get(i).getVoucherKanaName());
            assertEquals("Compare businessDayDate: ", StringUtility.convNullStringToNull(data.get("businessDayDate")),
            		voucherList.get(i).getBusinessDayDate());
            assertEquals("Compare trainingFlag: ", Integer.parseInt(data.get("trainingFlag")),
            		voucherList.get(i).getTrainingFlag());
            assertEquals("Compare salesItemCnt: ", Integer.parseInt(data.get("salesItemCnt")),
            		voucherList.get(i).getSalesItemCnt());
            assertEquals("Compare salesItemAmt: ", Double.parseDouble(data.get("salesItemAmt")),
            		voucherList.get(i).getSalesItemAmt(), 0);
            i++;
    	}
    }
    
    @Then("the resulting voucher list should be empty")
    public final void thenTheVoucherListShouldBeEmpty() {
    	assertTrue(settlementInfo.getVoucherList().isEmpty());
    }
    
    @Then("the result should be {$result}")
    public final void thenResultShouldBe(String result) {
    	 assertEquals(ResultBaseHelper.getErrorCode(result), settlementInfo.getNCRWSSResultCode());
    }
}
