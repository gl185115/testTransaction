/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerJournalDAO
 *
 * Implementation for IJournalDAO
 *
 * Rica Marie M. Del Rio
 * Jessel G. Dela Cerna
 * Carlos G. Campos
 * Chris Niven D. Meneses
 */

package ncr.res.mobilepos.journalization.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.sql.Types;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import ncr.realgate.util.Guid;
import ncr.realgate.util.IoWriter;
import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.constant.TransactionVariable;
import ncr.res.mobilepos.constant.TxTypes;
import ncr.res.mobilepos.constant.WindowsEnvironmentVariables;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.deviceinfo.dao.IDeviceInfoDAO;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.JournalizationException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.exception.TillException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.EasyJson;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.POSLogHandler;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.model.PointPosted;
import ncr.res.mobilepos.journalization.model.SearchForwardPosLog;
import ncr.res.mobilepos.journalization.model.poslog.AdditionalInformation;
import ncr.res.mobilepos.journalization.model.poslog.LineItem;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.RetailTransaction;
import ncr.res.mobilepos.journalization.model.poslog.Tender;
import ncr.res.mobilepos.journalization.model.poslog.Total;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;
import ncr.res.mobilepos.journalization.model.poslog.TransactionLink;
import ncr.res.mobilepos.journalization.model.poslog.TransactionSearch;
import ncr.res.mobilepos.journalization.model.poslog.WorkstationID;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.systemsetting.dao.ISystemSettingDAO;
import ncr.res.mobilepos.systemsetting.model.DateSetting;
import ncr.res.mobilepos.tillinfo.dao.ITillInfoDAO;
import ncr.res.mobilepos.tillinfo.model.Till;
import ncr.res.mobilepos.tillinfo.resource.TillInfoResource;

/**
 * A Data Access Object implementation for POSLog Journal.
 *
 * @see IPosLogDAO
 */
public class SQLServerPosLogDAO extends AbstractDao implements IPosLogDAO {
    /** The Database Manager. */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /** The twelve clock time. */
    private static final int TWELVECLOCKTIME = 12;
    /** The Trace Printer. */
    private Trace.Printer tp;
    /** Snap Logger */
    private SnapLogger snap;
    /** ServerId of Tomcat. */
    private String serverID = "";
    /** Abbreviation program name of the class. **/
    private static final String PROG_NAME = "PosLgDAO";
    /** default server type */
    static final String DEFAULT_SERVERTYPE = "STORE";

    // Constants for updating TXU_POS_CTRL.
    public static final int POS_CTRL_UPDATE_TYPE_SOD = 0;
    public static final int POS_CTRL_UPDATE_TYPE_EOD = 1;
    private static final short OPEN_CLOSE_STAT_SOD = 1;
    private static final short OPEN_CLOSE_STAT_EOD = 4;

    /**
     * Default Constructor for SQLServerPoslogDAO.
     *
     * @throws DaoException
     *             If exception occurred in creating instance of
     *             SQLServerPoslogDAO
     */
    public SQLServerPosLogDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
        this.snap = (SnapLogger) SnapLogger.getInstance();
    }

    /**
     * Gets the Database Manager for the Class.
     *
     * @return The Database Manager Object
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    /**
     * Private method that is used to return the type of the transaction
     *
     * @param connection
     *            The database connection
     * @param storeid
     *            The unique identifier of the store
     * @param terminalid
     *            The device number of the transaction.
     * @param txNumber
     *            The transaction number to check.
     * @param summarydateid
     *            the Summary Date ID.
     *
     * @return Returns the type of the transaction
     *
     * @throws Exception
     *             Exception thrown when the method failed.
     */
    private String getTransactionType(final Connection connection, final String storeid, final String terminalid,
            final String txNumber, final String summarydateid)
                    throws SQLException, SQLStatementException, DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("StoreID", storeid).println("TerminalID", terminalid)
                .println("Transaction Number", txNumber).println("Summary Date ID", summarydateid);

        PreparedStatement selectPosLogTxType = null;
        ResultSet resultTxType = null;
        String result = "";

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectPosLogTxType = connection.prepareStatement(sqlStatement.getProperty("get-poslog-xml-txtype"));

            selectPosLogTxType.setString(SQLStatement.PARAM1, storeid);
            selectPosLogTxType.setString(SQLStatement.PARAM2, terminalid);
            selectPosLogTxType.setString(SQLStatement.PARAM3, txNumber);
            selectPosLogTxType.setString(SQLStatement.PARAM4, summarydateid);
            resultTxType = selectPosLogTxType.executeQuery();

            if (resultTxType.next()) {
                result = resultTxType.getString("txtype");
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get poslog type.", e);
            throw e;
        } finally {
            closeConnectionObjects(null, selectPosLogTxType, resultTxType);
        }
        return tp.methodExit(result);
    }

    /**
     * Private Method that save the POSLog transaction and POSLog XML in the
     * TXL_POSLOG Table.
     *
     * @param transaction
     *            The transaction information
     * @param posLogXml
     *            The POSLog Xml
     * @param txType
     *            The transaction type
     * @param savePOSLogStmt
     *            The prepared Statement dedicated for saving the POSLOg xml
     *
     * @return Return the number of rows affected in the database.
     *
     * @throws SQLException
     *             The exception when saving the POSLog xml fails.
     * @throws NamingException
     *             The exception when generating guid fails.
     */
    private int savePosLogXML(final Transaction transaction, final String posLogXml, final String txType,
            final PreparedStatement savePOSLogStmt, final Connection connection, final int trainingMode)
                    throws SQLException, NamingException {
        String status = "0";
        String guid = getGuid();

        savePOSLogStmt.setString(SQLStatement.PARAM1, transaction.getRetailStoreID());
        savePOSLogStmt.setString(SQLStatement.PARAM2, transaction.getWorkStationID().getValue());
        savePOSLogStmt.setString(SQLStatement.PARAM3, transaction.getSequenceNo());
        savePOSLogStmt.setString(SQLStatement.PARAM4, transaction.getBusinessDayDate());
        savePOSLogStmt.setString(SQLStatement.PARAM5, transaction.getBeginDateTime());
        savePOSLogStmt.setString(SQLStatement.PARAM6, posLogXml);
        savePOSLogStmt.setString(SQLStatement.PARAM7, txType);
        savePOSLogStmt.setString(SQLStatement.PARAM8, status);
        savePOSLogStmt.setString(SQLStatement.PARAM9, guid);
        savePOSLogStmt.setString(SQLStatement.PARAM10, serverID);
        savePOSLogStmt.setString(SQLStatement.PARAM11, transaction.getOrganizationHierarchy().getId());
        savePOSLogStmt.setInt(SQLStatement.PARAM12, trainingMode);

        int result = savePOSLogStmt.executeUpdate();

        // update last tx id in deviceinfo
        updateLastTxId(transaction, connection, trainingMode);

        return result;
    }

    /**
     * Private Method that save the TXU_TOTAL_GUESTTILLDAY Table.
     *
     * @param transaction
     *            The transaction information
     * @param txType
     *            The transaction type
     * @param saveTxuTotalGuestTillDayStmt
     *            The prepared Statement for saving the TxuTotalGuestTillDay
     *            Table
     *
     * @return Return the number of rows affected in the database.
     *
     * @throws SQLException
     *             The exception when saving the TxuTotalGuestTillDay Table
     *             fails.
     * @throws NamingException
     *             The exception when generating guid fails.
     */
    private int saveTxuTotalGuestTillDay(final Transaction transaction, final String txType,
            final PreparedStatement saveTxuTotalGuestTillDayStmt, final Connection connection)
                    throws SQLException, NamingException {

        int result = 0;
        boolean returnFlag = false;
        boolean isVoidReturnFlag = false;
        long SalesGuestCnt = 0;
        long ReturnGuestCnt = 0;
        long VoidGuestCnt = 0;

        if (null != transaction.getRetailTransaction()) {
            if (null != transaction.getRetailTransaction().getTransactionLink()) {

            	TransactionLink transactionLink = POSLogHandler.getNormalTransactionLink(transaction.getRetailTransaction().getTransactionLink());

                if (TxTypes.LAYAWAY.equals(transactionLink.getReasonCode())
                            || TxTypes.HOLD.equals(transactionLink.getReasonCode())
                            || TxTypes.CUSTOMERORDER.equals(transactionLink.getReasonCode())) {
                        returnFlag = true;
                }
            }
        }

        if (!returnFlag) {
            if (txType.equalsIgnoreCase(TxTypes.SALES)) {
                SalesGuestCnt = 1;
            } else if (txType.equalsIgnoreCase(TxTypes.RETURN)) {
                ReturnGuestCnt = 1;
            } else {
                if (null != transaction.getRetailTransaction()) {
                    if (null != transaction.getRetailTransaction().getLineItems()) {
                        List<LineItem> lineItems = transaction.getRetailTransaction().getLineItems();

                        for (LineItem lineItem : lineItems) {
                            if (lineItem.getRetrn() != null) {
                                isVoidReturnFlag = true;
                                break;
                            }
                        }
                    }
                }

                if (isVoidReturnFlag) {
                    ReturnGuestCnt = -1;
                } else {
                    VoidGuestCnt = 1;
                }
            }

            saveTxuTotalGuestTillDayStmt.setString(SQLStatement.PARAM1, transaction.getRetailStoreID());
            saveTxuTotalGuestTillDayStmt.setString(SQLStatement.PARAM2, transaction.getTillID());
            saveTxuTotalGuestTillDayStmt.setString(SQLStatement.PARAM3, transaction.getBusinessDayDate());
            saveTxuTotalGuestTillDayStmt.setLong(SQLStatement.PARAM4, SalesGuestCnt);
            saveTxuTotalGuestTillDayStmt.setLong(SQLStatement.PARAM5, ReturnGuestCnt);
            saveTxuTotalGuestTillDayStmt.setLong(SQLStatement.PARAM6, VoidGuestCnt);

            result = saveTxuTotalGuestTillDayStmt.executeUpdate();
        }

        return result;
    }

    private void updateLastTxId(final Transaction transaction, final Connection connection, int trainingMode) {
        String functionName = DebugLogger.getCurrentMethodName();
        try {
            IDeviceInfoDAO iDeviceInfoDao = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER).getDeviceInfoDAO();
            boolean updated = iDeviceInfoDao.updateLastTxidAtJournal(transaction, connection, trainingMode);

            if (!updated) {
                tp.methodExit("Failed to update last txid - " + transaction.getSequenceNo() + ".");
            }
        } catch (DaoException e) {
            tp.methodExit("Failed to update last txid - " + transaction.getSequenceNo() + ".");
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL,
                    functionName + ": Failed to update last txid - " + transaction.getSequenceNo() + ".", e);
        } catch (Exception e) {
            tp.methodExit("Failed to update last txid - " + transaction.getSequenceNo() + ".");
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to update last txid - " + transaction.getSequenceNo() + ".", e);
        }
    }

    /**
     * Private Method that implement the Normal Transaction.
     *
     * @param transaction
     *            The Transaction Information
     * @param posLogXml
     *            The POSLog XML of the transaction
     * @param savePOSLogStmt
     *            The Prepared Statement for saving The POSLog xml in the POSLOg
     *            Table
     * @param saveTxuTotalGuestTillDayStmt
     *            The Prepared Statement for saving The TXU_TOTAL_GUESTTILLDAY
     *            Table
     *
     * @return void
     *
     * @throws SQLException
     *             The Exception thrown when Normal Transaction process fails.
     */
    private void doNormalTransaction(final Transaction transaction, final String posLogXml,
            final PreparedStatement savePOSLogStmt, final Connection connection,
            final PreparedStatement saveTxuTotalGuestTillDayStmt, final int trainingMode, final String txType)
                    throws SQLException, SQLStatementException, NamingException, DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        savePosLogXML(transaction, posLogXml, txType, savePOSLogStmt, connection, trainingMode);

        // insert return details to TXU_TOTAL_GUESTTILLDAY
        // saveTxuTotalGuestTillDay(transaction, TxTypes.SALE,
        // saveTxuTotalGuestTillDayStmt, connection);

        tp.methodExit();
    }

    /**
     * Private Method that implement the VOIDING of transaction.
     *
     * @param transaction
     *            The Transaction Information
     * @param posLogXml
     *            The POSLog XML of the transaction
     * @param txType
     *            The transaction type
     * @param connection
     *            The Database connection object
     * @param savePOSLogStmt
     *            The Prepared Statement for saving the POSLog xml in the POSLOg
     *            Table
     * @param saveVoidDetailsStmt
     *            The Prepared Statement for saving The TXL_SALES_VOIDED Table
     * @param connection
     *            The Database connection object
     * @param trainingMode
     *            The trainingMode
     *
     * @return void
     *
     * @throws Exception
     *             The Exception thrown when Void Transaction process fails.
     */
    private void doVoidTransactionByTxTypes(final Transaction transaction, final String posLogXml, final String txType,
            final PreparedStatement savePOSLogStmt, final PreparedStatement saveVoidDetailsStmt,
            final Connection connection, final int trainingMode)
                    throws SQLException, SQLStatementException, DaoException, JournalizationException, NamingException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        TransactionLink transactionLink = POSLogHandler.getNormalTransactionLink(transaction.getRetailTransaction().getTransactionLink());
        String companyId = transaction.getOrganizationHierarchy().getId();
        String returnedType = "";

        if (TxTypes.VOID.equals(txType) || TxTypes.EXCHANGESALESVOID.equals(txType)
                || TxTypes.PURCHASEVOID.equals(txType) || TxTypes.CHARGESALESVOID.equals(txType)) {
            switch (txType) {
            case TxTypes.VOID:
                returnedType = TxTypes.RETURN;
                break;
            case TxTypes.EXCHANGESALESVOID:
                returnedType = TxTypes.EXCHANGERETURN;
                break;
            case TxTypes.PURCHASEVOID:
                returnedType = TxTypes.PURCHASERETURN;
                break;
            case TxTypes.CHARGESALESVOID:
                returnedType = TxTypes.CHARGESALESVOID;
                break;
            }
            if (isTransactionVoidedOrReturned(transactionLink, connection, returnedType, companyId, trainingMode)) {
                tp.println("Original transaction is already returned.");
                throw new JournalizationException("Original transaction is already returned.", new Exception(),
                        ResultBase.RES_ERROR_TXALREADYRETURNED);
            }
        }
        if (isTransactionVoidedOrReturned(transactionLink, connection, txType, companyId, trainingMode)) {
            tp.println("Original transaction is already voided.");
            throw new JournalizationException("Original transaction is already voided.", new Exception(),
                    ResultBase.RES_ERROR_TXALREADYVOIDED);
        } else if (!StringUtility.isNullOrEmpty(transaction.getBusinessDayDate(), transactionLink.getBusinessDayDate())
                && !transaction.getBusinessDayDate().equals(transactionLink.getBusinessDayDate())) {
            tp.println("Original transaction was not registered on today.");
            throw new JournalizationException("Original transaction was not registered on today.", new Exception(),
                    ResultBase.RES_ERROR_TXINVALID);
        }

        savePosLogXML(transaction, posLogXml, txType, savePOSLogStmt, connection, trainingMode);

        // insert void details to TXL_SALES_VOIDED
        saveVoidReturnDetails(saveVoidDetailsStmt, transactionLink, txType, companyId, trainingMode);

        tp.methodExit();
    }

    /**
     * Private Method that implement the VOIDING of transaction.
     *
     * @param transaction
     *            The Transaction Information
     * @param posLogXml
     *            The POSLog XML of the transaction
     * @param connection
     *            The Database connection object
     * @param savePOSLogStmt
     *            The Prepared Statement for saving the POSLog xml in the POSLOg
     *            Table
     * @param saveTxuTotalGuestTillDayStmt
     *            The Prepared Statement for saving The TXU_TOTAL_GUESTTILLDAY
     *            Table
     *
     * @return void
     *
     * @throws Exception
     *             The Exception thrown when Void Transaction process fails.
     */
    private void doVoidTransaction(final Transaction transaction, final String posLogXml, final Connection connection,
            final PreparedStatement savePOSLogStmt, final PreparedStatement saveVoidDetailsStmt,
            final PreparedStatement saveTxuTotalGuestTillDayStmt, final int trainingMode)
                    throws SQLException, SQLStatementException, DaoException, JournalizationException, NamingException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        TransactionLink transactionLink = POSLogHandler.getNormalTransactionLink(transaction.getRetailTransaction().getTransactionLink());
        String companyId = transaction.getOrganizationHierarchy().getId();
        String txtType = getTransactionType(connection, transactionLink.getRetailStoreID(),
                transactionLink.getWorkStationID().getValue(), transactionLink.getSequenceNo(),
                transactionLink.getBusinessDayDate());

        if (txtType.isEmpty()) {
            tp.println("Original transaction not found.").println("Retail Store Id", transactionLink.getRetailStoreID())
                    .println("Work Station Id", transactionLink.getWorkStationID())
                    .println("Sequence Number", transactionLink.getSequenceNo())
                    .println("Business Day Date", transactionLink.getBusinessDayDate());
            throw new JournalizationException("Original transaction not found.", new Exception(),
                    ResultBase.RES_ERROR_TXNOTFOUND);
        } else if (!(txtType.equals(TxTypes.SALES) || txtType.equals(TxTypes.RETURN) || txtType.equals(TxTypes.HOLD)
                || txtType.equals(TxTypes.CUSTOMERORDER) || txtType.equals(TxTypes.LAYAWAY))) {
            tp.println("Original transaction is not of type Sale or Return or Layaway.").println("Transaction Type",
                    txtType);
            throw new JournalizationException("Original transaction is not of type Sale or Return or Layaway.",
                    new Exception(), ResultBase.RES_ERROR_TXINVALID);
        } else if (isTransactionReturned(transactionLink, connection, companyId, trainingMode)) {
            tp.println("Original transaction is already returned.");
            throw new JournalizationException("Original transaction is already returned.", new Exception(),
                    ResultBase.RES_ERROR_TXALREADYRETURNED);
        } else if (isTransactionVoided(transactionLink, connection, companyId, trainingMode)) {
            tp.println("Original transaction is already voided.");
            throw new JournalizationException("Original transaction is already voided.", new Exception(),
                    ResultBase.RES_ERROR_TXALREADYVOIDED);
        } else if (!(transaction.getBusinessDayDate() != transactionLink.getBusinessDayDate())) {
            tp.println("Original transaction was not registered on today.");
            throw new JournalizationException("Original transaction was not registered on today.", new Exception(),
                    ResultBase.RES_ERROR_TXINVALID);
        }

        savePosLogXML(transaction, posLogXml, TxTypes.VOID, savePOSLogStmt, connection, trainingMode);

        // insert void details to TXL_SALES_VOIDED
        saveVoidDetails(saveVoidDetailsStmt, transactionLink, companyId, trainingMode);

        // insert return details to TXU_TOTAL_GUESTTILLDAY
        // saveTxuTotalGuestTillDay(transaction, TxTypes.VOID,
        // saveTxuTotalGuestTillDayStmt, connection);

        tp.methodExit();
    }

    /**
     * Private Method for cancelling a transaction
     *
     * @param transaction
     *            The current transaction.
     * @param posLogXml
     *            The POSLog XML.
     * @param connection
     *            The database connection
     * @param savePOSLogStmt
     *            The Prepared Statement for inserting the POSLog XML for Return
     *            in the TXL_POSLOG
     *
     * @return void
     *
     * @throws Exception
     *             The Exception thrown when the process fails
     */
    private void doCancelTransaction(final Transaction transaction, final String posLogXml, final Connection connection,
            final PreparedStatement savePOSLogStmt, final int trainingMode)
                    throws SQLException, SQLStatementException, NamingException, DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        savePosLogXML(transaction, posLogXml, TxTypes.CANCEL, savePOSLogStmt, connection, trainingMode);

        tp.methodExit();
    }
    
    /**
     * Private Method for charge cancel
     *
     * @param transaction
     *            The current transaction.
     * @param posLogXml
     *            The POSLog XML.
     * @param connection
     *            The database connection
     * @param savePOSLogStmt
     *            The Prepared Statement for inserting the POSLog XML for Return
     *            in the TXL_POSLOG
     *
     * @return void
     *
     * @throws Exception
     *             The Exception thrown when the process fails
     */
    private void doChargeCancelTransaction(final Transaction transaction, final String posLogXml, final Connection connection,
            final PreparedStatement savePOSLogStmt, final int trainingMode)
                    throws SQLException, SQLStatementException, NamingException, DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        savePosLogXML(transaction, posLogXml, TxTypes.CHARGECANCEL, savePOSLogStmt, connection, trainingMode);

        tp.methodExit();
    }

    /**
     * Private Method that implement the chkPOSLogDuplicate of transaction.
     *
     * @param transaction
     *            The Transaction Information
     * @param connection
     *            The Database connection object
     *
     * @return The existence of the poslog.
     *
     * @throws Exception
     *             The Exception thrown when chkPOSLogDuplicate Transaction
     *             process fails.
     */
    private boolean chkPOSLogDuplicate(final Transaction transaction, final Connection connection,
            final int trainingMode) throws SQLException, SQLStatementException, DaoException {
        boolean found = false;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(sqlStatement.getProperty("check-poslog-duplicate"),
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            statement.setString(SQLStatement.PARAM1, transaction.getOrganizationHierarchy().getId());
            statement.setString(SQLStatement.PARAM2, transaction.getRetailStoreID());
            statement.setString(SQLStatement.PARAM3, transaction.getWorkStationID().getValue());
            statement.setString(SQLStatement.PARAM4, transaction.getSequenceNo());
            statement.setString(SQLStatement.PARAM5, transaction.getBusinessDayDate());
            statement.setInt(SQLStatement.PARAM6, trainingMode);

            resultSet = statement.executeQuery();
            if (resultSet.last() && (resultSet.getInt("rowcount") > 0)) {
                found = true;
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, "chkPOSLogDuplicate: Failed to check duplication.", e);
            throw e;
        } finally {
            closeConnectionObjects(null, statement, resultSet);
        }

        return found;
    }

    /**
     * Private Method for RETURNING the transaction.
     *
     * @param transaction
     *            The current transaction.
     * @param posLogXml
     *            The POSLog XML.
     * @param connection
     *            The database connection
     * @param savePOSLogStmt
     *            The Prepared Statement for inserting the POSLog XML for Return
     *            in the TXL_POSLOG
     * @param saveTxuTotalGuestTillDayStmt
     *            The Prepared Statement for saving The TXU_TOTAL_GUESTTILLDAY
     *            Table
     *
     * @return void
     *
     * @throws Exception
     *             The Exception thrown when the process fails
     */
    private void doReturnWithReceiptTransaction(final Transaction transaction, final String posLogXml,
            final Connection connection, final PreparedStatement savePOSLogStmt,
            final PreparedStatement saveVoidDetailsStmt, final PreparedStatement saveTxuTotalGuestTillDayStmt,
            final int trainingMode, final String txType)
                    throws SQLException, SQLStatementException, DaoException, JournalizationException, NamingException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        TransactionLink transactionLink = POSLogHandler.getNormalTransactionLink(transaction.getRetailTransaction().getTransactionLink());
        // String txtType = getTransactionType(connection,
        // transactionLink.getRetailStoreID(),
        // transactionLink.getWorkStationID().getValue(),
        // transactionLink.getSequenceNo(),
        // transactionLink.getBusinessDayDate());

        String companyId = transaction.getOrganizationHierarchy().getId();
        // if (txtType.isEmpty()) {
        // tp.println("Original transaction not found.")
        // .println("Retail Store Id", transactionLink.getRetailStoreID())
        // .println("Work Station Id", transactionLink.getWorkStationID())
        // .println("Sequence Number", transactionLink.getSequenceNo())
        // .println("Business Day Date",transactionLink.getBusinessDayDate());
        // throw new JournalizationException("Original transaction not found.",
        // new Exception(), ResultBase.RES_ERROR_TXNOTFOUND);
        // } else if (!(txtType.equals(TxTypes.SALES) ||
        // txtType.equals(TxTypes.EXCHANGESALES))) {
        // tp.println("Original transaction is not of type Sale.")
        // .println("Transaction Type", txtType);
        // throw new JournalizationException(
        // "Original transaction is not of type Sale.",
        // new Exception(), ResultBase.RES_ERROR_TXINVALID);
        // } else if (isTransactionVoided(transactionLink, connection,
        // companyId, trainingMode)) {
        // tp.println("Original transaction is already voided.");
        // throw new JournalizationException(
        // "Original transaction is already voided.",
        // new Exception(), ResultBase.RES_ERROR_TXALREADYVOIDED);
        // }

        savePosLogXML(transaction, posLogXml, txType, savePOSLogStmt, connection, trainingMode);

        // insert return details to TXL_SALES_VOIDED
        saveReturnDetails(saveVoidDetailsStmt, transactionLink, companyId, trainingMode);

        // insert return details to TXU_TOTAL_GUESTTILLDAY
        // saveTxuTotalGuestTillDay(transaction, TxTypes.RETURN,
        // saveTxuTotalGuestTillDayStmt, connection);

        tp.methodExit();
    }

    /**
     * Private Method for RETURNING a transaction without receipt.
     *
     * @param transaction
     *            The current transaction.
     * @param posLogXml
     *            The POSLog XML.
     * @param connection
     *            The database connection
     * @param savePOSLogStmt
     *            The Prepared Statement for inserting the POSLog XML for Return
     *            in the TXL_POSLOG
     * @param saveTxuTotalGuestTillDayStmt
     *            The Prepared Statement for saving The TXU_TOTAL_GUESTTILLDAY
     *            Table
     *
     * @return void
     *
     * @throws Exception
     *             The Exception thrown when the process fails
     */
    private void doReturnNoReceiptTransaction(final Transaction transaction, final String posLogXml,
            final Connection connection, final PreparedStatement savePOSLogStmt,
            final PreparedStatement saveTxuTotalGuestTillDayStmt, final int trainingMode, final String txType)
                    throws SQLException, SQLStatementException, NamingException, DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        savePosLogXML(transaction, posLogXml, txType, savePOSLogStmt, connection, trainingMode);

        // insert return details to TXU_TOTAL_GUESTTILLDAY
        // saveTxuTotalGuestTillDay(transaction, TxTypes.RETURN,
        // saveTxuTotalGuestTillDayStmt, connection);

        tp.methodExit();
    }

    /**
     * Private Method for Start of Day Transaction
     * This method updates RESMaster.dbo.MST_TILLIDINFO.SodFlag to 1.
     * @param transaction The current transaction.
     * @param connection The database connection
     * @throws DaoException
     * @throws TillException
     */
    private void doSODTransaction(final Transaction transaction,final Connection connection)
            throws DaoException, TillException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        ITillInfoDAO tillInfoDAO = daoFactory.getTillInfoDAO();
        Till currentTill = tillInfoDAO.fetchOne(
                transaction.getOrganizationHierarchy().getId(),
                transaction.getRetailStoreID(),
                transaction.getTillID());

        // Checks if the till exists on MST_TILLIDINFO.
        if( currentTill == null) {
            tp.methodExit();
            throw new TillException("TillException: @SQLServerPosLogDAO." + functionName + " Till does not exist.",
                    ResultBase.RES_TILL_NOT_EXIST);
        }

        // Checks if SOD is not finished yet.
        boolean isEnterprise = WindowsEnvironmentVariables.getInstance().isServerTypeEnterprise();
        if (!isEnterprise && TillInfoResource.SOD_FLAG_FINISHED.equals(currentTill.getSodFlag())) {
            tp.methodExit();
            throw new TillException("TillException: @SQLServerPosLogDAO." + functionName
                    + " SOD has already been performed for the Till.", ResultBase.RES_TILL_SOD_FINISHED);
        }

        // Sets new values for updating.
        Till updatingTill = new Till(currentTill);
        updatingTill.setSodFlag(TillInfoResource.SOD_FLAG_FINISHED);
        updatingTill.setEodFlag(TillInfoResource.EOD_FLAG_UNFINISHED);
        updatingTill.setBusinessDayDate(transaction.getBusinessDayDate());
        updatingTill.setUpdOpeCode(transaction.getOperatorID().getValue());

        tillInfoDAO.updateTillOnJourn(connection, currentTill, updatingTill, isEnterprise);
        tp.methodExit();
    }

    /**
     * Private Method for End of Day Transaction
     * This method updates RESMaster.dbo.MST_TILLIDINFO.EodFlag to 1.
     * @param transaction The current transaction.
     * @param connection The database connection
     * @throws DaoException
     * @throws TillException
     */
    private void doEODTransaction(final Transaction transaction, final Connection connection)
            throws DaoException, TillException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        ITillInfoDAO tillInfoDAO = daoFactory.getTillInfoDAO();
        Till currentTill = tillInfoDAO.fetchOne(
                transaction.getOrganizationHierarchy().getId(),
                transaction.getRetailStoreID(),
                transaction.getTillID());

        // Checks if the till exists on MST_TILLIDINFO.
        if( currentTill == null) {
            tp.methodExit();
            throw new TillException("TillException: @SQLServerPosLogDAO." + functionName + " Till does not exist.",
                    ResultBase.RES_TILL_NOT_EXIST);
        }

        // Checks if EOD is not finished yet.
        boolean isEnterprise = WindowsEnvironmentVariables.getInstance().isServerTypeEnterprise();
        if (!isEnterprise && TillInfoResource.EOD_FLAG_FINISHED.equals(currentTill.getEodFlag())) {
            tp.methodExit();
            throw new TillException("TillException: @SQLServerPosLogDAO." + functionName
                    + " EOD has already been performed for the Till.", ResultBase.RES_TILL_EOD_FINISHED);
        }

        // Sets new values for updating.
        Till updatingTill = new Till(currentTill);
        updatingTill.setSodFlag(TillInfoResource.SOD_FLAG_UNFINISHED);
        updatingTill.setEodFlag(TillInfoResource.EOD_FLAG_FINISHED);
        updatingTill.setBusinessDayDate(transaction.getBusinessDayDate());
        updatingTill.setUpdOpeCode(transaction.getOperatorID().getValue());

        tillInfoDAO.updateTillOnJourn(connection, currentTill, updatingTill, isEnterprise);
        tp.methodExit();
    }

    /**
     * Private Method for Loan Transaction
     *
     * @param transaction
     *            The current transaction.
     * @param posLogXml
     *            The POSLog XML.
     * @param connection
     *            The database connection
     * @param savePOSLogStmt
     *            The Prepared Statement for inserting the POSLog XML for Return
     *            in the TXL_POSLOG
     *
     * @return void
     *
     * @throws Exception
     *             The Exception thrown when the process fails
     */
    private void doLoanTransaction(final Transaction transaction, final String posLogXml, final Connection connection,
            final PreparedStatement savePOSLogStmt, final int trainingMode)
                    throws SQLException, SQLStatementException, NamingException, DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        savePosLogXML(transaction, posLogXml, TxTypes.LOAN, savePOSLogStmt, connection, trainingMode);

        tp.methodExit();
    }

    /**
     * Private Method for Pickup Transaction
     *
     * @param transaction
     *            The current transaction.
     * @param posLogXml
     *            The POSLog XML.
     * @param connection
     *            The database connection
     * @param savePOSLogStmt
     *            The Prepared Statement for inserting the POSLog XML for Return
     *            in the TXL_POSLOG
     *
     * @return void
     *
     * @throws Exception
     *             The Exception thrown when the process fails
     */
    private void doPickupTransaction(final Transaction transaction, final String posLogXml, final Connection connection,
            final PreparedStatement savePOSLogStmt, final int trainingMode)
                    throws SQLException, SQLStatementException, NamingException, DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        savePosLogXML(transaction, posLogXml, TxTypes.PICKUP, savePOSLogStmt, connection, trainingMode);

        tp.methodExit();
    }

    private void doSummaryReceiptTransaction(final Transaction transaction, final String posLogXml,
            final Connection connection, final PreparedStatement savePOSLogStmt,
            final PreparedStatement saveSummaryReceiptDetailsStmt, final int trainingMode)
                    throws SQLException, SQLStatementException, DaoException, JournalizationException, NamingException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        savePosLogXML(transaction, posLogXml, TxTypes.SUMMARY_RECEIPT, savePOSLogStmt, connection, trainingMode);

        TransactionLink transactionLink = transaction.getControlTransaction().getSummaryReceipt().getTransactionLink();
        // insert summaryreceipt details to TXL_SUMMARYRECEIPT_HISTORY
        saveSummaryReceiptDetailsStmt.setString(SQLStatement.PARAM1,
                transactionLink.getOrganizationHierarchy().getId());
        saveSummaryReceiptDetailsStmt.setString(SQLStatement.PARAM2, transactionLink.getRetailStoreID());
        saveSummaryReceiptDetailsStmt.setString(SQLStatement.PARAM3, transactionLink.getWorkStationID().getValue());
        saveSummaryReceiptDetailsStmt.setString(SQLStatement.PARAM4, transactionLink.getSequenceNo());
        saveSummaryReceiptDetailsStmt.setString(SQLStatement.PARAM5, transactionLink.getBusinessDayDate());
        saveSummaryReceiptDetailsStmt.setInt(SQLStatement.PARAM6,
                ("false".equals(transaction.getTrainingModeFlag())) ? 0 : 1);
        saveSummaryReceiptDetailsStmt.setString(SQLStatement.PARAM7, transaction.getRetailStoreID());
        saveSummaryReceiptDetailsStmt.setString(SQLStatement.PARAM8, transaction.getWorkStationID().getValue());
        saveSummaryReceiptDetailsStmt.setString(SQLStatement.PARAM9, transaction.getSequenceNo());
        saveSummaryReceiptDetailsStmt.setString(SQLStatement.PARAM10, transaction.getBusinessDayDate());
        saveSummaryReceiptDetailsStmt.setString(SQLStatement.PARAM11, transaction.getBeginDateTime());
        saveSummaryReceiptDetailsStmt.executeUpdate();

        tp.methodExit();
    }

    /**
     * Private Method for ReceiptReprint Transaction
     *
     * @param transaction
     *            The current transaction.
     * @param posLogXml
     *            The POSLog XML.
     * @param connection
     *            The database connection
     * @param savePOSLogStmt
     *            The Prepared Statement for inserting the POSLog XML for Return
     *            in the TXL_POSLOG
     *
     * @return void
     *
     * @throws Exception
     *             The Exception thrown when the process fails
     */
    private void doReceiptReprintTransaction(final Transaction transaction, final String posLogXml,
            final Connection connection, final PreparedStatement savePOSLogStmt, final int trainingMode)
                    throws SQLException, SQLStatementException, DaoException, JournalizationException, NamingException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());
        // save poslog
        savePosLogXML(transaction, posLogXml, TxTypes.RECEIPT_REPRINT, savePOSLogStmt, connection, trainingMode);

        TransactionLink transactionLink = transaction.getControlTransaction().getReceiptReprint().getTransactionLink();

        PreparedStatement saveReceiptReprintDetailsStmt = null;

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            saveReceiptReprintDetailsStmt = connection
                    .prepareStatement(sqlStatement.getProperty("save-receipt-reprint-details"));
            // insert receiptreprint details to TXL_RECEIPTREPRINT_HISTORY
            saveReceiptReprintDetailsStmt.setString(SQLStatement.PARAM1, transaction.getRetailStoreID());
            saveReceiptReprintDetailsStmt.setString(SQLStatement.PARAM2, transaction.getWorkStationID().getValue());
            saveReceiptReprintDetailsStmt.setString(SQLStatement.PARAM3, transaction.getSequenceNo());
            saveReceiptReprintDetailsStmt.setString(SQLStatement.PARAM4, transaction.getBusinessDayDate());
            saveReceiptReprintDetailsStmt.setString(SQLStatement.PARAM5, transactionLink.getRetailStoreID());
            saveReceiptReprintDetailsStmt.setString(SQLStatement.PARAM6, transactionLink.getWorkStationID().getValue());
            saveReceiptReprintDetailsStmt.setString(SQLStatement.PARAM7, transactionLink.getSequenceNo());
            saveReceiptReprintDetailsStmt.setString(SQLStatement.PARAM8, transactionLink.getBusinessDayDate());
            saveReceiptReprintDetailsStmt.setString(SQLStatement.PARAM9,
                    transaction.getOrganizationHierarchy().getId());
            saveReceiptReprintDetailsStmt.setInt(SQLStatement.PARAM10, trainingMode);

            saveReceiptReprintDetailsStmt.executeUpdate();
        } finally {
            closeConnectionObjects(null, saveReceiptReprintDetailsStmt, null);
            tp.methodExit();
        }
    }

    /**
     * Private Method for Saving Donation Transaction
     *
     * @param transaction
     *            The current transaction.
     * @param posLogXml
     *            The POSLog XML.
     * @param connection
     *            The database connection
     * @param savePOSLogStmt
     *            The Prepared Statement for inserting the POSLog XML for
     *            Donation in the TXL_POSLOG
     *
     * @return void
     *
     * @throws Exception
     *             The Exception thrown when the process fails
     */
    private void doDonationTransaction(final Transaction transaction, final String posLogXml,
            final Connection connection, final PreparedStatement savePOSLogStmt, final int trainingMode)
                    throws SQLException, SQLStatementException, NamingException, DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        savePosLogXML(transaction, posLogXml, TxTypes.PAYIN, savePOSLogStmt, connection, trainingMode);

        tp.methodExit();
    }

    /**
     * Private Method for Saving SignOn Transaction
     *
     * @param transaction
     *            The current transaction.
     * @param posLogXml
     *            The POSLog XML.
     * @param connection
     *            The database connection
     * @param savePOSLogStmt
     *            The Prepared Statement for inserting the POSLog XML for
     *            Donation in the TXL_POSLOG
     *
     * @return void
     *
     * @throws Exception
     *             The Exception thrown when the process fails
     */
    private void doSignOnTransaction(final Transaction transaction, final String posLogXml, final Connection connection,
            final PreparedStatement savePOSLogStmt, final int trainingMode)
                    throws SQLException, SQLStatementException, NamingException, DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        savePosLogXML(transaction, posLogXml, TxTypes.SIGNON, savePOSLogStmt, connection, trainingMode);

        if (transaction.getControlTransaction() != null
                && transaction.getControlTransaction().getOperatorSignOn() != null) {
            String json = transaction.getControlTransaction().getOperatorSignOn().getSoftwareVersion();
            if (!StringUtility.isNullOrEmpty(json)) {
                updateSoftwareVersion(transaction, EasyJson.toMap(json), connection);
            }
        }

        updatePosCtrlOpeCode(transaction, transaction.getOperatorID().getValue(), connection);

        tp.methodExit();
    }

    /**
     * Private Method for Saving SignOff Transaction
     *
     * @param transaction
     *            The current transaction.
     * @param posLogXml
     *            The POSLog XML.
     * @param connection
     *            The database connection
     * @param savePOSLogStmt
     *            The Prepared Statement for inserting the POSLog XML for
     *            Donation in the TXL_POSLOG
     *
     * @return void
     *
     * @throws Exception
     *             The Exception thrown when the process fails
     */
    private void doSignOffTransaction(final Transaction transaction, final String posLogXml,
            final Connection connection, final PreparedStatement savePOSLogStmt, final int trainingMode)
                    throws SQLException, SQLStatementException, NamingException, DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        savePosLogXML(transaction, posLogXml, TxTypes.SIGNOFF, savePOSLogStmt, connection, trainingMode);

        updatePosCtrlOpeCode(transaction, null, connection);

        tp.methodExit();
    }

    /**
     * Private Method for POSSOD Transaction.
     * @param transaction
     *            The current transaction.
     * @param posLogXml
     *            The POSLog XML.
     * @param connection
     *            The database connection
     * @param savePOSLogStmt
     *            The Prepared Statement for inserting the POSLog XML for
     *            Donation in the TXL_POSLOG
     * @param trainingMode
     * @throws SQLException
     * @throws SQLStatementException
     * @throws NamingException
     * @throws DaoException
     * @throws ParseException
     *             The Exception thrown when the process fails
     */
    private void doPosSodTransaction(final Transaction transaction, final String posLogXml,
                                     final Connection connection, final PreparedStatement savePOSLogStmt, final int trainingMode)
            throws SQLException, SQLStatementException, NamingException, DaoException, ParseException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());
        savePosLogXML(transaction, posLogXml, TxTypes.POSSOD, savePOSLogStmt, connection, trainingMode);
        // Updates TXU_POS_CTRL.OpenCloseStat, SodTranNo and SodTime.
        updatePosCtrlDailyOperation(transaction, connection, POS_CTRL_UPDATE_TYPE_SOD);
        tp.methodExit();
    }

    /**
     * Private Method for POSEOD Transaction.
     * @param transaction
     *            The current transaction.
     * @param posLogXml
     *            The POSLog XML.
     * @param connection
     *            The database connection
     * @param savePOSLogStmt
     *            The Prepared Statement for inserting the POSLog XML for
     *            Donation in the TXL_POSLOG
     * @param trainingMode
     * @throws SQLException
     * @throws SQLStatementException
     * @throws NamingException
     * @throws DaoException
     * @throws ParseException
     *             The Exception thrown when the process fails
     */
    private void doPosEodTransaction(final Transaction transaction, final String posLogXml,
                                      final Connection connection, final PreparedStatement savePOSLogStmt, final int trainingMode)
            throws SQLException, SQLStatementException, NamingException, DaoException, ParseException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());
        savePosLogXML(transaction, posLogXml, TxTypes.POSEOD, savePOSLogStmt, connection, trainingMode);
        // Updates TXU_POS_CTRL.OpenCloseStat, EodTranNo and EodTime.
        updatePosCtrlDailyOperation(transaction, connection, POS_CTRL_UPDATE_TYPE_EOD);
        tp.methodExit();
    }


    /**
     * Private Method for Saving AutoSignOff Transaction
     *
     * @param transaction
     *            The current transaction.
     * @param posLogXml
     *            The POSLog XML.
     * @param connection
     *            The database connection
     * @param savePOSLogStmt
     *            The Prepared Statement for inserting the POSLog XML for
     *            Donation in the TXL_POSLOG
     *
     * @return void
     *
     * @throws Exception
     *             The Exception thrown when the process fails
     */
    private void doAutoSignOffTransaction(final Transaction transaction, final String posLogXml,
            final Connection connection, final PreparedStatement savePOSLogStmt, final int trainingMode)
                    throws SQLException, SQLStatementException, NamingException, DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        savePosLogXML(transaction, posLogXml, TxTypes.AUTOSIGNOFF, savePOSLogStmt, connection, trainingMode);

        updatePosCtrlOpeCode(transaction, null, connection);

        tp.methodExit();
    }

    /**
     * Private Method for Saving Other Transaction Type
     *
     * @param transaction
     *            The current transaction.
     * @param posLogXml
     *            The POSLog XML.
     * @param connection
     *            The database connection
     * @param savePOSLogStmt
     *            The Prepared Statement for inserting the POSLog XML for
     *            Donation in the TXL_POSLOG
     *
     * @return void
     *
     * @throws Exception
     *             The Exception thrown when the process fails
     */
    private void doOtherTransaction(final Transaction transaction, final String posLogXml, final Connection connection,
            final PreparedStatement savePOSLogStmt, final int trainingMode)
                    throws SQLException, SQLStatementException, NamingException, DaoException {
        tp.methodEnter(DebugLogger.getCurrentMethodName());

        savePosLogXML(transaction, posLogXml, TxTypes.OTHER, savePOSLogStmt, connection, trainingMode);

        tp.methodExit();
    }

    /**
     * Do the POSLog Journalization of any type of transaction.
     *
     * @param posLog
     *            The POSLog which holds the transaction to be processed.
     * @param posLogXml
     *            The same POSLog but in xml format.
     *
     * @return void
     *
     * @throws Exception
     *             The exception thrown when the process fail.
     */
    public final void savePOSLog(final PosLog posLog, final String posLogXml, final int trainingMode)
            throws DaoException, JournalizationException, TillException,
            ParseException,SQLStatementException, NamingException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("poslogxml", posLogXml);

        Connection connection = null;
        PreparedStatement savePOSLogStmt = null;
        PreparedStatement saveVoidDetailsStmt = null;
        PreparedStatement saveSummaryReceiptDetailsStmt = null;
        PreparedStatement saveTxuTotalGuestTillDayStmt = null;

        Transaction transaction = posLog.getTransaction();

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            savePOSLogStmt = connection.prepareStatement(sqlStatement.getProperty("save-poslog"));
            saveVoidDetailsStmt = connection.prepareStatement(sqlStatement.getProperty("save-void-details"));
            saveSummaryReceiptDetailsStmt = connection
                    .prepareStatement(sqlStatement.getProperty("save-summary-receipt-details"));
            saveTxuTotalGuestTillDayStmt = connection
                    .prepareStatement(sqlStatement.getProperty("save-TxuTotalGuestTillDay-details"));

            // String transactionType =
            // POSLogHandler.getTransactionType(posLog);
            String transactionType = transaction.getTransactionType();
            boolean isTxEODorSOD = false;

            if (transactionType.equalsIgnoreCase(TxTypes.SOD)) {
                // Updates MST_TILL_INFO.SodFlag.
                doSODTransaction(transaction, connection);
                // Updates TXU_POS_CTRL.OpenCloseStat.
                updatePosCtrlDailyOperation(transaction, connection, POS_CTRL_UPDATE_TYPE_SOD);
                isTxEODorSOD = true;
            } else if (transactionType.equalsIgnoreCase(TxTypes.EOD)) {
                // Updates MST_TILL_INFO.EodFlag.
                doEODTransaction(transaction, connection);
                // Updates TXU_POS_CTRL.OpenCloseStat.
                updatePosCtrlDailyOperation(transaction, connection, POS_CTRL_UPDATE_TYPE_EOD);
                isTxEODorSOD = true;
            }

            if (!chkPOSLogDuplicate(transaction, connection, trainingMode)) {
                switch (transactionType) {
                case TxTypes.SALES:
                case TxTypes.ECSALES:
                case TxTypes.EXCHANGESALES:
                case TxTypes.GIFTCARDINQUIRY:
                case TxTypes.GIFTCARDRECHARGE:
                    doNormalTransaction(transaction, posLogXml, savePOSLogStmt, connection,
                            saveTxuTotalGuestTillDayStmt, trainingMode, transactionType);
                    break;
                case TxTypes.RETURN:
                case TxTypes.ECRETURN:
                case TxTypes.EXCHANGERETURN:
                    if (transaction.getRetailTransaction().getTransactionLink() != null) { // return with receipt
                        TransactionLink transactionLink = POSLogHandler.getNormalTransactionLink(transaction.getRetailTransaction().getTransactionLink());
                        if (transactionLink.getSequenceNo() == null) {
                            doReturnNoReceiptTransaction(transaction, posLogXml, connection, savePOSLogStmt,
                                    saveTxuTotalGuestTillDayStmt, trainingMode, transactionType);
                        } else {
                            doReturnWithReceiptTransaction(transaction, posLogXml, connection, savePOSLogStmt,
                                    saveVoidDetailsStmt, saveTxuTotalGuestTillDayStmt, trainingMode, transactionType);
                        }
                    } else { // return without receipt
                        doReturnNoReceiptTransaction(transaction, posLogXml, connection, savePOSLogStmt,
                                saveTxuTotalGuestTillDayStmt, trainingMode, transactionType);
                    }
                    break;
                case TxTypes.CANCEL:
                    doCancelTransaction(transaction, posLogXml, connection, savePOSLogStmt, trainingMode);
                    break;
                case TxTypes.CHARGECANCEL:
                    doChargeCancelTransaction(transaction, posLogXml, connection, savePOSLogStmt, trainingMode);
                    break;
                case TxTypes.VOID:
                case TxTypes.RETURNVOID:
                case TxTypes.EXCHANGESALESVOID:
                case TxTypes.EXCHANGERETURNVOID:
                case TxTypes.REMAINEDVOID:
                case TxTypes.PURCHASEVOID:
                case TxTypes.PURCHASERETURNVOID:
                case TxTypes.CHARGESALESVOID:
                    doVoidTransactionByTxTypes(transaction, posLogXml, transactionType, savePOSLogStmt,
                            saveVoidDetailsStmt, connection, trainingMode);
                    break;
                case TxTypes.LOAN:
                    doLoanTransaction(transaction, posLogXml, connection, savePOSLogStmt, trainingMode);
                    break;
                case TxTypes.PICKUP:
                    doPickupTransaction(transaction, posLogXml, connection, savePOSLogStmt, trainingMode);
                    break;
                case TxTypes.SUMMARY_RECEIPT:
                    doSummaryReceiptTransaction(transaction, posLogXml, connection, savePOSLogStmt,
                            saveSummaryReceiptDetailsStmt, trainingMode);
                    break;
                case TxTypes.PAYIN:
                    doDonationTransaction(transaction, posLogXml, connection, savePOSLogStmt, trainingMode);
                    break;
                case TxTypes.SIGNOFF:
                    doSignOffTransaction(transaction, posLogXml, connection, savePOSLogStmt, trainingMode);
                    break;
                case TxTypes.SIGNON:
                    doSignOnTransaction(transaction, posLogXml, connection, savePOSLogStmt, trainingMode);
                    break;
                case TxTypes.AUTOSIGNOFF:
                    doAutoSignOffTransaction(transaction, posLogXml, connection, savePOSLogStmt, trainingMode);
                    break;
                case TxTypes.RECEIPT_REPRINT:
                    doReceiptReprintTransaction(transaction, posLogXml, connection, savePOSLogStmt, trainingMode);
                    break;
                case TxTypes.POSSOD:
                    doPosSodTransaction(transaction, posLogXml, connection, savePOSLogStmt, trainingMode);
                    break;
                case TxTypes.POSEOD:
                    doPosEodTransaction(transaction, posLogXml, connection, savePOSLogStmt, trainingMode);
                    break;
                case TxTypes.SOD:
                case TxTypes.EOD:
                case TxTypes.EXCHANGE:
                case TxTypes.INQ:
                case TxTypes.CASHINDRAWER:
                case TxTypes.LAYAWAY:
                case TxTypes.HOLD:
                case TxTypes.RESERVATION:
                case TxTypes.CUSTOMERORDER:
                case TxTypes.CASHTODRAWER:
                case TxTypes.BALANCING:
                case TxTypes.PAYOUT:
                case TxTypes.GUARANTEE:
                case TxTypes.NEWMEMBER:
                case TxTypes.POSTPOINT:
                case TxTypes.CARDSWITCH:
                case TxTypes.CARDSTOP:
                case TxTypes.CARDMERGE:
                case TxTypes.POINTTICKET:
                case TxTypes.POSTPOINTVOID:
                case TxTypes.REMAINED:
                case TxTypes.LAYAWAYVOID:
                case TxTypes.PLUCHANGE:
                case TxTypes.PURCHASE:
                case TxTypes.PURCHASERETURN:
                case TxTypes.CASHIN:
                case TxTypes.CASHINVOID:
                case TxTypes.CASHOUT:
                case TxTypes.CASHOUTVOID:
                case TxTypes.CHARGESALES:
                case TxTypes.SUSPEND:
                case TxTypes.STOREEOD:
                case TxTypes.DAILYREPORT:
                case TxTypes.OPESUMDAY:
                case TxTypes.SUMTIME:
                case TxTypes.GROUPSUMDAY:
                case TxTypes.DPTSUMDAY:
                case TxTypes.GUESTSUMDAY:
                    savePosLogXML(transaction, posLogXml, transactionType, savePOSLogStmt, connection, trainingMode);
                    break;
                default:
                    doOtherTransaction(transaction, posLogXml, connection, savePOSLogStmt, trainingMode);
                }
                connection.commit();
            } else {
                if (isTxEODorSOD) {
                    connection.commit();
                }
                tp.println("Duplicate POSLog transaction.").println("Retail Store Id", transaction.getRetailStoreID())
                        .println("Work Station Id", transaction.getWorkStationID())
                        .println("Sequence Number", transaction.getSequenceNo())
                        .println("Business Day Date", transaction.getBusinessDayDate());
                LOGGER.logWarning(PROG_NAME, functionName, Logger.RES_ERROR_RESTRICTION,
                        "Duplicate POSLog Transaction");
                Snap.SnapInfo duplicatePOSLog = snap.write("Duplicate POSLog Transaction", posLogXml);
                LOGGER.logSnap(IoWriter.WARNING, PROG_NAME, functionName,
                        "Duplicate POSLog Transaction to snap file", duplicatePOSLog);
            }
        } catch (SQLStatementException sqlStmtEx) {
            rollBack(connection, "SQLServerPosLogDAO: @doPOSLogJournalization()", sqlStmtEx);
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT, functionName + ": Failed to save transaction.",
                    sqlStmtEx);
            throw sqlStmtEx;
        } catch (SQLException sqlEx) {
            rollBack(connection, "SQLServerPosLogDAO: @doPOSLogJournalization()", sqlEx);
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to save transaction.", sqlEx);
            throw new DaoException("SQLException: @doPOSLogJournalization - " + sqlEx.getMessage(), sqlEx);
        } catch (NamingException e) {
            rollBack(connection, "SQLServerPosLogDAO:" + " @doPOSLogJournalization()", e);
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_NAMINGEXC, functionName + ": Unable to find serverID from context parameters.", e);
            throw e;
        } catch (TillException tEx) {
            rollBack(connection, "SQLServerPosLogDAO:" + " @doPOSLogJournalization()", tEx);
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_TILL, functionName + ": Failed to update Till.", tEx);
            throw tEx;
        } catch (DaoException e) {
            if (e.getErrorCode() == ResultBase.RES_SYSTEM_PROP_ERROR) {
                rollBack(connection, "SQLServerPosLogDAO:" + " @doPOSLogJournalization()", e);
            }
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_DAO, functionName + ": Failed to save transaction.", e);
            throw new DaoException("Failed to save transaction. - " + e.getMessage(), e);
        } catch (ParseException parseEx) {
            // Failed by ParseException thrown by invalid format of BusinessDayDate or BeginDateTime .
            rollBack(connection, "SQLServerPosLogDAO:" + " @doPOSLogJournalization()", parseEx);
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_PARSE, functionName + ": Failed to update PosCtrl.", parseEx);
            throw parseEx;
        } catch (JournalizationException je) {
            rollBack(connection, "SQLServerPosLogDAO:" + " @doPOSLogJournalization()", je);
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_JOURNAL, functionName + ": Failed to update PosCtrl.", je);
            throw je;
        } finally {
            closeConnectionObjects(null, savePOSLogStmt, null);
            closeConnectionObjects(null, saveSummaryReceiptDetailsStmt, null);
            closeConnectionObjects(null, saveTxuTotalGuestTillDayStmt, null);
            closeConnectionObjects(connection, saveVoidDetailsStmt, null);

            tp.methodExit();
        }
    }

    /**
     * Get the transaction POSLog XML by specifying the transaction number.
     *
     * @param txDeviceNumber
     *            The ID of the device used to perform the target transaction
     * @param txNumber
     *            The Transaction Number
     * @param connection
     *            The Database Connection
     * @return The POSLog XML representation for the Transaction
     * @throws DaoException
     *             The Exception thrown when the getting of Transaction fails.
     */
    private String getTransaction(final String txDeviceNumber, final String txNumber, final Connection connection)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("Transaction Number", txNumber).println("Device Number", txDeviceNumber);
        PreparedStatement select = null;
        ResultSet result = null;
        String posLogXML = "";

        try {
            /*
             * Retrieves sql query statement from
             * /resource/ncr.res.webuitools.property/sql_statements.xml
             */
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-poslog-xml-by-id"));

            select.setString(SQLStatement.PARAM1, txDeviceNumber);
            select.setString(SQLStatement.PARAM2, txNumber);
            result = select.executeQuery();

            if (result.next()) {
                posLogXML = result.getString(result.findColumn("tx"));
            } else {
                tp.println("No Transaction found.");
            }
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get transaction's poslogxml.", ex);
            throw new DaoException("Exception: @getTransaction - " + ex.getMessage(), ex);
        } finally {
            closeConnectionObjects(null, select, result);
        }
        return tp.methodExit(posLogXML);
    }

    /**
     * Get the transaction POSLog XML by specifying the transaction number.
     *
     * @param companyid
     *            The Company ID
     * @param trainingflag
     *            The Training Flag
     * @param workstationid
     *            The Device Terminal ID
     * @param storeid
     *            The Store ID
     * @param txid
     *            The Transaction Number
     * @param connection
     *            The Database Connection
     * @return The POSLog XML representation for the Transaction
     * @throws DaoException
     *             The Exception thrown when the getting of Transaction fails.
     */
    private String getTransaction(final String companyid, final String storeid, final String workstationid,
            final String businessdate, final String txid, final int trainingflag, final String txtype,
            final Connection connection) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", companyid).println("StoreID", storeid)
                .println("WorkstationId", workstationid).println("BusinessDate", businessdate)
                .println("Transaction Number", txid).println("TrainingMode", trainingflag).println("TxType", txtype);
        PreparedStatement select = null;
        ResultSet result = null;
        String posLogXML = "";

        try {
            /*
             * Retrieves sql query statement from
             * /resource/ncr.res.webuitools.property/sql_statements.xml
             */
            SQLStatement sqlStatement = SQLStatement.getInstance();
            if (StringUtility.isNullOrEmpty(companyid)) {
                select = connection
                        .prepareStatement(sqlStatement.getProperty("get-poslog-xml-by-terminalid-storeid-txid"));
                select.setString(SQLStatement.PARAM1, storeid);
                select.setString(SQLStatement.PARAM2, workstationid);
                select.setString(SQLStatement.PARAM3, txid);
                select.setInt(SQLStatement.PARAM4, trainingflag);
            } else {
                select = connection.prepareStatement(
                        sqlStatement.getProperty("get-poslog-xml-by-companyid-terminalid-storeid-txid"));
                select.setString(SQLStatement.PARAM1, companyid);
                select.setString(SQLStatement.PARAM2, storeid);
                select.setString(SQLStatement.PARAM3, workstationid);
                select.setString(SQLStatement.PARAM4, businessdate);
                select.setString(SQLStatement.PARAM5, txid);
                select.setInt(SQLStatement.PARAM6, trainingflag);
                select.setString(SQLStatement.PARAM7, txtype);

            }

            result = select.executeQuery();
            if (result.next()) {
                posLogXML = result.getString(result.findColumn("tx"));
            } else {
                tp.println("No transaction found.");
            }
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get transaction's xml.",
                    ex);
            throw new DaoException("Exception: @getTransaction - " + ex.getMessage(), ex);
        } finally {
            closeConnectionObjects(null, select, result);
        }
        return tp.methodExit(posLogXML);
    }

    private boolean isTransactionVoided(TransactionLink transactionLink, Connection connection, final String companyId,
            final int trainingMode) throws SQLException, SQLStatementException, DaoException {
        return isTransactionVoidedOrReturned(transactionLink, connection, TxTypes.VOID, companyId, trainingMode);
    }

    private boolean isTransactionReturned(TransactionLink transactionLink, Connection connection,
            final String companyId, final int trainingMode) throws SQLException, SQLStatementException, DaoException {
        return isTransactionVoidedOrReturned(transactionLink, connection, TxTypes.RETURN, companyId, trainingMode);
    }

    private boolean isTransactionVoidedOrReturned(TransactionLink transactionLink, Connection connection, String type,
            String companyId, int trainingMode) throws SQLException, SQLStatementException, DaoException {
        boolean isAlreadyVoidedOrReturned = false;
        PreparedStatement statement = null;
        ResultSet resultSet = null;

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(sqlStatement.getProperty("get-void-details"),
                    ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);

            statement.setString(SQLStatement.PARAM1, transactionLink.getRetailStoreID());
            statement.setString(SQLStatement.PARAM2, transactionLink.getWorkStationID().getValue());
            statement.setString(SQLStatement.PARAM3, transactionLink.getSequenceNo());
            statement.setString(SQLStatement.PARAM4, transactionLink.getBusinessDayDate());
            statement.setString(SQLStatement.PARAM5, type);
            statement.setString(SQLStatement.PARAM6, companyId);
            statement.setInt(SQLStatement.PARAM7, trainingMode);

            resultSet = statement.executeQuery();
            if (resultSet.last() && (resultSet.getInt("rowcount") > 0)) {
                isAlreadyVoidedOrReturned = true;
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, "isTransactionVoidedOrReturned: Error in checking if "
                    + "transaction was already voided or returned.", e);
            throw e;
        } finally {
            closeConnectionObjects(null, statement, resultSet);
        }

        return isAlreadyVoidedOrReturned;
    }

    static final String[] VERSION_ENTITIES = { "Container", "MobileShop", "RESTransaction", "RESTabletUI",
            "RESUiConfig" };

    void updateSoftwareVersion(Transaction transaction, Map<String, String> version, Connection connection)
            throws SQLException, SQLStatementException, DaoException {
        PreparedStatement createUpdate = null;
        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            createUpdate = connection.prepareStatement(sqlStatement.getProperty("update-software-version"));
            createUpdate.setString(SQLStatement.PARAM1, transaction.getOrganizationHierarchy().getId());
            createUpdate.setString(SQLStatement.PARAM2, transaction.getRetailStoreID());
            createUpdate.setString(SQLStatement.PARAM3, transaction.getWorkStationID().getValue());
            for (int i = 0; i < VERSION_ENTITIES.length; i++) {
                createUpdate.setString(SQLStatement.PARAM4 + i, version.get(VERSION_ENTITIES[i]));
            }
            createUpdate.executeUpdate();
        } finally {
            closeConnectionObjects(null, createUpdate, null);
        }
    }

    private void updatePosCtrlOpeCode(Transaction transaction, String operatorId, Connection connection)
            throws SQLException, SQLStatementException, DaoException {
        PreparedStatement createUpdate = null;
        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            createUpdate = connection.prepareStatement(sqlStatement.getProperty("create-update-pos-opecode"));
            createUpdate.setString(SQLStatement.PARAM1, transaction.getOrganizationHierarchy().getId());
            createUpdate.setString(SQLStatement.PARAM2, transaction.getRetailStoreID());
            createUpdate.setString(SQLStatement.PARAM3, transaction.getWorkStationID().getValue());
            createUpdate.setString(SQLStatement.PARAM4, operatorId);

            createUpdate.executeUpdate();
        } finally {
            closeConnectionObjects(null, createUpdate, null);
        }
    }

    private void updatePosCtrlDailyOperation(Transaction transaction, Connection connection, int updateType)
            throws SQLStatementException, SQLException, DaoException, ParseException {
        PreparedStatement createUpdate = null;
        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            createUpdate = connection.prepareStatement(sqlStatement.getProperty(
                    updateType == POS_CTRL_UPDATE_TYPE_SOD ? "create-update-pos-sod-state" : "create-update-pos-eod-state"));

            createUpdate.setString(SQLStatement.PARAM1, transaction.getOrganizationHierarchy().getId());
            createUpdate.setString(SQLStatement.PARAM2, transaction.getRetailStoreID());
            createUpdate.setString(SQLStatement.PARAM3, transaction.getWorkStationID().getValue());
            createUpdate.setShort(SQLStatement.PARAM4,
                    updateType == POS_CTRL_UPDATE_TYPE_SOD ? OPEN_CLOSE_STAT_SOD : OPEN_CLOSE_STAT_EOD);

            // Concats the first part of BusinessDayDate and the last part of BeginDateTime.
            String concatBusinessDate = transaction.getBusinessDayDate()
                    + transaction.getBeginDateTime().substring(transaction.getBusinessDayDate().length());
            Timestamp businessDateTime = new Timestamp(DateFormatUtility.parseDate(concatBusinessDate,"yyyy-MM-dd'T'HH:mm:ss").getTime());
            createUpdate.setTimestamp(SQLStatement.PARAM5, businessDateTime);

            createUpdate.setString(SQLStatement.PARAM6, transaction.getSequenceNo());
            createUpdate.executeUpdate();
        } finally {
            closeConnectionObjects(null, createUpdate, null);
        }
    }

    /**
     * Gets a POSLog XML by specifying the transaction number.
     *
     * @param txDeviceNumber
     *            The ID of the device used to perform the target transaction
     * @param txNumber
     *            The transaction Number
     * @return The corresponding POSLog Xml of the given Transaction
     * @throws DaoException
     *             The Exception when getting of the POSLog XML failed.
     */
    @Override
    public final String getTransaction(final String txDeviceNumber, final String txNumber) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("Transaction Deviec Number", txDeviceNumber).println("Transaction Number",
                txNumber);

        Connection connection = null;
        String posLogXML = "";
        try {
            connection = dbManager.getConnection();
            posLogXML = getTransaction(txDeviceNumber, txNumber, connection);
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get transaction's xml.", ex);
            throw new DaoException("SQLException:" + " @SQLServerPosLogDao.gettransaction", ex);
        } finally {
            closeConnectionObjects(connection, null, null);
        }
        return tp.methodExit(posLogXML);
    }

    /**
     * Get the transaction POSLog XML in the TXL_POSLOG table. by specifying the
     * transaction number.
     *
     * @param companyid
     *            The Company ID
     * @param workstationid
     *            The Terminal ID
     * @param storeid
     *            The Store ID
     * @param trainingflag
     *            The Training Flag
     * @param txid
     *            The Transaction Number
     * @return The POSLog XML, else, empty string with transaction not found.
     * @throws DaoException
     *             Exception thrown when setting up the prepared Statement
     *             fails.
     */
    @Override
    public final String getPOSLogTransaction(final String companyid, final String storeid, final String workstationid,
            final String businessdate, final String txid, final int trainingflag, final String txtype)
                    throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", companyid).println("StoreID", storeid)
                .println("WorkstationId", workstationid).println("BusinessDate", businessdate)
                .println("Transaction Number", txid).println("TrainingMode", trainingflag).println("TxType", txtype);

        Connection connection = null;
        String posLogXML = "";

        try {
            connection = dbManager.getConnection();
            posLogXML = getTransaction(companyid, storeid, workstationid, businessdate, txid, trainingflag, txtype,
                    connection);
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get transaction's xml.", ex);
            throw new DaoException("SQLException: @" + "SQLServerPosLogDao.gettransaction", ex);
        } finally {
            closeConnectionObjects(connection, null, null);
        }
        return tp.methodExit(posLogXML);
    }

    /**
     * Get the BussinessDate of the Web API.
     *
     * @param companyId
     *            the company ID
     * @param storeId
     *            the store ID
     * @param switchTime
     *            can be an empty string
     * @return The Business Date
     * @throws DaoException
     *             The Exception thrown when error occur.
     */
    @Override
    public final String getBussinessDate(final String companyId, final String storeId, final String switchTime)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", companyId).println("StoreId", storeId).println("SwitchTime",
                switchTime);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;
        String strResult = null;
        try {
            if (null != switchTime && !switchTime.isEmpty()) {
                String switchTimeOnly = switchTime.replace("am", "").replace("pm", "");
                DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");

                int colonIndex = switchTimeOnly.indexOf(":");
                int switchTimeHour = Integer.parseInt(switchTimeOnly.substring(0, colonIndex));
                int switchTimeMin = Integer.parseInt(switchTimeOnly.substring(colonIndex + 1));

                Calendar swCal1 = Calendar.getInstance(Locale.ENGLISH);
                Calendar sysCal1 = Calendar.getInstance(Locale.ENGLISH);
                if (switchTime.contains("pm")) {
                    switchTimeHour += TWELVECLOCKTIME;
                }
                swCal1.set(Calendar.HOUR_OF_DAY, switchTimeHour);
                swCal1.set(Calendar.MINUTE, switchTimeMin);
                swCal1.set(Calendar.SECOND, 0);

                if (sysCal1.getTime().compareTo(swCal1.getTime()) < 0) {
                    sysCal1.add(Calendar.DATE, -1);
                    strResult = dateFormat.format(sysCal1.getTime());
                } else {
                    strResult = dateFormat.format(sysCal1.getTime());
                }
            } else {
                connection = dbManager.getConnection();
                SQLStatement sqlStatement = SQLStatement.getInstance();
                selectStmnt = connection.prepareStatement(sqlStatement.getProperty("get-bussiness-date"));
                selectStmnt.setString(SQLStatement.PARAM1, companyId);
                selectStmnt.setString(SQLStatement.PARAM2, storeId);
                resultSet = selectStmnt.executeQuery();
                if (resultSet.next()) {
                    strResult = resultSet.getDate("TodayDate").toString();
                }
            }
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get businessdaydate.", ex);
            throw new DaoException("Exception: @getBussinessDate - " + ex.getMessage(), ex);
        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);
        }
        return tp.methodExit(strResult);
    }

    @Override
    public final List<TransactionSearch> searchTransactions(final String limit, final String from, final String line,
            final String storeId, final String deviceId, final String itemName, final String subCode4,
            final String itemId, final String sequenceNumberFrom, final String sequenceNumberTo,
            final String businessDate, final String fromDate, final String fromTime, final String toDate,
            final String toTime, final String type, final String companyId, final int trainingMode)
                    throws DaoException, ParseException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("limit", limit).println("from", from).println("line", line)
                .println("storeId", storeId).println("deviceId", deviceId).println("itemName", itemName)
                .println("subCode4", subCode4).println("itemId", itemId)
                .println("sequenceNumberFrom", sequenceNumberFrom).println("sequenceNumberTo", sequenceNumberTo)
                .println("businessDate", businessDate).println("fromDate", fromDate).println("fromTime", fromTime)
                .println("toDate", toDate).println("toTime", toTime).println("type", type)
                .println("companyId", companyId).println("trainingMode", trainingMode);

        DAOFactory daoFactory = DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
        ISystemSettingDAO systemSettingDAO = daoFactory.getSystemSettingDAO();
        DateSetting dateSetting = systemSettingDAO.getDateSetting(companyId, storeId);
        tp.println("bizdate", dateSetting.getToday());

        List<TransactionSearch> txPosLogs = new ArrayList<TransactionSearch>();
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement select = null;

        try {
            connection = dbManager.getConnection();

            int limitIntValue, limitFrom;
            if (StringUtility.isNullOrEmpty(limit)) {
                limitIntValue = 50;
            } else {
                limitIntValue = Integer.parseInt(limit);
            }
            if (!StringUtility.isNullOrEmpty(from)) {
                limitFrom = Integer.parseInt(from);
            } else {
                limitFrom = 0;
            }
            int sequenceNumber = 0;
            String tempSequenceNumberFrom = null;
            if (!StringUtility.isNullOrEmpty(sequenceNumberFrom)) {
                sequenceNumber = Integer.parseInt(sequenceNumberFrom);
                tempSequenceNumberFrom = String.valueOf(sequenceNumber);
            }

            String tempSequenceNumberTo = null;
            if (!StringUtility.isNullOrEmpty(sequenceNumberTo)) {
                sequenceNumber = Integer.parseInt(sequenceNumberTo);
                tempSequenceNumberTo = String.valueOf(sequenceNumber);
            }

            if (!StringUtility.isNullOrEmpty(businessDate)
                    && !DateFormatUtility.isLegalFormat(businessDate, "yyyy-MM-dd")) {
                tp.println("Date should have yyyy-MM-dd format.");
                throw new ParseException("Invalid date format.", 0);
            }

            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date date = new Date();
            String currentDate = dateFormat.format(date);

            String fromDateTime = null;
            if (!StringUtility.isNullOrEmpty(fromDate)) {
                if (!DateFormatUtility.isLegalFormat(fromDate, "yyyy-MM-dd")) {
                    tp.println("Date should have yyyy-MM-dd format.");
                    throw new ParseException("Invalid date format.", 0);
                } else {
                    fromDateTime = fromDate;
                }
            }

            if (!StringUtility.isNullOrEmpty(fromTime)) {
                if (!DateFormatUtility.isLegalFormat(fromTime, "HH:mm:ss")) {
                    tp.println("Time should have HH:mm:ss format.");
                    throw new ParseException("Invalid time format.", 0);
                } else {
                    if (fromDateTime != null) {
                        fromDateTime += " " + fromTime;
                    } else {
                        // use current date
                        fromDateTime = currentDate + " " + fromTime;
                    }
                }
            }

            String toDateTime = null;
            if (!StringUtility.isNullOrEmpty(toDate)) {
                if (!DateFormatUtility.isLegalFormat(toDate, "yyyy-MM-dd")) {
                    tp.println("Date should have yyyy-MM-dd format.");
                    throw new ParseException("Invalid date format.", 0);
                } else {
                    toDateTime = toDate;
                }
            }

            if (!StringUtility.isNullOrEmpty(toTime)) {
                if (!DateFormatUtility.isLegalFormat(toTime, "HH:mm:ss")) {
                    tp.println("Time should have HH:mm:ss format.");
                    throw new ParseException("Invalid time format.", 0);
                } else {
                    if (toDateTime != null) {
                        toDateTime += " " + toTime;
                    } else {
                        // use current date
                        toDateTime = currentDate + " " + toTime;
                    }
                }
            } else {
                if (toDateTime != null) {
                    toDateTime += " " + "23:59:59"; // to include transactions
                                                    // created during this date
                }
            }

            // Prepare the SqlStatement for Transaction Search
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("select-transactions"));

            select.setInt(SQLStatement.PARAM1, limitIntValue);
            select.setString(SQLStatement.PARAM2, storeId);
            select.setString(SQLStatement.PARAM3, deviceId);
            select.setString(SQLStatement.PARAM4, itemName);
            select.setString(SQLStatement.PARAM5, itemId);
            select.setString(SQLStatement.PARAM6, tempSequenceNumberFrom);
            select.setString(SQLStatement.PARAM7, tempSequenceNumberTo);
            select.setString(SQLStatement.PARAM8, businessDate);
            select.setString(SQLStatement.PARAM9, fromDateTime);
            select.setString(SQLStatement.PARAM10, toDateTime);
            select.setString(SQLStatement.PARAM11, type);
            select.setString(SQLStatement.PARAM12, subCode4);
            select.setInt(SQLStatement.PARAM13, limitFrom);
            if (StringUtility.isNullOrEmpty(line)) {
                select.setNull(SQLStatement.PARAM14, Types.BIGINT);
            } else {
                select.setLong(SQLStatement.PARAM14, Long.parseLong(line));
            }
            select.setString(SQLStatement.PARAM15, companyId);
            select.setInt(SQLStatement.PARAM16, trainingMode);

            result = select.executeQuery();
            XmlSerializer<PosLog> serializer = new XmlSerializer<PosLog>();

            while (result.next()) {
                String company = result.getString("CompanyId");
                String posLogXml = result.getString("Tx");
                String transactionType = result.getString("TxType");

                PosLog posLog = serializer.unMarshallXml(posLogXml, PosLog.class);
                Transaction transaction = posLog.getTransaction();

                AdditionalInformation info = new AdditionalInformation();
                info.setJournalLine(result.getLong("JournalLine"));

                // check businessdate
                boolean voidable = isVoidable(dateSetting, transaction);
                info.setVoidable(ResultBase.toString(voidable));

                // check if already voided
                TransactionLink link = new TransactionLink();
                link.setRetailStoreID(transaction.getRetailStoreID());
                link.setWorkStationID(transaction.getWorkStationID());
                link.setSequenceNo(transaction.getSequenceNo());
                link.setBusinessDayDate(transaction.getBusinessDayDate());
                String voided = ResultBase.toString(isTransactionVoided(link, connection, companyId, trainingMode));
                info.setVoided(voided);

                String returned = ResultBase.toString(isTransactionReturned(link, connection, companyId, trainingMode));
                info.setReturned(returned);

				int summaryReceiptCount = getSummaryReceiptCount(company, transaction.getRetailStoreID(),
						transaction.getWorkStationID().getValue(), transaction.getSequenceNo(),
						transaction.getBusinessDayDate());
                info.setSummaryReceipt(String.valueOf(summaryReceiptCount));

                // TODO: get number of times a receipt was printed

                info.setTransactionType(transactionType);

                TransactionSearch transactionSearch = new TransactionSearch();
                transactionSearch.setPosLog(posLog);
                transactionSearch.setAdditionalInformation(info);

                txPosLogs.add(transactionSearch);
            }
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT, functionName + ": Failed to search transactions.",
                    ex);
            throw new DaoException("SQLStatementException: @searchTransactions", ex);
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to search transactions.", ex);
            throw new DaoException("SQLException: @searchTransactions", ex);
        } catch (ParseException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_PARSE, functionName + ": Failed to search transactions.", ex);
            throw new ParseException("ParseException: @searchTransactions:" + ex, 0);
        } catch (NumberFormatException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_PARSE, functionName + ": Failed to search transactions.", ex);
            throw new DaoException("NumberFormatException: @searchTransactions:", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to search transactions.", ex);
            throw new DaoException("Exception: @searchTransactions", ex);
        } finally {
            closeConnectionObjects(connection, select, result);
        }
        return cast(tp.methodExit(txPosLogs));
    }

    @SuppressWarnings("unchecked")
    static <T> List<T> cast(Object o) {
        return (List<T>) o;
    }

    boolean isVoidable(DateSetting dateSetting, Transaction tx) {
        RetailTransaction rtx = tx.getRetailTransaction();
        if (rtx == null)
            return false;
        boolean voidable = dateSetting.getToday().equals(tx.getBusinessDayDate());
        if (voidable) {
            checkTenderType: for (LineItem line : rtx.getLineItems()) {
                Tender tender = line.getTender();
                if (tender != null && !tender.getAmount().equals("0")) {
                    if (tender.getTenderType().equals(TransactionVariable.CREDITDEBIT)) {
                        voidable = false;
                        break checkTenderType;
                    } else if (tender.getTenderType().equals(TransactionVariable.VOUCHER) && tender.getVoucher().get(0)
                            .getTypeCode().equals(TransactionVariable.VOUCHER_TYPE_GIFT_CARD)) {
                        voidable = false;
                        break checkTenderType;
                    }
                }
            }
        }
        return voidable;
    }

    public int getSummaryReceiptCount(String companyid, String retailStoreID,String workStationID,String sequenceNo,String businessDayDate)
    throws SQLException, SQLStatementException, DaoException {
        int result = 0;
        PreparedStatement statement = null;
        ResultSet resultSet = null;
        Connection connection = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(sqlStatement.getProperty("get-summary-receipt-count"),
                ResultSet.TYPE_SCROLL_INSENSITIVE, ResultSet.CONCUR_READ_ONLY);
            statement.setString(SQLStatement.PARAM1, companyid);
            statement.setString(SQLStatement.PARAM2, retailStoreID);
            statement.setString(SQLStatement.PARAM3, workStationID);
            statement.setString(SQLStatement.PARAM4, sequenceNo);
            statement.setString(SQLStatement.PARAM5, businessDayDate);

            resultSet = statement.executeQuery();
            if (resultSet.last()) {
                result = resultSet.getInt("count");
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL,
                "getSummaryReceiptCount: Error in getting summary receipt counts.", e);
            throw e;
        } finally {
            closeConnectionObjects(connection, statement, resultSet);
        }

        return result;
    }


    private void saveVoidDetails(final PreparedStatement saveVoidDetailsStmt, TransactionLink transactionLink,
            String companyId, int trainingMode) throws SQLException {
        saveVoidReturnDetails(saveVoidDetailsStmt, transactionLink, TxTypes.VOID, companyId, trainingMode);
    }

    private void saveReturnDetails(final PreparedStatement saveVoidDetailsStmt, TransactionLink transactionLink,
            String companyId, int trainingMode) throws SQLException {
        try {
            saveVoidReturnDetails(saveVoidDetailsStmt, transactionLink, TxTypes.RETURN, companyId, trainingMode);
        } catch (SQLException e) {
            if (e.getErrorCode() == Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL,
                        "Duplicate Insert Error: Original Transaction " + "already returned.", e);
            } else {
                throw e;
            }
        }
    }

    private void saveVoidReturnDetails(final PreparedStatement saveVoidDetailsStmt, TransactionLink transactionLink,
            String type, String companyId, int trainingMode) throws SQLException {
        saveVoidDetailsStmt.setString(SQLStatement.PARAM1, transactionLink.getRetailStoreID());
        saveVoidDetailsStmt.setString(SQLStatement.PARAM2, transactionLink.getWorkStationID().getValue());
        saveVoidDetailsStmt.setString(SQLStatement.PARAM3, transactionLink.getSequenceNo());
        saveVoidDetailsStmt.setString(SQLStatement.PARAM4, transactionLink.getBusinessDayDate());
        saveVoidDetailsStmt.setString(SQLStatement.PARAM5, type);
        saveVoidDetailsStmt.setString(SQLStatement.PARAM6, companyId);
        saveVoidDetailsStmt.setInt(SQLStatement.PARAM7, trainingMode);

        saveVoidDetailsStmt.executeUpdate();
    }

    @Override
    public int saveForwardPosLog(PosLog posLog, String posLogXml, String queue, String total) throws DaoException {

        String functioName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functioName).println("queue", queue).println("total", total);

        int result = 0;
        Connection connection = null;
        PreparedStatement saveForwardPosLogPrepStmnt = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            Transaction transaction = posLog.getTransaction();
            Double amount = null;

            for(Total totalType : transaction.getRetailTransaction().getTotal()){
                if (("TransactionPurchaseQuantity").equals(totalType.getTotalType())) {
                    amount = new Double(totalType.getAmount());
                }
            }

            saveForwardPosLogPrepStmnt = connection.prepareStatement(sqlStatement.getProperty("save-forward-poslog"));
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM1, transaction.getOrganizationHierarchy().getId());
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM2, transaction.getRetailStoreID());
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM3, transaction.getWorkStationID().getValue());
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM4, transaction.getSequenceNo());
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM5, queue);
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM6, transaction.getBusinessDayDate());
            saveForwardPosLogPrepStmnt.setInt(SQLStatement.PARAM7,
                    ("false".equals(transaction.getTrainingModeFlag())) ? 0 : 1);
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM8, transaction.getBeginDateTime());
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM9, transaction.getOperatorID().getValue());
            saveForwardPosLogPrepStmnt.setInt(SQLStatement.PARAM10, Integer.parseInt(total));
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM11, transaction.getWorkStationID().getValue());
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM12, "0");
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM13, posLogXml);
            saveForwardPosLogPrepStmnt.setInt(SQLStatement.PARAM14,
                    "true".equals(transaction.getRetailTransaction().getLayawayFlag()) ? 1 : 0);
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM15, transaction.getTransactionType());
            saveForwardPosLogPrepStmnt.setInt(SQLStatement.PARAM16, amount.intValue());

            if (saveForwardPosLogPrepStmnt.executeUpdate() != 1) {
                result = ResultBase.RESSYS_ERROR_QB_QUEUEFULL;
            }
            connection.commit();
        } catch (SQLException e) {
            if (e.getErrorCode() != Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functioName + ": Failed to save forward poslog.", e);
                rollBack(connection, functioName, e);
                throw new DaoException("SQLException: @" + functioName, e);
            }
            Snap.SnapInfo duplicateSuspend = snap.write("Poslogxml to suspend", posLogXml);
            LOGGER.logSnap(PROG_NAME, functioName, "Duplicate suspend transaction to snap file", duplicateSuspend);
            result = SQLResultsConstants.ROW_DUPLICATE;
            tp.println("Duplicate Entry of Transaction.");
        } catch (Exception e) {
            rollBack(connection, functioName, e);
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functioName + ": Failed to save forward poslog.", e);
            throw new DaoException("Exception: @" + functioName, e);
        } finally {
            closeConnectionObjects(connection, saveForwardPosLogPrepStmnt);
            tp.methodExit(result);
        }

        return result;
    }

    @Override
    public SearchForwardPosLog getForwardItemsPosLog(String CompanyId, String RetailStoreId, String WorkstationId,
            String SequenceNumber, String Queue, String BusinessDayDate, String TrainingFlag) throws DaoException {

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", CompanyId).println("RetailStoreId", RetailStoreId)
                .println("WorkstationId", WorkstationId).println("SequenceNumber", SequenceNumber)
                .println("Queue", Queue).println("BusinessDayDate", BusinessDayDate)
                .println("TrainingFlag", TrainingFlag);

        SearchForwardPosLog searchForwardPosLog = new SearchForwardPosLog();
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-forward-items-posLog"));

            select.setString(SQLStatement.PARAM1, CompanyId);
            select.setString(SQLStatement.PARAM2, RetailStoreId);
            select.setString(SQLStatement.PARAM3, SequenceNumber.replaceFirst("^0*", ""));
            select.setString(SQLStatement.PARAM4, Queue);
            select.setString(SQLStatement.PARAM5, BusinessDayDate);
            select.setString(SQLStatement.PARAM6, TrainingFlag);
            select.setString(SQLStatement.PARAM7, WorkstationId);
            result = select.executeQuery();
            if (result.next()) {
                searchForwardPosLog.setPosLogXml(result.getString(result.findColumn("tx")));
                searchForwardPosLog.setStatus(result.getInt(result.findColumn("Status")));
            } else {
                tp.println("No forward items poslog found.");
            }
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get forward items poslog.",
                    ex);
            throw new DaoException("Exception: @getForwardItemsPosLog - " + ex.getMessage(), ex);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(searchForwardPosLog.getPosLogXml());
        }
        return searchForwardPosLog;
    }

    private String getGuid() throws NamingException {
        Context env = (Context) new InitialContext().lookup("java:comp/env");
        serverID = (String) env.lookup("serverID");
        String guid = Guid.toString(Guid.create(serverID));

        return guid.replaceAll("\\{", "").replaceAll("}", "");
    }

    public String getLastPayTxPoslog(String companyId, String storeId, String terminalId, String businessDate,
            int trainingFlag) throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyid", companyId).println("storeId", storeId)
                .println("terminalId", terminalId).println("businessDate", businessDate)
                .println("trainingFlag", trainingFlag);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        String poslog = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(sqlStatement.getProperty("get-last-pay-tx-poslog"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, terminalId);
            statement.setString(SQLStatement.PARAM4, businessDate);
            statement.setInt(SQLStatement.PARAM5, trainingFlag);
            result = statement.executeQuery();

            if (result.next()) {
                poslog = result.getString("Tx");
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get last pay tx poslog.", e);
            throw new Exception("SQLException: @SQLServerPosLogDAO." + functionName, e);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(poslog);
        }
        return poslog;
    }

    public String getLastBalancingTxPoslog(String companyId, String storeId, String terminalId, String businessDate,
            int trainingFlag) throws Exception {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyid", companyId).println("storeId", storeId)
                .println("terminalId", terminalId).println("businessDate", businessDate)
                .println("trainingFlag", trainingFlag);

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        String poslog = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(sqlStatement.getProperty("get-last-balancing-tx-poslog"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, terminalId);
            statement.setString(SQLStatement.PARAM4, businessDate);
            statement.setInt(SQLStatement.PARAM5, trainingFlag);
            result = statement.executeQuery();

            if (result.next()) {
                poslog = result.getString("Tx");
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get last balancing tx poslog.", e);
            throw new Exception("SQLException: @SQLServerPosLogDAO." + functionName, e);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(poslog);
        }
        return poslog;
    }

    @Override
    public AdditionalInformation getVoidedAndReturned(String companyid, String storeid, String workstationid,
            String businessdate, String txid, int trainingflag, String txtype) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", companyid).println("StoreID", storeid)
                .println("WorkstationId", workstationid).println("BusinessDate", businessdate)
                .println("Transaction Number", txid).println("TrainingMode", trainingflag).println("TxType", txtype);

        Connection connection = null;
        AdditionalInformation info = new AdditionalInformation();

        try {
            connection = dbManager.getConnection();

            TransactionLink link = new TransactionLink();
            link.setRetailStoreID(storeid);
            WorkstationID workstation = new WorkstationID();
            workstation.setValue(workstationid);
            link.setWorkStationID(workstation);
            link.setSequenceNo(txid);
            link.setBusinessDayDate(businessdate);

            String voided = ResultBase.toString(isTransactionVoided(link, connection, companyid, trainingflag));
            info.setVoided(voided);

            String returned = ResultBase.toString(isTransactionReturned(link, connection, companyid, trainingflag));
            info.setReturned(returned);

        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, "isTransactionVoidedOrReturned: Error in checking if "
                    + "transaction was already voided or returned.", ex);
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    "isTransactionVoidedOrReturned: Error in checking if "
                            + "transaction was already voided or returned.",
                    ex);
        } finally {
            closeConnectionObjects(connection, null, null);
        }
        return info;
    }

    /**
     * Public method that is used to validate or update lock status
     * @param companyid
     * @param storeid
     * @param workstationid
     * @param businessdate
     * @param sequencenumber
     * @param trainingflag
     * @param callType
     * @param appId
     * @param opeCode
     * @param type
     * @return Returns the status of the lock
     * @throws DaoException
     *             Exception thrown when the method failed.
     */
    public int getOrUpdLockStatus(String companyid, String storeid, String workstationid, String businessdate,
            int sequencenumber, int trainingflag, String callType, String appId, String opeCode, String type)
                    throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", companyid).println("StoreID", storeid)
                .println("WorkstationId", workstationid).println("BusinessDate", businessdate)
                .println("SequenceNumber", sequencenumber).println("TrainingFlag", trainingflag)
                .println("CallType", callType).println("AppId", appId).println("OpeCode", opeCode)
                .println("Type", type);

        PreparedStatement getLockStatus = null;
        PreparedStatement insertLock = null;
        PreparedStatement deleteLock = null;
        Connection connection = null;
        ResultSet resultSet = null;
        int result = -1;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            switch (type) {
            case "getLockStatus":
                getLockStatus = connection.prepareStatement(sqlStatement.getProperty("get-lock-status"));
                getLockStatus.setString(SQLStatement.PARAM1, companyid);
                getLockStatus.setString(SQLStatement.PARAM2, storeid);
                getLockStatus.setString(SQLStatement.PARAM3, workstationid);
                getLockStatus.setInt(SQLStatement.PARAM4, sequencenumber);
                getLockStatus.setString(SQLStatement.PARAM5, businessdate);
                getLockStatus.setInt(SQLStatement.PARAM6, trainingflag);
                resultSet = getLockStatus.executeQuery();
                if (resultSet.next()) {
                    result = resultSet.getInt("Status");
                }
                break;
            case "doLock":
                insertLock = connection.prepareStatement(sqlStatement.getProperty("insert-lock"));
                insertLock.setString(SQLStatement.PARAM1, companyid);
                insertLock.setString(SQLStatement.PARAM2, storeid);
                insertLock.setString(SQLStatement.PARAM3, workstationid);
                insertLock.setInt(SQLStatement.PARAM4, sequencenumber);
                insertLock.setString(SQLStatement.PARAM5, businessdate);
                insertLock.setInt(SQLStatement.PARAM6, trainingflag);
                insertLock.setString(SQLStatement.PARAM7, callType);
                insertLock.setInt(SQLStatement.PARAM8, 1);
                insertLock.setString(SQLStatement.PARAM9, companyid);
                insertLock.setString(SQLStatement.PARAM10, storeid);
                insertLock.setString(SQLStatement.PARAM11, workstationid);
                insertLock.setString(SQLStatement.PARAM12, appId);
                insertLock.setString(SQLStatement.PARAM13, opeCode);
                insertLock.setString(SQLStatement.PARAM14, appId);
                insertLock.setString(SQLStatement.PARAM15, opeCode);
                if (insertLock.executeUpdate() != 0) {
                    result = 1;
                }
                connection.commit();
                break;
            case "unLock":
                deleteLock = connection.prepareStatement(sqlStatement.getProperty("delete-lock"));
                deleteLock.setString(SQLStatement.PARAM1, companyid);
                deleteLock.setString(SQLStatement.PARAM2, storeid);
                deleteLock.setString(SQLStatement.PARAM3, workstationid);
                deleteLock.setInt(SQLStatement.PARAM4, sequencenumber);
                deleteLock.setString(SQLStatement.PARAM5, businessdate);
                deleteLock.setInt(SQLStatement.PARAM6, trainingflag);
                if (deleteLock.executeUpdate() != 0) {
                    result = 1;
                }
                connection.commit();
                break;
            }

        } catch (SQLException sqlE) {
            rollBack(connection, "SQLServerPosLogDAO: @doPOSLogJournalization()", sqlE);
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get or update lock status.",
                    sqlE);
            throw new DaoException("SQLException: @doPOSLogJournalization - " + sqlE.getMessage(), sqlE);
        } catch (Exception e) {
            rollBack(connection, "SQLServerPosLogDAO: @doPOSLogJournalization()", e);
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get or update lock status.", e);
            throw new DaoException("Exception: @doPOSLogJournalization - " + e.getMessage(), e);
        } finally {
            closeConnectionObjects(connection, null, null);
        }
        return tp.methodExit(result);
    }

    /**
     * Public method that is used to validate a sale transaction that is
     * already point granted
     * @param companyid
     * @param storeid
     * @param workstationid
     * @param businessdate
     * @param txid
     * @param trainingflag
     * @return Returns the status of the sale that is point granted
     * @throws DaoException Exception thrown when the method failed.
     */
	public PointPosted isPointPosted(String companyid, String storeid, String workstationid, String businessdate,
			String txid, int trainingflag) throws DaoException {
	  String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("CompanyId", companyid).println("StoreID", storeid)
                .println("WorkstationId", workstationid).println("BusinessDate", businessdate)
                .println("Transaction Number", txid).println("TrainingMode", trainingflag);

        Connection connection = null;
        ResultSet resultSet = null;
        PointPosted pointPosted = new PointPosted();
        pointPosted.setPostPointed(false);
        pointPosted.setMemberId(null);
        ResultSet result = null;
        PreparedStatement statement = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(sqlStatement.getProperty("get-postpoint-status"));
            statement.setString(SQLStatement.PARAM1, companyid);
            statement.setString(SQLStatement.PARAM2, storeid);
            statement.setString(SQLStatement.PARAM3, workstationid);
            statement.setString(SQLStatement.PARAM4, businessdate);
            statement.setString(SQLStatement.PARAM5, txid);
            statement.setInt(SQLStatement.PARAM6, trainingflag);
            resultSet = statement.executeQuery();
            if (resultSet.next()) {
            	String originalSequence = resultSet.getString("OriginalSequenceNumber");
            	if(!StringUtility.isNullOrEmpty(originalSequence)) {
            		pointPosted.setPostPointed(true);
            		pointPosted.setMemberId(resultSet.getString("MemberId"));
                    pointPosted.setCompanyId(resultSet.getString("CompanyId"));
                    pointPosted.setRetailStoreId(resultSet.getString("RetailStoreId"));
                    pointPosted.setWorkstationId(resultSet.getString("WorkstationId"));
                    pointPosted.setBusinessDayDate(resultSet.getString("BusinessDayDate"));
                    pointPosted.setSequenceNumber(resultSet.getString("SequenceNumber"));
                    pointPosted.setTrainingFlag(resultSet.getInt("TrainingFlag"));
            	}
            }
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, "isPostPointed: Error in checking if "
                    + "transaction was already post pointed", ex);
            pointPosted.setPostPointed(false);
        } finally {
            closeConnectionObjects(connection, null, null);
        }
		return pointPosted;
	}

	@Override
	public int saveForwardPosLogIncludeTag(PosLog posLog, String posLogXml, String queue, String tag, String total)
			throws DaoException {
		String functioName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functioName).println("queue", queue).println("tag",tag).println("total", total);

        int result = 0;
        Connection connection = null;
        PreparedStatement saveForwardPosLogPrepStmnt = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            Transaction transaction = posLog.getTransaction();
            Double amount = null;

            for(Total totalType : transaction.getRetailTransaction().getTotal()){
                if (("TransactionPurchaseQuantity").equals(totalType.getTotalType())) {
                    amount = new Double(totalType.getAmount());
                }
            }

            saveForwardPosLogPrepStmnt = connection.prepareStatement(sqlStatement.getProperty("save-forward-poslog-include-tag"));
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM1, transaction.getOrganizationHierarchy().getId());
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM2, transaction.getRetailStoreID());
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM3, transaction.getWorkStationID().getValue());
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM4, transaction.getSequenceNo());
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM5, queue);
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM6, transaction.getBusinessDayDate());
            saveForwardPosLogPrepStmnt.setInt(SQLStatement.PARAM7,
                    ("false".equals(transaction.getTrainingModeFlag())) ? 0 : 1);
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM8, transaction.getBeginDateTime());
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM9, transaction.getOperatorID().getValue());
            saveForwardPosLogPrepStmnt.setInt(SQLStatement.PARAM10, Integer.parseInt(total));
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM11, transaction.getWorkStationID().getValue());
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM12, "0");
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM13, posLogXml);
            saveForwardPosLogPrepStmnt.setInt(SQLStatement.PARAM14,
                    "true".equals(transaction.getRetailTransaction().getLayawayFlag()) ? 1 : 0);
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM15, transaction.getTransactionType());
            saveForwardPosLogPrepStmnt.setInt(SQLStatement.PARAM16, amount.intValue());
            saveForwardPosLogPrepStmnt.setString(SQLStatement.PARAM17, tag);
            if (saveForwardPosLogPrepStmnt.executeUpdate() != 1) {
                result = ResultBase.RESSYS_ERROR_QB_TAG_INUSE;
            }
            connection.commit();
        } catch (SQLException e) {
            if (e.getErrorCode() != Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functioName + ": Failed to save forward poslog.", e);
                rollBack(connection, functioName, e);
                throw new DaoException("SQLException: @" + functioName, e);
            }
            Snap.SnapInfo duplicateSuspend = snap.write("Poslogxml to suspend", posLogXml);
            LOGGER.logSnap(PROG_NAME, functioName, "Duplicate suspend transaction to snap file", duplicateSuspend);
            result = SQLResultsConstants.ROW_DUPLICATE;
            tp.println("Duplicate Entry of Transaction.");
        } catch (Exception e) {
            rollBack(connection, functioName, e);
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functioName + ": Failed to save forward poslog.", e);
            throw new DaoException("Exception: @" + functioName, e);
        } finally {
            closeConnectionObjects(connection, saveForwardPosLogPrepStmnt);
            tp.methodExit(result);
        }

        return result;
	}
	
    /**
     * É^ÉOî‘çÜÇ≈ëOéJè§ïiñæç◊ PosLog åüçı
     * @param companyId
     * @param retailStoreId
     * @param queue
     * @param businessDayDate
     * @param tag
     * @return ëOéJìoò^ PosLog
     * @throws DaoException
     */
    @Override
    public SearchForwardPosLog getForwardItemsPosLogWithTag(String companyId, String retailStoreId,
    		String queue, String businessDayDate, String tag) throws DaoException{

        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName)
        		.println("CompanyId", companyId)
        		.println("RetailStoreId", retailStoreId)
                .println("Queue", queue)
                .println("BusinessDayDate", businessDayDate)
                .println("tag", tag);

        SearchForwardPosLog searchForwardPosLog = new SearchForwardPosLog();
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-forward-items-posLog-with-tag"));

            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, retailStoreId);
            select.setString(SQLStatement.PARAM3, queue);
            select.setString(SQLStatement.PARAM4, businessDayDate);
            select.setString(SQLStatement.PARAM5, tag);

            result = select.executeQuery();
            if (result.next()) {
                searchForwardPosLog.setPosLogXml(result.getString(result.findColumn("tx")));
                searchForwardPosLog.setStatus(result.getInt(result.findColumn("Status")));
            } else {
                tp.println("No forward items poslog with tag found.");
            }
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get forward items poslog with tag.",
                    ex);
            throw new DaoException("Exception: @getForwardItemsPosLogWithTag - " + ex.getMessage(), ex);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(searchForwardPosLog.getPosLogXml());
        }
        return searchForwardPosLog;
    }
}
