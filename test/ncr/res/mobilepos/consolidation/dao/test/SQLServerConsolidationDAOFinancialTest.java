package ncr.res.mobilepos.consolidation.dao.test;

import org.junit.Ignore;

import ncr.res.mobilepos.test.TestRunnerScenario;

@Ignore
public class SQLServerConsolidationDAOFinancialTest
extends TestRunnerScenario{

    public SQLServerConsolidationDAOFinancialTest() {
        super(new SQLServerConsolidationDAOSteps());
    }

}
