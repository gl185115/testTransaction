/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* ReceiptCaptionSteps
*
* Class containing Steps for Unit Testing ReceiptCaptions Helper Class
*
* Meneses, Chris Niven
*/
package ncr.res.mobilepos.helper.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import ncr.res.mobilepos.helper.ReceiptCaptions;
import ncr.res.mobilepos.helper.Requirements;

import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class ReceiptCaptionsSteps extends Steps{
    String caption;
    ReceiptCaptions receiptCaptions;
    
    
    @Given("a ReceiptCaptions in English")
    public final void englishCaptions(){
        receiptCaptions = new ReceiptCaptions("en");
    }
    
    @Given("a ReceiptCaptions in Japanese")
    public final void japaneseCaptions(){
        receiptCaptions = new ReceiptCaptions("ja");
    }
    
    @Given("a ReceiptCaptions in language {$language}")
    public final void japaneseCaptions(final String language){
        receiptCaptions = new ReceiptCaptions(language);
    }
    
    @When("asked to retrieve caption for Approval Number")
    public final void getApprovalNumber(){
        caption = receiptCaptions.getApprovalNumber();
    }
    
    @When("asked to retrieve caption for Cash")
    public final void getCash(){
        caption = receiptCaptions.getCash();
    }

    @When("asked to retrieve caption for Change")
    public final void getChange(){
        caption = receiptCaptions.getChange();
    }

    @When("asked to retrieve caption for Credit")
    public final void getCredit(){
        caption = receiptCaptions.getCredit();
    }

    @When("asked to retrieve caption for Customer ID")
    public final void getCustomerID(){
        caption = receiptCaptions.getCustomerID();
    }

    @When("asked to retrieve caption for Date")
    public final void getDate(){
        caption = receiptCaptions.getDate();
    }

    @When("asked to retrieve caption for Device Number")
    public final void getDeviceNumber(){
        caption = receiptCaptions.getDeviceNumber();
    }

    @When("asked to retrieve caption for Discount")
    public final void getDiscount(){
        caption = receiptCaptions.getDiscount();
    }
    
    @When("asked to retrieve caption for Email Subject")
    public final void getEmailSubject(){
        caption = receiptCaptions.getEmailSubject();
    }
    
    @When("asked to retrieve caption for Member Number")
    public final void getMemberNumber(){
        caption = receiptCaptions.getMemberNumber();
    }

    @When("asked to retrieve caption for Sub Total")
    public final void getSubTotal(){
        caption = receiptCaptions.getSubTotal();
    }

    @When("asked to retrieve caption for Tax")
    public final void getTax(){
        caption = receiptCaptions.getTax();
    }

    @When("asked to retrieve caption for Total Amount")
    public final void getTotalAmount(){
        caption = receiptCaptions.getTotalAmount();
    }

    @When("asked to retrieve caption for Transaction Number")
    public final void getTransactionNumber(){
        caption = receiptCaptions.getTransactionNumber();
    }
    
    @Then("caption should be {$caption}")
    public final void checkOutputLog(final String captionToSet){
        assertThat(captionToSet, is(equalTo(this.caption)));
    }
}