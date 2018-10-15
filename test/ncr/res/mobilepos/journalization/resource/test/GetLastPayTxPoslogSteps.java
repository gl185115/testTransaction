package ncr.res.mobilepos.journalization.resource.test;

import junit.framework.Assert;

import org.dbunit.dataset.ITable;
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

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Map;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.model.PosControlOpenCloseStatus;
import ncr.res.mobilepos.deviceinfo.model.WorkingDevices;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.eod.test.EndOfDaySteps.Operation;
import ncr.res.mobilepos.forwarditemlist.resource.ForwardItemListResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.POSLogHandler;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.journalization.model.ForwardList;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.model.SearchedPosLog;
import ncr.res.mobilepos.journalization.model.SearchedPosLogs;
import ncr.res.mobilepos.journalization.resource.JournalizationResource;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.tillinfo.model.ViewTill;
import ncr.res.mobilepos.tillinfo.resource.TillInfoResource;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

public class GetLastPayTxPoslogSteps extends Steps {
	private JournalizationResource journalizationResource;
	private DBInitiator dbRESTransactionInitiator;
	private SearchedPosLog payinoutPoslog = null;
	
	@BeforeScenario
	public final void setUp(){
		Requirements.SetUp();		
		ServletContext mockContext = Requirements.getMockServletContext();
		journalizationResource = new JournalizationResource();
		try {
			Field journalizationContext = journalizationResource.getClass()
					.getDeclaredField("context");
			journalizationContext.setAccessible(true);
			journalizationContext.set(journalizationResource, mockContext);			
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	@AfterScenario
	public final void tearDown(){
		Requirements.TearDown();
	}
	@Given("that has no last payout transactions")
	public final void givenLastPayTransactions(){
		dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps", DATABASE.RESTransaction);
		try {
			dbRESTransactionInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/journalization/resource/datasets/TXL_SALES_JOURNAL_EMPTY.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Given("that has last payout transactions")
	public final void givenHasLastPayOutTransactions(){
		dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps2", DATABASE.RESTransaction);
		try {
			dbRESTransactionInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/journalization/resource/datasets/TXL_SALES_JOURNAL_PayOut_1.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Given("that has 2 last payout transactions")
	public final void givenHasMultipleLastPayOutTransactions(){
		dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps2", DATABASE.RESTransaction);
		try {
			dbRESTransactionInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/journalization/resource/datasets/TXL_SALES_JOURNAL_PayOut_2.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	@Given("that has 1 last payin transactions")
	public final void givenHasLastPayInTransactions(){
		dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps2", DATABASE.RESTransaction);
		try {
			dbRESTransactionInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/journalization/resource/datasets/TXL_SALES_JOURNAL_PayIn_1.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}	
	@Given("that has payin and payout transactions")
	public final void givenHasPayInPayOutTransactions(){
		dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps2", DATABASE.RESTransaction);
		try {
			dbRESTransactionInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/journalization/resource/datasets/TXL_SALES_JOURNAL_PayIn_PayOut_1.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@When("getting last payin/payout transactions companyid:$1 storeid:$2 terminalid:$3 businessdate:$4 trainingflag:$5")
	public final void whenGettingLastPayTxs(final String companyId, final String storeId, final String terminalId, final String businessDate, final String trainingFlag){
		payinoutPoslog = journalizationResource.getLastPayTxPoslog(companyId, storeId, terminalId, businessDate, Integer.valueOf(trainingFlag));
	}
	@Then("it should get the following: $expected")
	public final void getTheFollowingExpected(ExamplesTable expectedTable) {
		Assert.assertEquals("Compare NCRWSSResultCode", Integer
				.parseInt(expectedTable.getRow(0).get("NCRWSSResultCode")),
				payinoutPoslog.getNCRWSSResultCode());
		Assert.assertEquals("Compare RetailStoreId", expectedTable.getRow(0)
				.get("RetailStoreId"), payinoutPoslog.getTransaction()
				.getRetailStoreID());
		Assert.assertEquals("Compare WorkstationId", expectedTable.getRow(0)
				.get("WorkstationId"), payinoutPoslog.getTransaction()
				.getWorkStationID().getValue());
		Assert.assertEquals("Compare SequenceNumber", expectedTable.getRow(0)
				.get("SequenceNumber"), payinoutPoslog.getTransaction()
				.getSequenceNo());
		Assert.assertEquals("Compare BusinessDayDate", expectedTable.getRow(0)
				.get("BusinessDayDate"), payinoutPoslog.getTransaction()
				.getBusinessDayDate());
		Assert.assertEquals("Compare TxType",
				expectedTable.getRow(0).get("TxType"), payinoutPoslog
						.getTransaction().getTransactionType());
		Assert.assertEquals("Compare Amount",
				Double.parseDouble(expectedTable.getRow(0).get("Amount")),
				payinoutPoslog.getTransaction().getTenderControlTransaction()
						.getTillSettle().getPayOut().getAmount().getAmount());
	}
	@Then("it should get the NCRWSSResultCode:$expected")
	public final void getTheResult(final int expected) {
		Assert.assertEquals("Compare NCRWSSResultCode", expected, payinoutPoslog.getNCRWSSResultCode());
	}	
}
