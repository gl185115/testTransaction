package ncr.res.mobilepos.devicelog.dao.test;

import ncr.res.mobilepos.test.TestRunnerScenario;
import org.junit.Ignore;

@Ignore
public class SQLServerDeviceLogDAOTest extends TestRunnerScenario {

    public SQLServerDeviceLogDAOTest() {
        super(new SQLServerDeviceLogDAOSteps());
    }
}
