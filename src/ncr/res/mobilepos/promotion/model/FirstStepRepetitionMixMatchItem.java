package ncr.res.mobilepos.promotion.model;

import java.util.ArrayList;
import java.util.List;

import ncr.res.mobilepos.promotion.helper.SaleItemsHandler;


/**
 * The First Step Repetition Algorithm for Mix Match Item.
 *
 * @author jd185128
 * @author cc185102 - Added Mix and Match for Must buy
 *
 */
public class FirstStepRepetitionMixMatchItem extends MixMatchItem {
    /** The full Discount. */
    private static final int FULL_PERCENT = 100;
    /**
     * The custom constructor.
     *
     * @param codeToSet
     *            The Code to set.
     */
    public FirstStepRepetitionMixMatchItem(final String codeToSet) {
        this.setCode(codeToSet);
    }

    /**
     * Gets the mix match data.
     *
     * @return the mix match data
     */
    @Override
    public NormalMixMatchData getMixMatchData() {
        return (NormalMixMatchData) this.mixMatchData;
    }

    /**
     * Method to perform Normal Mix and Match Computation.
     * @param itemList The Item List for Mix Match Computation.
     * @return The List of Discounts.
     */
    @Override
    protected final List<Discount> mixMatchComputation(
            final List<Sale> itemList) {
        List<Sale> discountedSales = new ArrayList<Sale>();
        List<Discount> discounts = new ArrayList<Discount>();

        NormalMixMatchData mmData = this.getMixMatchData();
        int mmQty = mmData.getQuantity()[0];
        int sumSalesQty = SaleItemsHandler.getSumSalesQuantity(itemList);
        int unsavedQty = sumSalesQty - this.getItemsSaved();
        int numMMD = unsavedQty / mmQty; // number of Discount

        while (numMMD > 0) {
            int i = this.getCurrentIndex();
            String prevItemEntry = "";
            discountedSales = new ArrayList<Sale>();

            for (int j = 0; mmQty > j; j++) {
                Sale sale = itemList.get(i);
                sale.setDiscountCount(sale.getDiscountCount() + 1);
                discountedSales.add(sale);

                // increment rewardid for every item_entry
                if (!prevItemEntry.isEmpty()
                        && sale.getItemEntryId().equals(prevItemEntry)) {
                    sale.setRewardId(this.getDiscntCntr());
                } else {
                    this.setDiscntCntr(this.getDiscntCntr() + 1);
                    sale.setRewardId(this.getDiscntCntr());
                }
                prevItemEntry = sale.getItemEntryId();

                // continue next item, if done iterating current item
                if (sale.getDiscountCount() >= sale.getQuantity()) {
                    i++;
                }
            }

            List<Discount> newDiscount = getDiscounts(discountedSales, mmData);
            if (newDiscount.isEmpty()) {
                return discounts;
            }
            discounts.addAll(newDiscount);
            this.setItemsSaved(this.getItemsSaved() + mmQty);
            this.setCurrentIndex(i);
            numMMD--;
        }
        return discounts;
     }

    /**
     * Provide Mix and Match Establishment for
     * the List of Sales target for Discounts in Must Buy implementation.
     * @return The List of Sales for Must buy discount implementation.
     */
    protected final List<Sale> mustBuyMixMatchEstablishMent() {
        //Start the Establishment for MustBuy.
        List<Sale> mustBuyEstablishedList = new ArrayList<Sale>();
        int mmQty = this.getMixMatchData().getQuantity()[0];
        int sumOfSalesQty = this.getSumSalesQuantity();
        //Are there not enough items for Mix and Match?
        //If yes, then there are no Mix Match to be established.
        if (sumOfSalesQty < mmQty) {
            return new ArrayList<Sale>();
        }
        List<Sale> salesWithoutMustBuyFlag;
        List<Sale> salesWithMustBuyFlag;

        salesWithoutMustBuyFlag =
            SaleItemsHandler.copySales(this.getItemList());
        salesWithoutMustBuyFlag = SaleItemsHandler.reverseSales(
                salesWithoutMustBuyFlag);
        salesWithoutMustBuyFlag =
            SaleItemsHandler.sortSalesDescending(
                    salesWithoutMustBuyFlag);
        salesWithMustBuyFlag =
            SaleItemsHandler.extractAllSalesWithMustBuy(
                    salesWithoutMustBuyFlag);

        //Are there No Sales with Mustbuy flag?
        //If yes, then there is no Mix Match Establishment.
        if (null == salesWithMustBuyFlag || salesWithMustBuyFlag.isEmpty()) {
            return mustBuyEstablishedList;
        }

        salesWithoutMustBuyFlag.removeAll(salesWithMustBuyFlag);

        //Are there No Sales with Mustbuy flag?
        //If yes, then there is no Mix Match Establishment.
        if (null == salesWithMustBuyFlag || salesWithMustBuyFlag.isEmpty()) {
            return mustBuyEstablishedList;
        }

        //Return a Revoke for MixMatch Discount having
        //sum of sales qty greater than the First Qty
        boolean hasRevoke =
            //Is not automatic Discount?
            (!this.getCode().equals(MixMatchItem.GENERAL_PROMOTION))
            && (this.getSumSalesQuantity()
                    > this.getMixMatchData().getQuantity()[0]);
        //And also, it should be revoke when there is a MustBuy item qty greater
        //than 1; Else if there is one item with mustbuy,
        //ensure it is not the last item entry.
        Sale lastSaleEntered = this.getItemList().get(
                this.getItemList().size() - 1);
        hasRevoke = hasRevoke && (salesWithMustBuyFlag.size() > 1
                    || !salesWithMustBuyFlag.get(0).getItemEntryId().equals(
                            lastSaleEntered.getItemEntryId()));
        salesWithMustBuyFlag =
            SaleItemsHandler.iterateSales(salesWithMustBuyFlag);
        salesWithoutMustBuyFlag =
            SaleItemsHandler.iterateSales(
                    salesWithoutMustBuyFlag);

        int i = 0;
        List<Sale> mixMatchSales = new ArrayList<Sale>();
        for (Sale sale : salesWithoutMustBuyFlag) {
            boolean hasNoMoreMustBuyItem = (salesWithMustBuyFlag == null)
                || (salesWithMustBuyFlag.isEmpty());

           if (hasNoMoreMustBuyItem) {
               break;
           }

           i++;
           mixMatchSales.add(sale);

           //Is there a MixMatch pattern achieved?
           //If Yes, add the Sale with Must Buy for the Mix Match.
           if ((i % mmQty) == (mmQty - 1)) {
               mixMatchSales.add(salesWithMustBuyFlag.remove(0));
               i = 0;
               //Sort again the sales after the Sale with Must Buy was added
               mixMatchSales =
                   SaleItemsHandler.sortSalesDescending(mixMatchSales);
               mustBuyEstablishedList.addAll(mixMatchSales);
               //Set another MixMatch Batch of Sales
               mixMatchSales.clear();
           }
        }
        mixMatchSales.addAll(salesWithMustBuyFlag);

        //salesWithMustBuyFlag are already sorted.
        //Is the mixMatchSales Contains NonMustbuy item also?
        //If yes, perform sort by price again.
        if (mixMatchSales.size() > salesWithMustBuyFlag.size()) {
            mixMatchSales =
                SaleItemsHandler.sortSalesDescending(mixMatchSales);
        }

        //All items with Must Buy flag is under consideration for MixMatch.
        mustBuyEstablishedList.addAll(mixMatchSales);

        this.setRevoked(hasRevoke);
        return mustBuyEstablishedList;
    }

    /**
     * Gets the discounts.
     *
     * @param discountedSales
     *            the list of discounted sales
     * @param mmData
     *            the MixMatchData
     * @return the discounts
     */
    private List<Discount> getDiscounts(final List<Sale> discountedSales,
            final NormalMixMatchData mmData) {

        List<Discount> discounts = new ArrayList<Discount>();

        if (!discountedSales.isEmpty()) {
            long mmPrice = this.getMixMatchData().getDiscountprice()[0];
            int mmQty = mmData.getQuantity()[0];
            long totalItems = getTotalSales(discountedSales, true);
            long mmDiscountAmount = 0;
            int mixMatchType = this.getMixMatchData().getType();
            if (mixMatchType == MixMatchItem.FIRST_STEP_REPETITION_PRICE) {
                mmDiscountAmount = totalItems - mmPrice;
            } else if (mixMatchType
                    == MixMatchItem.FIRST_STEP_REPETITION_RATE) {
                mmDiscountAmount = (int) Math.floor(
                        totalItems * ((double) mmPrice
                                / FULL_PERCENT));
            } else if (mixMatchType
                    == MixMatchItem.FIRST_STEP_REPETITION_AMOUNT) {
            	mmDiscountAmount = mmPrice;
            }

            int cntr = 1; // distribute discount per item

            if (totalItems < mmPrice) {
                return discounts;
            }
            int unitDiscountTotal = 0;
            for (Sale sale : discountedSales) {
                Discount discount = new Discount();
                discount.setItemEntryID(sale.getItemEntryId());
                discount.setEarnedRewardID(mmData.getCode());
                discount.setPromotionCode(mmData.getCode());
                discount.setDiscountDescription(mmData.getName());

                discount.setRewardID(sale.getRewardId());

                double itemPrice = sale.getRegularSalesUnitPrice();
                //proportional discount distribution
                int unitDiscount = (int) Math.round(
                        (itemPrice / totalItems) * mmDiscountAmount);
                if (cntr < mmQty) {
                    unitDiscountTotal += unitDiscount;
                } else {
                    unitDiscount = (int) (mmDiscountAmount - unitDiscountTotal);
                }
                discount.setUnitDiscountAmount(unitDiscount);
                discounts.add(discount);
                cntr++;
            }
        }
        return discounts;
    }

    /**
     * @deprecated by using proportional discount distribution
     * generates the proper discount per the item based on
     * the mix match type.
     * @param totalPrice - the total of the items to apply discount
     * @param mixMatchType - type of the mix match. either 1 or 5
     * @return array of discounts
     *          - index 0 is the discount to apply to all other items
     *          - index 1 is the discount to apply to the last item
     */
    @SuppressWarnings("unused")
	@Deprecated
    private int[] calculateDiscount(
            final int totalPrice, final int mixMatchType) {
        double mmPrice = this.getMixMatchData().getDiscountprice()[0];
        int mmQty = this.getMixMatchData().getQuantity()[0];
        int[] mmdItem = {0, 0};
        if (mixMatchType == MixMatchItem.FIRST_STEP_REPETITION_PRICE) {
            double mmDiscount = totalPrice - mmPrice;

            // Compute MixMatchDiscount per Item
            double mmdItemDecimal = mmDiscount / mmQty;
            mmdItem[0] = (int) Math.round(mmdItemDecimal);
            mmdItem[1] = (int)
                mmDiscount - (mmdItem[0] * (mmQty - 1));
        } else if (mixMatchType == MixMatchItem.FIRST_STEP_REPETITION_RATE) {
            double mmPercent = mmPrice / FULL_PERCENT;
            mmdItem[0] = (int) Math.floor((totalPrice * mmPercent) / mmQty);
            mmdItem[1] = (int)
                    (totalPrice * mmPercent) - (mmdItem[0] * (mmQty - 1));
        }

        return mmdItem;
    }

    @Override
    protected final void resetComputation() {
        this.setCurrentIndex(0);
        this.setDiscntCntr(0);
        this.setItemsSaved(0);
        this.resetSalesDiscCnt();
    }
}
