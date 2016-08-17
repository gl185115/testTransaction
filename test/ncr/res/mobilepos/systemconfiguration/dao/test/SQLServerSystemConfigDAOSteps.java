package ncr.res.mobilepos.systemconfiguration.dao.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.Map;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.systemconfiguration.dao.SQLServerSystemConfigDAO;

import org.dbunit.DatabaseUnitException;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;


public class SQLServerSystemConfigDAOSteps extends Steps{
    private final String datasetPath =
        "test/ncr/res/mobilepos/systemconfiguration/resource/test/";
    private DBInitiator dbInit = null;
    private SQLServerSystemConfigDAO testSqlServerConfigDao = null;
    private Map<String, String> actualSystemParameters = null;
    private String actualParameterValue = null;
    private boolean actualBooleanValue = false;
    
    @BeforeScenario
    public final void SetUpClass()
    {        
        Requirements.SetUp();
        dbInit = new DBInitiator("SQLServerSystemConfigDAOSteps", DATABASE.RESMaster);
    }
    
    @AfterScenario
    public final void TearDownClass(){
        Requirements.TearDown();
    }
    
    @Given("a batch of values for System Configuration named $dataset")
    public final void aBatchOfValuesForSystemConfigurationNamed(
            final String dataset)
    throws DatabaseUnitException, SQLException, Exception {
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                datasetPath + dataset);
    }
    
    @Given("an instance of SQLServerSystemConfigDAO")
    public final void anInstanceOfSQLServerSystemConfigDAO(){
        try {
            testSqlServerConfigDao = new SQLServerSystemConfigDAO();
        } catch (DaoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Then("its Database Manager must have been settled")
    public final void itsDatabaseManagerMustHaveBeenSettled(){
        try {
            assertNotNull(testSqlServerConfigDao);
            assertNotNull(testSqlServerConfigDao.getClass()
                    .getDeclaredField("dbManager"));
        } catch (SecurityException e) {
            // TODO Auto-generated catch block
            Assert.fail("Security Exception for getting the Database Manager");
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            Assert.fail("Member variable named 'dbManager not found'");
            e.printStackTrace();
        }
    }
    
    @When("all parameters are retrieved")
    public final void allParametersAreRetrieved(){
        try {
            actualSystemParameters =
                testSqlServerConfigDao.getSystemParameters();
        } catch (DaoException e) {
            Assert.fail("Failed to retrive all the System Parameters");
            e.printStackTrace();
        }
    }
    
    @When("I want to get a value from parameter named"
            + " {$key} in {$category} category")
    public final void IWantToGetAValueFromParameterNamed(
            final String key, final String category) {
        try {
            actualParameterValue =
                testSqlServerConfigDao.getParameterString(key, category);
        } catch (DaoException e) {
            Assert.fail("Getting the value named "
                    + key +" in category of "+ category +" failed");
            e.printStackTrace();
        }
    }    
    
    @When("I want to get a BOOLEAN value from parameter named"
            + " {$key} in {$category} category")
    public final void IWantToGetABooleanValueFromParameterNamedInCategory(
            final String key, final String category) {
        actualBooleanValue = false;
        try {
            actualBooleanValue =
                testSqlServerConfigDao.getParameterBoolean(key, category);
        } catch (DaoException e) {
            Assert.fail("Getting the BOOLEAN value named "
                    + key +" in category of "+ category +" failed");
            e.printStackTrace();
        }
    } 
    
    @When("I want to update the following parameters $systemParameters")
    public final void IWantToUpdateTheFollowingParameters(
            final ExamplesTable systemParameters) {
        for (Map<String, String> param :  systemParameters.getRows()){
            String key = param.get("Key");
            String value = param.get("Value");
            String category = param.get("Category");
            
             try {
                testSqlServerConfigDao.setParameterString(value, category, key);
            } catch (DaoException e) {
                Assert.fail("Fail  to set a parameter : Name {" + key 
                        + "} Value {" + value
                        + "} Category {" + category + "}");
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    @When("I want to add the following parameters $systemparameters")
    public final void IWantToAddTheFollowingParameters(
            final ExamplesTable systemParameters) {
        for (Map<String, String> param :  systemParameters.getRows()){
            String key = param.get("Key");
            String value = param.get("Value");
            String category = param.get("Category");
            String description = param.get("Description");
            
             try {
                testSqlServerConfigDao.addParameterString(value,
                        category, key, description);
            } catch (DaoException e) {
                Assert.fail("Fail  to set a parameter : Name {" + key 
                        + "} Value {" + value
                        + "} Category {" + category + "}");
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
        }
    }
    
    
    @Then("I should have the following parameters $systemParameters")
    public final void IShouldHaveTheFollowingParameters(
            final ExamplesTable expectedSystemParameters){
        
        assertThat("System Parameters Count", actualSystemParameters.size(), 
                is(equalTo(expectedSystemParameters.getRowCount())));
        
        for (Map<String, String> expectedParam
                : expectedSystemParameters.getRows()){
            String parameterName = expectedParam.get("Key");
            String actualvalue = actualSystemParameters.get(
                    expectedParam.get("Key"));
            
            assertThat("System parameter named " + parameterName,
                    actualvalue, is(equalTo(expectedParam.get("Value"))));
        }
    }
    
    @Then("I should have {$key} value of {$value}")
    public final void IShouldHaveParameterValueOf(
            final String key, final String expectedValue){
        assertThat("System parameter named " + key,
                actualParameterValue, is(equalTo(expectedValue)));
    }
    
    @Then("I should have {$key} boolean value of {$value}")
    public final void IShouldHaveBooleanValueOf(
            final String key, final String value){
        boolean expectedValue = Boolean.parseBoolean(value);
        
        assertThat("System parameter named " + key,
                actualBooleanValue, is(equalTo(expectedValue)));
    }
}
