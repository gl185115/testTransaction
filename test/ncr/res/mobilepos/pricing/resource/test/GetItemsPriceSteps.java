package ncr.res.mobilepos.pricing.resource.test;

import java.io.IOException;
import java.io.StringWriter;
import java.lang.reflect.Field;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.FormParam;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import ncr.res.mobilepos.credential.dao.SQLServerCredentialDAO;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.dao.SQLServerItemDAO;
import ncr.res.mobilepos.pricing.model.Department;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.ItemMaintenance;
import ncr.res.mobilepos.pricing.model.PickListItem;
import ncr.res.mobilepos.pricing.model.PickListItemType;
import ncr.res.mobilepos.pricing.model.SearchedProduct;
import ncr.res.mobilepos.pricing.model.SearchedProducts;
import ncr.res.mobilepos.pricing.resource.ItemResource;
import ncr.res.mobilepos.promotion.model.Transaction;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;
import org.junit.Assert;

import com.wordnik.swagger.annotations.ApiParam;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class GetItemsPriceSteps extends Steps {
    ItemResource itemres;
    DBInitiator dbInit;
    private List<Item> items;
    
    @BeforeScenario
    public final void setUpClass() throws Exception {
        Requirements.SetUp();
        dbInit = new DBInitiator("GetItemsPriceSteps", DATABASE.RESMaster);
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
        	context = Requirements.getMockServletContext();
            Field field = itemres.getClass().getDeclaredField("context");
            field.setAccessible(true);
            field.set(itemres, context);
        } catch (Exception ex) { 
            Assert.fail("Cannot Start the WebAPI");
        }
    }

    @When("I get item by plu using $companyid $storeid $businessday ($transaction)")
    public final void getItemByPLUCoderesource(String companyId, String storeId, String businessDate, String transaction) {
    	 items = itemres.getItemsPrice(transaction, storeId, companyId, businessDate);
    }
    
    @Then("I should get the following: $expected")
	public final void testPOSLogs(ExamplesTable expectedDataTable)
			throws IOException {
		Iterator<Item> itemIterator = items.listIterator();
		int i = 0;
		while (itemIterator.hasNext()) {
			Item item = itemIterator.next();
			assertEquals("", expectedDataTable.getRow(i).get("actualprice"),
					String.valueOf(item.getRegularSalesUnitPrice()));
			i++;
		}

	}
}
