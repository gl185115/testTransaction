package ncr.res.mobilepos.store.resource.test;

import java.sql.SQLException;
import java.util.regex.Pattern;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.store.model.ViewStore;
import ncr.res.mobilepos.store.resource.StoreResource;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author jessel
 *
 */
public class ViewStoreResourceSteps extends Steps {
    /**
     * StoreResource instance.
     */
	@InjectMocks
    private StoreResource storeResource = null;
	@Mock
    private DAOFactory daoFactory;
    /**
     * Holds store information.
     */
    private ViewStore store = null;

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
                "test/ncr/res/mobilepos/store/resource/test/MST_STOREINFO.xml", DATABASE.RESMaster);
     
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
    @Given("a StoreResource")
    public final void givenStoreResource() {
        storeResource = new StoreResource();
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
     * Get Store details of given storeid.
     *
     * @param retailStoreID
     *            storeid to lookup.
     */
    @When("I view store detail of storeid: $retailStoreID")
    public final void viewStore(final String retailStoreID) {
        store = storeResource.viewStore(retailStoreID);
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
    @Then("I should get store details: $data")
    public final void shouldGetStoreDetails(final String storeData) {
        Pattern p = Pattern.compile("|", Pattern.LITERAL);
        String[] data = p.split(storeData);
        Assert.assertEquals(data[StoreEnum.STOREID.ordinal()],
                String.valueOf(store.getStore().getRetailStoreID()));
        Assert.assertEquals(data[StoreEnum.STORENAME.ordinal()],
                String.valueOf(store.getStore().getStoreName()));
        Assert.assertEquals(data[StoreEnum.ADDRESS.ordinal()],
                String.valueOf(store.getStore().getAddress()));
        Assert.assertEquals(data[StoreEnum.TEL.ordinal()],
                String.valueOf(store.getStore().getTel()));
        Assert.assertEquals(data[StoreEnum.URL.ordinal()],
                String.valueOf(store.getStore().getUrl()));
        Assert.assertEquals(data[StoreEnum.SALESSPACENAME.ordinal()],
                String.valueOf(store.getStore().getSalesSpaceName()));
        Assert.assertEquals(data[StoreEnum.EVENTNAME.ordinal()],
                String.valueOf(store.getStore().getEventName()));
        Assert.assertEquals(data[StoreEnum.ADS.ordinal()],
                String.valueOf(store.getStore().getAds()));
        Assert.assertEquals(data[StoreEnum.ELECTROFILEPATH.ordinal()],
                String.valueOf(store.getStore().getElectroFilePath()));
        Assert.assertEquals(data[StoreEnum.STAMPTAXFILEPATH.ordinal()],
                String.valueOf(store.getStore().getStampTaxFilePath()));
    }
}
