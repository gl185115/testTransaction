/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * UpdateItemResourceSteps
 *
 * The Test Steps for Creating Item Resource.
 *
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.pricing.resource.test;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.pricing.model.ItemMaintenance;
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
 * Test Class Steps for updating an item.
 *
 */
public class UpdateItemResourceSteps extends Steps {
    /**
     * The DBInitiator for the database.
     */
    private DBInitiator dbInitiator;
    /**
     * The Item Resource.
     */
    private ItemResource itemResource;
    /**
     * The actual Item Maintence to be compared.
     */
    private ItemMaintenance actualItemMaintenance;
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
        dbInitiator = new DBInitiator("UpdateItemResourceSteps", DATABASE.RESMaster);
        dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/pricing/resource/test/"
                + "prm_system_config_with_tax_rate_round_down.xml");
        this.context = Requirements.getMockServletContext();
    }

    /**
     * Executed after end of the scenario.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }

    /**
     * Given Step: Set the table definition for Item.
     */
    @Given("a table for item")
    public final void aTableForItem() {
        try {
            dbInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    "test/ncr/res/mobilepos/pricing/resource/test/"
                    + "MST_PLU_forUpdate.xml");
        } catch (Exception e) {
        	e.printStackTrace();
            Assert.fail("Cant set the database for item.");
        }
    }

    /**
     * Given Step: Set the Item Resource.
     */
    @Given("an item resource")
    public final void anItemResource() {
        itemResource = new ItemResource();
        this.itemResource.setContext(context);
    }

    /**
     * When Step: Update the item.
     * @param iteminfo The new updates for the item.
     */
    @When("the following item is updated: $iteminfo")
    public final void theFollowingItemIsUpdated(final ExamplesTable iteminfo) {
        Map<String, String> newitemUpdate = iteminfo.getRow(0);

        String storeid = newitemUpdate.get("retailstoreid");
        String itemid = newitemUpdate.get("itemid");
        String jsonitem = newitemUpdate.get("itemjson");
        if (storeid.equals("null")) {
            storeid = null;
        }

        if (itemid.equals("null")) {
            itemid = null;
        }

        actualItemMaintenance =
            itemResource.updateItem(storeid, itemid, jsonitem);
    }

    /**
     * Then Step: Compare the existing items in the database.
     * @param expecteditems The expected items in the database.
     * @throws Exception 
     * @throws DataSetException The Exception thrown when test fail.
     */
    @Then("the following items in the database should be: $expecteditems")
    public final void theFollowingItemsInTheDatabaseShouldBe(
            final ExamplesTable expecteditems) throws Exception {
        try {
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
                String taxtype = expItem.get("taxtype");
                String ageRestricted = expItem.get("agerestricted");
                String couponFlag = expItem.get("couponflag");
				String discountAmt = expItem.get("discountamount");	
    
                Assert.assertEquals("Compare the Store ID row" + index + ": ",
                        expItem.get("storeid"),
                        ((String)actualItemRows.getValue(index, "STOREID")).trim());
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
                        actualItemRows.getValue(index, "MDNAME"));
                Assert.assertEquals("Compare the MdNameLocal row" + index + ": ",
                        expItem.get("mdnamelocal"),
                        actualItemRows.getValue(index, "MDNAMELOCAL"));
                
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
                if (discountAmt == null || discountAmt.equals("null")) {
                    Assert.assertNull("Assume that DiscountAmt is "
                            + "null for row" + index + ":",
                            actualItemRows.getValue(index, "DISCOUNTAMT"));
                } else {
                    Assert.assertEquals("Compare the DiscountAmt row" + index + ": ",
                            Integer.parseInt(expItem.get("discountamount")),
                            ((BigDecimal) actualItemRows
                                    .getValue(index, "DISCOUNTAMT")).intValue());
                }                
                
    
                if (discountflag == null || discountflag.equals("null")) {
                    Assert.assertNull("Assume that DiscountFlag is "
                            + "null for row" + index + ":",
                            actualItemRows.getValue(index, "DISCOUNTFLAG"));
                } else {
                    Assert.assertEquals("Compare the DiscountFlag row" + index + ": ",
                            Integer.parseInt(expItem.get("discountflag")),
                            ((BigDecimal) actualItemRows
                                    .getValue(index, "DISCOUNTFLAG")).intValue());
                }
                if (taxtype.equals("null")) {
                    Assert.assertNull("Assume that TaxType is "
                            + "null for row" + index + ":",
                            actualItemRows.getValue(index, "TAXTYPE"));
                } else {
                    Assert.assertEquals("Compare the TaxType row" + index + ": ",
                            Integer.parseInt(expItem.get("taxtype")),
                            ((BigDecimal) actualItemRows
                                    .getValue(index, "TAXTYPE")).intValue());
                }
                Assert.assertEquals("Compare the Sales Actual Price: ",
                        BigDecimal.valueOf(
                                Integer.parseInt(expItem.get("actualprice"))),
                        actualItemRows.getValue(index, "SALESPRICE1"));
                Assert.assertEquals("Compare the Class: ",
                        expItem.get("class"),
                        actualItemRows.getValue(index, "CLASS"));
                
                if (ageRestricted.equals("null")) {
                    Assert.assertNull("Assume that Age Restricted is "
                            + "null for row" + index + ":",
                            actualItemRows.getValue(index, "AGERESTRICTEDFLAG"));
                } else {
                    Assert.assertEquals("Compare the Age Restricted row" + index + ": ",
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
                index++;
            }
        }catch(Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * Then Step: Compare the result xml of the JSON Result.
     * @param expectedXmlResult The Xml Representation
     * @throws JAXBException    The exception thrown when comparing failed.
     */
    @Then("the JSON result should have the following xml: $expectedXmlResult")
    public final void theJSONResultShouldHaveTheFollowingXml(
            final String expectedXmlResult) throws JAXBException {
        XmlSerializer<ItemMaintenance> itemMaintenanceSerializer =
            new XmlSerializer<ItemMaintenance>();
        String actualXmlResult =
            itemMaintenanceSerializer.marshallObj(ItemMaintenance.class,
                    actualItemMaintenance,
                    "UTF-8");
        System.out.println(actualXmlResult);
        Assert.assertEquals("Compare the xml of the actual result object",
                expectedXmlResult, actualXmlResult);
    }

    /**
     * Then Step: Compare the result code.
     * @param expectedresult The expected result.
     */
    @Then("the result code should be {$expectedresult}")
    public final void theResultCodeShouldBe(final int expectedresult) {
        Assert.assertEquals("Compare the actual result",
                expectedresult, actualItemMaintenance.getNCRWSSResultCode());
    }
}
