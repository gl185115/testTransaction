package ncr.res.mobilepos.creditauthorization.check.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;

import javax.xml.bind.JAXBException;



import ncr.res.mobilepos.creditauthorization.check.VoidCheck;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.creditauthorization.model.TxuNpsPastelPortLog;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.pastelport.platform.CommonTx;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

import atg.taglib.json.util.JSONException;

public class VoidCheckSteps extends Steps {
    public VoidCheck voidCheck;
    CommonTx tx ;
    public PastelPortResp ppResp;
    private String dataSetFile = "test/ncr/res/mobilepos/creditauthorization/"
        + "check/test/credit_authorization_dataset.xml";

    @BeforeScenario
    public void SetUpClass()
    {
    Requirements.SetUp();
    new DBInitiator("VoidCheckSteps", dataSetFile, DATABASE.RESTransaction);
    }

    @AfterScenario
    public void TearDown()
    {
    Requirements.TearDown();
    }
    @Given("I Have A VoidVheck")
    public void IHaveAVoidCheck() throws DaoException{
    voidCheck = new VoidCheck();
    }

    @Then("I should have a VoidCheck")
    public void iHaveVoidVheck(){
    assertNotNull(voidCheck);
    }

    @When("I have a CommonTx: $xml")
    public void iHaveACommon(String xml)
    throws Exception{
    TxuNpsPastelPortLog txuNpsPastelPortLog = unMarshall(xml);

     tx = new CommonTx();
    tx.setFieldValue("voidcorpid", txuNpsPastelPortLog.getVoidcorpid());
    tx.setFieldValue("voidstoreid", txuNpsPastelPortLog.getVoidstoreid());
    tx.setFieldValue("voidterminalid", txuNpsPastelPortLog.getVoidtermid());
    tx.setFieldValue("voidtxid", txuNpsPastelPortLog.getVoidtxid());
    tx.setFieldValue("paymentmethod", txuNpsPastelPortLog.getPaymentmethod());
    tx.setFieldValue("voidtxdatetime", txuNpsPastelPortLog.getVoidtxdate());
    tx.setFieldValue("txtype", txuNpsPastelPortLog.getTxtype());
    tx.setFieldValue("txdatetime", txuNpsPastelPortLog.getTxdate());
    tx.setFieldValue("amount", txuNpsPastelPortLog.getAmount());
    tx.setFieldValue("crcompanycode", txuNpsPastelPortLog.getCrcompanycode());
    
    ppResp = new PastelPortResp();
    voidCheck.voidcheck(tx, ppResp);
    }

    @Then("I should get a PastelPortResp: $expected")
    public void testVoidCheck(ExamplesTable expected) throws JSONException{
    Map<String, String> row = expected.getRows().get(0);
    PastelPortResp expectedResp = new PastelPortResp();
    String voidpay = null;
    if("null".equals(row.get("errorcode"))){
    	expectedResp.setErrorcode(null);
    }else{
    expectedResp.setErrorcode(row.get("errorcode"));
    }
    if("null".equals(row.get("errormessage"))){
    	 expectedResp.setErrormessage(null);
    }else{
    	 expectedResp.setErrormessage(row.get("errormessage"));
    }
    if("null".equals(row.get("creditstatus"))){
    	expectedResp.setCreditstatus(null);
   }else{
	   expectedResp.setCreditstatus(row.get("creditstatus"));
   }
    if(row.get("voidpaymentseq")!=null){
    	voidpay=row.get("voidpaymentseq");
    	assertEquals(tx.getFieldValue("voidpaymentseq"),voidpay);
    }
    
    assertEquals(expectedResp.getErrorcode(),ppResp.getErrorcode());
    assertEquals(expectedResp.getErrormessage(),ppResp.getErrormessage());
    assertEquals(expectedResp.getCreditstatus(),ppResp.getCreditstatus());

    }

    private TxuNpsPastelPortLog unMarshall(String xml) throws JAXBException{
    XmlSerializer<TxuNpsPastelPortLog> serializer =
        new XmlSerializer<TxuNpsPastelPortLog>();
    TxuNpsPastelPortLog creditAuth =
        serializer.unMarshallXml(xml, TxuNpsPastelPortLog.class);
    return creditAuth;
    }

}
