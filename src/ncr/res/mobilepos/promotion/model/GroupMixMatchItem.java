package ncr.res.mobilepos.promotion.model;

import java.util.ArrayList;
import java.util.Map;
import java.util.HashMap;
import java.util.List;

import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.promotion.helper.SaleItemsHandler;

/**
 * @author Developer
 *
 */
public class GroupMixMatchItem extends MixMatchItem {
    /** The current discount count. */
    private int currentDiscountCount;
    /** The SubItems inside the Group Mix Match. */
    private List<GroupMixMatchSubItem> subItems;

    /**
     * @param codeToSet The Code to set
     */
    public GroupMixMatchItem(final String codeToSet) {
        this.subItems = new ArrayList<GroupMixMatchSubItem>();
        this.currentDiscountCount = 0;
        this.setCode(codeToSet);
    }

    /**
     * @param subItemsToSet the subItems to set
     */
    public final void setSubItems(
            final List<GroupMixMatchSubItem> subItemsToSet) {
        this.subItems = subItemsToSet;
    }

    /**
     * @return the subItems
     */
    public final List<GroupMixMatchSubItem> getSubItems() {
        return subItems;
    }
    /** @return the current discount count. */
    public final int getCurrentDiscountCount() {
        return currentDiscountCount;
    }


    /**
     * Add a Sale in the MixMatchItem.
     * @param saleToAdd The Sale to add.
     */
    @Override
    public final void addItem(final Sale saleToAdd) {
        super.addItem(saleToAdd);
        String mmCode = saleToAdd.getMixMatchCode();
        for (MixMatchItem mmSubItem : this.subItems) {
            if (mmSubItem.getCode().equals(mmCode)) {
                mmSubItem.addItem(saleToAdd);
                break;
            }
        }
    }

    /**
     * Delete the target Sale in the Item List.
     * @param targetSale The instance of sale to be deleted.
     * @return true for success else false.
     */
    @Override
    protected final boolean deleteItemEntry(final Sale targetSale) {
        for (MixMatchItem mmSubItem : this.subItems) {
            mmSubItem.deleteItemEntry(targetSale);
        }
        return super.deleteItemEntry(targetSale);
    }

    /**
     * Sets the item list.
     *
     * @param itemListToSet the itemList to set
     */
    @Override
    public final void setItemList(final List<Sale> itemListToSet) {

        super.setItemList(itemListToSet);

        // clear existing sale lists
        for (MixMatchItem mmSubItem : this.subItems) {
            mmSubItem.setItemList(new ArrayList<Sale>());
        }

        // add each item in itemListToSet to its appropriate MixMatchItem
        for (Sale item : itemListToSet) {
            for (MixMatchItem mmSubItem : this.subItems) {
                if (mmSubItem.getCode().equals(item.getMixMatchCode())) {
                    mmSubItem.addItem(item);
                }
            }
        }
    }


    /**
     * Gets the mix match data.
     *
     * @return the mix match data
     */
    @Override
    public final GroupMixMatchData getMixMatchData() {
        return (GroupMixMatchData) this.mixMatchData;
    }

    /* (non-Javadoc)
     * @see ncr.res.mobilepos.promotion.model.MixMatchItem#resetComputation()
     */
    @Override
    protected void resetComputation() {
        // resetComputation() not used in GroupMixMatch

    }

    /* (non-Javadoc)
     * @see ncr.res.mobilepos.promotion.model.MixMatchItem#mixMatchComputation
     * (java.util.List)
     */
    @Override
    protected final List<Discount> mixMatchComputation(
            final List<Sale> itemListToSet) {
        class SaleMutableCopy {
            /** The pair count. */
            private int pairCount;
            /** The Sale. */
            private Sale saleItem;
            /**
             * The custom constructor.
             * @param aSaleToSet The Sale to copy.
             */
            public SaleMutableCopy(final Sale aSaleToSet) {
                this.pairCount = 0;
                this.saleItem = aSaleToSet;
            }
            /**
             * Tell of the MixMatch has pair.
             * @return true if able to pair, Else, false.
             */
            public final boolean hasPairable() {
                return pairCount < saleItem.getQuantity();
            }
            /**
             * Get the paircount.
             * @return return the pair count.
             */
            public final int getPairCount() {
                return pairCount;
            }
            /**
             * Get the Sale Quantity.
             * @return the Sa;e's quantity.
             */
            public final int getQuantity() {
                return saleItem.getQuantity();
            }
            /** @return  the ItemEntryID. */
            public final String getItementryid() {
                return saleItem.getItemEntryId();
            }
            /** @return TRUE, if the next Item is paired, else, FALSE */
            public final boolean pairNextItem() {
                if (this.hasPairable()) {
                    pairCount++;
                    return true;
                } else {
                    return false;
                }
            }
        }
        class SubItemMutableCopy {
            private GroupMixMatchSubItem mixMatchItem;
            private List<SaleMutableCopy> itemList;
            private boolean disCountApplicable;
            private int totalItemCount;
            private int pairedCount;
            private int currentIndex;

            public SubItemMutableCopy (final GroupMixMatchSubItem anItem) {
                this.mixMatchItem = anItem;
                List<Sale> tempItemList = SaleItemsHandler.sortSalesDescending(
                                    SaleItemsHandler.reverseSales(
                                            SaleItemsHandler.copySales(
                                                    anItem.getItemList())));
                this.itemList = new ArrayList<SaleMutableCopy>();
                totalItemCount = 0;
                pairedCount = 0;
                currentIndex = 0;
                for (Sale aSale : tempItemList) {
                    if (!aSale.isPriceOverride()) {
                        this.totalItemCount += aSale.getQuantity();
                        this.itemList.add(new SaleMutableCopy(aSale));
                    }
                }
                this.disCountApplicable = false;
            }
            /** @return the ItemList. */
            public final List<SaleMutableCopy> getItemList() {
                return itemList;
            }
            /** @return the GroupMixMatch SubItem. */
            public final GroupMixMatchSubItem getMixMatchItem() {
                return mixMatchItem;
            }

            /** @return TRUE, if discount count is applicable, else, false. */
            public final boolean isDisCountApplicable() {
                return disCountApplicable;
            }
            /**
             * Set the "discount count if applicable" flag.
             * @param disCountApplicable
             */
            public final void setDisCountApplicable(
                    final boolean disCountApplicable) {
                this.disCountApplicable = disCountApplicable;
            }
            /** @return TRUE, if pairable, else, FALSE. */
            public final boolean hasPairable() {
                return pairedCount < totalItemCount;
            }
            /** @return TRUE, if next item is paired, else FALSE. */
            public final boolean pairNextItem() {
                if (this.hasPairable()) {
                    SaleMutableCopy currentSalte =
                        this.itemList.get(currentIndex);
                    while (!currentSalte.pairNextItem()) {
                        currentIndex++;
                        currentSalte = this.itemList.get(currentIndex);
                    }
                    pairedCount++;
                    return true;
                } else {
                    return false;
                }
            }
        }

        class DiscountFactor {
        	
            private int factor;
            
            private int mixMatchType;
            
            private long disCount;

            public DiscountFactor (final int aFactor,
                    final int aMixMatchType, final long aDisCount) {
                this.factor = aFactor;
                this.mixMatchType = aMixMatchType;
                this.disCount = aDisCount;
            }
        }

        class GroupSet {
            private List<SubItemMutableCopy> subItemClones;

            public GroupSet (final List<SubItemMutableCopy> aSubItemClones) {
                this.subItemClones = aSubItemClones;
            }

            public boolean canCreateSet(
                    final SubItemMutableCopy referenceClone) {
                // Check if there are available items that
                // can be paired in other groups
                boolean ret = true;
                for (SubItemMutableCopy otherClone : this.subItemClones) {
                    if ((otherClone != referenceClone) && (!otherClone.hasPairable())) {
                            ret = false;
                            break;
                    }
                }
                return ret;
            }

            public void createSet(){
                // Apply discounts if all of the
                // other groups has available item to be paired
                for (SubItemMutableCopy aGroup : this.subItemClones) {
                    aGroup.pairNextItem();
                    if (aGroup.getMixMatchItem()
                            instanceof GroupMixMatchUniformityItem) {
                        aGroup.setDisCountApplicable(true);
                    }
                }
            }

            public List<Discount> getDiscountList(final List<Sale> itemList) {
                List<Discount> discountList = new ArrayList<Discount>();
                Map<String, DiscountFactor> discountFactor =
                    this.getDiscountFactor();
                MixMatchData mmData = GroupMixMatchItem.this.getMixMatchData();
                int rewardId = 0;
                for (Sale item : itemList) {
                    String itemEntryId = item.getItemEntryId();
                    DiscountFactor factor = discountFactor.get(itemEntryId);
                    if (factor != null) {
                        rewardId++;
                        Discount discount = new Discount();
                        discount.setRewardID(rewardId);
                        discount.setEarnedRewardID(mmData.getCode());
                        discount.setPromotionCode(mmData.getCode());
                        discount.setDiscountDescription(mmData.getName());
                        discount.setItemEntryID(item.getItemEntryId());

                        long mmDiscountAmount = 0;

                        switch (factor.mixMatchType) {
                        case MixMatchItem.GROUP_REPETITION_AMOUNT:
                        case MixMatchItem.GROUP_UNIFORMITY_AMOUNT:
                            mmDiscountAmount = factor.disCount * factor.factor;
                            break;
                        case MixMatchItem.GROUP_REPETITION_PRICE:
                        case MixMatchItem.GROUP_UNIFORMITY_PRICE:
                            mmDiscountAmount =
                                Math.round((item.getRegularSalesUnitPrice()
                                        - factor.disCount) * factor.factor);
                            break;
                        case MixMatchItem.GROUP_REPETITION_RATE:
                        case MixMatchItem.GROUP_UNIFORMITY_RATE:                        	
                        	mmDiscountAmount = this.computeUniformityRateDiscountAmount( item.getRegularSalesUnitPrice(), (double) factor.disCount, factor.factor);
                            break;
                        default:
                            break;
                        }

                        if (mmDiscountAmount < 0) {
                            mmDiscountAmount = 0;
                        }
                        discount.setUnitDiscountAmount((int) mmDiscountAmount);
                        discountList.add(discount);
                    }
                }
                return discountList;
            }

            private Map<String, DiscountFactor> getDiscountFactor() {
                Map<String, DiscountFactor> discountFactor =
                    new HashMap<String, DiscountFactor>();
                for (SubItemMutableCopy aGroup : this.subItemClones) {

                    NormalMixMatchData mixMatchData =
                        (NormalMixMatchData) aGroup.getMixMatchItem()
                                                 .getMixMatchData();
                    int mixMatchType = mixMatchData.getType();
                    long disCountPrice = mixMatchData.getDiscountprice()[0];

                    List<SaleMutableCopy> itemList = aGroup.getItemList();

                    if (aGroup.getMixMatchItem()
                            instanceof GroupMixMatchUniformityItem) {
                        if (aGroup.isDisCountApplicable()) {
                            for (SaleMutableCopy item : itemList) {
                                discountFactor.put(
                                        item.getItementryid(),
                                        new DiscountFactor(
                                                item.getQuantity(),
                                                mixMatchType,
                                                disCountPrice));
                            }
                        }
                    } else {
                        int pairCount = 0;
                        for (SaleMutableCopy item : itemList) {
                            pairCount = item.getPairCount();
                            if (pairCount > 0) {
                                discountFactor.put(
                                        item.getItementryid(),
                                        new DiscountFactor(
                                                item.getPairCount(),
                                                mixMatchType,
                                                disCountPrice));
                            }
                        }
                    }
                }
                return discountFactor;
            }            
            private long computeUniformityRateDiscountAmount(double regularSalesUnitPrice, double disCount, int ifactor){    	
            	return Math.round((regularSalesUnitPrice
                        * (disCount
                           / (double) GlobalConstant.PERCENT))
                        * ifactor);              	
            }
        }

        // Create hard copies of sub items
        List<SubItemMutableCopy> subItemClones =
            new ArrayList<SubItemMutableCopy>();
        for (GroupMixMatchSubItem subItem : this.subItems) {
            subItemClones.add(new SubItemMutableCopy(subItem));
        }

        // Pair available items
        GroupSet groupSet = new GroupSet(subItemClones);
        for (SubItemMutableCopy subItemClone : subItemClones) {
            if (subItemClone.getMixMatchItem()
                    instanceof GroupMixMatchUniformityItem) {
                if ((!subItemClone.isDisCountApplicable())
                        && (subItemClone.hasPairable())
                        && (groupSet.canCreateSet(subItemClone))) {
                            groupSet.createSet();
                }
            } else {
                while (subItemClone.hasPairable()) {
                    if (groupSet.canCreateSet(subItemClone)) {
                        groupSet.createSet();
                    } else {
                        break;
                    }
                }
            }
        }

        List<Discount> disCountList =
            groupSet.getDiscountList(this.getItemList());
        this.currentDiscountCount = disCountList.size();
        return disCountList;
    }

    /* (non-Javadoc)
     * @see ncr.res.mobilepos.promotion.model
     * .MixMatchItem#mustBuyMixMatchEstablishMent()
     */
    @Override
    protected final List<Sale> mustBuyMixMatchEstablishMent() {
        // Must Buy is not supported in Group MixMatch
        return this.getItemList();
    } 
}
