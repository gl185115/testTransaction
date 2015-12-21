/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerCreditDAOSteps
 *
 * Steps Class of SQLServerCreditDAO
 *
 * De la Cerna, Jessel G.
 */

package ncr.res.mobilepos.creditauthorization.dao.test;

import static org.junit.Assert.*;

import java.sql.SQLException;

import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.creditauthorization.dao.SQLServerCreditDAO;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorization;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.dbunit.DatabaseUnitException;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class SQLServerCreditDAOSteps extends Steps {
    private SQLServerCreditDAO sqlServerCreditDAO;
    private int result;
    private String creditPan;
    private DBInitiator dbInit;
    private String dataSetFile =
        "test/ncr/res/mobilepos/creditauthorization/dao/test/"
        + "credit_authorization_dataset.xml"; 
    
    @BeforeScenario
    public void setUp() {
        Requirements.SetUp();
        dbInit = new DBInitiator("CreditAuthorizationDAOTest", dataSetFile, DATABASE.RESTransaction);
    }
    
    @AfterScenario
    public void TearDown()
    throws DatabaseUnitException, SQLException, Exception{
        Requirements.TearDown();        
    }
    
    @Given("a SQLServerCreditDAO")
    public void createSQLServerCreditDAO() throws DaoException{
        sqlServerCreditDAO = new SQLServerCreditDAO();        
    }
    
    @Then("I should have a SQLServerCreditDAO")
    public void iHaveSQLServerCreditDAO(){
        assertNotNull(sqlServerCreditDAO);
    }
    
         
    @When("I request to $condition the external request transaction: $xml")
    public void requestToLockOrRelease(String condition, String xml) {
        boolean isToLock = condition.equals("lock") ? true: false;
        try{
            CreditAuthorization creditAuth = unMarshall(xml);
            result =
                sqlServerCreditDAO.lockOrReleaseCARequest(creditAuth, isToLock);
        }catch(JAXBException jaxbEx){
            result = 0;
        }catch(DaoException daoEx){
            result = 0;
        }catch(Exception ex){
            result = 0;
        }
    }
    
    @Then("the transaction status should $expected")
    public void testLockedOrReleased(String expected){
        int resultExp =  expected.equals("RELEASED") ? 0 : 9;
        if (expected.equals("FAILED")) {
            resultExp = -1;
        }
        assertEquals(resultExp, result);
    }
    
    private CreditAuthorization unMarshall(String xml) throws JAXBException{
        XmlSerializer<CreditAuthorization> serializer
            = new XmlSerializer<CreditAuthorization>();
        CreditAuthorization creditAuth =
            serializer.unMarshallXml(xml, CreditAuthorization.class);
        return creditAuth;
    }
    
    private String trimString(String paramStr){
        String trimStr = paramStr.replaceAll("\n","")
            .replaceAll("\r", "").replaceAll("\t", "");
                            //remove newline,return,tab
        return trimStr;
    }

}
