package ncr.res.mobilepos.queuebuster.dao.test;

import org.junit.Ignore;

import ncr.res.mobilepos.test.TestRunnerScenario;
@Ignore
public class SaveTransactionToQueueTest extends TestRunnerScenario{
    public SaveTransactionToQueueTest() {
        super(new SQLServerQueueBusterDaoSteps());
    }
}
