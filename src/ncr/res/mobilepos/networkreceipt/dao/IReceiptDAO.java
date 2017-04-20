/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D.
 *
 * IReceiptDAO
 *
 * IReceiptDAO is a DAO Interface for Receipt
 */
package ncr.res.mobilepos.networkreceipt.dao;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterInfo;

/**
 * IReceiptDAO is a DAO Interface for Receipt.
 */
public interface IReceiptDAO {

    /**
     * Get logo file path
     *
     * @param storeid
     * @return
     * @throws DaoException
     */
    String getLogoFilePath(String storeid) throws DaoException;

    /**
     * Get printer information.
     *
     * @param storeid - store id
     * @param termid - terminal id
     * @return NetPrinterInfo model
     * @throws DaoException - thrown when any exception occurs
     */
    NetPrinterInfo getPrinterInfo(String storeid, String termid)
            throws DaoException;

     /**
     * get operator name from MST_USER_CREDENTIALS.
     *
     * @param operatorNo - operator number to query
     * @return String - the operator name
     * @throws DaoException - thrown when any exception occurs
      */
     String getOperatorName(String operatorNo) throws DaoException;
}
