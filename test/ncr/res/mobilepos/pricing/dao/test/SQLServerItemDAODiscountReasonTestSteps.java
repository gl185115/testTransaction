package ncr.res.mobilepos.pricing.dao.test;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertThat;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.dao.SQLServerItemDAO;
import ncr.res.mobilepos.pricing.model.ReasonData;
import ncr.res.mobilepos.pricing.model.ReasonDataList;
import ncr.res.mobilepos.pricing.model.ItemReasonDataList;
import ncr.res.mobilepos.pricing.model.OverrideReasonDataList;
import ncr.res.mobilepos.pricing.model.TransactionReasonDataList;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class SQLServerItemDAODiscountReasonTestSteps extends Steps{
	private SQLServerItemDAO itemdao;
	private static DBInitiator dbInit = null;
    ResultBase resultCode;
    ReasonDataList reasonDataList;
    TransactionReasonDataList txnDscntRsnList;
    ItemReasonDataList itemDscntRsnList;
    OverrideReasonDataList overrideDscntRsnList;
    private List<ReasonData> txDiscountReason;
    private List<ReasonData> itemDiscountReason;
    private List<ReasonData> overrideDiscountReason;
    
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
        assertNotNull(itemdao.getDbManager());
    }
    
    @When("I get Discount Reason Codes")
    public final void IGetDiscountReason(){
        try{
        	reasonDataList = new ReasonDataList();
        	reasonDataList = itemdao.getDiscountReason();
        }
        catch(DaoException e){
        	reasonDataList = null;            
        }
    }
    	   
    @Then("I must have DiscountReasonList")
    public final void mustHaveDiscntRsnList(){   
    	assertNotNull(reasonDataList);
    }
    
    @When("I get Discount Reason for Transaction")
    public final void iGetDiscountReasonForTransaction() {
    	txnDscntRsnList = reasonDataList.getTxReasonDataList();
    }
    
    @Then("I must have the transaction discount reason data $txDiscount")
    public final void iMustHaveTxFollowingData(final ExamplesTable txDiscount) {
    	txDiscountReason = new ArrayList<ReasonData>();
    	txDiscountReason = txnDscntRsnList.getReasonData();
    	int i = 0;
        for(Map<String, String> txDiscountReasons : txDiscount.getRows()){
            assertThat("Compare the Type", txDiscountReason.get(i).getType(),
                    is(equalTo(txDiscountReasons.get("type"))));
            assertThat("Compare the Code", txDiscountReason.get(i).getCode(),
            		is(equalTo(txDiscountReasons.get("code"))));
            assertThat("Compare the Display Name", txDiscountReason.get(i).getDisplayname(),
            		is(equalTo(txDiscountReasons.get("displayname"))));
            i++;
        }        	
    }   
    
    @When("I get Discount Reason for Item")
    public final void iGetDiscountReasonForItem() {
    	itemDscntRsnList = reasonDataList.getItemReasonDataList();
    }

    @Then("I must have the item discount reason data $itemDiscount")
    public final void iMustHaveItemFollowingData(final ExamplesTable itemDiscount) {
    	itemDiscountReason = new ArrayList<ReasonData>();
    	itemDiscountReason = itemDscntRsnList.getReasonData();
    	int i = 0;
        for(Map<String, String> itemDiscountReasons : itemDiscount.getRows()){
            assertThat("Compare the Type", itemDiscountReason.get(i).getType(),
                    is(equalTo(itemDiscountReasons.get("type"))));
            assertThat("Compare the Code", itemDiscountReason.get(i).getCode(),
            		is(equalTo(itemDiscountReasons.get("code"))));
            assertThat("Compare the Display Name", itemDiscountReason.get(i).getDisplayname(),
            		is(equalTo(itemDiscountReasons.get("displayname"))));
            i++;
        }        	
    }
    
    @When("I get Discount Reason for Price Override")
    public final void iGetDiscountReasonForPriceOverride() {
    	overrideDscntRsnList = reasonDataList.getOverrideReasonDataList();
    }

    @Then("I must have the price override discount reason data $overrideDiscount")
    public final void iMustHaveOverrideFollowingData(final ExamplesTable overrideDiscount) {
    	overrideDiscountReason = new ArrayList<ReasonData>();
    	overrideDiscountReason = overrideDscntRsnList.getReasonData();
    	int i = 0;
        for(Map<String, String> overrideDiscountReasons : overrideDiscount.getRows()){
            assertThat("Compare the Type", overrideDiscountReason.get(i).getType(),
                    is(equalTo(overrideDiscountReasons.get("type"))));
            assertThat("Compare the Code", overrideDiscountReason.get(i).getCode(),
            		is(equalTo(overrideDiscountReasons.get("code"))));
            assertThat("Compare the Display Name", overrideDiscountReason.get(i).getDisplayname(),
            		is(equalTo(overrideDiscountReasons.get("displayname"))));
            i++;
        }        	
    }   
}
