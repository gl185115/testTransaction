/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * CreateLineResourceSteps
 *
 * The Test Steps for Creating Line Resource.
 *
 *	Sul Romares
 */
package ncr.res.mobilepos.classinfo.resource.test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import ncr.res.mobilepos.classinfo.resource.ClassInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
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
 * The Test Steps for Creating Line Resource.
 *
 * @author sr185217
 */
public class CreateClassInfoResourceSteps extends Steps {
    /**
     * ClassInfo instance.
     */
    private ClassInfoResource classInfoResource = null;
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
        dbInitiator = new DBInitiator("CreateLineResourceSteps", DATABASE.RESMaster);
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/classinfo/resource/test/"
                + "classinfo_empty.xml");        
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/classinfo/resource/test/"
                + "MST_STOREINFO_LIST.xml"); 
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/classinfo/resource/test/"
                + "MST_DPTINFO_LIST.xml");      
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/classinfo/resource/test/"
                + "MST_LINEINFO_LIST.xml"); 
    }

    /**
     * Executed after end of the scenario.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    } 
    
    /**
     * Given Step: Set the table definition for ClassInfo(s) to empty.
     */
    @Given("an empty table for classinfo")
    public final void anEmptyTableForClassInfo() {
        try {
            dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    "test/ncr/res/mobilepos/classinfo/resource/test/"
                    + "classinfo_empty.xml");
            dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
			        "test/ncr/res/mobilepos/department/resource/test/"
			        + "prm_system_config_with_tax_rate.xml");	
        } catch (Exception e) {
            Assert.fail("Cant set the database for line.");
        }
    }
    
    /**
     * Given Step: Set the table definition for ClassInfo(s) to empty.
     */
    @Given("a classinfo table")
    public final void anTableForClassInfo() {
        try {
            dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    "test/ncr/res/mobilepos/classinfo/resource/test/"
                    + "classinfo_create.xml");
        } catch (Exception e) {
            Assert.fail("Cant set the database for classinfo.");
        }
    }

    /**
     * Given Step: Set an empty resource.
     */
    @Given("a classinfo resource")
    public final void aLineResource() {
        classInfoResource = new ClassInfoResource();
        classInfoResource.setContext(Requirements.getMockServletContext());
    }

    /**
     * When Step: Add a classinfo.
     * @param newclassinfo The new line.
     */
    @When("the following classinfo is added: $newclassinfo")
    public final void theFollowingLineIsAdded(final ExamplesTable newclassinfo) {
        Map<String, String> classinfo = newclassinfo.getRow(0);        
        String classinfojson = classinfo.get("classinfojson");        
        if (classinfojson.equals("null")) {
            classinfojson = null;
        }
        actualresultBase =
            classInfoResource.createClassInfo(classinfojson);
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
     * Then Step: Compare the existing classes in the database.
     * @param expecteditems The expected classes in the database.
     * @throws DataSetException The Exception thrown when test fail.
     */
    @Then("the following classes in the database should be: $expectedClassInfos")
    public final void theFollowingClassesInTheDatabaseShouldBe(final ExamplesTable expectedClassInfos) throws DataSetException {

        List<Map<String, String>> expClassInfoRows = expectedClassInfos.getRows();
        ITable actualDataRows = null;       
        actualDataRows = dbInitiator.getTableSnapshot("MST_CLASSINFO");

        Assert.assertEquals("Compare that the number "
                + "of rows in ClassInfo are exact: ",
                expectedClassInfos.getRowCount(), actualDataRows.getRowCount());
        
        int index = 0;        
        try {
            for (Map<String, String> expItem : expClassInfoRows) {            	 
            	Assert.assertEquals(
            			"Compare the StoreId row" + index + ": ",
                        expItem.get("RetailStoreID"),
                        actualDataRows.getValue(index, "StoreId")); 
            	Assert.assertEquals
            			("Compare the Class row" + index + ": ",
                        expItem.get("Class"),
                        actualDataRows.getValue(index, "Class")); 
            	Assert.assertEquals(
            			"Compare the DescriptionEN row" + index + ": ", 
                		String.valueOf(expItem.get("DescriptionEN")),                		
                		String.valueOf(actualDataRows.getValue(index, "ClassName")));            	
            	Assert.assertEquals(
            			"Compare the DescriptionJP row" + index + ": ", 
                		String.valueOf(expItem.get("DescriptionJP")), 
                		String.valueOf(actualDataRows.getValue(index, "ClassNameLocal")));
            	Assert.assertEquals(
                        "Compare the Department row" + index + ": ",
                        expItem.get("Department"),
                        actualDataRows.getValue(index, "DPT"));
            	Assert.assertEquals(
                        "Compare the Line row" + index + ": ",
                        expItem.get("Line"),
                        actualDataRows.getValue(index, "Line"));
            	Assert.assertEquals(
                        "Compare the TaxType row" + index + ": ",
                        expItem.get("TaxType"), "" +
                        actualDataRows.getValue(index, "TaxType"));
            	Assert.assertEquals(
                        "Compare the TaxRate row" + index + ": ",
                        expItem.get("TaxRate"), "" +
                        actualDataRows.getValue(index, "TaxRate"));
            	Assert.assertEquals(
                        "Compare the DiscountType row" + index + ": ",
                        expItem.get("DiscountType"), "" +
                        actualDataRows.getValue(index, "DiscountType"));
            	Assert.assertEquals(
                        "Compare the ExceptionFlag row" + index + ": ",
                        expItem.get("ExceptionFlag"), "" +
                        actualDataRows.getValue(index, "ExceptionFlag")); 
            	Assert.assertEquals(
                        "Compare the DiscountFlag row" + index + ": ",
                        expItem.get("DiscountFlag"), "" + actualDataRows.getValue(index, "DiscountFlag")); 
            	
            	String strDiscountamount = expItem.get("DiscountAmount");
            	if (strDiscountamount.equals("null")) {
                    Assert.assertNull("Assume that DiscountAmount is "
                            + "null for row" + index + ":",
                            actualDataRows.getValue(index, "DiscountAmt"));
                } else {
                    Assert.assertEquals("Compare the DiscountAmount row" + index
                            + ": ", "" + (Double.parseDouble(strDiscountamount)),
                             "" + ((BigDecimal) actualDataRows.getValue(index,"DiscountAmt")).doubleValue());
                }
            	String strDiscountRate= expItem.get("DiscountRate");
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
                        expItem.get("AgeRestrictedFlag"), "" +
                        actualDataRows.getValue(index, "AgeRestrictedFlag"));
            	Assert.assertEquals(
                        "Compare the InheritFlag row" + index + ": ",
                        expItem.get("InheritFlag"), "" +
                        actualDataRows.getValue(index, "InheritFlag"));
            	Assert.assertEquals(
                        "Compare the SubSmallInt5 row" + index + ": ",
                        expItem.get("SubSmallInt5"), "" +
                        actualDataRows.getValue(index, "SubSmallInt5"));
                index++;
            }
        } catch (NullPointerException e) {
        	e.printStackTrace();
        	System.out.println(index);
        }
    }
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
    
}
