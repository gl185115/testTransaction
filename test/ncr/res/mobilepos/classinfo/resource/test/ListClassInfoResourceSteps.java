package ncr.res.mobilepos.classinfo.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.classinfo.model.ClassInfo;
import ncr.res.mobilepos.classinfo.model.SearchedClassInfo;
import ncr.res.mobilepos.classinfo.resource.ClassInfoResource;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

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

public class ListClassInfoResourceSteps extends Steps {
	private DBInitiator dbInitiator;
	private ClassInfoResource classInfoResource;
	private SearchedClassInfo actualSearchedClassInfo;
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
        dbInitiator = new DBInitiator("ListClassInfoResourceSteps", DATABASE.RESMaster);
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/classinfo/resource/test/classinfo.xml"); 
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
     * Given Step: Set the ClassInfo Resource.
     * @throws NoSuchFieldException 
     * @throws SecurityException 
     * @throws IllegalAccessException 
     * @throws IllegalArgumentException 
     */
    @Given("a ClassInfo resource")
    public final void aClassInfoResource() throws SecurityException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        classInfoResource = new ClassInfoResource();
        this.context = Requirements.getMockServletContext();
        Field contextToSet = null;
        contextToSet = classInfoResource.getClass().getDeclaredField("context");
        contextToSet.setAccessible(true);
        contextToSet.set(classInfoResource, this.context);
    }

    @Given("a class database table $filename")
    public final void aDatabaseTableForListOfClassInfos(String filename) {
    	try {
            dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    "test/ncr/res/mobilepos/classinfo/resource/test/"
                    + filename + ".xml");
        } catch (Exception e) {
            Assert.fail("Cant set the database for item.");
        }
    }

    @When("I get the list of Classes with storeid {$storeid} and dpt {$dpt} and key {$key} and name {$name} and limit {$limit}")
    public final void getListClasses(final String storeid, final String dpt, final String key, final String name, final String limit) {
        String keyTemp = key.equalsIgnoreCase("null") ? null : key;
        String nameTemp = name.equalsIgnoreCase("null") ? null : name;      
        int limitTemp = (limit == null || limit.equals("null"))?0: Integer.parseInt(limit);
        
        actualSearchedClassInfo = classInfoResource.list(storeid, dpt, keyTemp, nameTemp, limitTemp);
    }
    
    @When("the Search Limit is set to {$limit}")
    public final void theSearchLimitIsSetTo(final int limit) {
        GlobalConstant.setMaxSearchResults(limit);
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

    @Then("the list of Classes are: $expectedClassInfo")
    public final void theListOfClassInfosAre(final ExamplesTable expectedClassInfo){
    	List<ClassInfo> actualClassInfo =  actualSearchedClassInfo.getClassInfos();

    	Assert.assertEquals("Compare the exact number of ClassInfo retrived"
    			+ " during ClassInfo List", expectedClassInfo.getRowCount(),
    			actualClassInfo.size());

    	int i = 0;
        for(Map<String, String> expecedItem : expectedClassInfo.getRows()){
        	assertThat("Compare the RetailStoreID at row " + i, actualClassInfo.get(i).getRetailStoreId(),
                    is(equalTo(expecedItem.get("RetailStoreID"))));          	
        	assertThat("Compare the Class at row " + i, actualClassInfo.get(i).getItemClass(),
                    is(equalTo(expecedItem.get("Class"))));            
            if (expecedItem.get("DescriptionEN").equals("null")) {
                Assert.assertNull("Assume that DescriptionEN is "
                        + "null for row :",
                        actualClassInfo.get(i).getDescription().getEn());
            } else {
                assertThat("Compare the Class DescriptionEN at row " + i, 
                		actualClassInfo.get(i).getDescription().getEn(),
                    is(equalTo(expecedItem.get("DescriptionEN"))));
            }  
            if (expecedItem.get("DescriptionJP").equals("null")) {
                Assert.assertNull("Assume that DescriptionJP is "
                        + "null for row :",
                        actualClassInfo.get(i).getDescription().getJa());
            } else {
                assertThat("Compare the Class DescriptionJP at row " + i, 
                		actualClassInfo.get(i).getDescription().getJa(),
                    is(equalTo(expecedItem.get("DescriptionJP"))));
            }   
            assertThat("Compare the Classes Department at row " + i, actualClassInfo.get(i).getDepartment(),
                    is(equalTo(expecedItem.get("Department"))));
            assertThat("Compare the Classes Line", actualClassInfo.get(i).getLine(),
                    is(equalTo(expecedItem.get("Line"))));
            assertThat("Compare the Classes TaxType", "" + actualClassInfo.get(i).getTaxType(),
                    is(equalTo(expecedItem.get("TaxType"))));
            assertThat("Compare the Classes TaxRate", "" + actualClassInfo.get(i).getTaxRate(),
                    is(equalTo(expecedItem.get("TaxRate"))));
            assertThat("Compare the Classes DiscountType", "" + actualClassInfo.get(i).getDiscountType(),
                    is(equalTo(expecedItem.get("DiscountType"))));
            assertThat("Compare the Classes ExceptionFlag", "" + actualClassInfo.get(i).getExceptionFlag(),
                    is(equalTo(expecedItem.get("ExceptionFlag"))));
            assertThat("Compare the Classes DiscountFlag", "" + actualClassInfo.get(i).getDiscountFlag(),
                    is(equalTo(expecedItem.get("DiscountFlag"))));
            assertThat("Compare the Classes Discount Amount at row " + i,
            		actualClassInfo.get(i).getDiscountAmount(),
                    is(equalTo(Double.parseDouble(
                            expecedItem.get("DiscountAmount")))));
            assertThat("Compare the Classes DiscountRate at row " + i,
            		actualClassInfo.get(i).getDiscountRate(),
                    is(equalTo(Double.parseDouble(
                            expecedItem.get("DiscountRate")))));
            assertThat("Compare the Classes AgeRestrictedFlag", "" + actualClassInfo.get(i).getAgeRestrictedFlag(),
                    is(equalTo(expecedItem.get("AgeRestrictedFlag"))));
            assertThat("Compare the Classes InheritFlag", "" + actualClassInfo.get(i).getInheritFlag(),
                    is(equalTo(expecedItem.get("InheritFlag"))));
            assertThat("Compare the Classes SubSmallInt5", "" + actualClassInfo.get(i).getSubSmallInt5(),
                    is(equalTo(expecedItem.get("SubSmallInt5")))); 
            i++;
        }

    }
}
