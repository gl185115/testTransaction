package ncr.res.mobilepos.queuesignature.resource.test;

import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.queuesignature.resource.QueueSignatureResource;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class QueueSignatureUpdateRequestSteps extends Steps {
    private QueueSignatureResource queueSignature;
    private ResultBase resbase;
    private DBInitiator dbInit;

    @BeforeScenario
    public final void SetUpClass()
    {
        Requirements.SetUp();
        GlobalConstant.setCorpid("1111");
        dbInit = new DBInitiator("ReportResourceTest",
        "test/ncr/res/mobilepos/queuesignature/resource/"
                + "test/TXL_EXTERNAL_CA_REQ.xml", DATABASE.RESTransaction);
    }

    @AfterScenario
    public final void TearDownClass(){
        Requirements.TearDown();
        GlobalConstant.setCorpid("");
    }

    @Given("a QueueSignature resource")
    public final void createResource(){
        queueSignature = new QueueSignatureResource();
    }

    @Given("sample data {$table} data in database Master")
    public final void intializeTableMaster(final String table) throws Exception{
        Map<String, Object> replacements = new HashMap<String, Object>();

        //Update all txdate with "2012-12-12" to current date
        replacements.put("2012-12-12", "2012-12-12");
        
        DBInitiator db = new DBInitiator("Master", DATABASE.RESMaster);
        db.executeWithReplacement(
        "test/ncr/res/mobilepos/queuesignature/resource/test/"
                + table + ".xml", replacements);
    }

    @Given("sample data {$table} data in database Transaction")
    public final void intializeTableTransaction(final String table) throws Exception{
        Map<String, Object> replacements = new HashMap<String, Object>();

        //Update all txdate with "2012-12-12" to current date
        replacements.put("2012-12-12", "2012-12-12");
        
        DBInitiator db = new DBInitiator("Transaction", DATABASE.RESTransaction);
        db.executeWithReplacement(
        "test/ncr/res/mobilepos/queuesignature/resource/test/"
                + table + ".xml", replacements);
    }

    @When ("I update signature data storeid{$storeid}, queue{$queue},"
            + " workstationid{$workid}, sequencenumber{$seqnum}, businessdate{$businessdate}"
            + " with status{$stat}")
    public final void update(final String storeid,
            final String queue, final String workid, final String seqnum, final String businessdate, final String status){
        resbase = queueSignature.updateSignatureRequestStatus(
                storeid, queue, workid, seqnum, businessdate, status);
    }

    @Then ("resultcode should be {$result}")
    public final void checkresult(final int result){
        assertEquals(resbase.getNCRWSSResultCode(), result);
    }
}
