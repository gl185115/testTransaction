package ncr.res.mobilepos.report.model.test;

import static org.junit.Assert.assertEquals;
import ncr.res.mobilepos.report.model.ReportHeader;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.steps.Steps;

public class ReportHeaderSteps extends Steps{
    ReportHeader reportheader;
    
    @Given("I have a report header {$name} {$value} {$position}")
    public final void IHaveAReportHeader(final String name,
            final String value, final int position){
            reportheader = new ReportHeader(name, value, position);
    }
    
    @Then("its name is {$name}")
    public final void itsName(final String name){        
        assertEquals(name, reportheader.getName());
    }
    
    @Then("its value is {$value}")
    public final void itsValue(final String value){
        assertEquals(value, reportheader.getValue());
    }
    
    @Then("its position is {$position}")
    public final void itsPosition(final int position){
        assertEquals(position, reportheader.getPosition());
    }    
}
