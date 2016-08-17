package ncr.res.mobilepos.deviceinfo.resource.test;

import java.io.IOException;
import java.util.Map;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.model.ViewPosLinkInfo;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.JsonMarshaller;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.dbunit.dataset.ITable;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.json.JSONException;
import org.junit.Assert;
import org.skyscreamer.jsonassert.JSONAssert;
import org.skyscreamer.jsonassert.JSONCompareMode;

public class UpdateLinkSteps extends Steps {
    private DBInitiator dbInit;
    private DeviceInfoResource deviceInfoTest;
    private ViewPosLinkInfo newPosLinkInfo;
    private String dsFilePath = "test/ncr/res/mobilepos/deviceinfo/datasets/";

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        //GlobalConstant.setCorpid("9999"); // Set corpid
    }

    @AfterScenario
    public final void TearDown() {
        Requirements.TearDown();
    }

    @Given("a $filename database table")
    public final void aDataset(String fileName){
        dbInit = new DBInitiator("prm_queuebuster", dsFilePath + fileName +".xml", DATABASE.RESMaster);
    }

    @Given("a Device Information Web Service")
    public final void aDeviceInformationWebService() {
        deviceInfoTest = new DeviceInfoResource();
    }

    @Then("the $dbTableName database table should have the following row(s): $deviceTables")
    public final void theMST_QUEUEBUSTER_LINKDatabaseTableShouldHaveTheFollowingRow(
            String dbTableName, ExamplesTable expectedDataTable) {
        try {
            ITable actualDataTable =
                dbInit.getTableSnapshot(dbTableName);
            Assert.assertEquals("Compare the number of rows in " + dbTableName,
                    expectedDataTable.getRowCount(),
                    actualDataTable.getRowCount());

            if(dbTableName.equals("PRM_QUEUEBUSTER_LINK")){
                int i = 0;
                for(Map<String, String> expectedDeviceInfo : expectedDataTable.getRows()) {
                    Assert.assertEquals("Compare the STOREID in PRM_QUEUEBUSTER_LINK row" + i,
                            expectedDeviceInfo.get("storeid"),
                            String.valueOf(actualDataTable.getValue(i, "storeid")).trim());
                    Assert.assertEquals("Compare the ID in PRM_QUEUEBUSTER_LINK row" + i,
                            expectedDeviceInfo.get("id"),
                            String.valueOf(actualDataTable.getValue(i, "id")));
                    Assert.assertEquals("Compare the DISPLAYNAME in PRM_QUEUEBUSTER_LINK row" + i,
                            expectedDeviceInfo.get("displayname"),
                            String.valueOf(actualDataTable.getValue(i, "displayname")).trim());
                    i++;
                }
            } else if(dbTableName.equals("MST_STOREINFO")){
                int i = 0;
                for(Map<String, String> expectedStoreInfo : expectedDataTable.getRows()) {
                    Assert.assertEquals("Compare the STOREID in MST_STOREINFO  row" + i,
                            expectedStoreInfo.get("storeid"),
                            String.valueOf(actualDataTable.getValue(i, "storeid")).trim());
                    Assert.assertEquals("Compare the STORENAME in MST_STOREINFO  row" + i,
                            expectedStoreInfo.get("storename"),
                            String.valueOf(actualDataTable.getValue(i, "storename")));
                    i++;
                }
            }else if(dbTableName.equals("PRM_AUTHORIZATION_LINK")){
                int i = 0;
                for(Map<String, String> expectedDeviceInfo : expectedDataTable.getRows()) {
                    Assert.assertEquals("Compare the STOREID in PRM_AUTHORIZATION_LINK row" + i,
                            expectedDeviceInfo.get("storeid"),
                            String.valueOf(actualDataTable.getValue(i, "storeid")).trim());
                    Assert.assertEquals("Compare the ID in PRM_AUTHORIZATION_LINK row" + i,
                            expectedDeviceInfo.get("id"),
                            String.valueOf(actualDataTable.getValue(i, "id")));
                    Assert.assertEquals("Compare the DISPLAYNAME in PRM_AUTHORIZATION_LINK row" + i,
                            expectedDeviceInfo.get("displayname"),
                            String.valueOf(actualDataTable.getValue(i, "displayname")).trim());
                    i++;
                }
            } else if(dbTableName.equals("PRM_SIGNATURE_LINK")) {
                int i = 0;
                for(Map<String, String> expectedDeviceInfo : expectedDataTable.getRows()) {
                    Assert.assertEquals("Compare the STOREID in PRM_SIGNATURE_LINK row" + i,
                            expectedDeviceInfo.get("storeid"),
                            String.valueOf(actualDataTable.getValue(i, "storeid")).trim());
                    Assert.assertEquals("Compare the ID in PRM_SIGNATURE_LINK row" + i,
                            expectedDeviceInfo.get("id"),
                            String.valueOf(actualDataTable.getValue(i, "id")));
                    Assert.assertEquals("Compare the DISPLAYNAME in PRM_SIGNATURE_LINK row" + i,
                            expectedDeviceInfo.get("displayname"),
                            String.valueOf(actualDataTable.getValue(i, "displayname")));
                    i++;
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in PRM_SIGNATURE_LINK.");
        }
    }

    @When("I update $linkType link data of retailstoreid:$storeID, poslinkid:$linkID, poslinkinfo:$linkInfoJson")
    public final void updateLink(String linkType, String storeID, String linkID, String linkInfoJson){
        storeID = storeID.equalsIgnoreCase("null") ? null : storeID;
        linkID = linkID.equalsIgnoreCase("null") ? null : linkID;
        linkInfoJson = linkInfoJson.equalsIgnoreCase("null") ? null : linkInfoJson;

        newPosLinkInfo = deviceInfoTest.updateLink(linkType, storeID, linkID, linkInfoJson);
    }

    @Then("I should get $type code {$code}")
    public final void testResultCode(String type, String code){
        Assert.assertEquals(code, String.valueOf(newPosLinkInfo.getNCRWSSResultCode()));
    }

    @Then("I should get a response: $expectedResponseJson")
    public final void testResponse(String expectedResponseJson){     
    	JsonMarshaller<ViewPosLinkInfo> json = new JsonMarshaller<ViewPosLinkInfo>();
        try {
        	newPosLinkInfo.setMessage(null);
            String actualResponseJson = json.marshall(newPosLinkInfo);
            System.out.println("marshalljson: "+actualResponseJson);
            
            JSONAssert.assertEquals(expectedResponseJson, actualResponseJson, JSONCompareMode.NON_EXTENSIBLE);
            
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    }

}
