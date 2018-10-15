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
import java.util.ArrayList;
import java.util.List;


import ncr.realgate.util.Trace;
import ncr.res.mobilepos.customeraccount.model.Customer;
import ncr.res.mobilepos.customeraccount.model.LoyaltyAccount;
import ncr.res.mobilepos.customeraccount.model.LoyaltyAccountInfo;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
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
	private final SQLStatement sqlStatement;

    /**
     * The class constructor.
     * @throws DaoException        Exception thrown when construction fails.
     */
    public SQLServerCustomerInfoDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();

        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
		this.sqlStatement = SQLStatement.getInstance();
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

	/**
	 * Gets LoyaltyAccountInfo by specifying the companyId,storeId Fuzzying the
	 * connName,connKanaName,connTel.
	 *
	 * @param companyId
	 *            The company id to find.
	 * @param storeId
	 *            The store id to find.
	 * @param connName
	 *            The connName to find.
	 * @param connKanaName
	 *            The connKanaName to find.
	 * @param connTel
	 *            The connTel to find.
	 * @return Return the LoyaltyAccountInfo.
	 * @throws DaoException
	 *             Exception thrown when searching for LoyaltyAccount fails.
	 */
	public LoyaltyAccountInfo getLoyaltyAccountInfo(String companyId, String storeId, String connName,
			String connKanaName, String connTel) throws DaoException {

		tp.methodEnter("getLoyaltyAccountList");
		tp.println("companyId", companyId).println("storeId", storeId).println("connName", connName)
				.println("connKanaName", connKanaName).println("connTel", connTel);

		Connection connection = null;
		PreparedStatement select = null;
		ResultSet result = null;
		LoyaltyAccountInfo loyaltyAccountInfo = new LoyaltyAccountInfo();
		List<LoyaltyAccount> LoyaltyAccountList = new ArrayList<LoyaltyAccount>();

		try {

			connection = dbManager.getConnection();

			StringBuffer sql = new StringBuffer();
			sql.append(" ( ");
			if (!StringUtility.isNullOrEmpty(connName)) {
				sql.append(" connName LIKE '" + connName + "%' ");
			}
			if (!StringUtility.isNullOrEmpty(connKanaName) && !StringUtility.isNullOrEmpty(connName)) {
				sql.append(" or connKanaName LIKE '" + connKanaName + "%' ");
			}
			if (!StringUtility.isNullOrEmpty(connKanaName) && StringUtility.isNullOrEmpty(connName)) {
				sql.append(" connKanaName LIKE '" + connKanaName + "%' ");
			}
			if (!StringUtility.isNullOrEmpty(connTel)
					&& (!StringUtility.isNullOrEmpty(connName) || !StringUtility.isNullOrEmpty(connKanaName))) {
				sql.append(" or connTel LIKE '" + connTel + "%' ");
			}
			if (!StringUtility.isNullOrEmpty(connTel) && StringUtility.isNullOrEmpty(connName)
					&& StringUtility.isNullOrEmpty(connKanaName)) {
				sql.append(" connTel LIKE '" + connTel + "%' ");
			}
			sql.append(" ) ");

			select = prepareStatementCountUnsendPoslog(connection, companyId, storeId, sql.toString());
			select.setString(1, companyId);
			select.setString(2, storeId);
			result = select.executeQuery();
			while (result.next()) {
				LoyaltyAccount tempLoyaltyAccount = new LoyaltyAccount();
				tempLoyaltyAccount.setConnCode(result.getString(result.findColumn("ConnCode")));
				tempLoyaltyAccount.setConnName(result.getString(result.findColumn("ConnName")));
				tempLoyaltyAccount.setConnKanaName(result.getString(result.findColumn("ConnKanaName")));
				tempLoyaltyAccount.setConnTel(result.getString(result.findColumn("ConnTel")));

				tempLoyaltyAccount.setCompanyId(result.getString(result.findColumn("CompanyId")));
				tempLoyaltyAccount.setStoreId(result.getString(result.findColumn("StoreId")));
				tempLoyaltyAccount.setOrgCode(result.getString(result.findColumn("OrgCode")));
				tempLoyaltyAccount.setConnCat(result.getInt(result.findColumn("ConnCat")));
				tempLoyaltyAccount.setConnGrp(result.getString(result.findColumn("ConnGrp")));
				tempLoyaltyAccount.setConnZip(result.getString(result.findColumn("ConnZip")));
				tempLoyaltyAccount.setConnAddr(result.getString(result.findColumn("ConnAddr")));
				tempLoyaltyAccount.setConnFax(result.getString(result.findColumn("ConnFax")));
				tempLoyaltyAccount.setConnOwner(result.getString(result.findColumn("ConnOwner")));
				tempLoyaltyAccount.setConnLimit(result.getString(result.findColumn("ConnLimit")));
				tempLoyaltyAccount.setConnCloseDate(result.getString(result.findColumn("ConnCloseDate")));
				tempLoyaltyAccount.setOnlineType(result.getInt(result.findColumn("OnlineType")));
				tempLoyaltyAccount.setSlipType(result.getInt(result.findColumn("SlipType")));
				tempLoyaltyAccount.setPaymentType(result.getInt(result.findColumn("PaymentType")));
				tempLoyaltyAccount.setCostRate(result.getInt(result.findColumn("CostRate")));
				tempLoyaltyAccount.setConnStartDate(result.getString(result.findColumn("ConnStartDate")));
				tempLoyaltyAccount.setConnEndDate(result.getString(result.findColumn("ConnEndDate")));
				tempLoyaltyAccount.setConnSubCode(result.getString(result.findColumn("ConnSubCode")));
				tempLoyaltyAccount.setSubCode1(result.getString(result.findColumn("SubCode1")));
				tempLoyaltyAccount.setSubCode2(result.getString(result.findColumn("SubCode2")));
				tempLoyaltyAccount.setSubCode3(result.getString(result.findColumn("SubCode3")));
				tempLoyaltyAccount.setSubCode4(result.getString(result.findColumn("SubCode4")));
				tempLoyaltyAccount.setSubCode5(result.getString(result.findColumn("SubCode5")));
				tempLoyaltyAccount.setSubNum1(result.getDouble(result.findColumn("SubNum1")));
				tempLoyaltyAccount.setSubNum2(result.getDouble(result.findColumn("SubNum2")));
				tempLoyaltyAccount.setSubNum3(result.getDouble(result.findColumn("SubNum3")));
				tempLoyaltyAccount.setSubNum4(result.getDouble(result.findColumn("SubNum4")));
				tempLoyaltyAccount.setSubNum5(result.getDouble(result.findColumn("SubNum5")));

				LoyaltyAccountList.add(tempLoyaltyAccount);

			}
			if (LoyaltyAccountList == null || LoyaltyAccountList.size() == 0) {
				tp.println("LoyaltyAccountInfo not found.");
			}
			loyaltyAccountInfo.setLoyaltyAccountList(LoyaltyAccountList);

		} catch (SQLException e) {

			LOGGER.logAlert("CustAcnt", "SQLServerCustomerInfo.getLoyaltyAccountList", Logger.RES_EXCEP_SQL,
					"Failed getting customer information " + e.getMessage());
			throw new DaoException("SQLException:@SQLServerCustomerInfo.getLoyaltyAccountList ", e);

		} finally {
			closeConnectionObjects(connection, select, result);

			tp.methodExit(loyaltyAccountInfo);
		}
		return loyaltyAccountInfo;
	}

	/**
	 * Populates PreparedStatement with given SQL.
	 * 
	 * @param connection
	 *            DB-connection
	 * @param checkSql
	 * @return
	 */
	private PreparedStatement prepareStatementCountUnsendPoslog(Connection connection, String companyId, String storeId,
			String sql) throws SQLException {
		// Replace %s to check sql.
		String query = String.format(sqlStatement.getProperty("get-loyalty-account"), sql, sql);
		PreparedStatement statement = connection.prepareStatement(query);
		return statement;
	}
}
