package ncr.res.mobilepos.daofactory;

import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * Test class for changePasscode method in CredentialResource class.
 */
public class JndiDBManagerMSSqlServerTest extends TestRunnerScenario {

    /**
     * Default constructor.
     */
    public JndiDBManagerMSSqlServerTest() {
        super(new JndiDBManagerMSSqlServerSteps());
    }
}

