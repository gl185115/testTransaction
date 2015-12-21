package ncr.res.mobilepos.promotion.model;

import java.util.ArrayList;
import java.util.List;

import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.promotion.helper.DiscountItemsHandler;
import ncr.res.mobilepos.promotion.helper.SaleItemsHandler;
import ncr.res.mobilepos.promotion.helper.TerminalItem;

/**
 * The Mix and Match for Item abstract class.
 * @author cc185102
 *
 */
public abstract class MixMatchItem {
    /** The First Step Repetition Code. */
    public static final int FIRST_STEP_REPETITION_PRICE =  1;
    /** The Second Step Repetition Code. */
    public static final int SECOND_STEP_REPETITION_PRICE = 2;
    /** The First Step Uniformity Code. */
    public static final int FIRST_STEP_UNIFORMITY_PRICE = 3;
    /** The Second Step Uniformity Code. */
    public static final int SECOND_STEP_UNIFORMITY_PRICE = 4;
    /** The First Step Repetition Code. */
    public static final int FIRST_STEP_REPETITION_RATE =  5;
    /** The Second Step Repetition Code. */
    public static final int SECOND_STEP_REPETITION_RATE = 6;
    /** The First Step Uniformity Code. */
    public static final int FIRST_STEP_UNIFORMITY_RATE = 7;
    /** The Second Step Uniformity Code. */
    public static final int SECOND_STEP_UNIFORMITY_RATE = 8;
    /** The First Step Repetition Code for Discount Amount. */
    public static final int FIRST_STEP_REPETITION_AMOUNT =  9;
    /** The Second Step Repetition Code for Discount Amount. */
    public static final int SECOND_STEP_REPETITION_AMOUNT =  10;
    /** The First Step Uniformity Code. */
    public static final int FIRST_STEP_UNIFORMITY_AMOUNT = 11;
    /** The Second Step Uniformity Code. */
    public static final int SECOND_STEP_UNIFORMITY_AMOUNT = 12;
    /** The Group Repetition Code. */
    public static final int GROUP_REPETITION_PRICE = 13;
    /** The Group Repetition Rate. */
    public static final int GROUP_REPETITION_RATE = 14;
    /** The Group Repetition Amount. */
    public static final int GROUP_REPETITION_AMOUNT = 15;
    /** The Group Uniformity Code. */
    public static final int GROUP_UNIFORMITY_PRICE = 16;
    /** The Group Uniformity Rate. */
    public static final int GROUP_UNIFORMITY_RATE = 17;
    /** The Group Uniformity Amount. */
    public static final int GROUP_UNIFORMITY_AMOUNT = 18;
    /** The General Promotion. */
    public static final String GENERAL_PROMOTION = "GeneralPromotion";
    /** Add item quantity entry flag.*/
    public static final int ADD_QUANTITY_ENTRY_FLAG = 2;
    /** Remove item quantity entry flag.*/
    public static final int REMOVE_QUANTITY_ENTRY_FLAG = 0;
    /** Flag for SUBINT10 = 1. */
    public static final int SUBINT10_ONE =  1;
    /** Flag for SUBINT10 = 2. */
    public static final int SUBINT10_TWO =  2;
    /**
     * The Mix and match Code.
     */
    private String code = null;

    /** The mix match data. */
    protected MixMatchData mixMatchData;

    /** The current index. */
    private int currentIndex = 0;

    /** The items saved. */
    private int itemsSaved = 0;

    /** The counter for discount that is unique per item entry. */
    private int discntCntr;
    /** A flag that tells if Revoke has been implemented. **/
    private boolean revoked = false;
    
   

    /**
     * @return the revoked
     */
    public final boolean isRevoked() {
        boolean currentRevoke = revoked;
        revoked = false;
        return currentRevoke;
    }
    /**
     * @param isRevoked the revoked to set
     */
    public final void setRevoked(final boolean isRevoked) {
        this.revoked = isRevoked;
    }

    /**
     * The List of Sale item.
     */
    private List<Sale> itemList;

    /**
     * The Default Constructor.
     */
    public MixMatchItem() {
        this.itemList = new ArrayList<Sale>();
    }
    /**
     * Add a Sale in the MixMatchItem.
     * @param saleToAdd The Sale to add.
     */
    public void addItem(final Sale saleToAdd) {
        //Is the Computation for MixMatch during ItemEntry needs to be reset?
        this.itemList.add(saleToAdd);
    }
    
    /**
     * Compute the Discount for Promotion.
     * @return The list of {@link Discount}
     */
    public final List<Discount> computeDiscount() {

        if (this.getCode().equals(GENERAL_PROMOTION)) {
            return new ArrayList<Discount>();
        }

        List<Discount> discounts = null;
        if (this instanceof GroupMixMatchItem) {
           discounts = this.computeGroupMixMatchDiscount();
        } else {
           discounts = this.computeNormalMixMatchDiscount();
        }
        return discounts;
    }


    /**
     * Gets the item list.
     *
     * @return the itemList
     */
    public List<Sale> getItemList() {
        return itemList;
    }

    /**
     * Sets the item list.
     *
     * @param itemListToSet the itemList to set
     */
    public void setItemList(final List<Sale> itemListToSet) {
        this.itemList = itemListToSet;
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    public final String getCode() {
        return code;
    }

    /**
     * Sets the code.
     *
     * @param codeToSet the code to set
     */
    public final void setCode(final String codeToSet) {
        this.code = codeToSet;
    }

    /**
     * Sets the mix match data.
     *
     * @param newMixMatchData the new mix match data
     */
    public void setMixMatchData(final MixMatchData newMixMatchData) {
        this.mixMatchData = newMixMatchData;
    }

    /**
     * Gets the mix match data.
     *
     * @return the mix match data
     */
    public MixMatchData getMixMatchData() {
        return mixMatchData;
    }

    /**
     * Gets the sales sum of quantity.
     *
     * @return the sales sum quantity
     */
    public final int getSumSalesQuantity() {
        int quantity = 0;
        for (Sale sale : this.getItemList()) {
            quantity += sale.getQuantity();
        }
        return quantity;
    }

    /**
     * Sets the current index.
     *
     * @param newCurrentIndex the new current index
     */
    protected final void setCurrentIndex(final int newCurrentIndex) {
        this.currentIndex = newCurrentIndex;
    }

    /**
     * Gets the current index.
     *
     * @return the current index
     */
    protected final int getCurrentIndex() {
        return currentIndex;
    }

    /**
     * Sets the items saved.
     *
     * @param newNoItemsSaved the new items saved
     */
    protected final void setItemsSaved(final int newNoItemsSaved) {
        this.itemsSaved = newNoItemsSaved;
    }

    /**
     * Gets the items saved.
     *
     * @return the items saved
     */
    protected final int getItemsSaved() {
        return itemsSaved;
    }

    /**
     * Gets the counter for discount.
     *
     * @return the discount counter.
     */
    protected final int getDiscntCntr() {
        return discntCntr;
    }

    /**
     * Sets the counter for discount.
     *
     * @param newDiscntCntr the new discount counter
     */
    protected final void setDiscntCntr(final int newDiscntCntr) {
        this.discntCntr = newDiscntCntr;
    }

    /**
     * Computes the total of regularunitprice per sale.
     *
     * @param sales
     *            the list of sales.
     * @param isBySale
     *            if true, summation of regularunitprice per sale. if false,
     *            summation of regularunitprice x quantity per sale.
     * @return the total sales.
     */
    public final long getTotalSales(final List<Sale> sales,
            final boolean isBySale) {
        int total = 0;
        for (Sale sale : sales) {
            if (isBySale) {
                total += sale.getRegularSalesUnitPrice();
            } else {
                total += sale.getRegularSalesUnitPrice() * sale.getQuantity();
            }
        }
        return total;
    }

    /**
     * Updates an item in the MixMatchItem list.
     * @param itemEntryFlag The Item Entry Flag.
     * @param saleInput the new values for Sale
     * @return The {@link Promotion}
     */
	public final Promotion updateItem(final int itemEntryFlag,
			final Sale saleInput, final TerminalItem terminalItem) {
        int actualQuantity = saleInput.getQuantity();
        switch (itemEntryFlag) {
            case MixMatchItem.ADD_QUANTITY_ENTRY_FLAG:
                break;
            case MixMatchItem.REMOVE_QUANTITY_ENTRY_FLAG:
                actualQuantity = -actualQuantity;
                break;
            default:
               return null;
        }
        Sale targetSale = getItemEntry(saleInput.getItemEntryId());
        if (targetSale == null) {
            Promotion returnPromotion = new Promotion();
            returnPromotion.setDiscounts(new ArrayList<Discount>());
            returnPromotion.setNCRWSSResultCode(
                    ResultBase.PROMOTION.ITEM_ENTRYID_NOTFOUND);
            return returnPromotion;
        }

        boolean isGroupMixMatch =
                this instanceof GroupMixMatchItem;

        //Tell if to REVOKE or NOT.
        boolean hasRevoke = false;
        if (!isGroupMixMatch) {
          //Is not automatic Discount?
            hasRevoke = (!this.getCode().equals(MixMatchItem.GENERAL_PROMOTION))
                 && (this.getSumSalesQuantity()
                         >= ((NormalMixMatchData) this.getMixMatchData())
                             .getQuantity()[0]);
        }

        targetSale.setQuantity(targetSale.getQuantity() + actualQuantity);
        // adjustment for price override = 0
        if (targetSale.getSubInt10() == SUBINT10_ONE ||
        		targetSale.getSubInt10() == SUBINT10_TWO) {
            if (-1 < saleInput.getActualSalesUnitPrice()) {
		        targetSale.setPriceOverride(targetSale.getDiscountable()
		        		&& (saleInput.getInputType() != 0)
                        && targetSale.getActualSalesUnitPrice()
                            != saleInput.getActualSalesUnitPrice());
		    } 
        } else {
	        if (0 < saleInput.getActualSalesUnitPrice()) {
	            targetSale.setPriceOverride(targetSale.getDiscountable()
	                && targetSale.getActualSalesUnitPrice()
	                != saleInput.getActualSalesUnitPrice());
	        }
        }

        Promotion returnPromotion = new Promotion();
        //Is the Quantity equal to 0? If yes, it is considered that the target
        //item should be removed.
        if (targetSale.getQuantity() <= 0) {
            this.deleteItemEntry(targetSale);
			// Fix RES-7207-Delete TerminalItem's ItemEntry If No More Items
            // To avoid duplication error when re-adding the same itementry
			terminalItem.deleteTerminalItemEntry(targetSale.getItemEntryId());
        }

        //Re-start the computation.
        resetComputation();
        List<Discount> discounts = this.computeDiscount();

        returnPromotion.setDiscounts(DiscountItemsHandler
                .groupDiscounts(discounts));

        if (isGroupMixMatch && this.isRevoked()) {
                returnPromotion.setRevoke(
                        new Revoke(this.getMixMatchData().getCode()));
        } else if (hasRevoke) { //hasRevoke flag is for NormalMixMatch Item
                returnPromotion.setRevoke(
                        new Revoke(this.getMixMatchData().getCode()));
        }
        return returnPromotion;
    }

    /**
     * Get the target Sale by Item Entry ID.
     * @param itemEntryID The Item Entry ID.
     * @return  The target {@link Sale}
     */
    protected final Sale getItemEntry(final String itemEntryID) {
        for (Sale sale  : this.getItemList()) {
            if (sale.getItemEntryId().equals(itemEntryID)) {
                return sale;
            }
        }
        return null;
    }

    /**
     * Delete the target Sale in the Item List.
     * @param targetSale The instance of sale to be deleted.
     * @return true for success else false.
     */
    protected boolean deleteItemEntry(final Sale targetSale) {
        return this.getItemList().remove(targetSale);
    }

    /**
     * Gets the total sales.
     */
    protected final void resetSalesDiscCnt() {
        for (Sale sale : this.getItemList()) {
            sale.setDiscountCount(0);
        }
    }
    /**
     * Use to reset the calculation of discount.
     */
    protected abstract void resetComputation();
    /**
     * Method to perform Normal Mix and Match Computation.
     * @param itemListToSet The Item List for Mix Match Computation.
     * @return The List of Discounts.
     */
    protected abstract  List<Discount> mixMatchComputation(
            List<Sale> itemListToSet);
    /**
     * Provide Establishment for MustBuy MixMatch.
     * @return The List of Sales for Must buy discount implementation.
     */
    protected abstract List<Sale> mustBuyMixMatchEstablishMent();

    /**
     * Method to compute Discounts from Group Mix and Match.
     * @return The list of Discounts
     */
    private List<Discount> computeGroupMixMatchDiscount() {
        List<Sale> salesListCopy =
                SaleItemsHandler.copySales(this.getItemList());
        List<Sale> targetSalesToCompute =
                SaleItemsHandler.sortSalesDescending(
                        salesListCopy);
        targetSalesToCompute =
            SaleItemsHandler.extractAllSalesWithOutPriceOverride(
                    targetSalesToCompute);
        int prevDiscountCount =
                ((GroupMixMatchItem) this).getCurrentDiscountCount();
        List<Discount> discounts = mixMatchComputation(targetSalesToCompute);
        int newDiscounCount = discounts.size();
        this.setRevoked(
                (newDiscounCount != 0 || prevDiscountCount != newDiscounCount)
                && prevDiscountCount != 0);
        return discounts;
    }
    /**
     * Method to compute Discounts from Normal Mix and Match.
     * @return the List of Discounts
     */
    private List<Discount> computeNormalMixMatchDiscount() {
        //For Normal Mix and Match Computation here.
        List<Sale> targetSalesToCompute = null;
        NormalMixMatchData normalMMData =
                (NormalMixMatchData) this.getMixMatchData();
        
        if(normalMMData.getMustBuyFlag() ==  NormalMixMatchData.MUST_BUY_MIX_MATCH){
                targetSalesToCompute = mustBuyMixMatchEstablishMent();
                this.resetComputation();
        }else{            
                List<Sale> salesListCopy =
                    SaleItemsHandler.copySales(this.getItemList());

                int mmType = this.getMixMatchData().getType();
                switch (mmType) {
                    case FIRST_STEP_REPETITION_PRICE:
                    case SECOND_STEP_REPETITION_PRICE:
                    case FIRST_STEP_REPETITION_RATE:
                    case SECOND_STEP_REPETITION_RATE:
                    case FIRST_STEP_REPETITION_AMOUNT:
                    case SECOND_STEP_REPETITION_AMOUNT:
                        SaleItemsHandler.reverseSales(
                                salesListCopy);
                        break;
                    default:
                        break;
                }

                targetSalesToCompute =
                    SaleItemsHandler.sortSalesDescending(
                            salesListCopy);
                targetSalesToCompute =
                    SaleItemsHandler.extractAllSalesWithOutPriceOverride(
                            targetSalesToCompute);

                boolean hasRevoke =
                    //Is not automatic Discount?
                    (!this.getCode().equals(MixMatchItem.GENERAL_PROMOTION))
                    && (this.getItemsSaved()
                            >= normalMMData.getQuantity()[0]);
                this.setRevoked(hasRevoke);
                resetComputation();
        }

        List<Discount> discounts = mixMatchComputation(targetSalesToCompute);
        return discounts;
    }
}
