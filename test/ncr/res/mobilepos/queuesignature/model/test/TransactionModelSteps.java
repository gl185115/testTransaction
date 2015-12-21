package ncr.res.mobilepos.queuesignature.model.test;

import static org.junit.Assert.assertEquals;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.pricing.model.Department;
import ncr.res.mobilepos.queuesignature.model.Transaction;

import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.steps.Steps;

public class TransactionModelSteps extends Steps {
    private Transaction tx = null;
    @Given("a transaction model with WorkstationId{$WorkstationId},"
            + " SequenceNumber{$SequenceNumber}, Total{$Total}")
    public final void createObject(final String WorkstationId,
            final String SequenceNumber, final double Total){
        tx = new Transaction();
        tx.setSequenceNumber(SequenceNumber);
        tx.setTotal(Total);
        tx.setWorkstationID(WorkstationId);
    }
    
    @Then("xml string should be {$xml}")
    public final void validateXml(final String xml) throws Exception{
        XmlSerializer<Transaction> posLogRespSrlzr =
            new XmlSerializer<Transaction>();
        String actual = posLogRespSrlzr.marshallObj(
                Transaction.class, tx, "UTF-8");
        System.out.println(actual);
        assertEquals(xml, actual);
    }
}
