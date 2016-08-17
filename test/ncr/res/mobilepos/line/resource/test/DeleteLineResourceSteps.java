package ncr.res.mobilepos.line.resource.test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
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

/**
 * Steps class for deleteLine. It contains steps of a given scenario.
 */
public class DeleteLineResourceSteps extends Steps {
	private DBInitiator dbInitiator;
    /**
     * LineResource instance.
     */
    private LineResource lineResource = null;
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
        dbInitiator = new DBInitiator("DeleteLineResourceSteps", DATABASE.RESMaster);
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/line/resource/test/line_for_delete.xml"); 
        
        //initialize store
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/line/resource/test/"
                + "MST_STOREINFO_LIST.xml");
        
        //initialize department
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/line/resource/test/"
                + "MST_DPTINFO_LIST.xml");  
    }
    /**
     * Executed after end of the scenario.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }

    /**
     * Instantiate LineResource class.
     */
    @Given("a Line resource")
    public final void givenLineResource() {
        lineResource = new LineResource();
    }

    /**
     * A method when deleting an line of given line, storeID, and department.
     *
     * @param line
     *            The line id.
     * @param retailstoreid
     *            The retailstoreid
     * @param department
     *            The department.
     */
    @When("I delete line {$line} of store {$retailstoreid} in department {$department}")
    public final void deleteLine(final String line, final String retailstoreid, final String department) {
        resultBase = lineResource.deleteLine(retailstoreid, department, line);
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
            for (Map<String, String> expItem : expLineRows) {
            	Assert.assertEquals(
            			"Compare the StoreId row" + index + ": ",
                        expItem.get("RetailStoreID"),
                        actualDataRows.getValue(index, "StoreId")); 
            	Assert.assertEquals(
            			"Compare the Line row" + index + ": ",
                        expItem.get("Line"),
                        actualDataRows.getValue(index, "Line"));
            	Assert.assertEquals(
            			"Compare the DescriptionEN row" + index + ": ", 
                		String.valueOf(expItem.get("DescriptionEN")),                		
                		String.valueOf(actualDataRows.getValue(index, "LineName")));            	
            	Assert.assertEquals(
            			"Compare the DescriptionJP row" + index + ": ", 
                		String.valueOf(expItem.get("DescriptionJP")), 
                		String.valueOf(actualDataRows.getValue(index, "LineNameLocal")));
            	Assert.assertEquals(
                        "Compare the Department row" + index + ": ",
                        expItem.get("Department"),
                        actualDataRows.getValue(index, "DPT"));            	
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
                        expItem.get("DiscountFlag"), "" +
                        actualDataRows.getValue(index, "DiscountFlag")); 
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
