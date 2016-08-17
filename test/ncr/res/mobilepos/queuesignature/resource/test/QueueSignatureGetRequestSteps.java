package ncr.res.mobilepos.queuesignature.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.queuesignature.model.CAInfo;
import ncr.res.mobilepos.queuesignature.model.SignatureRequestBill;
import ncr.res.mobilepos.queuesignature.resource.QueueSignatureResource;

import org.dbunit.DatabaseUnitException;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class QueueSignatureGetRequestSteps extends Steps{
    private QueueSignatureResource queueSignature;
    private SignatureRequestBill requestBill;
    private DBInitiator dbInit;

    @BeforeScenario
    public final void SetUpClass()
    throws DatabaseUnitException, SQLException, Exception
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

        //Update all txdate with "2012-12-12" to current date in yyyy-MM-dd format
        replacements.put("2012-12-12", "2012-12-12");
        
        DBInitiator db = new DBInitiator("RESMaster", DATABASE.RESMaster);
        db.executeWithReplacement(
                "test/ncr/res/mobilepos/queuesignature/resource/test/"
                + table + ".xml", replacements);
    }

    @Given("sample data {$table} data in database Transaction")
    public final void intializeTableTransaction(final String table) throws Exception{
        Map<String, Object> replacements = new HashMap<String, Object>();

        //Update all txdate with "2012-12-12" to current date in yyyy-MM-dd format
        replacements.put("2012-12-12", "2012-12-12");
        
        DBInitiator db = new DBInitiator("RESMaster", DATABASE.RESTransaction);
        db.executeWithReplacement(
                "test/ncr/res/mobilepos/queuesignature/resource/test/"
                + table + ".xml", replacements);
    }

    @Then("resultcode should be {$result}")
    public final void checkResult(final int result){
        assertEquals(requestBill.getNCRWSSResultCode(), result);
    }

    @Then("workstationID should be {$result}")
    public final void checkWorkStationId(final String result){
        assertEquals(requestBill.getWorkstationID(), result);
    }

    @Then("retailStoreId should be {$result}")
    public final void checkRetailStoreId(final String result){
        assertEquals(requestBill.getRetailStoreID(), result);
    }

    @Then("sequenceNumber should be {$result}")
    public final void checkSequenceNumber(final String result){
        assertEquals(requestBill.getSequenceNumber(), result);
    }

    @Then("queueId should be {$result}")
    public final void checkQueueId(final String result){
        assertEquals(requestBill.getQueue(), result);
    }

    @Then("CAInfo should be:$exampletable")
    public final void checkCaInfo(final ExamplesTable exampleTable){
        CAInfo cainfo = requestBill.getCAInfo();

        Map<String, String> expected = exampleTable.getRow(0);
        assertThat("Compare Signature Transaction CAInfo ApprovalNo",
                cainfo.getApprovalNo(),
                is(equalTo(expected.get("ApprovalNo"))));
        assertThat("Compare Signature Transaction CAInfo TimeOfUse",
                cainfo.getTimeOfUse(), is(equalTo(expected.get("TimeOfUse"))));
        assertThat("Compare Signature Transaction CAInfo ShopName",
                cainfo.getShopName(), is(equalTo(expected.get("ShopName"))));
        assertThat("Compare Signature Transaction CAInfo SlipNo",
                "" + cainfo.getSlipNo(), is(equalTo(expected.get("SlipNo"))));
        assertThat("Compare Signature Transaction CAInfo CardNo",
                cainfo.getCardNo(), is(equalTo(expected.get("CardNo"))));
        assertThat("Compare Signature Transaction CAInfo ExpireDate",
                cainfo.getExpireDate(),
                is(equalTo(expected.get("ExpireDate"))));
        assertThat("Compare Signature Transaction CAInfo BusinessDivision",
                cainfo.getBusinessDivision(),
                is(equalTo(expected.get("BusinessDivision"))));
        assertThat("Compare Signature Transaction CAInfo MerchandiseCode",
                cainfo.getMerchandiseCode(),
                is(equalTo(expected.get("MerchandiseCode"))));
        assertThat("Compare Signature Transaction CAInfo CardCompanyCode",
                cainfo.getCardCompanyCode(),
                is(equalTo(expected.get("CardCompanyCode"))));
        assertThat("Compare Signature Transaction CAInfo CardCompanyName",
                cainfo.getCardCompanyName(),
                is(equalTo(expected.get("CardCompanyName"))));
        assertThat("Compare Signature Transaction CAInfo PaymentDivision",
                "" + cainfo.getPaymentDivision(),
                is(equalTo(expected.get("PaymentDivision"))));
        assertThat("Compare Signature Transaction CAInfo InstallmentAmount",
                "" + cainfo.getInstallmentAmount(),
                is(equalTo(expected.get("InstallmentAmount"))));
        assertThat("Compare Signature Transaction CAInfo NumberOfBonuses",
                "" + cainfo.getNumberOfBonuses(),
                is(equalTo(expected.get("NumberOfBonuses"))));
        assertThat("Compare Signature Transaction CAInfo Sum",
                "" + cainfo.getSum(), is(equalTo(expected.get("Sum"))));
        assertThat("Compare Signature Transaction CAInfo TaxAndPostage",
                "" + cainfo.getTaxAndPostage(),
                is(equalTo(expected.get("TaxAndPostage"))));
        assertThat("Compare Signature Transaction CAInfo RomanName",
                cainfo.getRomanName(), is(equalTo(expected.get("RomanName"))));
        assertThat("Compare Signature Transaction CAInfo Sign",
                cainfo.getSign(), is(equalTo(expected.get("Sign"))));
    }

    @When ("I get a signature request data with storeid{$storeid},"
            + " queue{$queue}, workstationid{$termid}, sequencenumber{$seqnum},"
            + " businessdate{$businessdate}")
    public final void get(final String storeid, final String queue,
            final String termid, final String seqnum, final String businessdate)
    {
        requestBill = queueSignature.getSignatureRequest(
                storeid, queue, termid, seqnum, businessdate);
    }
}
