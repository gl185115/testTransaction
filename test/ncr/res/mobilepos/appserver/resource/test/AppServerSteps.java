package ncr.res.mobilepos.appserver.resource.test;

import junit.framework.Assert;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;

import java.util.Map;

import ncr.res.mobilepos.appserver.model.AppServers;
import ncr.res.mobilepos.appserver.resource.AppServerResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;

public class AppServerSteps extends Steps {

	private DBInitiator dbInitRESMaster = null;
	private AppServerResource appServerResource = null;
	private AppServers appServers = null;
	
	@BeforeScenario
	public final void SetUpClass() {
		Requirements.SetUp();
		Requirements.getMockServletContext();
		dbInitRESMaster = new DBInitiator("AppServerSteps", DATABASE.RESMaster);
		appServerResource = new AppServerResource();
	}

	@AfterScenario
	public final void TearDownClass() {
		Requirements.TearDown();
	}
	
	@Given("an empty table in PRM_SERVER_TABLE")
	public final void initServerTbl() {
		try {
			dbInitRESMaster
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/appserver/resource/test/prm_server_tbl_(nodata).xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Given("data in table PRM_SERVER_TABLE")
	public final void initServerTblWithData() {
		try {
			dbInitRESMaster
					.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
							"test/ncr/res/mobilepos/appserver/resource/test/prm_server_tbl_(withdata).xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@When("I get app servers")
	public final void getAppServers() {
		appServers = appServerResource.getAppServers();
	}
	
	@Then("I should get NCRWSSResultCode:$expectedCode")
	public final void thenIShouldHave(String expectedCode) {
		Assert.assertEquals("Compare NCRWSSResultCode", expectedCode, String.valueOf(appServers.getNCRWSSResultCode()));
	}
	@Then("I should get Servers=null")
	public final void thenIShouldHaveNullServers(){
		Assert.assertNull(appServers.getAppServers());
	}
	@Then("I should get the following: $expectedData")
	public final void thenIShouldGetData(ExamplesTable expectedData) {
		int i = 0;
		for (Map<String, String> data : expectedData.getRows()) {
			Assert.assertEquals("Compare Name " + i,
					data.get("Name").trim(), 
							appServers.getAppServers().get(i).getName());
			Assert.assertEquals("Compare Type " + i,
					data.get("Type").trim(), 
							appServers.getAppServers().get(i).getType());
			if(null != appServers.getAppServers().get(i).getUrl()) {
				Assert.assertEquals("Compare Url " + i,
						data.get("Url").trim(), 
								appServers.getAppServers().get(i).getUrl());					
			}
			if (null != appServers.getAppServers().get(i).getIisUrl()) {
				Assert.assertEquals("Compare IISUrl " + i, data.get("IISUrl")
						.trim(), appServers.getAppServers().get(i).getIisUrl());
			}
			i++;
		}
	}

}
