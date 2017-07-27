/*
 * Copyright (c) 2011,2015 NCR/JAPAN Corporation SW-R&D
 *
 * Transaction
 *
 * Model Class for Transaction
 *
 * De la Cerna, Jessel G.
 */

package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Transaction Model Object.
 *
 * <P>
 * A Transaction Node in POSLog XML.
 *
 * <P>
 * The Transaction node is under POSLog Node. And holds the transaction details
 * whether the customer is a member or not.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Transaction")
public class Transaction {

    /**
     * The attribute of Transaction if this transaction is training mode or not
     */
    @XmlAttribute(name = "TrainingModeFlag")
    private String trainingModeFlag;
    /**
     * The attribute that tells if this transaction is a cancellation or not
     */
    @XmlAttribute(name = "CancelFlag")
    private String cancelFlag;
    /**
     * The private member variable that will hold the transactionType of
     * Transaction.
     */
    @XmlElement(name = "TransactionType")
    private String transactionType;

    /**
     * The private member variable that will hold the retail store ID of
     * Transaction.
     */
    @XmlElement(name = "RetailStoreID")
    private String retailStoreID;

    /**
     * The private member variable that will hold the workstation ID of
     * Transaction.
     */
    @XmlElement(name = "WorkstationID")
    private WorkstationID workStationID;

    /**
     * The private member variable that will hold the organization hierarchy
     * under Transaction.
     */
    @XmlElement(name = "OrganizationHierarchy")
    private OrganizationHierarchy organizationHierarchy;

    /**
     * The private member variable that will hold the sequence number of
     * Transaction.
     */
    @XmlElement(name = "SequenceNumber")
    private String sequenceNo;

    /**
     * The private member variable that will hold the business day date of
     * Transaction.
     */
    @XmlElement(name = "BusinessDayDate")
    private String businessDayDate;

    /**
     * The private member variable that will hold the begin day date of
     * Transaction.
     */
    @XmlElement(name = "BeginDateTime")
    private String beginDateTime;

    /**
     * The private member variable that will hold the operator ID of
     * Transaction.
     */
    @XmlElement(name = "OperatorID")
    private OperatorID operatorID;

    /**
     * The Receipt Date Time of the transaction.
     */
    @XmlElement(name = "ReceiptDateTime")
    private String receiptDateTime;

    /**
     * The private member variable that will hold the member ID of the customer.
     */
    @XmlElement(name = "CustomerID")
    private String customerid;

    /**
     * The private member variable that will\ hold the slip number of the
     * Transaction.
     */
    @XmlElement(name = "TillID")
    private String tillID;

    /**
     * The private member variable that will\ hold the summaryReceiptReservation of the
     * Transaction.
     */
    @XmlElement(name = "SummaryReceiptReservation")
    private int summaryReceiptReservation;

    /**
     * BusinessUnit.
     */
    @XmlElement(name = "BusinessUnit")
    private BusinessUnit businessUnit;

    /**
     * The private member variable that will hold the retail under Transaction.
     */
    @XmlElement(name = "RetailTransaction")
    private RetailTransaction retailTransaction;

    /**
     * The private member variable that will hold the cash drawer transactions
     */
    @XmlElement(name = "TenderControlTransaction")
    private TenderControlTransaction tenderControlTransaction;
    /**
     * SummaryReceiptNo of SummaryReceipt
     * being printed.
     */
    @XmlElement(name = "SummaryReceiptNo")
    private ControlTransaction summaryReceiptNo;
    /**
     * ControlTransaction of SummaryReceipt details for transaction that is
     * being printed.
     */
    @XmlElement(name = "ControlTransaction")
    private ControlTransaction controlTransaction;
    /**
     * The private member variable that will hold the Transaction Link details.
     */
    @XmlElement(name = "TransactionLink")
    private TransactionLink transactionLink;

    /**
     * The private member variable that will hold the return cash flag of
     * Transaction.
     */
    @XmlElement(name = "ReturnCashFlag")
    private String returnCashFlag;

    /**
     * The private member variable that will hold the auto sequence number of
     * Transaction.
     */
    @XmlElement(name = "AutoSequenceNumber")
    private String autoSequenceNumber;

    /**
     * The private member variable that will hold the before transaction of
     * Transaction.
     */
    @XmlElement(name = "BeforeTransaction")
    private BeforeTransaction beforeTransaction;

    /**
     * The private member variable that will hold the before transaction number
     * of Transaction.
     */
    @XmlElement(name = "BeforeTransactionNumber")
    private String beforeTransactionNumber;

    /**
     * The API Error List of the transaction.
     */
    @XmlElement(name = "APIErrorList")
    private APIErrorList apiErrorList;

    /**
     * The private member variable that will hold the floorId of the Transaction.
     */
    @XmlElement(name = "FloorID")
    private String floorId;

    /**
     * Gets the ControlTransaction for SymmaryReceipt.
     *
     * @return ControlTransaction of SummaryReceipt.
     */
    public ControlTransaction getControlTransaction() {
        return controlTransaction;
    }

    /**
     * Sets the ControlTransaction for SymmaryReceipt.
     *
     * @param controlTransaction
     *            ControlTransaction of SummaryReceipt.
     */
    public void setControlTransaction(ControlTransaction controlTransaction) {
        this.controlTransaction = controlTransaction;
    }

    /**
     * Gets the training mode flag of transaction.
     *
     * @return The training mode flag of transaction
     */
    public final String getTrainingModeFlag() {
        return trainingModeFlag;
    }

    /**
     * Sets training mode flag of transaction.
     *
     * @param trainingModeFlag
     */
    public final void setTrainingModeFlag(final String trainingModeFlag) {
        this.trainingModeFlag = trainingModeFlag;
    }

    /**
     * Gets the cancel flag of transaction.
     *
     * @return The cancel flag of transaction
     */
    public final String getCancelFlag() {
        return cancelFlag;
    }

    /**
     * Sets cancel flag of transaction.
     *
     * @param cancelFlagToSet
     */
    public final void setCancelFlag(final String cancelFlagToSet) {
        this.cancelFlag = cancelFlagToSet;
    }

    /**
     * @return the transactionType
     */
    public final String getTransactionType() {
        return transactionType;
    }

    /**
     * @param transactionType
     *            the transactionType to set
     */
    public final void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    /**
     * Gets the member ID of the customer.
     *
     * @return The member ID of the customer.
     */
    public final String getCustomerid() {
        return customerid;
    }

    /**
     * Sets the member ID of the customer.
     *
     * @param customeridToSet
     *            The new value for the member ID of the customer.
     */
    public final void setCustomerid(final String customeridToSet) {
        this.customerid = customeridToSet;
    }

    /**
     * Gets the begin date time of the Transaction.
     *
     * @return The begin date time of the Transaction.
     */
    public final String getBeginDateTime() {
        return beginDateTime;
    }

    /**
     * Sets the begin date time of the Transaction.
     *
     * @param beginDateTimeToSet
     *            The new value for the begin date time of the Transaction.
     */
    public final void setBeginDateTime(final String beginDateTimeToSet) {
        this.beginDateTime = beginDateTimeToSet;
    }

    /**
     * Gets the operator ID of the Transaction.
     *
     * @return The operator ID of the Transaction.
     */
    public final OperatorID getOperatorID() {
        return operatorID;
    }

    /**
     * Sets the operator ID of the Transaction.
     *
     * @param operatorIDToSet
     *            The new value for the operator ID of the Transaction.
     */
    public final void setOperatorID(final OperatorID operatorIDToSet) {
        this.operatorID = operatorIDToSet;
    }

    /**
     * Gets the business day date of the Transaction.
     *
     * @return The business day date of the Transaction.
     */
    public final String getBusinessDayDate() {
        return businessDayDate;
    }

    /**
     * Sets the business day date of the Transaction.
     *
     * @param businessDayDateToSet
     *            The new value for the business day date of the Transaction.
     */
    public final void setBusinessDayDate(final String businessDayDateToSet) {
        this.businessDayDate = businessDayDateToSet;
    }

    /**
     * Gets the retail store id of the Transaction.
     *
     * @return The retail store id of the Transaction.
     */
    public final String getRetailStoreID() {
        return retailStoreID;
    }

    /**
     * Sets the retail store id of the Transaction.
     *
     * @param retailStoreIDToSet
     *            The new value for the retail store id of the Transaction.
     */
    public final void setRetailStoreID(final String retailStoreIDToSet) {
        this.retailStoreID = retailStoreIDToSet;
    }

    /**
     * Gets the sequence number of the Transaction.
     *
     * @return The sequence number of the Transaction.
     */
    public final String getSequenceNo() {
        return sequenceNo;
    }

    /**
     * Sets the sequence number of the Transaction.
     *
     * @param sequenceNoToSet
     *            The new value for the sequence number of the Transaction.
     */
    public final void setSequenceNo(final String sequenceNoToSet) {
        this.sequenceNo = sequenceNoToSet;
    }

    /**
     * Gets the workstation id of the Transaction.
     *
     * @return The workstation id of the Transaction.
     */
    public final WorkstationID getWorkStationID() {
        return workStationID;
    }

    /**
     * Sets the workstation id of the Transaction.
     *
     * @param workStationIDToSet
     *            The new value for the workstation id of the Transaction.
     */
    public final void setWorkStationID(final WorkstationID workStationIDToSet) {
        this.workStationID = workStationIDToSet;
    }

    /**
     * Sets the organization hieararchy under the Transaction.
     *
     * @param organizationHierarchyToSet
     *            The new value for the retail transaction under the
     *            Transaction.
     */
    public final void setOrganizationHierarchy(final OrganizationHierarchy organizationHierarchyToSet) {
        this.organizationHierarchy = organizationHierarchyToSet;
    }

    /**
     * Gets the organization hierarchy under the Transaction.
     *
     * @return The organization hierarchy under the Transaction.
     */
    public final OrganizationHierarchy getOrganizationHierarchy() {
        return organizationHierarchy;
    }

    /**
     * Sets the retail transaction under the Transaction.
     *
     * @param retailTransactionToSet
     *            The new value for the retail transaction under the
     *            Transaction.
     */
    public final void setRetailTransaction(final RetailTransaction retailTransactionToSet) {
        this.retailTransaction = retailTransactionToSet;
    }

    /**
     * Gets the retail transaction under the Transaction.
     *
     * @return The retail transaction under the Transaction.
     */
    public final RetailTransaction getRetailTransaction() {
        return retailTransaction;
    }

    /**
     * Sets the business Unit under the Transaction.
     *
     * @param businessUnit
     *            The new value for the business Unit under the Transaction.
     */
    public final void setBusinessUnit(final BusinessUnit businessUnit) {
        this.businessUnit = businessUnit;
    }

    /**
     * Gets the business Unit under the Transaction.
     *
     * @return The business Unit under the Transaction.
     */
    public final BusinessUnit getBusinessUnit() {
        return businessUnit;
    }

    /**
     * Gets the Receipt Date Time of the Transaction.
     *
     * @return The receipt date time of the Transaction.
     */
    public final String getReceiptDateTime() {
        return receiptDateTime;
    }

    /**
     * Sets the Receipt Date Time of the Transaction.
     *
     * @param receiptDateTimeToSet
     *            The Receipt Date and Time.
     */
    public final void setReceiptDateTime(final String receiptDateTimeToSet) {
        receiptDateTime = receiptDateTimeToSet;
    }

    public ControlTransaction getSummaryReceiptNo() {
		return summaryReceiptNo;
	}

	public void setSummaryReceiptNo(ControlTransaction summaryReceiptNo) {
		this.summaryReceiptNo = summaryReceiptNo;
	}

	/**
     * Set the Till ID.
     *
     * @param tillIDToSet
     *            The Till ID.
     */
    public final void setTillID(final String tillIDToSet) {
        this.tillID = tillIDToSet;
    }

    /**
     * Get the Till ID.
     *
     * @return The Till ID.
     */
    public final String getTillID() {
        return tillID;
    }

    /**
     * Set the SummaryReceiptReservation.
     *
     * @param summaryReceiptReservationToSet
     *            The SummaryReceiptReservation.
     */
    public final void setSummaryReceiptReservation(final int summaryReceiptReservation) {
        this.summaryReceiptReservation = summaryReceiptReservation;
    }

    /**
     * Get the SummaryReceiptReservation.
     *
     * @return The SummaryReceiptReservation.
     */
    public final int getSummaryReceiptReservation() {
        return summaryReceiptReservation;
    }

    /**
     * Gets the Tender Control Transaction
     *
     * @return The tender control transaction
     */
    public final TenderControlTransaction getTenderControlTransaction() {
        return tenderControlTransaction;
    }

    /**
     * Sets the Tender Control Transaction
     *
     * @param tenderControlTransaction
     *            The tender control transaction
     */
    public final void setTenderControlTransaction(TenderControlTransaction tenderControlTransaction) {
        this.tenderControlTransaction = tenderControlTransaction;
    }

    /**
     * Tells if Transaction is empty.
     *
     * @return True if transaction is empty else false.
     */
    public final boolean isEmpty() {
        if (null == beginDateTime || null == retailTransaction) {
            return true;
        }
        return false;
    }

    /**
     * Overrides to String implementation.
     *
     * @return The string implementation of the Transaction.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";
        str.append("BeginDateTime : ").append(this.beginDateTime).append(crlf).append("BusinessDayDate : ")
                .append(this.businessDayDate).append(crlf).append("CustomerID : ").append(this.customerid).append(crlf)
                .append("OperatorID : ").append(this.operatorID).append(crlf).append("ReceiptDateTime : ")
                .append(this.receiptDateTime).append(crlf).append("RetailStoreID : ").append(this.retailStoreID)
                .append(crlf).append("SequenceNumber : ").append(this.sequenceNo).append(crlf).append("TillID : ")
                .append(this.tillID).append(crlf).append("WorkstationID : ").append(this.workStationID).append(crlf);

        if (null != retailTransaction) {
            str.append("RetailTransaction : ").append(this.retailTransaction.toString());
        }

        return str.toString();
    }

    public final TransactionLink getTransactionLink() {
        return transactionLink;
    }

    public final void setTransactionLink(TransactionLink tl) {
        this.transactionLink = tl;
    }

    /**
     * @return the returnCashFlag
     */
    public final String getReturnCashFlag() {
        return returnCashFlag;
    }

    /**
     * @param returnCashFlag
     *            the returnCashFlag to set
     */
    public final void setReturnCashFlag(String returnCashFlag) {
        this.returnCashFlag = returnCashFlag;
    }

    /**
     * @return the autoSequenceNumber
     */
    public final String getAutoSequenceNumber() {
        return autoSequenceNumber;
    }

    /**
     * @param autoSequenceNumber
     *            the autoSequenceNumber to set
     */
    public final void setAutoSequenceNumber(String autoSequenceNumber) {
        this.autoSequenceNumber = autoSequenceNumber;
    }

    /**
     * @return the apiErrorList
     */
    public final APIErrorList getApiErrorList() {
        return apiErrorList;
    }

    /**
     * @param apiErrorList
     *            the apiErrorList to set
     */
    public final void setApiErrorList(APIErrorList apiErrorList) {
        this.apiErrorList = apiErrorList;
    }

    /**
     * @return the beforeTransaction
     */
    public final BeforeTransaction getBeforeTransaction() {
        return beforeTransaction;
    }

    /**
     * @param beforeTransaction
     *            the beforeTransaction to set
     */
    public final void setBeforeTransaction(BeforeTransaction beforeTransaction) {
        this.beforeTransaction = beforeTransaction;
    }

    /**
     * @return the beforeTransactionNumber
     */
    public String getBeforeTransactionNumber() {
        return beforeTransactionNumber;
    }

    /**
     * @param beforeTransactionNumber
     *            the beforeTransactionNumber to set
     */
    public void setBeforeTransactionNumber(String beforeTransactionNumber) {
        this.beforeTransactionNumber = beforeTransactionNumber;
    }

    /**
     * @return the floorId
     */
    public String getFloorId() {
        return floorId;
    }

    /**
     * @param floorId
     *            the floorId to set
     */
    public void setFloorId(String floorId) {
        this.floorId = floorId;
    }
}
