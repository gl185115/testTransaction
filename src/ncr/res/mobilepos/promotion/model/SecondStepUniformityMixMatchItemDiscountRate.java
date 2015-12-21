package ncr.res.mobilepos.promotion.model;

import java.util.ArrayList;
import java.util.List;

import ncr.res.mobilepos.promotion.helper.SaleItemsHandler;

/**
 * Second Step Uniformity Mix Match Item Discount Rate.
 * @author rd185102
 * */

public class SecondStepUniformityMixMatchItemDiscountRate
                            extends SecondStepUniformityMixMatchItem {

/**
 * Discounts for the first step. Used for recalculating discounts.
 */
private List<Long> firstStepDiscounts = new ArrayList<Long>();
/**
 * Total of previous item sales.
 */
private int prevTotalSales = 0;
/**
 * Total of previous discounts.
 */
private int prevDiscountTotal = 0;
/**
 * Constant for first step.
 */
private static final int FIRST_STEP = 0;
/**
 * Constant for second step.
 */
private static final int SECOND_STEP = 1;
/**
 * Constant for Percent.
 */
private static final int PERCENT = 100;

/**
 * Determines if there is a first step that has to be updated.
 */
private boolean has1stStepProcessed = false;
/**
 * Determines whether 2nd step has triggered.
 */
private boolean has2ndStepProcessed = false;

/**
 * Custom Constructor.
 * @param codeToSet The Code to set.
 */
public SecondStepUniformityMixMatchItemDiscountRate(
                                    final String codeToSet) {
    super(codeToSet);
}

/**
 * gets the sale items to be discounted.
 * @param stepLevel the step level of the discount
 * @param startFromZero sets the index back to zero
 * @param itemList The List of Discountable sale.
 * @return List of items to be discounted
 */
private List<Sale> getDiscountedSale(
        final int stepLevel, final boolean startFromZero,
        final List<Sale> itemList) {
    if (startFromZero) {
        this.setCurrentIndex(0);
        this.setDiscntCntr(0);
        this.setItemsSaved(0);

        for (Sale sale : itemList) {
            sale.setDiscountCount(0);
        }
    }
    int numberOfDiscountItems =
        SaleItemsHandler.getSumSalesQuantity(itemList) - this.getItemsSaved();
    List<Sale> itemsToDiscount = new ArrayList<Sale>();

    for (int i = 0; i < numberOfDiscountItems; i++) {
        Sale currentSale = itemList.get(this.getCurrentIndex());
        currentSale.setDiscountCount(currentSale.getDiscountCount() + 1);
        itemsToDiscount.add(currentSale);
        if (currentSale.getDiscountCount() >= currentSale.getQuantity()) {
            this.setCurrentIndex(this.getCurrentIndex() + 1);
        }
    }
    return itemsToDiscount;
}
/**
 * Generates the proper discount per the item based on
     * the Mix Match Type.
 * @param mmItems   List of Items.
 * @param totalPrice   The total of the items to apply discount.
 * @param stepLevel    Level of the discount.
 * @return  Array of discounts.
 */
    private int[] calculateDiscount(final List<Sale> mmItems,
            final int totalPrice, final int stepLevel) {
        double mmPrice = this.getMixMatchData().getDiscountprice()[stepLevel];
        int[] mmdItem = new int[mmItems.size()];

            double mmPercent = mmPrice / PERCENT;
            double mmTotalDiscountAmt = Math.floor(mmPercent * totalPrice);
            int prevDiscounts = prevDiscountTotal;
            if (stepLevel == SECOND_STEP && has1stStepProcessed
                     && !has2ndStepProcessed) {
                mmTotalDiscountAmt -= prevDiscountTotal;
                prevDiscounts = 0;
            }
            for (int i = 0; i < mmItems.size(); i++) {
                double price = mmItems.get(i).getRegularSalesUnitPrice();
                double percentage = price / totalPrice;

                mmdItem[i] = (int) Math.round(percentage * mmTotalDiscountAmt);
            }

            int totalDiscount = 0;
            for (int i : mmdItem) {
                totalDiscount += i;
            }

            mmdItem[mmItems.size() - 1] +=
                  (mmTotalDiscountAmt - prevDiscounts) - totalDiscount;

        return mmdItem;
    }

    @Override
    protected final void resetComputation() {
        this.setCurrentIndex(0);
        this.setDiscntCntr(0);
        this.setItemsSaved(0);
        this.resetSalesDiscCnt();
        this.has1stStepProcessed = false;
        this.has2ndStepProcessed = false;
        this.prevDiscountTotal = 0;
        this.prevTotalSales = 0;
    }

    @Override
    protected final List<Discount> mixMatchComputation(
            final List<Sale> itemList) {
        //Initializations
        String mixMatchDescription = this.getMixMatchData().getName();
        int step1Qty = this.getMixMatchData().getQuantity()[FIRST_STEP];
        int step2Qty = this.getMixMatchData().getQuantity()[SECOND_STEP];

        List<Discount> promo = new ArrayList<Discount>();
        if (itemList == null || itemList.isEmpty()) {
            return promo;
        }
        double discountVal = 0;
        double discountedPrice = 0;
        List<Sale> itemsToDiscount = null;
        int totalSales = 0;
        int stepCounter = 0;
        int [] discountAmounts = null;

        if (SaleItemsHandler.getSumSalesQuantity(itemList) >= step2Qty) {

            //decide if step 2 has been applied
            if (!has2ndStepProcessed) {
                //decide if step 1 has been applied
                //this has to go first before the getting of itemsToDiscount
                //to preserve the this.getItemsSaved() value
                if (has1stStepProcessed && (firstStepDiscounts != null)) {
                }
                itemsToDiscount = this.getDiscountedSale(SECOND_STEP, true,
                        itemList);
                for (int i = 0; i < itemsToDiscount.size(); i++) {
                    totalSales += itemsToDiscount.get(i)
                                .getRegularSalesUnitPrice();
                }

                discountAmounts = this.calculateDiscount(itemsToDiscount,
                        totalSales, SECOND_STEP);

                stepCounter = step2Qty;

                for (int i = 0; i < stepCounter; i++) {
                    discountVal += discountAmounts[i];
                }

                discountedPrice = totalSales - discountVal;
                if (totalSales > discountedPrice) {
                    has2ndStepProcessed = true;
                }
            } else {
                //if step 2 already, discount directly
                itemsToDiscount = this.getDiscountedSale(SECOND_STEP, false,
                        itemList);

                for (int i = 0; i < itemsToDiscount.size(); i++) {
                    totalSales += itemsToDiscount.get(i)
                                .getRegularSalesUnitPrice();
                }

                totalSales = prevTotalSales + totalSales;

                discountAmounts = this.calculateDiscount(itemsToDiscount,
                        totalSales, SECOND_STEP);

                stepCounter = discountAmounts.length;

                for (int i = 0; i < stepCounter; i++) {
                     discountVal += discountAmounts[i];
                }

                discountedPrice = totalSales - discountVal;
                }

            if (!has2ndStepProcessed) {
                return promo;
            }

            String previousItemEntryId = "";
            int discountCounter = this.getDiscntCntr();
            int totalDiscount = 0;
            int index = 0;
            for (Sale sale : itemsToDiscount) {
                Discount discount = new Discount();
                discount.setDiscountDescription(mixMatchDescription);
                discount.setEarnedRewardID(
                        this.getMixMatchData().getCode());
                discount.setItemEntryID(sale.getItemEntryId());
                discount.setPromotionCode(this.getMixMatchData().getCode());

                if (!previousItemEntryId.equals(sale.getItemEntryId())) {
                    discountCounter++;
                }
                previousItemEntryId = sale.getItemEntryId();
                discount.setRewardID(discountCounter);

                discount.setUnitDiscountAmount(discountAmounts[index]);
                promo.add(discount);
                index++;
            }

            for (Discount discount : promo) {
                int amountOver = discount.getAmountRemaining();
                if (totalDiscount < amountOver) {
                    discount.setUnitDiscountAmount(
                       discount.getUnitDiscountAmount() + totalDiscount);
                    totalDiscount = 0;
                } else {
                    discount.setUnitDiscountAmount(
                            discount.getUnitDiscountAmount() + amountOver);
                    totalDiscount -= amountOver;
                }
            }
            this.setDiscntCntr(discountCounter);
        } else if (SaleItemsHandler.getSumSalesQuantity(itemList) >= step1Qty) {
            //decide if step 1 has been applied
            //if step 1 already, discount directly
            if (!has1stStepProcessed) {
                itemsToDiscount = this.getDiscountedSale(FIRST_STEP, true,
                        itemList);

                for (int i = 0; i < itemsToDiscount.size(); i++) {
                    totalSales += itemsToDiscount.get(i)
                                .getRegularSalesUnitPrice();
                }
                discountAmounts = this.calculateDiscount(itemsToDiscount,
                        totalSales, FIRST_STEP);
                stepCounter = step1Qty;

                for (int i = 0; i < stepCounter; i++) {
                    discountVal += discountAmounts[i];
                }

                discountedPrice = totalSales - discountVal;
                if (totalSales > discountedPrice) {
                    has1stStepProcessed = true;
                }

                discountVal = prevDiscountTotal + discountVal;
                totalSales = prevTotalSales + totalSales;
            } else {
                itemsToDiscount = this.getDiscountedSale(FIRST_STEP, false,
                        itemList);

                for (int i = 0; i < itemsToDiscount.size(); i++) {
                    totalSales += itemsToDiscount.get(i)
                                .getRegularSalesUnitPrice();
                }

                totalSales = prevTotalSales + totalSales;

                discountAmounts = this.calculateDiscount(itemsToDiscount,
                        totalSales, FIRST_STEP);

                stepCounter = discountAmounts.length;

                for (int i = 0; i < stepCounter; i++) {
                    discountVal += discountAmounts[i];
                }

                discountedPrice = totalSales - discountVal;
            }

            if (!has1stStepProcessed) {
                return promo;
            }

            String previousItemEntryId = "";
            int discountCounter = this.getDiscntCntr();
            int index = 0;
               for (Sale sale : itemsToDiscount) {
                    Discount discount = new Discount();
                    discount.setDiscountDescription(mixMatchDescription);
                    discount.setEarnedRewardID(
                            this.getMixMatchData().getCode());
                    discount.setItemEntryID(sale.getItemEntryId());
                    discount.setPromotionCode(this.getMixMatchData().getCode());

                    if (!previousItemEntryId.equals(sale.getItemEntryId())) {
                        discountCounter++;
                    }
                    previousItemEntryId = sale.getItemEntryId();
                    discount.setRewardID(discountCounter);

                    discount.setUnitDiscountAmount(discountAmounts[index]);
                    promo.add(discount);
                    index++;
                }
            this.setDiscntCntr(discountCounter);

            for (Discount discount : promo) {
                firstStepDiscounts.add(discount.getUnitDiscountAmount());
            }
        }

        if (itemsToDiscount != null) {
            this.setItemsSaved(this.getItemsSaved() + itemsToDiscount.size());
        }
        prevDiscountTotal += (int) discountVal;
        prevTotalSales = totalSales;

        return promo;
    }

}
