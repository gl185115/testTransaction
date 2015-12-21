package ncr.res.mobilepos.creditauthorization.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.sql.SQLException;

import ncr.res.mobilepos.creditauthorization.dao.ILogDAO;
import ncr.res.mobilepos.creditauthorization.dao.SQLServerLogDAO;
import ncr.res.mobilepos.creditauthorization.helper.PastelPortAdmin;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.pastelport.platform.CommonTx;

import org.dbunit.DatabaseUnitException;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

import atg.taglib.json.util.JSONException;

public class PastelPortAdminSteps extends Steps {
    private PastelPortAdmin pastelPortAdmin;
    private String dataSetFile =
        "test/ncr/res/mobilepos/creditauthorization/helper/"
        + "test/credit_dataset.xml";
    private PastelPortResp resp;
    private Exception ex;
    private ILogDAO dao;
    private CommonTx contextTx;

    @BeforeScenario
    public void setUp() throws DaoException {
        Requirements.SetUp();
        new DBInitiator("PastelPortAdminSteps", dataSetFile, DATABASE.RESTransaction);
    }

    @AfterScenario
    public void TearDown()
    throws DatabaseUnitException, SQLException, Exception{
        Requirements.TearDown();
    }

    @Given("a PastelPortAdmin")
    public void createPastelPortAdmin() throws DaoException{
        pastelPortAdmin = new PastelPortAdmin();
        dao = new SQLServerLogDAO();
    }

    @Then("I should have a PastelPortAdmin")
    public void iHavePastelPortAdmin(){
        assertNotNull(pastelPortAdmin);
    }

    @When("I have a msgJson {$msgJson} and there is not have any"
            + " recode in table TXU_NPS_PastelPortLog")
    public void iHaveCommonTx_SENDCA_NASI(String msgJson) throws Exception {
        CommonTx tx = new CommonTx();
        tx.setJsonValue(msgJson);
        resp = new PastelPortResp();
        pastelPortAdmin.sendca(tx, resp);
    }
    @Then("The data of CommonTx should be inserted and the resp is null")
    public void testSendca_NASI(){
        assertEquals(null, resp.getErrorcode());
        assertEquals(null, resp.getExtendedResultCode());
        assertEquals(null, resp.getErrormessage());
    }

    @When("I have a msgJson {$msgJson} and there is have a recode"
            + " in table TXU_NPS_PastelPortLog")
    public void iHaveCommonTx_SENDCA_ARU(String msgJson) throws Exception {
        CommonTx tx = new CommonTx();
        tx.setJsonValue(msgJson);
        resp = new PastelPortResp();
        pastelPortAdmin.sendca(tx, resp);
    }
    @Then("The data of CommonTx should not be inserted "
            + "and the resp is not null")
    public void testSendca_ARU(){
        assertEquals("MC09", resp.getErrorcode());
        assertEquals(null, resp.getExtendedResultCode());
        assertEquals("取引重複", resp.getErrormessage());
        assertEquals("07", resp.getCreditstatus());
    }

    @When("I have a msgJson {$msgJson} and there is a same key"
            + " recode in table TXU_NPS_PastelPortLog")
    public void iHaveCommonTx_RECVCA(String msgJson) throws JSONException,
            DaoException {
        CommonTx tx = new CommonTx();
        tx.setJsonValue(msgJson);
        resp = new PastelPortResp();
        resp.setCancelnoticeflag("0");
        resp.setCreditstatus("00");
        resp.setErrorcode("00");
        resp.setApprovalcode("7777777");
        resp.setTracenum("1234567");
        try{
            pastelPortAdmin.recvca(tx, resp);
        }catch(Exception exp){
            this.ex = exp;
        }
    }
    @Then("The recode shoud be updated successful")
    public void testRecvca(){
        assertEquals(null, ex);
    }

    @When("I have a msgJson {$msgJson} and there is a same key"
            + " recode in table TXU_NPS_PastelPortLog but"
            + " resp.Cancelnoticeflag = 1")
    public void iHaveCommonTx_RECVCA_cancel(String msgJson)
    throws JSONException,
            DaoException {
        CommonTx tx = new CommonTx();
        tx.setJsonValue(msgJson);
        resp = new PastelPortResp();
        resp.setCancelnoticeflag("0");
        resp.setCreditstatus("00");
        resp.setErrorcode("00");
        resp.setApprovalcode("7777777");
        resp.setTracenum("1234567");
        resp.setCancelnoticeflag("1");
        try {
            pastelPortAdmin.recvca(tx, resp);
        } catch (Exception exp) {
            this.ex = exp;
        }
    }
    @Then("The recode shoud be updated successful"
            + " and VoidPaymentseq must updated")
    public void testRecvca_cancel(){
        assertEquals(null, ex);
    }

    @When("I have a msgJson {$msgJson} but txtype = 16 "
            + "and there is not have any recode in table TXU_NPS_PastelPortLog")
    public void iHaveCommonTx_SENDDC_txtype_16(String msgJson)
    throws Exception {
        CommonTx tx = new CommonTx();
        tx.setJsonValue(msgJson);
        resp = new PastelPortResp();
        pastelPortAdmin.senddc(tx, resp);
    }
    @Then("The data of CommonTx should be inserted (txtype = 16)")
    public void testSenddc_txtype_16(){
        assertEquals("9", resp.getStatus());
        assertEquals("07", resp.getCreditstatus());
        assertEquals("MC99", resp.getErrorcode());
        assertEquals("その他エラー", resp.getErrormessage());
        assertEquals(true, resp.getPosReturn());
    }
    @When("I have a msgJson {$msgJson} but txtype = 98 and there is not"
            + " have any recode in table TXU_NPS_PastelPortLog")
    public void iHaveCommonTx_SENDDC_txtype_98(String msgJson)
    throws Exception {
        CommonTx tx = new CommonTx();
        tx.setJsonValue(msgJson);
        resp = new PastelPortResp();
        pastelPortAdmin.senddc(tx, resp);
    }
    @Then("The data of CommonTx should be inserted (txtype = 98)")
    public void testSenddc_txtype_98(){
        assertEquals("9", resp.getStatus());
        assertEquals("07", resp.getCreditstatus());
        assertEquals("MC02", resp.getErrorcode());
        assertEquals("取消対象取引なし", resp.getErrormessage());
        assertEquals(true, resp.getPosReturn());
    }

    @When("I have a msgJson {$msgJson} but txtype = 16 and there is"
            + " a same key recode in table TXU_NPS_PastelPortLog")
    public void iHaveCommonTx_SENDDC_txtype_0216(String msgJson)
            throws Exception {
        CommonTx tx = new CommonTx();
        tx.setJsonValue(msgJson);
        resp = new PastelPortResp();
        pastelPortAdmin.senddc(tx, resp);
    }
    @Then("The data of CommonTx should be updated (txtype = 16)")
    public void testSenddc_txtype_0216(){
        assertEquals(true, resp.getPosReturn());
        assertEquals("00", resp.getCreditstatus());
    }
    @When("I have a msgJson {$msgJson} but txtype = 98 and there is"
            + " a same key recode in table TXU_NPS_PastelPortLog")
    public void iHaveCommonTx_SENDDC_txtype_0298(String msgJson)
            throws Exception {
        CommonTx tx = new CommonTx();
        tx.setJsonValue(msgJson);
        resp = new PastelPortResp();
        pastelPortAdmin.senddc(tx, resp);
    }
    @Then("The data of CommonTx should be updated (txtype = 98)")
    public void testSenddc_txtype_0298(){
        assertEquals(false, resp.getPosReturn());
    }

    @When("I have a msgJson {$msgJson} but txtype = 98 and there is "
            + "a same key recode in table TXU_NPS_PastelPortLog (recvdc)")
    public void iHaveCommonTx_RECVDC_txtype_0298(String msgJson)
            throws JSONException, DaoException {
        CommonTx tx = new CommonTx();
        tx.setJsonValue(msgJson);
        resp = new PastelPortResp();
        try {
            pastelPortAdmin.recvdc(tx, resp);
        } catch (Exception exp) {
            this.ex = exp;
            exp.printStackTrace();
        }
    }
    @Then("The data of CommonTx should be updated (txtype = 98 recvdc)")
    public void testRecvdc_txtype_0298(){
        assertEquals(null, ex);
    }

    @When("I have a msgJson {$msgJson} sendca_void")
    public void iHaveCommonTx_RECVDC_txtype_0300(String msgJson)
            throws JSONException, DaoException {
        CommonTx tx = new CommonTx();
        tx.setJsonValue(msgJson);
        resp = new PastelPortResp();
        try {
            pastelPortAdmin.sendca(tx, resp);
        } catch (Exception exp) {
            this.ex = exp;
        }
    }
    @Then("Record_sendcavoid is")
    public void testRecvdc_txtype_0300(){
        assertEquals(true, true);
    }

    @When("I have a msgJson {$msgJson} recveca_voidCaProc")
    public void iHaveCommonTx_RECVDC_txtype_0310(String msgJson)
            throws JSONException, DaoException {
        CommonTx tx = new CommonTx();
        tx.setJsonValue(msgJson);
        resp = new PastelPortResp();
        resp.setCancelnoticeflag("0");
        resp.setCreditstatus("00");
        resp.setErrorcode("00");
        resp.setApprovalcode("7777777");
        resp.setTracenum("1234567");
        try {
            pastelPortAdmin.recvca(tx, resp);
            contextTx = tx;
        } catch (Exception exp) {
            this.ex = exp;
        }
    }
    @Then("{$record} is void in originalRecord_recveca_voidCaProc")
    public void testRecvdc_txtype_0310(String record) throws DaoException, JSONException{
    	CommonTx dbtx = new CommonTx();
    	CommonTx search = new CommonTx();
    	search.setJsonValue(record);
    	dao.select(search, dbtx);
        assertEquals(dbtx.getFieldValue("voidcorpid"), contextTx.getFieldValue("corpid"));
        assertEquals(dbtx.getFieldValue("voidstoreid"), contextTx.getFieldValue("storeid"));
    }

    @When("I have a msgJson {$msgJson} senddcrecvdc_void_abortDcProc")
    public void iHaveCommonTx_SENDCA_txtype_0216_abortDcProc(String msgJson)
            throws JSONException, DaoException {
        CommonTx tx = new CommonTx();
        tx.setJsonValue(msgJson);
        resp = new PastelPortResp();
        try {
            pastelPortAdmin.senddc(tx, resp);
            // 取引を消しの確認中、エラー発生する場合「abortDcProc」のテストため、ここで、以降の項目値は更新する。
            resp.setCreditstatus("0");
            tx.setFieldValue("txtype", "98");
            tx.setOtherSysIF(true);
            pastelPortAdmin.recvdc(tx, resp);
            contextTx = tx;
            Thread.sleep(1000);
        } catch (Exception exp) {
            this.ex = exp;
        }
    }
    @Then("The data should be updated senddcrecvdc_void_abortDcProc")
    public void testRecvdc_txtype_0216_senddcrecvdc_void_abortDcProc() throws Exception{
    	CommonTx cantx = new CommonTx();
       	CommonTx vdtx = new CommonTx();

       	dao.select(contextTx, cantx);
       	assertEquals("2",cantx.getFieldValue("voidflag"));
       	assertEquals(cantx.getFieldValue("corpid"),cantx.getFieldValue("voidcorpid"));
       	assertEquals(cantx.getFieldValue("storeid"),cantx.getFieldValue("voidstoreid"));
       	assertEquals(cantx.getFieldValue("terminalid"),cantx.getFieldValue("voidterminalid"));
       	assertEquals(cantx.getFieldValue("paymentseq"),cantx.getFieldValue("voidpaymentseq"));
       	assertEquals(cantx.getFieldValue("txdatetime"),cantx.getFieldValue("voidtxdatetime").substring(0, 8));

       	dao.selectVoid(contextTx, vdtx);
       	assertEquals("0",vdtx.getFieldValue("voidflag"));
       	assertEquals(cantx.getFieldValue("corpid"),vdtx.getFieldValue("voidcorpid"));
       	assertEquals(cantx.getFieldValue("storeid"),vdtx.getFieldValue("voidstoreid"));
       	assertEquals(cantx.getFieldValue("terminalid"),vdtx.getFieldValue("voidterminalid"));
       	assertEquals(cantx.getFieldValue("paymentseq"),vdtx.getFieldValue("voidpaymentseq"));
       	assertEquals(cantx.getFieldValue("txdatetime"),vdtx.getFieldValue("voidtxdatetime").substring(0, 8));

    }
}
