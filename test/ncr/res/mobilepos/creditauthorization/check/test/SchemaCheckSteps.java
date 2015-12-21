package ncr.res.mobilepos.creditauthorization.check.test;

import ncr.res.mobilepos.creditauthorization.check.SchemaCheck;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.pastelport.platform.CommonTx;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import static org.junit.Assert.assertEquals;
import atg.taglib.json.util.JSONException;



public class SchemaCheckSteps extends Steps {
    
    CommonTx tx;
    
    @BeforeScenario
    public void SetUp() {
        Requirements.SetUp();
    }

    @AfterScenario
    public void TearDown() {
        Requirements.TearDown();
    }
    @When("I should check the testcheckFiled {$msgJson}")
    public void setTx(String msgJson) throws JSONException{
         tx = new CommonTx();
        tx.setJsonValue(msgJson);
       }
    

    @Then("I should get the check result ($expected)")
    public void testCheckFiled(String expected) {
             assertEquals(expected,String.valueOf(SchemaCheck.check(tx)));
    }

}
