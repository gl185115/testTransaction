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

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.line.model.Line;
import ncr.res.mobilepos.line.model.ViewLine;
import ncr.res.mobilepos.model.ResultBase;

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
     * @param   companyid The ID of the Company where
     *                 the lines are located
     * @return  The list of lines.
     * @throws  DaoException
     *  The Exception thrown when getting the List of Lines failed.
     */
    List<Line> listLines(String retailstoreid, String department, String key, String name, int limit, String companyid) throws DaoException;

    /**
     * Deletes a Line.
     * @param retailStoreID The retailstoreid of a line.
     * @param department  The line department.
     * @param line        The line id.
     * @return resultBase   The ResultBase object containing
     *                      resultcode: 0 if success otherwise failed.
     * @throws DaoException The Exception thrown if error exists.
     */
    ResultBase deleteLine(String retailStoreID, String department,
            String line) throws DaoException;

    /**
     * Create a Line.
     * @param line          The new values of the line.
     * @return              The ResultBase object containing
     *                      resultcode: 0 if success otherwise failed.
     * @throws DaoException The Exception thrown when if error exists.
     */
    ResultBase createLine(Line line) throws DaoException;

    /**
     * Select Line.
     * @param retailStoreID - store number
     * @param department - department number
     * @param line - line
     * @param companyID - company id
     * @return Line Model
     * @throws DaoException - exception
     */
     ViewLine selectLineDetail(String retailStoreID,
         String department, String line, String companyID) throws DaoException;

     /**
      * Updates a Line infod
      *
      * @param retailStoreId the store of the to be updated line  info
      * @param department	the department of the to be updated line info
      * @param lineid		the lineid of the to be updated line info
      * @param line			the line object of the to be updated
      * @return ViewLine object with the updated line
      * @throws DaoException
      */
     ViewLine updateLine (String retailStoreId, String department, String lineid, Line line) throws DaoException;

}
