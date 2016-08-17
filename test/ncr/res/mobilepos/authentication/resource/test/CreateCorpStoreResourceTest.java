package ncr.res.mobilepos.authentication.resource.test;

import org.junit.Ignore;
import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * Test class for running create store test scenarios.
 * @author RD185102
 *
 */

public class CreateCorpStoreResourceTest extends TestRunnerScenario {
   /**
    * Constructor.
    */
    public CreateCorpStoreResourceTest() {
        super(new CreateCorpStoreResourceSteps());
    }
}