/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * DiscountReason
 *
 * Model class for DiscountReason object.
 *
 * del Rio, Rica Marie M.
 */

package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * DiscountReason Model Object.
 * Encapsulates the DiscountReason information.
 *
 */
@XmlRootElement(name = "ReasonData")
@XmlAccessorType(XmlAccessType.NONE)
public class ReasonData {	
    /**
     * Type of Reason Data.
     */
    @XmlElement(name = "Type")
    private String type;
    /**
     * Discount's Button Number.
     */
    @XmlElement(name = "ButtonNum")
    private String buttonNum;
    /**
     * The reason code set to POSLog.xml.
     */
    @XmlElement(name = "Code")
    private String code;
    /**
     * The Percentage set for  Discount button.
     */
    @XmlElement(name = "Percentage")
    private String percentage;
    /**
     * The Amount set for Discount button.
     */
    @XmlElement(name = "Amount")
    private String amount;
    /**
     * The reason discount name displayed on a spinner.
     */
    @XmlElement(name = "DisplayName")
    private String displayname;
    /*
     * Default constructor.
     */
    public ReasonData() {    	
    }
    /*
     * Constructor with all properties.
     */
    public ReasonData(String buttonNum, String type, String code, String percentage, String amt, String displayName) {
    	setButtonNum(buttonNum);
    	setType(type);
    	setCode(code);
    	setPercentage(percentage);
    	setAmount(amt);
    	setDisplayname(displayName);
    }
    /**
     * Getter for button number.
     * @return ButtonNum
     */
    public final String getButtonNum() {
        return buttonNum;
    }
    /**
     * Setter for button number.
     * @param buttonNumToSet  Button number to set.
     */
    public final void setButtonNum(final String buttonNumToSet) {
        this.buttonNum = buttonNumToSet;
    }
    /**
     * Getter for Percentage.
     * @return Percentage
     */
    public final String getPercentage() {
        return percentage;
    }
    /**
     * Setter for percentage.
     * @param percentageToSet  Percentage to set.
     */
    public final void setPercentage(final String percentageToSet) {
        this.percentage = percentageToSet;
    }
    /**
     * Getter for amount.
     * @return   Amount
     */
    public final String getAmount() {
        return amount;
    }
    /**
     * Setter for amount.
     * @param amountToSet   Amount to set.
     */
    public final void setAmount(final String amountToSet) {
        this.amount = amountToSet;
    }
    /**
     * Getter for type of Reason Discount.
     * @return Type of Reason Discount.
     */
    public final String getType() {
        return type;
    }
    /**
     * Setter for type of Reason Discount.
     * @param typeToSet   Reason Discount type to set.
     */
    public final void setType(final String typeToSet) {
        this.type = typeToSet;
    }
    /**
     * Getter for reason code.
     * @return   Reason Code.
     */
    public final String getCode() {
        return code;
    }
    /**
     * Setter for reason code.
     * @param codeToSet   Reason code to set.
     */
    public final void setCode(final String codeToSet) {
        this.code = codeToSet;
    }
    /**
     * Getter for reason discount display name.
     * @return   Display Name.
     */
    public final String getDisplayname() {
         return displayname;
    }
    /**
     * Setter for Reason Discount display name.
     * @param displaynameToSet   Display Name to set.
     */
    public final void setDisplayname(final String displaynameToSet) {
        this.displayname = displaynameToSet;
    }
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("Type: " + type).append("; ");
        sb.append("ButtonNum: " + buttonNum).append("; ");
        sb.append("Code: " + code).append("; ");
        sb.append("Percentage: " + percentage).append("; ");
        sb.append("Amount: " + amount).append("; ");
        sb.append("DisplayName: " + displayname).append("; ");

        return sb.toString();
    }
}
