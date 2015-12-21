package ncr.res.mobilepos.pricing.helper.test;

import static org.hamcrest.CoreMatchers.*;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.pricing.helper.PLUConverter;
import ncr.res.mobilepos.pricing.model.Description;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.systemconfiguration
        .resource.SystemConfigurationResource;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

import static org.junit.Assert.*;

public class PLUConverterSteps extends Steps{
    Item item;
    String pluCode;
    PLUConverter pluConverter;
    DBInitiator dbInit;
    ServletContext servletContext;

    @BeforeScenario
    public final void setUpClass() throws Exception {  
        Requirements.SetUp();
        dbInit = new DBInitiator("PLUConverterSteps", DATABASE.RESMaster);
        pluConverter = new PLUConverter();
    }

    @AfterScenario
    public final void TearDownClass(){
        Requirements.TearDown();
    }

    @Given("entries for {$dataset} in database")
    public final void initdatasets(final String dataset) throws Exception{
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/pricing/helper/test/"
                + dataset + ".xml");
    }
    
    @Given("the ServletContext")
    public final void theServletContext(){
        servletContext = Requirements.getMockServletContext();
        pluConverter.setContext(servletContext);
    }

    @Given("a plu {$plu}")
    public final void SetPlu(final String plu){
        pluCode = plu;
    }
    
    @Then("I should get converted-plu {$convertedPlu}")
    public final void AssertPlu(final String convertedPlu) throws DaoException {
        assertEquals(convertedPlu, pluConverter.convertPluCode(pluCode));
    }
    
    @Given("an Item with plu {$plu}, mdName {$mdName}, mdNameLocal {$mdNameLocal}, "
    		+ "salesPrice {$salesPrice}, dpt {$dpt}, line {$line}, clas {$clas}")
    public final void SetItem(
            final String plu, final String mdName, final String mdNameLocal, 
            final double salesPrice, final String dpt, final String line, 
            final String clas) {
    	Description description = new Description();
    	description.setEn(mdName);
    	description.setJa(mdNameLocal);
    	
        item = new Item(plu, description, salesPrice, dpt, line, clas, 0, null, null, 0, "");
        pluConverter.convertItem(pluCode, item);
    }
    
    @Then("I should get converted-item with plu {$plu}")
    public final void AssertPluOfItem(final String plu){
        assertEquals(plu, item.getItemId());
    }

    @Then("I should get converted-item with mdName {$mdName}")
    public final void AssertMdNameOfItem(final String mdName){
        assertEquals(mdName, item.getDescription().getEn());
    }

    @Then("I should get converted-item with mdNameLocal {$mdNameLocal}")
    public final void AssertMdNameLocalOfItem(final String mdNameLocal){
        assertEquals(mdNameLocal, item.getDescription().getJa());
    }
    
    @Then("I should get converted-item with salesPrice {$salesPrice}")
    public final void AssertSalesPriceOfItem(final Double salesPrice){
        assertThat(salesPrice,is(equalTo(item.getRegularSalesUnitPrice())));
    }
    
    @Then("I should get converted-item with dpt {$dpt}")
    public final void AssertDptOfItem(final String dpt){
        assertEquals(dpt, item.getDepartment());
    }
    
    @Then("I should get converted-item with line {$line}")
    public final void AssertLineOfItem(final String line){
        assertEquals(line, item.getLine());
    }
    
    @Then("I should get converted-item with clas {$clas}")
    public final void AssertClasOfItem(final String clas){
        assertEquals(clas, item.getItemClass());
    }
}
