/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * IClassInfoDAO
 *
 * Interface for the DAO specific to a table
 *
 * Romares, Sul
 */

package ncr.res.mobilepos.classinfo.dao;

import java.util.List;

import ncr.res.mobilepos.classinfo.model.ClassInfo;
import ncr.res.mobilepos.classinfo.model.ViewClassInfo;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.model.ResultBase;

/**
 * IClassInfoDAO is a DAO interface for ClassInfo.
 */
public interface IClassInfoDAO {


    /**
     * Gets the list of classes in a particular
     * Store, Common Store or even All Stores
     * Can bypass the searchLimit defined in the SystemConfig max search results
     * using limit
     *
     * @param   storeId The ID of the Store where
     *                          the classes are located
     * @param   key     The given key used
     *                          to search the class(s) by item class
     * @param   name     The given name used
     *                          to search the class(s) by class name
     * @param   limit   The limit of the class to be search.
     * @param   companyId The ID of the Company where
     *                          the classes are located
     * @return  The list of classes.
     * @throws  DaoException
     *  The Exception thrown when getting the List of Classes failed.
     */
    List<ClassInfo> listClasses(String storeId, String department, String key, String name, int limit, String companyId) throws DaoException;

    /**
     * Deletes a Class.
     * @param retailStoreID The retailstoreid of a class.
     * @param department  The department of the class .
     * @param line        The line of the class.
     * @param itemClass        The class of the class.
     * @return resultBase   The ResultBase object containing
     *                      resultcode: 0 if success otherwise failed.
     * @throws DaoException The Exception thrown if error exists.
     */
    ResultBase deleteClass(String retailStoreID, String department,
            String line, String itemClass) throws DaoException;

    /**
     * Create a Class.
     * @param classInfo          The new values of the classInfo.
     * @return              The ResultBase object containing
     *                      resultcode: 0 if success otherwise failed.
     * @throws DaoException The Exception thrown when if error exists.
     */
    ResultBase createClassInfo(ClassInfo classInfo) throws DaoException;

    /**
     * Select ClassInfo.
     * @param retailStoreID - store number
     * @param department - department number
     * @param line - line
     * @param itemClass - class id
     * @param companyID - company id
     *
     * @return ViewClassInfo Model
     * @throws DaoException - exception
     */
     ViewClassInfo selectClassInfoDetail(String retailStoreID,
         String department, String line, String itemClass, String companyID) throws DaoException;

     /**
      * Updates a CLass info
      *
      * @param retailStoreId the store of the to be updated class  info
      * @param department	the department of the to be updated class info
      * @param lineid		the lineid of the to be updated class info
      * @param itemClass	the itemClass of the to be updated class info
      * @param class		the class object of the to be updated
      * @return ViewClassInfo object with the updated line
      * @throws DaoException
      */
     ViewClassInfo updateClassInfo (String retailStoreId, String department, String lineid, String itemClass, ClassInfo classInfo) throws DaoException;
}
