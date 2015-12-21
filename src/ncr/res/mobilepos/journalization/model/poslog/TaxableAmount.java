/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * TaxableAmount
 *
 * Model Class for TaxableAmount
 *
 * De la Cerna, Jessel G.
 * Carlos G. Campos
 */

package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * TaxableAmount Model Object.
 *
 * <P>A TaxableAmount Node in POSLog XML.
 *
 * <P>The TaxableAmount node is under Tax Node.
 * And holds the value that is taxable
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TaxableAmount")
public class TaxableAmount {

    /**
     * The private member variable that will hold the tax
     *                      included in taxable amount.
     */
    @XmlAttribute(name = "TaxIncludedInTaxableAmountFlag")
    private String taxIncludedInTaxableAmountFlag;
    /**
     * The Amount.
     */
    @XmlValue
    private String amount;

    /**
     * Gets the taxable amount.
     *
     * @return        The taxable amount.
     */
    public final String getAmount() {
        return amount;
    }

    /**
     * Sets the taxable amount.
     *
     * @param amountToSet        The new value for taxable amount.
     */
    public final void setAmount(final String amountToSet) {
        this.amount = amountToSet;
    }

    /**
     * Sets the tax included in taxable amount.
     *
     * @param taxIncludedInTaxableAmountFlagToSet
     *              The new value for tax included in taxable amount.
     */
    public final void setTaxIncludedInTaxableAmountFlag(
            final String taxIncludedInTaxableAmountFlagToSet) {
        taxIncludedInTaxableAmountFlag = taxIncludedInTaxableAmountFlagToSet;
    }

    /**
     * Gets the tax included in taxable amount.
     *
     * @return        The tax included in taxable amount.
     */
    public final String getTaxIncludedInTaxableAmountFlag() {
        return taxIncludedInTaxableAmountFlag;
    }

    /**
     * Overrides the toString() Method.
     * @return the String representation of TaxableAmount.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("Amount : ").append(this.amount).append(crlf)
           .append("TaxIncludedInTaxableAmountFlag : ")
           .append(this.taxIncludedInTaxableAmountFlag);
        return str.toString();
    }
}
