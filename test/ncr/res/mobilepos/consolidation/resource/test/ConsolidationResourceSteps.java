package ncr.res.mobilepos.consolidation.resource.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.not;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.lang.reflect.Field;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletContext;

import junit.framework.Assert;
import ncr.res.mobilepos.consolidation.dao.TransactionInfo;
import ncr.res.mobilepos.consolidation.resource.ConsolidationResource;
import ncr.res.mobilepos.consolidation.resource.DcRsp;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;

import org.dbunit.DatabaseUnitException;
import org.dbunit.dataset.DataSetException;
import org.dbunit.dataset.ITable;
import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class ConsolidationResourceSteps extends Steps {
    private DBInitiator dbInit = null;
    private final String datasetPath = "test/ncr/res/mobilepos/consolidation/";
    private ConsolidationResource testConsolidationResource = null;
    private ServletContext servletContext = null;
    
    @BeforeScenario
    public final void setUpClass() throws DaoException
    {        
        Requirements.SetUp();
        dbInit = new DBInitiator("SQLServerConsolidationDAOSteps", datasetPath
                + "CONSOLIDATION_TABLES_EMPTY.xml", DATABASE.RESTransaction);
        
        //Set the Company ID as 000000000000
        GlobalConstant.setCorpid("000000000000");
    }
    
    @AfterScenario
    public final void tearDownClass(){
        Requirements.TearDown();        
        //Set the CompanyID back to default
        GlobalConstant.setCorpid("");
    }
    
    @Given("an instance of Consolidation Resource")
    public final void anInstanceOfConsolidationResource() {
        testConsolidationResource = new ConsolidationResource();
    }
    
    @Given("a batch of values for System Configuration named $systemName")
    public final void aBatchOfValuesForSystemConfigurationNamed(
            final String systemName)
    throws DatabaseUnitException, SQLException, Exception{
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/systemconfiguration/resource/test/"
                + systemName);        
    }
    
    
    @Given("a batch of values for POSLOGs named $systemName")
    public final void aBatchOfValuesForPOSLOGsNamed(final String systemName)
    throws DatabaseUnitException, SQLException, Exception{
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                datasetPath + systemName);        
    }
    
    @When("the WebAPI Starts Up")
    public final void theWebAPIStartsUp(){
        //servletContext = null;
        //startUp = true;
        try 
        {
            servletContext = Requirements.getMockServletContext();
            Field context = testConsolidationResource
                                .getClass().getDeclaredField("context");
            context.setAccessible(true);
            context.set(testConsolidationResource, servletContext);
        } catch (/*RuntimeError*/Exception ex) { 
            //startUp = false;
            Assert.fail("Cannot Start the WebAPI");
        }
    }
    
    @Then("I should have a Consolidation Resource")
    public final void IShouldHaveAConsolidationResource()
    throws SecurityException, NoSuchFieldException {
        assertNotNull(testConsolidationResource);        
        assertNotNull(testConsolidationResource
                .getClass()
                .getDeclaredField("context"));    
    }
    
    @Then("I should have {$count} transaction list remaining")
    public final void IShouldHaveTransactionList(final int count)
    throws IllegalArgumentException, SecurityException,
           IllegalAccessException, NoSuchFieldException{
        Field field =
            testConsolidationResource.getClass().getDeclaredField("context");
        field.setAccessible(true);
        
        ServletContext context =
            (ServletContext)field.get(testConsolidationResource);
        @SuppressWarnings("unchecked")
        List<TransactionInfo> actualTransList =
            (List<TransactionInfo>)context.getAttribute(
                    GlobalConstant.TRANSACTIONINFO);
        
        assertThat(actualTransList.size(), is(equalTo(count)));
    }
    private DcRsp dcRsp = null;
    @When("I call consolidation to {$count} threads")
    public final void ICallConsolidationToThreads(final int count){
        int i = 0;
        while ( i < count ) {
                dcRsp = testConsolidationResource.consolidate();
                i++;
        }
    }
    
    @Then("I should have {$count} consolidated items")
    public final void IShouldHaveConlidatedItems(final int count)
    throws DataSetException{
        ITable actuITable = dbInit.getQuery("TXL_POSLOG", 
                "Select COUNT(status) as count" +
                " FROM TXL_POSLOG where status = 1");
        
        assertThat("The number of POSLog consolidated ",
                (Integer)actuITable.getValue(0, "count"), is(equalTo(count)));
    }
    
    
    @Then("I should get the following TXL_POSLOG status of"
            + " {$status}: $expecteditem")
    public final void IShouldGetTheFollowingTXL_POSLOGStatuOf(
            final String status, final ExamplesTable expecteditems)
    throws DataSetException{
        
        ITable actualTable;
        StringBuilder strbuilder;
        
        int i =0;
        for (Map<String, String> row : expecteditems.getRows()){
            strbuilder = new StringBuilder();
            strbuilder.append("Select status from TXL_POSLOG where ")
            .append("corpid = ").append(row.get("corpid"))
            .append(" and ").append("storeid = ").append(row.get("storeid"))
            .append(" and ").append("termid = ").append(row.get("termid"))
            .append(" and ").append("txid = ").append(row.get("txid"));
            
            String str = strbuilder.toString();
            
            actualTable = dbInit.getQuery("TXL_POSLOG", strbuilder.toString());
            assertThat((String)actualTable.getValue(0, "status"),
                    is(equalTo(status)));
            i++;
        }
    }
    
    @Then("I should get {$dcRspStatus} of DcRsp status")
    public void iShouldGetStatus(int dcRspStatus){
        Assert.assertEquals(dcRspStatus, dcRsp.getStatus());
    }
}
