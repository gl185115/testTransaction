package ncr.res.mobilepos.systemsetting.resource.test;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ncr.res.mobilepos.systemsetting.resource.SystemSettingResource;
import ncr.res.mobilepos.test.TestRunnerScenario;

@RunWith(PowerMockRunner.class)
@PrepareForTest({SystemSettingResourceSteps.class, SystemSettingResource.class})
@PowerMockIgnore({"org.*", "javax.*", "ncr.res.mobilepos.constant.WindowsEnvironmentVariables"})
public class SystemSettingResourceTest extends TestRunnerScenario {
    public SystemSettingResourceTest() {
        super(new SystemSettingResourceSteps());
    }
}