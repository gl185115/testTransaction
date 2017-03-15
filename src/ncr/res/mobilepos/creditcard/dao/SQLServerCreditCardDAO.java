/*
 * Copyright (c) 2011-2012,2015 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerCreditCardDAO
 *
 * A Data Access Object implementation for CreditCard.
 *
 */
package ncr.res.mobilepos.creditcard.dao;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import atg.taglib.json.util.JSONArray;
import atg.taglib.json.util.JSONObject;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.webserviceif.model.JSONData;

/**
 * SQLServerCreditCardDAO is a Data Access Object implementation for CreditCard information
 *
 */
public class SQLServerCreditCardDAO extends AbstractDao implements ICreditCardAbstractDAO {
    /**
     * Instance of Logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Program name use in logging.
     */
    private String PROG_NAME = "Credcad";

    /**
     * DBMananger provides connection object.
     */
    private DBManager dbManager;

    /**
     * Gets the dbManager.
     *
     * @return the dbManager
     */
    public final DBManager getDbManager() {
        return dbManager;
    }

    /**
     * Sets the dbManager.
     *
     * @param dbMgr
     *            the new DBManager
     */
    public final void setDbManager(final DBManager dbMgr) {
        this.dbManager = dbMgr;
    }

    /** The Trace Printer. */
    private Trace.Printer tp;


    /**
     * Default constructor. Initializes dbManager and iowriter.
     *
     * @throws DaoException
     *             thrown when error occurs.
     */
    public SQLServerCreditCardDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }

    /**
     * Retrieves dbManager.
     *
     * @return dbManager object.
     */
    public final DBManager getDBManager() {
        return dbManager;
    }

	@Override
	public JSONData getCreditCardCompayInfo(String companyId) throws DaoException {
		String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName).println("companyId", companyId);
        
		Connection connection = null;
		PreparedStatement statement = null;
		ResultSet result = null;
		
		JSONData cardcompanies = new JSONData();
        
		try{
			
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            
            statement = connection.prepareStatement(sqlStatement.getProperty("get-credit-card-company"));
            statement.setString(SQLStatement.PARAM1, companyId);
            
            result = statement.executeQuery();
            JSONObject temp = null;
            JSONArray datas = new JSONArray();
            while (result.next()) {
            	temp = new JSONObject();
            	temp.put("CreditCompanyId", result.getString("CreditCompanyId"));
            	temp.put("CreditCompanyName", result.getString("CreditCompanyName"));
            	temp.put("CreditCompanyShortName", result.getString("CreditCompanyShortName"));
            	datas.add(temp);
            }
            if (datas.isEmpty()) {
            	cardcompanies.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
            	cardcompanies.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
            	cardcompanies.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return cardcompanies;
            } else {
            	cardcompanies.setJsonObject(datas.toString());
                cardcompanies.setNCRWSSResultCode(ResultBase.RESRPT_OK);
                cardcompanies.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
                cardcompanies.setMessage(ResultBase.RES_SUCCESS_MSG);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName + ": Failed to get credit card company infomation.",
                    sqlEx);
            throw new DaoException("SQLException:" + " @SQLServerCreditCardDAO.getCreditCardCompayInfo", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL,
                    functionName + ": Failed to get credit card company infomation.", ex);
            throw new DaoException("Exception:" + " @SQLServerCreditCardDAO.getCreditCardCompayInfo", ex);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(cardcompanies);
        }
		
		return cardcompanies;
	}

}
