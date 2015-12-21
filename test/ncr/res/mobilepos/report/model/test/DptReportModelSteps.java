package ncr.res.mobilepos.report.model.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import ncr.res.mobilepos.report.model.DepartmentReport;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class DptReportModelSteps extends Steps{
    DepartmentReport dptreport;
    boolean result;
    
    @Given("a Department report {$deviceNo} {$operatorNo}")
    public final void DepartmentReport(final String deviceNo,
            final String operatorNo){
        dptreport = new DepartmentReport();        
    }
    
    @When("I test adding data to DPT Num {$data}")
    public final void addDataToDPTNum(final String data){
        result = false;
        if(dptreport.addDataToDPTNum(data) == true) {
            result = true;
        }
    }
        
    @When("I test adding data to DPT Name {$data}")
    public final void addDataToDPTName(final String data){        
        result = false;
        if(dptreport.addDataToDPTName(data) == true) {
            result = true;
        }
    }
        
    @When("I test adding data to Customers {$data}")
    public final void addDataToCustomers(final String data){
        result = false;
        if(dptreport.addDataToCustomers(data) == true) {
            result= true;
        }
    }
    
    @When("I test adding data to Items {$data}")
    public final void addDataToItems(final String data){
        result = false;
        if(dptreport.addDataToItems(data) == true) {
            result= true;
        }
    }
    
    @When("I test adding data to Amount {$data}")
    public final void addDataToAmount(final String data){
        result = false;
        if(dptreport.addDataToAmount(data) == true) {
            result= true;
        }
    }
        
    @Then("data should be added sucessfully")
    public final void shouldAddData(){
        assertThat(result, is(equalTo(true)));        
    }
}
