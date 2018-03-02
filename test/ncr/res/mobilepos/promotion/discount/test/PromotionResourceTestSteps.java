package ncr.res.mobilepos.promotion.discount.test;

import junit.framework.Assert;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import java.io.File;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.barcodeassignment.factory.BarcodeAssignmentFactory;
import ncr.res.mobilepos.barcodeassignment.model.BarcodeAssignment;
import ncr.res.mobilepos.constant.EnvironmentEntries;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.factory.PriceMMInfoFactory;
import ncr.res.mobilepos.pricing.factory.PricePromInfoFactory;
import ncr.res.mobilepos.pricing.resource.ItemResource;
import ncr.res.mobilepos.promotion.helper.TerminalItem;
import ncr.res.mobilepos.promotion.model.Discount;
import ncr.res.mobilepos.promotion.model.MixMatchItem;
import ncr.res.mobilepos.promotion.model.Promotion;
import ncr.res.mobilepos.promotion.model.PromotionResponse;
import ncr.res.mobilepos.promotion.model.Sale;
import ncr.res.mobilepos.promotion.model.Transaction;
import ncr.res.mobilepos.promotion.resource.PromotionResource;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

@SuppressWarnings("deprecation")
public class PromotionResourceTestSteps extends Steps {
	private ServletContext servletContext = null;
	private PromotionResource testpromotionResource = null;
	private Map<String, TerminalItem> actualTerminalItems = null;
	private ResultBase actualResultBase = null;
	private DBInitiator dbInit = null;

	@BeforeScenario
	public final void SetUpClass() {
		dbInit = new DBInitiator("SQLServerItemDAOSteps", DATABASE.RESMaster);
		Requirements.SetUp();
	}

	@AfterScenario
	public final void TearDownClass() {
		Requirements.TearDown();
	}

	@Given("a Promotion Service")
	public void aPromotionService() {
		testpromotionResource = new PromotionResource();
		Field promotioncontext;
		Field itemcontext;
		try {
			ItemResource itemResourceObj = new ItemResource();
			promotioncontext = testpromotionResource.getClass()
					.getDeclaredField("context");
			itemcontext = itemResourceObj.getClass()
					.getDeclaredField("context");
			promotioncontext.setAccessible(true);
			itemcontext.setAccessible(true);
			promotioncontext.set(testpromotionResource, servletContext);
			itemcontext.set(itemResourceObj, servletContext);
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/resources/para/mst_bizday.xml");
		} catch (Exception ex) {
			Assert.fail("Can't Retrieve Servlet context from promotion.");
		}
	}

	@Given("ItemCode.xml file does not exist")
	public void itemCodeXmlNotFound() {
		Field promotionConfigField;
		try {
			promotionConfigField = testpromotionResource.getClass().getDeclaredField("barcodeAssignment");
			promotionConfigField.setAccessible(true);
			promotionConfigField.set(testpromotionResource, null);
		} catch (NoSuchFieldException e) {
			e.printStackTrace();
		} catch (SecurityException e) {
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			e.printStackTrace();
		}
	}
	
	@Given("ItemCode.xml file exist")
	public void giftXmlFound() {
		Field promotionConfigField;
		try {
			File configFile = new File("test\\ncr\\res\\mobilepos\\promotion\\discount\\test" + File.separator + "itemCode.xml");
			XmlSerializer<BarcodeAssignment> serializer = new XmlSerializer<BarcodeAssignment>();
			BarcodeAssignment barcodeAssignment = serializer.unMarshallXml(configFile, BarcodeAssignment.class);
			
			promotionConfigField = testpromotionResource.getClass().getDeclaredField("barcodeAssignment");
			promotionConfigField.setAccessible(true);
			promotionConfigField.set(testpromotionResource, barcodeAssignment);
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
	
	@Given("a businessdate $file")
	public final void noBusinessdateSet(String fileName) {
		try {
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/promotion/datasets/" + fileName);
		} catch (Exception ex) {
			Assert.fail("Can't Retrieve Servlet context from promotion.");
		}
	}
	
	@When("the Web API Starts Up")
	public final void theWebAPIStartsUp() {
		servletContext = null;
		try {
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/promotion/datasets/prm_system_config_no_tax_rate.xml");
			servletContext = Requirements.getMockServletContext();
			BarcodeAssignmentFactory.initialize(EnvironmentEntries.getInstance().getParaBasePath());
		} catch (/* RuntimeError */Exception ex) {
			Assert.fail("Can't Start Up WebStoreServer");
		}
	}

	@When("the Web API Starts Up with $systemConDataSet System Configuration")
	public final void theWebAPIStartsUpSytstemConfiguration(
			String systemConDataSet) {
		servletContext = null;
		try {
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					systemConDataSet);
			servletContext = Requirements.getMockServletContext();
			BarcodeAssignmentFactory.initialize(EnvironmentEntries.getInstance().getParaBasePath());
		} catch (/* RuntimeError */Exception ex) {
			Assert.fail("Can't Start Up WebStoreServer");
		}
	}

	@When("a Begin Transaction at promotion with parameters RetailStoreID $retailStoreID WorkStationId $workStationID SequenceNo $seqNo TransactionJson $transactionJson CompanyId $companyId")
	public final void aBeginTransactionAtPromotionWithParameters(
			String retailStoreId, String workStationId, String sequenceNo,
			String transactionJson, String companyId) throws Exception {

		if (retailStoreId.equals("NULL")) {
			retailStoreId = null;
		}
		if (workStationId.equals("NULL")) {
			workStationId = null;
		}
		if (sequenceNo.equals("NULL")) {
			sequenceNo = null;
		}
		if (transactionJson.equals("NULL")) {
			transactionJson = null;
		}
		
		if (!StringUtility.isNullOrEmpty(companyId) && !StringUtility.isNullOrEmpty(retailStoreId)) {
			PricePromInfoFactory.initialize(companyId,retailStoreId);
			PriceMMInfoFactory.initialize(companyId, retailStoreId);
		}
		
		actualResultBase = testpromotionResource.beginTransaction(
				retailStoreId, workStationId, sequenceNo, companyId,
				transactionJson);
	}

	@When("a Item Entry at promotion with parameters RetailStoreID $retailStoreID WorkStationId $workStationID SequenceNo $seqNo TransactionJson $transactionJson CompanyId $companyId PriceCheck $priceCheck Businessdate $businessDate")
	public final void aItemEntryAtPromotionWithParameters(String retailStoreId,
			String workStationId, String sequenceNo, String transactionJson,
			String companyId, String priceCheck, String businessDate) throws Exception {

		if (retailStoreId.equals("NULL")) {
			retailStoreId = null;
		}
		if (workStationId.equals("NULL")) {
			workStationId = null;
		}
		if (sequenceNo.equals("NULL")) {
			sequenceNo = null;
		}
		if (transactionJson.equals("NULL")) {
			transactionJson = null;
		}
		if (!StringUtility.isNullOrEmpty(companyId) && !StringUtility.isNullOrEmpty(retailStoreId)) {
			PricePromInfoFactory.initialize(companyId,retailStoreId);
			PriceMMInfoFactory.initialize(companyId, retailStoreId);
		}
		
		actualResultBase = testpromotionResource.itemEntry(retailStoreId,
				workStationId, sequenceNo, transactionJson, companyId, priceCheck,
				businessDate);
	}

	@When("I request to update Item Quantity at promotion with parameters RetailStoreID $retailStoreID WorkStationID $workStationID SequenceNo $sequenceNumber and TransactionJson $transactionJson")
	public final void itemQuantityAtPromotionWithParameters(
			final String retailStoreId, final String workStationId,
			final String sequenceNumber, final String transactionJson,
			final String isDelete) {
		String retailStoreIdTemp = retailStoreId.equalsIgnoreCase("null") ? null
				: retailStoreId;
		String workStationIdTemp = workStationId.equalsIgnoreCase("null") ? null
				: workStationId;
		String sequenceNumberTemp = sequenceNumber.equalsIgnoreCase("null") ? null
				: sequenceNumber;
		String transactionJsonTemp = transactionJson.equalsIgnoreCase("null") ? null
				: transactionJson;
		actualResultBase = testpromotionResource.itemUpdate(retailStoreIdTemp,
				workStationIdTemp, sequenceNumberTemp, transactionJsonTemp);
	}

	@When("I request to update Item at promotion with parameters RetailStoreID $retailStoreID WorkStationID $workStationID SequenceNo $sequenceNumber and TransactionJson $transactionJson")
	public final void itemUpdateAtPromotionWithParameters(
			final String retailStoreId, final String workStationId,
			final String sequenceNumber, final String transactionJson,
			final String isDelete) {
		String retailStoreIdTemp = retailStoreId.equalsIgnoreCase("null") ? null
				: retailStoreId;
		String workStationIdTemp = workStationId.equalsIgnoreCase("null") ? null
				: workStationId;
		String sequenceNumberTemp = sequenceNumber.equalsIgnoreCase("null") ? null
				: sequenceNumber;
		String transactionJsonTemp = transactionJson.equalsIgnoreCase("null") ? null
				: transactionJson;
		actualResultBase = testpromotionResource.itemUpdate(retailStoreIdTemp,
				workStationIdTemp, sequenceNumberTemp, transactionJsonTemp);
	}

	@When("I end the transaction at promotion with retailstoreid $retailstoreid workstationid $workstationid sequencenumber $sequencenumber and transactionJson $transactionJson")
	public void iEndTheTransaction(final String retailstoreid,
			final String workstationid, final String sequencenumber,
			final String transactionJson) {
		actualResultBase = testpromotionResource.endTransaction(retailstoreid,
				workstationid, sequencenumber, transactionJson);
	}

	@SuppressWarnings( "unchecked" )
	@Then("the following list of TerminalItem are: $expectedTerminalItems")
	public final void theFollowingListOfTerminalItemAre(
			ExamplesTable expectedTerminalItems) {
		// |id|sequenceNumber|transactionMode|operatorid|beginDateTime|
		actualTerminalItems = (Map<String, TerminalItem>) servletContext
				.getAttribute(GlobalConstant.PROMOTION_TERMINAL_ITEMS);
		Assert.assertEquals("Comnpare the expected count of TerminalItems",
				expectedTerminalItems.getRowCount(), actualTerminalItems.size());
		int i = 0;
		for (Entry<String, TerminalItem> param : actualTerminalItems.entrySet()) {
			TerminalItem actualTermItem = param.getValue();
			Map<String, String> expetedTermItem = expectedTerminalItems
					.getRow(i);

			Assert.assertEquals("Compare Terminal Item ID of row" + i,
					expetedTermItem.get("id"), actualTermItem.getId());
			Assert.assertEquals("Compare Terminal Item SequenceNumber of row"
					+ i, expetedTermItem.get("sequenceNumber"),
					actualTermItem.getSequenceNumber());
			Assert.assertEquals("Compare Terminal Item TransactionMode of row"
					+ i,
					Integer.parseInt(expetedTermItem.get("transactionMode")),
					actualTermItem.getTransactionMode());
			Assert.assertEquals("Compare Terminal Item OperatorID of row" + i,
					expetedTermItem.get("operatorid"),
					actualTermItem.getOperatorid());
			Assert.assertEquals("Compare Terminal Item BeginDateTime of row"
					+ i, expetedTermItem.get("beginDateTime"),
					actualTermItem.getBeginDateTime());

			// Compare the KEY with the Terminal ID.
			Assert.assertTrue("Compare the Key with regards to TerminalItemID",
					actualTermItem.getId().equals(param.getKey()));
			i++;
		}
	}

	@Then("the Result Code is $resultcode")
	public final void theResultCodeIs(int expectedResultCode) {
		Assert.assertEquals("Expect the  result code", expectedResultCode,
				actualResultBase.getNCRWSSResultCode());
	}
	
	@Then("the Extended Result Code is $resultcode")
	public final void theExtendedResultCodeIs(int expectedResultCode) {
		Assert.assertEquals("Expect the extended result code", expectedResultCode,
				actualResultBase.getNCRWSSExtendedResultCode());
	}
	
	@Then("the department is $department")
	public final void theDepartmentIs(String department) {
		PromotionResponse promotionResponse = (PromotionResponse) actualResultBase;
		Assert.assertEquals("Expect the  result code", department,
				promotionResponse.getDepartment());
	}

	@Then("the JSON should have the following format : $expectedJson")
	public final void theJsonShouldHaveTheFollowingJSONFormat(
			String expectedJson) {
		try {
			actualResultBase.setMessage(null);
			JsonMarshaller<ResultBase> jsonMarshaller = new JsonMarshaller<ResultBase>();
			String actualJson = jsonMarshaller.marshall(actualResultBase);

			/*
			 * Assert.assertEquals("Verify the PromotionResponse JSON Format",
			 * expectedJson, actualJson);
			 */

			// compare to json strings regardless of property ordering
			JSONAssert.assertEquals(expectedJson, actualJson,
					JSONCompareMode.NON_EXTENSIBLE);
		} catch (Exception e) {
			Assert.fail("Failed to verify the PromotionResponse JSON format");
			e.printStackTrace();
		}
	}

	@Given("a loaded dataset $dataset")
	public final void initdatasetsDpt(final String dataset) {
		try {
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, dataset);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("Sale data should be : $expectedItems")
	public final void saleDataShouldBe(final ExamplesTable expectedItems) {
		PromotionResponse promotionResponse = (PromotionResponse) actualResultBase;
		Transaction transactionResult = promotionResponse.getTransaction();
		assertThat("transactionResult is equal to null",
				transactionResult == null, is(equalTo(false)));
		Sale actualSale = transactionResult.getSale();
		assertThat("sale is equal to null", actualSale == null,
				is(equalTo(false)));
		for (Map<String, String> expectedItem : expectedItems.getRows()) {
			assertThat("Compare the ItemEntryID at row ",
					"" + actualSale.getItemEntryId(),
					is(equalTo(expectedItem.get("ItemEntryID"))));
			assertThat("Compare the ItemID row ", actualSale.getItemId(),
					is(equalTo(expectedItem.get("ItemID"))));
			assertThat("Compare the Quantity at row ",
					"" + actualSale.getQuantity(),
					is(equalTo(expectedItem.get("Quantity"))));
			assertThat("Compare the Department at row ",
					actualSale.getDepartment(),
					is(equalTo(expectedItem.get("Department"))));
			assertThat("Compare the Description EN at row",
					"" + String.valueOf(actualSale.getDescription().getEn()),
					is(equalTo(expectedItem.get("DescriptionEN"))));
			assertThat("Compare the Description JP at row", ""
					+ actualSale.getDescription().getJa(),
					is(equalTo(expectedItem.get("DescriptionJP"))));
			assertThat("Compare the RegularSalesUnitPrice at row ", ""
					+ actualSale.getRegularSalesUnitPrice(),
					is(equalTo(expectedItem.get("RegularSalesUnitPrice"))));
			assertThat("Compare the DiscountType row ",
					"" + actualSale.getDiscountType(),
					is(equalTo(expectedItem.get("DiscountType"))));
			assertThat("Compare the DiacountRate row ",
					"" + actualSale.getDiacountRate(),
					is(equalTo(expectedItem.get("DiacountRate"))));
			assertThat("Compare the DiscountAmt at row",
					"" + actualSale.getDiscountAmt(),
					is(equalTo(expectedItem.get("DiscountAmt"))));
			assertThat("Compare the ActualSalesUnitPrice at row", ""
					+ actualSale.getActualSalesUnitPrice(),
					is(equalTo(expectedItem.get("ActualSalesUnitPrice"))));
			assertThat("Compare the ExtendedAmount at row",
					"" + actualSale.getExtendedAmount(),
					is(equalTo(expectedItem.get("ExtendedAmount"))));
		}
	}
	
	@Then("itemId should be : $itemId")
	public final void itemIdShouldBe(final String itemId) {
		PromotionResponse promotionResponse = (PromotionResponse) actualResultBase;
		Transaction transactionResult = promotionResponse.getTransaction();
		assertThat("transactionResult is equal to null",
				transactionResult == null, is(equalTo(false)));
		assertThat("Compare the ItemID row ", transactionResult.getItemId(),
				is(equalTo(itemId)));
	}
	
	//2017/04/18 add start by kl
	@Then("getSaleItemId should be : $itemId")
	public final void getSaleItemIdShouldBe(final String itemId) {
		PromotionResponse promotionResponse = (PromotionResponse) actualResultBase;
		Transaction transactionResult = promotionResponse.getTransaction();
		assertThat("transactionResult is equal to null",
				transactionResult == null, is(equalTo(false)));
		assertThat("Compare the ItemID row ", transactionResult.getSale().getItemId(),
				is(equalTo(itemId)));
	}
	
	@Then("the Mdname should be : $MdName")
	public final void mdNameShouldBe(final String MdName) {
		PromotionResponse promotionResponse = (PromotionResponse) actualResultBase;
		Transaction transactionResult = promotionResponse.getTransaction();
		Sale sale = transactionResult.getSale();
		assertThat("Compare the ItemID row ", "" + sale.getMdNameLocal(),
				is(equalTo(MdName)));
	}
	
	@Then("the RegularSalesPrice should be : $RegularSalesPrice")
	public final void regularSalesPriceShouleBe(final String regularSalesPrice) {
		PromotionResponse promotionResponse = (PromotionResponse) actualResultBase;
		Transaction transactionResult = promotionResponse.getTransaction();
		Sale sale = transactionResult.getSale();
		assertThat("Compare the regularSalesPrice row ", "" + sale.getRegularSalesUnitPrice(),
				is(equalTo(regularSalesPrice)));
	}
	
	@Then("the ActualSalesPrice should be : $ActualSalesPrice")
	public final void salesPrice1ShouleBe(final String actualSalesPrice) {
		PromotionResponse promotionResponse = (PromotionResponse) actualResultBase;
		Transaction transactionResult = promotionResponse.getTransaction();
		Sale sale = transactionResult.getSale();
		assertThat("Compare the actualSalesPrice row ", "" + sale.getActualSalesUnitPrice(),
				is(equalTo(actualSalesPrice)));
	}
		
	//2017/04/18 add end by kl

	@Then("there should be no promotion data.")
	public final void shouldBeNoPromotionData() {
		PromotionResponse promotionResponse = (PromotionResponse) actualResultBase;
		Promotion promotionResult = promotionResponse.getPromotion();
		Assert.assertTrue(
				"Promotion data should be empty",
				promotionResult == null
						|| (promotionResult != null && promotionResult
								.getDiscounts() == null)
						|| (promotionResult != null && promotionResult
								.getDiscounts().size() == 0));
	}

	@Then("there should be no Discount promotion data for update quantity.")
	public final void thereShouldBeNoPromotionDataForUpdateQuantity() {
		PromotionResponse promotionResponse = (PromotionResponse) actualResultBase;
		Assert.assertTrue("Promotion data should be null",
				0 == promotionResponse.getPromotion().getDiscounts().size());
	}

	@Then("promotion should have the following data : $expectedItems")
	public final void theListOfItemsAre(final ExamplesTable expectedItems) {
		PromotionResponse promotionResponse = (PromotionResponse) actualResultBase;
		assertThat("promotion is equal to null",
				promotionResponse.getPromotion() == null, is(equalTo(false)));
		List<Discount> actualItems = promotionResponse.getPromotion()
				.getDiscounts();

		if (actualItems == null) {
			Assert.assertTrue("It is expected that there are no discounts",
					expectedItems.getRowCount() == 0);
			return;
		}

		Assert.assertEquals("Compare the exact number of Discounts",
				expectedItems.getRowCount(), actualItems.size());

		int i = 0;
		for (Map<String, String> expecedItem : expectedItems.getRows()) {
			assertThat("Compare the RewardID at row " + i, ""
					+ actualItems.get(i).getRewardID(),
					is(equalTo(expecedItem.get("RewardID"))));
			assertThat("Compare the EarnedRewardID row " + i, actualItems
					.get(i).getEarnedRewardID(),
					is(equalTo(expecedItem.get("EarnedRewardID"))));
			assertThat("Compare the PromotionCode at row " + i, actualItems
					.get(i).getPromotionCode(),
					is(equalTo(expecedItem.get("PromotionCode"))));
			assertThat("Compare the DiscountDescription at row " + i,
					actualItems.get(i).getDiscountDescription(),
					is(equalTo(expecedItem.get("DiscountDescription"))));
			assertThat("Compare the ItemEntryID at row " + i, actualItems
					.get(i).getItemEntryID(),
					is(equalTo(expecedItem.get("ItemEntryID"))));
			assertThat("Compare the UnitDiscountAmount() at row" + i, ""
					+ actualItems.get(i).getUnitDiscountAmount(),
					is(equalTo(expecedItem.get("UnitDiscountAmount"))));
			i++;
		}

	}

	@Then("there should be revoke")
	public final void revokePresent() {
		PromotionResponse promotionResponse = (PromotionResponse) actualResultBase;
		assertThat("revoke is equal to null", promotionResponse.getPromotion()
				.getRevoke() == null, is(equalTo(false)));
	}

	@Then("there should be no revoke")
	public final void revokeNotPresent() {
		PromotionResponse promotionResponse = (PromotionResponse) actualResultBase;
		assertThat("revoke is equal to null", promotionResponse.getPromotion()
				.getRevoke() == null, is(equalTo(true)));
	}
	
	//2017/04/17 add start by kl
	@Then("result should be : $expectedItems")
	public final void resultShouldBe(final ExamplesTable expectedItems) {
		PromotionResponse promotionResponse = (PromotionResponse) actualResultBase;
		Transaction transactionResult = promotionResponse.getTransaction();
		Sale sale = transactionResult.getSale();
		assertThat("transactionResult is equal to null",
				transactionResult == null, is(equalTo(false)));
		
		for (Map<String, String> expecedItem : expectedItems.getRows()) {
			assertThat("Compare the MdNameLocal row ", ""
					+ sale.getMdNameLocal(),
					is(equalTo(expecedItem.get("MdNameLocal"))));
			assertThat("Compare the TaxType row ", 
					sale.getTaxType(),
					is(equalTo(Integer.parseInt(expecedItem.get("TaxType")))));
			assertThat("Compare the RegularSalesUnitPrice row ", ""
					+ sale.getRegularSalesUnitPrice(),
					is(equalTo(expecedItem.get("RegularSalesPrice"))));
			assertThat("Compare the Description row ", ""
					+ sale.getDescription().getJa(),
					is(equalTo(expecedItem.get("description"))));
			assertThat("Compare the ActualSalesUnitPrice row ", ""
					+ sale.getActualSalesUnitPrice(),
					is(equalTo(expecedItem.get("ActualSalesPrice"))));
			assertThat("Compare the PublishingCode row ", ""
					+ sale.getPublishingCode(),
					is(equalTo(expecedItem.get("PublishingCode"))));
			if (sale.getCategoryCode() != null) {
				assertThat("Compare the CategoryCode row ", ""
						+ sale.getCategoryCode(),
						is(equalTo(expecedItem.get("CategoryCode"))));
			} else if (sale.getMagazineCode() != null) {
				assertThat("Compare the MagazineCode row ", ""
						+ sale.getMagazineCode(),
						is(equalTo(expecedItem.get("MagazineCode"))));
			}
			assertThat("Compare the dptSubCode1 row ", ""
					+ sale.getDptSubCode1(),
					is(equalTo(expecedItem.get("dptSubCode1"))));
		}
	}
	@Then("dptResult should be : $expectedItems")
	public final void dptResultShouldBe(final ExamplesTable expectedItems) {
		PromotionResponse promotionResponse = (PromotionResponse) actualResultBase;
		Transaction transactionResult = promotionResponse.getTransaction();
		Sale sale = transactionResult.getSale();
		assertThat("transactionResult is equal to null",
				transactionResult == null, is(equalTo(false)));
		
		for (Map<String, String> expecedItem : expectedItems.getRows()) {
			assertThat("Compare the DptName row ", ""
					+ promotionResponse.getDepartmentName(),
					is(equalTo(expecedItem.get("DptName"))));
			assertThat("Compare the MdNameLocal row ", ""
					+ sale.getMdNameLocal(),
					is(equalTo(expecedItem.get("MdNameLocal"))));
			assertThat("Compare the TaxType row ", 
					sale.getTaxType(),
					is(equalTo(Integer.parseInt(expecedItem.get("TaxType")))));
			assertThat("Compare the ItemId row ", ""
					+ sale.getItemId(),
					is(equalTo(expecedItem.get("ItemId"))));
			assertThat("Compare the Dpt row ", ""
					+ sale.getDepartment(),
					is(equalTo(expecedItem.get("Dpt"))));
			assertThat("Compare the Md11 row ", ""
					+ sale.getMd11(),
					is(equalTo(expecedItem.get("Md11"))));
			assertThat("Compare the Md12 row ", ""
					+ sale.getMd12(),
					is(equalTo(expecedItem.get("Md12"))));
			assertThat("Compare the Md13 row ", ""
					+ sale.getMd13(),
					is(equalTo(expecedItem.get("Md13"))));
			 assertThat("Compare the TaxRate row ", ""
						+ sale.getTaxRate(),
						is(equalTo(expecedItem.get("TaxRate"))));
			assertThat("Compare the NonSales row ", ""
					+ sale.getNonSales(),
					is(equalTo(expecedItem.get("NonSales"))));
			assertThat("Compare the DiscountType row ", ""
					+ sale.getDiscountType(),
					is(equalTo(expecedItem.get("DiscountType"))));
			assertThat("Compare the RegularSalesUnitPrice row ", ""
					+ sale.getRegularSalesUnitPrice(),
					is(equalTo(expecedItem.get("RegularSalesPrice"))));
			assertThat("Compare the ActualSalesUnitPrice row ", ""
					+ sale.getActualSalesUnitPrice(),
					is(equalTo(expecedItem.get("ActualSalesPrice"))));
			assertThat("Compare the CategoryCode row ", ""
					+ sale.getCategoryCode(),
					is(equalTo(expecedItem.get("CategoryCode"))));
			assertThat("Compare the LabelPrice row ", ""
					+ sale.getLabelPrice(),
					is(equalTo(expecedItem.get("LabelPrice"))));
			assertThat("Compare the MagazineCode row ", ""
					+ sale.getMagazineCode(),
					is(equalTo(expecedItem.get("MagazineCode"))));
			assertThat("Compare the dptSubCode1 row ", ""
					+ sale.getDptSubCode1(),
					is(equalTo(expecedItem.get("dptSubCode1"))));
		}
	}
	//2017/04/17 add end by kl

	@Then("promotion should have the following data for update quantity: $expectedItems")
	public final void theListOfItemsAreforForUpdateQuantity(
			final ExamplesTable expectedItems) {
		PromotionResponse promotionResponse = (PromotionResponse) actualResultBase;

		List<Discount> actualItems = promotionResponse.getPromotion()
				.getDiscounts();

		assertThat("promotion is equal to null",
				promotionResponse.getPromotion() == null, is(equalTo(false)));

		Assert.assertEquals("Compare the exact number of Discounts",
				expectedItems.getRowCount(), actualItems.size());

		int i = 0;
		for (Map<String, String> expecedItem : expectedItems.getRows()) {
			assertThat("Compare the RewardID at row " + i, ""
					+ actualItems.get(i).getRewardID(),
					is(equalTo(expecedItem.get("RewardID"))));
			assertThat("Compare the EarnedRewardID row " + i, actualItems
					.get(i).getEarnedRewardID(),
					is(equalTo(expecedItem.get("EarnedRewardID"))));
			assertThat("Compare the PromotionCode at row " + i, actualItems
					.get(i).getPromotionCode(),
					is(equalTo(expecedItem.get("PromotionCode"))));
			assertThat("Compare the DiscountDescription at row " + i,
					actualItems.get(i).getDiscountDescription(),
					is(equalTo(expecedItem.get("DiscountDescription"))));
			assertThat("Compare the ItemEntryID at row " + i, actualItems
					.get(i).getItemEntryID(),
					is(equalTo(expecedItem.get("ItemEntryID"))));
			assertThat("Compare the UnitDiscountAmount() at row" + i, ""
					+ actualItems.get(i).getUnitDiscountAmount(),
					is(equalTo(expecedItem.get("UnitDiscountAmount"))));
			i++;
		}

	}

	@When("$value PromotionTerminalItems in the context")
	public final void setPromotionTermItemsContext(String value) {
		String valueTemp = value.equalsIgnoreCase("null") ? null : value;
		servletContext.setAttribute(GlobalConstant.PROMOTION_TERMINAL_ITEMS,
				valueTemp);
	}

	@SuppressWarnings("unchecked")
	@Then("the following item quantity for Sale(s) in general promotion mix match with TerminalItem $termitem: $expectedSales")
	public final void theFollowingItemQuantityForSaleInGeneralPromotionMixMatch(
			final String termitem, final ExamplesTable expectedSales) {
		actualTerminalItems = (Map<String, TerminalItem>) servletContext
				.getAttribute(GlobalConstant.PROMOTION_TERMINAL_ITEMS);

		List<Sale> actualSales = actualTerminalItems.get(termitem)
				.getMixMatchItem(MixMatchItem.GENERAL_PROMOTION).getItemList();
		Assert.assertEquals("Compare the number of Sales",
				expectedSales.getRowCount(), actualSales.size());

		int i = 0;
		for (Map<String, String> expectedSale : expectedSales.getRows()) {
			Sale actualSale = actualSales.get(i);
			Assert.assertEquals("Compare ActualSalesUnitPrice for Sale in row"
					+ i, Double.parseDouble(expectedSale
					.get("ActualSalesUnitPrice")), actualSale
					.getActualSalesUnitPrice());
			Assert.assertEquals("Compare ItemEntryID for Sale in row" + i,
					expectedSale.get("ItemEntryID"),
					actualSale.getItemEntryId());
			Assert.assertEquals("Compare ItemID for Sale in row" + i,
					expectedSale.get("ItemID"), actualSale.getItemId());
			Assert.assertEquals("Compare ItemIDType for Sale in row" + i,
					expectedSale.get("ItemIDType"), actualSale.getItemIdType());
			Assert.assertEquals("Compare Quantity for Sale in row" + i,
					Integer.parseInt(expectedSale.get("Quantity")),
					actualSale.getQuantity());
			Assert.assertEquals("Compare Department for Sale in row" + i,
					expectedSale.get("Department"), actualSale.getDepartment());
			Assert.assertEquals("Compare Description for Sale in row" + i,
					expectedSale.get("Description"),
					actualSale.getDescription());
			Assert.assertEquals("Compare RegularSalesUnitPrice for Sale in row"
					+ i, Double.parseDouble(expectedSale
					.get("RegularSalesUnitPrice")), actualSale
					.getRegularSalesUnitPrice());
			Assert.assertEquals("Compare Discount for Sale in row" + i,
					expectedSale.get("Discount"), actualSale.getDiscount());
			Assert.assertEquals("Compare DiscountAmount for Sale in row" + i,
					Double.parseDouble(expectedSale.get("DiscountAmount")),
					actualSale.getDiscountAmount());
			Assert.assertEquals("Compare ExtendedAmount for Sale in row" + i,
					Double.parseDouble(expectedSale.get("ExtendedAmount")),
					actualSale.getExtendedAmount());
			Assert.assertEquals("Compare Discountable for Sale in row" + i,
					Boolean.parseBoolean(expectedSale.get("Discountable")),
					actualSale.getDiscountable());
			i++;
		}
	}
}
