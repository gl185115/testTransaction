package ncr.res.mobilepos.queuebuster.dao.test;

import org.junit.Ignore;

import ncr.res.mobilepos.test.TestRunnerScenario;
@Ignore
public class ListAllTransactionFromQueueTest extends TestRunnerScenario{
    public ListAllTransactionFromQueueTest() {
        super(new SQLServerQueueBusterDaoSteps());
    }
}
