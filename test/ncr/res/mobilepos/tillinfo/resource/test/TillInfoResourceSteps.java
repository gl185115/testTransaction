package ncr.res.mobilepos.tillinfo.resource.test;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.tillinfo.resource.TillInfoResource;

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


public class TillInfoResourceSteps extends Steps {
	private DBInitiator dbInitiator;
    /**
     *  Store Resource object.
     */
	private TillInfoResource tillRes;
	 /**
	  * ResultBase object.
	  */
	private ResultBase resultBase;
	/**
     * Holds the value for CompanyId
     */
   private String companyId;
	 /**
	  * Holds the value for StoreId
	  */
	private String storeId;
	 /**
	  * Holds the value for TillId
	  */
    private String tillId;
    private ServletContext context;
    /**
     *Connects to the database.
     */
    @BeforeScenario
    public final void setUp() {
    	Requirements.SetUp();
        dbInitiator =  new DBInitiator("TillInfoResourceTest", DATABASE.RESMaster);            
    }
    /**
     * Destroys the database connection.
     */
    @AfterScenario
    public final void tearDown() {
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
            res = 0;
        } else if ("STORE_INVALID_PARAMS".equals(condition)){
            res = ResultBase.RES_STORE_INVALIDPARAMS;
        } else if ("TILL_NOT_EXIST".equals(condition)){
            res = ResultBase.RES_TILL_NOT_EXIST;
        } else if ("TILL_INVALID_PARAMS".equals(condition)){
            res = ResultBase.RES_TILL_INVALIDPARAMS;
        } else if ("SOD_PROCESSING".equals(condition)){
            res = ResultBase.RES_TILL_SOD_PROCESSING;
        } else if ("SOD_FINISHED".equals(condition)){
            res = ResultBase.RES_TILL_SOD_FINISHED;
        } else if ("INVALID_SOD_FLAG".equals(condition)){
            res = ResultBase.RES_TILL_INVALID_SOD_FLAG_VAL;
        } else if ("INVALID_BIZDATE".equals(condition)){
            res = ResultBase.RES_TILL_INVALID_BIZDATE;
        } else if ("DATABASE_ERROR".equals(condition)){
            res = ResultBase.RES_ERROR_DB;
        } else if ("DAO_ERROR".equals(condition)) {
        	res = ResultBase.RES_ERROR_DAO;
        } else if ("GENERAL_ERROR".equals(condition)){
            res = ResultBase.RES_ERROR_GENERAL;
        } else if ("RES_ERROR_INVALIDPARAMETER".equals(condition)){
            res = ResultBase.RES_ERROR_INVALIDPARAMETER;
        } else if ("RES_ERROR_NO_BIZDATE".equals(condition)){
            res = ResultBase.RES_NO_BIZDATE;
        }  
        
        return res;
    }
    
    /**
     *  Given Step : I have Till Info Resource and other necessary resources.
     */
    @Given("I have a Till Info Resource and other resources")
    public final void iHaveTillInfoResource() throws Exception {   	
        ServletContext context = Requirements.getMockServletContext();
        tillRes = new TillInfoResource();
        try {
            Field tillContext = tillRes.getClass().getDeclaredField("servletContext");
            tillContext.setAccessible(true);
            tillContext.set(tillRes, context);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Cant load Mock Servlet Context.");
        } 
        resultBase = new ResultBase(); 
    }
    
    @Given("a MST_TILLIDINFO with data")
    public final void tillInfoWithData() throws Exception {
    	 dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
         		"test/ncr/res/mobilepos/tillinfo/resource/test/MST_TILLIDINFO_UPDATED.xml");
    }
    
    @Given("an empty MST_TILLIDINFO")
    public final void anEmptyTill() throws Exception {
    	 dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
         		"test/ncr/res/mobilepos/tillinfo/resource/test/MST_TILLIDINFO_EMPTY.xml");
    }
    
    @Given("a MST_BIZDAY with data") 
    public final void bizDayWithData() throws Exception {
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
        		"test/ncr/res/mobilepos/tillinfo/resource/test/MST_BIZDAY_2016-01-01.xml");
    }
    
    @Given("an empty MST_BIZDAY")
    public final void anEmptyBizDay() throws Exception {
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
        		"test/ncr/res/mobilepos/tillinfo/resource/test/MST_BIZDAY_EMPTY.xml");
    }
    
    @Given("a MST_BIZDAY.TodayDate equal to MST_TILLIDINFO.BusinessDayDate")
    public final void sameBizDay() throws Exception {
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
        		"test/ncr/res/mobilepos/tillinfo/resource/test/MST_BIZDAY_2014-11-14.xml");
    }
    
    @Given("a MST_BIZDAY.TodayDate less than MST_TILLIDINFO.BusinessDayDate")
    public final void lessThanBizDay() throws Exception {
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
        		"test/ncr/res/mobilepos/tillinfo/resource/test/MST_BIZDAY_2012-11-14.xml");
    }   
    
    @Given("a PRM_SYSTEM_CONFIG data where MultiSOD does not exist")
    public final void systemConfigNoMultiSOD() throws Exception {
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
        		"test/ncr/res/mobilepos/tillinfo/resource/test/PRM_SYSTEM_CONFIG_NO_MULTISOD.xml");
    	context = Requirements.getMockServletContext();
    }   
    
    @Given("a PRM_SYSTEM_CONFIG data where MultiSOD is set to false")
    public final void systemConfigMultiSODFalse() throws Exception {
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
        		"test/ncr/res/mobilepos/tillinfo/resource/test/PRM_SYSTEM_CONFIG_MULTISOD_FALSE.xml");
    	context = Requirements.getMockServletContext();
    } 
    
    @Given("a PRM_SYSTEM_CONFIG data where MultiSOD is set to true")
    public final void systemConfigMultiSODTrue() throws Exception {
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
        		"test/ncr/res/mobilepos/tillinfo/resource/test/PRM_SYSTEM_CONFIG_MULTISOD_TRUE.xml");
    	context = Requirements.getMockServletContext();
    } 
    
    @Given("a PRM_SYSTEM_CONFIG data where MultiSOD is empty")
    public final void systemConfigMultiSODEmpty() throws Exception {
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
        		"test/ncr/res/mobilepos/tillinfo/resource/test/PRM_SYSTEM_CONFIG_MULTISOD_EMPTY.xml");
    	context = Requirements.getMockServletContext();
    } 
    
    @Given("a PRM_SYSTEM_CONFIG data where MultiSOD is not equal to 1")
    public final void systemConfigMultiSODDiffValue() throws Exception {
    	dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
        		"test/ncr/res/mobilepos/tillinfo/resource/test/PRM_SYSTEM_CONFIG_MULTISOD_DIFF_VALUE.xml");
    	context = Requirements.getMockServletContext();
    } 
    
    @When("I request to perform SOD with the following data: $exampleTables")
    public final void testExecAuth(ExamplesTable exampleTables) {
    	String compulsoryFlag = "";
    	String processingType = "";
    	
    	for(Map<String, String> tempData : exampleTables.getRows()) {
    	    companyId = "null".equals(tempData.get("companyId")) || null == tempData.get("companyId") ?
                    "01" : tempData.get("companyId");
    		storeId = "null".equals(tempData.get("storeId")) ?
    				null : tempData.get("storeId");
    		tillId = "null".equals(tempData.get("tillId")) ?
    				null : tempData.get("tillId");   
    		compulsoryFlag = "null".equals(tempData.get("compulsoryFlag")) ?
    				null : tempData.get("compulsoryFlag");
    		processingType = "null".equals(tempData.get("processingType")) ?
    				null : tempData.get("processingType");
    		resultBase = tillRes.getExecuteAuthority(companyId, storeId,
    						tillId,
    						tempData.get("terminalId"),
    						tempData.get("operatorNo"),
    						processingType,
    						compulsoryFlag);
    	}
    }
    
    @When("I request to release execution authority with the following data: $exampleTables")
    public final void testReleaseAuth(ExamplesTable exampleTables) {
    	for(Map<String, String> tempData : exampleTables.getRows()) {
    	    companyId = tempData.get("companyId").equals("null") ?
                    null : tempData.get("companyId");
    		storeId = tempData.get("storeId").equals("null") ?
    				null : tempData.get("storeId");
    		tillId = tempData.get("tillId").equals("null") ?
    				null : tempData.get("tillId");   		
    		resultBase = tillRes.releaseExecuteAuthority(companyId, storeId, 
    						tillId, 
    						tempData.get("terminalId"), 
    						tempData.get("operatorNo"), 
    						tempData.get("processingType"));   		
    	}
    }
    
    @When("I request to allow multi-sod")
    public final void testMultiSod() {
    	GlobalConstant.setMultiSOD(true);
    }
    
    @Then("I should have the following sod till info data: $exampleTables")
    public final void testResultData(ExamplesTable exampleTables) throws Exception {
		ITable actualTable = dbInitiator.getQuery("MST_TILLIDINFO", "SELECT " +
							"StoreId, TillId, TerminalId, SodFlag, " + 
							"UpdAppId, UpdOpeCode " + 
							"FROM RESMaster.dbo.MST_TILLIDINFO " + 
							"WHERE StoreId = " + storeId + " " +
							"AND TillId = " + tillId);
		Assert.assertEquals("Compare the expected count of till items",
				exampleTables.getRowCount(), actualTable.getRowCount());
		int i = 0;
		
    	for(Map<String, String> tempData : exampleTables.getRows()) {
    		Assert.assertEquals("Compare storeId ", tempData.get("storeId"),
    				actualTable.getValue(i, "StoreId"));
    		Assert.assertEquals("Compare tillId ", tempData.get("tillId"),
    				actualTable.getValue(i, "TillId"));
    		Assert.assertEquals("Compare terminalId ", tempData.get("terminalId"),
    				actualTable.getValue(i, "TerminalId"));
    		Assert.assertEquals("Compare sodFlag ", Integer.parseInt(tempData.get("sodFlag")),
    				actualTable.getValue(i, "SodFlag"));
    		Assert.assertEquals("Compare updAppId ", tempData.get("updAppId"),
    				actualTable.getValue(i, "UpdAppId"));
    		Assert.assertEquals("Compare updOpeCode ", tempData.get("updOpeCode"),
    				actualTable.getValue(i, "UpdOpeCode"));
    		i++;
    	}
    }
    
	@Then("the result should be {$result}")
    public final void testResultCode(String result) {
    	 Assert.assertEquals(getResult(result), resultBase.getNCRWSSResultCode());
    }
}
