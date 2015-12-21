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
 * class that creates adjustment messages.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Adjustment")
public class Adjustment extends OutgoingMessage {

    /**
     * Unique identifier for the ADJUSTMENT within the transaction.
     */
    @XmlElement (name = "EntryID")
    private int entryID = -1;
    /**
     * flag for the kind of adjustment (0-3 only).
     */
    @XmlElement (name = "AdjustmentFlag")
    private int adjustmentFlag = -1;
    /**
     * flag for the type of adjustment (0-4 only).
     */
    @XmlElement (name = "AdjustmentType")
    private int adjustmentType = -1;
    /**
     * flag for the level of adjustment (0-4 only).
     */
    @XmlElement (name = "AdjustmentLevel")
    private int adjustmentLevel = -1;
    /**
     * flag for the type of calculation (0-2 only).
     */
    @XmlElement (name = "CalculationType")
    private int calculationType = -1;
    /**
     * ItemEntryID of the item that the Adjustment applies to.
     * Must match the ItemEntryID field of a previously entered item.
     */
    @XmlElement (name = "ItemEntryID")
    private int itemEntryID = -1;
    /**
     * PricePrecision field for MatchNetUnitPrice.
     */
    @XmlElement (name = "MNUPrecision")
    private int mnupPrecision = -1;
    /**
     * Match items with the net unit price specified here.
     */
    @XmlElement (name = "MatchNetUnitPrice")
    private int matchNetUnitPrice = -1;
    /**
     * Department Code value (0-9999).
     */
    @XmlElement (name = "Department")
    private int department = -1;
    /**
     * Price precision field for UnitAdjustment.
     */
    @XmlElement (name = "AdjustmentPrecision")
    private int adjustmentPrecision = -1;
    /**
     * Adjustment to make to each unit.
     */
    @XmlElement (name = "UnitAdjustment")
    private int unitAdjustment = -1;
    /**
     * the type of quantity (0-4 only).
     */
    @XmlElement (name = "QuantityType")
    private int quantityType = -1;
    /**
     * the subtype of the quantity (0-42 only).
     */
    @XmlElement (name = "QuantitySubType")
    private int quantitySubType = -1;
    /**
     * the precision of the quantity.
     */
    @XmlElement (name = "QuantityPrecision")
    private int quantityPrecision = -1;
    /**
     * the quantity.
     */
    @XmlElement (name = "Quantity")
    private int quantity = -1;
    /**
     * reserved for future use. ignored.
     */
    @XmlElement (name = "TLQFlag")
    private int tlqFlag = -1;
    /**
     * the flag for if the item is discountable.
     */
    @XmlElement (name = "DiscountableFlag")
    private int discountableFlag = -1;
    /**
     * the type of reward calculation (0-2 only).
     */
    @XmlElement (name = "RewardCalculation")
    private int rewardCalculation = -1;
    /**
     * the priority.
     */
    @XmlElement (name = "Priority")
    private int priority = -1;

    @Override
    public final String createMessage(final String termId,
            final String transactionId) throws MessageException {
        String messageBody = String.format("%02d", this.getMessageHeader());
        messageBody += String.format("%06d", entryID);
        messageBody += String.format("%1d", adjustmentFlag);
        messageBody += String.format("%1d", adjustmentType);
        messageBody += String.format("%1d", adjustmentLevel);
        messageBody += String.format("%1d", calculationType);
        messageBody += String.format("%06d", itemEntryID);
        messageBody += String.format("%1d", mnupPrecision);
        messageBody += String.format("%010d", matchNetUnitPrice);
        messageBody += String.format("%04d", department);
        messageBody += String.format("%1d", adjustmentPrecision);
        messageBody += String.format("%010d", unitAdjustment);
        messageBody += String.format("%1d", quantityType);
        messageBody += String.format("%02d", quantitySubType);
        messageBody += String.format("%1d", quantityPrecision);
        messageBody += String.format("%06d", quantity);
        messageBody += String.format("%1d", tlqFlag);
        messageBody += String.format("%1d", discountableFlag);
        messageBody += String.format("%1d", rewardCalculation);
        messageBody += String.format("%04d", priority);
        messageBody += OutgoingMessage.MESSAGE_TERMINATOR;

        String fullMessage = TransportHeader.create(
                                messageBody,
                                termId,
                                transactionId);
        fullMessage += messageBody;
        return fullMessage;
    }
    /**
     * Default constructor.
     */
    public Adjustment() {
        super(MessageTypes.ADJUSTMENT);
    }
    /**
     * constructor.
     * @param entryIDToSet - entryID
     * @param adjustmentFlagToSet - adjustmentFlag
     * @param adjustmentTypeToSet - adjustmentType
     * @param adjustmentLevelToSet - adjustmentLevel
     * @param calculationTypeToSet - calculationType
     * @param itemEntryIDToSet - itemEntryID
     * @param mnupPrecisionToSet - mnupPrecision
     * @param matchNetUnitPriceToSet - matchNetUnitPrice
     * @param departmentToSet - department
     * @param adjustmentPrecisionToSet - adjustmentPrecision
     * @param unitAdjustmentToSet - unitAdjustment
     * @param quantityTypeToSet - quantityType
     * @param quantitySubTypeToSet - quantitySubType
     * @param quantityPrecisionToSet - quantityPrecision
     * @param quantityToSet - quantity
     * @param tlqFlagToSet - tlqFlag
     * @param discountableFlagToSet - discountableFlag
     * @param rewardCalculationToSet - rewardCalculation
     * @param priorityToSet - priority
     */
    public Adjustment(final int entryIDToSet,
            final int adjustmentFlagToSet,
            final int adjustmentTypeToSet,
            final int adjustmentLevelToSet,
            final int calculationTypeToSet,
            final int itemEntryIDToSet,
            final int mnupPrecisionToSet,
            final int matchNetUnitPriceToSet,
            final int departmentToSet,
            final int adjustmentPrecisionToSet,
            final int unitAdjustmentToSet,
            final int quantityTypeToSet,
            final int quantitySubTypeToSet,
            final int quantityPrecisionToSet,
            final int quantityToSet,
            final int tlqFlagToSet,
            final int discountableFlagToSet,
            final int rewardCalculationToSet,
            final int priorityToSet) {
        super(MessageTypes.ADJUSTMENT);

        this.entryID = entryIDToSet;
        this.adjustmentFlag = adjustmentFlagToSet;
        this.adjustmentType = adjustmentTypeToSet;
        this.adjustmentLevel = adjustmentLevelToSet;
        this.calculationType = calculationTypeToSet;
        this.itemEntryID = itemEntryIDToSet;
        this.mnupPrecision = mnupPrecisionToSet;
        this.matchNetUnitPrice = matchNetUnitPriceToSet;
        this.department = departmentToSet;
        this.adjustmentPrecision = adjustmentFlagToSet;
        this.unitAdjustment = unitAdjustmentToSet;
        this.quantityType = quantityTypeToSet;
        this.quantitySubType = quantitySubTypeToSet;
        this.quantityPrecision = quantityPrecisionToSet;
        this.quantity = quantityToSet;
        this.tlqFlag = tlqFlagToSet;
        this.discountableFlag = discountableFlagToSet;
        this.rewardCalculation = rewardCalculationToSet;
        this.priority = priorityToSet;
    }

    /**
     * Getter for EntryID.
     * @return The Entry ID.
     */
    public final int getEntryID() {
        return entryID;
    }

    /**
     * Setter for Entry ID.
     * @param entryIDToSet The new Entry ID.
     */
    public final void setEntryID(final int entryIDToSet) {
        this.entryID = entryIDToSet;
    }

    /**
     * Getter for Adjustment Flag.
     * @return The Adjustment Flag.
     */
    public final int getAdjustmentFlag() {
        return adjustmentFlag;
    }

    /**
     * Setter for AdjusmentFlag.
     * @param adjustmentFlagToSet The new Adjustment Flag.
     */
    public final void setAdjustmentFlag(final int adjustmentFlagToSet) {
        this.adjustmentFlag = adjustmentFlagToSet;
    }

    /**
     * Getter for the Adjustment Type.
     * @return The Adjustment Type.
     */
    public final int getAdjustmentType() {
        return adjustmentType;
    }

    /**
     * Setter for Adjustment Type.
     * @param adjustmentTypeToSet The new Adjustment Type.
     */
    public final void setAdjustmentType(final int adjustmentTypeToSet) {
        this.adjustmentType = adjustmentTypeToSet;
    }

    /**
     * Getter for the Adjustment Level.
     * @return  The Adjustment Level.
     */
    public final int getAdjustmentLevel() {
        return adjustmentLevel;
    }

    /**
     * Setter for the Adjustment Level.
     * @param adjustmentLevelToSet The Adjustment Level.
     */
    public final void setAdjustmentLevel(final int adjustmentLevelToSet) {
        this.adjustmentLevel = adjustmentLevelToSet;
    }

    /**
     * Getter for the Calculation Type.
     * @return  The Calculation Type.
     */
    public final int getCalculationType() {
        return calculationType;
    }

    /**
     * Setter for the Calculation Type.
     * @param calculationTypeToSet  The new Calculation Type.
     */
    public final void setCalculationType(final int calculationTypeToSet) {
        this.calculationType = calculationTypeToSet;
    }

    /**
     * Getter for the Item Entry ID.
     * @return  The Item Entry ID.
     */
    public final int getItemEntryID() {
        return itemEntryID;
    }

    /**
     * Setter for the Item Entry ID.
     * @param itemEntryIDToSet  The new Item Entry ID.
     */
    public final void setItemEntryID(final int itemEntryIDToSet) {
        this.itemEntryID = itemEntryIDToSet;
    }

    /**
     * Getter for the MNUPrecision.
     * @return The MNUPrecision.
     */
    public final int getMnupPrecision() {
        return mnupPrecision;
    }

    /**
     * Setter for the MNUPrecision.
     * @param mnupPrecisionToSet The new MNUPrecision.
     */
    public final void setMnupPrecision(final int mnupPrecisionToSet) {
        this.mnupPrecision = mnupPrecisionToSet;
    }

    /**
     * Getter for the Match Net Unit Price.
     * @return The Match net Unit Price.
     */
    public final int getMatchNetUnitPrice() {
        return matchNetUnitPrice;
    }

    /**
     * Setter for the Match Net Unit Price.
     * @param matchNetUnitPriceToSet The new Match Net Unit Price.
     */
    public final void setMatchNetUnitPrice(final int matchNetUnitPriceToSet) {
        this.matchNetUnitPrice = matchNetUnitPriceToSet;
    }

    /**
     * Getter for the Department.
     * @return The Department.
     */
    public final int getDepartment() {
        return department;
    }

    /**
     * Setter for the Department.
     * @param departmentToSet The new Department.
     */
    public final void setDepartment(final int departmentToSet) {
        this.department = departmentToSet;
    }

    /**
     * Getter for the Adjustment Precision.
     * @return The Adjustment Precision.
     */
    public final int getAdjustmentPrecision() {
        return adjustmentPrecision;
    }

    /**
     * Setter for the Adjustment Precision.
     * @param adjustmentPrecisionToSet The Adjustment Precision.
     */
    public final void setAdjustmentPrecision(
            final int adjustmentPrecisionToSet) {
        this.adjustmentPrecision = adjustmentPrecisionToSet;
    }

    /**
     * Getter for the Unit Adjustment.
     * @return The Unit Adjustment.
     */
    public final int getUnitAdjustment() {
        return unitAdjustment;
    }

    /**
     * Setter for the Unit Adjustment.
     * @param unitAdjustmentToSet The Unit Adjustment.
     */
    public final void setUnitAdjustment(final int unitAdjustmentToSet) {
        this.unitAdjustment = unitAdjustmentToSet;
    }

    /**
     * Getter of the Quantity Type.
     * @return The Quantity Type.
     */
    public final int getQuantityType() {
        return quantityType;
    }

    /**
     * Setter of the Quantity Type.
     * @param quantityTypeToSet The Quantity Type.
     */
    public final void setQuantityType(final int quantityTypeToSet) {
        this.quantityType = quantityTypeToSet;
    }

    /**
     * Getter of the Quantity Sub Type.
     * @return  The Quantity Sub Type.
     */
    public final int getQuantitySubType() {
        return quantitySubType;
    }

    /**
     * Setter of the Quantity Sub Type.
     * @param quantitySubTypeToSet The new Quantity Sub Type.
     */
    public final void setQuantitySubType(final int quantitySubTypeToSet) {
        this.quantitySubType = quantitySubTypeToSet;
    }

    /**
     * Getter of the Quantity Precision.
     * @return  The Quantity Precision.
     */
    public final int getQuantityPrecision() {
        return quantityPrecision;
    }

    /**
     * Setter of the Quantity Precision.
     * @param quantityPrecisionToSet The new Quantity Precision.
     */
    public final void setQuantityPrecision(final int quantityPrecisionToSet) {
        this.quantityPrecision = quantityPrecisionToSet;
    }

    /**
     * Getter of the Quantity.
     * @return  The Quantity.
     */
    public final int getQuantity() {
        return quantity;
    }

    /**
     * Setter of the Quantity.
     * @param quantityToSet  The Quantity.
     */
    public final void setQuantity(final int quantityToSet) {
        this.quantity = quantityToSet;
    }

    /**
     * Getter of the TLQ Flag.
     * @return The TLQ Flag.
     */
    public final int getTlqFlag() {
        return tlqFlag;
    }

    /**
     * Setter of the TLQ Flag.
     * @param tlqFlagToSet   The new TLQ Flag.
     */
    public final void setTlqFlag(final int tlqFlagToSet) {
        this.tlqFlag = tlqFlagToSet;
    }

    /**
     * The Discountable Flag.
     * @return  The  Discountable flag.
     */
    public final int getDiscountableFlag() {
        return discountableFlag;
    }

    /**
     * Setter of the Discountable Flag.
     * @param discountableFlagtoSet  The Discountable flag.
     */
    public final void setDiscountableFlag(final int discountableFlagtoSet) {
        this.discountableFlag = discountableFlagtoSet;
    }

    /**
     * Getter of the Reward Calculation.
     * @return The Reward Calculation.
     */
    public final int getRewardCalculation() {
        return rewardCalculation;
    }

    /**
     * Setter of the Reward Calculation.
     * @param rewardCalculationToSet The Reward Calculation.
     */
    public final void setRewardCalculation(final int rewardCalculationToSet) {
        this.rewardCalculation = rewardCalculationToSet;
    }

    /**
     * Getter of the Priority.
     * @return The Priority.
     */
    public final int getPriority() {
        return priority;
    }

    /**
     * Setter of the Priority.
     * @param priorityToSet The new Priority.
     */
    public final void setPriority(final int priorityToSet) {
        this.priority = priorityToSet;
    }
}
