/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* UpdateStoreResourceTest
*
* The Test Step Class for listing the Stores.
*
*/
package ncr.res.mobilepos.store.resource.test;

import java.sql.SQLException;
import java.util.Map;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
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
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * The Test Step Class for Updating a Store.
 * @author cc185102
 *
 */
public class UpdateStoreResourceSteps extends Steps {
    /**
     * The Store Resource.
     */
	@InjectMocks
    private StoreResource storeResource;
	@Mock
	private DAOFactory daoFactory;
    /**
     * The expected Store result from update.
     */
    private ViewStore actualViewStore;
    
    private ResultBase resultBase;

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
    @Given("I have a Store Resource")
    public final void iHaveAStoreResource() {
        storeResource = new StoreResource();     
    }
    /**
     * Given Step: Set the Store Info.
     */
    @Given("a Store Table")
    public final void aStoreTable() {
        DBInitiator dbinit = new DBInitiator("AuthenticationResource",
                "test/ncr/res/mobilepos/store/resource/test/MST_STOREINFO.xml", DATABASE.RESMaster);
    }
    
    @Given("that database is throwing an unexpected {$exception}")
	public final void givenThrownException(String exception) {
		MockitoAnnotations.initMocks(this);
		Exception ex = new Exception();
		
		if ("DaoException".equalsIgnoreCase(exception)) {
			ex = new DaoException();
		} else if ("Exception".equalsIgnoreCase(exception)) {
			ex = new Exception();
		} else if ("SQLException".equalsIgnoreCase(exception)) {
			ex = new DaoException(new SQLException());
		} else if ("SQLStatementException".equalsIgnoreCase(exception)) {
			ex = new DaoException(new SQLStatementException());
		}
			
		try {
			Mockito.stub(daoFactory.getStoreDAO()).toThrow(ex);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }

    /**
     * When Step: Update a store.
     * @param storeinfo The Store Information for update.
     */
    @When("I update a store with the following details: $storeinfo")
    public final void iUpdateAStoreWithTheFollowingDetails(
            final ExamplesTable storeinfo) {
        Map<String, String> storeinfovalue =
            storeinfo.getRow(0);
        actualViewStore =
            storeResource.updateStores(storeinfovalue.get("StoreId"),
                storeinfovalue.get("storejson"));
    }
    
    /**
     * When Step: Update a deleted  store.
     * @param storeinfo The Store Information for update.
     */
    @When("I update a deleted store with the following storeID {$storeId} details: $storeinfo")
    public final void updateToDeleteStore(final String storeId,
            final ExamplesTable storeinfo) {
        Map<String, String> storeinfovalue =
            storeinfo.getRow(0);
        actualViewStore =
            storeResource.updateStores(storeId,
                storeinfovalue.get("storejson"));
    }

    
    /**
     * Scenario when deleting a Store.
     * @param retailStoreID - Store number
     * @param store - Store
     */

    @When("I delete an existing store with storenumber {$storeID}")
      public final void deleteStore(final String storeID) {
          String retailStoreId = storeID;
          if (retailStoreId.equals("null")) {
              retailStoreId = null;
          }
          resultBase =  storeResource.deleteStore(retailStoreId);
    }
    /**
     * Then Step: Compare the actual store updated
     * and expected to the store update.
     * @param storeinfo The expected Store information.
     */
    @Then("I should have the following store update: $storeinfo")
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
        Store actualStore = actualViewStore.getStore();

        Assert.assertEquals("Compare the address",
                expectedStore.get("StoreAddr"),
                actualStore.getAddress());
        Assert.assertEquals("Compare the ads",
                expectedStore.get("Ads"),
                actualStore.getAds());
        Assert.assertEquals("Compare the tel",
                expectedStore.get("StoreTel"),
                actualStore.getTel());
        Assert.assertEquals("Compare the url",
                expectedStore.get("StoreUrl"),
                actualStore.getUrl());
        Assert.assertEquals("Compare the Sales Space Name",
                actualStore.getSalesSpaceName(),
                expectedStore.get("SalesSpaceName"));
        Assert.assertEquals("Compare the Event Name",
                expectedStore.get("EventName"),
                actualStore.getEventName());
        Assert.assertEquals("Compare the ElectroFilePath",
                expectedStore.get("ElectroFilePath"),
                actualStore.getElectroFilePath());
        Assert.assertEquals("Compare the Stam Tax File Path",
                expectedStore.get("StampTaxFilePath"),
                actualStore.getStampTaxFilePath());
    }

    /**
     * Then Step: Compare the store xml.
     * @param expectedxml  The expected updated store xml representation.
     */
    @Then("I should have the following xml representation "
            + "of updated store: $updatedStoreXml")
    public final void
        iShouldHaveTheFollowingXmlRepresentation(final String expectedxml) {

        try {
            XmlSerializer<ViewStore> viewStore = new XmlSerializer<ViewStore>();
            String actualxml = viewStore.marshallObj(ViewStore.class,
                               actualViewStore, "UTF-8");
            System.out.println(actualxml);
            Assert.assertEquals("Compare the xml generated",
                       expectedxml, actualxml);
        } catch (Exception e) {
            Assert.fail("Failed to compare the updated store xml");
        }
    }
    @Then("the result code should be {$resultCode}")
    public final void resultCodeShouldBe(final int resultCode) {
    	Assert.assertEquals("Compare the result code", resultCode, actualViewStore.getNCRWSSResultCode());
    }
}
