package ncr.res.mobilepos.pricing.resource.test;

import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * Test class for delete item method in ItemResource.
 *
 */
public class DeleteItemResourceTest extends TestRunnerScenario {

    /**
     * Default Constructor.
     */
    public DeleteItemResourceTest() {
        super(new DeleteItemResourceSteps());
    }
}
