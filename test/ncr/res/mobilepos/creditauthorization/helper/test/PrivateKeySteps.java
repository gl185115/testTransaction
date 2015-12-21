package ncr.res.mobilepos.creditauthorization.helper.test;

import junit.framework.Assert;
import ncr.res.mobilepos.creditauthorization.helper.PrivateKey;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.steps.Steps;

public class PrivateKeySteps extends Steps{
    PrivateKey privateKey = null;
    
    @Given("a PrivateKey object with keystorefile"
            + "{$filepath} and password{$password}")
    public void privateKeyObject(String filepath, String password){        
        try {
            privateKey = new PrivateKey(filepath, password);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    
    @Then("should get key{$key} with alias{$alias}")
    public void shouldGetKey(String key, String alias){
        String actualKey = privateKey.getKey(alias);        
        
        if(0 == key.compareToIgnoreCase("empty")) {
            key = "";
        }
        
        Assert.assertEquals(key, actualKey);
    }
    
    @Then("should be able to set alias{$alias} with key{$key}")
    public void setAliasWithKey(String alias, String key){
        privateKey.setKey(alias, key);
    }
    
    @Then("should be able to delete alias{$alias}")
    public void deleteAlias(String alias){
        privateKey.deleteKey(alias);
    }

}
