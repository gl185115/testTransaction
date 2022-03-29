package ncr.res.mobilepos.mastersync.dao;

import java.util.List;
import java.util.Map;

import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.mastersync.model.DataFile;
import ncr.res.mobilepos.mastersync.model.MaintenanceLog;
import ncr.res.mobilepos.mastersync.model.MasterSyncParameter;
import ncr.res.mobilepos.mastersync.model.Record;

public interface IMasterSyncDAO {
    /**
     * Get DataFiles from REC_SYS_POSPARAM_VERSION.
     * @param companyId, storeId
     * @return List<DataFile>
     * @throws DaoException Exception when error occurs.
     */
    List<DataFile> getDataFiles(String companyId, String storeId) throws DaoException;

    /**
     * Get MaintenanceLogs from REC_MAINTENANCE_LOG.
     * @param maintenanceId
     * @return List<MaintenanceLog>
     * @throws DaoException Exception when error occurs.
     */
    List<MaintenanceLog> getNormalMaintenanceLogs(String companyId, String storeId, String bizCatId, long maintenanceId, int syncRecordCount) throws DaoException;

    /**
     * Get MaintenanceLogs from REC_MAINTENANCE_LOG_URGENT.
     * @param maintenanceId
     * @return List<MaintenanceLog>
     * @throws DaoException Exception when error occurs.
     */
    List<MaintenanceLog> getUrgentMaintenanceLogs(String companyId, String storeId, String bizCatId, long maintenanceId, int syncRecordCount) throws DaoException;

    /**
     * Get MasterSyncParameters from PRM_MASTER_SYNC.
     * @param syncGroupId
     * @return List<MasterSyncParameter>
     * @throws DaoException Exception when error occurs.
     */
    List<MasterSyncParameter> getMasterSyncParameters(int syncGroupId) throws DaoException;

    /**
     * Get MasterTableRecords from each master tables.
     * @param companyId
     * @param storeId
     * @param parameter
     * @param maintenanceLog
     * @return List<Record>
     * @throws DaoException Exception when error occurs.
     */
    List<Record> getMasterTableRecords(String companyId, String storeId, MasterSyncParameter parameter, MaintenanceLog maintenanceLog) throws DaoException;

    /**
     * Close DB Connection Object
     */
    void closeConnection();
}
