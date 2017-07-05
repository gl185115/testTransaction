package ncr.res.mobilepos.posmailinfo.resource.test;

import junit.framework.Assert;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.posmailinfo.resource.POSMailInfoResource;
import ncr.res.mobilepos.webserviceif.model.JSONData;

public class ListPOSMailInfoSteps extends Steps {

	private DBInitiator dbInitRESMaster = null;
	private POSMailInfoResource posMailInfoResource = null;
	private JSONData jsonData = null;
	
	@BeforeScenario
	public final void SetUpClass() {
		Requirements.SetUp();
		Requirements.getMockServletContext();
		dbInitRESMaster = new DBInitiator("ListPOSMailInfoSteps", DATABASE.RESMaster);
		posMailInfoResource = new POSMailInfoResource();
	}

	@AfterScenario
	public final void TearDownClass() {
		Requirements.TearDown();
	}
	
	@Given("an empty table in MST_POSMAIL_INFO")
	public final void initPOSMailInfoEmpty() {
		try {
			dbInitRESMaster
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/posmailinfo/resource/test/mst_posmail_info(nodata).xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Given("an empty table in MST_POSMAIL_STORE")
	public final void initPOSMailStoreEmpty() {
		try {
			dbInitRESMaster
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/posmailinfo/resource/test/mst_posmail_store(nodata).xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Given("data in table MST_POSMAIL_INFO")
	public final void initPOSMailInfo() {
		try {
			dbInitRESMaster
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/posmailinfo/resource/test/mst_posmail_info.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Given("data in table MST_POSMAIL_STORE")
	public final void initPOSMailStore() {
		try {
			dbInitRESMaster
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/posmailinfo/resource/test/mst_posmail_store.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	@When("I get list of posmail messages with companyid:$1, storeid:$2, terminalid:$3, businessdate:$4")
	public final void getPOSMailInfos(final String companyId, final String retailStoreId, final String workstationId, final String businessDate) {
		jsonData = posMailInfoResource.getPOSMailInfo(companyId, retailStoreId, workstationId, businessDate);
	}
	
	@Then("I should get NCRWSSResultCode:$expectedCode")
	public final void thenIShouldHave(String expectedCode) {
		Assert.assertEquals("Compare NCRWSSResultCode", expectedCode, String.valueOf(jsonData.getNCRWSSResultCode()));
	}
	@Then("I should get InfoData(JSON String): $1")
	public final void thenIShouldHaveInfoDataJSONString(final String expected){
		Assert.assertEquals("Compare InfoData", expected, jsonData.getInfoData());
	}
}
