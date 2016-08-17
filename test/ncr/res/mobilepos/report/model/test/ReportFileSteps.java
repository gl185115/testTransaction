package ncr.res.mobilepos.report.model.test;

import ncr.res.mobilepos.report.model.ReportFile;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.steps.Steps;
import static org.junit.Assert.*;

public class ReportFileSteps extends Steps{
    String reportfile;    
    
    @Given("I have a Report file and read financial report")
    public final void readFinancialReport(){
        try {
            reportfile = ReportFile.readFinancialRpt();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Then("it must have a report")
    public final void HaveReport(){
        assertNotNull(reportfile);        
    }
    
}
