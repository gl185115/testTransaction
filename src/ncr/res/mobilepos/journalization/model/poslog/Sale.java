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
 * Sale Model Object.
 *
 * <P>A Sale Node in POSLog XML.
 *
 * <P>The Sale node is under LineItem Node.
 * And holds the details of the items sold in the transaction.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Sale")
public class Sale {
    /**
     * The private member variable that will hold the tax type of Sale.
     */
    @XmlAttribute(name = "TaxType")
    private int taxType;

    /**
     * The private member variable that will hold the ReturnFlag of Sale.
     */
    @XmlAttribute(name = "ReturnFlag")
    private String returnFlag;

    /**
     * The private member variable that will hold the Department of Sale.
     */
    @XmlAttribute(name = "Department")
    private String department;

    /**
     * The private member variable that will hold the Line of Sale.
     */
    @XmlAttribute(name = "Line")
    private String line;

    /**
     * The private member variable that will hold the Class of Sale.
     */
    @XmlAttribute(name = "Class")
    private String clas;

    /**
     * The private member variable that will hold the newAddedFlag of Sale.
     */
    @XmlElement(name = "NewAddedFlag")
    private String newAddedFlag;

    /**
     * The private member variable that will hold the urgentEntryFlag of Sale.
     */
    @XmlElement(name = "UrgentEntryFlag")
    private String urgentEntryFlag;

    /**
     * The private member variable that will hold the returnExchangeFlag of Sale.
     */
    @XmlElement(name = "ReturnExchangeFlag")
    private String returnExchangeFlag;

    /**
     * The private member variable that will hold the item ID of Sale.
     */
    @XmlElement(name = "ItemID")
    private ItemID itemID;

    /**
     * The private member variable that will hold the ScanBarCode of Sale.
     */
    @XmlElement(name = "ScanBarCode")
    private String scanBarCode;

    /**
     * The private member variable that will hold the description of Sale.
     */
    @XmlElement(name = "Description")
    private String description;

    /**
     * The private member variable that will hold the mdKanaName of Sale.
     */
    @XmlElement(name = "MdKanaName")
    private String mdKanaName;

    /**
     * The private member variable that will hold the sku of Sale.
     */
    @XmlElement(name = "Sku")
    private String sku;

    /**
     * The private member variable that will hold the sku type of Sale.
     */
    @XmlElement(name = "SkuType")
    private String skuType;
    
    /**
     * The private member variable that will hold the mdVender of Sale.
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
     * The private member variable that will hold the colorId of Sale.
     */
    @XmlElement(name = "ColorId")
    private String colorId;
    
    /**
     * The private member variable that will hold the colorName of Sale.
     */
    @XmlElement(name = "ColorName")
    private String colorName;
    
    /**
     * The private member variable that will hold the sizeId of Sale.
     */
    @XmlElement(name = "SizeId")
    private String sizeId;
    
    /**
    *The private member variable that will hold the OldSizeId of Sale.
    */
    @XmlElement(name = "OldSizeId")
    private String oldSizeId;
    /**
    *The private member variable that will hold the SizePatternId1 of Sale.
    */
    @XmlElement(name = "SizePatternId1")
    private String sizePatternId1;
    /**
    *The private member variable that will hold the SizePatternId2 of Sale.
    */
    @XmlElement(name = "SizePatternId2")
    private String sizePatternId2;
    /**
    *The private member variable that will hold the BrandId of Sale.
    */
    @XmlElement(name = "BrandId")
    private String brandId;
    /**
    *The private member variable that will hold the BrandName of Sale.
    */
    @XmlElement(name = "BrandName")
    private String brandName;

    /**
    *The private member variable that will hold the DptNameLocal of Sale.
    */
    @XmlElement(name = "DptNameLocal")
    private String dptNameLocal;
    /**
    *The private member variable that will hold the OldSku of Sale.
    */
    @XmlElement(name = "OldSku")
    private String oldSku;
    /**
    *The private member variable that will hold the RepSku of Sale.
    */
    @XmlElement(name = "RepSku")
    private String repSku;
    /**
     * The private member variable that will hold the sizeName of Sale.
     */
    @XmlElement(name = "SizeName")
    private String sizeName;
    
    /**
     * The private member variable that will hold the groupCode of Sale.
     */
    @XmlElement(name = "GroupCode")
    private String groupCode;
    
    /**
     * The private member variable that will hold the sales priceFrom of Sale.
     */
    @XmlElement(name = "SalesPriceFrom")
    private SalesPriceFrom salesPriceFrom;
    
    /**
     * The private member variable that will hold the not computed sales unit
     *  price from price override of Sale.
     */
    @XmlElement(name = "RegularSalesUnitPrice")
    private double regularsalesunitprice;

    /**
     * The private member variable that will hold the actual
     *  sales unit price of Sale.
     */
    @XmlElement(name = "ActualSalesUnitPrice")
    private double actualsalesunitprice;

    /**
     * The private member variable that will hold the extended amount of Sale.
     */
    @XmlElement(name = "ExtendedAmount")
    private double extendedAmt;

    /**
     * The private member variable that will hold the quantity of Sale.
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
     * The private member variable that will hold the Item Selling Rule.
     */
    @XmlElement(name = "ItemSellingRule")
    private ItemSellingRule itemSellingRule;
    /**
     * The private member variable that will hold the Forward.
     */
    @XmlElement(name = "Forward")
    private Forward forward;

    /**
     * The private member variable that will hold text notes for this line item
     */
    @XmlElement(name = "Note")
    private String note;

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
     * kind of ID number.
     * 前受金データの場合使用
     */
    @XmlElement(name = "InventoryReservationID")
    private String inventoryReservationID;

    /**
     *Points (獲得ポイント)
     */
    @XmlElement(name = "Points")
    private Points points;
    /**
     *購買動機コード
     */
    @XmlElement(name="barPurpose")
    private String barPurpose;

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
     * ExchangeFlag
     */
    @XmlElement(name="ExchangeFlag")
    private int exchangeFlag;
    
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
     * Gets the Regular sales unit price of Sale.
     *
     * @return        Regular sales unit price of Sale.
     */
    
    /**
     * barReservationType
     */
    @XmlElement(name = "barReservationType")
    private BarReservationType barReservationType;
    
    /**
     * taxableFlag
     */
    @XmlElement(name = "TaxableFlag")
    private String taxableFlag;

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
    
    public final double getRegularsalesunitprice() {
        return regularsalesunitprice;
    }

    /**
     * Sets the Regular sales unit price of Sale.
     *
     * @param regularsalesunitpriceToSet   The new value for regular
     *                                      sales unit price of Sale.
     */
    public final void setRegularsalesunitprice(
            final long regularsalesunitpriceToSet) {
        this.regularsalesunitprice = regularsalesunitpriceToSet;
    }

    /**
     * Gets the Actual sales unit price of Sale.
     *
     * @return        Actual sales unit price of Sale.
     */
    public final double getActualsalesunitprice() {
        return actualsalesunitprice;
    }

    /**
     * Sets the Actual sales unit price of Sale.
     *
     * @param actualsalesunitpriceToSet   The new value for
     *                                     actual sales unit price of Sale.
     */
    public final void setActualsalesunitprice(
            final long actualsalesunitpriceToSet) {
        this.actualsalesunitprice = actualsalesunitpriceToSet;
    }

    /**
     * Default Constructor for Sale.
     *
     * Sets the member variable to default value.
     */
    public Sale() {
        description = null;
        extendedAmt = 0;
        itemID = null;
        taxType = 0;
        quantity = 0;
        actualsalesunitprice = 0;
        extendedAmt = 0;
        discountAmount = 0;
    }

    /**
     * Gets the description of Sale.
     *
     * @return        The description of Sale.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Sets the description of Sale.
     *
     * @param descriptionToSet      The new value for description of Sale.
     */
    public final void setDescription(final String descriptionToSet) {
        this.description = descriptionToSet;
    }

    /**
     * Gets the newAddedFlag of Sale.
     *
     * @return  The newAddedFlag of Sale.
     */
    public final String getNewAddedFlag() {
        return newAddedFlag;
    }

    /**
     * Sets the newAddedFlag of Sale.
     *
     * @param newAddedFlag      The new value for newAddedFlag of Sale.
     */
    public final void setNewAddedFlag(final String newAddedFlag) {
        this.newAddedFlag = newAddedFlag;
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
     * Gets the returnExchangeFlag of Sale.
     *
     * @return  The returnExchangeFlag of Sale.
     */
    public final String getReturnExchangeFlag() {
        return returnExchangeFlag;
    }

    /**
     * Sets the returnExchangeFlag of Sale.
     *
     * @param returnExchangeFlagToSet      The new value for returnExchangeFlag of Sale.
     */
    public final void setReturnExchangeFlag(final String returnExchangeFlagToSet) {
        this.returnExchangeFlag = returnExchangeFlagToSet;
    }

    /**
     * Gets the extended amount of Sale.
     *
     * @return        The extended amount of Sale.
     */
    public final double getExtendedAmt() {
        return extendedAmt;
    }

    /**
     * Sets the extended amount of Sale.
     *
     * @param extendedAmtToSet      The new value for extended amount of Sale.
     */
    public final void setExtendedAmt(final long extendedAmtToSet) {
        this.extendedAmt = extendedAmtToSet;
    }

    /**
     * Gets the item ID of Sale.
     *
     * @return        The item ID of Sale.
     */
    public final ItemID getItemID() {
        return itemID;
    }

    /**
     * Sets the item ID of Sale.
     *
     * @param itemIDToSet        The new value for item ID of Sale.
     */
    public final void setItemID(final ItemID itemIDToSet) {
        this.itemID = itemIDToSet;
    }

    /**
     * Gets the quantity of Sale.
     *
     * @return        The quantity of Sale.
     */
    public final int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity of Sale.
     *
     * @param quantityToSet        The new value for quantity of Sale.
     */
    public final void setQuantity(final int quantityToSet) {
        this.quantity = quantityToSet;
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
     * Gets the returnFlag  of Sale.
     *
     * @return        The returnFlag of Sale.
     */
    public final String getReturnFlag() {
        return returnFlag;
    }

    /**
     * Sets the returnFlag of Sale.
     *
     * @param returnFlag        The new value for returnFlag of Sale.
     */
    public final void setReturnFlag(final String returnFlag) {
        this.returnFlag = returnFlag;
    }
    
    /**
     * Gets the Department  of Sale.
     *
     * @return        The department of Sale.
     */
    public final String getDepartment() {
        return department;
    }

    /**
     * Sets the Department of Sale.
     *
     * @param departmentToSet        The new value for Department of Sale.
     */
    public final void setDepartment(final String departmentToSet) {
        this.department = departmentToSet;
    }

    /**
     * Gets the Line  of Sale.
     *
     * @return        The Line of Sale.
     */
    public final String getLine() {
        return line;
    }

    /**
     * Sets the Line of Sale.
     *
     * @param lineToSet        The new value for Line of Sale.
     */
    public final void setLine(final String lineToSet) {
        this.line = lineToSet;
    }

    /**
     * Gets the Class  of Sale.
     *
     * @return        The Class of Sale.
     */
    public final String getClas() {
        return clas;
    }

    /**
     * Sets the Class of Sale.
     *
     * @param clasToSet        The new value for Class of Sale.
     */
    public final void setClas(final String clasToSet) {
        this.clas = clasToSet;
    }

    /**
     * Sets the discount amount of the sale item.
     * @param discountAmountToSet The Discount Amount.
     */
    public final void setDiscountAmount(final double discountAmountToSet) {
        this.discountAmount = discountAmountToSet;
    }

    /**
     * Gets the discount amount of the sale item.
     * @return  The Discount Amount.
     */
    public final double getDiscountAmount() {
        return discountAmount;
    }

    /**
     * Sets the extended amount from Sales
     * Price and Discount Amount of sale item.
     * @param extendedDiscountAmountToSet The Extended Discount Amount.
     */
    public final void setExtendedDiscountAmount(
            final double extendedDiscountAmountToSet) {
        this.extendedDiscountAmount = extendedDiscountAmountToSet;
    }

    /**
     * Gets the extended amount from Sales Price
     *  and Discount Amount of sale item.
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
     * Sets the Tax under sale.
     *
     * @param taxToSet        The new value for the Tax under sale.
     */
    public final void setTax(final List<Tax> taxToSet) {
        this.tax = taxToSet;
    }
    /**
     * Gets the Tax under sale.
     *
     * @return        The Tax under sale.
     */
    public final List<Tax> getTax() {
        return tax;
    }

    /**
     * Sets the setItemSellingRule under sale.
     *
     * @param itemsellingruleToSet The new value for the Tax under
     * itemsellingrule.
     */
    public void setItemSellingRule(ItemSellingRule itemSellingRule) {
        this.itemSellingRule = itemSellingRule;
    }

    /**
     * Gets the Tax under sale.
     *
     * @return        The Tax under itemSellingRule.
     */
    public ItemSellingRule getItemSellingRule() {
        return itemSellingRule;
    }

    public final String getNote() {
        return note;
    }

    public final void setNote(final String note) {
        this.note = note;
    }


    /**
     * @return itemType
     */
    public String getItemType() {
        return itemType;
    }

    /**
     * @param itemType セットする itemType
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
     * @param action セットする action
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
     * @param reasonCode セットする reasonCode
     */
    public void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
    }

    /**
     * @return inventoryReservationID
     */
    public String getInventoryReservationID() {
        return inventoryReservationID;
    }

    /**
     * @param inventoryReservationID セットする inventoryReservationID
     */
    public void setInventoryReservationID(String inventoryReservationID) {
        this.inventoryReservationID = inventoryReservationID;
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
     * @return barPurpose
     */
    public String getBarPurpose() {
        return barPurpose;
    }

    /**
     * @param barPurpose セットする barPurpose
     */
    public void setBarPurpose(String barPurpose) {
        this.barPurpose = barPurpose;
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
     * @param regularsalesunitprice セットする regularsalesunitprice
     */
    public void setRegularsalesunitprice(double regularsalesunitprice) {
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
     * @return the String Representation of Sale.
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
           .append("TaxType : ").append(this.taxType).append(crlf)
           .append("ItemSellingRule : ").append(this.itemSellingRule);
        return str.toString();
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
    public final String getOldSizeId() {
		return oldSizeId;
	}
	public final void setOldSizeId(String oldSizeId) {
		this.oldSizeId = oldSizeId;
	}
	public final String getSizePatternId1() {
		return sizePatternId1;
	}
	public final void setSizePatternId1(String sizePatternId1) {
		this.sizePatternId1 = sizePatternId1;
	}
	public final String getSizePatternId2() {
		return sizePatternId2;
	}
	public final void setSizePatternId2(String sizePatternId2) {
		this.sizePatternId2 = sizePatternId2;
	}
	public final String getBrandId() {
		return brandId;
	}
	public final void setBrandId(String brandId) {
		this.brandId = brandId;
	}
	public final String getOldSku() {
		return oldSku;
	}
	public final void setOldSku(String oldSku) {
		this.oldSku = oldSku;
	}
	public final String getRepSku() {
		return repSku;
	}
	public final void setRepSku(String repSku) {
		this.repSku = repSku;
	}
	public final QRPromotionInfo getQrPromotionInfo() {
		return qrPromotionInfo;
	}
	public final void setQrPromotionInfo(QRPromotionInfo qrPromotionInfo) {
		this.qrPromotionInfo = qrPromotionInfo;
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
     * @return the exchangeFlag
     */
    public final int getExchangeFlag() {
        return exchangeFlag;
    }
    /**
     * @param exchangeFlag the exchangeFlag to set
     */
    public final void setExchangeFlag(final int exchangeFlag) {
        this.exchangeFlag = exchangeFlag;
    }
    /**
     * @param taxableflag the taxable flag to set
     */
    public final void setTaxableFlag(final String taxableFlag) {
        this.taxableFlag = taxableFlag;
    }
    /**
     * @return the taxableFlag
     */
    public final String getTaxableFlag() {
        return taxableFlag;
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
	public final String getBrandName() {
		return brandName;
	}
	public final void setBrandName(String brandName) {
		this.brandName = brandName;
	}
	public final String getDptNameLocal() {
		return dptNameLocal;
	}
	public final void setDptNameLocal(String dptNameLocal) {
		this.dptNameLocal = dptNameLocal;
	}
}
