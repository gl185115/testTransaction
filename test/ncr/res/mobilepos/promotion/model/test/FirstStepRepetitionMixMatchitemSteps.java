package ncr.res.mobilepos.promotion.model.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.pricing.model.Description;
import ncr.res.mobilepos.promotion.model.Discount;
import ncr.res.mobilepos.promotion.model.FirstStepRepetitionMixMatchItem;
import ncr.res.mobilepos.promotion.model.NormalMixMatchData;
import ncr.res.mobilepos.promotion.model.Sale;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

/**
 * The Class FirstStepRepetitionMixMatchitemSteps.
 */
public class FirstStepRepetitionMixMatchitemSteps extends Steps {

    /** The first step rep. */
    FirstStepRepetitionMixMatchItem firstStepRep = null;

    /** The mm data. */
    NormalMixMatchData mmData = new NormalMixMatchData();

    /** The sales. */
    List<Sale> sales = new ArrayList<Sale>();

    /** The discounts. */
    private List<Discount> discounts = null;

    /**
     * Sets the up class.
     * 
     * @throws Exception
     *             the exception
     */
    @BeforeScenario
    public final void setUpClass() throws Exception {
        Requirements.SetUp();
    }

    /**
     * Tear down class.
     */
    @AfterScenario
    public final void TearDownClass() {
        Requirements.TearDown();
    }

    /**
     * Given mm data.
     * 
     * @param exampleTables
     *            the example tables
     */
    @Given("a MixMatchData of the following: $exampleTables")
    public final void givenMMData(ExamplesTable exampleTables) {
        for (Map<String, String> mmDataTemp : exampleTables.getRows()) {
            String MMCode = mmDataTemp.get("MMCode");
            int MMQ = Integer.parseInt(mmDataTemp.get("MMQ"));
            int MMP = Integer.parseInt(mmDataTemp.get("MMP"));
            int type = Integer.parseInt(mmDataTemp.get("TYPE"));
            mmData.setType(type);
            mmData.setCode(MMCode);
            mmData.setQuantity(new int[] { MMQ });
            mmData.setDiscountprice(new long[] { MMP });
            mmData.setName(mmDataTemp.get("MMDescription"));
            firstStepRep = new FirstStepRepetitionMixMatchItem(
                    mmData.getCode());
            firstStepRep.setMixMatchData(mmData);
        }
    }

    // @Given("an items of the following: $exampleTables")
    // public final void givenItems(ExamplesTable exampleTables) {
    // for (Map<String, String> pluItem : exampleTables.getRows()) {
    // String name = pluItem.get("name");
    // int price = Integer.parseInt(pluItem.get("price"));
    // String code = pluItem.get("code");
    // Sale sale = new Sale();
    // sale.setDescription(name);
    // sale.setRegularsalesunitprice(price);
    // sale.setMixmatchcode(code);
    // items.add(sale);
    // }
    // }

    /**
     * Adds the sale.
     * 
     * @param exampleTables
     *            the example tables
     */
    @When("I add item to sale: $exampleTables")
    public final void addSale(ExamplesTable exampleTables) {
        for (Map<String, String> data : exampleTables.getRows()) {
            Sale sale = new Sale();
            
            // description
            Description description = new Description();
            description.setEn(data.get("nameEN"));
            description.setJa(data.get("nameJP"));            
            sale.setDescription(description);
            
            sale.setRegularSalesUnitPrice(Integer.parseInt(data.get("price")));
            sale.setQuantity(Integer.parseInt(data.get("qty")));
            sale.setItemEntryId(data.get("itementryid"));
            firstStepRep.addItem(sale);
        }
    }

    /**
     * Compute first step rep.
     */
    @When("I compute discount of 1stStepRepetition")
    public final void computeFirstStepRep() {
        discounts = firstStepRep.computeDiscount();
    }

    /**
     * Test no of discount.
     * 
     * @param expectedCnt
     *            the expected cnt
     */
    @Then("I should get {$expectedCnt} discounts")
    public final void testNoOfDiscount(int expectedCnt) {
        Assert.assertEquals(expectedCnt, discounts.size());
    }

    /**
     * Test discounts.
     * 
     * @param exampleTables
     *            the example tables
     */
    @Then("I should get list of discounts of the following: $exampleTables")
    public final void testDiscounts(ExamplesTable exampleTables) {
        Assert.assertEquals("Compare size of discounts", exampleTables
                .getRows().size(), discounts.size());
        int i = 0;
        for (Map<String, String> data : exampleTables.getRows()) {
            Assert.assertEquals("Compare rewardid " + i, Integer.parseInt(data
                    .get("rewardid")), discounts.get(i).getRewardID());
            Assert.assertEquals("Compare earnedrewardid " + i, data
                    .get("earnedrewardid"), discounts.get(i)
                    .getEarnedRewardID());
            Assert.assertEquals("Compare promotioncode " + i, data
                    .get("promotioncode"), discounts.get(i).getPromotionCode());
            Assert.assertEquals("Compare discdescription " + i, data
                    .get("discdescription"), discounts.get(i)
                    .getDiscountDescription());
            Assert.assertEquals("Compare itementryid " + i,
                    data.get("itementryid"), discounts.get(i).getItemEntryID());
            Assert.assertEquals("Compare unitdiscountamt " + i, Integer
                    .parseInt(data.get("unitdiscountamt")), discounts.get(i)
                    .getUnitDiscountAmount());
            i++;
        }
    }
}
