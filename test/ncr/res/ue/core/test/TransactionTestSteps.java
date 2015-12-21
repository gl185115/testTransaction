package ncr.res.ue.core.test;

import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import junit.framework.Assert;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.ue.TestServer;
import ncr.res.ue.core.Connection;
import ncr.res.ue.core.Transaction;
import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.response.BeginTransactionResponse;
import ncr.res.ue.message.response.DiscountResponse;
import ncr.res.ue.message.response.EndTransactionResponse;
import ncr.res.ue.message.response.ItemEntryResponse;
import ncr.res.ue.message.response.TenderEntryResponse;
import ncr.res.ue.message.response.UEResponseBase;
import ncr.res.ue.message.response.rewards.Discount;
import ncr.res.ue.message.response.TotalResponse;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class TransactionTestSteps extends Steps {
    private TestServer testServer = null;
    private Transaction transaction = null;
    private Connection connection = null;
    private BeginTransactionResponse response = null;
    private EndTransactionResponse endTxResponse = null;
    private List<UEResponseBase> entryResponses = null;
    private TotalResponse totalTxResponse = null;
    private ServletContext context = null;
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        GlobalConstant.setCorpid("000031");
        testServer = new TestServer();
    }
    @AfterScenario
    public final void TearDown() {
        Requirements.TearDown();
    }
    @Given("a connection object")
    public final void createConnectionObject() throws Exception {
        testServer.runTestServer();
        context = TestServer.getMockServletForUeParameters(3010);
        System.out.println((String) context.getAttribute("UeIoServerAddress"));
        connection = new Connection(context);
        connection.initializeUeConnection();
    }
    @When("I create Transaction object")
    public final void createTransaction() throws Exception {
        transaction = new Transaction(connection);
    }
    @When("I request begin transaction txMode{$txMode}, fuelFlag{$fuelFlag}, operator{$operator}, termId{$termId}, txId{$transactionId}")
    public final void beginTransaction(
            int transactionMode,
            int fuelFlag,
            String operator,
            String termId,
            String transactionId) throws Exception {
        response = transaction.beginTransaction(transactionMode, fuelFlag, operator, termId, transactionId);
    }
    @Then("Begin transaction should be a success")
    public final void validateResult(){
        assertThat(0, is(equalTo(response.getResponseFlag())));
    }
    @When("I request end transaction status{$status}, termId{$termId}, transactionId{$transactionId}")
    public final void endTransaction(int status, String termId, String transactionId) throws Exception {
        endTxResponse = transaction.endTransaction(status, termId, transactionId);
    }
    @Then("End Transaction should be a success")
    public final void validateEndResult(){
        assertThat(0, is(equalTo(endTxResponse.getResponseFlag())));
    }
    @When("I request Total action totalPrecision{$totalPrecision}, total{$total}, userTotal{$userTotal}, termId{$termId}, transactionId{$transactionId}")
    public final void total(int totalPrecision, int total, int userTotal, String termId, 
            String transactionId) throws Exception {        
        totalTxResponse = transaction.totalTx(totalPrecision, total, userTotal, termId,
                transactionId);
    }
    @Then("Total action should be a success")
    public final void totalTransaction() {
        assertThat("0000000013", is(equalTo(totalTxResponse.getNetTotal())));
        assertThat("0000000025", is(equalTo(totalTxResponse.getGrossTotal())));
        assertThat(0, is(equalTo(totalTxResponse.getResponseFlag())));        
    }    
    @Then("close connection")
    public final void closeConnection() throws Exception {
        connection.closeConnection();
        TestServer.stop = true;
    }
    
    @When("I send an item entry: itemEntryId{$id}, purchaseType{$entryflag}, returnType{$returnFlag}, itemCode{$itemCode}, familyCode{$familyCode}, dept{$department}, clearanceLevel{$clearanceLevel}, Discountable{$discountFlag}, qtyType{$quantityType}, qty{$quantity}, qtySubType{$quantitySubType}, qtyPrec{$quantityPrecision}, unitPrice{$unitPrice}, alternatePrice{$alternatePrice}, precisionPt{$pricePrecision}")
    public final void sendItemEntry(int id, int entryFlag, int returnFlag, String itemCode, int familyCode, int department, int clearanceLevel,
            int discountFlag, int quantityType, int quantity, int quantitySubType, int quantityPrecision, 
            int unitPrice, int alternatePrice, int pricePrecision) throws MessageException{
        
        entryResponses = transaction.sendItemEntry("1111", "1", id, entryFlag, returnFlag, itemCode,
                familyCode, department, clearanceLevel, pricePrecision, unitPrice,
                quantityType, quantitySubType, quantityPrecision, quantity, discountFlag, 1, alternatePrice);
    }
    
    @Then("I should get {$ItemEntryResponse}: $exampleTables")
    public final void testItemEntryResponse(final String response,
            final ExamplesTable expectedResult) {
        
        UEResponseBase entryResp = null;
        if (response.equals("ItemEntryResponse"))
            entryResp = new ItemEntryResponse();
        if (response.equals("DiscountResponse"))
            entryResp = new DiscountResponse();
        if (response.equals("TenderEntryResponse"))
            entryResp = new TenderEntryResponse();

        @SuppressWarnings("rawtypes")
        Iterator it = entryResponses.iterator();
        while (it.hasNext()) {
            UEResponseBase temp = (UEResponseBase) it.next();

            if ((temp instanceof ItemEntryResponse
                    && temp.getClass() == entryResp.getClass()) ||
                (temp instanceof TenderEntryResponse
                    && temp.getClass() == entryResp.getClass())) {
                Assert.assertEquals(expectedResult.getRow(0)
                        .get("responseFlag"), String.valueOf(temp
                        .getResponseFlag()));
                Assert.assertEquals(
                        expectedResult.getRow(0).get("messageType"),
                        String.valueOf(temp.getMessageType()));
                Assert.assertEquals(
                        expectedResult.getRow(0).get("parseResult"),
                        String.valueOf(temp.getResponseFlag()));

            } else if (temp instanceof DiscountResponse
                    && temp.getClass() == entryResp.getClass()) {
                DiscountResponse temp1 = (DiscountResponse) temp;

                Assert.assertTrue("Compare Number of Discounts: ",
                        expectedResult.getRows().size() == temp1.getDiscounts()
                                .size());

                int i = 0;

                @SuppressWarnings("rawtypes")
                Iterator it2 = temp1.getDiscounts().iterator();
                while (it2.hasNext()) {
                    Discount discount = (Discount) it2.next();
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("rwid"),
                            discount.getRewardID());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("earnedrw"),
                            discount.getEarnedRewardID());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("rwflag"),
                            discount.getRewardFlag());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("disctype"),
                            discount.getDiscountType());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("disclevel"),
                            discount.getDiscountLevel());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("calctype"),
                            discount.getCalculationType());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("flexneg"),
                            discount.getFlexNegative());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("itementryid"),
                            discount.getItemEntryId());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("discdept"),
                            discount.getDiscountDepartment());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("mnuprec"),
                            discount.getMNUPPrecision());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i)
                                    .get("matchnetunitprice"),
                            discount.getMatchNetUnitPrice());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("unitdiscprec"),
                            discount.getUnitDiscountPrecision());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("unitdiscamt"),
                            discount.getUnitDiscountAmt());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("retdiscprec"),
                            discount.getRetDiscountPrecision());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("retdiscamt"),
                            discount.getRetDiscountAmt());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("calcprec"),
                            discount.getCalculatePrecision());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("unitcalcamt"),
                            discount.getUnitCalculateAmount());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("qtytype"),
                            discount.getQuantityType());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("qtysubtype"),
                            discount.getQuantitySubtype());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("qtyprec"),
                            discount.getQuantityPrecision());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("qty"),
                            discount.getQuantity());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("promocode"),
                            discount.getPromoCode());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("discdata"),
                            discount.getDiscountData());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i).get("posrefid"),
                            discount.getPosReferenceID());
                    Assert.assertEquals(
                            expectedResult.getRows().get(i)
                                    .get("vendorcouponcode"), discount
                                    .getVendorCouponCode().trim());
                    
                    // Compare DiscountDescriptions and LanguageIDs
                    for (int j = 0; j < discount.getDiscountDescriptions()
                            .size(); j++) {
                        Assert.assertEquals(expectedResult.getRows().get(i)
                                .get("discdesc" + j), discount
                                .getDiscountDescriptions().get(j).trim());
                        Assert.assertEquals(expectedResult.getRows().get(i)
                                .get("desclangid" + j), discount
                                .getDiscountLanguageIDs().get(j));
                    }

                    i++;
                }
            } 
        }
    }
    
    @Then("I should only get ItemEntryResponse")
    public final void testDiscountRewardMessage() {
        Assert.assertEquals("Size is 1", 1, entryResponses.size());
        Assert.assertTrue("Reply is an ItemEntryResponse",
                entryResponses.get(0) instanceof ItemEntryResponse);
    }
    
    @Given("that item has {$numberOfDiscounts} discount reward messages")
    public final void givenItemHas(int numberOfDiscounts){
        if(numberOfDiscounts == 1){
            TestServer.Item_Entry_Response = "0165000011110000000161142000001000001010010000001000100000000500000000002500000000025000000000500000000000100000000300050000000                    Half              00\r\n002200001111000000013300\r\n";
        }else if(numberOfDiscounts == 2){            
            TestServer.Item_Entry_Response = "0310000011110000000161142000001000001010010000001000100000000500000000002500000000025000000000500000000000100000000300050000000                    Half              00142000002000002010010000001000100000000250000000000250000000002500000000250000000000100000000920010000000                    Congrats!         00\r\n002200001111000000013300\r\n";
        }else{
            TestServer.Item_Entry_Response = "002200001111000000013300";
        }
    }
    
    @When("I send tender entry: entryid{$entryId}, entryflag{$entryFlag}, tenderid{$tenderID}, tendersubid{$tenderSubID}, bin{$bin}, tenderprecision{$tenderPrec}, tenderamount{$tenderAmt}")
    public final void whenSendTenderEntry(int entryId, int entryFlag, int tenderId,
            int tenderSubId, String bin, int tenderPrec, int tenderAmt) throws MessageException{
        entryResponses = transaction.sendTenderEntry("1111", "1", entryId, entryFlag, tenderId, tenderSubId, bin, tenderPrec, tenderAmt);
    }
}
