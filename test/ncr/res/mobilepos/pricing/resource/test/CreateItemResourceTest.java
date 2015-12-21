/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * CreateItemResourceTest
 *
 * The Test Class for Creating Item Resource.
 *
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.pricing.resource.test;

import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * The test class for Creating Item Resource.
 *
 * @author CC1852102
 *
 */
public class CreateItemResourceTest extends TestRunnerScenario {
    /**
     * The Default constructor.
     */
    public CreateItemResourceTest() {
        super(new CreateItemResourceSteps());
    }
}
