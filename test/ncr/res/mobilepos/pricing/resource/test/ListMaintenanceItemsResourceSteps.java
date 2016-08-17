package ncr.res.mobilepos.pricing.resource.test;

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
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.SearchedProducts;
import ncr.res.mobilepos.pricing.resource.ItemResource;

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

public class ListMaintenanceItemsResourceSteps extends Steps {
    private DBInitiator dbInitiator;
    private ItemResource itemResource;
    private SearchedProducts actualSearchedProducts;
    private ServletContext context;

    @BeforeScenario
    public final void setUpClass() throws Exception {
        Requirements.SetUp();
        dbInitiator = new DBInitiator("CreateItemResourceSteps",
                DATABASE.RESMaster);
        GlobalConstant.setMaxSearchResults(5);
    }

    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }


    @Given("an item resource")
    public final void anItemResource() throws SecurityException,
            NoSuchFieldException, IllegalArgumentException,
            IllegalAccessException {
        itemResource = new ItemResource();
        this.context = Requirements.getMockServletContext();
        Field contextToSet = null;
        contextToSet = itemResource.getClass().getDeclaredField("context");
        contextToSet.setAccessible(true);
        contextToSet.set(itemResource, this.context);
    }

    @Given("a database table $filename for list of $data")
    public final void aDatabaseTableForListOfItems(String filename, String data) {
        try {
            dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    "test/ncr/res/mobilepos/pricing/resource/test/" + filename
                            + ".xml");
        } catch (Exception e) {
            Assert.fail("Cant set the database for item.");
        }
    }

    @When("I get the list of Items with storeid {$storeid} and key {$key} and deviceid {$deviceid} and limit {$limit} and name {$name}")
    public final void getListItems(final String storeid, final String key,
            final String deviceid, final int limit, final String name) {
        String keyTemp = key.equalsIgnoreCase("null") ? null : key;
        String nameTemp = name.equalsIgnoreCase("null") ? null : name;
        actualSearchedProducts = itemResource.list(storeid, keyTemp, deviceid, limit, nameTemp);
    }

    @When("the Global Search Limit is set to {$limit}")
    public final void theSearchLimitIsSetTo(final int limit) {
        GlobalConstant.setMaxSearchResults(limit);
    }

    /**
     * Then Step: Compare the existing items in the database.
     * 
     * @param expecteditems
     *            The expected items in the database.
     * @throws DataSetException
     *             The Exception thrown when test fail.
     */
    @Then("the following $data in the database should be: $expecteditems")
    public final void theFollowingItemsInTheDatabaseShouldBe(final String data,
            final ExamplesTable expecteditems) throws DataSetException {

        List<Map<String, String>> expItemRows = expecteditems.getRows();
        ITable actualDataRows = null;

        if (data.equals("items")) {
            actualDataRows = dbInitiator.getTableSnapshot("MST_PLU");

            Assert.assertEquals("Compare that the number "
                    + "of rows in Items are exact: ",
                    expecteditems.getRowCount(), actualDataRows.getRowCount());
            int index = 0;
            int i = 0;
            try {
                for (Map<String, String> expItem : expItemRows) {
                    String discount = expItem.get("discount");
                    String discountamount = expItem.get("discountamount");
                    i = index;
                    Assert.assertEquals("Compare the Store ID row" + index
                            + ": ", expItem.get("storeid"), actualDataRows
                            .getValue(index, "STOREID").toString().trim());
                    Assert.assertEquals("Compare the PLU row" + index + ": ",
                            expItem.get("plu"),
                            actualDataRows.getValue(index, "PLU"));
                    Assert.assertEquals("Compare the DISPLAYPLU row" + index
                            + ": ", expItem.get("displayplu"),
                            actualDataRows.getValue(index, "DISPLAYPLU"));
                    Assert.assertEquals("Compare the Department row" + index
                            + ": ", expItem.get("dpt"),
                            actualDataRows.getValue(index, "DPT"));
                    Assert.assertEquals("Compare the MDINTERNAL row" + index
                            + ": ", expItem.get("mdinternal"),
                            actualDataRows.getValue(index, "MDINTERNAL"));
                    Assert.assertEquals(
                            "Compare the MDNAME row" + index + ": ", String
                                    .valueOf(expItem.get("mdname")), String
                                    .valueOf(actualDataRows.getValue(index,
                                            "MDNAME")));
                    Assert.assertEquals("Compare the MDNAMELOCAL row" + index
                            + ": ", String.valueOf(expItem.get("mdnamelocal")),
                            String.valueOf(actualDataRows.getValue(index,
                                    "MDNAMELOCAL")));

                    if (discount.equals("null")) {
                        Assert.assertNull("Assume that Discount is "
                                + "null for row" + index + ":",
                                actualDataRows.getValue(index, "DISCOUNTRATE"));
                    } else {
                        Assert.assertEquals("Compare the Discount row" + index
                                + ": ", Integer.parseInt(expItem
                                .get("discount")), ((BigDecimal) actualDataRows
                                .getValue(index, "DISCOUNTRATE")).intValue());
                    }

                    if (discountamount.equals("null")) {
                        Assert.assertNull("Assume that Discountamount is "
                                + "null for row" + index + ":",
                                actualDataRows.getValue(index, "DISCOUNTAMT"));
                    } else {
                        Assert.assertEquals("Compare the Discountamount row"
                                + index + ": ", Integer.parseInt(expItem
                                .get("discountamount")),
                                ((BigDecimal) actualDataRows.getValue(index,
                                        "DISCOUNTAMT")).intValue());
                    }  
                  Assert.assertEquals("Compare the Sales Regular Price: ",
                  new BigDecimal(expItem.get("regularprice")), ((BigDecimal)actualDataRows
                          .getValue(index, "SALESPRICE1")));                    
                    
                    Assert.assertEquals("Compare the Class: ",
                            expItem.get("class"),
                            actualDataRows.getValue(index, "CLASS"));
                    index++;
                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                System.out.println(i);
            }
        } else if (data.equals("departments")) {
            actualDataRows = dbInitiator.getTableSnapshot("MST_DPTINFO");
            Assert.assertEquals("Compare that the number "
                    + "of rows in Departments are exact: ",
                    expecteditems.getRowCount(), actualDataRows.getRowCount());

            int index = 0;
            for (Map<String, String> expItem : expItemRows) {
                Assert.assertEquals("Compare the Store ID row" + index + ": ",
                        expItem.get("storeid"),
                        actualDataRows.getValue(index, "STOREID").toString()
                                .trim());
                Assert.assertEquals("Compare the Dpt row" + index + ": ",
                        expItem.get("dpt"),
                        actualDataRows.getValue(index, "DPT"));
                Assert.assertEquals("Compare the DptName row" + index + ": ",
                        String.valueOf(expItem.get("dptname")), String
                                .valueOf(actualDataRows.getValue(index,
                                        "DPTNAME")));
                Assert.assertEquals("Compare the DptNameLocal row" + index
                        + ": ", String.valueOf(expItem.get("dptnamelocal")),
                        String.valueOf(actualDataRows.getValue(index,
                                "DPTNAMELOCAL")));
                index++;
            }
        }

    }

    @Then("the list of Items are: $expectedItems")
    public final void theListOfItemsAre(final ExamplesTable expectedItems) {
        List<Item> actualItems = actualSearchedProducts.getItems();

        Assert.assertEquals("Compare the exact number of Items retrived"
                + " during Item List", expectedItems.getRowCount(),
                actualItems.size());

        int i = 0;
        for (Map<String, String> expecedItem : expectedItems.getRows()) {
            assertThat("Compare the Items PluCode at row " + i, actualItems
                    .get(i).getItemId(), is(equalTo(expecedItem.get("ItemID"))));
            if (expecedItem.get("DescriptionEN").equals("null")) {
                Assert.assertNull("Assume that DescriptionEN is "
                        + "null for row :",
                        actualItems.get(i).getDescription().getEn());
            } else {
                assertThat("Compare the Items DescriptionEN at row " + i, 
                        actualItems.get(i).getDescription().getEn(),
                    is(equalTo(expecedItem.get("DescriptionEN"))));
            }     
            
            if (expecedItem.get("DescriptionJP").equals("null")) {
                Assert.assertNull("Assume that DescriptionJP is "
                        + "null for row :",
                        actualItems.get(i).getDescription().getJa());
            } else {
                assertThat("Compare the Items DescriptionJP at row " + i, 
                        actualItems.get(i).getDescription().getJa(),
                    is(equalTo(expecedItem.get("DescriptionJP"))));
            }         
            assertThat("Compare the Items Sales Regular Price at row " + i,
                    actualItems.get(i).getRegularSalesUnitPrice(),
                    is(equalTo(Double.parseDouble(expecedItem
                            .get("RegularSalesUnitPrice")))));
            assertThat("Compare the Items Department at row " + i, actualItems
                    .get(i).getDepartment(),
                    is(equalTo(expecedItem.get("Department"))));
            assertThat("Compare the Items Class at row " + i, actualItems
                    .get(i).getItemClass(),
                    is(equalTo(expecedItem.get("Class"))));
            assertThat("Compare the Items Line", actualItems.get(i).getLine(),
                    is(equalTo(expecedItem.get("Line"))));
            assertThat("Compare the Items Discount at row " + i, ""
                    + actualItems.get(i).getDiscount(),
                    is(equalTo(expecedItem.get("Discount"))));
            assertThat("Compare the Items Discount Amount at row " + i,
                    actualItems.get(i).getDiscountAmount(),
                    is(equalTo(Double.parseDouble(expecedItem
                            .get("DiscountAmount")))));
            assertThat("Compare the Items Actual Sales Price at row " + i,
                    actualItems.get(i).getActualSalesUnitPrice(),
                    is(equalTo(Double.parseDouble(expecedItem
                            .get("ActualSalesPrice")))));
            i++;
        }

    }
}
