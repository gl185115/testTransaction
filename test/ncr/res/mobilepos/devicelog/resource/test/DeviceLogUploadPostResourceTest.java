/**
 * Unit test for device log and sign log collection.
 */
package ncr.res.mobilepos.devicelog.resource.test;

import java.util.Arrays;
import java.util.List;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.Test;

import junit.framework.TestCase;
import ncr.res.mobilepos.test.TestRunnerScenario;


/**
 * Class for unit test of device log and sign log collection.
 * @author Administrator
 *
 */
public class DeviceLogUploadPostResourceTest extends TestRunnerScenario {
    /**
     * default constructor.
     */
    public DeviceLogUploadPostResourceTest() {
        super(new DeviceLogUploadPostResourceSteps());
    }
}
