package ncr.res.mobilepos.queuebuster.dao.test;

import org.junit.Ignore;

import ncr.res.mobilepos.test.TestRunnerScenario;
@Ignore
public class SelectOldestTransactionFromQueueTest extends TestRunnerScenario{
 public SelectOldestTransactionFromQueueTest(){
     super(new SQLServerQueueBusterDaoSteps());
 }
}
