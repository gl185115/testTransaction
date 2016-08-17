package ncr.res.mobilepos.journalization.models.test;

import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import static org.junit.Assert.assertEquals;


public class PosLogRespTestSteps extends Steps {
    
    private XmlSerializer<PosLogResp> posLogRespSrlzr;
    private PosLogResp oPosLogResp;
    
    @Given("a poslogresp object")
    public final void createPosLogResp(){
         oPosLogResp = new PosLogResp();
         posLogRespSrlzr = new XmlSerializer<PosLogResp>();
    }
    
    @When("status is $status, txid is $txid")
    public final void setProperties(final String status, final String txid){
        oPosLogResp.setStatus(status);
        oPosLogResp.setTxID(txid);
    }
    
    @When("resultcode is $resultcode, extendedresultcode"
            + " is $exresultcode, status is $status, txid is $txid")
    public final void setProperties(
            final int resultcode, final int exresultcode,
            final String status, final String txid){
        oPosLogResp.setStatus(status);
        oPosLogResp.setTxID(txid);
        oPosLogResp.setNCRWSSResultCode(resultcode);
        oPosLogResp.setNCRWSSExtendedResultCode(exresultcode);
    }
    
    @Then("xml should be {$xml}")
    public final void validateXml(final String xml) throws Exception{
        assertEquals(xml, posLogRespSrlzr.marshallObj(PosLogResp.class,
                oPosLogResp, "UTF-8"));
    }
}
