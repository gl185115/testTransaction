package ncr.res.mobilepos.pricing.dao.test;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.dao.SQLServerItemDAO;
import ncr.res.mobilepos.pricing.model.Department;
import ncr.res.mobilepos.pricing.model.Item;
import ncr.res.mobilepos.pricing.model.ItemMaintenance;
import ncr.res.mobilepos.pricing.model.test.ItemModelSteps;
import ncr.res.mobilepos.pricing.resource.ItemResource;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

@SuppressWarnings("deprecation")
public class SQLServerItemDAOSteps extends Steps {
    private SQLServerItemDAO itemdao;
    private List<Item> items = null;
    private static DBInitiator dbInit = null;
    ResultBase resultCode;
    Department department;    
        
    @BeforeScenario
    public static void setUpClass() throws Exception {
         Requirements.SetUp();
         dbInit = new DBInitiator("SQLServerItemDAOSteps", DATABASE.RESMaster);
         GlobalConstant.setCorpid("000000000000");
         GlobalConstant.setMaxSearchResults(5);
    }
    
    @AfterScenario
    public final void TearDown(){
        Requirements.TearDown();
        GlobalConstant.setCorpid("");
    }
    
    @Given("entries for {$dataset} in database")
    public final void initdatasets(final String dataset) throws Exception{
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/pricing/dao/test/" + dataset + ".xml");
    }
    
    @Given("entries for {$departments} in departments")
    public final void initdatasetsDpt(final String departments)
    throws Exception{
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/pricing/dao/test/"
                + departments + ".xml");
    }
    
    @Given("I have an Item DAO Controller")
    public final void IHaveAnItemDAOController(){
        try{    
            itemdao = new SQLServerItemDAO();
        }
        catch(DaoException e){
            System.out.println(e.getMessage());
        }         
    }
    
    @Then("It should have a Database Manager")
    public final void IHaveADBManager(){
        assertNotNull(itemdao.getDbManager());
    }
    
    @When("I get Item By  storeid {$storeid} and PLU {$pluCode}")
    public final void IGetItemByPLU(final String companyid, final String storeid, final String pluCode, final String businessDate){
        try{
            item = itemdao.getItemByPLU(storeid, pluCode, companyid, Item.ROUND_DOWN, businessDate);
            items = new ArrayList<Item>();
            items.add(item == null ? new Item(): item);
        }
        catch(DaoException e){
            items = null;            
        }
    }
        
    @Then("I must have item: $item")
    public final void gotItems(final ExamplesTable item){
        int i = 0;
        for(Map<String, String> expecedItem : item.getRows()){
            assertThat("Compare the Items PluCode", items.get(i).getItemId(),
                    is(equalTo(expecedItem.get("ItemID"))));
            assertThat("Compare the Items DescriptionEN", 
            		items.get(i).getDescription().getEn(),
                    is(equalTo(expecedItem.get("DescriptionEN"))));
            assertThat("Compare the Items DescriptionJP", 
            		items.get(i).getDescription().getJa(),
                    is(equalTo(expecedItem.get("DescriptionJP"))));
            assertThat("Compare the Items Sales Regular Price",
                    items.get(i).getRegularSalesUnitPrice(),
                    is(equalTo(Double.parseDouble(
                            expecedItem.get("RegularSalesUnitPrice")))));
            assertThat("Compare the Items Department", items.get(i).getDepartment(),
                    is(equalTo(expecedItem.get("Department"))));
            assertThat("Compare the Items Class", items.get(i).getItemClass(),
                    is(equalTo(expecedItem.get("Class"))));
            assertThat("Compare the Items Line", items.get(i).getLine(),
                    is(equalTo(expecedItem.get("Line"))));
            assertThat("Compare the Items Discount", "" + items.get(i).getDiscount(),
                    is(equalTo(expecedItem.get("Discount"))));
            assertThat("Compare the Items Discount Amount",
                    items.get(i).getDiscountAmount(),
                    is(equalTo(Double.parseDouble(
                            expecedItem.get("DiscountAmount")))));
            assertThat("Compare the Items Actual Sales Price",
                    items.get(i).getActualSalesUnitPrice(),
                    is(equalTo(Double.parseDouble(
                            expecedItem.get("ActualSalesPrice")))));
            i++;
        }    
    }    
    private Item item = null;
    @When("I change the price of Item storeid {$storeid}"
            + " and PLU {$pluCode} to {$newPrice}")
    public final void IChangeItemPriceByPLU(final String storeid,
            final String pluCode, final String newPrice){
    	ItemResource itemResource = new ItemResource();
    	ItemMaintenance itemMaintenance = itemResource.changePrice(storeid, pluCode, newPrice);
    	
    	item = itemMaintenance.getItem();
    }
        
    @Then("I must have items with price {$newPrice}.")
    public final void gotItems(final double newPrice){
        assertFalse(ItemModelSteps.HasErrors(item));
        Assert.assertEquals(newPrice, item.getRegularSalesUnitPrice());
        
    }    
    
    @Then("I must have no item.")
    public final void gotNoItems(){        
        Assert.assertNull(item);
    }
    
    @When("I get the list of Items with storeid {$storeid} and key {$key} and name {$name}")
    public final void getListItems(final String storeid, final String key, final String name){
        String keyTemp = key.equals("null")?null:key;
        String nameTemp = name.equals("null")?null:name;
         try {
            items = itemdao.listItems(storeid, keyTemp, nameTemp, Item.ROUND_DOWN, GlobalConstant.getMaxSearchResults());
        } catch (DaoException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    
    @Then("the list of Items are: $expectedItems")
    public final void getListOfItems(final ExamplesTable expecetdItems){
        assertThat("Expect the number of Item Retrieved", 
                items.size(), is(equalTo(expecetdItems.getRowCount())));
        this.gotItems(expecetdItems);
    }
    
    /*@When("I get department details for DPT{$dpt} in STOREID{$storeid}")
    public final void getDptDetails(final String dpt, final String storeid){
        department = new Department();
        resultCode = new ResultBase();
        try {
            department = itemdao.getDepartmentDetails(dpt, storeid);            
        } catch (DaoException e) {            
            // TODO Auto-generated catch block
            e.printStackTrace();            
        }
    }
    
    @Then("I should get department model with properties: $expected")
    public final void AssertGetDptModel(final ExamplesTable expected){        
        for (Map<String, String> dptInfo : expected.getRows()) {
            assertThat("Compare Department", dptInfo.get("Dpt"), 
                    is(equalTo(department.getDpt())));
            assertThat("Compare Department Name", dptInfo.get("DptName"), 
                    is(equalTo(department.getDptName())));
            assertThat("Compare Kana Name", dptInfo.get("DptKanaName"), 
                    is(equalTo(department.getDptKanaName())));
            assertThat("Compare Category", dptInfo.get("Category"), 
                    is(equalTo(department.getCategory())));        
        }
    }       
    
    @Then("the department model should be not null")
    public final void departmentNotNull(){
        assertNotNull(department);
    }*/
    
    @Then("the resultcode should be $resultCode")
    public final void resultCodeShouldBe(final String expectedresultCode){
        int res = department.getNCRWSSResultCode();        
        assertThat(Integer.valueOf(expectedresultCode),is(equalTo(res)));
    }
}
