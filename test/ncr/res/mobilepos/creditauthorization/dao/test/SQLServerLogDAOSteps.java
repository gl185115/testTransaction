package ncr.res.mobilepos.creditauthorization.dao.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.creditauthorization.dao.SQLServerLogDAO;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.creditauthorization.model.TxuNpsPastelPortLog;
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

public class SQLServerLogDAOSteps extends Steps {
    private SQLServerLogDAO sqlServerLogDAO;
    private boolean result;
    private String dataSetFile =
       "test/ncr/res/mobilepos/creditauthorization/dao/test/credit_dataset.xml";

    @BeforeScenario
    public void setUp() {
        Requirements.SetUp();
        new DBInitiator("SQLServerLogDAOSteps", dataSetFile, DATABASE.RESTransaction);
    }

    @AfterScenario
    public void TearDown() throws DatabaseUnitException,
    SQLException, Exception{
        Requirements.TearDown();
    }

    @Given("a SQLServerLogDAO")
    public void createSQLServerLogDAO() throws DaoException{
        sqlServerLogDAO = new SQLServerLogDAO();
    }

    @Then("I should have a SQLServerLogDAO")
    public void iHaveSQLLogDAO(){
        assertNotNull(sqlServerLogDAO);
    }

    @When("I have a TxuNpsPastelPortLog_PKXml $xml")
    public void iHaveTxuNpsPastelPortLog_PKXml(String xml) {
        xml = this.trimString(xml);
        try {
            TxuNpsPastelPortLog txuNpsPastelPortLog = unMarshall(xml);

            CommonTx tx = new CommonTx();
            CommonTx dbtx = new CommonTx();
            tx.setFieldValue("storeid", txuNpsPastelPortLog.getStoreid());
            tx.setFieldValue("terminalid", txuNpsPastelPortLog.getTermid());
            tx.setFieldValue("paymentseq", txuNpsPastelPortLog.getPaymentseq());
            tx.setFieldValue("txdatetime", txuNpsPastelPortLog.getTxdate());

            result = sqlServerLogDAO.select(tx, dbtx);
        } catch (DaoException daoEx) {
            daoEx.printStackTrace();
            result = false;
        } catch (Exception ex) {
            result = false;
        }
    }
    @Then("I should get a CommonTx ($expected)")
    public void testSelect(String expected){
        assertEquals(expected, String.valueOf(result));
    }

    @When("I have a CommonTx_InsertXml $xml")
    public void iHaveCommonTx_InsertXml(String xml) {
        xml = this.trimString(xml);
        try {
            TxuNpsPastelPortLog txuNpsPastelPortLog = unMarshall(xml);
            CommonTx tx = new CommonTx();

            tx.setFieldValue("corpid", txuNpsPastelPortLog.getCorpid());
            tx.setFieldValue("storeid", txuNpsPastelPortLog.getStoreid());
            tx.setFieldValue("terminalid", txuNpsPastelPortLog.getTermid());
            tx.setFieldValue("paymentseq", txuNpsPastelPortLog.getPaymentseq());
            tx.setFieldValue("txdatetime", txuNpsPastelPortLog.getTxdate());
            tx.setFieldValue("txid", txuNpsPastelPortLog.getTxid());
            tx.setFieldValue("brand", txuNpsPastelPortLog.getBrand());
            tx.setFieldValue("service", txuNpsPastelPortLog.getService());
            tx.setFieldValue("txtype", txuNpsPastelPortLog.getTxtype());
            tx.setFieldValue("crcompanycode",
                    txuNpsPastelPortLog.getCrcompanycode());
            tx.setFieldValue("recvcompanycode",
                    txuNpsPastelPortLog.getRecvcompanycode());
            tx.setFieldValue("paymentmethod",
                    txuNpsPastelPortLog.getPaymentmethod());
            tx.setFieldValue("pan4",
                    txuNpsPastelPortLog.getPan());
            tx.setFieldValue("expirationdate",
                    txuNpsPastelPortLog.getExpdate());
            tx.setFieldValue("amount", txuNpsPastelPortLog.getAmount());
            tx.setFieldValue("settlementnum",
                    txuNpsPastelPortLog.getSettlementnum());
            result = sqlServerLogDAO.insert(tx);
        } catch (DaoException daoEx) {
            daoEx.printStackTrace();
            result = false;
        } catch (Exception ex) {
            result = false;
        }
    }
    @Then("It should be SAVED ($expected)")
    public void testInsert(String expected){
        assertEquals(expected, String.valueOf(result));
    }

    @When("I have a CommonTx_UpdateRcaXml $xml")
    public void iHaveCommonTx_UpdateRcaXml(String xml) {
        xml = this.trimString(xml);
        try {
            TxuNpsPastelPortLog txuNpsPastelPortLog = unMarshall(xml);
            CommonTx tx = new CommonTx();
            PastelPortResp resp = new PastelPortResp();

            tx.setFieldValue("corpid", txuNpsPastelPortLog.getCorpid());
            tx.setFieldValue("storeid", txuNpsPastelPortLog.getStoreid());
            tx.setFieldValue("terminalid", txuNpsPastelPortLog.getTermid());
            tx.setFieldValue("paymentseq", txuNpsPastelPortLog.getPaymentseq());
            tx.setFieldValue("txdatetime", txuNpsPastelPortLog.getTxdate());
            tx.setFieldValue("txstatus", txuNpsPastelPortLog.getTxstatus());
            resp.setCreditstatus(txuNpsPastelPortLog.getCastatus());
            resp.setErrorcode(txuNpsPastelPortLog.getCaerrorcode());
            resp.setApprovalcode(txuNpsPastelPortLog.getApprovalcode());
            resp.setTracenum(txuNpsPastelPortLog.getTracenum());
            resp.setCancelnoticeflag("1");

            result = sqlServerLogDAO.updateRca(tx, resp);
        } catch (DaoException daoEx) {
            daoEx.printStackTrace();
            result = false;
        } catch (Exception ex) {
            result = false;
            ex.printStackTrace();
        }
    }
    @Then("It should be UPDATE by updateRca ($expected)")
    public void testUpdateRca(String expected){
        assertEquals(expected, String.valueOf(result));
    }

    @When("I have a txtype {$txtype}, a CommonTx_UpdateSdcXml $xml")
    public void iHaveCommonTx_UpdateSdcXml(String txtype, String xml) {
        xml = this.trimString(xml);
        try {
            TxuNpsPastelPortLog txuNpsPastelPortLog = unMarshall(xml);
            CommonTx tx = new CommonTx();

            tx.setFieldValue("txtype", txtype);
            tx.setFieldValue("corpid", txuNpsPastelPortLog.getCorpid());
            tx.setFieldValue("storeid", txuNpsPastelPortLog.getStoreid());
            tx.setFieldValue("terminalid", txuNpsPastelPortLog.getTermid());
            tx.setFieldValue("paymentseq", txuNpsPastelPortLog.getPaymentseq());
            tx.setFieldValue("txdatetime", txuNpsPastelPortLog.getTxdate());

            result = sqlServerLogDAO.updateSdc(tx);
        } catch (DaoException daoEx) {
            daoEx.printStackTrace();
            result = false;
        } catch (Exception ex) {
            result = false;
        }
    }
    @Then("It should be UPDATE by updateSdc ($expected)")
    public void testUpdateSdc(String expected){
        assertEquals(expected, String.valueOf(result));
    }

    @When("I have a CommonTx_UpdateRdcXml $xml")
    public void iHaveCommonTx_UpdateRdcXml(String xml) {
        xml = this.trimString(xml);
        try {
            TxuNpsPastelPortLog txuNpsPastelPortLog = unMarshall(xml);
            CommonTx tx = new CommonTx();
            PastelPortResp resp = new PastelPortResp();

            tx.setFieldValue("corpid", txuNpsPastelPortLog.getCorpid());
            tx.setFieldValue("storeid", txuNpsPastelPortLog.getStoreid());
            tx.setFieldValue("terminalid", txuNpsPastelPortLog.getTermid());
            tx.setFieldValue("paymentseq", txuNpsPastelPortLog.getPaymentseq());
            tx.setFieldValue("txdatetime", txuNpsPastelPortLog.getTxdate());
            tx.setFieldValue("txstatus", txuNpsPastelPortLog.getTxstatus());
            tx.setFieldValue("alertflag", txuNpsPastelPortLog.getAlertflag());
            resp.setCreditstatus(txuNpsPastelPortLog.getDcstatus());
            resp.setErrorcode(txuNpsPastelPortLog.getDcerrorcode());
            resp.setApprovalcode(txuNpsPastelPortLog.getApprovalcode());
            resp.setTracenum(txuNpsPastelPortLog.getTracenum());

            result = sqlServerLogDAO.updateRdc(tx, resp);
        } catch (DaoException daoEx) {
            daoEx.printStackTrace();
            result = false;
        } catch (Exception ex) {
            result = false;
        }
    }
    @Then("It should be UPDATE by updateRdc ($expected)")
    public void testUpdateRdc(String expected){
        assertEquals(expected, String.valueOf(result));
    }

    @When("I have a CommonTx_UpdateVoidXml $xml")
    public void iHaveCommonTx_UpdateVoidXml(String xml) {
        xml = this.trimString(xml);
        try {
            TxuNpsPastelPortLog txuNpsPastelPortLog = unMarshall(xml);
            CommonTx tx = new CommonTx();

            tx.setFieldValue("corpid", txuNpsPastelPortLog.getCorpid());
            tx.setFieldValue("storeid", txuNpsPastelPortLog.getStoreid());
            tx.setFieldValue("terminalid", txuNpsPastelPortLog.getTermid());
            tx.setFieldValue("paymentseq", txuNpsPastelPortLog.getPaymentseq());
            tx.setFieldValue("txdatetime", txuNpsPastelPortLog.getTxdate());
            tx.setFieldValue("voidflag", txuNpsPastelPortLog.getVoidflag());
            tx.setFieldValue("voidcorpid", txuNpsPastelPortLog.getVoidcorpid());
            tx.setFieldValue("voidstoreid",
                    txuNpsPastelPortLog.getVoidstoreid());
            tx.setFieldValue("voidterminalid",
                    txuNpsPastelPortLog.getVoidtermid());
            tx.setFieldValue("voidpaymentseq",
                    txuNpsPastelPortLog.getVoidpaymentseq());
            tx.setFieldValue("voidtxid", txuNpsPastelPortLog.getVoidtxid());
            tx.setFieldValue("voidtxdatetime",
                    txuNpsPastelPortLog.getVoidtxdate());

            result = sqlServerLogDAO.updateVoid(tx);
        } catch (DaoException daoEx) {
            daoEx.printStackTrace();
            result = false;
        } catch (Exception ex) {
            result = false;
        }
    }
    @Then("It should be UPDATE by updateVoid ($expected)")
    public void testUpdateVoid(String expected){
        assertEquals(expected, String.valueOf(result));
    }

    @When("I have a ServerPaymentseq {$serverpaymentseq}, and"
            + " a CommonTx_UpdateServerPaymentseqXml $xml")
    public void iHaveCommonTx_UpdateVoidPaymentseqXml(
            String paymentseq, String xml) {
        xml = this.trimString(xml);
        try {
            TxuNpsPastelPortLog txuNpsPastelPortLog = unMarshall(xml);
            CommonTx tx = new CommonTx();
            tx.setFieldValue("corpid", txuNpsPastelPortLog.getCorpid());
            tx.setFieldValue("storeid", txuNpsPastelPortLog.getStoreid());
            tx.setFieldValue("terminalid", txuNpsPastelPortLog.getTermid());
            tx.setFieldValue("paymentseq", txuNpsPastelPortLog.getPaymentseq());
            tx.setFieldValue("txdatetime", txuNpsPastelPortLog.getTxdate());

            PastelPortResp resp = new PastelPortResp();
            resp.setPaymentseq(paymentseq);
            int upcn = sqlServerLogDAO.updateServerPaymentseq(tx, resp);
            if (upcn > 0) {
                result = true;
            }
        } catch (DaoException daoEx) {
            daoEx.printStackTrace();
            result = false;
        } catch (Exception ex) {
            ex.printStackTrace();
            result = false;
        }
    }
    @Then("It should be UPDATE by updateServerPaymentseq ($expected)")
    public void testUpdateVoidPaymentseq(String expected){
        assertEquals(expected, String.valueOf(result));
    }

    @When("I have a void TxuNpsPastelPortLog_PKXml $xml")
    public void iHaveVoidTxuNpsPastelPortLog_PKXml(String xml) {
        xml = this.trimString(xml);
        try {
            TxuNpsPastelPortLog txuNpsPastelPortLog = unMarshall(xml);

            CommonTx tx = new CommonTx();
            CommonTx dbtx = new CommonTx();
            tx.setFieldValue("voidcorpid", txuNpsPastelPortLog.getVoidcorpid());
            tx.setFieldValue("voidstoreid", txuNpsPastelPortLog.getVoidstoreid());
            tx.setFieldValue("voidterminalid", txuNpsPastelPortLog.getVoidtermid());
            tx.setFieldValue("voidtxid", txuNpsPastelPortLog.getVoidtxid());
            tx.setFieldValue("voidtxdatetime", txuNpsPastelPortLog.getVoidtxdate());
            tx.setFieldValue("txdatetime", txuNpsPastelPortLog.getTxdate());
            result = sqlServerLogDAO.selectVoid(tx, dbtx);
        } catch (DaoException daoEx) {
            daoEx.printStackTrace();
            result = false;
        } catch (Exception ex) {
            result = false;
        }
    }
    @Then("I should get a void CommonTx ($expected)")
    public void testVOidSelect(String expected){
        assertEquals(expected, String.valueOf(result));
    }

    private TxuNpsPastelPortLog unMarshall(String xml) throws JAXBException{
        XmlSerializer<TxuNpsPastelPortLog> serializer =
            new XmlSerializer<TxuNpsPastelPortLog>();
        TxuNpsPastelPortLog creditAuth =
            serializer.unMarshallXml(xml, TxuNpsPastelPortLog.class);
        return creditAuth;
    }

    private String trimString(String paramStr){
        String trimStr = paramStr.replaceAll("\n","").replaceAll("\r", "")
                            .replaceAll("\t", ""); //remove newline,return,tab
        return trimStr;
    }

}
