/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* UpdateStoreResourceTest
*
* The Test Step Class for listing the Stores.
*
*/
package ncr.res.mobilepos.tillinfo.resource.test;

import java.text.DateFormat;
import java.util.Map;

import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.tillinfo.resource.TillInfoResource;
import ncr.res.mobilepos.tillinfo.model.Till;
import ncr.res.mobilepos.tillinfo.model.ViewTill;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

/**
 * The Test Step Class for Updating a Store.
 * @author es185134
 *
 */
public class UpdateTillInfoResourceSteps extends Steps {
	
	private DBInitiator dbInitiator;
    /**
     * The Till Resource.
     */
    private TillInfoResource tillResource;
    /**
     * The expected Till result from update.
     */
    private ViewTill viewTill;

    /**
     * The Setting up Requirements.
     */
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }

    /**
     * The teardown.
     */
    @AfterScenario
    public final void tearDown() {
        Requirements.TearDown();
    }

    /**
     * Set up the Till Info Resource.
     */
    @Given("I have a Till Info Resource")
    public final void iHaveATillInfoResource() {
    	tillResource = new TillInfoResource();     
    }
    /**
     * Given Step: Set the Store Info.
     */
    @Given("a Till Table")
    public final void aTillTable() {
    	dbInitiator = new DBInitiator("TillInfoResource",
                "test/ncr/res/mobilepos/tillinfo/resource/"
                    + "test/MST_TILLIDINFO.xml", DATABASE.RESMaster);
        }

    /**
     * When Step: Update a till.
     * @param tillinfo The till Information for update.
     */
    @When("I update a till with the following details: $tillinfo")
    public final void iUpdateATillWithTheFollowingDetails(
            final ExamplesTable tillinfo) {
        Map<String, String> tillinfovalue =
        		tillinfo.getRow(0);
        
        if (tillinfovalue.get("StoreId").equals("null")) {
      	  tillinfovalue.put("StoreId", null);
        }
        if (tillinfovalue.get("TillId").equals("null")) {
        	tillinfovalue.put("TillId", null);
        }
        viewTill = tillResource.updateTill(tillinfovalue.get("StoreId"),
            	tillinfovalue.get("TillId"),	
                tillinfovalue.get("tilljson"));
    }
    
    /**
     * Then Step: Compare the actual till updated
     * and expected to the till update.
     * @param tillinfo The expected Till information.
     */
    @Then("I should have the following till update: $tillinfo")
    public final void iShouldHaveTheFollowingStoreUpdate(
            final ExamplesTable tillinfo) {

        Map<String, String> expectedTill =
            tillinfo.getRow(0);
        Assert.assertEquals("Compare the Result Code",
                Integer.parseInt(expectedTill.get("resultcode")),
                viewTill.getNCRWSSResultCode());
        Assert.assertEquals("Compare the Extended Result Code",
                Integer.parseInt(expectedTill.get("extendedresultcode")),
                viewTill.getNCRWSSExtendedResultCode());

        /*Compare the actual till updated and the expected till*/
        Till till = viewTill.getTill();
        
        if(till != null){
            Assert.assertEquals("Compare the StoreId",
                    expectedTill.get("StoreId"),
                    till.getStoreId());
            Assert.assertEquals("Compare the TillId",
                    expectedTill.get("TillId"),
                    till.getTillId());
            Assert.assertEquals("Compare the BusinessDayDate",
            		expectedTill.get("BusinessDayDate"),
                    till.getBusinessDayDate());
    	    Assert.assertEquals("Compare the SodFlag",
    	    		expectedTill.get("SodFlag"),
    	    		till.getSodFlag());
            Assert.assertEquals("Compare the EodFlag",
            		expectedTill.get("EodFlag"),
            		till.getEodFlag());
            Assert.assertEquals("Compare the State",
            		expectedTill.get("State"),
            		till.getState());
        }


    }

    /**
     * Then Step: Compare the till xml.
     * @param expectedxml  The expected updated store xml representation.
     */
    @Then("I should have the following xml representation "
            + "of updated till: $expectedxml")
    public final void
        iShouldHaveTheFollowingXmlRepresentation(final String expectedxml) {

        try {
            XmlSerializer<ViewTill> actualTill = new XmlSerializer<ViewTill>();
            String actualxml = actualTill.marshallObj(ViewTill.class,
            					viewTill, "UTF-8");
            System.out.println(actualxml);
            Assert.assertEquals("Compare the xml generated",
                       expectedxml, actualxml);
        } catch (Exception e) {
            Assert.fail("Failed to compare the updated till xml");
        }
    }
}
