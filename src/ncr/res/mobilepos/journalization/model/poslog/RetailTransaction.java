/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * RetailTransaction
 *
 * Model Class for RetailTransaction
 *
 * De la Cerna, Jessel G.
 * Carlos G. Campos
 */

package ncr.res.mobilepos.journalization.model.poslog;

import java.util.Collections;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * RetailTransaction Model Object.
 *
 * <P>A RetailTransaction Node in POSLog XML.
 *
 * <P>The RetailTransaction node is under Transaction Node.
 * And mainly holds the information of the Customer's transaction
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "RetailTransaction")
public class RetailTransaction {

    /**
     * The private member variable that holds
     * RetailTransaction node specific version.
     */
    @XmlAttribute(name = "Version")
    private String version;

    /**
     * The private member variable that holds the transaction status
     * of the customer.
     */
    @XmlAttribute(name = "TransactionStatus")
    private String transactionStatus;

    /**
     * The private member variable that holds the OutsideSales Flag
     * of the customer.
     */
    @XmlAttribute(name = "OutsideSalesFlag")
    private Boolean outsideSalesFlag;

    /**
     * The private member variable that holds the Type Code
     * of the customer.
     */
    @XmlAttribute(name = "TypeCode")
    private String typeCode;

    /**
     * The private member variable that holds the layaway flag
     * of the customer.
     */
    @XmlElement(name = "LayawayFlag")
    private String layawayFlag;

    /**
     * The private member variable that holds the voider retail store ID
     * of the customer.
     */
    @XmlElement(name = "VoiderRetailStoreID")
    private String voidRetailStoreID;

    /**
     * The private member variable that holds the voider workstation ID
     * of the customer.
     */
    @XmlElement(name = "VoiderWorkstationID")
    private String voidWorkstationID;

    /**
     * The private member variable that holds the voider sequence number
     * of the customer.
     */
    @XmlElement(name = "VoiderSequenceNumber")
    private String voidSequenceNumber;

    /**
     * The private member variable that holds the voider business day date
     * of the customer.
     */
    @XmlElement(name = "VoiderBusinessDayDate")
    private String voidBusinessDayDate;

    /**
     * The private member variable that holds the voider begin date time
     * of the customer.
     */
    @XmlElement(name = "VoiderBeginDateTime")
    private String voidBeginDateTime;

    /** The Price Derivation Result. */
    @XmlElement(name = "PriceDerivationResult")
    private List<PriceDerivationResult> priceDerivationResult;

    /**
     * customerId
     */
    @XmlElement(name = "CustomerID")
    private String customerId;

    /**
     * The private member variable that holds the qr promotion items
     */
    @XmlElement(name="QRPromotionInfo")
    private QRPromotionInfo qrPromotionInfo;

    /**
     * The private member variable that holds the number of items
     * of the customer.
     */
    @XmlElement(name = "ItemCount")
    private int itemCount;

    /**
     * The private member variable that holds the line items
     * of the customer.
     */
    @XmlElement(name = "LineItem")
    private List<LineItem> lineItems;

    /**
     * The private member variable that holds the pending.
     */
    @XmlElement(name = "Pending")
    private Pending pending;

    /**
     * The private member variable that holds the transaction link list.
     */
    @XmlElement(name = "TransactionLink")
    private List<TransactionLink> transactionLink;

    /**
     * The private member variable that holds the SlipNo.
     */
    @XmlElement(name = "SlipNo")
    private String slipNo;

    /**
     * The private member variable that holds the DeliveryRetailStoreId.
     */
    @XmlElement(name = "DeliveryRetailStoreId")
    private String deliveryRetailStoreId;

    /**
     * The private member variable that holds the voider workstation sub ID
     * of the customer.
     */
    @XmlElement(name = "VoiderWorkstationSubID")
    private String voidWorkstationSubID;

    /**
     * The private member variable that holds the voider operator ID
     * of the customer.
     */
    @XmlElement(name = "VoiderOperatorID")
    private String voidOperatorID;

    /**
     * The private member variable that holds the Customer's Demographic
     * information.
     */
    @XmlElement(name = "Customer")
    private Customer customer;

    /**
     * The private member variable that holds the ReturnFlag
     */
    @XmlElement(name = "ReturnFlag")
    private String returnFlag;

    /**
     * The private member variable that holds the AutoGenerationFlag
     */
    @XmlElement(name = "AutoGenerationFlag")
    private String autoGenerationFlag;

    /**
     * The private member variable that holds the AutoGenerationSequenceNumber
     */
    @XmlElement(name = "AutoGenerationSequenceNumber")
    private String autoGenerationSequenceNumber;

    /**
     * The private member variable that holds the PointsLater
     */
    @XmlElement(name = "PointsLater")
    private int pointsLater;

    /**
     * The private member variable that holds the Associate
     */
    @XmlElement(name = "Associate")
    private Associate associate;

    /**
     * The private member variable that holds the ChargeSales
     */
    @XmlElement(name = "ChargeSales")
    private ChargeSales chargeSales;

    /**
     * The private member variable that holds the TeamOrder
     */
    @XmlElement(name = "TeamOrder")
    private String teamOrder;

    /**
     * The private member variable that holds the PurSubsidy
     */
    @XmlElement(name = "PurSubsidy")
    private int purSubsidy;

    /**
     * The private member variable that holds the AllPointExp
     */
    @XmlElement(name = "AllPointExp")
    private String allPointExp;

    /**
     * The private member variable that holds the Nationality
     */
    @XmlElement(name = "Nationality")
    private String nationality;

    /**
     * The private member variable that holds the tax free mode
     */
    @XmlElement(name = "TaxFreeMode")
    private String taxFreeMode;

    /**
     * The private member variable that holds the loyalty account
     */
    @XmlElement(name = "LoyaltyAccount")
    private LoyaltyAccount loyaltyAccount;

    /**
     * The private member variable that holds the taxRate type
     */
    @XmlElement(name = "TaxRateType")
    private String taxRateType;

    /**
     * The private member variable that holds the Total
     * information.
     */
    @XmlElement(name = "Total")
    private List<Total> total;

    /**
     * The private member variable that holds
     * RetailTransaction node specific reason.
     */
    @XmlElement(name = "Reason")
    private String reason;

    /**
     * barReturnReason
     */
    @XmlElement(name = "barReturnReason")
    private String barReturnReason;

    /**
     * barEmployee
     */
    @XmlElement(name = "barEmployee")
    private BarEmployee barEmployee;
    /**
     * barLoyaltyReward
     */
    @XmlElement(name = "barLoyaltyReward")
    private BarLoyaltyReward barLoyaltyReward;

    /**
     * The Getter method for Total.
     * @return  The Total.
     */
    public final List<Total> getTotal() {
        return total;
    }
    /**
     * The Setter method for Total.
     * @param total  The Total.
     */
    public final void setTotal(List<Total> total) {
        this.total = total;
    }
    /**
     * The Getter method for Customer.
     * @return  The Customer.
     */
    public final Customer getCustomer() {
        return customer;
    }
    /**
     * The Setter method for Customer.
     * @param customerToSet  The new customer.
     */
    public final void setCustomer(final Customer customerToSet) {
        this.customer = customerToSet;
    }
    /**
     * Gets the transaction link.
     *
     * @return        Returns the transaction link list.
     */
    public final List<TransactionLink> getTransactionLink() {
        return transactionLink;
    }

    /**
     * Sets the transaction link.
     *
     * @param transactionLinkToSet      The new value for the transaction link list.
     */
    public final void setTransactionLink(
            final List<TransactionLink> transactionLinkToSet) {
        this.transactionLink = transactionLinkToSet;
    }

    /**
     * @return the pending
     */
    public Pending getPending() {
        return pending;
    }
    /**
     * @param pending the pending to set
     */
    public void setPending(Pending pending) {
        this.pending = pending;
    }
    /**
     * The default constructor for RetailTransaction class.
     */
    public RetailTransaction() {
    }

    /**
     * Gets the LineItems in List.
     *
     * @return        The lineItems of a customer order transaction.
     */
    public final List<LineItem> getLineItems() {
        if (lineItems == null) {
            return Collections.emptyList();
        }
        return lineItems;
    }

    /**
     * Sets the LineItems in List.
     *
     * @param lineItemsToSet        The new value for LineItems in List.
     */
    public final void setLineItems(final List<LineItem> lineItemsToSet) {
        this.lineItems = lineItemsToSet;
    }

    /**
     * Sets the qr promotion info.
     *
     * @param qrPromotionInfoToSet      The new value for qrPromotionInfot.
     */
    public final void setQrPromotionInfo(QRPromotionInfo qrPromotionInfoToSet) {
        this.qrPromotionInfo = qrPromotionInfoToSet;
    }

    /**
     * Gets the qr promotion info.
     *
     * @return        The qr promotion info.
     */
    public final QRPromotionInfo getQrPromotionInfo() {
        return qrPromotionInfo;
    }

    /**
     * Sets the item count of a customer order transaction.
     *
     * @param itemCountToSet      The new value for item count.
     */
    public final void setItemCount(final int itemCountToSet) {
        this.itemCount = itemCountToSet;
    }

    /**
     * Gets the item count in a customer order transaction.
     *
     * @return        The item count in customer order transaction.
     */
    public final int getItemCount() {
        return itemCount;
    }

    /**
     * Sets the version of the customer order transaction.
     *
     * @param versionToSet    The new value for version in
     *                          customer order transaction.
     */
    public final void setVersion(final String versionToSet) {
        this.version = versionToSet;
    }

    /**
     * Gets the version of a customer order transaction.
     *
     * @return        The version of customer order transaction.
     */
    public final String getVersion() {
        return version;
    }

    /**
     * Sets the workstation sub ID of the Transaction.
     *
     * @param voidWorkstationSubIDToSet  The new value for the void
     *                                     workstation sub ID
     *                                     of the Transaction.
     */
    public final void setVoidWorkstationSubID(
            final String voidWorkstationSubIDToSet) {
        this.voidWorkstationSubID = voidWorkstationSubIDToSet;
    }

    /**
     * Gets the voider workstation sub ID of the Transaction.
     *
     * @return        The voider workstation sub ID of the Transaction.
     */
    public final String getVoidWorkstationSubID() {
        return voidWorkstationSubID;
    }

    /**
     * Sets the voider OperatorID of the Transaction.
     *
     * @param voidOperatorIDToSet      The new value for the voider OperatorID
     *                                  of the Transaction.
     */
    public final void setVoidOperatorID(final String voidOperatorIDToSet) {
        this.voidOperatorID = voidOperatorIDToSet;
    }

    /**
     * Gets the voider OperatorID of the Transaction.
     *
     * @return        The voider OperatorID of the Transaction.
     */
    public final String getVoidOperatorID() {
        return voidOperatorID;
    }

    /**
     * Sets the transaction status of the customer order transaction.
     *
     * @param transactionStatusToSet   The new value for transaction status in
     *                                 customer order transaction.
     */
    public final void setTransactionStatus(
            final String transactionStatusToSet) {
        this.transactionStatus = transactionStatusToSet;
    }

    /**
     * Gets the transaction status of a customer order transaction.
     *
     * @return        The transaction status of customer order transaction.
     */
    public final String getTransactionStatus() {
        return transactionStatus;
    }

    /**
     * Sets the transaction status of the customer order transaction.
     *
     * @param transactionStatusToSet   The new value for transaction status in
     *                                 customer order transaction.
     */
    public final void setOutsideSalesFlag(
            final Boolean outsideSalesFlagToSet) {
        this.outsideSalesFlag = outsideSalesFlagToSet;
    }

    /**
     * Gets the transaction status of a customer order transaction.
     *
     * @return        The transaction status of customer order transaction.
     */
    public final Boolean getOutsideSalesFlag() {
        return outsideSalesFlag;
    }

    /**
     * Sets the transaction status of the customer order transaction.
     *
     * @param transactionStatusToSet   The new value for transaction status in
     *                                 customer order transaction.
     */
    public final void setTypeCode(
            final String typeCodeToSet) {
        this.typeCode = typeCodeToSet;
    }

    /**
     * Gets the transaction status of a customer order transaction.
     *
     * @return        The transaction status of customer order transaction.
     */
    public final String getTypeCode() {
        return typeCode;
    }

    /**
     * @return the layawayFlag
     */
    public String getLayawayFlag() {
        return layawayFlag;
    }
    /**
     * @param layawayFlag the layawayFlag to set
     */
    public void setLayawayFlag(final String layawayFlag) {
        this.layawayFlag = layawayFlag;
    }
    /**
     * Sets the voided retail store id of transaction link.
     *
     * @param voidRetailStoreIDToSet   The new value for the voided retail
     *                                 store id in transaction link.
     */
    public final void setVoidRetailStoreID(
            final String voidRetailStoreIDToSet) {
        this.voidRetailStoreID = voidRetailStoreIDToSet;
    }

    /**
     * Gets the voided retail store id of transaction link.
     *
     * @return        The voided retail store id of transaction link.
     */
    public final String getVoidRetailStoreID() {
        return voidRetailStoreID;
    }

    /**
     * Sets the voided workstation id in transaction link.
     *
     * @param voidWorkstationIDToSet  The new value for
     *                                voided workstation id in
     *                                transaction link.
     */
    public final void setVoidWorkstationID(
            final String voidWorkstationIDToSet) {
        this.voidWorkstationID = voidWorkstationIDToSet;
    }

    /**
     * Gets the voided workstation id in transaction link.
     *
     * @return        The voided workstation id in transaction link.
     */
    public final String getVoidWorkstationID() {
        return voidWorkstationID;
    }

    /**
     * Sets the voided sequence number in transaction link.
     *
     * @param voidSequenceNumberToSet     The new value for the voided
     *                                     workstation id in transaction link.
     */
    public final void setVoidSequenceNumber(
            final String voidSequenceNumberToSet) {
        this.voidSequenceNumber = voidSequenceNumberToSet;
    }

    /**
     * Gets the voided sequence number in transaction link.
     *
     * @return        The voided sequence number in transaction link.
     */
    public final String getVoidSequenceNumber() {
        return voidSequenceNumber;
    }

    /**
     * Sets the voided business day date in transaction link.
     *
     * @param voidBusinessDayDateToSet    The new value for the voided business
     *                                     day    date in transaction link.
     */
    public final void setVoidBusinessDayDate(
            final String voidBusinessDayDateToSet) {
        this.voidBusinessDayDate = voidBusinessDayDateToSet;
    }

    /**
     * Gets the voided business day date in transaction link.
     *
     * @return        The voided business day date in transaction link.
     */
    public final String getVoidBusinessDayDate() {
        return voidBusinessDayDate;
    }

    /**
     * Sets the begin date time in transaction link.
     *
     * @param voidBeginDateTimeToSet   The new value for the voided begin
     *                                 date time in transaction link.
     */
    public final void setVoidBeginDateTime(
            final String voidBeginDateTimeToSet) {
        this.voidBeginDateTime = voidBeginDateTimeToSet;
    }

    /**
     * Gets the voided begin date time in transaction link.
     *
     * @return        The voided begin date time in transaction link.
     */
    public final String getVoidBeginDateTime() {
        return voidBeginDateTime;
    }

    /**
     * Sets the reason of the customer order transaction.
     *
     * @param reasonToSet    The new value for reason in
     *                          customer order transaction.
     */
    public final void setReason(final String reasonToSet) {
        this.reason = reasonToSet;
    }

    /**
     * Gets the reason of a customer order transaction.
     *
     * @return        The reason of customer order transaction.
     */
    public final String getReason() {
        return reason;
    }

    /**
     * Override the toString() Method.
     * @return The String representation of CustomeOrderTransaction.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("ItemCount : ").append(this.itemCount).append(crlf)
           .append("TransactionStatus : ").append(this.transactionStatus)
           .append(crlf)
           .append("Version : ").append(this.version).append(crlf)
           .append("VoidBeginDateTime : ").append(this.voidBeginDateTime)
           .append(crlf)
           .append("VoidBusinessDayDate : ").append(this.voidBusinessDayDate)
           .append(crlf)
           .append("VoidOperatorID : ").append(this.voidOperatorID)
           .append(crlf)
           .append("VoidRetailStoreID : ").append(this.voidRetailStoreID)
           .append(crlf)
           .append("VoidSequenceNumber : ").append(this.voidSequenceNumber)
           .append(crlf)
           .append("VoidWorkstationID : ").append(this.voidWorkstationID)
           .append(crlf)
           .append("VoidWorkstationSubID : ").append(this.voidWorkstationSubID)
           .append(crlf);

        if (null != this.transactionLink) {
            str.append("TransactionLink : ")
            .append(this.transactionLink.toString());
        }

        if (null != this.lineItems) {
            for (LineItem line : lineItems) {
                str.append(crlf)
                   .append("LineItem : ").append(line.toString());
            }
        }
        return str.toString();
    }
    /**
     * @return the priceDerivationResult List
     */
    public final List<PriceDerivationResult> getPriceDerivationResult() {
        return priceDerivationResult;
    }
    /**
     * @param priceDerivationResultToSet the List
     * of priceDerivationResult to set
     */
    public final void setPriceDerivationResult(
            final List<PriceDerivationResult> priceDerivationResultToSet) {
        this.priceDerivationResult = priceDerivationResultToSet;
    }
    /**
     * @return barReturnReason
     */
    public String getBarReturnReason() {
        return barReturnReason;
    }
    /**
     * @param barReturnReason セットする barReturnReason
     */
    public void setBarReturnReason(String barReturnReason) {
        this.barReturnReason = barReturnReason;
    }
    /**
     * @return barEmployee
     */
    public BarEmployee getBarEmployee() {
        return barEmployee;
    }
    /**
     * @param barEmployee セットする barEmployee
     */
    public void setBarEmployee(BarEmployee barEmployee) {
        this.barEmployee = barEmployee;
    }
    /**
     * @return barLoyaltyReward
     */
    public BarLoyaltyReward getBarLoyaltyReward() {
        return barLoyaltyReward;
    }
    /**
     * @param barLoyaltyReward セットする barLoyaltyReward
     */
    public void setBarLoyaltyReward(BarLoyaltyReward barLoyaltyReward) {
        this.barLoyaltyReward = barLoyaltyReward;
    }
    /**
     * @return the returnFlag
     */
    public String getReturnFlag() {
        return returnFlag;
    }
    /**
     * @param returnFlag the returnFlag to set
     */
    public void setReturnFlag(String returnFlag) {
        this.returnFlag = returnFlag;
    }
    /**
     * @return the autoGenerationFlag
     */
    public String getAutoGenerationFlag() {
        return autoGenerationFlag;
    }
    /**
     * @param autoGenerationFlag the autoGenerationFlag to set
     */
    public void setAutoGenerationFlag(String autoGenerationFlag) {
        this.autoGenerationFlag = autoGenerationFlag;
    }
    /**
     * @return the autoGenerationSequenceNumber
     */
    public String getAutoGenerationSequenceNumber() {
        return autoGenerationSequenceNumber;
    }
    /**
     * @param autoGenerationSequenceNumber the autoGenerationSequenceNumber to set
     */
    public void setAutoGenerationSequenceNumber(String autoGenerationSequenceNumber) {
        this.autoGenerationSequenceNumber = autoGenerationSequenceNumber;
    }
    /**
     * @return the pointsLater
     */
    public final int getPointsLater() {
        return pointsLater;
    }
    /**
     * @param pointsLater the pointsLater to set
     */
    public final void setPointsLater(final int pointsLater) {
        this.pointsLater = pointsLater;
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
     * @return the chargeSales
     */
    public ChargeSales getChargeSales() {
        return chargeSales;
    }
    /**
     * @param chargeSales the chargeSales to set
     */
    public void setChargeSales(ChargeSales chargeSales) {
        this.chargeSales = chargeSales;
    }
    /**
     * @return the teamOrder
     */
    public final String getTeamOrder() {
        return teamOrder;
    }
    /**
     * @param teamOrder the teamOrder to set
     */
    public final void setTeamOrder(String teamOrder) {
        this.teamOrder = teamOrder;
    }
    /**
     * @return the purSubsidy
     */
    public final int getPurSubsidy() {
        return purSubsidy;
    }
    /**
     * @param purSubsidy the purSubsidy to set
     */
    public final void setPurSubsidy(int purSubsidy) {
        this.purSubsidy = purSubsidy;
    }
    /**
     * @return the allPointExp
     */
    public final String getAllPointExp() {
        return allPointExp;
    }
    /**
     * @param allPointExp the allPointExp to set
     */
    public final void setAllPointExp(String allPointExp) {
        this.allPointExp = allPointExp;
    }
    /**
     * @return the nationality
     */
    public final String getNationality() {
        return nationality;
    }
    /**
     * @param nationality the nationality to set
     */
    public final void setNationality(String nationality) {
        this.nationality = nationality;
    }
    /**
     * @return the taxFreeMode
     */
    public String getTaxFreeMode() {
        return taxFreeMode;
    }
    /**
     * @param taxFreeMode the taxFreeMode to set
     */
    public void setTaxFreeMode(String taxFreeMode) {
        this.taxFreeMode = taxFreeMode;
    }
    /**
     * @return the loyaltyAccount
     */
    public LoyaltyAccount getLoyaltyAccount() {
        return loyaltyAccount;
    }
    /**
     * @param loyaltyAccount the loyaltyAccount to set
     */
    public void setLoyaltyAccount(LoyaltyAccount loyaltyAccount) {
        this.loyaltyAccount = loyaltyAccount;
    }
    /**
     * @return the taxRateType
     */
    public String getTaxRateType() {
        return taxRateType;
    }
    /**
     * @param taxRateType the taxRateType to set
     */
    public void setTaxRateType(String taxRateType) {
        this.taxRateType = taxRateType;
    }
    /**
     * @return the slipNo
     */
    public String getSlipNo() {
        return slipNo;
    }
    /**
     * @param slipNo the slipNo to set
     */
    public void setSlipNo(String slipNo) {
        this.slipNo = slipNo;
    }
    /**
     * @return the deliveryRetailStoreId
     */
    public String getDeliveryRetailStoreId() {
        return deliveryRetailStoreId;
    }
    /**
     * @param deliveryRetailStoreId the deliveryRetailStoreId to set
     */
    public void setDeliveryRetailStoreId(String deliveryRetailStoreId) {
        this.deliveryRetailStoreId = deliveryRetailStoreId;
    }
    /**
     * @return the customerId
     */
    public String getCustomerID() {
        return customerId;
    }
    /**
     * @param customerId the customerId to set
     */
    public void setCustomerID(String customerId) {
        this.customerId = customerId;
    }
}
