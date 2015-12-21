package ncr.res.ue.message.response.test;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.action.ItemPoints;
import ncr.res.ue.message.response.AdjustmentResponse;
import ncr.res.ue.message.response.ConnectionInitializationResponse;
import ncr.res.ue.message.response.DiscountResponse;
import ncr.res.ue.message.response.ItemEntryResponse;
//import ncr.res.ue.message.response.PointsResponse;
//import ncr.res.ue.message.response.rewards.Points;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

public class PointsResponseTestSteps extends Steps{
/*    private PointsResponse resp = null;
    private String message = "";
    private String [] multiple = {"002200000031000000009300",
                                        "002200000031000000000300"};
    
    @When("I create points response with $messageType and ($message) expect ($expected)")
    public void createAdjustmentResponse(int messageType, String message, String expected) throws MessageException{
        try {
            resp = new PointsResponse(messageType, message);
            if(!expected.equals("success")){
                throw new MessageException("test fail - expected fail");
            }
        } catch (MessageException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            System.out.println(e.getMessage());
            if(expected.equals("success")){
                throw new MessageException("test fail - expected success");
            } else {
                String errorMessage = e.getMessage();
                assertThat(errorMessage, is(equalTo(expected)));
            }
        }
    }

    
    @Then("The return should contain ($messageType, $responseFlag)")
    public void validateMessage(int messageType, int responseFlag){
        assertThat(resp.getMessageType(), is(equalTo(messageType)));
        //assertThat(resp.getResponseFlag(), is(equalTo(responseFlag)));
    }
    
    @Then("the number of points should be $points")
    public void validateMessage(int points){
        assertThat(resp.getPoints().size(), is(equalTo(points)));

    }
    
    @Then("points at $index should contain : $params")
    public void checkPointsResp(int index, ExamplesTable param){
        Map<String,String> map = param.getRow(0);
        Points points = resp.getPoints().get(index-1);
        assertThat(points.getItemEntryid(), is(equalTo(map.get("itemEntryId").trim())));
        assertThat(points.getPricePrecision(), is(equalTo(map.get("pricePrecision").trim())));
        assertThat(points.getMatchNetUnitPrice(), is(equalTo(map.get("matchNetUnitPrice").trim())));
        assertThat(points.getItemQuantity(), is(equalTo(map.get("itemQuantity").trim())));
        assertThat(points.getProgramId(), is(equalTo(map.get("programId").trim())));
        assertThat(points.getQuantity(), is(equalTo(map.get("quantity").trim())));
        
    }
    @BeforeScenario
    public final void SetUpClass()
    {
        //By default Company ID is empty
        GlobalConstant.setCorpid("");
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDownClass(){
        Requirements.TearDown();
    }
*/
}