package ncr.res.mobilepos.deviceinfo.resource.test;

import java.util.Map;

import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

public class SetTillIdSteps extends Steps {
	private DBInitiator dbInitiator;
	private DeviceInfoResource deviceRes;
	private ResultBase result;
	private String storeId;
	private String terminalId;
	
	@BeforeScenario
    public final void setUp() throws Exception {
        Requirements.SetUp();
        dbInitiator =  new DBInitiator("SetTillIdTest", DATABASE.RESMaster);             
    }

    @AfterScenario
    public final void TearDown() {
        Requirements.TearDown();
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
        } else if ("GENERAL_ERROR".equals(condition)){
            res = ResultBase.RES_ERROR_GENERAL;
        } else if("DAO_ERROR".equals(condition)) {
        	res = ResultBase.RES_ERROR_DAO;
        } else if("TILL_NOT_FOUND".equals(condition)) {
        	res = ResultBase.RES_TILL_NOT_EXIST;
        } else if("DEVICE_CTRL_INVALID_PARAM".equals(condition)) {
        	res = ResultBase.RESDEVCTL_INVALIDPARAMETER;
        } else if("TILL_INVALID_PARAM".equals(condition)) {
        	res = ResultBase.RES_TILL_INVALIDPARAMS;
        }
        return res;
    }
    
    @Given("a DeviceInfo resource")
    public final void deviceInfoResource() {
        deviceRes = new DeviceInfoResource();
        deviceRes.setContext(Requirements.getMockServletContext());
        Assert.assertNotNull(deviceRes);
        result = new ResultBase();
    }
    
    @Given("a data set for MST_DEVICEINFO")
    public final void deviceInfoDataSet() throws Exception {
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
         		"test/ncr/res/mobilepos/deviceinfo/datasets/MST_DEVICE_INFO_9999_000031_9999_1_9876.xml");
    }
    
    @Given("a data set for MST_TILLIDINFO")
    public final void tillIdInfoDataSet() throws Exception {
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
         		"test/ncr/res/mobilepos/deviceinfo/datasets/MST_TILLIDINFO_FOR_SET.xml");
    }
    
    @When("I request to set till id with storeId{$storeId}, terminalId{$terminalId}, tillId{$tillId}")
    public final void setTillId(String storeId, String terminalId, String tillId) {
    	storeId = "null".equals(storeId) ? null: storeId;
    	terminalId = "null".equals(terminalId) ? null: terminalId;
    	tillId = "null".equals(tillId) ? null: tillId;
    	this.storeId = storeId;
    	this.terminalId = terminalId;
    	
    	result = deviceRes.setTillId(storeId, terminalId, tillId);
    }
    
    @Then("I should have the following data: $exampleTables")
    public final void testResultData(final ExamplesTable exampleTables) throws Exception {
    	ITable actualTable = dbInitiator.getQuery("MST_DEVICEINFO", "SELECT " +
				"StoreId, TerminalId, TillId, " + 
				"UpdAppId, UpdOpeCode " + 
				"FROM RESMaster.dbo.MST_DEVICEINFO " + 
				"WHERE StoreId = " + storeId + " " +
				"AND TerminalId = " + terminalId);
    	
    	int i = 0;
		
    	for(Map<String, String> tempData : exampleTables.getRows()) {
    		Assert.assertEquals("Compare storeId ", tempData.get("storeId"),
    				actualTable.getValue(i, "StoreId"));
    		Assert.assertEquals("Compare terminalId ", tempData.get("terminalId"),
    				actualTable.getValue(i, "TerminalId"));
    		Assert.assertEquals("Compare tillId ", "null".equals(tempData.get("tillId")) ? 
    				null : tempData.get("tillId"),
    					actualTable.getValue(i, "TillId"));
    		Assert.assertEquals("Compare updAppId ", tempData.get("updAppId"),
    				actualTable.getValue(i, "UpdAppId"));
    		Assert.assertEquals("Compare updOpeCode ", "null".equals(tempData.get("updOpeCode")) ? 
    				null : tempData.get("updOpeCode"),
    					actualTable.getValue(i, "UpdOpeCode"));
    		i++;
    	}
    }
    
    @Then("the result should be {$condition}")
    public final void testResult(final String condition){
    	int res = getResult(condition);
        Assert.assertEquals("Compare result ", res, result.getNCRWSSResultCode());
    }
}
