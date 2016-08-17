package ncr.res.mobilepos.forwarditemlist.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.forwarditemlist.resource.ForwardItemListResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.journalization.model.PosLogResp;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

@SuppressWarnings("deprecation")
public class ForwardItemListResourceSteps extends Steps{
    private DBInitiator dbInit = null;
    private String datasetPath = "test/ncr/res/mobilepos/forwarditemlist/";
    private ForwardItemListResource forwardedResource = null;
    private PosLogResp poslogresponse;
    private String actualForwardCountXml;
    private String actualForwardXml;

    @BeforeScenario
    public final void SetUpClass()
    {
        //By default Company ID is empty
        GlobalConstant.setCorpid("000000000000");
        Requirements.SetUp();
        dbInit = new DBInitiator("ItemForwardDataSteps", DATABASE.RESTransaction);
        forwardedResource = new ForwardItemListResource();
    }
    
    @AfterScenario
    public final void TearDownClass(){
        Requirements.TearDown();
        GlobalConstant.setCorpid("");
    }
    
    @SuppressWarnings("deprecation")
	@Given("a table entry named {$dataset} in the database")
    public final void ATableEntryNamedInTheDatabase(final String dataset){
        try {
            dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    datasetPath + dataset);
        } catch (Exception e) {
            Assert.fail("Fail to CLean Insert on TXL_FORWARD_ITEM");
        }    
    }
    
    @When("executing Upload ForwardTransactionToList"
            + " from a MobilePOS having $deviceNo and"
            + " $terminalNo with xml: $poslogxml")
    public final void executingUploadForwardTransactionWithXml(
            final String deviceNo,
            final String terminalNo, final String poslogxml){
        poslogresponse =
            forwardedResource
                .uploadForwardData(poslogxml, deviceNo, terminalNo);
    }
    
    @When("there is a request for Forwarded Item Count given that"
            + " storeid{$storeid}, terminalid{$termid}"
            + " and businessdate{$businessdate}")
    public final void thereIsARequestForForwardedItemCount(final String storeid,
            final String terminalid, final String txdate){
        actualForwardCountXml =
            forwardedResource.getCount(storeid, terminalid, txdate);
    }
    
    @When("there is a request to get a Forwarded item given that"
            + " storeid{$storeid}, terminalid{$terminalid}"
            + " and businessdate{$businessdate}")
    public final void thereIsARequestToGetAForwardedItem(final String storeid,
            final String terminalid, final String txdate){
        actualForwardXml =
            forwardedResource.requestForwardData(storeid, terminalid, txdate);
    }
    
    @Then("it have a successful POSLOg Response")
    public final void theFollowingPOSLogResponseWillBeRetrieved(){
        assertThat(poslogresponse.getStatus(), is(equalTo("0")));        
    }
    
    @Then("the Forwarded Item Count is in the following format:"
            + " $expectedForwardCountXml")
    public final void theForwardedItemCountIsInTheFollowingFormat(
            String expectedForwardCountXml) {
        
        if(expectedForwardCountXml.equals("NULL")){
            expectedForwardCountXml = null;
        }
        assertThat(actualForwardCountXml, is(equalTo(expectedForwardCountXml)));
    }
    
    @Then("I should get the Forwarded POSLOG XML: $forwardedXml")
    public final void IShouldGetTheForwardedPOSLOGXML(String forwardedXml){
        if(forwardedXml.equals("NULL")){
            forwardedXml = null;
        }
        System.out.println(actualForwardXml);
        assertThat(actualForwardXml, is(equalTo(forwardedXml)));
    }
}
