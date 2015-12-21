package ncr.res.mobilepos.department.dao;

import ncr.res.mobilepos.department.model.Department;
import ncr.res.mobilepos.department.model.DepartmentList;
import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.model.ResultBase;
/**
 * ���藚��
 * �o�[�W����         ������t       �S���Җ�           ������e
 * 1.01               2014.12.11     LiQian             DIV���݃`�F�b�N��Ή�
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
     * interface for deleteDepartment.
     *
     * @param storeid
     *            - id of the store
     * @param department
     *            - department object details
     * @return ResultBase
     * @throws DaoException
     *             - exception
     */
    ResultBase deleteDepartment(String storeid, Department department)
            throws DaoException;

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
    DepartmentList listDepartments(String retailStoreID, String key, String name, int searchLimit)
    throws DaoException;

     /**
      * interface for createDepartment.
      * @param storeid				id of the store
      * @param departmentid			id of the department
      * @param department			department object
      * 
      * @return ResultBase
      * 
      * @throws DaoException		exception
      */
     ResultBase createDepartment(String storeid,
             String departmentid, Department department)throws DaoException;

     /**
      * interface for updateDepartment.
      * @param storeid - id of the store
      * @param departmentid - id of department to update
      * @param department - the department data to update
      * @return ViewDepartment - contains the new department model
      * @throws DaoException - exception
      */
     ViewDepartment updateDepartment(String storeid,
             String departmentid, Department department) throws DaoException;  
     // 1.01  2014.12.11 LiQian DIV���݃`�F�b�N��Ή�   ADD START
     /**
      * interface for getDepartmentInfo.
      * @param retailStoreID - store number
      * @param departmentID - department number
      * @return ViewDepartment - the department info
      * @throws DaoException - exception
      */
     ViewDepartment getDepartmentInfo(String retailStoreID, String departmentID)throws DaoException;
     // 1.01  2014.12.11 LiQian DIV���݃`�F�b�N��Ή�   ADD END

}
