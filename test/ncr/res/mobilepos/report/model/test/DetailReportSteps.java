package ncr.res.mobilepos.report.model.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import ncr.res.mobilepos.report.model.DetailReport;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class DetailReportSteps extends Steps{
    DetailReport detailreport;
    boolean result;
    
    @Given("A Detail Report {$bizDate} {$storeNo} {$dpt} {$deviceNo}")
    public final void ADetailReport(final String bizDate, final String storeNo,
            final String dpt, final String deviceNo){
        detailreport = new DetailReport(bizDate, storeNo, dpt, deviceNo);
    }

    @When("I test adding data to timezone {$data}")
    public final void addDataToTimeZone(final String data){
        result = false;
        if(detailreport.addDataToTimeZone(data) == true) {
            result = true;
        }
    }
    
    @When("I test adding data to Customers {$data}")
    public final void addDataToCustomers(final String data){
        result = false;
        if(detailreport.addDataToCustomers(data) == true) {
            result = true;
        }
    }
    
    @When("I test adding data to Items {$data}")
    public final void addDataToItems(final String data){
        result = false;
        if(detailreport.addDataToItems(data) == true) {
            result = true;
        }
    }
    
    @When("I test adding data to Amount {$data}")
    public final void addDataToAmount(final String data){
        result = false;
        if(detailreport.addDataToAmount(data) == true) {
            result = true;
        }
    }
        
    @Then("data should be added sucessfully")
    public final void shouldAddData(){
        assertThat(result, is(equalTo(true)));        
    }
    
}
