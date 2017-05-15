/*
 * Copyright (c) 2017 NCR/JAPAN Corporation SW-R&D
 *
 * IServerTableDAO
 *
 * Interface for the DAO used for ServerTable 
 * 
 */
package ncr.res.mobilepos.servertable.dao;

import java.util.List;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.servertable.model.ServerTable;

/**
 * IServerTableDAO is a DAO Interface for ServerTable.
 */
public interface IServerTableDAO {
    /**
     * Gets the list of AP Server with Request Url of WebApp (Tomcat or WOApi).
     * @return List of ServerTable
     * @throws DaoException Database Error
     */
	List<ServerTable> getServerTables() throws DaoException;
	
}
