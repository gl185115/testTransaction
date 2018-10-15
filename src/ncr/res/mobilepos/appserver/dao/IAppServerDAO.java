/*
 * Copyright (c) 2017 NCR/JAPAN Corporation SW-R&D
 *
 * IAppServerDAO
 *
 * Interface for the DAO used for AppServer 
 * 
 */
package ncr.res.mobilepos.appserver.dao;

import java.util.List;

import ncr.res.mobilepos.appserver.model.AppServer;
import ncr.res.mobilepos.exception.DaoException;

/**
 * IAppServerDAO is a DAO Interface for AppServer.
 */
public interface IAppServerDAO {
    /**
     * Gets the list of AP Server with Request Url of WebApp (Tomcat or WOApi).
     * @return List of App Server
     * @throws DaoException Database Error
     */
	List<AppServer> getAppServers() throws DaoException;
	
}
