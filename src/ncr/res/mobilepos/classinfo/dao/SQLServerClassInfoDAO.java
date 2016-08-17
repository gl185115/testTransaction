/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerClassInfoDAO
 *
 * DAO which handles the operations in the table specific for Classes
 *
 * Romares, Sul
 */

package ncr.res.mobilepos.classinfo.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.classinfo.model.ClassInfo;
import ncr.res.mobilepos.classinfo.model.ViewClassInfo;
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
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.model.Description;
import ncr.res.mobilepos.property.SQLStatement;

/**
 * A Data Access Object implementation for Classes.
 * 
 * @see IClassInfoDAO
 */

public class SQLServerClassInfoDAO extends AbstractDao implements IClassInfoDAO {
    /**
     * The Database Manager of the class.
     */
    private DBManager dbManager;
    /** A private member variable used for logging the class implementations. */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get the Logger
    /**
     * The Program name.
     */
    private String progname = "ClassInfoDAO";    
    /**
     * The Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * Default Constructor for SQLServerClassInfoDAO
     * 
     * <P>
     * Sets DBManager for database connection, and Logger for logging.
     * 
     * @throws DaoException
     *             The exception thrown when the constructor fails.
     */
    public SQLServerClassInfoDAO() throws DaoException {
        this.dbManager = JndiDBManagerMSSqlServer.getInstance();
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
    }
    /**
     * Gets the Database Manager for SQLServerClassInfoDAO.
     * 
     * @return Returns a DBManager Instance.
     */
    public final DBManager getDbManager() {
        return dbManager;
    }    
    
    /**
     * {@inheritDoc}
     */    
    public final List<ClassInfo> listClasses(final String storeId, final String department,
            final String key, final String name, final int limit) throws DaoException {

        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("storeId", storeId)
                .println("department", department)
                .println("key", key)
                .println("limit", limit);
       
        List<ClassInfo> classInfoList = new ArrayList<ClassInfo>();
        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("get-class-list"));            
            int searchLimit = (limit == 0) ? GlobalConstant
                    .getMaxSearchResults() : limit;            
            select.setInt(SQLStatement.PARAM1, searchLimit);
            select.setString(SQLStatement.PARAM2, storeId);
            select.setString(SQLStatement.PARAM3, department);
            select.setString(SQLStatement.PARAM4, StringUtility.isNullOrEmpty(key)? null : StringUtility.escapeCharatersForSQLqueries(key) + '%');
            select.setString(SQLStatement.PARAM5, StringUtility.isNullOrEmpty(name)? null : '%' + StringUtility.escapeCharatersForSQLqueries(name) + '%' );
            result = select.executeQuery();

            while (result.next()) {
            	ClassInfo classInfo = new ClassInfo();                

            	classInfo.setRetailStoreId(result.getString(result
                        .findColumn("StoreId")));    
            	classInfo.setItemClass(result.getString(result
                        .findColumn("Class"))); 
            	Description description = new Description();
                description.setEn(result.getString(result.
                		findColumn("ClassName")));
                description.setJa(result.getString(result
                        .findColumn("ClassNameLocal")));
                classInfo.setDescription(description);                
                classInfo.setDepartment(result.getString(result
                        .findColumn("Dpt")));                
                classInfo.setLine(result.getString(result
                        .findColumn("Line")));
                classInfo.setTaxType(result.getString(result
                        .findColumn("TaxType")));
                classInfo.setTaxRate(result.getString(result
                        .findColumn("TaxRate")));
                classInfo.setDiscountType(result.getString(result
                        .findColumn("DiscountType")));
                classInfo.setExceptionFlag(result.getString(result
                        .findColumn("ExceptionFlag")));              
                classInfo.setDiscountFlag(result.getString(result.
                		findColumn("DiscountFlag")));                
                classInfo.setDiscountAmount(result.getDouble(result.
                		findColumn("DiscountAmt")));
                classInfo.setDiscountRate(result.getDouble(result.
                		findColumn("DiscountRate")));       
                classInfo.setAgeRestrictedFlag(result.getString(result.
                		findColumn("AgeRestrictedFlag")));                
                classInfo.setInheritFlag(result.getString(result.
                		findColumn("InheritFlag")));
                classInfo.setSubSmallInt5(result.getString(result
                        .findColumn("SubSmallInt5")));                
                classInfoList.add(classInfo);
            }

        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(progname, "SQLServerClassInfoDAO.listClasses()",
                    Logger.RES_EXCEP_SQLSTATEMENT, "Failed to get the classes.\n"
                            + sqlStmtEx.getMessage());
            throw new DaoException("SQLStatementException: @listClasses ",
                    sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progname, "SQLServerClassInfoDAO.listClasses()",
                    Logger.RES_EXCEP_SQL,
                    "Failed to get the classes.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @listClasses ", sqlEx);
        } finally {
            closeConnectionObjects(connection, select, result);

            tp.methodExit("Classes count: " + classInfoList.size());
        }

        return classInfoList;
    }
    
    /**
     * {@inheritDoc}
     */ 
    @Override
    public final ResultBase deleteClass(final String retailStoreID,	final String department,
    		final String line, final String itemClass) throws DaoException {

        tp.methodEnter(DebugLogger.getCurrentMethodName())
        	.println("retailStoreID", retailStoreID)
            .println("department", department)
            .println("line", line)
            .println("class", itemClass);

        Connection connection = null;
        PreparedStatement deleteStmt = null;
        int result = 0;
        ResultBase resultBase = new ResultBase();

        try {
            connection = dbManager.getConnection();

            SQLStatement sqlStatement = SQLStatement.getInstance();
            deleteStmt = connection.prepareStatement(sqlStatement
                    .getProperty("delete-class"));
            setValues(deleteStmt, retailStoreID, department, line, itemClass);
            
            result = deleteStmt.executeUpdate();
            if (result == 0) {
                resultBase.setNCRWSSResultCode(ResultBase.RES_CLASS_INFO_NOT_EXIST);
                tp.println("No class info was deleted.");
            }
            connection.commit();
        } catch (SQLStatementException e) {
            rollBack(connection,
                    "SQLStatementException:@SQLServerClassInfoDAO.deleteClass", e);
            LOGGER.logAlert(progname, "SQLServerClassInfoDAO.deleteClass",
                    Logger.RES_EXCEP_SQLSTATEMENT, "Failed to delete class info\n "
                            + e.getMessage());
            throw new DaoException("SQLServerClassInfoDAO: @deleteClass ", e);
        } catch (SQLException e) {
            rollBack(connection, "SQLException:@SQLServerClassInfoDAO.deleteClass", e);
            LOGGER.logAlert(progname, "SSQLServerClassInfoDAO.deleteClass",
                    Logger.RES_EXCEP_SQL,
                    "Failed to delete class info\n " + e.getMessage());
            throw new DaoException(
                    "SQLException: @SQLServerClassInfoDAO.deleteClass ", e);
        } catch (Exception e) {
            rollBack(connection, "Exception:@SQLServerClassInfoDAO.deleteClass", e);
            LOGGER.logAlert(progname, "SQLServerClassInfoDAO.deleteClass",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to delete class info\n " + e.getMessage());
            throw new DaoException(
                    "SQLException: @SQLServerClassInfoDAO.deleteClass ", e);
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
    public final ResultBase createClassInfo(final ClassInfo classInfo) throws DaoException {

        String functionName = "SQLServerClassInfoDAO.createClassInfo";
        tp.methodEnter(DebugLogger.getCurrentMethodName())               
                .println("ClassInfo", classInfo);
        
        ResultBase resultBase = new ResultBase();        
        if(classInfo == null){
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
                    .getProperty("insert-class"));
            
            String en = "";
            String ja = "";
            if (classInfo.getDescription() != null) {
                en = classInfo.getDescription().getEn() != null ? 
                		classInfo.getDescription().getEn() : "";
                ja = classInfo.getDescription().getJa() != null ? 
                		classInfo.getDescription().getJa() : "";
            }                                  
            insertStmt.setString(SQLStatement.PARAM1, classInfo.getRetailStoreId());
            insertStmt.setString(SQLStatement.PARAM2, classInfo.getItemClass());
            insertStmt.setString(SQLStatement.PARAM3, en);
            insertStmt.setString(SQLStatement.PARAM4, ja); 
            insertStmt.setString(SQLStatement.PARAM5, classInfo.getDepartment());
            insertStmt.setString(SQLStatement.PARAM6, classInfo.getLine());
            insertStmt.setString(SQLStatement.PARAM7, classInfo.getTaxType());
            insertStmt.setString(SQLStatement.PARAM8, classInfo.getTaxRate());
            insertStmt.setString(SQLStatement.PARAM9, classInfo.getDiscountType());
            insertStmt.setString(SQLStatement.PARAM10, classInfo.getExceptionFlag());
            insertStmt.setString(SQLStatement.PARAM11, classInfo.getDiscountFlag());
            insertStmt.setDouble(SQLStatement.PARAM12, classInfo.getDiscountAmount()); 
            insertStmt.setDouble(SQLStatement.PARAM13, classInfo.getDiscountRate()); 
            insertStmt.setString(SQLStatement.PARAM14,  classInfo.getAgeRestrictedFlag()); 
            insertStmt.setString(SQLStatement.PARAM15,  classInfo.getInheritFlag()); 
            insertStmt.setString(SQLStatement.PARAM16,  classInfo.getSubSmallInt5()); 
            insertStmt.setString(SQLStatement.PARAM17, classInfo.getUpdAppId());
            insertStmt.setString(SQLStatement.PARAM18, classInfo.getUpdOpeCode());
            insertStmt.executeUpdate();
            
            connection.commit();
        } catch (SQLStatementException e) {
            rollBack(connection, "SQLStatementException:@" + functionName, e);
            
            LOGGER.logAlert(progname, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT, "Failed to create classInfo\n "
                            + e.getMessage());
            throw new DaoException(functionName, e);
        } catch (SQLException e) {
            
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to create classInfo\n " + e.getMessage());
            if (Math.abs(SQLResultsConstants.ROW_DUPLICATE) != e.getErrorCode()) {
                rollBack(connection, "SQLException:@" + functionName, e);
            }
            throw new DaoException("SQLException: @" + functionName, e);
        } catch (Exception e) {
            rollBack(connection, "Exception:@" + functionName, e);
            
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to create classInfo\n " + e.getMessage());
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
    public final ViewClassInfo selectClassInfoDetail(
            final String retailStoreID, final String department, final String line, final String itemClass)
            throws DaoException {
        tp.methodEnter("selectClassInfoDetail");
        tp.println("RetailStoreID", retailStoreID)
        		.println("Department",department)
        		.println("line",line)
        		.println("class",itemClass);

        ViewClassInfo classInfoModel = new ViewClassInfo();

        Connection connection = null;
        PreparedStatement select = null;
        ResultSet result = null;

        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            select = connection.prepareStatement(sqlStatement
                    .getProperty("select-class-details"));
            select.setString(SQLStatement.PARAM1, retailStoreID);
            select.setString(SQLStatement.PARAM2, department);
            select.setString(SQLStatement.PARAM3, line);
            select.setString(SQLStatement.PARAM4, itemClass);

            result = select.executeQuery();
            if (result.next()) {
            	ClassInfo classInfo = new ClassInfo();  
            	classInfo.setRetailStoreId(result.getString(result
                        .findColumn("StoreId")));    
            	classInfo.setItemClass(result.getString(result
                        .findColumn("Class"))); 
            	Description description = new Description();
                description.setEn(result.getString(result.
                		findColumn("ClassName")));
                description.setJa(result.getString(result
                        .findColumn("ClassNameLocal")));
                classInfo.setDescription(description);                
                classInfo.setDepartment(result.getString(result
                        .findColumn("Dpt")));                
                classInfo.setLine(result.getString(result
                        .findColumn("Line")));
                classInfo.setTaxType(result.getString(result
                        .findColumn("TaxType")));
                classInfo.setTaxRate(result.getString(result
                        .findColumn("TaxRate")));
                classInfo.setDiscountType(result.getString(result
                        .findColumn("DiscountType")));
                classInfo.setExceptionFlag(result.getString(result
                        .findColumn("ExceptionFlag")));              
                classInfo.setDiscountFlag(result.getString(result.
                		findColumn("DiscountFlag")));                
                classInfo.setDiscountAmount(result.getDouble(result.
                		findColumn("DiscountAmt")));
                classInfo.setDiscountRate(result.getDouble(result.
                		findColumn("DiscountRate")));       
                classInfo.setAgeRestrictedFlag(result.getString(result.
                		findColumn("AgeRestrictedFlag")));                
                classInfo.setInheritFlag(result.getString(result.
                		findColumn("InheritFlag")));
                classInfo.setSubSmallInt5(result.getString(result
                        .findColumn("SubSmallInt5")));            	
            	classInfoModel.setClassInfo(classInfo);
            } else {
                classInfoModel.setNCRWSSResultCode(ResultBase.RES_CLASS_INFO_NOT_EXIST);
                tp.println("Class not updated.");                
            }  
        } catch (SQLStatementException sqlStmtEx) {
            LOGGER.logAlert(progname,
                    "SQLServerClassInfoDAO.selectClassInfoDetail()",
                    Logger.RES_EXCEP_SQLSTATEMENT, "Failed to get the "
                            + "ClassInfo Details.\n" + sqlStmtEx.getMessage());
            throw new DaoException("SQLStatementException:"
                    + " @selectClassInfoDetail ", sqlStmtEx);
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progname,
                    "SQLServerClassInfoDAO.selectClassInfoDetail()",
                    Logger.RES_EXCEP_SQL, "Failed to get the ClassInfo"
                            + "Details.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @selectClassInfoDetail ",
                    sqlEx);
        } finally {
            closeConnectionObjects(connection, select, result);
            
            tp.methodExit(classInfoModel.toString());
        }
        return classInfoModel;
    } 
    
    /**
     * {@inheritDoc}
     */
	public ViewClassInfo updateClassInfo(String retailStoreId, String department,
			String lineid, String itemClass, ClassInfo classInfo) throws DaoException {
		
	 	String functionName = "SQLServerClassInfoDAO.updateClassInfo";
        tp.methodEnter(DebugLogger.getCurrentMethodName())
                .println("RetailStoreID", retailStoreId)
                .println("department", department)
                .println("lineid", lineid)
                .println("itemClass", itemClass)
                .println("class", classInfo);

        Connection connection = null;
        PreparedStatement updateStmt = null;
        ResultSet result = null;
        ClassInfo updatedClassInfo = null;
        ViewClassInfo viewClassInfoResult = new ViewClassInfo();
        
        try {
            connection = dbManager.getConnection();
            SQLStatement sqlStatement = SQLStatement.getInstance();
            updateStmt = connection.prepareStatement(sqlStatement
                    .getProperty("update-class"));            
           
            String en = "";
            String ja = "";
            if (classInfo.getDescription() != null) {
                en = classInfo.getDescription().getEn() != null ? 
                		classInfo.getDescription().getEn() : "";
                ja = classInfo.getDescription().getJa() != null ? 
                		classInfo.getDescription().getJa() : "";
            }
            
            updateStmt.setString(SQLStatement.PARAM1, classInfo.getRetailStoreId());
            updateStmt.setString(SQLStatement.PARAM2, classInfo.getItemClass());            
            updateStmt.setString(SQLStatement.PARAM3, en); 
            updateStmt.setString(SQLStatement.PARAM4, ja); 
            updateStmt.setString(SQLStatement.PARAM5, classInfo.getDepartment());
            updateStmt.setString(SQLStatement.PARAM6, classInfo.getLine());
            updateStmt.setString(SQLStatement.PARAM7, classInfo.getTaxType());
            updateStmt.setString(SQLStatement.PARAM8, classInfo.getTaxRate());
            updateStmt.setString(SQLStatement.PARAM9, classInfo.getDiscountType());
            updateStmt.setString(SQLStatement.PARAM10, classInfo.getExceptionFlag());
            updateStmt.setString(SQLStatement.PARAM11, classInfo.getDiscountFlag());
            updateStmt.setDouble(SQLStatement.PARAM12, classInfo.getDiscountAmount()); 
            updateStmt.setDouble(SQLStatement.PARAM13, classInfo.getDiscountRate()); 
            updateStmt.setString(SQLStatement.PARAM14,  classInfo.getAgeRestrictedFlag()); 
            updateStmt.setString(SQLStatement.PARAM15,  classInfo.getInheritFlag()); 
            updateStmt.setString(SQLStatement.PARAM16,  classInfo.getSubSmallInt5()); 
            updateStmt.setString(SQLStatement.PARAM17, classInfo.getUpdAppId());
            updateStmt.setString(SQLStatement.PARAM18, classInfo.getUpdOpeCode());
            updateStmt.setString(SQLStatement.PARAM19, retailStoreId);
            updateStmt.setString(SQLStatement.PARAM20, department);
            updateStmt.setString(SQLStatement.PARAM21, lineid);           
            updateStmt.setString(SQLStatement.PARAM22, itemClass);           
            
            result = updateStmt.executeQuery();
            if (result.next()) {
                updatedClassInfo = new ClassInfo();
                updatedClassInfo.setRetailStoreId(result.getString(result
                        .findColumn("StoreId"))); 
                updatedClassInfo.setItemClass(result.getString(result
                        .findColumn("Class")));             	            	
            	Description description = new Description();
                description
                        .setEn(result.getString(result.findColumn("ClassName")));
                description.setJa(result.getString(result
                        .findColumn("ClassNameLocal")));
                updatedClassInfo.setDescription(description);                
                updatedClassInfo.setDepartment(result.getString(result
                        .findColumn("Dpt"))); 
                updatedClassInfo.setLine(result.getString(result
                        .findColumn("Line"))); 
                updatedClassInfo.setTaxType(result.getString(result
                        .findColumn("TaxType")));
                updatedClassInfo.setTaxRate(result.getString(result
                        .findColumn("TaxRate")));
                updatedClassInfo.setDiscountType(result.getString(result
                        .findColumn("DiscountType")));
                updatedClassInfo.setExceptionFlag(result.getString(result
                        .findColumn("ExceptionFlag")));                
                updatedClassInfo.setDiscountFlag(result.getString(result.
                		findColumn("DiscountFlag")));
                updatedClassInfo.setDiscountAmount(result.getDouble(result.
                		findColumn("DiscountAmt")));
                updatedClassInfo.setDiscountRate(result.getDouble(result.
                		findColumn("DiscountRate")));             
                updatedClassInfo.setAgeRestrictedFlag(result.getString(result.
                		findColumn("AgeRestrictedFlag")));                
                updatedClassInfo.setInheritFlag(result.getString(result.
                		findColumn("InheritFlag")));
                updatedClassInfo.setSubSmallInt5(result.getString(result.
                		findColumn("SubSmallInt5")));
                viewClassInfoResult.setNCRWSSResultCode(ResultBase.RES_OK);
            } else {
            	viewClassInfoResult.setNCRWSSResultCode(ResultBase.RES_CLASS_INFO_NOT_UPDATED);
                tp.println("Class not updated.");
            }
            connection.commit();
        } catch (SQLStatementException e) {
            rollBack(connection, "SQLStatementException:@" + functionName, e);            
            LOGGER.logAlert(progname, functionName,
                    Logger.RES_EXCEP_SQLSTATEMENT, "Failed to update classinfo\n "
                            + e.getMessage());
            throw new DaoException(functionName, e);
        } catch (SQLException e) {            
            if (e.getErrorCode() != Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                rollBack(connection, "SQLException:@" + functionName, e);
            }           
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to update classinfo\n " + e.getMessage());
            throw new DaoException("SQLException: @" + functionName, e);
        } catch (Exception e) {
            rollBack(connection, "Exception:@" + functionName, e);
            
            LOGGER.logAlert(progname, functionName, Logger.RES_EXCEP_GENERAL,
                    "Failed to update classinfo\n " + e.getMessage());
            throw new DaoException("SQLException: @" + functionName, e);
        } finally {
            closeConnectionObjects(connection, updateStmt, result);

            viewClassInfoResult.setClassInfo(updatedClassInfo);
            tp.methodExit(viewClassInfoResult);
        }
        return viewClassInfoResult;
		
	}
    
}
