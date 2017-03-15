package ncr.res.mobilepos.discountplaninfo.dao;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.webserviceif.model.JSONData;

public interface IPremiumFlagDAO {

    /**
     * @param companyId
     * @param storeId
     * @param terminalId
     * @param dptIdList
     * @return premiumFlag(only return the dptList where the premiumFlag is true)
     * @throws DaoException
     */
    public JSONData getPremiumFlag(String companyId, String storeId, String terminalId, String dptIdList)
            throws DaoException;
}
