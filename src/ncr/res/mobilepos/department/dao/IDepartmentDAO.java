package ncr.res.mobilepos.department.dao;

import ncr.res.mobilepos.department.model.DepartmentList;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.exception.DaoException;
/**
 * 改定履歴
 * バージョン         改定日付       担当者名           改定内容
 * 1.01               2014.12.11     LiQian             DIV存在チェックを対応
 */

/***
*
* @author AP185142
* @author RD185102
*/
public interface IDepartmentDAO {

    /**
    * Select Department.
    * @param retailStoreID - store number
    * @param departmentID - department number
    * @return Department Model
    * @throws DaoException - exception
    */
    ViewDepartment selectDepartmentDetail(String companyID, String retailStoreID,
        String departmentID) throws DaoException;
    /**
     * Gets the list of active departments of a store.
     *
     * @param retailStoreID store to which the department belongs.
     * @param key           search key. if null, list all departments of the store.
     * 						if numeric, list all departments prefix with department id.
     * 						if non-numeric, list all departments containing the department name.
     * @param name          search Department using name
     * @param searchLimit limit, if 0, use the systemConfig defined limit
     *                           if -1, no limit
     *                           if any int value, search limit
     * @return a list of department
     * @throws DaoException
     *             if error exists.
     */
    DepartmentList listDepartments(String companyId, String retailStoreID, String key, String name, int searchLimit)
    throws DaoException;
}
