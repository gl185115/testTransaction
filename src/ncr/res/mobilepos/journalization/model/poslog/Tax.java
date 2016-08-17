/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * Tax
 *
 * Model Class for Tax
 *
 * Carlos G. Campos
 */

package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Tax Model Object.
 *
 * <P>A Tax Node in POSLog XML.
 *
 * <P>The Tax node is under LineItem Node.
 * And holds the tax details whenever it is applicable.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Tax")
public class Tax {

    /**
     * The private member variable that will hold the tax type of Tax.
     */
    @XmlAttribute(name = "TaxType")
    private String taxType;

    /**
     * The private member variable that will hold the tax sub type of Tax.
     */
    @XmlAttribute(name = "TaxSubType")
    private String taxSubType;

    /**
     * The private member variable that will hold the normal tax type value of Tax.
     */
    @XmlAttribute(name = "NormalTaxType")
    private String normalTaxType;

    /**
     * The private member variable that will hold the type code value of Tax.
     */
    @XmlAttribute(name = "TypeCode")
    private String typeCode;

    /**
     * The private member variable that will hold the taxable amount of Tax.
     */
    @XmlElement(name = "TaxableAmount")
    private TaxableAmount taxableAmt;

    /**
     * The private member variable that will hold the amount of Tax.
     */
    @XmlElement(name = "Amount")
    private String amount;

    /**
     * The private member variable that will hold the taxIncludeAmount of Tax.
     */
    @XmlElement(name = "TaxIncludeAmount")
    private int taxIncludeAmount;

    /**
     * The private member variable that will hold the taxExcludeAmount of Tax.
     */
    @XmlElement(name = "TaxExcludeAmount")
    private int taxExcludeAmount;

    /**
     * The private member variable that will hold the percent of Tax.
     */
    @XmlElement(name = "Percent")
    private String percent;

    /**
     * Holds the default value of "StampDuty" for Documentary Tax Stamp
     */
    @XmlElement(name = "TaxAuthority")
    private String taxAuthority;

    /**
     * The private member variable that will hold text notes for this line item
     */
    @XmlElement(name = "Note")
    private String note;

    /**
     * Tax exempt (ñ∆ê≈)
     */
    @XmlElement(name = "TaxExempt")
    private TaxExempt taxExempt;

    /**
     * Gets TaxAuthority
     * @return
     */
    public String getTaxAuthority() {
        return taxAuthority;
    }
    /**
     * Sets TaxAuthority
     * @param taxAuthority
     */
    public void setTaxAuthority(String taxAuthority) {
        this.taxAuthority = taxAuthority;
    }

    /**
     * Gets the tax type of Tax.
     *
     * @return        The tax type of Tax.
     */
    public final String getTaxType() {
        return taxType;
    }

    /**
     * Sets the tax type of Tax.
     *
     * @param taxTypeToSet       The new value for ax type of Tax.
     */
    public final void setTaxType(final String taxTypeToSet) {
        this.taxType = taxTypeToSet;
    }

    /**
     * Gets the taxable amount of Tax.
     *
     * @return        The tax type of Tax.
     */
    public final TaxableAmount getTaxableAmt() {
        return taxableAmt;
    }

    /**
     * Sets the taxable amount of Tax.
     *
     * @param taxableAmtToSet        The new value for taxable amount of Tax.
     */
    public final void setTaxableAmt(final TaxableAmount taxableAmtToSet) {
        this.taxableAmt = taxableAmtToSet;
    }

    /**
     * Gets the amount of Tax.
     *
     * @return        The amount of Tax.
     */
    public final String getAmount() {
        return amount;
    }

    /**
     * Sets the amount of Tax.
     *
     * @param amountToSet        The new value for amount of Tax.
     */
    public final void setAmount(final String amountToSet) {
        this.amount = amountToSet;
    }

    /**
     * Gets the taxIncludeAmount of Tax.
     *
     * @return        The taxIncludeAmount of Tax.
     */
    public final int getTaxIncludeAmount() {
        return taxIncludeAmount;
    }

    /**
     * Sets the taxIncludeAmount of Tax.
     *
     * @param taxIncludeAmount        The new value for taxIncludeAmount of Tax.
     */
    public final void setTaxIncludeAmount(final int taxIncludeAmount) {
        this.taxIncludeAmount = taxIncludeAmount;
    }

    /**
     * Gets the taxExcludeAmount of Tax.
     *
     * @return        The taxExcludeAmount of Tax.
     */
    public final int getTaxExcludeAmount() {
        return taxExcludeAmount;
    }

    /**
     * Sets the taxExcludeAmount of Tax.
     *
     * @param taxExcludeAmount        The new value for taxExcludeAmount of Tax.
     */
    public final void setTaxExcludeAmount(final int taxExcludeAmount) {
        this.taxExcludeAmount = taxExcludeAmount;
    }

    /**
     * Gets the percent of Tax.
     *
     * @return        The percent of Tax.
     */
    public final String getPercent() {
        return percent;
    }

    /**
     * Sets the percent of Tax.
     *
     * @param percentToSet        The new value for percent of Tax.
     */
    public final void setPercent(final String percentToSet) {
        this.percent = percentToSet;
    }

    /**
     * @return the normalTaxType
     */
    public final String getNormalTaxType() {
        return normalTaxType;
    }

    /**
     * @param normalTaxType the normalTaxType to set
     */
    public final void setNormalTaxType(String normalTaxType) {
        this.normalTaxType = normalTaxType;
    }

    /**
     * @return the typeCode
     */
    public final String getTypeCode() {
        return typeCode;
    }

    /**
     * @param typeCode		the new typeCode to set
     */
    public final void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * @return the taxSubType
     */
    public String getTaxSubType() {
        return taxSubType;
    }

    /**
     * @param taxSubType the taxSubType to set
     */
    public void setTaxSubType(String taxSubType) {
        this.taxSubType = taxSubType;
    }

    public final String getNote() {
        return note;
    }
    public final void setNote(final String note) {
        this.note = note;
    }

    /**
     * @return the taxExempt
     */
    public final TaxExempt getTaxExempt() {
        return taxExempt;
    }
    /**
     * @param taxExempt the taxExempt to set
     */
    public final void setTaxExempt(final TaxExempt taxExempt) {
        this.taxExempt = taxExempt;
    }
    /**
     * Overrides the toString() Method.
     * @return The string representation of tax.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("Amount : ").append(this.amount).append(crlf)
           .append("Percent : ").append(this.percent).append(crlf)
           .append("TaxType : ").append(this.taxType);

        if (null != taxableAmt) {
            str.append("TaxableAmount : ")
            .append(this.taxableAmt.toString());
        }
        return str.toString();
    }
}
