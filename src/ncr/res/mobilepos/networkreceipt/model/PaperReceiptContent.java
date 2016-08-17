/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.networkreceipt.model;

import java.util.List;

/**
 * PaperReceiptContent Class is a Model representation of the
 * information which identifies the content of paper receipt.
 */
public class PaperReceiptContent {
    /**
     * date time of the receipt.
     */
    private String receiptDateTime;
    /**
     * Void date time.
     */
    private String voidDateTime;
    /**
     * Customer tier code.
     */
    private String customerTierCode;
    /**
     * Customer tier name.
     */
    private String customerTierName;
    /**
     * List of purchased items.
     */
    private List<ReceiptProductItem> productItemList;
    /**
     * List of mix match blocks
     */
    private List<ReceiptMixMatchBlock> mmBlockList;
    /**
     * total amount due.
     */
    private double total;
    /**
     * the amount paid in credit.
     */
    private long paymentCredit;
    /**
     * the amount paid in cash.
     */
    private long paymentCash;
    /**
     * the change due.
     */
    private double paymentChange;
    /**
     * miscellaneous tender
     */
    private long paymentVoucher;
    /**
     * total amount due.
     */
    private double subTotal;
    /**
     * total dicount amount.
     */
    private String discount;
    /**
     * the tax amount.
     */
    private String tax;
    /**
     * the training mode flag
     */
    private boolean trainingModeFlag;
    /**
     * @return receiptDateTime
     */
    public final String getReceiptDateTime() {
        return receiptDateTime;
    }
    /**
     * @param receiptDateTimeToSet  receiptDateTime
     */
    public final void setReceiptDateTime(final String receiptDateTimeToSet) {
        this.receiptDateTime = receiptDateTimeToSet;
    }
    public final String getVoidDateTime() {
        return voidDateTime;
    }
    public final void setVoidDateTime(final String voidDateTime) {
        this.voidDateTime = voidDateTime;
    }
    public final String getCustomerTierCode() {
        return customerTierCode;
    }
    public final void setCustomerTierCode(final String customerTierCode) {
        this.customerTierCode = customerTierCode;
    }
    public final String getCustomerTierName() {
        return customerTierName;
    }
    public final void setCustomerTierName(final String customerTierName) {
        this.customerTierName = customerTierName;
    }
    /**
     * @return productItemList
     */
    public final List<ReceiptProductItem> getProductItemList() {
        return productItemList;
    }
    /**
     * @param productItemListToSet  productItemList
     */
    public final void setProductItemList(
            final List<ReceiptProductItem> productItemListToSet) {
        this.productItemList = productItemListToSet;
    }
    /**
     * @return total
     */
    public final double getTotal() {
        return total;
    }
    /**
     * @param totalToSet  total
     */
    public final void setTotal(final double totalToSet) {
        this.total = totalToSet;
    }
    /**
     * @return paymentCredit
     */
    public final long getPaymentCredit() {
        return paymentCredit;
    }
    /**
     * @param paymentCreditToSet  paymentCredit
     */
    public final void setPaymentCredit(final long paymentCreditToSet) {
        this.paymentCredit = paymentCreditToSet;
    }
    /**
     * @return paymentCash
     */
    public final long getPaymentCash() {
        return paymentCash;
    }
    /**
     * @param paymentCashToSet  paymentCash
     */
    public final void setPaymentCash(final long paymentCashToSet) {
        this.paymentCash = paymentCashToSet;
    }
    /**
     * @return paymentChange
     */
    public final double getPaymentChange() {
        return paymentChange;
    }
    /**
     * @param paymentChangeToSet  paymentChange
     */
    public final void setPaymentChange(final double paymentChangeToSet) {
        this.paymentChange = paymentChangeToSet;
    }
    
    public long getPaymentVoucher() {
        return paymentVoucher;
    }
    public void setPaymentVoucher(final long paymentVoucher) {
        this.paymentVoucher = paymentVoucher;
    }
    /**
     * @return subTotal
     */
    public final double getSubTotal() {
        return subTotal;
    }
    /**
     * @param subTotalToSet  subTotal
     */
    public final void setSubTotal(final double subTotalToSet) {
        this.subTotal = subTotalToSet;
    }
    /**
     * @return discount
     */
    public final String getDiscount() {
        return discount;
    }
    /**
     * @param discountToSet  discount
     */
    public final void setDiscount(final String discountToSet) {
        this.discount = discountToSet;
    }
    /**
     * @return tax
     */
    public final String getTax() {
        return tax;
    }
    /**
     * @param taxToSet  tax
     */
    public final void setTax(final String taxToSet) {
        this.tax = taxToSet;
    }
    /**
     * @return mmBlockList
     */
    public final List<ReceiptMixMatchBlock> getMmBlockList() {
        return mmBlockList;
    }
    /**
     * @param mmBlockList  mmBlockList
     */
    public final void setMmBlockList(
            final List<ReceiptMixMatchBlock> mmBlockList) {
        this.mmBlockList = mmBlockList;
    }
    
    public final boolean isTrainingModeFlag() {
        return trainingModeFlag;
    }
    public final void setTrainingModeFlag(final boolean trainingModeFlag) {
        this.trainingModeFlag = trainingModeFlag;
    }
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Discount=" + discount);
        sb.append("\nPayment Cash=" + paymentCash);
        sb.append("\nPayment Change=" + paymentChange);
        sb.append("\nPayment Credit=" + paymentCredit);
        sb.append("\nProduct Item List Count=" + productItemList.size());
        sb.append("\nReceipt Date Time=" + receiptDateTime);
        sb.append("\nSubTotal=" + subTotal);
        sb.append("\nTax=" + tax);
        sb.append("\nTotal=" + total);
        return sb.toString();
    }
}
