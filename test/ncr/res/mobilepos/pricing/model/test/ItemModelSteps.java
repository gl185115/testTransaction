package ncr.res.mobilepos.pricing.model.test;
import static org.hamcrest.CoreMatchers.*;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.pricing.model.Description;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.SearchedProducts;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.steps.Steps;

import static org.junit.Assert.*;

public class ItemModelSteps extends Steps{
    Item item;
    SearchedProducts searchedprod;
    
    public static  boolean HasErrors(final Item it)
    {
        return !((null != it.getDescription()) && (0.0 != it.getRegularSalesUnitPrice())
                && (null != it.getDepartment()) && (null != it.getLine())
                && (null != it.getItemClass()));
    }
    
    @Given("an empty Item")
    public final void AnItemModel(){
        item = new Item();
    }
        
    @Given("an Item with plu {$plu}, mdName {$mdName}, mdNameLocal {$mdNameLocal},"
            + " salesPrice {$salesPrice}, dpt {$dpt}, line {$line}, clas {$clas}")
    public final void AnItem(final String plu, final String mdName, 
    		final String mdNameLocal, final double salesPrice, 
    		final String dpt, final String line, final String clas) {
    	Description description = new Description();
    	description.setEn(mdName);
    	description.setJa(mdNameLocal);
        item = new Item(plu, description, salesPrice, dpt, line, clas, 0, null, null, 0, "");
    }
    
    @Then("it has no error")
    public final void hasNoError(){
        assertFalse(HasErrors(item));
    }
    
    @Then("I should have an Item that has errors")
    public final void hasErrors(){
        assertTrue(HasErrors(item));
    }
    
    @Then("its PLU is {$plu}")
    public final void itsPlu(final String plu){
        assertEquals(plu, item.getItemId());
    }
    
    @Then("its md Name is {$mdName}")
    public final void itsMDName(final String mdName){
        assertEquals(mdName, item.getDescription().getEn());
    }
    
    @Then("its md Name Local is {$mdNameLocal}")
    public final void itsMDNameLocal(final String mdNameLocal){
        assertEquals(mdNameLocal, item.getDescription().getJa());
    }
    
    @Then("its Sales Price is {$salesPrice}")
    public final void itsSalesPrice(final Double salesPrice){
        assertThat(salesPrice,is(equalTo(item.getRegularSalesUnitPrice())));
    }
    
    @Then("its DPT is {$dpt}")
    public final void itsDPT(final String dpt){
        assertEquals(dpt, item.getDepartment());
    }
    
    @Then("its Line is {$line}")
    public final void itsLine(final String line){
        assertEquals(line, item.getLine());
    }
    
    @Then("its Class is {$clas}")
    public final void itsClass(final String clas){
        assertEquals(clas, item.getItemClass());
    }
        
    @Then("I should have searched items")
    public final void getSearchedItems(){
        assertNotNull(searchedprod.getItems());
    }
    
}
