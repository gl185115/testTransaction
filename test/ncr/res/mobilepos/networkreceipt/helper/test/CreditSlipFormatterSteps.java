package ncr.res.mobilepos.networkreceipt.helper.test;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Locale;

import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.networkreceipt.helper.CreditSlipFormatter;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptFooter;
import ncr.res.mobilepos.networkreceipt.model.PaperReceiptPayment;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class CreditSlipFormatterSteps extends Steps{
    
    private PaperReceiptPayment payment;
    private PaperReceiptFooter footer;
    private Locale locale;
    private List<String> lines;
    private CreditSlipFormatter formatter;

    @BeforeScenario
    public final void SetUpClass(){
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @When("I ask for Credit Slip Formatter in language {$lan}")
    public final void getCreditSlipFormatter(final String lan){
        payment = new PaperReceiptPayment();
        payment.setCreditAmount("123456");
        payment.setCrCompanyCode("12");
        payment.setCompanyName("JCB");
        payment.setRecvCompanyCode("1234567");
        payment.setPanLast4("************1234");
        payment.setCaStatus("07");
        payment.setExpiryMaster("**/**");
        payment.setPaymentSeq("00035");
        payment.setApprovalCode("7654321");
        payment.setTraceNum("123456");
        payment.setSettlementNum("201206-00015027-00035-03111");
        
        footer = new PaperReceiptFooter();
        footer.setShopName("Demo Store");
        footer.setSaleMan("Naruto Uzumaki");
        footer.setRegisterNum("5027");
        footer.setTradeNum("0035");
        footer.setDepartmentName("Sales Space Name");
        footer.setHoldName("Event Name");
        
        if(lan.equals("ja")){
            locale = Locale.JAPAN;
        }
        if(lan.equals("en")){
            locale = Locale.ENGLISH;
        }
        
        formatter = new CreditSlipFormatter(35);
        lines = formatter.getCreditCardSlipFormat(payment, footer, lan);
    }
    
    @Then("I will get the Credit Slip Formatted list $text")
    public final void checkCreditSlip(String text){
        StringBuffer sb = new StringBuffer();
        for(int i = 0; i < lines.size(); i++){
            sb.append(lines.get(i));
            System.out.println(lines.get(i));
        }
        text = text.replaceAll("(\\r|\\t|\\n)", "");
        assertEquals(text, sb.toString());
    }
}
