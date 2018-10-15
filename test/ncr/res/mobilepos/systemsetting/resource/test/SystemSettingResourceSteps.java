package ncr.res.mobilepos.systemsetting.resource.test;

import org.hamcrest.Matchers;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.steps.Steps;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;

import java.io.IOException;
import java.lang.reflect.Field;
import java.net.InetAddress;
import java.net.UnknownHostException;

import javax.naming.NamingException;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.systemsetting.resource.SystemSettingResource;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class SystemSettingResourceSteps extends Steps {
	private DBInitiator dbInitRESMaster = null;
	private SystemSettingResource systemSettingResource = null;
	private ResultBase resultBase = null;
	private InetAddress mockResolvableSite;


	@BeforeScenario
	public void setUp() throws NamingException {
		Requirements.SetUp();
		Requirements.getMockServletContext();
		systemSettingResource = new SystemSettingResource();
		PowerMockito.mockStatic(InetAddress.class);
	}

	@AfterScenario
	public void tearDown() throws NamingException {
	}

	@Given("ipAddress, $ipAddress is invalid and cannot be resolved")
	public void mockGetByNameAsUnknownHostException(String ipAddress) throws UnknownHostException {
		String value = StringUtility.convNullStringToNull(ipAddress);
		PowerMockito.when(InetAddress.getByName(ipAddress)).thenThrow(new UnknownHostException());
	}

	@Given("ipAddress, $ipAddress is valid and resolvable")
	public void mockResolvableSite(String ipAddress) throws UnknownHostException {
		String value = StringUtility.convNullStringToNull(ipAddress);
		mockResolvableSite = mock(InetAddress.class);
		when(InetAddress.getByName(ipAddress)).thenReturn(mockResolvableSite);
	}

	@Given("AppServer can be reachable within $distance ms")
	public void mockReachableSite(int distance) throws IOException {
		// The Server is expected to reach in 'distance' seconds.
		// If timeout is large enough to reach the server, returns successful.
		when(mockResolvableSite.isReachable(Mockito.intThat(Matchers.greaterThanOrEqualTo(distance)))).thenReturn(true);
		// If timeout is less than the 'distance', returns fail.
		when(mockResolvableSite.isReachable(Mockito.intThat(Matchers.lessThan(distance)))).thenReturn(false);
	}

	@Given("Timeout is defined as $timeout ms")
	public void setServerTimeout(int timeout) throws NoSuchFieldException, IllegalAccessException {
		Field fieldContext = GlobalConstant.class.getDeclaredField("serverPingTimeout");
		fieldContext.setAccessible(true);
		fieldContext.set(null, timeout);
	}

	@Given("Network has trouble")
	public void mockNetworkTrouble() throws IOException {
		when(mockResolvableSite.isReachable(anyInt())).thenThrow(new IOException());
	}

	@Given("System has trouble")
	public void mockSystemTrouble() throws Exception {
		when(mockResolvableSite.isReachable(anyInt())).thenThrow(new NullPointerException());
	}


	@When("I make a ping to ipAddress, $ipAddress")
	public void makePingRequest(String ipAddress) {
		resultBase = systemSettingResource.ping(ipAddress);
	}

	@Then("Ping returns sucessfully")
	public void expectSuccessfulPing() {
		assertEquals(ResultBase.RES_OK, resultBase.getNCRWSSResultCode());
	}

	@Then("Ping failed with RES_ERROR_PING")
	public void expectFailedPingWithUnknownHostException() {
		assertEquals(ResultBase.RES_ERROR_PING, resultBase.getNCRWSSResultCode());
		assertTrue(resultBase.getMessage().contains("ping failed"));
	}

	@Then("Ping failed by timeout with RES_ERROR_PING")
	public void expectFailedPingWithTimeout() {
		assertEquals(ResultBase.RES_ERROR_PING, resultBase.getNCRWSSResultCode());
		assertTrue(resultBase.getMessage().contains("is not reachable"));
	}

	@Then("Ping failed with RES_ERROR_IOEXCEPTION")
	public void expectFailedPingWithIOException() {
		assertEquals(ResultBase.RES_ERROR_IOEXCEPTION, resultBase.getNCRWSSResultCode());
	}

	@Then("Ping failed with RES_ERROR_GENERAL")
	public void expectFailedPingWithException() {
		assertEquals(ResultBase.RES_ERROR_GENERAL, resultBase.getNCRWSSResultCode());
	}

}