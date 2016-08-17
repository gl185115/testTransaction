/*
* Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
*
* ICustomerDAO
*
* ICustomerDAO is a DAO interface for Customer Account
*
* Campos, Carlos
*/
package ncr.res.mobilepos.customeraccount.dao;

import ncr.res.mobilepos.customeraccount.model.Customer;
import ncr.res.mobilepos.exception.DaoException;

/**
 * ICustomerDAO is a DAO interface for Customer Account.
 */
public interface ICustomerDAO {
    /**
     * Gets a Customer Information by specifying the Customer ID.
     *
     * @param customerID    The customer id to find.
     * @return              Return the customer information.
     * @throws DaoException Exception thrown when searching for customer fails.
     * @see    Customer
     */
    Customer getCustomerByID(String customerID) throws DaoException;
    /**
     * Gets a Customer Information by specifying the Customer ID.
     *
     * @param customerID     The customer id to find.
     * @param bPartialSearch Boolean that holds if partial search or not.
     * @return               Return the customer information.
     * @throws DaoException  Exception thrown when searching for customer fails.
     * @see    Customer
     */
    Customer getCustomerByID(String customerID, boolean bPartialSearch)
                                    throws DaoException;
}
