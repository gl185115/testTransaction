/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerReportDAO
 *
 * DAO which handles manipulation of tables in generating Reports
 *
 * Meneses, Chris Niven
 * Guinabo, Jefraim
 * del Rio, Rica Marie
 */
package ncr.res.mobilepos.report.dao;


import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.report.constants.ReportConstants;
import ncr.res.mobilepos.report.model.AccountancyReport;
import ncr.res.mobilepos.report.model.DailyReport;
import ncr.res.mobilepos.report.model.ReportItems;
import ncr.res.mobilepos.report.model.TotalAmount;

/**
 * A Data Access Object implementation for Report Generation.
 *
 * @see IReportDAO
 */
public class SQLServerReportDAO extends AbstractDao implements IReportDAO {

    private static final String PROG_NAME = "SQLServerReportDAO";

    /** The database manager. */
    private DBManager dbManager;

    /** The connection. */
    private Connection connection = null;

    /** The prepared statement. */
    private PreparedStatement prepdStatement = null;

    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger

    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /**
     * The Constructor of the Class.
     *
     * @throws DaoException
     *             thrown when process fails.
     */
    public SQLServerReportDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Gets the Database Manager for the Class.
     *
     * @return The Database Manager Object
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    // 1.05 2015.1.21 MAJINHUI  精算会計レポート出力を対応 add start
    /**
     * get accountacty data.
     *
     * @param storeNo
     *            the store id
     * @param tillid
     *            the till id
     * @param language
     *            the language
     * @return Object ReportItems
     * @throws DaoException
     *             thrown when process fails.
     */
    @Override
    public String getActData(String storeid, String tillid,
            String businessDayDate, String language) throws DaoException {
        String functionName = "getActData";
        tp.methodEnter(functionName);
        tp.println("storeNo", storeid).println("tillid", tillid)
                .println("businessDayDate", businessDayDate)
                .println("language", language);

        ResultSet resultSet = null;
        String dataSrc = "";
        try {
            Locale locale = null;

            if (StringUtility.isNullOrEmpty(language) || !language.equals("en")) {
                locale = Locale.JAPANESE;
            } else {
                locale = Locale.ENGLISH;
            }
            ResourceBundle rb = ResourceBundle.getBundle("label", locale);
            String statement = "get-accountancy-report";
            resultSet = executeQuery(statement, storeid, tillid,
                    businessDayDate);
            while (resultSet.next()) {
                dataSrc = dataSrc
                        + resultSet.getString(ReportConstants.COLITEMNAME)
                        + ":"
                        + rb.getString("ATYReport"
                                + resultSet
                                        .getString(ReportConstants.COLITEMNAME))
                        + ":" + resultSet.getString(ReportConstants.COLACCOUNT)
                        + ":" + resultSet.getString(ReportConstants.COLAMOUNT)
                        + ";";
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get Accountancy data.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @"
                    + "SqlServerReportDAO:getActData"
                    + " - Failed to get Accountancy data.", sqlEx);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to get Accountancy data.\n" + daoEx.getMessage());
            throw new DaoException("DaoException: @"
                    + "SqlServerReportDAO:getActData"
                    + " - Failed to get Accountancy data", daoEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Exception: @"
                    + "SqlServerReportDAO:getActData"
                    + " - Failed to get Accountancy data.", e);
        } finally {
            closeConnectionObjects(connection, prepdStatement, resultSet);
            tp.methodExit();
        }

        return dataSrc;

    }
    // 1.05 2015.1.21 MAJINHUI  精算会計レポート出力を対応 add start
    // 1.03 2014.12.8 FENGSHA レポート出力を対応 START
    /**
     * Generates Accountancy Report.
     *
     * @param storeid
     *            the store id
     * @param tillid
     *            the till id
     * @param language
     *            the language
     * @return Object ReportItems
     * @throws DaoException
     *             thrown when process fails.
     */
    @Override
    public ReportItems generateAccountancyReport(String storeid, String tillid,
            String businessDayDate, String language) throws DaoException{

        String functionName = "generateAccountancyReport";
        tp.methodEnter(functionName);
        tp.println("storeNo", storeid)
          .println("tillid", tillid)
          .println("businessDayDate", businessDayDate)
          .println("language", language);

        ResultSet resultSet = null;
        AccountancyReport accountancyreport = null;
        try {
            accountancyreport = new AccountancyReport();
            Locale locale = null;

            if (StringUtility.isNullOrEmpty(language)
                    || !language.equals("en")) {
                locale = Locale.JAPANESE;
            } else {
                locale = Locale.ENGLISH;
            }
            ResourceBundle rb = ResourceBundle.getBundle("label", locale);
            String statement = "get-accountancy-report";
            resultSet = executeQuery(statement, storeid, tillid, businessDayDate);
            while (resultSet.next()) {
                accountancyreport.addDataToATYItemName(rb.getString("ATYReport"+resultSet
                        .getString(ReportConstants.COLITEMNAME)));
                accountancyreport.addDataToATYSaleAccount(resultSet
                        .getString(ReportConstants.COLACCOUNT));
                accountancyreport.addDataToATYSaleAmount(resultSet
                        .getString(ReportConstants.COLAMOUNT));
                accountancyreport.addDateToSalesOrPay(resultSet
                        .getString(ReportConstants.SALESORPAY));
                accountancyreport.addDateToATYItem(resultSet
                        .getString(ReportConstants.COLITEMNAME));

            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate Accountancy report.\n"
                            + sqlEx.getMessage());
            throw new DaoException("SQLException: @"
                    + "SqlServerReportDAO:generateAccountancyReport"
                    + " - Failed to generate Accountancy report.", sqlEx);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to generate Accountancy report.\n" + daoEx.getMessage());
            throw new DaoException("DaoException: @"
                    + "SqlServerReportDAO:generateAccountancyReport"
                    + " - Failed to generate Accountancy report", daoEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Exception: @"
                    + "SqlServerReportDAO:getAccountancyReport"
                    + " - Failed to generate Accountancy report.", e);
        } finally {
            closeConnectionObjects(connection, prepdStatement, resultSet);
            tp.methodExit();
        }
        return accountancyreport.getReportItems();
    }

    // 1.03 2014.12.8 FENGSHA レポート出力を対応 END

    //1.02  2014.11.27   FENGSHA   売上表を対応 ADD START

    //1.02  2014.11.27   FENGSHA   売上表を対応 ADD END

    /**
     * Execute query.
     *
     * @param statement
     *            the sql statement
     * @param params
     *            the array of sql statment parameters
     * @return the result set
     * @throws DaoException
     *             thrown when process fails.
     */
    protected final ResultSet executeQuery(final String statement,
            final String... params) throws DaoException {
        String functionName = "SQLServerReportDAO.executeQuery";
        tp.methodEnter("executeQuery");
        tp.println("statement", statement);
        for (int i = 0; i < params.length; i++) {
            tp.println("param" + Integer.toString(i), params[i]);
        }

        if (dbManager == null) {
            tp.methodExit("dbmanager is null");
            return null;
        }
        ResultSet retVal = null;
        SQLStatement sqlStatement = null;

        try {
            if (connection == null
                    || connection.isClosed()) {
                connection = dbManager.getConnection();
            }

            sqlStatement = SQLStatement.getInstance();
            /* for not detail report */
            if (!statement.equals(ReportConstants.SQL_SELECTDETAILREPORT)) {
                prepdStatement = connection.prepareStatement(sqlStatement
                        .getProperty(statement));

                for (int i = 0; i < params.length; i++) {
                    prepdStatement.setString(i + 1, params[i]);
                }
            } else {
                /* for detail report */
                String statementHolder = sqlStatement.getProperty(statement);
                List<String> newParam = new ArrayList<String>(
                        Arrays.asList(params));

                /* check if device number is not null */
                if (null != params[SQLStatement.PARAM6]) {
                    // Removed the commented deviceNo in where clause
                    statementHolder = statementHolder.replace("/*2", "");
                    statementHolder = statementHolder.replace("2*/", "");
                } else {
                    newParam.remove(SQLStatement.PARAM6);
                }

                /* check if operator number is not null */
                if (null != params[SQLStatement.PARAM5]) {
                    // Removed the commented operactoreCode in where clause
                    statementHolder = statementHolder.replace("/*1", "");
                    statementHolder = statementHolder.replace("1*/", "");
                } else {
                    newParam.remove(SQLStatement.PARAM5);
                }

                prepdStatement = connection.prepareStatement(statementHolder);

                for (int i = 0; i < newParam.size(); i++) {
                    prepdStatement.setString(i + 1, newParam.get(i));
                }
            }

            retVal = prepdStatement.executeQuery();
        } catch (SQLException sqlExcp) {

            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_SQL, "Failed to execute query.\n"
                            + sqlExcp.getMessage());
            throw new DaoException("There is an Error in the SQL", sqlExcp);
        } finally {
            tp.methodExit(retVal);
        }
        return retVal;
    }

    //1.02 2014.11.27  FENGSHA  売上表を対応 ADD START
    /**
     * Get the Store name
     *
     *
     * @param storeNo
     *            the store number
     * @return the storeName String
     * @throws DaoException
     *             thrown when process fails.
     * @throws SQLStatementException
     *             thrown when SQL error occurs.
     */
    @Override
    public String findStoreName(String storeNo) throws DaoException {

        String functionName = "findStoreName";
        tp.methodEnter(functionName)
          .println("storeNo", storeNo);

        ResultSet resultSet = null;
        String storeName=null;

        try {
            if (StringUtility.isNullOrEmpty(storeNo)) {
                return storeName;
            }

            String selectStmt = "get-storeInfo";
            resultSet = executeQuery(selectStmt, storeNo);

            if (resultSet.next()) {
                storeName = resultSet.getString("storeName");
            }
        }catch (DaoException daoEx) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO, "Failed to find storeName.\n"
                            + daoEx.getMessage());
            throw new DaoException("Exception: @"
                    + "SqlServerReportDAO:findStoreName"
                    + " - Error find StoreName", daoEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate sales man report.\n"
                            + sqlEx.getMessage());
            throw new DaoException("SQLException: @"
                    + "SqlServerReportDAO:findStoreName"
                    + " - Error find StoreName", sqlEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            throw new DaoException("Exception: @"
                    + "SqlServerReportDAO:findStoreName"
                    + " - Error find StoreName", e);
        } finally {

            closeConnectionObjects(connection, prepdStatement, resultSet);
            tp.methodExit();

        }
        return storeName;
    }

    //1.02 2014.11.27  FENGSHA   売上表を対応 ADD END

    // 1.04 2014.12.29 MAJINHUI 会計レポート出力を対応 ADD START
    /*
     * Get the store tel number
     *
     * @param storeId the store number
     *
     * @return the store telephone number
     *
     * @throws DaoException thrown when process fails.
     */
    @Override
    public String getStoreTel(String storeId) throws DaoException {

        String functionName = "getStoreTel";
        tp.methodEnter(functionName);
        tp.println("storeId", storeId);

        ResultSet rs = null;
        String storeTel = null;

        try {
             if (StringUtility.isNullOrEmpty(storeId)) {
                 return storeTel;
             }

            String statement = "get-storeInfo";
            rs = executeQuery(statement, storeId);
            if (rs.next()) {
                storeTel = rs.getString("StoreTel");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get storeTel.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @"
                    + "SqlServerReportDAO:getStoreTel"
                    + " - Error get StoreTel", sqlEx);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to get storeTel.\n" + daoEx.getMessage());
            throw new DaoException("DaoException: @"
                    + "SqlServerReportDAO:getStoreTel"
                    + " -Error get StoreTel", daoEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
            throw new DaoException("Exception: @"
                    + "SqlServerReportDAO:getStoreTel"
                    + " - Error get StoreTel", e);
        } finally {
            closeConnectionObjects(connection, prepdStatement, rs);
            tp.methodExit();
        }
        return storeTel;
    }
    // 1.04 2014.12.29 MAJINHUI 会計レポート出力を対応 ADD END

    /**
     * サーバー計算した在高金額を取得.
     *
     * @param storeId
     *            the store number.
     * @param tillId
     *            the till number.
     * @param businessDate
     *            the business day date
     * @return the total amount
     * @throws DaoException
     *            thrown when process fails.
     */
    public final TotalAmount getTotalAmount(final String storeId, final String tillId,
			String businessDate)throws DaoException{

    	String functionName = "getTotalAmount";
    	tp.methodEnter(functionName);
		tp.println("storeid", storeId)
		  .println("tillId", tillId)
		  .println("businessDate", businessDate);

        ResultSet resultSet = null;
        TotalAmount totalAmount = new TotalAmount();

        try {
            String selectStmt = "get-total-amount";
            resultSet = executeQuery(selectStmt, storeId, tillId, businessDate);

            if(resultSet.next()){
            	totalAmount.setCash(resultSet.getString("Cash"));
            	totalAmount.setCredit(resultSet.getString("Credit"));
            	totalAmount.setGiftCard(resultSet.getString("GiftCard"));

            	totalAmount.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            	totalAmount.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            	totalAmount.setMessage(ResultBase.RES_SUCCESS_MSG);
            }
         } catch (SQLException sqlEx) {
             LOGGER.logAlert(
                     PROG_NAME,
                     functionName,
                     Logger.RES_EXCEP_SQL,
                     "Failed to get Total Amount.\n"
                             + sqlEx.getMessage());
             throw new DaoException("SQLException: @"
                     + "SqlServerReportDAO:" + functionName
                     + " - Failed to get Total Amount", sqlEx);
         }catch (DaoException daoEx) {
             LOGGER.logAlert(
                     PROG_NAME,
                     functionName,
                     Logger.RES_EXCEP_DAO,
                     "Failed to get Total Amount.\n"
                             + daoEx.getMessage());
             throw new DaoException("DAOException: @"
                     + "SqlServerReportDAO:" + functionName
                     + " - Failed to get Total Amount", daoEx);
         } catch (Exception e) {
             LOGGER.logAlert(PROG_NAME,
                     functionName,
                     Logger.RES_EXCEP_GENERAL,
                     "Failed to get Total Amount.\n"
                     		  + e.getMessage());
             throw new DaoException("Exception: @"
                     + "SqlServerReportDAO:" + functionName
                     + " - Failed to get Total Amount.", e);
         } finally {
             closeConnectionObjects(connection, prepdStatement, resultSet);
             tp.methodExit(totalAmount);
         }

         return totalAmount;
    }

    /* (non-Javadoc)
     * @see ncr.res.mobilepos.report.dao.IReportDAO#getDailyReportItems(java.lang.String, java.lang.String)
     */
    @Override
    public List<DailyReport> getDailyReportItems(String companyId, String storeId,
            String tillId, String businessDate, int trainingFlag) throws DaoException {

        String functionName = "getDailyReportItems";
        tp.methodEnter(functionName);
        tp.println("companyId", companyId)
          .println("storeId", storeId)
          .println("tillId", tillId)
          .println("businessDate", businessDate)
          .println("trainingFlag", trainingFlag);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;
        List<DailyReport> dailyReportItems = new ArrayList<DailyReport>();
        DailyReport dailyReport = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement.getProperty("get-report-items"));
            selectStmnt.setString(SQLStatement.PARAM1, companyId);
            selectStmnt.setString(SQLStatement.PARAM2, storeId);
            selectStmnt.setString(SQLStatement.PARAM3, tillId);
            selectStmnt.setString(SQLStatement.PARAM4, businessDate);
            selectStmnt.setInt(SQLStatement.PARAM5, trainingFlag);
            resultSet = selectStmnt.executeQuery();

            while (resultSet.next()) {
                dailyReport = new DailyReport();
                dailyReport.setCompanyId(resultSet.getString("CompanyId"));
                dailyReport.setStoreId(resultSet.getString("StoreId"));
                dailyReport.setTillId(resultSet.getString("TillId"));
                dailyReport.setDataType(resultSet.getString("DataType"));
                dailyReport.setItemLevel1(resultSet.getString("ItemLevel1"));
                dailyReport.setItemLevel2(resultSet.getString("ItemLevel2"));
                dailyReport.setItemLevel3(resultSet.getString("ItemLevel3"));
                dailyReport.setItemLevel4(resultSet.getString("ItemLevel4"));
                dailyReport.setItemName(resultSet.getString("ItemName"));
                dailyReport.setItemCount(resultSet.getInt("ItemCnt"));
                dailyReport.setItemAmt(resultSet.getInt("ItemAmt"));
                dailyReportItems.add(dailyReport);
            }
         } catch (Exception e) {
             LOGGER.logAlert(PROG_NAME,
                     functionName,
                     Logger.RES_EXCEP_GENERAL,
                     "Failed to get Daily Report items.\n"
                              + e.getMessage());
             throw new DaoException("Exception: @"
                     + "SqlServerReportDAO:" + functionName
                     + " - Failed to get Daily Report Items.", e);
         } finally {
             closeConnectionObjects(connection, prepdStatement, resultSet);
             tp.methodExit(dailyReportItems);
         }
        return dailyReportItems;
    }

}
