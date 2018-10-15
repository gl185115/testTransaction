package ncr.res.mobilepos.futurePay.dao;

import java.util.Map;

import ncr.res.mobilepos.exception.DaoException;

public interface IFuturePayDAO {

    /** return value */
    public Map<String ,String> getPrmSystemConfigValue(String Category)
            throws DaoException;
}
