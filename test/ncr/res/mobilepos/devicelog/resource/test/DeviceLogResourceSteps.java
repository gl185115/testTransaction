/**
 * 
 */
package ncr.res.mobilepos.devicelog.resource.test;

import static org.junit.Assert.*;

import java.io.IOException;

import ncr.res.mobilepos.devicelog.DeviceLogTestHelper;
import ncr.res.mobilepos.devicelog.model.DeviceLog;
import ncr.res.mobilepos.devicelog.resource.DeviceLogResource;
import ncr.res.mobilepos.helper.Requirements;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Named;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import org.springframework.mock.web.MockHttpServletRequest;




/**
 * @author Developer
 *
 */
public class DeviceLogResourceSteps extends Steps {
    private String udid            = null;
    private String logDate        = null;
    private String rowId        = null;
    private byte[] logFile         = null;
    private DeviceLog devLogRes = null;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        this.udid        = null;
        this.logDate    = null;
        this.rowId        = null;
        this.logFile    = null;
        this.devLogRes    = null;
        DeviceLogTestHelper.loadCleanTable();
    }
    
    @AfterScenario
    public final void TearDown(){
        this.udid        = null;
        this.logDate    = null;
        this.rowId        = null;
        this.logFile    = null;
        this.devLogRes    = null;
        
        Requirements.TearDown();
    }
    
    @Given("a universal device id '<udid>'")
    public final void aUdid(@Named("udid") final String expectedudid){
        this.udid = (expectedudid.isEmpty()
                || expectedudid.equals("null")) ? null : expectedudid;
    }
    
    @Given("a log date '<logDate>'")
    public final void aLogDate(@Named("logDate") final String logDateToSet){
        this.logDate = (logDateToSet.isEmpty()
                || logDateToSet.equals("null")) ? null : logDateToSet;
    }
    
    @Given("I received a row ID '<givenRowId>'")
    public final void aRowId(@Named("givenRowId") final String givenRowId){
        DeviceLogTestHelper.loadTableWithData();
        this.rowId = (givenRowId.isEmpty()
                || givenRowId.equals("null")) ? null : givenRowId;
    }
    
    @Given("a log file '<logFileName>'")
    public final void alogFile(@Named("logFileName") final String logFileName)
    throws IOException{
        this.logFile = DeviceLogTestHelper.createLogFileByteArray(logFileName);
    }
    
    @When("I request for upload with file field '<fileField>'")
    public final void requestLogUpload(
            @Named("fileField") final String fileField){
        MockHttpServletRequest f = new MockHttpServletRequest();
        if(null != fileField && !fileField.isEmpty()){
            DeviceLogTestHelper.createUploadFormRequest(
                    f, fileField, this.logFile);
        }
        DeviceLogResource r = new DeviceLogResource();
        this.devLogRes = r.upload(f, this.udid, this.logDate, this.rowId);
    }
    
    @When("I request for download")
    public final void logDownloadReques(){
        fail("NOT YET IMPLEMENTED");
    }    
    
    @Then("the result should be <uploadResult>")
    public final void resultSatusShouldBe(
            @Named("uploadResult") final String uploadResult){
        if (uploadResult.equalsIgnoreCase("successful")){
            assertTrue(0 == this.devLogRes.getNCRWSSResultCode());
        }else if (uploadResult.equalsIgnoreCase("failed")){
            assertFalse(0 == this.devLogRes.getNCRWSSResultCode());
        }else{
            assertTrue(Integer.parseInt(uploadResult) ==
                this.devLogRes.getNCRWSSResultCode());
        }
    }
    
    @Then("rowId should be set to <rowId>")
    public final void rowIdShouldBe(@Named("rowId") final String expectedrowId){
        if (expectedrowId.equalsIgnoreCase("not empty")){
            assertFalse(this.devLogRes.getRowId().isEmpty());
        }else if (expectedrowId.equalsIgnoreCase("empty")){
            assertTrue((null == this.devLogRes.getRowId())
                    || this.devLogRes.getRowId().isEmpty());
        }else{
            assertTrue(this.devLogRes.getRowId()
                    .equalsIgnoreCase(expectedrowId));
        }
    }
    
}
