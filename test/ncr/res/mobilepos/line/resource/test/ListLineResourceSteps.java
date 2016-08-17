package ncr.res.mobilepos.line.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.line.model.Line;
import ncr.res.mobilepos.line.model.SearchedLine;
import ncr.res.mobilepos.line.resource.LineResource;

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

public class ListLineResourceSteps extends Steps {
	private DBInitiator dbInitiator;
	private LineResource lineResource;
	private SearchedLine actualSearchedLine;
	private ServletContext context;

	/**
     * Executed before start of the scenario.
     *
     * @throws Exception
     *             thrown when set-up fail.
     */
    @BeforeScenario
    public final void setUpClass() throws Exception {
        Requirements.SetUp();
        dbInitiator = new DBInitiator("ListLineResourceSteps", DATABASE.RESMaster);
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/line/resource/test/line.xml"); 
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
     * Given Step: Set the Line Resource.
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @Given("a Line resource")
    public final void aLineResource() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        lineResource = new LineResource();
        this.context = Requirements.getMockServletContext();
        Field contextToSet = null;
        contextToSet = lineResource.getClass().getDeclaredField("context");
        contextToSet.setAccessible(true);
        contextToSet.set(lineResource, this.context);
    }

    @Given("a line database table $filename")
    public final void aDatabaseTableForListOfLines(String filename) {
    	try {
            dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    "test/ncr/res/mobilepos/line/resource/test/"
                    + filename + ".xml");
        } catch (Exception e) {
            Assert.fail("Cant set the database for line.");
        }
    }    

    @When("I get the list of Lines with storeid {$storeid} and dpt {$dpt} and key {$key} and name {$name} and limit {$limit}")
    public final void getListLines(final String storeid, final String dpt, final String key, final String name, final String limit) {
        String keyTemp = (key == null || key.equalsIgnoreCase("null")) ? null : key;
        String nameTemp = (name == null || name.equalsIgnoreCase("null")) ? null : name;          
        int limitTemp = (limit == null || limit.equalsIgnoreCase("null")) ? 0 : Integer.parseInt(limit); 
        
        actualSearchedLine = lineResource.list(storeid, dpt, keyTemp, nameTemp, limitTemp);       
    }
    
    @When("the Search Limit is set to {$limit}")
    public final void theSearchLimitIsSetTo(final int limit) {
        GlobalConstant.setMaxSearchResults(limit);
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

    @Then("the list of Lines are: $expectedLines")
    public final void theListOfLinesAre(final ExamplesTable expectedLines){
    	List<Line> actualLine =  actualSearchedLine.getLines();

    	Assert.assertEquals("Compare the exact number of Line retrived"
    			+ " during Line List", expectedLines.getRowCount(),
    			actualLine.size());

    	int i = 0;
        for(Map<String, String> expecedItem : expectedLines.getRows()){
        	assertThat("Compare the RetailStoreID at row " + i, actualLine.get(i).getRetailStoreId(),
                    is(equalTo(expecedItem.get("RetailStoreID")))); 
        	assertThat("Compare the Line", actualLine.get(i).getLine(),
                    is(equalTo(expecedItem.get("Line"))));  
            if (expecedItem.get("DescriptionEN").equals("null")) {
                Assert.assertNull("Assume that DescriptionEN is "
                        + "null for row :",
                        actualLine.get(i).getDescription().getEn());
            } else {
                assertThat("Compare the Line DescriptionEN at row " + i, 
                		actualLine.get(i).getDescription().getEn(),
                    is(equalTo(expecedItem.get("DescriptionEN"))));
            }  
            if (expecedItem.get("DescriptionJP").equals("null")) {
                Assert.assertNull("Assume that DescriptionJP is "
                        + "null for row :",
                        actualLine.get(i).getDescription().getJa());
            } else {
                assertThat("Compare the Line DescriptionJP at row " + i, 
                		actualLine.get(i).getDescription().getJa(),
                    is(equalTo(expecedItem.get("DescriptionJP"))));
            }   
            assertThat("Compare the Line Department at row " + i, actualLine.get(i).getDepartment(),
                    is(equalTo(expecedItem.get("Department"))));
            
            assertThat("Compare the Line TaxType", "" + actualLine.get(i).getTaxType(),
                    is(equalTo(expecedItem.get("TaxType"))));
            assertThat("Compare the Line TaxRate", "" + actualLine.get(i).getTaxRate(),
                    is(equalTo(expecedItem.get("TaxRate"))));
            assertThat("Compare the Line DiscountType", "" + actualLine.get(i).getDiscountType(),
                    is(equalTo(expecedItem.get("DiscountType"))));
            assertThat("Compare the Line ExceptionFlag", "" + actualLine.get(i).getExceptionFlag(),
                    is(equalTo(expecedItem.get("ExceptionFlag"))));
            assertThat("Compare the Line DiscountFlag", "" + actualLine.get(i).getDiscountFlag(),
                    is(equalTo(expecedItem.get("DiscountFlag"))));
            assertThat("Compare the Line Discount Amount at row " + i,
            		actualLine.get(i).getDiscountAmount(),
                    is(equalTo(Double.parseDouble(
                            expecedItem.get("DiscountAmount")))));
            assertThat("Compare the Line DiscountRate at row " + i,
            		actualLine.get(i).getDiscountRate(),
                    is(equalTo(Double.parseDouble(
                            expecedItem.get("DiscountRate")))));
            assertThat("Compare the Line AgeRestrictedFlag", "" + actualLine.get(i).getAgeRestrictedFlag(),
                    is(equalTo(expecedItem.get("AgeRestrictedFlag"))));
            assertThat("Compare the Line InheritFlag", "" + actualLine.get(i).getInheritFlag(),
                    is(equalTo(expecedItem.get("InheritFlag"))));
            assertThat("Compare the Line SubSmallInt5", "" + actualLine.get(i).getSubSmallInt5(),
                    is(equalTo(expecedItem.get("SubSmallInt5")))); 
            i++;
        }

    }
}
