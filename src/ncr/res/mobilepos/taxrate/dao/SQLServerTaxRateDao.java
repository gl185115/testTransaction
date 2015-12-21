package ncr.res.mobilepos.taxrate.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * A Data access object implementation for taxRate.
 *
 */
public class SQLServerTaxRateDao extends AbstractDao implements ITaxRateDao {
    /**
     * The Database Manager of the class.
     */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * The Program name.
     */
    private String progname = "MixMatchDAO";
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
    public SQLServerTaxRateDao() throws DaoException {
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
     * get TaxRate
     * @param date the date of request
     * @return TaxRate
     * @exception DaoException the exception of sql
     */
    @Override
    public Map<String,String> getTaxRateByDate(String date) throws DaoException {
        tp.methodEnter("getTaxRateByDate");
        tp.println("date", date);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet resultSet = null;
        SQLStatement sqlStatement = null;
        Map<String,String> map = new HashMap<String,String>();
        try {
            sqlStatement = SQLStatement.getInstance();
            connection = dbManager.getConnection();
            select = connection.prepareStatement(sqlStatement.getProperty("get-taxrate-info"));
            select.setString(SQLStatement.PARAM1, date);
            resultSet = select.executeQuery();
            while (resultSet.next()) {
                if(!StringUtility.isNullOrEmpty(resultSet.getString(resultSet.findColumn("TaxRate")),
                        resultSet.getString(resultSet.findColumn("TaxRateFlag")))){
                    if("1".equals(resultSet.getString(resultSet.findColumn("TaxRateFlag")))){
                        map.put("oldTaxRate", resultSet.getString(resultSet.findColumn("TaxRate")));
                    }else if("2".equals(resultSet.getString(resultSet.findColumn("TaxRateFlag")))){
                        map.put("currentTaxRate", resultSet.getString(resultSet.findColumn("TaxRate")));
                    }else if("3".equals(resultSet.getString(resultSet.findColumn("TaxRateFlag")))){
                        map.put("futureTaxRate", resultSet.getString(resultSet.findColumn("TaxRate")));
                    }
                }
            }
        } catch (SQLException e) {
            LOGGER.logAlert(progname, "SQLServerTaxRateDao.getTaxRateByDate()", Logger.RES_EXCEP_SQL,
                    "Failed to get the taxRate Data.\n" + e.getMessage());
            throw new DaoException("SQLException: @getTaxRateByDate ", e);
        } catch (SQLStatementException e) {
            LOGGER.logAlert(progname, "SQLServerTaxRateDao.getTaxRateByDate()", Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to get the taxRate Data.\n" + e.getMessage());
            throw new DaoException("SQLStatementException: @getTaxRateByDate ", e);
        }catch (Exception ex) {
            LOGGER.logAlert(progname,"SQLServerTaxRateDao.getTaxRateByDate()", Logger.RES_EXCEP_GENERAL,
                    "Failed to get taxRate class info.",
                    ex);
            throw new DaoException("Exception: @SQLServerTaxRateDao.getTaxRateByDate", ex);
        } finally {
            closeConnectionObjects(connection, select, resultSet);
            tp.methodExit(map);
        }
        return map;
    }

	
}
