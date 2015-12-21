/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * XmlSerializerCreditAuthorizationSteps
 *
 * Steps Class of XmlSerializerCreditAuthorization
 *
 * De la Cerna, Jessel G.
 */

package ncr.res.mobilepos.creditauthorization.model.test;

import java.util.Map;
import javax.xml.bind.JAXBException;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import ncr.res.mobilepos.creditauthorization.model.CARequestResult;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorizationResp;
import ncr.res.mobilepos.helper.XmlSerializer;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.*;

public class XmlSerializerCreditAuthorizationSteps extends Steps {
    
    /*** @Start: CreditAuthorizationResp Steps ***/
    private XmlSerializer<CreditAuthorizationResp> xmlserializerCreditAuthResp;
    private CreditAuthorizationResp creditAuthResp;
    
    @Given("I have an XmlSerializer for CreditAuthorizationResp")
    public void iHaveACredAuthRespXmlSerializer()
    {
        xmlserializerCreditAuthResp =
            new XmlSerializer<CreditAuthorizationResp>();
    }
    
    @When("I have a CreditAuthorizationResp xml: $xml")
    public void iHaveACredAuthRespXml(String xml) throws JAXBException
    {
        creditAuthResp = xmlserializerCreditAuthResp
                    .unMarshallXml(xml, CreditAuthorizationResp.class);
    }
    
    @Then("I should get a CrediAuthorizationResp: $expected")
    public void iShouldGetCredAuthResp(ExamplesTable expected)
    {
        CreditAuthorizationResp creditAuthRespExpectd =
            new CreditAuthorizationResp();
        Map<String, String> row = expected.getRows().get(0);
        creditAuthRespExpectd.setAmount(row.get("amount"));
        creditAuthRespExpectd.setApprovalcode(row.get("approvalcode"));
        creditAuthRespExpectd.setCorpid(row.get("corpid"));
        creditAuthRespExpectd
            .setCreditcompanycode(row.get("creditcompanycode"));
        creditAuthRespExpectd
            .setCreditcompanyname(row.get("creditcompanyname"));
        creditAuthRespExpectd.setCreditstatus(row.get("creditstatus"));
        creditAuthRespExpectd.setErrorcode(row.get("errorcode"));
        creditAuthRespExpectd.setErrormessage(row.get("errormessage"));
        creditAuthRespExpectd.setExpdate(row.get("expdate"));
        creditAuthRespExpectd.setPan(row.get("pan"));
        creditAuthRespExpectd.setPaymentmethod(row.get("paymentmethod"));
        creditAuthRespExpectd.setStatus(row.get("status"));
        creditAuthRespExpectd.setStoreid(row.get("storeid"));
        creditAuthRespExpectd.setTerminalid(row.get("terminalid"));
        creditAuthRespExpectd.setTillid(row.get("tillid"));
        creditAuthRespExpectd.setTracenum(row.get("tracenum"));
        creditAuthRespExpectd.setTxdatetime(row.get("txdatetime"));
        creditAuthRespExpectd.setTxid(row.get("txid"));
        
        assertThat(creditAuthRespExpectd.getAmount(),
                is(equalTo(creditAuthResp.getAmount())));
        assertThat(creditAuthRespExpectd.getApprovalcode(),
                is(equalTo(creditAuthResp.getApprovalcode())));
        assertThat(creditAuthRespExpectd.getCorpid(),
                is(equalTo(creditAuthResp.getCorpid())));
        assertThat(creditAuthRespExpectd.getCreditcompanyname(),
                is(equalTo(creditAuthResp.getCreditcompanyname())));
        assertThat(creditAuthRespExpectd.getCreditcompanycode(),
                is(equalTo(creditAuthResp.getCreditcompanycode())));
        assertThat(creditAuthRespExpectd.getCreditstatus(),
                is(equalTo(creditAuthResp.getCreditstatus())));
        assertThat(creditAuthRespExpectd.getErrorcode(),
                is(equalTo(creditAuthResp.getErrorcode())));
        assertThat(creditAuthRespExpectd.getErrormessage(),
                is(equalTo(creditAuthResp.getErrormessage())));
        assertThat(creditAuthRespExpectd.getExpdate(),
                is(equalTo(creditAuthResp.getExpdate())));
        assertThat(creditAuthRespExpectd.getPan(),
                is(equalTo(creditAuthResp.getPan())));
        assertThat(creditAuthRespExpectd.getPaymentmethod(),
                is(equalTo(creditAuthResp.getPaymentmethod())));
        assertThat(creditAuthRespExpectd.getStatus(),
                is(equalTo(creditAuthResp.getStatus())));
        assertThat(creditAuthRespExpectd.getStoreid(),
                is(equalTo(creditAuthResp.getStoreid())));
        assertThat(creditAuthRespExpectd.getTerminalid(),
                is(equalTo(creditAuthResp.getTerminalid())));
        assertThat(creditAuthRespExpectd.getTillid(),
                is(equalTo(creditAuthResp.getTillid())));
        assertThat(creditAuthRespExpectd.getTracenum(),
                is(equalTo(creditAuthResp.getTracenum())));
        assertThat(creditAuthRespExpectd.getTxdatetime(),
                is(equalTo(creditAuthResp.getTxdatetime())));
        assertThat(creditAuthRespExpectd.getTxid(),
                is(equalTo(creditAuthResp.getTxid())));
    }
    /*** @End: CreditAuthorizationResp Steps ***/
    
    /*** @Start: CARequestResult Steps ***/
    private CARequestResult caRequestResult;
    private String caRequestResultXml;
    
    @Given("I have a CARequestResult: CreditAuthorizationResp{$status,"
            + " $resultCode, $extendedErrorCode}")
    public void iHaveCARequestResult(String status, int resultCode,
            int extendedErrorCode) throws JAXBException{
        CreditAuthorizationResp newcreditAuthResp
            = new CreditAuthorizationResp();
        newcreditAuthResp.setNCRWSSResultCode(resultCode);
        newcreditAuthResp.setNCRWSSExtendedResultCode(extendedErrorCode);
        newcreditAuthResp.setStatus(status);
        
        caRequestResult = new CARequestResult();
        caRequestResult.setCreditAuthorizationResp(newcreditAuthResp);
    }
    
    @When("I marshall the CARequestResult into xml")
    public void iMarshallCAReqResToXml() throws JAXBException{
        XmlSerializer<CARequestResult> xmlserializerCARequestResult =
            new XmlSerializer<CARequestResult>();
        caRequestResultXml = xmlserializerCARequestResult.marshallObj(
                CARequestResult.class, caRequestResult,"UTF-8");
    }
    
    @Then("I should get CARequestResult xml: $xml")
    public void iShouldGetCAReqResXml(String xml){
        //remove newline,return,tab
        xml = xml.replaceAll("\n","").replaceAll("\r", "")
                                     .replaceAll("\t", ""); 
        assertThat(xml, is(equalTo(caRequestResultXml)));
    }
    /*** @End: CARequestResult Steps ***/
    
}
