/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * CreditAuthorizationResourceSteps
 *
 * Steps Class of CreditAuthorizationResource
 *
 * De la Cerna, Jessel G.
 */

package ncr.res.mobilepos.creditauthorization.resource.test;

import static org.junit.Assert.*;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.creditauthagency.resource.CreditAuthAgencyResource;
import ncr.res.mobilepos.creditauthorization.helper.CreditAuthMsgLogger;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorizationResp;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.creditauthorization
          .resource.CreditAuthorizationResource;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.model.ResultBase;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import org.mockito.Mockito;

public class CreditAuthorizationResourceSteps extends Steps {
    static class TestCreditAuthorizationResource extends CreditAuthorizationResource {
        @Override
        public CreditAuthorizationResp authorizeExtCredit(final String creditAuthorizationXml) {
            return super.authorizeExtCredit(creditAuthorizationXml);
        }
    }
    private TestCreditAuthorizationResource creditAuthorizationResource;
    private CreditAuthorizationResp creditAuthResp;    
    private ResultBase resultBase;
    private PastelPortResp pastelPortResp = null;
    private PastelPortResp ppResp = new PastelPortResp();
    private CreditAuthorizationResource ca = null;
    private CreditAuthMsgLogger caMsgLog;
    private ServletContext context;
    private CreditAuthAgencyResource caAgencyRes;
    
    @BeforeScenario
    public void setUp() {
        Requirements.SetUp();
        new DBInitiator("CreditAuthorizationServiceTest", 
                "test/ncr/res/mobilepos/creditauthorization/"
                + "resource/test/credit_authorization_dataset.xml", DATABASE.RESTransaction);
        new DBInitiator("CreditAuthorizationServiceTest", 
                "test/ncr/res/mobilepos/creditauthorization/"
                + "resource/test/devices_credential_dataset.xml", DATABASE.RESMaster);
    }
    
    @AfterScenario
    public void TearDown(){
        Requirements.TearDown();
        creditAuthorizationResource = null;
        creditAuthResp = null;
        resultBase = null;
    }
    
    @Given("a CreditAuthorizationResource")
    public void createResource(){
        context = Requirements.getMockServletContext();    
        creditAuthorizationResource = new TestCreditAuthorizationResource();
        caAgencyRes = new CreditAuthAgencyResource();
        creditAuthorizationResource.setContext(context);        
        caMsgLog = new CreditAuthMsgLogger();
    }
    
    @Then("I should have a CreditAuthorizationResource")
    public void testCreditAuthorizationResource(){
        assertNotNull(creditAuthorizationResource);
    }
    

    
    @When("request creditauthorization from POS $creditAuthXml")
    public void creditAuthorizationExternalRequest(String creditAuthXml){
        creditAuthResp = creditAuthorizationResource
                            .authorizeExtCredit(creditAuthXml);
    }
    
    @When("request to $status the transaction: txid{$txid}, txdate{$txdate}," +
            " posTermId{$postermid}, storeId{$storeid}")
    public void lockOrReleaseExtCredit(String status, String txID,
            String txDate, String posTermID, String storeID){
        boolean toLock = (status.equals("lock")) ? true: false;
        resultBase = creditAuthorizationResource.lockExtCredit(toLock, storeID,
                posTermID, txID, txDate);    
    }
    
    @Then("the transaction status should be $expected")
    public void testResultBase(String expected){
        assertEquals(0, resultBase.getNCRWSSResultCode());        
    }
    
    @When("pastelport reply {$approval} approvalcode of parameters:"
            + " $strmsgjson, {$storeid}, {$posterminalid},"
            + " {$txdatetime}, {$txid}")
    public void pastelPortReplyNoApprovalCode(String approvalcode,
            String strmsgjson, String companyid, String storeid,
            String postermid, String datetime, String txid){
        ppResp = new PastelPortResp();
        ppResp.setApprovalcode(approvalcode.equalsIgnoreCase("empty") ? "" : 
            (approvalcode.equalsIgnoreCase("null") ? null : approvalcode));
        
        //Mock AuthorizeWithPastalPort methods
        ca = Mockito.spy(creditAuthorizationResource);
        Mockito.doReturn(ppResp).when(ca)
            .authorizeCreditWithPastelPort(strmsgjson, companyid, storeid);
        
        pastelPortResp = ca.authorizeExtCreditToPastelPort(strmsgjson, companyid,
                storeid, postermid, datetime, txid);        
    }
    
    @Given("a PastelPortPortResp")
    public void pastelPortResp(){
        ppResp = new PastelPortResp();
        ppResp.setCreditcompanycode("12");
        ppResp.setCreditcompanyname("Master_äCäOî≠çs");
        ppResp.setPaymentmethod("10");
        ppResp.setApprovalcode("7654321");
        ppResp.setCreditstatus("07");
        ppResp.setTracenum("123456");
        ppResp.setPan4("************3117");
        ppResp.setExpdate("1111");
    }
    
    @When("logging of external CA returns {$status}")
    public void pastelPortReplyApprovalCode(String status) throws DaoException{
        pastelPortResp = new PastelPortResp();        
        //Dummy data
        String strmsgjson = "{\"carddata\":\"GYt5MpNtqMEqS7FPMXyahnywBwMEMQA2"
            + "3LDB8kQNq9s69xq8gD2mrPYKqYJG2vhvcmgI9dmgur2pFobFuFywdA==\"}";
        String companyid = "01";
        String storeid = "000031";
        String postermid = "1111";
        String datetime = "20120604190401";
        String txid = "0001";
        //Dummy Response
        CreditAuthorizationResp caResp = new CreditAuthorizationResp();
        caResp.setStatus(status);
        //Mocking
        CreditAuthMsgLogger caMsgLogMock = Mockito.spy(caMsgLog);
        ca.setCreditAuthMsgLogger(caMsgLogMock);
        Mockito.doReturn(ppResp)
            .when(ca).authorizeCreditWithPastelPort(strmsgjson, companyid, storeid);        
        Mockito.doReturn(caResp).when(caMsgLogMock).logExtCA(ppResp,
                storeid, postermid, datetime, txid);
        //Call
        pastelPortResp = ca.authorizeExtCreditToPastelPort(strmsgjson, companyid, storeid,
                postermid, datetime, txid);    
    }
        
    @When("logging of external CA for parameters: $strmsgjson, {$storeid},"
            + " {$posterminalid}, {$txdatetime}, {$txid}")
    public void loggingExtCAToDB(String strmsgjson, String companyid, String storeid,
            String postermid, String datetime, String txid) throws DaoException{
        Mockito.doReturn(ppResp).when(ca)
               .authorizeCreditWithPastelPort(strmsgjson, companyid, storeid);
        pastelPortResp = ca.authorizeExtCreditToPastelPort(strmsgjson, companyid,
                storeid, postermid, datetime, txid);    
    }
    
    @When("pastelport reply {$status} approvalcode for manual entry")
    public void approvalCodeIsNull(String approvalcode){
        //Dummy data
        String strmsgjson = "{\"carddata\":\"GYt5MpNtqMEqS7FPMXyahnywBwMEMQA23"
            + "LDB8kQNq9s69xq8gD2mrPYKqYJG2vhvcmgI9dmgur2pFobFuFywdA==\"}";
        String companyid = "01";
        String storeid = "000031";
        String postermid = "1111";
        String datetime = "20120604190401";
        String txid = "0001";
        String operatorcode = "1111";
        //Mock
        Mockito.reset(ca);
        ca = Mockito.spy(creditAuthorizationResource);
        //Response
        ppResp = new PastelPortResp();
        ppResp.setApprovalcode(approvalcode.equalsIgnoreCase("empty") ? "" : 
            (approvalcode.equalsIgnoreCase("null") ? null : approvalcode));
        Mockito.doReturn(ppResp).when(ca)
                .authorizeCreditWithPastelPort(strmsgjson, companyid, storeid);
        //Call
        pastelPortResp = ca.manualAuthorizeCreditToPastelPort(strmsgjson, companyid,
                storeid, postermid, datetime, txid, operatorcode);    
    }
    
    @When("uploading of external CA returns {$status}")
    public void uploadingExternalCA(String status) throws DaoException{
        //Dummy data
        String strmsgjson = "{\"carddata\":\"GYt5MpNtqMEqS7FPMXyahnywBwMEM"
            + "QA23LDB8kQNq9s69xq8gD2mrPYKqYJG2vhvcmgI9dmgur2pFobFuFywdA==\"}";
        String companyid = "01";
        String storeid = "000031";
        String postermid = "1111";
        String datetime = "20120604190401";
        String txid = "0001";
        String operatorcode = "1111";
        //Dummy Response
        PosLogResp poslogResp = new PosLogResp();
        poslogResp.setStatus(status);
        //Mocking
        CreditAuthAgencyResource caAgencyResMock = Mockito.spy(caAgencyRes);
        ca.setCaAgencyRes(caAgencyResMock);
        Mockito.doReturn(ppResp).when(ca)
            .authorizeCreditWithPastelPort(strmsgjson, companyid, storeid);        
        Mockito.doReturn(poslogResp).when(caAgencyResMock)
            .uploadExtCARequest(pastelPortResp, storeid, postermid,
                    datetime, txid, operatorcode);
        //Call
        pastelPortResp = ca.manualAuthorizeCreditToPastelPort(strmsgjson, companyid, 
                storeid, postermid, datetime, txid, operatorcode);    
    }
    
    @When("uploading of external CA for parameters: $strmsgjson, {$storeid},"
            + " {$posterminalid}, {$txdatetime}, {$txid}, {$operatorcode}")
    public void uploadingExtCAToDB(String strmsgjson, String companyid, String storeid,
            String postermid, String datetime, String txid, String operatorcode)
    throws DaoException{
        ppResp.setAmount("12000");
        Mockito.doReturn(ppResp)
               .when(ca).authorizeCreditWithPastelPort(strmsgjson, companyid, storeid);
        pastelPortResp = ca.manualAuthorizeCreditToPastelPort(strmsgjson, companyid,
                storeid, postermid, datetime, txid, operatorcode);    
    }
    
    @Then("I should get a pastelport response with resultcode"
            + " equal to $ncrwssresultcode")
    public void shouldGetPastelPortResponse(String ncrwssresultcode){
        assertNotNull(pastelPortResp);
        assertEquals(Integer.parseInt(ncrwssresultcode),
                pastelPortResp.getNCRWSSResultCode());
    }
}
