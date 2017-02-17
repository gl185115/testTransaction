package ncr.res.mobilepos.tillinfo.resource.test;

import junit.framework.Assert;

import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.tillinfo.model.ViewTill;
import ncr.res.mobilepos.tillinfo.resource.TillInfoResource;

import static org.junit.Assert.assertEquals;

@SuppressWarnings("deprecation")
public class GetTillListSteps extends Steps {
	private TillInfoResource tillInfoResource;
	private ViewTill list;
	private DBInitiator dbInitMaster;
	
	@BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }

    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("a TillInfoResource Resource")
    public final void createResource() {        
        ServletContext context = Requirements.getMockServletContext();
        tillInfoResource = new TillInfoResource();
        try {
            Field tillContext = tillInfoResource.getClass().getDeclaredField("servletContext");
            tillContext.setAccessible(true);
            tillContext.set(tillInfoResource, context);
        } catch (Exception e) {
            Assert.fail("Cant load Mock Servlet Context.");
        } 
    }

    @Given("a RESMaster DBInitiator")
    public final void createDBInitiator() {
        dbInitMaster = new DBInitiator("GetTillListSteps", DATABASE.RESMaster);
    }

    @Given("a $dataset dataset")
    public final void insertDatabase(final String dataset) {
    	try {
	    	dbInitMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
	    				"test/ncr/res/mobilepos/tillinfo/resource/test/"
	    				+ dataset + ".xml");
	    	
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @When("I get list of tills with retailstoreid:$storeid")
    public final void getExecuteAuthority(String storeId) {
    	if ("null".equalsIgnoreCase(storeId)) {
    		storeId = null;
    	}
    	list = tillInfoResource.getTillList(storeId);
    }
    
    @Then("ResultCode should be $resultCode")
    public final void validateResult(final String resultCode) {
        assertEquals("Assert that ResultCode equals " + resultCode, 
        		resultCode, String.valueOf(list.getNCRWSSResultCode()));
    }
    
    @Then("MST_TILLIDINFO should have the following row(s): $rows")
    public final void validateDatabase(final ExamplesTable rows) {
    	ITable actualTable = dbInitMaster.getTableSnapshot("MST_TILLIDINFO");
    	String[] columns = {"CompanyId", "StoreId", "TillId", "TerminalId", 
    				"BusinessDayDate", "SodFlag", "EodFlag", "DeleteFlag"};
        assertEquals("Compare the number of rows in MST_TILLIDINFO", 
        		rows.getRowCount(), actualTable.getRowCount());
        try {
	        int i = 0;
	        for (Map<String, String> row : rows.getRows()) {
	            for (int j = 0; j < columns.length; j++) {
		        	assertEquals("Compare " + columns[j] + " at row " + i, 
		            		row.get(columns[j]), String.valueOf(
		            				actualTable.getValue(i, columns[j]))
		            				.trim());
	            }
	            i++;
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }
    
    @Then("I should get the following tills: $rows")
    public final void shouldGetTheTills(ExamplesTable rows) {
        assertEquals("Compare the number of result", rows.getRowCount(), list.getTillList().size());
        try {
        	int i = 0;
	        for (Map<String, String> row : rows.getRows()) {
	            assertEquals("Compare StoreId at row " + i, 
		            		row.get("StoreId"), list.getTillList().get(i).getStoreId());
	            assertEquals("Compare TillId at row " + i, 
	            		row.get("TillId"), list.getTillList().get(i).getTillId());
	            assertEquals("Compare TerminalId at row " + i, 
	            		row.get("TerminalId"), list.getTillList().get(i).getTerminalId());
	            assertEquals("Compare BusinessDayDate at row " + i, 
	            		row.get("BusinessDayDate"), list.getTillList().get(i).getBusinessDayDate());
	            assertEquals("Compare SodFlag at row " + i, 
	            		row.get("SodFlag"), list.getTillList().get(i).getSodFlag());
	            assertEquals("Compare EodFlag at row " + i, 
	            		row.get("EodFlag"), list.getTillList().get(i).getEodFlag());
	            i++;
	        }
        } catch (Exception e) {
        	e.printStackTrace();
        }
    }

}
