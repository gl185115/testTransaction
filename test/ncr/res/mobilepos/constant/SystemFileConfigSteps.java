package ncr.res.mobilepos.constant;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;

import java.io.IOException;

import ncr.res.mobilepos.helper.Requirements;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

public class SystemFileConfigSteps extends Steps {
	private String systemPath;
	private SystemFileConfig systemFileConfig;
	private Exception thrownException;

	@BeforeScenario
    public final void setUpClass() {
		systemPath = null;
		systemFileConfig = null;
		thrownException = null;
    }

    @AfterScenario
    public final void tearDownClass() {
        Requirements.TearDown();
    }

	@Given("the system path is $systemPath")
	public final void setSystemPath(String systemPath) throws Exception {
		this.systemPath = systemPath;
	}

	@When("SystemFileConfig is instantiated")
	public final void initSystemFileConfig() {
		try {
			systemFileConfig = SystemFileConfig.initInstance(this.systemPath);
		} catch (Exception ex) {
			thrownException = ex;
		}
	}

	@Then("the result should be CompanyID:$companyId and StoreID:$storeId")
	public void checkIDs(String companyId, String storeId) {
		assertEquals(companyId, systemFileConfig.getCompanyId());
		assertEquals(storeId, systemFileConfig.getStoreId());
	}

	@Then("it sucessfully instantiates")
	public final void checkInstance() {
		assertNotNull(systemFileConfig);
	}

	@Then("it fails to instantiate")
	public final void checkNullInstance() {
		assertNull(systemFileConfig);
	}

	@Then("no Exception is thrown")
	public final void checkNoException() {
		assertNull(thrownException);
	}

	@Then("IOException is thrown")
	public final void checkIOException() {
		assertNotNull(thrownException);
		assertTrue(thrownException instanceof IOException);
	}

}