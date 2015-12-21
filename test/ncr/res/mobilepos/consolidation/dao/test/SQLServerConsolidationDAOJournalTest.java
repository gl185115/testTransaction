package ncr.res.mobilepos.consolidation.dao.test;

import org.junit.Ignore;

import ncr.res.mobilepos.test.TestRunnerScenario;

@Ignore
public class SQLServerConsolidationDAOJournalTest extends TestRunnerScenario{
    public SQLServerConsolidationDAOJournalTest() {
        super(new SQLServerConsolidationDAOSteps());
    }
}
