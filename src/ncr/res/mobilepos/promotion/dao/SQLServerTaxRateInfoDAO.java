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
import ncr.res.mobilepos.pricing.model.TaxRateInfo;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerTaxRateInfoDAO extends AbstractDao implements ITaxRateInfoDAO {
	/**
     * The Database Manager of the class.
     */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * The Program name.
     */
    private String progname = "TaxRateInfoDAO";
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
    public SQLServerTaxRateInfoDAO() throws DaoException {
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
     * Get TaxRateInfo from MST_TAXRATE
     * @param bussinessDate
     * @return TaxRateInfoList
     * @throws DaoException Exception when error occurs.
     */
    @Override
	public final List<TaxRateInfo> getTaxRateInfoList(String bussinessDate) throws DaoException {
    	String functionName = "getTaxRateInfoList";

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;

        List<TaxRateInfo> TaxRateInfoList = null;
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement.getProperty("get-taxrate-info-list"));
            select.setString(SQLStatement.PARAM1, bussinessDate);
            result = select.executeQuery();

            TaxRateInfo taxRateInfo = null;
            while(result.next()){
                if (TaxRateInfoList == null){
                	TaxRateInfoList = new ArrayList<TaxRateInfo>();
                }
                taxRateInfo = new TaxRateInfo();
                taxRateInfo.setTaxId(result.getInt(result.findColumn("TaxId")));
                taxRateInfo.setStartDate(result.getString(result.findColumn("StartDate")));
                taxRateInfo.setEndDate(result.getString(result.findColumn("EndDate")));
                taxRateInfo.setTaxRate(result.getInt(result.findColumn("TaxRate")));
                taxRateInfo.setSubCode1(result.getString(result.findColumn("SubCode1")));
                taxRateInfo.setSubNum1(result.getInt(result.findColumn("SubNum1")));
                taxRateInfo.setSubNum2(result.getInt(result.findColumn("SubNum2")));
                TaxRateInfoList.add(taxRateInfo);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to get the taxrate information.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getTaxRateInfoList ", sqlEx);
        }
        catch (NumberFormatException nuEx) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_PARSE,
                    "Failed to get the taxrate information.\n" + nuEx.getMessage());
            throw new DaoException("NumberFormatException: @getTaxRateInfoList ", nuEx);
        } catch (Exception e) {
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to get the taxrate information.\n" + e.getMessage());
            throw new DaoException("Exception: @getTaxRateInfoList ", e);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(TaxRateInfoList);
        }
        return TaxRateInfoList;
    }
}