/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * AbstractDao
 *
 * AbstractDao which handles the closing of sql connection,
 *  preparedstatement and resultset.
 *
 * Dela Cerna, Jessel
 * Carlos G. Campos
 */

package ncr.res.mobilepos.daofactory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import ncr.res.mobilepos.exception.DaoException;

/**
 * AbstractDao is an abstract class that represents the DAO implementations in
 * general.
 *
 */
public abstract class AbstractDao {

    /**
     * Calling the Rollback implementation for the connection.
     *
     * @param conn - a connection object tot rollback
     * @param method - a string
     * @param ex - an exception object
     * @throws DaoException - thrown when an exception occurs
     */
    public final void rollBack(
            final Connection conn, final String method, final Exception ex)
            throws DaoException {
        if (null == conn) {
            return;
        }

        try {
            conn.rollback();
        } catch (SQLException e) {
            throw new DaoException("SQLException:"
                    + " Error on calling Rollback", e);
        }
    }

    /**
     * Closes the ResultSet, Prepared Statement and Connection Afterwards.
     *
     * @param connection
     *            The Connection Object to be closed
     * @param preparedStatement
     *            The Prepared Statement to be closed
     * @return true if successful, false if not
     * @throws DaoException - thrown when an exception occurs
     */
    public final boolean closeConnectionObjects(final Connection connection,
            final PreparedStatement preparedStatement) throws DaoException {
        return closeConnectionObjects(connection, preparedStatement, null);
    }

    /**
     * Method called to close a Connection Object.
     *
     * @param connection
     *            The Connection to be closed
     * @return Return true if closing of the connection is successful, else
     *         false when closing of the connection failed.
     */
    public final boolean closeObject(Connection connection) {
        boolean hasErrorOccur = false;

        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                hasErrorOccur = true;
            } finally {
                connection = null;
            }
        }

        return hasErrorOccur;

    }

    /**
     * Method called to close a ResultSet Object.
     *
     * @param resultSet
     *            The ResultSet to be closed
     * @return Return true if closing of the resultSet is successful, else false
     *         when closing of the resultSet failed.
     */
    public final boolean closeObject(ResultSet resultSet) {
        boolean hasErrorOccur = false;

        if (resultSet != null) {
            try {
                resultSet.close();
            } catch (SQLException e) {
                hasErrorOccur = true;
            } finally {
                resultSet = null;
            }
        }

        return hasErrorOccur;
    }

    /**
     * Method called to close a PreparedStatement Object.
     *
     * @param preparedStatement
     *            The PreparedStatement to be closed
     * @return Return true if closing of the preparedStatement is successful,
     *         else false when closing of the preparedStatement failed.
     */
    public final boolean closeObject(PreparedStatement preparedStatement) {
        boolean hasErrorOccur = false;

        if (preparedStatement != null) {
            try {
                preparedStatement.close();
            } catch (SQLException e) {
                hasErrorOccur = true;
            } finally {
                preparedStatement = null;
            }
        }

        return hasErrorOccur;
    }

    /**
     * Closes the ResultSet, Prepared Statement and Connection Afterwards.
     *
     * @param connection
     *            The Connection Object to be closed
     * @param preparedStatement
     *            The Prepared Statement to be closed
     * @param resultSet
     *            The ResultSet to be closed
     * @return true if error closing the objects.
     */
    public final boolean closeConnectionObjects(final Connection connection,
            final PreparedStatement preparedStatement,
            final ResultSet resultSet) throws DaoException {
        boolean hasErrorOccur = false;

        hasErrorOccur = (hasErrorOccur) || closeObject(resultSet);
        hasErrorOccur = (hasErrorOccur) || closeObject(preparedStatement);
        hasErrorOccur = (hasErrorOccur) || closeObject(connection);
        
        if (hasErrorOccur) {
        	throw new DaoException("SQLException: "
        			+ "Error closing database resources.");
        }

        return hasErrorOccur;
    }

    /**
     * Sets the parameter values of PreparedStatement.
     *
     * @param preparedStatement
     *            The PreparedStatement to set the parameter values in.
     * @param values
     *            The parameter values to be set in the PreparedStatement.
     * @throws SQLException
     *             If fails when setting the PreparedStatement values.
     */
    public static void setValues(final PreparedStatement preparedStatement,
            final Object... values) throws SQLException {
        for (int i = 0; i < values.length; i++) {
            preparedStatement.setObject(i + 1, values[i]);
        }
    }
}
