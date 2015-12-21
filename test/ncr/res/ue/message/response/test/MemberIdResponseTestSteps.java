package ncr.res.ue.message.response.test;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.response.MemberIdResponse;
import java.util.Map;


import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;


public class MemberIdResponseTestSteps extends Steps{
    private MemberIdResponse resp = null;
    private String message = "";
    private String [] multiple = {"002200000031000000009300",
                                        "002200000031000000000300"};
    
    @When("I create member id response with $messageType and ($message) expect ($expected)")
    public void createAdjustmentResponse(int messageType, String message, String expected) throws MessageException{
        try {
            resp = new MemberIdResponse(messageType, message);
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
        assertThat(resp.getResponseFlag(), is(equalTo(responseFlag)));
    }
    
    @Then("I should get parameters : $param")
    public void createEndTransactionAction(ExamplesTable param){
        Map<String,String> map = param.getRow(0);
        /*|itemEntryID|entryFlag|returnFlag|itemCode|familyCode
        |department|clearanceLevel|pricePrecision|unitPrice|
        quantityType|quantitySubType|quantityPrecision|quantity|
        discountFlag|tlqFlag|alternatePrice|*/
        /*outMessage = new EndTransaction(Integer.parseInt(map.get("status").trim()),
                                    map.get("date").trim(),
                                    map.get("time").trim());*/
        assertThat(resp.getMemberId(), is(equalTo(map.get("memberid").trim())));
        assertThat(resp.getSecondaryId(), is(equalTo(map.get("secondaryid").trim())));
        assertThat(resp.getSecondaryIdType(), is(equalTo(map.get("secondaryidtype").trim())));
        assertThat(resp.getFirstName(), is(equalTo(map.get("firstname").trim())));
        assertThat(resp.getLastName(), is(equalTo(map.get("lastname").trim())));
        assertThat(resp.getMemberFlag(), is(equalTo(map.get("memberflag").trim())));
        
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
}
