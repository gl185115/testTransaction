/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* ReceiptFormatterSteps
*
* Class containing Steps for Unit Testing ReceiptFormatter Helper Class
*
* Meneses, Chris Niven
*/
package ncr.res.mobilepos.helper.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;

import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.helper.ReceiptFormatter;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;

import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

public class ReceiptFormatterSteps extends Steps{
    ReceiptFormatter receiptFormatter;
    String amount;
    String receipt;
    List<String> paperReceipt;
    
    
    @Given("a ReceiptFormatter")
    public final void englishCaptions(){
        receiptFormatter = new ReceiptFormatter();
    }
    
    @When("asked to retrieve amount {$amount} with currency symbol")
    public final void getCurrencySymbol(final long amountToSet){
        this.amount = ReceiptFormatter.getCurrencySymbol(amountToSet);
    }
    
    @When("asked to retrieve amount {$amount} with comma")
    public final void getComma(final long amountToSet){
        this.amount = ReceiptFormatter.getNumberWithComma(amountToSet);
    }
    
    @When("asked to make receipt using PosLog: $samplePosLog")
    public final void generateReceipt(final String samplePosLog){
        XmlSerializer<PosLog> xmlSerializer = new XmlSerializer<PosLog>();
        PosLog posLog = new PosLog();
        
        try {
            posLog = xmlSerializer.unMarshallXml(samplePosLog, PosLog.class);
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.receipt = ReceiptFormatter.toReceiptFormat(posLog);
    }
    
    @When("asked to make receipt in English with credit number: $creditNum"
            + " and using PosLog: $samplePosLog")
    public final void generateReceiptInEnglish(
            final String creditNum, final String samplePosLog){
        XmlSerializer<PosLog> xmlSerializer = new XmlSerializer<PosLog>();
        PosLog posLog = new PosLog();
        
        try {
            posLog = xmlSerializer.unMarshallXml(samplePosLog, PosLog.class);
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        receiptFormatter = new ReceiptFormatter();
        this.receipt = receiptFormatter.toMailReceiptFormat(
                posLog, creditNum, "en");
    }
    
    @When("asked to make receipt in Japanese with credit number:"
            + " $creditNum and using PosLog: $samplePosLog")
    public final void generateReceiptInJapanese(final String creditNum,
            final String samplePosLog){
        XmlSerializer<PosLog> xmlSerializer = new XmlSerializer<PosLog>();
        PosLog posLog = new PosLog();
        
        try {
            posLog = xmlSerializer.unMarshallXml(samplePosLog, PosLog.class);
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        receiptFormatter = new ReceiptFormatter();
        this.receipt =
            receiptFormatter.toMailReceiptFormat(posLog, creditNum, "ja");
    }
    
    @When("asked to make paper receipt with credit number:"
            + " $creditNum and using PosLog: $samplePosLog")
    public final void generatePaperReceipt(
            final String creditNum, final String samplePosLog){
        XmlSerializer<PosLog> xmlSerializer = new XmlSerializer<PosLog>();
        PosLog posLog = new PosLog();
        
        try {
            posLog = xmlSerializer.unMarshallXml(samplePosLog, PosLog.class);
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        receiptFormatter = new ReceiptFormatter();
        
        try {
            this.paperReceipt =
                receiptFormatter.toPaperReceiptFormat(posLog, creditNum);
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @When("asked to make receipt using null PosLog")
    public final void generateReceiptWithNullPosLog(){
        PosLog posLog = new PosLog();
        this.receipt = ReceiptFormatter.toReceiptFormat(posLog);
    }
    
    @When("asked to make mail receipt using PosLog: $samplePosLog")
    public final void generateReceiptMail(final String samplePosLog){
        XmlSerializer<PosLog> xmlSerializer = new XmlSerializer<PosLog>();
        PosLog posLog = new PosLog();
        
        try {
            posLog = xmlSerializer.unMarshallXml(samplePosLog, PosLog.class);
        } catch (JAXBException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        this.receipt = ReceiptFormatter.toReceiptFormatMail(posLog);
    }
    
    @Then("value should be {$amount}")
    public final void checkAmount(final String expectedamount){
        assertThat(expectedamount, is(equalTo(this.amount)));
    }
    
    @Then("receipt should be: $receipt")
    public final void checkReceipt(String expectedreceipt){
        expectedreceipt = expectedreceipt.replaceAll("(\\r|\\t)", "");
        assertThat(true, is(equalTo(expectedreceipt.equals(this.receipt))));
    }
    
    @Then("paper receipt should be: $receipt")
    public final void checkPaperReceipt(String expectedreceipt){
        expectedreceipt = expectedreceipt.replaceAll("(\\r)", "");
        List<String> receiptLines = Arrays.asList(expectedreceipt.split("\\n"));
        for (int i = 0; i < this.paperReceipt.size(); i++) {
            assertThat("The Receipt line named ", receiptLines.get(i),
                   is(equalTo(this.paperReceipt.get(i))));
        }
    }
    
    @Then("receipt should be empty")
    public final void checkEmptyReceipt(){
        Assert.assertEquals("", this.receipt);
    }
}
