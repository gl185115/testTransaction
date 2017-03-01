package ncr.res.mobilepos.pricing.resource.test;

import java.io.StringWriter;
import java.lang.reflect.Field;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;

import ncr.res.mobilepos.credential.dao.SQLServerCredentialDAO;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.dao.SQLServerItemDAO;
import ncr.res.mobilepos.pricing.model.Department;
import ncr.res.mobilepos.pricing.model.ItemMaintenance;
import ncr.res.mobilepos.pricing.model.SearchedProduct;
import ncr.res.mobilepos.pricing.model.SearchedProducts;
import ncr.res.mobilepos.pricing.resource.ItemResource;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import org.junit.Assert;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;

public class ItemResourceSteps extends Steps {
    ItemResource itemres;
    SearchedProducts searchedprod;
    SearchedProduct actualProduct;
    ResultBase resultbase;
    ServletContext context;
    SQLServerCredentialDAO credentialserver;
    DBInitiator dbInit;
    Department department;
    SQLServerItemDAO dptdao;
    int expctdResultCode;
    ItemMaintenance itemmaintenance;

    public static boolean ResourceHasErrors(final SearchedProducts sd) {
        return !((0 != sd.getNCRWSSExtendedResultCode())
                && (null != sd.getItems()) && (null != sd.getMessage()) && (0 != sd
                .getNCRWSSResultCode()));
    }

    @BeforeScenario
    public final void setUpClass() throws Exception {
        Requirements.SetUp();
        dbInit = new DBInitiator("ItemResourceSteps", DATABASE.RESMaster);
    }

    @Given("entries for $dataset in database")
    public final void initdatasets(final String dataset) throws Exception {
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/pricing/resource/datasets/" + dataset + ".xml");
    }

    @AfterScenario
    public final void TearDownClass() {
        Requirements.TearDown();
    }

    @Given("I have Item resource")
    public final void IHaveItemResource() {
    	this.context = Requirements.getMockServletContext();
        itemres = new ItemResource();
        try 
        {
        	this.context = Requirements.getMockServletContext();
            Field context = itemres.getClass().getDeclaredField("context");
            context.setAccessible(true);
            context.set(itemres, this.context);
        } catch (Exception ex) { 
            Assert.fail("Cannot Start the WebAPI");
        }
    }

    @When("I get item by plu using $storeid $plucode $deviceid $businessday")
    public final void getItemByPLUCoderesource(String storeid, String pluCode, String companyId, String businessDay) {
        if (storeid.equals("null")) {
            storeid = null;
        }
        if (pluCode.equals("null")) {
            pluCode = null;
        }

        actualProduct = itemres.getItemByPLUcode(storeid, pluCode, companyId, businessDay);
        expctdResultCode = actualProduct.getNCRWSSResultCode();
    }
    
    @Then("I should have item with promo values $tagcode, $taxtype, $discounttype, $mustbuyflag, $salesitemflag")
    public final void checkPromotionValues(String tagcode, int taxtype, int discounttype, int mustbuyflag, int salesitemflag) {
        if (tagcode.equals("null")) {
            tagcode = null;
        }
        assertThat(actualProduct.getItem().getMixMatchCode(), is(equalTo(tagcode)));
        assertThat(actualProduct.getItem().getTaxType(), is(equalTo(taxtype)));
        assertThat(actualProduct.getItem().getDiscountType(), is(equalTo(discounttype)));
        assertThat(actualProduct.getItem().getMustBuyFlag(), is(equalTo(mustbuyflag)));
        assertThat(actualProduct.getItem().getNonSales(), is(equalTo(salesitemflag)));
    }

    @When("I get the list of items using $storeid $key $deviceid $name")
    public final void getListOfItems(final String storeid,final String key,final String deviceid,final String name) {
        String storeidTemp = storeid.equals("null") ? null : storeid;
        String keyTemp = key.equals("null") ? null : key;
        String nameTemp = name.equals("null") ? null : name;      
        searchedprod = itemres.list(storeidTemp, keyTemp, deviceid, 0, nameTemp); 
        expctdResultCode = searchedprod.getNCRWSSResultCode();
    }

    @When("I change the price of item $storeid $pluCode to $unitPrice")
    public final void IChangeThePrice(final String storeid,
            final String pluCode, final String unitPrice) {
        itemmaintenance = itemres.changePrice(storeid, pluCode, unitPrice);
        expctdResultCode = itemmaintenance.getNCRWSSResultCode();
    }

    @Then("I should have $count items")
    public final void IHaveItemsof(final int count) {

        assertThat(searchedprod.getItems().size() - 1, is(equalTo(count)));
    }

    @Then("I should have a xml: $xml")
    public final void IHaveAnItem(final String xml) {
        try {
            StringWriter writer = new StringWriter();
            JAXBContext jaxbcontext;

            jaxbcontext = JAXBContext.newInstance(SearchedProducts.class);

            Marshaller m = jaxbcontext.createMarshaller();
            m.marshal(searchedprod, writer);
            Assert.assertEquals("Assert the xml String",
                    xml, writer.toString());


        } catch (JAXBException e) {    
            e.printStackTrace();
        }
    }

    @Then("I should have a xml for item: $xml")
    public final void IHaveAnItemForItem(final String xml) {
        try {
            StringWriter writer = new StringWriter();
            JAXBContext jaxbcontext;
            jaxbcontext = JAXBContext.newInstance(SearchedProduct.class);
            Marshaller m = jaxbcontext.createMarshaller();
            m.marshal(actualProduct, writer);
            Assert.assertEquals("Assert the xml String",
                    xml, writer.toString());

        } catch (JAXBException e) {   
            e.printStackTrace();
        }
    }

    @Then("I should have a maintenance xml: $xml")
    public final void IHaveAnMaintenanceItem(final String xml) {
        try {
            StringWriter writer = new StringWriter();
            JAXBContext jaxbcontext;
            jaxbcontext = JAXBContext.newInstance(ItemMaintenance.class);
            Marshaller m = jaxbcontext.createMarshaller();
            m.marshal(itemmaintenance, writer);
            assertThat(writer.toString(), is(equalTo(xml)));

        } catch (JAXBException e) {

            e.printStackTrace();
        }
    }



    @Then("the resultcode should be $resultCode")
    public final void resultCodeShouldBe(final int resultcode) {
        Assert.assertEquals("Asset the expected resultcode", expctdResultCode, resultcode);
    }

    @Then("I should get resultcode $resultCode")
    public final void testResultCode(final int actualResultCode) {
        Assert.assertEquals("Assert the expected resultcode",
                expctdResultCode, actualResultCode);
    }
}
