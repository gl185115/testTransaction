package ncr.res.mobilepos.queuebuster.dao.test;

import org.junit.Ignore;

import ncr.res.mobilepos.test.TestRunnerScenario;
@Ignore
public class SelectTransactionFromQueueTest extends TestRunnerScenario {
    public SelectTransactionFromQueueTest() {
        super(new SQLServerQueueBusterDaoSteps());
    }
}
