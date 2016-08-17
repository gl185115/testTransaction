package ncr.res.mobilepos.pricing.dao.test;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.dao.SQLServerItemDAO;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.steps.Steps;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import java.util.ArrayList;
import java.util.List;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.pricing.model.ItemReasonDataList;
import ncr.res.mobilepos.pricing.model.OverrideReasonDataList;
import ncr.res.mobilepos.pricing.model.ReasonData;
import ncr.res.mobilepos.pricing.model.ReasonDataList;
import ncr.res.mobilepos.pricing.model.TransactionReasonDataList;

import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.junit.Assert;

public class SQLServerItemDAODiscountButtonTestSteps extends Steps{
	private SQLServerItemDAO itemdao;
	private static DBInitiator dbInit = null;
    ResultBase resultCode;
    ReasonDataList discountButtonList;
    TransactionReasonDataList transactionDiscountButton;
    ItemReasonDataList itemDiscountButton;
    OverrideReasonDataList overrideDiscountButton;
	private List<ReasonData> ReasonData;
    
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
    	Assert.assertNotNull(itemdao.getDbManager());
    }

    @Then("I must have ReasonDataList")
    public final void mustHaveReasonDataListObject(){   
    	Assert.assertNotNull(discountButtonList);	
    }
    
    @Then("I must have successful ResultCode")
    public final void mustHaveSuccessfulResultCode(){   
    	Assert.assertThat("Compare the Result Code", 
        		discountButtonList.getNCRWSSResultCode() ,
                is(0));    	
    }
    
    @Then("I must have failed ResultCode")
    public final void mustHaveFailedResultCode(){   
    	Assert.assertThat("Compare the Result Code", 
        		discountButtonList.getNCRWSSResultCode() ,
                is(-5));    	
    }
    
    @Then("I must have TransactionReasonDataList")
    public final void mustHaveTransactionReasonDataList(){ 
    	Assert.assertNotNull(transactionDiscountButton);
    }
    
    @Then("I must have ItemReasonDataList")
    public final void mustHaveItemReasonDataList(){ 
    	Assert.assertNotNull(itemDiscountButton);
    }
    
    @Then("I must have a Null value for ReasonData")
    public final void mustHaveANullValueForReasonData(){ 
    	Assert.assertEquals(ReasonData.toString(), "[]");
    }

    @Then("I must have a Null value for ReasonData2")
    public final void mustHaveANullValueForReasonData2(){ 
    	Assert.assertEquals(ReasonData.toString(), "[]");
    }
    
    @Then("I must have a value of ReasonData")
    public final void iMustHaveAValueOfReasonData(){ 
    	Assert.assertNotNull(ReasonData);
    }
    
    @Then("I must have OverrideReasonDataList")
    public final void mustHaveOverrideReasonDataList(){ 
    	assertNotNull(overrideDiscountButton);
    }
    
    @When("I get ReasonDataList with error data")
    public final void iGetReasonDataListWithErrorData(){
        try{
        	discountButtonList = new ReasonDataList();
        	discountButtonList = itemdao.getDiscountButtons();
        }
        catch(DaoException e){
        	discountButtonList = null;            
        }
    }
    
    @When("I get Reason Discount Data List")
    public final void IGetReasonDiscountDataList(){
        try{
        	discountButtonList = new ReasonDataList();
        	discountButtonList = itemdao.getDiscountButtons();
        }
        catch(DaoException e){
        	discountButtonList = null;            
        }
    }
    
    @When("I get ReasonDatalist for transaction")
    public final void iGetReasonDataListForTransaction() {
        transactionDiscountButton = new TransactionReasonDataList();
        transactionDiscountButton= discountButtonList.getTxReasonDataList();
    }

    @When("I get ReasonDatalist for Item")
    public final void iGetReasonDataListForItem() {
        itemDiscountButton = new ItemReasonDataList();
        itemDiscountButton= discountButtonList.getItemReasonDataList();
    }

    @When("I get ReasonDatalist for Override")
    public final void iGetReasonDataListForOverride() {
        overrideDiscountButton = new OverrideReasonDataList();
        overrideDiscountButton= discountButtonList.getOverrideReasonDataList();
    }

    @When("I get Transaction for ReasonData")
    public final void iGetTransactionForReasonData() {  
    	ReasonData = new ArrayList<ReasonData>();
    	ReasonData = transactionDiscountButton.getReasonData();   
    }
    
    @When("I get Transaction for ReasonData2")
    public final void iGetTransactionForReasonData2() {  
    	ReasonData = new ArrayList<ReasonData>();
    	ReasonData = transactionDiscountButton.getReasonData2();   
    }
    
    @When("I get Item for ReasonData")
    public final void iGetItemForReasonData() {  
    	ReasonData = new ArrayList<ReasonData>();
    	ReasonData = itemDiscountButton.getReasonData();   
    }
 }
