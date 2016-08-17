// Copyright (c) 2015 NCR Japan Ltd.
package ncr.res.mobilepos.eventlog.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.res.mobilepos.constant.SQLResultsConstants;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;

public class SQLServerEventLogDAO extends AbstractDao implements IEventLogDAO {
    static final String SAVE_STATEMENT = "insert-eventlog";
    static final String GET_STATEMENT = "select-eventlog";

    static final String PROGNAME = "SQLEVLOG";

    static final Logger LOGGER = (Logger) Logger.getInstance();

    /** The Database Manager. */
    DBManager dbManager;

    public SQLServerEventLogDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
    }

    /**
     * Gets the Database Manager for the Class.
     *
     * @return    The Database Manager Object
     */
    public DBManager getDbManager() {
        return dbManager;
    }

    @Override
    public boolean save(String companyId, String retailStoreId, String workstationId, int training,
                     int sequenceNumber, int child, String businessDayDate, String eventLog) throws Exception {
        try (Connection c = dbManager.getConnection();
             PreparedStatement ps = c.prepareStatement(SQLStatement.getInstance().getProperty(SAVE_STATEMENT))) {
            int i = 1;
            ps.setString(i++, companyId);
            ps.setString(i++, retailStoreId);
            ps.setString(i++, workstationId);
            ps.setInt(i++, training);
            ps.setInt(i++, sequenceNumber);
            ps.setInt(i++, child);
            ps.setString(i++, businessDayDate);
            ps.setString(i++, eventLog);
            int updateCount = ps.executeUpdate();
            c.commit();
            return 1 == updateCount;
        } catch (SQLException e) {
            if (e.getErrorCode() == Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                LOGGER.logTransaction(PROGNAME, "EV", 
                                      "duplicate event:" + companyId + "/" + retailStoreId + "/" + 
                                      workstationId + "/" + training + "/" + sequenceNumber + "/" + 
                                      child + "/" + businessDayDate,
                                      eventLog);
                return true;
            } else {
                throw e;
            }
        }
    }

    @Override
    public String load(String companyId, String retailStoreId, String workstationId, int training, 
                       int sequenceNumber) throws Exception {
        try (Connection c = dbManager.getConnection();
             PreparedStatement ps = c.prepareStatement(SQLStatement.getInstance().getProperty(GET_STATEMENT))) {
            int i = 1;
            ps.setString(i++, companyId);
            ps.setString(i++, retailStoreId);
            ps.setString(i++, workstationId);
            ps.setInt(i++, training);
            ps.setInt(i++, sequenceNumber);
            try (ResultSet rs = ps.executeQuery()) {
                StringBuilder sb = new StringBuilder();
                while (rs.next()) {
                    sb.append(rs.getString(1));
                }
                return sb.toString();
            }
        }
    }

    @Override
    public void close() {
    }

}
