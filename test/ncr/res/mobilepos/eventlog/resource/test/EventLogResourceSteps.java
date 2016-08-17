package ncr.res.mobilepos.eventlog.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertEquals;

import java.util.Map;

import junit.framework.Assert;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.eventlog.resource.EventLogResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.POSLogHandler;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

public class EventLogResourceSteps extends Steps {
    DBInitiator dbInitTransaction;
    EventLogResource resource;
    ResultBase result;
    String resultJson;

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        result = null;
        resultJson = null;
    }

    @Given("a EventLog Resource")
    public void createResource() {
        resource = new EventLogResource();
    }

    @Given("a RESTransaction DBInitiator")
    public final void createDBInitiatorRESTransaction() {
        dbInitTransaction = new DBInitiator("RESTransaction", DATABASE.RESTransaction);
    }

    @Given("TXL_OPE_EVENT is empty")
    public void emptyDatabase() throws Exception {
    	dbInitTransaction.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/eventlog/resource/datasets/"
    			+ "TXL_OPE_EVENT.xml");
    }

    @Given("TXL_OPE_EVENT continas multiple rows")
    public void havingRowDatabase() throws Exception {
    	dbInitTransaction.ExecuteOperation(DatabaseOperation.CLEAN_INSERT, 
    			"test/ncr/res/mobilepos/eventlog/resource/datasets/"
    			+ "TXL_OPE_EVENT_WITH_ROWS.xml");
    }

    @When("executing log with company{$company} storeid{$storeid} workstationid{$workstationid} sequenceNumber{$sequenceNumber} businessDayDate{$bizdate} training{$training} and log{$log}")
    public void executeLog(String company, String storeid, String workstationid, int sequenceNumber,
                           String bizdate, int training, String log) throws Exception {
        result = resource.log(company, storeid, workstationid, training, sequenceNumber, bizdate, log);
    }

    @When("executing log with company{$company} storeid{$storeid} workstationid{$workstationid} sequenceNumber{$sequenceNumber} childNumber{$child} businessDayDate{$bizdate} training{$training} and log{$log}")
    public void executeLog(String company, String storeid, String workstationid, int sequenceNumber,
                           int child, String bizdate, int training, String log) throws Exception {
        result = resource.log(company, storeid, workstationid, training, sequenceNumber, child, bizdate, log);
    }

    @When("read log with company{$company} storeid{$storeid} workstationid{$workstationid} sequenceNumber{$sequenceNumber} training{$training}")
    public void readLog(String company, String storeid, String workstationid, int sequenceNumber,
                        int training) throws Exception {
        resultJson = resource.read(company, storeid, workstationid, training, sequenceNumber);
    }

    @Then("result json should be {$json}")
    public void checkResultJson(String json) throws Exception {
        assertEquals("json", json, resultJson);
    }

    @Then("result should be {$status}")
    public void checkResultBaseStatus(int status) throws Exception {
        assertEquals(status, result.getNCRWSSResultCode());
    }

    static String[] ROWNAMES = {
        "CompanyId",
        "RetailStoreId",
        "WorkstationId",
        "SequenceNumber",
        "ChildNumber",
        "BusinessDayDate",
        "TrainingFlag",
        "Log"
    };

    @Then("$tableName should have the following row(s): $data")
    public void showTableValues(String tableName, ExamplesTable expected) throws Exception {
        ITable actual = dbInitTransaction.getTableSnapshot(tableName);
        assertEquals("Compare the number of rows in " + tableName, 
                     expected.getRowCount(), actual.getRowCount());
        int row = 0;
        for (Map<String, String> i : expected.getRows()) {
            for (String name : ROWNAMES) {
                assertEquals("Compare " + name + "(" + row + ")",
                             i.get(name), String.valueOf(actual.getValue(row, name)).trim());
            }
            row++;
        }
    }

}

