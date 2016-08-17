package ncr.res.mobilepos.line.resource.test;

import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * Test class for delete item method in ItemResource.
 *
 */
public class DeleteLineResourceTest extends TestRunnerScenario {

    /**
     * Default Constructor.
     */
    public DeleteLineResourceTest() {
        super(new DeleteLineResourceSteps());
    }
}
