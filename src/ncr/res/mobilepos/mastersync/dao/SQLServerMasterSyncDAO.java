package ncr.res.mobilepos.mastersync.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import ncr.realgate.util.Trace;

import ncr.res.mobilepos.constant.WindowsEnvironmentVariables;
import ncr.res.mobilepos.daofactory.AbstractDao;
import ncr.res.mobilepos.daofactory.DBManager;
import ncr.res.mobilepos.daofactory.JndiDBManagerMSSqlServer;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.property.SQLStatement;

import ncr.res.mobilepos.mastersync.model.DataFile;
import ncr.res.mobilepos.mastersync.model.Field;
import ncr.res.mobilepos.mastersync.model.MaintenanceLog;
import ncr.res.mobilepos.mastersync.model.MasterSyncParameter;
import ncr.res.mobilepos.mastersync.model.Record;

public class SQLServerMasterSyncDAO extends AbstractDao implements IMasterSyncDAO {
    // Get the logger.
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    // メンテナンスログテーブル情報
    private static final String MAINTENANCE_LOG_TABLE_NAME= "REC_MAINT_LOG";
    private static final String MAINTENANCE_LOG_STORE_TABLE_NAME = "REC_MAINT_LOG_STORE";
    private static final String URGENT_MAINTENANCE_LOG_TABLE_NAME = "REC_MAINT_LOG_URGENT";
    private static final String URGENT_MAINTENANCE_LOG_STORE_TABLE_NAME = "REC_MAINT_LOG_URGENT_STORE";
    private static final String MAINTENANCE_ID_COLUMN = "MaintenanceId";
    private static final String RECEIVED_MAINTENANCE_ID_COLUMN = "ReceivedMaintenanceId";

    // The Database Manager of the class.
    private DBManager dbManager;
    // The instance of the trace debug printer.
    private Trace.Printer tp;
    // The Program Name.
    private String progName = "SQLServerMasterSyncDAO";
    // The SQLStatement instance.
    private final SQLStatement sqlStatement;

    /**
     * Default constructor for SQLServerMasterSyncDao.
     * <p>
     *  Sets DBManager for database connection, and Logger for logging.
     * @throws DaoException
     *          The exception thrown when the constructor fails.
     */
    public SQLServerMasterSyncDAO() throws DaoException {
        dbManager = JndiDBManagerMSSqlServer.getInstance();
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
        sqlStatement = SQLStatement.getInstance();
    }

    /**
     * Gets the Database Manager for SQLServerMasterSyncDAO.
     * @return Returns a DBManager instance.
     */
    public final DBManager getDBManager() {
        return dbManager;
    }

    /**
     * Get DataFiles from REC_SYS_POSPARAM_VERSION.
     */
    @Override
    public final List<DataFile> getDataFiles(String companyId, String storeId) throws DaoException {
        String functionName = "getDataFiles";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        List<DataFile> dataFiles = new ArrayList<DataFile>();

        try {
            connection = dbManager.getConnection();

            statement = connection.prepareStatement(sqlStatement.getProperty("mastersync-get-data-file"));
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);

            result = statement.executeQuery();

            while (result.next()) {
                DataFile dataFile = new DataFile();
                dataFile.setType(result.getInt(result.findColumn("DataFileType")));
                dataFile.setVersion(result.getString(result.findColumn("DataFileVersion")));
                dataFile.setMaintenanceId(result.getLong(result.findColumn("Maintenanceid")));
                dataFiles.add(dataFile);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL, "Failed to get the DataFiles.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getDataFiles", sqlEx);
        } catch (NumberFormatException numberEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_PARSE, "Failed to get the DataFiles.\n" + numberEx.getMessage());
            throw new DaoException("NumberFormatException: @getDataFiles", numberEx);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL, "Failed to get the DataFiles.\n" + ex.getMessage());
            throw new DaoException("Exception: @getDataFiles", ex);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(dataFiles);
        }

        return dataFiles;
    }

    /**
     * Get MaintenanceLogs from REC_MAINTENANCE_LOG.
     */
    @Override
    public List<MaintenanceLog> getNormalMaintenanceLogs(String companyId, String storeId, String bizCatId, long maintenanceId, int syncRecordCount) throws DaoException {
        // HOSTでは自動採番されたメンテナンスIDではなくEnterpriseから受信したメンテナンスIDを使用する
        String columnName;
        if (WindowsEnvironmentVariables.getInstance().isServerTypeEnterprise()) {
            columnName = MAINTENANCE_ID_COLUMN;
        } else {
            columnName = RECEIVED_MAINTENANCE_ID_COLUMN;
        }

        // メンテナンスログテーブルからメンテナンスログを取得
        return getMaintenanceLogs(companyId, storeId, bizCatId, maintenanceId, syncRecordCount, MAINTENANCE_LOG_TABLE_NAME, MAINTENANCE_LOG_STORE_TABLE_NAME, columnName);
    }

    /**
     * Get MaintenanceLogs from REC_MAINTENANCE_LOG_URGENT.
     */
    @Override
    public List<MaintenanceLog> getUrgentMaintenanceLogs(String companyId, String storeId, String bizCatId, long maintenanceId, int syncRecordCount) throws DaoException {
        // 緊急売変メンテナンスログテーブルからメンテナンスログを取得
        // M-POS(メイン)からのみ配信する想定なのでメンテナンスIDは常に自動採番されたものを使う
        return getMaintenanceLogs(companyId, storeId, bizCatId, maintenanceId, syncRecordCount, URGENT_MAINTENANCE_LOG_TABLE_NAME, URGENT_MAINTENANCE_LOG_STORE_TABLE_NAME, MAINTENANCE_ID_COLUMN);
    }

    /**
     * Get MasterSyncParameter from PRM_MASTER_SYNC.
     */
    public List<MasterSyncParameter> getMasterSyncParameters(int syncGroupId) throws DaoException {
        String functionName = "getMasterSyncParameters";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        List<MasterSyncParameter> parameters = new ArrayList<MasterSyncParameter>();

        try {
            connection = dbManager.getConnection();

            statement = connection.prepareStatement(sqlStatement.getProperty("mastersync-get-parameter"));
            statement.setInt(SQLStatement.PARAM1, syncGroupId);

            result = statement.executeQuery();

            while (result.next()) {
                MasterSyncParameter parameter = new MasterSyncParameter();

                parameter.setDatabaseName(result.getString(result.findColumn("DatabaseName")));
                parameter.setSchemaName(result.getString(result.findColumn("SchemaName")));
                parameter.setTableName(result.getString(result.findColumn("TableName")));
                parameter.setFunctionName(result.getString(result.findColumn("FunctionName")));

                parameters.add(parameter);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL, "Failed to get the MasterSyncParameters.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getMasterSyncParameters", sqlEx);
        } catch (NumberFormatException numberEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_PARSE, "Failed to get the MasterSyncParameters.\n" + numberEx.getMessage());
            throw new DaoException("NumberFormatException: @getMasterSyncParameters", numberEx);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL, "Failed to get the MasterSyncParameters.\n" + ex.getMessage());
            throw new DaoException("Exception: @getMasterSyncParameters", ex);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(parameters);
        }

        return parameters;
    }

    /**
     * Get MasterTableRecords from each master tables.
     */
    public List<Record> getMasterTableRecords(String companyId, String storeId, MasterSyncParameter parameter, MaintenanceLog maintenanceLog) throws DaoException {
        String functionName = "getMasterTableRecords";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        List<Record> records = new LinkedList<Record>();

        try {
            connection = dbManager.getConnection();

            String sql = "EXEC " + parameter.getDatabaseName() + "." + parameter.getSchemaName() + "." + parameter.getFunctionName() + " ?, ?, ?";

            statement = connection.prepareStatement(sql);
            statement.setString(SQLStatement.PARAM1, companyId);
            statement.setString(SQLStatement.PARAM2, storeId);
            statement.setString(SQLStatement.PARAM3, maintenanceLog.getReferenceCondition());

            result = statement.executeQuery();
            ResultSetMetaData meta = statement.getMetaData();

            int recordNumber = 1;
            while (result.next()) {
                Record record = new Record();

                int columnCount = meta.getColumnCount();

                for (int i = 1; i <= columnCount; i++) {
                    String columnName = meta.getColumnName(i);

                    Field field = new Field();

                    field.setNumber(i);
                    field.setName(columnName);
                    // フィールドの値はDBのデータ型に応じた適切なデータ型で設定する
                    if (result.getObject(result.findColumn(columnName)) == null) {
                        // NULL
                        field.setValue(null);
                    } else {
                        // NULL以外は適切なデータ型で設定
                        switch (meta.getColumnType(i)) {
                            case Types.BIGINT:
                            case Types.DECIMAL:
                            case Types.NUMERIC:
                                field.setValue(result.getLong(result.findColumn(columnName)));
                                break;
                            case Types.BIT:
                            case Types.BOOLEAN:
                                field.setValue(result.getBoolean(result.findColumn(columnName)));
                                break;
                            case Types.TINYINT:
                            case Types.SMALLINT:
                            case Types.INTEGER:
                                field.setValue(result.getInt(result.findColumn(columnName)));
                                break;
                            case Types.FLOAT:
                            case Types.DOUBLE:
                                field.setValue(result.getDouble(result.findColumn(columnName)));
                                break;
                            default:
                                field.setValue(result.getString(result.findColumn(columnName)));
                                break;
                        }
                    }

                    record.addField(field);
                }

                record.setNumber(recordNumber++);

                records.add(record);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL, "Failed to get the MasterTableRecords.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getMasterTableRecords", sqlEx);
        } catch (NumberFormatException numberEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_PARSE, "Failed to get the MasterTableRecords.\n" + numberEx.getMessage());
            throw new DaoException("NumberFormatException: @getMasterTableRecords", numberEx);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL, "Failed to get the MasterTableRecords.\n" + ex.getMessage());
            throw new DaoException("Exception: @getMasterTableRecords", ex);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(records);
        }

        return records;
    }

    /**
     * メンテナンスログを取得する。
     * @param companyId
     * @param storeId
     * @param maintenanceId
     * @param syncRecordCount
     * @param maintenanceLogTableName
     * @param maintenanceIdColumn
     * @return
     * @throws DaoException
     */
    private List<MaintenanceLog> getMaintenanceLogs(String companyId, String storeId, String bizCatId, long maintenanceId, int syncRecordCount,
                                                    String logTableName, String storeTableName, String idColumnName) throws DaoException {
        String functionName = "getMaintenanceLogs";

        Connection connection = null;
        PreparedStatement statement = null;
        ResultSet result = null;

        List<MaintenanceLog> maintenanceLogs = new LinkedList<MaintenanceLog>();

        try {
            connection = dbManager.getConnection();

            // メンテナンスログ取得クエリの準備
            String sql = sqlStatement.getProperty("mastersync-get-maintenance-log");
            // 指定されたメンテナンスログテーブルからメンテナンスログを取得する
            sql = sql.replace("{MaintenanceLogTable}", logTableName);
            sql = sql.replace("{MaintenanceLogStoreTable}", storeTableName);
            // 指定されたメンテナンスID列を参照してメンテナンスログを取得する
            sql = sql.replace("{MaintenanceIdColumn}", idColumnName);

            // 期間FROM-TOが設定されたメンテナンスログは当日分のみ連携対象
            DateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date today = new Date();

            statement = connection.prepareStatement(sql);
            statement.setLong(SQLStatement.PARAM1, maintenanceId);
            statement.setInt(SQLStatement.PARAM2, syncRecordCount);
            statement.setString(SQLStatement.PARAM3, dateFormat.format(today));
            statement.setString(SQLStatement.PARAM4, companyId);
            statement.setString(SQLStatement.PARAM5, storeId);
            statement.setString(SQLStatement.PARAM6, bizCatId);

            result = statement.executeQuery();

            while (result.next()) {
                MaintenanceLog maintenanceLog = new MaintenanceLog();

                // メンテナンスログ情報の設定
                maintenanceLog.setTableName(logTableName);
                maintenanceLog.setMaintenanceId(result.getLong(result.findColumn("MaintenanceId")));
                maintenanceLog.setMaintenanceLogId(result.getString(result.findColumn("MaintenanceLogId")));
                maintenanceLog.setMaintenanceType(result.getInt(result.findColumn("MaintenanceType")));
                maintenanceLog.setSyncGroupId(result.getInt(result.findColumn("SyncGroupId")));
                maintenanceLog.setReferenceCondition(result.getString(result.findColumn("ReferenceCondition")));
                maintenanceLog.setMasterUpdDate(result.getString(result.findColumn("MasterUpdDate")));
                maintenanceLog.setMasterUpdAppId(result.getString(result.findColumn("MasterUpdAppId")));
                maintenanceLog.setMasterUpdOpeCode(result.getString(result.findColumn("MasterUpdOpeCode")));
                maintenanceLog.setTargetStoreType(result.getString(result.findColumn("TargetStoreType")));
                maintenanceLog.setStartDate(result.getString(result.findColumn("StartDate")));
                maintenanceLog.setEndDate(result.getString(result.findColumn("EndDate")));
                maintenanceLog.setStartTime(result.getString(result.findColumn("StartTime")));
                maintenanceLog.setEndTime(result.getString(result.findColumn("EndTime")));

                // メンテナンスログ店舗情報の設定
                maintenanceLog.getStore().setTableName(storeTableName);
                maintenanceLog.getStore().setCompanyId(result.getString(result.findColumn("CompanyId")));
                maintenanceLog.getStore().setStoreId(result.getString(result.findColumn("StoreId")));
                maintenanceLog.getStore().setBizCatId(result.getString(result.findColumn("BizCatId")));

                maintenanceLogs.add(maintenanceLog);
            }
        } catch (SQLException sqlEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_SQL, "Failed to get the MaintenanceLogs.\n" + sqlEx.getMessage());
            throw new DaoException("SQLException: @getMaintenanceLogs", sqlEx);
        } catch (NumberFormatException numberEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_PARSE, "Failed to get the MaintenanceLogs.\n" + numberEx.getMessage());
            throw new DaoException("NumberFormatException: @getMaintenanceLogs", numberEx);
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL, "Failed to get the MaintenanceLogs.\n" + ex.getMessage());
            throw new DaoException("Exception: @getMaintenanceLogs", ex);
        } finally {
            closeConnectionObjects(connection, statement, result);
            tp.methodExit(maintenanceLogs);
        }

        return maintenanceLogs;
    }
}
