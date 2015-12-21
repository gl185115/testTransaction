/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * ReportResourceSteps
 *
 * Class containing Steps for Unit Testing ReportResource Class
 *
 * jd185128
 */
package ncr.res.mobilepos.queuesignature.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.math.BigDecimal;
import java.sql.Date;
import java.util.Map;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.queuesignature.resource.QueueSignatureResource;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

import atg.taglib.json.util.JSONException;

public class QueueSignatureAddRequestSteps extends Steps {
    
    private DBInitiator dbInit = null;
    private QueueSignatureResource qsignature = null;
    private ResultBase rb = null;
    
    @BeforeScenario
    public final void SetUpClass()
    {        
        Requirements.SetUp();
        GlobalConstant.setCorpid("1111");
        dbInit = new DBInitiator("ReportResourceTest",
        "test/ncr/res/mobilepos/queuesignature/resource/test/"
                + "TXL_EXTERNAL_CA_REQ.xml", DATABASE.RESTransaction);
    }
    
    @AfterScenario
    public final void TearDownClass(){
        Requirements.TearDown();
        GlobalConstant.setCorpid("");
    }
    
    @Then("TXL_EXTERNAL_CA_REQ table values should be: $expecteditems")
    public final void  IShouldGetTheFollowingTXU_TOTAL_ITEMDAYResults(
            final ExamplesTable expecteditems) throws DataSetException{ 
        ITable actualTable = dbInit.getTableSnapshot("TXL_EXTERNAL_CA_REQ");
        
        //please base table verification with this implementation
        
        int i = 0;        
        for (Map<String, String> item : expecteditems.getRows()) {
            assertThat("TXL_EXTERNAL_CA_REQ (row"+i+" - RetailStoreId",
                    item.get("RetailStoreId").trim(), 
                    is(equalTo(actualTable.getValue(i, "RetailStoreId"))));
            assertThat("TXL_EXTERNAL_CA_REQ (row"+i+" - WorkstationId",
                    item.get("WorkstationId").trim(), 
                    is(equalTo(actualTable.getValue(i, "WorkstationId")
                            .toString().trim())));
            assertThat("TXL_EXTERNAL_CA_REQ (row"+i+" - BusinessDayDate",
                    item.get("BusinessDayDate").trim(), 
                    is(equalTo(actualTable.getValue(i, "BusinessDayDate")
                            .toString().trim())));
            assertThat("TXL_EXTERNAL_CA_REQ (row"+i+" - SequenceNumber",
                    item.get("SequenceNumber").trim(), 
                    is(equalTo(actualTable.getValue(i, "SequenceNumber")
                            .toString().trim())));
            assertThat("TXL_EXTERNAL_CA_REQ (row"+i+" - Type",
                    item.get("Type").trim(), 
                    is(equalTo(actualTable.getValue(i, "Type")
                            .toString().trim())));
            assertThat("TXL_EXTERNAL_CA_REQ (row"+i+" - TermId",
                    item.get("TermId").trim(), 
                    is(equalTo(actualTable.getValue(i, "TermId")
                            .toString().trim())));
            assertThat("TXL_EXTERNAL_CA_REQ (row"+i+" - Status",
                    item.get("Status").trim(), 
                    is(equalTo(actualTable.getValue(i, "Status")
                            .toString().trim())));
            assertThat("TXL_EXTERNAL_CA_REQ (row"+i+" - Pan",
                    item.get("Pan").trim(), 
                    is(equalTo(actualTable.getValue(i, "Pan")
                            .toString().trim())));
            assertThat("TXL_EXTERNAL_CA_REQ (row"+i+" - Amount",
                    new BigDecimal(item.get("Amount")), 
                    is(equalTo((BigDecimal)actualTable.getValue(i, "Amount"))));
            assertThat("TXL_EXTERNAL_CA_REQ (row"+i+" - CafisTraceNum",
                    item.get("CafisTraceNum").trim(), 
                    is(equalTo(actualTable.getValue(i, "CafisTraceNum")
                            .toString().trim())));
            assertThat("TXL_EXTERNAL_CA_REQ (row"+i+" - CaInfoRaw",
                    item.get("CaInfoRaw").trim(), 
                    is(equalTo(actualTable.getValue(i, "CaInfoRaw")
                            .toString().trim())));
            i++;
        }
    }
    
    @When ("I add the signature request with storeid{$storeid},"
            + " queueid{$queueid}, posterminalid{$termid}, txid{$txid},"
            + " cainfo{$cainfo}")
    public final void addsign(final String storeid,
            final String queueid, final String termid,
            final String txid, final String cainfo) throws JSONException
    {        
        rb = qsignature.addSignatureRequest(storeid, queueid,
                termid, txid, cainfo);
    }
    
    @Given ("a QueueSignature resource")
    public final void queueresource()
    {
        qsignature = new QueueSignatureResource();
    }
    
    @Then ("result shoud be {$result}")
    public final void checkresult(final int result)
    {
        assertThat(result, is(equalTo(rb.getNCRWSSResultCode())));
    }
    
    
}
