package ncr.res.mobilepos.authentication.model.test;

import static org.junit.Assert.assertEquals;
import ncr.res.mobilepos.authentication.model.ActivationKey;
import ncr.res.mobilepos.creditauthorization.model.Key;
import ncr.res.mobilepos.helper.XmlSerializer;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.steps.Steps;

public class ActivationKeySteps extends Steps {
    private ActivationKey key = null;
    @Given("an ActivationKey object with key{$key} and tid{$tid}")
    public final void createActivationKey(
            final String keyToSet, final String tidToSet){
        this.key = new ActivationKey(keyToSet, tidToSet);
    }
    
    @Then("xml string should be {$xml}")
    public final void seriallize(final String xml) throws Exception{
        XmlSerializer<ActivationKey> posLogRespSrlzr =
            new XmlSerializer<ActivationKey>();
        String actual =
            posLogRespSrlzr.marshallObj(ActivationKey.class, key, "UTF-8");
        System.out.println(actual);
        assertEquals(xml, actual);
    }
}
