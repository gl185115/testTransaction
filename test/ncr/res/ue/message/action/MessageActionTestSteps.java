package ncr.res.ue.message.action;

import java.math.BigInteger;
import java.util.Map;

import ncr.res.ue.message.OutgoingMessage;

import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class MessageActionTestSteps extends Steps{
    private OutgoingMessage outMessage = null;
    private String message = "";
    @When("I create connection initialization locationCode{$lc},protocolVersion{$pv},protocolBuild{$pb}}")
    public void createConnectionInitializationAction(String lc, String pv, String pb){
        outMessage = new ConnectionInitialization(lc, pv, pb);
    }
    @When("I create begin transaction txmode{$txmode}, fuelflag{$fuelflag}, startdate{$date}, starttime{$time}, operator{$operator}")
    public void createBeginTransactionaAction(int txmode, int fuelflag, String date, String time, String operator){
        outMessage = new BeginTransaction(txmode, fuelflag, date, time, operator);
    }
    @When("I create item entry with parameters : $param")
    public void createItemEntryAction(ExamplesTable param){
        Map<String,String> map = param.getRow(0);
        /*|itemEntryID|entryFlag|returnFlag|itemCode|familyCode
        |department|clearanceLevel|pricePrecision|unitPrice|
        quantityType|quantitySubType|quantityPrecision|quantity|
        discountFlag|tlqFlag|alternatePrice|*/
        outMessage = new ItemEntry(Integer.parseInt(map.get("itemEntryID").trim()),
                                    Integer.parseInt(map.get("entryFlag").trim()),
                                    Integer.parseInt(map.get("returnFlag").trim()),
                                    map.get("itemCode").trim(),
                                    Integer.parseInt(map.get("familyCode").trim()),
                                    Integer.parseInt(map.get("department").trim()),
                                    Integer.parseInt(map.get("clearanceLevel").trim()),
                                    Integer.parseInt(map.get("pricePrecision").trim()),
                                    Integer.parseInt(map.get("unitPrice").trim()),
                                    Integer.parseInt(map.get("quantityType").trim()),
                                    Integer.parseInt(map.get("quantitySubType").trim()),
                                    Integer.parseInt(map.get("quantityPrecision").trim()),
                                    Integer.parseInt(map.get("quantity").trim()),
                                    Integer.parseInt(map.get("discountFlag").trim()),
                                    Integer.parseInt(map.get("tlqFlag").trim()),
                                    Integer.parseInt(map.get("alternatePrice").trim()));
    }
    @When("I create adjustment with parameters : $param")
    public void createAdjustmentAction(ExamplesTable param){
        Map<String,String> map = param.getRow(0);
        /*|itemEntryID|entryFlag|returnFlag|itemCode|familyCode
        |department|clearanceLevel|pricePrecision|unitPrice|
        quantityType|quantitySubType|quantityPrecision|quantity|
        discountFlag|tlqFlag|alternatePrice|*/
        outMessage = new Adjustment(Integer.parseInt(map.get("entryID").trim()),
                                    Integer.parseInt(map.get("adjustmentFlag").trim()),
                                    Integer.parseInt(map.get("adjustmentType").trim()),
                                    Integer.parseInt(map.get("adjustmentLevel").trim()),
                                    Integer.parseInt(map.get("calculationType").trim()),
                                    Integer.parseInt(map.get("itemEntryID").trim()),
                                    Integer.parseInt(map.get("mnupPrecision").trim()),
                                    Integer.parseInt(map.get("matchNetUnitPrice").trim()),
                                    Integer.parseInt(map.get("department").trim()),
                                    Integer.parseInt(map.get("adjustmentPrecision").trim()),
                                    Integer.parseInt(map.get("unitAdjustment").trim()),
                                    Integer.parseInt(map.get("quantityType").trim()),
                                    Integer.parseInt(map.get("quantitySubType").trim()),
                                    Integer.parseInt(map.get("quantityPrecision").trim()),
                                    Integer.parseInt(map.get("quantity").trim()),
                                    Integer.parseInt(map.get("tlqFlag").trim()),
                                    Integer.parseInt(map.get("discountableFlag").trim()),
                                    Integer.parseInt(map.get("rewardCalculation").trim()),
                                    Integer.parseInt(map.get("priority").trim()));
    }
    @When("I create total with parameters : $param")
    public void createTotalAction(ExamplesTable param){
        Map<String,String> map = param.getRow(0);
        /*|itemEntryID|entryFlag|returnFlag|itemCode|familyCode
        |department|clearanceLevel|pricePrecision|unitPrice|
        quantityType|quantitySubType|quantityPrecision|quantity|
        discountFlag|tlqFlag|alternatePrice|*/
        outMessage = new Total(Integer.parseInt(map.get("totalPrecision").trim()),
                                    Integer.parseInt(map.get("total").trim()),
                                    Integer.parseInt(map.get("userTotal").trim()),
                                    map.get("date").trim(),
                                    map.get("time").trim());
    }
    @When("I create item points with parameters : $param")
    public void createItemPointsAction(ExamplesTable param){
        Map<String,String> map = param.getRow(0);
        /*|itemEntryID|entryFlag|returnFlag|itemCode|familyCode
        |department|clearanceLevel|pricePrecision|unitPrice|
        quantityType|quantitySubType|quantityPrecision|quantity|
        discountFlag|tlqFlag|alternatePrice|*/
        outMessage = new ItemPoints(Integer.parseInt(map.get("itemEntryId").trim()),
                                    Integer.parseInt(map.get("itemQuantity").trim()),
                                    Integer.parseInt(map.get("programId").trim()),
                                    Integer.parseInt(map.get("quantity").trim()));
    }

    @When("I create member id with parameters : $param")
    public void createMemeberIdAction(ExamplesTable param){
        Map<String,String> map = param.getRow(0);
        /*|itemEntryID|entryFlag|returnFlag|itemCode|familyCode
        |department|clearanceLevel|pricePrecision|unitPrice|
        quantityType|quantitySubType|quantityPrecision|quantity|
        discountFlag|tlqFlag|alternatePrice|*/
        outMessage = new MemberId(new BigInteger((map.get("memberid").trim())),
                Integer.parseInt(map.get("entryflag").trim()),
                Integer.parseInt(map.get("type").trim()));
    }

    @When("I create resume transaction with parameters : $param")
    public void resumeTransaction(ExamplesTable param) {
        Map<String, String> map = param.getRow(0);
        outMessage = new Resume(Integer.parseInt(map.get("resumeflag").trim()),
                map.get("resumeid").trim());
    }
    
    @When("I create end transaction with parameters : $param")
    public void createEndTransactionAction(ExamplesTable param){
        Map<String,String> map = param.getRow(0);
        /*|itemEntryID|entryFlag|returnFlag|itemCode|familyCode
        |department|clearanceLevel|pricePrecision|unitPrice|
        quantityType|quantitySubType|quantityPrecision|quantity|
        discountFlag|tlqFlag|alternatePrice|*/
        outMessage = new EndTransaction(Integer.parseInt(map.get("status").trim()),
                                    map.get("date").trim(),
                                    map.get("time").trim());
    }

    @When("I create tender entry with parameters : $param")
    public void createTenderEntryAction(ExamplesTable param){
        Map<String,String> map = param.getRow(0);
        /*|itemEntryID|entryFlag|returnFlag|itemCode|familyCode
        |department|clearanceLevel|pricePrecision|unitPrice|
        quantityType|quantitySubType|quantityPrecision|quantity|
        discountFlag|tlqFlag|alternatePrice|*/
        outMessage = new TenderEntry(Integer.parseInt(map.get("entryID").trim()),
                                    Integer.parseInt(map.get("entryFlag").trim()),
                                    Integer.parseInt(map.get("tenderID").trim()),
                                    Integer.parseInt(map.get("tenderSubID").trim()),
                                    map.get("bin").trim(),
                                    Integer.parseInt(map.get("tenderPrecision").trim()),
                                    Integer.parseInt(map.get("tenderAmount").trim()));
    }
    
    @When("I create triggercode with parameters : $params")
    public void iCreateTriggercodeWithParameters(ExamplesTable params) {
        /*|entryid|entryflag|triggercode|*/
        Map<String, String> map = params.getRow(0);
        outMessage = new TriggerCode(
                Integer.parseInt(map.get("entryid")),
                Integer.parseInt(map.get("entryflag")),
                Integer.parseInt(map.get("triggercode")));
    }
    
    @When("I create item quantity with parameters : $params")
    public void iCreateItemQuantityWithParameters(ExamplesTable params) {
        Map<String, String> map = params.getRow(0);
        /*|entryflag|itementryid|rewardchecksum|quantity|*/
        outMessage =
            new ItemQuantity(Integer.parseInt(map.get("entryflag")),
                    Integer.parseInt(map.get("itementryid")),
                    Integer.parseInt(map.get("rewardchecksum")),
                    Integer.parseInt(map.get("quantity")));
    } 
    @When("I create item store value response with parameters : $params")
    public void iCreateStoreValueWithParameters(ExamplesTable params) {
        Map<String, String> map = params.getRow(0);
        /*|itementryid|itemquantiy|programid|quantity|expiration|*/
        outMessage =
            new ItemSV(Integer.parseInt(map.get("itementryid")),
                    Integer.parseInt(map.get("itemquantiy")),
                    Integer.parseInt(map.get("programid")),
                    Integer.parseInt(map.get("quantity")),
                    map.get("expiration"));
    }
    @When("I create cancel operation value with parameters : $params")
    public void iCreateCancelOperationWithParameters(ExamplesTable params) {
        Map<String, String> map = params.getRow(0);
        outMessage =
            new CancelOperation(Integer.parseInt(map.get("reasonflag")));
    }

    @When("I create the message with termid{$termId},txid{$txId}")
    public void createMessage(String termId, String txId) throws Exception{
        message = outMessage.createMessage(termId, txId);
    }

    /******** SUSPEND TRANSACTION TEST ********/
    @When("I suspend transaction with parameters : $param")
    public void suspendTransaction(ExamplesTable param){
        Map<String,String> map = param.getRow(0);
        outMessage = new SuspendTransaction(Integer.parseInt(map.get(
                "suspendflag").trim()), Integer.parseInt(map.get("resumeid").trim()));
    }
    
    @When("I create GS1_Coupon with parameters : $param")
    public void gsOneCoupon(ExamplesTable param) {
        Map<String, String> map = param.getRow(0);
        System.out.println("SERIAL NUMBER :" + map.get("serialNum").trim());
        outMessage = new GSOneCoupon(map.get("entryid").trim(),
                Integer.parseInt(map.get("applyflag").trim()),
                map.get("applicationid").trim(),
                Integer.parseInt(map.get("primgs1compprefvli").trim()),
                map.get("primgs1comppref").trim(),
                map.get("offercode").trim(),
                Integer.parseInt(map.get("savevaluevli").trim()),
                map.get("savevalue").trim(),
                Integer.parseInt(map.get("primprchsereqvli").trim()),
                map.get("primprchsereq").trim(),
                Integer.parseInt(map.get("primprchsereqcode").trim()),
                map.get("primprchsefmlycode").trim(),
                Integer.parseInt(map.get("scndqualifyingprchse").trim()),
                Integer.parseInt(map.get("additionalprchserulescode").trim()),
                Integer.parseInt(map.get("scndprchsereqvli").trim()),
                map.get("scndprchsereq").trim(),
                Integer.parseInt(map.get("scndprchsereqcode").trim()),
                map.get("scndprchsefmlycode").trim(),
                Integer.parseInt(map.get("scndprchsegs1cmpnyprefvli").trim()),
                map.get("scndgs1cmpnypref").trim(),
                Integer.parseInt(map.get("thrdqualifyingprchse").trim()),
                Integer.parseInt(map.get("thrdprchsereqvli").trim()),
                map.get("thrdprchsereq").trim(),
                Integer.parseInt(map.get("thrdprchsereqcode").trim()),
                map.get("thrdprchsefmlycode").trim(),
                Integer.parseInt(map.get("thrdPrchseGs1CmpnyPrefVli").trim()),
                map.get("thrdGs1CmpnyPref").trim(),
                Integer.parseInt(map.get("expDateIndicator").trim()),
                map.get("expDate").trim(),
                Integer.parseInt(map.get("startDateIndicator").trim()),
                map.get("startDate").trim(),
                Integer.parseInt(map.get("serialNumIndicator").trim()),
                Integer.parseInt(map.get("serialNumVli").trim()),
                map.get("serialNum").trim(),
                Integer.parseInt(map.get("retailerId").trim()),
                Integer.parseInt(map.get("retailerGs1CmpnyPrefGlnVli").trim()),
                map.get("retailerGs1CmpnyPrefGln").trim(),
                Integer.parseInt(map.get("miscElements").trim()),
                Integer.parseInt(map.get("saveValueCode").trim()),
                Integer.parseInt(map.get("saveValAppliesToWchItem").trim()),
                Integer.parseInt(map.get("storeCouponFlag").trim()),
                Integer.parseInt(map.get("doNotMultiplyFlag").trim()),
                map.get("priority").trim());
    }
    
    @Then("message data should be {$message}")
    public void validateMessage(String message){
        String expectedMsg = message.replace("\\r\\n", System.getProperty("line.separator"));
        assertEquals("Compare the Message received", expectedMsg, this.message);
    }
}
