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
import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.JAXBException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.forwarditemlist.model.ForwardCountData;
import ncr.res.mobilepos.forwarditemlist.model.ForwardvoidListInfo;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.DataBinding;
import ncr.res.mobilepos.journalization.model.PosLogResp;
import ncr.res.mobilepos.journalization.model.poslog.PosLog;
import ncr.res.mobilepos.journalization.model.poslog.Transaction;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;

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
            PosLog posLog = GlobalConstant.poslogDataBinding.unMarshallXml(poslogXml);

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

    /**
     * Get the Forward Item Count.
     * @param companyId       The Company ID
     * @param storeId         The Store ID
     * @param businessDayDate The BusinessDate
     * @param workstationId   The Workstation ID
     * @param queue           The Queue
     * @param trainingFlag    The training Flag
     *
     * @return Forward Item Count
     * @exception DaoException The exception thrown when error occur.
     */
    @Override
    public final String selectForwardItemCount(final String companyId,
            final String storeId, final String businessDayDate,
            final String workstationId, final String queue, final String trainingFlag) throws DaoException {
        String functionName = "selectForwardItemCount";
        tp.methodEnter(functionName);
        tp.println("CompanyId", companyId)
            .println("StoreId", storeId)
            .println("BusinessDayDate",businessDayDate)
            .println("WorkstationId", workstationId)
            .println("Queue", queue)
            .println("TrainingFlag", trainingFlag);

        String resultCnt = null;
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet resultset = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-forward-item-count"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, storeId);
            select.setString(SQLStatement.PARAM3, businessDayDate);
            select.setString(SQLStatement.PARAM4, StringUtility.convNullToEmpty(workstationId));
            select.setString(SQLStatement.PARAM5, StringUtility.convNullToEmpty(queue));
            select.setString(SQLStatement.PARAM6, trainingFlag);
            resultset = select.executeQuery();

            if (resultset.next()) {
                resultCnt = resultset.getString("count");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to Select Forward Item Count.", sqlEx);
            throw new DaoException("SQLException: @" + functionName, sqlEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to Select Forward Item Count.", e);
            throw new DaoException("Exception: @" + functionName, e);
        } finally {
            closeConnectionObjects(connection, select, resultset);
            tp.methodExit();
        }
        return resultCnt;
    }

    /**
     * 前捌保留件数取得（精算機ごと）
     *
     * @param companyId
     * @param retailStoreId
     * @param cashierId
     * @param trainingFlag
     *
     * @return Forward Count With Cashier
     * @exception DaoException The exception thrown when error occur.
     */
    @Override
    public final String getForwardCountWithCashier(final String companyId, final String retailStoreId, final String cashierId,
    		                                  final String trainingFlag) throws DaoException  {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId).println("retailStoreId", retailStoreId)
                .println("cashierId", cashierId)
                .println("trainingFlag", trainingFlag);

        String resultCnt = null;
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet resultset = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-forward-countwithcashier"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, retailStoreId);
            select.setString(SQLStatement.PARAM3, cashierId);
            select.setInt(SQLStatement.PARAM4, Integer.valueOf(trainingFlag));
            resultset = select.executeQuery();

            if (resultset.next()) {
                resultCnt = resultset.getString("Count");
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to Select Forward Count With Cashier.", sqlEx);
            throw new DaoException("Database error");
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to Select Forward Count With Cashier.", e);
            throw new DaoException("Exception: @" + functionName, e);
        } finally {
            closeConnectionObjects(connection, select, resultset);
            tp.methodExit();
        }
        return resultCnt;
    }

    /**
     * 呼出取消データ情報一覧取得
     *
     * @param companyId
     * @param retailStoreId
     * @param workStationId
     * @param trainingFlag
     *
     * @return Forward Resume Void List
     * @exception DaoException The exception thrown when error occur.
     */
    @Override
    public final List<ForwardvoidListInfo> getForwardResumeVoidList(final String companyId, final String retailStoreId,
    	    final String workStationId, final String trainingFlag) throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("companyId", companyId).println("retailStoreId", retailStoreId)
                .println("workStationId", workStationId)
                .println("trainingFlag", trainingFlag);

        ArrayList<ForwardvoidListInfo> forwardResumeVoidList = new ArrayList<ForwardvoidListInfo>();
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement selectStmnt = null;

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement.getProperty("get-forward-resumeVoidList"));
            selectStmnt.setString(SQLStatement.PARAM1, companyId);
            selectStmnt.setString(SQLStatement.PARAM2, retailStoreId);
            selectStmnt.setString(SQLStatement.PARAM3, workStationId);
            selectStmnt.setInt(SQLStatement.PARAM4, Integer.valueOf(trainingFlag));
            result = selectStmnt.executeQuery();
            while (result.next()) {
                ForwardvoidListInfo forwardvoidListInfo = new ForwardvoidListInfo();
                forwardvoidListInfo.setCompanyId(result.getString("CompanyId"));
                forwardvoidListInfo.setRetailStoreId(result.getString("RetailStoreId"));
                forwardvoidListInfo.setWorkstationId(result.getString("WorkstationId"));
                forwardvoidListInfo.setQueue(result.getString("Queue"));
                forwardvoidListInfo.setTrainingFlag(result.getString("TrainingFlag"));
                forwardvoidListInfo.setBusinessDayDate(result.getString("BusinessDayDate"));
                forwardvoidListInfo.setStatus(result.getString("Status"));
                forwardvoidListInfo.setSequenceNumber(String.format("%04d", result.getInt("SequenceNumber")));
                forwardvoidListInfo.setBusinessDateTime(result.getString("BusinessDateTime"));
                forwardvoidListInfo.setOperatorId(result.getString("OperatorId"));
                forwardvoidListInfo.setSalesTotalAmt(result.getString("SalesTotalAmt"));
                forwardvoidListInfo.setSalesTotalQty(result.getString("SalesTotalQty"));
                forwardvoidListInfo.setExt2(result.getString("Ext2"));
                forwardvoidListInfo.setDeviceName(result.getString("DeviceName"));
                forwardResumeVoidList.add(forwardvoidListInfo);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get forwardresumevoid list.", sqlEx);
            throw new DaoException("Database error");
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get forwardresumevoid list.", ex);
            throw new DaoException("Exception:" + functionName, ex);
        } finally {
            closeConnectionObjects(connection, selectStmnt, result);
            tp.methodExit(forwardResumeVoidList);
        }
        return forwardResumeVoidList;
    }
}
