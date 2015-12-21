package ncr.res.mobilepos.customerSearch.dao;

import java.util.Map;

import ncr.res.mobilepos.exception.DaoException;

public interface ICustomerSearthDAO {

    /** return value */
    public Map<String ,String> getPrmSystemConfigValue(String Category)
            throws DaoException;
}
