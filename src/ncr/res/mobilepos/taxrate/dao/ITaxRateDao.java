package ncr.res.mobilepos.taxrate.dao;

import java.util.Map;

import ncr.res.mobilepos.exception.DaoException;

/**
 * DAO Interface for taxRate.
 *
 */
public interface ITaxRateDao {

    /**
     * get the taxRate by date
     * @param bussinessDate the date of current
     * @return taxRate
     */
    public Map<String,String> getTaxRateByDate(String bussinessDate) throws DaoException;

    /**
     * get the taxId by companyId and retailstoreId and departmentId
     * @param companyId ,retailstoreId ,departmentId
     * @return MST_DPTINFO.SubNum5
     */
    public String getTaxRateByDptId(String companyId ,String retailstoreId ,String departmentId) throws DaoException;
}
