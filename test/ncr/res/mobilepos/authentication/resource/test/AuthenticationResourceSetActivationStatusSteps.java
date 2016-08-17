package ncr.res.mobilepos.authentication.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;
import ncr.res.mobilepos.authentication.model.DeviceStatus;
import ncr.res.mobilepos.authentication.model.SignDetails;
import ncr.res.mobilepos.authentication.resource.AuthenticationResource;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

public class AuthenticationResourceSetActivationStatusSteps extends Steps{
    private AuthenticationResource authresource;
    private DeviceStatus result;
    
    @BeforeScenario
    public final void setUp() {
        Requirements.SetUp();
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }
    
    @Given("an Authentication Resource")
    public final void createResource() throws Exception{
        authresource = new AuthenticationResource();
        DBInitiator dbinit = new DBInitiator("AuthenticationResource",
                "test/ncr/res/mobilepos/authentication/resource/test/"
                + "insertAuthResourceDevices.xml", DATABASE.RESMaster);
        dbinit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/authentication/resource/test/"
                + "MST_SIGN_ACTIVATION_LIST.xml");
        authresource.setContext(Requirements.getMockServletContext());
    }
    
    @When ("I authenticate the device: corpid{$corpid}, storeid{$storeid},"
            + " terminalid{$terminalid}, uuid{$uuid}, udid{$udid}")
    public final void authenticateDevice(final String corpid,
            final String storeid,
            String terminalid, String uuid, String udid)
    {
        if(terminalid.equals("null")) {
            terminalid = null;
        }
        if(uuid.equals("null")) {
            uuid = null;
        }
        if(udid.equals("null")) {
            udid = null;
        }
        
        result = authresource.authenticateDevice(corpid, storeid ,terminalid, udid, uuid);
    }
    
    @When ("I set activation status of device {$corpId,$storeId,$terminalId,"
            + "$udid,$uuid} to signstatus{$signStatus}, signtid{$signTid},"
            + " signactivationkey{$signActivationKey}")
    public final void setActivationStatus(final String corpId,
                                    final String storeId,
                                    final String terminalId,
                                    final String udid,
                                    final String uuid,
                                    final int signStatus,
                                    final String signTid,
                                    final String signActivationKey){
        result = authresource.setSignatureActivationStatus(corpId, storeId,
                terminalId, udid, uuid, signStatus, signTid, signActivationKey);
    }
    
    @Then ("the result should be $expected")
    public final void checkResult(final int expected)
    {
        assertThat(result.getNCRWSSResultCode(),is(equalTo(expected)));
    }
    

    @Then ("the signStatus should be $expected")
    public final void checkSignStatus(final int expected)
    {
        assertThat(result.getSignStatus(),is(equalTo(expected)));
    }

    @Then ("the activationKey.signActivationKey should be {$expected}")
    public final void checkSignActivationKey(String expected)
    {
        if(expected.equals("empty")){
            expected = null;
        }
        assertThat(result.getActivationKey().getActivationKey(),
                is(equalTo(expected)));
    }
    
    @Then ("the activationKey.signTid should be {$expected}")
    public final void checkSignTid(String expected)
    {
        if(expected.equals("empty")){
            expected = null;
        }
        assertThat(result.getActivationKey().getSignatureTid(),
                is(equalTo(expected)));
    }
}
