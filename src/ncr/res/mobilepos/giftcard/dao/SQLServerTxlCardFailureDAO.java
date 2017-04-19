/**
 * Copyright (c) 2015 NCR Japan Ltd. 
 */
package ncr.res.mobilepos.giftcard.dao;

import java.sql.Connection;
import java.sql.SQLException;

import ncr.realgate.util.Trace;

import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.SnapLogger;

import ncr.res.giftcard.toppan.dao.BatchTxlCardFailureDAO;
import ncr.res.giftcard.toppan.dao.ITxlCardFailureDAO;
import ncr.res.giftcard.toppan.dao.DataBaseConnection;
import ncr.res.giftcard.toppan.model.Message;

/**
 * SQL Server implementation of TXL_CARD_FAILURE 
 */
public class SQLServerTxlCardFailureDAO extends AbstractDao implements ITxlCardFailureDAO {

    /**
     * The Program Name.
     */
    private static final String PROG_NAME = "CARDFDAO";
    
    /** 
     * The database manager. 
     */
    private DBManager dbManager = null;
    
    /** 
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    
    /** 
     * The instance of the trace debug printer. 
     */
    private Trace.Printer tp = null;

    /** 
     * The instance of the SnapLogger. 
     */
    private SnapLogger snap = null;

    /**
     * The Constructor of the Class.
     *
     * @throws DaoException
     *             thrown when process fails.
     */
    public SQLServerTxlCardFailureDAO() {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
        snap = (SnapLogger)SnapLogger.getInstance();
    }

    /**
     * Gets the Database Manager for the Class.
     *
     * @return The Database Manager Object
     */
    public DBManager getDbManager() {
        return dbManager;
    }

    public void storeRequest(Message request) throws SQLException {
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("Message", request.toString());
        
        Connection conn = null;
        try {
        	conn = this.getDbManager().getConnection();
        	DataBaseConnection dbc = new DataBaseConnection(conn);
            BatchTxlCardFailureDAO dao = new BatchTxlCardFailureDAO(dbc, LOGGER, snap);
            dao.storeRequest(request);
            conn.commit();
        } catch (SQLException e) {
        	conn.rollback();
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to storeRequest.\n" + e.getMessage());
            throw e;
        } catch (Exception e) {
        	conn.rollback();
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
        } finally {
        	if (conn != null) {
                try {
                	conn.close();
                } catch (SQLException e) {
                	throw e;
                } finally {
                	conn = null;
                }
            }
            tp.methodExit();
        }
    }

    public void updateStore(int id, int status, Message response) throws SQLException {
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("id", String.valueOf(id));
        tp.println("status", String.valueOf(status));
        tp.println("response", response.toString());
        
        Connection conn = null;
        try {
        	conn = this.getDbManager().getConnection();
        	DataBaseConnection dbc = new DataBaseConnection(conn);
            BatchTxlCardFailureDAO dao = new BatchTxlCardFailureDAO(dbc, LOGGER, snap);
            dao.updateStore(id, status, response);
            conn.commit();
        } catch (SQLException e) {
        	conn.rollback();
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_SQL,
                    "Failed to updateStore.\n" + e.getMessage());
            throw e;
        } catch (Exception e) {
        	conn.rollback();
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
        } finally {
        	if (conn != null) {
                try {
                	conn.close();
                } catch (SQLException e) {
                	throw e;
                } finally {
                	conn = null;
                }
            }
            tp.methodExit();
        }
    }

    public int readUnsent(Message message) throws SQLException {
    	String functionName = DebugLogger.getCurrentMethodName();
        tp.methodEnter(functionName);
        tp.println("Message", message.toString());
        
        Connection conn = null;
        try {
        	conn = this.getDbManager().getConnection();
        	DataBaseConnection dbc = new DataBaseConnection(conn);
            BatchTxlCardFailureDAO dao = new BatchTxlCardFailureDAO(dbc, LOGGER, snap);
            return dao.readUnsent(message);
        } catch (Exception e) {
        	conn.rollback();
            LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_GENERAL,
                    e.getMessage());
        } finally {
        	if (conn != null) {
                try {
                	conn.close();
                } catch (SQLException e) {
                	throw e;
                } finally {
                	conn = null;
                }
            }
            tp.methodExit();
        }
        
        return NO_RECORD;
    }
}
