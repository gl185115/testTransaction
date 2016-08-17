/*
* Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
*
* JsonSerializerTest
*
* Test Class for Json Serializer.
*
* cc185102
*/
package ncr.res.mobilepos.helper.serializer.test;

import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * The Test Class for JSON Serializer.
 * @author cc185102
 *
 */
public class JsonMarshallerTest extends TestRunnerScenario {
    /**
     * The Default constructor.
     */
    public JsonMarshallerTest() {
        super(new JsonMarshallerSteps());
    }
}
