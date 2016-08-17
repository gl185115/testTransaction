package ncr.res.mobilepos.credential.dao.test;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;
import javax.xml.bind.JAXBException;

import junit.framework.Assert;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.credential.dao.SQLServerCredentialDAO;
import ncr.res.mobilepos.credential.model.Employee;
import ncr.res.mobilepos.credential.model.Operator;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.*;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.mockito.Mockito;


/*
Test Steps:
-Given an is instance of SQLServerCredentialDAO
-Then its Database Manager must have been settled
-Given an Operator with Operator Number {$operator}
-When the Operator do sign-on with passcode {$passCode}
-When the Operator sign's off
-Then the Status of the operator should be {$status}
-Then it should succeed
-Then it should fail
-When the validity of the operator is updated by {$credentialExpiry}
-Given a new operator with Operator Number {$operatorNumber}
 and pass-code {$passCode} to be created
-Then its serialized XML should be have operatorNo
 {$operatorNo}, name {$name}, signOnAt {$signOnAt},
  date {$date}, message {$message}, resultCode {$resultCode},
  extendedResultCode {$extendedResultCode}
*/
@SuppressWarnings("deprecation")
public class SQLServerCredentialDAOSteps extends Steps {
    private ServletContext sc;
    private DBInitiator dbInit;
    private String datasetpath = "test/ncr/res/mobilepos/credential/";
    private SQLServerCredentialDAO testCredentialDAO;
    private CredentialResource resource;
    private Operator givenOperator;
    private Operator signedOnOperator;
    private int resultCode;    
    private List<Employee> actualListOperators = null;
    
    @BeforeScenario
    public final void SetUpClass()
    {        
        Requirements.SetUp();    
        dbInit = new DBInitiator("SQLServerCredentialDAOSteps", DATABASE.RESMaster);        
        sc = Requirements.getMockServletContext();
    }
    
    @AfterScenario
    public final void TearDownClass(){
        Requirements.TearDown();
    }
    
    @Given("an instance of SQLServerCredentialDAO")
    public final void AnInstanceOfSQLServerCredintialDAO()
    {        
        try
        {
            testCredentialDAO = new SQLServerCredentialDAO();
            resource = new CredentialResource();
            
            //Initialize ServletContext
            Field field = resource.getClass().getDeclaredField("context");
            field.setAccessible(true);
            field.set(resource,sc);
            //Initialize ServletContext
        }
        catch (Exception e){
            Assert.fail("Failed to get an Instance of SQLServerCredentialDAO: " 
                    + e.getMessage());
        }
    }
    
    @Given("a table entry named {$dataset} in the database")
    public final void aTableEntryNamedInTheDatabase(final String dataset){
        try {
            dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    datasetpath + dataset);
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Setting the Database table"
                    + " for MST_USER CREDENTIALS failed");
        }
    }
    
    @Then("its Database Manager must have been settled")
    public final void ItsDBIsSettled()
    {
        assertNotNull(testCredentialDAO);
        assertNotNull(testCredentialDAO.getDBManager());
    }
    
    @Given("an Operator with Operator Number {$operator}")
    public final void AnOperatorWith(final String operatorNumber){        
        givenOperator = new Operator();
        givenOperator.setOperatorNo(operatorNumber);            
        resultCode = 0;                        
    }
    @When("the Operator do sign-on with passcode {$passCode}"
            + " and terminalid {$terminalid}")
    public final void OpertorSignOn(String companyId,
            String passCode, final String terminalid) throws DaoException {
        
        passCode = (passCode.equalsIgnoreCase("null")) ? null: passCode;
        
        signedOnOperator = testCredentialDAO.signOnOperator(companyId,
                givenOperator.getOperatorNo(), passCode, terminalid);
        
        resultCode = signedOnOperator.getNCRWSSResultCode();

    }
    
    
    @When("the Operator do sign-on with passcode {$passCode}"
            + " and terminalid {$terminalid} then an $exception occurs")
    public final void OpertorSignOnWithException(final String companyId,
            final String passCode, final String terminalid, String exception) throws Exception {
        
        if(exception.equals("SQLException")){
            DBManager db = Mockito.mock(DBManager.class);
            testCredentialDAO.setDbManager(db);            
            Mockito.doThrow(new SQLException()).when(db).getConnection();
            
            DAOFactory dao = Mockito.mock(DAOFactory.class);
            resource.setDaoFactory(dao);
            Mockito.when(resource.getDaoFactory().getCredentialDAO()).thenReturn(testCredentialDAO);
            
            signedOnOperator = resource.requestSignOn(givenOperator.getOperatorNo(), companyId,
                    passCode, terminalid, false);
            
        } else {
            
            signedOnOperator = resource.requestSignOn(givenOperator.getOperatorNo(), companyId,
                    passCode, terminalid, false);
        }
        
        resultCode = signedOnOperator.getNCRWSSResultCode();

    }
    
   
    @Given("an instance of SQLServerCredentialDAO with mock")
    public final void sqlException() throws DaoException, SQLException{
        try
        {
            testCredentialDAO = new SQLServerCredentialDAO();
            resource = new CredentialResource();
            //Initialize ServletContext
            Field field = resource.getClass().getDeclaredField("context");
            field.setAccessible(true);
            field.set(resource,sc);
            //Initialize ServletContext
        }
        catch (Exception e){
            Assert.fail("Failed to get an Instance of SQLServerCredentialDAO: " 
                    + e.getMessage());
        }
    }
        
    @When("the Operator sign's off")
    public final void OpertorSignOff(){
            ResultBase result =
                resource.requestSignOff(signedOnOperator.getOperatorNo());
            resultCode = result.getNCRWSSResultCode();
    }
    
    @When("a request to list all the operators with"
            + " RetailStoreID{$retailstoreid}")
    public final void aRequestToListAllTheOperators(final String retailstoreid){
        try {
            actualListOperators = testCredentialDAO.listOperators(retailstoreid, null, null, -1); //-1 means list All
        } catch (DaoException e) {
            Assert.fail("Fail to request the list of operator");
        }
    }
    
    @When("a request to reset the passcode of the Operator{$operatorno}"
            + " with retailstoreid{$retailstoreid}")
    public final void aRequestToResetTheOperatorPasscode(
            final String operatorno,
            final String retailstoreid){
        try {
            testCredentialDAO.resetOperatorPasscode(retailstoreid, operatorno);
        } catch (DaoException e) {
            e.printStackTrace();
            Assert.fail("Fail to Reset the Operator's"
                    + " passcode with OperatorNo: " + operatorno );
        }
    }
    
    @Then("the Status of the operator should be {$status}")
    public final void theStausOfOperatorShouldBe(final int expected){
        ResultBase resultBase =
            resource.getOperatorStatus(givenOperator.getOperatorNo());
        int operatorStatus =
            (resultBase.getNCRWSSResultCode()
                    == ResultBase.RESCREDL_OPERATOR_ONLINE) ? 1: 0; 
        assertThat(expected, is(equalTo(operatorStatus)));
        resultCode = 0;
    }
    @Then("it should succeed")
    public final void shouldSucceed(){
        assertThat(0, is(equalTo(resultCode)));
    }
    
    @Then("it should fail with error $error")
    public final void shouldFail(int error){
        assertThat(error, is(equalTo(resultCode)));
    }
    
    @Given("a new operator with Operator Number {$operatorNumber}"
            + " and pass-code {$passCode} to be created")
    public final void newOperatorCreatedWith(
            final String operatorNumber, final String passCode){
            ResultBase result =
                resource.createOperator(operatorNumber, passCode);            
            resultCode = result.getNCRWSSResultCode();

    }    
    
    @Then("its serialized XML should be: $expectedXml")
    public final void serializedXmlHave(final String expectedXml)
    {
        try {
            XmlSerializer<Operator> operatorSerializer =
                new XmlSerializer<Operator>();
            String actualXml;
            //make SignOnAt constant(it takes CURRENT time)
            signedOnOperator.setSignOnAt("19:13");
            signedOnOperator.setDate("Nov 22, '12");
            actualXml = operatorSerializer.marshallObj(Operator.class,
                       signedOnOperator, "UTF-8");
            Assert.assertEquals("Compare the operator xml",
                    expectedXml, actualXml);
        } catch (JAXBException e) {
            e.printStackTrace();
            Assert.fail("Can not perform marshalling to Operator Object.");
        }
    }    
    
    @Then("I should have the following operators: $expectedListOperator")
    public final void iShouldHaveTheFollowingOperators(
            final ExamplesTable expectedListOperator){
        Assert.assertEquals("Compare the exact number of Operator being listed",
                expectedListOperator.getRowCount(), actualListOperators.size());
        
        int i = 0;
        for(Map<String, String> expectedOperator :
            expectedListOperator.getRows()){
            Employee actualOperator = actualListOperators.get(i);
            Assert.assertEquals("Compare the Operator's Number of Operator" + i,
                    expectedOperator.get("operatorno"),
                    actualOperator.getNumber());
            Assert.assertEquals("Compare the Operator's Name of Operator" + i , 
                    expectedOperator.get("operatorname"),
                    actualOperator.getName());
            i++;
        } 
    }
    
    @Then("I should have the following operator's passcode:"
            + "$expectedListOperator")
    public final void IShouldHaveTheFollowingOperatorsPasscode(
            final ExamplesTable expectedListOperator) throws DataSetException{
        ITable actualListOperator = 
            dbInit.getTableSnapshot("MST_USER_CREDENTIALS");
        Assert.assertEquals("Must have exact number of Operators",
                expectedListOperator.getRowCount(),
                actualListOperator.getRowCount());
        
        int i = 0;
        for(Map<String, String> expectedOperator :
            expectedListOperator.getRows()){
            String operatorno = (expectedOperator
                    .get("operatorno")
                    .equals("empty"))
                    ? "" : expectedOperator.get("operatorno");
            String passcode = (expectedOperator.get("passcode")
                    .equals("empty"))
                    ? "" : expectedOperator.get("passcode") ;
            
            Assert.assertEquals("Compare the Operator's Number of Operator" + i,
                    operatorno,
                    String.valueOf(actualListOperator.getValue(i, "operatorno")).trim());
            Assert.assertEquals(
                    "Compare the Operator's Passcode of Operator" + i, 
                    passcode,
                    String.valueOf(actualListOperator.getValue(i, "passcode")).trim());
            i++;
        }    
    }
    
    @Then("I should have an updated table:"
            + "$expectedListOperator")
    public final void IShouldHaveUpdatedTable(
            final ExamplesTable expectedListOperator) throws DataSetException{
        ITable actualListOperator = 
            dbInit.getTableSnapshot("MST_USER_CREDENTIALS");
        Assert.assertEquals("Must have exact number of Operators",
                expectedListOperator.getRowCount(),
                actualListOperator.getRowCount());
        
        int i = 0;
        for(Map<String, String> expectedOperator :
            expectedListOperator.getRows()){
            Assert.assertEquals("Compare the Operator's Number of Operator" + i,
                    String.valueOf(expectedOperator.get("operatorno")),
                    String.valueOf(actualListOperator.getValue(i, "operatorno")).trim());
            Assert.assertEquals(
                    "Compare the Operator's Passcode of Operator" + i, 
                    String.valueOf(expectedOperator.get("passcode")),
                    String.valueOf(actualListOperator.getValue(i, "passcode")));
            Assert.assertEquals(
                    "Compare the Operator's TerminalID of Operator" + i, 
                    String.valueOf(expectedOperator.get("terminalid")),
                    String.valueOf(actualListOperator.getValue(i, "terminalid")).trim());
            i++;
        }    
    }
}
