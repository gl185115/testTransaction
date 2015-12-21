package ncr.res.mobilepos.deviceinfo.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.io.IOException;
import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.model.POSLinkInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewDeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewPosLinkInfo;
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
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class AuthorizationLinkTestSteps extends Steps{
    DeviceInfoResource deviceInfoRes;
    ViewPosLinkInfo viewPosLinkInfo;
    POSLinkInfo posLinkInfo;
    int expectedResultCode;

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        new DBInitiator("Mst_DeviceInfo", "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/" + "PRM_AUTHORIZATION_LINK_List.xml", DATABASE.RESMaster);       
    }
    @AfterScenario
    public final void TearDownClass() {
        Requirements.TearDown();
    }

    @Given("I have DeviceInfo resource")
    public final void IHaveItemResource() {
        deviceInfoRes = new DeviceInfoResource();
        deviceInfoRes.setContext(Requirements.getMockServletContext());
        GlobalConstant.setCorpid("9999"); // Set corpid
        GlobalConstant.setMaxSearchResults(2);
        Assert.assertNotNull(deviceInfoRes);
    }

    @When("I get Authorization Link Item with Link Type{$linktype} and StoreID{$storeid} and POSLink ID{$poslinkid}")
    public final void IgetAuthorizationLinkItem(final String linktype, final String storeid,
            final String poslinkid) {
        viewPosLinkInfo = new ViewPosLinkInfo();
        viewPosLinkInfo = deviceInfoRes.getLinkItem(linktype, storeid, poslinkid);
        posLinkInfo = viewPosLinkInfo.getPosLinkInfo();
        expectedResultCode = viewPosLinkInfo.getNCRWSSResultCode();
    }
    @Then("I should get POSLinkInfo model with JSON format: $jsonPosLinkInfo")
    public final void AssertGetPosLinkInfoModel(String jsonPosLinkInfo) {
        try {
            JsonMarshaller<ViewPosLinkInfo> posLinkInfoJsonMarshaller =
                new JsonMarshaller<ViewPosLinkInfo>();
            String actualPosLinkInfoJson =
                posLinkInfoJsonMarshaller.marshall(viewPosLinkInfo);
            System.out.println(actualPosLinkInfoJson);
            String resCode = "\"NCRWSSResultCode\":"+ viewPosLinkInfo.getNCRWSSResultCode(); 
            System.out.println("rescode: "+resCode);
            if(actualPosLinkInfoJson.contains(resCode)){
            	actualPosLinkInfoJson = actualPosLinkInfoJson.replaceAll(resCode, "");
            	actualPosLinkInfoJson = actualPosLinkInfoJson.replaceAll("\"NCRWSSExtendedResultCode\":0", "");
            }
            if(jsonPosLinkInfo.contains(resCode)){
            	jsonPosLinkInfo = jsonPosLinkInfo.replaceAll(resCode, "");
            	jsonPosLinkInfo = jsonPosLinkInfo.replaceAll("\"NCRWSSExtendedResultCode\":0", "");
            }
            Assert.assertEquals("Verify the POS Link Information JSON Format",
                    jsonPosLinkInfo, actualPosLinkInfoJson);
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
}
