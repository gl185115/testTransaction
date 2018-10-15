package ncr.res.mobilepos.tillinfo.resource.test;

import org.junit.Ignore;

import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * Test class for running view till test scenarios.
 *
 */
@Ignore
public class CreateTillInfoResourceTest extends TestRunnerScenario {
    /**
     * Constructor.
     */
    public CreateTillInfoResourceTest() {
        super(new CreateTillInfoResourceSteps());
    }
}
