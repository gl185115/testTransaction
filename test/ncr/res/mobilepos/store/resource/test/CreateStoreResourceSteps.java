package ncr.res.mobilepos.store.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import java.sql.SQLException;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.store.resource.StoreResource;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

/**
 * @author RD185102
 *
 */
public class CreateStoreResourceSteps extends Steps {

    /**
     *  Store Resource object.
     */
	 @InjectMocks
     private StoreResource storeRes;

     @Mock
     private DAOFactory daoFactory;

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
    @Given("I have a Store Resource")
     public final void iHaveStoreResource() {
         storeRes = new StoreResource();
         //GlobalConstant.setCorpid("000000000000");
         resultBase = new ResultBase();
    }

    /**
     *  Give Step : An empty Store table.
     */
    @Given("an empty Store Table")
    public final void anEmptyStoreTable() {
         DBInitiator dbinit = new DBInitiator("StoreResource",
            "test/ncr/res/mobilepos/store/resource/"
                + "test/MST_STOREINFO_EMPTY.xml", DATABASE.RESMaster);
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
     * Scenario when creating a Store.
     * @param retailStoreID - Store number
     * @param store - Store
     */

    @When("I add a store with storenumber {$retailStoreID} and store [$store]")
      public final void createStore(String retailStoreID,
        final String store) {

          if (retailStoreID.equals("null")) {
              retailStoreID = null;
          }
          resultBase =  storeRes.createStore(retailStoreID, store);
    }

    
    /**
     * Scenario when deleting a Store.
     * @param retailStoreID - Store number
     * @param store - Store
     */

    @When("I delete a store with storenumber {$retailStoreID}")
      public final void deleteStore(String retailStoreID) {

          if (retailStoreID.equals("null")) {
              retailStoreID = null;
          }
          resultBase =  storeRes.deleteStore(retailStoreID);
    }
    
    /**
     * Shows the result code.
     * @param result - Result Code
     */
    @Then("the result should be {$Result}")
     public final void resultShouldBe(final int result) {
        assertThat(resultBase.getNCRWSSResultCode(), is(equalTo(result)));
     }

}
