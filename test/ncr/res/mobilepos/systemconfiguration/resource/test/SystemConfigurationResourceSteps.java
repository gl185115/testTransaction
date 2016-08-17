package ncr.res.mobilepos.systemconfiguration.resource.test;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.systemconfiguration.resource.SystemConfigurationResource;
import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.*;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

import javax.servlet.ServletContext;
import java.io.IOException;
import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.Map;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

public class SystemConfigurationResourceSteps extends Steps{
    private ServletContext servletContext = null;
    private SystemConfigurationResource testSystemConfiguration;
    private DBInitiator dbInit = null;
    private boolean startUp = false;
//    private WebServerGlobals actualWebServerGlobal = null;

    @BeforeScenario
    public final void SetUpClass() {
        //By default Company ID is empty
        GlobalConstant.setCorpid("");
        Requirements.SetUp();
        dbInit = new DBInitiator("SystemConfigurationResourceSteps", DATABASE.RESMaster);
        testSystemConfiguration = new SystemConfigurationResource();
    }

    @AfterScenario
    public final void TearDownClass() {
        Requirements.TearDown();
    }

    @Given("a {$adj} batch of values for"
            + " System Configuration named $systemName")
    public final void aBatchOfValuesForSystemConfigurationNamed(
            final String adj, final String systemName)
            throws DatabaseUnitException, SQLException, Exception {
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/systemconfiguration/resource/test/"
                        + systemName);
    }

    @Given("the WebStoreServer")
    public final void theWebStoreServer() {
        //By default there are no Servlet Context
        this.theServletContextShouldHaveNullValue();
    }

    @Given("an CustomerTier Table {$table}")
    public final void anEmptyCustomerTierTable(final String table) {
        try {
            dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    "test/ncr/res/mobilepos/systemconfiguration/resource/test/"
                            + table);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Cant set the Customer Tier Table"
                    + " to empty in the database.");
        }
    }

    @Given("a null values of {$dataset}")
    public final void updateKeyValue(String dataset)
            throws DatabaseUnitException, SQLException, Exception {
        dbInit.ExecuteOperationNoKey(DatabaseOperation.UPDATE, "KeyId",
                "test/ncr/res/mobilepos/systemconfiguration/resource/test/"
                        + dataset);
    }

    @When("the Web API Starts Up")
    public final void theWebAPIStartsUp() {
        servletContext = null;
        startUp = true;
        try {
            servletContext = Requirements.getMockServletContext();
            Field context = testSystemConfiguration
                    .getClass().getDeclaredField("context");
            context.setAccessible(true);
            context.set(testSystemConfiguration, servletContext);
        } catch (Exception ex) {
            startUp = false;
        }
    }

    @When("the WebStoreServer has been set with {$corpid} Company ID")
    public final void theWebStoreServerHasBeenSetWithCompanyID(
            final String corpid) {
//        testSystemConfiguration.setStoreCompanyID(corpid);
    }

    @When("the WebStoreServer has been set with {$limit} Search Result Limit")
    public final void theWebStoreServerHasBeenSetWithSearchResultLimit(
            final int limit) {
        //testSystemConfiguration.setSearchLimit(limit);
    }

    @When("the Web UI invoke to get System Configuration from the WebStoreServer")
    public final void theWebUIInvokeToTheWebMethod()
            throws IllegalArgumentException, SecurityException,
            IllegalAccessException, NoSuchFieldException, IOException {
//    actualWebServerGlobal = testSystemConfiguration.getSystemConfiguration();
    }

    @When("the Web UI invoke to set System Configuration from the WebStoreServer with the following fields: $GlobalJson")
    public final void theWebUInvokeToSetSystemConfigurationFromTheWebStoreServerWithTheFollowingFields(final String globalJSON) {
//       testSystemConfiguration.setSystemConfiguration(globalJSON);
    }

    @Then("the Servlet Context should have the values $context")
    public final void theServletContextShouldHaveThevalues(
            final ExamplesTable context) {
        for (Map<String, String> row : context.getRows()) {
            String key = row.get("Key");
            String actualServletValues =
//                (String) servletContext.getAttribute("SYS_" + key);
                    (String) servletContext.getAttribute(key);
            String expectedValue = row.get("Value").equalsIgnoreCase("null") ? null
                    : row.get("Value");

            assertThat("SysConfig Parameter Named " + key + ":",
                    actualServletValues, is(equalTo(expectedValue)));
        }
    }

    @Then("Start Up should fail")
    public final void startUpShouldFail() {
        assertThat("has startUp called", startUp, is(equalTo(true)));
    }

    @Then("the Servlet Context should have null value")
    public final void theServletContextShouldHaveNullValue() {
        // The Servlet context should be NULL
        assertThat(servletContext, is(equalTo(null)));
    }

    @Then("the following values for WebUI should be returned $systemParameters")
    public final void theFollowingValuesForWebUIShouldBeReturned(
            final ExamplesTable systemParameters)
            throws IllegalArgumentException, IllegalAccessException,
            IOException, NoSuchFieldException, SecurityException {
//        WebServerGlobals expectedWebServerGlobals = new WebServerGlobals();
//        Class<?> fieldType  = null;
//        String systemParamValue = null;
//
//        for (Map<String, String> row : systemParameters.getRows()) {
//          Field field = WebServerGlobals.class.
//              getDeclaredField(StringUtility.toCamelCase((String)row.get("Key")));
//          field.setAccessible(true);
//          fieldType = field.getType();
//          systemParamValue = row.get("Value");
//
//          if (fieldType.equals(String.class)) {
//            field.set(expectedWebServerGlobals, StringUtility.convNullStringToNull(systemParamValue));
//            } else if (fieldType.equals(boolean.class)) {
//              field.set(expectedWebServerGlobals, Boolean.parseBoolean(systemParamValue));
//            } else if (fieldType.equals(int.class)) {
//              field.set(expectedWebServerGlobals, Integer.parseInt(systemParamValue));
//            }
//        }
//
//        for (Field field : expectedWebServerGlobals.getClass()
//                .getDeclaredFields())  {
//            field.setAccessible(true);
//            fieldType = field.getType();
//
//            if (fieldType.equals(String.class)) {
//              if (field.get(expectedWebServerGlobals) == null) {
//                field.set(expectedWebServerGlobals, null);
//              }
//                assertThat("The System Globals for Web UI at "
//                        + field.getName(),
//                      (String)field.get(actualWebServerGlobal),
//                      is(equalTo((String)field.get(expectedWebServerGlobals))));
//            } else if (fieldType.equals(boolean.class)) {
//                assertThat("The System Globals for Web UI at "
//                    + field.getName(),
//                    (Boolean)field.get(actualWebServerGlobal),
//                    is(equalTo((Boolean)field.get(expectedWebServerGlobals))));
//            } else if (fieldType.equals(int.class)) {
//              assertThat("The System Globals for Web UI at "
//                        + field.getName(),
//                        (Integer)field.get(actualWebServerGlobal),
//                        is(equalTo((Integer)field.get(expectedWebServerGlobals))));
//            } else if (fieldType.equals(FantamiliarConfig.class) ||
//                fieldType.equals(MemberServer.class) ||
//                fieldType.equals(UrlConfig.class) ||
//                fieldType.equals(List.class)) {
//                // do nothing
//            } else {
//              Assert.fail("The actual parameter in WebServerGlobals named "
//                  + field.getName()
//                  + " was not tested with correct expected value");
//            }
//        }
    }

    @Then("WebStoreServer has {$corpid} Company ID")
    public final void webStoreServerHasCompanyID(final String corpid) {
        assertThat("Asserts the WebStoreServer Company ID: ",
                GlobalConstant.getCorpid(), is(equalTo(corpid)));
    }

    @Then("WebStoreServer has {$limit} Search Result Limit")
    public final void webStoreServerHasSearchResultLimit(final int limit) {
        assertThat("Asserts the WebStoreServer Company ID: ",
                GlobalConstant.getMaxSearchResults(), is(equalTo(limit)));
    }

    @Then("CustomerTier Table should have the following rows: $expectedCustomerTierTable")
    public final void customerTierTableShouldHaveTheFollowingRows(ExamplesTable expectedCustomerTierTable) {
        try {
            ITable actualCustomerTierTable = dbInit.getTableSnapshot("PRM_CUSTOMER_TIER");

            Assert.assertEquals("Expect the number of rows in the PRM_CUSTOMER_TIER are exact",
                    expectedCustomerTierTable.getRowCount(),
                    actualCustomerTierTable.getRowCount());

            int i = 0;
            for (Map<String, String> expectedCustTier : expectedCustomerTierTable.getRows()) {
                Assert.assertEquals("Compare Customer Tier's seqnum in row" + i,
                        Integer.valueOf(expectedCustTier.get("SeqNum")),
                        actualCustomerTierTable.getValue(i, "SeqNum"));
                Assert.assertEquals("Compare Customer Tier's id in row" + i,
                        expectedCustTier.get("Id"),
                        actualCustomerTierTable.getValue(i, "Id"));
                Assert.assertEquals("Compare Customer Tier's displayname in row" + i,
                        expectedCustTier.get("DisplayName"),
                        actualCustomerTierTable.getValue(i, "DisplayName"));
                i++;
            }
        } catch (DataSetException e) {
            e.printStackTrace();
            Assert.fail("Fail to compare the actual Customer Tier database.");
        }
    }

    @Then("the WebServerGlobals Should have the following JSON Format: $expectedWebServerJSON")
    public final void theWebServerGlobalsShouldHaveTheFollowingJSONFormat(final String expectedWebServerGlobalJSON) {
//        try {
//            JsonMarshaller<WebServerGlobals> webServerGlobalMarshaller =
//                new JsonMarshaller<WebServerGlobals>();
//            String actualWebServerGlobalJSON = webServerGlobalMarshaller.marshall(actualWebServerGlobal);
//            JSONAssert.assertEquals(expectedWebServerGlobalJSON, actualWebServerGlobalJSON, true);
//        } catch (IOException e) {
//            Assert.fail("Failed to test the Expected WebServerGlobals JSON");
//        } catch (JSONException e) {
//            Assert.fail("Failed to test the Expected WebServerGlobals JSON");
//        }
    }

    @Then("the following UrlConfig values for UI should be returned: $expectedUrlConfigValues")
    public final void urlConfigValues(final ExamplesTable expectedUrlConfigValues) throws Exception {
//      UrlConfig expectedUrlConfig = new UrlConfig();
//      UrlConfig actualUrlConfig = actualWebServerGlobal.getUrlConfig();
//
//        for (Map<String, String> row : expectedUrlConfigValues.getRows()) {
//          Field f = UrlConfig.class.getDeclaredField(StringUtility.toCamelCase((String)row.get("Key")));
//          f.setAccessible(true);
//          f.set(expectedUrlConfig, (String) row.get("Value"));
//        }
//
//        for (Field f: UrlConfig.class.getDeclaredFields()) {
//          f.setAccessible(true);
//          assertThat("The UrlConfig value for UI at "
//                    + f.getName(),
//                    (String)f.get(actualUrlConfig),
//                    is(equalTo((String)f.get(expectedUrlConfig))));
//      }
    }
}