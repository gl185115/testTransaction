/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * Sale
 *
 * Model Class for Sale
 *
 * De la Cerna, Jessel G.
 * Carlos G. Campos
 */

package ncr.res.mobilepos.journalization.model.poslog;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author      mlwang      <mlwangi @ isoftstone.com>
 *
 * RainCheck Model Object.
 *
 * <P>An RainCheck Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "RainCheck")
public class RainCheck {
    /**
     * The private member variable that will hold the taxable flag of RainCheck.
     */
    @XmlAttribute(name = "TaxableFlag")
    private String taxableFlag;

    /**
     * The private member variable that will hold the Department of RainCheck.
     */
    @XmlAttribute(name = "Department")
    private String department;

    /**
     * The private member variable that will hold the Line of RainCheck.
     */
    @XmlAttribute(name = "Line")
    private String line;

    /**
     * The private member variable that will hold the Class of RainCheck.
     */
    @XmlAttribute(name = "Class")
    private String clas;

    /**
     * Item Type for Advance transaction
     * 前受金の場合は、ItemType=Stock
     */
    @XmlAttribute(name="ItemType")
    private String itemType;

    /**
     * Action
     */
    @XmlAttribute(name = "Action")
    private String action;

    /**
     * ReasonCode
     */
    @XmlAttribute(name = "ReasonCode")
    private String reasonCode;

    /**
     * Note
     */
    @XmlElement(name = "Note")
    private String note;

    /**
     * kind of ID number.
     * 前受金データの場合使用
     */
    @XmlElement(name = "InventoryReservationID")
    private String inventoryReservationID;

    /**
     * The private member variable that will hold the item ID of RainCheck.
     */
    @XmlElement(name = "ItemID")
    private ItemID itemID;

    /**
     * The private member variable that will hold the description of RainCheck.
     */
    @XmlElement(name = "Description")
    private String description;

    /**
     * The private member variable that will hold the not computed RainCheck unit
     *  price from price override of RainCheck.
     */
    @XmlElement(name = "RegularSalesUnitPrice")
    private double regularsalesunitprice;

    /**
     * The private member variable that will hold the actual
     *  sales unit price of RainCheck.
     */
    @XmlElement(name = "ActualSalesUnitPrice")
    private double actualsalesunitprice;

    /**
     * The private member variable that will hold the extended amount of RainCheck.
     */
    @XmlElement(name = "ExtendedAmount")
    private double extendedAmt;

    /**
     * The private member variable that will hold the quantity of RainCheck.
     */
    @XmlElement(name = "Quantity")
    private int quantity;
    /**
     * The Discount Amount.
     */
    @XmlElement(name = "DiscountAmount")
    private double discountAmount;
    /**
     * The extended Discount Amount.
     */
    @XmlElement(name = "ExtendedDiscountAmount")
    private double extendedDiscountAmount;
    /**
     * The Retail Price Modifier.
     */
    @XmlElement(name = "RetailPriceModifier")
    private List<RetailPriceModifier> retailPriceModifier;
    /**
     * The private member variable that will hold the Tax.
     */
    @XmlElement(name = "Tax")
    private List<Tax> tax;

    /**
     *Points (獲得ポイント)
     */
    @XmlElement(name = "barPoints")
    private BarPoints barPoints;
    /**
     *購買動機コード
     */
    @XmlElement(name="barPurpose")
    private String barPurpose;

    /**
     * SalesmanID 販売員ID
     */
    @XmlElement(name="SalesmanID")
    private String salesmanID;

    /**
     * The private member variable that will hold the Item Selling Rule.
     */
    @XmlElement(name = "ItemSellingRule")
    private ItemSellingRule itemSellingRule;

    /**
     * operatorID
     */
    @XmlElement(name = "OperatorID")
    private OperatorID operatorID;

    /**
     * barOperatorName
     */
    @XmlElement(name = "barOperatorName")
    private BarOperatorName barOperatorName;
    
    /**
     * barInventoryLineNo
     */
    @XmlElement(name = "barInventoryLineNo")
    private int barInventoryLineNo;

    /**
     * barPSType 
     */
    @XmlElement(name = "barPSType")
    private String barPSType;
    /**
     * barReservationType
     */
    @XmlElement(name = "barReservationType")
    private BarReservationType barReservationType;
    /**
     * @return the barReservationType
     */
    public BarReservationType getBarReservationType() {
		return barReservationType;
	}
    /**
     * Sets the barReservationType of RainCheck.
     *
     * @param barReservationType   
     */
	public void setBarReservationType(BarReservationType barReservationType) {
		this.barReservationType = barReservationType;
	}

	/**
     * Gets the Regular sales unit price of RainCheck.
     *
     * @return        Regular sales unit price of RainCheck.
     */
    public final double getRegularSalesUnitPrice() {
        return regularsalesunitprice;
    }

    /**
     * Sets the Regular sales unit price of RainCheck.
     *
     * @param regularsalesunitpriceToSet   The new value for regular
     *                                      sales unit price of RainCheck.
     */
    public final void setRegularsalesunitprice(
            final long regularsalesunitpriceToSet) {
        this.regularsalesunitprice = regularsalesunitpriceToSet;
    }

    /**
     * Gets the Actual sales unit price of RainCheck.
     *
     * @return        Actual sales unit price of RainCheck.
     */
    public final double getActualSalesUnitPrice() {
        return actualsalesunitprice;
    }

    /**
     * Sets the Actual sales unit price of RainCheck.
     *
     * @param actualsalesunitpriceToSet   The new value for
     *                                     actual sales unit price of RainCheck.
     */
    public final void setActualSalesUnitPrice(
            final long actualsalesunitpriceToSet) {
        this.actualsalesunitprice = actualsalesunitpriceToSet;
    }

    /**
     * Default Constructor for RainCheck.
     *
     * Sets the member variable to default value.
     */
    public RainCheck() {
        description = null;
        extendedAmt = 0;
        itemID = null;
        taxableFlag = null;
        quantity = 0;
        actualsalesunitprice = 0;
        extendedAmt = 0;
        discountAmount = 0;
    }

    /**
     * Gets the description of RainCheck.
     *
     * @return        The description of RainCheck.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Sets the description of RainCheck.
     *
     * @param descriptionToSet      The new value for description of RainCheck.
     */
    public final void setDescription(final String descriptionToSet) {
        this.description = descriptionToSet;
    }

    /**
     * Gets the extended amount of RainCheck.
     *
     * @return        The extended amount of RainCheck.
     */
    public final double getExtendedAmt() {
        return extendedAmt;
    }

    /**
     * Sets the extended amount of RainCheck.
     *
     * @param extendedAmtToSet      The new value for extended amount of RainCheck.
     */
    public final void setExtendedAmt(final long extendedAmtToSet) {
        this.extendedAmt = extendedAmtToSet;
    }

    /**
     * Gets the item ID of RainCheck.
     *
     * @return        The item ID of RainCheck.
     */
    public final ItemID getItemID() {
        return itemID;
    }

    /**
     * Sets the item ID of RainCheck.
     *
     * @param itemIDToSet        The new value for item ID of RainCheck.
     */
    public final void setItemID(final ItemID itemIDToSet) {
        this.itemID = itemIDToSet;
    }

    /**
     * Gets the quantity of RainCheck.
     *
     * @return        The quantity of RainCheck.
     */
    public final int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of RainCheck.
     *
     * @param quantityToSet        The new value for quantity of RainCheck.
     */
    public final void setQuantity(final int quantityToSet) {
        this.quantity = quantityToSet;
    }

    /**
     * Gets the taxable flag  of RainCheck.
     *
     * @return        The taxable flag of RainCheck.
     */
    public final String getTaxableFlag() {
        return taxableFlag;
    }

    /**
     * Sets the taxableFlag of RainCheck.
     *
     * @param taxableFlagToSet        The new value for taxableFlag of RainCheck.
     */
    public final void setTaxableFlag(final String taxableFlagToSet) {
        this.taxableFlag = taxableFlagToSet;
    }

    /**
     * Gets the Department  of RainCheck.
     *
     * @return        The department of RainCheck.
     */
    public final String getDepartment() {
        return department;
    }

    /**
     * Sets the Department of RainCheck.
     *
     * @param departmentToSet        The new value for Department of RainCheck.
     */
    public final void setDepartment(final String departmentToSet) {
        this.department = departmentToSet;
    }

    /**
     * Gets the Line  of RainCheck.
     *
     * @return        The Line of RainCheck.
     */
    public final String getLine() {
        return line;
    }

    /**
     * Sets the Line of RainCheck.
     *
     * @param lineToSet        The new value for Line of RainCheck.
     */
    public final void setLine(final String lineToSet) {
        this.line = lineToSet;
    }

    /**
     * Gets the Class  of RainCheck.
     *
     * @return        The Class of RainCheck.
     */
    public final String getClas() {
        return clas;
    }

    /**
     * Sets the Class of RainCheck.
     *
     * @param clasToSet        The new value for Class of RainCheck.
     */
    public final void setClas(final String clasToSet) {
        this.clas = clasToSet;
    }

    /**
     * Sets the discount amount of the RainCheck item.
     * @param discountAmountToSet The Discount Amount.
     */
    public final void setDiscountAmount(final double discountAmountToSet) {
        this.discountAmount = discountAmountToSet;
    }

    /**
     * Gets the discount amount of the RainCheck item.
     * @return  The Discount Amount.
     */
    public final double getDiscountAmount() {
        return discountAmount;
    }

    /**
     * Sets the extended amount from RainCheck
     * Price and Discount Amount of RainCheck item.
     * @param extendedDiscountAmountToSet The Extended Discount Amount.
     */
    public final void setExtendedDiscountAmount(
            final double extendedDiscountAmountToSet) {
        this.extendedDiscountAmount = extendedDiscountAmountToSet;
    }

    /**
     * Gets the extended amount from RainCheck Price
     *  and Discount Amount of RainCheck item.
     * @return The Extended Discount Amount.
     */
    public final double getExtendedDiscountAmount() {
        return extendedDiscountAmount;
    }

    /**
     * @return the retailPriceModifier
     */
    public final List<RetailPriceModifier> getRetailPriceModifier() {
        return retailPriceModifier;
    }

    /**
     * @param retailPriceModifierToSet the retailPriceModifier to set
     */
    public final void setRetailPriceModifier(
            final List<RetailPriceModifier> retailPriceModifierToSet) {
        this.retailPriceModifier = retailPriceModifierToSet;
    }

    /**
     * Sets the Tax under RainCheck.
     *
     * @param taxToSet        The new value for the Tax under RainCheck.
     */
    public final void setTax(final List<Tax> taxToSet) {
        this.tax = taxToSet;
    }
    /**
     * Gets the Tax under RainCheck.
     *
     * @return        The Tax under RainCheck.
     */
    public final List<Tax> getTax() {
        return tax;
    }

    /**
     * Sets the setItemSellingRule under RainCheck.
     *
     * @param itemsellingruleToSet The new value for the Tax under
     * itemsellingrule.
     */
    public void setItemSellingRule(ItemSellingRule itemSellingRule) {
        this.itemSellingRule = itemSellingRule;
    }

    /**
     * Gets the Tax under RainCheck.
     *
     * @return        The Tax under itemSellingRule.
     */
    public ItemSellingRule getItemSellingRule() {
        return itemSellingRule;
    }
    /**
     * @return itemType
     */
    public String getItemType() {
        return itemType;
    }
    /**
     * @param itemType
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
    /**
     * @return action
     */
    public String getAction() {
        return action;
    }
    /**
     * @param action
     */
    public void setAction(String action) {
        this.action = action;
    }
    /**
     * @return reasonCode
     */
    public String getReasonCode() {
        return reasonCode;
    }
    /**
     * @param reasonCode
     */
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }
    /**
     * @return note
     */
    public String getNote() {
        return note;
    }
    /**
     * @param note
     */
    public void setNote(String note) {
        this.note = note;
    }
    /**
     * @return barPoints
     */
    public BarPoints getBarPoints() {
        return barPoints;
    }
    /**
     * @param barPoints
     */
    public void setBarPoints(BarPoints barPoints) {
        this.barPoints = barPoints;
    }
    /**
     * @return barPurpose
     */
    public String getBarPurpose() {
        return barPurpose;
    }
    /**
     * @param barPurpose
     */
    public void setBarPurpose(String barPurpose) {
        this.barPurpose = barPurpose;
    }

    /**
     * @return salesmanID
     */
    public String getSalesmanID() {
        return salesmanID;
    }
    /**
     * @param salesmanID
     */
    public void setSalesmanID(String salesmanID) {
        this.salesmanID = salesmanID;
    }
    /**
     * @return inventoryReservationID
     */
    public String getInventoryReservationID() {
        return inventoryReservationID;
    }
    /**
     * @param inventoryReservationID
     */
    public void setInventoryReservationID(String inventoryReservationID) {
        this.inventoryReservationID = inventoryReservationID;
    }

    /**
     * @return operatorID
     */
    public OperatorID getOperatorID() {
        return operatorID;
    }

    /**
     * @param operatorID セットする operatorID
     */
    public void setOperatorID(OperatorID operatorID) {
        this.operatorID = operatorID;
    }

    /**
     * @return the barOperatorName
     */
    public BarOperatorName getBarOperatorName() {
        return barOperatorName;
    }

    /**
     * @param barOperatorName the operatorID to set
     */
    public void setBarOperatorName(BarOperatorName barOperatorName) {
        this.barOperatorName = barOperatorName;
    }
    
    /**
     * @return barInventoryLineNo
     */
    public int getBarInventoryLineNo() {
        return barInventoryLineNo;
    }

    /**
     * @param barInventoryLineNo セットする barInventoryLineNo
     */
    public void setBarInventoryLineNo(int barInventoryLineNo) {
        this.barInventoryLineNo = barInventoryLineNo;
    }

    /**
     * @return barPSType
     */
    public String getBarPSType() {
        return barPSType;
    }

    /**
     * @param barPSType セットする barPSType
     */
    public void setBarPSType(String barPSType) {
        this.barPSType = barPSType;
    }

    /**
     * @param regularsalesunitprice セットする regularsalesunitprice
     */
    public void setRegularSalesUnitPrice(double regularsalesunitprice) {
        this.regularsalesunitprice = regularsalesunitprice;
    }

    /**
     * @param actualsalesunitprice セットする actualsalesunitprice
     */
    public void setActualsalesunitprice(double actualsalesunitprice) {
        this.actualsalesunitprice = actualsalesunitprice;
    }

    /**
     * @param extendedAmt セットする extendedAmt
     */
    public void setExtendedAmt(double extendedAmt) {
        this.extendedAmt = extendedAmt;
    }

    /**
     * Overrides the toString() Method.
     * @return the String Representation of RainCheck.
     */
    public final String toString() {
        StringBuffer str = new StringBuffer();
        String crlf = "\r\n";
        str.append("ItemID : ").append(this.itemID).append(crlf)
           .append("ActualSalesUnitPrice : ").append(this.actualsalesunitprice)
           .append(crlf)
           .append("Class : ").append(this.clas).append(crlf)
           .append("Department : ").append(this.department).append(crlf)
           .append("Description : ").append(this.description).append(crlf)
           .append("ExtendedAmount : ").append(this.extendedAmt).append(crlf)
           .append("ExtrendedDiscountAmount : ")
           .append(this.extendedDiscountAmount).append(crlf)
           .append("Line : ").append(this.line).append(crlf)
           .append("Quantity : ").append(this.quantity).append(crlf)
           .append("RegularSalesUnitPrice : ")
           .append(this.regularsalesunitprice).append(crlf)
           .append("TaxableFlag : ").append(this.taxableFlag)
           .append("ItemSellingRule : ").append(this.itemSellingRule);
        return str.toString();
    }
}
