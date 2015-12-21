package ncr.res.mobilepos.creditauthorization.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.creditauthorization.model.PastelPortResp;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.systemsetting.model.SystemSetting;
import ncr.res.mobilepos.systemsetting.resource.SystemSettingResource;
import ncr.res.pastelport.platform.CommonTx;
import atg.taglib.json.util.JSONException;

/**
 * class that access to TXU_NPS_PastelPortLog.
 */
public class SQLServerLogDAO extends AbstractDao implements ILogDAO {

    /** The Constant PROGNAME. */
    private static final String PROGNAME = "AuthPP";

    /**
     * A private member variable that represents the Database Manager for the
     * class.
     */
    private DBManager dbManager;
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger;
    /**
     * Instance of the trace debug printer.
     */
    private Trace.Printer tp = null;
    /**
     * The Class Constructor.
     *
     * @throws DaoException
     *             thrown when database error occurs
     */
    public SQLServerLogDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Gets the Database Manager.
     *
     * @return Return an database manager instance
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    /**
     * Gets the value with a key from CommonTx.
     *
     * @param tx
     *            [in] object of CommonTx that including transaction information
     * @param dbtx
     *            [out] object of CommonTx that including search results true
     *            when have the record and false when don't have
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    @Override
    public final boolean select(final CommonTx tx, final CommonTx dbtx)
            throws DaoException, JSONException {
        tp.methodEnter("select");
        boolean creditLogSelect = false;
        Connection connection = null;
        PreparedStatement creditLogSelectPrepStmnt = null;
        ResultSet resultSet = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            creditLogSelectPrepStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-CreditLogSelect"));
            setSelectStatement(creditLogSelectPrepStmnt, tx);

            resultSet = creditLogSelectPrepStmnt.executeQuery();
            if (resultSet.next()) {
                creditLogSelect = true;
                setSelectedInfo(resultSet, dbtx, tx);
            } else {
                tp.println("No credit log to select.");
            }
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.select()", Logger.RES_EXCEP_SQLSTATEMENT,
                    ex.getMessage());
            throw new DaoException(ex.getMessage());
        } catch (SQLException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.select()", Logger.RES_EXCEP_SQL,
                    ex.getMessage());
            throw new DaoException(ex.getMessage());
        } catch (NumberFormatException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.select()", Logger.RES_EXCEP_PARSE,
                    ex.getMessage());
            throw new DaoException(ex.getMessage());
        } catch (IndexOutOfBoundsException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.select()", Logger.RES_ERROR_RESTRICTION,
                    ex.getMessage());
            throw new DaoException(ex.getMessage());
        } finally {
            closeConnectionObjects(connection, creditLogSelectPrepStmnt, 
            		resultSet);
            
            tp.methodExit(creditLogSelect);
        }
        return creditLogSelect;
    }

    /**
     * constructs stm for select with values in tx.
     *
     * @param stm
     *            the prepared statement
     * @param tx
     *            the tx
     * @throws JSONException
     *             thrown when JSON error occurs
     * @throws SQLException
     *             thrown when SQL error occurs
     */
    protected final void setSelectStatement(final PreparedStatement stm,
            final CommonTx tx) throws JSONException, SQLException {
        tp.methodEnter("setSelectStatement");
        String storeid = tx.getFieldValue("storeid");
        String terminalid = tx.getFieldValue("terminalid");
        String paymentseq = tx.getFieldValue("paymentseq");
        String txdatetime = tx.getFieldValue("txdatetime");
        tp.println("storeid", storeid);
        tp.println("terminalid", terminalid);
        tp.println("paymentseq", paymentseq);
        tp.println("txdatetime", txdatetime);
        stm.setString(SQLStatement.PARAM1, storeid);
        stm.setString(SQLStatement.PARAM2, terminalid);
        stm.setInt(SQLStatement.PARAM3,
                Integer.parseInt(paymentseq));
        stm.setString(SQLStatement.PARAM4,
                DateFormatUtility.localeTime(txdatetime));
        tp.methodExit();
    }

    /**
     * Constructs dbtx with values in rs and values in tx.
     *
     * @param rs
     *            the result Set from prepared statement query.
     * @param dbtx
     *            the dbtx
     * @param tx
     *            the tx
     * @throws SQLException
     *             thrown when SQL error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    protected final void setSelectedInfo(final ResultSet rs,
            final CommonTx dbtx, final CommonTx tx) throws SQLException,
            JSONException {
        tp.methodEnter("setSelectedInfo");
        dbtx.setFieldValue("storeid", rs.getString(SQLStatement.PARAM1));
        dbtx.setFieldValue("terminalid", rs.getString(SQLStatement.PARAM2));
        dbtx.setFieldValue("paymentseq",
                Integer.toString(rs.getInt(SQLStatement.PARAM3)));
        dbtx.setFieldValue("txdatetime", tx.getFieldValue("txdatetime"));
        dbtx.setFieldValue("txid",
                Integer.toString(rs.getInt(SQLStatement.PARAM5)));
        dbtx.setFieldValue("brand", rs.getString(SQLStatement.PARAM6));
        dbtx.setFieldValue("service", rs.getString(SQLStatement.PARAM7));
        dbtx.setFieldValue("txtype",
                Integer.toString(rs.getInt(SQLStatement.PARAM8)));
        dbtx.setFieldValue("txstatus", rs.getString(SQLStatement.PARAM9));
        dbtx.setFieldValue("crcompanycode",
                Integer.toString(rs.getInt(SQLStatement.PARAM10)));
        dbtx.setFieldValue("recvcompanycode",
                rs.getString(SQLStatement.PARAM11));
        dbtx.setFieldValue("paymentmethod",
                Integer.toString(rs.getInt(SQLStatement.PARAM12)));
        String pan = rs.getString(SQLStatement.PARAM13);
        dbtx.setFieldValue("pan", pan);
        String expdate = rs.getString(SQLStatement.PARAM14);
        dbtx.setFieldValue("expirationdate", expdate);
        dbtx.setFieldValue("amount",
                Long.toString(rs.getLong(SQLStatement.PARAM15)));
        dbtx.setFieldValue("castatus",
                Integer.toString(rs.getInt(SQLStatement.PARAM16)));
        dbtx.setFieldValue("caerrorcode", rs.getString(SQLStatement.PARAM17));
        dbtx.setFieldValue("dcstatus",
                Integer.toString(rs.getInt(SQLStatement.PARAM18)));
        dbtx.setFieldValue("dcerrorcode", rs.getString(SQLStatement.PARAM19));
        dbtx.setFieldValue("approvalcode", rs.getString(SQLStatement.PARAM20));
        dbtx.setFieldValue("tracenum", rs.getString(SQLStatement.PARAM21));
        dbtx.setFieldValue("alertflag",
                Integer.toString(rs.getInt(SQLStatement.PARAM22)));
        dbtx.setFieldValue("voidflag",
                Integer.toString(rs.getInt(SQLStatement.PARAM23)));
        dbtx.setFieldValue("voidcorpid", rs.getString(SQLStatement.PARAM24));
        dbtx.setFieldValue("voidstoreid", rs.getString(SQLStatement.PARAM25));
        dbtx.setFieldValue("voidterminalid",
                rs.getString(SQLStatement.PARAM26));
        dbtx.setFieldValue("voidpaymentseq",
                Integer.toString(rs.getInt(SQLStatement.PARAM27)));
        dbtx.setFieldValue("voidtxid",
                Integer.toString(rs.getInt(SQLStatement.PARAM28)));
        String voidtxdate = rs.getString(SQLStatement.PARAM29);
        if (voidtxdate != null) {
            dbtx.setFieldValue("voidtxdatetime", voidtxdate + "000000");
        } else {
            dbtx.setFieldValue("voidtxdatetime", voidtxdate);
        }
        dbtx.setFieldValue("settlementnum",
                rs.getString(SQLStatement.PARAM30));
        String txdate2 = rs.getString(SQLStatement.PARAM31);
        if (txdate2 != null) {
            dbtx.setFieldValue("txdate2", txdate2 + "000000");
        } else {
            dbtx.setFieldValue("txdate2", txdate2);
        }
        
        tp.methodExit();
    }

    /**
     * Add record with valus in tx.
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
        tp.methodEnter("insert");
        PreparedStatement creditLogInsertPrepStmnt = null;
        Connection connection = null;
        boolean ret = false;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            creditLogInsertPrepStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("save-creditloginsert"));
            setInsertStatement(creditLogInsertPrepStmnt, tx);
            if (1 == creditLogInsertPrepStmnt.executeUpdate()) {
                connection.commit();
                ret = true;
            } else {
                connection.rollback();
                ret = false;
            }
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.insert()", Logger.RES_EXCEP_SQLSTATEMENT,
                    ex.getMessage());
            rollBack(connection, "@SQLServerLogDAO:insert", ex);
            throw new DaoException(ex.getMessage());
        } catch (SQLException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.insert()", Logger.RES_EXCEP_SQL,
                    ex.getMessage());
            rollBack(connection, "@SQLServerLogDAO:insert", ex);
            throw new DaoException(ex.getMessage());
        } catch (NumberFormatException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.insert()", Logger.RES_EXCEP_PARSE,
                    ex.getMessage());
            rollBack(connection, "@SQLServerLogDAO:insert", ex);
            throw new DaoException(ex.getMessage());
        } finally {
            closeConnectionObjects(connection, creditLogInsertPrepStmnt);
            
            tp.methodExit(ret);
        }
        return ret;
    }

    /**
     * Constructs statement for insert with values in tx.
     *
     * @param stm
     *            the prepared statement
     * @param tx
     *            the tx
     * @throws SQLException
     *             thrown when SQL error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    protected final void setInsertStatement(final PreparedStatement stm,
            final CommonTx tx) throws SQLException, JSONException {
        tp.methodEnter("setInsertStatement");
        String companyid = tx.getFieldValue("companyid");
        String storeid = tx.getFieldValue("storeid");
        String terminalid = tx.getFieldValue("terminalid");
        String paymentseq = tx.getFieldValue("paymentseq");
        String txdatetime = tx.getFieldValue("txdatetime");
        String txid = tx.getFieldValue("txid");
         tp.println("storeid", storeid);
        tp.println("terminalid", terminalid);
        tp.println("paymentseq", paymentseq);
        tp.println("txdatetime", txdatetime);
        tp.println("txid", txid);
         stm.setString(SQLStatement.PARAM1, storeid);
        stm.setString(SQLStatement.PARAM2, terminalid);
        stm.setInt(SQLStatement.PARAM3,
                Integer.parseInt(paymentseq));
        stm.setString(SQLStatement.PARAM4,
                DateFormatUtility.localeTime(txdatetime));
        stm.setInt(SQLStatement.PARAM5,
                Integer.parseInt(txid));
        stm.setString(SQLStatement.PARAM6, tx.getBrand());
        stm.setString(SQLStatement.PARAM7, tx.getService());
        stm.setInt(SQLStatement.PARAM8, tx.getTxType());
        String cc = tx.getFieldValue("crcompanycode");
        if (cc != null) {
            stm.setInt(SQLStatement.PARAM9, Integer.parseInt(cc));
        } else {
            stm.setInt(SQLStatement.PARAM9, 0);
        }
        stm.setString(SQLStatement.PARAM10,
                tx.getFieldValue("recvcompanycode"));
        String pm = tx.getFieldValue("paymentmethod");
        if (pm != null) {
            stm.setInt(SQLStatement.PARAM11, Integer.parseInt(pm));
        } else {
            stm.setInt(SQLStatement.PARAM11, 0);
        }

        stm.setString(SQLStatement.PARAM12, tx.getFieldValue("pan4"));
        StringBuilder strBldEpirationdate = new StringBuilder("");
        for (int i = 0; i < tx.getFieldValue("expirationdate").length(); i++) {
            strBldEpirationdate.append("*");
        }
        stm.setString(SQLStatement.PARAM13, strBldEpirationdate.toString());
        stm.setLong(SQLStatement.PARAM14,
                Long.parseLong(tx.getFieldValue("amount")));
        // RES 3.1 対応 START
        stm.setString(SQLStatement.PARAM15, tx.getFieldValue("settlementnum"));

        // add business date
        SystemSettingResource sysSetRes = new SystemSettingResource();
        SystemSetting sysSetting = sysSetRes.getDateSetting(companyid, storeid);
        String bizDate = "";
        if (sysSetting.getDateSetting() != null) {
            bizDate = sysSetting.getDateSetting().getToday();
        }
        stm.setString(SQLStatement.PARAM16, bizDate);
        // RES 3.1 対応 END
        tp.methodExit();
    }

    /**
     * Updates data after ca received.
     *
     * @param tx
     *            object of CommonTx
     * @param resp
     *            object PastelPortResp true when update is successful and false
     *            when is failed
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    @Override
    public final boolean updateRca(final CommonTx tx, final PastelPortResp resp)
            throws DaoException, JSONException {
        tp.methodEnter("updateRca");
        PreparedStatement stm = null;
        boolean ret = false;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            stm = con.prepareStatement(sqlStatement
                    .getProperty("set-CreditLogUpdateRca"));
            setUpdateRcaStatement(stm, tx, resp);
            int upcnt = stm.executeUpdate();
            if (upcnt == 1) {
                con.commit();
                ret = true;
            } else {
                con.rollback();
                ret = false;
            }
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.updateRca()",
                    Logger.RES_EXCEP_SQLSTATEMENT, ex.getMessage());
            rollBack(con, "@SQLServerLogDAO:updateRca", ex);
            throw new DaoException(ex.getMessage());
        } catch (SQLException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.updateRca()", Logger.RES_EXCEP_SQL,
                    ex.getMessage());
            rollBack(con, "@SQLServerLogDAO:updateRca", ex);
            throw new DaoException(ex.getMessage());

        } catch (NumberFormatException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.updateRca()", Logger.RES_EXCEP_PARSE,
                    ex.getMessage());
            rollBack(con, "@SQLServerLogDAO:updateRca", ex);
            throw new DaoException(ex.getMessage());
        } finally {
            closeConnectionObjects(con, stm);
            
            tp.methodExit(ret);
        }
        return ret;
    }

    /**
     * Sets the update rca statement.
     *
     * @param stm
     *            the prepared statement.
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response.
     * @throws SQLException
     *             thrown when SQL error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    protected final void setUpdateRcaStatement(final PreparedStatement stm,
            final CommonTx tx, final PastelPortResp resp) throws SQLException,
            JSONException {
        tp.methodEnter("setUpdateRcaStatement");
        String storeid = tx.getFieldValue("storeid");
        String terminalid = tx.getFieldValue("terminalid");
        int paymentseq = Integer.parseInt(tx.getFieldValue("paymentseq"));
        String txdatetime = tx.getFieldValue("txdatetime");
        String txstatus = tx.getFieldValue("txstatus");
       tp.println("storeid", storeid);
        tp.println("terminalid", terminalid);
        tp.println("paymentseq", paymentseq);
        tp.println("txdatetime", txdatetime);
        tp.println("txstatus", txstatus);
        stm.setString(SQLStatement.PARAM1, txstatus);
        stm.setInt(SQLStatement.PARAM2,
                Integer.parseInt(resp.getCreditstatus()));
        stm.setString(SQLStatement.PARAM3, resp.getErrorcode());
        stm.setString(SQLStatement.PARAM4, resp.getApprovalcode());
        stm.setString(SQLStatement.PARAM5, resp.getTracenum());
        stm.setString(SQLStatement.PARAM6, storeid);
        stm.setString(SQLStatement.PARAM7, terminalid);
        stm.setInt(SQLStatement.PARAM8, paymentseq);
        stm.setString(SQLStatement.PARAM9,
                DateFormatUtility.localeTime(txdatetime));
        tp.methodExit();
    }

    /**
     * Updates data before send dc.
     *
     * @param tx
     *            object of CommonTx
     * @return true when update is successful and false when is failed
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    @Override
    public final boolean updateSdc(final CommonTx tx) throws DaoException,
            JSONException {
        tp.methodEnter("updateSdc");
        PreparedStatement stm = null;
        boolean ret = false;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            stm = con.prepareStatement(sqlStatement
                    .getProperty("set-CreditLogUpdateSdc"));
            setUpdateSdcStatement(stm, tx);
            int upcnt = stm.executeUpdate();
            if (upcnt == 1) {
                con.commit();
                ret = true;
            } else {
                con.rollback();
                ret = false;
            }
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.updateSdc()",
                    Logger.RES_EXCEP_SQLSTATEMENT, ex.getMessage());
            rollBack(con, "@SQLServerLogDAO:updateSdc", ex);
            throw new DaoException(ex.getMessage());
        } catch (SQLException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.updateSdc()", Logger.RES_EXCEP_SQL,
                    ex.getMessage());
            rollBack(con, "@SQLServerLogDAO:updateSdc", ex);
            throw new DaoException(ex.getMessage());
        } catch (NumberFormatException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.updateSdc()", Logger.RES_EXCEP_PARSE,
                    ex.getMessage());
            rollBack(con, "@SQLServerLogDAO:updateSdc", ex);
            throw new DaoException(ex.getMessage());
        } finally {
            closeConnectionObjects(con, stm);
            
            tp.methodExit(ret);
        }
        return ret;
    }

    /**
     * Constructs statement for update with values in tx before send dc.
     *
     * @param stm
     *            the prepared statement
     * @param tx
     *            the tx
     * @throws SQLException
     *             thrown when SQL error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    protected final void setUpdateSdcStatement(final PreparedStatement stm,
            final CommonTx tx) throws SQLException, JSONException {
        tp.methodEnter("setUpdateSdcStatement");
        String txtype = tx.getFieldValue("txtype");
        String storeid = tx.getFieldValue("storeid");
        String terminalid = tx.getFieldValue("terminalid");
        int paymentseq = Integer.parseInt(tx.getFieldValue("paymentseq"));
        String txdatetime = tx.getFieldValue("txdatetime");
        tp.println("txtype", txtype);
        tp.println("storeid", storeid);
        tp.println("terminalid", terminalid);
        tp.println("paymentseq", paymentseq);
        tp.println("txdatetime", txdatetime);
        stm.setInt(SQLStatement.PARAM1,
                Integer.parseInt(txtype));
        stm.setString(SQLStatement.PARAM2, storeid);
        stm.setString(SQLStatement.PARAM3, terminalid);
        stm.setInt(SQLStatement.PARAM4,
                paymentseq);
        stm.setString(SQLStatement.PARAM5,
                DateFormatUtility.localeTime(txdatetime));
        tp.methodExit();
    }

    /**
     * Updates data after dc received.
     *
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @return true when update is successful and false when is failed
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    @Override
    public final boolean updateRdc(final CommonTx tx, final PastelPortResp resp)
            throws DaoException, JSONException {
        tp.methodEnter("updateRdc");
        PreparedStatement stm = null;
        boolean ret = false;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            stm = con.prepareStatement(sqlStatement
                    .getProperty("set-CreditLogUpdateRdc"));
            setUpdateRdcStatement(stm, tx, resp);
            int upcnt = stm.executeUpdate();
            if (upcnt == 1) {
                con.commit();
                ret = true;
            } else {
                con.rollback();
                ret = false;
            }

        } catch (SQLStatementException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.updateRdc()",
                    Logger.RES_EXCEP_SQLSTATEMENT, ex.getMessage());
            rollBack(con, "@SQLServerLogDAO:updateRdc", ex);
            throw new DaoException(ex.getMessage());
        } catch (SQLException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.updateRdc()", Logger.RES_EXCEP_SQL,
                    ex.getMessage());
            rollBack(con, "@SQLServerLogDAO:updateRdc", ex);
            throw new DaoException(ex.getMessage());
        } catch (NumberFormatException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.updateRdc()", Logger.RES_EXCEP_PARSE,
                    ex.getMessage());
            rollBack(con, "@SQLServerLogDAO:updateRdc", ex);
            throw new DaoException(ex.getMessage());
        } finally {
            closeConnectionObjects(con, stm);
            
            tp.methodExit(ret);
        }
        return ret;
    }

    /**
     * Constructs statement with values in tx after dc received.
     *
     * @param stm
     *            the prepared statement
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @throws SQLException
     *             thrown when SQL error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    protected final void setUpdateRdcStatement(final PreparedStatement stm,
            final CommonTx tx, final PastelPortResp resp) throws SQLException,
            JSONException {
        tp.methodEnter("setUpdateRdcStatement");
        stm.setString(SQLStatement.PARAM1, tx.getFieldValue("txstatus"));
        String creditStatus = resp.getCreditstatus();
        if (creditStatus == null) {
            stm.setInt(SQLStatement.PARAM2, 0);
        } else {
            stm.setInt(SQLStatement.PARAM2,
                    Integer.parseInt(resp.getCreditstatus()));
        }
        stm.setString(SQLStatement.PARAM3, resp.getErrorcode());
        String approvalcode = resp.getApprovalcode();
        if (approvalcode == null) {
            approvalcode = "";
        }
        stm.setString(SQLStatement.PARAM4, approvalcode);
        stm.setString(SQLStatement.PARAM5, resp.getTracenum());
        int alertflag = 0;
        try {
            alertflag = Integer.parseInt(tx.getFieldValue("alertflag"));
        } catch (JSONException ex) {
            alertflag = 0;
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.updateRdc()", "タグなし", "タグなし");
            tp.println("Alert flag was not found.");
        }
        stm.setInt(SQLStatement.PARAM6, alertflag);
        String storeid = tx.getFieldValue("storeid");
        String terminalid = tx.getFieldValue("terminalid");
        int paymentseq = Integer.parseInt(tx.getFieldValue("paymentseq"));
        String txdatetime = tx.getFieldValue("txdatetime");
        tp.println("storeid", storeid);
        tp.println("terminalid", terminalid);
        tp.println("paymentseq", paymentseq);
        tp.println("txdatetime", txdatetime);
        stm.setString(SQLStatement.PARAM7, storeid);
        stm.setString(SQLStatement.PARAM8, terminalid);
        stm.setInt(SQLStatement.PARAM9,
                paymentseq);
        stm.setString(SQLStatement.PARAM10,
                DateFormatUtility.localeTime(txdatetime));
        tp.methodExit();
    }

    /**
     * Updates data when is cancel a deal.
     *
     * @param tx
     *            the tx
     * @return true when update is successful and false when is failed
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    @Override
    public final boolean updateVoid(final CommonTx tx) throws DaoException,
            JSONException {
        tp.methodEnter("updateVoid");
        PreparedStatement stm = null;
        boolean ret = false;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            stm = con.prepareStatement(sqlStatement
                    .getProperty("set-CreditLogUpdateVoid"));

            setUpdateVoidStatement(stm, tx);
            int upcnt = stm.executeUpdate();
            if (upcnt == 1) {
                con.commit();
                ret = true;
            } else {
                con.rollback();
                ret = false;
            }
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.updateRdc()",
                    Logger.RES_EXCEP_SQLSTATEMENT, ex.getMessage());
            rollBack(con, "@SQLServerLogDAO:updateVoid", ex);
            throw new DaoException(ex.getMessage());
        } catch (SQLException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.updateRdc()", Logger.RES_EXCEP_SQL,
                    ex.getMessage());
            rollBack(con, "@SQLServerLogDAO:updateVoid", ex);
            throw new DaoException(ex.getMessage());
        } catch (NumberFormatException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.updateRdc()", Logger.RES_EXCEP_PARSE,
                    ex.getMessage());
            rollBack(con, "@SQLServerLogDAO:updateVoid", ex);
            throw new DaoException(ex.getMessage());
        } finally {
            closeConnectionObjects(con, stm);
            
            tp.methodExit(ret);
        }
        return ret;
    }
    // RES 3.1の対応 START
    /**
     * Constructs statement for update with values in tx when is cancel a deal.
     *
     * @param stm
     *            the prepared statement
     * @param tx
     *            the tx
     * @throws SQLException
     *             thrown when SQL error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    protected final void setUpdateVoidStatement(final PreparedStatement stm,
            final CommonTx tx) throws SQLException, JSONException {
        tp.methodEnter("setUpdateVoidStatement");
        String voidflag = tx.getFieldValue("voidflag");
        String voidcorpid = tx.getFieldValue("voidcorpid");
        String voidstoreid = tx.getFieldValue("voidstoreid");
        String voidterminalid = tx.getFieldValue("voidterminalid");
        int voidpaymentseq = Integer.parseInt(
                tx.getFieldValue("voidpaymentseq"));
        String voidtxid = tx.getFieldValue("voidtxid");
        String voidtxdate = tx.getFieldValue("voidtxdatetime");
         String storeid = tx.getFieldValue("storeid");
        String terminalid = tx.getFieldValue("terminalid");
        int paymentseq = Integer.parseInt(tx.getFieldValue("paymentseq"));
        String txdatetime = tx.getFieldValue("txdatetime");
        tp.println("voidflag", voidflag)
        .println("voidcorpid", voidcorpid)
        .println("voidstoreid", voidstoreid)
        .println("voidterminalid", voidterminalid)
        .println("voidpaymentseq", voidpaymentseq)
        .println("voidtxid", voidtxid)
        .println("voidtxdate", voidtxdate)
        .println("storeid", storeid)
        .println("terminalid", terminalid)
        .println("paymentseq", paymentseq)
        .println("txdatetime", txdatetime);
        stm.setInt(SQLStatement.PARAM1,
                Integer.parseInt(voidflag));
        // RES 3.1 対応 START
        stm.setString(SQLStatement.PARAM2, voidcorpid);
        stm.setString(SQLStatement.PARAM3, voidstoreid);
        stm.setString(SQLStatement.PARAM4, voidterminalid);
        // RES 3.1 対応 END
        stm.setInt(SQLStatement.PARAM5, voidpaymentseq);
        stm.setString(SQLStatement.PARAM6, voidtxid);
        stm.setString(SQLStatement.PARAM7,
                DateFormatUtility.localeTime(voidtxdate));
         stm.setString(SQLStatement.PARAM8, storeid);
        stm.setString(SQLStatement.PARAM9, terminalid);
        stm.setInt(SQLStatement.PARAM10, paymentseq);
        stm.setString(SQLStatement.PARAM11,
                DateFormatUtility.localeTime(txdatetime));
        tp.methodExit();
    }

    /**
     * Gets max paymentseq within a single day.
     *
     * @param tx
     *            the tx
     * @return the max paymentseq
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    @Override
    public final int getMaxServerPaymentseq(final CommonTx tx)
            throws DaoException, JSONException {
        tp.methodEnter("getMaxServerPaymentseq");
        PreparedStatement stm = null;
        // PastelPort : minimal value of Payment sequence is 1
        int rePaymentseq = 1;
        Connection con = null;
        ResultSet resultSet = null;
        try {
            con = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            stm = con.prepareStatement(sqlStatement
                    .getProperty("get-MaxServerPaymentseq"));
            setMaxServerPaymentseqStatement(stm, tx);
            resultSet = stm.executeQuery();
            if (resultSet.next()) {
                rePaymentseq = resultSet.getInt("SERVERPAYMENTSEQ");
            } else {
                tp.println("MaxServerPaymentSeq was not found.");
            }
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.getMaxServerPaymentseq()",
                    Logger.RES_EXCEP_SQLSTATEMENT, ex.getMessage());
            throw new DaoException(ex.getMessage());
        } catch (SQLException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.getMaxServerPaymentseq()",
                    Logger.RES_EXCEP_SQL, ex.getMessage());
            throw new DaoException(ex.getMessage());
        } finally {
            closeConnectionObjects(con, stm, resultSet);
            
            tp.methodExit(rePaymentseq);
        }
        return rePaymentseq;
    }

    // 2012/5/14 add
    /**
     * @deprecated
     * Constructs statement for get max paymentseq with values in tx.
     *
     * @param stm
     *            the stm
     * @param tx
     *            the tx
     * @throws SQLException
     *             thrown when SQL error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    @Deprecated
    protected final void setMaxServerPaymentseqStatement(
            final PreparedStatement stm, final CommonTx tx)
            throws SQLException, JSONException {
        tp.methodEnter("setMaxServerPaymentseqStatement");
       String storeid = tx.getFieldValue("storeid");
        String terminalid = tx.getFieldValue("terminalid");
        tp.println("storeid", storeid)
        .println("terminalid", terminalid);
        stm.setString(SQLStatement.PARAM1, tx.getFieldValue("corpid"));
        stm.setString(SQLStatement.PARAM2, tx.getFieldValue("storeid"));
        stm.setString(SQLStatement.PARAM3, tx.getFieldValue("terminalid"));
        tp.methodExit();
    }


    /**
     * Updates serverpaymentseq when autovoid is occurred.
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @return the int
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    @Override
    public final int updateServerPaymentseq(final CommonTx tx,
            final PastelPortResp resp) throws DaoException, JSONException {
        tp.methodEnter("updateServerPaymentseq");
        PreparedStatement stm = null;
        int upcnt = 0;
        Connection con = null;
        try {
            con = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            stm = con.prepareStatement(sqlStatement
                    .getProperty("set-ServerPaymentseq"));
            setServerPaymentseqStatement(stm, tx, resp);
            upcnt = stm.executeUpdate();
            if (upcnt == 1) {
                con.commit();
            } else {
                con.rollback();
            }
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.updateServerPaymentseq()",
                    Logger.RES_EXCEP_SQLSTATEMENT, ex.getMessage());
            rollBack(con, "@SQLServerLogDAO:updateServerPaymentseq", ex);
            throw new DaoException(ex.getMessage());
        } catch (SQLException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.updateServerPaymentseq()",
                    Logger.RES_EXCEP_SQL, ex.getMessage());
            rollBack(con, "@SQLServerLogDAO:updateServerPaymentseq", ex);
            throw new DaoException(ex.getMessage());
        } finally {
            closeConnectionObjects(con, stm);
            
            tp.methodExit(upcnt);
        }
        return upcnt;
    }

    /**
     * @deprecated
     * Constructs stm for get server paymentseq with values in tx and resp.
     *
     * @param stm
     *            the prepared statement
     * @param tx
     *            the tx
     * @param resp
     *            the pastel port response
     * @throws SQLException
     *             thrown when SQL error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    @Deprecated
    protected final void setServerPaymentseqStatement(
            final PreparedStatement stm, final CommonTx tx,
            final PastelPortResp resp) throws SQLException, JSONException {
        tp.methodEnter("setServerPaymentseqStatement");
         String storeid = tx.getFieldValue("storeid");
        String terminalid = tx.getFieldValue("terminalid");
        String paymentseq = tx.getFieldValue("paymentseq");
        String txdatetime = tx.getFieldValue("txdatetime");
        tp.println("storeid", storeid)
        .println("terminalid", terminalid)
        .println("paymentseq", paymentseq)
        .println("txdatetime", txdatetime);
        stm.setString(SQLStatement.PARAM1, resp.getPaymentseq());
        stm.setString(SQLStatement.PARAM3, storeid);
        stm.setString(SQLStatement.PARAM4, terminalid);
        stm.setString(SQLStatement.PARAM5, paymentseq);
        stm.setString(SQLStatement.PARAM6,
                DateFormatUtility.localeTime(txdatetime));
        tp.methodExit();
    }

    /**
     * Gets the void value with a key from CommonTx.
     *
     * @param tx
     *            [in] object of CommonTx that including transaction information
     * @param dbtx
     *            [out] object of CommonTx that including search results true
     *            when have the record and false when don't have
     * @return true, if successful
     * @throws DaoException
     *             thrown when database error occurs
     * @throws JSONException
     *             thrown when JSON error occurs
     */
    @Override
    public final boolean selectVoid(final CommonTx tx, final CommonTx dbtx)
            throws DaoException, JSONException {
        tp.methodEnter("selectVoid");
        boolean creditLogSelect = false;
        Connection connection = null;
        PreparedStatement creditLogSelectPrepStmnt = null;
        ResultSet resultSet = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            creditLogSelectPrepStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-creditlogselectvoid"));
            setSelectStatementVoid(creditLogSelectPrepStmnt, tx);

            resultSet = creditLogSelectPrepStmnt.executeQuery();
            if (resultSet.next()) {
                creditLogSelect = true;
                setSelectedInfo(resultSet, dbtx, tx);
            } else {
                tp.println("No credit log to select.");
            }
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.select()", Logger.RES_EXCEP_SQLSTATEMENT,
                    ex.getMessage());
            throw new DaoException(ex.getMessage());
        } catch (SQLException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.select()", Logger.RES_EXCEP_SQL,
                    ex.getMessage());
            throw new DaoException(ex.getMessage());
        } catch (NumberFormatException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.select()", Logger.RES_EXCEP_PARSE,
                    ex.getMessage());
            throw new DaoException(ex.getMessage());
        } catch (IndexOutOfBoundsException ex) {
            LOGGER.logAlert(SQLServerLogDAO.PROGNAME,
                    "SQLServerLogDAO.select()", Logger.RES_ERROR_RESTRICTION,
                    ex.getMessage());
            throw new DaoException(ex.getMessage());
        } finally {
            closeConnectionObjects(connection, creditLogSelectPrepStmnt, 
            		resultSet);
            
            tp.methodExit(creditLogSelect);
        }
        return creditLogSelect;
    }


    /**
     * constructs stm for select with values in tx.
     *
     * @param stm
     *            the prepared statement
     * @param tx
     *            the tx
     * @throws JSONException
     *             thrown when JSON error occurs
     * @throws SQLException
     *             thrown when SQL error occurs
     */
    protected final void setSelectStatementVoid(final PreparedStatement stm,
            final CommonTx tx) throws JSONException, SQLException {
        tp.methodEnter("setSelectStatement");
        String voidstoreid = tx.getFieldValue("voidstoreid");
        String voidterminalid = tx.getFieldValue("voidterminalid");
        String voidtxid = tx.getFieldValue("voidtxid");
        String voidtxdatetime = tx.getFieldValue("voidtxdatetime");
        tp.println("terminalid", voidterminalid)
        .println("voidtxid", voidtxid)
        .println("txdatetime", voidtxdatetime);

        stm.setString(SQLStatement.PARAM1, voidstoreid);
        stm.setString(SQLStatement.PARAM2, voidterminalid);
        stm.setString(SQLStatement.PARAM3, voidtxid);
        stm.setString(SQLStatement.PARAM4,
                DateFormatUtility.localeTime(voidtxdatetime));
        tp.methodExit();
    }
 // RES3.1の対応 END
}
