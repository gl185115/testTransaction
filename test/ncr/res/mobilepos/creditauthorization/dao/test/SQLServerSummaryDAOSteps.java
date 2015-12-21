package ncr.res.mobilepos.creditauthorization.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.creditauthorization.dao.SQLServerSummaryDAO;
import ncr.res.mobilepos.creditauthorization
          .model.TxuNpsPastelPortCreditSummary;
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

public class SQLServerSummaryDAOSteps extends Steps {
    private SQLServerSummaryDAO sqlServerSummaryDAO;
    private boolean result;
    private String dataSetFile =
      "test/ncr/res/mobilepos/creditauthorization/dao/test/credit_dataset.xml"; 
    
    @BeforeScenario
    public void setUp() {
        Requirements.SetUp();
        new DBInitiator("SQLServerSummaryDAOTest", dataSetFile, DATABASE.RESTransaction);        
    }
    
    @AfterScenario
    public void TearDown()
    throws DatabaseUnitException, SQLException, Exception{
        Requirements.TearDown();        
    }
    
    @Given("a SQLServerSummaryDAO")
    public void createSQLServerSummaryDAO() throws DaoException{
        sqlServerSummaryDAO = new SQLServerSummaryDAO();        
    }
    
    @Then("I should have a SQLServerSummaryDAO")
    public void iHaveSQLServerSummaryDAO(){
        assertNotNull(sqlServerSummaryDAO);
    }
    
    @When("I have a corpid {$corpid} , a storeid {$storeid} ,"
            + " and a termid {$termid}(select)")
    public void iHaveCorpidStoreidTermid_select(String corpid, String storeid,
            String termid) {
        try {
            CommonTx tx = new CommonTx();
            CommonTx dbtx = new CommonTx();
            tx.setFieldValue("corpid", corpid);
            tx.setFieldValue("storeid", storeid);
            tx.setFieldValue("terminalid", termid);
            result = sqlServerSummaryDAO.select(tx, dbtx);
        } catch (DaoException daoEx) {
            result = false;
        } catch (Exception ex) {
            result = false;
        }
    }
    @Then("I should get a CommonTx ($expected)")
    public void testSelect(String expected){
        assertEquals(expected, String.valueOf(result));
    }
    
    @When("I have a corpid {$corpid} , a storeid {$storeid}"
            + " , and a termid {$termid}(insert)")
    public void iHaveCorpidStoreidTermid_insert(String corpid, String storeid,
            String termid) {
        try {
            CommonTx tx = new CommonTx();
            tx.setFieldValue("corpid", corpid);
            tx.setFieldValue("storeid", storeid);
            tx.setFieldValue("terminalid", termid);
            result = sqlServerSummaryDAO.insert(tx);
        } catch (DaoException daoEx) {
            result = false;
        } catch (Exception ex) {
            result = false;
        }
    }
    @Then("A record should be SAVED ($expected)")
    public void testInsert(String expected){
        assertEquals(expected, String.valueOf(result));
    }
    
    @When("I have a CommonTx_updateSales Xml $xml")
    public void iHaveCommonTx_updateSales(String xml) {
        xml = this.trimString(xml);
        try {
            TxuNpsPastelPortCreditSummary
                txuNpsPastelPortCreditSummary = unMarshall(xml);
            CommonTx tx = new CommonTx();
            tx.setFieldValue("corpid",
                    txuNpsPastelPortCreditSummary.getCorpid());
            tx.setFieldValue("storeid",
                    txuNpsPastelPortCreditSummary.getStoreid());
            tx.setFieldValue("terminalid",
                    txuNpsPastelPortCreditSummary.getTermid());
            
            tx.setFieldValue("salesamount",
                    txuNpsPastelPortCreditSummary.getSalesamount());
            tx.setFieldValue("salescount",
                    txuNpsPastelPortCreditSummary.getSalescount());
            tx.setFieldValue("voidamount",
                    txuNpsPastelPortCreditSummary.getVoidamount());
            tx.setFieldValue("voidcount",
                    txuNpsPastelPortCreditSummary.getVoidcount());
            tx.setFieldValue("refundamount",
                    txuNpsPastelPortCreditSummary.getRefundamount());
            tx.setFieldValue("refundcount",
                    txuNpsPastelPortCreditSummary.getRefundcount());
            tx.setFieldValue("cancelamount",
                    txuNpsPastelPortCreditSummary.getCancelamount());
            tx.setFieldValue("cancelcount",
                    txuNpsPastelPortCreditSummary.getCancelcount());
            
            result = sqlServerSummaryDAO.updateSales(tx);
        } catch (DaoException daoEx) {
            result = false;
        } catch (Exception ex) {
                ex.printStackTrace();
            result = false;
        }
    }
    @Then("A record should be UPDATE by CommonTx_updateSales ($expected)")
    public void testUpdateSales(String expected){
        assertEquals(expected, String.valueOf(result));
    }
    
    
    @When("I have a CommonTx_updateInit Xml $xml")
    public void iHaveCommonTx_updateInit(String xml) {
        xml = this.trimString(xml);
        try {
            TxuNpsPastelPortCreditSummary
                txuNpsPastelPortCreditSummary = unMarshall(xml);
            CommonTx tx = new CommonTx();
            tx.setFieldValue("corpid",
                    txuNpsPastelPortCreditSummary.getCorpid());
            tx.setFieldValue("storeid",
                    txuNpsPastelPortCreditSummary.getStoreid());
            tx.setFieldValue("terminalid",
                    txuNpsPastelPortCreditSummary.getTermid());            
            tx.setFieldValue("finishdate",
                    txuNpsPastelPortCreditSummary.getFinishdate());
            tx.setFieldValue("finishpaymentseq",
                    txuNpsPastelPortCreditSummary.getFinishpaymentseq());
            result = sqlServerSummaryDAO.updateInit(tx);
        } catch (DaoException daoEx) {
            daoEx.printStackTrace();
            result = false;
        } catch (Exception ex) {
                ex.printStackTrace();
            result = false;
        }
    }
    @Then("A record should be UPDATE by CommonTx_updateInit ($expected)")
    public void testUpdateInit(String expected){
        assertEquals(expected, String.valueOf(result));
    }    
    
    
    @When("I have a CommonTx_updateFinish Xml $xml")
    public void iHaveCommonTx_updateFinish(String xml) {
        xml = this.trimString(xml);
        try {
            TxuNpsPastelPortCreditSummary txuNpsPastelPortCreditSummary =
                unMarshall(xml);
            CommonTx tx = new CommonTx();
            tx.setFieldValue("corpid",
                    txuNpsPastelPortCreditSummary.getCorpid());
            tx.setFieldValue("storeid",
                    txuNpsPastelPortCreditSummary.getStoreid());
            tx.setFieldValue("terminalid",
                    txuNpsPastelPortCreditSummary.getTermid());            
            tx.setFieldValue("finishdate",
                    txuNpsPastelPortCreditSummary.getFinishdate());            
            tx.setFieldValue("finishpaymentseq",
                    txuNpsPastelPortCreditSummary.getFinishpaymentseq());
            
            tx.setFieldValue("totalsubtractamount",
                    txuNpsPastelPortCreditSummary.getPossalesamount());
            tx.setFieldValue("totalsubtractcount",
                    txuNpsPastelPortCreditSummary.getPossalescount());
            tx.setFieldValue("totalvoidamount",
                    txuNpsPastelPortCreditSummary.getPosvoidamount());
            tx.setFieldValue("totalvoidcount",
                    txuNpsPastelPortCreditSummary.getPosvoidcount());
            tx.setFieldValue("totalrefundamount",
                    txuNpsPastelPortCreditSummary.getPosrefundamount());
            tx.setFieldValue("totalrefundcount",
                    txuNpsPastelPortCreditSummary.getPosrefundcount());        
            tx.setFieldValue("totalauthcancelamount",
                    txuNpsPastelPortCreditSummary.getPoscancelamount());
            tx.setFieldValue("totalauthcancelcount",
                    txuNpsPastelPortCreditSummary.getPoscancelcount());

            result = sqlServerSummaryDAO.updateFinish(tx);
        } catch (DaoException daoEx) {
            result = false;
        } catch (Exception ex) {
                ex.printStackTrace();
            result = false;
        }
    }
    @Then("A record should be UPDATE by CommonTx_updateFinish ($expected)")
    public void testUpdateFinish(String expected){
        assertEquals(expected, String.valueOf(result));
    }

    private TxuNpsPastelPortCreditSummary unMarshall(String xml)
    throws JAXBException{
        XmlSerializer<TxuNpsPastelPortCreditSummary> serializer =
            new XmlSerializer<TxuNpsPastelPortCreditSummary>();
        TxuNpsPastelPortCreditSummary creditAuth =
            serializer.unMarshallXml(xml, TxuNpsPastelPortCreditSummary.class);
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
