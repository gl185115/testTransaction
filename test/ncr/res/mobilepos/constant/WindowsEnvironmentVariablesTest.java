package ncr.res.mobilepos.constant;

import org.junit.runner.RunWith;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;

import ncr.res.mobilepos.test.TestRunnerScenario;

@RunWith(PowerMockRunner.class)
@PrepareForTest({WindowsEnvironmentVariablesSteps.class, WindowsEnvironmentVariables.class})
public class WindowsEnvironmentVariablesTest extends TestRunnerScenario {
    public WindowsEnvironmentVariablesTest() {
        super(new WindowsEnvironmentVariablesSteps());
    }
}