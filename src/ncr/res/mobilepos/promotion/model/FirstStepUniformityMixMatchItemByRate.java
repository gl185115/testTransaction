package ncr.res.mobilepos.promotion.model;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ncr.res.mobilepos.helper.RoundingUtility;
import ncr.res.mobilepos.promotion.helper.SaleItemsHandler;

/**
 * The First Step Uniformity Mix and Match Item computed by rate.
 *
 * @author jd185128
 *
 */
public class FirstStepUniformityMixMatchItemByRate extends
        FirstStepUniformityMixMatchItem {

    /** The Constant rounding for five decimal places. */
    private static final int FIVE_DECIMAL_PLACES = 5;

    /** The Constant one hundred over for percent. */
    private static final int ONE_HUNDRED_OVER = 100;

    /**
     * The Custom constructor.
     *
     * @param codeToSet
     *            The code to set.
     */
    public FirstStepUniformityMixMatchItemByRate(final String codeToSet) {
        super(codeToSet);
    }

    /**
     * Gets discounts for every discounted sales.
     *
     * @param discountedSales
     *            the list of discounted sales
     * @param mmData
     *            the MixMatchData
     * @return the discounts
     */
    @SuppressWarnings("rawtypes")
    @Override
    protected final List<Discount> getDiscounts(
            final List<Sale> discountedSales, final NormalMixMatchData mmData) {

        List<Discount> discounts = new ArrayList<Discount>();

        if (!discountedSales.isEmpty()) {
            int mmQty = mmData.getQuantity()[0];
            double rate = (double) mmData.getDiscountprice()[0]
                    / ONE_HUNDRED_OVER;

            boolean isFirstMMD = true;
            long totalAmt = 0;
            double totalDiscount = 0.0;
            List<Sale> items = new ArrayList<Sale>();

            if (this.getItemsSaved() == 0) {
                totalAmt = getTotalSales(discountedSales, true);
                totalDiscount = Math.floor(rate * totalAmt);
            } else if (this.getItemsSaved() >= mmQty) {
                isFirstMMD = false;
                List<Sale> sales = SaleItemsHandler.copySales(this
                        .getItemList());
                for (int j = 0; j <= this.getCurrentIndex(); j++) {
                    items.add(sales.get(j));
                }
            }

            int discSalesSize = discountedSales.size();
            int iDiscSalesSize = discSalesSize;

            long sumDiscount = 0;
            int saleCount = 1;
            int itemDiscount = 0;

            List<Sale> clonedItems = SaleItemsHandler.copySales(this
                    .getItemList());
            Iterator it = discountedSales.iterator();
            while (it.hasNext()) {
                Sale sale = (Sale) it.next();

                Discount discount = new Discount();
                discount.setItemEntryID(sale.getItemEntryId());
                discount.setEarnedRewardID(mmData.getCode());
                discount.setPromotionCode(mmData.getCode());
                discount.setDiscountDescription(mmData.getName());
                discount.setRewardID(sale.getRewardId());

                // distribute proportionally totalDiscount to per sale item
                if (isFirstMMD) {
                    // discount rate of sale
                    double itemDiscountRate = sale
                            .getRegularSalesUnitPrice() / totalAmt;
                    // round off the decimal point sixth place.(half adjust)
                    double roundedItemDiscountRate = RoundingUtility.round(
                            itemDiscountRate, FIVE_DECIMAL_PLACES,
                            BigDecimal.ROUND_HALF_UP);
                    // round off the decimal point first place.(half adjust)
                    itemDiscount = (int) RoundingUtility.round(
                            totalDiscount * roundedItemDiscountRate, 0,
                            BigDecimal.ROUND_HALF_UP);
                } else {
                    Sale saleClone = (Sale) clonedItems.get(
                            this.getCurrentIndex()).clone();
                    int i = saleClone.getQuantity() - iDiscSalesSize;
                    saleClone.setQuantity(i);
                    // update quantity of sale from items copy
                    items.set(this.getCurrentIndex(), saleClone);

                    // compute total & discount before the sale
                    long prevItemsTotalAmt = getTotalSales(items, false);
                    double prevItemsTotalDiscnt = Math.floor(prevItemsTotalAmt
                            * rate);

                    // compute total & discount including the sale
                    totalAmt = (long) (prevItemsTotalAmt + sale
                            .getRegularSalesUnitPrice());
                    totalDiscount = Math.floor(rate * totalAmt);
                    // subtract total discount from old discount
                    itemDiscount = (int) (totalDiscount - prevItemsTotalDiscnt);
                }

                iDiscSalesSize--;

                if (this.getItemsSaved() < mmQty && (discSalesSize == saleCount)) {
                        long currentTotalDisc = sumDiscount + itemDiscount;
                        if (currentTotalDisc > totalDiscount) {
                            // Subtract excess to last item if sum of discounts
                            // is greater than MMD
                            int remainder =
                                (int) (currentTotalDisc - totalDiscount);
                            itemDiscount = itemDiscount - remainder;
                        } else if (currentTotalDisc < totalDiscount) {
                            // Add excess to last item if sum of discounts is
                            // lesser than MMD
                            int remainder =
                                (int) (totalDiscount - currentTotalDisc);
                            itemDiscount = itemDiscount + remainder;
                        }

                }

                discount.setUnitDiscountAmount(itemDiscount);
                sumDiscount += discount.getUnitDiscountAmount();
                saleCount++;
                discounts.add(discount);
            }
        }
        return discounts;
    }
}
