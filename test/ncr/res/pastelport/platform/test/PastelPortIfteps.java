package ncr.res.pastelport.platform.test;

import java.io.IOException;
import java.util.Map;

import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.creditauthorization.model.PastelPortEnv;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.pastelport.platform.CommonTx;
import ncr.res.pastelport.platform.PastelPortIf;
import ncr.res.pastelport.platform.PastelPortTxRecvBase;
import ncr.res.pastelport.platform.PastelPortTxRecvImpl;
import static org.junit.Assert.assertEquals;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

import atg.taglib.json.util.JSONException;

public class PastelPortIfteps extends Steps {
	PastelPortEnv pastelPortEnv;
	PastelPortTxRecvImpl pastelPortTxRecvImpl;
    CommonTx tx;
    PastelPortResp resp;
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
    @Given("I have a pastelPortEnv")
    public void setPastelPortEnv(){
    	pastelPortEnv = new PastelPortEnv();
    	pastelPortEnv.setCompanyCode("12345");
    	pastelPortEnv.setConnectionPortNO(8012);
    	pastelPortEnv.setHandledCompanyCode("1234567");
    	pastelPortEnv.setHandledCompanySubCode("8901");
    	pastelPortEnv.setMscedit("JIS1");
    	pastelPortEnv.setPastelPortMultiServerIP("localhost");
    	pastelPortEnv.setPaymentClassification("");
    	pastelPortEnv.setPrintPPLog(true);
    	pastelPortEnv.setRetryCount(1);
    	pastelPortEnv.setSignLessReceiptWord("");
    	pastelPortEnv.setTimeOut(65);
    	pastelPortEnv.setTradingUnfinishedReceiptWord("");
    }
    @When("I Play for PastelPortIf set tx {$strMsgjson} $xml")
    public void testcommunicationWithPastalPort(String strMsgjson,String xml) throws Exception{
    	xml = this.trimString(xml);
    	resp = unMarshall(xml);
    	 tx = new CommonTx();
    	tx.setJsonValue(strMsgjson);
    	/*Map<String, String> row = expected.getRows().get(0);
    	resp = new PastelPortResp();
    	resp.setAmount(row.get("amount"));
    	resp.setCorpid(row.get("corpid"));
    	resp.setCreditcompanycode(row.get("creditcompanycode"));
    	resp.setCreditcompanyname(row.get("creditcompanyname"));
    	resp.setCreditstatus(row.get("creditStatus"));
    	resp.setErrorcode(row.get("errorcode"));
    	resp.setExpdate(row.get("expdate"));
    	resp.setNCRWSSExtendedResultCode(Integer.valueOf(row.get("extendedResultCode")));
    	resp.setNCRWSSResultCode(Integer.valueOf(row.get("resultCode")));
    	resp.setPan4(row.get("pan4"));
    	resp.setPan6(row.get("pan6"));
    	resp.setPaymentmethod(row.get("paymentMethod"));
    	resp.setPaymentseq(row.get("paymentSeq"));
    	resp.setPosReturn(Boolean.parseBoolean(row.get("posReturn")));
    	resp.setStatus(row.get("status"));
    	resp.setStoreid(row.get("storeID"));
    	resp.setTerminalid(row.get("terminalID"));
    	resp.setTillid(row.get("tillID"));
    	resp.setTxid(row.get("txID"));*/
    	pastelPortTxRecvImpl  = (PastelPortTxRecvImpl) PastelPortIf.communicationWithPastalPort(pastelPortEnv, tx, resp);
        }
    @Then("I should check the pastelPortTxRecvImpl $xml")
    public void checkPastelPortTxRecvImpl(String xml) throws JAXBException{
    	PastelPortTxRecvImpl pastelPortTxRecvImplEx = null;
    	xml = this.trimString(xml);
    	pastelPortTxRecvImplEx = unMarshallRes(xml);
    	assertEquals(pastelPortTxRecvImplEx.getStoreid(),pastelPortTxRecvImpl.getStoreid());
    	assertEquals(pastelPortTxRecvImplEx.getSourceflag(),pastelPortTxRecvImpl.getSourceflag());
    	assertEquals(pastelPortTxRecvImplEx.getResultflag(),pastelPortTxRecvImpl.getResultflag());
    	assertEquals(pastelPortTxRecvImplEx.getErrorcode(),pastelPortTxRecvImpl.getErrorcode());
    }
    private PastelPortResp unMarshall(String xml) throws JAXBException{
        XmlSerializer<PastelPortResp> serializer =
            new XmlSerializer<PastelPortResp>();
        PastelPortResp creditAuth =
            serializer.unMarshallXml(xml, PastelPortResp.class);
        return creditAuth;
    }
    private PastelPortTxRecvImpl unMarshallRes(String xml) throws JAXBException{
        XmlSerializer<PastelPortTxRecvImpl> serializer =
            new XmlSerializer<PastelPortTxRecvImpl>();
        PastelPortTxRecvImpl creditAuth =
            serializer.unMarshallXml(xml, PastelPortTxRecvImpl.class);
        return creditAuth;
    }
    private String trimString(String paramStr){
        String trimStr = paramStr.replaceAll("\n","").replaceAll("\r", "")
                            .replaceAll("\t", ""); //remove newline,return,tab
        return trimStr;
    }
}
