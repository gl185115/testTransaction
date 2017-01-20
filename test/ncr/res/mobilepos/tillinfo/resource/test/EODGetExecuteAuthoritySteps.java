package ncr.res.mobilepos.tillinfo.resource.test;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;

import junit.framework.Assert;
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

public class EODGetExecuteAuthoritySteps extends Steps {
	
	private TillInfoResource tillInfoResource;
	private ResultBase resultBase;
	private DBInitiator dbInitMaster;
	private DBInitiator dbInitTransaction;

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }

    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("a {$resource} Resource")
    public final void createResource(String resource) {
    	ServletContext context = Requirements.getMockServletContext();
        if ("TillInfoResource".equalsIgnoreCase(resource)) {
        	tillInfoResource = new TillInfoResource();
        }
        try {
            Field tillContext = tillInfoResource.getClass().getDeclaredField("servletContext");
            tillContext.setAccessible(true);
            tillContext.set(tillInfoResource, context);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Cant load Mock Servlet Context.");
        } 
    }

    @Given("a {$databaseName} DBInitiator")
    public final void createDBInitiator(String databaseName) {
        if ("RESMaster".equalsIgnoreCase(databaseName)) {
        	dbInitMaster = new DBInitiator("RESMaster", DATABASE.RESMaster);
        } else if ("RESTransaction".equalsIgnoreCase(databaseName)) {
        	dbInitTransaction = new DBInitiator("RESTransaction", 
        			DATABASE.RESTransaction);
        }
    }

    @Given("a {$databaseName} {$dataset} dataset")
    public final void insertDatabase(String databaseName, String dataset) {
    	try {
	    	if ("RESMaster".equalsIgnoreCase(databaseName)) {
	    		dbInitMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
	    				"test/ncr/res/mobilepos/tillinfo/resource/test/"
	    				+ dataset + ".xml");
	    	} else if ("RESTransaction".equalsIgnoreCase(databaseName)) {
	    		dbInitTransaction.ExecuteOperation(
	    				DatabaseOperation.CLEAN_INSERT, 
	    				"test/ncr/res/mobilepos/tillinfo/resource/test/"
	    				+ dataset + ".xml");
	    	}
    	} catch (Exception e) {
    		e.printStackTrace();
    	}
    }
    
    @When("I perform a getExecuteAuthotity request with companyid:{$companyId} "
    		+ "retailstoreid:{$retailStoreId}, "
    		+ "tillid:{$tillId}, "
    		+ "terminalid:{$terminalId}, "
    		+ "operatorno:{$operatorNo}, "
    		+ "processing:{$processing}, "
    		+ "compulsoryflag:{$compulsoryFlag}")
    public final void getExecuteAuthority(String companyId, String retailStoreId, String tillId, 
    		String terminalId, String operatorNo, String processing, 
    		String compulsoryFlag) {
    	if ("null".equalsIgnoreCase(retailStoreId)) {
    		retailStoreId = null;
    	}
    	if ("null".equalsIgnoreCase(tillId)) {
    		tillId = null;
    	}
    	if ("null".equalsIgnoreCase(terminalId)) {
    		terminalId = null;
    	}
    	if ("null".equalsIgnoreCase(operatorNo)) {
    		operatorNo = null;
    	}
    	if ("null".equalsIgnoreCase(processing)) {
    		processing = null;
    	}
    	if ("null".equalsIgnoreCase(compulsoryFlag)) {
    		compulsoryFlag = null;
    	}
    	
    	resultBase = tillInfoResource.getExecuteAuthority(companyId, retailStoreId, 
    			tillId, terminalId, operatorNo, processing, compulsoryFlag);
    }
    
    @When("I perform a releaseExecuteAuthotity request with companyid:{$companyId} "
    		+ "retailstoreid:{$retailStoreId}, "
    		+ "tillid:{$tillId}, "
    		+ "terminalid:{$terminalId}, "
    		+ "operatorno:{$operatorNo}, "
    		+ "processing:{$processing}")
    public final void releaseExecuteAuthority(String companyId, String retailStoreId, String tillId, 
    		String terminalId, String operatorNo, String processing) {
    	if ("null".equalsIgnoreCase(retailStoreId)) {
    		retailStoreId = null;
    	}
    	if ("null".equalsIgnoreCase(tillId)) {
    		tillId = null;
    	}
    	if ("null".equalsIgnoreCase(terminalId)) {
    		terminalId = null;
    	}
    	if ("null".equalsIgnoreCase(operatorNo)) {
    		operatorNo = null;
    	}
    	if ("null".equalsIgnoreCase(processing)) {
    		processing = null;
    	}
    	
    	resultBase = tillInfoResource.releaseExecuteAuthority(companyId, retailStoreId, 
    			tillId, terminalId, operatorNo, processing);
    }
    
    @When("I perform a search request with "
    		+ "retailstoreid:{$retailStoreId}, "
    		+ "tillid:{$tillId}, "
    		+ "terminalid:{$terminalId}")
    public final void search(String retailStoreId, String tillId, 
    		String terminalId) {
    	if ("null".equalsIgnoreCase(retailStoreId)) {
    		retailStoreId = null;
    	}
    	if ("null".equalsIgnoreCase(tillId)) {
    		tillId = null;
    	}
    	if ("null".equalsIgnoreCase(terminalId)) {
    		terminalId = null;
    	}
    	// for test
    	//resultBase = tillInfoResource.search(companyId, retailStoreId, tillId, terminalId);
    	resultBase = tillInfoResource.search("01", retailStoreId, tillId, terminalId);
    }
    
    @Then("ResultCode should be {$resultCode}")
    public final void validateResult(String resultCode) {
        assertEquals("Assert that ResultCode equals " + resultCode, 
        		resultCode, String.valueOf(resultBase.getNCRWSSResultCode()));
    }
    
    @Then("{$databaseName} {$table} should have the following row(s): $rows")
    public final void validateDatabase(String databaseName, String table, 
    		ExamplesTable rows) {
    	ITable actualTable = null;
    	
    	if ("RESMaster".equalsIgnoreCase(databaseName)) {
    		actualTable = dbInitMaster.getTableSnapshot(table);
    	} else if ("RESTransaction".equalsIgnoreCase(databaseName)) {
    		actualTable = dbInitTransaction.getTableSnapshot(table);
    	}
    	
    	String[] columns = null;
    	if ("MST_TILLIDINFO".equalsIgnoreCase(table)) {
    		String[] cols = {"StoreId", "TillId", "TerminalId", 
    				"BusinessDayDate", "SodFlag", "EodFlag", "UpdOpeCode"};
    		
    		columns = cols;
    	} else if ("MST_DEVICEINFO".equalsIgnoreCase(table)) {
    		String[] cols = {"StoreId", "TerminalId", "TillId"};
    		
    		columns = cols;
    	} else if ("TXU_POS_CTRL".equalsIgnoreCase(table)) {
    		String[] cols = {"StoreId", "TerminalNo", "OpeCode"};
    		
    		columns = cols;
    	}
    	
        assertEquals("Compare the number of rows in " + table, 
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

}
