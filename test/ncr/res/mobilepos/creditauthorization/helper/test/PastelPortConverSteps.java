package ncr.res.mobilepos.creditauthorization.helper.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.IOException;

import ncr.res.mobilepos.creditauthorization.helper.PastelPortConvert;
import ncr.res.mobilepos.creditauthorization.model.PastelPortEnv;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.pastelport.platform.CommonTx;
import ncr.res.pastelport.platform.PastelPortTxRecvImpl;
import ncr.res.pastelport.platform.PastelPortTxSendImpl;

import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

import atg.taglib.json.util.JSONException;

public class PastelPortConverSteps extends Steps {
    private PastelPortConvert portConvert;
    private CommonTx tx;
    private String teString = "";
    private PastelPortTxSendImpl pastelPort ;
    PastelPortTxRecvImpl pastelPT;


    @BeforeScenario
    public void setUp() {
        Requirements.SetUp();
        
    }
    
    public String scenarioString() {
    StringBuffer stringBuffer = new StringBuffer("{");
    stringBuffer.append("\"storeid\":\"6789\","); 
    stringBuffer.append("\"terminalid\":\"0001\","); 
    stringBuffer.append("\"txid\":\"0001\","); 
    stringBuffer.append("\"paymentseq\":\"00001\","); 
    stringBuffer.append("\"service\":\"SUBTRACT\","); 
    //stringBuffer.append("\"service\":\"TESTSUBTRACT\",");
    stringBuffer.append("\"txdatetime\":\"20080701101010\",");
    stringBuffer.append("\"amount\":\"009999999\","); 
    stringBuffer.append("\"txtype\":\"02\","); 
    stringBuffer.append("\"pan\":\"1234567890123456\","); 
    stringBuffer.append("\"expirationdate\":\"200810\","); 
    stringBuffer.append("\"carddata\":\"22222\","); 
    stringBuffer.append("\"cardinputtype\":\"1\","); 
    stringBuffer.append("\"paymentmethod\":\"10\","); 
    stringBuffer.append("\"paymentinfo\":\"\","); 
    stringBuffer.append("\"taxothers\":\"0\","); 
    //stringBuffer.append("\"approvalcode\":\"      \","); 
    stringBuffer.append("\"securityinfo\":\"000000\","); 
    //stringBuffer.append("\"jis2data\":\"0000000\","); 
    stringBuffer.append("\"pin\":\"0000\","); // 
    
    stringBuffer.append("\"alliancecode\":\"  \",");    
    stringBuffer.append("\"alliance\":\"0\",");
    stringBuffer.append("\"locationdecision\":\" \",");
    stringBuffer.append("\"responsedata\":\"0\",");
    
    stringBuffer.append("\"Jis1CardData\":"
            + "\"1234567890123456789012345678901234567\","); // jis1 data
    stringBuffer.append("\"Jis2CardData\":"
            + "\"1234567890123456789012345678901234567890\","); // jis2 data
    //Json the end
    stringBuffer.append("\"accountno\":\"0123456789\"}"); // Company


    teString = stringBuffer.toString();
    return teString;
    }

    //
    // /**
    // * {@link
    // ncr.res.mobilepos.creditauthorization.helper
    // .PastelPortConvert#PastelPortConvert
    // (ncr.res.pastelport.platform.PastelPortConfig)}
    // */
    // @AfterScenario
    // public void testPastelPortConvertPastelPortConfig() {
    // }
    @Given("a PastelPortConver")
    public void createPastelPortConvert(){
        PastelPortEnv env = new PastelPortEnv();
        env.setCompanyCode("111");
        env.setPastelPortMultiServerIP("localhost");
        env.setConnectionPortNO(8088);
        env.setHandledCompanyCode("1234567");
        env.setHandledCompanySubCode("8901");
        env.setMscedit("JIS1");
        env.setCompanyCode("12345");
        env.setTimeOut(65);
        env.setPaymentClassification("");
        env.setSignLessReceiptWord("サインレスした");
        env.setTradingUnfinishedReceiptWord("取引失敗");
        env.setRetryCount(1);
        env.setPrintPPLog(true);
        portConvert = new PastelPortConvert(env);        
    }
    /**
     * {@link ncr.res.mobilepos.creditauthorization.helper.
     * PastelPortConvert#PastelPortConvert()}
     * 
     */
    @Then("I should have a PastelPortConver")
    public void testPastelPortConvert() {
    assertNotNull(portConvert);
    }

    @Given("a CommonTx")
    public void createCommonTx() {
    tx = new CommonTx();
    }

    /**
     * {@link ncr.res.mobilepos.creditauthorization.
     * helper.PastelPortConvert#PastelPortConvert()}
     * 
     */
    @Then("I should have a CommonTx")
    public void testCommonTx() {
        teString = scenarioString();
    try {
        tx.setJsonValue(teString);
    } catch (JSONException e) {
        e.printStackTrace();
    }
    assertNotNull(tx);
    // assert false : "";
    }

    /**
     * {@link ncr.res.mobilepos.creditauthorization.helper.
     * PastelPortConvert#convertSendTx(ncr.res.pastelport.platform.CommonTx)}
     * 
     */
    @Then("I test a ConvertSendTx")
    public void testConvertSendTx() {
        
    try {
        pastelPort =  (PastelPortTxSendImpl)portConvert.convertSendTx(tx);
    } catch (JSONException e) {
        // TODO Auto-generated catch block
        e.printStackTrace();
    }
    }
    
    @Then("I should get a PastelPortTxSendImpl:{$strMsg}")
    public void testPastelPortTxSendImpl(String strMsg){
        assertEquals(strMsg, pastelPort.toString());
    
    }



    @When("I have a TEST SERVICE & 6 digits stroid:")
    public void testTestService() throws JSONException {
        tx.setFieldValue("service", "TEST");
        tx.setFieldValue("stroid","123456" );
        try {
            pastelPort =  (PastelPortTxSendImpl)portConvert.convertSendTx(tx);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
  @Then("I should get a TESTService PastelPortTxSendImpl:{$strMsg}")
    public void testTESTServicePastelPortTxSendImpl(String strMsg){
        assertEquals(strMsg, pastelPort.toString());
    
    }
    @When("I have a txtype is 98 & 4 digits expirationdate:")
    public void testTestTxtype() throws JSONException {
        tx.setFieldValue("txtype", "98");
        tx.setFieldValue("expirationdate","0810" );
        try {
            pastelPort =  (PastelPortTxSendImpl)portConvert.convertSendTx(tx);
        } catch (JSONException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
   
    @Then("I should get a Txtype PastelPortTxSendImpl")
    public void testTestTxtypePastelPortTxSendImpl(){
        assertEquals(pastelPort.getPaymentseq(),
                pastelPort.getLastpaymentseq());
        assertEquals("C3",pastelPort.getIdentification2());
        assertEquals("200810",pastelPort.getExpirationdate());
        assertEquals(null,pastelPort.getRequestcode());
    
    }
    @Then("I should TestStringToObject:{$strMsg}")
    public void testStringToObject(String strMsg) 
            throws StringIndexOutOfBoundsException, IOException{
         pastelPT =(PastelPortTxRecvImpl)portConvert
         .stringToObject(strMsg.getBytes());
    }
    @Then("I should have a PastelPortTxRecvImpl")
    public void testPastelPortTxRecvImpl(){
        assertEquals("ご利用ありがとうございました。またのご来店をお待ちしております。"
                + "                                                          "
                + "                                 ",
                pastelPT.getPrintmessage());
        assertEquals("3",pastelPT.getApplicationerrorflag());
        assertEquals("0",pastelPT.getResultflag());
        assertEquals("1",pastelPT.getCancelnoticeflag());
        assertEquals("0000000000002",pastelPT.getPaymentseq());
    }
    @When("I have a Error String:{$strMsg}")
    public void testErrorStringToObject (String strMsg)
    throws StringIndexOutOfBoundsException, IOException{
        try{
        pastelPT =  (PastelPortTxRecvImpl)portConvert
                        .stringToObject(strMsg.getBytes());
        }catch(IOException ioE){
            ioE.printStackTrace();
        }
    }
  
}
