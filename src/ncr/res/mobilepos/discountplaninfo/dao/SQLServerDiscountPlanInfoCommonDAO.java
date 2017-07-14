package ncr.res.mobilepos.discountplaninfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.discountplaninfo.model.SubtotalDiscountInfo;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerDiscountPlanInfoCommonDAO extends AbstractDao implements
    DiscountPlanInfoCommomDAO{

	private final String PROG_NAME = "DisCountCommomDao";
    /** The database manager. */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /** The instance of the trace debug printer. */
    private Trace.Printer tp = null;

    /**
     * Initializes DBManager.
     *
     * @throws DaoException
     *             if error exists.
     */
    public SQLServerDiscountPlanInfoCommonDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Retrieves DBManager.
     *
     * @return dbManager instance of DBManager.
     */
    public final DBManager getDBManager() {
        return dbManager;
    }
	/**
     * @param SubtotalDiscount
     * @param companyId
     * @param storeId
     * @return the SubtotalDiscount information
     * @throws DaoException
     *             Thrown when process fails.
     */
	public ArrayList<SubtotalDiscountInfo> getSubtotalDiscount(String companyId, String storeId)
            throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);

        ArrayList<SubtotalDiscountInfo> subtotalDiscountInfo = null;
        ResultSet result = null;
        Connection connection = null;
        PreparedStatement select = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-subtotalDiscount"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, storeId);

            result = select.executeQuery();
            while (result.next()) {
                if(subtotalDiscountInfo == null){
                	subtotalDiscountInfo = new ArrayList<SubtotalDiscountInfo>();
                }
                SubtotalDiscountInfo subtotalDiscountList = new SubtotalDiscountInfo();
                subtotalDiscountList.setDiscountReason(result.getString("DiscountReason"));
                subtotalDiscountList.setDiscountReasonName(result.getString("DiscountReasonName"));

                subtotalDiscountInfo.add(subtotalDiscountList);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get subtotalDiscountList.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerDiscountPlanInfoCommonDAO.getSubtotalDiscount", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get subtotalDiscountList.", ex);
            throw new DaoException("Exception:"
                    + " @SQLServerDiscountPlanInfoCommonDAO.getSubtotalDiscount", ex);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit(subtotalDiscountInfo);
        }

        return subtotalDiscountInfo;
    }

}
