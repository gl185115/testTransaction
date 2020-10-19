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
    
    /**
     * Update the Date Setting of the Web Store Server.
     *
     * @param companyid            The company id
     * @param storeid              The store id
     * @param bizdate              The business date
     * @return                     Updated count
     * @throws DaoException        The Exception when the method failed
     */
    int updateDateSetting(String companyid, String storeid, String bizdate) throws DaoException;
    
    /**
     * Set the Boot Time of the Web Store Server.
     *
     * @param companyid            The company id
     * @param storeid              The store id
     * @param workstationid        The workstation id
     * @param bootdatetime         The boot date time
     * @return                     Updated count
     * @throws DaoException        The Exception when the method failed
     */
	int setBootTime(String companyid, String storeid, String workstationid, String bootdatetime) throws DaoException;
    /**
     * Set the Boot Time of the Web Store Server.
     *
     * @param companyid            The company id
     * @param storeid              The store id
     * @param workstationid        The workstation id
     * @param verTablet            The verTablet 
     * @param verTransaction       The verTransaction 
     * @param verBatch             The verBatch 
     * @param misc1                The misc1
     * @param misc2                The misc2
     * @param misc3                The misc3
     * @param misc4                The misc4
     * @param misc5                The misc5
     * @return                     Updated count
     * @throws DaoException        The Exception when the method failed
     */
    int setSoftwareVersionTime(String companyId, String storeId, String workstationid, String verTablet, String verTransaction, String verBatch, String misc1, String misc2, String misc3, String misc4, String misc5) throws DaoException;
}
