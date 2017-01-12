/*
 * Copyright (c) 2011-2012 NCR/JAPAN Corporation SW-R&D
 *
 * SQLServerCredentialDAO
 *
 * A Data Access Object implementation for Operator Sign ON/OFF.
 *
 * Menesses, Chris Niven
 * Campos, Carlos (cc185102)
 */
package ncr.res.mobilepos.credential.dao;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;


/**
 * SQLServerGroupDAO is a Data Access Object implementation for Groups.
 *
 */
public class SQLServerGroupDAO extends AbstractDao implements
        IGroupDAO {
    /**
     * class instance of trace debug printer.
     */
    private Trace.Printer tp = null;

    /**
     * SQLServerGroupDAO default constructor.
     * @throws DaoException database exception
     */
    public SQLServerGroupDAO() throws DaoException {
        JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(
                Thread.currentThread().getId(), getClass());
    }
}
