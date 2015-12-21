package ncr.res.mobilepos.classinfo.model.test;

import ncr.res.mobilepos.classinfo.model.ClassInfo;
import ncr.res.mobilepos.pricing.model.Description;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.steps.Steps;
import static org.junit.Assert.*;

public class ClassInfoModelSteps extends Steps{
	ClassInfo classInfo;   
    
    public static  boolean HasErrors(final ClassInfo classInfo)
    {
        return !((null != classInfo.getDescription()) 
                && (null != classInfo.getDepartment()) && (null != classInfo.getLine())
                && (null != classInfo.getItemClass()));
    }
    
    @Given("an empty ClassInfo")
    public final void AClassInfoModel(){
    	classInfo = new ClassInfo();
    }  
    
    @Given("a ClassInfo with itemClass {$itemClass}, retailStoreId {$retailStoreId}, className {$className}, classNameLocal {$classNameLocal},"
            + " dpt {$dpt}, line {$line}")
    public final void aClassInfo(final String itemClass, final String retailStoreId, final String className, 
    		final String classNameLocal, final String dpt, final String line) {
    	Description description = new Description();
    	description.setEn(className);
    	description.setJa(classNameLocal);
    	
        classInfo = new ClassInfo(itemClass, retailStoreId, description, dpt, line);
    }
    
    @Then("it has no error")
    public final void hasNoError(){
        assertFalse(HasErrors(classInfo));
    }
    
    @Then("I should have a ClassInfo that has errors")
    public final void hasErrors(){
        assertTrue(HasErrors(classInfo));
    }
    
    @Then("its RetailStoreId is {$retailStoreId}")
    public final void lineRetailStoreId(final String retailStoreId){
        assertEquals(retailStoreId, classInfo.getRetailStoreId());
    }
    
    @Then("its Item Class is {$itemClass}")
    public final void itemClass(final String itemClass){
        assertEquals(itemClass, classInfo.getItemClass());
    }
    
    @Then("its Class Name is {$className}")
    public final void itsClassName(final String className){
        assertEquals(className, classInfo.getDescription().getEn());
    }
    
    @Then("its Class Name Local is {$classNameLocal}")
    public final void itsClassNameLocal(final String classNameLocal){
        assertEquals(classNameLocal, classInfo.getDescription().getJa());
    }       
    
    @Then("its DPT is {$dpt}")
    public final void itsDPT(final String dpt){
        assertEquals(dpt, classInfo.getDepartment());
    }
    
    @Then("its Line is {$line}")
    public final void itsLine(final String line){
        assertEquals(line, classInfo.getLine());
    }   
    
    @Then("I should have searched classes {$classString}")
    public final void getClasses(final String classString){
    	assertEquals(classString,classInfo.toString());
    }
    
}
