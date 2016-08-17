// Copyright (c) 2015 NCR Japan Ltd.
package ncr.res.mobilepos.offlinecredit.dao;

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

public class SQLServerOfflineCreditDAO extends AbstractDao implements IOfflineCreditDAO {
    static final String SAVE_STATEMENT = "insert-offline-credit";

    static final String PROGNAME = "SQLOFFCRD";

    static final Logger LOGGER = (Logger) Logger.getInstance();

    /** The Database Manager. */
    DBManager dbManager;

    public SQLServerOfflineCreditDAO() throws DaoException {
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
                int sequenceNumber, String businessDayDate, byte[] iv, byte[] data) throws Exception {
        try (Connection c = dbManager.getConnection();
             PreparedStatement ps = c.prepareStatement(SQLStatement.getInstance().getProperty(SAVE_STATEMENT))) {
            int i = 1;
            ps.setString(i++, companyId);
            ps.setString(i++, retailStoreId);
            ps.setString(i++, workstationId);
            ps.setInt(i++, sequenceNumber);
            ps.setString(i++, businessDayDate);
            ps.setInt(i++, training);
            ps.setBytes(i++, iv);
            ps.setBytes(i++, data);
            int updateCount = ps.executeUpdate();
            c.commit();
            return 1 == updateCount;
        } catch (SQLException e) {
            if (e.getErrorCode() == Math.abs(SQLResultsConstants.ROW_DUPLICATE)) {
                LOGGER.logBytes(PROGNAME, "OF", 
                                "duplicate row:" + companyId + "/" + retailStoreId + "/" + 
                                workstationId + "/" + training + "/" + sequenceNumber + "/" + 
                                businessDayDate, data);
            } else {
                throw e;
            }
        }
        return true;
    }

    @Override
    public void close() {
    }
}

