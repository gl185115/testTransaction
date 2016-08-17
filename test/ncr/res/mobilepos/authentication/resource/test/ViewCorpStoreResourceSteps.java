package ncr.res.mobilepos.authentication.resource.test;

import java.io.IOException;
import java.util.regex.Pattern;

import junit.framework.Assert;
import ncr.res.mobilepos.authentication.model.ViewCorpStore;
import ncr.res.mobilepos.authentication.resource.CorpStoreResource;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.store.model.Store;
import ncr.res.mobilepos.store.model.ViewStore;
import ncr.res.mobilepos.store.resource.StoreResource;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

/**
 * @author jessel
 *
 */
public class ViewCorpStoreResourceSteps extends Steps {
    /**
     * StoreResource instance.
     */
    private CorpStoreResource storeResource = null;
    /**
     * Holds store information.
     */
    private ViewCorpStore store = null;

    /**
     * Enum Class for Store Member Variable.
     */
    private enum StoreEnum {
        /**
         * Store id.
         */
        STOREID,
        /**
         * Store name.
         */
        STORENAME,
        /**
         * Store address.
         */
        ADDRESS,
        /**
         * Store contact number.
         */
        TEL,
        /**
         * Store url address.
         */
        URL,
        /**
         * Store sales space name.
         */
        SALESSPACENAME,
        /**
         * Store event name.
         */
        EVENTNAME,
        /**
         * Store ADS.
         */
        ADS,
        /**
         * Store electronic file path.
         */
        ELECTROFILEPATH,
        /**
         * Store stamp tax file path.
         */
        STAMPTAXFILEPATH
    };

    /**
     * @throws Exception
     *             if error exists.
     */
    @BeforeScenario
    public final void setUpClass() throws Exception {
        Requirements.SetUp();
        new DBInitiator("ReportResourceTest",
                "test/ncr/res/mobilepos/authentication/resource/datasets/CORP_STORE.xml", DATABASE.RESMaster);
        GlobalConstant.setCorpid("000000000000");
    }

    /**
     * Executed after end of the scenario.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }

    /**
     * Instantiate StoreResource class.
     */
    @Given("a CorpStoreResource")
    public final void givenStoreResource() {
        storeResource = new CorpStoreResource();
    }

    /**
     * Get Store details of given storeid.
     *
     * @param retailStoreID
     *            storeid to lookup.
     */
    @When("I view corpstore detail of corpid: {$companyID} storeid: {$retailStoreID}")
    public final void viewStore(String companyID, String retailStoreID) {

        if (companyID.equals("null")) {
            companyID = null;
        }
        if (retailStoreID.equals("null")) {
            retailStoreID = null;
        }
        store = storeResource.viewCorpStore(companyID, retailStoreID);
    }

    /**
     * Asserts return result.
     *
     * @param resultCode
     *            expected resultcode value.
     */
    @Then("I should get resultcode equal to $resultCode")
    public final void shouldGetResultCode(final int resultCode) {
        Assert.assertEquals(resultCode, store.getNCRWSSResultCode());
    }

    /**
     * Asserts store information results.
     *
     * @param storeData
     *            expected store information.
     */
    @Then("I should get corpstore details: $data")
    public final void shouldGetStoreDetails(final String storeData) {
        Pattern p = Pattern.compile("|", Pattern.LITERAL);
        String[] data = p.split(storeData);
        Assert.assertEquals(data[0],
                String.valueOf(store.getCorpstore().getStorename()));
        Assert.assertEquals(data[1],
                String.valueOf(store.getCorpstore().getPasscode()));
        Assert.assertEquals(data[2],
                String.valueOf(store.getCorpstore().getPermission()));
        Assert.assertEquals(data[3],
                String.valueOf(store.getCorpstore().getStoreid()));
    }

    @Then ("the JSON should have the following format : $expectedJson")
    public final void theJsonShouldHaveTheFollowingJSONFormat(
            final String expectedJson) {
        try {
            JsonMarshaller<ViewCorpStore> jsonMarshaller =
                new JsonMarshaller<ViewCorpStore>();

            String actualJson =
                jsonMarshaller.marshall(store);
            System.out.println(actualJson);
            Assert.assertEquals("Verify the PromotionResponse JSON Format",
                    expectedJson, actualJson);
        } catch (IOException e) {
            Assert.fail("Failed to verify the PromotionResponse JSON format");
            e.printStackTrace();
        }
    }
}
