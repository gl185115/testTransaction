package ncr.res.mobilepos.creditauthorization.helper.test;


import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import java.util.Map;
import ncr.res.mobilepos.creditauthorization.helper.PastelPortRestore;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.pastelport.platform.PastelPortTxRecvImpl;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import atg.taglib.json.util.JSONException;

public class PastelPortRestoreSteps extends Steps {
    PastelPortRestore ppRestore;
    PastelPortTxRecvImpl cfstx;

    PastelPortResp resp ;

    @BeforeScenario
    public void SetUpClass()
    {
    Requirements.SetUp();
    }

    @AfterScenario
    public void TearDown()
    {
    Requirements.TearDown();
    }

    @Given("a PastelPortRestore")
    public void iHaveAPastelPortRestore(){
    ppRestore = new PastelPortRestore();
    }

    @Then("I should have a PastelPortRestore")
    public void iHavePastelPortRestore(){
    assertNotNull(ppRestore);
    }

    @When("I have a PastelPortTxRecvImpl: $expected")
    public void iHavePastelPortTxRecvImpl(ExamplesTable expected)
    throws JSONException{

        setRespDate(expected);


    resp = new PastelPortResp();
    ppRestore.restore(cfstx,  resp);

    }
    @Then("I should get a commonresp: $expected")
    public void testGetCommonresp(ExamplesTable expected){

        PastelPortResp respExpacted=setExpactedDate(expected);
    // RES 3.1の仕様「返送データの「tillid」タグを削除。」から、テスト対応修正した。
    //assertEquals(respExpacted.getTillid(), resp.getTillid());
    assertEquals(respExpacted.getResultCode(), resp.getResultCode());
    assertEquals(respExpacted.getCreditstatus(),resp.getCreditstatus());
    assertEquals(respExpacted.getExtendedResultCode(),
            resp.getExtendedResultCode());

    assertEquals(respExpacted.getPaymentseq(),resp.getPaymentseq());
    assertEquals(respExpacted.getStatus(), resp.getStatus());
    assertEquals(respExpacted.getErrorcode(), resp.getErrorcode());
    assertEquals(respExpacted.getErrormessage(), resp.getErrormessage());
    assertEquals(respExpacted.getApprovalcode(), resp.getApprovalcode());
    assertEquals(respExpacted.getMessage(),resp.getMessage());
    assertEquals(respExpacted.getCancelnoticeflag(),resp.getCancelnoticeflag());
    assertEquals(respExpacted.getTracenum(), resp.getTracenum());

    }


    @When("I have a PastelPortTxRecvImplErrorCodeAllSpace: $expected")
    public void iHavePastelPortTxRecvImplErrorCodeSapce(ExamplesTable expected)
    throws JSONException{

        setRespDate(expected);

    resp = new PastelPortResp();
    ppRestore.restore(cfstx,  resp);

    }

    @Then("I should get a PastelPortResp: $expected")
    public void testPastelPortResp(ExamplesTable expected){

        PastelPortResp respExpacted=setExpactedDate(expected);

    //assertEquals(respExpacted.getTillid(), resp.getTillid());
    assertEquals(respExpacted.getResultCode(), resp.getResultCode());
    assertEquals(respExpacted.getCreditstatus(),resp.getCreditstatus());
    assertEquals(respExpacted.getExtendedResultCode(),
            resp.getExtendedResultCode());

    assertEquals(respExpacted.getPaymentseq(),resp.getPaymentseq());
    assertEquals(respExpacted.getStatus(), resp.getStatus());
    assertEquals(respExpacted.getErrorcode(), resp.getErrorcode());
    assertEquals(respExpacted.getErrormessage(), resp.getErrormessage());
    assertEquals(respExpacted.getApprovalcode(), resp.getApprovalcode());
    assertEquals(respExpacted.getMessage(),resp.getMessage());
    assertEquals(respExpacted.getCancelnoticeflag(),resp.getCancelnoticeflag());
    assertEquals(respExpacted.getTracenum(), resp.getTracenum());

    }

    @When("I have a PastelPortTxRecvImplErrorCodeG300: $expected")
    public void iHavePastelPortTxRecvImplErrorCodeG300(ExamplesTable expected)
    throws JSONException{

        setRespDate(expected);


    resp = new PastelPortResp();
    ppRestore.restore(cfstx,  resp);

    }

    @Then("I should get a PastelPortRespErrorCodeG300: $expected")
    public void testPastelPortRespG300(ExamplesTable expected){

        PastelPortResp respExpacted=setExpactedDate(expected);

    //assertEquals(respExpacted.getTillid(), resp.getTillid());
    assertEquals(respExpacted.getResultCode(), resp.getResultCode());
    assertEquals(respExpacted.getCreditstatus(),resp.getCreditstatus());
    assertEquals(respExpacted.getExtendedResultCode(),
            resp.getExtendedResultCode());

    assertEquals(respExpacted.getPaymentseq(),resp.getPaymentseq());
    assertEquals(respExpacted.getStatus(), resp.getStatus());
    assertEquals(respExpacted.getErrorcode(), resp.getErrorcode());
    assertEquals(respExpacted.getErrormessage(), resp.getErrormessage());
    assertEquals(respExpacted.getApprovalcode(), resp.getApprovalcode());
    assertEquals(respExpacted.getMessage(),resp.getMessage());
    assertEquals(respExpacted.getCancelnoticeflag(),resp.getCancelnoticeflag());
    assertEquals(respExpacted.getTracenum(), resp.getTracenum());

    }
    @When("I have a PastelPortTxRecvImplErrorCodeOther: $expected")
    public void iHavePastelPortTxRecvImplErrorCodeOther(ExamplesTable expected)
    throws JSONException{

        setRespDate(expected);


    resp = new PastelPortResp();
    ppRestore.restore(cfstx,  resp);

    }

    @Then("I should get a PastelPortRespErrorCodeOther: $expected")
    public void testPastelPortRespOther(ExamplesTable expected){

        PastelPortResp respExpacted=setExpactedDate(expected);

    //assertEquals(respExpacted.getTillid(), resp.getTillid());
    assertEquals(respExpacted.getResultCode(), resp.getResultCode());
    assertEquals(respExpacted.getCreditstatus(),resp.getCreditstatus());
    assertEquals(respExpacted.getExtendedResultCode(),
            resp.getExtendedResultCode());

    assertEquals(respExpacted.getPaymentseq(),resp.getPaymentseq());
    assertEquals(respExpacted.getStatus(), resp.getStatus());
    assertEquals(respExpacted.getErrorcode(), resp.getErrorcode());
    assertEquals(respExpacted.getErrormessage(), resp.getErrormessage());
    assertEquals(respExpacted.getApprovalcode(), resp.getApprovalcode());
    assertEquals(respExpacted.getMessage(),resp.getMessage());
    assertEquals(respExpacted.getCancelnoticeflag(),resp.getCancelnoticeflag());
    assertEquals(respExpacted.getTracenum(), resp.getTracenum());

    }

    @When("I have a PastelPortTxRecvImplApplicationA: $expected")
    public void iHavePastelPortApplicationA(ExamplesTable expected)
    throws JSONException{

        setRespDate(expected);


    resp = new PastelPortResp();
    ppRestore.restore(cfstx,  resp);

    }

    @Then("I should get a PastelPortRespApplicationA: $expected")
    public void testPastelPortApplicationA(ExamplesTable expected){

        PastelPortResp respExpacted=setExpactedDate(expected);

    //assertEquals(respExpacted.getTillid(), resp.getTillid());
    assertEquals(respExpacted.getResultCode(), resp.getResultCode());
    assertEquals(respExpacted.getCreditstatus(),resp.getCreditstatus());
    assertEquals(respExpacted.getExtendedResultCode(),
            resp.getExtendedResultCode());

    assertEquals(respExpacted.getPaymentseq(),resp.getPaymentseq());
    assertEquals(respExpacted.getStatus(), resp.getStatus());
    assertEquals(respExpacted.getErrorcode(), resp.getErrorcode());
    assertEquals(respExpacted.getErrormessage(), resp.getErrormessage());
    assertEquals(respExpacted.getApprovalcode(), resp.getApprovalcode());
    assertEquals(respExpacted.getMessage(),resp.getMessage());
    assertEquals(respExpacted.getCancelnoticeflag(),resp.getCancelnoticeflag());
    assertEquals(respExpacted.getTracenum(), resp.getTracenum());

    }


    @When("I have a PastelPortTxRecvImplApplication2: $expected")
    public void iHavePastelPortApplication2(ExamplesTable expected)
    throws JSONException{

        setRespDate(expected);


    resp = new PastelPortResp();
    ppRestore.restore(cfstx,  resp);

    }

    @Then("I should get a PastelPortRespApplication2: $expected")
    public void testPastelPortApplication2(ExamplesTable expected){

        PastelPortResp respExpacted=setExpactedDate(expected);

    //assertEquals(respExpacted.getTillid(), resp.getTillid());
    assertEquals(respExpacted.getResultCode(), resp.getResultCode());
    assertEquals(respExpacted.getCreditstatus(),resp.getCreditstatus());
    assertEquals(respExpacted.getExtendedResultCode(),
            resp.getExtendedResultCode());

    assertEquals(respExpacted.getPaymentseq(),resp.getPaymentseq());
    assertEquals(respExpacted.getStatus(), resp.getStatus());
    assertEquals(respExpacted.getErrorcode(), resp.getErrorcode());
    assertEquals(respExpacted.getErrormessage(), resp.getErrormessage());
    assertEquals(respExpacted.getApprovalcode(), resp.getApprovalcode());
    assertEquals(respExpacted.getMessage(),resp.getMessage());
    assertEquals(respExpacted.getCancelnoticeflag(),resp.getCancelnoticeflag());
    assertEquals(respExpacted.getTracenum(), resp.getTracenum());

    }

    @When("I have a PastelPortTxRecvImplApplicationD: $expected")
    public void iHavePastelPortApplicationD(ExamplesTable expected)
    throws JSONException{

        setRespDate(expected);


    resp = new PastelPortResp();
    ppRestore.restore(cfstx,  resp);

    }

    @Then("I should get a PastelPortRespApplicationD: $expected")
    public void testPastelPortApplicationD(ExamplesTable expected){

        PastelPortResp respExpacted=setExpactedDate(expected);

    //assertEquals(respExpacted.getTillid(), resp.getTillid());
    assertEquals(respExpacted.getResultCode(), resp.getResultCode());
    assertEquals(respExpacted.getCreditstatus(),resp.getCreditstatus());
    assertEquals(respExpacted.getExtendedResultCode(),
            resp.getExtendedResultCode());

    assertEquals(respExpacted.getPaymentseq(),resp.getPaymentseq());
    assertEquals(respExpacted.getStatus(), resp.getStatus());
    assertEquals(respExpacted.getErrorcode(), resp.getErrorcode());
    assertEquals(respExpacted.getErrormessage(), resp.getErrormessage());
    assertEquals(respExpacted.getApprovalcode(), resp.getApprovalcode());
    assertEquals(respExpacted.getMessage(),resp.getMessage());
    assertEquals(respExpacted.getCancelnoticeflag(),resp.getCancelnoticeflag());
    assertEquals(respExpacted.getTracenum(), resp.getTracenum());

    }

    @When("I have a PastelPortTxRecvImplApplicationO: $expected")
    public void iHavePastelPortApplicationO(ExamplesTable expected)
    throws JSONException{

        setRespDate(expected);


    resp = new PastelPortResp();
    ppRestore.restore(cfstx,  resp);

    }

    @Then("I should get a PastelPortRespApplicationO: $expected")
    public void testPastelPortApplicationO(ExamplesTable expected){

        PastelPortResp respExpacted=setExpactedDate(expected);

    //assertEquals(respExpacted.getTillid(), resp.getTillid());
    assertEquals(respExpacted.getResultCode(), resp.getResultCode());
    assertEquals(respExpacted.getCreditstatus(),resp.getCreditstatus());
    assertEquals(respExpacted.getExtendedResultCode(),
            resp.getExtendedResultCode());

    assertEquals(respExpacted.getPaymentseq(),resp.getPaymentseq());
    assertEquals(respExpacted.getStatus(), resp.getStatus());
    assertEquals(respExpacted.getErrorcode(), resp.getErrorcode());
    assertEquals(respExpacted.getErrormessage(), resp.getErrormessage());
    assertEquals(respExpacted.getApprovalcode(), resp.getApprovalcode());
    assertEquals(respExpacted.getMessage(),resp.getMessage());
    assertEquals(respExpacted.getCancelnoticeflag(),resp.getCancelnoticeflag());
    assertEquals(respExpacted.getTracenum(), resp.getTracenum());

    }

    @When("I have a PastelPortTxRecvImplCancelO: $expected")
    public void iHavePastelPortCancelO(ExamplesTable expected)
    throws JSONException{

        setRespDate(expected);


    resp = new PastelPortResp();
    ppRestore.restore(cfstx,  resp);

    }

    @Then("I should get a PastelPortRespCancelO: $expected")
    public void testPastelPortCancelO(ExamplesTable expected){

        PastelPortResp respExpacted=setExpactedDate(expected);

    //assertEquals(respExpacted.getTillid(), resp.getTillid());
    assertEquals(respExpacted.getResultCode(), resp.getResultCode());
    assertEquals(respExpacted.getCreditstatus(),resp.getCreditstatus());
    assertEquals(respExpacted.getExtendedResultCode(),
            resp.getExtendedResultCode());

    assertEquals(respExpacted.getPaymentseq(),resp.getPaymentseq());
    assertEquals(respExpacted.getStatus(), resp.getStatus());
    assertEquals(respExpacted.getErrorcode(), resp.getErrorcode());
    assertEquals(respExpacted.getErrormessage(), resp.getErrormessage());
    assertEquals(respExpacted.getApprovalcode(), resp.getApprovalcode());
    assertEquals(respExpacted.getMessage(),resp.getMessage());
    assertEquals(respExpacted.getCancelnoticeflag(),resp.getCancelnoticeflag());
    assertEquals(respExpacted.getTracenum(), resp.getTracenum());

    }
    private PastelPortResp setExpactedDate(ExamplesTable expected){
        Map<String, String> row = expected.getRows().get(0);
        PastelPortResp respExpacted = new PastelPortResp();
        respExpacted.setApprovalcode(row.get("approvalcode"));
        respExpacted.setCancelnoticeflag(row.get("cancelnoticeflag"));
        respExpacted.setCreditstatus(row.get("creditstatus"));
        respExpacted.setErrorcode(row.get("errorcode"));
        respExpacted.setErrormessage(row.get("errormessage"));
        respExpacted.setExtendedResultCode(row.get("extendedresultcode"));
        respExpacted.setMessage(row.get("message"));
        respExpacted.setPaymentseq(row.get("paymentseq"));
        respExpacted.setResultCode(row.get("resultcode"));
        respExpacted.setStatus(row.get("status"));
        respExpacted.setTillid(row.get("tillid"));
        respExpacted.setTracenum(row.get("tracenum"));
        return respExpacted;

    }

    private void setRespDate(ExamplesTable expected){
        Map<String, String> row = expected.getRows().get(0);
        cfstx = new PastelPortTxRecvImpl();

        cfstx.setCancelnoticeflag(row.get("cancelnoticeflag"));
        cfstx.setApplicationerrorflag(row.get("applicationerrorflag"));
        cfstx.setErrorcode(row.get("errorcode"));
        cfstx.setPaymentseq(row.get("paymentseq"));
        cfstx.setCafisseq(row.get("cafisseq"));
        cfstx.setApprovalcode(row.get("approvalcode"));

    }
}
