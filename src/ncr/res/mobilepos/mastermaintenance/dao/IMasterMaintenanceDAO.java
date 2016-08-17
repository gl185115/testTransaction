/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * IMasterMaintenanceDAO
 *
 * Interface for the DAO that processes between Spart database and Web Store 
 * Server database.
 * 
 * del Rio, Rica Marie
 */
package ncr.res.mobilepos.mastermaintenance.dao;

import java.sql.Connection;

import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.mastermaintenance.model.MaintenanceTbl;
import ncr.res.mobilepos.mastermaintenance.model.OpeMastTbl;
import ncr.res.mobilepos.model.ResultBase;

/**
 *A DAO Interface for Master Maintenance implementation.
 */
public interface IMasterMaintenanceDAO {

    /**
     * Import a data from Spart into the WebStoreServer database.
     * @param spartdata The Data from SPART to be imported
     *  inside the WebStoreServer.
     * @return The ResultBase.
     * @throws DaoException the exception thrown when error occur.
     */
    ResultBase importSpartData(MaintenanceTbl spartdata) throws DaoException ;
}
