package ncr.res.mobilepos.eod.test;

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
import org.junit.Assert;

import java.lang.reflect.Field;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.cashaccount.model.GetCashBalance;
import ncr.res.mobilepos.cashaccount.resource.CashAccountResource;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.deviceinfo.model.PosControlOpenCloseStatus;
import ncr.res.mobilepos.deviceinfo.model.WorkingDevices;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.forwarditemlist.resource.ForwardItemListResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.journalization.model.ForwardList;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.model.SearchedPosLog;
import ncr.res.mobilepos.journalization.resource.JournalizationResource;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.report.model.DailyReportItems;
import ncr.res.mobilepos.report.resource.ReportResource;
import ncr.res.mobilepos.settlement.model.SettlementInfo;
import ncr.res.mobilepos.settlement.resource.SettlementResource;
import ncr.res.mobilepos.store.model.CMPresetInfos;
import ncr.res.mobilepos.store.resource.StoreResource;
import ncr.res.mobilepos.tillinfo.model.ViewTill;
import ncr.res.mobilepos.tillinfo.resource.TillInfoResource;

public class EndOfDaySteps extends Steps {

	private JournalizationResource journalizationResource;
	private DBInitiator dbRESTransactionInitiator;
	private DBInitiator dbRESMasterInitiator;
	private ReportResource reportResource = null;
	private DailyReportItems reportItems = null;
	private int resultCode = 0;
	private CMPresetInfos cmPresetInfos = null;

	public enum Operation {
		GETLASTPAYINPAYOUT, GETSUSPENDEDTXS, GETEXECUTEAUTHORITY, GETWORKINGDEVICES, GETTILLINFO, RELEASEEXECAUTHORITY, GETDEVICESTATUS, GETCREDIT, 
		GETVOUCHERLIST, GETITEMTYPE3DAILYSALESREPORT, GETTRANSACTIONCOUNT, GETTXCOUNTBYBUSINESSDATE, GETREPORTITEMS, SAVEPOSLOG, GETCMPRESETCMINFOS,
		GETCOUNTPAYMENTAMT
	};

	Operation operation = Operation.GETLASTPAYINPAYOUT;
	private SearchedPosLog payinoutPoslog = null;
	private ForwardItemListResource forwardItemResource = null;
	private ForwardList suspendeTxs = null;
	private TillInfoResource tillInfoResource = null;
	private ResultBase executeResult = null;
	private DeviceInfoResource deviceInfoResource = null;
	private WorkingDevices workingDevices = null;
	private ViewTill till = null;
	private ResultBase releaseExecAuthResult = null;
	private PosControlOpenCloseStatus deviceStatResult = null;
	private SettlementResource settlementResource = null;
	private SettlementInfo settlementInfo = null;
	private SettlementInfo voucherList = null;
	private CashAccountResource cashAcctResource = null;
	private GetCashBalance totalCashOnHand = null;
	private StoreResource storeResource = null;
	
	@BeforeScenario
	public final void setUp() {
		Requirements.SetUp();
		ServletContext mockContext = Requirements.getMockServletContext();
		journalizationResource = new JournalizationResource();
		forwardItemResource = new ForwardItemListResource();
		tillInfoResource = new TillInfoResource();
		deviceInfoResource = new DeviceInfoResource();
		settlementResource = new SettlementResource();
		cashAcctResource = new CashAccountResource();
		reportResource =  new ReportResource();
		storeResource = new StoreResource();
		try {
			Field journalizationContext = journalizationResource.getClass()
					.getDeclaredField("context");
			journalizationContext.setAccessible(true);
			journalizationContext.set(journalizationResource, mockContext);
			Field forwardItemContext = forwardItemResource.getClass()
					.getDeclaredField("context");
			forwardItemContext.setAccessible(true);
			forwardItemContext.set(forwardItemResource, mockContext);
			Field tillInfoContext = tillInfoResource.getClass()
					.getDeclaredField("servletContext");
			tillInfoContext.setAccessible(true);
			tillInfoContext.set(tillInfoResource, mockContext);
			Field deviceInfoContext = deviceInfoResource.getClass()
					.getDeclaredField("context");
			deviceInfoContext.setAccessible(true);
			deviceInfoContext.set(deviceInfoResource, mockContext);
			Field setttlementInfoContext = settlementResource.getClass()
					.getDeclaredField("servletContext");
			setttlementInfoContext.setAccessible(true);
			setttlementInfoContext.set(settlementResource, mockContext);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@AfterScenario
	public final void tearDown() {
		Requirements.TearDown();
	}
	@Given("that operator has sign-on")
	public final void givenOperatorSignOn(){
		try {
			dbRESMasterInitiator = new DBInitiator("EndOfDaySteps2",
					DATABASE.RESMaster);
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/data_users.xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
   @Given("that payment amt data")
	public final void givenPaymentAmt() {
		try {
			dbRESMasterInitiator = new DBInitiator("EndOfDaySteps2",
					DATABASE.RESMaster);
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/payment_amt_data.xml");
			dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps2",
					DATABASE.RESTransaction);
			dbRESTransactionInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/eod/test/TXL_TRAN_PAYMENT.xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	@Given("that presetcminfo data")
	public final void givenPresetCMInfo() {
		try {
			dbRESMasterInitiator = new DBInitiator("EndOfDaySteps2",
					DATABASE.RESMaster);
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/MST_PRESET_CMINFO.xml");
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/MST_STOREINFO.xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	@Given("that no transactions")
	public final void givenNoTransactions() {
		try {
			dbRESMasterInitiator = new DBInitiator("EndOfDaySteps2",
					DATABASE.RESMaster);
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/MST_TILLIDINFO.xml");
			dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps2",
					DATABASE.RESTransaction);
			dbRESTransactionInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/eod/test/TXL_SALES_JOURNAL_EMPTY.xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	@Given("that no report items")
	public final void givenNoReportItems() {
		try {
			dbRESMasterInitiator = new DBInitiator("EndOfDaySteps2",
					DATABASE.RESMaster);
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/MST_DEVICEINFO.xml");
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/MST_NAME_DAILYREPORT.xml");
			dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps2",
					DATABASE.RESTransaction);
			dbRESTransactionInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/eod/test/TXU_TOTAL_DAILYREPORT_(Empty).xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	@Given("that has cashbalance data in TXU_TOTAL_DAILYREPORT")
	public final void givenItemType3InTxuTotalDailyReport() {
		try {
			dbRESMasterInitiator = new DBInitiator("EndOfDaySteps2",
					DATABASE.RESMaster);
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/MST_DEVICEINFO.xml");
			dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps2",
					DATABASE.RESTransaction);
			dbRESTransactionInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/eod/test/TXU_TOTAL_DAILYREPORT_(CashBalance).xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	@Given("that has no changefund data")
	public final void givenNoChangeFundData(){
		try {
			dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps2",
					DATABASE.RESTransaction);
			dbRESTransactionInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/eod/test/TXU_TOTAL_DAILYREPORT_(Empty).xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Given("that has payments type")
	public final void givenHasPaymentTypes() {
		try {
			dbRESMasterInitiator = new DBInitiator("EndOfDaySteps2",
					DATABASE.RESMaster);
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/MST_TENDERINFO.xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Given("that has no last payin/payout transactions")
	public final void givenLastPayTransactions() {
		dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps",
				DATABASE.RESTransaction);
		try {
			dbRESTransactionInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/eod/test/TXL_SALES_JOURNAL_EMPTY.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Given("that has suspended transactions")
	public final void givenSuspendedTransactions() {
		try {
			dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps",
					DATABASE.RESTransaction);
			dbRESTransactionInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/TXL_FORWARD_ITEM.xml");
			dbRESMasterInitiator = new DBInitiator("EndOfDaySteps2",
					DATABASE.RESMaster);
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/MST_EMPINFO.xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Given("that has no suspended transactions")
	public final void givenNoSuspendedTransactions() {
		try {
			dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps",
					DATABASE.RESTransaction);
			dbRESTransactionInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/eod/test/TXL_FORWARD_ITEM_EMPTY.xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@Given("that has credit payments")
	public final void givenHasCreditPayments() {
		try {
			dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps",
					DATABASE.RESTransaction);
			dbRESTransactionInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/eod/test/TXU_TOTAL_DAILYREPORT_(Credit).xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	@Given("that has no EOD transaction")
	public final void givenNoEODTx() {
		dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps",
				DATABASE.RESTransaction);
		try {
			dbRESTransactionInitiator
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/eod/test/TXL_SALES_JOURNAL_EMPTY.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	@When("signing-off with operatorid:$1")
	public final void whenSigningOff(final String operatorId) {
		CredentialResource credentialResource = new CredentialResource();
		ResultBase resultBase = credentialResource.requestSignOff(operatorId);
		resultCode = resultBase.getNCRWSSResultCode();
	}
	@When("getting presetcminfo of companyid:$1, storeid:$2, terminalid:$3, businessdaydate:$4")
	public final void whenGettingPrecetCMInfo(final String companyId, final String storeId, final String terminalId, final String businessDayDate) {
		operation = Operation.GETCMPRESETCMINFOS;
		cmPresetInfos = storeResource.getCMPresetInfoList(companyId, storeId, terminalId, businessDayDate);
		resultCode = cmPresetInfos.getNCRWSSResultCode();
	}
	@When("getting payment amt of companyid:$1, storeid:$2, businessdaydate:$3, trainingFlag:$4, terminalId:$5")
	public final void whenGettingPaymentAmtData(final String companyId, final String storeId, final String businessDayDate, final int trainingFlag, final String terminalId) {
		operation = Operation.GETCOUNTPAYMENTAMT;
		settlementInfo = settlementResource.getCountPaymentAmt(companyId, storeId, businessDayDate, trainingFlag, terminalId);
		resultCode = settlementInfo.getNCRWSSResultCode();
	}
	@When("saving poslogxml:$1, trainingmode:$2")
	public final void whenSavingPOSlog(final String posLogXml, final int trainingMode){
		operation = Operation.SAVEPOSLOG;
		PosLogResp poslogResp = journalizationResource.journalize(posLogXml, trainingMode);
		resultCode = poslogResp.getNCRWSSResultCode();
	}
	@When("getting report items of companyId:$1, storeId:$2, tillId:$3, businessDate:$4, trainingFlag:$5")
	public final void whenGettingReportItems(final String companyId,
			final String storeId, final String tillId,
			final String businessDate, final int trainingFlag) {
		operation = Operation.GETREPORTITEMS;
		reportItems = reportResource.getReportItems(companyId, storeId, tillId, businessDate,
				trainingFlag);
	}
	@When("getting transaction count of companyId=$1, storeId=$2, txtype=$3, trainingFlag=$4")
	public final void whenGettingTransactionCount(final String companyId, final String storeId, final String txtype, final int trainingFlag){
		operation = Operation.GETTRANSACTIONCOUNT;
		settlementInfo = settlementResource.getTransactionCount(companyId, storeId, txtype, trainingFlag);
	}
	@When("getting tx count by businessdate of companyId=$1, storeId=$2, workStationId=$3, txtype=$4, businessDate=$5, trainingFlag=$6")
	public final void whenGettingTxCountByBusinessDate(final String companyId, final String storeId, final String workStationId, final String txtype, final String businessDate, final int trainingFlag){
		operation = Operation.GETTXCOUNTBYBUSINESSDATE;
		settlementInfo = settlementResource.getTxCountByBusinessDate(companyId, storeId, workStationId, txtype, businessDate, trainingFlag);
	}
	@Then("it should get TxCount:$1, NCRWSSResultCode:$3")
	public final void testTransactionCnt(final int txCnt, final int resultCode) {
		Assert.assertEquals("Compare TxCount", txCnt, settlementInfo.getTxCount());
		Assert.assertEquals("Compare NCRWSSResultCode", resultCode, settlementInfo.getNCRWSSResultCode());
	}
	
	@When("getting daily sales report for companyId:$1 storeId:$2 tillId:$3 terminalId:$4 businessDate:$5 trainingFlag:$6 dataType:$7 itemLevel1:$8 itemLevel2:$9")
	public final void whenGettingItemType3DailySalesReport(
			final String companyId, final String storeId, final String tillId,
			String terminalId, final String businessDate,
			final int trainingFlag, final String dataType,
			final String itemLevel1, final String itemLevel2) {
		terminalId = terminalId.equalsIgnoreCase("empty") ? "": terminalId;
		operation = Operation.GETITEMTYPE3DAILYSALESREPORT;
		totalCashOnHand = cashAcctResource.getReportItems(companyId, storeId, tillId, terminalId,
				businessDate, trainingFlag, dataType, itemLevel1, itemLevel2);
		resultCode = totalCashOnHand.getNCRWSSResultCode();
	}	
	@Then("it should get NCRWSSResultCode:$1")
	public final void testSalesReportChangeFund(final int expected) {
		Assert.assertEquals("Compare NCRWSSResultCode", expected, resultCode);
	}

	@When("getting last payin/payout transactions companyid:$1 storeid:$2 terminalid:$3 businessdate:$4 trainingflag:$5")
	public final void whenGettingLastPayTxs(final String companyId,
			final String storeId, final String terminalId,
			final String businessDate, final String trainingFlag) {
		operation = Operation.GETLASTPAYINPAYOUT;
		payinoutPoslog = journalizationResource.getLastPayTxPoslog(companyId,
				storeId, terminalId, businessDate,
				Integer.valueOf(trainingFlag));
	}

	@Then("it should get the following: $expected")
	public final void getTheFollowingExpected(ExamplesTable expectedTable) {
		switch (operation) {
		case GETLASTPAYINPAYOUT: {
			Assert.assertEquals(
					"Compare NCRWSSResultCode",
					Integer.parseInt(expectedTable.getRow(0).get(
							"NCRWSSResultCode")),
					payinoutPoslog.getNCRWSSResultCode());
			Assert.assertEquals(
					"Compare NCRWSSExtendedResultCode",
					Integer.parseInt(expectedTable.getRow(0).get(
							"NCRWSSExtendedResultCode")),
					payinoutPoslog.getNCRWSSExtendedResultCode());
			Assert.assertEquals("Compare TrainingModeFlag",
					expectedTable.getRow(0).get("TrainingModeFlag")
							.equalsIgnoreCase("null") ? null : expectedTable
							.getRow(0).get("TrainingModeFlag"), payinoutPoslog
							.getTransaction().getTrainingModeFlag());
			Assert.assertEquals("Compare CancelFlag", expectedTable.getRow(0)
					.get("CancelFlag").equalsIgnoreCase("null") ? null
					: expectedTable.getRow(0).get("CancelFlag"), payinoutPoslog
					.getTransaction().getCancelFlag());
		}
			;
			break;
		case GETSUSPENDEDTXS: {
			int i = 0;
			for (Map<String, String> expected : expectedTable.getRows()) {
				Assert.assertEquals("Compare CompanyId",
						expected.get("CompanyId"), suspendeTxs
								.getForwardListInfo().get(i).getCompanyId());
				Assert.assertEquals("Compare RetailStoreId",
						expected.get("RetailStoreId"), suspendeTxs
								.getForwardListInfo().get(i).getRetailStoreId());
				Assert.assertEquals("Compare WorkstationId",
						expected.get("WorkstationId"), suspendeTxs
								.getForwardListInfo().get(i).getWorkstationId());
				Assert.assertEquals("Compare SequenceNumber",
						expected.get("SequenceNumber"), suspendeTxs
								.getForwardListInfo().get(i)
								.getSequenceNumber());
				Assert.assertEquals("Compare Queue", expected.get("Queue"),
						suspendeTxs.getForwardListInfo().get(i).getQueue());
				Assert.assertEquals("Compare BusinessDayDate",
						expected.get("BusinessDayDate"), suspendeTxs
								.getForwardListInfo().get(i)
								.getBusinessDayDate());
				Assert.assertEquals("Compare TrainingFlag",
						expected.get("TrainingFlag"), suspendeTxs
								.getForwardListInfo().get(i).getTrainingFlag());
				Assert.assertEquals("Compare BusinessDateTime",
						expected.get("BusinessDateTime"), suspendeTxs
								.getForwardListInfo().get(i)
								.getBusinessDateTime());
				Assert.assertEquals("Compare OperatorId",
						expected.get("OperatorId"), suspendeTxs
								.getForwardListInfo().get(i).getOperatorId());
				Assert.assertEquals("Compare OperatorName",
						expected.get("OperatorName"), suspendeTxs
								.getForwardListInfo().get(i).getOperatorName());
				Assert.assertEquals("Compare SalesTotalAmt",
						expected.get("SalesTotalAmt"), suspendeTxs
								.getForwardListInfo().get(i).getSalesTotalAmt());
				Assert.assertEquals("Compare SalesTotalQty",
						expected.get("SalesTotalQty"), suspendeTxs
								.getForwardListInfo().get(i).getSalesTotalQty());
				Assert.assertEquals("Compare Status", expected.get("Status"),
						suspendeTxs.getForwardListInfo().get(i).getStatus());
				i++;
			}
		}
			break;
		case GETEXECUTEAUTHORITY: {
			Assert.assertEquals(
					"Compare NCRWSSResultCode",
					Integer.parseInt(expectedTable.getRow(0).get(
							"NCRWSSResultCode")),
					executeResult.getNCRWSSResultCode());
			Assert.assertEquals(
					"Compare NCRWSSExtendedResultCode",
					Integer.parseInt(expectedTable.getRow(0).get(
							"NCRWSSExtendedResultCode")),
					executeResult.getNCRWSSExtendedResultCode());
		}
			break;
		case GETTILLINFO: {
			Assert.assertEquals(
					"Compare NCRWSSResultCode",
					Integer.parseInt(expectedTable.getRow(0).get(
							"NCRWSSResultCode")), till.getNCRWSSResultCode());
			Assert.assertEquals("Compare StoreId",
					expectedTable.getRow(0).get("StoreId"), till.getTill()
							.getStoreId());
			Assert.assertEquals("Compare TillId",
					expectedTable.getRow(0).get("TillId"), till.getTill()
							.getTillId());
			Assert.assertEquals("Compare BusinessDayDate", expectedTable
					.getRow(0).get("BusinessDayDate"), till.getTill()
					.getBusinessDayDate());
			Assert.assertEquals("Compare SodFlag",
					expectedTable.getRow(0).get("SodFlag"), till.getTill()
							.getSodFlag());
			Assert.assertEquals("Compare EodFlag",
					expectedTable.getRow(0).get("EodFlag"), till.getTill()
							.getEodFlag());
		}
			break;
		case GETDEVICESTATUS: {
			Assert.assertEquals(
					"Compare OpenCloseStatus",
					Short.parseShort(expectedTable.getRow(0).get(
							"OpenCloseStatus")),
					deviceStatResult.getOpenCloseStat());
			Assert.assertEquals(
					"Compare NCRWSSResultCode",
					Integer.parseInt(expectedTable.getRow(0).get(
							"NCRWSSResultCode")),
					deviceStatResult.getNCRWSSResultCode());
			Assert.assertEquals(
					"Compare NCRWSSExtendedResultCode",
					Integer.parseInt(expectedTable.getRow(0).get(
							"NCRWSSExtendedResultCode")),
					deviceStatResult.getNCRWSSExtendedResultCode());
		}
			break;
		case GETCREDIT: {
			Assert.assertEquals("Compare CompanyId", expectedTable.getRow(0)
					.get("CompanyId"), settlementInfo.getCreditInfo()
					.getCompanyId());
			Assert.assertEquals("Compare RetailStoreId", expectedTable
					.getRow(0).get("RetailStoreId"), settlementInfo
					.getCreditInfo().getStoreId());
			Assert.assertEquals("Compare BusinessDayDate", expectedTable
					.getRow(0).get("BusinessDayDate"), settlementInfo
					.getCreditInfo().getBusinessDayDate());
			Assert.assertEquals("Compare TrainingFlag", Integer
					.parseInt(expectedTable.getRow(0).get("TrainingFlag")),
					settlementInfo.getCreditInfo().getTrainingFlag());
			Assert.assertEquals("Compare SalesCntSum", Integer
					.parseInt(expectedTable.getRow(0).get("SalesCntSum")),
					settlementInfo.getCreditInfo().getSalesCntSum());
			Assert.assertEquals("Compare SalesItemAmt", expectedTable.getRow(0)
					.get("SalesItemAmt"), String.valueOf(settlementInfo
					.getCreditInfo().getSalesItemAmt()));
			Assert.assertEquals("Compare SalesItemCnt", Integer
					.parseInt(expectedTable.getRow(0).get("SalesItemCnt")),
					settlementInfo.getCreditInfo().getSalesItemCnt());
			Assert.assertEquals("Compare SalesAmtSum", expectedTable.getRow(0)
					.get("SalesAmtSum"), String.valueOf(settlementInfo
					.getCreditInfo().getSalesAmtSum()));
		}
			break;
		case GETVOUCHERLIST: {
			// |CompanyId |StoreId |VoucherCompanyId |VoucherType |TrainingFlag
			// |SalesItemCnt |SalesItemAmt |VoucherName|VoucherKanaName |
			int i = 0;
			for (Map<String, String> expected : expectedTable.getRows()) {
				Assert.assertEquals("Compare CompanyId",
						expected.get("CompanyId"), voucherList.getVoucherList()
								.get(i).getCompanyId());
				Assert.assertEquals("Compare StoreId", expected.get("StoreId"),
						voucherList.getVoucherList().get(i).getStoreId());
				Assert.assertEquals("Compare VoucherCompanyId",
						expected.get("VoucherCompanyId"), voucherList
								.getVoucherList().get(i).getVoucherCompanyId());
				Assert.assertEquals("Compare VoucherType",
						expected.get("VoucherType"), voucherList
								.getVoucherList().get(i).getVoucherType());
				Assert.assertEquals("Compare TrainingFlag",
						Integer.parseInt(expected.get("TrainingFlag")),
						voucherList.getVoucherList().get(i).getTrainingFlag());
				Assert.assertEquals("Compare SalesItemCnt",
						Integer.parseInt(expected.get("SalesItemCnt")),
						voucherList.getVoucherList().get(i).getSalesItemCnt());
				Assert.assertEquals(
						"Compare SalesItemAmt",
						expected.get("SalesItemAmt"),
						String.valueOf(voucherList.getVoucherList().get(i)
								.getSalesItemAmt()));
				Assert.assertEquals("Compare VoucherName",
						expected.get("VoucherName"), voucherList
								.getVoucherList().get(i).getVoucherName());
				Assert.assertEquals("Compare VoucherKanaName",
						expected.get("VoucherKanaName"), voucherList
								.getVoucherList().get(i).getVoucherKanaName());
				i++;
			}
		}
			break;
		case GETITEMTYPE3DAILYSALESREPORT: {
			Assert.assertEquals("Compare NCRWSSResultCode", Integer.parseInt(expectedTable.getRow(0).get("NCRWSSResultCode")), totalCashOnHand.getNCRWSSResultCode());
			Assert.assertEquals("Compare CashOnHand", expectedTable.getRow(0).get("CashOnHand"), totalCashOnHand.getCashBalance().getCashOnHand());
		}break;
		case GETREPORTITEMS: {
			//|CompanyId	|StoreId	|DataType	|ItemLevel1	|ItemLevel2	|ItemLevel3	|ItemLevel4	|ItemName	|TillId		|ItemCount	|ItemAmt	|NCRWSSExtendedResultCode	|NCRWSSResultCode	|
			int i = 0;			
			for (Map<String, String> expected : expectedTable.getRows()) {
				Assert.assertEquals("Compare CompanyId", expected.get("CompanyId"), reportItems.getReportItems().get(i).getCompanyId());
				Assert.assertEquals("Compare StoreId", expected.get("StoreId"), reportItems.getReportItems().get(i).getStoreId());
				Assert.assertEquals("Compare DataType", expected.get("DataType"), reportItems.getReportItems().get(i).getDataType());
				Assert.assertEquals("Compare ItemLevel1", expected.get("ItemLevel1"), reportItems.getReportItems().get(i).getItemLevel1());
				Assert.assertEquals("Compare ItemLevel2", expected.get("ItemLevel2"), reportItems.getReportItems().get(i).getItemLevel2());
				Assert.assertEquals("Compare ItemLevel3", expected.get("ItemLevel3").trim(), reportItems.getReportItems().get(i).getItemLevel3().trim());
				Assert.assertEquals("Compare ItemLevel4", expected.get("ItemLevel4").trim(), reportItems.getReportItems().get(i).getItemLevel4().trim());
				Assert.assertEquals("Compare ItemName", expected.get("ItemName"), reportItems.getReportItems().get(i).getItemName());
				Assert.assertEquals("Compare TillId", expected.get("TillId"), reportItems.getReportItems().get(i).getTillId());
				Assert.assertEquals("Compare ItemCount", Integer.parseInt(expected.get("ItemCount")), reportItems.getReportItems().get(i).getItemCount());
				Assert.assertEquals("Compare ItemAmt", Integer.parseInt(expected.get("ItemAmt")), reportItems.getReportItems().get(i).getItemAmt());
				Assert.assertEquals("Compare NCRWSSExtendedResultCode", Integer.parseInt(expected.get("NCRWSSExtendedResultCode")), reportItems.getReportItems().get(i).getNCRWSSExtendedResultCode());
				Assert.assertEquals("Compare NCRWSSResultCode", Integer.parseInt(expected.get("NCRWSSResultCode")), reportItems.getReportItems().get(i).getNCRWSSResultCode());
				i++;
			}
		}break;
		case GETCMPRESETCMINFOS: {
			Assert.assertEquals("Compare CompanyId", expectedTable.getRow(0).get("CompanyId"), cmPresetInfos.getCMPresetInfoList().get(0).getCompanyId());
			// CMId column in database is auto incremented by 1. Therefore, it is dynamic.
			//Assert.assertEquals("Compare CMId", expectedTable.getRow(0).get("CMId"), cmPresetInfos.getCMPresetInfoList().get(0).getCMId());
			Assert.assertEquals("Compare CMName", expectedTable.getRow(0).get("CMName"), cmPresetInfos.getCMPresetInfoList().get(0).getCMName());
			Assert.assertEquals("Compare CMType", expectedTable.getRow(0).get("CMType"), cmPresetInfos.getCMPresetInfoList().get(0).getCMType());
			Assert.assertEquals("Compare BizCatId", expectedTable.getRow(0).get("BizCatId"), cmPresetInfos.getCMPresetInfoList().get(0).getBizCatId());
			Assert.assertEquals("Compare StoreId", expectedTable.getRow(0).get("StoreId"), cmPresetInfos.getCMPresetInfoList().get(0).getStoreId());
			Assert.assertEquals("Compare TerminalId", expectedTable.getRow(0).get("TerminalId"), cmPresetInfos.getCMPresetInfoList().get(0).getTerminalId());
			Assert.assertEquals("Compare Top1Message", expectedTable.getRow(0).get("Top1Message"), cmPresetInfos.getCMPresetInfoList().get(0).getTop1Message());
			Assert.assertEquals("Compare Top2Message", expectedTable.getRow(0).get("Top2Message"), cmPresetInfos.getCMPresetInfoList().get(0).getTop2Message());
			Assert.assertEquals("Compare Top3Message", expectedTable.getRow(0).get("Top3Message"), cmPresetInfos.getCMPresetInfoList().get(0).getTop3Message());
			Assert.assertEquals("Compare Top4Message", expectedTable.getRow(0).get("Top4Message"), cmPresetInfos.getCMPresetInfoList().get(0).getTop4Message());
			Assert.assertEquals("Compare Top5Message", expectedTable.getRow(0).get("Top5Message"), cmPresetInfos.getCMPresetInfoList().get(0).getTop5Message());
			Assert.assertEquals("Compare Bottom1Message", expectedTable.getRow(0).get("Bottom1Message"), cmPresetInfos.getCMPresetInfoList().get(0).getBottom1Message());
			Assert.assertEquals("Compare Bottom2Message", expectedTable.getRow(0).get("Bottom2Message"), cmPresetInfos.getCMPresetInfoList().get(0).getBottom2Message());
			Assert.assertEquals("Compare Bottom3Message", expectedTable.getRow(0).get("Bottom3Message"), cmPresetInfos.getCMPresetInfoList().get(0).getBottom3Message());
			Assert.assertEquals("Compare Bottom4Message", expectedTable.getRow(0).get("Bottom4Message"), cmPresetInfos.getCMPresetInfoList().get(0).getBottom4Message());
			Assert.assertEquals("Compare Bottom5Message", expectedTable.getRow(0).get("Bottom5Message"), cmPresetInfos.getCMPresetInfoList().get(0).getBottom5Message());
		}break;
		case GETCOUNTPAYMENTAMT: {
			int i = 0;
			for (Map<String, String> expected : expectedTable.getRows()) {
				Assert.assertEquals("Compare TenderId " + i, expected.get("TenderId"), settlementInfo.getPaymentAmtList().get(i).getTenderId());
				Assert.assertEquals("Compare TenderName " + i, expected.get("TenderName"), settlementInfo.getPaymentAmtList().get(i).getTenderName());
				Assert.assertEquals("Compare TenderType " + i, expected.get("TenderType"), settlementInfo.getPaymentAmtList().get(i).getTenderType());
				Assert.assertEquals("Compare TenderIdentification " + i, expected.get("TenderIdentification"), settlementInfo.getPaymentAmtList().get(i).getTenderIdentification());				
				Assert.assertEquals("Compare SumAmt " + i, Integer.parseInt(expected.get("SumAmt")), settlementInfo.getPaymentAmtList().get(i).getSumAmt());
				i++;
			}
		}break;
		default:
			break;
		}
	}

	@When("getting suspended transactions companyid:$1 storeid:$2 queue:$3 trainingflag:$4 layawayflag:$5 txtype:$6")
	public final void getSuspendedTxs(final String companyId,
			final String retailStoreId, final String queue,
			final String trainingFlag, final String layawayFlag,
			final String txType) {
		operation = Operation.GETSUSPENDEDTXS;
		suspendeTxs = (ForwardList) forwardItemResource.getForwardList(
				companyId, retailStoreId, trainingFlag, layawayFlag, queue,
				txType);
	}

	@Given("that has no other working devices")
	public final void givenNoOtherWorkingDevices() {
		try {
			dbRESMasterInitiator = new DBInitiator("EndOfDaySteps2",
					DATABASE.RESMaster);
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/MST_TILLIDINFO.xml");
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/MST_BIZDAY.xml");
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/AUT_DEVICES.xml");
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/MST_DEVICEINFO.xml");
			dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps",
					DATABASE.RESTransaction);
			dbRESTransactionInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/TXU_POS_CTRL.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Given("that EOD has started")
	public final void givenThatEODStarted(){
		try {
			dbRESMasterInitiator = new DBInitiator("EndOfDaySteps2",
					DATABASE.RESMaster);
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/MST_TILLIDINFO_(Started).xml");
			dbRESMasterInitiator.ExecuteOperation(
					DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/eod/test/MST_BIZDAY.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("getting execute authority companyid:$1 retailstoreid:$2 tillid:$3 terminalid:$4 operatorno:$5 processing:$6 compulsoryflag:$7")
	public final void getExecuteAuthority(final String companyId,
			final String storeId, final String tillId, final String terminalId,
			final String operatorNo, final String processingType,
			final String compulsoryFlag) {
		operation = Operation.GETEXECUTEAUTHORITY;
		executeResult = tillInfoResource.getExecuteAuthority(companyId,
				storeId, tillId, terminalId, operatorNo, processingType,
				compulsoryFlag);
	}

	@Given("that execute authority is done for companyid:$1 retailstoreid:$2 tillid:$3 terminalid:$4 operatorno:$5 processing:$6 compulsoryflag:$7")
	public final void executeAuthorityIsDone(final String companyId,
			final String storeId, final String tillId, final String terminalId,
			final String operatorNo, final String processingType,
			final String compulsoryFlag) {
		executeResult = tillInfoResource.getExecuteAuthority(companyId,
				storeId, tillId, terminalId, operatorNo, processingType,
				compulsoryFlag);
		Assert.assertEquals("Success Execute Authority",
				executeResult.getNCRWSSResultCode(), 0);
	}

	@When("getting the voucher list companyId:$1 storeId:$2 businessDayDate:$3 trainingFlag:$4 tillId:$5 terminalId:$6")
	public final void getVoucherList(final String companyId,
			final String storeId, final String businessDayDate,
			final int trainingFlag, final String tillId,
			final String terminalIdParam) {
		operation = Operation.GETVOUCHERLIST;
		String terminalId = terminalIdParam.equalsIgnoreCase("Empty") ? ""
				: terminalIdParam;
		voucherList = settlementResource.getVoucherList(companyId, storeId,
				tillId, terminalId, businessDayDate, trainingFlag);
	}

	@When("getting working devices companyId:$1 storeId:$2 terminalId:$2")
	public final void getWorkingDevices(final String companyId,
			final String storeId, final String terminalId) {
		operation = Operation.GETWORKINGDEVICES;
		workingDevices = (WorkingDevices) deviceInfoResource.getWorkingDevices(
				companyId, storeId, terminalId);
	}

	@Then("it should get the NCRWSSResultCode:$1")
	public final void getResultCode(final String resultCode) {
		switch (operation) {
		case GETWORKINGDEVICES: {
			Assert.assertEquals("Compare NCRWSSResultCode",
					Integer.parseInt(resultCode),
					workingDevices.getNCRWSSResultCode());
		}
			break;
		case RELEASEEXECAUTHORITY: {
			Assert.assertEquals("Compare NCRWSSResultCode",
					Integer.parseInt(resultCode),
					releaseExecAuthResult.getNCRWSSResultCode());
		}
		default:
			break;
		}
	}

	@Then("it should get the following group.terminals:$expected")
	public final void getGroupTillId(ExamplesTable expectedTable) {
		int i = 0;
		for (Map<String, String> expected : expectedTable.getRows()) {
			Assert.assertEquals("Compare terminalName",
					expected.get("terminalName"), workingDevices
							.getOwnTillGroup().getTerminals().get(i)
							.getTerminalName());
			Assert.assertEquals("Compare terminalid",
					expected.get("terminalid"), workingDevices
							.getOwnTillGroup().getTerminals().get(i)
							.getTerminalId());
		}
	}

	@Then("MST_TILLIDINFO table should have: $expected")
	public final void checkMstTillIdInfo(final ExamplesTable expecteditems) throws DataSetException {
		List<Map<String, String>> expectedItemRows = expecteditems.getRows();
		ITable actualItemRows = dbRESMasterInitiator.getTableSnapshot("MST_TILLIDINFO");
		Assert.assertEquals("Compare that the number of rows in Items are exact: ", expecteditems.getRowCount(),
				actualItemRows.getRowCount());
		int i = 0;
		for (Map<String, String> expItem : expectedItemRows) {
			Assert.assertEquals("Compare the CompanyId row " + i + ": ", expItem.get("CompanyId"), actualItemRows.getValue(i, "CompanyId").toString());
			Assert.assertEquals("Compare the StoreId row " + i + ": ", expItem.get("StoreId"), actualItemRows.getValue(i, "StoreId").toString());
			Assert.assertEquals("Compare the TillId row " + i + ": ", expItem.get("TillId"), actualItemRows.getValue(i, "TillId").toString());
			Assert.assertEquals("Compare the TerminalId row " + i + ": ", expItem.get("TerminalId"), actualItemRows.getValue(i, "TerminalId").toString());
			Assert.assertEquals("Compare the BusinessDayDate row " + i + ": ", expItem.get("BusinessDayDate"), actualItemRows.getValue(i, "BusinessDayDate").toString());
			Assert.assertEquals("Compare the SodFlag row " + i + ": ", expItem.get("SodFlag"), actualItemRows.getValue(i, "SodFlag").toString());
			Assert.assertEquals("Compare the EodFlag row " + i + ": ", expItem.get("EodFlag"), actualItemRows.getValue(i, "EodFlag").toString());			
			i++;
		}
	}
	@Then("MST_NAME_DAILYREPORT table should have: $expected")
	public final void checkMstNameDailyReport(final ExamplesTable expecteditems) throws DataSetException {
		List<Map<String, String>> expectedItemRows = expecteditems.getRows();
		ITable actualItemRows = dbRESMasterInitiator.getTableSnapshot("MST_NAME_DAILYREPORT");
		Assert.assertEquals("Compare that the number of rows in Items are exact: ", expecteditems.getRowCount(),
				actualItemRows.getRowCount());
		int i = 0;
		for (Map<String, String> expItem : expectedItemRows) {
			Assert.assertEquals("Compare the CompanyId row " + i + ": ", expItem.get("CompanyId"), actualItemRows.getValue(i, "CompanyId").toString());
			Assert.assertEquals("Compare the StoreId row " + i + ": ", expItem.get("StoreId"), actualItemRows.getValue(i, "StoreId").toString());
			Assert.assertEquals("Compare the DataType row " + i + ": ", expItem.get("DataType"), actualItemRows.getValue(i, "DataType").toString());
			Assert.assertEquals("Compare the ItemLevel1 row " + i + ": ", expItem.get("ItemLevel1"), actualItemRows.getValue(i, "ItemLevel1").toString());
			Assert.assertEquals("Compare the ItemLevel2 row " + i + ": ", expItem.get("ItemLevel2"), actualItemRows.getValue(i, "ItemLevel2").toString());
			Assert.assertEquals("Compare the ItemLevel3 row " + i + ": ", expItem.get("ItemLevel3"), actualItemRows.getValue(i, "ItemLevel3").toString().trim());
			Assert.assertEquals("Compare the ItemLevel4 row " + i + ": ", expItem.get("ItemLevel4"), actualItemRows.getValue(i, "ItemLevel4").toString().trim());	
			Assert.assertEquals("Compare the ItemName row " + i + ": ", expItem.get("ItemName"), actualItemRows.getValue(i, "ItemName").toString());	
			Assert.assertEquals("Compare the DisplayOrder row " + i + ": ", expItem.get("DisplayOrder"), actualItemRows.getValue(i, "DisplayOrder").toString());	
			Assert.assertEquals("Compare the DeleteFlag row " + i + ": ", expItem.get("DeleteFlag"), actualItemRows.getValue(i, "DeleteFlag").toString());	
			i++;
		}
	}	
	
	@Then("TXU_TOTAL_DAILYREPORT table should have: $expected")
	public final void checkTxuTotalDailyReport(final ExamplesTable expecteditems)
			throws DataSetException {
		List<Map<String, String>> expectedItemRows = expecteditems.getRows();
		ITable actualItemRows = dbRESTransactionInitiator.getTableSnapshot("TXU_TOTAL_DAILYREPORT");
		Assert.assertEquals("Compare that the number of rows in Items are exact: ", expecteditems.getRowCount(), actualItemRows.getRowCount());
		// |CompanyId |RetailStoreId |WorkstationId |BusinessDayDate |ItemType |ItemLevel1 |ItemLevel2 |ItemLevel3 |ItemLevel4 |TrainingFlag|ItemCnt |ItemAmt |
		int i = 0;
		for (Map<String, String> expItem : expectedItemRows) {
			Assert.assertEquals("Compare the CompanyId row " + i + ": ", expItem.get("CompanyId"), actualItemRows.getValue(i, "CompanyId").toString());
			Assert.assertEquals("Compare the RetailStoreId row " + i + ": ", expItem.get("RetailStoreId"), actualItemRows.getValue(i, "RetailStoreId").toString());
			Assert.assertEquals("Compare the WorkstationId row " + i + ": ", expItem.get("WorkstationId"), actualItemRows.getValue(i, "WorkstationId").toString());
			Assert.assertEquals("Compare the BusinessDayDate row " + i + ": ", expItem.get("BusinessDayDate"), actualItemRows.getValue(i, "BusinessDayDate").toString());
			Assert.assertEquals("Compare the ItemType row " + i + ": ", expItem.get("ItemType"), actualItemRows.getValue(i, "ItemType").toString());
			Assert.assertEquals("Compare the ItemLevel1 row " + i + ": ", expItem.get("ItemLevel1"), actualItemRows.getValue(i, "ItemLevel1").toString());
			Assert.assertEquals("Compare the ItemLevel2 row " + i + ": ", expItem.get("ItemLevel2"), actualItemRows.getValue(i, "ItemLevel2").toString());
			Assert.assertEquals("Compare the ItemLevel3 row " + i + ": ", expItem.get("ItemLevel3"), actualItemRows.getValue(i, "ItemLevel3").toString());
			Assert.assertEquals("Compare the ItemLevel4 row " + i + ": ", expItem.get("ItemLevel4"), actualItemRows.getValue(i, "ItemLevel4").toString());
			Assert.assertEquals("Compare the TrainingFlag row " + i + ": ", expItem.get("TrainingFlag"), actualItemRows.getValue(i, "TrainingFlag").toString());
			Assert.assertEquals("Compare the ItemCnt row " + i + ": ", expItem.get("ItemCnt"), actualItemRows.getValue(i, "ItemCnt").toString());
			Assert.assertEquals("Compare the ItemAmt row " + i + ": ", expItem.get("ItemAmt"), actualItemRows.getValue(i, "ItemAmt").toString());
			i++;
		}
	}
	
	@Then("TXL_SALES_JOURNAL table should have: $expected")
	public final void checkTxlSalesJournal(final ExamplesTable expecteditems) throws DataSetException {
		List<Map<String, String>> expectedItemRows = expecteditems.getRows();
		ITable actualItemRows = dbRESTransactionInitiator.getTableSnapshot("TXL_SALES_JOURNAL");
		Assert.assertEquals("Compare that the number of rows in Items are exact: ", expecteditems.getRowCount(),
				actualItemRows.getRowCount());
		int i = 0;
		for (Map<String, String> expItem : expectedItemRows) {
			Assert.assertEquals("Compare the CompanyId row " + i + ": ", expItem.get("CompanyId"), actualItemRows.getValue(i, "CompanyId").toString());
			Assert.assertEquals("Compare the RetailStoreId row " + i + ": ", expItem.get("RetailStoreId"), actualItemRows.getValue(i, "RetailStoreId").toString());
			Assert.assertEquals("Compare the WorkstationId row " + i + ": ", expItem.get("WorkstationId"), actualItemRows.getValue(i, "WorkstationId").toString());
			Assert.assertEquals("Compare the SequenceNumber row " + i + ": ", expItem.get("SequenceNumber"), actualItemRows.getValue(i, "SequenceNumber").toString());
			Assert.assertEquals("Compare the BusinessDayDate row " + i + ": ", expItem.get("BusinessDayDate"), actualItemRows.getValue(i, "BusinessDayDate").toString());
			Assert.assertEquals("Compare the TrainingFlag row " + i + ": ", expItem.get("TrainingFlag"), actualItemRows.getValue(i, "TrainingFlag").toString());
			Assert.assertEquals("Compare the TxType row " + i + ": ", expItem.get("TxType"), actualItemRows.getValue(i, "TxType").toString());			
			Assert.assertEquals("Compare the ServerId row " + i + ": ", expItem.get("ServerId"), actualItemRows.getValue(i, "ServerId").toString());			
			Assert.assertEquals("Compare the Status row " + i + ": ", expItem.get("Status"), actualItemRows.getValue(i, "Status").toString());			
			Assert.assertEquals("Compare the SendStatus1 row " + i + ": ", expItem.get("SendStatus1"), actualItemRows.getValue(i, "SendStatus1").toString());			
			Assert.assertEquals("Compare the SendStatus2 row " + i + ": ", expItem.get("SendStatus2"), actualItemRows.getValue(i, "SendStatus2").toString());	
			Assert.assertEquals("Compare the EjStatus row " + i + ": ", expItem.get("EjStatus"), actualItemRows.getValue(i, "EjStatus").toString());	
			Assert.assertEquals("Compare the Tx row " + i + ": ", expItem.get("Tx"), actualItemRows.getValue(i, "Tx").toString());	
			i++;
		}
	}
	
	@Then("MST_DEVICEINFO table should have: $expected")
	public final void checkMstDeviceInfo(final ExamplesTable expecteditems) throws DataSetException {
		List<Map<String, String>> expectedItemRows = expecteditems.getRows();
		ITable actualItemRows = dbRESMasterInitiator.getTableSnapshot("MST_DEVICEINFO");
		Assert.assertEquals("Compare that the number of rows in Items are exact: ", expecteditems.getRowCount(),
				actualItemRows.getRowCount());
		int i = 0;
		for (Map<String, String> expItem : expectedItemRows) {
			Assert.assertEquals("Compare the CompanyId row " + i + ": ", expItem.get("CompanyId"), actualItemRows.getValue(i, "CompanyId").toString());
			Assert.assertEquals("Compare the StoreId row " + i + ": ", expItem.get("StoreId"), actualItemRows.getValue(i, "StoreId").toString());
			Assert.assertEquals("Compare the TerminalId row " + i + ": ", expItem.get("TerminalId"), actualItemRows.getValue(i, "TerminalId").toString());
			Assert.assertEquals("Compare the Training row " + i + ": ", expItem.get("Training"), actualItemRows.getValue(i, "Training").toString());
			Assert.assertEquals("Compare the DeviceName row " + i + ": ", expItem.get("DeviceName"), actualItemRows.getValue(i, "DeviceName").toString());
			Assert.assertEquals("Compare the AttributeId row " + i + ": ", expItem.get("AttributeId"), actualItemRows.getValue(i, "AttributeId").toString());
			Assert.assertEquals("Compare the TillId row " + i + ": ", expItem.get("TillId"), actualItemRows.getValue(i, "TillId").toString());			
			Assert.assertEquals("Compare the LastTxId row " + i + ": ", expItem.get("LastTxId"), actualItemRows.getValue(i, "LastTxId").toString());			
			Assert.assertEquals("Compare the Status row " + i + ": ", expItem.get("Status"), actualItemRows.getValue(i, "Status").toString());	
			i++;
		}
	}
	
	@When("getting till information of storeid:$1 tillid:$2")
	public final void getTillInfo(final String storeId, final String tillId) {
		operation = Operation.GETTILLINFO;
		till = tillInfoResource.viewTill(storeId, tillId);
	}

	@When("releasing execute authority for EOD companyid:$1 retailstoreid:$2 tillid:$3 terminalid:$4 operatorno:$5 processing:$6")
	public final void releaseExecuteAuthority(final String companyId,
			final String storeId, final String tillId, final String terminalId,
			final String operatorNo, final String processingType) {
		operation = Operation.RELEASEEXECAUTHORITY;
		releaseExecAuthResult = tillInfoResource.releaseExecuteAuthority(
				companyId, storeId, tillId, terminalId, operatorNo,
				processingType);
	}

	@When("getting the device status of companyId:$1 storeId:$2 terminalId:$3")
	public final void getDeviceStatus(final String companyId,
			final String storeId, final String terminalId) {
		operation = Operation.GETDEVICESTATUS;
		deviceStatResult = (PosControlOpenCloseStatus) deviceInfoResource
				.getDeviceStatus(companyId, storeId, terminalId);
	}

	@When("getting credit companyId:$1 storeId:$2 businessDate:$3 trainingFlag:$4 dataType:$5 itemLevel1:$6 itemLevel2:$7 tillId:$8 terminalId:$9")
	public final void getCredit(final String companyId, final String storeId,
			final String businessDate, final int trainingFlag,
			final String dataType, final String itemLevel1,
			final String itemLevel2, final String tillId,
			final String terminalIdParam) {
		operation = Operation.GETCREDIT;
		String terminalId = terminalIdParam.equalsIgnoreCase("null") ? null
				: terminalIdParam;
		settlementInfo = settlementResource.getCredit(companyId, storeId,
				tillId, terminalId, businessDate, trainingFlag, dataType,
				itemLevel1, itemLevel2);
	}
}
