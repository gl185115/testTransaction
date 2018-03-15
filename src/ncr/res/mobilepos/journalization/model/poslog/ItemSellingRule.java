/*
 * Copyright (c) 2013 NCR/JAPAN Corporation SW-R&D
 *
 * ItemSellingRule
 *
 * Model Class for ItemSellingRule
 *
 * Vener G. Rosales
 */
package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * ItemSellingRule Model Object.
 *
 * <P>An ItemSellingRule Node in POSLog XML.
 *
 * <P>The ItemSellingRule node is under Sale Node.
 * And holds the value for posmdtype and discounttype
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ItemSellingRule")
public class ItemSellingRule {
    /**
     * The private member variable that will hold the itemDiscount flag.
     */
    @XmlAttribute(name = "ItemDiscountFlag")
    private int itemDiscountFlag;
    
    /**
     * The private member variable that will hold the totalDiscount flag.
     */
    @XmlAttribute(name = "TotalDiscountFlag")
    private int totalDiscountFlag;
    
    /**
     * The private member variable that will hold the nonItem flag.
     */
    @XmlAttribute(name = "NonItemFlag")
    private int nonItemFlag;
    
    /**
     * The private member variable that will hold the point flag.
     */
    @XmlAttribute(name = "PointFlag")
    private int pointFlag;
    
    /**
     * The private member variable that will hold the discountable flag.
     */
    @XmlAttribute(name = "DiscountableFlag")
    private String discountableFlag;
    /**
     * The private member variable that will hold the non sales flag.
     */
    @XmlAttribute(name = "NonSalesFlag")
    private String nonSalesFlag;

    /**
     * Sets the NonSalesFlag under ItemSellingRule.
     *
     * @param  nonSalesFlagToSet       The new value for the Tax under LineItem.
     */
    public final void setNonSalesFlag(final String nonSalesFlagToSet) {
        this.nonSalesFlag = nonSalesFlagToSet;
    }
    /**
     * Gets the NonSalesFlag under ItemSellingRule.
     *
     * @return        The nonSalesFlag under ItemSellingRule.
     */
    public final String getNonSalesFlag() {
        return nonSalesFlag;
    }

    /**
     * Sets the DiscountableFlag under ItemSellingRule.
     *
     * @param discountableFlagToSet    The new value for the DiscountableFlag
     * under ItemSellingRule.
     */
    public final void setDiscountableFlag(final String discountableFlagToSet) {
        this.discountableFlag = discountableFlagToSet;
    }
    /**
     * Gets the DiscountableFlag under ItemSellingRule.
     *
     * @return        The discountableFlag under ItemSellingRule.
     */
    public final String getDiscountableFlag() {
        return discountableFlag;
    }

    /**
     * Overrides the toString() Method.
     * @return The string representation of tax.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("DiscountableFlag : ")
           .append(this.discountableFlag).append(crlf)
           .append("NonSalesFlag : ").append(this.nonSalesFlag).append(crlf)
           .append("itemDiscountFlag : ").append(this.itemDiscountFlag).append(crlf)
           .append("pointFlag : ").append(this.pointFlag).append(crlf)
           .append("nonItemFlag : ").append(this.nonItemFlag).append(crlf)
           .append("totalDiscountFlag : ").append(this.totalDiscountFlag).append(crlf)
           .append("PointFlag : ").append(this.pointFlag);

        return str.toString();
    }
    /**
     * @return the itemDiscountFlag
     */
    public final int getItemDiscountFlag() {
        return itemDiscountFlag;
    }
    /**
     * @param itemDiscountFlag the itemDiscountFlag to set
     */
    public final void setItemDiscountFlag(final int itemDiscountFlag) {
        this.itemDiscountFlag = itemDiscountFlag;
    }
    /**
     * @return the totalDiscountFlag
     */
    public final int getTotalDiscountFlag() {
        return totalDiscountFlag;
    }
    /**
     * @param totalDiscountFlag the totalDiscountFlag to set
     */
    public final void setTotalDiscountFlag(final int totalDiscountFlag) {
        this.totalDiscountFlag = totalDiscountFlag;
    }
    /**
     * @return the nonItemFlag
     */
    public final int getNonItemFlag() {
        return nonItemFlag;
    }
    /**
     * @param nonItemFlag the nonItemFlag to set
     */
    public final void setNonItemFlag(final int nonItemFlag) {
        this.nonItemFlag = nonItemFlag;
    }
    /**
     * @return the pointFlag
     */
    public final int getPointFlag() {
        return pointFlag;
    }
    /**
     * @param pointFlag the pointFlag to set
     */
    public final void setPointFlag(int pointFlag) {
        this.pointFlag = pointFlag;
    }
}
