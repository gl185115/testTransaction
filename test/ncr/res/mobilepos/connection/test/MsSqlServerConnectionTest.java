package ncr.res.mobilepos.connection.test;

import ncr.res.mobilepos.test.TestRunnerScenario;

public class MsSqlServerConnectionTest extends TestRunnerScenario{
    public MsSqlServerConnectionTest(){
        super(new MsSqlServerConnectionSteps());
    }
}

