/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.forwarditemlist.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import javax.xml.bind.JAXBException;

import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.forwarditemlist.model.ForwardCountData;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.realgate.util.Trace;
/**
 * A Data Access Object implementation for
 * Transfer transactions between smart phone and POS.
 */
public class SQLServerForwardItemListDAO
extends AbstractDao implements IForwardItemListDAO {
    private static final String PROG_NAME = "FwardDAO";
	/**
     * The Database manager.
     */
    private DBManager dbManager;
    /**
     * The IOWriter to log.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); //Get the Logger
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * constructor.
     * @throws DaoException The exception thrown when error occur.
     */
    public SQLServerForwardItemListDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }

    /**
     * Gets the Database Manager for the Class.
     *
     * @return    The Database Manager Object
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    /**
     * Get Shopping Cart Data from TXL_FORWARD_ITEM.
     *
     * @param storeid       The Store Number
     * @param terminalid    The Terminal Number
     * @param txdate        The BusinessDate
     *
     * @return Shopping Cart Data
     * @exception   DaoException The exception thrown when error occur.
     */
    @Override
    public final String getShoppingCartData(final String storeid,
            final String terminalid, final String txdate)
            throws DaoException {

    	String functionName = "SQLServerForwardItemListDAO.getShoppingCartData";
        tp.methodEnter(functionName);
        tp.println("StoreID", storeid).println("TerminalID", terminalid).
            println("TransactionDate", txdate);

        String shoppingCartData = null;
        int txid = -1;
        boolean result = true;
        String txDate = "";
        boolean isErrorClosing = false;
        Connection connection = null;
        PreparedStatement selectForwardDataStmt = null;
        PreparedStatement updateForwardItemStatusStmt = null;
        SQLStatement sqlStatement = null;
        ResultSet rs = null;

        SimpleDateFormat fromUser = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
            txDate = myFormat.format(fromUser.parse(txdate));

            connection = dbManager.getConnection();
            sqlStatement = SQLStatement.getInstance();

            selectForwardDataStmt = connection.prepareStatement(
                    sqlStatement.getProperty("get-forwardData"));

            selectForwardDataStmt.setString(SQLStatement.PARAM1, storeid);
            selectForwardDataStmt.setString(SQLStatement.PARAM2, terminalid);
            selectForwardDataStmt.setString(SQLStatement.PARAM3, txDate);
            
            rs = selectForwardDataStmt.executeQuery();
			if (rs.next()) {
				shoppingCartData = rs.getString("Tx");
				txid = rs.getInt("SequenceNumber");
			} else {
				tp.println("No shopping cart data found.");
				return null;
			}
        } catch (SQLStatementException sqlStmtEx) {
            result = false;
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "SQL Statement Error occured. \n" + sqlStmtEx.getMessage());
        } catch (SQLException sqlEx) {
            result = false;
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "SQL Exception Error occured. \n" + sqlEx.getMessage());
        } catch (ParseException peEx) {
            result = false;
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_PARSE,
                    "Parse Exception Error occured. \n" + peEx.getMessage());
		} finally {
			isErrorClosing = closeConnectionObjects(null,
					selectForwardDataStmt, rs);
			try {
				if (!isErrorClosing && connection != null) {
					updateForwardItemStatusStmt = connection
							.prepareStatement(sqlStatement
									.getProperty("set-forwardStatus"));
					String status = (result) ? "1" : "2";
					updateForwardItemStatusStmt.setString(SQLStatement.PARAM1,
							status);
					updateForwardItemStatusStmt.setString(SQLStatement.PARAM2,
							storeid);
					updateForwardItemStatusStmt.setString(SQLStatement.PARAM3,
							terminalid);
					updateForwardItemStatusStmt.setString(SQLStatement.PARAM4,
							txDate);
					updateForwardItemStatusStmt.setInt(SQLStatement.PARAM5,
							txid);

					int affectedRow = updateForwardItemStatusStmt
							.executeUpdate();
					if (affectedRow == SQLResultsConstants.ONE_ROW_AFFECTED) {
						connection.commit();
						tp.println("Status updated to " + status);
					} else {
						tp.println("Status not updated.");
						shoppingCartData = null;						
					}
				} else {
					tp.println("Error closing objects or exception occurs.");
					shoppingCartData = null;					
				}
			} catch (SQLException sqlEx) {
				LOGGER.logAlert(
						PROG_NAME,
						functionName,
						Logger.RES_EXCEP_SQL,
						"SQL Exception Error occured."
								+ " Failed to update status. \n"
								+ sqlEx.getMessage());
				isErrorClosing = true;
			} catch (Exception ex) {
				LOGGER.logAlert(
						PROG_NAME,
						functionName,
						Logger.RES_EXCEP_GENERAL,
						"SQL Exception Error occured."
								+ " Failed to update status. \n"
								+ ex.getMessage());
				isErrorClosing = true;
			} finally {
				closeConnectionObjects(connection, selectForwardDataStmt, null);
				
				tp.methodExit(shoppingCartData);
			}
        }
        return shoppingCartData;
    }

    /**
     * Get the Forward Count Data.
     * @param storeid       The Store ID
     * @param terminalid    The terminal ID
     * @param txdate        The BusinessDate
     *
     * @return ForwardCountData The Forwarded Count Data
     * @exception DaoException The exception thrown when error occur.
     */
    @Override
    public final ForwardCountData getForwardCountData(final String storeid,
            final String terminalid, final String txdate) throws DaoException {

    	String functionName = "SQLServerForwardItemListDAO.getForwardCountData().";
        tp.methodEnter(functionName);
        tp.println("StoreID", storeid).println("TerminalID", terminalid).
            println("TransactionDate", txdate);

        ForwardCountData forwardCountData = new ForwardCountData();

        Connection connection = null;
        PreparedStatement getForwardItemCount = null;
        ResultSet rs = null;

        SimpleDateFormat fromUser = new SimpleDateFormat("yyyyMMdd");
        SimpleDateFormat myFormat = new SimpleDateFormat("yyyy-MM-dd");

		try {
            String txDate = myFormat.format(fromUser.parse(txdate));

            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            getForwardItemCount = connection.prepareStatement(
                    sqlStatement.getProperty("get-forwardCount"));

            getForwardItemCount.setString(SQLStatement.PARAM1, storeid);
            getForwardItemCount.setString(SQLStatement.PARAM2, terminalid);
            getForwardItemCount.setString(SQLStatement.PARAM3, txDate);

            rs = getForwardItemCount.executeQuery();

            if (rs.next()) {
                forwardCountData.setStatus("0");
                forwardCountData.setForwardCount(rs.getString("count"));
            }
        } catch (SQLStatementException sqlStmtEx) {
            forwardCountData.setStatus("1");
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "SQL Statement Error occured. \n" + sqlStmtEx.getMessage());
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "SQL Exception Error occured. \n" + sqlEx.getMessage());
            forwardCountData.setStatus("1");
        } catch (ParseException peEx) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_PARSE,
                    "Parse Exception Error occured. \n" + peEx.getMessage());
            forwardCountData.setStatus("1");
        } finally {
            closeConnectionObjects(connection, getForwardItemCount, rs);
            
            tp.methodExit(forwardCountData.toString());
        }

        return forwardCountData;
    }

    /**
     * Upload the item forward data to TXL_FORWARD_ITEM.
     *
     * @param deviceNo      The device No
     * @param terminalNo    The Terminal no
     * @param poslogXml     The POSLog XML
     * @return The POSLog response {@see PosLogResp}
     * @exception DaoException The exception thrown when error occur.
     */
    @Override
    public final PosLogResp uploadItemForwardData(
            final String deviceNo, final String terminalNo,
            final String poslogXml)
    throws DaoException {
        //Start log.
    	String functionName = "SQLServerForwardItemListDAO.uploadItemForwardData";
        tp.methodEnter(functionName);
        tp.println("DeviceNo", deviceNo).println("TerminalNo", terminalNo).
            println("POSLogXML", poslogXml);

        Connection connection = null;
        PreparedStatement saveItemForwardData = null;

        PosLogResp poslogResp = new PosLogResp();
        int result = 0;

		try {
            XmlSerializer<PosLog> xmlTmpl = new XmlSerializer<PosLog>();
            PosLog posLog =
                (PosLog) xmlTmpl.unMarshallXml(poslogXml, PosLog.class);

            Transaction transaction = posLog.getTransaction();
            String txid = transaction.getSequenceNo();
            String businessDate = transaction.getBusinessDayDate();
            String storeNo = transaction.getRetailStoreID();

            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            saveItemForwardData = connection.prepareStatement(
                    sqlStatement.getProperty("save-itemForwardData"));

            saveItemForwardData.setString(SQLStatement.PARAM1, storeNo);
            saveItemForwardData.setString(SQLStatement.PARAM2, deviceNo);
            saveItemForwardData.setString(SQLStatement.PARAM3, txid);
            saveItemForwardData.setString(SQLStatement.PARAM4, businessDate);
            saveItemForwardData.setString(SQLStatement.PARAM5, terminalNo);
            saveItemForwardData.setString(SQLStatement.PARAM6, poslogXml);

            result = saveItemForwardData.executeUpdate();
            if (result >= SQLResultsConstants.ONE_ROW_AFFECTED) {
                connection.commit();
                poslogResp.setNCRWSSResultCode(ResultBase.RES_OK);
                poslogResp.setStatus("0");
            } else {
            	poslogResp.setNCRWSSResultCode(ResultBase.RES_FORWARD_ITEM_NO_INSERT);
            	poslogResp.setStatus("1");
                tp.println("No row(s)( affected. Check TXL_FORWARD_ITEM table structure.");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to save transaction in the database.\n"
                    + sqlEx.getMessage());
            rollBack(connection,
                    functionName,
                    sqlEx);
        } catch (SQLStatementException sqlStmtEx) {
            rollBack(connection,
            		functionName,
                    sqlStmtEx);
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to save transaction in the database.\n"
                    + sqlStmtEx.getMessage());
        } catch (JAXBException e) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_JAXB,
                    "Failed to log PosLog transaction.\n"
                    + "POSLog XML Parse Error\n"
                    + "PosLog was null.");
        } finally {
            closeConnectionObjects(connection, saveItemForwardData);
            
            tp.methodExit(poslogResp.toString());
        }
        return poslogResp;
    }
}
