package ncr.res.mobilepos.report.model.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import ncr.res.mobilepos.report.model.SalesClerkReport;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;


public class SalesClerkReportSteps extends Steps{    
    SalesClerkReport salesclerkreport;
    boolean result;
    
    @Given("A Sales Clerk Report Model {$deviceNo} {$operatorNo}")
    public final void ASalesClerkReport(
            final String deviceNo, final String operatorNo){
        salesclerkreport = new SalesClerkReport(deviceNo, operatorNo);
        System.out.println(salesclerkreport);
    }

    @When("I test adding data to Operator Number {$data}")
    public final void addDataToOperNumber(final String data){
        result = false;
        if(salesclerkreport.addDataToOperNumber(data) == true) {
            result = true;
        }
    }
    
    @When("I test adding data to Name {$data}")
    public final void addDataToName(final String data){
        result = false;
        if(salesclerkreport.addDataToName(data) == true) {
            result = true;
        }
    }
    
    @When("I test adding data to Sales Customers {$data}")
    public final void addDataToSalesCustomers(final String data){
        result = false;
        if(salesclerkreport
                .addDataToSalesCustomers(data) == true) {
            result = true;
        }
    }
    
    @When("I test adding data to Sales Items {$data}")
    public final void addDataToSalesItems(final String data){
        result = false;
        if(salesclerkreport.addDataToSalesItems(data)
                == true) {
            result = true;
        }
    }
    
    @When("I test adding data to Sales Amount {$data}")
    public final void addDataToSalesAmount(final String data){
        result = false;
        if(salesclerkreport.addDataToSalesAmount(data)
                == true) {
            result = true;
        }
    }
    
    @Then("data should be added sucessfully")
    public final void shouldAddData(){
        assertThat(result, is(equalTo(true)));        
    }
}
