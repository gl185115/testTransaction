/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D.
 *
 * IReceiptDAO
 *
 * IReceiptDAO is a DAO Interface for Receipt
 */
package ncr.res.mobilepos.networkreceipt.dao;

import ncr.res.mobilepos.exception.DaoException;

/**
 * IReceiptDAO is a DAO Interface for Receipt.
 */
public interface IReceiptDAO {

     /**
     * get operator name from MST_USER_CREDENTIALS.
     *
     * @param operatorNo - operator number to query
     * @return String - the operator name
     * @throws DaoException - thrown when any exception occurs
      */
     String getOperatorName(String operatorNo) throws DaoException;
}
