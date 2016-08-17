package ncr.res.mobilepos.journalization.models.test;

import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.model.TransactionData;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import static org.junit.Assert.assertEquals;


public class TransactionDataTestSteps extends Steps {
    
    private XmlSerializer<TransactionData> TransactionDataSrlzr;
    private TransactionData oTransactionData;
    private String strdata; 
    
    @Given("a transactiondata object")
    public final void createPosLogResp(){
        oTransactionData = new TransactionData();
        TransactionDataSrlzr = new XmlSerializer<TransactionData>();
    }
    
    @When("transaction type is $txtype")
    public final void setProperties(final String txtype){
        oTransactionData.setTxType(txtype);
    }
    
    @When("xml data is {$xmldata}, transaction type is $txtype")
    public final void setProperties(final String xmldata, final String txtype){
        oTransactionData.setTxType(txtype);
        oTransactionData.setXmlData(xmldata);
    }
    
    @When("receipt data is {$receiptdata}, xml data is {$xmldata},"
            + " transaction type is $txtype")
    public final void setProperties(final String receiptdata,
            final String xmldata , final String txtype){
        oTransactionData.setTxType(txtype);
        oTransactionData.setXmlData(xmldata);
        oTransactionData.setReceiptData(receiptdata);
    }
    
    @When("resultcode is $resultcode, extendedresultcode is $exresultcode,"
            + " receipt data is {$receiptdata}, xml data is {$xmldata},"
            + " transaction type is $txtype")
    public final void setProperties(
            final int resultcode, final int exresultcode,
            final String receiptdata, final String xmldata ,
            final String txtype){
        oTransactionData.setTxType(txtype);
        oTransactionData.setXmlData(xmldata);
        oTransactionData.setReceiptData(receiptdata);
        oTransactionData.setNCRWSSResultCode(resultcode);
        oTransactionData.setNCRWSSExtendedResultCode(exresultcode);
    }
    
    @Then("xml should be {$xml}")
    public final void validateXml(final String xml) throws Exception{
        assertEquals(xml, TransactionDataSrlzr.marshallObj(
                TransactionData.class, oTransactionData, "UTF-8"));
    }
    
    @When("object is marshalled to xml")
    public final void marshallObject() throws Exception{
        strdata = TransactionDataSrlzr.marshallObj(
                TransactionData.class, oTransactionData, "UTF-8");
    }
    
    @When("unmarshalled back to object")
    public final void unMarshallObject() throws Exception{
        oTransactionData = TransactionDataSrlzr.unMarshallXml(strdata,
                TransactionData.class);
    }
    
    @Then("the data xml should be {$xmldata}")
    public final void validateXmlData(final String xmldata){
        assertEquals(xmldata, oTransactionData.getXmlData());
    }
    
    @Then("the receipt data should be {$receiptdata}")
    public final void validateReceiptData(final String receiptdata){
        assertEquals(receiptdata, oTransactionData.getReceiptData());
    }
    
    @Then("the result code should be $resultcode")
    public final void validateResultCode(final int resultcode){
        assertEquals(resultcode,
                oTransactionData.getNCRWSSResultCode());
    }
    
    @Then("the extended result code should be $exresultcode")
    public final void validateExResultCode(final int exresultcode){
        assertEquals(exresultcode,
                oTransactionData.getNCRWSSExtendedResultCode());
    }
}
