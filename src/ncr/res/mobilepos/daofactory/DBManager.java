/*
    * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
    *
    * DBManager
    *
    * DBManager handles the interface of getting connection object
    *
    * Dela Cerna, Jessel
    */

package ncr.res.mobilepos.daofactory;

import java.sql.Connection;
import java.sql.SQLException;

import ncr.res.mobilepos.exception.DaoException;

/**
 * DBManager is an interface for managing the database.
 *
 */
public interface DBManager {

    /**
     * Gets the connection object.
     *
     * @return                    A Connection object.
     * @throws SQLException     Exception thrown when
     *                              getting a connection object fails.
     * @throws DaoException     thrown when an exception occurs
     */
    Connection getConnection() throws SQLException;
}
