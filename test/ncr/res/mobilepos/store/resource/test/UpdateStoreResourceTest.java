/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* UpdateStoreResourceTest
*
* The Test Class for listing the Stores.
*
*/
package ncr.res.mobilepos.store.resource.test;

import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * The Test Class for Updating a Store.
 * @author cc185102
 *
 */
public class UpdateStoreResourceTest extends TestRunnerScenario {
    /**
     * The Default constructor.
     */
    public UpdateStoreResourceTest() {
        super(new UpdateStoreResourceSteps());
    }
}
