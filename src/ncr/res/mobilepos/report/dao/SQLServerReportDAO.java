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


import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.ResourceBundle;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.constant.TransactionVariable.TransactionCode;
import ncr.res.mobilepos.constant.TxTypes;
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
import ncr.res.mobilepos.report.model.ClerkProductReport;
import ncr.res.mobilepos.report.model.Column;
import ncr.res.mobilepos.report.model.DailyReport;
import ncr.res.mobilepos.report.model.DepartmentReport;
import ncr.res.mobilepos.report.model.DetailReport;
import ncr.res.mobilepos.report.model.Details;
import ncr.res.mobilepos.report.model.DivHourlyReport;
import ncr.res.mobilepos.report.model.DrawerFinancialReport;
import ncr.res.mobilepos.report.model.FinancialReport;
import ncr.res.mobilepos.report.model.GroupReport;
import ncr.res.mobilepos.report.model.HourlyReport;
import ncr.res.mobilepos.report.model.ItemReport;
import ncr.res.mobilepos.report.model.ReportItems;
import ncr.res.mobilepos.report.model.SalesClerkReport;
import ncr.res.mobilepos.report.model.SalesManReport;
import ncr.res.mobilepos.report.model.SalesTargetMarketReport;
import ncr.res.mobilepos.report.model.StoreReport;
import ncr.res.mobilepos.report.model.TotalAmount;
import ncr.res.mobilepos.simpleprinterdriver.NetPrinterInfo;

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
        String functionName = DebugLogger.getCurrentMethodName();
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

        String functionName = DebugLogger.getCurrentMethodName();
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

    /**
     * Generates Hourly Report.
     *
     * @param deviceNo
     *            the device number
     * @param operatorNo
     *            the operator number
     * @param businessdaydate
     *            the business day date
     * @param storeNo
     *            the store number
     * @param startTime
     *            the start time
     * @return the string
     * @throws DaoException
     *             thrown when process fails.
     */
    @Override
    public final String generateHourlyReport(final String deviceNo,
            final String operatorNo, final String businessdaydate,
            final String storeNo, final String startTime) throws DaoException {

        tp.methodEnter("generateHourlyReport");
        tp.println("deviceNo", deviceNo)
            .println("operatorNo", operatorNo)
            .println("businessdaydate", businessdaydate)
            .println("storeNo", storeNo)
            .println("startTime", startTime);

        ResultSet result = null;
        //1.02 FENGSHA 2014.11.27 売上表を対応 MOD START
        //HourlyReport report = new HourlyReport(deviceNo, operatorNo);
          HourlyReport report = new HourlyReport();
        //1.02 FENGSHA 2014.11.27 売上表を対応 MOD END
        String functionName = "SQLServerReportDAO.generateHourlyReport";
        try {
            result = executeQuery("select-acntday-hourly", deviceNo,
                    businessdaydate, storeNo);
            int storeopentime =
                Integer.valueOf(GlobalConstant.getStoreOpenTime());

            String switchTime = startTime;
            String switchTimeOnly = switchTime.replace("am", "").replace("pm",
                    "");

            int colonIndex = switchTimeOnly.indexOf(":");
            int switchTimeHour = Integer.parseInt(switchTimeOnly.substring(0,
                    colonIndex));
            int switchTimeMin = Integer.parseInt(switchTimeOnly
                    .substring(colonIndex + 1));

            Calendar swCal1 = Calendar.getInstance(Locale.ENGLISH);

            if (switchTime.contains("pm")) {
                switchTimeHour += ReportConstants.TWELVE_HOUR;
            }

            swCal1.set(Calendar.HOUR_OF_DAY, switchTimeHour);
            swCal1.set(Calendar.MINUTE, switchTimeMin);
            swCal1.set(Calendar.SECOND, 0);

            int strt = swCal1.get(Calendar.HOUR_OF_DAY);
            int cntr = storeopentime;

            int ctrBefore = storeopentime;
            String timezoneBefore = "before";
            boolean isBeforeAdded = false;
            int col2 = 0;
            int col3 = 0;
            int col4 = 0;

            if (result.getFetchSize() >= SQLResultsConstants.ONE_ROW_AFFECTED) {
                while (result.next()) {
                    if (strt < storeopentime) {
                        int timeZone = Integer.valueOf(result
                                .getString(ReportConstants.ROW_TIMEZONECODE));

                        if (timeZone < ctrBefore) {
                            tp.println("timezone is before ctrbefore");
                            if (timeZone < strt) {
                                continue;
                            }

                            col2 += result.getInt("SalesGuestCnt");
                            col3 += result.getInt("SalesItemCnt");
                            col4 += result.getInt("SalesSalesAmt");
                        } else {
                            if (!isBeforeAdded) { // Display Before transactions
                                tp.println(
                                        "timezone is not before transactions");
                                report.addDataToColumn(ReportConstants.PARAM0,
                                        timezoneBefore);
                                report.addDataToColumn(ReportConstants.PARAM1,
                                        String.valueOf(col2));
                                report.addDataToColumn(ReportConstants.PARAM2,
                                        String.valueOf(col3));
                                report.addDataToColumn(ReportConstants.PARAM3,
                                        String.valueOf(col4));
                                isBeforeAdded = true;
                            }

                            if (storeopentime < Integer
                                    .valueOf(result
                                            .getString(ReportConstants
                                                    .ROW_TIMEZONECODE))) {

                                tp.println(
                                "row is before open time");
                                while (cntr < Integer
                                        .valueOf(result
                                                .getString(ReportConstants
                                                        .ROW_TIMEZONECODE))) {
                                    report.addDataToColumn(0, cntr
                                            + ReportConstants.TIMEZONE_OCLOCK);
                                    report.addDataToColumn(ReportConstants
                                            .PARAM1, "0");
                                    report.addDataToColumn(ReportConstants
                                            .PARAM2, "0");
                                    report.addDataToColumn(ReportConstants
                                            .PARAM3, "0");
                                    cntr++;
                                }
                            }

                            report.addDataToColumn(
                                    ReportConstants.PARAM0,
                                    result.getString(ReportConstants
                                            .ROW_TIMEZONECODE)
                                            + ReportConstants.TIMEZONE_OCLOCK);
                            report.addDataToColumn(ReportConstants.PARAM1,
                                    result.getString("SalesGuestCnt"));
                            report.addDataToColumn(ReportConstants.PARAM2,
                                    result.getString("SalesItemCnt"));
                            report.addDataToColumn(ReportConstants.PARAM3,
                                    result.getString("SalesSalesAmt"));
                            cntr++;
                            storeopentime = cntr;
                        }
                    }
                }
                // If there is only transactions before storeopentime
                if (!isBeforeAdded) {
                    report.addDataToColumn(ReportConstants.PARAM0,
                            timezoneBefore);
                    report.addDataToColumn(ReportConstants.PARAM1,
                            String.valueOf(col2));
                    report.addDataToColumn(ReportConstants.PARAM2,
                            String.valueOf(col3));
                    report.addDataToColumn(ReportConstants.PARAM3,
                            String.valueOf(col4));
                    isBeforeAdded = true;
                }
            } else {
                tp.println("ResultSet is empty");
                Calendar cal = Calendar.getInstance(Locale.ENGLISH);
                int currentTime = cal.get(Calendar.HOUR_OF_DAY);

                for (cntr = storeopentime; cntr <= currentTime; cntr++) {
                    report.addDataToColumn(ReportConstants.PARAM0, cntr
                            + ReportConstants.TIMEZONE_OCLOCK);
                    report.addDataToColumn(ReportConstants.PARAM1, "0");
                    report.addDataToColumn(ReportConstants.PARAM2, "0");
                    report.addDataToColumn(ReportConstants.PARAM3, "0");
                }
            }
        } catch (NumberFormatException e) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to generate hourly report.\n" + e.getMessage());
            throw new DaoException("Abnormal operation", e);
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate hourly report.\n" + e.getMessage());
            throw new DaoException("Abnormal operation", e);
        } finally {
            closeConnectionObjects(connection, prepdStatement, result);

            tp.methodExit(report.toHTML());
        }

        return report.toHTML();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ncr.res.mobilepos.report.dao.IReportDAO#generateHourlyReportNew(java.
     * lang.String, java.lang.String, java.lang.String, java.lang.String,
     * java.lang.String)
     */
    @Override
    public final ReportItems generateHourlyReportNew(
            //1.02 FENGSHA 2014.11.27 売上表を対応 DEL START
              //final String deviceNo,final String operatorNo,
            //1.02 FENGSHA 2014.11.27 売上表を対応 DEL END
              final String businessdaydate,
              final String storeNo, final String startTime) throws DaoException {
        //1.02 FENGSHA  売上表を対応 ADD START
        String functionName = DebugLogger.getCurrentMethodName();
        //1.02 FENGSHA  売上表を対応 ADD END
        //1.02 FENGSHA  売上表を対応 MOD START
        //tp.methodEnter("generateHourlyReportNew");
        tp.methodEnter(functionName);
      //1.02 FENGSHA  売上表を対応 MOD END
        //1.02 FENGSHA 2014.11.27 売上表を対応 DEL START
        //tp.println("deviceNo", deviceNo)
            //.println("operatorNo", operatorNo)
       //1.02 FENGSHA 2014.11.27 売上表を対応 DEL END
            tp.println("businessdaydate", businessdaydate)
            .println("storeNo", storeNo)
            .println("startTime", startTime);

        ResultSet result = null;
        //1.02 FENGSHA 2014.11.27 売上表を対応 MOD START
        //HourlyReport report = new HourlyReport(deviceNo, operatorNo);
        HourlyReport report = new HourlyReport();
       //1.02 FENGSHA 2014.11.27 売上表を対応 MOD END
      //1.02 FENGSHA  売上表を対応 DEL START
        //String functionName = "SQLServerReportDAO.generateHourlyReport";
       //1.02 FENGSHA  売上表を対応 DEL END
        try {
            String selectStmt = "select-hourlyreport-bystore";
        //1.02 FENGSHA 2014.11.27 売上表を対応 DEL START
        //if (!StringUtility.isNullOrEmpty(deviceNo)) {
                //selectStmt = "select-hourlyreport-bydevice";
                //result = executeQuery(selectStmt, deviceNo,
                //        businessdaydate, storeNo);
           // } else {
        //1.02 FENGSHA 2014.11.27 売上表を対応 DEL END

       result = executeQuery(selectStmt, businessdaydate, storeNo);

       //1.02 FENGSHA 2014.11.27 売上表を対応 DEL START
            //}
       //1.02 FENGSHA 2014.11.27 売上表を対応 DEL END


            int storeopentime =
                Integer.valueOf(GlobalConstant.getStoreOpenTime());

            String switchTime = startTime;
            String switchTimeOnly = switchTime.replace("am", "").replace("pm",
                    "");

            int colonIndex = switchTimeOnly.indexOf(":");
            int switchTimeHour = Integer.valueOf(switchTimeOnly.substring(0,
                    colonIndex).trim());
            int switchTimeMin = Integer.valueOf(switchTimeOnly.substring(
                    colonIndex + 1).trim());

            Calendar swCal1 = Calendar.getInstance(Locale.ENGLISH);

            if (switchTime.contains("pm")) {
                switchTimeHour += ReportConstants.TWELVE_HOUR;
            }

            swCal1.set(Calendar.HOUR_OF_DAY, switchTimeHour);
            swCal1.set(Calendar.MINUTE, switchTimeMin);
            swCal1.set(Calendar.SECOND, 0);

            int cntr = storeopentime;
            String timezoneBefore = "before";
            boolean isBeforeAdded = false;
            int col2 = 0;
            int col3 = 0;
            int col4 = 0;

            if (result.getFetchSize() >= SQLResultsConstants.ONE_ROW_AFFECTED) {
                while (result.next()) {
                    int timeZone = Integer.valueOf(result
                            .getString(ReportConstants.ROW_TIMEZONECODE));

                    if (timeZone < storeopentime) {
                        tp.println("timezone is before opentime");
                        col2 += result.getInt("SalesGuestCnt");
                        col3 += result.getInt("SalesItemCnt");
                        col4 += result.getInt("SalesSalesAmt");
                    } else {
                        tp.println("timezone is not before opentime");
                        if (!isBeforeAdded) { // Display Before transactions
                            report.addDataToColumn(ReportConstants.PARAM0,
                                    timezoneBefore);
                            report.addDataToColumn(ReportConstants.PARAM1,
                                    String.valueOf(col2));
                            report.addDataToColumn(ReportConstants.PARAM2,
                                    String.valueOf(col3));
                            report.addDataToColumn(ReportConstants.PARAM3,
                                    String.valueOf(col4));
                            isBeforeAdded = true;
                        }

                        if (storeopentime < Integer.valueOf(result
                                .getString(ReportConstants.ROW_TIMEZONECODE))) {

                            while (cntr < Integer.valueOf(result
                                            .getString(ReportConstants
                                                    .ROW_TIMEZONECODE))) {
                                report.addDataToColumn(ReportConstants.PARAM0,
                                        cntr + ReportConstants.TIMEZONE_OCLOCK);
                                report.addDataToColumn(ReportConstants.PARAM1,
                                        "0");
                                report.addDataToColumn(ReportConstants.PARAM2,
                                        "0");
                                report.addDataToColumn(ReportConstants.PARAM3,
                                        "0");
                                cntr++;
                            }
                        }

                        report.addDataToColumn(
                                ReportConstants.PARAM0,
                                result.getString(
                                        ReportConstants.ROW_TIMEZONECODE)
                                        + ReportConstants.TIMEZONE_OCLOCK);

                        report.addDataToColumn(ReportConstants.PARAM1,
                                result.getString("SalesGuestCnt"));
                        report.addDataToColumn(ReportConstants.PARAM2,
                                result.getString("SalesItemCnt"));
                        report.addDataToColumn(ReportConstants.PARAM3,
                                result.getString("SalesSalesAmt"));
                        cntr++;
                        storeopentime = cntr;
                    }
                }
            } else {
                tp.println("ResultSet is empty");
            }
            // If there is only transactions before storeopentime
            if (!isBeforeAdded) {
                report.addDataToColumn(ReportConstants.PARAM0,
                        timezoneBefore);
                report.addDataToColumn(ReportConstants.PARAM1,
                        String.valueOf(col2));
                report.addDataToColumn(ReportConstants.PARAM2,
                        String.valueOf(col3));
                report.addDataToColumn(ReportConstants.PARAM3,
                        String.valueOf(col4));
                isBeforeAdded = true;
            }
        } catch (NumberFormatException numEx) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_PARSE,
                    "Failed to generate hourly report.\n" + numEx.getMessage());
            throw new DaoException("SQLException: @"
                    + "SqlServerReportDAO:generateHourlyReport"
                    + " - Failed to generate hourly report.",numEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate hourly report.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @"
                    + "SqlServerReportDAO:generateHourlyReport"
                    + " - Failed to generate hourly report.",sqlEx);
        }
        //1.02 FENGSHA 売上表を対応 ADD START
        catch (DaoException daoEx) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO, "Failed to generate hourly report.\n"
                            + daoEx.getMessage());
            throw new DaoException("DaoException: @"
                    + "SqlServerReportDAO:generateHourlyReport"
                    + " - Failed to generate hourly report", daoEx);
        }//1.02 FENGSHA 売上表を対応 ADD END
        catch (Exception e) {
            LOGGER.logAlert( PROG_NAME,functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            throw new DaoException("Exception: @"
                    + "SqlServerReportDAO:generateHourlyReport"
                    + " -Failed to generate hourly report.", e);
        }finally {
            closeConnectionObjects(connection, prepdStatement, result);

            tp.methodExit();
        }

        return report.getReportItems();
    }

    /**
     * Generates SalesClerk Report.
     *
     * @param deviceNo
     *            the device number
     * @param operatorNo
     *            the operator number
     * @param businessDaydate
     *            the business day date
     * @param storeNo
     *            the store number
     * @return the string
     * @throws DaoException
     *             thrown when process fails.
     */
    @Override
    public final String generateSalesClerkReport(final String deviceNo,
            final String operatorNo, final String businessDaydate,
            final String storeNo) throws DaoException {

        tp.methodEnter("generateSalesClerkReport");
        tp.println("deviceNo", deviceNo)
            .println("operatorNo", operatorNo)
            .println("businessDaydate", businessDaydate)
            .println("storeNo", storeNo);

        ResultSet resultSet = null;
        SalesClerkReport salesClerkReport = null;

        String functionName = "SQLServerReportDAO.generateSalesClerkReport";
        try {
            salesClerkReport = new SalesClerkReport(deviceNo, operatorNo);
            resultSet = executeQuery("select-salesclerk-report", deviceNo,
                    businessDaydate, storeNo);

            while (resultSet.next()) {
                salesClerkReport.addDataToOperNumber((String) resultSet
                        .getString(ReportConstants.ROW_OPERATORNUMBER));
                salesClerkReport.addDataToName((String) resultSet
                        .getString(ReportConstants.ROW_OPERATORNAME));
                salesClerkReport.addDataToSalesCustomers((String) resultSet
                        .getString(ReportConstants.ROW_SALESGUESTCNT));
                salesClerkReport.addDataToSalesItems((String) resultSet
                        .getString(ReportConstants.ROW_ITEMCNT));
                salesClerkReport.addDataToSalesAmount((String) resultSet
                        .getString(ReportConstants.ROW_SALESAMOUNT));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate sales clerk report.\n"
                            + sqlEx.getMessage());
            throw new DaoException("Abnormal operation", sqlEx);
        } finally {
            closeConnectionObjects(connection, prepdStatement, resultSet);

            tp.methodExit(salesClerkReport.toHTML());
        }

        return salesClerkReport.toHTML();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ncr.res.mobilepos.report.dao.IReportDAO#generateSalesClerkReportNew(java
     * .lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public final ReportItems generateSalesClerkReportNew(final String deviceNo,
            final String operatorNo, final String businessDaydate,
            final String storeNo) throws DaoException {

        tp.methodEnter("generateSalesClerkReportNew");
        tp.println("deviceNo", deviceNo)
            .println("operatorNo", operatorNo)
            .println("businessDaydate", businessDaydate)
            .println("storeNo", storeNo);

        ResultSet resultSet = null;
        SalesClerkReport salesClerkReport = null;
        String functionName = "SQLServerReportDAO.generateSalesClerkReport";
        try {
            salesClerkReport = new SalesClerkReport(deviceNo, operatorNo);
            String selectStmt = "select-salesclerkreport-bystore";
            if (!StringUtility.isNullOrEmpty(deviceNo)) {
                selectStmt = "select-salesclerkreport-bydevice";
                resultSet = executeQuery(selectStmt, deviceNo,
                        businessDaydate, storeNo);
            } else {
                resultSet = executeQuery(selectStmt, businessDaydate, storeNo);
            }

            while (resultSet.next()) {
                salesClerkReport.addDataToOperNumber((String) resultSet
                        .getString(ReportConstants.ROW_OPERATORNUMBER));
                salesClerkReport.addDataToName((String) resultSet
                        .getString(ReportConstants.ROW_OPERATORNAME));
                salesClerkReport.addDataToSalesCustomers((String) resultSet
                        .getString(ReportConstants.ROW_SALESGUESTCNT));
                salesClerkReport.addDataToSalesItems((String) resultSet
                        .getString(ReportConstants.ROW_ITEMCNT));
                salesClerkReport.addDataToSalesAmount((String) resultSet
                        .getString(ReportConstants.ROW_SALESAMOUNT));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate sales clerk report.\n"
                            + sqlEx.getMessage());
            throw new DaoException("Abnormal operation", sqlEx);
        } finally {
            closeConnectionObjects(connection, prepdStatement, resultSet);

            tp.methodExit();
        }

        return salesClerkReport.getReportItems();
    }

    //1.02  2014.11.27   FENGSHA   売上表を対応 ADD START
    /**
     * Get the Group Product List
     *
     * @param businessdaydate
     *            the business date
     * @param storeNo
     *            the store number
     * @return the ReportItems obj
     * @throws DaoException
     *             throw when process fails.
     */
    @Override
    public ReportItems generateGroupReportNew(String businessdaydate,
                    String storeNo)
            throws DaoException{

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("businessdaydate", businessdaydate)
          .println("storeNo", storeNo);

        ResultSet resultSet = null;
        GroupReport groupReport = null;
        try {
            groupReport = new GroupReport();
            String selectStmt = "select-groupreport";
            resultSet = executeQuery(selectStmt, businessdaydate,storeNo);
            while (resultSet.next()) {
                groupReport.addDataToGroupNum(resultSet
                        .getString(ReportConstants.GROUPID));
                groupReport.addDataGroupName(resultSet
                        .getString(ReportConstants.GROUPNAME));
                groupReport.addDataToCustomers(resultSet
                        .getString(ReportConstants.ROW_SALESGUESTCNT));
                groupReport.addDataToItems(resultSet
                        .getString(ReportConstants.ROW_ITEMCNT));
                groupReport.addDataToAmount(resultSet
                        .getString(ReportConstants.ROW_SALESAMOUNT));

            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate group report.\n"
                            + sqlEx.getMessage());
            throw new DaoException("SQLException: @"
                    + "SqlServerReportDAO:generateGroupReportNew"
                    + " - Failed to generate group report", sqlEx);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO, "Failed to generate group report.\n"
                            + daoEx.getMessage());
            throw new DaoException("DaoException: @"
                    + "SqlServerReportDAO:generateGroupReportNew"
                    + " - Failed to generate group report", daoEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            throw new DaoException("Exception: @"
                    + "SqlServerReportDAO:generateGroupReportNew"
                    + " - Failed to generate group report", e);
        } finally {
            closeConnectionObjects(connection,prepdStatement, resultSet);

            tp.methodExit();
        }
        return groupReport.getReportItems();
    }

    /**
     * Get the Store Report
     *
     * @param businessdaydate
     *            the business date
     * @param storeNo
     *            the store number
     * @return the ReportMode obj
     * @throws DaoException
     *             throw when process fails.
     */
    @Override
    public ReportItems generateStoreReportNew(final String businessdaydate,
            final String storeNo) throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("businessdaydate", businessdaydate)
          .println("storeNo", storeNo);

        ResultSet resultSet = null;
        StoreReport storereport = null;

        try {
            storereport = new StoreReport();
            String selectStmt = "get-storereport";
            resultSet = executeQuery(selectStmt, storeNo, businessdaydate);
            while (resultSet.next()) {
                storereport.addDataToStoreId(resultSet
                        .getString(ReportConstants.ROW_STORE));
                storereport.addDataToStoreName(resultSet
                        .getString(ReportConstants.ROW_STORENAME));
                storereport.addDataToCustomers(resultSet
                        .getString(ReportConstants.ROW_SALESGUESTCNT));
                storereport.addDataToItems(resultSet
                        .getString(ReportConstants.ROW_ITEMCNT));
                storereport.addDataToAmount(resultSet
                        .getString(ReportConstants.ROW_SALESAMOUNT));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate store sales report new.\n"
                            + sqlEx.getMessage());
            throw new DaoException("SQLException: @"
                    + "SqlServerReportDAO : generateStoreReportNew"
                    + " - Failed to generate store sales report", sqlEx);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to generate store sales report new.\n"
                            + daoEx.getMessage());
            throw new DaoException("DaoException: @"
                    + "SqlServerReportDAO : generateStoreReportNew"
                    + " - Failed to generate store sales report", daoEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            throw new DaoException("Exception: @"
                    + "SqlServerReportDAO : generateStoreReportNew"
                    + " - Failed to generate store sales report", e);
        } finally {
            closeConnectionObjects(connection,prepdStatement, resultSet);

            tp.methodExit();
        }
        /* Return report items */
        return storereport.getReportItems();
    }
    /**
     * Get the Item Report
     *
     * @param businessdaydate
     *            the business date
     * @param storeNo
     *            the store number
     * @param departmentNo
     *            the department number
     * @return the ReportMode obj
     *
     * @throws DaoException
     *             thrown when process fails.
     */
    @Override
    public ReportItems generateItemReportNew(final String businessdaydate,
            final String storeNo, final String departmentNo) throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("businessdaydate", businessdaydate)
          .println("storeNo", storeNo)
          .println("departmentNo", departmentNo);

        ResultSet resultSet = null;
        ItemReport itemreport = null;
        try {
            itemreport = new ItemReport();
            String selectStmt = "get-itemreport";
            resultSet = executeQuery(selectStmt, storeNo, businessdaydate,
                    departmentNo);

            while (resultSet.next()) {
                itemreport.addDataToItemId(resultSet
                        .getString(ReportConstants.ROW_ITEMCODE));
                itemreport.addDataToItemName(resultSet
                        .getString(ReportConstants.ROW_ITEMNAME));
                itemreport.addDataToCustomers(resultSet
                        .getString(ReportConstants.ROW_SALESGUESTCNT));
                itemreport.addDataToItems(resultSet
                        .getString(ReportConstants.ROW_ITEMCNT));
                itemreport.addDataToAmount(resultSet
                        .getString(ReportConstants.ROW_SALESAMOUNT));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate item sales report new.\n"
                            + sqlEx.getMessage());
            throw new DaoException("SQLException: @"
                    + "SQLServerReportDAO.generateItemReportNew"
                    + " - Failed to generate item sales report new", sqlEx);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to generate item sales report new.\n"
                            + daoEx.getMessage());
            throw new DaoException("DAOException: @"
                    + "SQLServerReportDAO.generateItemReportNew"
                    + " - Failed to generate item sales report new", daoEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            throw new DaoException("Exception: @"
                    + "SQLServerReportDAO.generateItemReportNew"
                    + " - Failed to generate item sales report new", e);
        } finally {
            closeConnectionObjects(connection, prepdStatement, resultSet);

            tp.methodExit();
        }
        /* Return report items */
        return itemreport.getReportItems();
    }
    /**
     * Get the Div Hourly Report
     *
     * @param businessdaydate
     *            the business date
     * @param storeNo
     *            the store number
     * @param departmentNo
     *            the department number
     * @return the ReportMode obj
     *
     * @throws DaoException
     *             thrown when process fails.
     */
    @Override
    public final ReportItems generateDivHourlyReportNew(
            final String businessdaydate,
            final String storeNo,
            final String departmentNo) throws DaoException {

           String functionName = DebugLogger.getCurrentMethodName();
         tp.methodEnter(functionName);
         tp.println("businessdaydate", businessdaydate)
           .println("storeNo", storeNo)
           .println("departmentNo", departmentNo);

        ResultSet result = null;
        DivHourlyReport Dhreport = new DivHourlyReport();
        try {
            String selectStmt = "select-divHourlyReport";

            result = executeQuery(selectStmt, businessdaydate, storeNo,
                    departmentNo);
            while (result.next()) {
                Dhreport.addDataToDPTHOURNum(result
                        .getString(ReportConstants.ROW_DPT));
                Dhreport.addDataToDPTHOURName(result
                        .getString(ReportConstants.ROW_DPTNAME));
                Dhreport.addDataToDPTHOURTime(result
                        .getString(ReportConstants.ROW_TIMEZONECODE)
                        + ReportConstants.TIMEZONE_OCLOCK);
                Dhreport.addDataToDPTHOURCustomers(result
                        .getString(ReportConstants.ROW_SALESGUESTCNT));
                Dhreport.addDataToDPTHOURItems(result
                        .getString(ReportConstants.ROW_SALESITEMCNT));
                Dhreport.addDataToDPTHOURAmount(result
                        .getString(ReportConstants.ROW_SALESSALESAMOUNT));
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate department and hourly report.\n"
                            + e.getMessage());
            throw new DaoException(
                    "SQLException: @"
                            + "SQLServerReportDAO.generateDivHourlyReportNew"
                            + " - Failed to generate department and hourly sales report",e);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to generate department and hourly sales report.\n"
                            + daoEx.getMessage());
            throw new DaoException(
                    "DAOException: @"
                            + "SQLServerReportDAO.generateDivHourlyReportNew"
                            + " - Failed to generate department and hourly sales report", daoEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            throw new DaoException(
                    "Exception: @"
                            + "SQLServerReportDAO.generateDivHourlyReportNew"
                            + " - Failed to generate department and hourly sales report",
                    e);
        } finally {
            closeConnectionObjects(connection,prepdStatement, result);

            tp.methodExit();
        }

        return Dhreport.getReportItems();
    }

    /**
     * Get the Target Market Report
     *
     * @param businessDaydate
     *            the business date
     * @param storeNo
     *            the store number
     * @return the ReportMode obj
     *
     * @throws DaoException
     *             throw when process fails.
     */
    @Override
    public final ReportItems generateTargetMarketReportNew(
            final String businessDaydate, final String storeNo)
            throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("businessDaydate", businessDaydate)
          .println("storeNo", storeNo);

        ResultSet resultSet = null;
        SalesTargetMarketReport salesTargetMarketReport = null;
        try {
            salesTargetMarketReport = new SalesTargetMarketReport();
            String selectStmt = "select-salesTargetMarketReport";

            resultSet = executeQuery(selectStmt, businessDaydate, storeNo);

            while (resultSet.next()) {
                salesTargetMarketReport.addDataToGuestNumber(resultSet
                        .getString(ReportConstants.ROW_GUESTCODE));
                salesTargetMarketReport.addDataToName(resultSet
                        .getString(ReportConstants.ROW_GUESTZONENAME));
                salesTargetMarketReport.addDataToSalesCustomers(resultSet
                        .getString(ReportConstants.ROW_SALESGUESTCNT));
                salesTargetMarketReport.addDataToSalesItems(resultSet
                        .getString(ReportConstants.ROW_SALESITEMCNT));
                salesTargetMarketReport.addDataToSalesAmount(resultSet
                        .getString(ReportConstants.ROW_SALESSALESAMOUNT));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate TargetMarket report.\n"
                            + sqlEx.getMessage());
            throw new DaoException("SQLException: @"
                    + "SqlServerReportDAO:generateTargetMarketReportNew"
                    + " - Failed to generate TargetMarket report",sqlEx);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to generate TargetMarket report.\n"
                            + daoEx.getMessage());
            throw new DaoException("DaoException: @"
                    + "SqlServerReportDAO:generateTargetMarketReportNew"
                    + " - Failed to generate TargetMarket report", daoEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            throw new DaoException("Exception: @"
                    + "SqlServerReportDAO:generateTargetMarketReportNew"
                    + " - Failed to generate TargetMarket report", e);
        } finally {
            closeConnectionObjects(connection,prepdStatement, resultSet);

            tp.methodExit();
        }

        return salesTargetMarketReport.getReportItems();
    }

    //1.02  2014.11.27   FENGSHA   売上表を対応 ADD END

    /**
     * Generates Department Sales Report.
     *
     * @param deviceNo
     *            the device number
     * @param operatorNo
     *            the operator number
     * @param businessdaydate
     *            the business day date
     * @param storeNo
     *            the store number
     * @return the string
     * @throws DaoException
     *             thrown when process fails
     */
    @Override
    public final String generateDepartmentReport(final String deviceNo,
            final String operatorNo, final String businessdaydate,
            final String storeNo) throws DaoException {

        tp.methodEnter("generateDepartmentReport");
        tp.println("deviceNo", deviceNo)
            .println("operatorNo", operatorNo)
            .println("businessdaydate", businessdaydate)
            .println("storeNo", storeNo);

        ResultSet resultSet = null;
        DepartmentReport dptReport = null;
        String functionName = "SQLServerReportDAO.generateDepartmentReport";
        try {
            //1.02 FENGSHA 2014.11.27 売上表を対応 MOD START
            //dptReport = new DepartmentReport(deviceNo, operatorNo);
           dptReport = new DepartmentReport();
          //1.02 FENGSHA 2014.11.27 売上表を対応 MOD END

            /*
             * Retrieve values in TXU_TOTAL_DPTSUMDAY, TXU_TOTAL_GUESTDPTDAY,
             * and MST_DPT_MASTTBL
             */
            resultSet = executeQuery("select-dptreport", deviceNo,
                    businessdaydate, storeNo);
            while (resultSet.next()) {
                dptReport.addDataToDPTNum((String) resultSet
                        .getString(ReportConstants.ROW_DPT));
                dptReport.addDataToDPTName((String) resultSet
                        .getString(ReportConstants.ROW_DPTNAME));
                dptReport.addDataToCustomers((String) resultSet
                        .getString(ReportConstants.ROW_GUESTCNT));
                dptReport.addDataToItems((String) resultSet
                        .getString(ReportConstants.ROW_ITEMCNT));
                dptReport.addDataToAmount((String) resultSet
                        .getString(ReportConstants.ROW_SALESAMOUNT));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate department sales report.\n"
                            + sqlEx.getMessage());
            throw new DaoException("Abnormal operation", sqlEx);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to generate department sales report.\n"
                            + daoEx.getMessage());
            throw new DaoException("Abnormal operation", daoEx);
        } finally {
            closeConnectionObjects(connection, prepdStatement, resultSet);

            tp.methodExit(dptReport.toHTML());
        }

        /* Return HTML formatted String Department Report */
        return dptReport.toHTML();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ncr.res.mobilepos.report.dao.IReportDAO#generateDepartmentReportNew(java
     * .lang.String, java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public final ReportItems generateDepartmentReportNew(
      //1.02 FENGSHA 2014.11.27 売上表を対応 DEL START
            //final String deviceNo,final String operatorNo,
      //1.02 FENGSHA 2014.11.27 売上表を対応 DEL END
            final String businessdaydate,
      //1.02 FENGSHA 2014.11.27 売上表を対応 ADD START
            //final String storeNo) throws DaoException {
            final String storeNo,final String departmentNo) throws DaoException {
      //1.02 FENGSHA 2014.11.27 売上表を対応 ADD END
        //1.02 FENGSHA  売上表を対応 ADD START
        String functionName = DebugLogger.getCurrentMethodName();
        //1.02 FENGSHA  売上表を対応 ADD END
        tp.methodEnter(functionName);
      //1.02 FENGSHA 2014.11.27 売上表を対応 DEL START
        //tp.println("deviceNo", deviceNo)
            //.println("operatorNo", operatorNo)
      //1.02 FENGSHA 2014.11.27 売上表を対応 DEL END
          tp.println("businessdaydate", businessdaydate)
            .println("storeNo", storeNo)
      //1.02 FENGSHA 2014.11.27 売上表を対応 ADD START
            .println("departmentNo",departmentNo);
      //1.02 FENGSHA 2014.11.27 売上表を対応 ADD END


        ResultSet resultSet = null;
        DepartmentReport dptReport = null;
      //1.02 FENGSHA 売上表を対応 DEL START
        //String functionName = "SQLServerReportDAO.generateDepartmentReport";
      //1.02 FENGSHA  売上表を対応 DEL END
        try {
            //1.02 FENGSHA 2014.11.27 売上表を対応 MOD START
            //dptReport = new DepartmentReport(deviceNo, operatorNo);
            dptReport = new DepartmentReport();
          //1.02 FENGSHA 2014.11.27 売上表を対応 MOD END
            String selectStmt = "select-dptreport-bystore";
          //1.02 FENGSHA 2014.11.27 売上表を対応 DEL START
            //if (!StringUtility.isNullOrEmpty(deviceNo)) {
                //selectStmt = "select-dptreport-bydevice";
                //resultSet = executeQuery(selectStmt, deviceNo, businessdaydate,
                          //storeNo);
               //} else {
          //1.02 FENGSHA 2014.11.27 売上表を対応 DEL END
          //1.02 FENGSHA 2014.11.27 売上表を対応 MOD START
               // resultSet = executeQuery(selectStmt, businessdaydate, storeNo);
                  resultSet = executeQuery(selectStmt,businessdaydate,
                                                      storeNo,departmentNo);
          //1.02 FENGSHA 2014.11.27 売上表を対応 MOD END
         //1.02 FENGSHA 2014.11.27 売上表を対応 DEL START
            //}
         //1.02 FENGSHA 2014.11.27 売上表を対応 DEL END
            while (resultSet.next()) {
                dptReport.addDataToDPTNum((String) resultSet
                        .getString(ReportConstants.ROW_DPT));
                dptReport.addDataToDPTName((String) resultSet
                        .getString(ReportConstants.ROW_DPTNAME));
                dptReport.addDataToCustomers((String) resultSet
                        .getString(ReportConstants.ROW_GUESTCNT));
                dptReport.addDataToItems((String) resultSet
                        .getString(ReportConstants.ROW_ITEMCNT));
                dptReport.addDataToAmount((String) resultSet
                        .getString(ReportConstants.ROW_SALESAMOUNT));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate department sales report.\n"
                            + sqlEx.getMessage());
            throw new DaoException("SQLException: @"
                    + "SQLServerReportDAO.generateDepartmentReportNew"
                    + " - Failed to generate department sales report", sqlEx);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to generate department sales report.\n"
                            + daoEx.getMessage());
            throw new DaoException("DAOException: @"
                    + "SQLServerReportDAO.generateDepartmentReportNew"
                    + " - Failed to generate department sales report", daoEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            throw new DaoException("Exception: @"
                    + "SQLServerReportDAO.generateDepartmentReportNew"
                    + " - Failed to generate department sales report", e);
        } finally {
            closeConnectionObjects(connection, prepdStatement, resultSet);

            tp.methodExit();
        }

        /* Return report items */
        return dptReport.getReportItems();
    }

    /**
     * Generates Detail Sales Report.
     *
     * @param details
     *            the details
     * @return the string
     * @throws DaoException
     *             the dao exception
     */
    @Override
    public final String generateDetailReport(final Details details)
            throws DaoException {
        String functionName = "SQLServerReportDAO.generateDetailReport";
        tp.methodEnter("generateDetailReport");
        tp.println("Details", details.toString());

        ResultSet resultSet = null;
        String dpt = details.getDepartment();
        String detailsDevNo = details.getDetailsDeviceNumber();
        String detailsOpNo = details.getDetailsOperatorNumber();
        String storeNo = details.getStoreNumber();
        String timeZoneRange = details.getTimeZone();

        SimpleDateFormat fromUser = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

        String businessDate = "";

        /* Retrieve start timeZone */
        String stTimeZone = timeZoneRange.substring(0,
                ReportConstants.HOUR_SEPARATOR_INDEX);
        String endTimeZone = timeZoneRange
                .substring(ReportConstants.HOUR_SEPARATOR_INDEX,
                        ReportConstants.MINUTE_SECOND_INDEX);

        DetailReport detailReport = null;

        try {
            businessDate = myFormat.format(fromUser.parse(details
                    .getBusinessDate()));

            if (null == detailsDevNo) {
                detailsDevNo = "";
            }

            detailReport = new DetailReport(businessDate, storeNo, dpt,
                    detailsDevNo);

            /*
             * Retrieve values in TXU_TOTAL_ACNTDAY, TXU_TOTAL_GUESTDPTDAY, and
             * MST_DPT_MASTTBL
             */
            resultSet = executeQuery("select-detailreport", businessDate,
                    storeNo, dpt, stTimeZone, endTimeZone, detailsOpNo,
                    detailsDevNo);
            int strtTime = Integer.valueOf(stTimeZone);
            int endTime = Integer.valueOf(endTimeZone);

            while (resultSet.next()) {
                int timeZone = Integer.valueOf(resultSet
                        .getString(ReportConstants.ROW_TIMEZONECODE));

                while (strtTime != timeZone && strtTime < endTime) {
                    String timeTemp = String.format("%" + 2 + "s",
                            String.valueOf(strtTime)).replace(' ', '0');
                    detailReport.addDataToTimeZone(timeTemp
                            + ReportConstants.TIMEZONE_OCLOCK);
                    detailReport.addDataToCustomers("0");
                    detailReport.addDataToItems("0");
                    detailReport.addDataToAmount("0");
                    strtTime++;
                }
                if (strtTime == timeZone) {
                    detailReport.addDataToTimeZone((String) resultSet
                            .getString(ReportConstants.ROW_TIMEZONECODE)
                            + ReportConstants.TIMEZONE_OCLOCK);
                    detailReport.addDataToCustomers((String) resultSet
                            .getString(ReportConstants.ROW_SALESGUESTCNT));
                    detailReport.addDataToItems((String) resultSet
                            .getString(ReportConstants.ROW_SALESITEMCNT));
                    detailReport.addDataToAmount((String) resultSet
                            .getString(ReportConstants.ROW_SALESSALESAMOUNT));
                }
                strtTime++;
            }

            // If no more results, but strtTime is less than endTime,
            // continue looping with zero results
            while (strtTime <= endTime) {
                String timeTemp = String.format("%" + 2 + "s",
                        String.valueOf(strtTime)).replace(' ', '0'); // padleft
                detailReport.addDataToTimeZone(timeTemp
                        + ReportConstants.TIMEZONE_OCLOCK);
                detailReport.addDataToCustomers("0");
                detailReport.addDataToItems("0");
                detailReport.addDataToAmount("0");
                strtTime++;
            }

        } catch (SQLException sqlEx) {

            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL, "Failed to generate detail report.\n"
                            + sqlEx.getMessage());
            throw new DaoException("Abnormal operation", sqlEx);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO, "Failed to generate detail report.\n"
                            + daoEx.getMessage());
            throw new DaoException("Abnormal operation", daoEx);
        } catch (ParseException parseEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_PARSE,
                    "Failed to generate detail report.\n"
                            + parseEx.getMessage());
            return ReportConstants.INVALID_BUSINESSDATE;
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to generate detail report.\n" + e.getMessage());
            return ReportConstants.INVALID_BUSINESSDATE;
        } finally {
            closeConnectionObjects(connection, prepdStatement, resultSet);

            String detailReportStr = "";
            if (null != detailReport) {
                detailReportStr = detailReport.toHTML();
            }
            tp.methodExit(detailReportStr);
        }

        /* Return HTML formatted String Detail Report */
        return detailReport.toHTML();
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ncr.res.mobilepos.report.dao.IReportDAO#generateFinancialReportNew(java
     * .lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public final FinancialReport generateFinancialReportNew(
            final String deviceNo, final String businessDate,
            final String storeNo) throws DaoException, SQLException {
        String functionName = "SQLServerReportDAO.generateFinancialReport";
        tp.methodEnter("generateFinancialReportNew");
        tp.println("deviceNo", deviceNo)
            .println("businessDate", businessDate)
            .println("storeNo", storeNo);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;

        if (dbManager == null) {
            dbManager = JndiDBManagerMSSqlServer.getInstance();
        }
        try {
            connection = dbManager.getConnection();
        } catch (SQLException ex) {

            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate financial report.\n"
                            + ex.getMessage());
            tp.methodExit("error getting connection");
            throw new SQLException(ex.getMessage(), ex.getCause());
        }

        FinancialReport financialRpt = new FinancialReport();
        financialRpt.setDeviceNo(deviceNo);
        financialRpt.setBusinessDate(businessDate);

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-financial-data"));
            selectStmnt.setString(SQLStatement.PARAM1, deviceNo);
            selectStmnt.setString(SQLStatement.PARAM2, businessDate);
            selectStmnt.setString(SQLStatement.PARAM3, storeNo);

            resultSet = selectStmnt.executeQuery();

            while (resultSet.next()) {
                int transCode = Integer.valueOf(resultSet.getString(
                        Column.ACCOUNT_CODE).toString());
                TransactionCode code = TransactionCode.get(transCode);
                String strAccountSubCode = resultSet
                        .getString(Column.ACCOUNT_SUBCODE);
                BigDecimal bdCreditor = new BigDecimal(
                        resultSet.getString(Column.CREDITOR) == null ? "0"
                                : resultSet.getString(Column.CREDITOR));
                BigDecimal bdDebtor = new BigDecimal(
                        resultSet.getString(Column.DEBTOR) == null ? "0"
                                : resultSet.getString(Column.DEBTOR));
                BigDecimal bdCreditorDebtorDifference = bdDebtor
                        .subtract(bdCreditor);

                switch (code) {
                case ITEMCODE:
                    financialRpt = this.getFinancialForItemCode(financialRpt,
                            strAccountSubCode, bdCreditor, bdDebtor);
                    break;
                case ITEMDISCOUNTCODE:
                    financialRpt.setDiscountSale(bdCreditorDebtorDifference);
                    break;
                case CASHCODE:
                    financialRpt.setCash(bdCreditorDebtorDifference);
                    break;
                case CREDITDEBITCODE:
                    financialRpt.setCreditCard(bdCreditorDebtorDifference);
                    break;
                case VOUCHERCODE:
                    financialRpt.setMiscellaneous(bdCreditorDebtorDifference);
                    break;
                case DISCOUNTCODE:
                    financialRpt.setDiscountSale(bdCreditorDebtorDifference);
                    break;
                default:
                    break;
                }
            }

        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);
        }

        return financialRpt;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ncr.res.mobilepos.report.dao.IReportDAO#generateDrawerFinancialReport
     * (java.lang.String, java.lang.String, java.lang.String)
     */
    @Override
    public final DrawerFinancialReport generateDrawerFinancialReport(
            final String printerID, final String businessDate,
            final String storeNo) throws DaoException, SQLException {
        String functionName = "SQLServerReportDAO.generateDrawerFinancialReport";
        tp.methodEnter("generateDrawerFinancialReport");
        tp.println("printerID", printerID)
            .println("businessDate", businessDate)
            .println("storeNo", storeNo);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;

        if (dbManager == null) {
            dbManager = JndiDBManagerMSSqlServer.getInstance();
        }
        try {
            connection = dbManager.getConnection();
        } catch (SQLException ex) {

            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate drawer financial report.\n"
                            + ex.getMessage());
            tp.methodExit("error getting connection");
            throw new SQLException(ex.getMessage(), ex.getCause());
        }

        DrawerFinancialReport drawerFinancialRpt = new DrawerFinancialReport();
        drawerFinancialRpt.setPrinterID(printerID);
        drawerFinancialRpt.setBusinessDate(businessDate);

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-drawerfinancial-data"));
            selectStmnt.setString(SQLStatement.PARAM1, printerID);
            selectStmnt.setString(SQLStatement.PARAM2, businessDate);
            selectStmnt.setString(SQLStatement.PARAM3, storeNo);

            resultSet = selectStmnt.executeQuery();

            while (resultSet.next()) {
                int transCode = Integer.valueOf(resultSet.getString(
                        Column.ACCOUNT_CODE).toString());
                TransactionCode code = TransactionCode.get(transCode);
                String strAccountSubCode = resultSet
                        .getString(Column.ACCOUNT_SUBCODE);
                BigDecimal bdCreditor = new BigDecimal(
                        resultSet.getString(Column.CREDITOR) == null ? "0"
                                : resultSet.getString(Column.CREDITOR));
                BigDecimal bdDebtor = new BigDecimal(
                        resultSet.getString(Column.DEBTOR) == null ? "0"
                                : resultSet.getString(Column.DEBTOR));
                BigDecimal bdCreditorDebtorDifference = bdDebtor
                        .subtract(bdCreditor);
                switch (code) {
                case ITEMCODE:
                    drawerFinancialRpt = this.getDrawerFinancialForItemCode(
                            drawerFinancialRpt, strAccountSubCode, bdCreditor,
                            bdDebtor);
                    break;
                case ITEMDISCOUNTCODE:
                    drawerFinancialRpt
                            .setDiscountSale(bdCreditorDebtorDifference);
                    break;
                case CASHCODE:
                    drawerFinancialRpt.setCash(bdCreditorDebtorDifference);
                    break;
                case CREDITDEBITCODE:
                    drawerFinancialRpt
                            .setCreditCard(bdCreditorDebtorDifference);
                    break;
                case VOUCHERCODE:
                    drawerFinancialRpt
                            .setMiscellaneous(bdCreditorDebtorDifference);
                    break;
                case DISCOUNTCODE:
                    drawerFinancialRpt
                            .setDiscountSale(bdCreditorDebtorDifference);
                    break;
                default:
                    break;
                }
            }

        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);

            tp.methodExit(drawerFinancialRpt.toHTML());
        }

        return drawerFinancialRpt;
    }

    /*
     * (non-Javadoc)
     *
     * @see
     * ncr.res.mobilepos.report.dao.IReportDAO#getPrinterInfo(java.lang.String,
     * java.lang.String, java.lang.String)
     */
    @Override
    public final NetPrinterInfo getPrinterInfo(final String storeid, final String printerid) throws DaoException {

        tp.methodEnter("getPrinterInfo");
        tp.println("storeid", storeid).println("printerid", printerid);

        NetPrinterInfo netPrinterInfo = new NetPrinterInfo();

        PreparedStatement getnetPrinterInfoStmt = null;
        ResultSet rs = null;

        String functionName = "SQLServerReportDAO.getPrinterInfo";
        try {
            if (dbManager == null) {
                dbManager = JndiDBManagerMSSqlServer.getInstance();
            }
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            getnetPrinterInfoStmt = connection.prepareStatement(sqlStatement
                    .getProperty("get-netprinterinfo-byprinterid"));
            getnetPrinterInfoStmt.setString(SQLStatement.PARAM1, storeid);
            getnetPrinterInfoStmt.setString(SQLStatement.PARAM2, printerid);

            rs = getnetPrinterInfoStmt.executeQuery();
            if (rs.next()) {
                netPrinterInfo.setUrl(rs.getString("IpAddress"));
                netPrinterInfo.setPortTCP(rs.getInt("PortNumTcp"));
                netPrinterInfo.setPortUDP(rs.getInt("PortNumUdp"));
            } else {
                tp.println("ResultSet is empty");
                netPrinterInfo = null;
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_SQL, "SQL Exception Error occured. \n"
                            + sqlEx.getMessage());
        } finally {
            closeConnectionObjects(connection, getnetPrinterInfoStmt, rs);

            tp.methodExit(netPrinterInfo.toString());
        }

        return netPrinterInfo;
    }

    /**
     * Get the Financial Report Object.
     *
     * @param deviceNo
     *            the device number
     * @param businessDate
     *            the business date
     * @param storeNo
     *            the store number
     * @return the financial report obj
     * @throws DaoException
     *             thrown when process fails.
     * @throws SQLException
     *             thrown when SQL error occurs.
     */
    public final FinancialReport getFinancialReportObj(final String deviceNo,
            final String businessDate, final String storeNo)
            throws DaoException, SQLException {

        tp.methodEnter("getFinancialReportObj");
        tp.println("deviceNo", deviceNo)
            .println("businessDate", businessDate)
            .println("storeNo", storeNo);

        PreparedStatement selectStmnt = null;
        PreparedStatement getStoreName = null;
        ResultSet resultSet = null;

        if (dbManager == null) {
            dbManager = JndiDBManagerMSSqlServer.getInstance();
        }
        String functionName = "SQLServerReportDAO.getFinancialReportObj";
        try {
            connection = dbManager.getConnection();
        } catch (SQLException ex) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate financial report.\n"
                            + ex.getMessage());
            tp.methodExit("error getting connection");
            throw new SQLException(ex.getMessage(), ex.getCause());
        }

        FinancialReport financialRpt = new FinancialReport();
        financialRpt.setDeviceNo(deviceNo);
        financialRpt.setBusinessDate(businessDate);
        financialRpt.setStoreid(storeNo);

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();

            getStoreName = connection.prepareStatement(sqlStatement
                    .getProperty("get-storeInfo"));
            getStoreName.setString(SQLStatement.PARAM1, storeNo);

            ResultSet rs = getStoreName.executeQuery();
            if (rs.next()) {
                financialRpt.setStoreName(rs.getString("StoreName"));
            }
            closeConnectionObjects(null, getStoreName, rs);
            selectStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-financial-data"));
            selectStmnt.setString(SQLStatement.PARAM1, deviceNo);
            selectStmnt.setString(SQLStatement.PARAM2, businessDate);
            selectStmnt.setString(SQLStatement.PARAM3, storeNo);

            resultSet = selectStmnt.executeQuery();

            while (resultSet.next()) {
                int transCode = Integer.valueOf(resultSet.getString(
                        Column.ACCOUNT_CODE).toString());
                TransactionCode code = TransactionCode.get(transCode);
                String strAccountSubCode = resultSet
                        .getString(Column.ACCOUNT_SUBCODE);
                BigDecimal bdCreditor = new BigDecimal(
                        resultSet.getString(Column.CREDITOR) == null ? "0"
                                : resultSet.getString(Column.CREDITOR));
                BigDecimal bdDebtor = new BigDecimal(
                        resultSet.getString(Column.DEBTOR) == null ? "0"
                                : resultSet.getString(Column.DEBTOR));
                BigDecimal bdCreditorDebtorDifference = bdDebtor
                        .subtract(bdCreditor);
                switch (code) {
                case ITEMCODE:
                    financialRpt = this.getFinancialForItemCode(financialRpt,
                            strAccountSubCode, bdCreditor, bdDebtor);
                    break;
                case ITEMDISCOUNTCODE:
                    financialRpt.setDiscountSale(bdCreditorDebtorDifference);
                    break;
                case CASHCODE:
                    financialRpt.setCash(bdCreditorDebtorDifference);
                    break;
                case CREDITDEBITCODE:
                    financialRpt.setCreditCard(bdCreditorDebtorDifference);
                    break;
                case DISCOUNTCODE:
                    financialRpt.setDiscountSale(bdCreditorDebtorDifference);
                    break;
                default:
                    break;
                }
            }

        }  finally {
            closeConnectionObjects(null, getStoreName, null);
            closeConnectionObjects(connection, selectStmnt, resultSet);
        }

        return financialRpt;
    }

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

        String functionName = DebugLogger.getCurrentMethodName();
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

    /**
     * Get the Department name
     *
     * @param storeNo
     *            the store number
     * @param departmentNo
     *            the department number
     * @return dptName
     *            the departmentName String
     * @throws DaoException
     *             thrown when process fails.
     */
    @Override
    public String findDepartmentName(String storeNo, String departmentNo) throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
          .println("storeNo", storeNo)
          .println("departmentNo", departmentNo);

        ResultSet resultSet = null;
        String dptName=null;

        try {
            if(StringUtility.isNullOrEmpty(departmentNo)){
                return dptName;
            }

            String selectStmt = "get-dptDetails";
            resultSet = executeQuery(selectStmt, departmentNo, storeNo);

            if(resultSet.next()){
                 dptName= resultSet.getString("DptName");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to get departmentname.\n" + e.getMessage());
            throw new DaoException("SQLException: @"
                    + "SqlServerReportDAO:findDepartmentName"
                    + " - Error find DepartmentName", e);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                        "Failed to get departmentname.\n" + daoEx.getMessage());
                throw new DaoException("DAOException: @"
                    + "SqlServerReportDAO:findDepartmentName"
                    + " - Error find DepartmentName", daoEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            throw new DaoException("Exception: @"
                    + "SqlServerReportDAO:findDepartmentName"
                    + " - Error find DepartmentName", e);
        } finally {

                closeConnectionObjects(connection, prepdStatement, resultSet);
                tp.methodExit();

            }
        return dptName;
    }
    /**
     * Get the Store name
     *
     *
     * @param operatorNo
     *            the operator number
     * @return the operatorName String
     * @throws DaoException
     *             thrown when process fails.
     * @throws SQLStatementException
     *             thrown when SQL error occurs.
     */
    @Override
    public String findOperatorName(String operatorNo) throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
          .println("operatorNo", operatorNo);

        ResultSet resultSet = null;
        String operatorName=null;

        try {
            if(StringUtility.isNullOrEmpty(operatorNo)){
                return operatorName;
            }

            String statement = "get-operator-by-operatorno";
            resultSet = executeQuery(statement, operatorNo);
            if(resultSet.next()){
                operatorName= resultSet.getString("OperatorName");
            }
        } catch (DaoException daoEx) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO, "Failed to find operatorName.\n"
                            + daoEx.getMessage());
            throw new DaoException("DaoException: @"
                    + "SqlServerReportDAO:findOperatorName"
                    + " - Error find OperatorName", daoEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate sales man report.\n"
                            + sqlEx.getMessage());
            throw new DaoException("SQLException: @"
                    + "SqlServerReportDAO:findOperatorName"
                    + " - Error find OperatorName", sqlEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            throw new DaoException("Exception: @"
                    + "SqlServerReportDAO:findOperatorName"
                    + " - Error find StoreName", e);
        } finally{

               closeConnectionObjects(connection, prepdStatement, resultSet);
               tp.methodExit();

            }
        return operatorName;
    }

    /**
     * Get the Clerk Product List
     *
     *
     * @param operatorNo
     *            the device number
     * @param businessDaydate
     *            the business date
     * @param storeNo
     *            the store number
     * @param operatorNo
     *            the operator number
     * @return the ReportMode obj
     * @throws DaoException
     *             throw when process fails.
     */
    @Override
    public final ReportItems generateClerkProductReportNew(
            String operatorNo,String businessDaydate, String storeNo)
            throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("operatorNo",operatorNo)
          .println("businessDaydate",businessDaydate)
          .println("storeNo",storeNo);

        ResultSet resultSet = null;
        ClerkProductReport clerkProductReport = null;
        try {
            clerkProductReport = new ClerkProductReport();
            String selectStmt = "get-clerkproduct";
            resultSet = executeQuery(selectStmt, businessDaydate, storeNo,
                    operatorNo);
            while (resultSet.next()) {
                clerkProductReport.addDataToProductCode(resultSet
                        .getString(ReportConstants.ROW_PRODUCTCODE));
                clerkProductReport.addDataToProductName(resultSet
                        .getString(ReportConstants.ROW_PRODUCT));
                clerkProductReport.addDataToSalesItems(resultSet
                        .getString(ReportConstants.ROW_ITEM));
                clerkProductReport.addDataToSalesAmount(resultSet
                        .getString(ReportConstants.ROW_SALES));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate clerk product report.\n"
                            + sqlEx.getMessage());
            throw new DaoException("SQLException: @"
                    + "SqlServerReportDAO:generateClerkProductReportNew"
                    + " - Failed to generate clerk product report", sqlEx);
        } catch (DaoException daoEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to generate clerk product report.\n"
                            + daoEx.getMessage());
            throw new DaoException("DAOException: @"
                    + "SqlServerReportDAO:generateClerkProductReportNew"
                    + " - Failed to generate clerk product report", daoEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            throw new DaoException("Exception: @"
                    + "SqlServerReportDAO:generateClerkProductReportNew"
                    + " - Failed to generate clerk product report", e);
        } finally {
            closeConnectionObjects(connection, prepdStatement, resultSet);
            tp.methodExit();
        }
        return clerkProductReport.getReportItems();
    }

    /**
     * Get the Sales man report
     *
     * @param businessDaydate
     *            the business date
     * @param storeNo
     *            the store number
     * @param operatorNo
     *            the operator number
     * @return the ReportMode obj
     * @throws DaoException
     *             throw when process fails.
     */
    public final ReportItems generateSalesManReportNew(final String operatorNo,
            final String businessDaydate,
            final String storeNo) throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("operatorNo", operatorNo)
          .println("businessDaydate", businessDaydate)
          .println("storeNo", storeNo);

        ResultSet resultSet = null;
        SalesManReport salesManReport = null;
        try {
            salesManReport = new SalesManReport();
            String selectStmt = "select-salesmanreport";
            resultSet = executeQuery(selectStmt, businessDaydate,storeNo,operatorNo);
            while (resultSet.next()) {
                salesManReport.addDataToSalManNumber(resultSet
                        .getString(ReportConstants.ROW_SALESPERSONID));
                salesManReport.addDataToName(resultSet
                        .getString(ReportConstants.ROW_OPERATORNAME));
                salesManReport.addDataToSalesCustomers(resultSet
                        .getString(ReportConstants.ROW_SALESGUESTCNT));
                salesManReport.addDataToSalesItems(resultSet
                        .getString(ReportConstants.ROW_ITEMCNT));
                salesManReport.addDataToSalesAmount(resultSet
                        .getString(ReportConstants.ROW_SALESAMOUNT));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to generate sales man report.\n"
                            + sqlEx.getMessage());
            throw new DaoException("SQLException: @"
                    + "SqlServerReportDAO:generateSalesManReportNew"
                    + " - Failed to generate sales man report", sqlEx);
        }catch (DaoException daoEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_DAO,
                    "Failed to generate sales man report.\n"
                            + daoEx.getMessage());
            throw new DaoException("DAOException: @"
                    + "SqlServerReportDAO:generateSalesManReportNew"
                    + " - Failed to generate sales man report", daoEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL, e.getMessage());
            throw new DaoException("Exception: @"
                    + "SqlServerReportDAO:generateSalesManReportNew"
                    + " - Failed to generate sales man report", e);
        } finally {
            closeConnectionObjects(connection, prepdStatement, resultSet);
            tp.methodExit();
        }

        return salesManReport.getReportItems();
    }
    //1.02 2014.11.27  FENGSHA   売上表を対応 ADD END

    private FinancialReport getFinancialForItemCode(
            FinancialReport financialRpt, String strAccountSubCode,
            BigDecimal bdCreditor, BigDecimal bdDebtor) {

        FinancialReport financialRptTemp = financialRpt;
        if (TxTypes.NORMAL.equals(strAccountSubCode)) {
            financialRptTemp.setGrossSalesRevenue(bdCreditor);
        } else if (TxTypes.VOIDER.equals(strAccountSubCode)) {
            financialRptTemp.setVoidSale(bdDebtor);
        } else if (TxTypes.RETURNER.equals(strAccountSubCode)) {
            financialRptTemp.setReturnSale(bdDebtor);
        }
        return financialRptTemp;
    }

    private DrawerFinancialReport getDrawerFinancialForItemCode(
            DrawerFinancialReport drawerFinancialReport,
            String strAccountSubCode, BigDecimal bdCreditor, BigDecimal bdDebtor) {

        DrawerFinancialReport drawerFinancialReportTemp = drawerFinancialReport;
        if (TxTypes.NORMAL.equals(strAccountSubCode)) {
            drawerFinancialReportTemp.setGrossSalesRevenue(bdCreditor);
        } else if (TxTypes.VOIDER.equals(strAccountSubCode)) {
            drawerFinancialReportTemp.setVoidSale(bdDebtor);
        } else if (TxTypes.RETURNER.equals(strAccountSubCode)) {
            drawerFinancialReportTemp.setReturnSale(bdDebtor);
        }
        return drawerFinancialReportTemp;
    }

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

        String functionName = DebugLogger.getCurrentMethodName();
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

    	String functionName = DebugLogger.getCurrentMethodName();
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

        String functionName = DebugLogger.getCurrentMethodName();
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
