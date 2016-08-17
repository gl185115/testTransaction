/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * StoreViewResourceTest
 *
 * Unit Test class for view store service in StoreResource class
 *
 */
package ncr.res.mobilepos.authentication.resource.test;

import org.junit.Ignore;

import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * Test class for running view store test scenarios.
 *
 */
@Ignore
public class ViewCorpStoreResourceTest extends TestRunnerScenario {
    /**
     * Constructor.
     */
    public ViewCorpStoreResourceTest() {
        super(new ViewCorpStoreResourceSteps());
    }
}
