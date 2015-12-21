package ncr.res.mobilepos.customeraccount.dao.test;

import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * Tests the Customer Account DAO.
 *
 */
public class CustomerAccountDaoTest  extends TestRunnerScenario {

    /**
     * Constructor.
     */
    public CustomerAccountDaoTest() {
        super(new CustomerAccountDaoSteps());
    }
}
