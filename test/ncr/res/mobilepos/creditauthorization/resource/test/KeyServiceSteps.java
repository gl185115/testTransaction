package ncr.res.mobilepos.creditauthorization.resource.test;

import java.io.IOException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.CertificateException;
import java.util.Calendar;

import static org.junit.Assert.*;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.creditauthorization.helper.PrivateKey;
import ncr.res.mobilepos.creditauthorization.helper.Sha256KeyGenerator;
import ncr.res.mobilepos.creditauthorization.model.Key;
import ncr.res.mobilepos.creditauthorization
          .resource.CreditAuthorizationResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import org.junit.runner.RunWith;
import org.mockito.Mockito;
import org.springframework.mock.web.MockServletContext;

public class KeyServiceSteps extends Steps{
    private CreditAuthorizationResource creditAuthorizationResource;
    private Key key;
    private ServletContext context;
    private PrivateKey keystore = null;

    @BeforeScenario
    public void setUp(){
        Requirements.SetUp();        
    }
    
    @AfterScenario
    public void tearDown(){
        Requirements.TearDown();
    }
    
    @Given("a CreditAuthorizationResource")
    public void createResource() throws KeyStoreException,
        NoSuchAlgorithmException, CertificateException, IOException{
        context = Requirements.getMockServletContext();    
        creditAuthorizationResource = new CreditAuthorizationResource();
        creditAuthorizationResource.setContext(context);
        
        try {
            keystore = new PrivateKey(
                    (String)context.getInitParameter("AESKeyStorePath"), 
                    (String)context.getInitParameter("AESKeyStorePass"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        creditAuthorizationResource.setPrivateKey(keystore);
    }
    
    @Given("that keystore does not have gendate key")
    public void removeGenDate(){
        if(!keystore.getKey(
                (String)context
                    .getInitParameter("AESKeyStoreGenDateAlias")).isEmpty()){
            keystore.deleteKey((String)context
                    .getInitParameter("AESKeyStoreGenDateAlias"));
        }
    }
    
    
    @When("I get AES key for today{$datetime}")
    public void getAesKey(long datetime, String companyid, String storeid){
        Calendar cal = Calendar.getInstance();
        cal.setTimeInMillis(datetime);
        
        String masterKey = keystore.getAesKey();
        Sha256KeyGenerator sha256KeyGenerator =
            new Sha256KeyGenerator(masterKey);
        sha256KeyGenerator.setCalendar(cal);
        creditAuthorizationResource.setSha256(sha256KeyGenerator);
        
        key = creditAuthorizationResource.getAesKey(companyid, storeid);
        keystore = creditAuthorizationResource.getPrivateKey();
    }
    
    @Then("the key should be {$keyvalue}")
    public void validateKey(String keyvalue){
        if(keyvalue.equals("")){
            keyvalue = null;
        }
        assertEquals(keyvalue, key.getKey());
    }
    
    @Then("the key64 should be {$keyvalue}")
    public void validateKey64(String keyvalue){
        assertEquals(keyvalue, key.getKey64());
    }
    
    @Then("the genDate should be {$gendate}")
    public void validateGendate(String gendate){
        assertEquals(gendate, key.getGenDate());
    }
    
    @Then("the keystore gendate key should be {$gendate}")
    public void validateKeystoreGenDate(String gendate){
        assertEquals(gendate, keystore.getKey((
                (String)context.getInitParameter("AESKeyStoreGenDateAlias"))));
    }
    
    @When("date is set to {$date}")
    public void setDate(String date){
        new DBInitiator("ReportResourceTest",
        "test/ncr/res/mobilepos/creditauthorization/"
           + "resource/test/PRM_SYSTEM_CONFIG_BusinessDateComputeFlag_False_"
           + date + ".xml", DATABASE.RESMaster);
    }
    
    @When("AESKey flag is set to {$flag}")
    public void setAesKeyFlag(String flag){
        new DBInitiator("ReportResourceTest",
        "test/ncr/res/mobilepos/creditauthorization/resource/"
                + "test/PRM_SYSTEM_CONFIG_AESKey_" + flag + ".xml", DATABASE.RESMaster);
    }

}
