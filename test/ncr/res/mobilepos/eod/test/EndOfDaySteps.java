package ncr.res.mobilepos.eod.test;

import java.lang.reflect.Field;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.deviceinfo.model.PosControlOpenCloseStatus;
import ncr.res.mobilepos.deviceinfo.model.WorkingDevices;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.forwarditemlist.resource.ForwardItemListResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.journalization.model.ForwardList;
import ncr.res.mobilepos.journalization.model.SearchedPosLog;
import ncr.res.mobilepos.journalization.resource.JournalizationResource;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.settlement.model.SettlementInfo;
import ncr.res.mobilepos.settlement.resource.SettlementResource;
import ncr.res.mobilepos.tillinfo.model.ViewTill;
import ncr.res.mobilepos.tillinfo.resource.TillInfoResource;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;
import org.junit.Assert;

public class EndOfDaySteps extends Steps {

	private JournalizationResource journalizationResource;
	private DBInitiator dbRESTransactionInitiator;
	private DBInitiator dbRESMasterInitiator;
	public enum Operation {
		GETLASTPAYINPAYOUT, GETSUSPENDEDTXS, GETEXECUTEAUTHORITY, GETWORKINGDEVICES, GETTILLINFO, RELEASEEXECAUTHORITY, GETDEVICESTATUS, GETCREDIT, GETVOUCHERLIST
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
	
	@BeforeScenario
	public final void setUp(){
		Requirements.SetUp();		
		ServletContext mockContext = Requirements.getMockServletContext();
		journalizationResource = new JournalizationResource();
		forwardItemResource = new ForwardItemListResource();
		tillInfoResource = new TillInfoResource();
		deviceInfoResource = new DeviceInfoResource();
		settlementResource = new SettlementResource();
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
	public final void tearDown(){
		Requirements.TearDown();
	}
	@Given("that has payments type")
	public final void givenHasPaymentTypes() {
		try{
			dbRESMasterInitiator = new DBInitiator("EndOfDaySteps2", DATABASE.RESMaster);
			dbRESMasterInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/eod/test/MST_TENDERINFO.xml");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	@Given("that has no last payin/payout transactions")
	public final void givenLastPayTransactions(){
		dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps", DATABASE.RESTransaction);
		try {
			dbRESTransactionInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/eod/test/TXL_SALES_JOURNAL_EMPTY.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Given("that has suspended transactions")
	public final void givenSuspendedTransactions(){
		try{
			dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps", DATABASE.RESTransaction);
			dbRESTransactionInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/eod/test/TXL_FORWARD_ITEM.xml");
			dbRESMasterInitiator = new DBInitiator("EndOfDaySteps2", DATABASE.RESMaster);
			dbRESMasterInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/eod/test/MST_EMPINFO.xml");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	@Given("that has no suspended transactions")
	public final void givenNoSuspendedTransactions(){
		try{
			dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps", DATABASE.RESTransaction);
			dbRESTransactionInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/eod/test/TXL_FORWARD_ITEM_EMPTY.xml");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}
	@Given("that has credit payments")
	public final void givenHasCreditPayments(){
		try{
			dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps", DATABASE.RESTransaction);
			dbRESTransactionInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/eod/test/TXU_TOTAL_DAILYREPORT_(Credit).xml");
		}catch(Exception ex){
			ex.printStackTrace();
		}
	}	
	@When("getting last payin/payout transactions companyid:$1 storeid:$2 terminalid:$3 businessdate:$4 trainingflag:$5")
	public final void whenGettingLastPayTxs(final String companyId, final String storeId, final String terminalId, final String businessDate, final String trainingFlag){
		operation = Operation.GETLASTPAYINPAYOUT;
		payinoutPoslog = journalizationResource.getLastPayTxPoslog(companyId, storeId, terminalId, businessDate, Integer.valueOf(trainingFlag));
	}
	@Then("it should get the following: $expected")
	public final void getTheFollowingExpected(ExamplesTable expectedTable) {
		switch (operation) {
			case GETLASTPAYINPAYOUT: {
				Assert.assertEquals("Compare NCRWSSResultCode", Integer
						.parseInt(expectedTable.getRow(0).get("NCRWSSResultCode")),
						payinoutPoslog.getNCRWSSResultCode());
				Assert.assertEquals(
						"Compare NCRWSSExtendedResultCode",
						Integer.parseInt(expectedTable.getRow(0).get(
								"NCRWSSExtendedResultCode")),
						payinoutPoslog.getNCRWSSExtendedResultCode());
				Assert.assertEquals("Compare TrainingModeFlag", expectedTable.getRow(0)
						.get("TrainingModeFlag").equalsIgnoreCase("null") ? null
						: expectedTable.getRow(0).get("TrainingModeFlag"),
						payinoutPoslog.getTransaction().getTrainingModeFlag());
				Assert.assertEquals(
						"Compare CancelFlag",
						expectedTable.getRow(0).get("CancelFlag")
								.equalsIgnoreCase("null") ? null : expectedTable.getRow(
								0).get("CancelFlag"), payinoutPoslog
								.getTransaction().getCancelFlag());
			}; break;
			case GETSUSPENDEDTXS: {
				int i = 0;
				for (Map<String, String> expected: expectedTable.getRows()) {
					Assert.assertEquals("Compare CompanyId", expected.get("CompanyId"), suspendeTxs.getForwardListInfo().get(i).getCompanyId());
					Assert.assertEquals("Compare RetailStoreId", expected.get("RetailStoreId"), suspendeTxs.getForwardListInfo().get(i).getRetailStoreId());
					Assert.assertEquals("Compare WorkstationId", expected.get("WorkstationId"), suspendeTxs.getForwardListInfo().get(i).getWorkstationId());
					Assert.assertEquals("Compare SequenceNumber", expected.get("SequenceNumber"), suspendeTxs.getForwardListInfo().get(i).getSequenceNumber());
					Assert.assertEquals("Compare Queue", expected.get("Queue"), suspendeTxs.getForwardListInfo().get(i).getQueue());
					Assert.assertEquals("Compare BusinessDayDate", expected.get("BusinessDayDate"), suspendeTxs.getForwardListInfo().get(i).getBusinessDayDate());
					Assert.assertEquals("Compare TrainingFlag", expected.get("TrainingFlag"), suspendeTxs.getForwardListInfo().get(i).getTrainingFlag());
					Assert.assertEquals("Compare BusinessDateTime", expected.get("BusinessDateTime"), suspendeTxs.getForwardListInfo().get(i).getBusinessDateTime());
					Assert.assertEquals("Compare OperatorId", expected.get("OperatorId"), suspendeTxs.getForwardListInfo().get(i).getOperatorId());
					Assert.assertEquals("Compare OperatorName", expected.get("OperatorName"), suspendeTxs.getForwardListInfo().get(i).getOperatorName());
					Assert.assertEquals("Compare SalesTotalAmt", expected.get("SalesTotalAmt"), suspendeTxs.getForwardListInfo().get(i).getSalesTotalAmt());
					Assert.assertEquals("Compare Status", expected.get("Status"), suspendeTxs.getForwardListInfo().get(i).getStatus());
					i++;
				}
			}break;
			case GETEXECUTEAUTHORITY: {
				Assert.assertEquals("Compare NCRWSSResultCode", Integer.parseInt(expectedTable.getRow(0).get("NCRWSSResultCode")), executeResult.getNCRWSSResultCode());
				Assert.assertEquals("Compare NCRWSSExtendedResultCode", Integer.parseInt(expectedTable.getRow(0).get("NCRWSSExtendedResultCode")), executeResult.getNCRWSSExtendedResultCode());
			}break;
			case GETTILLINFO: {
				Assert.assertEquals("Compare NCRWSSResultCode", Integer.parseInt(expectedTable.getRow(0).get("NCRWSSResultCode")), till.getNCRWSSResultCode());
				Assert.assertEquals("Compare StoreId", expectedTable.getRow(0).get("StoreId"), till.getTill().getStoreId());
				Assert.assertEquals("Compare TillId", expectedTable.getRow(0).get("TillId"), till.getTill().getTillId());
				Assert.assertEquals("Compare BusinessDayDate", expectedTable.getRow(0).get("BusinessDayDate"), till.getTill().getBusinessDayDate());
				Assert.assertEquals("Compare SodFlag", expectedTable.getRow(0).get("SodFlag"), till.getTill().getSodFlag());
				Assert.assertEquals("Compare EodFlag", expectedTable.getRow(0).get("EodFlag"), till.getTill().getEodFlag());
			}break;
			case GETDEVICESTATUS: {
				Assert.assertEquals("Compare OpenCloseStatus", Short.parseShort(expectedTable.getRow(0).get("OpenCloseStatus")), deviceStatResult.getOpenCloseStat());
				Assert.assertEquals("Compare NCRWSSResultCode", Integer.parseInt(expectedTable.getRow(0).get("NCRWSSResultCode")), deviceStatResult.getNCRWSSResultCode());
				Assert.assertEquals("Compare NCRWSSExtendedResultCode", Integer.parseInt(expectedTable.getRow(0).get("NCRWSSExtendedResultCode")), deviceStatResult.getNCRWSSExtendedResultCode());
			}break;
			case GETCREDIT: {
				Assert.assertEquals("Compare CompanyId", expectedTable.getRow(0).get("CompanyId"), settlementInfo.getCreditInfo().getCompanyId());
				Assert.assertEquals("Compare RetailStoreId", expectedTable.getRow(0).get("RetailStoreId"), settlementInfo.getCreditInfo().getStoreId());
				Assert.assertEquals("Compare BusinessDayDate", expectedTable.getRow(0).get("BusinessDayDate"), settlementInfo.getCreditInfo().getBusinessDayDate());
				Assert.assertEquals("Compare TrainingFlag", Integer.parseInt(expectedTable.getRow(0).get("TrainingFlag")), settlementInfo.getCreditInfo().getTrainingFlag());
				Assert.assertEquals("Compare SalesCntSum", Integer.parseInt(expectedTable.getRow(0).get("SalesCntSum")), settlementInfo.getCreditInfo().getSalesCntSum());
				Assert.assertEquals("Compare SalesItemAmt", expectedTable.getRow(0).get("SalesItemAmt"), String.valueOf(settlementInfo.getCreditInfo().getSalesItemAmt()));
				Assert.assertEquals("Compare SalesItemCnt", Integer.parseInt(expectedTable.getRow(0).get("SalesItemCnt")), settlementInfo.getCreditInfo().getSalesItemCnt());
				Assert.assertEquals("Compare SalesAmtSum", expectedTable.getRow(0).get("SalesAmtSum"), String.valueOf(settlementInfo.getCreditInfo().getSalesAmtSum()));
			}break;
			case GETVOUCHERLIST: {
				//|CompanyId	|StoreId	|VoucherCompanyId	|VoucherType	|TrainingFlag	|SalesItemCnt	|SalesItemAmt	|VoucherName|VoucherKanaName	|
				int i = 0;
				for (Map<String, String> expected: expectedTable.getRows()) {
					Assert.assertEquals("Compare CompanyId", expected.get("CompanyId"), voucherList.getVoucherList().get(i).getCompanyId());
					Assert.assertEquals("Compare StoreId", expected.get("StoreId"), voucherList.getVoucherList().get(i).getStoreId());
					Assert.assertEquals("Compare VoucherCompanyId", expected.get("VoucherCompanyId"), voucherList.getVoucherList().get(i).getVoucherCompanyId());
					Assert.assertEquals("Compare VoucherType", expected.get("VoucherType"), voucherList.getVoucherList().get(i).getVoucherType());
					Assert.assertEquals("Compare TrainingFlag", Integer.parseInt(expected.get("TrainingFlag")), voucherList.getVoucherList().get(i).getTrainingFlag());
					Assert.assertEquals("Compare SalesItemCnt", Integer.parseInt(expected.get("SalesItemCnt")), voucherList.getVoucherList().get(i).getSalesItemCnt());
					Assert.assertEquals("Compare SalesItemAmt", expected.get("SalesItemAmt"), String.valueOf(voucherList.getVoucherList().get(i).getSalesItemAmt()));
					Assert.assertEquals("Compare VoucherName", expected.get("VoucherName"), voucherList.getVoucherList().get(i).getVoucherName());
					Assert.assertEquals("Compare VoucherKanaName", expected.get("VoucherKanaName"), voucherList.getVoucherList().get(i).getVoucherKanaName());
					i++;
				}
			}break;
			default: break;
		}
	}
	@When("getting suspended transactions companyid:$1 storeid:$2 queue:$3 trainingflag:$4 layawayflag:$5 txtype:$6")
	public final void getSuspendedTxs(final String companyId, final String retailStoreId, final String queue, final String trainingFlag, final String layawayFlag, final String txType){
		operation = Operation.GETSUSPENDEDTXS;
		suspendeTxs = (ForwardList) forwardItemResource.getForwardList(companyId, retailStoreId, trainingFlag, layawayFlag, queue, txType);
	}
	@Given("that has no other working devices")
	public final void givenNoOtherWorkingDevices(){
		try {
			dbRESMasterInitiator = new DBInitiator("EndOfDaySteps2", DATABASE.RESMaster);
			dbRESMasterInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/eod/test/MST_TILLIDINFO.xml");
			dbRESMasterInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/eod/test/MST_BIZDAY.xml");
			dbRESMasterInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/eod/test/AUT_DEVICES.xml");
			dbRESMasterInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/eod/test/MST_DEVICEINFO.xml");
			dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps", DATABASE.RESTransaction);
			dbRESTransactionInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/eod/test/TXU_POS_CTRL.xml");
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
		executeResult = tillInfoResource.getExecuteAuthority(companyId, storeId, tillId,
				terminalId, operatorNo, processingType, compulsoryFlag);
	}
	@Given("that execute authority is done for companyid:$1 retailstoreid:$2 tillid:$3 terminalid:$4 operatorno:$5 processing:$6 compulsoryflag:$7")
	public final void executeAuthorityIsDone(final String companyId,
			final String storeId, final String tillId, final String terminalId,
			final String operatorNo, final String processingType,
			final String compulsoryFlag) {
		executeResult = tillInfoResource.getExecuteAuthority(companyId, storeId, tillId,
				terminalId, operatorNo, processingType, compulsoryFlag);
		Assert.assertEquals("Success Execute Authority", executeResult.getNCRWSSResultCode(), 0);
	}
	@When("getting the voucher list companyId:$1 storeId:$2 businessDayDate:$3 trainingFlag:$4 tillId:$5 terminalId:$6")
	public final void getVoucherList(final String companyId,
			final String storeId, final String businessDayDate,
			final int trainingFlag, final String tillId,
			final String terminalIdParam) {
		operation = Operation.GETVOUCHERLIST;
		String terminalId = terminalIdParam.equalsIgnoreCase("Empty") ? "": terminalIdParam;
		voucherList = settlementResource.getVoucherList(companyId, storeId, tillId, terminalId, businessDayDate, trainingFlag);	
	}	
	@When("getting working devices companyId:$1 storeId:$2 terminalId:$2")
	public final void getWorkingDevices(final String companyId, final String storeId, final String terminalId){
		operation = Operation.GETWORKINGDEVICES;
		workingDevices = (WorkingDevices) deviceInfoResource.getWorkingDevices(companyId, storeId, terminalId);
	}
	@Then("it should get the NCRWSSResultCode:$1")
	public final void getResultCode(final String resultCode){
		switch (operation) {
			case GETWORKINGDEVICES: {
				Assert.assertEquals("Compare NCRWSSResultCode", Integer.parseInt(resultCode), workingDevices.getNCRWSSResultCode());
			} break;
			case RELEASEEXECAUTHORITY: {
				Assert.assertEquals("Compare NCRWSSResultCode", Integer.parseInt(resultCode), releaseExecAuthResult.getNCRWSSResultCode());
			}
		default:
			break;
		}
	}
	@Then("it should get the following group.terminals:$expected")
	public final void getGroupTillId(ExamplesTable expectedTable){
		int i = 0;
		for(Map<String, String> expected: expectedTable.getRows()){
			Assert.assertEquals("Compare terminalName", expected.get("terminalName"), workingDevices.getOwnTillGroup().getTerminals().get(i).getTerminalName());
			Assert.assertEquals("Compare terminalid", expected.get("terminalid"), workingDevices.getOwnTillGroup().getTerminals().get(i).getTerminalId());
		}
	}
	@When("getting till information of storeid:$1 tillid:$2")
	public final void getTillInfo(final String storeId, final String tillId){
		operation = Operation.GETTILLINFO;
		till = tillInfoResource.viewTill(storeId, tillId);
	}
	@When("releasing execute authority for EOD companyid:$1 retailstoreid:$2 tillid:$3 terminalid:$4 operatorno:$5 processing:$6")
	public final void releaseExecuteAuthority(final String companyId,
			final String storeId, final String tillId, final String terminalId,
			final String operatorNo, final String processingType) {
		operation = Operation.RELEASEEXECAUTHORITY;
		releaseExecAuthResult = tillInfoResource.releaseExecuteAuthority(companyId, storeId, tillId,
				terminalId, operatorNo, processingType);
	}
	@When("getting the device status of companyId:$1 storeId:$2 terminalId:$3")
	public final void getDeviceStatus(final String companyId, final String storeId, final String terminalId) {
		operation = Operation.GETDEVICESTATUS;
		deviceStatResult = (PosControlOpenCloseStatus)deviceInfoResource.getDeviceStatus(companyId, storeId, terminalId);
	}
	@When("getting credit companyId:$1 storeId:$2 businessDate:$3 trainingFlag:$4 dataType:$5 itemLevel1:$6 itemLevel2:$7 tillId:$8 terminalId:$9")
	public final void getCredit(final String companyId, final String storeId,
			final String businessDate, final int trainingFlag,
			final String dataType, final String itemLevel1,
			final String itemLevel2, final String tillId,
			final String terminalIdParam) {
		operation = Operation.GETCREDIT;
		String terminalId = terminalIdParam.equalsIgnoreCase("null") ? null: terminalIdParam;
		settlementInfo = settlementResource.getCredit(companyId, storeId, tillId, terminalId, businessDate, trainingFlag, dataType, itemLevel1, itemLevel2);
		System.out.println(settlementInfo);
	}
}
