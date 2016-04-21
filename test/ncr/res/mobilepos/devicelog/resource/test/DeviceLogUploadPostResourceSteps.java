package ncr.res.mobilepos.devicelog.resource.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.ZoneId;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Named;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import org.springframework.mock.web.MockHttpServletRequest;

import ncr.res.mobilepos.devicelog.DeviceLogTestHelper;
import ncr.res.mobilepos.devicelog.resource.DeviceLogResource;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.model.ResultBase;

public class DeviceLogUploadPostResourceSteps extends Steps {

    private String storeid = null;
    private String termid = null;
    private String data = null;
    private Clock time = null;
    private String fileName = null;
    private ResultBase result = null;

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        this.storeid = null;
        this.termid = null;
        this.data = null;
        this.result = null;
        this.time = null;
        this.fileName = null;
    }

    @AfterScenario
    public final void TearDown(){
        this.storeid = null;
        this.termid = null;
        this.data = null;
        this.result = null;
        this.time = null;
        this.fileName = null;
        Requirements.TearDown();
    }

    @Given("a store id '<storeid>'")
    public final void aStoreid(@Named("storeid") final String expectedstoreid) {
        this.storeid = (expectedstoreid.isEmpty()
                || expectedstoreid.equals("null")) ? null : expectedstoreid;
    }

    @Given("a terminal id '<termid>'")
    public final void aTermid(@Named("termid") final String expectedtermid) {
        this.termid = (expectedtermid.isEmpty()
                || expectedtermid.equals("null")) ? null : expectedtermid;
    }

    @Given("a content of log '<data>'")
    public final void aData(@Named("data") final String dataToSet) {
        this.data = (dataToSet.isEmpty()
                || dataToSet.equals("null")) ? null : dataToSet;
    }

    @Given("a time to trigger the method '<time>'")
    public final void aClock(@Named("time") final String timeToSet)
    throws IOException {
	if( timeToSet.isEmpty() || timeToSet.equals("null")) {
	    this.time = null;
	} else {
	    LocalDateTime date = LocalDateTime.parse(
		    timeToSet, DeviceLogResource.MOBILE_DEVICE_LOG_DATEFORMAT);
	    ZoneId defaultZoneId = ZoneId.systemDefault();
	    this.time = Clock.fixed(date.atZone(defaultZoneId).toInstant(), defaultZoneId);	    
	}
    }

    @Given("a name of the file stored in local '<filename>'")
    public final void aFilename(@Named("filename") final String expectedname) {
        this.fileName = (expectedname.isEmpty() || expectedname.equals("null"))
                ? null : expectedname;
    }

    @When("I request for upload")
    public final void requestLogUpload() {
        MockHttpServletRequest f = new MockHttpServletRequest();
        // Sets fixed clock to the constructor. it always returns fixed time.
        DeviceLogResource r = new DeviceLogResource(this.time);
        this.result = r.uploadLog(f, this.storeid, this.termid, this.data);
    }

    @Then("the result should be '<uploadResult>'")
    public final void resultStatusShouldBe(
            @Named("uploadResult") final String uploadResult) {
        // If test case is successful, it should have 0 result code.
	if (uploadResult.equalsIgnoreCase("successful")) {
	    // It should successfully create file.
	    if (DeviceLogTestHelper.deleteLogFiles(this.fileName)) {
		// Code 0 for successful, is expected.
		assertTrue(0 == this.result.getNCRWSSResultCode());
            } else {
        	// No file for successful result, is not expected.
                fail("Failed because the file is not existing or failed "
                        + "to delete.");
            }
        // If test case is failed, it should have non-zero result code.
        } else if (uploadResult.equalsIgnoreCase("failed")) {
            // Code 0 for failed case, is not expected.
            assertFalse(0 == this.result.getNCRWSSResultCode());
        } else {
            // Code non-zero for failed case, is expected.
            assertTrue(Integer.parseInt(uploadResult)
                == this.result.getNCRWSSResultCode());
        }
    }
}
