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
}
