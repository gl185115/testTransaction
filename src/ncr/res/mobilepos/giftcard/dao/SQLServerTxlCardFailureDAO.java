/**
 * Copyright (c) 2015 NCR Japan Ltd. 
 */
package ncr.res.mobilepos.giftcard.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.realgate.util.Snap;
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

    static final String PROGNAME = "CARDFDAO";

    public SQLServerTxlCardFailureDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
        snap = (SnapLogger)SnapLogger.getInstance();
        logger = (Logger) Logger.getInstance();
    }

    /**
     * Gets the Database Manager.
     * this method is overridable for testing.
     * @return instance of DBManager
     */
    public DBManager getDbManager() {
        return dbManager;
    }

    public void storeRequest(Message request) throws SQLException {
        Connection c = getDbManager().getConnection();
        try (DataBaseConnection dbc = new DataBaseConnection(c)) {
            BatchTxlCardFailureDAO dao = new BatchTxlCardFailureDAO(dbc, logger, snap);
            dao.storeRequest(request);
            c.commit();
        } catch (SQLException e) {
            c.rollback();
            throw e;
        } catch (Exception e) {
            logger.logSnapException(PROGNAME, Logger.RES_EXCEP_GENERAL,
                                    "error on storeRequest", e);
            c.rollback();
        }
    }

    public void updateStore(int id, int status, Message response) throws SQLException {
        Connection c = getDbManager().getConnection();
        try (DataBaseConnection dbc = new DataBaseConnection(c)) {
            BatchTxlCardFailureDAO dao = new BatchTxlCardFailureDAO(dbc, logger, snap);
            dao.updateStore(id, status, response);
            c.commit();
        } catch (SQLException e) {
            c.rollback();
            throw e;
        } catch (Exception e) {
            logger.logSnapException(PROGNAME, Logger.RES_EXCEP_GENERAL,
                                    "error on storeRequest", e);
            c.rollback();
        }
    }

    public int readUnsent(Message message) throws SQLException {
        Connection c = getDbManager().getConnection();
        try (DataBaseConnection dbc = new DataBaseConnection(c)) {
            BatchTxlCardFailureDAO dao = new BatchTxlCardFailureDAO(dbc, logger, snap);
            return dao.readUnsent(message);
        } catch (Exception e) {
            logger.logSnapException(PROGNAME, Logger.RES_EXCEP_GENERAL, 
                                    "error on storeRequest", e);
            c.rollback();
        }
        return NO_RECORD;
    }

    DBManager dbManager;
    BatchTxlCardFailureDAO dao;
    Trace.Printer tp;
    SnapLogger snap;
    Logger logger;
}
