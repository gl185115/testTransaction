package ncr.res.mobilepos.mastermaintenance.resource.test;

import java.lang.reflect.Field;

import javax.servlet.ServletContext;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.mastermaintenance.dao.SQLServerMasterMaintenanceDAO;
import ncr.res.mobilepos.mastermaintenance.model.OpeMastTbl;
import ncr.res.mobilepos.mastermaintenance.resource.MasterMaintenanceResource;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.systemconfiguration.resource.SystemConfigurationResource;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;

//import static org.hamcrest.CoreMatchers.equalTo;
//import static org.hamcrest.CoreMatchers.is;
//import static org.hamcrest.CoreMatchers.not;
//import static org.junit.Assert.assertThat;

import org.junit.Assert;
import org.omg.CosNaming.NamingContextPackage.NotEmpty;


public class MasterMaintenanceResourceSteps extends Steps {
    private ServletContext servletContext = null;
    private boolean startUp = false;
    private DBInitiator dbInit = null;
    private String rawSpartData = null;
    private SQLServerMasterMaintenanceDAO sqlservermastermaintenanceTest;
    private ResultBase actualResultBase = null;
    private MasterMaintenanceResource mastermaintenance = null; 
    
    @BeforeScenario
    public final void setUp() throws DaoException {
        Requirements.SetUp();
        this.dbInit = new DBInitiator("MasterMaintenanceResourceSteps", DATABASE.RESMaster);
        this.sqlservermastermaintenanceTest =
        new SQLServerMasterMaintenanceDAO();
        this.mastermaintenance = new MasterMaintenanceResource();
        GlobalConstant.setCorpid("000001");
    }

    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
    }

    @When("the Web API Starts Up")
    public final void theWebAPIStartsUp(){
        servletContext = null;
        startUp = true;
        try
        {
            servletContext = Requirements.getMockServletContext();
            Field context = mastermaintenance
            .getClass().getDeclaredField("context");
            context.setAccessible(true);
            context.set(mastermaintenance, servletContext);            
        } catch (/*RuntimeError*/Exception ex) {
            startUp = false;
        }
    }
    
    /**
     * A Given step, initializes rawSpartData.
     */
    @Given("a SpartData {$spartdata}")
    public final void aSpartData(final String spartdata){ 
       if (spartdata.equalsIgnoreCase("null"))
          this.rawSpartData = null;
       else
             this.rawSpartData = spartdata;
    }
    
    @When("a SpartData is imported with TableFlag value of {$tableFlag}")
    public final void whenaDBTriggerFromSpart(int tblFlag) {
            this.actualResultBase = this.mastermaintenance
                    .spartImport(this.rawSpartData, tblFlag);
    }
    
    @Then("resultBase should be {$spartdata}")
    public final void resultBaseShouldBe(int expectedresultbase) {
       Assert.assertEquals("Compare the ResultBase", expectedresultbase, 
          this.actualResultBase.getNCRWSSResultCode());
    }
    
    
}
