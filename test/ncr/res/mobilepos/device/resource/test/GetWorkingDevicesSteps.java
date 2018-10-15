package ncr.res.mobilepos.device.resource.test;

import java.lang.reflect.Field;
import java.util.ListIterator;
import javax.servlet.ServletContext;

import junit.framework.Assert;
import ncr.res.mobilepos.deviceinfo.model.TerminalStatus;
import ncr.res.mobilepos.deviceinfo.model.WorkingDevices;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;

public class GetWorkingDevicesSteps extends Steps {
	private DBInitiator dbRESMasterInitiator = null;
	private DBInitiator dbRESTransactionInitiator = null;
	private WorkingDevices workingDevices = null;
	private DeviceInfoResource deviceInfoResource = null;
	private int groupsCtr = 0;
	@BeforeScenario
	public final void setUp() {
		Requirements.SetUp();
		groupsCtr = 0;
		ServletContext mockContext = Requirements.getMockServletContext();
		deviceInfoResource = new DeviceInfoResource();
		try {
			Field contextField = deviceInfoResource.getClass()
					.getDeclaredField("context");
			contextField.setAccessible(true);
			contextField.set(deviceInfoResource, mockContext);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	@AfterScenario
	public final void tearDown() {
		Requirements.TearDown();
	}
	@Given("that has working devices")
	public final void givenWithOtherWorkingDevices(){
		try {
			dbRESMasterInitiator = new DBInitiator("EndOfDaySteps2", DATABASE.RESMaster);			
			dbRESMasterInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/device/resource/test/MST_BIZDAY.xml");
			dbRESMasterInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/device/resource/test/AUT_DEVICES.xml");
			dbRESMasterInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/device/resource/test/MST_DEVICEINFO.xml");
			dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps", DATABASE.RESTransaction);
			dbRESTransactionInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/device/resource/test/TXU_POS_CTRL.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@Given("that has no other working devices")
	public final void givenNoOtherWorkingDevices(){
		try {
			dbRESMasterInitiator = new DBInitiator("EndOfDaySteps2", DATABASE.RESMaster);			
			dbRESMasterInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/device/resource/test/MST_BIZDAY.xml");
			dbRESMasterInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/device/resource/test/AUT_DEVICES_0004.xml");
			dbRESMasterInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/device/resource/test/MST_DEVICEINFO_0004.xml");
			dbRESTransactionInitiator = new DBInitiator("EndOfDaySteps", DATABASE.RESTransaction);
			dbRESTransactionInitiator.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, "test/ncr/res/mobilepos/device/resource/test/TXU_POS_CTRL_0004.xml");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	@When("getting working devices companyId:$1 storeId:$2 terminalId:$2")
	public final void getWorkingDevices(final String companyId, final String storeId, final String terminalId){		
		workingDevices = (WorkingDevices) deviceInfoResource.getWorkingDevices(companyId, storeId, terminalId);
	}
	@Then("it should get the following terminals: $1")
	public final void getTerminals(ExamplesTable expectedTbl) {
		Assert.assertEquals("Compare size", expectedTbl.getRows().size(),
				workingDevices.getActiveTerminals().size());
		ListIterator<TerminalStatus> actualTerminals = workingDevices
				.getActiveTerminals().listIterator();
		int i = 0;
		while (actualTerminals.hasNext()) {
			TerminalStatus actual = actualTerminals.next();
			Assert.assertEquals("Compare companyid",
					expectedTbl.getRows().get(i).get("companyid"),
					actual.getCompanyId());
			Assert.assertEquals("Compare storeid", expectedTbl.getRows().get(i)
					.get("storeid"), actual.getStoreId());
			Assert.assertEquals("Compare terminalid", expectedTbl.getRows()
					.get(i).get("terminalid"), actual.getTerminalId());
			Assert.assertEquals("Compare tillid", expectedTbl.getRows().get(i)
					.get("tillid"), actual.getTillId());
			Assert.assertEquals("Compare terminalname", expectedTbl.getRows()
					.get(i).get("terminalname"), actual.getTerminalName());
			Assert.assertEquals(
					"Compare openclosestat",
					Short.parseShort(expectedTbl.getRows().get(i)
							.get("openclosestat")), actual.getOpenCloseStat());
			Assert.assertEquals("Compare sodtime", expectedTbl.getRows().get(i)
					.get("sodtime"), String.valueOf(actual.getSodTime()));
			Assert.assertEquals("Compare eodtime", expectedTbl.getRows().get(i)
					.get("eodtime").equalsIgnoreCase("NULL") ? null
					: expectedTbl.getRows().get(i).get("eodtime"), actual
					.getEodTime());
			i++;
		}
	}
	@Then("it should get the following group:$expected")
	public final void getGroup(ExamplesTable expectedTbl){
		Assert.assertEquals("Compare group size", expectedTbl.getRows().size(), workingDevices.getOwnTillGroup().getTerminals().size());
		ListIterator<TerminalStatus> iterate = workingDevices.getOwnTillGroup().getTerminals().listIterator();
		int i = 0;
		while(iterate.hasNext()){
			TerminalStatus actual = iterate.next();
			Assert.assertEquals("Compare companyid",
					expectedTbl.getRows().get(i).get("companyid"),
					actual.getCompanyId());
			Assert.assertEquals("Compare storeid", expectedTbl.getRows().get(i)
					.get("storeid"), actual.getStoreId());
			Assert.assertEquals("Compare terminalid", expectedTbl.getRows()
					.get(i).get("terminalid"), actual.getTerminalId());
			Assert.assertEquals("Compare tillid", expectedTbl.getRows().get(i)
					.get("tillid"), actual.getTillId());
			Assert.assertEquals("Compare terminalname", expectedTbl.getRows()
					.get(i).get("terminalname"), actual.getTerminalName());
			Assert.assertEquals(
					"Compare openclosestat",
					Short.parseShort(expectedTbl.getRows().get(i)
							.get("openclosestat")), actual.getOpenCloseStat());
			Assert.assertEquals("Compare sodtime", expectedTbl.getRows().get(i)
					.get("sodtime"), String.valueOf(actual.getSodTime()));
			Assert.assertEquals("Compare eodtime", expectedTbl.getRows().get(i)
					.get("eodtime").equalsIgnoreCase("NULL") ? null
					: expectedTbl.getRows().get(i).get("eodtime"), actual
					.getEodTime());
			i++;
		}
	}	
	@Then("it should get the following groups of $1: $2")
	public final void getGroups(final String tillId, ExamplesTable expectedTbl) {
		Assert.assertEquals("Compare terminals size", expectedTbl.getRows().size(),
				workingDevices.getTillGroups().get(groupsCtr).getTerminals().size());		
		ListIterator<TerminalStatus> actualTerminals = workingDevices.getTillGroups().get(groupsCtr).getTerminals().listIterator();		
		int i = 0;
		while (actualTerminals.hasNext()) {
			TerminalStatus actual = actualTerminals.next();
			Assert.assertEquals("Compare companyid",
					expectedTbl.getRows().get(i).get("companyid"),
					actual.getCompanyId());
			Assert.assertEquals("Compare storeid", expectedTbl.getRows().get(i)
					.get("storeid"), actual.getStoreId());
			Assert.assertEquals("Compare terminalid", expectedTbl.getRows()
					.get(i).get("terminalid"), actual.getTerminalId());
			Assert.assertEquals("Compare tillid", expectedTbl.getRows().get(i)
					.get("tillid"), actual.getTillId());
			Assert.assertEquals("Compare terminalname", expectedTbl.getRows()
					.get(i).get("terminalname"), actual.getTerminalName());
			Assert.assertEquals(
					"Compare openclosestat",
					Short.parseShort(expectedTbl.getRows().get(i)
							.get("openclosestat")), actual.getOpenCloseStat());
			Assert.assertEquals("Compare sodtime", expectedTbl.getRows().get(i)
					.get("sodtime"), String.valueOf(actual.getSodTime()));
			Assert.assertEquals("Compare eodtime", expectedTbl.getRows().get(i)
					.get("eodtime").equalsIgnoreCase("NULL") ? null
					: expectedTbl.getRows().get(i).get("eodtime"), actual
					.getEodTime());
			i++;
		}
		groupsCtr++;
	}
}
