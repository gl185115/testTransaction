package ncr.res.mobilepos.pricing.resource.test;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.Iterator;
import javax.servlet.ServletContext;

import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.pricing.model.PickList;
import ncr.res.mobilepos.pricing.model.PickListItem;
import ncr.res.mobilepos.pricing.model.PickListItemType;
import ncr.res.mobilepos.pricing.resource.ItemResource;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;
import org.junit.Assert;

public class GetPicklistSteps extends Steps {
    ItemResource itemres;
    PickList pickList;
    DBInitiator dbInit;
    private String itemType;

    @BeforeScenario
    public final void setUpClass() throws Exception {
        Requirements.SetUp();
        dbInit = new DBInitiator("ItemResourceSteps", DATABASE.RESMaster);
    }
    
    @AfterScenario
    public final void TearDownClass() {
        Requirements.TearDown();
    }

    @Given("entries for $dataset in database")
    public final void initdatasets(final String dataset) throws Exception {
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/pricing/resource/datasets/" + dataset + ".xml");
    }

    @Given("I have Item resource")
    public final void IHaveItemResource() {
    	ServletContext context = Requirements.getMockServletContext();
        itemres = new ItemResource();
        try 
        {
        	Field field = itemres.getClass().getDeclaredField("context");
            field.setAccessible(true);
            field.set(itemres, context);
        } catch (Exception ex) { 
            Assert.fail("Cannot Start the WebAPI");
        }
    }

    @When("I get picklist of $companyid $storeid $itemtype")
    public final void getItemByPLUCoderesource(final String companyId, final String storeId, final String itemType) {
    	this.itemType = itemType;
        pickList = itemres.getPickList(companyId, storeId, itemType);

    }
    
    @Then("the resultcode should be $resultCode")
    public final void resultCodeShouldBe(final int resultcode) {
        Assert.assertEquals("Asset the expected resultcode", pickList.getNCRWSSResultCode(), resultcode);
    }
 
	@Then("I should get the following: $expected")
	public final void testPOSLogs(ExamplesTable expectedDataTable)
			throws IOException {
		PickListItemType type = pickList.getItems().get(Integer.valueOf(this.itemType));
		Iterator<PickListItem> itemIterator = type.getItems().listIterator();
		int i = 0;
		while (itemIterator.hasNext()) {
			PickListItem item = itemIterator.next();
			assertEquals("", expectedDataTable.getRow(i).get("displayorder"),
					String.valueOf(item.getDisplayOrder()));
			assertEquals("", expectedDataTable.getRow(i).get("itemname"),
					item.getItemName());
			assertEquals("", expectedDataTable.getRow(i).get("itemtype"),
					String.valueOf(item.getItemType()));
			assertEquals("", expectedDataTable.getRow(i).get("mdinternal"),
					item.getMdInternal());
			i++;
		}

	}
}
