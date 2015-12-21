package ncr.res.mobilepos.creditauthorization.helper.test;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import junit.framework.Assert;
import ncr.res.mobilepos.creditauthorization.helper.Sha256KeyGenerator;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.steps.Steps;

public class Sha256KeyGeneratorSteps extends Steps{
    private Sha256KeyGenerator generator = null;
    @Given("an instance of Sha256KeyGenerator with key{$key}")
    public void createGenerator(String key){
        generator = new Sha256KeyGenerator(key);
    }
    
    @Then("Sha256KeyGenerator.now should be equal to now with proper format")
    public void validateNow(){
        Calendar cal = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
        String now = sdf.format(cal.getTimeInMillis());
        assertEquals(generator.now(), now);
    }

    @Then("Sha256KeyGenerator.getKeyBase64(datetime) should be equal"
            + " to base64{$generatedKey} when datetime string is {$datetime}")
    public void generateKeyBase64(String generatedKey, String datetime){
        try {
            assertEquals("Expect the generated Key in Base64 to be",
                    generatedKey,
                    generator.getKeyBase64(datetime));
        } catch (Exception e) {
            Assert.fail("Failed to implement the Sha256KeyGenerator"
                    + ".getKeyBase64(" + datetime + ")");
        }
    }
    
    @Then("Sha256KeyGenerator.getKey(datetime) should be equal to"
            + " {$generatedKey} when datetime string is {$datetime}")
    public void generateKey(String generatedKey, String datetime){
        try {
            assertEquals("Expect the generated Key to be", generatedKey,
                    generator.getKeyHex(datetime));
        } catch (Exception e) {
            Assert.fail("Failed to implement the Sha256KeyGenerator.getKey("
                    + datetime + ")");
        }
    }
    
    @Then("Sha256KeyGenerator.getKeyBase64() should be equal to "
            + "base64{$generatedKey}")
    public void generateKeyBase64(String generatedKey){
        Sha256KeyGenerator genSpy = spy(generator);
        
        try {
            doReturn("20120521191815").when(genSpy).now();
            assertEquals(generatedKey, genSpy.getKeyBase64());
        } catch (Exception e) {
            Assert.fail("Failed to implement the "
                    + "Sha256KeyGenerator.getKeyBase64(20120521191815)");
        }
    }
    
    @Then("Sha256KeyGenerator.getKey() should be equal to {$generatedKey}")
    public void generateKey(String generatedKey){
        Sha256KeyGenerator genSpy = spy(generator);                
        try {
            doReturn("20120521191815").when(genSpy).now();
            assertEquals(generatedKey, genSpy.getKeyHex());
        } catch (Exception e) {
            Assert.fail("Failed to implement unit test on the "
                    + "Sha256KeyGenerator.getKey(20120521191815)");
        }
    }
}
