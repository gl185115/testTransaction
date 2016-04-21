/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * Layaway
 *
 * Model Class for Layaway
 * create by Beijing ODC mlwang
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
 * Layaway Model Object.
 *
 * <P>A Layaway Node in POSLog XML.
 *
 * <P>The Layaway node is under LineItem Node.
 * And holds the details of the items sold in the transaction.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Layaway")
public class Layaway {
    /**
     * The private member variable that will hold the tax type of Sale.
     */
    @XmlAttribute(name = "TaxType")
    private int taxType;

    /**
     * The private member variable that will hold the taxable flag of Layaway.
     */
    @XmlAttribute(name = "TaxableFlag")
    private String taxableFlag;

    /**
     * The private member variable that will hold the Department of Layaway.
     */
    @XmlAttribute(name = "Department")
    private String department;

    /**
     * The private member variable that will hold the Line of Layaway.
     */
    @XmlAttribute(name = "Line")
    private String line;

    /**
     * The private member variable that will hold the Class of Layaway.
     */
    @XmlAttribute(name = "Class")
    private String clas;

    /**
     * The private member variable that will hold the urgentEntryFlag of Layaway.
     */
    @XmlElement(name = "UrgentEntryFlag")
    private String urgentEntryFlag;

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
     * The private member variable that will hold the item ID of Layaway.
     */
    @XmlElement(name = "ItemID")
    private ItemID itemID;

    /**
     * The private member variable that will hold the ScanBarCode of Layaway.
     */
    @XmlElement(name = "ScanBarCode")
    private String scanBarCode;

    /**
     * The private member variable that will hold the description of Layaway.
     */
    @XmlElement(name = "Description")
    private String description;

    /**
     * The private member variable that will hold the mdKanaName of Layaway.
     */
    @XmlElement(name = "MdKanaName")
    private String mdKanaName;

    /**
     * The private member variable that will hold the sku of Layaway.
     */
    @XmlElement(name = "Sku")
    private String sku;

    /**
     * The private member variable that will hold the sku type of Layaway.
     */
    @XmlElement(name = "SkuType")
    private String skuType;
    
    /**
     * The private member variable that will hold the mdVenderof Layaway.
     */
    @XmlElement(name = "MdVender")
    private String mdVender;
    
    /**
     * The private member variable that will hold the SupplierCode of Sale.
     */
    @XmlElement(name = "SupplierCode")
    private String supplierCode;
    
    /**
     * The private member variable that will hold the SubSupplierCode Sale.
     */
    @XmlElement(name = "SubSupplierCode")
    private String subSupplierCode;
    
    /**
     * The private member variable that will hold the colorId of Layaway.
     */
    @XmlElement(name = "ColorId")
    private String colorId;
    
    /**
     * The private member variable that will hold the colorName of Layaway.
     */
    @XmlElement(name = "ColorName")
    private String colorName;
    
    /**
     * The private member variable that will hold the sizeId of Layaway.
     */
    @XmlElement(name = "SizeId")
    private String sizeId;
    
    /**
     * The private member variable that will hold the sizeName of Layaway.
     */
    @XmlElement(name = "SizeName")
    private String sizeName;
    
    /**
     * The private member variable that will hold the groupCode of Layaway.
     */
    @XmlElement(name = "GroupCode")
    private String groupCode;
    
    /**
     * The private member variable that will hold the sales priceFrom of Layaway.
     */
    @XmlElement(name = "SalesPriceFrom")
    private SalesPriceFrom salesPriceFrom;

    /**
     * The private member variable that will hold the not computed Layaway unit
     *  price from price override of Layaway.
     */
    @XmlElement(name = "RegularSalesUnitPrice")
    private double regularsalesunitprice;

    /**
     * The private member variable that will hold the actual
     *  sales unit price of Layaway.
     */
    @XmlElement(name = "ActualSalesUnitPrice")
    private double actualsalesunitprice;

    /**
     * The private member variable that will hold the extended amount of Layaway.
     */
    @XmlElement(name = "ExtendedAmount")
    private double extendedAmt;

    /**
     * The private member variable that will hold the quantity of Layaway.
     */
    @XmlElement(name = "Quantity")
    private int quantity;
    /**
     * The private member variable that will hold the Forward.
     */
    @XmlElement(name = "Forward")
    private Forward forward;
    
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
     * The private member variable that will hold the CostPrice of Sale.
     */
    @XmlElement(name = "CostPrice")
    private int costPrice;
    
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
    @XmlElement(name = "Points")
    private Points points;

    /**
     * Associate 販売員
     */
    @XmlElement(name="Associate")
    private Associate associate;
    
    /**
     * SizeChange
     */
    @XmlElement(name="SizeChange")
    private String sizeChange;
    
    /**
     * SizeChanged
     */
    @XmlElement(name="SizeChanged")
    private String sizeChanged;

    /**
     * PointExp
     */
    @XmlElement(name="PointExp")
    private String pointExp;

    /**
     * DiscountExp
     */
    @XmlElement(name="DiscountExp")
    private String discountExp;

    /**
     * GuaranteeFlag
     */
    @XmlElement(name="GuaranteeFlag")
    private String guaranteeFlag;

    /**
     * Coupon
     */
    @XmlElement(name="Coupon")
    private Coupon coupon;
    
    /**
     * PremiumInfo
     */
    @XmlElement(name="PremiumInfo")
    private PremiumInfo premiumInfo;

    /**
     * QRPromotionInfo
     */
    @XmlElement(name="QRPromotionInfo")
    private QRPromotionInfo qrPromotionInfo;

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
     * Gets the Regular sales unit price of Layaway.
     *
     * @return        Regular sales unit price of Layaway.
     */
    public final double getRegularsalesunitprice() {
        return regularsalesunitprice;
    }

    /**
     * Sets the Regular sales unit price of Layaway.
     *
     * @param regularsalesunitpriceToSet   The new value for regular
     *                                      sales unit price of Layaway.
     */
    public final void setRegularsalesunitprice(
            final long regularsalesunitpriceToSet) {
        this.regularsalesunitprice = regularsalesunitpriceToSet;
    }

    /**
     * Gets the Actual sales unit price of Layaway.
     *
     * @return        Actual sales unit price of Layaway.
     */
    public final double getActualsalesunitprice() {
        return actualsalesunitprice;
    }

    /**
     * Sets the Actual sales unit price of Layaway.
     *
     * @param actualsalesunitpriceToSet   The new value for
     *                                     actual sales unit price of Layaway.
     */
    public final void setActualsalesunitprice(
            final long actualsalesunitpriceToSet) {
        this.actualsalesunitprice = actualsalesunitpriceToSet;
    }

    /**
     * Default Constructor for Layaway.
     *
     * Sets the member variable to default value.
     */
    public Layaway() {
        description = null;
        extendedAmt = 0;
        itemID = null;
        taxType = 0;
        taxableFlag = null;
        quantity = 0;
        actualsalesunitprice = 0;
        extendedAmt = 0;
        discountAmount = 0;
    }

    /**
     * Gets the description of Layaway.
     *
     * @return        The description of Layaway.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Sets the description of Layaway.
     *
     * @param descriptionToSet      The new value for description of Layaway.
     */
    public final void setDescription(final String descriptionToSet) {
        this.description = descriptionToSet;
    }

    /**
     * Gets the extended amount of Layaway.
     *
     * @return        The extended amount of Layaway.
     */
    public final double getExtendedAmt() {
        return extendedAmt;
    }

    /**
     * Sets the extended amount of Layaway.
     *
     * @param extendedAmtToSet      The new value for extended amount of Layaway.
     */
    public final void setExtendedAmt(final long extendedAmtToSet) {
        this.extendedAmt = extendedAmtToSet;
    }

    /**
     * Gets the item ID of Layaway.
     *
     * @return        The item ID of Layaway.
     */
    public final ItemID getItemID() {
        return itemID;
    }

    /**
     * Sets the item ID of Layaway.
     *
     * @param itemIDToSet        The new value for item ID of Layaway.
     */
    public final void setItemID(final ItemID itemIDToSet) {
        this.itemID = itemIDToSet;
    }

    /**
     * Gets the quantity of Layaway.
     *
     * @return        The quantity of Layaway.
     */
    public final int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of Layaway.
     *
     * @param quantityToSet        The new value for quantity of Layaway.
     */
    public final void setQuantity(final int quantityToSet) {
        this.quantity = quantityToSet;
    }

    /**
     * Gets the taxable flag  of Layaway.
     *
     * @return        The taxable flag of Layaway.
     */
    public final String getTaxableFlag() {
        return taxableFlag;
    }

    /**
     * Sets the taxableFlag of Layaway.
     *
     * @param taxableFlagToSet        The new value for taxableFlag of Layaway.
     */
    public final void setTaxableFlag(final String taxableFlagToSet) {
        this.taxableFlag = taxableFlagToSet;
    }

    /**
     * Gets the Department  of Layaway.
     *
     * @return        The department of Layaway.
     */
    public final String getDepartment() {
        return department;
    }

    /**
     * Sets the Department of Layaway.
     *
     * @param departmentToSet        The new value for Department of Layaway.
     */
    public final void setDepartment(final String departmentToSet) {
        this.department = departmentToSet;
    }

    /**
     * Gets the Line  of Layaway.
     *
     * @return        The Line of Layaway.
     */
    public final String getLine() {
        return line;
    }

    /**
     * Sets the Line of Layaway.
     *
     * @param lineToSet        The new value for Line of Layaway.
     */
    public final void setLine(final String lineToSet) {
        this.line = lineToSet;
    }

    /**
     * Gets the Class  of Layaway.
     *
     * @return        The Class of Layaway.
     */
    public final String getClas() {
        return clas;
    }

    /**
     * Sets the Class of Layaway.
     *
     * @param clasToSet        The new value for Class of Layaway.
     */
    public final void setClas(final String clasToSet) {
        this.clas = clasToSet;
    }

    /**
     * Sets the discount amount of the Layaway item.
     * @param discountAmountToSet The Discount Amount.
     */
    public final void setDiscountAmount(final double discountAmountToSet) {
        this.discountAmount = discountAmountToSet;
    }

    /**
     * Gets the discount amount of the Layaway item.
     * @return  The Discount Amount.
     */
    public final double getDiscountAmount() {
        return discountAmount;
    }

    /**
     * Sets the extended amount from Layaway
     * Price and Discount Amount of Layaway item.
     * @param extendedDiscountAmountToSet The Extended Discount Amount.
     */
    public final void setExtendedDiscountAmount(
            final double extendedDiscountAmountToSet) {
        this.extendedDiscountAmount = extendedDiscountAmountToSet;
    }

    /**
     * Gets the extended amount from Layaway Price
     *  and Discount Amount of Layaway item.
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
     * Sets the Tax under Layaway.
     *
     * @param taxToSet        The new value for the Tax under Layaway.
     */
    public final void setTax(final List<Tax> taxToSet) {
        this.tax = taxToSet;
    }
    /**
     * Gets the Tax under Layaway.
     *
     * @return        The Tax under Layaway.
     */
    public final List<Tax> getTax() {
        return tax;
    }

    /**
     * Sets the setItemSellingRule under Layaway.
     *
     * @param itemsellingruleToSet The new value for the Tax under
     * itemsellingrule.
     */
    public void setItemSellingRule(ItemSellingRule itemSellingRule) {
        this.itemSellingRule = itemSellingRule;
    }

    /**
     * Gets the Tax under Layaway.
     *
     * @return        The Tax under itemSellingRule.
     */
    public ItemSellingRule getItemSellingRule() {
        return itemSellingRule;
    }

    /**
     * @return the itemType
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * @param itemType the itemType to set
     */
    public void setItemType(String itemType) {
        this.itemType = itemType;
    }

    /**
     * @return the action
     */
    public String getAction() {
        return action;
    }

    /**
     * @param action the action to set
     */
    public void setAction(String action) {
        this.action = action;
    }

    /**
     * @return the reasonCode
     */
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * @param reasonCode the reasonCode to set
     */
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    /**
     * @return the note
     */
    public String getNote() {
        return note;
    }

    /**
     * @param note the note to set
     */
    public void setNote(String note) {
        this.note = note;
    }

    /**
     * @return the inventoryReservationID
     */
    public String getInventoryReservationID() {
        return inventoryReservationID;
    }

    /**
     * @param inventoryReservationID the inventoryReservationID to set
     */
    public void setInventoryReservationID(String inventoryReservationID) {
        this.inventoryReservationID = inventoryReservationID;
    }

    /**
     * @return the barPoints
     */
    public BarPoints getBarPoints() {
        return barPoints;
    }

    /**
     * @param barPoints the barPoints to set
     */
    public void setBarPoints(BarPoints barPoints) {
        this.barPoints = barPoints;
    }

    /**
     * @return the barPurpose
     */
    public String getBarPurpose() {
        return barPurpose;
    }

    /**
     * @param barPurpose the barPurpose to set
     */
    public void setBarPurpose(String barPurpose) {
        this.barPurpose = barPurpose;
    }

    /**
     * @return the salesmanID
     */
    public String getSalesmanID() {
        return salesmanID;
    }

    /**
     * @param salesmanID the salesmanID to set
     */
    public void setSalesmanID(String salesmanID) {
        this.salesmanID = salesmanID;
    }

    /**
     * @return the operatorID
     */
    public OperatorID getOperatorID() {
        return operatorID;
    }

    /**
     * @param operatorID the operatorID to set
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
     * @return the barInventoryLineNo
     */
    public int getBarInventoryLineNo() {
        return barInventoryLineNo;
    }

    /**
     * @param barInventoryLineNo the barInventoryLineNo to set
     */
    public void setBarInventoryLineNo(int barInventoryLineNo) {
        this.barInventoryLineNo = barInventoryLineNo;
    }

    /**
     * @param regularsalesunitprice the regularsalesunitprice to set
     */
    public void setRegularsalesunitprice(double regularsalesunitprice) {
        this.regularsalesunitprice = regularsalesunitprice;
    }

    /**
     * @param actualsalesunitprice the actualsalesunitprice to set
     */
    public void setActualsalesunitprice(double actualsalesunitprice) {
        this.actualsalesunitprice = actualsalesunitprice;
    }

    /**
     * @param extendedAmt the extendedAmt to set
     */
    public void setExtendedAmt(double extendedAmt) {
        this.extendedAmt = extendedAmt;
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
     * Overrides the toString() Method.
     * @return the String Representation of Layaway.
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

    /**
     * @return the taxType
     */
    public final int getTaxType() {
        return taxType;
    }
    /**
     * @param taxType the taxType to set
     */
    public final void setTaxType(final int taxType) {
        this.taxType = taxType;
    }

    /**
     * Gets the urgentEntryFlag of Sale.
     *
     * @return  The urgentEntryFlag of Sale.
     */
    public final String getUrgentEntryFlag() {
        return urgentEntryFlag;
    }

    /**
     * Sets the urgentEntryFlag of Sale.
     *
     * @param urgentEntryFlagToSet      The new value for urgentEntryFlag of Sale.
     */
    public final void setUrgentEntryFlag(final String urgentEntryFlagToSet) {
        this.urgentEntryFlag = urgentEntryFlagToSet;
    }
    /**
     * @return the mdKanaName
     */
    public final String getMdKanaName() {
        return mdKanaName;
    }
    /**
     * @param mdKanaName the mdKanaName to set
     */
    public final void setMdKanaName(final String mdKanaName) {
        this.mdKanaName = mdKanaName;
    }
    /**
     * @return the sku
     */
    public final String getSku() {
        return sku;
    }
    /**
     * @param sku the sku to set
     */
    public final void setSku(final String sku) {
        this.sku = sku;
    }
    /**
     * @return the skuType
     */
    public final String getSkuType() {
        return skuType;
    }
    /**
     * @param skuType the skuType to set
     */
    public final void setSkuType(final String skuType) {
        this.skuType = skuType;
    }
    /**
     * @return the mdVender
     */
    public final String getMdVender() {
        return mdVender;
    }
    /**
     * @param mdVender the mdVender to set
     */
    public final void setMdVender(final String mdVender) {
        this.mdVender = mdVender;
    }
    /**
     * @return the colorId
     */
    public final String getColorId() {
        return colorId;
    }
    /**
     * @param colorId the colorId to set
     */
    public final void setColorId(final String colorId) {
        this.colorId = colorId;
    }
    /**
     * @return the sizeId
     */
    public final String getSizeId() {
        return sizeId;
    }
    /**
     * @param sizeId the sizeId to set
     */
    public final void setSizeId(final String sizeId) {
        this.sizeId = sizeId;
    }
    /**
     * @return the groupCode
     */
    public final String getGroupCode() {
        return groupCode;
    }
    /**
     * @param groupCode the groupCode to set
     */
    public final void setGroupCode(final String groupCode) {
        this.groupCode = groupCode;
    }
    /**
     * @return the salesPriceFrom
     */
    public final SalesPriceFrom getSalesPriceFrom() {
        return salesPriceFrom;
    }
    /**
     * @param salesPriceFrom the salesPriceFrom to set
     */
    public final void setSalesPriceFrom(final SalesPriceFrom salesPriceFrom) {
        this.salesPriceFrom = salesPriceFrom;
    }
    /**
     * @return the forward
     */
    public final Forward getForward() {
        return forward;
    }
    /**
     * @param forward the forward to set
     */
    public final void setForward(final Forward forward) {
        this.forward = forward;
    }
    /**
     * @return the points
     */
    public Points getPoints() {
        return points;
    }
    /**
     * @param points the points to set
     */
    public void setPoints(final Points points) {
        this.points = points;
    }
    /**
     * @return the associate
     */
    public final Associate getAssociate() {
        return associate;
    }
    /**
     * @param associate the associate to set
     */
    public final void setAssociate(Associate associate) {
        this.associate = associate;
    }
    /**
     * @return the sizeChange
     */
    public final String getSizeChange() {
        return sizeChange;
    }
    /**
     * @param sizeChange the sizeChange to set
     */
    public final void setSizeChange(final String sizeChange) {
        this.sizeChange = sizeChange;
    }
    /**
     * @return the pointExp
     */
    public final String getPointExp() {
        return pointExp;
    }
    /**
     * @param pointExp the pointExp to set
     */
    public final void setPointExp(final String pointExp) {
        this.pointExp = pointExp;
    }
    /**
     * @return the discountExp
     */
    public String getDiscountExp() {
        return discountExp;
    }
    /**
     * @param discountExp the discountExp to set
     */
    public void setDiscountExp(final String discountExp) {
        this.discountExp = discountExp;
    }
    /**
     * @return the guaranteeFlag
     */
    public final String getGuaranteeFlag() {
        return guaranteeFlag;
    }
    /**
     * @param guaranteeFlag the guaranteeFlag to set
     */
    public final void setGuaranteeFlag(final String guaranteeFlag) {
        this.guaranteeFlag = guaranteeFlag;
    }
    /**
     * @return the coupon
     */
    public final Coupon getCoupon() {
        return coupon;
    }
    /**
     * @param coupon the coupon to set
     */
    public final void setCoupon(final Coupon coupon) {
        this.coupon = coupon;
    }
    /**
     * @return the premiumInfo
     */
    public final PremiumInfo getPremiumInfo() {
        return premiumInfo;
    }
    /**
     * @param premiumInfo the premiumInfo to set
     */
    public final void setPremiumInfo(final PremiumInfo premiumInfo) {
        this.premiumInfo = premiumInfo;
    }
    /**
     * @return the qrPromotionInfo
     */
    public final QRPromotionInfo getQRPromotionInfo() {
        return qrPromotionInfo;
    }
    /**
     * @param qrPromotionInfo the qrPromotionInfo to set
     */
    public final void setQRPromotionInfo(final QRPromotionInfo qrPromotionInfo) {
        this.qrPromotionInfo = qrPromotionInfo;
    }
    /**
     * @param supplierCode the supplierCode to set
     */
    public final void setSupplierCode(final String supplierCode) {
        this.supplierCode = supplierCode;
    }
    /**
     * @return the supplierCode
     */
    public final String getSupplierCode() {
        return supplierCode;
    }
    /**
     * @param subSupplierCode the subSupplierCode to set
     */
    public final void setSubSupplierCode(final String subSupplierCode) {
        this.subSupplierCode = subSupplierCode;
    }
    /**
     * @return the subSupplierCode
     */
    public final String getSubSupplierCode() {
        return subSupplierCode;
    }
    /**
     * @param costPrice the costPrice to set
     */
    public final void setCostPrice(final int costPrice) {
        this.costPrice = costPrice;
    }
    /**
     * @return the costPrice
     */
    public final int getCostPrice() {
        return costPrice;
    }
    /**
     * @param sizeChanged the sizeChanged to set
     */
    public final void setSizeChanged(final String sizeChanged) {
        this.sizeChanged = sizeChanged;
    }
    /**
     * @return the sizeChanged
     */
    public final String getSizeChanged() {
        return sizeChanged;
    }
    /**
     * @param scanBarCode the scanBarCode to set
     */
    public final void setScanBarCode(final String scanBarCode) {
        this.scanBarCode = scanBarCode;
    }
    /**
     * @return the scanBarCode
     */
    public final String getScanBarCode() {
        return scanBarCode;
    }
    /**
     * @param colorName the colorName to set
     */
    public final void setColorName(final String colorName) {
        this.colorName = colorName;
    }
    /**
     * @return the colorName
     */
    public final String getColorName() {
        return colorName;
    }
    /**
     * @param sizeName the sizeName to set
     */
    public final void setSizeName(final String sizeName) {
        this.sizeName = sizeName;
    }
    /**
     * @return the sizeName
     */
    public final String getSizeName() {
        return sizeName;
    }
}
