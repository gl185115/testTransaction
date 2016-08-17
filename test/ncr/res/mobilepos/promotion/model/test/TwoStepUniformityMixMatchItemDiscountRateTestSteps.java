package ncr.res.mobilepos.promotion.model.test;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.Assert;
import ncr.res.mobilepos.helper.Requirements;
import ncr.res.mobilepos.pricing.model.Description;
import ncr.res.mobilepos.promotion.model.Discount;
import ncr.res.mobilepos.promotion.model.NormalMixMatchData;
import ncr.res.mobilepos.promotion.model.Sale;
import ncr.res.mobilepos.promotion.model.SecondStepUniformityMixMatchItemDiscountRate;

import org.jbehave.scenario.annotations.AfterScenario;
import org.jbehave.scenario.annotations.BeforeScenario;
import org.jbehave.scenario.annotations.Given;
import org.jbehave.scenario.annotations.Then;
import org.jbehave.scenario.annotations.When;
import org.jbehave.scenario.definition.ExamplesTable;
import org.jbehave.scenario.steps.Steps;

public class TwoStepUniformityMixMatchItemDiscountRateTestSteps extends Steps{
    
    SecondStepUniformityMixMatchItemDiscountRate secondStepUniformityDiscRate = null;
    
    NormalMixMatchData mixMatchData = new NormalMixMatchData();
    
    List<Sale> sales = new ArrayList<Sale>();
    
    private List<Discount> discounts = null;
    
    /**
     * Sets up the class.
     */
    @BeforeScenario
    public final void setUpClass() {
        Requirements.SetUp();
    }
    
    /**
     * Tear Down class.
     */
    @AfterScenario
    public final void TearDownClass() {
        Requirements.TearDown();
    }
    
    @Given("a MixMatchData of the following: $examplesTable")
    public final void givenMMData(ExamplesTable examplesTable) {
        for(Map<String, String> mmDataTemp : examplesTable.getRows()) {
            String MMCode = mmDataTemp.get("MMCode");
            int MMQ = Integer.parseInt(mmDataTemp.get("MMQ"));
            int MMDiscountRate = Integer.parseInt(mmDataTemp.get("MMDiscountRate"));
            int MMQ2 = Integer.parseInt(mmDataTemp.get("MMQ2"));
            int MMDiscountRate2 = Integer.parseInt(mmDataTemp.get("MMDiscountRate2"));
            
            mixMatchData.setCode(MMCode);
            mixMatchData.setQuantity(new int[] { MMQ, MMQ2 });
            mixMatchData.setDiscountprice(new long[] { MMDiscountRate, MMDiscountRate2 });
            mixMatchData.setName(mmDataTemp.get("MMDescription"));
            secondStepUniformityDiscRate = new SecondStepUniformityMixMatchItemDiscountRate(mixMatchData.getCode());
            secondStepUniformityDiscRate.setMixMatchData(mixMatchData);
        }
    }
    
    @When("I add item to Sale: $exampleTables")
    public final void addSale(ExamplesTable examplesTable) {
        for(Map<String, String> data : examplesTable.getRows()) {
            Sale sale = new Sale();
            
            // description
            Description description = new Description();
            description.setEn(data.get("nameEN"));
            description.setJa(data.get("nameJP"));            
            sale.setDescription(description);
            
            sale.setRegularSalesUnitPrice(Integer.parseInt(data.get("price")));
            sale.setQuantity(Integer.parseInt(data.get("quantity")));
            sale.setItemEntryId(data.get("itementryid"));
            secondStepUniformityDiscRate.addItem(sale);
        }
    }
    
    /**
     * Compute discount of 2nd Step Uniformity MixMatch Data Discount Rate
     */
    @When("I compute discount of 2ndStepUniformity")
    public final void compute2ndStepUniformity() {
        discounts = secondStepUniformityDiscRate.computeDiscount();
        System.out.println("DISCOUNTS: " + discounts.toString());
    }
    
    @Then("I should get {$expectedCount} discounts")
    public final void testOfNoDiscount(int expectedCount) {
        Assert.assertEquals(expectedCount, discounts.size());
    }
    
    @Then("I should get list of discounts of the following: $examplesTable")
    public final void testDiscounts(ExamplesTable examplesTable) {
        Assert.assertEquals("Compare size of discounts", examplesTable
                .getRows().size(), discounts.size());
        int i = 0;
        for (Map<String, String> data : examplesTable.getRows()) {
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
