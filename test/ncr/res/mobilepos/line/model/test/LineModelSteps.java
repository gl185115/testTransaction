package ncr.res.mobilepos.line.model.test;

import ncr.res.mobilepos.line.model.Line;
import ncr.res.mobilepos.pricing.model.Description;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.steps.Steps;

import static org.junit.Assert.*;

public class LineModelSteps extends Steps{
	Line line;   
    
    public static  boolean HasErrors(final Line line)
    {
        return !((null != line.getDescription()) 
                && (null != line.getDepartment()) && (null != line.getLine()));
    }
    
    @Given("an empty Line")
    public final void ALineModel(){
    	line = new Line();
    }      
   
    @Given("a Line with Item line {$itemLine}, retailStoreId {$retailStoreId}, lineName {$lineName}, lineNameLocal {$lineNameLocal}, dpt {$dpt}")
    public final void aLine(final String itemLine, final String retailStoreId, final String lineName, 
    		final String lineNameLocal, final String dpt) {
    	Description description = new Description();
    	description.setEn(lineName);
    	description.setJa(lineNameLocal);    	
    	line = new Line(itemLine, retailStoreId, description, dpt);
    }
    
    @Then("it has no error")
    public final void hasNoError(){
        assertFalse(HasErrors(line));
    }
    
    @Then("I should have a Line that has errors")
    public final void hasErrors(){
        assertTrue(HasErrors(line));
    }
    
    @Then("its Item Line is {$itemLine}")
    public final void itemLine(final String itemLine){
    	assertEquals(itemLine, line.getLine());
    }
    
    @Then("its RetailStoreId is {$retailStoreId}")
    public final void lineRetailStoreId(final String retailStoreId){
        assertEquals(retailStoreId, line.getRetailStoreId());
    }
    
    @Then("its Line Name is {$lineName}")
    public final void itsLineName(final String lineName){
        assertEquals(lineName, line.getDescription().getEn());
    }
    
    @Then("its Line Name Local is {$lineNameLocal}")
    public final void itsLineNameLocal(final String lineNameLocal){
        assertEquals(lineNameLocal, line.getDescription().getJa());
    }       
    
    @Then("its DPT is {$dpt}")
    public final void itsDPT(final String dpt){
        assertEquals(dpt, line.getDepartment());
    } 
    
    @Then("I should have searched lines {$lineString}")
    public final void getClasses(final String lineString){
    	assertEquals(lineString,line.toString());
    }
    
}
