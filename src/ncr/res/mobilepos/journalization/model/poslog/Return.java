/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * Return
 *
 * Model Class for Return
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
 * Return Model Object.
 *
 * <P>A Return Node in POSLog XML.
 *
 * <P>The Return node is under LineItem Node.
 * And mainly holds the details of the returned transaction.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Return")
public class Return {
    /**
     * The private member variable that will hold the tax type of Return.
     */
    @XmlAttribute(name = "TaxType")
    private int taxType;

    /**
     * The private member variable that will hold the Department.
     */
    @XmlAttribute(name = "Department")
    private String department;

    /**
     * The private member variable that will hold the Line.
     */
    @XmlAttribute(name = "Line")
    private String line;

    /**
     * The private member variable that will hold the Class.
     */
    @XmlAttribute(name = "Class")
    private String clas;

    /**
     * The private member variable that will hold the newAddedFlag of Return.
     */
    @XmlElement(name = "NewAddedFlag")
    private String newAddedFlag;

    /**
     * The private member variable that will hold the urgentEntryFlag of Return.
     */
    @XmlElement(name = "UrgentEntryFlag")
    private String urgentEntryFlag;

    /**
     * The private member variable that will hold the item type of Return.
     */
    @XmlAttribute(name = "ItemType")
    private String itemType;

    /**
     * ReasonCode
     */
    @XmlAttribute(name = "ReasonCode")
    private String reasonCode;
    
    /**
     * The private member variable that will hold the taxable flag of Return.
     */
    @XmlAttribute(name = "TaxableFlag")
    private String taxableFlag;

    /**
     * The private member variable that will hold the item ID
     * of the returned item.
     */
    //start modify by mlwang 2014/09/03
    @XmlElement(name = "ItemID")
    private ItemID itemID;
    //end modify 2014/09/02

    /**
     * The private member variable that will hold the ScanBarCode of Return.
     */
    @XmlElement(name = "ScanBarCode")
    private String scanBarCode;
    
    /**
     * The private member variable that will hold the description
     * of the returned item.
     */
    @XmlElement(name = "Description")
    private String description;

    /**
     * The private member variable that will hold the mdKanaName of Return.
     */
    @XmlElement(name = "MdKanaName")
    private String mdKanaName;

    /**
     * The private member variable that will hold the sku of Return.
     */
    @XmlElement(name = "Sku")
    private String sku;

    /**
     * The private member variable that will hold the sku type of Return.
     */
    @XmlElement(name = "SkuType")
    private String skuType;
    
    /**
     * The private member variable that will hold the mdVenderof Return.
     */
    @XmlElement(name = "MdVender")
    private String mdVender;
    
    /**
     * The private member variable that will hold the SupplierCode of Return.
     */
    @XmlElement(name = "SupplierCode")
    private String supplierCode;
    
    /**
     * The private member variable that will hold the SubSupplierCode Return.
     */
    @XmlElement(name = "SubSupplierCode")
    private String subSupplierCode;
    
    /**
     * The private member variable that will hold the colorId of Return.
     */
    @XmlElement(name = "ColorId")
    private String colorId;
    
    /**
     * The private member variable that will hold the colorName of Return.
     */
    @XmlElement(name = "ColorName")
    private String colorName;
    
    /**
     * The private member variable that will hold the sizeId of Return.
     */
    @XmlElement(name = "SizeId")
    private String sizeId;
    
    /**
     * The private member variable that will hold the sizeName of Return.
     */
    @XmlElement(name = "SizeName")
    private String sizeName;
    
    /**
     * The private member variable that will hold the groupCode of Return.
     */
    @XmlElement(name = "GroupCode")
    private String groupCode;
    
    /**
     * The private member variable that will hold the sales priceFrom of Return.
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
     * sales unit price of the returned item.
     */
    @XmlElement(name = "ActualSalesUnitPrice")
    private long actualsalesunitprice;

    /**
     * The private member variable that will hold the extended.
     * amount of the returned item.
     */
    @XmlElement(name = "ExtendedAmount")
    private long extendedAmt;
    /**
     * The extended Discount Amount.
     */
    @XmlElement(name = "ExtendedDiscountAmount")
    private double extendedDiscountAmount;
    
    /**
     * The private member variable that will hold the CostPrice of Return.
     */
    @XmlElement(name = "CostPrice")
    private int costPrice;
    
    /**
     * The Retail Price Modifier.
     */
    @XmlElement(name = "RetailPriceModifier")
    private List<RetailPriceModifier> retailPriceModifier;
    /**
     * The private member variable that will hold the quantity.
     * of the returned item.
     */
    @XmlElement(name = "Quantity")
    private int quantity;

    /**
     * The private member variable that will hold the Disposal.
     * of the returned item.
     */
    @XmlElement(name = "Disposal")
    private Disposal disposal;

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
     * The private member variable that will hold text notes for this line item
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
     * barInventoryLineNo
     */
    @XmlElement(name = "barInventoryLineNo")
    private int barInventoryLineNo;
    
    /**
     * barPoints
     */
    @XmlElement(name = "barPoints")
    private BarPoints barPoints;

    /**
     * barPurpose
     */
    @XmlElement(name = "barPurpose")
    private String barPurpose;

    /**
     * OperatorID
     */
    @XmlElement(name = "OperatorID")
    private OperatorID operatorID;

    /**
     * Disposa
     */
    @XmlElement(name = "Disposa")
    private String Disposa;

    /**
     * barPSType
     */
    @XmlElement(name = "barPSType")
    private String barPSType;

    /**
     * Gets the Item Selling Rule information of the returned item.
     *
     * @return        Returns the Item Selling Rule information of returned item.
     */
    public final ItemSellingRule getItemSellingRule() {
        return itemSellingRule;
    }

    /**
     * Sets the Item Selling Rule information of the returned item.
     *
     * @param itemSellingRule        The new value for Item Selling Rule information
     */
    public final void setItemSellingRule(ItemSellingRule itemSellingRule) {
        this.itemSellingRule = itemSellingRule;
    }

    /**
     * Gets the Disposal of the returned item.
     *
     * @return        Returns the disposal information of Return.
     */
    public final Disposal getDisposal() {
        return disposal;
    }

    /**
     * Sets the Return's Disposal.
     *
     * @param disposalToSet        The new value for disposal of the Return
     */
    public final void setDisposal(final Disposal disposalToSet) {
        this.disposal = disposalToSet;
    }

    /**
     * Gets the item type of Return.
     *
     * @return        The item type of Return.
     */
    public final String getItemType() {
        return itemType;
    }

    /**
     * Sets the item type of Return.
     *
     * @param itemTypeToSet        The new value of Return's item type.
     */
    public final void setItemType(final String itemTypeToSet) {
        this.itemType = itemTypeToSet;
    }

    /**
     * Gets the actual sales price of Return.
     *
     * @return        The actual sales price of Return.
     */
    public final long getActualsalesunitprice() {
        return actualsalesunitprice;
    }

    /**
     * Sets the actual sales price of Return.
     *
     * @param actualsalesunitpriceToSet  The new value of Return's actual
     *                                     sales price.
     */
    public final void setActualsalesunitprice(
            final long actualsalesunitpriceToSet) {
        this.actualsalesunitprice = actualsalesunitpriceToSet;
    }

    /**
     * The default constructor for Return class.
     */
    public Return() {
        description = null;
        extendedAmt = 0;
        itemID = null;
        taxType = 0;
        itemType = null;
        quantity = 0;
        actualsalesunitprice = 0;
    }

    /**
     * Gets the description of Return.
     *
     * @return        The description of Return.
     */
    public final String getDescription() {
        return description;
    }

    /**
     * Sets the description of Return.
     *
     * @param descriptionToSet        The new value of Return's description.
     */
    public final void setDescription(final String descriptionToSet) {
        this.description = descriptionToSet;
    }

    /**
     * Gets the newAddedFlag of Return.
     *
     * @return  The newAddedFlag of Return.
     */
    public final String getNewAddedFlag() {
        return newAddedFlag;
    }

    /**
     * Sets the newAddedFlag of Return.
     *
     * @param newAddedFlag      The new value for newAddedFlag of Return.
     */
    public final void setNewAddedFlag(final String newAddedFlag) {
        this.newAddedFlag = newAddedFlag;
    }

    /**
     * Gets the urgentEntryFlag of Return.
     *
     * @return  The urgentEntryFlag of Return.
     */
    public final String getUrgentEntryFlag() {
        return urgentEntryFlag;
    }

    /**
     * Sets the urgentEntryFlag of Return.
     *
     * @param urgentEntryFlagToSet      The new value for urgentEntryFlag of Return.
     */
    public final void setUrgentEntryFlag(final String urgentEntryFlagToSet) {
        this.urgentEntryFlag = urgentEntryFlagToSet;
    }

    /**
     * Gets the extended amount of Return.
     *
     * @return        The extended amount of Return.
     */
    public final long getExtendedAmt() {
        return extendedAmt;
    }

    /**
     * Sets the extended amount of Return.
     *
     * @param extendedAmtToSet        The new value of Return's extended amount.
     */
    public final void setExtendedAmt(final long extendedAmtToSet) {
        this.extendedAmt = extendedAmtToSet;
    }

    /**
     * Gets the item ID of Return.
     *
     * @return        The item ID of Return.
     */
    public final ItemID getItemID() {
        return itemID;
    }

    /**
     * Sets the item ID of Return.
     *
     * @param itemIDToSet        The new value of Return's item ID.
     */
    public final void setItemID(final ItemID itemIDToSet) {
        this.itemID = itemIDToSet;
    }

    /**
     * Gets the quantity of Return.
     *
     * @return        The quantity of Return.
     */
    public final int getQuantity() {
        return quantity;
    }

    /**
     * Sets the item ID of Return.
     *
     * @param quantityToSet        The new value of Return's quantity.
     */
    public final void setQuantity(final int quantityToSet) {
        this.quantity = quantityToSet;
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
     * @param clasToSet        The new value for Line of Sale.
     */
    public final void setClas(final String clasToSet) {
        this.clas = clasToSet;
    }

    public double getRegularsalesunitprice() {
        return regularsalesunitprice;
    }

    public void setRegularsalesunitprice(double regularsalesunitprice) {
        this.regularsalesunitprice = regularsalesunitprice;
    }

    public double getExtendedDiscountAmount() {
        return extendedDiscountAmount;
    }

    public void setExtendedDiscountAmount(double extendedDiscountAmount) {
        this.extendedDiscountAmount = extendedDiscountAmount;
    }

    public List<RetailPriceModifier> getRetailPriceModifier() {
        return retailPriceModifier;
    }

    public void setRetailPriceModifier(List<RetailPriceModifier> retailPriceModifier) {
        this.retailPriceModifier = retailPriceModifier;
    }

    /**
     * Get the taxableFlag of Return.
     */
    public String getTaxableFlag() {
        return taxableFlag;
    }

    /**
     * Sets the taxableFlag of Return.
     */
    public void setTaxableFlag(String taxableFlag) {
        this.taxableFlag = taxableFlag;
    }

    /**
     * @return tax
     */
    public List<Tax> getTax() {
        return tax;
    }

    /**
     * @param tax セットする tax
     */
    public void setTax(List<Tax> tax) {
        this.tax = tax;
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
    
    public final String getNote() {
        return note;
    }

    public final void setNote(final String note) {
        this.note = note;
    }

    /**
     * @return barPoints
     */
    public BarPoints getBarPoints() {
        return barPoints;
    }

    /**
     * @param barPoints セットする barPoints
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
     * @param barPurpose セットする barPurpose
     */
    public void setBarPurpose(String barPurpose) {
        this.barPurpose = barPurpose;
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
     * @return disposa
     */
    public String getDisposa() {
        return Disposa;
    }

    /**
     * @param disposa セットする disposa
     */
    public void setDisposa(String disposa) {
        Disposa = disposa;
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
     * @return The String representation of Return.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("ItemID : ").append(this.itemID).append(crlf)
        .append("ActualSalesUnitPrice : ").append(this.actualsalesunitprice)
        .append(crlf)
        .append("Class : ").append(this.clas).append(crlf)
        .append("Department : ").append(this.department).append(crlf)
        .append("Description : ").append(this.description).append(crlf)
        .append("ExtendedAmount : ").append(this.extendedAmt).append(crlf)
        .append("ItemType : ").append(this.itemType).append(crlf)
        .append("Line : ").append(this.line).append(crlf)
        .append("Quantity : ").append(this.quantity).append(crlf);

        if (null != this.disposal) {
            str.append("Disposal : ").append(this.disposal.toString());
        }

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
