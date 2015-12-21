package ncr.res.mobilepos.consolidation.resource.test;

import static org.junit.Assert.assertEquals;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletContext;

import junit.framework.Assert;
import ncr.res.mobilepos.consolidation.dao.TransactionInfo;
import ncr.res.mobilepos.consolidation.resource.ConsolidationResource;
import ncr.res.mobilepos.consolidation.resource.DcRsp;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.dbunit.DatabaseUnitException;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.steps.Steps;


public class DcRspSteps extends Steps {
    private DcRsp dcrsp;
    private ConsolidationResource consolidator;
    private ServletContext servletcontext;
    private DBInitiator dbInit;
    private final String datasetPath = "test/ncr/res/mobilepos/consolidation/";
    
    @BeforeScenario
    public final void SetUpClass()
    throws DatabaseUnitException, SQLException, Exception
    {        
        Requirements.SetUp();
        dbInit = new DBInitiator("DcRspSteps", DATABASE.RESTransaction);
        dbInit.ExecuteOperation( DatabaseOperation.DELETE_ALL, datasetPath
                + "CONSOLIDATION_TABLES_EMPTY.xml");
        
        //Set the Company ID as 000000000000
        GlobalConstant.setCorpid("000000000000");
    }
    
    @AfterScenario
    public final void TearDownClass(){
        Requirements.TearDown();        
        //Set the CompanyID back to default
        GlobalConstant.setCorpid("");
    }
    
    @Given("a batch of values for System Configuration named $systemName")
    public final void aBatchOfValuesForSystemConfigurationNamed(
            final String systemName)
    throws DatabaseUnitException, SQLException, Exception{
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                datasetPath + systemName);        
    }
    
    @Given("a DcRsp model")
    public final void createDcRsp(){
        dcrsp = new DcRsp();
    }
    
    @Given("an instance of Consolidation Resource")
    public final void anInstanceOfConsolidationResource(){
        consolidator = new ConsolidationResource();
    }

    @When("DcResp status is set to $status")
    public final void setStatus(final int status){
        dcrsp.setStatus(status);
    }
    
    @When("the WebAPI Starts Up")
    public final void theWebAPIStartsUp(){
        //servletContext = null;
        //startUp = true;
        try 
        {
            servletcontext = Requirements.getMockServletContext();
            Field context = consolidator.getClass().getDeclaredField("context");
            context.setAccessible(true);
            context.set(consolidator, servletcontext);
        } catch (Exception ex) { 
            //startUp = false;
            Assert.fail("Cannot Start the WebAPI");
        }
    }
    
    //$adj is just used to describe the State of the items to be consolidated
    @When("a {$adj} POSLog xml has been consolidated")
    public final void APOSLogXmlHasBeenConsolidated(final String adj){
        dcrsp = consolidator.consolidate(); 
    }
    
    @Then("DcResp xml string should be {$xml}")
    public final void seriallize(final String xml) throws Exception{
        XmlSerializer<DcRsp> posLogRespSrlzr = new XmlSerializer<DcRsp>();
        String actual =
            posLogRespSrlzr.marshallObj(DcRsp.class, dcrsp, "UTF-8");
        assertEquals(xml, actual);
    }
}
