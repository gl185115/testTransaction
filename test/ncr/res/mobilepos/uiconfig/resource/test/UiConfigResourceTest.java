package ncr.res.mobilepos.uiconfig.resource.test;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ncr.res.mobilepos.systemsetting.resource.SystemSettingResource;
import ncr.res.mobilepos.test.TestRunnerScenario;

@RunWith(PowerMockRunner.class)
@PrepareForTest({UiConfigResourceSteps.class, SystemSettingResource.class})
@PowerMockIgnore({"org.*", "javax.*", "ncr.res.mobilepos.constant.WindowsEnvironmentVariables"})
public class UiConfigResourceTest extends TestRunnerScenario {
    public UiConfigResourceTest() {
        super(new UiConfigResourceSteps());
    }
}