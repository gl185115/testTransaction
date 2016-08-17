package ncr.res.mobilepos.promotion.model;

import java.util.ArrayList;
import java.util.List;

import ncr.res.mobilepos.promotion.helper.SaleItemsHandler;

/**
 * The Second Step Repetition Mix and Match Item.
 * @author cc185102
 */
public class SecondStepRepetitionMixMatchItem extends MixMatchItem {
    /**
     * constant for first step.
     */
    private static final int FIRST_STEP = 0;
    /**
     * constant for second step.
     */
    private static final int SECOND_STEP = 1;
    /** The full Discount. */
    private static final int FULL_PERCENT = 100;
    /**
     * determines if there is a first step that has to be updated.
     */
    private boolean has1stStepProcessed = false;
    /**
     * The custom constructor.
     * @param codeToSet The Code to set.
     */
    public SecondStepRepetitionMixMatchItem(final String codeToSet) {
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
        //Initializations
        NormalMixMatchData mixMatchData = this.getMixMatchData();
        int step1Qty = mixMatchData.getQuantity()[FIRST_STEP];
        int step2Qty = mixMatchData.getQuantity()[SECOND_STEP];
        double step2Discount = mixMatchData.getDiscountprice()[SECOND_STEP];
        int mmType = mixMatchData.getType();
        int itemToProcessCount = SaleItemsHandler.getSumSalesQuantity(itemList)
                                        - this.getItemsSaved();
        boolean shouldApplyFirstStep =
            ((itemToProcessCount % step2Qty) >= step1Qty)
            && !has1stStepProcessed;
        int loopCount2ndStep = itemToProcessCount / step2Qty;
        List<Discount> promo = new ArrayList<Discount>();

        List<Sale> itemsToDiscount = null;
        for (int i = 0; i < loopCount2ndStep; i++) {
            itemsToDiscount = this.getDiscountedSale(SECOND_STEP, itemList);

            long totalPrice = SaleItemsHandler.getSumSalesRegularPrice(
                    itemsToDiscount, false);

            if (totalPrice < step2Discount
                    && mmType == MixMatchItem.SECOND_STEP_REPETITION_PRICE) {
                return promo;
            }

            int[] discountAmounts = this.calculateDiscount(itemsToDiscount,
                    (int) totalPrice, mmType, SECOND_STEP);

            promo.addAll(this.createDiscounts(
                    itemsToDiscount, discountAmounts, SECOND_STEP));
            int itemSaved = this.getItemsSaved()
                    + (loopCount2ndStep * step2Qty);
            this.setItemsSaved(itemSaved);
        }

        if (shouldApplyFirstStep) {
            List<Discount> firstStepDiscounts = applyFirstStep(itemList);
            promo.addAll(firstStepDiscounts);
        }

        return promo;
    }

    /**
     * Provide Mix and Match Establishment for
     * the List of Sales target for Discounts in Must Buy implementation.
     * @return The List of Sales for Must buy discount implementation.
     */
    protected final List<Sale> mustBuyMixMatchEstablishMent() {
      //Start the Establishment for MustBuy.
        List<Sale> mustBuyEstablishedList = new ArrayList<Sale>();
        int mmQty1 = this.getMixMatchData().getQuantity()[FIRST_STEP];

        int sumSalesQuantity = this.getSumSalesQuantity();
        //Are there not enough items for Mix and Match?
        //If yes, then there are no Mix Match to be established.
        if (sumSalesQuantity < mmQty1) {
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

        int mmQty2 = this.getMixMatchData().getQuantity()[SECOND_STEP];
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

        // For RES-5277
        List<Sale> overrideMustBuy = new ArrayList<Sale>();
        for (Sale sale : salesWithMustBuyFlag) {
            if (!sale.isPriceOverride()) {
                overrideMustBuy.add(sale);
            }
        }
        
        int i = 0;
        List<Sale> mixMatchSales = new ArrayList<Sale>();
        for (Sale sale : salesWithoutMustBuyFlag) {
            boolean hasNoMoreMustBuyItem = (overrideMustBuy == null)
                || (overrideMustBuy.isEmpty());
           if (hasNoMoreMustBuyItem) {
               break;
           }
           // For RES-5277
           if (!sale.isPriceOverride()) {
               i++;
               mixMatchSales.add(sale);
               //Is there a MixMatch pattern achieved?
               //If Yes, add the Sale with Must Buy for the Mix Match.
               if ((i % mmQty2) == (mmQty2 - 1)) {
                   mixMatchSales.add(overrideMustBuy.remove(0));
                   i = 0;
                   //Sort again the sales after the Sale with Must Buy was added
                   mixMatchSales =
                       SaleItemsHandler.sortSalesDescending(mixMatchSales);
                   mustBuyEstablishedList.addAll(mixMatchSales);
                   //Set another MixMatch Batch of Sales
                   mixMatchSales.clear();
               }
            }
        }

        mixMatchSales.addAll(overrideMustBuy);
        //salesWithMustBuyFlag are already sorted.
        //Is the mixMatchSales Contains NonMustbuy item also?
        //If yes, perform sort by price again.
        if (mixMatchSales.size() > overrideMustBuy.size()) {
            mixMatchSales =
                SaleItemsHandler.sortSalesDescending(mixMatchSales);
        }
        //All items with Must Buy flag is under consideration for MixMatch.
        mustBuyEstablishedList.addAll(priceOverrideMustBuyItems(mixMatchSales));
        this.setRevoked(hasRevoke);
        return mustBuyEstablishedList;
    }

    /**
     * RES-4277
     * applies price override must buy items
     * The must buy items are not included in the group incase the 
     * actual retail price less than to not must buy items.
     * @param mixMatchItems The List of Sale to be Mix and Match.
     * @return List of must buy items
     */
    private List<Sale> priceOverrideMustBuyItems(List<Sale> mixMatchItems) {
       NormalMixMatchData mixMatchData = this.getMixMatchData();
       int step1Qty = mixMatchData.getQuantity()[FIRST_STEP];
       int step2Qty = mixMatchData.getQuantity()[SECOND_STEP];
       int itemToProcessCount = SaleItemsHandler
       		                  .getSumSalesQuantity(mixMatchItems);
       boolean shouldApplyFirstStep =
           itemToProcessCount < step2Qty;

       int cntQty = 0;
       boolean hasMustBuy = false;
       List<Sale> mustBuyItem = new ArrayList<Sale>(); 
       for (Sale sale : mixMatchItems) {
		if (sale.getMustBuyFlag() == 1) {
			hasMustBuy = true;
		}
		cntQty += sale.getQuantity();
		if ((cntQty >= step1Qty) && shouldApplyFirstStep) {
			if (hasMustBuy) {
				mustBuyItem.add(sale);
				break;
			}     		
		} else {
			mustBuyItem.add(sale);
		}
       }
       return mustBuyItem;
   }

    /**
     * applies first step discounts.
     * @param targetSales The List of Sale to be Mix and Match.
     * @return List of Discounts
     */
    private List<Discount> applyFirstStep(final List<Sale> targetSales) {
        has1stStepProcessed = true;

        NormalMixMatchData mixMatchData = this.getMixMatchData();
        int step1Qty = mixMatchData.getQuantity()[FIRST_STEP];

        //process if a first step is to be applied
        List<Sale> itemsTo1stStepDiscount = null;
        itemsTo1stStepDiscount =
            this.getDiscountedSale(FIRST_STEP, targetSales);

        double total1stStep = SaleItemsHandler.getSumSalesRegularPrice(
                itemsTo1stStepDiscount, false);

        if (mixMatchData.getType() == MixMatchItem.SECOND_STEP_REPETITION_PRICE
                && total1stStep < mixMatchData.getDiscountprice()[FIRST_STEP]) {
            return  new ArrayList<Discount>();
        }

        int[] discountAmounts = this.calculateDiscount(itemsTo1stStepDiscount,
                (int) total1stStep, mixMatchData.getType(),
                FIRST_STEP);
        List<Discount> promo1stStep =
            this.createDiscounts(itemsTo1stStepDiscount,
                    discountAmounts, FIRST_STEP);
        this.setItemsSaved(step1Qty);
        return promo1stStep;
    }

    /**
     * gets the sale items to be discounted.
     * @param stepLevel the step level of the discount
     * @param targetSales The List Sales Target for Mix and Match.
     * @return List of items to be discounted
     */
    private List<Sale> getDiscountedSale(final int stepLevel,
            final List<Sale> targetSales) {
        int tempCurrentIndex = this.getCurrentIndex();

        int numberOfDiscountItems = this.getMixMatchData()
                                    .getQuantity()[stepLevel];
        List<Sale> itemsToDiscount = new ArrayList<Sale>();
        int numberOfDiscountedCount = targetSales
                                .get(tempCurrentIndex).getDiscountCount();

        for (int i = 0; i < numberOfDiscountItems; i++) {
            Sale currentSale = targetSales.get(tempCurrentIndex);
            numberOfDiscountedCount++;
            itemsToDiscount.add(currentSale);
            if (numberOfDiscountedCount >= currentSale.getQuantity()) {
                //if this is second step, retain numberOfDiscountedCount
                if (stepLevel == SECOND_STEP) {
                    currentSale.setDiscountCount(numberOfDiscountedCount);
                }
                numberOfDiscountedCount = 0;
                tempCurrentIndex++;
            }
        }

        //if this is second step, keep tempCurrentIndex
        //and numberOfDiscountedCount
        if (stepLevel == SECOND_STEP) {
            this.setCurrentIndex(tempCurrentIndex);
            if (tempCurrentIndex <= targetSales.size() - 1) {
                Sale saleCurrentIndex = targetSales
                                            .get(this.getCurrentIndex());
                saleCurrentIndex.setDiscountCount(numberOfDiscountedCount);
            }
        }
        return itemsToDiscount;
    }

    @Override
    protected final void resetComputation() {
        this.setCurrentIndex(0);
        this.setDiscntCntr(0);
        this.setItemsSaved(0);
        this.resetSalesDiscCnt();
        this.has1stStepProcessed = false;
    }

    /**
     * generates the proper discount per the item based on
     * the mix match type.
     * @param mmItems list of Items
     * @param totalPrice - the total of the items to apply discount
     * @param mixMatchType - type of the mix match. either 1 or 5
     * @param stepLevel level of the discount
     * @return array of discounts
     */
    private int[] calculateDiscount(final List<Sale> mmItems,
            final int totalPrice, final int mixMatchType, final int stepLevel) {
        int[] mmdItem = new int[mmItems.size()];
        double mmDiscount = Math.floor(
                this.getExpectedDiscount(stepLevel, totalPrice));

        int totalDiscount = 0;
        for (int i = 0; i < mmItems.size(); i++) {
            double price = mmItems.get(i).getRegularSalesUnitPrice();
            double percentage = price / totalPrice;

            mmdItem[i] = (int) Math.round(percentage * mmDiscount);
            totalDiscount += mmdItem[i];
        }

        if (mmDiscount > totalDiscount) {
            mmdItem[mmItems.size() - 1] += mmDiscount - totalDiscount;
        } else {
            mmdItem[mmItems.size() - 1] -= totalDiscount - mmDiscount;
        }

        return mmdItem;
    }
    /**
     * Get the Discount price from a Given Step.
     * @param stepLevel The repetition level
     * @param totalPrice The Total Price to extract a discount
     * @return The DisCount price for the given step
     */
    private double getExpectedDiscount(final int stepLevel,
            final long totalPrice) {
        NormalMixMatchData mixmatchdata = this.getMixMatchData();
        double mmPrice = mixmatchdata.getDiscountprice()[stepLevel];
        int mmType = this.getMixMatchData().getType();
        switch (mmType) {
            case MixMatchItem.SECOND_STEP_REPETITION_PRICE:
                return totalPrice - mmPrice;
            case MixMatchItem.SECOND_STEP_REPETITION_RATE:
                return (mmPrice / FULL_PERCENT) * totalPrice;
            case MixMatchItem.SECOND_STEP_REPETITION_AMOUNT:
                return mmPrice;
            default:
                return 0;
        }
    }

    /**
     * Create the Discounts.
     * @param salesTobeDiscounted The Sales target to be discounted
     * @param unitDiscountAmnts The distributed discount amount for each Sale
     * @param stepLevel   The Designated Level of Step (Either 1 step or 2 step)
     * @return  The Created Discounts based fromList of Sales.
     */
    private List<Discount> createDiscounts(
            final List<Sale> salesTobeDiscounted,
            final int[] unitDiscountAmnts,
            final int stepLevel) {
        List<Discount> discounts = new ArrayList<Discount>();
        String previousItemEntryId1stStep = "";
        NormalMixMatchData mixMatchData = this.getMixMatchData();
        boolean isSecondStep = stepLevel == SECOND_STEP;
        int stepQty = mixMatchData.getQuantity()[stepLevel];
        int[] firsStepDiscount = null;
        if (isSecondStep
                && has1stStepProcessed) {
            has1stStepProcessed = false;
            List<Sale> discountedBy1stStep =
                salesTobeDiscounted.subList(0,
                        mixMatchData.getQuantity()[FIRST_STEP]);
            long totalPrice1stStep =
                SaleItemsHandler.getSumSalesRegularPrice(
                        discountedBy1stStep, false);
            firsStepDiscount = this.calculateDiscount(discountedBy1stStep,
                     (int) totalPrice1stStep,
                    mixMatchData.getType(), FIRST_STEP);
        }

        for (int i = 0; i < stepQty; i++) {
            Sale sale = salesTobeDiscounted.get(i);
            Discount discount = new Discount();
            discount.setDiscountDescription(mixMatchData.getName());
            discount.setEarnedRewardID(mixMatchData.getCode());
            discount.setItemEntryID(sale.getItemEntryId());
            discount.setPromotionCode(mixMatchData.getCode());
            String itemEntryID = sale.getItemEntryId();
            if (!previousItemEntryId1stStep.equals(itemEntryID)) {
                this.setDiscntCntr(this.getDiscntCntr() + 1);
            }
            previousItemEntryId1stStep = itemEntryID;
            discount.setRewardID(this.getDiscntCntr());

            int unitDisCount = unitDiscountAmnts[i];
            if (isSecondStep && i < stepQty && firsStepDiscount != null) {
                unitDisCount -= firsStepDiscount[i];
            }
            discount.setUnitDiscountAmount(unitDisCount);

            discounts.add(discount);
        }
        return discounts;
    }
}
