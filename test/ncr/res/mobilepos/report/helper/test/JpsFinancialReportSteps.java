package ncr.res.mobilepos.report.helper.test;

import java.awt.Font;
import java.io.UnsupportedEncodingException;

import junit.framework.Assert;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.report.helper.JpsFinancialReport;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class JpsFinancialReportSteps extends Steps{

    JpsFinancialReport jpsFinancialReport;
    String printerName;
    boolean result;
    Font font = new Font("ÇlÇr ÉSÉVÉbÉN", Font.PLAIN, 12);
    
    @BeforeScenario
    public final void SetUpClass()
    {        
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown()
    {
        Requirements.TearDown();
    }
    
    @Given("a JpsFinancialReport using {$printerName}")
    public final void getJpsFinancialReport(final String printerNameToSet){
        jpsFinancialReport = new JpsFinancialReport(printerNameToSet);
    }
    
    @When("add Item and value using {$item} and {$value}")
    public final void addItemStr(final String item, final String value){
        try {
            result = jpsFinancialReport.addItemStr(item, value, font);
        } catch (UnsupportedEncodingException e) {
            result = false;
        }
    }
    
    @When("add Text using {$text} and the type {$type}")
    public final void addText(final String text, final int type){
        try {
            result = jpsFinancialReport.addText(text, type, font);
        } catch (UnsupportedEncodingException e) {
            result = false;
        }
    }
    
    @When("add Item and value left using {$item}"
            + " and {$value} in align {$align}")
    public final void addItemStrLeft(final String item, final String value,
            final int align){
        try {
            result = jpsFinancialReport
                            .addItemStrLeft(item, value, align, font);
        } catch (UnsupportedEncodingException e) {
            result = false;
        }
    }
    
    @Then("I should have a JpsFinancialRepoert.")
    public final void checkJpsFinancialReport(){
        Assert.assertNotNull(jpsFinancialReport);
    }
    
    @Then("it should be true.")
    public final void successFinancialReport(){
        Assert.assertEquals(true, result);
    }
    
    @Then("it should be false.")
    public final void failedFinancialReport(){
        Assert.assertEquals(false, result);
    }
}
