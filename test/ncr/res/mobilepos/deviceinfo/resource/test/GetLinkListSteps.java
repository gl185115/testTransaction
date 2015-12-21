package ncr.res.mobilepos.deviceinfo.resource.test;

import java.io.IOException;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.model.POSLinks;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;

public class GetLinkListSteps extends Steps{
    DeviceInfoResource deviceInfoRes;
    POSLinks poslinks;
    int expectedResultCode;
    private String datasetPath = "test/ncr/res/mobilepos/deviceinfo/datasets/";
    private DBInitiator dbInit = null;
    private POSLinks queuebusterlinks = null;

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }
    @AfterScenario
    public final void TearDownClass() {
        Requirements.TearDown();
    }

    @Given("a $fileName database table")
    public final void givenADataset(String fileName){
        dbInit = new DBInitiator("Mst_DeviceInfo", datasetPath + fileName + ".xml", DATABASE.RESMaster);
    }
    
    @Given("I have DeviceInfo resource")
    public final void IHaveItemResource() {
        deviceInfoRes = new DeviceInfoResource();
        deviceInfoRes.setContext(Requirements.getMockServletContext());
        Assert.assertNotNull(deviceInfoRes);
        GlobalConstant.setMaxSearchResults(2);
    }



    @When("I get $linktype Link Items with StoreID{$storeid}, Key{$key}, Name{$name}, Limit{$limit}")
    public final void IgetQueueBusterLinkItem(final String linkType, final String storeid, final String key, final String name, final int limit) {
        poslinks = new POSLinks();
        String storeIDTemp = storeid.equalsIgnoreCase("null") ? null : storeid;
        String linkTypeTemp = "null".equals(linkType) ? null : linkType;
        poslinks = deviceInfoRes.getLinksList(linkTypeTemp, storeIDTemp, key, name, limit);
    }

    @Then("I should get POSLinks in JSON format: $posLinksJson")
    public final void testListResult(String expectedPOSLinksJson) {
        JsonMarshaller<POSLinks> jsonM = new JsonMarshaller<POSLinks>();
        try {
            String actualPOSLinksJson = jsonM.marshall(poslinks);
            String resCode = "NCRWSSResultCode\":"+ poslinks.getNCRWSSResultCode(); 
            if(actualPOSLinksJson.contains(resCode)){
            	actualPOSLinksJson = actualPOSLinksJson.replaceAll(resCode, "");
            	actualPOSLinksJson = actualPOSLinksJson.replaceAll("NCRWSSExtendedResultCode\":0", "");
            }
            if(expectedPOSLinksJson.contains(resCode)){
            	expectedPOSLinksJson = expectedPOSLinksJson.replaceAll(resCode, "");
            	expectedPOSLinksJson = expectedPOSLinksJson.replaceAll("NCRWSSExtendedResultCode\":0", "");
            }

            Assert.assertEquals(expectedPOSLinksJson, actualPOSLinksJson);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
