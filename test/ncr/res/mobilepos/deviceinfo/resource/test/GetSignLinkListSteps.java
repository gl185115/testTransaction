package ncr.res.mobilepos.deviceinfo.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.model.POSLinkInfo;
import ncr.res.mobilepos.deviceinfo.model.POSLinks;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class GetSignLinkListSteps extends Steps{
    DeviceInfoResource deviceInfoRes;
    POSLinks poslinks;
    int expectedResultCode;

    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        new DBInitiator("Mst_DeviceInfo", "test/ncr/res/mobilepos/"
                + "deviceinfo/datasets/" + "PRM_SIGNATURE_LINK.xml", DATABASE.RESMaster);    
        GlobalConstant.setMaxSearchResults(5);
    }
    @AfterScenario
    public final void TearDownClass() {
        Requirements.TearDown();
    }

    @Given("I have DeviceInfo resource")
    public final void IHaveItemResource() {
        deviceInfoRes = new DeviceInfoResource();
        deviceInfoRes.setContext(Requirements.getMockServletContext());
        Assert.assertNotNull(deviceInfoRes);       
    }

    @When("I get Signature Link Items with StoreID{$storeid}")
    public final void IgetSignatureItem(String storeid) {
        
        if (storeid.equals("null")) {
            storeid = null;
        }
        
        poslinks = new POSLinks();
        poslinks = deviceInfoRes.getLinksList("signature", storeid, null, null, 0);
        expectedResultCode = poslinks.getNCRWSSResultCode();
    }
    
    @When("I get $linktype link items with StoreID{$storeid}")
    public final void IHaveInvalidLinkType(String linktype, String storeid) {
        
    	linktype = "null".equals(linktype) ? null : linktype;
    	storeid = "null".equals(storeid) ? null : storeid;
       
        poslinks = new POSLinks();
        poslinks = deviceInfoRes.getLinksList(linktype, storeid, null, null, 0);
        expectedResultCode = poslinks.getNCRWSSResultCode();
    }
    
    @Then("I should get POSLinkInfo model with properties: $expected")
    public final void AssertGetPosLinkInfoModel(final ExamplesTable expected) {
        int i = 0;
        for (Map<String, String> poslinkinfo : expected.getRows()) {
            assertEquals(poslinkinfo.get("LinkName"), poslinks.getPOSLinkInfos().get(i).getLinkName());
            assertEquals(poslinkinfo.get("LinkID"), poslinks.getPOSLinkInfos().get(i).getPosLinkId());
            i++;
        }
    }
    @Then("the POSLinkInfo model should be not null")
    public final void posLinkInfoNotNull() {
        assertNotNull(poslinks);
    }
    @Then("the result code should be $resultCode")
    public final void resultCodeShouldBe(final String resultcode) {
        assertThat(Integer.valueOf(expectedResultCode), is(equalTo(Integer.valueOf(resultcode))));
    }
}
