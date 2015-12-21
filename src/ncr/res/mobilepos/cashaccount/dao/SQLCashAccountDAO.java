package ncr.res.mobilepos.cashaccount.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.cashaccount.model.CashBalance;
import ncr.res.mobilepos.cashaccount.model.GetCashBalance;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLCashAccountDAO extends AbstractDao implements ICashAccountDAO {

    /**
     * logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;
    /**
     * database manager object
     */
    private DBManager dbManager;
    /**
     * Program name, used for logging
     */
    private String progName = "CashAcct";
    
    public SQLCashAccountDAO() throws DaoException {
    	this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    	this.dbManager = JndiDBManagerMSSqlServer.getInstance();
    }
    
	@Override
	public GetCashBalance getCashBalance(String tillId, String storeId, 
			String businessDayDate) throws DaoException {
		tp.methodEnter("getCashBalance");
    	tp.println("Till Id", tillId)
    		.println("Store Id", storeId)
    		.println("Business Day Date", businessDayDate);
    	
    	Connection connection = null;
    	PreparedStatement statement = null;
    	ResultSet result = null;
    	GetCashBalance getCashBalance = new GetCashBalance();
    	
    	try {
    		connection = dbManager.getConnection();
    		SQLStatement sqlStatement = SQLStatement.getInstance();
    		statement = connection.prepareStatement(sqlStatement
                    .getProperty("get-cash-balance"));
    		statement.setString(SQLStatement.PARAM1, storeId);
    		statement.setString(SQLStatement.PARAM2, tillId);
    		statement.setString(SQLStatement.PARAM3, businessDayDate);
    		
    		result = statement.executeQuery();
    		
    		if (result.next()) {
    			String cashOnHand = result.getString("cashOnHand");
    			if (cashOnHand == null) {
        			getCashBalance.setNCRWSSResultCode(
        					ResultBase.RES_CASH_ACCOUNT_NO_CASH_BALANCE);
        			tp.println("Cash balance not retrieved.");
        			
        			return getCashBalance;
    			}
    			
    			CashBalance cashBalance = new CashBalance();
    			cashBalance.setCashOnHand(cashOnHand);
    			cashBalance.setTillId(tillId);
    			
    			getCashBalance.setCashBalance(cashBalance);
    		} else {
    			getCashBalance.setNCRWSSResultCode(
    					ResultBase.RES_CASH_ACCOUNT_NO_CASH_BALANCE);
    			tp.println("Cash balance not retrieved.");
    		}
    	} catch (SQLException e) {
    		LOGGER.logAlert(progName, "SQLCashAccountDAO.getCashBalance()", 
    				Logger.RES_EXCEP_SQL, 
    				"Failed to get cash balance.\n" + e.getMessage());
    		throw new DaoException("SQLException: "
    				+ "@SQLCashAccountDAO.getCashBalance() - " + e.getMessage(), e);
    	} catch (SQLStatementException e) {
    		LOGGER.logAlert(progName, "SQLCashAccountDAO.getCashBalance()", 
    				Logger.RES_EXCEP_SQLSTATEMENT, 
    				"Failed to get cash balance.\n" + e.getMessage());
    		throw new DaoException("SQLStatementException: "
    				+ "@SQLCashAccountDAO.getCashBalance() - " + e.getMessage(), e);
    	} finally {
            closeConnectionObjects(connection, statement, result);
            
            tp.methodExit(getCashBalance.toString());
    		
    	}
    	
		return getCashBalance;
	}

}
