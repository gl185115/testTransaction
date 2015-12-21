package ncr.res.ue.message;

import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.Assert.*;

public class MessageTestSteps extends Steps{
    String header = "";
    @When("I create transport header from messageData{$message}, termId{$termId}, transactionId{$txId}")
    public void createHeader(String message, String termId, String txId) throws Exception {
        header = TransportHeader.create(message + "\n\r", termId, txId);
    }
    @Then("transport header should be {$header}")
    public void validateTransportHeader(String header){
        assertThat(header, is(equalTo(this.header)));
    }
}
