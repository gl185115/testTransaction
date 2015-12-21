/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerConsolidation
 *
 * A Data Access Object implementation for Consolidation.
 *
 * Campos, Carlos  (cc185102)
 */
package ncr.res.mobilepos.consolidation.dao;

import ncr.res.mobilepos.consolidation.constant.TransactionVariable
        .TransactionCode;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Snap;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.consolidation.constant.InsrtTotalAcntDayConstants;
import ncr.res.mobilepos.consolidation.constant.InsrtTotalItemDayConstants;
import ncr.res.mobilepos.consolidation.constant.TransactionVariable;
import ncr.res.mobilepos.consolidation.constant.TxlJournalConstants;
import ncr.res.mobilepos.consolidation.constant.UpdTotDrwrFinConstants;
import ncr.res.mobilepos.consolidation.constant.UpdTotalAcntDayConstants;
import ncr.res.mobilepos.consolidation.constant.UpdTotalFinancialConstants;
import ncr.res.mobilepos.consolidation.constant.UpdatePOSLogStatusConstants;
import ncr.res.mobilepos.consolidation.constant.UpdtTotalItemDayConstants;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.constant.TxTypes;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SnapLogger;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.model.poslog.OperatorID;
import ncr.res.mobilepos.journalization.model.poslog.RetailTransaction;
import ncr.res.mobilepos.journalization.model.poslog.Discount;
import ncr.res.mobilepos.journalization.model.poslog.LineItem;
import ncr.res.mobilepos.journalization.model.poslog.TransactionLink;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.Sale;
import ncr.res.mobilepos.journalization.model.poslog.Tender;
import ncr.res.mobilepos.journalization.model.poslog.TenderChange;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * A Data Access Object implementation for Consolidation.
 *
 * @see IConsolidate
 */
public class SQLServerConsolidationDAO extends AbstractDao implements
        IConsolidate {
    /**
     * Database manager.
     */
    private DBManager dbManager;
    /**
     * Logs information.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Trace logger.
     */
    private Trace.Printer tp;
    /**
     * Snap Logger.
     */
    private SnapLogger snap;
    /**
     * Program name use for logging.
     */
    private String progName = "Consldtn";
    /**
     * Class name use for logging.
     */
    private String className = "SQLServerConsolidationDAO.";
    /**
     * Status for normal consolidation process.
     */
    private static final String POSLOG_STATUS_NORMAL = "1";
    /**
     * Status for abnormal consolidation process.
     */
    private static final String POSLOG_STATUS_ABNORMAL = "2";
    /**
     * Maximum list.
     */
    private static final int MAX_LIST_CNT = 100;
    /**
     * Normal exception.
     */
    private static final int NORMAL_EXCEPTION = -1;
    /**
     * SQL Error code id for duplicate.
     */
    private static final int ERRORCODE_DUPLICATE = 2627;
    /**
     * SQL error code id for deadlock.
     */
    private static final int ERRORCODE_DEADLOCK = 1205;

    /**
     * The Constructor of the Class.
     *
     * @throws DaoException
     *             Thrown when exception occurs.
     */
    public SQLServerConsolidationDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        snap = (SnapLogger) SnapLogger.getInstance();
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
     * Method called that selects at most 100 POSLog XML in the database occur.
     *
     * @param seqnum
     *            The function will return transactions with sequence number
     *            greater than this sequence number.
     * @return List of {@link TransactionInfo}
     * @throws DaoException
     *             The exception thrown when no method fails.
     */
    public final List<TransactionInfo> selectPOSLogXML(final String seqnum)
            throws DaoException {
        String functionName = className + "selectPOSLogXML";

        tp.methodEnter("selectPOSLogXML");
        tp.println("seqnum", seqnum);

        List<TransactionInfo> posLogxmlList = new ArrayList<TransactionInfo>();

        Connection connection = null;
        PreparedStatement selectPOSLOgXMLStmnt = null;
        ResultSet resultSet = null;

        try {

            if (dbManager == null) {
                dbManager = JndiDBManagerMSSqlServer.getInstance();
            }

            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            /* Get the Select PoslOG SQL statement */
            selectPOSLOgXMLStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-poslog-not-consolidated"));
            selectPOSLOgXMLStmnt.setString(1, seqnum);
            resultSet = selectPOSLOgXMLStmnt.executeQuery();

            int cnt = 0;

            TransactionInfo trans = null;

            // Retrieve the POSLog xml
            while (resultSet.next()) {
                if (cnt >= MAX_LIST_CNT) {
                    // The number of the maximum lists was reached.
                    break;
                }
                trans = new TransactionInfo();
                trans.setTx((String) resultSet.getString("tx"));
                trans.setSeqNum((String) resultSet.getString("seqnum"));
                trans.setTxCorpId((String) resultSet.getString("corpid"));
                posLogxmlList.add(trans);
                cnt++;
            }
            tp.println("count", cnt);
        } catch (SQLStatementException sqlStmntEx) {
            LOGGER.logAlert(progName, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "SQL Statement Error occured.\n" + sqlStmntEx.getMessage());
            throw new DaoException("SQLStatementException:@"
                    + "SQLServerConsolidationDAO.selectPOSLogXML ", sqlStmntEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "SQL Error occured. \n" + sqlEx.getMessage());
            throw new DaoException("SQLException:@"
                    + "SQLServerConsolidationDAO.selectPOSLogXML", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to update Total Account Day.\n" + ex.getMessage());
            throw new DaoException("ParseException:@"
                    + "SQlServerConsolidationDAO.selectPOSLogXML", ex);
        } finally {
            closeConnectionObjects(connection, selectPOSLOgXMLStmnt, resultSet);

            tp.methodExit(posLogxmlList.size());
        }
        return posLogxmlList;
    }

    /**
     * Private method that update the TXU_TOTAL_ACNT_DAY summary table.
     *
     * @param txCorpId
     *            The corporate identifier string.
     * @param transaction
     *            The current transaction
     * @param line
     *            The sale's line
     * @param connection
     *            The Connection to the database
     * @param txtype
     *            The transaction type
     * @return The number of rows affected
     * @throws DaoException Thrown when error occurs.
     */
    private int doTotalAccountDayUpdate(final String txCorpId,
            final Transaction transaction, final LineItem line,
            final Connection connection, final String txtype)
            throws DaoException {
        // Start log.
        String functionName = className + "doTotalAccountDayUpdate";

        tp.methodEnter("doTotalAccountDayUpdate");

        int result = 0;
        boolean isDup = false;
        PreparedStatement insertTotalAcntDayStmnt = null;
        PreparedStatement updateTotalAcntDayStmnt = null;
        try {
            /* End of calculation */
            SQLStatement sqlStatement = SQLStatement.getInstance();
            insertTotalAcntDayStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("save-total-account-day"));
            updateTotalAcntDayStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("update-total-account-day"));
            // The Account Code
            String accountCode = line.getAccountCode();
            // Is the accountCode unknown?
            // If yes there is no need to process here
            if (null == accountCode) {
                tp.println("AccountCode is unknown.");
                return 0;
            }
            // Set the local variable flag to tell the Account code
            boolean isDiscountLine = accountCode
                    .equals(TransactionVariable.DISCOUNT_CODE);

            if (isDiscountLine
                    || !accountCode.equals(TransactionVariable.ITEM_CODE)) {
                tp.println("Discount line has invalid AccountCode",
                        accountCode);
                return 0;
            }
            // Get the TimeZone Code from beginDateTime. The TimeZone Code
            // is treated as PK in TXU_TOTAL_ACNT_DAY
            SimpleDateFormat dateFormat = new SimpleDateFormat(
                    "yyyy-MM-dd HH:MM:SS");
            java.util.Date date = dateFormat.parse(transaction
                    .getBeginDateTime().replace('T', ' '));
            @SuppressWarnings("deprecation")
            String timeZone = String.valueOf(date.getHours());
            /* Prepare values necessary for calculation TXU_TOTAL_ACNT_DAY */
            long lSalesItemCount = 0;
            long lReturnItemCount = 0;
            long lVoidItemCount = 0;
            long ldcItemCount = 0;
            double dSalesSalesAmount = 0;
            double dReturnSalesAmount = 0;
            double dVoidSalesAmount = 0;
            double dDcSalesAmount = 0;
            // Get the Sale
            Sale sale = line.getSale();
            String strLine = sale.getLine();
            if (strLine == null || strLine.isEmpty()) {
                strLine = "0";
            }
            String strClass = sale.getClas();
            if (strClass == null || strClass.isEmpty()) {
                strClass = "0";
            }
            String strDepartment = sale.getDepartment();
            /* The calculation will start here */
            if (txtype.equals(TxTypes.NORMAL)) {
                dSalesSalesAmount += sale.getExtendedAmt();
                lSalesItemCount += sale.getQuantity();
                if (0 != sale.getDiscountAmount()) {
                    dSalesSalesAmount -= Double.valueOf(sale
                            .getDiscountAmount());
                    dDcSalesAmount += Double.valueOf(sale.getDiscountAmount());
                    ldcItemCount += sale.getQuantity();
                }
            } else if (txtype.equals(TxTypes.RETURNER)) {
                // also modify the sales data
                // if in case a new row is needed to be inserted
                dSalesSalesAmount -= sale.getExtendedAmt();
                lSalesItemCount -= sale.getQuantity();
                lReturnItemCount += sale.getQuantity();
                dReturnSalesAmount += sale.getExtendedAmt();
                if (0 != sale.getDiscountAmount()) {
                    Double lcreditor = Double.valueOf(sale.getDiscountAmount());
                    dSalesSalesAmount += lcreditor;
                    dDcSalesAmount -= lcreditor;
                    dReturnSalesAmount -= lcreditor;
                    ldcItemCount -= sale.getQuantity();
                }
            } else if (txtype.equals(TxTypes.VOIDER)) {
                dSalesSalesAmount -= sale.getExtendedAmt();
                lSalesItemCount -= sale.getQuantity();
                lVoidItemCount += sale.getQuantity();
                dVoidSalesAmount += sale.getExtendedAmt();
                if (0 != sale.getDiscountAmount()) {
                    Double lcreditor = Double.valueOf(sale.getDiscountAmount());
                    dSalesSalesAmount += lcreditor;
                    dDcSalesAmount -= lcreditor;
                    dVoidSalesAmount -= lcreditor;
                    ldcItemCount -= sale.getQuantity();
                }
            }
            /* Set the Sales amount of each transaction of type */
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.DCSALESAMNT,
                    String.valueOf(dDcSalesAmount));
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.RETURNSALESAMNT,
                    String.valueOf(dReturnSalesAmount));
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.VOIDSALESAMNT,
                    String.valueOf(dVoidSalesAmount));
            /* Set the item count of each transaction of type */
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.DCITEMCNT,
                    String.valueOf(ldcItemCount));
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.RETURNITEMCNT,
                    String.valueOf(lReturnItemCount));
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.VOIDITEMCNT,
                    String.valueOf(lVoidItemCount));

            /* Set the SalesAmount of each transaction of type */
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.SALESITEMCNT,
                    String.valueOf(lSalesItemCount));
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.SALESSALESAMNT,
                    String.valueOf(dSalesSalesAmount));
            /* Set the Cost Amount of each transaction of type */
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.SALESCOSTAMNT, "0");
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.DCCOSTAMNT, "0");
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.RETURNCOSTAMNT, "0");
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.VOIDCOSTAMNT, "0");
            /* Set the primary code */
            updateTotalAcntDayStmnt.setString(UpdTotalAcntDayConstants.CLASS,
                    strClass);
            updateTotalAcntDayStmnt.setString(UpdTotalAcntDayConstants.CORPID,
                    txCorpId);
            updateTotalAcntDayStmnt.setString(UpdTotalAcntDayConstants.DPT,
                    strDepartment);
            updateTotalAcntDayStmnt.setString(UpdTotalAcntDayConstants.LINE,
                    strLine);
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.OPERATORCODE,
                    transaction.getOperatorID().getValue());
            updateTotalAcntDayStmnt.setString(UpdTotalAcntDayConstants.STOREID,
                    transaction.getRetailStoreID());
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.SUMMARYDATEID,
                    transaction.getBusinessDayDate());
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.TERMINALID,
                    transaction.getWorkStationID().getValue());
            updateTotalAcntDayStmnt.setString(
                    UpdTotalAcntDayConstants.TIMEZONECODE, timeZone);
            result += updateTotalAcntDayStmnt.executeUpdate();
            // Are there no row updated?
            if (result >= SQLResultsConstants.ONE_ROW_AFFECTED) {
                tp.println("TotalAcntDay data updated.");
                return result;
            }
            /* !IMPORTANT! */
            /*
             * If the code pass here it means that the row needs to be
             * inserted(Not Updated) in the database.
             */
            insertTotalAcntDayStmnt.setString(InsrtTotalAcntDayConstants.CLASS,
                    strClass);
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.CORPID, txCorpId);
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.DCCOSTAMNT, "0");
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.DCITEMCNT,
                    String.valueOf(ldcItemCount));
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.DCSALESAMNT,
                    String.valueOf(dDcSalesAmount));
            insertTotalAcntDayStmnt.setString(InsrtTotalAcntDayConstants.DPT,
                    strDepartment);
            insertTotalAcntDayStmnt.setString(InsrtTotalAcntDayConstants.LINE,
                    strLine);
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.OPERATORCODE,
                    transaction.getOperatorID().getValue());
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.RETURNCOSTAMNT, "0");
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.RETURNITEMCNT,
                    String.valueOf(lReturnItemCount));
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.RETURNSALESAMNT,
                    String.valueOf(dReturnSalesAmount));
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.SALESCOSTAMNT, "0");
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.SALESITEMCNT,
                    String.valueOf(lSalesItemCount));
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.SALESSALESAMNT,
                    String.valueOf(dSalesSalesAmount));
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.STOREID,
                    transaction.getRetailStoreID());
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.SUMMARYDATEID,
                    transaction.getBusinessDayDate());
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.TERMINALID,
                    transaction.getWorkStationID().getValue());
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.TIMEZONECODE, timeZone);
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.VOIDCOSTAMNT, "0");
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.VOIDITEMCNT,
                    String.valueOf(lVoidItemCount));
            insertTotalAcntDayStmnt.setString(
                    InsrtTotalAcntDayConstants.VOIDSALESAMNT,
                    String.valueOf(dVoidSalesAmount));
            result += insertTotalAcntDayStmnt.executeUpdate();
        } catch (SQLException e) {
            // Duplication of insertion
            if (e.getErrorCode() != ERRORCODE_DUPLICATE) {
                LOGGER.logAlert(functionName, functionName,
                        Logger.RES_EXCEP_SQL,
                        "Failed to update Total Account Day. SQL ErrorCode = "
                                + e.getErrorCode() + " \n" + e.getMessage());
                throw new DaoException("SQLException:@"
                        + "SQlServerConsolidationDAO.doTotalAccountDayUpdate",
                        e);
            }
            isDup = true;
        } catch (ParseException parseEx) {
            LOGGER.logAlert(
                    functionName,
                    functionName,
                    Logger.RES_EXCEP_PARSE,
                    "Failed to update Total Account Day. \n"
                            + parseEx.getMessage());
            throw new DaoException("ParseException:@"
                    + "SQlServerConsolidationDAO.doTotalAccountDayUpdate",
                    parseEx);
        } catch (Exception ex) {
            LOGGER.logAlert(functionName, functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to update Total Account Day. \n" + ex.getMessage());
            throw new DaoException("ParseException:@"
                    + "SQlServerConsolidationDAO.doTotalAccountDayUpdate", ex);
        } finally {
            // When the result of having Insert(ed)
            // is duplication, update is performed again.
            if (isDup) {
                result += updateTotalAcntDay(updateTotalAcntDayStmnt);
            }
            closeObject(updateTotalAcntDayStmnt);
            closeObject(insertTotalAcntDayStmnt);

            tp.methodExit(result);
        }
        return result;
    }

    private int updateTotalAcntDay(PreparedStatement updateTotalAcntDayStmnt)
            throws DaoException {
        int result = 0;
        try {
            result = updateTotalAcntDayStmnt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.logAlert(progName,
                    "SQLServerConsolidationDAO.updateTotalAcntDay",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to update Total Account Day.\n"
                    + e.getMessage());
            tp.methodExit("Failed to update Total Account Day.");
            throw new DaoException(
                    "SQLException:@SQlServerConsolidationDAO"
                            + ".updateTotalAcntDay", e);
        } finally {
            closeObject(updateTotalAcntDayStmnt);
        }

        return result;
    }

    /**
     * Save the specific contents of the LineItems to the TXL_JOURNAL table.
     *
     * @param lineItem
     *            The Line Item that contains the information needed for
     *            TXL_JOURNAL
     * @param saveLineItemStmt
     *            The prepared Statement necessary for TXL_JOURNAL
     * @param swapFlag
     *            A flag that tells whether credit and Debtor will be swap
     * @param lineItemCounter
     *            Counter for LineItem.
     * @return The number of rows affected in the database.
     * @throws DaoException Thrown when Exception on DAO occurs.
     * @throws SQLException Thrown when Exception on SQL occurs.
     */
    private int saveLineItemToJournal(final LineItem lineItem,
            final PreparedStatement saveLineItemStmt,
            final LineItemCounter lineItemCounter, final boolean swapFlag)
            throws DaoException, SQLException {
        String functionName = className + "saveLineItemToJournal";

        tp.methodEnter("saveLineItemToJournal");
        tp.println("lineItemCounter", lineItemCounter.getIndex());
        tp.println("swapFlag", swapFlag);

        int result = 0;
        String strAccountCode = null;
        String strplu = null;
        String strdescription = null;
        String stritemcount = null;
        String stractsalesamount = null;
        String strcreditor = null;
        String strdebtor = null;
        boolean isItemDiscount = false;

        try {

            strAccountCode = lineItem.getAccountCode();

            if (null == strAccountCode) {
                tp.println("AccountCode is unknown.");
                return result;
            }

            lineItemCounter.increment();
            saveLineItemStmt.setString(TxlJournalConstants.ACCOUNT_CODE,
                    strAccountCode);

            // Is the LineItem holds the information for Sale?
            // Save the sale information
            if (strAccountCode.equals(TransactionVariable.ITEM_CODE)) {
                Sale sale = lineItem.getSale();

                strplu = sale.getItemID().getPluCode();
                strdescription = sale.getDescription();
                stritemcount = checkValue(0, sale.getQuantity());
                stractsalesamount = checkValue(0,
                        (int) sale.getActualsalesunitprice());
                strcreditor = checkValue(0, (int) sale.getExtendedAmt());

                if (0 != sale.getDiscountAmount()) {
                        isItemDiscount = true;
                }
            } else if (strAccountCode.equals(
                    TransactionVariable.DISCOUNT_CODE)) {
             // Is the LineItem holds the information for Discount?
             // Save the Discount information instead.
                Discount discount = lineItem.getDiscount();
                strdebtor = discount.getAmount();
            } else if (strAccountCode.equals(TransactionVariable.CASH_CODE)
                    || strAccountCode.equals(
                            TransactionVariable.CREDITDEBIT_CODE)) {
             // Is the lineitem holds the information for Tender?
             // Save the tender information instead
                Tender tender = lineItem.getTender();
                strdebtor = tender.getAmount();
            }

            // Is the Creditor and Debtor should swapped its values?
            // This case should only happen when VOIDING transaction
            // If yes, then swap the value of creditor and debtor
            if (swapFlag) {
                String strTemp = strcreditor;
                strcreditor = strdebtor;
                strdebtor = strTemp;
            }

            saveLineItemStmt.setString(TxlJournalConstants.PLU, strplu);
            saveLineItemStmt.setString(TxlJournalConstants.DESCRIPTION,
                    strdescription);
            saveLineItemStmt.setString(TxlJournalConstants.ITEMCOUNT,
                    stritemcount);
            saveLineItemStmt.setString(TxlJournalConstants.ACTSALESAMOUNT,
                    stractsalesamount);
            saveLineItemStmt.setString(TxlJournalConstants.CREDITOR,
                    strcreditor);
            saveLineItemStmt.setString(TxlJournalConstants.DEBTOR, strdebtor);
            saveLineItemStmt.setInt(TxlJournalConstants.TRANLINENO,
                    lineItemCounter.getIndex());

            result += saveLineItemStmt.executeUpdate();

            // Save the item with discount in another Row of TXL_JOURNAL
            if (isItemDiscount) {
                lineItemCounter.increment();
                stractsalesamount = String.valueOf(new BigDecimal(lineItem
                        .getSale().getDiscountAmount()).divide(new BigDecimal(
                        lineItem.getSale().getQuantity()), RoundingMode.FLOOR));
                strdebtor = String.valueOf(new BigDecimal(stritemcount)
                        .multiply(new BigDecimal(stractsalesamount)));

                if (swapFlag) {
                    strcreditor = strdebtor;
                } else {
                    strcreditor = null;
                }

                if (swapFlag) {
                    strdebtor =  null;
                }

                StringBuilder salesAmount = new StringBuilder();
                salesAmount.append("-").append(stractsalesamount);

                saveLineItemStmt.setString(TxlJournalConstants.ACCOUNT_CODE,
                        TransactionVariable.ITEM_DISCOUNT_CODE);
                saveLineItemStmt.setString(TxlJournalConstants.ACTSALESAMOUNT,
                        salesAmount.toString());
                saveLineItemStmt.setString(TxlJournalConstants.CREDITOR,
                        strcreditor);
                saveLineItemStmt.setString(TxlJournalConstants.DEBTOR,
                        strdebtor);
                saveLineItemStmt.setInt(TxlJournalConstants.TRANLINENO,
                        lineItemCounter.getIndex());
                result += saveLineItemStmt.executeUpdate();
            }

            if (!strAccountCode.equals(TransactionVariable.CASH_CODE)) {
                // Most of the time codes pass here.
                return result;
            }

            // This is a special case:
            // Save the tenderChange in another Row of TXL_JOURNAl
            TenderChange tenderChange = lineItem.getTender().getTenderChange();

            if (0 == tenderChange.getAmount()) {
                return result;
            }

            if (swapFlag) {
                strcreditor = null;
            } else {
                strcreditor = String.valueOf(tenderChange.getAmount());
            }

            if (swapFlag) {
                strdebtor = String.valueOf(tenderChange.getAmount());
            } else {
                strdebtor = null;
            }

            lineItemCounter.increment();
            saveLineItemStmt.setString(TxlJournalConstants.CREDITOR,
                    strcreditor);
            saveLineItemStmt.setString(TxlJournalConstants.DEBTOR, strdebtor);
            saveLineItemStmt.setInt(TxlJournalConstants.TRANLINENO,
                    lineItemCounter.getIndex());
            result += saveLineItemStmt.executeUpdate();
        } catch (SQLException ex) {
            if (ex.getErrorCode() == ERRORCODE_DUPLICATE
                    || ex.getErrorCode() == ERRORCODE_DEADLOCK) {
                tp.println("SQL duplication/deadlock error.",
                        ex.getErrorCode());
                throw ex;
            }
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Saving the Line item to journal failed.\n"
                            + ex.getMessage());
            throw new DaoException("SQLException:@"
                    + "SQlServerConsolidationDAO.saveLineItemToJournal", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to process normal transaction.\n"
                    + ex.getMessage());
            throw new DaoException("ParseException:@"
                    + "SQLServerConsolidationDAO.saveLineItemToJournal", ex);
        } finally {
            tp.methodExit(result);
        }

        return result;
    }

    /**
     * Checks value if fromValue is greater than or equal to toValue.
     * @param fromValue The value to be compare from.
     * @param toValue   The value to be compare.
     * @return null if greater or equal, else return string value of toValue.
     */
    private String checkValue(final int fromValue, final int toValue) {
        if (fromValue >= toValue) {
            return null;
        } else {
            return String.valueOf(toValue);
        }
    }

    /**
     * Private Method that implement the Normal/Regular Transaction.
     *
     * @param transaction
     *            The Transaction Information
     * @param posLogXml
     *            The POSLog xml to save.
     * @param saveLineItemStmt
     *            The Prepared Statement for saving the Line Item in the
     *            TXL_JOURNAL
     * @return The number of rows affected in the database.
     * @throws DaoException
     *             The exception thrown when processing Normal transaction
     *             failed.
     * @throws SQLException
     *             The exception thrown when there is SQL error occurs.
     */
    private int doNormalTransaction(final Transaction transaction,
            final String posLogXml, final PreparedStatement saveLineItemStmt)
            throws DaoException, SQLException {
        String functionName = className + "doNormalTransaction";

        tp.methodEnter("doNormalTransaction");

        int result = 0;

        try {
            /* Update the journal table */
            RetailTransaction customerOrder = transaction
                    .getRetailTransaction();

            // Specify the Additional information for TXL_Journal
            saveLineItemStmt.setString(TxlJournalConstants.TRANNO,
                    transaction.getSequenceNo());
            saveLineItemStmt.setString(TxlJournalConstants.SUMMARYDATEID,
                    transaction.getBusinessDayDate());
            saveLineItemStmt.setString(TxlJournalConstants.ORIGINALTRANNO,
                    null);
            saveLineItemStmt.setString(TxlJournalConstants.TRANLINENO,
                    transaction.getSequenceNo());
            // sqlstmt #6 param is transaction number

            List<LineItem> lineItems = customerOrder.getLineItems();
            LineItemCounter lineItemCounter = new LineItemCounter();
            for (LineItem lineItem : lineItems) {
                result += saveLineItemToJournal(lineItem, saveLineItemStmt,
                        lineItemCounter, false);
            }
        } catch (SQLException ex) {
            if (ex.getErrorCode() == ERRORCODE_DUPLICATE
                    || ex.getErrorCode() == ERRORCODE_DEADLOCK) {
                    tp.println("SQL duplication/deadlock error.",
                        ex.getErrorCode());
                throw ex;
            }
            LOGGER.logAlert(functionName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to process normal transaction.\n"
                    + ex.getMessage());
            throw new DaoException("ParseException:@"
                    + "SQlServerConsolidationDAO.doNormalTransaction", ex);
        } catch (DaoException ex) {
            LOGGER.logAlert(functionName, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to process normal transaction.\n"
                    + ex.getMessage());
            throw new DaoException("DaoException:@"
                    + "SQlServerConsolidationDAO.doNormalTransaction", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(functionName, functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to process normal transaction.\n"
                    + ex.getMessage());
            throw new DaoException("Exception:@"
                    + "SQlServerConsolidationDAO.doNormalTransaction", ex);
        } finally {
            tp.methodExit(result);
        }

        return result;
    }

    /**
     * Private Method that implement the VOIDING of transaction.
     *
     * @param transaction
     *            The Transaction Information
     * @param posLogXml
     *            The POSLog XML of the transaction
     * @param connection
     *            The Database Connection
     * @param saveLineItemStmt
     *            The Prepared Statement for saving the Line Item in the
     *            TXL_JOURNAL
     * @param updtPoslogStmnt
     *            The Prepared Statement for saving the POSLog xml in the POSLOg
     *            Table
     * @return The Number of rows affected
     * @throws DaoException
     *             The Exception thrown when voiding of the transaction fail.
     * @throws SQLException
     *             The exception thrown when SQL error occurs.
     */
    private int doVoidTransaction(final Transaction transaction,
            final String posLogXml, final Connection connection,
            final PreparedStatement saveLineItemStmt,
            final PreparedStatement updtPoslogStmnt) throws DaoException,
            SQLException {

        String functionName = className + "doVoidTransaction";

        tp.methodEnter("doVoidTransaction");

        int result = 0;

        try {
            RetailTransaction customerOrder = transaction
                    .getRetailTransaction();

            String tranNo = customerOrder.getVoidSequenceNumber();

            // Specify the Additional information for TXL_Journal
            saveLineItemStmt.setString(TxlJournalConstants.TRANNO, tranNo);
            saveLineItemStmt.setString(TxlJournalConstants.SUMMARYDATEID,
                    customerOrder.getVoidBusinessDayDate());
            saveLineItemStmt.setString(TxlJournalConstants.ORIGINALTRANNO,
                    transaction.getSequenceNo());
            saveLineItemStmt.setString(TxlJournalConstants.VOIDFLAG, "1");
            saveLineItemStmt.setString(TxlJournalConstants.OPERATORCODE,
                    customerOrder.getVoidOperatorID());
            saveLineItemStmt.setString(TxlJournalConstants.TRANLINENO, tranNo);
            // sqlstmt #6 param is transaction number

            /* Update the journal table */
            List<LineItem> lineItems = customerOrder.getLineItems();
            LineItemCounter lineItemCounter = new LineItemCounter();
            for (LineItem lineItem : lineItems) {
                // Must put TRUE in the third parameter since data of creditor
                // and debtor should be swapped.
                result += saveLineItemToJournal(lineItem, saveLineItemStmt,
                        lineItemCounter, true);

            }
        } catch (SQLException ex) {
            if (ex.getErrorCode() == ERRORCODE_DUPLICATE
                    || ex.getErrorCode() == ERRORCODE_DEADLOCK) {
                tp.println("SQL duplication/deadlock error.",
                        ex.getErrorCode());
                throw ex;
            }
            LOGGER.logAlert(functionName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to process void transaction.\n" + ex.getMessage());
            throw new DaoException(
                    "SQLException:@SQlServerConsolidationDAO.doVoidTransaction",
                    ex);
        } catch (DaoException ex) {
            LOGGER.logAlert(functionName, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to process void transaction.\n" + ex.getMessage());
            throw new DaoException("DaoException:@"
                    + "SQlServerConsolidationDAO.doVoidTransaction", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(functionName, functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to process void transaction.\n" + ex.getMessage());
            throw new DaoException("Exception:@"
                    + "SQlServerConsolidationDAO.doVoidTransaction", ex);
        } finally {
            tp.methodExit(result);
        }
        return result;
    }

    /**
     * Private Method for RETURNING the transaction.
     *
     * @param transaction
     *            The current transaction
     * @param poslogOrig
     *            The Original POSLog
     * @param tranNoOrig
     *            The transaction number of the Original POSLog
     * @param connection
     *            The database connection
     * @param saveLineItemStmt
     *            The Prepared Statement for inserting the lineitem(s) in the
     *            TXL_JOURNAL
     * @param updtPoslogStmnt
     *            The Prepared Statement for updating transaction type of the
     *            target transaction to be returned
     * @return Returns the number of affected rows
     * @throws DaoException
     *             The Exception thrown when the process fails
     * @throws SQLException
     *             The Exception thrown when SQL error occurs.
     */
    private int doReturnTransaction(final Transaction transaction,
            final PosLog poslogOrig, final String tranNoOrig,
            final Connection connection,
            final PreparedStatement saveLineItemStmt,
            final PreparedStatement updtPoslogStmnt) throws DaoException,
            SQLException {

        String functionName = className + "doReturnTransaction";

        tp.methodEnter("doReturnTransaction");

        int result = 0;

        // Get the Original and Returner Transaction number
        RetailTransaction customerOrderOrig = poslogOrig
                .getTransaction().getRetailTransaction();
        try {
            // Get the returner transaction number
            String tranNo = transaction.getSequenceNo();

            // Specify the Additional information for TXL_Journal
            saveLineItemStmt.setString(TxlJournalConstants.TRANNO, tranNo);
            saveLineItemStmt.setString(TxlJournalConstants.SUMMARYDATEID,
                    transaction.getBusinessDayDate());
            saveLineItemStmt.setString(TxlJournalConstants.ORIGINALTRANNO,
                    tranNoOrig);
            saveLineItemStmt.setString(TxlJournalConstants.RETURNFLAG, "1");
            saveLineItemStmt.setString(TxlJournalConstants.OPERATORCODE,
                    transaction.getOperatorID().getValue());
            saveLineItemStmt.setString(TxlJournalConstants.TRANLINENO, tranNo);
            // sqlstmt #6 param is transaction number

            /* Update the journal table */
            List<LineItem> lineItems = customerOrderOrig.getLineItems();
            LineItemCounter lineItemCounter = new LineItemCounter();
            for (LineItem lineItem : lineItems) {
                // Must put TRUE in the third parameter since
                // data of creditor and debtor should be swapped.
                result += saveLineItemToJournal(lineItem, saveLineItemStmt,
                        lineItemCounter, true);
            }
        } catch (SQLException ex) {
            if (ex.getErrorCode() == ERRORCODE_DUPLICATE
                    || ex.getErrorCode() == ERRORCODE_DEADLOCK) {
                tp.println("SQL duplication/deadlock error.",
                        ex.getErrorCode());
                throw ex;
            }
            LOGGER.logAlert(functionName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to process return transaction\n "
                    + ex.getMessage());
            throw new DaoException("SQLException:@"
                    + "SQlServerConsolidationDAO.doReturnTransaction", ex);
        } catch (DaoException ex) {
            LOGGER.logAlert(functionName, functionName, Logger.RES_EXCEP_DAO,
                    "Failed to process return transaction\n "
                    + ex.getMessage());
            throw new DaoException("DaoException:@"
                    + "SQlServerConsolidationDAO.doReturnTransaction", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(functionName, functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to process return transaction\n "
                    + ex.getMessage());
            throw new DaoException("ParseException:@"
                    + "SQlServerConsolidationDAO.doReturnTransaction", ex);
        } finally {
            tp.methodExit(result);
        }

        return result;
    }

    /**
     * Holds the list of number of guest.
     */
    private List<String> arrguest = new ArrayList<String>();

    /**
     * Private Method that update the Total Guest Department Day.
     *
     * @param txCorpId
     *            The corporate identifier string.
     * @param transaction
     *            The current transaction
     * @param line
     *            The current LineItem
     * @param connection
     *            The database connection
     * @param txtype
     *            The transaction type
     * @return The number of rows affected
     * @throws DaoException
     *             The Exception thrown when the method fails
     */
    private int doTotalGuestDptDayUpdate(final String txCorpId,
            final Transaction transaction, final LineItem line,
            final Connection connection, final String txtype)
            throws DaoException {


        String functionName = className + "doTotalGuestDptDayUpdate";

        tp.methodEnter("doTotalGuestDptDayUpdate");
        PreparedStatement updateGuestTable = null;
        int result = 0;
        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateGuestTable = connection
                    .prepareStatement(sqlStatement
                            .getProperty("update-total-guest-dept-day"));

            RetailTransaction customerOrder = transaction
                    .getRetailTransaction();

            if (line.getSale() != null) {
                if (!arrguest.contains(line.getSale().getDepartment())) {
                    arrguest.add(line.getSale().getDepartment());
                    updateGuestTable.setString(SQLStatement.PARAM1, txCorpId);
                    updateGuestTable.setString(SQLStatement.PARAM2,
                            transaction.getRetailStoreID());
                    updateGuestTable.setString(SQLStatement.PARAM3,
                            transaction.getWorkStationID().getValue());
                    updateGuestTable.setString(SQLStatement.PARAM4,
                            transaction.getOperatorID().getValue());
                    updateGuestTable.setString(SQLStatement.PARAM5,
                            transaction.getBusinessDayDate());
                    updateGuestTable.setString(SQLStatement.PARAM6,
                            line.getSale().getDepartment());

                    SimpleDateFormat dateFormat = new SimpleDateFormat(
                            "yyyy-MM-dd HH:MM:SS");
                    java.util.Date date = dateFormat.parse(transaction
                            .getBeginDateTime().replace('T', ' '));
                    @SuppressWarnings("deprecation")
                    String timeZone = String.valueOf(date.getHours());
                    updateGuestTable.setString(SQLStatement.PARAM7, timeZone);

                    // SALESGUESTCNT = 9
                    if (TxTypes.NORMAL.equals(txtype)) {
                        updateGuestTable.setString(SQLStatement.PARAM8, "1");
                        updateGuestTable.setString(SQLStatement.PARAM9, "0");
                        updateGuestTable.setString(SQLStatement.PARAM10, "0");
                    } else if (TxTypes.RETURNER.equals(txtype)) {
                     // RETURNGUESTCNT = 10
                        updateGuestTable.setString(SQLStatement.PARAM8, "0");
                        updateGuestTable.setString(SQLStatement.PARAM9, "1");
                        updateGuestTable.setString(SQLStatement.PARAM10, "0");
                    } else if (TxTypes.VOIDER.equals(txtype)) {
                     // VOIDGUESTCNT = 11
                        updateGuestTable.setString(SQLStatement.PARAM4,
                                customerOrder.getVoidOperatorID());
                        updateGuestTable.setString(SQLStatement.PARAM8, "0");
                        updateGuestTable.setString(SQLStatement.PARAM9, "0");
                        updateGuestTable.setString(SQLStatement.PARAM10, "1");
                    }
                    result = updateGuestTable.executeUpdate();
                } else {
                    tp.println("Sale department not found.");
                }
            } else {
                tp.println("Line sale not found.");
            }
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(functionName, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to update Total Guest" + " Department Day Update\n"
                            + ex.getMessage());
            throw new DaoException("SQLStatementException:@"
                    + "SqlServerConsolidationDAO.doTotalGuestDptDayUpdate", ex);
        } catch (SQLException ex) {
            LOGGER.logAlert(
                    functionName,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to update Total Guest Department"
                            + " Day Update with SQL ErrorCode = "
                            + ex.getErrorCode() + " \n" + ex.getMessage());
            throw new DaoException("SQLException:@"
                    + "SqlServerConsolidationDAO.doTotalGuestDptDayUpdate", ex);
        } catch (ParseException ex) {
            LOGGER.logAlert(
                    functionName,
                    functionName,
                    Logger.RES_EXCEP_PARSE,
                    "Failed to update Total Guest Department Day Update\n"
                            + ex.getMessage());
            throw new DaoException("ParseException:@"
                    + "SqlServerConsolidationDAO.doTotalGuestDptDayUpdate", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(
                    functionName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to update Total Guest Department Day Update\n"
                            + ex.getMessage());
            throw new DaoException("Exception:@"
                    + "SqlServerConsolidationDAO.doTotalGuestDptDayUpdate", ex);
        } finally {
            closeObject(updateGuestTable);
            tp.methodExit(result);
        }
        return result;
    }

    @Override
    public final int doPOSLogJournalization(final PosLog posLog,
            final TransactionInfo txinfo) throws DaoException {

        snap = (SnapLogger) SnapLogger.getInstance();
        String functionName = className + "doPOSLogJournalization";

        tp.methodEnter("doPOSLogJournalization");
        tp.println("txinfo", txinfo);

        int result = 0;
        String txtype = "";
        String currentTransactionNumber = null;
        Connection connection = null;
        PreparedStatement saveLineItemStmt = null;
        PreparedStatement updtPoslogStatusStmnt = null;
        PreparedStatement updtPoslogTxTypeStmnt = null;
        String posLogXml = txinfo.getTx();
        Transaction transaction = posLog.getTransaction();
        Transaction transactionOrig = transaction;
        RetailTransaction customerOrder = transaction
                .getRetailTransaction();

        // Get the transaction type
        String transactionStatus = customerOrder.getTransactionStatus();
        // Make the sequence be the temporary
        // transaction number of the Transaction being processed
        currentTransactionNumber = transaction.getSequenceNo();

        try {
            if (dbManager == null) {
                dbManager = JndiDBManagerMSSqlServer.getInstance();
            }

            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            saveLineItemStmt = connection.prepareStatement(sqlStatement
                    .getProperty("save-lineItem"));
            updtPoslogTxTypeStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("update-poslog-txtype"));
            updtPoslogStatusStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("update-poslog-status"));

            /* Set the general information for every transaction in Journal */
            saveLineItemStmt.setString(TxlJournalConstants.OPERATORCODE,
                    transaction.getOperatorID().getValue());
            saveLineItemStmt.setString(TxlJournalConstants.CORPID,
                    txinfo.getTxCorpId());
            saveLineItemStmt.setString(TxlJournalConstants.STOREID,
                    transaction.getRetailStoreID());
            saveLineItemStmt.setString(TxlJournalConstants.TERMID,
                    transaction.getWorkStationID().getValue());
            saveLineItemStmt.setString(TxlJournalConstants.CUSTOMERID,
                    transaction.getCustomerid());
            saveLineItemStmt.setString(TxlJournalConstants.RETURNFLAG, null);
            saveLineItemStmt.setString(TxlJournalConstants.VOIDFLAG, null);
            saveLineItemStmt.setString(TxlJournalConstants.CANCELFLAG, null);

            // Is the transaction has no Transaction Status?
            // If yes, then it is possible that the transaction
            // might be Normal or Return Transaction
            if (null == transactionStatus || transactionStatus
                    .equals(TransactionVariable.TRANSACTION_STATUS_RETURNED)) {
                // Is the transaction's Customer Order has Transaction Link?
                // If yes, it means the transaction needs to be returned.

                if (null != transaction.getRetailTransaction().getTransactionLink()) {
                    TransactionLink transactionLink = transaction
                            .getRetailTransaction().getTransactionLink();
                    String tranNoOrig = transactionLink.getSequenceNo();

                    // Use the Original Transaction's CustomerOrder
                    // in saving data for TXL_JOURNAL
                    String origPosLogXML = getTransaction(
                            transactionLink.getRetailStoreID(),
                            tranNoOrig, transactionLink.getWorkStationID().getValue(),
                            transactionLink.getBusinessDayDate(), connection);
                    XmlSerializer<PosLog> posLogSerializer =
                        new XmlSerializer<PosLog>();
                    PosLog poslogOrig = posLogSerializer.unMarshallXml(
                            origPosLogXML, PosLog.class);

                    result = doReturnTransaction(transaction, poslogOrig,
                            tranNoOrig, connection, saveLineItemStmt,
                            updtPoslogTxTypeStmnt);

                    txtype = TxTypes.RETURNER;

                    // In Return transaction the Returned transaction must
                    // be Subject to Summary table update.
                    Transaction original = poslogOrig.getTransaction();
                    original.setOperatorID(transaction.getOperatorID());
                    original.setWorkStationID(transaction.getWorkStationID());
                    original.setRetailStoreID(transaction.getRetailStoreID());
                    original.setBeginDateTime(transaction.getBeginDateTime());
                    original.setReceiptDateTime(transaction
                            .getReceiptDateTime());
                    transaction = original;

                } else {
                    result = doNormalTransaction(transaction, posLogXml,
                            saveLineItemStmt);
                    txtype = TxTypes.NORMAL;
                }
            } else if (transactionStatus
                    .equals(TransactionVariable.TRANSACTION_STATUS_VOIDED)) {
                // Is the the transaction going to be voided?
                // If yes, do the voiding of transaction

                // If pass here it means the transaction will be voided.
                result = doVoidTransaction(transaction, posLogXml, connection,
                        saveLineItemStmt, updtPoslogTxTypeStmnt);
                currentTransactionNumber = customerOrder
                        .getVoidSequenceNumber();
                txtype = TxTypes.VOIDER;

                // modify transaction object to update it with the proper
                // transaction data for the voider transaction
                //modify by mlwang 2014/12/12 start
                OperatorID opID = new OperatorID();
                opID.setValue(customerOrder.getVoidOperatorID());
                //transaction.setOperatorID(customerOrder.getVoidOperatorID());
                transaction.setOperatorID(opID);
                //modify by mlwang 2014/12/12 end
                transaction.getWorkStationID().setValue(customerOrder
                        .getVoidWorkstationID());
//                transaction.setWorkStationID(customerOrder
//                        .getVoidWorkstationID());
                transaction.setRetailStoreID(customerOrder
                        .getVoidRetailStoreID());
                transaction.setBusinessDayDate(customerOrder
                        .getVoidBusinessDayDate());
                transaction.setBeginDateTime(customerOrder
                        .getVoidBeginDateTime());
            } else if (transactionStatus
                    .equals(TransactionVariable.TRANSACTION_STATUS_CANCELED)) {
                saveLineItemStmt
                        .setString(TxlJournalConstants.CANCELFLAG, "1");
                txtype = TxTypes.CANCELED;
            }

            // Do All Summary Tables Update
            if (!TxTypes.CANCELED.equals(txtype)) {
                List<LineItem> newList = transaction
                        .getRetailTransaction().getLineItems();

                sortLineItems(newList);

                for (LineItem line : newList) {
                    tp.println("line", line);
                    tp.println("corpid", txinfo.getTxCorpId());
                    tp.println("txtype", txtype);

                    result += doTotalAccountDayUpdate(txinfo.getTxCorpId(),
                            transaction, line, connection, txtype);
                    result += doTotalDepartmentSumDay(txinfo.getTxCorpId(),
                            transactionOrig, line, connection, txtype);
                    result += doTotalItemDayUpdate(txinfo.getTxCorpId(),
                            transaction, line, connection, txtype);
                    result += doTotalFinancialUpdate(txinfo.getTxCorpId(),
                            transactionOrig, line, connection, txtype);
                    result += doTotalDrawerFinancialUpdate(
                            txinfo.getTxCorpId(), transactionOrig, line,
                            connection, txtype);
                    result += doTotalGuestDptDayUpdate(txinfo.getTxCorpId(),
                            transaction, line, connection, txtype);
                }
                // Clears the storage of dpt
                arrguest.clear();
            }
            // --End test code

            // Update the status of the transaction in the TXL_POSLOG
            updtPoslogStatusStmnt.setString(UpdatePOSLogStatusConstants.STATUS,
                    POSLOG_STATUS_NORMAL);
            updtPoslogStatusStmnt.setString(UpdatePOSLogStatusConstants.CORPID,
                    txinfo.getTxCorpId());
            updtPoslogStatusStmnt.setString(
                    UpdatePOSLogStatusConstants.STOREID,
                    transaction.getRetailStoreID());
            updtPoslogStatusStmnt.setString(
                    UpdatePOSLogStatusConstants.TERMINALID,
                    transaction.getWorkStationID().getValue());
            updtPoslogStatusStmnt.setString(UpdatePOSLogStatusConstants.TXID,
                    currentTransactionNumber);
            updtPoslogStatusStmnt.setString(
                    UpdatePOSLogStatusConstants.SUMMARYDATEID,
                    transaction.getBusinessDayDate());
            result += updtPoslogStatusStmnt.executeUpdate();

            // Do commit here. To finish transaction.
            connection.commit();
        } catch (SQLException ex) {
            // Is the exception caused by deadlock?
            int errorcode = ex.getErrorCode();
            if (errorcode == ERRORCODE_DUPLICATE) {
                result = SQLResultsConstants.ROW_DUPLICATE;
            } else {
                if (errorcode == ERRORCODE_DEADLOCK) {
                    tp.println("SQL deadlock exception.");
                    result = SQLResultsConstants.DEADLOCK;
                } else {
                    result = NORMAL_EXCEPTION;
                }

                if (errorcode == NORMAL_EXCEPTION) {
                    LOGGER.logAlert(progName, functionName,
                            Logger.RES_EXCEP_SQL,
                            "Failed to process POSLog Journal with"
                                    + " SQL error code " + ex.getErrorCode()
                                    + ": \n" + ex.getMessage());
                }
                rollBack(connection, "SQLServerPosLogDAO:@"
                        + "SqlServerConsolidationDAO.doPOSLogJournalization",
                        ex);
            }
            Snap.SnapInfo info = snap.write("Error occured when SQLException",
                    txinfo.getTx());
            LOGGER.logSnap(progName, functionName,
                    "Output error Transection data to snap file.", info);
        } catch (SQLStatementException ex) {
            // Make result set equal -1; In order to verify that
            // a rollback has been called
            result = NORMAL_EXCEPTION;
            LOGGER.logAlert(progName, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to process POSLog Journal\n " + ex.getMessage());
            rollBack(connection, "SQLServerPosLogDAO: @"
                    + "doPOSLogJournalization()", ex);
            Snap.SnapInfo info =
                    snap.write("Error occured when SQLStatementException",
                    txinfo.getTx());
            LOGGER.logSnap(progName, functionName,
                    "Output error Transection data to snap file.", info);
        } catch (Exception ex) {
            // Make result set equal -1 In order
            // to verify that a rollback has been called
            result = NORMAL_EXCEPTION;
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to process POSLog Journal\n " + ex.getMessage());
            rollBack(connection, "SQLServerPosLogDAO:"
                    + " @doPOSLogJournalization()", ex);
            Snap.SnapInfo info =
                    snap.write("Error occured when Exception",
                    txinfo.getTx());
            LOGGER.logSnap(progName, functionName,
                    "Output error Transection data to snap file.", info);
        } finally {
            boolean hasErrorOccur = false;
            hasErrorOccur = closeObject(saveLineItemStmt)
                    || closeObject(updtPoslogTxTypeStmnt);

            // Are there EXCEPTION happen aside from deadlock?
            // If yes, set the Transaction Status into Abnormal in the
            // TXL_POSLOG
            if (NORMAL_EXCEPTION == result && updtPoslogStatusStmnt != null) {
                try {
                    updtPoslogStatusStmnt.setString(
                            UpdatePOSLogStatusConstants.STATUS,
                            POSLOG_STATUS_ABNORMAL);
                    updtPoslogStatusStmnt.setString(
                            UpdatePOSLogStatusConstants.TXID,
                            currentTransactionNumber);
                    updtPoslogStatusStmnt.setString(
                            UpdatePOSLogStatusConstants.CORPID,
                            txinfo.getTxCorpId());
                    updtPoslogStatusStmnt.setString(
                            UpdatePOSLogStatusConstants.STOREID,
                            transaction.getRetailStoreID());
                    updtPoslogStatusStmnt.setString(
                            UpdatePOSLogStatusConstants.TERMINALID,
                            transaction.getWorkStationID().getValue());
                    updtPoslogStatusStmnt.setString(
                            UpdatePOSLogStatusConstants.SUMMARYDATEID,
                            transaction.getBusinessDayDate());
                    result += updtPoslogStatusStmnt.executeUpdate();
                    connection.commit();
                    tp.println("Consolidation failed. "
                            + "Status was set to ABNORMAL.");
                } catch (SQLException ex) {
                    LOGGER.logAlert(progName, functionName,
                            Logger.RES_EXCEP_SQL,
                            "Failed to set TXL_POSLOG status to"
                                    + " 2 (ABNORMAL)\n " + ex.getMessage());
                    tp.println("Consolidation failed. "
                            + "But status was not set to ABNORMAL.");
                    hasErrorOccur = true;
                } catch (Exception ex) {
                    LOGGER.logAlert(progName, functionName,
                            Logger.RES_EXCEP_GENERAL,
                            "Failed to set TXL_POSLOG status to"
                                    + " 2 (ABNORMAL)\n " + ex.getMessage());
                    tp.println("Consolidation failed. "
                            + "But status was not set to ABNORMAL.");
                } finally {
                    hasErrorOccur = hasErrorOccur
                                || closeConnectionObjects(connection,
                                updtPoslogStatusStmnt);
                }
            }
            closeConnectionObjects(connection, updtPoslogStatusStmnt);

            tp.methodExit(result);
        }

        return result;
    }

    /**
     * Get the transaction POSLog XML by specifying the transaction number.
     *
     * @param txNumber
     *            The Transaction Number
     * @param deviceno
     *            The device number of the current device accessing
     * @param connection
     *            The database connection
     * @return The POSLog XML representation for the Transaction
     * @throws DaoException
     *             The Exception thrown when the getting of Transaction fails.
     */
    private String getTransaction(final String storeid,
            final String txNumber, final String deviceno,
            final String summarydateid, final Connection connection)
                    throws DaoException {

        String functionName = className + "getTransaction";

        tp.methodEnter("getTransaction");
        tp.println("storeid", storeid);
        tp.println("txNumber", txNumber);
        tp.println("deviceno", deviceno);
        tp.println("summarydateid", summarydateid);

        PreparedStatement select = null;
        ResultSet result = null;
        String posLogXML = "";
        try {
            /*
             * Retrieves sql query statement from
             * /resource/ncr.res.webuitools.property/sql_statements.xml
             */
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-poslog-xml-by-id"));

            select.setString(1, GlobalConstant.getCorpid());
            select.setString(2, storeid);
            select.setString(3, deviceno);
            select.setString(4, summarydateid);
            select.setString(5, txNumber);
            result = select.executeQuery();

            if (result.next()) {
                posLogXML = result.getString(result.findColumn("tx"));
            } else {
                tp.println("Transaction not found.");
            }
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(progName, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT, "Failed to get transaction "
                            + txNumber + ": " + ex.getMessage());
            throw new DaoException("SQLStatementException:@"
                    + "SqlServerConsolidationDAO.getTransaction", ex);
        } catch (SQLException ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to get transaction " + txNumber + ": "
                            + ex.getMessage());
            throw new DaoException("SQLException:@"
                    + "SqlServerConsolidationDAO.getTransaction", ex);
        } finally {
            closeConnectionObjects(null, select, result);

            tp.methodExit();
        }

        return posLogXML;
    }

    /**
     * Private method that update the TXU_TOTAL_DPTSUMDAY summary table.
     *
     * @param txCorpId
     *            The corporate identifier string.
     * @param transaction
     *            The current transaction
     * @param line
     *            The sale's line
     * @param connection
     *            The database
     * @param txtype
     *            The transaction type
     * @return    The number of rows affected.
     * @throws DaoException
     *             The exception thrown when updating the TXU_TOTAL_ACNT_DAY
     *             fails.
     * @throws SQLException
     *             The exception thrown when SQL error occurs.
     */
    @SuppressWarnings("deprecation")
    private int doTotalDepartmentSumDay(final String txCorpId,
            final Transaction transaction, final LineItem line,
            final Connection connection, final String txtype)
            throws DaoException, SQLException {

        String functionName = className + "doTotalDepartmentSumDay";

        tp.methodEnter("doTotalDepartmentSumDay");

        int resultCode = 0;
        SQLStatement sqlStatement = null;
        boolean isDup = false;
        PreparedStatement updateStmt = null;
        PreparedStatement insertStmt = null;

        long lSalesItemCount = 0;
        long lReturnItemCount = 0;
        double dSalesSalesAmount = 0;
        double dReturnSalesAmount = 0;
        long lDcItemCount = 0;
        double dDcSalesAmount = 0;
        double dVoidSalesAmount = 0;
        long lVoidItemCount = 0;
        String accountCode = null;
        String timeZoneCode = null;
        SimpleDateFormat dateFormat = null;
        java.util.Date date = null;

        try {
            if (null == line.getAccountCode()) {
                accountCode =  null;
            } else {
                accountCode = line.getAccountCode();
            }

            if (null == accountCode) {
                tp.println("AccountCode is invalid.");
                return 0;
            }

            // Is Transaction Discount?(0061)
            boolean isDiscountLine = accountCode
                    .equals(TransactionVariable.DISCOUNT_CODE);
            boolean isItemDiscount = false;
            Sale sale = null;

            // Process only LineItem with Sale (0060)
            // Set the local variable flag to tell the Account code
            isDiscountLine = accountCode
                    .equals(TransactionVariable.DISCOUNT_CODE);
            // Is the accountcode for Discount? OR
            // Is the accountcode not for Item?
            // If yes, validate if the accountcode is for Item
            if (isDiscountLine
                    || !(accountCode.equals(TransactionVariable.ITEM_CODE))) {
                tp.println("Discount line does not match with "
                        + "the AccountCode.");
                return 0;
            } else {
                sale = line.getSale();
                // Is Sale Item has Discount?(1060)
                if (0 != line.getSale().getDiscountAmount()) {
                        isItemDiscount = true;
                }
            }

            // for timezonecode
            dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
            date = dateFormat.parse(transaction.getBeginDateTime().replace('T',
                    ' '));
            timeZoneCode = String.valueOf(date.getHours());

            /** @Start: Update Total_DPTSUMDAY **/
            sqlStatement = SQLStatement.getInstance();
            updateStmt = connection.prepareStatement(sqlStatement
                    .getProperty("update-total-dept-day"));

            updateStmt.setString(SQLStatement.PARAM9, txCorpId);
            updateStmt.setString(SQLStatement.PARAM10,
                    transaction.getRetailStoreID());
            updateStmt.setString(SQLStatement.PARAM11,
                    transaction.getBusinessDayDate());
            updateStmt.setString(SQLStatement.PARAM12, timeZoneCode);
            updateStmt.setString(SQLStatement.PARAM13,
                    line.getSale().getDepartment());

            // set the update variables depending on the txtype
            if (TxTypes.NORMAL.equals(txtype)) {
                lSalesItemCount = line.getSale().getQuantity();
                dSalesSalesAmount = line.getSale().getExtendedAmt();

                if (isItemDiscount) {
                    Double lItemDebtor = Double.valueOf(sale
                            .getDiscountAmount());
                    dSalesSalesAmount -= lItemDebtor;
                    lDcItemCount += sale.getQuantity();
                    dDcSalesAmount += lItemDebtor;
                }

            } else if (TxTypes.RETURNER.equals(txtype)) {
                lReturnItemCount = line.getSale().getQuantity();
                dReturnSalesAmount = line.getSale().getExtendedAmt();
                dSalesSalesAmount -= line.getSale().getExtendedAmt();
                lSalesItemCount -= line.getSale().getQuantity();

                if (isItemDiscount) {
                    Double lCreditor = Double.valueOf(sale.getDiscountAmount());
                    dSalesSalesAmount += lCreditor;
                    dReturnSalesAmount -= lCreditor;
                    lDcItemCount -= sale.getQuantity();
                    dDcSalesAmount -= lCreditor;
                }

            } else if (TxTypes.VOIDER.equals(txtype)) {
                dSalesSalesAmount -= line.getSale().getExtendedAmt();
                lSalesItemCount -= line.getSale().getQuantity();
                lVoidItemCount += sale.getQuantity();
                dVoidSalesAmount += sale.getExtendedAmt();

                dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:MM:SS");
                date = dateFormat.parse(transaction
                        .getRetailTransaction().getVoidBeginDateTime()
                        .replace('T', ' '));
                timeZoneCode = String.valueOf(date.getHours());

                updateStmt.setString(SQLStatement.PARAM10, transaction
                        .getRetailTransaction().getVoidRetailStoreID());
                updateStmt
                        .setString(SQLStatement.PARAM11, transaction
                                .getRetailTransaction()
                                .getVoidBusinessDayDate());
                updateStmt.setString(SQLStatement.PARAM12, timeZoneCode);
                updateStmt.setString(SQLStatement.PARAM13,
                        line.getSale().getDepartment());

                if (isItemDiscount) {
                    Double lCreditor = Double.valueOf(sale.getDiscountAmount());
                    dSalesSalesAmount += lCreditor;
                    dVoidSalesAmount -= lCreditor;
                    lDcItemCount -= sale.getQuantity();
                    dDcSalesAmount -= lCreditor;
                }
            }

            // put new values into update string
            updateStmt.setLong(SQLStatement.PARAM1, lSalesItemCount);
            updateStmt.setDouble(SQLStatement.PARAM2, dSalesSalesAmount);
            updateStmt.setLong(SQLStatement.PARAM3, lReturnItemCount);
            updateStmt.setDouble(SQLStatement.PARAM4, dReturnSalesAmount);
            updateStmt.setLong(SQLStatement.PARAM5, lVoidItemCount);
            updateStmt.setDouble(SQLStatement.PARAM6, dVoidSalesAmount);
            updateStmt.setLong(SQLStatement.PARAM7, lDcItemCount);
            updateStmt.setDouble(SQLStatement.PARAM8, dDcSalesAmount);

            resultCode = updateStmt.executeUpdate();
            /** @End: Update Total_DPTSUMDAY **/

            // return if update is success
            if (resultCode != 0) {
                tp.println("TotalDepartmentSumDay data was updated.");
                return resultCode;
            }

            /** @Start: Insert Total_DPTSUMDAY **/
            // Insert If No Rows to Update
            insertStmt = connection.prepareStatement(sqlStatement
                    .getProperty("insert-total-dept-day"));

            insertStmt.setString(SQLStatement.PARAM1, txCorpId);
            insertStmt.setString(SQLStatement.PARAM2,
                    transaction.getRetailStoreID());
            insertStmt.setString(SQLStatement.PARAM3,
                    transaction.getBusinessDayDate());
            insertStmt.setString(SQLStatement.PARAM4,
                    line.getSale().getDepartment());
            insertStmt.setString(SQLStatement.PARAM5, timeZoneCode);
            insertStmt.setString(SQLStatement.PARAM6,
                    String.valueOf(lSalesItemCount));
            insertStmt.setString(SQLStatement.PARAM7,
                    String.valueOf(dSalesSalesAmount));
            insertStmt.setString(SQLStatement.PARAM8,
                    String.valueOf(lReturnItemCount));
            insertStmt.setString(SQLStatement.PARAM9,
                    String.valueOf(dReturnSalesAmount));
            insertStmt.setString(SQLStatement.PARAM10,
                    String.valueOf(lVoidItemCount));
            insertStmt.setString(SQLStatement.PARAM11,
                    String.valueOf(dVoidSalesAmount));
            insertStmt.setString(SQLStatement.PARAM12,
                    String.valueOf(lDcItemCount));
            insertStmt.setString(SQLStatement.PARAM13,
                    String.valueOf(dDcSalesAmount));

            resultCode = insertStmt.executeUpdate();
            /** @End: Insert Total_DPTSUMDAY **/
        } catch (SQLException sqle) {
            // Is duplicate
            if (sqle.getErrorCode() != ERRORCODE_DUPLICATE) {
                if (sqle.getErrorCode() == ERRORCODE_DEADLOCK) {
                    tp.println("SQL deadlock exception.");
                    throw sqle;
                }
                LOGGER.logAlert(
                        progName,
                        functionName,
                        Logger.RES_EXCEP_SQL,
                        "Failed to update Total Department"
                                + " Sum Day with SQL ErrorCode = "
                                + sqle.getErrorCode() + " :"
                                + sqle.getMessage());
                throw new DaoException("SQLException:@"
                        + "SQLServerConsolidationDAO.doTotalDepartmentSumDay"
                        + " - Failed to update Department Sum Day", sqle);
            }
            isDup = true;
        } catch (ParseException ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_PARSE,
                    "Failed to update Total Department Sum Day :"
                            + ex.getMessage());
            throw new DaoException("ParseException:@"
                    + "SQLServerConsolidationDAO.doTotalDepartmentSumDay", ex);
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to update Total Department Sum Day :"
                            + ex.getMessage());
            throw new DaoException("SQLStatementException:@"
                    + "SQLServerConsolidationDAO.doTotalDepartmentSumDay", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(
                    progName,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to update Total Department Sum Day :"
                            + ex.getMessage());
            throw new DaoException("ParseException:@"
                    + "SQLServerConsolidationDAO.doTotalDepartmentSumDay", ex);
        } finally {
            if (isDup && updateStmt != null) {
                resultCode += updateTotalDeptDay(updateStmt);
            }
            closeObject(updateStmt);
            closeObject(insertStmt);
            tp.methodExit(resultCode);
        }
        return resultCode;
    }

    private int updateTotalDeptDay(PreparedStatement updateStmt)
            throws DaoException {
        int result = 0;
        try {
            result = updateStmt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.logAlert(progName,
                    "SQLServerConsolidationDAO.updateTotalDeptDay",
                    Logger.RES_EXCEP_SQL,
                    "Failed to update Total_DptSumDay.\n"
                    + e.getMessage());
            tp.methodExit("Failed to update Total_DptSumDay.");
            throw new DaoException("SQLException:@"
                    + "SQlServerConsolidationDAO"
                    + ".updateTotalDeptDay", e);
        } finally {
            closeObject(updateStmt);
        }
        return result;
    }

    /**
     * Private method that update the TXU_TOTAL_ITEMDAY summary table.
     *
     * @param txCorpId
     *            The transaction corporate identifier string.
     * @param transaction
     *            The current transaction
     * @param line
     *            The Line Item to be recorded
     * @param connection
     *            The database connection
     * @param txtype
     *            The transaction type
     * @return The number of rows affected
     * @throws DaoException
     *             The exception thrown when the method fails
     * @throws SQLException
     *             The exception thrown when SQL error occurs.
     */
    private int doTotalItemDayUpdate(final String txCorpId,
            final Transaction transaction, final LineItem line,
            final Connection connection, final String txtype)
            throws DaoException, SQLException {

        String functionName = className + "doTotalItemDayUpdate";

        tp.methodEnter("doTotalItemDayUpdate");

        int result = 0;
        boolean isDup = false;
        PreparedStatement insertTotalItemDayStmnt = null;
        PreparedStatement updateTotalItemDayStmnt = null;

        // Get the Sale
        Sale sale = line.getSale();

        // The Account Code
        String accountCode = null;

        try {
            accountCode = line.getAccountCode();

            // Is the accountCode unknown?
            // If yes there is no need to process here
            if (null == accountCode) {
                tp.println("AccountCode is invalid.");
                return 0;
            }

            // Set the local variable flag to tell the Account code
            boolean isDiscountLine = accountCode
                    .equals(TransactionVariable.DISCOUNT_CODE);

            boolean isItemDiscount = false;
            if (isDiscountLine
                    || !accountCode.equals(TransactionVariable.ITEM_CODE)) {
                tp.println("Discount line does match with AccountCode.");
                return 0;
            } else {
                if (0 != sale.getDiscountAmount()) {
                    isItemDiscount = true;
                }
            }

            /* Prepare values necessary for calculation TXU_TOTAL_ACNT_DAY */
            long lSalesItemCount = 0;
            long lReturnItemCount = 0;
            long lVoidItemCount = 0;
            long ldcItemCount = 0;
            double dSalesSalesAmount = 0;
            double dReturnSalesAmount = 0;
            double dVoidSalesAmount = 0;
            double dDcSalesAmount = 0;

            String strLine = sale.getLine();
            if (strLine == null || strLine.isEmpty()) {
                strLine = "0";
            }
            String strClass = sale.getClas();
            if (strClass == null || strClass.isEmpty()) {
                strClass = "0";
            }
            String strDepartment = sale.getDepartment();

            /* The calculation will start here */
            if (TxTypes.NORMAL.equals(txtype)) {
                dSalesSalesAmount += sale.getExtendedAmt();
                lSalesItemCount += sale.getQuantity();

                if (isItemDiscount) {
                    Double lItemDebtor = Double.valueOf(sale
                            .getDiscountAmount());
                    dSalesSalesAmount -= lItemDebtor;
                    ldcItemCount += sale.getQuantity();
                    dDcSalesAmount += lItemDebtor;
                }
            } else if (TxTypes.RETURNER.equals(txtype)) {
                dSalesSalesAmount -= sale.getExtendedAmt();
                lSalesItemCount -= sale.getQuantity();
                lReturnItemCount += sale.getQuantity();
                dReturnSalesAmount += sale.getExtendedAmt();

                if (isItemDiscount) {
                    Double lCreditor = Double.valueOf(sale.getDiscountAmount());
                    dSalesSalesAmount += lCreditor;
                    dReturnSalesAmount -= lCreditor;
                    ldcItemCount -= sale.getQuantity();
                    dDcSalesAmount -= lCreditor;
                }
            } else if (TxTypes.VOIDER.equals(txtype)) {
                dSalesSalesAmount -= sale.getExtendedAmt();
                lSalesItemCount -= sale.getQuantity();
                lVoidItemCount += sale.getQuantity();
                dVoidSalesAmount += sale.getExtendedAmt();

                if (isItemDiscount) {
                    Double lCreditor = Double.valueOf(sale.getDiscountAmount());
                    dSalesSalesAmount += lCreditor;
                    dVoidSalesAmount -= lCreditor;
                    ldcItemCount -= sale.getQuantity();
                    dDcSalesAmount -= lCreditor;
                }
            }
            /* End of calculation */
            SQLStatement sqlStatement = SQLStatement.getInstance();
            insertTotalItemDayStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("save-total-item-day"));
            updateTotalItemDayStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("update-total-item-day"));
            /* Set the Sales amount of each transaction of type */
            updateTotalItemDayStmnt.setString(
                    UpdtTotalItemDayConstants.DCSALESAMNT,
                    String.valueOf(dDcSalesAmount));
            updateTotalItemDayStmnt.setString(
                    UpdtTotalItemDayConstants.RETURNSALESAMNT,
                    String.valueOf(dReturnSalesAmount));
            updateTotalItemDayStmnt.setString(
                    UpdtTotalItemDayConstants.VOIDSALESAMNT,
                    String.valueOf(dVoidSalesAmount));

            /* Set the item count of each transaction of type */
            updateTotalItemDayStmnt.setString(
                    UpdtTotalItemDayConstants.DCITEMCNT,
                    String.valueOf(ldcItemCount));
            updateTotalItemDayStmnt.setString(
                    UpdtTotalItemDayConstants.RETURNITEMCNT,
                    String.valueOf(lReturnItemCount));
            updateTotalItemDayStmnt.setString(
                    UpdtTotalItemDayConstants.VOIDITEMCNT,
                    String.valueOf(lVoidItemCount));

            /* Set the SalesAmount of each transaction of type */
            updateTotalItemDayStmnt.setString(
                    UpdtTotalItemDayConstants.SALESITEMCNT,
                    String.valueOf(lSalesItemCount));
            updateTotalItemDayStmnt.setString(
                    UpdtTotalItemDayConstants.SALESSALESAMNT,
                    String.valueOf(dSalesSalesAmount));

            /* Set the Cost Amount of each transaction of type */
            updateTotalItemDayStmnt.setString(
                    UpdtTotalItemDayConstants.SALESCOSTAMNT, "0");
            updateTotalItemDayStmnt.setString(
                    UpdtTotalItemDayConstants.DCCOSTAMNT, "0");
            updateTotalItemDayStmnt.setString(
                    UpdtTotalItemDayConstants.RETURNCOSTAMNT, "0");
            updateTotalItemDayStmnt.setString(
                    UpdtTotalItemDayConstants.VOIDCOSTAMNT, "0");

            /* Set the primary code */
            updateTotalItemDayStmnt.setString(UpdtTotalItemDayConstants.CLASS,
                    strClass);
            updateTotalItemDayStmnt.setString(UpdtTotalItemDayConstants.CORPID,
                    txCorpId);
            updateTotalItemDayStmnt.setDouble(
                    UpdtTotalItemDayConstants.SALESPRICE,
                    sale.getActualsalesunitprice());
            updateTotalItemDayStmnt.setString(UpdtTotalItemDayConstants.DPT,
                    strDepartment);
            updateTotalItemDayStmnt.setString(UpdtTotalItemDayConstants.LINE,
                    strLine);
            updateTotalItemDayStmnt.setString(UpdtTotalItemDayConstants.PLU,
                    sale.getItemID().getPluCode());
            updateTotalItemDayStmnt.setString(
                    UpdtTotalItemDayConstants.STOREID,
                    transaction.getRetailStoreID());
            updateTotalItemDayStmnt.setString(
                    UpdtTotalItemDayConstants.SUMMARYDATEID,
                    transaction.getBusinessDayDate());

            result += updateTotalItemDayStmnt.executeUpdate();

            // Are there no row updated?
            if (result >= SQLResultsConstants.ONE_ROW_AFFECTED) {
                tp.println("TotalItemDay data was updated.");
                return result;
            }

            /* !IMPORTANT! */
            /*
             * If the code pass here it means that the row needs to be
             * inserted(Not Updated) in the database.
             */
            insertTotalItemDayStmnt.setString(InsrtTotalItemDayConstants.CLASS,
                    strClass);
            insertTotalItemDayStmnt.setString(
                    InsrtTotalItemDayConstants.CORPID, txCorpId);
            insertTotalItemDayStmnt.setString(
                    InsrtTotalItemDayConstants.DCCOSTAMNT, "0");
            insertTotalItemDayStmnt.setString(
                    InsrtTotalItemDayConstants.DCITEMCNT,
                    String.valueOf(ldcItemCount));
            insertTotalItemDayStmnt.setString(
                    InsrtTotalItemDayConstants.DCSALESAMNT,
                    String.valueOf(dDcSalesAmount));
            insertTotalItemDayStmnt.setDouble(
                    InsrtTotalItemDayConstants.SALESPRICE,
                    sale.getActualsalesunitprice());
            insertTotalItemDayStmnt.setString(InsrtTotalItemDayConstants.DPT,
                    strDepartment);
            insertTotalItemDayStmnt.setString(InsrtTotalItemDayConstants.LINE,
                    strLine);
            insertTotalItemDayStmnt.setString(InsrtTotalItemDayConstants.PLU,
                    sale.getItemID().getPluCode());
            insertTotalItemDayStmnt.setString(
                    InsrtTotalItemDayConstants.RETURNCOSTAMNT, "0");
            insertTotalItemDayStmnt.setString(
                    InsrtTotalItemDayConstants.RETURNITEMCNT, "0");
            insertTotalItemDayStmnt.setString(
                    InsrtTotalItemDayConstants.RETURNSALESAMNT,
                    String.valueOf(dReturnSalesAmount));
            insertTotalItemDayStmnt.setString(
                    InsrtTotalItemDayConstants.SALESCOSTAMNT, "0");
            insertTotalItemDayStmnt.setString(
                    InsrtTotalItemDayConstants.SALESITEMCNT,
                    String.valueOf(lSalesItemCount));
            insertTotalItemDayStmnt.setString(
                    InsrtTotalItemDayConstants.SALESSALESAMNT,
                    String.valueOf(dSalesSalesAmount));
            insertTotalItemDayStmnt.setString(
                    InsrtTotalItemDayConstants.STOREID,
                    transaction.getRetailStoreID());
            insertTotalItemDayStmnt.setString(
                    InsrtTotalItemDayConstants.SUMMARYDATEID,
                    transaction.getBusinessDayDate());
            insertTotalItemDayStmnt.setString(
                    InsrtTotalItemDayConstants.VOIDCOSTAMNT, "0");
            insertTotalItemDayStmnt.setString(
                    InsrtTotalItemDayConstants.VOIDITEMCNT, "0");
            insertTotalItemDayStmnt.setString(
                    InsrtTotalItemDayConstants.VOIDSALESAMNT,
                    String.valueOf(dVoidSalesAmount));

            result += insertTotalItemDayStmnt.executeUpdate();
        } catch (SQLException ex) {
            if (ex.getErrorCode() != ERRORCODE_DUPLICATE) {
                if (ex.getErrorCode() == ERRORCODE_DEADLOCK) {
                    tp.println("SQL deadlock exception.");
                    throw ex;
                }
                LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                        "Failed to update total item Day with SQL ErrorCode = "
                                + ex.getErrorCode() + " " + ex.getMessage());
                throw new DaoException("SQLException:@"
                        + "SQLServerConsolidation.doTotalItemDayUpdate", ex);
            }
            isDup = true;
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(progName, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to update total item Day " + ex.getMessage());
            throw new DaoException("SQLStatementException:@"
                    + "SQLServerConsolidation.doTotalItemDayUpdate", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to update total item Day " + ex.getMessage());
            throw new DaoException("Exception:@"
                    + "SQLServerConsolidation.doTotalItemDayUpdate", ex);
        } finally {
            // When the result of having Insert(ed) is duplication,
            // update is performed again.
            if (isDup && updateTotalItemDayStmnt != null) {
                result += updateTotalItemDay(updateTotalItemDayStmnt);
            }

            closeObject(updateTotalItemDayStmnt);
            closeObject(insertTotalItemDayStmnt);
            tp.methodExit(result);
        }

        return result;
    }

    private int updateTotalItemDay(PreparedStatement updateTotalItemDayStmnt)
            throws DaoException {
        int result = 0;
        try {
            result = updateTotalItemDayStmnt.executeUpdate();
        } catch (SQLException ex) {
            LOGGER.logAlert(progName,
                    "SQLServerConsolidationDAO.updateTotalItemDay",
                    Logger.RES_EXCEP_SQL,
                    "Failed to update total item Day.\n"
                    + ex.getMessage());
            tp.println("Failed to update total item Day.");
            throw new DaoException("SQLException:@"
                    + "SQLServerConsolidation.doTotalItemDayUpdate",
                        ex);
        } finally {
            closeObject(updateTotalItemDayStmnt);
        }

        return result;
    }

    /**
     * Private method for updating the TXU_TOTAL_FINANCIAL.
     *
     * @param txCorpId
     *            The corporate identifier string.
     * @param transaction
     *            The Current transaction
     * @param lineItem
     *            The line item to be recorded
     * @param connection
     *            The database connection
     * @param txtype
     *            The transaction type
     * @return The number of rows affected
     * @throws DaoException
     *             The exception thrown when the method fails
     * @throws SQLException
     *             The exception thrown when SQL error occurs.
     */
    private int doTotalFinancialUpdate(final String txCorpId,
            final Transaction transaction, final LineItem lineItem,
            final Connection connection, final String txtype)
            throws DaoException, SQLException {

        String functionName = className + "doTotalFinancialUpdate";

        tp.methodEnter("doTotalFinancialUpdate");

        int result = 0;
        boolean isDup = false;
        PreparedStatement updateTotalFinacialStmnt = null;

        try {
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateTotalFinacialStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("update-total-financial"));
            if (lineItem.getAccountCode() != null) {
                int transCode = Integer.valueOf(lineItem.getAccountCode());
                TransactionCode code = TransactionCode.get(transCode);

                switch (code) {
                case ITEMCODE:
                    Sale sale = lineItem.getSale();
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.CORPID, txCorpId);
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.ACCOUNTCODE,
                            lineItem.getAccountCode());
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.ACCOUNTSUBCODE, txtype);
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.SUMMARYDATEID,
                            transaction.getBusinessDayDate());
                    if (TxTypes.RETURNER.equals(txtype)) {
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.STOREID, transaction
                                        .getRetailTransaction().getTransactionLink()
                                        .getRetailStoreID());
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.TERMINALID,
                                transaction.getRetailTransaction().getTransactionLink()
                                        .getWorkStationID().getValue());
                    } else {
                        if (TxTypes.VOIDER.equals(txtype)) {
                            updateTotalFinacialStmnt.setString(
                                    UpdTotalFinancialConstants.SUMMARYDATEID,
                                    transaction.getRetailTransaction()
                                            .getVoidBusinessDayDate());
                        }
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.STOREID,
                                transaction.getRetailStoreID());
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.TERMINALID,
                                transaction.getWorkStationID().getValue());
                    }
                    if (TxTypes.NORMAL.equals(txtype)) {
                        updateTotalFinacialStmnt.setLong(
                                UpdTotalFinancialConstants.DEBTOR, 0);
                        updateTotalFinacialStmnt.setDouble(
                                UpdTotalFinancialConstants.CREDITOR,
                                sale.getExtendedAmt());
                    } else if (TxTypes.VOIDER.equals(txtype)
                            || TxTypes.RETURNER.equals(txtype)) {
                        updateTotalFinacialStmnt.setDouble(
                                UpdTotalFinancialConstants.DEBTOR,
                                sale.getExtendedAmt());
                        updateTotalFinacialStmnt.setLong(
                                UpdTotalFinancialConstants.CREDITOR, 0);
                    }
                    result += updateTotalFinacialStmnt.executeUpdate();
                    if (0 != sale.getDiscountAmount()) {
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.ACCOUNTCODE,
                                TransactionVariable.ITEM_DISCOUNT_CODE);
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.ACCOUNTSUBCODE,
                                TxTypes.NOTAVAILABLE);
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.SUMMARYDATEID,
                                transaction.getBusinessDayDate());

                        if (TxTypes.NORMAL.equals(txtype)) {
                            updateTotalFinacialStmnt.setDouble(
                                    UpdTotalFinancialConstants.DEBTOR,
                                    sale.getDiscountAmount());
                            updateTotalFinacialStmnt.setLong(
                                    UpdTotalFinancialConstants.CREDITOR, 0);
                        } else if (TxTypes.VOIDER.equals(txtype)
                                || TxTypes.RETURNER.equals(txtype)) {
                            updateTotalFinacialStmnt.setLong(
                                    UpdTotalFinancialConstants.DEBTOR, 0);
                            updateTotalFinacialStmnt.setDouble(
                                    UpdTotalFinancialConstants.CREDITOR,
                                    sale.getDiscountAmount());
                        }
                        result += updateTotalFinacialStmnt.executeUpdate();
                    }
                    break;
                case CASHCODE:
                    try {
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.CORPID, txCorpId);
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.ACCOUNTCODE,
                                lineItem.getAccountCode());
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.ACCOUNTSUBCODE,
                                TxTypes.NOTAVAILABLE);
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.SUMMARYDATEID,
                                transaction.getBusinessDayDate());

                        if (TxTypes.RETURNER.equals(txtype)) {
                            updateTotalFinacialStmnt.setString(
                                    UpdTotalFinancialConstants.STOREID,
                                    transaction.getRetailTransaction().getTransactionLink()
                                            .getRetailStoreID());
                            updateTotalFinacialStmnt.setString(
                                    UpdTotalFinancialConstants.TERMINALID,
                                    transaction.getRetailTransaction().getTransactionLink()
                                            .getWorkStationID().getValue());
                        } else {
                            if (TxTypes.VOIDER.equals(txtype)) {
                                updateTotalFinacialStmnt
                                    .setString(
                                            UpdTotalFinancialConstants
                                            .SUMMARYDATEID,
                                            transaction
                                            .getRetailTransaction()
                                             .getVoidBusinessDayDate());
                            }
                            updateTotalFinacialStmnt.setString(
                                    UpdTotalFinancialConstants.STOREID,
                                    transaction.getRetailStoreID());
                            updateTotalFinacialStmnt.setString(
                                    UpdTotalFinancialConstants.TERMINALID,
                                    transaction.getWorkStationID().getValue());
                        }

                        if (TxTypes.NORMAL.equals(txtype)) {
                            updateTotalFinacialStmnt.setLong(
                                    UpdTotalFinancialConstants.DEBTOR, Long
                                            .valueOf(lineItem.getTender()
                                                    .getAmount()));
                            updateTotalFinacialStmnt.setLong(
                                    UpdTotalFinancialConstants.CREDITOR, 0);
                        } else if (TxTypes.VOIDER.equals(txtype)
                                || TxTypes.RETURNER.equals(txtype)) {
                            updateTotalFinacialStmnt.setLong(
                                    UpdTotalFinancialConstants.DEBTOR, 0);
                            updateTotalFinacialStmnt.setLong(
                                    UpdTotalFinancialConstants.CREDITOR, Long
                                            .valueOf(lineItem.getTender()
                                                    .getAmount()));
                        }

                        result += updateTotalFinacialStmnt.executeUpdate();

                    } catch (SQLException e) {
                        if (e.getErrorCode() != ERRORCODE_DUPLICATE) {
                            throw e;
                        }
                        isDup = true;
                    }
                    if (isDup) {
                        try {
                            result += updateTotalFinacialStmnt.executeUpdate();
                        } catch (SQLException e) {
                            throw e;
                        } finally {
                            isDup = false;
                        }
                    }
                    if (lineItem.getTender().getTenderChange().getAmount()
                            != 0) {
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.CORPID, txCorpId);
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.ACCOUNTCODE,
                                lineItem.getAccountCode());
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.ACCOUNTSUBCODE,
                                TxTypes.NOTAVAILABLE);
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.SUMMARYDATEID,
                                transaction.getBusinessDayDate());

                        if (TxTypes.RETURNER.equals(txtype)) {
                            updateTotalFinacialStmnt.setString(
                                    UpdTotalFinancialConstants.STOREID,
                                    transaction.getRetailTransaction().getTransactionLink()
                                            .getRetailStoreID());
                            updateTotalFinacialStmnt.setString(
                                    UpdTotalFinancialConstants.TERMINALID,
                                    transaction.getRetailTransaction().getTransactionLink()
                                            .getWorkStationID().getValue());
                        } else {
                            if (TxTypes.VOIDER.equals(txtype)) {
                                updateTotalFinacialStmnt.setString(
                                        UpdTotalFinancialConstants
                                            .SUMMARYDATEID,
                                        transaction
                                            .getRetailTransaction()
                                            .getVoidBusinessDayDate());
                            }
                            updateTotalFinacialStmnt.setString(
                                    UpdTotalFinancialConstants.STOREID,
                                    transaction.getRetailStoreID());
                            updateTotalFinacialStmnt.setString(
                                    UpdTotalFinancialConstants.TERMINALID,
                                    transaction.getWorkStationID().getValue());
                        }

                        if (TxTypes.NORMAL.equals(txtype)) {
                            updateTotalFinacialStmnt.setLong(
                                    UpdTotalFinancialConstants.DEBTOR, 0);
                            updateTotalFinacialStmnt.setDouble(
                                    UpdTotalFinancialConstants.CREDITOR,
                                    Double.valueOf(lineItem.getTender()
                                            .getTenderChange().getAmount()));
                        } else if (TxTypes.VOIDER.equals(txtype)
                                || TxTypes.RETURNER.equals(txtype)) {
                            updateTotalFinacialStmnt.setDouble(
                                    UpdTotalFinancialConstants.DEBTOR,
                                    Double.valueOf(lineItem.getTender()
                                            .getTenderChange().getAmount()));
                            updateTotalFinacialStmnt.setLong(
                                    UpdTotalFinancialConstants.CREDITOR, 0);
                        }
                        result += updateTotalFinacialStmnt.executeUpdate();
                    }
                    break;
                case CREDITDEBITCODE:
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.CORPID, txCorpId);
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.ACCOUNTCODE,
                            lineItem.getAccountCode());
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.ACCOUNTSUBCODE,
                            TxTypes.NOTAVAILABLE);
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.SUMMARYDATEID,
                            transaction.getBusinessDayDate());
                    if (TxTypes.RETURNER.equals(txtype)) {
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.STOREID, transaction
                                        .getRetailTransaction().getTransactionLink()
                                        .getRetailStoreID());
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.TERMINALID,
                                transaction.getRetailTransaction().getTransactionLink()
                                        .getWorkStationID().getValue());
                    } else {
                        if (TxTypes.VOIDER.equals(txtype)) {
                            updateTotalFinacialStmnt.setString(
                                    UpdTotalFinancialConstants.SUMMARYDATEID,
                                    transaction.getRetailTransaction()
                                            .getVoidBusinessDayDate());
                        }
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.STOREID,
                                transaction.getRetailStoreID());
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.TERMINALID,
                                transaction.getWorkStationID().getValue());
                    }
                    if (TxTypes.NORMAL.equals(txtype)) {
                        updateTotalFinacialStmnt.setLong(
                                UpdTotalFinancialConstants.DEBTOR,
                                Long.valueOf(lineItem.getTender().getAmount()));
                        updateTotalFinacialStmnt.setLong(
                                UpdTotalFinancialConstants.CREDITOR, 0);
                    } else if (TxTypes.VOIDER.equals(txtype)
                            || TxTypes.RETURNER.equals(txtype)) {
                        updateTotalFinacialStmnt.setLong(
                                UpdTotalFinancialConstants.DEBTOR, 0);
                        updateTotalFinacialStmnt.setLong(
                                UpdTotalFinancialConstants.CREDITOR,
                                Long.valueOf(lineItem.getTender().getAmount()));
                    }
                    result += updateTotalFinacialStmnt.executeUpdate();
                    break;
                case VOUCHERCODE:
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.CORPID, txCorpId);
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.ACCOUNTCODE,
                            lineItem.getAccountCode());
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.ACCOUNTSUBCODE,
                            TxTypes.NOTAVAILABLE);
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.SUMMARYDATEID,
                            transaction.getBusinessDayDate());
                    if (TxTypes.RETURNER.equals(txtype)) {
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.STOREID, transaction
                                        .getRetailTransaction().getTransactionLink()
                                        .getRetailStoreID());
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.TERMINALID,
                                transaction.getRetailTransaction().getTransactionLink()
                                        .getWorkStationID().getValue());
                    } else {
                        if (TxTypes.VOIDER.equals(txtype)) {
                            updateTotalFinacialStmnt.setString(
                                    UpdTotalFinancialConstants.SUMMARYDATEID,
                                    transaction.getRetailTransaction()
                                            .getVoidBusinessDayDate());
                        }
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.STOREID,
                                transaction.getRetailStoreID());
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.TERMINALID,
                                transaction.getWorkStationID().getValue());
                    }
                    if (TxTypes.NORMAL.equals(txtype)) {
                        updateTotalFinacialStmnt.setLong(
                                UpdTotalFinancialConstants.DEBTOR,
                                Long.valueOf(lineItem.getTender().getAmount()));
                        updateTotalFinacialStmnt.setLong(
                                UpdTotalFinancialConstants.CREDITOR, 0);
                    } else if (TxTypes.VOIDER.equals(txtype)
                            || TxTypes.RETURNER.equals(txtype)) {
                        updateTotalFinacialStmnt.setLong(
                                UpdTotalFinancialConstants.DEBTOR, 0);
                        updateTotalFinacialStmnt.setLong(
                                UpdTotalFinancialConstants.CREDITOR,
                                Long.valueOf(lineItem.getTender().getAmount()));
                    }
                    result += updateTotalFinacialStmnt.executeUpdate();
                    break;
                case DISCOUNTCODE:
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.CORPID, txCorpId);
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.ACCOUNTCODE,
                            lineItem.getAccountCode());
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.ACCOUNTSUBCODE,
                            TxTypes.NOTAVAILABLE);
                    updateTotalFinacialStmnt.setString(
                            UpdTotalFinancialConstants.SUMMARYDATEID,
                            transaction.getBusinessDayDate());
                    if (TxTypes.RETURNER.equals(txtype)) {
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.STOREID, transaction
                                        .getRetailTransaction().getTransactionLink()
                                         .getRetailStoreID());
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.TERMINALID,
                                transaction.getRetailTransaction().getTransactionLink()
                                        .getWorkStationID().getValue());
                    } else {
                        if (TxTypes.VOIDER.equals(txtype)) {
                            updateTotalFinacialStmnt.setString(
                                    UpdTotalFinancialConstants.SUMMARYDATEID,
                                    transaction.getRetailTransaction()
                                            .getVoidBusinessDayDate());
                        }
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.STOREID,
                                transaction.getRetailStoreID());
                        updateTotalFinacialStmnt.setString(
                                UpdTotalFinancialConstants.TERMINALID,
                                transaction.getWorkStationID().getValue());
                    }
                    if (TxTypes.NORMAL.equals(txtype)) {
                        updateTotalFinacialStmnt.setLong(
                                UpdTotalFinancialConstants.DEBTOR, Long
                                        .valueOf(lineItem.getDiscount()
                                                .getAmount()));
                        updateTotalFinacialStmnt.setLong(
                                UpdTotalFinancialConstants.CREDITOR, 0);
                    } else if (TxTypes.VOIDER.equals(txtype)
                            || TxTypes.RETURNER.equals(txtype)) {
                        updateTotalFinacialStmnt.setLong(
                                UpdTotalFinancialConstants.DEBTOR, 0);
                        updateTotalFinacialStmnt.setLong(
                                UpdTotalFinancialConstants.CREDITOR, Long
                                        .valueOf(lineItem.getDiscount()
                                                .getAmount()));
                    }
                    result += updateTotalFinacialStmnt.executeUpdate();
                    break;
                default:
                    break;
                }
            } else {
                tp.println("AccountCode is unknown.");
            }
        } catch (SQLException e) {
            // Duplication
            if (e.getErrorCode() != ERRORCODE_DUPLICATE) {
                if (e.getErrorCode() == ERRORCODE_DEADLOCK) {
                    tp.println("SQL deadlock exception.");
                    throw e;
                }
                LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                        "Failed to update Total Financial with SQL ErrorCode = "
                                + e.getErrorCode() + " \n " + e.getMessage());
                throw new DaoException("SQLException:@"
                        + "SQLServerConsolidationDAO.doTotalFinancialUpdate",
                            e);
            }
            isDup = true;
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(progName, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to update Total Financial\n " + ex.getMessage());
            throw new DaoException("SQLStatementException:@"
                    + "SQLServerConsolidationDAO.doTotalFinancialUpdate", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to update Total Financial\n " + ex.getMessage());
            throw new DaoException("Exception:@"
                    + "SQlServerConsolidationDAO.selectPOSLogXML", ex);
        } finally {
            if (isDup) {
                result += updateTotalFinancial(updateTotalFinacialStmnt);
                isDup = false;
            }
            closeObject(updateTotalFinacialStmnt);
            tp.methodExit(result);
        }
        return result;
    }

    private int updateTotalFinancial(PreparedStatement
            updateTotalFinacialStmnt) throws DaoException {
        int result = 0;
        try {
            result = updateTotalFinacialStmnt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.logAlert(progName,
                    "SQLServerConsolidationDAO.updateTotalFinancial",
                    Logger.RES_EXCEP_SQL, "Failed to update Total Financial.\n "
                    + e.getMessage());
            tp.methodExit("Failed to update Total Financial.");
            throw new DaoException("SQLException:@"
               + "SQLServerConsolidationDAO.doTotalFinancialUpdate", e);
        }

        return result;
    }

    /**
     * Private method for updating the TXU_TOTAL_DRAWER_FINANCIAL.
     *
     * @param txCorpId
     *            The corporate identifier string.
     * @param transaction
     *            The Current transaction
     * @param lineItem
     *            The line item to be recorded
     * @param connection
     *            The database connection
     * @param txtype
     *            The transaction type
     * @return The number of rows affected
     * @throws DaoException
     *             The exception thrown when the method fails
     * @throws SQLException
     *             The exception thrown when SQL error occurs.
     */
    private int doTotalDrawerFinancialUpdate(final String txCorpId,
            final Transaction transaction, final LineItem lineItem,
            final Connection connection, final String txtype)
            throws DaoException, SQLException {

        String functionName = className + "doTotalDrawerFinancialUpdate";

        tp.methodEnter("doTotalDrawerFinancialUpdate");

        int result = 0;
        boolean isDup = false;
        PreparedStatement updtotDrwrFinStmnt = null;

        try {
            int tillID = Integer.valueOf(transaction.getTillID());
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updtotDrwrFinStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("update-total-drawer-financial"));
            if (lineItem.getAccountCode() != null) {
                int transCode = Integer.valueOf(lineItem.getAccountCode());
                TransactionCode code = TransactionCode.get(transCode);

                switch (code) {
                case ITEMCODE:
                    Sale sale = lineItem.getSale();
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.CORPID, txCorpId);
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.ACCOUNTCODE,
                            lineItem.getAccountCode());
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.ACCOUNTSUBCODE, txtype);
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.SUMMARYDATEID,
                            transaction.getBusinessDayDate());
                    if (TxTypes.RETURNER.equals(txtype)) {
                        TransactionLink translink = transaction
                                .getRetailTransaction().getTransactionLink();
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.STOREID, translink
                                        .getRetailStoreID());
                        updtotDrwrFinStmnt.setInt(
                                UpdTotDrwrFinConstants.PRINTERID,
                                Integer.valueOf( transaction.getTillID()));
                    } else {
                        if (TxTypes.VOIDER.equals(txtype)) {
                            updtotDrwrFinStmnt.setString(
                                    UpdTotDrwrFinConstants.SUMMARYDATEID,
                                    transaction.getRetailTransaction()
                                            .getVoidBusinessDayDate());
                        }
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.STOREID,
                                transaction.getRetailStoreID());
                        updtotDrwrFinStmnt.setInt(
                                UpdTotDrwrFinConstants.PRINTERID,
                                tillID);
                    }
                    if (TxTypes.NORMAL.equals(txtype)) {
                        updtotDrwrFinStmnt.setLong(
                                UpdTotDrwrFinConstants.DEBTOR, 0);
                        updtotDrwrFinStmnt.setDouble(
                                UpdTotDrwrFinConstants.CREDITOR,
                                sale.getExtendedAmt());
                    } else if (TxTypes.VOIDER.equals(txtype)
                            || txtype == TxTypes.RETURNER) {
                        updtotDrwrFinStmnt.setDouble(
                                UpdTotDrwrFinConstants.DEBTOR,
                                sale.getExtendedAmt());
                        updtotDrwrFinStmnt.setLong(
                                UpdTotDrwrFinConstants.CREDITOR, 0);
                    }
                    result += updtotDrwrFinStmnt.executeUpdate();
                    if (0 != sale.getDiscountAmount()) {
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.ACCOUNTCODE,
                                TransactionVariable.ITEM_DISCOUNT_CODE);
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.ACCOUNTSUBCODE,
                                TxTypes.NOTAVAILABLE);
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.SUMMARYDATEID,
                                transaction.getBusinessDayDate());

                        if (TxTypes.NORMAL.equals(txtype)) {
                            updtotDrwrFinStmnt.setDouble(
                                    UpdTotDrwrFinConstants.DEBTOR,
                                    sale.getDiscountAmount());
                            updtotDrwrFinStmnt.setLong(
                                    UpdTotDrwrFinConstants.CREDITOR, 0);
                        } else if (TxTypes.VOIDER.equals(txtype)
                                || TxTypes.RETURNER.equals(txtype)) {
                            updtotDrwrFinStmnt.setLong(
                                    UpdTotDrwrFinConstants.DEBTOR, 0);
                            updtotDrwrFinStmnt.setDouble(
                                    UpdTotDrwrFinConstants.CREDITOR,
                                    sale.getDiscountAmount());
                        }
                        result += updtotDrwrFinStmnt.executeUpdate();
                    }
                    break;
                case CASHCODE:
                    try {
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.CORPID, txCorpId);
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.ACCOUNTCODE,
                                lineItem.getAccountCode());
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.ACCOUNTSUBCODE,
                                TxTypes.NOTAVAILABLE);
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.SUMMARYDATEID,
                                transaction.getBusinessDayDate());

                        if (TxTypes.RETURNER.equals(txtype)) {
                            TransactionLink transLink =
                                transaction.getRetailTransaction().getTransactionLink();
                            updtotDrwrFinStmnt.setString(
                                    UpdTotDrwrFinConstants.STOREID,
                                    transLink.getRetailStoreID());
                            updtotDrwrFinStmnt.setInt(
                                    UpdTotDrwrFinConstants.PRINTERID,
                                    Integer.valueOf(transaction.getTillID()));
                        } else {
                            if (TxTypes.VOIDER.equals(txtype)) {
                                updtotDrwrFinStmnt
                                    .setString(
                                            UpdTotDrwrFinConstants
                                            .SUMMARYDATEID,
                                            transaction
                                            .getRetailTransaction()
                                             .getVoidBusinessDayDate());
                            }
                            updtotDrwrFinStmnt.setString(
                                    UpdTotDrwrFinConstants.STOREID,
                                    transaction.getRetailStoreID());
                            updtotDrwrFinStmnt.setInt(
                                    UpdTotDrwrFinConstants.PRINTERID,
                                    tillID);
                        }

                        if (TxTypes.NORMAL.equals(txtype)) {
                            updtotDrwrFinStmnt.setLong(
                                    UpdTotDrwrFinConstants.DEBTOR, Long
                                            .valueOf(lineItem.getTender()
                                                    .getAmount()));
                            updtotDrwrFinStmnt.setLong(
                                    UpdTotDrwrFinConstants.CREDITOR, 0);
                        } else if (TxTypes.VOIDER.equals(txtype)
                                || TxTypes.RETURNER.equals(txtype)) {
                            updtotDrwrFinStmnt.setLong(
                                    UpdTotDrwrFinConstants.DEBTOR, 0);
                            updtotDrwrFinStmnt.setLong(
                                    UpdTotDrwrFinConstants.CREDITOR, Long
                                            .valueOf(lineItem.getTender()
                                                    .getAmount()));
                        }

                        result += updtotDrwrFinStmnt.executeUpdate();

                    } catch (SQLException e) {
                        if (e.getErrorCode() != ERRORCODE_DUPLICATE) {
                            throw e;
                        }
                        isDup = true;
                    }
                    if (isDup) {
                        try {
                            result += updtotDrwrFinStmnt.executeUpdate();
                        } catch (SQLException e) {
                            throw e;
                        } finally {
                            isDup = false;
                        }
                    }
                    if (lineItem.getTender().getTenderChange().getAmount()
                            != 0) {
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.CORPID, txCorpId);
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.ACCOUNTCODE,
                                lineItem.getAccountCode());
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.ACCOUNTSUBCODE,
                                TxTypes.NOTAVAILABLE);
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.SUMMARYDATEID,
                                transaction.getBusinessDayDate());

                        if (TxTypes.RETURNER.equals(txtype)) {
                            TransactionLink translink =
                                transaction.getRetailTransaction().getTransactionLink();
                            updtotDrwrFinStmnt.setString(
                                    UpdTotDrwrFinConstants.STOREID,
                                            translink.getRetailStoreID());
                            updtotDrwrFinStmnt.setInt(
                                    UpdTotDrwrFinConstants.PRINTERID,
                                    Integer.valueOf(transaction.getTillID()));
                        } else {
                            if (TxTypes.VOIDER.equals(txtype)) {
                                updtotDrwrFinStmnt.setString(
                                        UpdTotDrwrFinConstants
                                            .SUMMARYDATEID,
                                        transaction
                                            .getRetailTransaction()
                                            .getVoidBusinessDayDate());
                            }
                            updtotDrwrFinStmnt.setString(
                                    UpdTotDrwrFinConstants.STOREID,
                                    transaction.getRetailStoreID());
                            updtotDrwrFinStmnt.setInt(
                                    UpdTotDrwrFinConstants.PRINTERID,
                                    tillID);
                        }

                        if (TxTypes.NORMAL.equals(txtype)) {
                            updtotDrwrFinStmnt.setLong(
                                    UpdTotDrwrFinConstants.DEBTOR, 0);
                            updtotDrwrFinStmnt.setDouble(
                                    UpdTotDrwrFinConstants.CREDITOR,
                                    Double.valueOf(lineItem.getTender()
                                            .getTenderChange().getAmount()));
                        } else if (TxTypes.VOIDER.equals(txtype)
                                || TxTypes.RETURNER.equals(txtype)) {
                            updtotDrwrFinStmnt.setDouble(
                                    UpdTotDrwrFinConstants.DEBTOR,
                                    Double.valueOf(lineItem.getTender()
                                            .getTenderChange().getAmount()));
                            updtotDrwrFinStmnt.setLong(
                                    UpdTotDrwrFinConstants.CREDITOR, 0);
                        }
                        result += updtotDrwrFinStmnt.executeUpdate();
                    }
                    break;
                case CREDITDEBITCODE:
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.CORPID, txCorpId);
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.ACCOUNTCODE,
                            lineItem.getAccountCode());
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.ACCOUNTSUBCODE,
                            TxTypes.NOTAVAILABLE);
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.SUMMARYDATEID,
                            transaction.getBusinessDayDate());
                    if (TxTypes.RETURNER.equals(txtype)) {
                        TransactionLink transLink =
                            transaction.getRetailTransaction().getTransactionLink();
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.STOREID, transLink
                                        .getRetailStoreID());
                        updtotDrwrFinStmnt.setInt(
                                UpdTotDrwrFinConstants.PRINTERID,
                                    Integer.valueOf(transaction.getTillID()));
                    } else {
                        if (TxTypes.VOIDER.equals(txtype)) {
                            updtotDrwrFinStmnt.setString(
                                    UpdTotDrwrFinConstants.SUMMARYDATEID,
                                    transaction.getRetailTransaction()
                                            .getVoidBusinessDayDate());
                        }
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.STOREID,
                                transaction.getRetailStoreID());
                        updtotDrwrFinStmnt.setInt(
                                UpdTotDrwrFinConstants.PRINTERID,
                                tillID);
                    }
                    if (TxTypes.NORMAL.equals(txtype)) {
                        updtotDrwrFinStmnt.setLong(
                                UpdTotDrwrFinConstants.DEBTOR,
                                Long.valueOf(lineItem.getTender().getAmount()));
                        updtotDrwrFinStmnt.setLong(
                                UpdTotDrwrFinConstants.CREDITOR, 0);
                    } else if (TxTypes.VOIDER.equals(txtype)
                            || TxTypes.RETURNER.equals(txtype)) {
                        updtotDrwrFinStmnt.setLong(
                                UpdTotDrwrFinConstants.DEBTOR, 0);
                        updtotDrwrFinStmnt.setLong(
                                UpdTotDrwrFinConstants.CREDITOR,
                                Long.valueOf(lineItem.getTender().getAmount()));
                    }
                    result += updtotDrwrFinStmnt.executeUpdate();
                    break;
                case VOUCHERCODE:
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.CORPID, txCorpId);
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.ACCOUNTCODE,
                            lineItem.getAccountCode());
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.ACCOUNTSUBCODE,
                            TxTypes.NOTAVAILABLE);
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.SUMMARYDATEID,
                            transaction.getBusinessDayDate());
                    if (TxTypes.RETURNER.equals(txtype)) {
                        TransactionLink translink = transaction
                            .getRetailTransaction().getTransactionLink();
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.STOREID, translink
                                        .getRetailStoreID());
                        updtotDrwrFinStmnt.setInt(
                                UpdTotDrwrFinConstants.PRINTERID,
                                Integer.valueOf(transaction.getTillID()));
                    } else {
                        if (TxTypes.VOIDER.equals(txtype)) {
                            updtotDrwrFinStmnt.setString(
                                    UpdTotDrwrFinConstants.SUMMARYDATEID,
                                    transaction.getRetailTransaction()
                                            .getVoidBusinessDayDate());
                        }
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.STOREID,
                                transaction.getRetailStoreID());
                        updtotDrwrFinStmnt.setInt(
                                UpdTotDrwrFinConstants.PRINTERID,
                                tillID);
                    }
                    if (TxTypes.NORMAL.equals(txtype)) {
                        updtotDrwrFinStmnt.setLong(
                                UpdTotDrwrFinConstants.DEBTOR,
                                Long.valueOf(lineItem.getTender().getAmount()));
                        updtotDrwrFinStmnt.setLong(
                                UpdTotDrwrFinConstants.CREDITOR, 0);
                    } else if (TxTypes.VOIDER.equals(txtype)
                            || TxTypes.RETURNER.equals(txtype)) {
                        updtotDrwrFinStmnt.setLong(
                                UpdTotDrwrFinConstants.DEBTOR, 0);
                        updtotDrwrFinStmnt.setLong(
                                UpdTotDrwrFinConstants.CREDITOR,
                                Long.valueOf(lineItem.getTender().getAmount()));
                    }
                    result += updtotDrwrFinStmnt.executeUpdate();
                    break;
                case DISCOUNTCODE:
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.CORPID, txCorpId);
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.ACCOUNTCODE,
                            lineItem.getAccountCode());
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.ACCOUNTSUBCODE,
                            TxTypes.NOTAVAILABLE);
                    updtotDrwrFinStmnt.setString(
                            UpdTotDrwrFinConstants.SUMMARYDATEID,
                            transaction.getBusinessDayDate());
                    if (TxTypes.RETURNER.equals(txtype)) {
                        TransactionLink translink = transaction
                            .getRetailTransaction().getTransactionLink();
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.STOREID, translink
                                        .getRetailStoreID());
                        updtotDrwrFinStmnt.setInt(
                                UpdTotDrwrFinConstants.PRINTERID,
                                Integer.valueOf(transaction.getTillID()));
                    } else {
                        if (TxTypes.VOIDER.equals(txtype)) {
                            updtotDrwrFinStmnt.setString(
                                    UpdTotDrwrFinConstants.SUMMARYDATEID,
                                    transaction.getRetailTransaction()
                                            .getVoidBusinessDayDate());
                        }
                        updtotDrwrFinStmnt.setString(
                                UpdTotDrwrFinConstants.STOREID,
                                transaction.getRetailStoreID());
                        updtotDrwrFinStmnt.setInt(
                                UpdTotDrwrFinConstants.PRINTERID,
                                tillID);
                    }
                    if (TxTypes.NORMAL.equals(txtype)) {
                        updtotDrwrFinStmnt.setLong(
                                UpdTotDrwrFinConstants.DEBTOR, Long
                                        .valueOf(lineItem.getDiscount()
                                                .getAmount()));
                        updtotDrwrFinStmnt.setLong(
                                UpdTotDrwrFinConstants.CREDITOR, 0);
                    } else if (TxTypes.VOIDER.equals(txtype)
                            || TxTypes.RETURNER.equals(txtype)) {
                        updtotDrwrFinStmnt.setLong(
                                UpdTotDrwrFinConstants.DEBTOR, 0);
                        updtotDrwrFinStmnt.setLong(
                                UpdTotDrwrFinConstants.CREDITOR, Long
                                        .valueOf(lineItem.getDiscount()
                                                .getAmount()));
                    }
                    result += updtotDrwrFinStmnt.executeUpdate();
                    break;
                default:
                    break;
                }
            } else {
                tp.println("AccountCode is unknown.");
            }
        } catch (SQLException e) {
            // Duplication
            if (e.getErrorCode() != ERRORCODE_DUPLICATE) {
                if (e.getErrorCode() == ERRORCODE_DEADLOCK) {
                    tp.println("SQL deadlock exception.");
                    throw e;
                }
                LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                        "Failed to update Total Financial with SQL ErrorCode = "
                                + e.getErrorCode() + " \n " + e.getMessage());
                throw new DaoException("SQLException:@"
                     + "SQLServerConsolidationDAO.doTotalDrawerFinancialUpdate",
                            e);
            }
            isDup = true;
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(progName, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to update Total Financial\n " + ex.getMessage());
            throw new DaoException("SQLStatementException:@"
                + "SQLServerConsolidationDAO.doTotalDrawerFinancialUpdate", ex);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to update Total Financial\n " + ex.getMessage());
            throw new DaoException("Exception:@"
                    + "SQlServerConsolidationDAO.selectPOSLogXML", ex);
        } finally {
            if (isDup) {
                result += updateTotalDrawerFinancial(updtotDrwrFinStmnt);
                isDup = false;
            }
            closeConnectionObjects(null, updtotDrwrFinStmnt, null);
            tp.methodExit(result);
        }
        return result;
    }

    private int updateTotalDrawerFinancial(PreparedStatement
            updtotDrwrFinStmnt) throws DaoException {
        int result = 0;
        try {
            result = updtotDrwrFinStmnt.executeUpdate();
        } catch (SQLException e) {
            LOGGER.logAlert(progName,
                    "SQLServerConsolidationDAO.updateTotalDrawerFinancial",
                    Logger.RES_EXCEP_SQL,
                    "Failed to update Total Financial.\n"
                    + e.getMessage());
            tp.methodExit("Failed to update Total Financial.");
            throw new DaoException("SQLException:@SQLServerConsolidationDAO"
                    + ".updateTotalDrawerFinancial", e);
        }

        return result;
    }

    /**
     * A helper method that is used to sort LineItem needed to update.
     *
     * @param lineArray
     *            The List of LineItems to be sorted
     */
    private void sortLineItems(final List<LineItem> lineArray) {
        boolean compareResult = false;
        int lineArraySize = lineArray.size();
        for (int i = 0; i < lineArraySize; i++) {
            for (int j = 1; j < lineArraySize - i; j++) {
                compareResult = compareLineItems(lineArray.get(j - 1),
                        lineArray.get(j));
                // if the previous is greater than the current SWAP
                if (compareResult) {
                    LineItem temp1 = lineArray.get(j - 1);
                    LineItem temp2 = lineArray.get(j);

                    lineArray.set(j, temp1);
                    lineArray.set(j - 1, temp2);
                }
            }

        }
    }

    /**
     * A helper method that is used to compare LineItems.
     *
     * @param prev
     *            The previous LineItem
     * @param current
     *            The current LineItem
     * @return true, if previous is greater than current, else, false.
     */
    private boolean compareLineItems(final LineItem prev,
            final LineItem current) {
        // Escape LineItem with no Sale
        if (null == current.getSale() || null == prev.getSale()) {
            return false;
        }

        // if previous is greater than current
        if (0 > current.getSale().getDepartment()
                .compareTo(prev.getSale().getDepartment())) {
            return true;
        }

        // if previous is greater than current
        if (0 > current.getSale().getLine()
                .compareTo(prev.getSale().getLine())) {
            return true;
        }

        // if previous is greater than current
        if (0 > current.getSale().getClas()
                .compareTo(prev.getSale().getClas())) {
            return true;
        }

        // if previous is greater than current
        if (0 > current.getSale().getItemID().getPluCode()
                .compareTo(prev.getSale().getItemID().getPluCode())) {
            return true;
        }

        // OPERATORID is also the same throughout
        // the transaction so it is skipped also
        return false;
    }
}
