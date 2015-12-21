package ncr.res.mobilepos.networkreceipt.model;

import java.util.List;

public class ReceiptMixMatchBlock {
    /**
     * Mix Match rule id.
     */
    private String mmRuleID;
    /**
     * Mix Match name.
     */
    private String mmName;
    /**
     * List of purchased items with mix match.
     */
    private List<ReceiptProductItem> productItemList;
    /**
     * The Previous Price.
     */
    private double mmPreviousPrice;
    /**
     * The count of items with mix match.
     */
    private int mmItemCount;
    /**
     * The Amount value of mix match.
     */
    private double mmAmount;
    /**
     * The Sales Price of items with mix match.
     */
    private double mmItemsSalesPrice;

    /**
     * @return productItemList
     */
    public final List<ReceiptProductItem> getProductItemList() {
        return productItemList;
    }
    /**
     * @param productItemList  productItemList
     */
    public final void setProductItemList(
            final List<ReceiptProductItem> productItemList) {
        this.productItemList = productItemList;
    }
    /**
     * @return mmPreviousPrice
     */
    public final double getMmPreviousPrice() {
        return mmPreviousPrice;
    }
    /**
     * @param mmPreviousPrice  mmPreviousPrice
     */
    public final void setMmPreviousPrice(final double mmPreviousPrice) {
        this.mmPreviousPrice = mmPreviousPrice;
    }
    /**
     * @return mmItemCount
     */
    public final int getMmItemCount() {
        return mmItemCount;
    }
    /**
     * @param mmItemCount  mmItemCount
     */
    public final void setMmItemCount(final int mmItemCount) {
        this.mmItemCount = mmItemCount;
    }
    /**
     * @return mmAmount
     */
    public final double getMmAmount() {
        return mmAmount;
    }
    /**
     * @param mmAmount  mmAmount
     */
    public final void setMmAmount(final double mmAmount) {
        this.mmAmount = mmAmount;
    }
    /**
     * @return mmItemsSalesPrice
     */
    public final double getMmItemsSalesPrice() {
        return mmItemsSalesPrice;
    }
    /**
     * @param mmItemsSalesPrice  mmItemsSalesPrice
     */
    public final void setMmItemsSalesPrice(final double mmItemsSalesPrice) {
        this.mmItemsSalesPrice = mmItemsSalesPrice;
    }
    /**
     * @return mmRuleID
     */
    public final String getMmRuleID() {
        return mmRuleID;
    }
    /**
     * @param mmRuleID  mmRuleID
     */
    public final void setMmRuleID(String mmRuleID) {
        this.mmRuleID = mmRuleID;
    }
    /**
     * @return mmName
     */
    public final String getMmName() {
        return mmName;
    }
    /**
     * @param mmName  mmName
     */
    public final void setMmName(String mmName) {
        this.mmName = mmName;
    }
}
