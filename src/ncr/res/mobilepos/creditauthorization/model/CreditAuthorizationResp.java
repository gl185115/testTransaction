/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * CreditAuthorizationResp
 *
 * Model Class for Credit Authorization Response
 *
 * Campos, Carlos
 */

package ncr.res.mobilepos.creditauthorization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * CreditAuthorizationResp Class is a Model representation to
 * CreditAuthorizationResp XML.
 *
 * CreditAuthorizationResp contains the responses of the Credit Authorization.
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name="CreditAuthorizationResp")
public class CreditAuthorizationResp extends ResultBase {

    /** The CorpID from a Credit Authorization Request. */
	private String corpid;

    /** The StoreID from a Credit Authorization Request. */
	private String storeid;

    /** The terminalid from a Credit Authorization Request. */
    private String terminalid;

    /** The transaction id from a Credit Authorization Request. */
    private String txid;

    /** The Slip Number from a Credit Authorization Request. */
    private String tillid;

    /** The Settlement Amount from a Credit Authorization Request. */
    private String amount;
    /**
     * The Status of the Credit Authorization Response.
     */
    private String status;
    /**
     * The Credit Status of the Credit Authorization response.
     */
    private String creditstatus;

    /** The Error Code of the Credit Authorization response. */
    private String errorcode;

    /** The Approval code of the Credit Authorization Response. */
    private String approvalcode;

    /** The transaction datetime. */
    private String txdatetime;

    /** The pan number of credit card. */
    private String pan;

    /** The expiration date of credit card. */
    private String expdate;

    /** The payment method. */
    private String paymentmethod;

    /** The credit company name. */
    private String creditcompanyname;

    /** The credit company code. */
    private String creditcompanycode;

    /** The trace number. */
    private String tracenum;

    /** The error message. */
    private String errormessage;

    /**
     * Gets the error message.
     *
     * @return the error message
     */
    public final String getErrormessage() {
        return errormessage;
    }

    /**
     * Sets the error message.
     *
     * @param errorMessage
     *            the new error message
     */
    public final void setErrormessage(final String errorMessage) {
        this.errormessage = errorMessage;
    }

    /**
     * Gets the trace number.
     *
     * @return the trace number
     */
    public final String getTracenum() {
        return tracenum;
    }

    /**
     * Sets the trace number.
     *
     * @param traceNum
     *            the new trace number
     */
    public final void setTracenum(final String traceNum) {
        this.tracenum = traceNum;
    }

    /**
     * Gets the credit company code.
     *
     * @return the credit company code
     */
    public final String getCreditcompanycode() {
        return creditcompanycode;
    }

    /**
     * Sets the credit company code.
     *
     * @param creditCompanyCode
     *            the new credit company code
     */
    public final void setCreditcompanycode(final String creditCompanyCode) {
        this.creditcompanycode = creditCompanyCode;
    }

    /**
     * Gets the credit company name.
     *
     * @return the credit company name
     */
    public final String getCreditcompanyname() {
        return creditcompanyname;
    }

    /**
     * Sets the credit company name.
     *
     * @param creditCompanyName
     *            the new creditcompanyname
     */
    public final void setCreditcompanyname(final String creditCompanyName) {
        this.creditcompanyname = creditCompanyName;
    }

    /**
     * Gets the payment method.
     *
     * @return the payment method
     */
    public final String getPaymentmethod() {
        return paymentmethod;
    }

    /**
     * Sets the payment method.
     *
     * @param paymentMethod
     *            the new payment method
     */
    public final void setPaymentmethod(final String paymentMethod) {
        this.paymentmethod = paymentMethod;
    }

    /**
     * Gets the expiration date.
     *
     * @return the expiration date
     */
    public final String getExpdate() {
        return expdate;
    }

    /**
     * Sets the expiration date.
     *
     * @param expirationDate
     *            the new expiration date
     */
    public final void setExpdate(final String expirationDate) {
        this.expdate = expirationDate;
    }

    /**
     * Gets the pan.
     *
     * @return the pan
     */
    public final String getPan() {
        return pan;
    }

    /**
     * Sets the pan.
     *
     * @param panNumber
     *            the new pan
     */
    public final void setPan(final String panNumber) {
        this.pan = panNumber;
    }

    /**
     * Gets the transaction datetime.
     *
     * @return the transaction datetime
     */
    public final String getTxdatetime() {
        return txdatetime;
    }

    /**
     * Sets the transaction datetime.
     *
     * @param transactionDatetime
     *            the new transaction datetime
     */
    public final void setTxdatetime(final String transactionDatetime) {
        this.txdatetime = transactionDatetime;
    }

    /**
     * Gets the CorpID from a given unsettled Credit Authorization request.
     *
     * @return the CorpID
     */
    public final String getCorpid() {
        return corpid;
    }

    /**
     * Sets the CorpID from a given unsettled Credit Authorization request.
     *
     * @param corpID
     *            the new CorpID
     */
    public final void setCorpid(final String corpID) {
        this.corpid = corpID;
    }

    /**
     * Gets the Web Service Processing status.
     *
     * @return Returns the processing status response
     */
    public final String getStatus() {
        return status;
    }

    /**
     * Get the StoreID from a given unsettled Credit Authorization request.
     *
     * @return The StoreID
     */
    public final String getStoreid() {
        return storeid;
    }

    /**
     * Sets the StoreID from a given unsettled Credit Authorization request.
     *
     * @param storeID
     *            The StoreID needs to set.
     */
    public final void setStoreid(final String storeID) {
        this.storeid = storeID;
    }

    /**
     * Gets the TerminalID from a given unsettled Credit Authorization request.
     *
     * @return The Terminal ID
     */
    public final String getTerminalid() {
        return terminalid;
    }

    /**
     * Sets the TerminalID from a given unsettled Credit Authorization request.
     *
     * @param terminalID
     *            The new value for the Terminal ID
     */
    public final void setTerminalid(final String terminalID) {
        this.terminalid = terminalID;
    }

    /**
     * Gets the TransactionID from a given unsettled Credit Authorization
     * request.
     *
     * @return the txid
     */
    public final String getTxid() {
        return txid;
    }

    /**
     * Sets the transaction ID from a given unsettled Credit Authorization
     * request.
     *
     * @param tranctionID
     *            the new Transaction ID
     */
    public final void setTxid(final String tranctionID) {
        this.txid = tranctionID;
    }

    /**
     * Gets the Slip Number from a given unsettled Credit Authorization request.
     *
     * @return The Slip Number
     */
    public final String getTillid() {
        return tillid;
    }

    /**
     * Sets the SlipNumber from a given unsettled Credit Authorization request.
     *
     * @param tillID
     *            The new value for the Slip Number
     */
    public final void setTillid(final String tillID) {
        this.tillid = tillID;
    }

    /**
     * Gets the Amount from a given unsettled Credit Authorization request.
     *
     * @return The unsettled Amount
     */
    public final String getAmount() {
        return amount;
    }

    /**
     * Sets the Amount from a given unsettled Credit Authorization request.
     *
     * @param amountToSet
     *            The unsettled Amount
     */
    public final void setAmount(final String amountToSet) {
        this.amount = amountToSet;
    }

    /**
     * Sets the Web Service Processing status.
     *
     * @param statusToSet
     *            The new value for processing status response
     */
    public final void setStatus(final String statusToSet) {
        this.status = statusToSet;
    }

    /**
     * Gets the Response code from CAFIS Center.
     *
     * @return Returns the credit status response
     */
    public final String getCreditstatus() {
        return creditstatus;
    }

    /**
     * Sets the Response code from CAFIS Center.
     *
     * @param creditStatus
     *            The new value for credit status response
     */
    public final void setCreditstatus(final String creditStatus) {
        this.creditstatus = creditStatus;
    }

    /**
     * Gets the reason code of an error.
     *
     * @return Returns the error code response
     */
    public final String getErrorcode() {
        return errorcode;
    }

    /**
     * Sets the reason code of an error.
     *
     * @param errorCode
     *            The new value for error code response
     */
    public final void setErrorcode(final String errorCode) {
        this.errorcode = errorCode;
    }

    /**
     * Gets the Approval number for CreditAuthorizationResp.
     *
     * @return Returns the approval number response
     */
    public final String getApprovalcode() {
        return approvalcode;
    }

    /**
     * Sets the Approval number for CreditAuthorizationResp.
     *
     * @param approvalCode
     *            The new value for approval number response
     */
    public final void setApprovalcode(final String approvalCode) {
        this.approvalcode = approvalCode;
    }

}
