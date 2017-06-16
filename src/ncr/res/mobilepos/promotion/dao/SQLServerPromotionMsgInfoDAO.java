package ncr.res.mobilepos.promotion.dao;

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
import ncr.res.mobilepos.promotion.model.PromotionMsgInfo;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerPromotionMsgInfoDAO extends AbstractDao implements IPromotionMsgInfoDAO{

	/**
     * The Database Manager of the class.
     */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * The Program name.
     */
    private String progname = "PromotionMsgInfoDAO";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * Default Constructor for SQLServerPromotionMsgInfoDAO
     *
     * <P>
     * Sets DBManager for database connection, and Logger for logging.
     *
     * @throws DaoException
     *             The exception thrown when the constructor fails.
     */
    public SQLServerPromotionMsgInfoDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Gets the Database Manager for SQLServerPromotionMsgInfoDAO.
     *
     * @return Returns a DBManager Instance.
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    /**
     * Get PromotionId from MST_PROMOTIONMSG_INFO, MST_PROMOTIONMSG_DETAIL and MST_PROMOTIONMSG_STORE.
     * @param companyId,
	 *         storeId,
	 *         dayDate
     * @return List<PromotionMsgInfo>
     * @throws DaoException Exception when error occurs.
     */
    @Override
    public List<PromotionMsgInfo> getPromotionMsgInfoList(String companyId, String storeId, String dayDate) throws DaoException {
    	String functionName = DebugLogger.getCurrentMethodName();
    	tp.println("CompanyId", companyId);
        tp.println("storeId", storeId);
        tp.println("dayDate", dayDate);

        List<PromotionMsgInfo> promMsgList = new ArrayList<PromotionMsgInfo>();
        try (Connection connection = dbManager.getConnection();
              PreparedStatement statement = prepareGetPromotionMsgInfoList(companyId, storeId, dayDate, connection);
              ResultSet result = statement.executeQuery();)
        {
        	PromotionMsgInfo promMsgInfo = null;
            while(result.next()){
                promMsgInfo = new PromotionMsgInfo();
                promMsgInfo.setRecordId(result.getInt(result.findColumn("RecordId")));
                promMsgInfo.setSubject(result.getString(result.findColumn("Subject")));
                promMsgInfo.setMinimunPrice(result.getLong(result.findColumn("MinimunPrice")));
                promMsgInfo.setMessageBody(result.getString(result.findColumn("MessageBody")));
                promMsgInfo.setItemCode(result.getString(result.findColumn("MdInternal")));
                promMsgList.add(promMsgInfo);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the PromotionMsg information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getPromotionMsgInfoList ", sqlEx);
        } finally {
        	tp.methodExit(promMsgList);
        }
        return promMsgList;
	}

    protected PreparedStatement prepareGetPromotionMsgInfoList(String companyId,String storeId,String businessDate,Connection connection
            ) throws SQLException {
        // Creates PreparedStatement .
        SQLStatement sqlStatement = SQLStatement.getInstance();
        PreparedStatement statement = connection.prepareStatement(sqlStatement.getProperty("get-promotion-msg-info-list"));
        statement.setString(SQLStatement.PARAM1, companyId);
        statement.setString(SQLStatement.PARAM2, storeId);
        statement.setString(SQLStatement.PARAM3, businessDate);

        return statement;
    }

    @Override
	public List<String> getPromotionMsgDptList(String companyId, int recordId) throws DaoException {

    	String functionName = DebugLogger.getCurrentMethodName();
    	tp.println("CompanyId", companyId);
        tp.println("storeId", recordId);

        List<String> dptList = new ArrayList<String>();
        try (Connection connection = dbManager.getConnection();
              PreparedStatement statement = prepareGetPromotionMsgDptList(companyId, recordId, connection);
              ResultSet result = statement.executeQuery();)
        {
            while(result.next()){
                dptList.add(result.getString(result.findColumn("Dpt")));
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the PromotionMsgDpt.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getPromotionMsgDptList ", sqlEx);
        } finally {
        	tp.methodExit(dptList);
        }
        return dptList;
	}

    protected PreparedStatement prepareGetPromotionMsgDptList(String companyId, int recordId, Connection connection
            ) throws SQLException {
        // Creates PreparedStatement .
        SQLStatement sqlStatement = SQLStatement.getInstance();
        PreparedStatement statement = connection.prepareStatement(sqlStatement.getProperty("get-promotion-msg-dpt-list"));
        statement.setString(SQLStatement.PARAM1, companyId);
        statement.setInt(SQLStatement.PARAM2, recordId);

        return statement;
    }
}
