package ncr.res.mobilepos.additem.test;

import junit.framework.Assert;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.barcodeassignment.factory.BarcodeAssignmentFactory;
import ncr.res.mobilepos.constant.EnvironmentEntries;
import ncr.res.mobilepos.constant.SystemFileConfig;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.department.resource.DepartmentResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.pricing.factory.PriceMMInfoFactory;
import ncr.res.mobilepos.pricing.factory.PricePromInfoFactory;
import ncr.res.mobilepos.pricing.resource.ItemResource;
import ncr.res.mobilepos.promotion.factory.TaxRateInfoFactory;
import ncr.res.mobilepos.promotion.model.PromotionResponse;
import ncr.res.mobilepos.promotion.resource.PromotionResource;

@SuppressWarnings("deprecation")
public class AddItemSteps extends Steps {

	private PromotionResource testpromotionResource = null;
	private DBInitiator dbInit = null;
	private DepartmentResource testDepartmentResource = null;
	private ServletContext servletContext = null;
	private String companyId = null;
	private String storeId = null;
	private String terminalId = null;
	private String businessDate = null;
	private String seqNo = null;
	private PromotionResponse itemEntryResponse = null;
	private ViewDepartment departmentInfoResponse = null;
	private String priceCheck = null;

	@BeforeScenario
	public final void SetUpClass() {
		dbInit = new DBInitiator("SQLServerItemDAOSteps", DATABASE.RESMaster);
		Requirements.SetUp();
	}

	@AfterScenario
	public final void TearDownClass() {
		Requirements.TearDown();
	}

	@Given("an items $dataset in database")
	public final void initdatasetsDpt(final String dataset) {
		try {
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, dataset);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Given("a companyid $companyid storeid $storeid workstationid $terminalid businessdate $businessdate seqno $seqno PriceCheck $priceCheck")
	public final void givenParameters(String companyId, String storeId,
			String terminalId, String businessDate, String seqNo, String priceCheck) throws Exception {
		this.companyId = companyId;
		this.storeId = storeId;
		this.terminalId = terminalId;
		this.businessDate = businessDate;
		this.seqNo = seqNo;
		this.priceCheck = priceCheck;
		PricePromInfoFactory.initialize(companyId, storeId, null, null, null, null);
		PriceMMInfoFactory.initialize(companyId, storeId, null);
		TaxRateInfoFactory.initialize(companyId, storeId);
	}

	@Given("the Web API Starts Up with $systemConDataSet System Configuration")
	public final void GivenThatTheWebAPIStartsUpSytstemConfiguration(
			String systemConDataSet) {
		servletContext = null;
		try {
			servletContext = Requirements.getMockServletContext();
			BarcodeAssignmentFactory.initialize(EnvironmentEntries.getInstance().getParaBasePath());
			TaxRateInfoFactory.initialize(SystemFileConfig.getInstance().getCompanyId(), SystemFileConfig.getInstance().getStoreId());
			testpromotionResource = new PromotionResource();
			testDepartmentResource = new DepartmentResource();
			Field promotioncontext;
			Field itemcontext;
			try {
				ItemResource itemResourceObj = new ItemResource();
				promotioncontext = testpromotionResource.getClass()
						.getDeclaredField("context");
				itemcontext = itemResourceObj.getClass().getDeclaredField(
						"context");
				promotioncontext.setAccessible(true);
				itemcontext.setAccessible(true);
				promotioncontext.set(testpromotionResource, servletContext);
				itemcontext.set(itemResourceObj, servletContext);
				testDepartmentResource.setContext(servletContext);
				dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
						systemConDataSet);
				dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
						"test/resources/para/mst_bizday.xml");
			} catch (Exception ex) {
				Assert.fail("Can't Retrieve Servlet context from promotion.");
			}
		} catch (/* RuntimeError */Exception ex) {
			Assert.fail("Can't Start Up WebStoreServer");
		}
	}

	@When("a Begin Transaction at promotion with parameters RetailStoreID $retailStoreID WorkStationId $workStationID SequenceNo $seqNo TransactionJson $transactionJson CompanyId $companyId")
	public final void aBeginTransactionAtPromotionWithParameters(
			String retailStoreId, String workStationId, String sequenceNo,
			String transactionJson, String companyId) {
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
		testpromotionResource.beginTransaction(retailStoreId, workStationId,
				sequenceNo, companyId, transactionJson);
	}

	/**
	 * https://localhost:8443/resTransaction/rest/promotion/begin_transaction
	 * https://localhost:8443/resTransaction/rest/promotion/item_entry
	 * https://localhost:8443/resTransaction/rest/departmentinfo/detail?companyid=01&retailstoreid=0247&departmentid=397
	 * https://localhost:8443/resTransaction/rest/point/getitempointrate?companyId=01&storeId=0247&terminalId=0004&businessDate=2016-07-21&deptCode=397&groupCode=21111&brandId=004099&sku=04208711
	 */
	@When("I add an item $itemcode")
	public final void whenIAddItem(String itemCode) {
		String beginTxJson = "{\"TransactionMode\":\"0\",\"OperatorID\":\"9011011\",\"BeginDateTime\":\"2016-12-13T13:31:56.0Z\"}";
		testpromotionResource.beginTransaction(this.storeId, this.terminalId,
				this.seqNo, this.companyId, beginTxJson);
		String itemEntryJson = "{\"EntryFlag\":\"true\",\"Sale\":{\"ItemEntryID\":1,\"ItemID\":\""
				+ itemCode
				+ "\",\"ItemIDType\":\"EAN13\",\"Department\":\"\",\"Quantity\":1}}";
		itemEntryResponse = testpromotionResource.itemEntry(this.storeId,
				this.terminalId, this.seqNo, itemEntryJson, this.companyId, false, this.priceCheck,
				this.businessDate);
		departmentInfoResponse = testDepartmentResource.selectDepartmentDetail(
				this.companyId, this.storeId, itemEntryResponse
						.getTransaction().getSale().getDepartment());
	}

	@Then("I should get the following: $data")
	public final void thenIShouldGetData(ExamplesTable expectedData) {
		int i = 0;
		for (Map<String, String> data : expectedData.getRows()) {
			Assert.assertEquals("Compare Description " + i,
					data.get("Description(ja)").trim(), itemEntryResponse
							.getTransaction().getSale().getDescription()
							.getJa());
			Assert.assertEquals("Compare Price " + i,
					Double.parseDouble(data.get("ActualSalesUnitPrice")),
					itemEntryResponse.getTransaction().getSale()
							.getActualSalesUnitPrice());
			Assert.assertEquals("Compare Plu " + i, data.get("ItemID").trim(),
					itemEntryResponse.getTransaction().getSale().getItemId());
			Assert.assertEquals("Compare Qty " + i,
					Integer.parseInt(data.get("Quantity")), itemEntryResponse
							.getTransaction().getSale().getQuantity());
			Assert.assertEquals("Compare Discount " + i,
					Integer.parseInt(data.get("DiscountAmt")),
					itemEntryResponse.getTransaction().getSale()
							.getDiscountAmt());
			Assert.assertEquals("Compare Dpt " + i, data.get("Department")
					.trim(), itemEntryResponse.getTransaction().getSale()
					.getDepartment());
			Assert.assertEquals("Compare Plu " + i, data.get("DptNameLocal")
					.trim(), departmentInfoResponse.getDepartment()
					.getDepartmentName().getJa());
			i++;
		}
	}
	
	 /**
     * Then Step: Compare the existing data in the database.
     * @param expecteditems The expected items in the database.
     * @throws DataSetException The Exception thrown when test fail.
     */
	@Then("the data in MST_PLU should have the following: $expecteditems")
	public final void theFollowingItemsInTheDatabaseShouldBe(
			final ExamplesTable expecteditems) throws DataSetException {
		List<Map<String, String>> expectedItemRows = expecteditems.getRows();
		ITable actualItemRows = dbInit.getTableSnapshot("MST_PLU");

		Assert.assertEquals("Compare that the number "
				+ "of rows in Items are exact: ", expecteditems.getRowCount(),
				actualItemRows.getRowCount());
		int i = 0;
		for (Map<String, String> expItem : expectedItemRows) {
			Assert.assertEquals("Compare the CompanyId row " + i + ": ",
					expItem.get("CompanyId"),
					actualItemRows.getValue(i, "CompanyId").toString());
			Assert.assertEquals("Compare the StoreId row " + i + ": ",
					expItem.get("StoreId"),
					actualItemRows.getValue(i, "StoreId").toString());
			Assert.assertEquals("Compare the MdInternal row " + i + ": ",
					expItem.get("MdInternal"),
					actualItemRows.getValue(i, "MdInternal").toString());
			Assert.assertEquals("Compare the Dpt row " + i + ": ",
					expItem.get("Dpt"),
					actualItemRows.getValue(i, "Dpt").toString());
			Assert.assertEquals("Compare the SalesPrice1 row " + i + ": ",
					expItem.get("SalesPrice1"),
					actualItemRows.getValue(i, "SalesPrice1").toString());
			Assert.assertEquals("Compare the MdNameLocal row " + i + ": ",
					expItem.get("MdNameLocal"),
					actualItemRows.getValue(i, "MdNameLocal").toString());
			Assert.assertEquals("Compare the TaxType row " + i + ": ", expItem
					.get("TaxType"), actualItemRows.getValue(i, "TaxType")
					.toString());
			i++;
		}
	}

}
