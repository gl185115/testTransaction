package ncr.res.mobilepos.classinfo.dao.test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;



import java.util.List;
import java.util.Map;

import org.junit.Assert;

import ncr.res.mobilepos.classinfo.dao.SQLServerClassInfoDAO;
import ncr.res.mobilepos.classinfo.model.ClassInfo;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class SQLServerClassInfoDAOListSteps extends Steps {
    private SQLServerClassInfoDAO classInfoDAO;
    private List<ClassInfo> classInfos = null;
    private ClassInfo classInfo = null;
    private static DBInitiator dbInit = null;
    ResultBase resultCode;  
        
    @BeforeScenario
    public static void setUpClass() throws Exception {
         Requirements.SetUp();
         dbInit = new DBInitiator("SQLServerClassInfoDAOListSteps", DATABASE.RESMaster);          
         dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                 "test/ncr/res/mobilepos/classinfo/dao/test/"
                 + "MST_STOREINFO_LIST.xml"); 
         dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                 "test/ncr/res/mobilepos/classinfo/dao/test/"
                 + "MST_DPTINFO_LIST.xml");      
         dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                 "test/ncr/res/mobilepos/classinfo/dao/test/"
                 + "MST_LINEINFO_LIST.xml");           
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
                "test/ncr/res/mobilepos/classinfo/dao/test/" + dataset + ".xml");
    }   
    
    
    @Given("I have a ClassInfoDAO Controller")
    public final void IHaveAClassInfoDAOController(){
        try{    
        	classInfoDAO = new SQLServerClassInfoDAO();
        }
        catch(DaoException e){
            System.out.println(e.getMessage());
        }         
    }
    
    @Then("It should have a Database Manager")
    public final void IHaveADBManager(){
        assertNotNull(classInfoDAO.getDbManager());
    }    
    
        
    
    @Then("I must have no classInfo.")
    public final void gotNoCs(){        
        Assert.assertNull(classInfo);
    }
    
    @When("I get the list of Classes with storeid {$storeid} and dpt {$dpt} and key {$key} and name {$name} and limit {$limit}")
    public final void getClassInfoList(final String storeid, final String dpt, final String key, final String name, final String limit){
        String keyTemp = (key == null || key.equals("null"))?null:key;
        String nameTemp = (name == null || name.equals("null"))?null:name;
        int limitTemp = (limit == null || limit.equals("null"))?0: Integer.parseInt(limit);
         try {
            classInfos = classInfoDAO.listClasses(storeid, dpt, keyTemp, nameTemp, limitTemp);
        } catch (DaoException e) {           
            e.printStackTrace();
        }
    }
    
    @Then("the list of Classes are: $expectedClasses")
    public final void getListOfClasses(final ExamplesTable expectedClasses){
        assertThat("Expect the number of Classes Retrieved", 
                classInfos.size(), is(equalTo(expectedClasses.getRowCount())));
        this.gotClasses(expectedClasses);
    }
    
    @Then("I must have classInfo: $classInfo")
    public final void gotClasses(final ExamplesTable classInfo){
        int i = 0;
        for(Map<String, String> expecedItem : classInfo.getRows()){
            assertThat("Compare the Class", classInfos.get(i).getItemClass(),
                    is(equalTo(expecedItem.get("Class"))));
            assertThat("Compare the Class DescriptionEN", 
            		classInfos.get(i).getDescription().getEn(),
                    is(equalTo(expecedItem.get("DescriptionEN"))));
            assertThat("Compare the Class DescriptionJP", 
            		classInfos.get(i).getDescription().getJa(),
                    is(equalTo(expecedItem.get("DescriptionJP"))));           
            assertThat("Compare the Class Department", classInfos.get(i).getDepartment(),
                    is(equalTo(expecedItem.get("Department"))));
            assertThat("Compare the Class Line", classInfos.get(i).getLine(),
                    is(equalTo(expecedItem.get("Line"))));            
            assertThat("Compare the Class TaxType", "" + classInfos.get(i).getTaxType(),
                    is(equalTo(expecedItem.get("TaxType"))));
            assertThat("Compare the Class TaxRate", "" + classInfos.get(i).getTaxRate(),
                    is(equalTo(expecedItem.get("TaxRate"))));
            assertThat("Compare the Class DiscountType", "" + classInfos.get(i).getDiscountType(),
                    is(equalTo(expecedItem.get("DiscountType"))));
            assertThat("Compare the Class ExceptionFlag", "" + classInfos.get(i).getExceptionFlag(),
                    is(equalTo(expecedItem.get("ExceptionFlag"))));
            assertThat("Compare the Class DiscountFlag", "" + classInfos.get(i).getDiscountFlag(),
                    is(equalTo(expecedItem.get("DiscountFlag"))));
            assertThat("Compare the Class Discount Amount",
            		classInfos.get(i).getDiscountAmount(),
                    is(equalTo(Double.parseDouble(
                            expecedItem.get("DiscountAmount")))));   
            assertThat("Compare the Class Discount Rate",
            		classInfos.get(i).getDiscountRate(),
                    is(equalTo(Double.parseDouble(
                            expecedItem.get("DiscountRate")))));   
            assertThat("Compare the Class AgeRestrictedFlag", "" + classInfos.get(i).getAgeRestrictedFlag(),
                    is(equalTo(expecedItem.get("AgeRestrictedFlag"))));
            assertThat("Compare the Class InheritFlag", "" + classInfos.get(i).getInheritFlag(),
                    is(equalTo(expecedItem.get("InheritFlag"))));
            assertThat("Compare the Class SubSmallInt5", "" + classInfos.get(i).getSubSmallInt5(),
                    is(equalTo(expecedItem.get("SubSmallInt5"))));            
                   
            i++;
        }    
    }    
    
}
