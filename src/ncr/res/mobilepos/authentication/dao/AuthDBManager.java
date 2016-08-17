package ncr.res.mobilepos.authentication.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;

/**
 * Manages Database requests for authentication class.
 *
 */
public class AuthDBManager extends AbstractDao {
    /**
     * the class instance of dbmanager.
     */
    private DBManager dbmngr;
    /**
     * the class instance of new connection.
     */
    private Connection newCon = null;
    /**
     * the class instance of new statement.
     */
    private PreparedStatement stmt = null;

    /**
     * constructor.
     * @throws DaoException - holds the exception that was thrown
     */
    public AuthDBManager() throws DaoException {
        dbmngr = JndiDBManagerMSSqlServer.getInstance();
    }

    /**
     * executes database updates.
     * @param queryString - the string to execute
     * @return int - how many rows where affected
     * @throws DaoException - holds the exception that was thrown
     */
    protected final int executeUpdate(final String queryString)
    throws DaoException {
        if (dbmngr == null) {
            return 0;
        }
        int retVal = 0;

        try {
            if (newCon == null || newCon.isClosed()) {
                newCon = dbmngr.getConnection();
            }

            stmt = newCon.prepareStatement(queryString);

            retVal = stmt.executeUpdate();
            newCon.commit();
        } catch (SQLException sqlExcp) {
            throw new DaoException("There is an Error in the SQL", sqlExcp);
        }
        return retVal;
    }

    /**
     * execute database updates.
     * @param statement - the statement to be executed
     * @param params - the values to replace in the statement variable
     * @return int - number of rows affected
     * @throws DaoException - holds the exception that was thrown
     */
    protected final int executeUpdate(final String statement,
            final String ... params) throws DaoException {
        if (dbmngr == null) {
            return 0;
        }
        int retVal = 0;

        try {
            if (newCon == null || newCon.isClosed()) {
                newCon = dbmngr.getConnection();
            }

            stmt = newCon.prepareStatement(statement);

            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }

            retVal = stmt.executeUpdate();
            newCon.commit();
        } catch (SQLException sqlExcp) {
            throw new DaoException("There is an Error in the SQL", sqlExcp);
        }
        return retVal;
    }

    /**
     * executes database queries.
     * @param queryString - the string to execute
     * @return int - how many rows where affected
     * @throws DaoException - holds the exception that was thrown
     */
    protected final ResultSet executeQuery(final String queryString)
    throws DaoException {
        if (dbmngr == null) {
            return null;
        }
        ResultSet retVal = null;

        try {
            if (newCon == null || newCon.isClosed()) {
                newCon = dbmngr.getConnection();
            }

            stmt = newCon.prepareStatement(queryString);

            retVal = stmt.executeQuery();
        } catch (SQLException sqlExcp) {
            throw new DaoException("There is an Error in the SQL", sqlExcp);
        }

        return retVal;
    }

    /**
     * executes database queries.
     * @param statement - the string to execute
     * @param params - the values to replace in the statement variable
     * @return int - how many rows where affected
     * @throws DaoException - holds the exception that was thrown
     */
    protected final ResultSet executeQuery(final String statement,
            final String ... params) throws DaoException {
        if (dbmngr == null) {
            return null;
        }
        ResultSet retVal = null;

        try {
            if (newCon == null || newCon.isClosed()) {
                newCon = dbmngr.getConnection();
            }

            stmt = newCon.prepareStatement(statement);

            for (int i = 0; i < params.length; i++) {
                stmt.setString(i + 1, params[i]);
            }

            retVal = stmt.executeQuery();
        } catch (SQLException sqlExcp) {
            throw new DaoException("There is an Error in the SQL", sqlExcp);
        }

        return retVal;
    }

    /**
     * closes connections.
     * @throws DaoException - holds the exception that was thrown
     */
    public final void connectionClose() throws DaoException {

        try {
            if ((null != newCon) && !newCon.isClosed()) {
                closeConnectionObjects(newCon, stmt);
            }
        } catch (SQLException sqlExcp) {
            throw new DaoException("There is an Error in the SQL", sqlExcp);
        }
    }
}
