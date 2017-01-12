package ncr.res.mobilepos.userlogin.test;

import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import junit.framework.Assert;
import ncr.res.mobilepos.credential.model.Operator;
import ncr.res.mobilepos.credential.resource.CredentialResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.tillinfo.model.ViewTill;
import ncr.res.mobilepos.tillinfo.resource.TillInfoResource;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

import static org.mockito.Mockito.*;

/**
 * Steps class for component testing of operator signon.
 */
@SuppressWarnings("deprecation")
public class UserLoginSteps extends Steps {
	private CredentialResource credentialResource;
	private TillInfoResource tillInfoResource;
	private Operator userInfo;
	private Operator operatorSecStatus;
	private DBInitiator dbinit = null;
	private String companyId, storeId, terminalId;
	private ViewTill tillInfo;
	/**
     * Invokes before execution of each scenario.
     */
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        mock(Calendar.class);
        initResources();
    }

    /**
     * Invokes after execution of each scenario.
     */
    @AfterScenario
    public final void tearDown() {
        Requirements.TearDown();
    }

    public final void initResources() {
        ServletContext context = Requirements.getMockServletContext();
        credentialResource = new CredentialResource();
        tillInfoResource = new TillInfoResource();
        try {
            Field credentialContext = credentialResource.getClass().getDeclaredField("context");
            credentialContext.setAccessible(true);
            credentialContext.set(credentialResource, context);
            Field tillContext = tillInfoResource.getClass().getDeclaredField("servletContext");
            tillContext.setAccessible(true);
            tillContext.set(tillInfoResource, context);            
        } catch (Exception e) {
            e.printStackTrace();
            Assert.fail("Cant load Mock Servlet Context.");
        } 
    }

    /**
     * Initialize user tables.
     *
     * @param filename the xml file.
     */
    @Given("an initial data from {$filename}")
    public final void givenInitialData(final String filename) {
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
			dbinit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/userlogin/test/" + filename);
		} catch (Exception e) {
			e.printStackTrace();
		}
    }
    
    /**
     * Initialize company store and terminal.
     * @param companyId
     * @param storeId
     * @param terminalId
     */
    @Given("a companyid{$companyid} storeid{$storeid} terminalid{$terminalid}")
    public final void givenTheFollowing(final String companyId, final String storeId, final String terminalId){
    	this.companyId = companyId;
    	this.storeId = storeId;
    	this.terminalId = terminalId;
    }

    /**
     * Start SOD.
     * @param filename
     */
    @Given("that SOD has started {$filename}")
    public final void givenSOD(final String filename) {
    	try {
			dbinit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
					"test/ncr/res/mobilepos/userlogin/test/" + filename);
		} catch (Exception e) {
			e.printStackTrace();
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
	@When("I login with operator{$operatorNo} passcode{$passcode} demo{$isDemoParam}")
	public final void signOn(final String operatorNo, final String passcode, final String isDemoParam) {
		try {
			boolean isDemo = isDemoParam.equals("true") ? true: false;
			userInfo = credentialResource.requestSignOn(operatorNo, companyId,
					passcode, terminalId, isDemo);
			operatorSecStatus = credentialResource.getStatusOfOperator(companyId, operatorNo);
			tillInfo = tillInfoResource.viewTill(storeId, storeId.concat(terminalId));
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Can't Mock Calendar for SignOn");
		}
	}
    
	/**
	 * Check user information result.
	 * @param expected
	 */
	@Then("I should get the following operator details: $expected")
    public final void thenIShouldGetOperatorDetails(ExamplesTable expected) {
        Map<String, String> expectedData = expected.getRow(0);
		for (int i = 0; i < expected.getRowCount(); i++) {
			Assert.assertEquals("ncrwssresultcode", Integer.parseInt(expectedData.get("ncrwssresultcode")),
					userInfo.getNCRWSSResultCode());
			if(Integer.parseInt(expectedData.get("ncrwssresultcode")) == 0) {
				Assert.assertEquals("operatorname", expectedData.get("operatorname").trim().equals("null") ? null: expectedData.get("operatorname").trim(),
						userInfo.getName());
				Assert.assertEquals("operatorkananame", expectedData.get("operatorkananame").trim().equals("null") ? null: expectedData.get("operatorkananame").trim(),
						userInfo.getOpeNameKana());
				Assert.assertEquals("operatortype", Integer.parseInt(expectedData.get("operatortype")),
						userInfo.getOperatorType());
				Assert.assertEquals("isTransactions", Boolean .parseBoolean(expectedData.get("isTransactions")),
						userInfo.getPermissions().isTransactions());
				Assert.assertEquals("isReports", Boolean.parseBoolean(expectedData.get("isReports")),
						userInfo.getPermissions().isReports());
				Assert.assertEquals("isSettings", Boolean.parseBoolean(expectedData.get("isSettings")),
						userInfo.getPermissions().isSettings());
				Assert.assertEquals("isMerchandise", Boolean.parseBoolean(expectedData.get("isMerchandise")),
						userInfo.getPermissions().isMerchandise());
				Assert.assertEquals("isAdministration", Boolean.parseBoolean(expectedData.get("isAdministration")),
						userInfo.getPermissions().isAdministration());
				Assert.assertEquals("isDrawer", Boolean.parseBoolean(expectedData.get("isDrawer")),
						userInfo.getPermissions().isDrawer());
			}
		}
    }
	
	/**
	 * Check user security status.
	 * @param expected
	 */
	@Then("I should get the following operator security status: $expected")
    public final void thenIShouldGetOperatorSecStatus(ExamplesTable expected) {
        Map<String, String> expectedData = expected.getRow(0);
        for (int i = 0; i < expected.getRowCount(); i++) {
			Assert.assertEquals("ncrwssresultcode", Integer.parseInt(expectedData.get("ncrwssresultcode")),
					operatorSecStatus.getNCRWSSResultCode());
			Assert.assertEquals("securitylevel", expectedData.get("securitylevel").trim(),
					operatorSecStatus.getSecuritylevel());
			Assert.assertEquals("isSecLevel1", Boolean.parseBoolean(expectedData.get("isSecLevel1")),
					operatorSecStatus.getAuthorization().isSecLevel1());
			Assert.assertEquals("isSecLevel2", Boolean.parseBoolean(expectedData.get("isSecLevel2")),
					operatorSecStatus.getAuthorization().isSecLevel2());
			Assert.assertEquals("isSecLevel3", Boolean.parseBoolean(expectedData.get("isSecLevel3")),
					operatorSecStatus.getAuthorization().isSecLevel3());
			Assert.assertEquals("isSecLevel4", Boolean.parseBoolean(expectedData.get("isSecLevel4")),
					operatorSecStatus.getAuthorization().isSecLevel4());
			Assert.assertEquals("isSecLevel5", Boolean.parseBoolean(expectedData.get("isSecLevel5")),
					operatorSecStatus.getAuthorization().isSecLevel5());
			Assert.assertEquals("isSecLevel6", Boolean.parseBoolean(expectedData.get("isSecLevel6")),
					operatorSecStatus.getAuthorization().isSecLevel6());
			Assert.assertEquals("isSecLevel7", Boolean.parseBoolean(expectedData.get("isSecLevel7")),
					operatorSecStatus.getAuthorization().isSecLevel7());
			Assert.assertEquals("isSecLevel8", Boolean.parseBoolean(expectedData.get("isSecLevel8")),
					operatorSecStatus.getAuthorization().isSecLevel8());
			Assert.assertEquals("isSecLeve9", Boolean.parseBoolean(expectedData.get("isSecLevel9")),
					operatorSecStatus.getAuthorization().isSecLevel9());
			Assert.assertEquals("isSecLevel10", Boolean.parseBoolean(expectedData.get("isSecLevel10")),
					operatorSecStatus.getAuthorization().isSecLevel10());
		}
    }
	
	/**
	 * Check drawer or till details.
	 * @param expected
	 */
	@Then("I should get the following till details: $expected")
    public final void thenIShouldGetTillDetails(ExamplesTable expected) {
        Map<String, String> expectedData = expected.getRow(0);        
        for (int i = 0; i < expected.getRowCount(); i++) {
			Assert.assertEquals("ncrwssresultcode", Integer.parseInt(expectedData.get("ncrwssresultcode")),
					tillInfo.getNCRWSSResultCode());
			Assert.assertEquals("businessdaydate", expectedData.get("businessdaydate").trim(),
					tillInfo.getTill().getBusinessDayDate());
			Assert.assertEquals("sodflag", expectedData.get("sodflag").trim(),
					tillInfo.getTill().getSodFlag());
			Assert.assertEquals("eodflag", expectedData.get("eodflag").trim(),
					tillInfo.getTill().getEodFlag());
		}
    }
	
}
