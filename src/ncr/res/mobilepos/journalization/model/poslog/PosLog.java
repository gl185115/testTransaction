/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * PosLog
 *
 * Model Class for PosLog
 *
 * De la Cerna, Jessel G.
 * Carlos G. Campos
 */

package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * PosLog Model Object.
 *
 * <P>A POSLog Node in POSLog XML.
 *
 * <P>The POSLog node mainly holds the whole details of the transaction
 * made whether a customer is a member or not.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "POSLog")
public class PosLog {

    /**
     * The private member variable that will hold the Transaction information.
     */
    @XmlElement(name = "Transaction")
    private Transaction transaction;

    /**
     * Default Constructor for PosLog.
     *
     * <P>Initializes Transaction object.
     */
    public PosLog() {
        transaction = new Transaction();
    }

    /**
     * Sets the PosLog.
     *
     * @param transactionToSet        The new value for the Transaction
     *                                   under POSLog.
     */
    public PosLog(final Transaction transactionToSet) {
        setTransaction(transactionToSet);
    }

    /**
     * Gets the Transaction.
     *
     * @return        Returns the Transaction under POSLog.
     */
    public final Transaction getTransaction() {
        return transaction;
    }

    /**
     * Sets the Transaction.
     *
     * @param transactionToSet        The new value for the Transaction
     *                                   under POSLog.
     */
    public final void setTransaction(final Transaction transactionToSet) {
        this.transaction = transactionToSet;
    }

    /**
     * Overrides the toString() method.
     * @return The String representration of POSLog.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        str.append(super.toString()).append("\r\n");

        if (null != transaction) {
            str.append("Transaction: ").append(transaction.toString());
        }

        return str.toString();
    }
}
