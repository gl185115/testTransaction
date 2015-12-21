package ncr.res.mobilepos.report.model.test;

import static org.junit.Assert.assertNotNull;
import ncr.res.mobilepos.report.model.HourlyReport;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.steps.Steps;

public class HourlyReportSteps extends Steps{
    HourlyReport hourlyreport;

    @Given("A Hourly Report {$deviceNo} {$operatorNo}")
    public final void AHourlyReport(
            final String deviceNo, final String operatorNo){
        hourlyreport = new HourlyReport();
    }
    
    @Then("I Should have a Hourly Report")
    public final void IShouldHaveAHourlyReport(){
        assertNotNull(hourlyreport);
    }
}
