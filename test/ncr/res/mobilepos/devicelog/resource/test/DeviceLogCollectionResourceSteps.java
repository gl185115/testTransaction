package ncr.res.mobilepos.devicelog.resource.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;

import junit.framework.Assert;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.devicelog.DeviceLogTestHelper;
import ncr.res.mobilepos.devicelog.resource.DeviceLogResource;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.model.ResultBase;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Named;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import org.springframework.mock.web.MockHttpServletRequest;

public class DeviceLogCollectionResourceSteps extends Steps {

    private String storeid = null;
    private String termid = null;
    private String logdate = null;
    private String seqnum = null;
    private byte[] logFile = null;
    private ResultBase result = null;
    private String fileName = null;

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        this.storeid = null;
        this.termid = null;
        this.logdate = null;
        this.seqnum = null;
        this.logFile = null;
        this.result = null;
        this.fileName = null;
        GlobalConstant.setCorpid("000001");
    }

    @AfterScenario
    public final void TearDown(){
        this.storeid = null;
        this.termid = null;
        this.logdate = null;
        this.seqnum = null;
        this.logFile = null;
        this.result = null;
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

    @Given("a log date '<logDate>'")
    public final void aLogDate(@Named("logDate") final String logDateToSet) {
        this.logdate = (logDateToSet.isEmpty()
                || logDateToSet.equals("null")) ? null : logDateToSet;
    }

    @Given("a sequence number '<seqnum>'")
    public final void aSeqnum(@Named("seqnum") final String expectedseqnum) {
        this.seqnum = (expectedseqnum.isEmpty()
                || expectedseqnum.equals("null")) ? null : expectedseqnum;
    }

    @Given("a log file '<logFileName>'")
    public final void alogFile(@Named("logFileName") final String logFileName)
    throws IOException {
        this.logFile = DeviceLogTestHelper.createLogFileByteArray(logFileName);
    }

    @Given("a name of the file stored in local '<filename>'")
    public final void aFilename(@Named("filename") final String expectedname) {
        this.fileName = (expectedname.isEmpty() || expectedname.equals("null"))
                ? null : expectedname;
    }

    @When("I request for upload with file field '<fileField>'")
    public final void requestLogUpload(
            @Named("fileField") final String fileField) {
        MockHttpServletRequest f = new MockHttpServletRequest();
        if (null != fileField && !fileField.isEmpty()) {
            DeviceLogTestHelper.createUploadFormRequest(
                    f, fileField, this.logFile);
        }
        DeviceLogResource r = new DeviceLogResource();
        this.result = r.uploadFile(f, this.storeid, this.termid,
                this.logdate, this.seqnum);
    }

    @Then("the result should be <uploadResult>")
    public final void resultSatusShouldBe(
            @Named("uploadResult") final String uploadResult) {
        if (uploadResult.equalsIgnoreCase("successful")) {
            if (DeviceLogTestHelper.deleteLogFiles(this.fileName)) {
                assertTrue(0 == this.result.getNCRWSSResultCode());
            } else {
                Assert.fail("Failed because the file is not existing or failed "
                        + "to delete.");
            }
        } else if (uploadResult.equalsIgnoreCase("failed")) {
            assertFalse(0 == this.result.getNCRWSSResultCode());
        } else {
            assertTrue(Integer.parseInt(uploadResult)
                == this.result.getNCRWSSResultCode());
        }
    }
}
