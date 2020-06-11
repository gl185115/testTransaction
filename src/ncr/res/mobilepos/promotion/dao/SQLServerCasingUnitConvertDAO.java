package ncr.res.mobilepos.promotion.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerCasingUnitConvertDAO extends AbstractDao implements ICashingUnitConvertDAO {
    /**
     * The Database Manager of the class.
     */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * The Program name.
     */
    private String progname = "ItemInfoDAO";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * Default Constructor for SQLServerMixMatchDAO
     *
     * <P>
     * Sets DBManager for database connection, and Logger for logging.
     *
     * @throws DaoException
     *             The exception thrown when the constructor fails.
     */
    public SQLServerCasingUnitConvertDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Gets the Database Manager for SQLServerItemDAO.
     *
     * @return Returns a DBManager Instance.
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    /**
     * Convert RecordId to CashingUnit from MST_MAGCODEINFO.
     * @param companyId ,
     *        recordId
     * @return CashingUnit cashingUnit
     * @throws DaoException Exception when error occurs.
     */
    @Override
    public final String convertRecordIdToCashingUnit(final String companyId, final String recordId) throws DaoException {
        String functionName = "convertRecordIdToCashingUnit";
        tp.methodEnter("convertRecordIdToCashingUnit");
        tp.println("CompanyId", companyId);
        tp.println("RecordId", recordId);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        String cashingUint = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(
                    sqlStatement.getProperty("get-cashingUnit-value"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, recordId);
            result = select.executeQuery();

            if (result.next()) {
                cashingUint = result.getString("CashingUint");
            }
        } catch (SQLException e) {
            LOGGER.logAlert(progname, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get dpt code from MST_MAGCODEINFO.", e);
            throw new DaoException("SQLException:@"
                    + "SQLServerItemInfoDAO." + functionName, e);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit(result.toString());
        }
        return cashingUint;
    }
}
