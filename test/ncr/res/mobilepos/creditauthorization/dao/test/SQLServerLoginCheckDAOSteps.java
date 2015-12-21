package ncr.res.mobilepos.creditauthorization.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.creditauthorization.dao.SQLServerLoginCheckDAO;
import ncr.res.mobilepos.creditauthorization.model.TxlNpsTxlog;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.pastelport.platform.CommonTx;

import org.dbunit.DatabaseUnitException;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class SQLServerLoginCheckDAOSteps extends Steps {
    private SQLServerLoginCheckDAO sqlServerLoginCheckDAO;
    private boolean result;
    private String dataSetFile =
       "test/ncr/res/mobilepos/creditauthorization/dao/test/credit_dataset.xml";
    
    @BeforeScenario
    public void setUp() {
        Requirements.SetUp();
        new DBInitiator("SQLServerLoginCheckDAOTest", dataSetFile, DATABASE.RESTransaction);        
    }
    
    @AfterScenario
    public void TearDown()
    throws DatabaseUnitException, SQLException, Exception{
        Requirements.TearDown();        
    }
    
    @Given("a SQLServerLoginCheckDAO")
    public void createSQLServerLoginCheckDAO() throws DaoException{
        sqlServerLoginCheckDAO = new SQLServerLoginCheckDAO();        
    }
    
    @Then("I should have a SQLServerLoginCheckDAO")
    public void iHaveSQLServerLoginCheckDAO(){
        assertNotNull(sqlServerLoginCheckDAO);
    }
    
    @When("I have a TxlNpsTxlog_PKXml $xml")
    public void iHaveTxlNpsTxlog_PKXml(String xml){    
        xml = this.trimString(xml);
        try{
            TxlNpsTxlog txlNpsTxlog = unMarshall(xml);

            CommonTx tx = new CommonTx();
            tx.setFieldValue("corpid", txlNpsTxlog.getCorpid());
            tx.setFieldValue("storeid", txlNpsTxlog.getStoreid());
            tx.setFieldValue("terminalid", txlNpsTxlog.getTermid());
            tx.setFieldValue("txid", txlNpsTxlog.getTxid());
            tx.setFieldValue("paymentseq", txlNpsTxlog.getPaymentseq());
            tx.setFieldValue("guid", txlNpsTxlog.getGuid());
            result = sqlServerLoginCheckDAO.getLoggingInfo(tx);
        }catch(JAXBException jaxbEx){
            
            jaxbEx.printStackTrace();
            result = false;
        }catch(DaoException daoEx){
            result = false;
        }catch(Exception ex){
            result = false;
            
        }
    }
    @Then("I should get a record ($expected)")
    public void testGetLoggingInfo(String expected){
        assertEquals(expected, String.valueOf(result));
    }
    
    @When("I have a TxlNpsTxlogXml $xml")
    public void iHaveTxlNpsTxlogXml(String xml){    
        xml = this.trimString(xml);
        try{
            TxlNpsTxlog txlNpsTxlog = unMarshall(xml);

            CommonTx tx = new CommonTx();
            tx.setFieldValue("corpid", txlNpsTxlog.getCorpid());
            tx.setFieldValue("storeid", txlNpsTxlog.getStoreid());
            tx.setFieldValue("terminalid", txlNpsTxlog.getTermid());
            tx.setFieldValue("txid", txlNpsTxlog.getTxid());
            tx.setFieldValue("paymentseq", txlNpsTxlog.getPaymentseq());
            tx.setFieldValue("guid", txlNpsTxlog.getGuid());
            tx.setFieldValue("brand", txlNpsTxlog.getBrand());
            tx.setFieldValue("service", txlNpsTxlog.getService());
            tx.setFieldValue("amount", txlNpsTxlog.getAmount());
            tx.setFieldValue("txdatetime", txlNpsTxlog.getTxdatetime());
            tx.setFieldValue("rwid", txlNpsTxlog.getRwid());
            tx.setFieldValue("txtype", txlNpsTxlog.getTxtype());
            tx.setFieldValue("rwkind", txlNpsTxlog.getRwkind());
            tx.setFieldValue("xmldocument", txlNpsTxlog.getXmldocument());
            
            result = sqlServerLoginCheckDAO.saveLoggingInfo(tx);
        }catch(JAXBException jaxbEx){
            
            jaxbEx.printStackTrace();
            result = false;
        }catch(DaoException daoEx){
            result = false;
        }catch(Exception ex){
            result = false;
            
        }
    }
    @Then("It should be SAVED ($expected)")
    public void testSaveLoggingInfo(String expected){
        assertEquals(expected, String.valueOf(result));
    }
    
    private TxlNpsTxlog unMarshall(String xml) throws JAXBException{
        XmlSerializer<TxlNpsTxlog> serializer =
            new XmlSerializer<TxlNpsTxlog>();
        TxlNpsTxlog creditAuth =
            serializer.unMarshallXml(xml, TxlNpsTxlog.class);
        return creditAuth;
    }

    private String trimString(String paramStr){
        String trimStr = paramStr.replaceAll("\n","")
                                 .replaceAll("\r", "")
                                 .replaceAll("\t", "");
                                 //remove newline,return,tab
        return trimStr;
    }

}
