package ncr.res.mobilepos.classinfo.resource.test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
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

/**
 * Steps class for deleteClass. It contains steps of a given scenario.
 */
public class DeleteClassResourceSteps extends Steps {
	private DBInitiator dbInitiator;
    /**
     * ClassInfoResource instance.
     */
    private ClassInfoResource classInfoResource = null;
    /**
     * ResultBase instance. Holds the actual resultcode.
     */
    private ResultBase resultBase = null;

    /**
     * Executed before start of the scenario.
     *
     * @throws Exception
     *             thrown when set-up fail.
     */
    @BeforeScenario
    public final void setUpClass() throws Exception {
        Requirements.SetUp();
        dbInitiator = new DBInitiator("DeleteClassResourceSteps", DATABASE.RESMaster);
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/classinfo/resource/test/classinfo_for_delete.xml"); 
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
     * Instantiate ClassInfoResource class.
     */
    @Given("a ClassInfo resource")
    public final void givenClassResource() {
        classInfoResource = new ClassInfoResource();
    }

    /**
     * A method when deleting an class info of given line, storeID, department, and class.
     *
     * @param line
     *            The line of the class.
     * @param retailstoreid
     *            The retailstoreid
     * @param department
     *            The department.
     * @param itemClass
     *            The class id.
     */
    @When("I delete class{$itemClass} and line {$line} of store {$retailstoreid} in department {$department}")
    public final void deleteClass(final String itemClass, final String line, final String retailstoreid, final String department) {
        resultBase = classInfoResource.deleteClass(retailstoreid, department, line, itemClass);
    }

    /**
     * Tests the resulcode of actual and expected.
     *
     * @param resultCode
     *            expected resultcode.
     */
    @Then("I should get resultCode equal to $resultCode")
    public final void shouldGetResultCode(final int resultCode) {
        Assert.assertEquals(resultCode, resultBase.getNCRWSSResultCode());
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
            	Object objDiscountFlag = actualDataRows.getValue(index, "DiscountFlag"); 
            	Assert.assertEquals(
                        "Compare the DiscountFlag row" + index + ": ",
                        expItem.get("DiscountFlag"),
                        objDiscountFlag == null ? "-1" : objDiscountFlag.toString());  
            	
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
