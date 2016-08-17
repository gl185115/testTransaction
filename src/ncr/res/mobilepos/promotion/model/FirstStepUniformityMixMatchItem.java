package ncr.res.mobilepos.promotion.model;

import java.util.ArrayList;
import java.util.List;

import ncr.res.mobilepos.promotion.helper.DiscountItemsHandler;
import ncr.res.mobilepos.promotion.helper.SaleItemsHandler;

/**
 * The First Step Uniformity Mix and Match Item.
 *
 * @author cc185102
 *
 */
public class FirstStepUniformityMixMatchItem extends MixMatchItem {
    /**
     * determines if the mix match discount has been triggered successfully.
     * success means that the first items that meet the mix match quantity
     * have a higher total than the mix match price meaning it can
     * be discounted.
     */
    private boolean mixMatchTriggerSuccess = false;
    /**
     * Remainder discount holder.
     */
    private long aliquantbalance = 0;
    /**
     * The Custom constructor.
     *
     * @param codeToSet
     *            The code to set.
     */
    public FirstStepUniformityMixMatchItem(final String codeToSet) {
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
        List<Discount> discounts = new ArrayList<Discount>();
        List<Sale> discountedSales = null;

        NormalMixMatchData mmData = this.getMixMatchData();
        int mmQty = mmData.getQuantity()[0];
        int sumSalesQty = SaleItemsHandler.getSumSalesQuantity(itemList);
        int unsavedQty = sumSalesQty - this.getItemsSaved();

        // 1st item entry
        if (unsavedQty >= mmQty) {
            String prevItemEntry = "";
            while ((sumSalesQty - this.getItemsSaved()) > 0) {
                int i = this.getCurrentIndex();
                discountedSales = new ArrayList<Sale>();
                if (this.getItemsSaved() >= mmQty) {
                    int unsavedQtyTemp = sumSalesQty
                            - this.getItemsSaved();
                    for (int j = 0; unsavedQtyTemp > j; j++) {
                        Sale sale = itemList.get(i);
                        sale.setDiscountCount(sale.getDiscountCount() + 1);
                        discountedSales.add(sale);

                        // increment rewardid for every item_entry
                        if (!prevItemEntry.isEmpty()
                                && sale.getItemEntryId()
                                    .equals(prevItemEntry)) {
                            sale.setRewardId(this.getDiscntCntr());
                        } else {
                            this.setDiscntCntr(this.getDiscntCntr() + 1);
                            sale.setRewardId(this.getDiscntCntr());
                        }
                        prevItemEntry = sale.getItemEntryId();

                        // continue next item, if done iterating current item
                        if (sale.getDiscountCount() >= sale.getQuantity()) {
                            i++;
                            discounts.addAll(getDiscounts(discountedSales,
                                    mmData));
                            discountedSales = new ArrayList<Sale>();
                        }
                        this.setCurrentIndex(i);
                    }
                    this.setItemsSaved(this.getItemsSaved() + unsavedQtyTemp);
                } else {
                    for (int j = 0; mmQty > j; j++) {
                        Sale sale = itemList.get(i);
                        sale.setDiscountCount(sale.getDiscountCount() + 1);
                        discountedSales.add(sale);

                        // increment rewardid for every item_entry
                        if (!prevItemEntry.isEmpty()
                                && sale.getItemEntryId()
                                    .equals(prevItemEntry)) {
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
                    discounts.addAll(getDiscounts(discountedSales, mmData));
                    this.setCurrentIndex(i);

                    this.setItemsSaved(this.getItemsSaved() +  mmQty);
                }
            }
        }

        //Compute the proportional Distribution of discounts
        return calculateDiscountProportinalDistribution(discounts, itemList);
    }

    /**
     * Provide Mix and Match Establishment for
     * the List of Sales target for Discounts in Must Buy implementation.
     * @return The List of Sales for Must buy discount implementation.
     */
    @Override
    protected final List<Sale> mustBuyMixMatchEstablishMent() {
        int mmQty = this.getMixMatchData().getQuantity()[0];
        int sumOfSalesQty = this.getSumSalesQuantity();
        //Are there not enough items for Mix and Match?
        //If yes, then there are no Mix Match to be established.
        if (sumOfSalesQty < mmQty) {
            return new ArrayList<Sale>();
        }
        List<Sale> mustBuyEstablishedList;

        mustBuyEstablishedList =
            SaleItemsHandler.copySales(this.getItemList());

        // For RES-5277
        List<Sale> overrideMustBuy = new ArrayList<Sale>();
        for (Sale sale : mustBuyEstablishedList) {
            if (!sale.isPriceOverride()) {
            	overrideMustBuy.add(sale);
            }
        }        
        mustBuyEstablishedList.clear();
        mustBuyEstablishedList.addAll(overrideMustBuy);
        
       boolean hasMustBuyItem =
           SaleItemsHandler.hasMustBuyItem(mustBuyEstablishedList);

        if (sumOfSalesQty < mmQty || !hasMustBuyItem) {
            return new ArrayList<Sale>();
        }

        mustBuyEstablishedList =
            SaleItemsHandler.sortSalesDescending(mustBuyEstablishedList);

        boolean hasRevoke =
            //Is not automatic Discount?
            (!this.getCode().equals(MixMatchItem.GENERAL_PROMOTION))
            && (this.getItemsSaved()
                    >= this.getMixMatchData().getQuantity()[0]);

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
    protected List<Discount> getDiscounts(
            final List<Sale> discountedSales,
            final NormalMixMatchData mmData) {

        List<Discount> discounts = new ArrayList<Discount>();

        if (!discountedSales.isEmpty()) {
            int mmQty = mmData.getQuantity()[0];
            long totalItems = getTotalSales(discountedSales, true);
            long mmPrice = mmData.getDiscountprice()[0];
            int mixMatchType = this.getMixMatchData().getType();

            //for first trigger
            //check if total price can trigger the mix match
            if ((this.getItemsSaved() < mmQty)
                    && totalItems > mmPrice) {
                mixMatchTriggerSuccess = true;
            } else if (!mixMatchTriggerSuccess) {
                //if not first trigger and first trigger
                //is not successful, return no discount.
                return discounts;
            }

            

            int cntr = 1; // distribute discount per item
            int unitDiscountToSet = 0;
            int unitDiscountTotal = 0;
            if (this.aliquantbalance == 0) {
                this.aliquantbalance = mmPrice;
            }
            for (Sale sale : discountedSales) {
                Discount discount = new Discount();
                discount.setItemEntryID(sale.getItemEntryId());
                discount.setEarnedRewardID(mmData.getCode());
                discount.setPromotionCode(mmData.getCode());
                discount.setDiscountDescription(mmData.getName());

                discount.setRewardID(sale.getRewardId());

                if (this.getItemsSaved() >= mmQty) {
                    if (MixMatchItem.FIRST_STEP_UNIFORMITY_PRICE
                            == mixMatchType) {
                        // calculate price per item for this iteration
                        // to consider discount remainders for
                        // continuous discounts
                    	double dppItem = (double) mmPrice / mmQty;
                        double ppi = 0;
                        ppi = Math.ceil(dppItem);
                        aliquantbalance -= ppi;

                        if (aliquantbalance <= 0) {
                            ppi += aliquantbalance;
                            aliquantbalance = mmPrice;
                        }
                        // do not calculate discount if sales unit price
                        // < price per item
                        if ((ppi - sale.getRegularSalesUnitPrice())
                                < 0) {
                            unitDiscountToSet += Math.abs(ppi
                            - sale.getRegularSalesUnitPrice());
                        }
                    } else if (MixMatchItem.FIRST_STEP_UNIFORMITY_AMOUNT
                            == mixMatchType) {
                    	double unitDiscount = Math.floor(mmPrice / mmQty);
                    	aliquantbalance -= unitDiscount;

                        if (aliquantbalance < unitDiscount) {
                            unitDiscount += aliquantbalance;
                            aliquantbalance = mmPrice;
                        }
                        unitDiscountToSet += unitDiscount;
                    }
                } else {
                    long mmDiscountAmount = 0;
                    if (mixMatchType
                            == MixMatchItem.FIRST_STEP_UNIFORMITY_AMOUNT) {
                        mmDiscountAmount = mmPrice;
                    } else if (mixMatchType
                            == MixMatchItem.FIRST_STEP_UNIFORMITY_PRICE) {
                        mmDiscountAmount = totalItems - mmPrice;
                    }

                    if (mmDiscountAmount < 0) {
                        mmDiscountAmount = 0;
                    }
                    double itemPrice = sale.getRegularSalesUnitPrice();
                    //proportional discount distribution
                    int unitDiscount = (int) Math.round(
                            (itemPrice / totalItems) * mmDiscountAmount);
                    if (cntr < mmQty) {
                        unitDiscountTotal += unitDiscount;
                    } else {
                        unitDiscount = (int) (mmDiscountAmount
                                - unitDiscountTotal);
                    }
                    unitDiscountToSet = unitDiscount;
                }

                if (sale.getRegularSalesUnitPrice() > unitDiscountToSet) {
                    discount.setUnitDiscountAmount(unitDiscountToSet);
                    discounts.add(discount);
                    unitDiscountToSet = 0;
                }
                cntr++;
            }

            for (Discount discount : discounts) {
                int amountOver = discount.getAmountRemaining();
                if (unitDiscountToSet < amountOver) {
                    discount.setUnitDiscountAmount(
                       discount.getUnitDiscountAmount() + unitDiscountToSet);
                    unitDiscountToSet = 0;
                } else {
                    discount.setUnitDiscountAmount(
                            discount.getUnitDiscountAmount() + amountOver);
                    unitDiscountToSet -= amountOver;
                }
            }
        }

        return discounts;
    }

    /**
     * calculate the discount proportional distribution.
     * @param discounts The Actual List of Discounts.
     * @param actualSales   The List of Sales.
     * @return  The List of Discount with correct proportional distribution.
     */
    public final List<Discount> calculateDiscountProportinalDistribution(
            final List<Discount> discounts, final List<Sale> actualSales) {
        int mmType = this.getMixMatchData().getType();
        if (!(mmType == MixMatchItem.FIRST_STEP_UNIFORMITY_PRICE
                || mmType == MixMatchItem.FIRST_STEP_UNIFORMITY_AMOUNT)) {
            return discounts;
        }

        if (null == actualSales || actualSales.isEmpty()) {
            return  new ArrayList<Discount>();
        }

        int discountSize = discounts.size();
        long totalSumDiscount =
            DiscountItemsHandler.getSumUnitDiscountAmt(discounts);
        long totalSumSales =
            SaleItemsHandler.getSumSalesRegularPrice(actualSales, true);
        for (Discount discount : discounts) {
            //If this is not the last discount
            if (discount != discounts.get(discountSize - 1)) {
                String itemEntryID = discount.getItemEntryID();
                Sale targetSale =
                    SaleItemsHandler.getSale(actualSales, itemEntryID);
                int unitDiscountToSet = (int)
                    Math.round((targetSale.getRegularSalesUnitPrice()
                            / totalSumSales)
                            *  totalSumDiscount);
                discount.setUnitDiscountAmount((int) unitDiscountToSet);
            } else {
                long totaldiscount = DiscountItemsHandler.getSumUnitDiscountAmt(
                        discounts.subList(0, discountSize - 1));
                discount.setUnitDiscountAmount((int) totalSumDiscount
                        - (int) totaldiscount);
            }
        }
        return discounts;
    }

    @Override
    protected final void resetComputation() {
        this.setCurrentIndex(0);
        this.setDiscntCntr(0);
        this.setItemsSaved(0);
        this.resetSalesDiscCnt();
        this.aliquantbalance = 0;
    }
}
