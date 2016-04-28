package ncr.res.mobilepos.uiconfig.dao;

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
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.uiconfig.model.schedule.CompanyInfo;

public class SQLServerUiConfigCommonDAO extends AbstractDao implements
		IUiConfigCommonDAO{
	private final String PROG_NAME = "UcfCmnDAO";
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
    public SQLServerUiConfigCommonDAO() throws DaoException {
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
    
    /**
     * @return return the guest zone information
     *
     * @throws DaoException
     *             Thrown when process fails.
     */
    @Override
	public List<CompanyInfo> getCompanyInfo() throws DaoException {
		// TODO Auto-generated method stub
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter(functionName);
		
		List<CompanyInfo> companylist = null;
		ResultSet result = null;
		Connection connection = null;
		PreparedStatement select = null;
		
		try{
			connection = dbManager.getConnection();
			SQLStatement sqlStatement = SQLStatement.getInstance();
			select = connection.prepareStatement(sqlStatement
					.getProperty("get-company-info"));
			result = select.executeQuery();
			while(result.next()) {
				if(companylist == null) {
					companylist = new ArrayList<CompanyInfo>();
				}
				CompanyInfo companyInfo = new CompanyInfo();
				companyInfo.setCompanyId(result.getString("CompanyId"));
				companyInfo.setCompanyName(result.getString("CompanyName"));
				companyInfo.setCompanyName(result.getString("CompanyKanaName"));
				companyInfo.setCompanyName(result.getString("CompanyShortName"));
				companyInfo.setCompanyName(result.getString("CompanyShortKanaName"));
				companylist.add(companyInfo);
			}
		} catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to get CompanyInfo list.", sqlStmtEx);
            throw new DaoException("SQLStatementException:"
                    + " @SQLServerCompanyInfoDAO.getCompanyInfo", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get CompanyInfo list.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerCompanyInfoDAO.getCompanyInfo", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get CompanyInfo list.", ex);
            throw new DaoException("Exception:"
                    + " @SQLServerCompanyInfoDAO.getCompanyInfo", ex);
        } finally {
            closeConnectionObjects(connection, select, result);
            tp.methodExit(companylist);
        }
		
		return companylist;
	}

}
