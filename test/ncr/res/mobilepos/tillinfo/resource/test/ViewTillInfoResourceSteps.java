package ncr.res.mobilepos.tillinfo.resource.test;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import javax.servlet.ServletContext;

import junit.framework.Assert;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.tillinfo.model.Till;
import ncr.res.mobilepos.tillinfo.model.ViewTill;
import ncr.res.mobilepos.tillinfo.resource.TillInfoResource;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;

public class ViewTillInfoResourceSteps extends Steps{
	
	private DBInitiator dbInitiator;
    /**
     * StoreResource instance.
     */
    private TillInfoResource tillInfoResource = null;
    /**
     * Holds store information.
     */
    private ViewTill viewTill = null;

    /**
     * Enum Class for Store Member Variable.
     */


    /**
     * @throws Exception
     *             if error exists.
     */
    @BeforeScenario
    public final void setUpClass() throws Exception {
        Requirements.SetUp();
        dbInitiator =  new DBInitiator("TillInfoResourceTest",
                "test/ncr/res/mobilepos/tillinfo/resource/test/MST_TILLIDINFO.xml", DATABASE.RESMaster);     
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
    @Given("a TillResource")
    public final void givenTillResource() {
    	ServletContext context = Requirements.getMockServletContext();
    	tillInfoResource = new TillInfoResource();
        try {
            Field tillContext = tillInfoResource.getClass().getDeclaredField("servletContext");
            tillContext.setAccessible(true);
            tillContext.set(tillInfoResource, context);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Cant load Mock Servlet Context.");
        } 
    }

    /**
     * Get till details of given storeid and tillid.
     *
     * @param storeID
     *            storeid to lookup.
     * @param tillID
     *            tillid to lookup.
     */
    @When("I view till detail of storeid: $storeID and tillid: $tillID")
    public final void viewStore(String storeID, String tillID) {
    	if(storeID.equalsIgnoreCase("null")){
    		storeID = null;
    	}
    	if(tillID.equalsIgnoreCase("null")){
    		tillID = null;
    	}
        viewTill = tillInfoResource.viewTill(storeID, tillID);
    }

    /**
     * Asserts return result.
     *
     * @param resultCode
     *            expected resultcode value.
     */
    @Then("I should get resultcode equal to $resultCode")
    public final void shouldGetResultCode(final int resultCode) {
        Assert.assertEquals(resultCode, viewTill.getNCRWSSResultCode());
    }

    /**
     * Asserts store information results.
     *
     * @param storeData
     *            expected store information.
     */
    @Then("I should get till details: $expecteditems")
    public final void shouldGetTillDetails(final ExamplesTable expecteditems) 
    		throws DataSetException{
        
        List<Map<String, String>> expItemRows = expecteditems.getRows();
        
        int index = 0;
        int i = 0;
        
        try{
            for (Map<String, String> expItem : expItemRows) {

                i=index;
 
                Assert.assertEquals("Compare the Store ID row" + index + ": ",
                        expItem.get("storeid"),
                        String.valueOf(viewTill.getTill().getStoreId()));
                Assert.assertEquals("Compare the Till ID row" + index + ": ",
                        expItem.get("tillid"),
                        String.valueOf(viewTill.getTill().getTillId()));
                Assert.assertEquals(
                        "Compare the Business Day Date row" + index + ": ",
                        expItem.get("businessdaydate"),
                        String.valueOf(viewTill.getTill().getBusinessDayDate()));
                Assert.assertEquals(
                        "Compare the SOD flag row" + index + ": ",
                        expItem.get("sodflag"),
                        String.valueOf(viewTill.getTill().getSodFlag()));
                Assert.assertEquals("Compare the EOD flag row" + index + ": ",
                        expItem.get("eodflag"), 
                        String.valueOf(viewTill.getTill().getEodFlag()));
                index++;
            }
        }catch(NullPointerException e){
        	e.printStackTrace();
        	System.out.println(i);
        }

    }
}
