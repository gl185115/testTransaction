/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * TenderChange
 *
 * Model Class for TenderChange
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
 * TenderChange Model Object.
 *
 * <P>A TenderChange Node in POSLog XML.
 *
 * The TenderChange node is under LineItem Node.
 * And holds the tender change information from the transaction.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TenderChange")
public class TenderChange {
	
    /**
     * ID of TenderType.
     */
    @XmlAttribute(name = "TenderType")
    private String tenderType;

    /**
     * The private member variable that holds the amount of TenderChange.
     */
    @XmlElement(name = "Amount")
    private double amount;
    
    /**
     * ID of UnspentAmount.
     */
    @XmlElement(name = "UnspentAmount")
    private UnspentAmount unspentAmount;

    /**
     * The default constructor for TenderChange class.
     */
    public TenderChange() {
        amount = 0;
        this.unspentAmount = new UnspentAmount();
        tenderType = null;
    }

    /**
     * Gets the tenderType of cash change.
     *
     * @return        The tenderType of TenderChange.
     */
    public final String getTenderType() {
        return tenderType;
    }
    /**
     * Sets the tenderType of cash change.
     *
     * @param tenderTypeToSet        The new value for the tenderType of TenderChange.
     */
    public final void setTenderType(String tenderType) {
        this.tenderType = tenderType;
    }

    /**
     * Gets the amount of cash change.
     *
     * @return        The amount of TenderChange.
     */
    public final double getAmount() {
        return amount;
    }

    /**
     * Sets the amount of cash change.
     *
     * @param amountToSet        The new value for the amount of TenderChange.
     */
    public final void setAmount(final double amountToSet) {
        this.amount = amountToSet;
    }

    /**
     * @return unspentAmount
     */
    public final UnspentAmount getUnspentAmount() {
        return unspentAmount;
    }

    /**
     * @param unspentAmount ƒZƒbƒg‚·‚é unspentAmount
     */
    public final void setUnspentAmount(UnspentAmount unspentAmount) {
        this.unspentAmount = unspentAmount;
    }

    /**
     * Overrides the toString() Method.
     * @return String that represents the TenderChange model.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("Amount : ").append(this.amount).append(crlf)
        	.append("UnspentAmount : ").append(this.unspentAmount).append(crlf)
        	.append("TenderType :").append(this.tenderType).append(crlf);
        	
        return str.toString();
    }
}
