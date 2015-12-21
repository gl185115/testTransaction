/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * CreateItemResourceSteps
 *
 * The Test Steps for Creating Item Resource.
 *
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.pricing.resource.test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
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

/**
 * The Test Steps for Creating Item Resource.
 *
 * @author cc185102
 */
public class CreateItemResourceSteps extends Steps {
    /**
     * ItemResource instance.
     */
    private ItemResource itemResource = null;
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
        dbInitiator = new DBInitiator("CreateItemResourceSteps", DATABASE.RESMaster);
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/pricing/resource/test/"
                + "prm_system_config_with_tax_rate_round_down.xml");        
    }

    /**
     * Executed after end of the scenario.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }

    /**
     * Given Step: Set the table definition for Item(s) to empty.
     */
    @Given("an empty table for item")
    public final void anEmptyTableForItem() {
        try {
            dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    "test/ncr/res/mobilepos/pricing/resource/test/"
                    + "MST_PLU_forInsert.xml");
        } catch (Exception e) {
            Assert.fail("Cant set the database for item.");
        }
    }

    /**
     * Given Step: Set an empty resource.
     */
    @Given("an item resource")
    public final void anItemResource() {
        itemResource = new ItemResource();
        itemResource.setContext(Requirements.getMockServletContext());
    }

    /**
     * When Step: Add an item.
     * @param newitem The new item.
     */
    @When("the following item is added: $newitem")
    public final void theFollowingItemIsAdded(final ExamplesTable newitem) {
        Map<String, String> iteminfo = newitem.getRow(0);

        String storeid = iteminfo.get("retailstoreid");
        String itemid = iteminfo.get("itemid");
        String itemjson = iteminfo.get("itemjson");
        
        if (storeid.equals("null")) {
            storeid = null;
        }

        if (itemid.equals("null")) {
            itemid = null;
        }
        
        if (itemjson.equals("null")) {
            itemjson = null;
        }

        actualresultBase =
            itemResource.createItem(storeid,
                    itemid, itemjson);
    }

    /**
     * Then Step: Compare the existing items in the database.
     * @param expecteditems The expected items in the database.
     * @throws DataSetException The Exception thrown when test fail.
     */
    @Then("the following items in the database should be: $expecteditems")
    public final void theFollowingItemsInTheDatabaseShouldBe(
            final ExamplesTable expecteditems) throws DataSetException {
        List<Map<String, String>> expItemRows = expecteditems.getRows();
        ITable actualItemRows = dbInitiator.getTableSnapshot("MST_PLU");

        Assert.assertEquals("Compare that the number "
                + "of rows in Items are exact: ",
                expecteditems.getRowCount(),
                actualItemRows.getRowCount());

        int index = 0;
        for (Map<String, String> expItem : expItemRows) {
            String discount = expItem.get("discount");
            String discountflag = expItem.get("discountflag");
            String couponFlag = expItem.get("couponflag");
            Assert.assertEquals("Compare the Store ID row" + index + ": ",
                    expItem.get("storeid"),
                    actualItemRows.getValue(index, "STOREID").toString().trim());
            Assert.assertEquals("Compare the PLU row" + index + ": ",
                    expItem.get("plu"),
                    actualItemRows.getValue(index, "PLU"));
            Assert.assertEquals("Compare the DISPLAYPLU row" + index + ": ",
                    expItem.get("displayplu"),
                    actualItemRows.getValue(index, "DISPLAYPLU"));
            Assert.assertEquals("Compare the Department row" + index + ": ",
                    expItem.get("dpt"),
                    actualItemRows.getValue(index, "DPT"));
            Assert.assertEquals("Compare the MdName row" + index + ": ",
                    expItem.get("mdname"),
                    actualItemRows.getValue(index, "MdName"));
            Assert.assertEquals("Compare the MdNameLocal row" + index + ": ",
                    expItem.get("mdnamelocal"),
                    actualItemRows.getValue(index, "MdNameLocal"));
            
            if (discount.equals("null")) {
                Assert.assertNull("Assume that Discount is "
                        + "null for row" + index + ":",
                        actualItemRows.getValue(index, "DISCOUNTRATE"));
            } else {
                Assert.assertEquals("Compare the Discount row" + index + ": ",
                        Integer.parseInt(expItem.get("discount")),
                        ((BigDecimal) actualItemRows
                                .getValue(index, "DISCOUNTRATE")).intValue());
            }

            if (discountflag.equals("null")) {
                Assert.assertNull("Assume that DiscountFlag is "
                        + "null for row" + index + ":",
                        actualItemRows.getValue(index, "DISCOUNTFLAG"));
            } else {
                Assert.assertEquals("Compare the Discount row" + index + ": ",
                        Integer.parseInt(expItem.get("discountflag")),
                        ((BigDecimal) actualItemRows
                                .getValue(index, "DISCOUNTFLAG")).intValue());
            }

            Assert.assertEquals("Compare the Sales Actual Price: ",
                    BigDecimal.valueOf(
                            Integer.parseInt(expItem.get("actualprice"))),
                    actualItemRows.getValue(index, "SALESPRICE1"));
            Assert.assertEquals("Compare the Class: ",
                    expItem.get("class"),
                    actualItemRows.getValue(index, "CLASS"));
            Assert.assertEquals("Compare the Line: ",
                    expItem.get("line"),
                    actualItemRows.getValue(index, "LINE"));
            Assert.assertEquals("Compare the Discount Type: ",
                    Integer.parseInt(expItem.get("discounttype")),
                    ((BigDecimal) actualItemRows
                            .getValue(index, "DISCOUNTTYPE")).intValue());
            Assert.assertEquals("Compare the Tax Type: ",
                    Integer.parseInt(expItem.get("taxtype")),
                    ((BigDecimal) actualItemRows
                            .getValue(index, "TAXTYPE")).intValue());
            Assert.assertEquals("Compare the Tax Rate: ",
                    Integer.parseInt(expItem.get("taxrate")),
                    ((BigDecimal) actualItemRows
                            .getValue(index, "TAXRATE")).intValue());
            String ageRestricted =expItem.get("agerestricted"); 
            if(ageRestricted.equals("null")){
                Assert.assertNull("Assume that AgeRestricted is "
                        + "null for row" + index + ":",
                        actualItemRows.getValue(index, "AGERESTRICTEDFLAG"));
            }else{
            Assert.assertEquals("Compare the ageRestricted:",
                    Integer.parseInt(expItem.get("agerestricted")),
                    ((BigDecimal) actualItemRows
                            .getValue(index, "AGERESTRICTEDFLAG")).intValue());
            }
            
            if (couponFlag == null || couponFlag.equals("null")) {
                Assert.assertNull("Assume that Coupon Flag is "
                        + "null for row" + index + ":",
                        actualItemRows.getValue(index, "MD32"));
            } else {
                System.out.println("CouponFlag: "+couponFlag);
                Assert.assertEquals("Compare the Coupon Flag row" + index + ": ",
                    Integer.parseInt(couponFlag),
                    ((BigDecimal) actualItemRows
                            .getValue(index, "MD32")).intValue());
            }
            Assert.assertEquals("Compare the InheritFlag: ",
                    expItem.get("inheritflag"),
                    actualItemRows.getValue(index, "INHERITFLAG")+"");
            Assert.assertEquals("Compare the NonSales: ",
                    expItem.get("nonsales"),
                    actualItemRows.getValue(index, "POSMDTYPE")+"");
            Assert.assertEquals("Compare the SubInt10: ",
                    expItem.get("subint10"),
                    actualItemRows.getValue(index, "SUBINT10")+"");
            index++;
        }
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
}
