/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * StoreViewResourceTest
 *
 * Unit Test class for view store service in StoreResource class
 *
 */
package ncr.res.mobilepos.store.resource.test;

import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * Test class for running view store test scenarios.
 *
 */
public class ViewStoreResourceTest extends TestRunnerScenario {
    /**
     * Constructor.
     */
    public ViewStoreResourceTest() {
        super(new ViewStoreResourceSteps());
    }
}
