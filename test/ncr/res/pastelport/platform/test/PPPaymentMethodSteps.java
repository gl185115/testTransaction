package ncr.res.pastelport.platform.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;


import ncr.res.mobilepos.helper.Requirements;
import ncr.res.pastelport.platform.CommonTx;
import ncr.res.pastelport.platform.PPPaymentMethod;
import ncr.res.pastelport.platform.PastelPortTxSendImpl;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

import atg.taglib.json.util.JSONException;

public class PPPaymentMethodSteps extends Steps {
    PPPaymentMethod pppMethod;
    PastelPortTxSendImpl pptsb;
    CommonTx tx;
    String[] resultStr =new  String[14];
    String errorStr="";
    
    @BeforeScenario
    public void SetUpClass()
    {
    Requirements.SetUp();
    }

    @AfterScenario
    public void TearDown()
    {
    Requirements.TearDown();
    }

    @Given("I have a PPPaymentMethod")
    public void ASalesReportFieldsModel(){
    pppMethod = new PPPaymentMethod();
    }

    @Then("I should have a PPPaymentMethod")
    public void iHavePPPaymentMethod(){
    assertNotNull(pppMethod);
    }


    @When("I Play for PaymentMethod: $expected")
    public void testPaymentMethod(ExamplesTable expected) throws JSONException{
    pptsb=new PastelPortTxSendImpl();
    tx = new CommonTx();
    String[] sarray=new String[14];
    String[] sarrayinfoStrings=new String[14];
    Map<String, String> row = expected.getRows().get(0);
    for(int i =0;i<14;i++){
        sarray[i]=row.get("paymentmethod"+i);
        sarrayinfoStrings[i]=row.get("paymentinfo"+i);
    }
    tx.setFieldValue("service", row.get("service"));
    for (int i = 0; i < sarray.length; i++) {
        tx.setFieldValue("paymentmethod", sarray[i]);
        tx.setFieldValue("paymentinfo", sarrayinfoStrings[i]);

        resultStr[i]=pppMethod.paymentMethod(pptsb,tx);
    }
    }
    @Then("I should check the result: $expected")
    public void testCheck(ExamplesTable expected){

    String[] expends = new String[14];

    Map<String,String> row = expected.getRows().get(0);
    for(int i=0;i<14;i++){
        expends[i]= row.get("expends"+i);
    }

    for(int i=0;i<14;i++){

        assertEquals(expends[i],resultStr[i]);
        
    }

    }
    
    @When("I play for PaymentMethod error: $expected")
    public void testPaymentMethodError(ExamplesTable expected)
    throws JSONException{
        pptsb=new PastelPortTxSendImpl();
        tx = new CommonTx();
    
        Map<String, String> row = expected.getRows().get(0);
        tx.setFieldValue("service", row.get("service"));
          tx.setFieldValue("paymentmethod", row.get("paymentmethod"));
          tx.setFieldValue("paymentinfo", row.get("paymentinfo"));
       
        try {
            errorStr = pppMethod.paymentMethod(pptsb, tx);
        } catch (IndexOutOfBoundsException inooE) {
            errorStr = "IndexOutOfBoundsException";
        } catch (NumberFormatException nFE) {
            errorStr = "NumberFormatException";
        }
    }
    
    @Then("I should check the error")
    public void testErrorCheck(){
         assertEquals("IndexOutOfBoundsException",errorStr);
    }


}
