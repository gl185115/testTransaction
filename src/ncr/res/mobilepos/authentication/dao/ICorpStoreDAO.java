/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * IStoreDAO
 *
 * Is a DAO interface for Store Maintenance.
 *
 */

package ncr.res.mobilepos.authentication.dao;

import java.util.List;

import ncr.res.mobilepos.authentication.model.CorpStore;
import ncr.res.mobilepos.authentication.model.ViewCorpStore;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.model.ResultBase;


/**
 * Interface class for CorpStore maintenance DAO implementation.
 */
public interface ICorpStoreDAO {

    /**
     * Creates CorpStore.
     *
     * @param companyID company id
     * @param retailStoreID Store number
     * @param corpstore CorpStore
     * @throws DaoException if error occurred
     * @return ResultBase
     */
    ResultBase createCorpStore(
            String companyID, String retailStoreID, CorpStore corpstore)
              throws DaoException;
    /**
     * View CorpStore Details.
     *
     * @param companyid company id
     * @param retailStoreID Store's id
     * @throws DaoException if error occurred.
     * @return ViewCorpStore instance of CorpStore class.
     */
    ViewCorpStore viewCorpStore(
          String companyid, String retailStoreID) throws DaoException;

    /**
     * Delete CorpStore.
     *
     * @param companyID company id
     * @param retailStoreID Store's id
     * @throws DaoException if error occurred.
     * @return Result of the request.
     */
    ResultBase deleteCorpStore(
            String companyID, String retailStoreID) throws DaoException;

    /**
     * Update CorpStore.
     *
     * @param companyid The company id
     * @param storeid The retail storeid
     * @param corpstore The updates for corpstore.
     * @return The updated instance of CorpStore.
     * @throws DaoException The exception thrown when error occurred.
     */
    ViewCorpStore updateCorpStore(
            String companyid, String storeid, CorpStore corpstore)
    throws DaoException;
}
