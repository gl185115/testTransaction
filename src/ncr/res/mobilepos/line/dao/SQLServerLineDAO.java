/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerClassInfoDAO
 *
 * DAO which handles the operations in the table specific for Classes
 *
 * Romares, Sul
 */

package ncr.res.mobilepos.line.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.exception.SQLStatementException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.line.model.Line;
import ncr.res.mobilepos.line.model.ViewLine;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.model.Description;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * A Data Access Object implementation for Line.
 * 
 * @see ILineDAO
 */

public class SQLServerLineDAO extends AbstractDao implements ILineDAO {
    /**
     * The Database Manager of the class.
     */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * The Program name.
     */
    private static final String PROG_NAME = "LineDAO";    
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * Default Constructor for SQLServerLineDAO
     * 
     * <P>
     * Sets DBManager for database connection, and Logger for logging.
     * 
     * @throws DaoException
     *             The exception thrown when the constructor fails.
     */
    public SQLServerLineDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }
    /**
     * Gets the Database Manager for SQLServerLineDAO.
     * 
     * @return Returns a DBManager Instance.
     */
    public final DBManager getDbManager() {
        return dbManager;
    }    
    
    /**
     * {@inheritDoc}
     */    
    public final List<Line> listLines(final String retailstoreid, final String department,
            final String key, final String name, final int limit) throws DaoException {

    	String functionName = "SQLServerLineDAO.listLineS";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
        		.println("retailstoreid", retailstoreid)
                .println("department", department)
                .println("key", key)
                .println("name", name)
                .println("limit", limit);
       
        List<Line> lineList = new ArrayList<Line>();
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;

		try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-line-list"));            
            int searchLimit = (limit == 0) ? GlobalConstant
                    .getMaxSearchResults() : limit;            
            select.setInt(SQLStatement.PARAM1, searchLimit);
            select.setString(SQLStatement.PARAM2, retailstoreid);
            select.setString(SQLStatement.PARAM3, department);
            select.setString(SQLStatement.PARAM4, StringUtility.isNullOrEmpty(key)? null : StringUtility.escapeCharatersForSQLqueries(key) + '%');
            select.setString(SQLStatement.PARAM5, StringUtility.isNullOrEmpty(name)? null : '%' + StringUtility.escapeCharatersForSQLqueries(name) + '%' );
            result = select.executeQuery();

            while (result.next()) {
            	Line line = new Line(); 
            	line.setRetailStoreId(result.getString(result
                        .findColumn("StoreId")));    
            	line.setLine(result.getString(result
                        .findColumn("Line")));             	
            	Description description = new Description();
                description
                        .setEn(result.getString(result.findColumn("LineName")));
                description.setJa(result.getString(result
                        .findColumn("LineNameLocal")));
                line.setDescription(description);                
                line.setDepartment(result.getString(result
                        .findColumn("Dpt"))); 
                line.setTaxType(result.getString(result
                        .findColumn("TaxType")));
                line.setTaxRate(result.getString(result
                        .findColumn("TaxRate")));
                line.setDiscountType(result.getString(result
                        .findColumn("DiscountType")));
                line.setExceptionFlag(result.getString(result
                        .findColumn("ExceptionFlag")));                
                line.setDiscountFlag(result.getString(result.findColumn("DiscountFlag")));
                line.setDiscountAmount(result.getDouble(result.findColumn("DiscountAmt")));
                line.setDiscountRate(result.getDouble(result.findColumn("DiscountRate")));             
                line.setAgeRestrictedFlag(result.getString(result.findColumn("AgeRestrictedFlag")));                
                line.setInheritFlag(result.getString(result.findColumn("InheritFlag")));
                line.setSubSmallInt5(result.getString(result.findColumn("SubSmallInt5")));   
                
                lineList.add(line);
            }
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT, "Failed to get the lines.\n"
                            + sqlStmtEx.getMessage());
            throw new DaoException("SQLStatementException: @listLines ",
                    sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to get the lines.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @listLines ", sqlEx);
        } finally {
            closeConnectionObjects(connection, select, result);
            
            tp.methodExit("Lines count: " + lineList.size());
        }
        return lineList;
    }
    
    
    /**
     * {@inheritDoc}
     */ 
    @Override
    public final ResultBase deleteLine(final String retailStoreID,
    		final String department,
    		final String line) throws DaoException {

    	String functionName = "SQLServerLineDAO.deleteLine";
        tp.methodEnter(functionName)
        	.println("retailStoreID", retailStoreID)
            .println("department", department)
            .println("line", line);

        Connection connection = null;
        PreparedStatement deleteStmt = null;
        int result = 0;
        ResultBase resultBase = new ResultBase();

		try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            deleteStmt = connection.prepareStatement(sqlStatement
                    .getProperty("delete-line"));
            setValues(deleteStmt, retailStoreID, department, line);
            
            result = deleteStmt.executeUpdate();
            if (result == 0) {
                resultBase.setNCRWSSResultCode(ResultBase.RES_LINE_INFO_NOT_EXIST);
                tp.println("No line was deleted.");
            }
            connection.commit();
        } catch (SQLStatementException e) {
            rollBack(connection,
                    "SQLStatementException:@"+functionName, e);          
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT, "Failed to delete line\n "
                            + e.getMessage());
            throw new DaoException("SQLServerLineDAO: @deleteLine ", e);
        } catch (SQLException e) {
            rollBack(connection, "SQLException:@"+functionName, e);           
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_SQL,
                    "Failed to delete line\n " + e.getMessage());
            throw new DaoException(
                    "SQLException: @"+functionName, e);
        } catch (Exception e) {
            rollBack(connection, "Exception:@"+functionName, e);           
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to delete line\n " + e.getMessage());
            throw new DaoException(
                    "SQLException: @"+functionName, e);
        } finally {
            closeConnectionObjects(connection, deleteStmt);
            
            tp.methodExit(resultBase);
        }

        return resultBase;
    }
    
    
    /**
     * {@inheritDoc} .
     */
    @Override
    public final ResultBase createLine(final Line line) throws DaoException {

        String functionName = "SQLServerLineDAO.createLine";
        tp.methodEnter(DebugLogger.getCurrentMethodName())               
                .println("Line", line);
        
        ResultBase resultBase = new ResultBase();        
        if(line == null){
        	resultBase
		            .setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
		    tp.println("Parameter[s] is null or empty.");
		    tp.methodExit(resultBase);
		    return resultBase;
        }        
        
        Connection connection = null;
        PreparedStatement insertStmt = null;
        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            insertStmt = connection.prepareStatement(sqlStatement
                    .getProperty("insert-line"));

            // line description
            String en = "";
            String ja = "";
            if (line.getDescription() != null) {
                en = line.getDescription().getEn() != null ? 
                		line.getDescription().getEn() : "";
                ja = line.getDescription().getJa() != null ? 
                		line.getDescription().getJa() : "";
            }                                  
            insertStmt.setString(SQLStatement.PARAM1, line.getRetailStoreId());
            insertStmt.setString(SQLStatement.PARAM2, line.getLine());
            insertStmt.setString(SQLStatement.PARAM3, en);
            insertStmt.setString(SQLStatement.PARAM4, ja); 
            insertStmt.setString(SQLStatement.PARAM5, line.getDepartment());
            insertStmt.setString(SQLStatement.PARAM6, line.getTaxType());
            insertStmt.setString(SQLStatement.PARAM7, line.getTaxRate());
            insertStmt.setString(SQLStatement.PARAM8, line.getDiscountType());
            insertStmt.setString(SQLStatement.PARAM9, line.getExceptionFlag());
            insertStmt.setString(SQLStatement.PARAM10, line.getDiscountFlag());
            insertStmt.setDouble(SQLStatement.PARAM11, line.getDiscountAmount()); 
            insertStmt.setDouble(SQLStatement.PARAM12, line.getDiscountRate()); 
            insertStmt.setString(SQLStatement.PARAM13,  line.getAgeRestrictedFlag()); 
            insertStmt.setString(SQLStatement.PARAM14,  line.getInheritFlag()); 
            insertStmt.setString(SQLStatement.PARAM15,  line.getSubSmallInt5()); 
            insertStmt.setString(SQLStatement.PARAM16, line.getUpdAppId());
            insertStmt.setString(SQLStatement.PARAM17, line.getUpdOpeCode());
            insertStmt.executeUpdate();
            
            connection.commit();
        } catch (SQLStatementException e) {
            rollBack(connection, "SQLStatementException:@" + functionName, e);           
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT, "Failed to create line\n "
                            + e.getMessage());
            throw new DaoException(functionName, e);
        } catch (SQLException e) {          
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to create line\n " + e.getMessage());
            if (Math.abs(SQLResultsConstants.ROW_DUPLICATE) != e.getErrorCode()) {
                rollBack(connection, "SQLException:@" + functionName, e);
            }
            throw new DaoException("SQLException: @" + functionName, e);
        } catch (Exception e) {
            rollBack(connection, "Exception:@" + functionName, e);
           
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to create line\n " + e.getMessage());           
            throw new DaoException("SQLException: @" + functionName, e);
        } finally {
            closeConnectionObjects(connection, insertStmt);
            
            tp.methodExit(resultBase);
        }
        return resultBase;
    }
    
    
    /**
     * {@inheritDoc}
     */
    public final ViewLine selectLineDetail(
            final String retailStoreID, final String department, final String line)
            throws DaoException {
    	
    	String functionName = "SQLServerLineDAO.selectLineDetail()";
        tp.methodEnter(functionName);
        tp.println("RetailStoreID", retailStoreID)
        		.println("Department",department)
        		.println("line",line);

        ViewLine lineModel = new ViewLine();

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;

		try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("select-line-details"));
            select.setString(SQLStatement.PARAM1, retailStoreID);
            select.setString(SQLStatement.PARAM2, department);
            select.setString(SQLStatement.PARAM3, line);

            result = select.executeQuery();
            if (result.next()) {
            	Line lineFound = new Line();
            	lineFound.setRetailStoreId(result.getString(result
                        .findColumn("StoreId")));    
            	lineFound.setLine(result.getString(result
                        .findColumn("Line")));             	
            	Description description = new Description();
                description
                        .setEn(result.getString(result.findColumn("LineName")));
                description.setJa(result.getString(result
                        .findColumn("LineNameLocal")));
                lineFound.setDescription(description);                
                lineFound.setDepartment(result.getString(result
                        .findColumn("Dpt"))); 
                lineFound.setTaxType(result.getString(result
                        .findColumn("TaxType")));
                lineFound.setTaxRate(result.getString(result
                        .findColumn("TaxRate")));
                lineFound.setDiscountType(result.getString(result
                        .findColumn("DiscountType")));
                lineFound.setExceptionFlag(result.getString(result
                        .findColumn("ExceptionFlag")));                
                lineFound.setDiscountFlag(result.getString(result.
                		findColumn("DiscountFlag")));
                lineFound.setDiscountAmount(result.getDouble(result.
                		findColumn("DiscountAmt")));
                lineFound.setDiscountRate(result.getDouble(result.
                		findColumn("DiscountRate")));             
                lineFound.setAgeRestrictedFlag(result.getString(result.
                		findColumn("AgeRestrictedFlag")));                
                lineFound.setInheritFlag(result.getString(result.
                		findColumn("InheritFlag")));
                lineFound.setSubSmallInt5(result.getString(result.
                		findColumn("SubSmallInt5")));  
            	lineModel.setLine(lineFound);
            } else {
                lineModel.setNCRWSSResultCode(ResultBase.RES_LINE_INFO_NOT_EXIST);
                tp.println("Line not found.");                
            }  
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT, "Failed to get the "
                            + "LIne Details.\n" + sqlStmtEx.getMessage());
            throw new DaoException("SQLStatementException:"
                    + " @selectLineDetail ", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(PROG_NAME,
                    functionName,
                    Logger.RES_EXCEP_SQL, "Failed to get the Line"
                            + "Details.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @selectLineDetail ",
                    sqlEx);
        } finally {
            closeConnectionObjects(connection, select, result);
            
            tp.methodExit(lineModel.toString());
        }
        return lineModel;
    }    
    
    /**
     * {@inheritDoc}
     */
	public ViewLine updateLine(String retailStoreId, String department,
			String lineid, Line line) throws DaoException {
		
	 	String functionName = "SQLServerLineDAO.updateLine";
        tp.methodEnter(functionName)
                .println("RetailStoreID", retailStoreId)
                .println("department", department)
                .println("lineid", lineid)
                .println("line", line);

        Connection connection = null;
        PreparedStatement updateStmt = null;
        ResultSet result = null;
        Line updatedLine = null;
        ViewLine viewLineResult = new ViewLine();
        
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateStmt = connection.prepareStatement(sqlStatement
                    .getProperty("update-line"));            
           
            String en = "";
            String ja = "";
            if (line.getDescription() != null) {
                en = line.getDescription().getEn() != null ? 
                		line.getDescription().getEn() : "";
                ja = line.getDescription().getJa() != null ? 
                		line.getDescription().getJa() : "";
            }
            
            updateStmt.setString(SQLStatement.PARAM1, line.getRetailStoreId());
            updateStmt.setString(SQLStatement.PARAM2, line.getLine());
            updateStmt.setString(SQLStatement.PARAM3, en);
            updateStmt.setString(SQLStatement.PARAM4, ja); 
            updateStmt.setString(SQLStatement.PARAM5, line.getDepartment());
            updateStmt.setString(SQLStatement.PARAM6, line.getTaxType());
            updateStmt.setString(SQLStatement.PARAM7, line.getTaxRate());
            updateStmt.setString(SQLStatement.PARAM8, line.getDiscountType());
            updateStmt.setString(SQLStatement.PARAM9, line.getExceptionFlag());
            updateStmt.setString(SQLStatement.PARAM10, line.getDiscountFlag());
            updateStmt.setDouble(SQLStatement.PARAM11, line.getDiscountAmount()); 
            updateStmt.setDouble(SQLStatement.PARAM12, line.getDiscountRate()); 
            updateStmt.setString(SQLStatement.PARAM13,  line.getAgeRestrictedFlag()); 
            updateStmt.setString(SQLStatement.PARAM14,  line.getInheritFlag()); 
            updateStmt.setString(SQLStatement.PARAM15,  line.getSubSmallInt5()); 
            updateStmt.setString(SQLStatement.PARAM16, line.getUpdAppId());
            updateStmt.setString(SQLStatement.PARAM17, line.getUpdOpeCode());
            updateStmt.setString(SQLStatement.PARAM18, retailStoreId);
            updateStmt.setString(SQLStatement.PARAM19, department);
            updateStmt.setString(SQLStatement.PARAM20, lineid);           
            
            result = updateStmt.executeQuery();
            if (result.next()) {
                updatedLine = new Line();
                updatedLine.setRetailStoreId(result.getString(result
                        .findColumn("StoreId")));    
            	updatedLine.setLine(result.getString(result
                        .findColumn("Line")));             	
            	Description description = new Description();
                description
                        .setEn(result.getString(result.findColumn("LineName")));
                description.setJa(result.getString(result
                        .findColumn("LineNameLocal")));
                updatedLine.setDescription(description);                
                updatedLine.setDepartment(result.getString(result
                        .findColumn("Dpt"))); 
                updatedLine.setTaxType(result.getString(result
                        .findColumn("TaxType")));
                updatedLine.setTaxRate(result.getString(result
                        .findColumn("TaxRate")));
                updatedLine.setDiscountType(result.getString(result
                        .findColumn("DiscountType")));
                updatedLine.setExceptionFlag(result.getString(result
                        .findColumn("ExceptionFlag")));                
                updatedLine.setDiscountFlag(result.getString(result.
                		findColumn("DiscountFlag")));
                updatedLine.setDiscountAmount(result.getDouble(result.
                		findColumn("DiscountAmt")));
                updatedLine.setDiscountRate(result.getDouble(result.
                		findColumn("DiscountRate")));             
                updatedLine.setAgeRestrictedFlag(result.getString(result.
                		findColumn("AgeRestrictedFlag")));                
                updatedLine.setInheritFlag(result.getString(result.
                		findColumn("InheritFlag")));
                updatedLine.setSubSmallInt5(result.getString(result.
                		findColumn("SubSmallInt5")));
                viewLineResult.setNCRWSSResultCode(ResultBase.RES_OK);
            } else {
            	viewLineResult.setNCRWSSResultCode(ResultBase.RES_LINE_INFO_NOT_UPDATED);
                tp.println("Line not updated.");
            }
            connection.commit();
        } catch (SQLStatementException e) {
            rollBack(connection, "SQLStatementException:@" + functionName, e);            
            LOGGER.logAlert(PROG_NAME, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT, "Failed to update line\n "
                            + e.getMessage());
            throw new DaoException(functionName, e);
        } catch (SQLException e) {            
            if (e.getErrorCode() != Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                rollBack(connection, "SQLException:@" + functionName, e);
            }           
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to update line\n " + e.getMessage());
            throw new DaoException("SQLException: @" + functionName, e);
        } catch (Exception e) {
            rollBack(connection, "Exception:@" + functionName, e);            
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to update line\n " + e.getMessage());
            throw new DaoException("SQLException: @" + functionName, e);
        } finally {
            closeConnectionObjects(connection, updateStmt, result);
            
            viewLineResult.setLine(updatedLine);
            tp.methodExit(viewLineResult);
        }
        return viewLineResult;
		
	}

    
}
