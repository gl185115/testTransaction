package ncr.res.mobilepos.tillinfo.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.tillinfo.resource.TillInfoResource;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

/**
 * @author RD185102
 *
 */
public class CreateTillInfoResourceSteps extends Steps {

	private DBInitiator dbInitiator;
    /**
     *  Store Resource object.
     */
     private TillInfoResource tillRes;

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
     public final void setUp() throws Exception {
        Requirements.SetUp();
        dbInitiator =  new DBInitiator("TillInfoResourceTest",
                "test/ncr/res/mobilepos/tillinfo/resource/test/MST_TILLIDINFO.xml", DATABASE.RESMaster); 
    }

    /**
     * Destroys the database connection.
     */
    @AfterScenario
    public final void tearDown() {
        Requirements.TearDown();
    }

    /**
     *  Given Step : I have Till Info Resource.
     */
    @Given("I have a Till Info Resource")
     public final void iHaveTillInfoResource() {
         tillRes = new TillInfoResource();
         resultBase = new ResultBase();
    }

    /**
     *  Give Step : An empty Store table.
     */
    @Given("an empty Till Table")
    public final void anEmptyTillTable() {
         DBInitiator dbinit = new DBInitiator("TillInfoResource",
            "test/ncr/res/mobilepos/tillinfo/resource/"
                + "test/MST_TILLIDINFO_EMPTY.xml", DATABASE.RESMaster);
    }

    /**
     * Scenario when creating a till.
     * @param storeID - Store number
     *  @param tillID - Till number
     * @param till - Till
     */

    @When("I add a till with storeid {$storeID} and tillid {$tillID} and  till [$till]")
      public final void createTill(String storeID, String tillID,
        final String store) {

          if (storeID.equals("null")) {
        	  storeID = null;
          }
          if (tillID.equals("null")) {
        	  tillID = null;
          }
          resultBase =  tillRes.createTill(storeID, tillID, store);
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
