package ncr.res.mobilepos.line.dao.test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;






import java.util.List;
import java.util.Map;

import org.junit.Assert;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.line.dao.SQLServerLineDAO;
import ncr.res.mobilepos.line.model.Line;
import ncr.res.mobilepos.model.ResultBase;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class SQLServerLineDAOListSteps extends Steps {
    private SQLServerLineDAO lineDAO;
    private List<Line> lineList = null;
    private Line line = null;
    private static DBInitiator dbInit = null;
    private Logger ioWriter;
    ResultBase resultCode;    
    
    public SQLServerLineDAOListSteps(){
    	this.ioWriter = (Logger) Logger.getInstance();
    }
    
    @BeforeScenario
    public static void setUpClass() throws Exception {
         Requirements.SetUp();
         dbInit = new DBInitiator("SQLServerLineDAOListSteps", DATABASE.RESMaster);
         //initialize department
         dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                 "test/ncr/res/mobilepos/line/resource/test/"
                 + "MST_DPTINFO_LIST.xml");    
         GlobalConstant.setMaxSearchResults(4);
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
        GlobalConstant.setCorpid("");
    }
    
    @Given("entries for {$dataset} in database")
    public final void initdatasets(final String dataset) throws Exception{
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/line/dao/test/" + dataset + ".xml");
    }
    
    @Given("I have a LineDAO Controller")
    public final void IHaveALineDAOController(){
        try{    
        	lineDAO = new SQLServerLineDAO();
        }
        catch(DaoException e){
            System.out.println(e.getMessage());
        }         
    }
    
    @Then("It should have a Database Manager")
    public final void IHaveADBManager(){
        assertNotNull(lineDAO.getDbManager());
    } 
        
    
    @Then("I must have no line.")
    public final void gotNoCs(){        
        Assert.assertNull(line);
    }
    
    @When("I get the list of Lines with storeid {$storeid} and dpt {$dpt} and key {$key} and name {$name} and limit {$limit}")
    public final void getLineList(final String storeid, final String dpt, final String key, final String name, final String limit){
        String keyTemp = (key == null || key.equals("null"))?null:key;
        String nameTemp = (name == null || name.equals("null"))?null:name;
        int limitTemp = (limit == null || limit.equals("null"))?0: Integer.parseInt(limit);
         try {
            lineList = lineDAO.listLines(storeid, dpt, keyTemp, nameTemp, limitTemp);
        } catch (DaoException e) { 
        	ioWriter.logAlert("SQLServerLineDAOListSteps", "SQLServerLineDAOListSteps.getLineList",
                    Logger.RES_EXCEP_DAO, "Failed to get the list of Lines.\n"
                            + e.getMessage());
        }
    }
    
    @Then("the list of Lines are: $expectedLines")
    public final void getListOfLines(final ExamplesTable expectedLines){
        assertThat("Expect the number of Lines Retrieved", 
                lineList.size(), is(equalTo(expectedLines.getRowCount())));
        this.gotLines(expectedLines);
    }
    
    @Then("I must have Line: $line")
    public final void gotLines(final ExamplesTable line){
        int i = 0;
        for(Map<String, String> expecedItem : line.getRows()){
            assertThat("Compare the Line", lineList.get(i).getLine(),
                    is(equalTo(expecedItem.get("Line"))));
            assertThat("Compare the Line DescriptionEN", 
            		lineList.get(i).getDescription().getEn(),
                    is(equalTo(expecedItem.get("DescriptionEN"))));
            assertThat("Compare the Line DescriptionJP", 
            		lineList.get(i).getDescription().getJa(),
                    is(equalTo(expecedItem.get("DescriptionJP"))));           
            assertThat("Compare the Line Department", lineList.get(i).getDepartment(),
                    is(equalTo(expecedItem.get("Department"))));          
            assertThat("Compare the Line TaxType", "" + lineList.get(i).getTaxType(),
                    is(equalTo(expecedItem.get("TaxType"))));
            assertThat("Compare the Line TaxRate", "" + lineList.get(i).getTaxRate(),
                    is(equalTo(expecedItem.get("TaxRate"))));
            assertThat("Compare the Line DiscountType", "" + lineList.get(i).getDiscountType(),
                    is(equalTo(expecedItem.get("DiscountType"))));
            assertThat("Compare the Line ExceptionFlag", "" + lineList.get(i).getExceptionFlag(),
                    is(equalTo(expecedItem.get("ExceptionFlag"))));
            assertThat("Compare the Line DiscountFlag", "" + lineList.get(i).getDiscountFlag(),
                    is(equalTo(expecedItem.get("DiscountFlag"))));
            assertThat("Compare the Line Discount Amount",
            		lineList.get(i).getDiscountAmount(),
                    is(equalTo(Double.parseDouble(
                            expecedItem.get("DiscountAmount")))));   
            assertThat("Compare the Line Discount Rate",
            		lineList.get(i).getDiscountRate(),
                    is(equalTo(Double.parseDouble(
                            expecedItem.get("DiscountRate")))));   
            assertThat("Compare the Line AgeRestrictedFlag", "" + lineList.get(i).getAgeRestrictedFlag(),
                    is(equalTo(expecedItem.get("AgeRestrictedFlag"))));
            assertThat("Compare the Line InheritFlag", "" + lineList.get(i).getInheritFlag(),
                    is(equalTo(expecedItem.get("InheritFlag"))));
            assertThat("Compare the Line SubSmallInt5", "" + lineList.get(i).getSubSmallInt5(),
                    is(equalTo(expecedItem.get("SubSmallInt5"))));            
                   
            i++;
        }    
    }    
    
}
