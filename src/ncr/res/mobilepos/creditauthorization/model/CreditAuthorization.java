/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * CreditAuthorization
 *
 * Model Class for CreditAuthorization
 *
 * Campos, Carlos
 */

package ncr.res.mobilepos.creditauthorization.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * CreditAuthorization Class is a Model representation to CreditAuthorization
 * XML.
 *
 * The CreditAuthorization XML holds the information of the Authorized Credit.
 */
@XmlRootElement(name = "CreditAuthorization")
public class CreditAuthorization {

    /** The Card's Corporate ID. */
    private String corpid;

    /** The Store ID. */
    private String storeid;

    /** The Terminal ID. */
    private String terminalid;

    /** The Transaction Number. */
    private String txid;

    /** The Transaction Transaction Date Time. */
    private String txdatetime;

    /**
     * Gets the Creation time of CA transaction.
     *
     * @return Returns the generated Transaction time
     */
    public final String getTxdatetime() {
        return txdatetime;
    }

    /**
     * Sets the Transaction generating time.
     *
     * @param txDateTime
     *            The new value for Transaction generating time
     */
    public final void setTxdatetime(final String txDateTime) {
        this.txdatetime = txDateTime;
    }

    /**
     * Gets the Card's corporate number.
     *
     * @return Returns the corporate id of CA transaction.
     */
    public final String getCorpid() {
        return corpid;
    }

    /** The Amount of Credit. */
    private double amount;

    /** The Member Number. */
    private String pan;

    /** The Cards Expiration date. */
    private String expirationdate;

    /**
     * Gets the Settlement amount.
     *
     * @return Returns the Total amount.
     */
    public final double getAmount() {
        return amount;
    }

    /**
     * Sets the Settlement amount.
     *
     * @param amountToSet
     *            The new value for amount in CreditAthorization
     */
    public final void setAmount(final double amountToSet) {
        this.amount = amountToSet;
    }

    /**
     * Gets the Membership number for CreditAuthorization.
     *
     * @return Returns the Membership number of the customer.
     */
    public final String getPan() {
        return pan;
    }

    /**
     * Sets the Membership number for CreditAuthorization.
     *
     * @param panToSet
     *            The new value for Membership number of the customer
     */
    public final void setPan(final String panToSet) {
        this.pan = panToSet;
    }

    /**
     * Gets the expiration date for CreditAuthorization.
     *
     * @return Returns the expiration date of the customer's credit card.
     */
    public final String getExpirationdate() {
        return expirationdate;
    }

    /**
     * Sets the expiration date for CreditAuthorization.
     *
     * @param expirationDate
     *            The new value for expiration date of the customer's credit
     *            card.
     */
    public final void setExpirationdate(final String expirationDate) {
        this.expirationdate = expirationDate;
    }

    /**
     * Gets the store number.
     *
     * @return Returns the store id of CA transaction.
     */
    public final String getStoreid() {
        return storeid;
    }

    /**
     * Gets the terminal number.
     *
     * @return Returns the terminal id of CA transaction.
     */
    public final String getTerminalid() {
        return terminalid;
    }

    /**
     * Gets the transaction number.
     *
     * @return Returns the transaction number for Credit, whenever it makes CA
     *         transaction, it adds one time.
     */
    public final String getTxid() {
        return txid;
    }

    /**
     * Sets the Card's corporate number.
     *
     * @param corpID
     *            The new value for corporate id of CA transaction.
     */
    public final void setCorpid(final String corpID) {
        this.corpid = corpID;
    }

    /**
     * Sets the store number.
     *
     * @param storeID
     *            The new value for store id of CA transaction.
     */
    public final void setStoreid(final String storeID) {
        this.storeid = storeID;
    }

    /**
     * Sets the terminal number.
     *
     * @param terminalID
     *            The new value for terminal id of CA transaction.
     */
    public final void setTerminalid(final String terminalID) {
        this.terminalid = terminalID;
    }

    /**
     * Sets the transaction number.
     *
     * @param txID
     *            The new value for transaction id of CA transaction.
     */
    public final void setTxid(final String txID) {
        this.txid = txID;
    }
}
