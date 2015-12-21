package ncr.res.mobilepos.helper.test;

import junit.framework.Assert;
import ncr.res.mobilepos.helper.PrinterDrawer;
import ncr.res.mobilepos.helper.Requirements;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.steps.Steps;

public class PrinterDrawerSteps extends Steps{
    PrinterDrawer printerDrawer;
    
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
    
    @Given("a PrinterDrawer using {$printerName}")
    public final void createPronterDrawer(final String printerName){
        printerDrawer = new PrinterDrawer(printerName);
    }
    
    @Then("I should have a PrinterDrawer.")
    public final void checkPrinterDrawer(){
        Assert.assertNotNull(printerDrawer);
    }

}
