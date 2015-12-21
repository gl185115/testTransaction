package ncr.res.mobilepos.creditauthorization.model.test;

import ncr.res.mobilepos.creditauthorization.model.Key;
import ncr.res.mobilepos.helper.XmlSerializer;
import static org.junit.Assert.assertEquals;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class KeyModelSteps extends Steps{
    private Key key;

    @Given("a Key model")
    public void createDptModel(){    
        key = new Key();        
    }
    
    @When("the genDate is set to {$genDate}")
    public void setGenDate(String genDate){
        this.key.setGenDate(genDate);
    }
    
    @When("the key is set to {$strKey}")
    public void setKey(String strKey){
        this.key.setKey(strKey);
    }
    
    @When("the key64 is set to {$strKey}")
    public void setKey64(String strKey){
        this.key.setKey64(strKey);
    }
    
    @When("the oldKey is set to {$strKey}")
    public void setOldKey(String strKey){
        this.key.setOldKey(strKey);
    }
    
    @When("the oldKey64 is set to {$strKey}")
    public void setOldKey64(String strKey){
        this.key.setOldKey64(strKey);
    }
        
    @Then("xml string should be {$xml}")
    public void seriallize(String xml) throws Exception{
        XmlSerializer<Key> posLogRespSrlzr = new XmlSerializer<Key>();
        String actual = posLogRespSrlzr.marshallObj(Key.class, key, "UTF-8");
        System.out.println(actual);
        assertEquals(xml, actual);
    }
}
