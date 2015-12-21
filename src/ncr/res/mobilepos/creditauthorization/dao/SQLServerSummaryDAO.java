package ncr.res.mobilepos.creditauthorization.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.pastelport.platform.CommonTx;
import atg.taglib.json.util.JSONException;

/**
 * SummaryDAO implementation class that access TXU_NPS_PastelPortCreditSummary.
 */
public class SQLServerSummaryDAO extends AbstractDao implements ISummaryDAO {
    /**
     * A private member variable that represents the Database Manager for the
     * class.
     */
    private DBManager dbManager;
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * Instance of the trace debug printer.
     */
    private Trace.Printer tp = null;
    /** The Constant PROGNAME. */
    private static final String PROGNAME = "AuthPP";

    /**
     * The Class Constructor.
     *
     * @throws DaoException
     *             thrown when database error occurs
     */
    public SQLServerSummaryDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Gets the Database Manager.
     *
     * @return database manager instance
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    /**
     * Gets the record with a key from CommonTx.
     *
     * @param tx
     *            the key is in the Object of CommonTx
     * @param dbtx
     *            selected record save in dbtx
     * @return if have the record return trueAelse return falseB
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    @Override
    public final boolean select(final CommonTx tx, final CommonTx dbtx)
            throws DaoException, JSONException {
        tp.methodEnter("select");
        boolean summarySelect = false;
        Connection connection = null;
        PreparedStatement summarySelectPrepStmnt = null;
        ResultSet rs = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
           String storeid = tx.getFieldValue("storeid");
            String terminalid = tx.getFieldValue("terminalid");

            summarySelectPrepStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-SummarySelect"));
            summarySelectPrepStmnt.setString(SQLStatement.PARAM1,
                    storeid);
            summarySelectPrepStmnt.setString(SQLStatement.PARAM2,
                    terminalid);

            tp.println("storeid", storeid);
            tp.println("terminalid", terminalid);

            rs = summarySelectPrepStmnt.executeQuery();
            if (rs.next()) {
                summarySelect = true;
                dbtx.setFieldValue("storeid", tx.getFieldValue("storeid"));
                dbtx.setFieldValue("terminalid",
                        tx.getFieldValue("terminalid"));
                dbtx.setFieldValue("salesamount",
                        rs.getLong(SQLStatement.PARAM1) + "");
                dbtx.setFieldValue("salescount",
                        rs.getLong(SQLStatement.PARAM2) + "");
                dbtx.setFieldValue("voidamount",
                        rs.getLong(SQLStatement.PARAM3) + "");
                dbtx.setFieldValue("voidcount",
                        rs.getLong(SQLStatement.PARAM4) + "");
                dbtx.setFieldValue("refundamount",
                        rs.getLong(SQLStatement.PARAM5) + "");
                dbtx.setFieldValue("refundcount",
                        rs.getLong(SQLStatement.PARAM6) + "");
                dbtx.setFieldValue("cancelamount",
                        rs.getLong(SQLStatement.PARAM7) + "");
                dbtx.setFieldValue("cancelcount",
                        rs.getLong(SQLStatement.PARAM8) + "");
                dbtx.setFieldValue("possalesamount",
                        rs.getLong(SQLStatement.PARAM9) + "");
                dbtx.setFieldValue("possalescount",
                        rs.getLong(SQLStatement.PARAM10) + "");
                dbtx.setFieldValue("posvoidamount",
                        rs.getLong(SQLStatement.PARAM11) + "");
                dbtx.setFieldValue("posvoidcount",
                        rs.getLong(SQLStatement.PARAM12) + "");
                dbtx.setFieldValue("posrefundamount",
                        rs.getLong(SQLStatement.PARAM13) + "");
                dbtx.setFieldValue("posrefundcount",
                        rs.getLong(SQLStatement.PARAM14) + "");
                dbtx.setFieldValue("poscancelamount",
                        rs.getLong(SQLStatement.PARAM15) + "");
                dbtx.setFieldValue("poscancelcount",
                        rs.getLong(SQLStatement.PARAM16) + "");
            } else {
                tp.println("Record not found.");
            }
        } catch (SQLException sqlStmtEx) {
            LOGGER.logAlert(SQLServerSummaryDAO.PROGNAME,
                    "SQLServerSummaryDAO.select().", Logger.RES_EXCEP_SQL,
                    "SQL Statement Error occured. \n" + sqlStmtEx.getMessage());
        } catch (SQLStatementException sqlEx) {
            LOGGER.logAlert(SQLServerSummaryDAO.PROGNAME,
                    "SQLServerSummaryDAO.select().",
                    Logger.RES_EXCEP_SQLSTATEMENT, "SQL Error occured. \n"
                            + sqlEx.getMessage());
        } catch (NumberFormatException ex) {
            LOGGER.logAlert(SQLServerSummaryDAO.PROGNAME,
                    "SQLServerSummaryDAO.select().", Logger.RES_EXCEP_PARSE,
                    "NumberFormat Error occured. \n" + ex.getMessage());
        } finally {
            closeConnectionObjects(connection, summarySelectPrepStmnt, rs);

            tp.methodExit(summarySelect);
        }
        return summarySelect;
    }

    /**
     * Inserts the record in CommonTx.
     *
     * @param tx
     *            object of CommonTx that including values will be used when add
     *            record
     * @return true when insert is successful and false when is failed
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    @Override
    public final boolean insert(final CommonTx tx) throws DaoException,
            JSONException {
        // Start log.
        tp.methodEnter("insert");
        boolean summaryInsert = false;
        Connection connection = null;
        PreparedStatement summaryInsertPrepStmnt = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            summaryInsertPrepStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("save-SummaryInsert"));

          
            String storeid = tx.getFieldValue("storeid");
            String terminalid = tx.getFieldValue("terminalid");

            summaryInsertPrepStmnt.setString(SQLStatement.PARAM1,
                    storeid);
            summaryInsertPrepStmnt.setString(SQLStatement.PARAM2,
                    terminalid);

            tp.println("storeid", storeid);
            tp.println("terminalid", terminalid);

            if (summaryInsertPrepStmnt.executeUpdate() > 0) {
                summaryInsert = true;
                connection.commit();
            } else {
                summaryInsert = false;
                connection.rollback();
                tp.println("Failed to insert new pastelport record.");
            }
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(SQLServerSummaryDAO.PROGNAME,
                    "SQLServerSummaryDAO.insert()",
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "SQL Statement Error occured. \n" + sqlStmtEx.getMessage());
            rollBack(connection, "@SQLServerSummaryDAO.insert", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(SQLServerSummaryDAO.PROGNAME,
                    "SQLServerSummaryDAO.insert()", Logger.RES_EXCEP_SQL,
                    "SQL Exception Error occured. \n" + sqlEx.getMessage());
            rollBack(connection, "@SQLServerSummaryDAO:insert", sqlEx);

        } catch (NumberFormatException ex) {
            LOGGER.logAlert(SQLServerSummaryDAO.PROGNAME,
                    "SQLServerSummaryDAO.insert().", Logger.RES_EXCEP_PARSE,
                    "NumberFormat Error occured. \n" + ex.getMessage());
            rollBack(connection, "@SQLServerSummaryDAO:insert", ex);
            throw ex;
        } finally {
            closeConnectionObjects(connection, summaryInsertPrepStmnt);

            tp.methodExit(summaryInsert);
        }
        return summaryInsert;
    }

    /**
     * Updates data when server is VOID/SUBTRACT/and so on.
     *
     * @param tx
     *            object of CommonTx
     * @return true when insert is successful and false when is failed
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    @Override
    public final boolean updateSales(final CommonTx tx) throws DaoException,
                JSONException {
        // Start log.
        tp.methodEnter("updateSales");
        boolean summaryUpdateSales = false;
        Connection connection = null;
        PreparedStatement summaryUpdateSalesPrepStmnt = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            summaryUpdateSalesPrepStmnt = connection
                    .prepareStatement(sqlStatement
                            .getProperty("set-SummaryUpdateSales"));
            summaryUpdateSalesPrepStmnt.setLong(SQLStatement.PARAM1,
                    Long.parseLong(tx.getFieldValue("salesamount")));
            summaryUpdateSalesPrepStmnt.setLong(SQLStatement.PARAM2,
                    Long.parseLong(tx.getFieldValue("salescount")));
            summaryUpdateSalesPrepStmnt.setLong(SQLStatement.PARAM3,
                    Long.parseLong(tx.getFieldValue("voidamount")));
            summaryUpdateSalesPrepStmnt.setLong(SQLStatement.PARAM4,
                    Long.parseLong(tx.getFieldValue("voidcount")));
            summaryUpdateSalesPrepStmnt.setLong(SQLStatement.PARAM5,
                    Long.parseLong(tx.getFieldValue("refundamount")));
            summaryUpdateSalesPrepStmnt.setLong(SQLStatement.PARAM6,
                    Long.parseLong(tx.getFieldValue("refundcount")));
            summaryUpdateSalesPrepStmnt.setLong(SQLStatement.PARAM7,
                    Long.parseLong(tx.getFieldValue("cancelamount")));
            summaryUpdateSalesPrepStmnt.setLong(SQLStatement.PARAM8,
                    Long.parseLong(tx.getFieldValue("cancelcount")));

           String storeid = tx.getFieldValue("storeid");
            String terminalid = tx.getFieldValue("terminalid");

           summaryUpdateSalesPrepStmnt.setString(SQLStatement.PARAM9,
                    storeid);
            summaryUpdateSalesPrepStmnt.setString(SQLStatement.PARAM10,
                    terminalid);

            tp.println("storeid", storeid);
            tp.println("terminalid", terminalid);

            if (summaryUpdateSalesPrepStmnt.executeUpdate() == 1) {
                summaryUpdateSales = true;
                connection.commit();
            } else {
                summaryUpdateSales = false;
                connection.rollback();
                tp.println("Failed to update pastelport sales.");
            }

        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(SQLServerSummaryDAO.PROGNAME,
                    "SQLServerSummaryDAO.updateSales()",
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "SQL Statement Error occured. \n" + sqlStmtEx.getMessage());
            rollBack(connection, "@SQLServerSummaryDAO.updateSales", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(SQLServerSummaryDAO.PROGNAME,
                    "SQLServerSummaryDAO.updateSales()", Logger.RES_EXCEP_SQL,
                    "SQL Exception Error occured. \n" + sqlEx.getMessage());
            rollBack(connection, "@SQLServerSummaryDAO:updateSales", sqlEx);

        } catch (NumberFormatException ex) {
            LOGGER.logAlert(SQLServerSummaryDAO.PROGNAME,
                    "SQLServerSummaryDAO.updateSales().",
                    Logger.RES_EXCEP_PARSE, "NumberFormat Error occured. \n"
                            + ex.getMessage());
            rollBack(connection, "@SQLServerSummaryDAO:updateSales", ex);
            throw ex;
        } finally {
            closeConnectionObjects(connection, summaryUpdateSalesPrepStmnt);

            tp.methodExit(summaryUpdateSales);
        }
        return summaryUpdateSales;
    }

    /**
     * Updates CommonTx init process.
     *
     * @param tx
     *            update object CommonTx
     * @return true when insert is successful and false when is failed
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    @Override
    public final boolean updateInit(final CommonTx tx) throws DaoException,
            JSONException {
        // Start log.
        tp.methodEnter("updateInit");
        boolean summaryUpdateInit = false;
        Connection connection = null;
        PreparedStatement summaryUpdateInitPrepStmnt = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            summaryUpdateInitPrepStmnt = connection
                    .prepareStatement(sqlStatement
                            .getProperty("set-SummaryUpdateInit"));

            summaryUpdateInitPrepStmnt.setString(SQLStatement.PARAM1,
                    DateFormatUtility
                        .localeTime(tx.getFieldValue("finishdate")));
            summaryUpdateInitPrepStmnt.setInt(SQLStatement.PARAM2,
                    Integer.parseInt(tx.getFieldValue("finishpaymentseq")));
            String storeid = tx.getFieldValue("storeid");
            String terminalid = tx.getFieldValue("terminalid");
            summaryUpdateInitPrepStmnt.setString(SQLStatement.PARAM3,
                    storeid);
            summaryUpdateInitPrepStmnt.setString(SQLStatement.PARAM4,
                    terminalid);
            tp.println("storeid", storeid);
            tp.println("terminalid", terminalid);

            if (summaryUpdateInitPrepStmnt.executeUpdate() == 1) {
                summaryUpdateInit = true;
                connection.commit();
            } else {
                summaryUpdateInit = false;
                connection.rollback();
                tp.println("Failed to update pastelport init.");
            }

        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(SQLServerSummaryDAO.PROGNAME,
                    "SQLServerSummaryDAO.updateInit()",
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "SQL Statement Error occured. \n" + sqlStmtEx.getMessage());
            rollBack(connection, "@SQLServerSummaryDAO.updateInit", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(SQLServerSummaryDAO.PROGNAME,
                    "SQLServerSummaryDAO.updateInit()", Logger.RES_EXCEP_SQL,
                    "SQL Exception Error occured. \n" + sqlEx.getMessage());
            rollBack(connection, "@SQLServerSummaryDAO:updateInit", sqlEx);

        } catch (NumberFormatException ex) {
            LOGGER.logAlert(SQLServerSummaryDAO.PROGNAME,
                    "SQLServerSummaryDAO.updateInit().",
                    Logger.RES_EXCEP_PARSE, "NumberFormat Error occured. \n"
                            + ex.getMessage());
            rollBack(connection, "@SQLServerSummaryDAO:updateInit", ex);
            throw ex;
        } finally {
            closeConnectionObjects(connection, summaryUpdateInitPrepStmnt);

            tp.methodExit(summaryUpdateInit);
        }
        return summaryUpdateInit;
    }

    /**
     * CommonTx conntect summary process.
     *
     * @param tx
     *            update record keep in CommonTx
     * @return true when insert is successful and false when is failed
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    @Override
    public final boolean updateFinish(final CommonTx tx) throws DaoException,
            JSONException {
        // Start log.
        tp.methodEnter("updateFinish");
        boolean summaryUpdateFinish = false;
        Connection connection = null;
        PreparedStatement summaryUpdateFinishPrepStmnt = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            summaryUpdateFinishPrepStmnt = connection
                    .prepareStatement(sqlStatement
                            .getProperty("set-SummaryUpdateFinish"));
            summaryUpdateFinishPrepStmnt.setString(SQLStatement.PARAM1,
                    DateFormatUtility
                        .localeTime(tx.getFieldValue("finishdate")));
            summaryUpdateFinishPrepStmnt.setInt(SQLStatement.PARAM2,
                    Integer.parseInt(tx.getFieldValue("finishpaymentseq")));
            summaryUpdateFinishPrepStmnt.setLong(SQLStatement.PARAM3,
                    Long.parseLong(tx.getFieldValue("totalsubtractamount")));
            summaryUpdateFinishPrepStmnt.setLong(SQLStatement.PARAM4,
                    Long.parseLong(tx.getFieldValue("totalsubtractcount")));
            summaryUpdateFinishPrepStmnt.setLong(SQLStatement.PARAM5,
                    Long.parseLong(tx.getFieldValue("totalvoidamount")));
            summaryUpdateFinishPrepStmnt.setLong(SQLStatement.PARAM6,
                    Long.parseLong(tx.getFieldValue("totalvoidcount")));
            summaryUpdateFinishPrepStmnt.setLong(SQLStatement.PARAM7,
                    Long.parseLong(tx.getFieldValue("totalrefundamount")));
            summaryUpdateFinishPrepStmnt.setLong(SQLStatement.PARAM8,
                    Long.parseLong(tx.getFieldValue("totalrefundcount")));
            summaryUpdateFinishPrepStmnt.setLong(SQLStatement.PARAM9,
                    Long.parseLong(tx.getFieldValue("totalauthcancelamount")));
            summaryUpdateFinishPrepStmnt.setLong(SQLStatement.PARAM10,
                    Long.parseLong(tx.getFieldValue("totalauthcancelcount")));
           String storeid = tx.getFieldValue("storeid");
            String terminalid = tx.getFieldValue("terminalid");
            summaryUpdateFinishPrepStmnt.setString(SQLStatement.PARAM11,
                    storeid);
            summaryUpdateFinishPrepStmnt.setString(SQLStatement.PARAM12,
                    terminalid);
            tp.println("storeid", storeid);
            tp.println("terminalid", terminalid);

            if (summaryUpdateFinishPrepStmnt.executeUpdate() == 1) {
                summaryUpdateFinish = true;
                connection.commit();
            } else {
                summaryUpdateFinish = false;
                connection.rollback();
                tp.println("Failed to update pastelport finish.");
            }

        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(SQLServerSummaryDAO.PROGNAME,
                    "SQLServerSummaryDAO.updateFinish()",
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "SQL Statement Error occured. \n" + sqlStmtEx.getMessage());
            rollBack(connection, "@SQLServerSummaryDAO.updateFinish",
                    sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(SQLServerSummaryDAO.PROGNAME,
                    "SQLServerSummaryDAO.updateFinish()", Logger.RES_EXCEP_SQL,
                    "SQL Exception Error occured. \n" + sqlEx.getMessage());
            rollBack(connection, "@SQLServerSummaryDAO:updateFinish", sqlEx);

        } catch (NumberFormatException ex) {
            LOGGER.logAlert(SQLServerSummaryDAO.PROGNAME,
                    "SQLServerSummaryDAO.updateFinish().",
                    Logger.RES_EXCEP_PARSE, "NumberFormat Error occured. \n"
                            + ex.getMessage());
            rollBack(connection, "@SQLServerSummaryDAO:updateFinish", ex);
            throw ex;
        } finally {
            closeConnectionObjects(connection, summaryUpdateFinishPrepStmnt);

            tp.methodExit(summaryUpdateFinish);
        }
        return summaryUpdateFinish;
    }
}
