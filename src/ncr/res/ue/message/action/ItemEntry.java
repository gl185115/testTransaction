package ncr.res.ue.message.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.TransportHeader;

/**
 * class that creates a message for ITEM_ENTRY.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ItemEntry")
public class ItemEntry extends OutgoingMessage {

    /**
     * Unique identifier for the ITEM_ENTRY within the transaction.
     */
    @XmlElement(name = "ItemEntryID")
    private int itemEntryID = -1;
    /**
     * Purchase type flags (0-3 only).
     */
    @XmlElement(name = "EntryFlag")
    private int entryFlag = -1;

    /**
     * Gets the item entry id.
     *
     * @return the item entry id
     */
    public final int getItemEntryID() {
        return itemEntryID;
    }

    /**
     * Sets the item entry id.
     *
     * @param newItemEntryID the new item entry id
     */
    public final void setItemEntryID(final int newItemEntryID) {
        this.itemEntryID = newItemEntryID;
    }

    /**
     * Gets the entry flag.
     *
     * @return the entry flag
     */
    public final int getEntryFlag() {
        return entryFlag;
    }

    /**
     * Sets the entry flag.
     *
     * @param newEntryFlag the new entry flag
     */
    public final void setEntryFlag(final int newEntryFlag) {
        this.entryFlag = newEntryFlag;
    }

    /**
     * Gets the return flag.
     *
     * @return the return flag
     */
    public final int getReturnFlag() {
        return returnFlag;
    }

    /**
     * Sets the return flag.
     *
     * @param newReturnFlag the new return flag
     */
    public final void setReturnFlag(final int newReturnFlag) {
        this.returnFlag = newReturnFlag;
    }

    /**
     * Gets the item code.
     *
     * @return the item code
     */
    public final String getItemCode() {
        return itemCode;
    }

    /**
     * Sets the item code.
     *
     * @param newItemCode the new item code
     */
    public final void setItemCode(final String newItemCode) {
        this.itemCode = newItemCode;
    }

    /**
     * Gets the family code.
     *
     * @return the family code
     */
    public final int getFamilyCode() {
        return familyCode;
    }

    /**
     * Sets the family code.
     *
     * @param newFamilyCode the new family code
     */
    public final void setFamilyCode(final int newFamilyCode) {
        this.familyCode = newFamilyCode;
    }

    /**
     * Gets the department.
     *
     * @return the department
     */
    public final int getDepartment() {
        return department;
    }

    /**
     * Sets the department.
     *
     * @param newDepartment the new department
     */
    public final void setDepartment(final int newDepartment) {
        this.department = newDepartment;
    }

    /**
     * Gets the clearance level.
     *
     * @return the clearance level
     */
    public final int getClearanceLevel() {
        return clearanceLevel;
    }

    /**
     * Sets the clearance level.
     *
     * @param newClearanceLevel the new clearance level
     */
    public final void setClearanceLevel(final int newClearanceLevel) {
        this.clearanceLevel = newClearanceLevel;
    }

    /**
     * Gets the price precision.
     *
     * @return the price precision
     */
    public final int getPricePrecision() {
        return pricePrecision;
    }

    /**
     * Sets the price precision.
     *
     * @param newPricePrecision the new price precision
     */
    public final void setPricePrecision(final int newPricePrecision) {
        this.pricePrecision = newPricePrecision;
    }

    /**
     * Gets the unit price.
     *
     * @return the unit price
     */
    public final int getUnitPrice() {
        return unitPrice;
    }

    /**
     * Sets the unit price.
     *
     * @param newUnitPrice the new unit price
     */
    public final void setUnitPrice(final int newUnitPrice) {
        this.unitPrice = newUnitPrice;
    }

    /**
     * Gets the quantity type.
     *
     * @return the quantity type
     */
    public final int getQuantityType() {
        return quantityType;
    }

    /**
     * Sets the quantity type.
     *
     * @param newQuantityType the new quantity type
     */
    public final void setQuantityType(final int newQuantityType) {
        this.quantityType = newQuantityType;
    }

    /**
     * Gets the quantity sub type.
     *
     * @return the quantity sub type
     */
    public final int getQuantitySubType() {
        return quantitySubType;
    }

    /**
     * Sets the quantity sub type.
     *
     * @param newQuantitySubType the new quantity sub type
     */
    public final void setQuantitySubType(final int newQuantitySubType) {
        this.quantitySubType = newQuantitySubType;
    }

    /**
     * Gets the quantity precision.
     *
     * @return the quantity precision
     */
    public final int getQuantityPrecision() {
        return quantityPrecision;
    }

    /**
     * Sets the quantity precision.
     *
     * @param newQuantityPrecision the new quantity precision
     */
    public final void setQuantityPrecision(final int newQuantityPrecision) {
        this.quantityPrecision = newQuantityPrecision;
    }

    /**
     * Gets the quantity.
     *
     * @return the quantity
     */
    public final int getQuantity() {
        return quantity;
    }

    /**
     * Sets the quantity.
     *
     * @param newQuantity the new quantity
     */
    public final void setQuantity(final int newQuantity) {
        this.quantity = newQuantity;
    }

    /**
     * Gets the discount flag.
     *
     * @return the discount flag
     */
    public final int getDiscountFlag() {
        return discountFlag;
    }

    /**
     * Sets the discount flag.
     *
     * @param newDiscountFlag the new discount flag
     */
    public final void setDiscountFlag(final int newDiscountFlag) {
        this.discountFlag = newDiscountFlag;
    }

    /**
     * Gets the tlq flag.
     *
     * @return the tlq flag
     */
    public final int getTlqFlag() {
        return tlqFlag;
    }

    /**
     * Sets the tlq flag.
     *
     * @param newTlqFlag the new tlq flag
     */
    public final void setTlqFlag(final int newTlqFlag) {
        this.tlqFlag = newTlqFlag;
    }

    /**
     * Gets the alternate price.
     *
     * @return the alternate price
     */
    public final int getAlternatePrice() {
        return alternatePrice;
    }

    /**
     * Sets the alternate price.
     *
     * @param newAlternatePrice the new alternate price
     */
    public final void setAlternatePrice(final int newAlternatePrice) {
        this.alternatePrice = newAlternatePrice;
    }

    /**
     * Returns integration behavior indicator (0-3 only).
     */
    @XmlElement(name = "ReturnFlag")
    private int returnFlag = -1;
    /**
     * The PLU Code, UPC or EAN.
     */
    @XmlElement(name = "ItemCode")
    private String itemCode = "";
    /**
     * ManufacturerÅfs Family Code for the item.
     */
    @XmlElement(name = "FamilyCode")
    private int familyCode = -1;
    /**
     * Department Code value (0-9999).
     */
    @XmlElement(name = "Dept")
    private int department = -1;
    /**
     * the clearance level (0-9 only).
     */
    @XmlElement(name = "ClearanceLevel")
    private int clearanceLevel = -1;
    /**
     * the price precision.
     */
    @XmlElement(name = "PricePrecision")
    private int pricePrecision = -1;
    /**
     * the unit price.
     */
    @XmlElement(name = "UnitPrice")
    private int unitPrice = -1;
    /**
     * the quantity type (0-4 only).
     */
    @XmlElement(name = "QuantityType")
    private int quantityType = -1;
    /**
     * the quantity subtype (0-42 only).
     */
    @XmlElement(name = "QuantitySubType")
    private int quantitySubType = -1;
    /**
     * the quantity precision.
     */
    @XmlElement(name = "QuantityPrecision")
    private int quantityPrecision = -1;
    /**
     * the quantity.
     */
    @XmlElement(name = "Quantity")
    private int quantity = -1;
    /**
     * the discount flag.
     */
    @XmlElement(name = "DiscountFlag")
    private int discountFlag = -1;
    /**
     * reserved for future use. ignored.
     */
    @XmlElement(name = "TLQFlag")
    private int tlqFlag = -1;
    /**
     * the alternate price.
     */
    @XmlElement(name = "AlternatePrice")
    private int alternatePrice = -1;

    /*
     * (non-Javadoc)
     *
     * @see ncr.res.ue.message.OutgoingMessage#createMessage(java.lang.String,
     * java.lang.String)
     */
    @Override
    public final String createMessage(final String termId,
            final String transactionId) throws MessageException {
        String messageBody = String.format("%02d", this.getMessageHeader());
        messageBody += String.format("%06d", itemEntryID);
        messageBody += String.format("%1d", entryFlag);
        messageBody += String.format("%1d", returnFlag);
        messageBody += String.format("%16s", itemCode);
        messageBody += String.format("%03d", familyCode);
        messageBody += String.format("%04d", department);
        messageBody += String.format("%1d", clearanceLevel);
        messageBody += String.format("%1d", pricePrecision);
        messageBody += String.format("%010d", unitPrice);
        messageBody += String.format("%1d", quantityType);
        messageBody += String.format("%02d", quantitySubType);
        messageBody += String.format("%1d", quantityPrecision);
        messageBody += String.format("%06d", quantity);
        messageBody += String.format("%1d", discountFlag);
        messageBody += String.format("%1d", tlqFlag);
        messageBody += String.format("%010d", alternatePrice);
        messageBody += OutgoingMessage.MESSAGE_TERMINATOR;

        String fullMessage = TransportHeader.create(
                                messageBody,
                                termId,
                                transactionId);
        fullMessage += messageBody;

        return fullMessage;
    }

    /**
     * constructor.
     * @param itemEntryIDToSet - itemEmtry
     * @param entryFlagToSet - entryFlag
     * @param returnFlagToSet - returnFlag
     * @param itemCodeToSet - itemCode
     * @param familyCodeToSet - familyCode
     * @param alternatePriceToSet - alternatePrice
     * @param departmentToSet - department
     * @param clearanceLevelToSet - clearanceLevel
     * @param pricePrecisionToSet - pricePrecision
     * @param unitPriceToSet - unitPrice
     * @param quantityTypeToSet - quantityType
     * @param quantitySubTypeToSet - quantitySubType
     * @param quantityPrecisionToSet - quantityPrecision
     * @param quantityToSet - quantity
     * @param discountFlagToSet - discountFlag
     * @param tlqFlagToSet - tlqFlag
     */
    public ItemEntry(final int itemEntryIDToSet, final int entryFlagToSet,
            final int returnFlagToSet, final String itemCodeToSet,
            final int familyCodeToSet, final int alternatePriceToSet,
            final int departmentToSet, final int clearanceLevelToSet,
            final int pricePrecisionToSet, final int unitPriceToSet,
            final int quantityTypeToSet, final int quantitySubTypeToSet,
            final int quantityPrecisionToSet, final int quantityToSet,
            final int discountFlagToSet, final int tlqFlagToSet) {
        super(MessageTypes.ITEM_ENTRY);

        this.itemEntryID = itemEntryIDToSet;
        this.entryFlag = entryFlagToSet;
        this.returnFlag = returnFlagToSet;
        this.itemCode = itemCodeToSet;
        this.familyCode = familyCodeToSet;
        this.department = departmentToSet;
        this.clearanceLevel = clearanceLevelToSet;
        this.pricePrecision = pricePrecisionToSet;
        this.unitPrice = unitPriceToSet;
        this.quantityType = quantityTypeToSet;
        this.quantitySubType = quantitySubTypeToSet;
        this.quantityPrecision = quantityPrecisionToSet;
        this.quantity = quantityToSet;
        this.discountFlag = discountFlagToSet;
        this.tlqFlag = tlqFlagToSet;
        this.alternatePrice = alternatePriceToSet;
    }

    /**
     * Instantiates a new item entry.
     */
    public ItemEntry() {
        super(MessageTypes.ITEM_ENTRY);
    }

}
