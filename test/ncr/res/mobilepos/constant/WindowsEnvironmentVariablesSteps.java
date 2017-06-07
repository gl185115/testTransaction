package ncr.res.mobilepos.constant;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import org.junit.Assert;
import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;

import javax.naming.NamingException;

import ncr.res.mobilepos.helper.StringUtility;

public class WindowsEnvironmentVariablesSteps extends Steps {
	Exception thrownException = null;

	@BeforeScenario
	public void setUp() throws NamingException {
		PowerMockito.mockStatic(System.class);
	}

	@AfterScenario
	public void tearDown() throws NamingException {
	}

	@Given("System Variables has key $key, value $inValue")
	public void setEnvironemtVariables(String key, String inValue) {
		String value = StringUtility.convNullStringToNull(inValue);
		PowerMockito.when(System.getenv(key)).thenReturn(value);
	}

	@When("Initialization is triggered")
	public void trigerInit() throws NamingException, IOException {
		try {
			WindowsEnvironmentVariables.initInstance();
		} catch (NamingException nme) {
			thrownException = nme;
		}
	}

	@Then("NamingException should be thrown")
	public void expectException() {
		Assert.assertNotNull(thrownException);
	}

	@Then("No NamingException should be thrown")
	public void expectNoException() {
		Assert.assertNull(thrownException);
	}

	@Then("Instance should be created")
	public void expectInstance() {
		WindowsEnvironmentVariables instance = WindowsEnvironmentVariables.getInstance();
		Assert.assertNotNull(instance);
	}

	@Then("Instance should not be created")
	public void expectNoInstance() {
		WindowsEnvironmentVariables instance = WindowsEnvironmentVariables.getInstance();
		Assert.assertNull(instance);
	}

	@Then("getSystemPath returns $expected")
	public void checkGetSystem(String expected) {
		Assert.assertEquals(expected, WindowsEnvironmentVariables.getInstance().getSystemPath());
	}

	@Then("server type is $expected")
	public void checkServerType(String expected) {
		if(ServerTypes.ENTERPRISE.equalsIgnoreCase(expected)) {
			Assert.assertTrue(WindowsEnvironmentVariables.getInstance().isServerTypeEnterprise());
		} else {
			Assert.assertFalse(WindowsEnvironmentVariables.getInstance().isServerTypeEnterprise());
		}
	}

}