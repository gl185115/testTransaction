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
     * Set the BusinessDate of the Server.
     *
     * @param companyid                 The Company ID
     * @param storeid                   The Store ID
     * @param businessdate              The new BusinessDate
     * @param eod                       The End Of Day
     * @param skips                     The time to Skip
     *
     * @return                          The number of rows affected
     * @throws Exception                The exception thrown when error occur
     */
    int setDateSetting(String companyid, String storeid, String businessdate, String eod, String skips)
    throws DaoException;

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
