/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * Discount
 *
 * Model Class for Discount
 *
 * Carlos G. Campos
 */
package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Discount Model Object.
 *
 * <P>A Discount Node in POSLog XML.
 *
 * <P>The Discount node is under LineItem Node.
 * And mainly holds the amount information of the Customer's discount.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Discount")
public class Discount {
    
    /**
     * The private member variable that will hold the InputType.
     */
    @XmlElement(name = "InputType")
    private String inputType;
    
    /**
     * The private member variable that will hold the Barcode.
     */
    @XmlElement(name = "Barcode")
    private String barcode;
    
    /**
     * The private member variable that will hold the QRcode.
     */
    @XmlElement(name = "QRcode")
    private String qRcode;
    
    /**
     * The private member variable that will hold the Percentage.
     */
	@XmlElement(name = "Percentage")
    private int percentage;
	
    /**
     * The public member variable that holds the amount of the discount
     * of the customer.
     */
    @XmlElement(name = "Amount")
    private String amount;
    
    /**
     * The public member variable that holds the count of the discount
     * of the customer.
     */
    @XmlElement(name = "Count")
    private int count;

    /**
     * The public member variable that holds the FaceValueAmount
     * of the customer.
     */
    @XmlElement(name = "FaceValueAmount")
    private int faceValueAmount;
    
    /**
     * The public member variable that holds the price derivation rule
     * of the customer.
     */
    @XmlElement(name = "PriceDerivationRule")
    private PriceDerivationRule priceDerivationRule;
    
    /**
     * The private member variable that will hold the Tax.
     */
    @XmlElement(name = "Tax")
    private Tax tax;

    /**
     * The private member variable that will hold text notes for this line item
     */
    @XmlElement(name = "Note")
    private String note;
    
    /**
	 * @return the percentage
	 */
	public final int getPercentage() {
		return percentage;
	}

	/**
	 * @param percentage the percentage to set
	 */
	public final void setPercentage(final int percentage) {
		this.percentage = percentage;
	}

	/**
     * Gets the amount in discount.
     *
     * @return        Returns the amount in discount.
     */
    public final String getAmount() {
        return amount;
    }

    /**
     * Sets the amount in discount.
     *
     * @param amountToSet        The new value for amount in discount.
     */
    public final void setAmount(final String amountToSet) {
        this.amount = amountToSet;
    }
    
    /**
     * Sets the price derivation rule.
     *
     * @param pricederivruleToSet       The new value for price derivation rule.
     */
    public final void setPriceDerivationRule(final PriceDerivationRule pricederivruleToSet) {
        this.priceDerivationRule = pricederivruleToSet;
    }

    /**
     * Gets the price derivation rule.
     *
     * @return        Returns the price derivation rule.
     */
    public final PriceDerivationRule getPriceDerivationRule() {
        return priceDerivationRule;
    }
    
    /**
     * Gets the tax in discount.
     *
     * @return        Returns the tax in discount.
     */
    public final Tax getTax() {
        return tax;
    }

    /**
     * Sets the tax in discount.
     *
     * @param taxToSet        The new value for tax in discount.
     */
    public final void setTax(final Tax taxToSet) {
        this.tax = taxToSet;
    }

    public final String getNote() {
		return note;
	}

	public final void setNote(final String note) {
		this.note = note;
	}

	/**
     * Overrides the toString() Method.
     * @return The String representation of Discount.
     */
    public final String toString() {
        return "Amount: " + this.amount;
    }

    /**
     * @return the inputType
     */
    public final String getInputType() {
        return inputType;
    }

    /**
     * @param inputType the inputType to set
     */
    public final void setInputType(final String inputType) {
        this.inputType = inputType;
    }

    /**
     * @return the barcode
     */
    public final String getBarcode() {
        return barcode;
    }

    /**
     * @param barcode the barcode to set
     */
    public final void setBarcode(final String barcode) {
        this.barcode = barcode;
    }

    /**
     * @return the qRcode
     */
    public final String getqRcode() {
        return qRcode;
    }

    /**
     * @param qRcode the qRcode to set
     */
    public final void setqRcode(final String qRcode) {
        this.qRcode = qRcode;
    }

    /**
     * @return the count
     */
    public final int getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public final void setCount(final int count) {
        this.count = count;
    }

    /**
     * @return the faceValueAmount
     */
    public final int getFaceValueAmount() {
        return faceValueAmount;
    }

    /**
     * @param faceValueAmount the faceValueAmount to set
     */
    public final void setFaceValueAmount(final int faceValueAmount) {
        this.faceValueAmount = faceValueAmount;
    }
}
