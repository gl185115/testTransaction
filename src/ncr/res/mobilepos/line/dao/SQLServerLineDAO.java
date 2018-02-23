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
            	line.setRetailStoreId(retailstoreid);    
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
                
                lineList.add(line);
            }
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
    
}
