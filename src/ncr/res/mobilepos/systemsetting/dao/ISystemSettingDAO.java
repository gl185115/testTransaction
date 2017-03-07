package ncr.res.mobilepos.systemsetting.dao;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.systemsetting.model.DateSetting;

/**
 * ISystemSettingDAO.
 *
 * A Data Access Object implementation for Web Store Server's Business Date.
 */
public interface ISystemSettingDAO {

    /**
     * Get the Date Setting of the Web Store Server.
     *
     * @param companyid            The company id
     * @param storeid              The store id
     * @return                     The Date Setting
     * @throws DaoException        The Exception when the method failed
     */
    DateSetting getDateSetting(String companyid, String storeid) throws DaoException;
}
