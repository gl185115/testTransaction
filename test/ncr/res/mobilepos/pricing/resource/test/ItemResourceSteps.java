package ncr.res.mobilepos.pricing.resource.test;

import java.io.StringWriter;
import java.lang.reflect.Field;
import javax.servlet.ServletContext;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import java.io.File;
import java.util.Map;
import java.util.Map.Entry;

import ncr.res.mobilepos.credential.dao.SQLServerCredentialDAO;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.dao.SQLServerItemDAO;
import ncr.res.mobilepos.pricing.factory.PriceMMInfoFactory;
import ncr.res.mobilepos.pricing.factory.PricePromInfoFactory;
import ncr.res.mobilepos.pricing.model.Department;
import ncr.res.mobilepos.pricing.model.ItemMaintenance;
import ncr.res.mobilepos.pricing.model.SearchedProduct;
import ncr.res.mobilepos.pricing.model.SearchedProducts;
import ncr.res.mobilepos.pricing.resource.ItemResource;
import ncr.res.mobilepos.barcodeassignment.model.BarcodeAssignment;
import ncr.res.mobilepos.helper.DataBinding;
import ncr.res.mobilepos.barcodeassignment.model.MultiForwardRecallCard;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import org.jbehave.core.model.ExamplesTable;
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
    BarcodeAssignment barcodeAssignment;

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
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
				"test/resources/para/mst_bizday.xml");
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
    public final void getItemByPLUCoderesource(String storeid, String pluCode, String companyId, String businessDay) throws Exception {
        if (storeid.equals("null")) {
            storeid = null;
        }
        if (pluCode.equals("null")) {
            pluCode = null;
        }

        if (!StringUtility.isNullOrEmpty(companyId) && !StringUtility.isNullOrEmpty(storeid)) {
			PricePromInfoFactory.initialize(companyId,storeid, null, null, null, null);
			PriceMMInfoFactory.initialize(companyId,storeid, null);
		}

        actualProduct = itemres.getItemByPLUcode(storeid, pluCode, companyId, businessDay);
        expctdResultCode = actualProduct.getNCRWSSResultCode();
    }

    @Then("I should have item with promo values $tagcode, $taxtype, $discounttype, $mustbuyflag, $salesitemflag")
    public final void checkPromotionValues(String tagcode, String taxtype, String discounttype, int mustbuyflag, int salesitemflag) {
        if (tagcode.equals("null")) {
            tagcode = null;
        }
        if (discounttype.equals("null")) {
        	discounttype = null;
        }
        if (taxtype.equals("null")){
        	taxtype = null;
        }
        assertThat(actualProduct.getItem().getMixMatchCode(), is(equalTo(tagcode)));
        assertThat(actualProduct.getItem().getTaxType(), is(equalTo(taxtype)));
        assertThat(actualProduct.getItem().getDiscountType(), is(equalTo(discounttype)));
        assertThat(actualProduct.getItem().getMustBuyFlag(), is(equalTo(mustbuyflag)));
        assertThat(actualProduct.getItem().getNonSales(), is(equalTo(salesitemflag)));
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

    @Given("ItemCode.xml file exist")
    public final void iHaveItemCodeXML() {
        Field barcodeAssignmentField;
        try {
            File configFile = new File("test\\ncr\\res\\mobilepos\\pricing\\resource\\datasets" + File.separator + "itemCode.xml");
            DataBinding<BarcodeAssignment> serializer = new DataBinding<BarcodeAssignment>(BarcodeAssignment.class);
            BarcodeAssignment barcodeAssignment = serializer.unMarshallXml(configFile);

            barcodeAssignmentField = itemres.getClass().getDeclaredField("barcodeAssignment");
            barcodeAssignmentField.setAccessible(true);
            barcodeAssignmentField.set(itemres, barcodeAssignment);
        } catch (NoSuchFieldException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (JAXBException e) {
            e.printStackTrace();
        }
    }

    @When("I get barcode info")
    public final void getBarcodeInfo() {
        this.barcodeAssignment = itemres.getBarcodeInfo();
    }

    @Then("I should get the following: $expectedBarcodeInfo")
    public final void testBarcodeInfo(final ExamplesTable expectedBarcodeInfo) {
        int i = 0;
        MultiForwardRecallCard multiForwardRecallCard = this.barcodeAssignment.getMultiForwardRecallCard();
        Assert.assertEquals("Compare the size of multiForwardRecallCard", multiForwardRecallCard.getMultiForwardRecallCards().size(), expectedBarcodeInfo.getRowCount());

        for (Map<String, String> expectedItem : expectedBarcodeInfo.getRows()) {
            assertThat("Compare the id",
                    "" + multiForwardRecallCard.getMultiForwardRecallCards().get(i).getId(),
                    is(equalTo(expectedItem.get("id"))));
            assertThat("Compare the description",
                    "" + multiForwardRecallCard.getMultiForwardRecallCards().get(i).getDescription(),
                    is(equalTo(expectedItem.get("description"))));
            assertThat("Compare the type",
                    "" + multiForwardRecallCard.getMultiForwardRecallCards().get(i).getType(),
                    is(equalTo(expectedItem.get("type"))));
            assertThat("Compare the format",
                    "" + multiForwardRecallCard.getMultiForwardRecallCards().get(i).getFormat(),
                    is(equalTo(expectedItem.get("format"))));
            i++;
        }
    }
}
