package ncr.res.mobilepos.promotion.resource.endtransaction.test;

import java.lang.reflect.Field;
import java.util.Map;
import javax.servlet.ServletContext;
import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.promotion.helper.TerminalItem;
import ncr.res.mobilepos.promotion.resource.PromotionResource;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class EndTransactionTestSteps extends Steps{
    private ServletContext servletContext = null;
    private PromotionResource promotionRes = null;
    private ResultBase result = null;
    
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
    
    @When("I end the transaction at promotion with retailstoreid{$retailstoreid} workstationid{$workstationid} sequencenumber{$sequencenumber} and transactionJson{$transactionJson}")
    public void iEndTheTransaction(final String retailstoreid, final String workstationid, final String sequencenumber, final String transactionJson) {
        result = promotionRes.endTransaction(retailstoreid, workstationid, sequencenumber, transactionJson);
    }
    
    @Then("the Result Code is $resultcode")
    public final void theResultCodeIs(int expectedResultCode) {
        Assert.assertEquals("Expect the  result code", expectedResultCode,
                result.getNCRWSSResultCode());
    }
}
