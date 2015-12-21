package ncr.res.mobilepos.queuesignature.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.util.Map;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.queuesignature.model.SignatureRequestList;
import ncr.res.mobilepos.queuesignature.model.Transaction;
import ncr.res.mobilepos.queuesignature.resource.QueueSignatureResource;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class QueueSignatureListRequestSteps extends Steps{
    private QueueSignatureResource queueSignature;
    private SignatureRequestList requestList;
    private DBInitiator dbInit;

    @BeforeScenario
    public final void SetUpClass()
    {
        Requirements.SetUp();
        GlobalConstant.setCorpid("1111");
        dbInit = new DBInitiator("ReportResourceTest", DATABASE.RESTransaction);
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
        DBInitiator db = new DBInitiator("Master", DATABASE.RESMaster);
        db.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/queuesignature/resource/test/"
                + table + ".xml");
    }

    @Given("sample data {$table} data in database Transaction")
    public final void intializeTableTransaction(final String table) throws Exception{
        DBInitiator db = new DBInitiator("Transaction", DATABASE.RESTransaction);
        db.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/queuesignature/resource/test/"
                + table + ".xml");
    }

    @Then("resultcode should be {$result}")
    public final void checkResult(final int result){
        assertEquals(requestList.getNCRWSSResultCode(), result);
    }

    @Then("the queue id should be {$queueid}")
    public final void checkQueueId(final String queueid){
        assertEquals(requestList.getQueue(), queueid);
    }

    @Then("the transaction list should be:$exampleTable")
    public final void checkTransactionList(final ExamplesTable exampleTable){
        assertEquals(requestList.getTransactionList().size(),
                exampleTable.getRowCount());
        Transaction tx = null;
        int i = 0;
        //Test the CA Response Values
        for (Map<String, String> txSampleRow : exampleTable.getRows()) {
            tx = requestList.getTransactionList().get(i++);
            assertThat("Compare Signature Transaction WorkstationID",
                    tx.getWorkstationID(),
                    is(equalTo(txSampleRow.get("WorkstationID"))));
            assertThat("Compare Signature Transaction SequenceNumber",
                    tx.getSequenceNumber(),
                    is(equalTo(txSampleRow.get("SequenceNumber"))));
            assertThat("Compare Signature Transaction Total",
                    "" + tx.getTotal(),
                    is(equalTo(txSampleRow.get("Total"))));
        }
    }

    @When ("querying data for storeid{$storeid} and queue{$queue}  and txdate {$txdate}")
    public final void queryData(final String storeid, final String queue, final String txdate)
    throws Exception {
        requestList = queueSignature.getSignatureRequestList(storeid, queue, txdate);
    }

    @Then("xml should be {$xml}")
    public final void printXml(final String xml) throws Exception{
        XmlSerializer<SignatureRequestList> posLogRespSrlzr =
            new XmlSerializer<SignatureRequestList>();
        String actual = posLogRespSrlzr.marshallObj(SignatureRequestList.class,
                requestList, "UTF-8");
        System.out.println(actual);
        assertThat("Compare the XML generation", actual, is(equalTo(xml)));
    }

}
