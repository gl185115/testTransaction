package ncr.res.mobilepos.report.model.test;

import static org.junit.Assert.assertEquals;
import ncr.res.mobilepos.report.model.ReportColumn;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class ReportColumnSteps extends Steps{
    ReportColumn reportcolumn;
    
    @Given("a Report Column {$name}")
    public final void AReportColumn(final String name){
        reportcolumn = new ReportColumn(name);
    }
    
    @Then("its Name is {$name}")
    public final void ItsName(final String name){        
        assertEquals(name, reportcolumn.getName());
    }
    
    @Then("its Width is {$width}")
    public final void ItsWidth(final float width){        
        assertEquals(width, reportcolumn.getWidth(), 50.0);
    }
        
    @When("I add data {$data}")
    public final void addData(final String data){        
        reportcolumn.addData(data);
    }
    
    @Then("I should have a data {$data}")
    public final void IShouldHaveAData(final String data){
        assertEquals(data, reportcolumn.getData());
    }
    
    
    
}
