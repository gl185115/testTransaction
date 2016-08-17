package ncr.res.mobilepos.tillinfo.resource.test;

import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * Test class for running view till test scenarios.
 *
 */
public class CreateTillInfoResourceTest extends TestRunnerScenario {
    /**
     * Constructor.
     */
    public CreateTillInfoResourceTest() {
        super(new CreateTillInfoResourceSteps());
    }
}
