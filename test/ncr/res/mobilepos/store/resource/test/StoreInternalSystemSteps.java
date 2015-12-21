package ncr.res.mobilepos.store.resource.test;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.store.model.StoreInternSys;
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

/**
 * The Test Step Class for StoreInternalSystem
 * @author VR185019
 *
 */

public class StoreInternalSystemSteps  extends Steps {
    /**
     * The DB initiator for the unit test.
     */
    private DBInitiator dbInit;
    /**
     * The path of the the Data Set.
     */
    private String dsPathStore =
        "test/ncr/res/mobilepos/store/resource/test/";
    /**
     * The actual Stores necessary to be tested.
     */
    private StoreInternSys actualData;
    private ServletContext servletContext = null;
    private StoreResource store = null; 
    /**
     * The Setup method for the Test Class.
     * @throws Exception
     *             if error exists.
     */
    @BeforeScenario
    public final void setUpClass() throws Exception {
        Requirements.SetUp();
        GlobalConstant.setMaxSearchResults(5);
        dbInit = new DBInitiator("StoreInternalSystemSteps", DATABASE.RESMaster);
        this.store = new StoreResource();
    }

    /**
     * The Teardown after the Test Class.
     * Executed after end of the scenario.
     */
    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }
    
    @Given("entries for {$dataset} in database")
    public final void initdatasets(final String dataset) throws Exception{
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
        		dsPathStore + dataset + ".xml");
    }
    
    @When("the Web API Starts Up")
    public final void theWebAPIStartsUp(){
        servletContext = null;
        try
        {     	
            servletContext = Requirements.getMockServletContext();
            Field context = store
            .getClass().getDeclaredField("context");
            context.setAccessible(true);
            context.set(store, servletContext);            
        } catch (/*RuntimeError*/Exception ex) {
          
        }
    }
    
    @When("the {$storecode} and  {$usage}")
    public final void theStorecodeAndUsage(String storecode, int usage){
    	actualData = store.spartStore(storecode, usage);
    }
    @Then("the store value should be $expectedItems")
    public final void theStoreValueShouldBe(final ExamplesTable expectedItems) {
    	Map<String, String> expectedItem = expectedItems.getRow(0);
    	if (expectedItem.get("value").equals("null")) {
        	Assert.assertEquals("Store is closing" ,
                    actualData.getValue(),
                    null);
    	} else {
        	Assert.assertEquals("Store is closing" ,
                    actualData.getValue(),
                    expectedItem.get("value"));	
    	}

    }
}
