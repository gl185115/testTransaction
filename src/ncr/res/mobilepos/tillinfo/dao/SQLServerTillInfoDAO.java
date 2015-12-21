package ncr.res.mobilepos.tillinfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.exception.TillException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.property.SQLStatement;
import ncr.res.mobilepos.tillinfo.model.Till;
import ncr.res.mobilepos.tillinfo.model.ViewTill;

public class SQLServerTillInfoDAO  extends AbstractDao implements ITillInfoDAO{
    /**
     * DBManager instance, provides database connection object.
     */
    private DBManager dbManager;
    /**
     * Logger instance, logs error and information.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * program name of the class.
     */
    private static final String PROG_NAME = "TilDao";
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;


    private static final String SOD_FLAG_PROCESSING= "9";

    private static final String SOD_FLAG_FINISHED = "1";
    
    private static final String SOD_STATE_ALLOW = "1";

    private static final String SOD_STATE_FINISHED = "2";

    private static final String SOD_STATE_OTHER_PROCESSING = "3";
    /**
     * Initializes DBManager.
     * 
     * @throws DaoException
     *             if error exists.
     */
    public SQLServerTillInfoDAO() throws DaoException {
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
    
    /*
     * view till details.
     * 
     * @see ncr.res.mobilepos.store.dao.ITillInfoDAO#viewTill(java.lang.String)
     */
    @Override
    public final ViewTill viewTill(final String storeID, String tillID)
            throws DaoException {

        String functionName = "SQLServerTillInfoDAO.viewTill";

        tp.methodEnter("viewTill");
        tp.println("storeID", storeID);
        tp.println("tillID", tillID);

        Till till = new Till();
        till.setStoreId(storeID);

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;
        ViewTill viewTill = new ViewTill();

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-till-info"));

            select.setString(SQLStatement.PARAM1, storeID);
            select.setString(SQLStatement.PARAM2, tillID);
            
            result = select.executeQuery();

            if (result.next()) {
                
                till.setStoreId(result.getString("StoreId"));
                till.setTillId(result.getString("TillId"));
                till.setBusinessDayDate(result.getString("BusinessDayDate"));
                till.setSodFlag(result.getString("SodFlag"));
                till.setEodFlag(result.getString("EodFlag"));                
                               
            } else {
                till.setStoreId(storeID);
                till.setTillId(tillID);
                
                viewTill.setNCRWSSResultCode(ResultBase.RES_TILL_NOT_EXIST);
                tp.println("Till not found.");
            }


        } catch (SQLException sqlEx) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to View Till#" + storeID + " : "
                            + sqlEx.getMessage());
            throw new DaoException("SQLException: @SQLServerTillInfoDAO"
                    + ".viewTill - Error view store", sqlEx);
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT, "Failed to View Till#"
                            + storeID + " : " + sqlStmtEx.getMessage());
            throw new DaoException("SQLStatementException: @SQLServerStoreDAO"
                    + ".viewTill - Error view till", sqlStmtEx);
        } catch (Exception ex) {
            LOGGER.logAlert(
                    PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to View Till#" + storeID + " : "
                            + ex.getMessage());
            throw new DaoException("Exception: @SQLServerStoreDAO"
                    + ".viewTill - Error view till", ex);
        } finally {
            closeConnectionObjects(connection, select, result);
            
            tp.methodExit(till);
        }
        
        viewTill.setTill(till);
        return viewTill;
    }
    /*
     * create till info.
     * 
     * @see ncr.res.mobilepos.store.dao.ITillInfoDAO#createTill(java.lang.String)
     */
	@Override
	public ResultBase createTill(String storeId, String tillId, Till till)
			throws DaoException {
        String functionName = "SQLServerTillInfoDAO.createTill";
        tp.methodEnter("createTill");
        tp.println("storeId", storeId);
        tp.println("tillId", tillId);
        tp.println("till", till);
        
        Connection connection = null;
        PreparedStatement insert = null;
        ResultBase res = new ResultBase();     
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();

            insert = connection.prepareStatement(sqlStatement
                    .getProperty("create-till-info"));
            
            insert.setString(SQLStatement.PARAM1, storeId);
            insert.setString(SQLStatement.PARAM2, tillId);
            insert.setString(SQLStatement.PARAM3, till.getBusinessDayDate());
            insert.setString(SQLStatement.PARAM4, till.getSodFlag());
            insert.setString(SQLStatement.PARAM5, till.getEodFlag());
            insert.setString(SQLStatement.PARAM6, till.getUpdAppId());
            insert.setString(SQLStatement.PARAM7, till.getUpdOpeCode());

            int result = insert.executeUpdate();
	          if (result > 0) {
	        	  res.setNCRWSSResultCode(ResultBase.RES_STORE_OK);
	          }
            connection.commit();
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to add Till\n " + e.getMessage());
            rollBack(connection, "@SQLServerTillInfoDAO.createTill ", e);
            if (Math.abs(SQLResultsConstants.ROW_DUPLICATE) == e
                    .getErrorCode()) {
            	res.setNCRWSSResultCode(ResultBase.RES_TILL_EXISTS);
                tp.println("Store entry is duplicated.");
            }else{
            	throw new DaoException("SQLException:"
                        + "@SQLServerTillInfoDAO.createTill ", e);
            }
                      
        } catch (SQLStatementException e) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to add Till\n " + e.getMessage());
            rollBack(connection, "SQLServerTillInfoDAO: @createTill ", e);
            throw new DaoException("SQLServerTillInfoDAO: @createTill ", e);
        } catch (Exception e) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to add Store\n " + e.getMessage());
            rollBack(connection, "@SQLServerTillInfoDAO.createTill ", e);
            throw new DaoException("SQLException:"
                    + "@SQLServerTillInfoDAO.createTill ", e);
        } finally {
            closeConnectionObjects(connection, insert);
            
            tp.methodExit(res);
        }
        return res;
	}
	
    /*
     * update till info.
     * 
     * @see ncr.res.mobilepos.store.dao.ITillInfoDAO#updateTill(java.lang.String)
     */
    @Override
	public final ViewTill updateTill(String storeId, String tillId, Till till) 
			throws DaoException {
        String functionName = "SQLServerStoreDAO.updateTill";

        tp.methodEnter("updateTill");
        tp.println("StoreId", storeId);

        Connection conn = null;
        PreparedStatement updateTillStmnt = null;
        ResultSet result = null;
		Till newTill = null;
        ViewTill viewTill = new ViewTill();
        boolean hasErrorOccur = false;
        try {

			SQLStatement sqlStatement = SQLStatement.getInstance();
			conn = dbManager.getConnection();
			updateTillStmnt = conn.prepareStatement(sqlStatement.getProperty("select-till-updlock"));
			updateTillStmnt.setString(SQLStatement.PARAM1,
					storeId);
			updateTillStmnt.setString(SQLStatement.PARAM2,
					tillId);
			updateTillStmnt.setString(SQLStatement.PARAM3,
					till.getBusinessDayDate());
			updateTillStmnt.setString(SQLStatement.PARAM4,
					till.getSodFlag());
			updateTillStmnt.setString(SQLStatement.PARAM5,
					till.getEodFlag());

			result = updateTillStmnt.executeQuery();
			
			newTill = new Till();
			if(result.next()){
				newTill.setStoreId(result.getString("StoreId"));
				newTill.setTillId(result.getString("TillId"));
				newTill.setBusinessDayDate(result.getString("BusinessDayDate"));
				newTill.setSodFlag(result.getString("SodFlag"));
				newTill.setEodFlag(result.getString("EodFlag"));
				
				if(newTill.getSodFlag().equalsIgnoreCase(SOD_FLAG_PROCESSING)){
					newTill.setState(SOD_STATE_ALLOW);
				}				
				
				if(newTill.getSodFlag().equalsIgnoreCase(SOD_FLAG_FINISHED)){
					newTill.setState(SOD_STATE_FINISHED);
				}
				
				
			}else {
				viewTill
						.setNCRWSSResultCode(ResultBase.RES_TILL_NO_UPDATE);
				
				newTill.setState(SOD_STATE_OTHER_PROCESSING);
				tp.println("Till update was not successful. "
						+ "Other tablet is in the SOD processing");
			}
			conn.commit();
			
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to Update Till with TillID#" + tillId + " : "
                            + ex.getMessage());
            
        } catch (SQLStatementException ex) {
            rollBack(conn, functionName, ex);
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT,
                    "Failed to Update Store with StoreID#" + storeId + " : "
                            + ex.getMessage());
            throw new DaoException("SQLStatementException: @"
                    + " - Error update store", ex);
        } catch (Exception ex) {
            rollBack(conn, functionName, ex);
            
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to Update Stores with StoreID#" + storeId + " : "
                            + ex.getMessage());
            throw new DaoException("Exception: @" + functionName
                    + " - Error update store", ex);
        } finally {
            hasErrorOccur = closeConnectionObjects(conn,
            		updateTillStmnt, result);            
        }
        
        if (hasErrorOccur) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_GENERAL, "@" + functionName
                            + " - Error Closing object");
            tp.methodExit(newTill);
            throw new DaoException("SQLException:@" + functionName
                    + " - Error Closing object");
        }
        
        tp.methodExit(newTill);       
        viewTill.setTill(newTill);
        return viewTill;
    }
    
    /**
     * Updates specific fields of Till when SOD is triggered.
     * @see ncr.res.mobilepos.store.dao.ITillInfoDAO#updateSODTill(java.lang.String)
     */
    public ResultBase updateSODTill(Till aTill, int sodFlagToChange) 
    		throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();            
        tp.methodEnter("updateSODTill");
        tp.println("till", aTill);
        tp.println("sodFlagToChange", sodFlagToChange);
        
        Connection connection = null;
        PreparedStatement updateTill = null;
        ResultSet result = null;
        ResultBase resultBase = new ResultBase();   
    
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateTill = connection.prepareStatement(
                    sqlStatement.getProperty("update-sod-till-updlock"));
            updateTill.setString(SQLStatement.PARAM1, aTill.getStoreId());
            updateTill.setString(SQLStatement.PARAM2, aTill.getTillId());
            updateTill.setString(SQLStatement.PARAM3, aTill.getTerminalId());
            updateTill.setInt(SQLStatement.PARAM4, Integer.parseInt(aTill.getSodFlag()));
            updateTill.setString(SQLStatement.PARAM5, aTill.getUpdAppId());
            updateTill.setString(SQLStatement.PARAM6, aTill.getUpdOpeCode());
            updateTill.setInt(SQLStatement.PARAM7, sodFlagToChange);
            
            result = updateTill.executeQuery();     
            
            if(!result.next()) {                 
                //Other terminal is already processing SOD.
                resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_SOD_PROCESSING);             
                resultBase.setMessage("Failed to update SOD Till Info.");
                tp.println("Failed to update SOD Till Info.");
            }
            connection.commit();
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to update SOD Till Info.", ex);
            throw new DaoException("SQLStatementException:"
                    + " @SQLServerTillInfoDAO." + functionName, ex);
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to update SOD Till Info.", ex);
            rollBack(connection, functionName, ex);
            throw new DaoException("SQLException: @" + functionName, ex);
        } finally {
            closeConnectionObjects(connection, updateTill, result);         
            tp.methodExit(resultBase);
        }   
        return resultBase;
    }
    
    @Override
    public ResultBase updateEODTill(Till aTill, int eodFlagToChange) 
    		throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();            
        tp.methodEnter(functionName);
        tp.println("till", aTill);
        tp.println("eodFlagToChange", eodFlagToChange);
        
        Connection connection = null;
        PreparedStatement updateTill = null;
        ResultSet result = null;
        ResultBase resultBase = new ResultBase();   
    
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateTill = connection.prepareStatement(
                    sqlStatement.getProperty("update-eod-till-updlock"));
            
            updateTill.setString(SQLStatement.PARAM1, aTill.getStoreId());
            updateTill.setString(SQLStatement.PARAM2, aTill.getTillId());
            updateTill.setString(SQLStatement.PARAM3, aTill.getTerminalId());
            updateTill.setInt(SQLStatement.PARAM4, Integer.parseInt(aTill.getEodFlag()));
            updateTill.setString(SQLStatement.PARAM5, aTill.getUpdAppId());
            updateTill.setString(SQLStatement.PARAM6, aTill.getUpdOpeCode());
            updateTill.setInt(SQLStatement.PARAM7, eodFlagToChange);
            
            result = updateTill.executeQuery();     
            
            if(!result.next()) {                 
                //Other terminal is already processing EOD.
                resultBase.setNCRWSSResultCode(ResultBase.RES_TILL_EOD_PROCESSING);             
                resultBase.setMessage("Failed to update EOD Till Info.");
                tp.println("Failed to update EOD Till Info.");
            }
            connection.commit();
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to update EOD Till Info.", ex);
            throw new DaoException("SQLStatementException:"
                    + " @SQLServerTillInfoDAO." + functionName, ex);
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to update EOD Till Info.", ex);
            rollBack(connection, functionName, ex);
            throw new DaoException("SQLException: @" + functionName, ex);
        } finally {
            closeConnectionObjects(connection, updateTill, result);         
            tp.methodExit(resultBase);
        }   
        return resultBase;
    }
    
    /**
     * Updates specific fields of Till after successful SOD/EOD.
     * @see ncr.res.mobilepos.store.dao.ITillInfoDAO#updateTillOnJourn(java.lang.String)
     */
    public void updateTillOnJourn(Connection connection,Till till, String oldSodFlag, String oldEodFlag, boolean isEnterprise) 
    		throws DaoException, TillException {
    	String functionName = DebugLogger.getCurrentMethodName();            
        tp.methodEnter("updateTillOnJourn");
        tp.println("till", till);
        tp.println("oldSodFlag", oldSodFlag);
        tp.println("oldEodFlag", oldEodFlag);
        
        PreparedStatement updateTill = null;
        ResultSet result = null; 
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date date = new Date();
        java.sql.Date sqlDate = null;
    
        try {
        	if(!StringUtility.isNullOrEmpty(till.getBusinessDayDate())) { 
        		date = dateFormat.parse(till.getBusinessDayDate());
        		sqlDate = new java.sql.Date(date.getTime());
        	}      	
            String stringSqlStatement = isEnterprise ? "update-till-on-journ-updlock-ent" : "update-till-on-journ-updlock";
            
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateTill = connection.prepareStatement(
                    sqlStatement.getProperty(stringSqlStatement));
            updateTill.setString(SQLStatement.PARAM1, till.getStoreId());
            updateTill.setString(SQLStatement.PARAM2, till.getTillId());
            updateTill.setInt(SQLStatement.PARAM3, Integer.parseInt(oldSodFlag));
            updateTill.setInt(SQLStatement.PARAM4, Integer.parseInt(till.getSodFlag()));
            updateTill.setInt(SQLStatement.PARAM5, Integer.parseInt(oldEodFlag));
            updateTill.setInt(SQLStatement.PARAM6, Integer.parseInt(till.getEodFlag()));
            updateTill.setDate(SQLStatement.PARAM7, sqlDate);
            updateTill.setString(SQLStatement.PARAM8, till.getUpdOpeCode());
                        
            result = updateTill.executeQuery();     
            
            if(!result.next()) {     
                //Concurrent access scenario. Failed to update till.
                throw new TillException("TillException: @SQLServerTillInfoDAO." + functionName + 
                        " Failed to update till. Other terminal is processing the current till.",
                        ResultBase.RES_TILL_NO_UPDATE);
            }
            //connection.commit() is commented out, journalization resource execute the commit functionality.
            //so that rollback functionality will work.
        } catch (SQLStatementException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to update till.", ex);
            throw new DaoException("SQLStatementException:"
                    + " @SQLServerTillInfoDAO." + functionName, ex);
        } catch (SQLException ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to update till.", ex);
            rollBack(connection, functionName, ex);
            throw new DaoException("SQLException: @" + functionName, ex);
        } catch (ParseException ex) { 
        	LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_PARSE, functionName
                    + ": Failed to parse business day date input.", ex);
            throw new TillException("ParseException: @SQLServerTillInfoDAO." + functionName, ex,
            		ResultBase.RES_TILL_INVALID_BIZDATE);
        } catch (TillException ex) { 
        	LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_TILL, functionName
                    + ": Failed to update till. Other terminal is processing the current till.", ex);
        	throw new TillException("TillException: @SQLServerTillInfoDAO." + functionName, ex, 
        			ex.getErrorCode());
        } finally {
            //rollback functionlity will work on same connection. it's neccessary not to close it until
            //saving of poslog is done.
            closeConnectionObjects(null, updateTill, result);         
            tp.methodExit();
        }   	
    }
    
    @Override
    public ResultBase searchLogonUsers(String companyId, String storeId, String tillId, 
    		String terminalId) throws DaoException {
    	String functionName = DebugLogger.getCurrentMethodName();            
        tp.methodEnter(functionName)
            .println("companyId", companyId)
        	.println("storeId", storeId)
        	.println("tillId", tillId)
        	.println("terminalId", terminalId);
        
        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;
        ResultBase resultBase = new ResultBase();
        
        try {
        	connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            statement = connection.prepareStatement(
                    sqlStatement.getProperty("search-logon-users"));
            
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, tillId);
            statement.setString(SQLStatement.PARAM4, terminalId);
            
            result = statement.executeQuery();
            
            if (result.next()) {
                resultBase.setNCRWSSResultCode(
                		ResultBase.RES_TILL_OTHER_USERS_SIGNED_ON);             
                resultBase.setMessage("Other users connected to till are "
                		+ "still signed on.");
                tp.println("Other users connected to till are still "
                		+ "signed on.");
            }
        } catch (SQLException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to search for logon users.", e);
            throw new DaoException("SQLException: @SQLServerTillInfoDAO."
            		+ functionName, e);
        } catch (SQLStatementException e) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to search for logon users.", e);
            throw new DaoException("SQLStatementException: "
            		+ "@SQLServerTillInfoDAO." + functionName, e);
        } finally {
        	closeConnectionObjects(connection, statement, result);
        	tp.methodExit(resultBase);
        }
        
    	return resultBase;
    }
    
    /*
     * get till information.
     * 
     * @see ncr.res.mobilepos.store.dao.ITillInfoDAO#getTillInfoList(java.lang.String)
     */
    @Override
	public  List<Till> getTillInformation(String storeId) 
			throws DaoException {
        String functionName = DebugLogger.getCurrentMethodName();

        tp.methodEnter(functionName);
        tp.println("StoreId", storeId);

        Connection connection = null;
        PreparedStatement selectStmnt = null;
        ResultSet result = null;
        List<Till> tillInfoList = null;
        
        try {

        	connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            selectStmnt = connection.prepareStatement(sqlStatement.getProperty("get-till-information-list"));
            selectStmnt.setString(SQLStatement.PARAM1,storeId);
			result = selectStmnt.executeQuery();
			
			while(result.next()){
				if(tillInfoList == null){
					tillInfoList = new ArrayList<Till>();
				}
				Till till = new Till();
				till.setStoreId(result.getString("StoreId"));
				till.setTillId(result.getString("TillId"));
				till.setTerminalId(result.getString("TerminalId"));
				till.setBusinessDayDate(result.getString("BusinessDayDate"));
				till.setSodFlag(result.getString("SodFlag"));
				till.setEodFlag(result.getString("EodFlag"));
				tillInfoList.add(till);
			}
			
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQLSTATEMENT,
                    functionName + ": Failed to get till infomation.", sqlStmtEx);
            throw new DaoException("SQLStatementException:"
                    + " @SQLServerTillInfoDAO.getTillInformation", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_SQL, functionName
                    + ": Failed to get till infomation.", sqlEx);
            throw new DaoException("SQLException:"
                    + " @SQLServerTillInfoDAO.getTillInformation", sqlEx);
        } catch (Exception ex) {
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName
                    + ": Failed to get till infomation.", ex);
            throw new DaoException("Exception:"
                    + " @SQLServerTillInfoDAO.getTillInformation", ex);
        } finally {
            closeConnectionObjects(connection, selectStmnt, result);
            tp.methodExit(tillInfoList);
        }
        return tillInfoList;
    }
    
}
