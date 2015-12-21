package ncr.res.mobilepos.pricing.dao.test;

import ncr.res.mobilepos.test.TestRunnerScenario;

public class SQLServerItemDAOTest extends TestRunnerScenario{
    
    public SQLServerItemDAOTest(){
            super(new SQLServerItemDAOSteps());        
    }
}
