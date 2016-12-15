package ncr.res.mobilepos.department.dao;

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
}
