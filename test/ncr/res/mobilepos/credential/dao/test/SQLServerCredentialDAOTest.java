package ncr.res.mobilepos.credential.dao.test;

import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * SQLServerCredentialDAO Test class.
 */
public class SQLServerCredentialDAOTest extends TestRunnerScenario {
    /**
     * Default constructor.
     */
    public SQLServerCredentialDAOTest() {
        super(new SQLServerCredentialDAOSteps());
    }
}
