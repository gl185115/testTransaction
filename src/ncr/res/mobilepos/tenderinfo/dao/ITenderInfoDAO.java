package ncr.res.mobilepos.tenderinfo.dao;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.webserviceif.model.JSONData;

public interface ITenderInfoDAO {
    /**
     * @param companyId
     * @param storeId
     * @param tenderType
     * @return tender
     * @throws DaoException
     */
    public JSONData getTenderInfo(String companyId, String storeId, String tenderType) throws DaoException;
    /**
     * @param companyId
     * @param storeId
     * @return tender
     * @throws DaoException
     */
    public JSONData getAllTenderInfo(String companyId, String storeId) throws DaoException;
    
    /**
     * @param companyId
     * @param storeId
     * @param tenderType
     * @return tender
     * @throws DaoException
     */
    public JSONData getTenderInfoByType(String companyId, String storeId, String tenderType,String tenderId) throws DaoException;
}