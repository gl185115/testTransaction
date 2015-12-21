/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * TransactionInfo
 *
 * TransactionInfo is a model class for a Transaction information.
 *
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.consolidation.dao;

/**
 * TransactionInfo is a model class for a Transaction. It only holds the
 * Transaction's sequence number and POSLog xml.
 *
 * TransactionInfo is an inner class of SQLServerConsolidationDAO.
 */
public class TransactionInfo {
    /**
     * Sequence number of a transaction.
     */
    private String seqNum;
    /**
     * String representation of a POSLog XML.
     */
    private String tx;
    /**
     * Corporate identifier string.
     */
    private String txCorpId;

    /**
     * Gets poslog xml string.
     *
     * @return tx.
     */
    public final String getTx() {
        return tx;
    }

    /**
     * Sets poslog xml string.
     *
     * @param txXml
     *            The POSLog xml string to set.
     */
    public final void setTx(final String txXml) {
        this.tx = txXml;
    }

    /**
     * Sets sequence number of transaction.
     *
     * @param seqNumToSet
     *            The sequence number to set.
     */
    public final void setSeqNum(final String seqNumToSet) {
        this.seqNum = seqNumToSet;
    }

    /**
     * Gets sequence number.
     *
     * @return The seqNum.
     */
    public final String getSeqNum() {
        return seqNum;
    }

    /**
     * Sets transaction corporate id.
     *
     * @param txCorpIdToSet
     *            The corporate id to set.
     */
    public final void setTxCorpId(final String txCorpIdToSet) {
        this.txCorpId = txCorpIdToSet;
    }

    /**
     * Gets transaction corporate id.
     *
     * @return The transaction's corporate id.
     */
    public final String getTxCorpId() {
        return txCorpId;
    }

    /**
     * Retreives string representation of
     * {@link TransactionInfo} object.
     * @return String
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String spacer = ", ";
        str.append("{SeqNum : ").append(this.getSeqNum()).append(spacer)
            .append("TxCorpId : ").append(this.getTxCorpId()).append("}");
        return str.toString();
    }
}
