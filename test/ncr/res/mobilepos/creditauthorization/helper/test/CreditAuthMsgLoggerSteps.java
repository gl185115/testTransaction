/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * CreditAuthorizationResourceSteps
 *
 * Steps Class of CreditAuthMsgLogger
 *
 * De la Cerna, Jessel G.
 */

package ncr.res.mobilepos.creditauthorization.helper.test;

import static org.junit.Assert.*;

import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.creditauthorization.helper.CreditAuthMsgLogger;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorization;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorizationResp;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class CreditAuthMsgLoggerSteps extends Steps {
    private CreditAuthMsgLogger creditAuthMsgLogger;
    private CreditAuthorizationResp credAuthResp;
    private int result; 
    
    @BeforeScenario
    public void setUp() {
        Requirements.SetUp();
        new DBInitiator("CreditAuthMsgLoggerTest", 
                "test/ncr/res/mobilepos/creditauthorization/"
                + "helper/test/credit_authorization_dataset.xml", DATABASE.RESTransaction);
    }
    
    @AfterScenario
    public void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("a CreditAuthMsgLogger")
    public void createCreditAuthMsgLogger(){
        creditAuthMsgLogger = new CreditAuthMsgLogger();        
    }
    
    @Then("I should have a CreditAuthMsgLogger")
    public void testCreditAuthMsgLogger(){
        assertNotNull(creditAuthMsgLogger);
    }

    @When("I set SqlServerDAO")
    public void setSqlServerDAO(){
        creditAuthMsgLogger.setDAOFactory(
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER));
    }
    
    @Then("I should have DAOFactory")
    public void testSqlServerDAO()
    throws SecurityException, NoSuchFieldException{
        assertNotNull(creditAuthMsgLogger
                .getClass().getDeclaredField("daoFactory"));
    }
    
    @When("I have a creditAuthorizationXml $xml")
    public void iHaveCreditAuthXml(String xml)
    throws JAXBException, DaoException{
        credAuthResp = creditAuthMsgLogger.log(xml);
    }
    
    @Then("It should be $expected")
    public void testLog(String expected){
        assertEquals(expected.equals("logged")
                ? "0" : "9", credAuthResp.getStatus());
    }
    
    @When("I have an external request of creditAuthorizationXml $xml")
    public void iHaveExternalCreditAuthXml(String xml)
    throws JAXBException, DaoException{
        credAuthResp = creditAuthMsgLogger.log(xml, true);
    }
    
    @When("I request to $condition the external request transaction:"
            + " txid{$txID}, txdate{$txDate}, posTermId{$posTermID},"
            + " storeId{$storeID}")
    public void lockTransaction(String condition, String txID,
            String txDate, String posTermID, String storeID)
    throws DaoException, JAXBException{
        CreditAuthorization credAuth = new CreditAuthorization();
        credAuth.setTxid(txID);
        credAuth.setTxdatetime(txDate);
        credAuth.setTerminalid(posTermID);
        credAuth.setStoreid(storeID);         
        result =
            creditAuthMsgLogger.lock(credAuth,
                    condition.equals("lock") ? true: false);
    }
    
    @Then("the transaction status should be $expected")
    public void testTransactionStatus(String expected){
        assertEquals(expected.equals("released") ? 0 : 9, result);    
    }
}
