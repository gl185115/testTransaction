package ncr.res.mobilepos.journalization.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.journalization.model.ForwardListInfo;
import ncr.res.mobilepos.journalization.model.SearchGuestOrder;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerCommonDAO extends AbstractDao implements
        ICommonDAO {
    private final String PROG_NAME = "BnsCommomDao";
    /** The database manager. */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /**
     * The Constructor of the Class.
     *
     * @throws DaoException
     *             thrown when process fails.
     */
    public SQLServerCommonDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Gets the Database Manager for the Class.
     *
     * @return The Database Manager Object
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    // 1.05 2014.11.20 取引番号により前受金番号取得  ADD START
    /**
     * @param storeId
     *            the store id
     * @param deviceId
     *            the device id
     * @param sequenceNo
     *            the sequence No
     * @param businessDate
     *            the business Date
     *
     * @return return the guest order information
     *
     * @throws DaoException
     *             Thrown when process fails.
     */
    @Override
    public SearchGuestOrder searchGuestOrderInfoBySequenceNo(String storeId,
            String deviceId, String sequenceNo, String businessDate)
            throws DaoException {

        String functionName = "searchGuestOrderInfoBySequenceNo";
        tp.methodEnter(functionName).println("storeId", storeId)
                .println("deviceId", deviceId)
                .println("sequenceNo", sequenceNo)
                .println("businessDate", businessDate);

        PreparedStatement selectStmnt = null;
        ResultSet resultSet = null;
        Connection connection = null;
        SearchGuestOrder searchGuestOrder = new SearchGuestOrder();

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement
                    .getProperty("get-guest-order-info-by-sequenceNo"));
            selectStmnt.setString(SQLStatement.PARAM1, storeId);
            selectStmnt.setString(SQLStatement.PARAM2, deviceId);
            selectStmnt.setString(SQLStatement.PARAM3, sequenceNo);
            selectStmnt.setString(SQLStatement.PARAM4, businessDate);

            resultSet = selectStmnt.executeQuery();
            if (!resultSet.next()) {
                searchGuestOrder
                        .setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                searchGuestOrder
                        .setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
                searchGuestOrder.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return searchGuestOrder;
            } else {
                searchGuestOrder.setGuestNo(resultSet.getString("GuestNO"));
            }
            searchGuestOrder.setNCRWSSResultCode(ResultBase.RESRPT_OK);
            searchGuestOrder.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
            searchGuestOrder.setMessage(ResultBase.RES_SUCCESS_MSG);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to search guest order By SequenceNo.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerCommonDAO.searchGuestOrderInfoBySequenceNo", sqlEx);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to search guest order info By SequenceNo.", e);
            throw new DaoException("Exception:"
                    + " @SQLServerCommonDAO.searchGuestOrderInfoBySequenceNo", e);
        } finally {
            closeConnectionObjects(connection, selectStmnt, resultSet);
            tp.methodExit(searchGuestOrder);
        }
        return searchGuestOrder;
    }
    // 1.05 2014.11.20 取引番号により前受金番号取得 ADD END

    /**
     * 前捌 リストの取得
     */
    @Override
    public List<ForwardListInfo> getForwardList(String CompanyId, String RetailStoreId,
            String TrainingFlag, String LayawayFlag, String Queue, String TxType, String BussinessDayDate) throws DaoException {
        String functionName = "getForwardList";
        tp.methodEnter(functionName);
        tp.println("CompanyId", CompanyId).println("RetailStoreId", RetailStoreId)
                .println("TrainingFlag", TrainingFlag)
                .println("LayawayFlag", LayawayFlag)
                .println("Queue", Queue)
                .println("TxType", TxType)
                .println("BussinessDayDate", BussinessDayDate);

        ArrayList<ForwardListInfo> forwardList = null;
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement select = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-forward-list"));
            select.setString(SQLStatement.PARAM1, CompanyId);
            select.setString(SQLStatement.PARAM2, RetailStoreId);
//            select.setString(SQLStatement.PARAM3, WorkstationId);
            select.setString(SQLStatement.PARAM3, TrainingFlag);
            select.setString(SQLStatement.PARAM4, LayawayFlag);
            select.setString(SQLStatement.PARAM5, Queue);
            select.setString(SQLStatement.PARAM6, TxType);
            select.setString(SQLStatement.PARAM7, BussinessDayDate);
            result = select.executeQuery();
            while (result.next()) {
                if (forwardList == null) {
                    forwardList = new ArrayList<ForwardListInfo>();
                }
                ForwardListInfo forwardListInfo = new ForwardListInfo();
                forwardListInfo.setCompanyId(result.getString("CompanyId"));
                forwardListInfo.setRetailStoreId(result.getString("RetailStoreId"));
                forwardListInfo.setWorkstationId(result.getString("WorkstationId"));
                forwardListInfo.setQueue(result.getString("Queue"));
                forwardListInfo.setSequenceNumber(String.format("%04d", result.getInt("SequenceNumber")));
                forwardListInfo.setTrainingFlag(result.getString("TrainingFlag"));
                forwardListInfo.setBusinessDayDate(result.getString("BusinessDayDate"));
                forwardListInfo.setBusinessDateTime(result.getString("BusinessDateTime"));
                forwardListInfo.setOperatorId(result.getString("OperatorId"));
                forwardListInfo.setOperatorName(result.getString("OperatorName"));
                forwardListInfo.setStatus(result.getString("Status"));
                forwardListInfo.setSalesTotalAmt(result.getString("SalesTotalAmt"));
                forwardListInfo.setSalesTotalQty(result.getString("SalesTotalQty"));
                forwardListInfo.setTag(result.getString("Ext1"));
                forwardList.add(forwardListInfo);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get forward list.", sqlEx);
            throw new DaoException("SQLException:" + functionName, sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to get forward list.", ex);
            throw new DaoException("Exception:" + functionName, ex);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(forwardList);
        }
        return forwardList;
    }

    /**
     * 前捌レコード ステータスの更新
     *
     * @throws SQLException
     */
    @Override
    public int updateForwardStatus(String CompanyId, String RetailStoreId, String WorkstationId, String SequenceNumber,
            String Queue, String BusinessDayDate, String TrainingFlag, int Status) throws DaoException, SQLException {

        String functionName = "updateForwardStatus";
        tp.methodEnter(functionName).println("CompanyId", CompanyId).println("RetailStoreId", RetailStoreId)
                .println("WorkstationId", WorkstationId).println("SequenceNumber", SequenceNumber)
                .println("Queue", Queue).println("BusinessDayDate", BusinessDayDate)
                .println("TrainingFlag", TrainingFlag);

        int result = 0;
        Connection connection = null;
        PreparedStatement updateForwardPrepStmnt = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateForwardPrepStmnt = connection.prepareStatement(sqlStatement.getProperty("update-forward-status"));
            updateForwardPrepStmnt.setInt(SQLStatement.PARAM1, Status);
            updateForwardPrepStmnt.setString(SQLStatement.PARAM2, CompanyId);
            updateForwardPrepStmnt.setString(SQLStatement.PARAM3, RetailStoreId);
            updateForwardPrepStmnt.setString(SQLStatement.PARAM4, WorkstationId);
            updateForwardPrepStmnt.setString(SQLStatement.PARAM5, SequenceNumber.replaceFirst("^0*", ""));
            updateForwardPrepStmnt.setString(SQLStatement.PARAM6, Queue);
            updateForwardPrepStmnt.setString(SQLStatement.PARAM7, BusinessDayDate);
            updateForwardPrepStmnt.setString(SQLStatement.PARAM8, TrainingFlag);

            if (updateForwardPrepStmnt.executeUpdate() != 1) {
                result = ResultBase.RESSYS_ERROR_QB_QUEUEFULL;
            }
            connection.commit();
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to update forward status.",
                    sqlEx);
            connection.rollback();
            throw new DaoException("SQLException:" + functionName, sqlEx);
        } catch (Exception ex) {
            connection.rollback();
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ": Failed to update forward status.",
                    ex);
            throw new DaoException("Exception:" + functionName, ex);
        } finally {
            closeConnectionObjects(connection, updateForwardPrepStmnt);
            tp.methodExit(result);
        }
        return result;
    }
}
