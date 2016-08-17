/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.networkreceipt.model;

/**
 * ReceiptProductItem Class is a Model representation of the
 * information which identifies the detail of products.
 */
public class ReceiptProductItem {
    /**
     * product name.
     */
    private String productName;
    /**
     * unit price.
     */
    private double price;
    /**
     * quantity.
     */
    private int quantity;
    /**
     * total amount of item.
     */
    private double amount;
    /**
     * total discount amount of the item.
     */
    private double discountAmount;
    /**
     * @return productName
     */
    public final String getProductName() {
        return productName;
    }
    /**
     * @param productNameToSet  productName
     */
    public final void setProductName(final String productNameToSet) {
        this.productName = productNameToSet;
    }
    /**
     * @return price
     */
    public final double getPrice() {
        return price;
    }
    /**
     * @param priceToSet  price
     */
    public final void setPrice(final double priceToSet) {
        this.price = priceToSet;
    }
    /**
     * @return quantity
     */
    public final int getQuantity() {
        return quantity;
    }
    /**
     * @param quantityToSet  quantity
     */
    public final void setQuantity(final int quantityToSet) {
        this.quantity = quantityToSet;
    }
    /**
     * @return amount
     */
    public final double getAmount() {
        return amount;
    }
    /**
     * @param amountToSet  amount
     */
    public final void setAmount(final double amountToSet) {
        this.amount = amountToSet;
    }
    /**
     * @return discountAmount
     */
    public final double getDiscountAmount() {
        return discountAmount;
    }
    /**
     * @param discountAmountToSet  discountAmount
     */
    public final void setDiscountAmount(final double discountAmountToSet) {
        this.discountAmount = discountAmountToSet;
    }

}
