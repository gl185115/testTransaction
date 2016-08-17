package ncr.res.mobilepos.systemsetting.dao.test;

import java.sql.Date;
import java.sql.SQLException;
import java.util.Map;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.systemsetting.dao.SQLServerSystemSettingDAO;

import org.dbunit.DatabaseUnitException;
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

public class SQLServerSystemSettingDAOSteps extends Steps{
    private DBInitiator dbInit;
    private SQLServerSystemSettingDAO testSQLServerSystemDAO = null;
    private String datapath = "test/ncr/res/mobilepos/systemsetting/";
    
    @BeforeScenario
    public final void SetUpClass()
    {
        //By default Company ID is empty
        //GlobalConstant.setCorpid("");
        Requirements.SetUp();
        dbInit = new DBInitiator("SQLServerSystemSettingDAOSteps", DATABASE.RESMaster);
    }
    
    @AfterScenario
    public final void TearDownClass(){
        Requirements.TearDown();
    }
    
    
    @Given("a table entry named {$dataset} in the database")
    public final void aTableEntryNamedInTheDatabase(final String dataset){
        try {
            dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    datapath + dataset);
        } catch (Exception e) {
            Assert.fail("Fail to set System Settings");
        }
    }
    
    @Given("an instance of SQLServerSystemSettingDAO")
    public final void anInstanceOfSQLServerSystemSettingDAO(){
        try {
            testSQLServerSystemDAO = new SQLServerSystemSettingDAO();
        } catch (DaoException e) {
            Assert.fail("Instantiating SQLServerSystemSettingDAO failed. \n"
                    + e.getMessage());
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
        String skips = settings.get("skips");
        
        businessdate = (businessdate.equals("dontset")) ? null : businessdate;
        eod = (eod.equals("dontset")) ? null : eod;
        skips = skips.equals("dontset") ? null : skips; 

        try {
            testSQLServerSystemDAO.setDateSetting(companyid, storeid, businessdate, eod, skips);
        } catch (DaoException e) {
            Assert.fail("Fail to set the following DateSettings. "
                    + e.getMessage());
        }
    }
    
    @Then("the following date settings follows: $expectedDateSettings")
    public final void theFollowingDateSettingsFollows(
            final ExamplesTable expectedSettings)
    throws DataSetException{
        ITable actualMSTBizDayTable = dbInit.getTableSnapshot("MST_BIZDAY");
        
        if(expectedSettings.getRowCount() == 0){
            Assert.assertEquals("There is no BusinessDate", 
                    0, actualMSTBizDayTable.getRowCount());
            return;
        }

        /*Expect the results here*/
        Map<String, String> expectedSetting = expectedSettings.getRow(0);

        Assert.assertEquals("Compare the BusinessDayDate", 
                Date.valueOf(expectedSetting.get("businessdate")), 
                (Date)actualMSTBizDayTable.getValue(0, "TodayDate"));
        //Assert the EOD only
        Assert.assertEquals("Compare the EOD", 
                expectedSetting.get("eod"), 
                ((String)actualMSTBizDayTable.getValue(0, "SwitchTime"))
                .trim());    
        //Assert for the Skip only
        Assert.assertEquals("Compare the Skips", 
                Integer.parseInt(expectedSetting.get("skips")), 
                actualMSTBizDayTable.getValue(0, "Skip"));
    }
}
