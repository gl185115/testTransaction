package ncr.res.mobilepos.creditauthagency.dao.test;

import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.creditauthagency.dao.IExtCARequestDAO;
import ncr.res.mobilepos.creditauthagency.dao.SQLServerExtCARequestDAO;
import ncr.res.mobilepos.creditauthorization.model.CreditAuthorizationResp;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

import static org.junit.Assert.assertThat;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;

@SuppressWarnings("deprecation")
public class CreditauthagencyDaoSteps extends Steps {
    private IExtCARequestDAO testIExtCARequestDAO;
    private List<CreditAuthorizationResp> creditResps;

    @BeforeScenario
    public void SetUpClass()
    {        
        Requirements.SetUp();
    }
    
    @AfterScenario
    public void TearDownClass(){
        Requirements.TearDown();
    }
    
    @Given("a ca request transaction")
    public void insertToDatabase(){
        new DBInitiator("Txl_External_Ca_Req",
                "test/ncr/res/mobilepos/creditauthagency/"
                + "dao/test/TXL_EXTERNAL_CA_REQ.xml", DATABASE.RESTransaction);
        new DBInitiator("Mst_User_Credentials",
                "test/ncr/res/mobilepos/creditauthagency/"
                + "dao/test/MST_USER_CREDENTIALS.xml", DATABASE.RESMaster);
    }
    
    @Given("an instance of SQLServerCreditAuthAgencyDAO")
    public void creditAuthAgencyDAOInstance()
    {        
        try {
            testIExtCARequestDAO = new SQLServerExtCARequestDAO();
        } catch (DaoException e) {
            Assert.fail("SQLServerExtCARequestDAO failed"
                    + " to be initialized in CreditauthagencyDaoSteps");
        }
    }
    
    @When("a Credit Authorization Search has been performed with details"
            + " of storeid{$storeid} terminalid{$termid} businessdate{$txdate}")
    public void ACreditAuthorizationSearchHasBeenPerformedWithDetails(
            String storeid, String termid, String txdate){
        try {
            creditResps =
                testIExtCARequestDAO.getExtCARequest(storeid, termid, txdate);
        } catch (Exception e) {
            Assert.fail("A Search of list of Credit Authorization fails.");
        }
    }
    
    @Then("the following Credit Authorization XML is displayed: $caRequests")
    public void TheFollowingCreditAuthgorizationXMLIsDisplayed(
            ExamplesTable caRequests){        
        int i = 0;
        CreditAuthorizationResp caResp = null;
        
        assertThat("The Number of CA Responses must be equal",
                caRequests.getRowCount(), is(equalTo(creditResps.size())));
        
        //Test the CA Response Values
        for (Map<String, String> caRequest : caRequests.getRows()) {
            caResp = creditResps.get(i++);
            assertThat("Compare CA Response AMOUNT", caResp.getAmount(),
                    is(equalTo(caRequest.get("amount"))));
            assertThat("Compare CA Response CORPID", caResp.getCorpid(),
                    is(equalTo(caRequest.get("corpid"))));
            assertThat("Compare CA Response STATUS", caResp.getStatus(),
                    is(equalTo(caRequest.get("status"))));
            assertThat("Compare CA Response STOREID", caResp.getStoreid(),
                    is(equalTo(caRequest.get("storeid"))));
            assertThat("Compare CA Response TERMINALID", caResp.getTerminalid(),
                    is(equalTo(caRequest.get("posterminalid"))));
            assertThat("Compare CA Response SLIPNUMBER", caResp.getTillid(),
                    is(equalTo(caRequest.get("slipnumber"))));
            assertThat("Compare CA Response TXID", caResp.getTxid(),
                    is(equalTo(caRequest.get("txid"))));
        }
    }
}
