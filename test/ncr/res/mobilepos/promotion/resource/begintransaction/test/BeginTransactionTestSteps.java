package ncr.res.mobilepos.promotion.resource.begintransaction.test;

import java.lang.reflect.Field;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;

import junit.framework.Assert;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.promotion.helper.TerminalItem;
import ncr.res.mobilepos.promotion.resource.PromotionResource;
import ncr.res.mobilepos.systemconfiguration.resource.SystemConfigurationResource;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class BeginTransactionTestSteps extends Steps {
    private ServletContext servletContext = null;
    private PromotionResource testpromotionResource = null;
    private Map<String, TerminalItem> actualTerminalItems = null;
    private ResultBase actualResultBase = null;
    

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
        testpromotionResource = new PromotionResource();
        Field context;
        try {
            context = testpromotionResource
            .getClass().getDeclaredField("context");
            context.setAccessible(true);
            context.set(testpromotionResource, servletContext);
        } catch (Exception ex) {
            Assert.fail("Can't Retrieve Servlet context from promotion.");
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
    
    @When("a Begin Transaction at promotion with parameters RetailStoreID{$retailStoreID} WorkStationId{$workStationID} SequenceNo{$seqNo} TransactionJson{$transactionJson}")
    public final void aBeginTransactionAtPromotionWithParameters(
             String retailStoreId,  String workStationId,  String sequenceNo, String companyId, String transactionJson) {
        
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
        actualResultBase =
            testpromotionResource.beginTransaction(retailStoreId, workStationId,
                    sequenceNo, companyId, transactionJson);
    }
    
    @SuppressWarnings("unchecked")
    @Then("the following list of TerminalItem are: $expectedTerminalItems")
    public final void theFollowingListOfTerminalItemAre(ExamplesTable expectedTerminalItems) {
        //|id|sequenceNumber|transactionMode|operatorid|beginDateTime|
       actualTerminalItems = (Map<String, TerminalItem>) 
           servletContext.getAttribute(GlobalConstant.PROMOTION_TERMINAL_ITEMS);
      Assert.assertEquals("Comnpare the expected count of TerminalItems",
              expectedTerminalItems.getRowCount(), actualTerminalItems.size());
      int i = 0;
      for (Entry<String, TerminalItem> param : actualTerminalItems.entrySet()) {
          TerminalItem actualTermItem = param.getValue();
          Map<String, String> expetedTermItem = expectedTerminalItems.getRow(i);
          
          Assert.assertEquals("Compare Terminal Item ID of row" + i,
                  expetedTermItem.get("id"),
                  actualTermItem.getId());
          Assert.assertEquals("Compare Terminal Item SequenceNumber of row" + i,
                  expetedTermItem.get("sequenceNumber"),
                  actualTermItem.getSequenceNumber());
          Assert.assertEquals("Compare Terminal Item TransactionMode of row" + i,
                  Integer.parseInt(expetedTermItem.get("transactionMode")),
                  actualTermItem.getTransactionMode());    
          Assert.assertEquals("Compare Terminal Item OperatorID of row" + i,
                  expetedTermItem.get("operatorid"),
                  actualTermItem.getOperatorid());    
          Assert.assertEquals("Compare Terminal Item BeginDateTime of row" + i,
                  expetedTermItem.get("beginDateTime"),
                  actualTermItem.getBeginDateTime());
          
          //Compare the KEY with the Terminal ID.
          Assert.assertTrue("Compare the Key with regards to TerminalItemID",
                  actualTermItem.getId().equals(param.getKey()));
          i++;
      }
    }
    @Then("the Result Code is $resultcode")
    public final void theResultCodeIs(int expectedResultCode) {
        Assert.assertEquals("Expect the  result code", expectedResultCode,
                actualResultBase.getNCRWSSResultCode());
    }
}
