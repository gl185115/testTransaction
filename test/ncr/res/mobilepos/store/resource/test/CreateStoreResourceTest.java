package ncr.res.mobilepos.store.resource.test;

import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * Test class for running create store test scenarios.
 * @author RD185102
 *
 */
public class CreateStoreResourceTest extends TestRunnerScenario {
   /**
    * Constructor.
    */
    public CreateStoreResourceTest() {
        super(new CreateStoreResourceSteps());
    }
}