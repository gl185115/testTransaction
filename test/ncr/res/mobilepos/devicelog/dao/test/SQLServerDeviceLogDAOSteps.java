package ncr.res.mobilepos.devicelog.dao.test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ncr.res.mobilepos.devicelog.DeviceLogTestHelper;
import ncr.res.mobilepos.devicelog.model.DeviceLog;
import ncr.res.mobilepos.devicelog.dao.SQLServerDeviceLogDAO;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.Requirements;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Named;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class SQLServerDeviceLogDAOSteps extends Steps {
    
    private DeviceLog     deviceLog    = null;
    private FileItem     fileItem    = null;
    private String         udid        = null;
    private Date         logDate        = null;
    private String        rowId        = null;
    private boolean        readResult     = false;
    private DeviceLog[] logs         = null;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        this.deviceLog    = null;
        this.fileItem    = null;
        this.udid        = null;
        this.logDate    = null;
        this.rowId        = null;
        this.readResult    = false;
        this.logs        = null;
        DeviceLogTestHelper.loadCleanTable();
    }
    
    @AfterScenario
    public final void TearDown(){
        this.readResult = false;
        this.logs        = null;
        this.deviceLog    = null;
        this.fileItem    = null;
        this.udid        = null;
        this.logDate    = null;
        this.rowId        = null;    
        Requirements.TearDown();
    }
    
    @Given("I received a row ID '<givenRowId>'")
    public final void aRowId(@Named("givenRowId") final String givenRowId){
        DeviceLogTestHelper.loadTableWithData();
        this.rowId = (givenRowId.isEmpty() ||
                givenRowId.equals("null")) ? null : givenRowId;
    }
    
    @Given("I have a file item '<givenFileItem>'")
    public final void givenFileItem(
            @Named("givenFileItem") final String givenFileItem)
    throws IOException, FileUploadException{
        if (null == givenFileItem || givenFileItem.equals("null")){
            this.fileItem = null;
        } else {
            this.fileItem = DeviceLogTestHelper.createFileItem(givenFileItem);
        }
    }
    
    @Given("a universal device ID '<givenUdid>'")
    public final void givenUdid(@Named("givenUdid") final String givenUdid){
        this.udid = (givenUdid.isEmpty()
                || givenUdid.equals("null")) ? null : givenUdid;
    }
    
    @Given("a log date '<givenLogDate>'")
    public final void givenLogDate(
            @Named("givenLogDate") final String givenLogDate)
    throws ParseException{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (null == givenLogDate || givenLogDate.isEmpty()
                || givenLogDate.equals("null")){
            this.logDate = null;
        } else {
            this.logDate = df.parse(givenLogDate);
        }
    }
    
    @When("I save to database")
    public final void saveToDatabase() throws DaoException{
        SQLServerDeviceLogDAO dao = new SQLServerDeviceLogDAO();
        this.deviceLog =
            dao.saveLogFile(this.udid, this.logDate, this.fileItem);
    }
    
    @When("I update the database")
    public final void updateFileItem() throws DaoException{
        SQLServerDeviceLogDAO dao = new SQLServerDeviceLogDAO();
        try {
            this.deviceLog = dao.updateLogFile(this.rowId, this.fileItem);
        } catch (DaoException e) {
            this.deviceLog = new DeviceLog();
            this.deviceLog.setNCRWSSResultCode(100);
        }
    }
    
    @When("I query for device log information")
    public final void queryDeviceLogInfo() throws DaoException{
        SQLServerDeviceLogDAO dao = new SQLServerDeviceLogDAO();
        try {
            this.deviceLog = dao.getDeviceLogInfo(this.rowId);
        } catch (DaoException e) {
            this.deviceLog = new DeviceLog();
            this.deviceLog.setNCRWSSResultCode(100);
        }
    }
    
    @When("I read binary data")
    public final void executeReadBinaryData() throws DaoException{
        OutputStream os = DeviceLogTestHelper.createOutpuStream();
        SQLServerDeviceLogDAO dao = new SQLServerDeviceLogDAO();
        try {
            this.readResult = dao.readBinaryData(rowId, os);
        } catch (DaoException e) {
            this.readResult = false;
        }
    }
    
    @Then("the result should be <aResult>")
    public final void resultShouldBe(@Named("aResult") final String aResult){
        if (aResult.equalsIgnoreCase("successful")){
            assertTrue(0 == this.deviceLog.getNCRWSSResultCode());
        }else if (aResult.equalsIgnoreCase("failed")){
            assertFalse(0 == this.deviceLog.getNCRWSSResultCode());
        }else{
            assertTrue(Integer.parseInt(aResult)
                    == this.deviceLog.getNCRWSSResultCode());
        }
    }
    
    @When("I get device logs")
    public final void getDeviceLogs() throws DaoException{
        DeviceLogTestHelper.loadTableWithData();
        SQLServerDeviceLogDAO dao = new SQLServerDeviceLogDAO();
        this.logs = dao
            .getLogsOfDevice("7d23641f121a404fab06c8811c155df67d23641f", 0, 5);
    }
    
    @Then("rowId should be set to <rowId>")
    public final void rowIdShouldBe(@Named("rowId") final String expectedrowId){
        if (expectedrowId.equalsIgnoreCase("not empty")){
            assertFalse(this.deviceLog.getRowId().isEmpty());
        }else if (expectedrowId.equalsIgnoreCase("empty")
                || expectedrowId.equalsIgnoreCase("null")){
            assertTrue((null == this.deviceLog.getRowId()) ||
                    this.deviceLog.getRowId().isEmpty());
        }else{
            assertTrue(this.deviceLog.getRowId()
                    .equalsIgnoreCase(expectedrowId));
        }
    }
    
    @Then("udid should be set to <udid>")
    public final void udidShouldBe(@Named("udid") final String expectedudid){
        if (expectedudid.equalsIgnoreCase("not empty")){
            assertFalse(this.deviceLog.getUdid().isEmpty());
        }else if (expectedudid.equalsIgnoreCase("empty")
                || expectedudid.equalsIgnoreCase("null")){
            assertTrue((null == this.deviceLog.getUdid())
                    || this.deviceLog.getUdid().isEmpty());
        }else{
            assertTrue(this.deviceLog.getUdid().equalsIgnoreCase(expectedudid));
        }
    }
    
    @Then("logDate should be set to <logDate>")
    public final void logDateShouldBe(@Named("logDate") final
            String expectedlogDate) throws ParseException{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        if (expectedlogDate.equalsIgnoreCase("not empty")){
            assertFalse(null == this.deviceLog.getLogDate());
        }else if (expectedlogDate.equalsIgnoreCase("empty")
                || expectedlogDate.equalsIgnoreCase("null")){
            assertTrue(null == this.deviceLog.getLogDate());
        }else{
            assertTrue(df.parse(expectedlogDate)
                    .equals(this.deviceLog.getLogDate()));
        }
    }
    
    @Then("uploadTime should be set to <uploadTime>")
    public final void uploadTimeShouldBe(@Named("uploadTime") final
            String uploadTime) throws ParseException{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        if (uploadTime.equalsIgnoreCase("not empty")){
            assertFalse(null == this.deviceLog.getUploadTime());
        }else if (uploadTime.equalsIgnoreCase("empty")
                || uploadTime.equalsIgnoreCase("null")){
            assertTrue(null == this.deviceLog.getUploadTime());
        }else{
            assertTrue(df.parse(uploadTime)
                    .equals(this.deviceLog.getUploadTime()));
        }
    }
    
    @Then("lastUpdated should be set to <lastUpdated>")
    public final void lastUpdatedShouldBe(
            @Named("lastUpdated") final String lastUpdated)
    throws ParseException{
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        if (lastUpdated.equalsIgnoreCase("not empty")){
            assertFalse(null == this.deviceLog.getLastUpdated());
        }else if (lastUpdated.equalsIgnoreCase("empty")
                || lastUpdated.equalsIgnoreCase("null")){
            assertTrue(null == this.deviceLog.getLastUpdated());
        }else{
            assertTrue(df.parse(lastUpdated)
                    .equals(this.deviceLog.getLastUpdated()));
        }
    }
    
    @Then("the read result should be <readResult>")
    public final void readResultShouldBe(@Named("readResult") final
            String expectedreadResult){
        if (expectedreadResult.equalsIgnoreCase("true")){
            assertTrue(this.readResult);
        } else {
            assertFalse(this.readResult);
        }
    }
    
}
