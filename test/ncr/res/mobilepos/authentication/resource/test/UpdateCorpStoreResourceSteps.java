/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* UpdateStoreResourceTest
*
* The Test Step Class for listing the Stores.
*
*/
package ncr.res.mobilepos.authentication.resource.test;

import java.util.Map;

import ncr.res.mobilepos.authentication.model.CorpStore;
import ncr.res.mobilepos.authentication.model.ViewCorpStore;
import ncr.res.mobilepos.authentication.resource.CorpStoreResource;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.store.model.Store;
import ncr.res.mobilepos.store.model.ViewStore;
import ncr.res.mobilepos.store.resource.StoreResource;

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
 * @author cc185102
 *
 */
public class UpdateCorpStoreResourceSteps extends Steps {
    /**
     * The Store Resource.
     */
    private CorpStoreResource storeResource;
    /**
     * The expected Store result from update.
     */
    private ViewCorpStore actualViewStore;
    
    private DBInitiator dbinit;

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
     * Set up the Store Resource.
     */
    @Given("I have a CorpStore Resource")
    public final void iHaveAStoreResource() {
        storeResource = new CorpStoreResource();      
    }
    /**
     * Given Step: Set the Store Info.
     */
    @Given("a CorpStore Table")
    public final void aStoreTable() {
        dbinit = new DBInitiator("AuthenticationResource",
                "test/ncr/res/mobilepos/authentication/resource/datasets/CORP_STORE_for_update.xml", DATABASE.RESMaster);
    }

    /**
     * When Step: Update a store.
     * @param storeInfo The Store Information for update.
     */
    @When("I update a corpstore with the following details: $storeinfo")
    public final void iUpdateAStoreWithTheFollowingDetails(
            final ExamplesTable storeInfo) {
        Map<String, String> storeinfovalue =
            storeInfo.getRow(0);
        
        String storeid = "", json = "";
     
        storeid = (storeinfovalue.get("storeid").equals("null"))? null:storeinfovalue.get("storeid");
        json = (storeinfovalue.get("storejson").equals("null"))? null:storeinfovalue.get("storejson");
        
        actualViewStore =
            storeResource.updateCorpStore("", storeid,json);
    }

    /**
     * Then Step: Compare the actual store updated
     * and expected to the store update.
     * @param storeinfo The expected Store information.
     */
    @Then("I should have the following corpstore update: $storeinfo")
    public final void iShouldHaveTheFollowingStoreUpdate(
            final ExamplesTable storeinfo) {

        Map<String, String> expectedStore =
            storeinfo.getRow(0);
        Assert.assertEquals("Compare the Result Code",
                Integer.parseInt(expectedStore.get("resultcode")),
                actualViewStore.getNCRWSSResultCode());
        Assert.assertEquals("Compare the Extended Result Code",
                Integer.parseInt(expectedStore.get("extendedresultcode")),
                actualViewStore.getNCRWSSExtendedResultCode());

        /*Compare the actual store updated and the expected store*/
        CorpStore actualStore = actualViewStore.getCorpstore();

        Assert.assertEquals("Compare the storeid",
                expectedStore.get("storeid"),
                actualStore.getStoreid());
        Assert.assertEquals("Compare the storename",
                expectedStore.get("storename"),
                actualStore.getStorename());
        Assert.assertEquals("Compare the passcode",
                expectedStore.get("passcode"),
                actualStore.getPasscode());
        if (expectedStore.get("permission") != null) {
        Assert.assertEquals("Compare the permission",
                Integer.parseInt(expectedStore.get("permission")),
                actualStore.getPermission());
        }
    }

    /**
     * Then Step: Compare the store xml.
     * @param expectedxml  The expected updated store xml representation.
     */
    @Then("I should have the following xml representation "
            + "of updated corpstore: $updatedStoreXml")
    public final void
        iShouldHaveTheFollowingXmlRepresentation(final String expectedxml) {

        try {
            XmlSerializer<ViewCorpStore> viewStore = new XmlSerializer<ViewCorpStore>();
            String actualxml = viewStore.marshallObj(ViewCorpStore.class,
                               actualViewStore, "UTF-8");
            System.out.println(actualxml);
            Assert.assertEquals("Compare the xml generated",
                       expectedxml, actualxml);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Failed to compare the updated store xml");
            
        }
    }
}
