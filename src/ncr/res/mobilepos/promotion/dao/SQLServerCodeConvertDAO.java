package ncr.res.mobilepos.promotion.dao;

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
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerCodeConvertDAO extends AbstractDao implements ICodeConvertDAO {
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
    public SQLServerCodeConvertDAO() throws DaoException {
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
     * Convert CCode to DptCode from MST_CCODEINFO.
     * @param companyId ,
	 *        CCode    
     * @return Dpt code
     * @throws DaoException Exception when error occurs.
     */
    @Override
	public final String convertCCodeToDpt(final String companyId, final String code) throws DaoException {
    	String functionName = DebugLogger.getCurrentMethodName();
    	tp.methodEnter("convertCCodeToDpt");
    	tp.println("CompanyId", companyId);
        tp.println("Code", code);
        
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        String dptCode = null;
        
        try {
        	connection = dbManager.getConnection();
        	SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(
                    sqlStatement.getProperty("convert-ccode-to-dpt"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, code);
            result = select.executeQuery();
            
            if (result.next()) {
            	dptCode = result.getString("Dpt");
            }
        } catch (SQLException e) {
			LOGGER.logAlert(progname, Logger.RES_EXCEP_SQL, functionName
					+ ": Failed to get dpt code from MST_CCODEINFO.", e);
			throw new DaoException("SQLException:@"
					+ "SQLServerItemInfoDAO." + functionName, e);
        } finally {
            closeConnectionObjects(connection, select, result);
            
            tp.methodExit(result.toString());
        }
        return dptCode;
    }
    
    /**
     * Convert MagCode to DptCode from MST_MAGCODEINFO.
     * @param companyId ,
	 *        magCode    
     * @return Dpt code
     * @throws DaoException Exception when error occurs.
     */
    @Override
	public final String convertMagCodeToDpt(final String companyId, final String code) throws DaoException {
    	String functionName = DebugLogger.getCurrentMethodName();
    	tp.methodEnter("convertMagCodeToDpt");
    	tp.println("CompanyId", companyId);
        tp.println("Code", code);
        
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        String dptCode = null;
        
        try {
        	connection = dbManager.getConnection();
        	SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(
                    sqlStatement.getProperty("convert-magcode-to-dpt"));
            select.setString(SQLStatement.PARAM1, companyId);
            select.setString(SQLStatement.PARAM2, code);
            result = select.executeQuery();
            
            if (result.next()) {
            	dptCode = result.getString("Dpt");
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
        return dptCode;
    }
}
