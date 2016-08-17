/**
 * Unit test for device log and sign log collection.
 */
package ncr.res.mobilepos.devicelog.resource.test;

import ncr.res.mobilepos.test.TestRunnerScenario;

/**
 * Class for unit test of device log and sign log collection.
 * @author Administrator
 *
 */
public class DeviceLogCollectionResourceTest extends TestRunnerScenario {

    /**
     * default constructor.
     */
    public DeviceLogCollectionResourceTest() {
        super(new DeviceLogCollectionResourceSteps());
    }

}
