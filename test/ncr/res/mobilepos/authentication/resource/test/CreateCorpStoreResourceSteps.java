package ncr.res.mobilepos.authentication.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.List;
import java.util.Map;

import ncr.res.mobilepos.authentication.resource.CorpStoreResource;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.store.model.Store;
import ncr.res.mobilepos.store.resource.StoreResource;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

/**
 * @author RD185102
 *
 */
public class CreateCorpStoreResourceSteps extends Steps {

    /**
     *  Store Resource object.
     */
     private CorpStoreResource storeRes;

     /**
      * Store object.
      */
    // private Store myStore;

     /**
      * ResultBase object.
      */
     private ResultBase resultBase;

    /**
     *Connects to the database.
     */
    @BeforeScenario
     public final void setUp() {
        Requirements.SetUp();
    }

    /**
     * Destroys the database connection.
     */
    @AfterScenario
    public final void tearDown() {
        Requirements.TearDown();
    }

    /**
     *  Given Step : I have Store Resource.
     */
    @Given("I have a CorpStore Resource")
     public final void iHaveStoreResource() {
         storeRes = new CorpStoreResource();       
         resultBase = new ResultBase();
    }
    
    private DBInitiator dbinit = null;

    /**
     *  Give Step : An empty Store table.
     */
    @Given("an empty CorpStore Table")
    public final void anEmptyStoreTable() {
         dbinit = new DBInitiator("StoreResource",
            "test/ncr/res/mobilepos/authentication/resource/datasets/"
                + "CORP_STORE_EMPTY.xml", DATABASE.RESMaster);
    }

    /**
     * Scenario when creating a Store.
     * @param retailStoreId - Store number
     * @param store - Store
     */

    @When("I add a corpstore with company number {$companyID} storenumber {$retailStoreID} and store [$store]")
      public final void createStore(String companyID, String retailStoreId,
        String store) {
        if (companyID.equals("null")) {
            companyID = null;
        }
        if (retailStoreId.equals("null")) {
            retailStoreId = null;
        }
        if (store.equals("null")) {
            store = null;
        }
          resultBase =  storeRes.createCorpStore(companyID, retailStoreId, store);
    }

    /**
     * Shows the result code.
     * @param result - Result Code
     */
    @Then("the result should be {$Result}")
     public final void resultShouldBe(final int result) {
        assertThat(resultBase.getNCRWSSResultCode(), is(equalTo(result)));
     }
    
    /**
     * Then Step: Verify the corpstores in the database.
     * @param expectedstores The Expected Stores
     * @throws DataSetException 
     */
    @Then("the following corpstores should exist: $expectedcorpstores")
    public final void theFollowingStoresAreRetrieved(
            final ExamplesTable expectedcorpstores) throws DataSetException {
        ITable actualDeviceInfoTable = dbinit.getTableSnapshot("MST_CORP_STORE");

        Assert.assertEquals(
                "Compare the number of stores:",
                expectedcorpstores.getRowCount(),
                actualDeviceInfoTable.getRowCount());
        int i = 0;
        for (Map<String, String> expectedDeviceInfo : expectedcorpstores.getRows()) {
            String storeid = (String)actualDeviceInfoTable.getValue(i, "storeid");
            Assert.assertEquals("Compare the storeid in MST_DEVICEINFO row" + i,
                    expectedDeviceInfo.get("storeid"),
                    storeid.trim());
            Assert.assertEquals("Compare the storename in MST_DEVICEINFO row" + i,
                    expectedDeviceInfo.get("storename"),
                    (String)actualDeviceInfoTable.getValue(i, "storename"));
            Assert.assertEquals("Compare the passcode in MST_DEVICEINFO row" + i,
                    expectedDeviceInfo.get("passcode"),
                    (String)actualDeviceInfoTable.getValue(i, "passcode"));
            Assert.assertEquals("Compare the permission in MST_DEVICEINFO row" + i,
                    Integer.parseInt(expectedDeviceInfo.get("permission")),
                    (int)(Integer)actualDeviceInfoTable.getValue(i, "permission"));
            i++;
        }
    }

}
