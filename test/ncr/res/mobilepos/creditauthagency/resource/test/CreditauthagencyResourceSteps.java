package ncr.res.mobilepos.creditauthagency.resource.test;

import javax.xml.bind.JAXBException;

import junit.framework.Assert;
import ncr.res.mobilepos.creditauthagency.resource.CreditAuthAgencyResource;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorizationResp;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorizationResps;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

/*
Test Steps:
-Given a transaction is initiated
-When the storeID {$storeID} at terminalID {$terminalID}
 sends transactionID {$transactionID} on transactionDate {$transactionDate}
-Then CA request should succeed
!-- Then CA request should fail
- When operator searches for a CA request with storeid {$storeid}
 at termID {$termID} on txDate {$txDate} at devID {$devID} with operator {$opID}
- Then CA search should be found
- When CA request is uploaded with XML of {poslogxml}
- Then Upload of CA Request should succeed and status must not be null.
*/

@SuppressWarnings("deprecation")
public class CreditauthagencyResourceSteps extends Steps {
    private CreditAuthAgencyResource testCreditAuthAgencyResource;
    private CreditAuthorizationResps caResps;
    private CreditAuthorizationResp caResp;
    private PosLogResp poslogResp;
    
    @BeforeScenario
    public void setUp() {
        Requirements.SetUp();
    }
    
    @AfterScenario
    public void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("a transaction is initiated")
    public void transactionInitiated(){
        testCreditAuthAgencyResource = new CreditAuthAgencyResource();
        poslogResp = new PosLogResp();
        new DBInitiator("Txl_External_Ca_Req",
                "test/ncr/res/mobilepos/creditauthagency/"
                + "dao/test/TXL_EXTERNAL_CA_REQ.xml", DATABASE.RESTransaction);
    }
    
    @When("the storeID {$storeID} at terminalID {$terminalID} sends"
            + " transactionID {$transactionID}"
            + " on transactionDate {$transactionDate}")
    public void caReqest(String storeID, String terminalID,
            String transactionID, String transactionDate){
        caResp = testCreditAuthAgencyResource.getCARequestStatus(
                storeID, terminalID, transactionID, transactionDate);
    }
    
    @Then("I should get XML from getstatus: $expectedXML")
    public void testCreditAuthorizationRespXML(String expectedXML) {
    	XmlSerializer<CreditAuthorizationResp> xmlSerializer = new XmlSerializer<CreditAuthorizationResp>();
    	String creditAuthorizationRespXml = "";    	
    	try {
			creditAuthorizationRespXml = xmlSerializer.marshallObj(CreditAuthorizationResp.class, caResp, "shift_jis");			
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	System.out.println(creditAuthorizationRespXml);
    	Assert.assertEquals(expectedXML, creditAuthorizationRespXml);
    }
    
    @When("operator searches for a CA request with storeid"
            + " {$storeid} at termID {$termID} on txDate {$txDate}")
    public void caSearch(String storeid, String termID, String txDate){
        caResps = testCreditAuthAgencyResource.searchCreditRequest(
                storeid, termID, txDate);
    }
    
    @Then("I should get XML from search: $expectedXML")
    public void testCreditAuthorizationRespsXML(String expectedXML) {
    	String creditAuthorizationResps = "";
    	try {
    		XmlSerializer<CreditAuthorizationResps> xmlSerializer1 = new XmlSerializer<CreditAuthorizationResps>();
    		creditAuthorizationResps = xmlSerializer1.marshallObj(CreditAuthorizationResps.class, caResps, "shift_jis");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	System.out.println(creditAuthorizationResps);
    	Assert.assertEquals(expectedXML, creditAuthorizationResps);
    }
        
    @When("CA request is uploaded with XML of {$poslogxml}")
    public void uploadRequestOfCA(String poslogxml){        
        poslogResp = testCreditAuthAgencyResource.uploadCARequest(poslogxml);
    }
    
    @Then("I should get XML from uploadcarequest: $expectedXML")
    public void testPosLogRespXML(String expectedXML) {
    	String posLogResp = "";
    	try {
    		XmlSerializer<PosLogResp> xmlSerializer1 = new XmlSerializer<PosLogResp>();
    		posLogResp = xmlSerializer1.marshallObj(PosLogResp.class, poslogResp, "shift_jis");
		} catch (JAXBException e) {
			e.printStackTrace();
		}
    	System.out.println(posLogResp);
    	Assert.assertEquals(expectedXML, posLogResp);
    }
}
