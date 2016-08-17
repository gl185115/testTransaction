package ncr.res.mobilepos.systemsetting.resource.test;

import static org.junit.Assert.assertEquals;

import java.sql.Date;
import java.util.Map;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.systemsetting.model.DateTime;
import ncr.res.mobilepos.systemsetting.model.DateSetting;
import ncr.res.mobilepos.systemsetting.model.SystemSetting;
import ncr.res.mobilepos.systemsetting.resource.SystemSettingResource;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

public class SystemSettingResourceSteps extends Steps{
        private DBInitiator dbInit;
        private SystemSettingResource testSystemSettingResource;
        private String datapath =
            "test/ncr/res/mobilepos/systemsetting/";
        private ResultBase actualResultBase;
        private SystemSetting actualSystemSetting;
        private DateTime dateTime;
        
        @BeforeScenario
        public final void SetUpClass()
        {
            //By default Company ID is empty
            GlobalConstant.setCorpid("");
            Requirements.SetUp();
            dbInit = new DBInitiator("SystemSettingResourceSteps", DATABASE.RESMaster);
            testSystemSettingResource = new SystemSettingResource();
        }
        
        @AfterScenario
        public final void TearDownClass(){
            Requirements.TearDown();
        }
        
        @Given("a table entry named {$dataset} in the database")
        public final void aTableEntryNamedInTheDatabase(final String dataset){
            try {
                dbInit.ExecuteOperation(
                        DatabaseOperation.CLEAN_INSERT,
                        datapath + dataset);
            } catch (Exception e) {
                Assert.fail("Fail to set System Settings");
            }
        }

        @When("the following date settings are set: $dateSetting")
        public final void theFollowingDateSettingsAreSet(
                final ExamplesTable dateSetting){
            Map<String, String> settings = dateSetting.getRow(0);
            String companyid = "01";
            String storeid = "0031";
            String businessdate = settings.get("businessdate");
            String eod = settings.get("eod");
            String skips = settings.get("skip");
        
            businessdate =
                (businessdate.equals("empty")) ? "" : businessdate;
            businessdate =
                    (businessdate.equals("dontset")) ? null : businessdate;

            eod = (eod.equals("empty")) ? "" : eod;
            skips = skips.equals("empty") ? "" : skips;
            
            eod = (eod.equals("dontset")) ? null : eod;
            skips = skips.equals("dontset") ? null : skips;

            actualResultBase =
                testSystemSettingResource
                    .setDateSetting(companyid, storeid, businessdate, eod, skips);                
        }
        @When("a request to get the Server's current Date and Time")
        public final void aRequestToGetCurrentDateTime() {
        	dateTime = testSystemSettingResource.getCurrentDateTime();
        	actualResultBase = dateTime;
        }
        @Then("Date and Time should not be empty.")
        public final void dateAndTimeIsNotEmpty() {
        	Assert.assertNotNull(dateTime.getCurrentDateTime());
        }
        @Then("Time Of Day data should be in yyyyMMddHHmmss format")
        public void timeOfDayFormat() {
        	boolean formatted = DateFormatUtility.isLegalFormat(
        			dateTime.getCurrentDateTime(), "yyyyMMddHHmmss");
        	Assert.assertTrue(formatted);
        }
        
        @When("a request to get the Date Settings")
        public final void aRequestToGetTheDateSettings(final String companyId, final String storeId){
            actualSystemSetting = testSystemSettingResource.getDateSetting(companyId, storeId);
            actualResultBase = actualSystemSetting;
        }
        
        @Then("the following date settings follows: $expectedDateSettings")
        public final void theFollowingDateSettingsFollows(
                final ExamplesTable expectedSettings) throws DataSetException{
            ITable actualMSTBizDayTable = dbInit.getTableSnapshot("MST_BIZDAY");
            
            if(expectedSettings.getRowCount() == 0){
                Assert.assertEquals("There is no BusinessDate", 
                        0, actualMSTBizDayTable.getRowCount());
                return;
            }

            /*Expect the results here*/
            Map<String, String> expectedSetting = expectedSettings.getRow(0);
            
            String skips = expectedSetting.get("skip");

            Assert.assertEquals("Compare the BusinessDayDate", 
                    Date.valueOf(expectedSetting.get("businessdate")), 
                    (Date)actualMSTBizDayTable.getValue(0, "todaydate"));
            //Assert the EOD only
            Assert.assertEquals("Compare the EOD", 
                expectedSetting.get("eod"), ((String)actualMSTBizDayTable
                                            .getValue(0, "switchtime")).trim());
            //Assert for the Skip only
            Assert.assertEquals("Compare the Skips", 
                    Integer.parseInt(skips),
                    actualMSTBizDayTable.getValue(0, "skip"));
        }
        
        @Then("resultcode should be $code")
        public final void resultCodeShouldBe(final int code){
            assertEquals(code, actualResultBase.getNCRWSSResultCode());
        }
        
        @Then("returned Result Code should be {$code}")
        public final void returnedResultCodeShouldbe(final String code){
            int expectedResultCode = 0; //0 means OK
            int expectedExtendedResultCode = 0; //0 means OK
            
            if(code.equals("FAILED_DATE_SETTINGS")){
                expectedResultCode = ResultBase.RESSYS_ERROR_DATESET_FAIL;
            }
            
            Assert.assertEquals(expectedResultCode,
                    actualResultBase.getNCRWSSResultCode());
            Assert.assertEquals(expectedExtendedResultCode,
                    actualResultBase.getNCRWSSExtendedResultCode());
        }
        
        @Then("the following Date Setting JSON" +
                " object are retrieved: $retrievedJSON")
        public final void theFollowingDateSettingJSONObjectAreRetrieved(
                final ExamplesTable retrievedJSON){
            Map<String, String> dateSetting = retrievedJSON.getRow(0);
            
            int skips = Integer.parseInt(dateSetting.get("skip"));
            String eod = dateSetting.get("eod");
            String businessdate = dateSetting.get("businessdate");
            
            eod = eod.equals("empty") ? null : eod;
            businessdate = businessdate.equals("empty") ? null : businessdate;
            
            DateSetting actualDateSetting =
                actualSystemSetting.getDateSetting();
            
            Assert.assertEquals("Expect the businessDate",
                    businessdate, actualDateSetting.getToday());
            Assert.assertEquals("Expect the EOD",
                    eod, actualDateSetting.getEod().trim());
            Assert.assertEquals("Expect the Skips",
                    skips, actualDateSetting.getSkips());
        }
        
        @Then("SystemSetting xml string should be {$xml}")
        public final void seriallize(final String xml) throws Exception{
            XmlSerializer<SystemSetting> posLogRespSrlzr =
                new XmlSerializer<SystemSetting>();
            String actual =
                posLogRespSrlzr.marshallObj(SystemSetting.class,
                        actualSystemSetting, "UTF-8");
            System.out.println(actual);
            assertEquals(xml, actual);
        }
}

