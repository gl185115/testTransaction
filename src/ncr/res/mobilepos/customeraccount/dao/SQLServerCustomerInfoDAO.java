/*
* Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
*
* SQLServerCustomerInfoDAO
*
* SQLServerCustomerInfoDAO is a DAO interface for Customer Account
*
* Campos, Carlos
*/
package ncr.res.mobilepos.customeraccount.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.customeraccount.model.Customer;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * A Data Access Object implementation for Customer Account.
 *
 * @see    ICustomerDAO
 */
public class SQLServerCustomerInfoDAO
extends AbstractDao implements ICustomerDAO {

    /**
     * DBManager that manages the database.
     */
    private DBManager dbManager;
    /**
     * Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * The class constructor.
     * @throws DaoException        Exception thrown when construction fails.
     */
    public SQLServerCustomerInfoDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();

        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }
    /**
     * Getter.
     * @param customerID    Customer's ID
     * @throws DaoException Exception when there is an error.
     * @return Customer information
     */
    public final Customer getCustomerByID(final String customerID)
    throws DaoException {
        return this.getCustomerByID(customerID, true);
    }

    /**
     * Getter.
     * @param customerID        Customer's ID
     * @param bPartialSearch    Boolean that holds if searching
     *                          is partial or not.
     * @throws DaoException     Exception when there is an error.
     * @return Customer's information.
     */
    public final Customer getCustomerByID(final String customerID,
            final boolean bPartialSearch) throws DaoException {

        tp.methodEnter("getCustomerByID");
        tp.println("CustomerID", customerID)
        .println("bPartialSearch", bPartialSearch);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        Customer customerInfo = new Customer();

        try {

            connection = dbManager.getConnection();
            /* Retrieves sql query statement from
             *      /resource/ncr.res.webuitools.property/sql_statements.xml
             */
            SQLStatement sqlStatement = SQLStatement.getInstance();

            String custID = customerID;

            if (bPartialSearch) {
                custID = "%" + custID + "%";
                select = connection.prepareStatement(
                         sqlStatement.getProperty("get-customer-partial"));
            } else {
                select = connection.prepareStatement(
                        sqlStatement.getProperty("get-customer-complete"));
            }

            select.setString(1, custID);
            result = select.executeQuery();
            if (result.next()) {
                    Customer tempCust = new Customer();
                    tempCust.setCustomerid(result.getString(
                            result.findColumn("CustomerId")));
                    tempCust.setCustomername(result.getString(
                            result.findColumn("CustomerName")));
                    tempCust.setDestreceipt(result.getString(
                            result.findColumn("DestReceipt")));
                    tempCust.setDiscountrate(result.getString(
                            result.findColumn("DiscountRate")));
                    tempCust.setMailaddress(result.getString(
                            result.findColumn("MailAddress")));
                    tempCust.setUpddate(result.getString(
                            result.findColumn("UpdDate")));
                    tempCust.setGrade(result.getString(
                            result.findColumn("Grade")));
                    tempCust.setPoints(result.getInt(
                            result.findColumn("Points")));
                    tempCust.setAddress(result.getString(
                            result.findColumn("Address")));
                    customerInfo = tempCust;

            } else {
                tp.println("Customer not found.");
            }
        } catch (SQLException e) {
            LOGGER.logAlert("CustAcnt",
                    "SQLServerCustomerInfo.getCustomerByID",
                    Logger.RES_EXCEP_SQL,
                    "Failed getting customer information " + e.getMessage());
            throw new DaoException(
                    "SQLException:@SQLServerCustomerInfo.getCustomerByID ", e);
        } finally {
            closeConnectionObjects(connection, select, result);
            
            tp.methodExit(customerInfo);
        }

        return customerInfo;
    }
}
