package ncr.res.mobilepos.promotion.resource.itemquantity.test;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;

import junit.framework.Assert;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.promotion.helper.TerminalItem;
import ncr.res.mobilepos.promotion.model.PromotionResponse;
import ncr.res.mobilepos.promotion.resource.PromotionResource;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class ItemQuantityTestSteps extends Steps{
    private ServletContext servletContext = null;
    private PromotionResource promotionRes = null;
    private ResultBase promotionResponse = null;
    
    @BeforeScenario
    public final void SetUpClass()
    {
        Requirements.SetUp();
    }

    @AfterScenario
    public final void TearDownClass(){
        Requirements.TearDown();
    }
    
    @Given("a Promotion Service")
    public void aPromotionService() {
        promotionRes = new PromotionResource();
        Field context;
        try {
            context = promotionRes
            .getClass().getDeclaredField("context");
            context.setAccessible(true);
            context.set(promotionRes, servletContext);
        } catch (Exception ex) {
            Assert.fail("Can't Retrieve Servlet context from promotion.");
        }
    }
    @When("a Begin Transaction at promotion with parameters RetailStoreID{$retailStoreID} WorkStationId{$workStationID} SequenceNo{$seqNo} TransactionJson{$transactionJson}")
    public final void aBeginTransactionAtPromotionWithParameters(
             String retailStoreId,  String workStationId,  String sequenceNo,  String companyId, String transactionJson) {
        
        if (retailStoreId.equals("NULL")) {
            retailStoreId = null;
        }
        if (workStationId.equals("NULL")) {
            workStationId = null;
        }
        if (sequenceNo.equals("NULL")) {
            sequenceNo = null;
        }
        if (transactionJson.equals("NULL")) {
            transactionJson = null;
        }
        promotionResponse =
            promotionRes.beginTransaction(retailStoreId, workStationId,
                    sequenceNo, companyId, transactionJson);
    }
    @SuppressWarnings("unchecked")
    @Given("a list of TerminalItem in the Servletcontext: $terminalItems")
    public void aListOfTerminalItemInTheServletcontext(ExamplesTable terminalItems) {
        
        Map<String, TerminalItem> actualTerminalItems = (Map<String, TerminalItem>) 
        servletContext.getAttribute(GlobalConstant.PROMOTION_TERMINAL_ITEMS);
        
        for (Map<String, String> param : terminalItems.getRows()){
            String retailstoreid = param.get("retailstoreid");
            String workstationid = param.get("workstationid");
            String sequenceNumberToSet = param.get("sequencenumber");
            TerminalItem termitem = new TerminalItem(retailstoreid, workstationid, sequenceNumberToSet);
            
            actualTerminalItems.put(termitem.getId(), termitem);
        }
    }
    
    @When("the Web API Starts Up")
    public final void theWebAPIStartsUp(){
        servletContext = null;
        try
        {
            servletContext = Requirements.getMockServletContext();
        } catch (/*RuntimeError*/Exception ex) {
            Assert.fail("Can't Start Up WebStoreServer");
        }
    }
   
    @When("I request to update Item Quantity at promotion with parameters RetailStoreID{$retailStoreID} WorkStationID{$workStationID} SequenceNo{$sequenceNumber} and TransactionJson{$transactionJson}")
    public final void itemQuantityAtPromotionWithParameters(final String retailStoreId,
            final String workStationId, final String sequenceNumber, final String transactionJson) {
        //function does not exist in PromotionResouce
        /*
        promotionResponse =
            promotionRes.itemQuantity(retailStoreId, workStationId, sequenceNumber, transactionJson);
        */
    }
    
    @Then("the Result Code is $resultcode")
    public final void theResultCodeIs(int expectedResultCode) {
        Assert.assertEquals("Expect the  result code", expectedResultCode,
                promotionResponse.getNCRWSSResultCode());
    }
    
    @Then ("the JSON should have the following format : $expectedJson")
    public final void theJsonShouldHaveTheFollowingJSONFormat(
            final String expectedJson) {
        try {
            JsonMarshaller<PromotionResponse> jsonMarshaller =
                new JsonMarshaller<PromotionResponse>();
            
            String actualJson =
                jsonMarshaller.marshall((PromotionResponse)promotionResponse);
            System.out.println("ACTUAL JSON: " + actualJson);
            Assert.assertEquals("Verify the PromotionResponse JSON Format",
                    expectedJson, actualJson);
        } catch (IOException e) {
            Assert.fail("Failed to verify the PromotionResponse JSON format");
            e.printStackTrace();
        }
    }
}
