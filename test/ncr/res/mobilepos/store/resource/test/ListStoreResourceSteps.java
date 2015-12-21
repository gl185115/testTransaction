/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* ListStoreResourceSteps
*
* The Test Step Class for Listing the Stores.
*
*/
package ncr.res.mobilepos.store.resource.test;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Map;

import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.ExceptionHelper;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.ResultBaseHelper;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.store.model.Store;
import ncr.res.mobilepos.store.model.Stores;
import ncr.res.mobilepos.store.resource.StoreResource;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * The Test Step Class for Listing the Stores.
 * @author cc185102
 *
 */
public class ListStoreResourceSteps extends Steps {
    /**
     * The DB initiator for the unit test.
     */
    private DBInitiator dbInit;
    /**
     * The path of the the Data Set.
     */
    private String dsPathStore = 
        "test/ncr/res/mobilepos/store/resource/test/";
    private String dsPathSysConfig = 
        "test/ncr/res/mobilepos/systemconfiguration/resource/test/";
    /**
     * The Store Resource.
     */
    @InjectMocks
    private StoreResource storeResource;
    @Mock
    private DAOFactory daoFactory;
    /**
     * The actual Stores necessary to be tested.
     */
    private Stores actualStores;

    /**
     * The Setup method for the Test Class.
     * @throws Exception
     *             if error exists.
     */
    @BeforeScenario
    public final void setUpClass() throws Exception {
        Requirements.SetUp();
        GlobalConstant.setMaxSearchResults(5);
        dbInit = new DBInitiator("ListStoreResourceSteps", DATABASE.RESMaster);
   
    }

    /**
     * The Teardown after the Test Class.
     * Executed after end of the scenario.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }

    /**
     * Given Step: initialize the database needed.
     * @param dataset   The dataset provided.
     */
    @Given("a table entry named {$dataset} in the database")
    public final void aTableEntryNamedInTheDatabase(final String dataset) {
        try {
            dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    dsPathStore + dataset);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Assert.fail("Setting the Database table for MST_STOREINFO failed");
        }
    }

    @Given("a batch of values for System Configuration named $table")
    public final void aSytemConfigurationTable(final String tablename){
        try {
            dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    dsPathSysConfig + tablename);
        } catch (Exception e) {
            System.err.println(e.getMessage());
            Assert.fail("Setting the Database table for PRM_SYSTEM_CONFIG failed");
        }
    }
    
    /**
     * Given Step: Initialize the Store Resource.
     */
    @Given("a StoreResource")
    public final void aStoreResource() {
        storeResource = new StoreResource();
    }
    
    @Given("that database is throwing an unexpected {$exception}")
	public final void givenThrownException(String exception) {
		MockitoAnnotations.initMocks(this);
		Exception ex = ExceptionHelper.getException(exception);
		try {
			Mockito.stub(daoFactory.getStoreDAO()).toThrow(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    @Given("a system configuration has {$limit} key limit")
    public final void setConfigurationLimit(String limit){
        Requirements.addContextAttribute(GlobalConstant.MAX_SEARCH_RESULTS,
                limit.equalsIgnoreCase("null") ? null : limit);
    }
    
    @When("I set the search limit to {$limit}")
    public final void setMaxLimit(String limit){
        limit = limit.equals("null") ? "0" : limit;
        GlobalConstant.setMaxSearchResults(Integer.parseInt(limit));
    }

    /**
     * When Step: List all stores.
     * @param key The Key.
     */
    @When("a request to list all stores with companyId {$companyId}, key {$key} and name {$name}")
    public final void aRequestToListAllStores(String companyId, String key, String name) {
    	companyId = StringUtility.convNullStringToNull(companyId);
        key = StringUtility.convNullStringToNull(key);
        name = StringUtility.convNullStringToNull(name);
        actualStores = storeResource.searchStores(companyId, key, name, -1);
    }

    /**
     * When Step: List all stores.
     */
    @When("a request to list all stores")
    public final void aRequestToListAllStores() {
        actualStores = storeResource.searchStores(null, null, null, -1);
    }

    /**
     * Then Step: Compare the Actual Stores retrieved from the expected Stores.
     * @param expectedstores The Expected Stores
     */
    @Then("the following stores are retrieved: $expectedstores")
    public final void theFollowingStoresAreRetrieved(
            final ExamplesTable expectedstores) {
        List<Store> actualStoresList = actualStores.getStorelist();

        Assert.assertEquals("Compare the number of stores:", expectedstores.getRowCount(),
                actualStoresList.size());

        String storeid = null;
        String storename = null;
        String storeaddress = null;
        String storetel = null;

        int index = 0;
        for (Map<String, String> expectedStore : expectedstores.getRows()) {
            Store actualStore = actualStoresList.get(index);
            Assert.assertEquals("Compare the Result Code",
                    Integer.parseInt(
                            expectedStore.get("ResultCode")),
                    actualStores.getNCRWSSResultCode());
            Assert.assertEquals("Compare the Extended Result Code",
                    Integer.parseInt(
                            expectedStore.get("ExtendedResultCode")),
                    actualStores.getNCRWSSExtendedResultCode());

            storeid = expectedStore.get("RetailStoreID");
            storename = expectedStore.get("StoreName");
            storeaddress = expectedStore.get("StoreAddr");
            storetel = expectedStore.get("StoreTel");

            if (storeid.equals("NULL")) {
                storeid = null;
            }

            if (storename.equals("NULL")) {
                storename = null;
            }

            if (storetel.equals("NULL")) {
                storetel = null;
            }

            if (storeaddress.equals("NULL")) {
                storeaddress = null;
            }

            /*Compare a Store*/
            Assert.assertEquals("Compare the Store ID",
                    storeid,
                    actualStore.getRetailStoreID());
            Assert.assertEquals("Compare the Store Name",
                    storename,
                    actualStore.getStoreName());
            Assert.assertEquals("Compare the Address",
                    storeaddress,
                    actualStore.getAddress());
            Assert.assertEquals("Compare the Telephone",
                    storetel,
                    actualStore.getTel());
            index++;
        }
    }

    /**
     * Then Step: Compare the expected xml representation of the actual Stores.
     * @param expectedXml The expected xml format.
     */
    @Then("the stores is in the following xml: $expectedXml")
    public final void theStoresIsInTheFollowingXml(final String expectedXml) {
        XmlSerializer<Stores> storesXmlSerializer =
            new XmlSerializer<Stores>();

        try {
            String actualXml = storesXmlSerializer
                .marshallObj(Stores.class, actualStores, "UTF-8");
            System.out.println(actualXml);
            Assert.assertEquals("Compare the xml representation of stores",
                    expectedXml, actualXml);
        } catch (JAXBException e) {
            e.printStackTrace();
            Assert.fail("Failed to represent Stores Model "
                            + "into it xml representation.");
        }
    }
    
    @Then("the result should be {$result}")
    public final void thenResultShouldBe(String result) {
    	 assertEquals(ResultBaseHelper.getErrorCode(result), actualStores.getNCRWSSResultCode());
    }
   
}
