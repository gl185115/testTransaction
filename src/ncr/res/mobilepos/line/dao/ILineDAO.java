/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * IClassInfoDAO
 *
 * Interface for the DAO specific to a table
 *
 * Romares, Sul
 */

package ncr.res.mobilepos.line.dao;

import java.util.List;

import ncr.res.mobilepos.line.model.Line;
import ncr.res.mobilepos.line.model.ViewLine;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.exception.DaoException;

/**
 * IClassInfoDAO is a DAO interface for ClassInfo.
 */
public interface ILineDAO {
   
	
    /**
     * Gets the list of Lines in a particular
     * Store, Common Store or even All Stores   
     * Can bypass the searchLimit defined in the SystemConfig max search results
     * using limit
     *
     * @param   retailstoreid The ID of the Store where
     *                          the lines are located
     * @param   key     The given key used
     *                          to search the line(s) by line
     * @param   name     The given name used
     *                          to search the line(s) by line name
     * @param   limit   The limit of the line to be search.  
     * @return  The list of lines.
     * @throws  DaoException
     *  The Exception thrown when getting the List of Lines failed.
     */
    List<Line> listLines(String retailstoreid, String department, String key, String name, int limit) throws DaoException;  

}
