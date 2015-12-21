package ncr.res.mobilepos.deviceinfo.resource.test;

import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.deviceinfo.model.DeviceInfo;
import ncr.res.mobilepos.deviceinfo.model.ViewPosLinkInfo;
import ncr.res.mobilepos.deviceinfo.resource.DeviceInfoResource;
import ncr.res.mobilepos.helper.DBInitiator;
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

public class SetAuthorizationLinkTestSteps extends Steps {
    
    DeviceInfoResource pdc = null;
    private DBInitiator dbInit;
    ResultBase resBase = null;
    DeviceInfo resPDI = null;
    ViewPosLinkInfo viewInfo = null;
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
        GlobalConstant.setCorpid("9999");
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("a PeripheralDeviceControl service")
    public final void getPeripheralDeviceService()
    {
        pdc = new DeviceInfoResource();
        pdc.setContext(Requirements.getMockServletContext());
        GlobalConstant.setCorpid("9999");
        Assert.assertNotNull(pdc);
    }
    
    @Given("an initial deviceinfo entry in database")
    public final void addDeviceInfoInDB() throws Exception{
        dbInit =  new DBInitiator("Mst_DeviceInfo",
                "test/ncr/res/mobilepos/deviceinfo/"
                + "datasets/MST_DEVICE_INFO_setLink.xml", DATABASE.RESMaster);
    }
    
    @Given("entries in PRM_AUTHORIZATION_LINK database table")
    public final void entriesSignatureLinkDB() throws Exception{
        dbInit =  new DBInitiator("Mst_DeviceInfo",
                "test/ncr/res/mobilepos/deviceinfo/"
                + "datasets/PRM_AUTHORIZATION_LINK_setLink.xml", DATABASE.RESMaster);
    }
    
    @Then ("ResultCode should be $result")
    public final void checkResultBaseCode(final int result)
    {
        Assert.assertEquals(result, viewInfo.getNCRWSSResultCode());
    }

    @When ("I set creditauthorization link of storeid{$storeid} and deviceid{$deviceid} to {$authorizationlink}")
    public final void setAuthorizationLink(final String storeid,
            final String deviceid, String authorizationlink)
    {
        if(pdc == null){
            pdc = new DeviceInfoResource();
        }
        
        if (authorizationlink.equals("null")) {
            authorizationlink = null;
        }
        
        resBase = pdc.setAuthorizationLink(
                storeid, deviceid, authorizationlink);
    }
    
    @Then ("the result base should be $result")
    public final void checkResultBase(final int result)
    {
        Assert.assertEquals(result, resBase.getNCRWSSResultCode());
    }
    
    @Then("the MST_DEVICEINFO database table should have the following row(s): $deviceTables")
    public final void theMST_DEVICEINFODatabaseTableShouldHaveTheFollowingRow(ExamplesTable expectedDeviceInfoTable) {
        try {
            ITable actualDeviceInfoTable = 
                dbInit.getTableSnapshot("MST_DEVICEINFO");
            Assert.assertEquals("Compare the number of rows in MST_DEVICEINFO",
                    expectedDeviceInfoTable.getRowCount(),
                    actualDeviceInfoTable.getRowCount());
            
            int i = 0;
            for(Map<String, String> expectedDeviceInfo : expectedDeviceInfoTable.getRows()) {
                String storeid = (String)actualDeviceInfoTable.getValue(i, "storeid");
                Assert.assertEquals("Compare the STOREID in MST_DEVICEINFO row" + i,
                        expectedDeviceInfo.get("storeid"),
                        storeid.trim());
                Assert.assertEquals("Compare the ID in MST_DEVICEINFO row" + i,
                        expectedDeviceInfo.get("terminalid"),
                        ((String)actualDeviceInfoTable.getValue(i, "terminalid")).trim());
                String link = (String)actualDeviceInfoTable.getValue(i, "linkauthorization");
                if (link == null) {
                    link = "";
                }
                Assert.assertEquals("Compare the linkauthorization in MST_DEVICEINFO row" + i,
                        expectedDeviceInfo.get("linkauthorization") == null ? "" : expectedDeviceInfo.get("linkauthorization"), link);
                i++;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Assert.fail("Fail to test the entry in MST_DEVICEINFO.");
        }
    }
    
}
