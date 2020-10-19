package ncr.res.mobilepos.mujiPassport.dao;

import java.util.Map;

import ncr.res.mobilepos.exception.DaoException;

public interface IMujiPassportDAO {

    /** return value */
    public Map<String, String> getPrmSystemConfigValue(String Category) throws DaoException;
}
