/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerCreditDAOTest
 *
 * TestRunnerScenario Class of SQLServerCreditDAO
 *
 * De la Cerna, Jessel G.
 */

package ncr.res.mobilepos.creditauthorization.dao.test;

import org.junit.Ignore;

import ncr.res.mobilepos.test.TestRunnerScenario;
@Ignore
public class SQLServerCreditDAOTest extends TestRunnerScenario {
	
    public SQLServerCreditDAOTest() {
        super(new SQLServerCreditDAOSteps());        
    }
    
}
