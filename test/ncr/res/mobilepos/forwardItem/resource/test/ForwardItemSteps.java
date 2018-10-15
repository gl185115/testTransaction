package ncr.res.mobilepos.forwardItem.resource.test;

import java.lang.reflect.Field;

import javax.servlet.ServletContext;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;
import org.mockito.Mock;

import junit.framework.Assert;
import ncr.res.mobilepos.credential.model.Operator;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.forwarditemlist.resource.ForwardItemListResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.journalization.model.SearchForwardPosLog;
import ncr.res.mobilepos.model.ResultBase;

public class ForwardItemSteps extends Steps {
	@Mock
	private DAOFactory daoFactory;
	/**
	 * Holds the DBInitiator object.
	 */
	private DBInitiator dbInit;

	private ForwardItemListResource forwardItemResource;
	private Operator operatorModel = null;
	private ResultBase resultBase = null;
	private SearchForwardPosLog poslog = null;

	/**
	 * Method to test the database connection.
	 */
	@BeforeScenario
	public final void setUpClass() {
		Requirements.SetUp();
		ServletContext mockContext = Requirements.getMockServletContext();
		forwardItemResource = new ForwardItemListResource();
		try {
			Field forwardItemContext = forwardItemResource.getClass()
					.getDeclaredField("context");
			forwardItemContext.setAccessible(true);
			forwardItemContext.set(forwardItemResource, mockContext);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@AfterScenario
	public final void tearDownClass() {
		Requirements.TearDown();
	}

	@Given("entries for $forwarditem in $database")
	public final void initDatabase(final String forwarditem,final String database) {
		dbInit = new DBInitiator("ForwardItemSteps", "RESMaster".equals(database)?DATABASE.RESMaster:DATABASE.RESTransaction);
		try {
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/forwardItem/resource/test/" + forwarditem + ".xml");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}

	@When("I get operator with companyid $companyid storeid $storeid workstationid $workstationid and operatorid $operatorid")
	public final void getUserPermission(final String companyid, final String storeid, final String workstationid,
			final String operatorid) {
		operatorModel = new Operator();
		operatorModel = forwardItemResource.UserPermission(companyid, storeid, workstationid, operatorid);
	}

	@Then("I should get the OperatorResult : $examplesResult")
	public final void operatorResult(final ExamplesTable examplesResult) {
		Assert.assertEquals("Expect the  result code", 0, operatorModel.getNCRWSSResultCode());
		Assert.assertEquals("Expect the  result message", ResultBase.RES_SUCCESS_MSG, operatorModel.getMessage());
		Assert.assertEquals("Expect the OperatorNo", examplesResult.getRow(0).get("OperatorNo"),
				String.valueOf(operatorModel.getOperatorNo()));
		Assert.assertEquals("Expect the OperatorType", examplesResult.getRow(0).get("OperatorType"),
				String.valueOf(operatorModel.getOperatorType()));
		Assert.assertEquals("Expect the Securitylevel", examplesResult.getRow(0).get("Securitylevel"),
				String.valueOf(operatorModel.getSecuritylevel()));
	}

	@When("I get ResultBase with companyid $companyid retailstoreid $retailstoreid queue $queue workstationid $workstationid trainingmode $trainingmode tag $tag total $total and poslogxml $poslogxml")
	public final void getForwardPosLogWithTagInsertResult(final String companyid, final String retailstoreid, final String queue,
			final String workstationid, final String trainingmode, final String tag, final String total, final String poslogxml) {
		resultBase= new ResultBase();
		resultBase = forwardItemResource.saveForwardPosLogIncludeTag(companyid, retailstoreid, queue, workstationid, trainingmode
				, tag, total, poslogxml);
	}

	@Then("I should get the ResultCode : $examplesResult")
	public final void ForwardPosLogWithTagInsertResult(final ExamplesTable examplesResult) {
		Assert.assertEquals("Expect the ResultCode", examplesResult.getRow(0).get("ResultCode"),
				String.valueOf(resultBase.getNCRWSSResultCode()));
	}

	@When("I get forwardItem with companyId $companyId retailStoreId $retailStoreId queue $queue businessDayDate $businessDayDate tag $tag and trainingFlag $trainingFlag")
	public final void getForwardItemsWithTagSelectResult(final String companyId, final String retailStoreId, final String queue,
			final String businessDayDate, final String tag, final String trainingFlag) {
		poslog = new SearchForwardPosLog();
		poslog = forwardItemResource.getForwardItemsWithTag(companyId, retailStoreId, queue, businessDayDate, tag, trainingFlag);
	}

	@Then("I should get the forwardItemSuccessResult : $examplesResultWithTag")
	public final void ForwardItemsWithTagSelectResultSuccess(final ExamplesTable examplesResultWithTag) {
		Assert.assertEquals("Expect the result Status", examplesResultWithTag.getRow(0).get("Status"), String.valueOf(poslog.getStatus()));
		Assert.assertEquals("Expect the result NCRWSSResultCode", examplesResultWithTag.getRow(0).get("NCRWSSResultCode"), String.valueOf(poslog.getNCRWSSResultCode()));
		Assert.assertEquals("Expect the result NCRWSSExtendedResultCode", examplesResultWithTag.getRow(0).get("NCRWSSExtendedResultCode"), String.valueOf(poslog.getNCRWSSExtendedResultCode()));
		Assert.assertEquals("Expect the result Tx", examplesResultWithTag.getRow(0).get("Tx"), poslog.getPosLogXml());
	}

	@Then("I should get the forwardItemErrorResult : $examplesResultWithTag")
	public final void ForwardItemsWithTagSelectResultError(final ExamplesTable examplesResultWithTag) {
		Assert.assertEquals("Expect the result NCRWSSResultCode", examplesResultWithTag.getRow(0).get("NCRWSSResultCode"), String.valueOf(poslog.getNCRWSSResultCode()));
	}
}