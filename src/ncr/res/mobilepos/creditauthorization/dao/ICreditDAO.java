/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * ICreditDAO
 *
 * Interface for the DAO specific to a table named TXL_CREDITLOG
 *
 * Campos, Carlos
 */

package ncr.res.mobilepos.creditauthorization.dao;

import ncr.res.mobilepos.creditauthorization.model.CreditAuthorization;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.exception.DaoException;

/**
 * ICreditDAO is a DAO interface for Credit Authorization.
 */
public interface ICreditDAO {
    /**
     * Saves the Information of Credit Authorization in the database.
     *
     * @param credit
     *            The Credit Information
     * @param creditAuthorization
     *            The Credit Authorization XML.
     * @return integer Return the number of rows affected in the database.
     * @throws DaoException
     *             Exception thrown when the process fails.
     */
    int saveCreditAuthorization(CreditAuthorization credit,
            String creditAuthorization) throws DaoException;

    /**
     * Saves the Information of the External POS Credit Authorization in the
     * database.
     *
     * @param credit
     *            The Credit Information
     * @param creditAuthorization
     *            The Credit Authorization XML
     * @return The number of rows affected in the table
     * @throws DaoException
     *             The Exception thrown when the method fails
     */
    int saveExtCreditAuthorization(CreditAuthorization credit,
            String creditAuthorization) throws DaoException;

    /**
     * Gets the credit pan.
     *
     * @param txid
     *            the txid
     * @return the credit pan
     * @throws DaoException
     *             The Exception thrown when the method fails
     */
    String getCreditPan(String txid) throws DaoException;

    /**
     * Lock Or Release A Credit Authorization Request from getting Authorize.
     *
     * @param credit
     *            The Credit under Lock/Release
     * @param isToLock
     *            The Flag that tells if Lock(true) or Release(false)
     * @return The number of rows affected
     * @throws DaoException
     *             The Exception returned when the method failed.
     */
    int lockOrReleaseCARequest(CreditAuthorization credit, boolean isToLock)
            throws DaoException;

    /**
     * Saves the Information of the External POS Credit Authorization in the
     * database.
     *
     * @param pastelPortResp
     *            The PastelPortResp object
     * @param storeId
     *            The store identifier
     * @param posterminalId
     *            The POS terminal identifier
     * @param txDate
     *            The transaction date
     * @param txId
     *            The transaction identifier
     * @return The number of rows affected in the table
     * @throws DaoException
     *             The Exception thrown when the method fails
     */
    int saveExtCreditAuthorization(PastelPortResp pastelPortResp,
            String storeId, String posterminalId, String txDate, String txId)
            throws DaoException;

}
