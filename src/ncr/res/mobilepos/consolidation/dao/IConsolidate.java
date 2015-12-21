/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * IConsolidate
 *
 * IConsolidate is a DAO interface for Consolidating Journal Transaction
 *
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.consolidation.dao;

import java.util.List;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;

/**
 * IConsolidate is a DAO interface for Consolidating Journal Transaction.
 */
public interface IConsolidate {
    /**
     * Selects a single POSLog XML in the TXL_POSLOG. The POSLog XML retrieved
     * must not have been undergone Consolidation.
     *
     * @param seqnum
     *            The sequence number.
     * @return A list of TransactionInfo object.
     * @throws DaoException
     *             The exception thrown when the process fail.
     */
    List<TransactionInfo> selectPOSLogXML(String seqnum) throws DaoException;

    /**
     * Do the POSLog Journalization of any type of transaction.
     *
     * @param posLog
     *            The POSLog which holds the transaction to be processed.
     * @param txinfo
     *            The TransactionInfo object containing poslog xml.
     * @return Return the number of rows affected in the database.
     * @throws DaoException
     *             The exception thrown when the process fail.
     */
    int doPOSLogJournalization(PosLog posLog, TransactionInfo txinfo)
            throws DaoException;

}
