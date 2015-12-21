package ncr.res.mobilepos.promotion.dao.test;

import static org.junit.Assert.assertNotNull;

import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DBInitiator;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.helper.DBInitiator.DATABASE;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.promotion.dao.SQLServerMixMatchDAO;
import ncr.res.mobilepos.promotion.model.GroupMixMatchData;
import ncr.res.mobilepos.promotion.model.NormalMixMatchData;

import org.dbunit.operation.DatabaseOperation;
import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class SQLServerMixMatchDAOTestSteps extends Steps{
    private SQLServerMixMatchDAO mixMatchDao;
    private NormalMixMatchData mixMatchData = null;
    private GroupMixMatchData groupMixMatchData = null;
    private static DBInitiator dbInit = null;
    ResultBase resultCode;    

    @BeforeScenario
    public static void setUpClass() throws Exception {
         Requirements.SetUp();
         dbInit = new DBInitiator("SQLServerItemDAOSteps", DATABASE.RESMaster);
         GlobalConstant.setMaxSearchResults(5);
    }

    @AfterScenario
    public final void TearDown() {
        Requirements.TearDown();
    }

    @Given("entries for {$mixMatch} in MST_MIXMATCH")
    public final void initdatasetsDpt(final String mixMatch)
    throws Exception {
        dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                "test/ncr/res/mobilepos/promotion/dao/test/"
                + mixMatch + ".xml");
    }

    @Given("I have a MixMatch DAO Controller")
    public final void IHaveAMixMatchDAOController() {
        try {
            mixMatchDao = new SQLServerMixMatchDAO();
            dbInit.ExecuteOperation(DatabaseOperation.CLEAN_INSERT,
                    "test/ncr/res/mobilepos/promotion/datasets/mst_bizday.xml");
        } catch(Exception e) {
            System.out.println(e.getMessage());
        }
    }

    @Then("should have a Database Manager")
    public final void haveDbManager() {
        assertNotNull(mixMatchDao.getDbManager());
    }

    @When("I get Mix Match Data with storeid{$storeid} and mixmatchcode{$mixmatchcode}")
    public final void getMixMatchData(final String companyid, final String storeid, final String mixmatchcode) {
        try {
            mixMatchData = (NormalMixMatchData) mixMatchDao.getNormalMixMatchData(companyid, storeid, mixmatchcode);
        } catch (DaoException e) {
            System.out.println(e.getMessage());
        }
    }
    
    @When("I get Group Mix Match Data with storeid{$storeid} and mixmatchcode{$mixmatchcode}")
    public final void getGroupMixMatchData(final String companyid, final String storeid, final String mixmatchcode){
    	try {
            groupMixMatchData = mixMatchDao.getGroupMixMatchData(companyid, storeid, mixmatchcode);
        } catch (DaoException e) {
            System.out.println(e.getMessage());
        }
    }

    @SuppressWarnings("deprecation")
	@Then("I should get MixMatch data with properties: $expected")
    public final void AssertGetMixMatchData(final ExamplesTable expected) {
        int subZ =0, subO =1;
        for (Map<String, String> mixMatch : expected.getRows()) {
            Assert.assertEquals(mixMatch.get("code"),
                    String.valueOf(mixMatchData.getCode()));
            Assert.assertEquals(mixMatch.get("startdate"),
                    String.valueOf(mixMatchData.getStartDate()));
            Assert.assertEquals(mixMatch.get("enddate"),
                    String.valueOf(mixMatchData.getEndDate()));
            Assert.assertEquals(mixMatch.get("name"),
                    String.valueOf(mixMatchData.getName()));
            Assert.assertEquals(mixMatch.get("type"),
                    String.valueOf(mixMatchData.getType()));
            Assert.assertEquals(mixMatch.get("mustbuyflag"),
                    String.valueOf(mixMatchData.getMustBuyFlag()));

            Assert.assertEquals(mixMatch.get("quantity1"),
                    String.valueOf(mixMatchData.getQuantity()[subZ]));
            Assert.assertEquals(mixMatch.get("quantity2"),
                    String.valueOf(mixMatchData.getQuantity()[subO]));

            Assert.assertEquals(mixMatch.get("discountprice1"),
                    String.valueOf(mixMatchData.getDiscountprice()[subZ]));
            Assert.assertEquals(mixMatch.get("discountprice2"),
                    String.valueOf(mixMatchData.getDiscountprice()[subO]));

            Assert.assertEquals(mixMatch.get("empprice11"),
                    String.valueOf(mixMatchData.getEmpprice1()[subZ]));
            Assert.assertEquals(mixMatch.get("empprice21"),
                    String.valueOf(mixMatchData.getEmpprice1()[subO]));

            Assert.assertEquals(mixMatch.get("empprice12"),
                    String.valueOf(mixMatchData.getEmpprice2()[subZ]));
            Assert.assertEquals(mixMatch.get("empprice22"),
                    String.valueOf(mixMatchData.getEmpprice2()[subO]));

            Assert.assertEquals(mixMatch.get("empprice13"),
                    String.valueOf(mixMatchData.getEmpprice3()[subZ]));
            Assert.assertEquals(mixMatch.get("empprice23"),
                    String.valueOf(mixMatchData.getEmpprice3()[subO]));
        }
    }
    
    @SuppressWarnings("deprecation")
	@Then("I should get Group MixMatch data with properties: $expected")
    public final void AssertGetGroupMixMatchData(final ExamplesTable expected) {
        for (Map<String, String> mixMatch : expected.getRows()) {
            Assert.assertEquals(mixMatch.get("code1"),
                    String.valueOf(groupMixMatchData.getSubCodes().get(0)));
            Assert.assertEquals(mixMatch.get("code2"),
            		 String.valueOf(groupMixMatchData.getSubCodes().get(1)));
            Assert.assertEquals(mixMatch.get("startdate"),
                    String.valueOf(groupMixMatchData.getStartDate()));
            Assert.assertEquals(mixMatch.get("enddate"),
                    String.valueOf(groupMixMatchData.getEndDate()));
            Assert.assertEquals(mixMatch.get("name"),
                    String.valueOf(groupMixMatchData.getName()));
            Assert.assertEquals(mixMatch.get("groupcode"),
                    String.valueOf(groupMixMatchData.getCode()));
            Assert.assertEquals(mixMatch.get("type"),
                    String.valueOf(groupMixMatchData.getType()));
        }
    }
    
    @SuppressWarnings("deprecation")
	@Then("Mix Match Data is null.")
    public final void gotNoMixMatchData() {
        Assert.assertNull(mixMatchData);
    }
    
    @SuppressWarnings("deprecation")
	@Then("Group Mix Match Data is null.")
    public final void gotNoGroupMixMatchData() {
    	Assert.assertNull(groupMixMatchData);
    }

}
