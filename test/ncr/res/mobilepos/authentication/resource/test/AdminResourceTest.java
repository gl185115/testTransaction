package ncr.res.mobilepos.authentication.resource.test;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.junit.Ignore;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.test.TestRunnerScenario;

//@Ignore
public class AdminResourceTest extends TestRunnerScenario  {
    public AdminResourceTest(){
        super(new AdminResourceSteps());
    }
}