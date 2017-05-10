package ncr.res.mobilepos.poslogstatus.test;

import java.lang.reflect.Field;

import javax.servlet.ServletContext;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import junit.framework.Assert;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.poslogstatus.model.PoslogStatusInfo;
import ncr.res.mobilepos.poslogstatus.resource.PoslogStatusResource;

public class PoslogStatusTestSteps extends Steps {

	private PoslogStatusResource poslogStatusResource = null;
	private DBInitiator dbInit = null;
	private PoslogStatusInfo poslogStatusInfo = null;

	@BeforeScenario
	public final void setUpClass() {
		Requirements.SetUp();
		initResources();
		dbInit = new DBInitiator("PoslogStatusTestSteps", DATABASE.RESTransaction);
	}

	@AfterScenario
	public final void tearDownClass() {
		Requirements.TearDown();
	}

	private void initResources() {
		ServletContext context = Requirements.getMockServletContext();
		try {
			poslogStatusResource = new PoslogStatusResource();
			Field contextField = poslogStatusResource.getClass().getDeclaredField("context");
			contextField.setAccessible(true);
			contextField.set(poslogStatusResource, context);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Cant load Mock Servlet Context.");
		}
	}
	
	@Given("a loaded dataset $dataset")
	public final void initdatasetsDpt(final String dataset) {
		try {
			dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/poslogstatus/test/" + dataset + ".xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@When("a check status with consolidation $consolidation transfer $transfer")
	public void iEndTheTransaction(final boolean consolidation, final boolean transfer) {
		poslogStatusInfo = poslogStatusResource.checkResultCount(consolidation,transfer);
	}
	
	@Then("a consolidationResult is $consolidationResult")
	public final void theConsolidationResultIs(final int consolidationResult) {
		Assert.assertEquals("Expect the  consolidationResult code", consolidationResult,
				poslogStatusInfo.getConsolidationResult());
	}
	
	@Then("a transferResult is $transferResult")
	public final void theTransferResultIs(final int transferResult) {
		Assert.assertEquals("Expect the  transferResult code", transferResult,
				poslogStatusInfo.getTransferResult());
	}
	
	@Then("a resultCode is $resultCode")
	public final void theresultCodeIs(int resultCode) {
		Assert.assertEquals("Expect the  resultCode code", resultCode,
				poslogStatusInfo.getNCRWSSResultCode());
	}
	
	@Then("a resultExtendedCode is $resultExtendedCode")
	public final void theresultExtendedCodeIs(int resultExtendedCode) {
		Assert.assertEquals("Expect the  resultExtendedCode code", resultExtendedCode,
				poslogStatusInfo.getNCRWSSExtendedResultCode());
	}

}
