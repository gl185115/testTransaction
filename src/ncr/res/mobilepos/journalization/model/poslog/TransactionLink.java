/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * TransactionLink
 *
 * Model Class for TransactionLink
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
 * TransactionLink Model Object.
 *
 * <P>
 * A TransactionLink Node in POSLog XML.
 *
 * <P>
 * The TransactionLink node is under RetailTransaction Node. And holds the
 * returned Transaction details under RetailTransaction.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TransactionLink")
public class TransactionLink {

    /** The private member variable that will hold the Reason Code of Return. */
    @XmlAttribute(name = "ReasonCode")
    private String reasonCode;

    /**
     * The private member variable that will hold the retail store ID of the
     * returned Transaction.
     */
    @XmlElement(name = "RetailStoreID")
    private String retailStoreID;

    /**
     * The private member variable that will hold the workstation ID of the
     * returned Transaction.
     */
    @XmlElement(name = "WorkstationID")
    private WorkstationID workStationID;

    /**
     * The private member variable that will hold the sequence number of the
     * returned Transaction.
     */
    @XmlElement(name = "SequenceNumber")
    private String sequenceNo;
    /**
     * The private member variable that will hold the operatorID
     */
    @XmlElement(name = "OperatorID")
    private String operatorID;
    /**
     * The private member variable that will hold the OperatorName
     */
    @XmlElement(name = "OperatorName")
    private String operatorName;
    /**
     * The private member variable that will hold the OperatorNameKana
     */
    @XmlElement(name = "OperatorNameKana")
    private String operatorNameKana;
    /**
     * The private member variable that will hold the business day date of the
     * returned Transaction.
     */
    @XmlElement(name = "BusinessDayDate")
    private String businessDayDate;

    /**
     * The private member variable that will hold the begin day time of the
     * returned Transaction.
     */
    @XmlElement(name = "BeginDateTime")
    private String beginDateTime;

    /**
     * The private member variable that will hold the organization hierarchy of
     * the returned Transaction.
     */
    @XmlElement(name = "OrganizationHierarchy")
    private OrganizationHierarchy organizationHierarchy;
    /**
     * kind of ID number. 前受金データの場合使用
     */
    @XmlElement(name = "InventoryReservationID")
    private String inventoryReservationID;
    /**
     * The private member variable for EODCounct
     */
    @XmlElement(name = "EODCount")
    private String eODCount;
    /**
     * The private member variable for Weather
     */
    @XmlElement(name = "Weather")
    private Weather weather;
    /**
     * The private member variable for Customer
     */
    @XmlElement(name = "Customer")
    private String customer;
    /**
     * Private member variable for Authorization
     */
    @XmlElement(name = "AuthorizationNumber")
    private String authorizationNumber;
    /**
     * Private member variable for CustomerCount
     */
    @XmlElement(name = "CustomerCount")
    private CustomerCount customerCount;
    /**
     * pivate member LatestUpdateStoreID
     */
    @XmlElement(name = "LatestUpdateStoreID")
    private String latestUdateStoreID;
    /**
     * Private member LatestUpdateBusinessDate
     */
    @XmlElement(name = "LatestUpdateBusinessDate")
    private String latestUpdateBusinessDate;
    /**
     * Private member LatestUpdateSequenceNumber
     */
    @XmlElement(name = "LatestUpdateSequenceNumber")
    private String latestUpdateSequenceNumber;
    /**
     * Private member LatestUpdateOperatorID
     */
    @XmlElement(name = "LatestUpdateOperatorID")
    private String latestUpdateOperatorID;
    /**
     * Private member LatestUpdateOperatorID
     */
    @XmlElement(name = "TotalAmount")
    private int totalAmount;
    /**
     * Private member TotalTaxAmount
     */
    @XmlElement(name = "TotalTaxAmount")
    private int totalTaxAmount;

    /**
     * return the latestUpdateOperatorID
     *
     * @return
     */
    public final String getLatestUpdateOperatorID() {
        return this.latestUpdateOperatorID;
    }

    /**
     * sets the latestUpdateOperatorID
     */
    public final void setLatestUpdateOperatorID(String str) {
        this.latestUpdateOperatorID = str;
    }

    /**
     * return the latestUpdateSequenceNumber
     *
     * @return
     */
    public final String getLatestUpdateSequenceNumber() {
        return this.latestUpdateSequenceNumber;
    }

    /**
     * sets the latestUpdateSequenceNumber
     *
     * @param str
     */
    public final void setLatestUpdateSequenceNumber(String str) {
        this.latestUpdateSequenceNumber = str;
    }

    /**
     * return the latestUpdateBusinessDate
     *
     * @return
     */
    public final String getLatestUpdateBusinessDate() {
        return this.latestUpdateBusinessDate;
    }

    /**
     * sets the latestUpdateBusinessDate
     *
     * @param str
     */
    public final void setLatestUpdateBusinessDate(String str) {
        this.latestUpdateBusinessDate = str;
    }

    /**
     * return the latestUpdateStoreID
     *
     * @return
     */
    public final String getLatestUpdateStoreID() {
        return this.latestUdateStoreID;
    }

    /**
     * sets the latestUpdateStoreId
     *
     * @param str
     */
    public final void setLatestUpdateStoreID(String str) {
        this.latestUdateStoreID = str;
    }

    /**
     * returns the customerCount variable
     *
     * @return
     */
    public final CustomerCount getCustomerCount() {
        return this.customerCount;
    }

    /**
     * sets the customerCount variable
     *
     * @param cc
     */
    public final void setCustomerCount(CustomerCount cc) {
        this.customerCount = cc;
    }

    /**
     * return the authorizationNumber
     *
     * @return
     */
    public final String getAuthorizationNumber() {
        return this.authorizationNumber;
    }

    /**
     * sets the authorizationNumber
     */
    public final void setAuthorizationNumber(String str) {
        this.authorizationNumber = str;
    }

    /**
     * @return the inventoryReservationID
     */
    public String getInventoryReservationID() {
        return inventoryReservationID;
    }

    /**
     * @param inventoryReservationID
     *            the inventoryReservationID to set
     */
    public void setInventoryReservationID(String inventoryReservationID) {
        this.inventoryReservationID = inventoryReservationID;
    }

    /**
     * Gets the Reason Code of the Transaction Link.
     *
     * @return Return if transaction is Return.
     */
    public String getReasonCode() {
        return reasonCode;
    }

    /**
     * Sets the Reason Code of the Transaction Link.
     *
     * @param reasonCodeToSet
     *            The new value for the Reason Code of the Transaction Link.
     */
    public void setReasonCode(String reasonCodeToSet) {
        this.reasonCode = reasonCodeToSet;
    }

    /**
     * Gets the Retail Store ID of the Transaction Link.
     *
     * @return The Retail Store ID.
     */
    public String getRetailStoreID() {
        return retailStoreID;
    }

    /**
     * Sets the Retail Store ID of the Transaction Link.
     *
     * @param retailStoreIDToSet
     *            The new value for the Retail Store ID of the Transaction Link.
     */
    public void setRetailStoreID(String retailStoreIDToSet) {
        this.retailStoreID = retailStoreIDToSet;
    }

    /**
     * Gets the Workstation ID of the Transaction Link.
     *
     * @return The Workstation ID.
     */
    public WorkstationID getWorkStationID() {
        return workStationID;
    }

    /**
     * Sets the Workstation ID of the Transaction Link.
     *
     * @param workStationIDToSet
     *            The new value for the Workstation ID of the Transaction Link.
     */
    public void setWorkStationID(WorkstationID workStationIDToSet) {
        this.workStationID = workStationIDToSet;
    }

    /**
     * Gets the Sequence number of the Transaction Link.
     *
     * @return The Sequence number.
     */
    public String getSequenceNo() {
        return sequenceNo;
    }

    /**
     * Sets the Sequence number of the Transaction Link.
     *
     * @param sequenceNoToSet
     *            The new value for the Sequence number of the Transaction Link.
     */
    public void setSequenceNo(String sequenceNoToSet) {
        this.sequenceNo = sequenceNoToSet;
    }

    /**
     * Gets the business day date of the Transaction Link.
     *
     * @return The business day date.
     */
    public String getBusinessDayDate() {
        return businessDayDate;
    }

    /**
     * Sets the business day date of the Transaction Link.
     *
     * @param businessDayDateToSet
     *            The new value for the business day date of the Transaction
     *            Link.
     */
    public void setBusinessDayDate(String businessDayDateToSet) {
        this.businessDayDate = businessDayDateToSet;
    }

    /**
     * Gets the begin date time of the Transaction Link.
     *
     * @return The begin date time.
     */
    public String getBeginDateTime() {
        return beginDateTime;
    }

    /**
     * Sets the begin day time of the Transaction Link.
     *
     * @param beginDateTimeToSet
     *            The new value for the begin day time of the Transaction Link.
     */
    public void setBeginDateTime(String beginDateTimeToSet) {
        this.beginDateTime = beginDateTimeToSet;
    }

    /**
     * @return the organizationHierarchy
     */
    public OrganizationHierarchy getOrganizationHierarchy() {
        return organizationHierarchy;
    }

    /**
     * @param organizationHierarchy
     *            the organizationHierarchy to set
     */
    public void setOrganizationHierarchy(final OrganizationHierarchy organizationHierarchy) {
        this.organizationHierarchy = organizationHierarchy;
    }

    /**
     * Gets the operatorID of the Transaction Link.
     *
     * @return operatorID.
     */
    public String getOperatorID() {
        return operatorID;
    }

    /**
     * Sets the operatorID of the Transaction Link.
     *
     * @param operatorID
     *            The new value for the operatorID of the Transaction Link.
     */
    public void setOperatorID(String operatorID) {
        this.operatorID = operatorID;
    }
    /**
     * Gets the operatorName of the Transaction Link.
     *
     * @return operatorName.
     */
    public String getOperatorName() {
        return operatorName;
    }

    /**
     * Sets the operatorName of the Transaction Link.
     *
     * @param operatorName
     *            The new value for the operatorName of the Transaction Link.
     */
    public void setOperatorName(String operatorName) {
        this.operatorName = operatorName;
    }
    /**
     * Gets the operatorNameKana of the Transaction Link.
     *
     * @return operatorNameKana.
     */
    public String getOperatorNameKana() {
        return operatorNameKana;
    }

    /**
     * Sets the operatorNameKana of the Transaction Link.
     *
     * @param operatorNameKana
     *            The new value for the operatorNameKana of the Transaction Link.
     */
    public void setOperatorNameKana(String operatorNameKana) {
        this.operatorNameKana = operatorNameKana;
    }
    /**
     * Gets the EODCount of the Transaction Link
     *
     * @return eODCount
     */
    public String getEODCount() {
        return eODCount;
    }

    /**
     * Sets the EODCount of the Transaction Link.
     *
     * @param eodcount
     *            The new value of eODCount.
     */
    public void setEODCount(String eodcount) {
        this.eODCount = eodcount;
    }

    /**
     * Gets the Weather value of the Transaction Link.
     *
     * @return weather
     */
    public Weather getWeather() {
        return weather;
    }

    /**
     * Sets the Weather value of the Transaction Link.
     *
     * @param weather
     *            The new value of weather
     */
    public void setWeather(Weather weather) {
        this.weather = weather;
    }

    /**
     * Gets the Customer of the Transaction Link.
     *
     * @return customer
     */
    public String getCustomer() {
        return customer;
    }

    /**
     * Sets the Customer of the Transaction Link.
     *
     * @param customer
     *            The new value of customer
     */
    public void setCustomer(String customer) {
        this.customer = customer;
    }

    /**
     * @return the totalAmount
     */
    public int getTotalAmount() {
        return totalAmount;
    }

    /**
     * @param totalAmount
     *            the totalAmount to set
     */
    public void setTotalAmount(int totalAmount) {
        this.totalAmount = totalAmount;
    }
    /**
     * @return the totalTaxAmount
     */
    public int getTotalTaxAmount() {
        return totalTaxAmount;
    }

    /**
     * @param totalTaxAmount
     *            the totalTaxAmount to set
     */
    public void setTotalTaxAmount(int totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

}
