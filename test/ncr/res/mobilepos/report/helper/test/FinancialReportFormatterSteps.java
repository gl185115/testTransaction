package ncr.res.mobilepos.report.helper.test;

import static org.junit.Assert.assertEquals;
import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.report.helper.FinancialReportFormatter;
import ncr.res.mobilepos.report.model.FinancialReport;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class FinancialReportFormatterSteps  extends Steps{
    
    private FinancialReport financialReport;
    private FinancialReportFormatter financialReportFormatter;
    private List<String> reportLines;
    private Locale locale;

    @BeforeScenario
    public final void SetUpClass(){
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @When("I ask for financial report text list in language {$lan}")
    public final void getFinancialReportText(final String lan){
        
        financialReport = new FinancialReport();
        financialReport.setStoreid("9999");
        financialReport.setStoreName("Demo Store");
        financialReport.setDeviceNo("1111");
        financialReport.setBusinessDate("2012-06-05");
        financialReport.setGrossSalesRevenue(new BigDecimal("123456"));
        financialReport.setDiscountSale(new BigDecimal("0"));
        financialReport.setReturnSale(new BigDecimal("0"));
        financialReport.setVoidSale(new BigDecimal("0"));
        financialReport.setCash(new BigDecimal("123456"));
        financialReport.setCreditCard(new BigDecimal("0"));
        financialReport.setMiscellaneous(new BigDecimal("0"));
        
        if(lan.equals("ja")){
            locale = Locale.JAPAN;
        }
        if(lan.equals("en")){
            locale = Locale.ENGLISH;
        }
        financialReportFormatter = new FinancialReportFormatter(lan);
        reportLines = financialReportFormatter
                        .getFinancialReportText(financialReport);
    }
    
    @Then("I will get financial report text list $text")
    public final void checkFinancialReportText(String text){
        StringBuffer sb = new StringBuffer();
        
        for(int i = 0; i < 19; i++){
            sb.append(reportLines.get(i));
        }
        text = text.replaceAll("(\\r|\\t|\\n)", "");
        assertEquals(text, sb.toString());
    }
    
}
