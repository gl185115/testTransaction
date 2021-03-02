package ncr.res.mobilepos.intaPay.dao;

import java.util.Map;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.intaPay.model.IntaPayStoreConfig;

public interface IIntaPayDAO {

    /** return value */
    public Map<String, String> getPrmSystemConfigValue(String Category) throws DaoException;

    public IntaPayStoreConfig getStoreParam(String companyId, String storeId) throws DaoException;
}
