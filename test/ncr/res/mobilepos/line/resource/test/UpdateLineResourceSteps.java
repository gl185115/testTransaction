/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * UpdateLineResourceSteps
 *
 * The Test Steps for Updating Line Resource.
 *
 *	Sul Romares
 */
package ncr.res.mobilepos.line.resource.test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.line.resource.LineResource;
import ncr.res.mobilepos.model.ResultBase;

import org.dbunit.dataset.DataSetException;
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

/**
 * The Test Steps for Updating Line Resource.
 *
 * @author sr185217
 */
public class UpdateLineResourceSteps extends Steps {
    /**
     * LineResource instance.
     */
    private LineResource lineResource = null;
    /**
     * ResultBase instance. Holds the actual resultcode.
     */
    private ResultBase actualresultBase = null;
    /**
     * The database unit test initiator.
     */
    private DBInitiator dbInitiator = null;

    /**
     * Executed before start of the scenario.
     *
     * @throws Exception
     *             thrown when set-up fail.
     */
    @BeforeScenario
    public final void setUpClass() throws Exception {
        Requirements.SetUp();
        dbInitiator = new DBInitiator("UpdateLineResourceSteps", DATABASE.RESMaster);
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/line/resource/test/"
                + "line_for_update.xml");
        //initialize store
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/line/resource/test/"
                + "MST_STOREINFO_LIST.xml");
        
        //initialize department
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/line/resource/test/"
                + "MST_DPTINFO_LIST.xml");  
        
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
		        "test/ncr/res/mobilepos/department/resource/test/"
		        + "prm_system_config_with_tax_rate.xml");	
    }

    /**
     * Executed after end of the scenario.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    } 
    
    /**
     * Given Step: Set the table definition for Line(s) to empty.
     */
    @Given("an empty table for line")
    public final void anEmptyTableForLine() {
        try {
            dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    "test/ncr/res/mobilepos/line/resource/test/"
                    + "line_empty.xml");
        } catch (Exception e) {
            Assert.fail("Cant set the database for line.");
        }
    }
    
    /**
     * Given Step: Set the table definition for Line(s) to empty.
     */
    @Given("a line table")
    public final void anTableForLine() {
        try {
            dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    "test/ncr/res/mobilepos/line/resource/test/"
                    + "line_for_update.xml");
        } catch (Exception e) {
            Assert.fail("Cant set the database for line.");
        }
    }

    /**
     * Given Step: Set an empty resource.
     */
    @Given("a line resource")
    public final void aLineResource() {
        lineResource = new LineResource();
        lineResource.setContext(Requirements.getMockServletContext());
    }

    /**
     * When Step: Add a Line.
     * @param newline The new line.
     */
    @When("the following line is updated: $newline")
    public final void theFollowingLineIsUpdated(final ExamplesTable newline) {
        Map<String, String> lineinfo = newline.getRow(0);  

        String linejson = lineinfo.get("linejson");     
        linejson = (null == linejson || "null".equalsIgnoreCase(linejson))? null : linejson;        
       
        String retailstoreid = lineinfo.get("retailstoreid"); 
        retailstoreid = (null == retailstoreid || "null".equalsIgnoreCase(retailstoreid))? null : retailstoreid; 
        
        String department = lineinfo.get("department");     
        department = (null == department || "null".equalsIgnoreCase(department))? null : department; 
                
        String lineid = lineinfo.get("lineid");     
        lineid = (null == lineid || "null".equalsIgnoreCase(lineid))? null : lineid;
        
        actualresultBase =
            lineResource.updateLine(retailstoreid, department, lineid, linejson);
        
    }    
    /**
     * Then Step: Compare the result code.
     * @param expectedresult The expected result.
     */
    @Then("the result code should be {$expectedresult}")
    public final void theResultCodeShouldBe(final int expectedresult) {
        Assert.assertEquals("Compare the actual result",
                expectedresult, actualresultBase.getNCRWSSResultCode());
    }
    
    /**
     * Then Step: Compare the existing Lines in the database.
     * @param expectedLines The expected Lines in the database.
     * @throws DataSetException The Exception thrown when test fail.
     */
    @Then("the following lines in the database should be: $expectedLines")
    public final void theFollowingLinesInTheDatabaseShouldBe(final ExamplesTable expectedLines) throws DataSetException {

        List<Map<String, String>> expLineRows = expectedLines.getRows();
        ITable actualDataRows = null;       
        actualDataRows = dbInitiator.getTableSnapshot("MST_LINEINFO");

        Assert.assertEquals("Compare that the number "
                + "of rows in Line are exact: ",
                expectedLines.getRowCount(), actualDataRows.getRowCount());
        
        int index = 0;        
        try {
            for (Map<String, String> expLine : expLineRows) {
            	Assert.assertEquals(
            			"Compare the StoreId row" + index + ": ",
                        expLine.get("RetailStoreID"),
                        actualDataRows.getValue(index, "StoreId")); 
            	Assert.assertEquals(
            			"Compare the Line row" + index + ": ",
                        expLine.get("Line"),
                        actualDataRows.getValue(index, "Line"));
            	Assert.assertEquals(
            			"Compare the DescriptionEN row" + index + ": ", 
                		String.valueOf(expLine.get("DescriptionEN")),                		
                		String.valueOf(actualDataRows.getValue(index, "LineName")));            	
            	Assert.assertEquals(
            			"Compare the DescriptionJP row" + index + ": ", 
                		String.valueOf(expLine.get("DescriptionJP")), 
                		String.valueOf(actualDataRows.getValue(index, "LineNameLocal")));
            	Assert.assertEquals(
                        "Compare the Department row" + index + ": ",
                        expLine.get("Department"),
                        actualDataRows.getValue(index, "DPT"));            	
            	Assert.assertEquals(
                        "Compare the TaxType row" + index + ": ",
                        expLine.get("TaxType"), "" +
                        actualDataRows.getValue(index, "TaxType"));
            	Assert.assertEquals(
                        "Compare the TaxRate row" + index + ": ",
                        expLine.get("TaxRate"), "" +
                        actualDataRows.getValue(index, "TaxRate"));
            	Assert.assertEquals(
                        "Compare the DiscountType row" + index + ": ",
                        expLine.get("DiscountType"), "" +
                        actualDataRows.getValue(index, "DiscountType"));
            	Assert.assertEquals(
                        "Compare the ExceptionFlag row" + index + ": ",
                        expLine.get("ExceptionFlag"), "" +
                        actualDataRows.getValue(index, "ExceptionFlag"));              	
            	Assert.assertEquals(
                        "Compare the DiscountFlag row" + index + ": ",
                        expLine.get("DiscountFlag"), "" +
                        actualDataRows.getValue(index, "DiscountFlag"));  
            	String strDiscountamount = expLine.get("DiscountAmount");
            	if (strDiscountamount.equals("null")) {
                    Assert.assertNull("Assume that DiscountAmount is "
                            + "null for row" + index + ":",
                            actualDataRows.getValue(index, "DiscountAmt"));
                } else {
                    Assert.assertEquals("Compare the DiscountAmount row" + index
                            + ": ", "" + (Double.parseDouble(strDiscountamount)),
                             "" + ((BigDecimal) actualDataRows.getValue(index,"DiscountAmt")).doubleValue());
                }
            	String strDiscountRate= expLine.get("DiscountRate");
            	if (strDiscountRate.equals("null")) {
                    Assert.assertNull("Assume that DiscountRate is "
                            + "null for row" + index + ":",
                            actualDataRows.getValue(index, "DiscountRate"));
                } else {
                    Assert.assertEquals("Compare the DiscountRate row" + index
                            + ": ", "" + (Double.parseDouble(strDiscountRate)),
                             "" + ((BigDecimal) actualDataRows.getValue(index,"DiscountRate")).doubleValue());
                }
            	Assert.assertEquals(
                        "Compare the AgeRestrictedFlag row" + index + ": ",
                        expLine.get("AgeRestrictedFlag"), "" +
                        actualDataRows.getValue(index, "AgeRestrictedFlag"));
            	Assert.assertEquals(
                        "Compare the InheritFlag row" + index + ": ",
                        expLine.get("InheritFlag"), "" +
                        actualDataRows.getValue(index, "InheritFlag"));
            	Assert.assertEquals(
                        "Compare the SubSmallInt5 row" + index + ": ",
                        expLine.get("SubSmallInt5"), "" +
                        actualDataRows.getValue(index, "SubSmallInt5"));
                index++;
            }
        } catch (NullPointerException e) {
        	e.printStackTrace();
        	System.out.println(index);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
