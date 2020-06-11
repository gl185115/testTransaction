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
import ncr.res.mobilepos.helper.DataBinding;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.jbehave.core.annotations.AfterScenario;
import org.jbehave.core.annotations.BeforeScenario;
import org.jbehave.core.annotations.Given;
import org.jbehave.core.annotations.Then;
import org.jbehave.core.annotations.When;
import org.jbehave.core.model.ExamplesTable;
import org.jbehave.core.steps.Steps;

import static org.mockito.Mockito.*;

/**
 * Steps class for operator's changePasscode.
 */
@SuppressWarnings("deprecation")
public class SignOnSteps extends Steps {
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
     * A Given step, initializes new dataset.
     *
     * @param filename
     *            the filename
     */
    @Given("an initial data from $filename")
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

    @When("signing on companyid $companyId operator $operatorNo passcode $passcode terminal $terminalId isdemo $isDemoParam")
	public final void signOn(final String companyId, final String operatorNo, final String passcode, final String terminalId, final String isDemoParam) {
		boolean isDemo = isDemoParam.equals("true") ? true : false;
		try {
			resultBase = operatorResult = credResource.requestSignOn(
					operatorNo, companyId, passcode, terminalId, isDemo);
		} catch (Exception e) {
			e.printStackTrace();
			Assert.fail("Can't Mock Calendar for SignOn");
		}
	}

    /**
     * A Then step, tests actual and expect result code.
     *
     * @param result
     *            The expected result code.
     */
    @Then("I should get resultcode $Result")
    public final void checkResult(final int result) {
        assertThat(resultBase.getNCRWSSResultCode(), is(equalTo(result)));
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

    @Then("the Operator should have the following XML: $expectedXml")
    public final void theOperatorShouldHaveTheFollowingXML(final String expectedXml){
        try {
            if (operatorResult.getNCRWSSResultCode() == 0) {
               operatorResult.setSignOnAt("21:05");
               operatorResult.setDate("Nov 22, '12");
            }
            DataBinding<Operator> operatorSerializer = new DataBinding<Operator>(Operator.class);
            String actualXml = operatorSerializer.marshallObj(operatorResult, "UTF-8");
            Assert.assertEquals("Expect the Serialize Operator", expectedXml, actualXml);
        } catch (JAXBException e) {
            e.printStackTrace();
            Assert.fail("Can not perform marshalling to Operator Object.");
        }
    }
}
