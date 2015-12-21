package ncr.res.mobilepos.deviceinfo.resource.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.tillinfo.model.Till;
import ncr.res.mobilepos.tillinfo.model.ViewTill;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

public class ListTillSteps extends Steps {
	private DBInitiator dbInitiator;
	private DeviceInfoResource deviceRes;
    private ViewTill viewTill;
    private List<Till> tillList;
   
	@BeforeScenario
    public final void setUp() throws Exception {
        Requirements.SetUp();
        dbInitiator =  new DBInitiator("ListTillTest", DATABASE.RESMaster);      
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
         		"test/ncr/res/mobilepos/deviceinfo/datasets/MST_TILLIDINFO_FOR_LIST.xml");
        
    }

    @AfterScenario
    public final void TearDown() {
        Requirements.TearDown();
        GlobalConstant.setMaxSearchResults(2);
    }
    
	/**
	 * A helper function to get the a given result Code
	 * @param condition
	 * @return
	 */
    private int getResult(final String condition) {
        int res = -1; //Fail
        
        if ("OK".equals(condition)){
            res = ResultBase.RES_OK;
        } else if ("DATABASE_ERROR".equals(condition)){
            res = ResultBase.RES_ERROR_DB;
        }  else if ("GENERAL_ERROR".equals(condition)){
            res = ResultBase.RES_ERROR_GENERAL;
        } 
        return res;
    }
    
    @Given("a DeviceInfo resource")
    public final void deviceInfoResource() {
        deviceRes = new DeviceInfoResource();
        deviceRes.setContext(Requirements.getMockServletContext());
        Assert.assertNotNull(deviceRes);
        GlobalConstant.setMaxSearchResults(2);
        viewTill = new ViewTill();
        tillList = new ArrayList<Till>();
    }
    
    @When("I set the search limit to $limit")
    public final void setLimit(final int limit) {
        GlobalConstant.setMaxSearchResults(limit);
    }
    
    @When("I request to get tills with storeid{$storeid}, key{$key}, limit{$limit}")
    public final void getTills(String storeid, String key, int limit) {
    	storeid = storeid.equals("null") ? null: storeid;
    	key = key.equals("null") ? null: key;

        viewTill = deviceRes.getAllTills(storeid, key, limit);
        tillList = viewTill.getTillList();
    }
    
    @Then("I should have the following list of tills: $exampleTables")
    public final void testResultArray(ExamplesTable exampleTables) {   	
    	int i = 0;
		
    	for(Map<String, String> tempData : exampleTables.getRows()) {
    		Assert.assertEquals("Compare storeId ", tempData.get("storeId"),
    				tillList.get(i).getStoreId());
    		Assert.assertEquals("Compare tillId ", tempData.get("tillId"),
    				tillList.get(i).getTillId());
    		Assert.assertEquals("Compare terminalId ", tempData.get("terminalId"),
    				tillList.get(i).getTerminalId());
    		Assert.assertEquals("Compare business day date", tempData.get("bizdate"),
    				tillList.get(i).getBusinessDayDate());
    		Assert.assertEquals("Compare sodFlag ", tempData.get("sodFlag"),
    				tillList.get(i).getSodFlag());	
    		Assert.assertEquals("Compare eodFlag ", tempData.get("eodFlag"),
    				tillList.get(i).getEodFlag());  		
    		Assert.assertEquals("Compare operatorId ", tempData.get("operatorId"),
    				tillList.get(i).getUpdOpeCode());
    		i++;
    	}
    }
    
    @Then("I should get {$numOfTills} tills")
    public final void testListSize(final int numOfTills) {
    	Assert.assertEquals("Compare the expected count of till items",
    			numOfTills, tillList.size());
    }
    
    @Then("the result should be {$condition}")
    public final void testResult(final String condition){
    	int res = getResult(condition);
        Assert.assertEquals("Compare result ", res, viewTill.getNCRWSSResultCode());
    }
}
