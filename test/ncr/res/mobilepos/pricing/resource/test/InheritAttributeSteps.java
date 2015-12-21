package ncr.res.mobilepos.pricing.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.pricing.model.SearchedProduct;
import ncr.res.mobilepos.pricing.resource.ItemResource;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class InheritAttributeSteps extends Steps {
	
	private ItemResource itemResource;
	private ServletContext context;
	private SearchedProduct item;
	
    private DBInitiator dbInitMaster;

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        dbInitMaster = new DBInitiator("InheritAttributeSteps", DATABASE.RESMaster);
    }

    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }

    @Given("I have Item resource")
    public final void iHaveItemResource() {
    	this.itemResource = new ItemResource();
        this.context = Requirements.getMockServletContext();        
        try {
        	this.context = Requirements.getMockServletContext();
            Field context = itemResource.getClass().getDeclaredField("context");
            context.setAccessible(true);
            context.set(itemResource, this.context);
        } catch (Exception ex) { 
            Assert.fail("Cannot Start the WebAPI");
        }
    }

    @Given("a database data {$filename}")
    public final void aDatabaseData(String filename) {
    	try {
    		dbInitMaster.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    "test/ncr/res/mobilepos/pricing/resource/test/"
                    + filename + ".xml");
        } catch (Exception e) {
            Assert.fail("Cant set the database for item.");
        }
    }

    @When("I get item with storeId{$storeId} and pluCode{$pluCode}")
    public final void getItem(String storeId, String pluCode, String companyId) {
        if (storeId.equals("null")) {
        	storeId = null;
        }
        if (pluCode.equals("null")) {
        	pluCode = null;
        }

        item = this.itemResource.getItemByPLUcode(storeId, pluCode, companyId, null);
    }

	@Then("the item JSON should be: $expected")
	public final void theItemJsonShouldBe(String expected) {
		try {
			JsonMarshaller<SearchedProduct> jsonMarshaller = 
					new JsonMarshaller<SearchedProduct>();
			String actual = jsonMarshaller.marshall(item);
			
			System.out.println(actual);
			
			// compare to json strings regardless of property ordering
			JSONAssert.assertEquals(expected, actual, JSONCompareMode.NON_EXTENSIBLE);
		} catch (Exception e) {
			Assert.fail("Failed to verify the PromotionResponse JSON format");
		}
	}
    
    @Then("item {$attribute} is {$value}")
    public final void compareAttributeValue(String attribute, String value) {
    	String actual = "";
    	if (attribute.equalsIgnoreCase("DiscountType")) {
    		actual = String.valueOf(item.getItem().getDiscountType());
    	}
    	
    	assertThat(actual, is(equalTo(value)));
    }
    
    @Then("item attributes should be: $expected")
    public final void compareItemAttributeValues(ExamplesTable expected) {
    	for(Map<String, String> expecedItem : expected.getRows()) {
            assertThat("Compare the ItemId", item.getItem().getItemId(),
                    is(equalTo(expecedItem.get("ItemId"))));
            assertThat("Compare the Department", item.getItem().getDepartment(),
                    is(equalTo(expecedItem.get("Department"))));
            assertThat("Compare the Line", item.getItem().getLine(),
                    is(equalTo(expecedItem.get("Line"))));
            assertThat("Compare the Class", item.getItem().getItemClass(),
                    is(equalTo(expecedItem.get("Class"))));
            assertThat("Compare the DiscountType", String.valueOf(item.getItem().getDiscountType()),
                    is(equalTo(expecedItem.get("DiscountType"))));
            assertThat("Compare the DiscountFlag", String.valueOf(item.getItem().getDiscountFlag()),
                    is(equalTo(expecedItem.get("DiscountFlag"))));
            assertThat("Compare the DiscountAmt", String.valueOf(item.getItem().getDiscountAmount()),
                    is(equalTo(expecedItem.get("DiscountAmt"))));
            assertThat("Compare the DiscountRate", String.valueOf(item.getItem().getDiscount()),
                    is(equalTo(expecedItem.get("DiscountRate"))));
            assertThat("Compare the TaxType", String.valueOf(item.getItem().getTaxType()),
                    is(equalTo(expecedItem.get("TaxType"))));
            assertThat("Compare the TaxRate", String.valueOf(item.getItem().getTaxRate()),
                    is(equalTo(expecedItem.get("TaxRate"))));
            assertThat("Compare the AgeRestrictedFlag", String.valueOf(item.getItem().getAgeRestrictedFlag()),
                    is(equalTo(expecedItem.get("AgeRestrictedFlag"))));
            assertThat("Compare the PosMdType", String.valueOf(item.getItem().getNonSales()),
                    is(equalTo(expecedItem.get("PosMdType"))));
            assertThat("Compare the SubInt10", String.valueOf(item.getItem().getSubInt10()),
                    is(equalTo(expecedItem.get("SubInt10"))));
    		
    	}
    }

}
