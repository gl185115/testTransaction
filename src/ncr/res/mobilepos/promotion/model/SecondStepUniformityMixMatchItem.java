package ncr.res.mobilepos.promotion.model;

import java.util.ArrayList;
import java.util.List;

import ncr.res.mobilepos.promotion.helper.SaleItemsHandler;
/**
 * The Second Step uniformity Mix and Match Item.
 * @author CC185102
 *
 */
public class SecondStepUniformityMixMatchItem extends MixMatchItem {
    /**
     * constant for first step.
     */
    private static final int FIRST_STEP = 0;
    /**
     * constant for second step.
     */
    private static final int SECOND_STEP = 1;
    /**
     * determines if there is a first step that has to be updated.
     */
    private boolean has1stStepProcessed = false;
    /**
     * Use to compute aliquant case for discount.
     */
    private long aliquantBalance = 0;
    /** The Custom Constructor.
     * @param codeToSet The Code to Set.
     */
    public SecondStepUniformityMixMatchItem(final String codeToSet) {
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
     * gets the sale items to be discounted.
     * @param stepLevel the step level of the discount
     * @param startFromZero sets the index back to zero
     * @param itemList The Target list of Sales to be discounted
     * @return List of items to be discounted
     */
    private List<Sale> getDiscountedSale(
            final int stepLevel,
            final boolean startFromZero,
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
            SaleItemsHandler.getSumSalesQuantity(itemList)
                - this.getItemsSaved();
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

    @Override
    protected void resetComputation() {
        this.setCurrentIndex(0);
        this.setDiscntCntr(0);
        this.setItemsSaved(0);
        this.resetSalesDiscCnt();
        this.has1stStepProcessed = false;
        this.aliquantBalance = 0;
    }

    @Override
    protected List<Discount> mixMatchComputation(
            final List<Sale> itemList) {
        NormalMixMatchData mixMatchData = this.getMixMatchData();
        int step1Qty = mixMatchData.getQuantity()[FIRST_STEP];
        int step2Qty = mixMatchData.getQuantity()[SECOND_STEP];
        List<Discount> promo = new ArrayList<Discount>();
        int sumSalesQty = SaleItemsHandler.getSumSalesQuantity(itemList);

        if (sumSalesQty >= step2Qty) {
           promo = secondStepMixMatchComputation(itemList);
        } else if (sumSalesQty >= step1Qty) {
           promo = firstStepMixMatchComputation(itemList);
        }
        promo = this.computeProportionalDistribution(promo, itemList);
        return promo;
    }

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

        boolean hasMustBuyItem =
            SaleItemsHandler.hasMustBuyItem(overrideMustBuy);

         if (sumOfSalesQty < mmQty || !hasMustBuyItem) {
             return new ArrayList<Sale>();
         }

        mustBuyEstablishedList =
            SaleItemsHandler.sortSalesDescending(overrideMustBuy);

        boolean hasRevoke =
            //Is not automatic Discount?
            (!this.getCode().equals(MixMatchItem.GENERAL_PROMOTION))
            && (this.getItemsSaved()
                    >= this.getMixMatchData().getQuantity()[0]);

        this.setRevoked(hasRevoke);
        return mustBuyEstablishedList;
    }

    /**
     * The First Step Mix and Match Computation.
     * @param itemList  The List of Sale.
     * @return The List of Discounts.
     */
    private List<Discount> firstStepMixMatchComputation(
            final List<Sale> itemList) {
        String mixMatchDescription = this.getMixMatchData().getName();
        NormalMixMatchData mixMatchData = this.getMixMatchData();
        int step1Qty = mixMatchData.getQuantity()[FIRST_STEP];
        List<Discount> promo = new ArrayList<Discount>();
        double step1Discount = 0;

        double uniformValue1stStep = 0;
        int mmType = mixMatchData.getType();

        List<Sale> itemsToDiscount = null;
        int totalSales = 0;
        //decide if step 1 has been applied
        //if step 1 already, discount directly
        if (!has1stStepProcessed) {
            itemsToDiscount =
                this.getDiscountedSale(FIRST_STEP, true, itemList);

            for (int i = 0; i < step1Qty; i++) {
                totalSales += itemsToDiscount.get(i)
                            .getRegularSalesUnitPrice();
            }
        }

        long mmDiscountAmount = 0;
        if (MixMatchItem.SECOND_STEP_UNIFORMITY_PRICE == mmType) {
            step1Discount = mixMatchData.getDiscountprice()[FIRST_STEP];
            has1stStepProcessed = (totalSales > step1Discount);
            if (!has1stStepProcessed) {
                return promo;
            }
            this.aliquantBalance = (long) step1Discount;
            uniformValue1stStep = Math.ceil(step1Discount / step1Qty);
            mmDiscountAmount = totalSales - (int) step1Discount;
        } else if (MixMatchItem.SECOND_STEP_UNIFORMITY_AMOUNT == mmType) {
            mmDiscountAmount =  mixMatchData.getDiscountprice()[FIRST_STEP];
            this.aliquantBalance = mmDiscountAmount;
        }

        String previousItemEntryId = "";
        int cntr = 1; // distribute discount per item
        long unitDiscountTotal = 0;
        int discountCounter = this.getDiscntCntr();
        for (Sale sale : itemsToDiscount) {
            Discount discount = new Discount();
            discount.setDiscountDescription(mixMatchDescription);
            if (!previousItemEntryId.equals(sale.getItemEntryId())) {
                discountCounter++;
            }
            previousItemEntryId = sale.getItemEntryId();
            discount.setRewardID(discountCounter);
            discount.setEarnedRewardID(
                    this.getMixMatchData().getCode());
            discount.setItemEntryID(sale.getItemEntryId());
            discount.setPromotionCode(this.getMixMatchData().getCode());
            double itemPrice = sale.getRegularSalesUnitPrice();
            long unitDiscount = 0;
            if (mmDiscountAmount > 0) {
              //proportional discount distribution
                unitDiscount = (int) Math.round(
                        (itemPrice / totalSales) * mmDiscountAmount);
                if (cntr < step1Qty) {
                    unitDiscountTotal += unitDiscount;
                } else if (cntr == step1Qty) {
                    unitDiscount = mmDiscountAmount - unitDiscountTotal;
                } else {
                    switch (mmType) {
                    case MixMatchItem.SECOND_STEP_UNIFORMITY_PRICE:
                        unitDiscount = this.computeUnitDiscountUniPrice((long) uniformValue1stStep, (long) step1Discount, (int) sale.getRegularSalesUnitPrice());
                        break;
                    case MixMatchItem.SECOND_STEP_UNIFORMITY_AMOUNT:                        
                        unitDiscount = this.computeUnitDiscountUniAmount(mmDiscountAmount, step1Qty);
                        break;
                    default:
                       break;
                    }
                }
            }
            discount.setUnitDiscountAmount(unitDiscount);
            promo.add(discount);
            cntr++;
        }

        this.setDiscntCntr(discountCounter);
        if (itemsToDiscount != null) {
            this.setItemsSaved(this.getItemsSaved() + itemsToDiscount.size());
        }

        return promo;
    }    

    /**
     * The Second Step Mix and Match Computation.
     * @param itemList  The List of Sale.
     * @return The List of Discounts.
     */
    private List<Discount> secondStepMixMatchComputation(
            final List<Sale> itemList) {
        //Initializations
        String mixMatchDescription = this.getMixMatchData().getName();
        NormalMixMatchData mixMatchData = this.getMixMatchData();
        int step2Qty = mixMatchData.getQuantity()[SECOND_STEP];
        List<Discount> promo = new ArrayList<Discount>();

        int mmType = mixMatchData.getType();

        List<Sale> itemsToDiscount = null;
        int totalSales = 0;
        double step2Discount = 0;
        long mmDiscountAmount = 0;
        double uniformValue2ndStep = 0;

        itemsToDiscount =
                this.getDiscountedSale(SECOND_STEP, true, itemList);
        for (int i = 0; i < step2Qty; i++) {
            totalSales += itemsToDiscount.get(i)
                        .getRegularSalesUnitPrice();
        }

        if (MixMatchItem.SECOND_STEP_UNIFORMITY_PRICE == mmType) {
            step2Discount =
                    mixMatchData.getDiscountprice()[SECOND_STEP];
            uniformValue2ndStep =
                    Math.ceil(step2Discount / step2Qty);
            this.aliquantBalance = (long) step2Discount;
            mmDiscountAmount = (int) (totalSales - step2Discount);
        } else  if (MixMatchItem.SECOND_STEP_UNIFORMITY_AMOUNT == mmType) {
            mmDiscountAmount = mixMatchData.getDiscountprice()[SECOND_STEP];
            this.aliquantBalance = mmDiscountAmount;
        }

        String previousItemEntryId = "";
        int cntr = 1; // distribute discount per item
        int unitDiscountTotal = 0;
        int discountCounter = this.getDiscntCntr();
        for (Sale sale : itemsToDiscount) {
            Discount discount = new Discount();
            discount.setDiscountDescription(mixMatchDescription);
            String itemEntryID = sale.getItemEntryId();
            if (!previousItemEntryId.equals(itemEntryID)) {
                discountCounter++;
            }
            previousItemEntryId = itemEntryID;
            discount.setRewardID(discountCounter);
            discount.setEarnedRewardID(
                    mixMatchData.getCode());
            discount.setItemEntryID(itemEntryID);
            discount.setPromotionCode(mixMatchData.getCode());
            double itemPrice = sale.getRegularSalesUnitPrice();
            long unitDiscount = 0;

            unitDiscount = (int) Math.round(
                    (itemPrice / totalSales) * mmDiscountAmount);
            if (cntr < step2Qty) {
                unitDiscountTotal += unitDiscount;
            } else if (cntr == step2Qty) {
                unitDiscount = mmDiscountAmount - unitDiscountTotal;
            } else {
                switch (mmType) {
                case MixMatchItem.SECOND_STEP_UNIFORMITY_PRICE:                    
                    unitDiscount = this.computeUnitDiscountUniPrice((long) uniformValue2ndStep, (long) step2Discount, (int)sale.getRegularSalesUnitPrice());
                    break;
                case MixMatchItem.SECOND_STEP_UNIFORMITY_AMOUNT:                    
                    unitDiscount = this.computeUnitDiscountUniAmount(mmDiscountAmount, step2Qty);
                    break;
                default:
                    break;
                }
            }

            discount.setUnitDiscountAmount(unitDiscount);
            promo.add(discount);
            cntr++;
        }
        this.setDiscntCntr(discountCounter);

        if (itemsToDiscount != null) {
            this.setItemsSaved(itemsToDiscount.size() + itemsToDiscount.size());
        }
        return promo;
    }
    /**
     * Compute the Proportional Distribution Discount.
     * @param promo The List of Discounts
     * @param itemList  The List of Sale.
     * @return The Proportionally distributed Discount.
     */
    private List<Discount> computeProportionalDistribution(
            final List<Discount> promo,
            final List<Sale> itemList) {
        //Distribute the promo
        if (!promo.isEmpty()) {
            int toBeDivided = 0;
            for (Discount disc : promo) {
                toBeDivided += disc.getUnitDiscountAmount();
            }
            int total = 0;
            for (Sale sale : itemList) {
                total += sale.getRegularSalesUnitPrice()
                    * sale.getQuantity();
            }
            int remainder = toBeDivided;
            int saleIndex = -1;
            int qty = 0;
            Sale saleItem = null;
            for (int i = 0; i < promo.size() - 1; i++) {
                Discount disc = promo.get(i);
                if (qty == 0) {
                    saleIndex += 1;
                    saleItem = itemList.get(saleIndex);
                    qty = saleItem.getQuantity();
                }
                qty--;
                long toSet = Math.round(toBeDivided
                        * (saleItem.getRegularSalesUnitPrice() / total));
                disc.setUnitDiscountAmount((int) toSet);
                remainder -= toSet;
            }
            //last item
            promo.get(promo.size() - 1)
                .setUnitDiscountAmount(remainder);
        }
        return promo;
    }
    
    /**
     * 
     * @param mmDiscountAmount
     * @param stepQty
     * @return
     */
	private long computeUnitDiscountUniAmount(long mmDiscountAmount, int stepQty) {
		long unitDiscount = (int) Math.floor(mmDiscountAmount / stepQty);
		this.aliquantBalance -= unitDiscount;
		if (this.aliquantBalance < unitDiscount) {
			unitDiscount += this.aliquantBalance;
			this.aliquantBalance =  mmDiscountAmount;
		}
		return unitDiscount;
	}
    
    
    /**
     * 
     * @param ppi
     * @param stepDiscount
     * @param regularSalesPrice
     * @return
     */
	private long computeUnitDiscountUniPrice(long ppi, long stepDiscount,
			int regularSalesPrice) {
		this.aliquantBalance -= ppi;
		if (0 >= this.aliquantBalance) {
			ppi += this.aliquantBalance;
			this.aliquantBalance = stepDiscount;
		}
		long unitDiscount = regularSalesPrice - ppi;
		if (unitDiscount < 0) {
			unitDiscount = 0;
		}
		return unitDiscount;
	}
}
