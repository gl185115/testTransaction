package ncr.res.mobilepos.credential.resource.test;

import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * Test class for deleteEmployee method in CredentialResource class.
 *
 */
public class DeleteEmployeeResourceTest extends TestRunnerScenario {

    /**
     * Default constructor.
     */
    public DeleteEmployeeResourceTest() {
        super(new DeleteEmployeeResourceSteps());
    }
}
