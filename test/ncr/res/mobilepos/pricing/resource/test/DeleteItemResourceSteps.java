package ncr.res.mobilepos.pricing.resource.test;

import junit.framework.Assert;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.resource.ItemResource;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

/**
 * Steps class for deleteItem. It contains steps of a given scenario.
 */
@SuppressWarnings("deprecation")
public class DeleteItemResourceSteps extends Steps {
    /**
     * ItemResource instance.
     */
    private ItemResource itemResource = null;
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
        new DBInitiator("ItemResourceSteps",
                "test/ncr/res/mobilepos/pricing/resource/test/"
                        + "MST_PLU_forDelete.xml", DATABASE.RESMaster);        
    }

    /**
     * Executed after end of the scenario.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }

    /**
     * Instantiate ItemResource class.
     */
    @Given("an ItemResource")
    public final void givenItemResource() {
        itemResource = new ItemResource();
    }

    /**
     * A method when deleting an item of given itemID and storeID.
     *
     * @param itemID
     *            The item id.
     * @param storeID
     *            The store id.
     */
    @When("I delete item {$itemID} of {$storeID}")
    public final void deleteItem(final String itemID, final String storeID) {
        resultBase = itemResource.deleteItem(storeID, itemID);
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
}
