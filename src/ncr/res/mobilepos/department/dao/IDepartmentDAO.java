package ncr.res.mobilepos.department.dao;

import ncr.res.mobilepos.department.model.ViewDepartment;
import ncr.res.mobilepos.exception.DaoException;
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
}
