package ncr.res.mobilepos.promotion.helper.test;

import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletContext;

import junit.framework.Assert;

import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.promotion.helper.TerminalItem;
import ncr.res.mobilepos.promotion.helper.TerminalItemsHandler;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class TerminalItemsHandlerSteps extends Steps {
    ServletContext servletContext;
    HashMap<String, TerminalItem> terminalItems = new HashMap<String, TerminalItem>();

    @BeforeScenario
    public final void setUpClass() throws Exception {
        Requirements.SetUp();
    }

    @AfterScenario
    public final void TearDownClass() {
        Requirements.TearDown();
    }

    @Given("an empty terminalitems")
    public final void givenEmptyList() {
        terminalItems = new HashMap<String, TerminalItem>();
    }

    @Then("I should get a list terminalitems of the following: $exampleTables")
    public final void shouldHaveTerminalItems(ExamplesTable exampleTables) {
        for (Map<String, String> item : exampleTables.getRows()) {
            String id = item.get("id");
            String sequenceno = item.get("sequenceno");
            Assert.assertEquals(id, terminalItems.get(id).getId());
            Assert.assertEquals(sequenceno, terminalItems.get(id)
                    .getSequenceNumber());
        }
    }

    @Then("I should get {$cnt} list of terminalitems")
    public final void testList(String expectedSize) {
        Assert.assertEquals(Integer.parseInt(expectedSize),
                terminalItems.size());
    }

    @When("I add retailstoreid{$storeid}, workstationid{$workstationid}, sequenceno{$seqNo}")
    public final void addObject(String retailStoreId, String workStationId,
            String sequenceNo) {
        TerminalItemsHandler.add(retailStoreId, workStationId, sequenceNo,
                terminalItems);
    }

    private TerminalItem terminalItem = null;

    @When("I get retailstoreid{$storeid}, workstationid{$workstationid}, sequenceno{$seqNo}")
    public final void getObject(String retailStoreId, String workStationId,
            String sequenceNo) {
        terminalItem = TerminalItemsHandler.get(retailStoreId, workStationId,
                sequenceNo, terminalItems);
    }

    @When("I delete retailstoreid{$storeid}, workstationid{$workstationid}")
    public final void deleteObject(String retailStoreId, String workStationId) {
        TerminalItemsHandler
                .delete(retailStoreId, workStationId, terminalItems);
    }

    @Then("I should get {$expected} terminalitem with the following details: $exampleTables")
    public final void testGet(String expected, ExamplesTable exampleTables) {
        if (expected.equalsIgnoreCase("null")) {
            Assert.assertNull(terminalItem);
        } else if (expected.equalsIgnoreCase("notnull")) {
            Assert.assertNotNull(terminalItem);
        }

        for (Map<String, String> item : exampleTables.getRows()) {
            String expctdId = item.get("id").equalsIgnoreCase("null") ? null
                    : item.get("id");
            String expctdSeqNo = item.get("sequenceno")
                    .equalsIgnoreCase("null") ? null : item.get("sequenceno");

            String actlId = terminalItem != null ? terminalItem.getId() : null;
            String actlSeqNo = terminalItem != null ? terminalItem
                    .getSequenceNumber() : null;

            Assert.assertEquals(expctdId, actlId);
            Assert.assertEquals(expctdSeqNo, actlSeqNo);
        }
    }

}
