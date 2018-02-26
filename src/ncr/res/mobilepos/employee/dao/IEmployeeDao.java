package ncr.res.mobilepos.employee.dao;

import ncr.res.mobilepos.employee.model.EmployeeInfoResponse;
import ncr.res.mobilepos.exception.DaoException;

/**
 * IEmployeeDao Interface.
 */
public interface IEmployeeDao {

	/**
	 * Get EmployeeInfo List Information for a companyID.
	 * 
	 * @param companyID
	 *            - âÔé–ÉRÅ[Éh
	 * @return íSìñé“ÇÃèÓïÒ List
	 * @throws DaoException
	 *             The exception thrown for non-SQL related issue.
	 */
	EmployeeInfoResponse EmpList(String companyID) throws DaoException;
}
