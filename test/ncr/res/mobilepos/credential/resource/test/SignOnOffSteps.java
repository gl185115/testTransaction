package ncr.res.mobilepos.credential.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import junit.framework.Assert;
import ncr.res.mobilepos.credential.model.Operator;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

import static org.mockito.Mockito.*;

/**
 * Steps class for operator's changePasscode.
 */
@SuppressWarnings("deprecation")
public class SignOnOffSteps extends Steps {
    /**
     * CredentialResource instance.
     */
    private CredentialResource credResource;
    /**
     * ResultBase instance.
     */
    private ResultBase resultBase;
    /*
     * Operator result. 
     */
    private Operator operatorResult; 
    /** The dbinit. */
    private DBInitiator dbinit = null;
    private ServletContext context;
    /**
     * Invokes before execution of each scenario.
     */
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        mock(Calendar.class);
    }

    /**
     * Invokes after execution of each scenario.
     */
    @AfterScenario
    public final void tearDown() {
        Requirements.TearDown();
    }

    /**
     * A Given step, initializes CredentialResource.
     */
    @Given("a Credential Resource")
    public final void aCredentialResource() {
        this.context = Requirements.getMockServletContext();
        credResource = new CredentialResource();
        try {
            Field contextField = credResource.getClass().getDeclaredField("context");
            contextField.setAccessible(true);
            contextField.set(credResource, this.context);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.fail("Cant load Mock Servlet Context.");
        } 
    }

    /**
     * A Then step, tests actual and expect result code.
     *
     * @param result
     *            The expected result code.
     */
    @Then("I should get resultcode {$Result}")
    public final void checkResult(final int result) {
        assertThat(resultBase.getNCRWSSResultCode(), is(equalTo(result)));
    }

    /**
     * A Given step, initializes new dataset.
     *
     * @param filename
     *            the filename
     */
    @Given("an initial data from {$filename}")
    public final void emptyTable(final String filename) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
        Calendar c = Calendar.getInstance();
        Map<String, Object> replacements = new HashMap<String, Object>(); 
        replacements.put("NOW", sdf.format(c.getTime()));
        c.add(Calendar.DATE, 756);  // number of days to add
        replacements.put("756DaysAhead", sdf.format(c.getTime()));
        c.add(Calendar.DATE, 1812);
        replacements.put("2568DaysAhead", sdf.format(c.getTime()));
        
        dbinit = new DBInitiator("CredentialResource", DATABASE.RESMaster);
        try {
            dbinit.executeWithReplacement("test/ncr/res/mobilepos/credential/resource/test/"
                    + filename, replacements);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.fail("Can't set the database for SignOn.");
        }
    }

    /**
     * Sign on.
     *
     * @param operator
     *            The operator
     * @param passcode
     *            The passcode
     * @param terminal
     *            The terminal id
     */
    @When("signing on operator:{$operator} passcode:{$passcode} terminal:{$terminal}")
    public final void signOn(String operator, String companyid,
            String passcode,
            String terminal) {
        new SimpleDateFormat("yyyyMMddhhmmss");
        try {
        resultBase =
            operatorResult =
                credResource.requestSignOn(operator, companyid,
                        passcode, terminal, false);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
            Assert.fail("Can't Mock Calendar for SignOn");
        }
    }
    
    /**
     * Sign on.
     *
     * @param operator
     *            The operator
     * @param passcode
     *            The passcode
     * @param terminal
     *            The terminal id
     */
    @When("SPART signing on operator:{$operator} passcode:{$passcode} terminal:{$terminal}")
    public final void spartSignOn(String operator,
            String passcode,
            String terminal) {
        resultBase =
            operatorResult = 
                credResource.credentialSpartLogin(operator,
                        passcode, terminal);
    }
    
    @When("logging off operator:{$operator}")
    public final void signOff(String operator) {
        resultBase =
                credResource.requestSignOff(operator);
    }
    
    @When("demo signing on operator:{$operator} passcode:{$passcode} terminal:{$terminal}")
    public final void signOnDemo(String operator, String companyid,
            String passcode,
            String terminal) {
        resultBase =
            operatorResult =
                credResource.requestSignOn(operator, companyid,
                        passcode, terminal, true);
    }

    @Then("Operator result should be $expected")
    public final void checkResult(ExamplesTable expected) {
        Map<String, String> expectedData = expected.getRow(0);
        Assert.assertEquals(
                "Operator No",
                expectedData.get("operatorno"), operatorResult.getOperatorNo()
                == null ? "" : operatorResult.getOperatorNo());
        Assert.assertEquals(
                "Passcode",
                expectedData.get("passcode"), operatorResult.getPasscode()
                == null ? "" : operatorResult.getPasscode());
        Assert.assertEquals(
                "Name",
                expectedData.get("name"), operatorResult.getName()
                == null ? "" : operatorResult.getName());
    }

    /**
     * Test updated data in MST_USER_CREDENTIAL table.
     *
     * @param expectedEmployees
     *            the expected employees
     * @throws DataSetException
     *             the exception thrown
     */
    @Then("the MST_USER_CREDENTIALS database table should have the following row(s): $expectedEmployees")
    public final void testEmployeeData(final ExamplesTable expectedEmployees)
            throws DataSetException {
        ITable actualListEmployee = dbinit
                .getTableSnapshot("MST_USER_CREDENTIALS");
        
        Assert.assertEquals("verify MST_USER_CREDENTIALS noumber of row", expectedEmployees.getRowCount(),
                actualListEmployee.getRowCount());
        
        int i = 0;
        for(Map<String, String> expectedOpt: expectedEmployees.getRows()){
            String operatorno = String.valueOf(
                    expectedOpt.get("operatorno"))
                    .trim();
            String passcode = String.valueOf(expectedOpt
                    .get("passcode")).trim();
            String operatorname = String.valueOf(
                    expectedOpt.get("operatorname"));
            String operatorType = String.valueOf(
                    expectedOpt.get("operatortype"));
            String terminalid= String.valueOf(
                    expectedOpt.get("terminalid"))
                    .trim();
            if (operatorno.equals("NULL")) {
                Assert.assertTrue(actualListEmployee.getValue(i, "OperatorNo") == null);
            } else {
                Assert.assertEquals("Compare OperatorNo at row" + i,
                     operatorno,
                    String.valueOf(actualListEmployee.getValue(i, "OperatorNo"))
                            .trim());
            }
            if (passcode.equals("NULL")) {
                Assert.assertTrue(actualListEmployee.getValue(i, "PassCode") == null);
            } else {
                Assert.assertEquals("Compare Passcode at row" + i,
                        passcode,
                        String.valueOf(actualListEmployee.getValue(i, "PassCode")).trim());
            }
           if (operatorname.equals("NULL")) {
               Assert.assertTrue(actualListEmployee.getValue(i, "OperatorName") == null);
           } else {
               Assert.assertEquals("Compare OperatorName at row" + i,
                       operatorname,
                       String.valueOf(actualListEmployee.getValue(i, "OperatorName"))
                       .trim());
           }
           if (operatorType.equals("NULL")) {
               Assert.assertTrue(actualListEmployee.getValue(i, "OperatorType") == null);
           } else {
               Assert.assertEquals("Compare OperatorType at row" + i,
                       Integer.parseInt(operatorType),
                       ((Integer)actualListEmployee.getValue(i, "OperatorType"))
                       .intValue());
           }
            
            if (terminalid.equals("NULL")) {
                Assert.assertTrue(actualListEmployee.getValue(i, "TerminalId") == null);
            } else {
                Assert.assertEquals("Compare TerminalID at row" + i,
                        terminalid,
                        String.valueOf(actualListEmployee.getValue(i, "TerminalId"))
                                .trim());
            }
            
            i++;
        }
    }
    
    @Then("the PRM_GROUP_FUNCTION database table should have the following row(s): $expectedtable")
    public final void thePrmGroupFunctionDatabaseTableShouldHaveTheFollowingRow(ExamplesTable expectedTable) {
        try {
            ITable actualTable = 
                dbinit.getTableSnapshot("PRM_GROUP_FUNCTION");
            Assert.assertEquals("Compare the number of rows in PRM_GROUP_FUNCTION",
                    expectedTable.getRowCount(),
                    actualTable.getRowCount());
            //|group|groupname|transaction|reports|settings|merchandise|administration|drawer|
            int i = 0;
            for(Map<String, String> expectedDeviceInfo : expectedTable.getRows()) {
                Assert.assertEquals("Compare the GROUP in PRM_GROUP_FUNCTION row" + i,
                        Integer.parseInt(expectedDeviceInfo.get("groupcode")),
                        ((Integer)actualTable.getValue(i, "groupcode")).intValue());
                Assert.assertEquals("Compare the GROUPNAME in PRM_GROUP_FUNCTION row" + i,
                        expectedDeviceInfo.get("groupname"),
                        (String)actualTable.getValue(i, "groupname"));
                Assert.assertEquals("Compare the TRANSACTION in PRM_GROUP_FUNCTION row" + i,
                        Boolean.parseBoolean(expectedDeviceInfo.get("transactions")),
                        ((Boolean)actualTable.getValue(i, "transactions")).booleanValue());
                Assert.assertEquals("Compare the REPORTS in PRM_GROUP_FUNCTION row" + i,
                        Boolean.parseBoolean(expectedDeviceInfo.get("reports")),
                        ((Boolean)actualTable.getValue(i, "reports")).booleanValue());
                Assert.assertEquals("Compare the SETTINGS in PRM_GROUP_FUNCTION row" + i,
                        Boolean.parseBoolean(expectedDeviceInfo.get("settings")),
                        ((Boolean)actualTable.getValue(i, "settings")).booleanValue());
                Assert.assertEquals("Compare the MERCHANDISE in PRM_GROUP_FUNCTION row" + i,
                        Boolean.parseBoolean(expectedDeviceInfo.get("merchandise")),
                        ((Boolean)actualTable.getValue(i, "merchandise")).booleanValue());
                Assert.assertEquals("Compare the ADMINISTRATION in PRM_GROUP_FUNCTION row" + i,
                        Boolean.parseBoolean(expectedDeviceInfo.get("administration")),
                        ((Boolean)actualTable.getValue(i, "administration")).booleanValue());
                Assert.assertEquals("Compare the DRAWER in PRM_GROUP_FUNCTION row" + i,
                        Boolean.parseBoolean(expectedDeviceInfo.get("drawer")),
                        ((Boolean)actualTable.getValue(i, "drawer")).booleanValue());
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in PRM_GROUP_FUNCTION.");
        }
    }
    
    @Then("the MST_EMPINFO database table should have the following row(s): $expectedtable")
    public final void theOpeMastTblShouldHaveTheFollowingRows(ExamplesTable expectedTable) {
        try {
            ITable actualTable = 
                dbinit.getTableSnapshot("MST_EMPINFO");
            Assert.assertEquals("Compare the number of rows in MST_EMPINFO",
                    expectedTable.getRowCount(),
                    actualTable.getRowCount());
            //|empcode|password|opetype|seclevel1|seclevel2|subchar1|
            int i = 0;
            for(Map<String, String> expectedDeviceInfo : expectedTable.getRows()) {
                Assert.assertEquals("Compare the EMPCODE in MST_EMPINFO row" + i,
                        expectedDeviceInfo.get("empcode"),
                        ((String)actualTable.getValue(i, "EmpCode")));
                if (expectedDeviceInfo.get("password").equals("NULL")) {
                  Assert.assertNull("Expected that PASSWORD should be Null at row" + i, actualTable.getValue(i, "password"));
                } else {
                    Assert.assertEquals("Compare the PASSWORD in MST_EMPINFO row" + i,
                            expectedDeviceInfo.get("password"),
                            ((String)actualTable.getValue(i, "Password")));   
                }
                Assert.assertEquals("Compare the OPETYPE in MST_EMPINFO row" + i,
                        expectedDeviceInfo.get("opetype"),
                        ((String)actualTable.getValue(i, "OpeType")));
                Assert.assertEquals("Compare the SECLEVEL1 in MST_EMPINFO row" + i,
                        Integer.parseInt(expectedDeviceInfo.get("seclevel1")),
                        ((Integer)actualTable.getValue(i, "SecLevel1")).intValue());
                Assert.assertEquals("Compare the SECLEVEL2 in MST_EMPINFO row" + i,
                        Integer.parseInt(expectedDeviceInfo.get("seclevel2")),
                        ((Integer)actualTable.getValue(i, "SecLevel2")).intValue());
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in PRM_GROUP_FUNCTION.");
        }
    }

    @Then("the Operator should have the following XML: $expectedXml")
    public final void theOperatorShouldHaveTheFollowingXML(final String expectedXml){
        XmlSerializer<Operator> operatorSerializer = new XmlSerializer<Operator>();
        try {
            if (operatorResult.getNCRWSSResultCode() == 0) {
               operatorResult.setSignOnAt("21:05");
               operatorResult.setDate("Nov 22, '12");
            }
            
            String actualXml =
            operatorSerializer.marshallObj(Operator.class, operatorResult, "UTF-8");
            Assert.assertEquals("Expect the Serialize Operator", expectedXml, actualXml);
        } catch (JAXBException e) {
            e.printStackTrace();
            Assert.fail("Can not perform marshalling to Operator Object.");
        }
    }
}
