package ncr.res.mobilepos.deviceinfo.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.model.POSLinkInfo;
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

public class SignatureLinkTestSteps extends Steps{
    
    private DBInitiator dbInit;
    private DeviceInfoResource deviceInfoRes;
    private ViewPosLinkInfo viewPosLinkInfo;
    private POSLinkInfo posLinkInfo;
    private ResultBase resultBase;
    private int expectedResultCode;

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        new DBInitiator("Mst_DeviceInfo", "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/" + "PRM_SIGNATURE_LINK.xml", DATABASE.RESMaster);
        
        GlobalConstant.setMaxSearchResults(2);
    }
    @AfterScenario
    public final void TearDownClass() {
        Requirements.TearDown();
    }

    @Given("I have DeviceInfo resource")
    public final void IHaveItemResource() {
        deviceInfoRes = new DeviceInfoResource();
        deviceInfoRes.setContext(Requirements.getMockServletContext());        
        GlobalConstant.setMaxSearchResults(2);
        Assert.assertNotNull(deviceInfoRes);        
    }

    @When("I get Signature Link Item with Link Type{$linktype} and StoreID{$storeid} and POSLink ID{$poslinkid}")
    public final void IgetSignatureLinkItem(final String linktype, String storeid,
            String poslinkid) {
        if (storeid.equals("null")) {
            storeid = null;
        }
        if (poslinkid.equals("null")) {
            poslinkid = null;
        }
        viewPosLinkInfo = new ViewPosLinkInfo();
        viewPosLinkInfo = deviceInfoRes.getLinkItem(linktype, storeid, poslinkid);
        posLinkInfo = viewPosLinkInfo.getPosLinkInfo();
        expectedResultCode = viewPosLinkInfo.getNCRWSSResultCode();
    }
    @Then("I should get POSLinkInfo model with JSON format: $jsonPosLinkInfo")
    public final void AssertGetPosLinkInfoModel(String expectedResponseJson) {
        try {
            JsonMarshaller<ViewPosLinkInfo> posLinkInfoJsonMarshaller =
                new JsonMarshaller<ViewPosLinkInfo>();
            String actualResponseJson =
                posLinkInfoJsonMarshaller.marshall(viewPosLinkInfo);
            String resCode = "\"NCRWSSResultCode\":"+ viewPosLinkInfo.getNCRWSSResultCode(); 
            System.out.println("rescode: "+resCode);
            if(actualResponseJson.contains(resCode)){
                actualResponseJson = actualResponseJson.replaceAll(resCode, "");
                actualResponseJson = actualResponseJson.replaceAll("\"NCRWSSExtendedResultCode\":0", "");
            }
            if(expectedResponseJson.contains(resCode)){
                expectedResponseJson = expectedResponseJson.replaceAll(resCode, "");
                expectedResponseJson = expectedResponseJson.replaceAll("\"NCRWSSExtendedResultCode\":0", "");
            }
            Assert.assertEquals("Verify the POS Link Information JSON Format",
                    expectedResponseJson, actualResponseJson);
        } catch (IOException e) {
            Assert.fail("Failed to verify the POS Link Information JSON format");
            e.printStackTrace();
        }
    }
    
    @Then("the POSLinkInfo model should be not null")
    public final void posLinkInfoNotNull() {
        assertNotNull(posLinkInfo);
    }
    @Then("the POSLinkInfo model should be null")
    public final void posLinkInfoNull() {
        assertThat(posLinkInfo, is(equalTo(null)));
    }
    @Then("the result code should be $resultCode")
    public final void resultCodeShouldBe(final String resultcode) {
        assertThat(Integer.valueOf(resultcode), is(equalTo(expectedResultCode)));
    }
    
    //  FOR DELETE SIGNATURE LINK
    @Given("an entries from PRM_SIGNATURE_LINK database table")
    public final void anEntriesPRM_SIGNATURE_LINKDatabaseTable(){
        dbInit = new DBInitiator("Prm_SignatureInfo", "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/" + "PRM_SIGNATURE_LINK_forDelete.xml", DATABASE.RESMaster);       
        }
    

    @Then("the PRM_SIGNATURE_LINK database table should have the following row(s): $signatureTable")
    public final void theMST_SIGNATURE_LINKDatabaseTableShouldHaveTheFollowingRow(ExamplesTable expectedSignatureInfoTable) {
        try {
            ITable actualSignatureInfoTable = 
                dbInit.getTableSnapshot("PRM_SIGNATURE_LINK");
            Assert.assertEquals("Compare the number of rows in PRM_SIGNATURE_LINK",
                    expectedSignatureInfoTable.getRowCount(),
                    actualSignatureInfoTable.getRowCount());
            
            int i = 0;
            for(Map<String, String> expectedSignatureInfo : expectedSignatureInfoTable.getRows()) {
                Assert.assertEquals("Compare the storeid in PRM_SIGNATURE_LINK row" + i,
                        expectedSignatureInfo.get("storeid"),
                        ((String)actualSignatureInfoTable.getValue(i, "storeid")).trim());
                Assert.assertEquals("Compare the id in PRM_SIGNATURE_LINK row" + i,
                        expectedSignatureInfo.get("id"),
                        ((String)actualSignatureInfoTable.getValue(i, "id")).trim());
                Assert.assertEquals("Compare the displayname in PRM_SIGNATURE_LINK row" + i,
                        expectedSignatureInfo.get("displayname"),
                        ((String)actualSignatureInfoTable.getValue(i, "displayname")).trim());
                Assert.assertEquals("Compare the status in PRM_SIGNATURE_LINK row" + i,
                        expectedSignatureInfo.get("status"),
                        ((String)actualSignatureInfoTable.getValue(i, "status")).trim());
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in PRM_SIGNATURE_LINK.");
        }
    }
    
    @When("I delete a Signature Link with storeid:$storeid and linkid:$linkid")
    public final void deleteLink(String storeid, String linkid) {
        if (storeid.equals("null")) {
            storeid = null;
        }
        if (linkid.equals("null")) {
            linkid = null;
        }
        resultBase = deviceInfoRes.deleteLink("signature", storeid, linkid);
    }
    
    @When("I delete a Signature Link with invalid Link Type")
    public final void deleteLinkInvalidLink() {
        resultBase = deviceInfoRes.deleteLink("sign", "0", "0");
    }
    
  
    @Then("the actual ResultBase would be $jsonResultBase")
    public final void theActualResultBaseWouldBe(String jsonResultBase) {
        JsonMarshaller<ResultBase> resultbaseMarshaller =
            new JsonMarshaller<ResultBase>();
        try {
            ResultBase expectedResultBase =
                resultbaseMarshaller.unMarshall(jsonResultBase, ResultBase.class);
            
            String actualPOSLinksJson = resultbaseMarshaller.marshall(viewPosLinkInfo);
            String resCode = "NCRWSSResultCode\":"+ viewPosLinkInfo.getNCRWSSResultCode(); 
            if(actualPOSLinksJson.contains(resCode)){
                actualPOSLinksJson = actualPOSLinksJson.replaceAll(resCode, "");
                actualPOSLinksJson = actualPOSLinksJson.replaceAll("NCRWSSExtendedResultCode\":0", "");
            }
            if(jsonResultBase.contains(resCode)){
                jsonResultBase = jsonResultBase.replaceAll(resCode, "");
                jsonResultBase = jsonResultBase.replaceAll("NCRWSSExtendedResultCode\":0", "");
            }

            Assert.assertEquals("Compare the ResultCode",
                    expectedResultBase.getNCRWSSResultCode(),
                    resultBase.getNCRWSSResultCode());
            Assert.assertEquals("Compare the ExtendedResultCode",
                    expectedResultBase.getNCRWSSExtendedResultCode(),
                    resultBase.getNCRWSSExtendedResultCode());
        } catch (IOException e) {
            Assert.fail("Failed to compare the Expected"
                    + " ResultBase from the Actual ResultBase");
        }
    }
    
    @Then("the actual ResultBase for delete would be $jsonResultBase")
    public final void theActualResultBaseForDeleteWouldBe(final String jsonResultBase) {
        JsonMarshaller<ResultBase> resultbaseMarshaller =
            new JsonMarshaller<ResultBase>();
        try {
            ResultBase expectedResultBase =
                resultbaseMarshaller.unMarshall(jsonResultBase, ResultBase.class);
            
            Assert.assertEquals("Compare the ResultCode",
                    expectedResultBase.getNCRWSSResultCode(),
                    resultBase.getNCRWSSResultCode());
            Assert.assertEquals("Compare the ExtendedResultCode",
                    expectedResultBase.getNCRWSSExtendedResultCode(),
                    resultBase.getNCRWSSExtendedResultCode());
        } catch (IOException e) {
            Assert.fail("Failed to compare the Expected"
                    + " ResultBase from the Actual ResultBase");
        }
    }
}
