package ncr.res.mobilepos.forwardItem.resource.test;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;
import org.mockito.Mock;

import java.lang.reflect.Field;
import javax.servlet.ServletContext;

import junit.framework.Assert;
import ncr.res.mobilepos.credential.model.Operator;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.forwarditemlist.resource.ForwardItemListResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
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

	/**
	 * Method to test the database connection.
	 */
	@BeforeScenario
	public final void setUpClass() {
		Requirements.SetUp();
		dbInit = new DBInitiator("ForwardItemSteps", DATABASE.RESMaster);
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

	@Given("entries for $operator in database")
	public final void initOperator(final String operator) {
		try {
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/forwardItem/resource/test/" + operator + ".xml");
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
}