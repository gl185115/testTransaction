package ncr.res.mobilepos.authentication.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.util.Map;

import ncr.res.mobilepos.authentication.resource.CorpStoreResource;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.store.resource.StoreResource;

import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;
import org.junit.Assert;


public class DeleteCorpStoreResourceSteps extends Steps {
    private CorpStoreResource strResource;
    private ResultBase resultBase;
    private DBInitiator dbinit;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("I have a CorpStore Resource")
    public final void IHaveAStoreResource(){
        strResource = new CorpStoreResource();
        GlobalConstant.setCorpid("000000000000");
    }

    @Then("the result should be {$Result}")
    public final void checkResult(final int Result){
        assertThat(resultBase.getNCRWSSResultCode(), is(equalTo(Result)));
    }
    
    @Given ("a CorpStore Table")
    public final void emptyTable(){
        dbinit = new DBInitiator("AuthenticationResource",
                "test/ncr/res/mobilepos/authentication/resource/datasets/"
                + "CORP_STORE_for_delete.xml", DATABASE.RESMaster);
    }
    
    @When ("I delete a corpstore ($companyid) ($storeid)")
    public final void delStore(String companyid, String storeid)
    {
        if (companyid.equals("null")) {
            companyid = null;
        }
        if (storeid.equals("null")) {
            storeid = null;
        }
        resultBase = strResource.deleteCorpStore(companyid, storeid);
    }
    
    @Then("the following corpstores should exist: $expectedcorpstores")
    public final void theFollowingStoresAreRetrieved(
            final ExamplesTable expectedcorpstores) throws DataSetException {
        ITable actualDeviceInfoTable = dbinit.getTableSnapshot("MST_CORP_STORE");

        Assert.assertEquals(
                "Compare the number of stores:",
                expectedcorpstores.getRowCount(),
                actualDeviceInfoTable.getRowCount());
        int i = 0;
        for (Map<String, String> expectedDeviceInfo : expectedcorpstores.getRows()) {
            String storeid = (String)actualDeviceInfoTable.getValue(i, "storeid");
            Assert.assertEquals("Compare the storeid in MST_DEVICEINFO row" + i,
                    expectedDeviceInfo.get("storeid"),
                    storeid.trim());
            Assert.assertEquals("Compare the storename in MST_DEVICEINFO row" + i,
                    expectedDeviceInfo.get("storename"),
                    (String)actualDeviceInfoTable.getValue(i, "storename"));
            Assert.assertEquals("Compare the passcode in MST_DEVICEINFO row" + i,
                    expectedDeviceInfo.get("passcode"),
                    (String)actualDeviceInfoTable.getValue(i, "passcode"));
            Assert.assertEquals("Compare the permission in MST_DEVICEINFO row" + i,
                    Integer.parseInt(expectedDeviceInfo.get("permission")),
                    (int)(Integer)actualDeviceInfoTable.getValue(i, "permission"));
            i++;
        }
    }
}
