package ncr.res.mobilepos.mastersync.resource;

import java.sql.SQLException;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import ncr.realgate.util.Trace;

import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;

import ncr.res.mobilepos.mastersync.dao.IMasterSyncDAO;
import ncr.res.mobilepos.mastersync.model.DataFile;
import ncr.res.mobilepos.mastersync.model.DataFileVersionMatchingResult;
import ncr.res.mobilepos.mastersync.model.MaintenanceLogRequest;
import ncr.res.mobilepos.mastersync.model.MaintenanceLogResponse;
import ncr.res.mobilepos.mastersync.model.MaintenanceLog;
import ncr.res.mobilepos.mastersync.model.MasterSyncParameter;
import ncr.res.mobilepos.mastersync.model.MasterTable;
import ncr.res.mobilepos.mastersync.model.Record;

@Path("/mastersync")
@Api(value = "mastersync", description = "日中マスタ連携API")
public class MasterSyncResource {
    // Get the logger.
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    // The Resource DAO factory.
    private DAOFactory daoFactory;
    // The instance of the trace debug printer.
    private Trace.Printer tp;
    // The Program Name.
    private String progName = "MaintenanceResource";

    /**
     * MaintenanceResourceを初期化する。
     */
    public MasterSyncResource() {
        setDaoFactory(DAOFactory.getDAOFactory(DAOFactory.SQLSERVER));
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Sets the DAOFactory of the MaintenanceResource to use the DAO methods.
     * @param daoFactory The new value for the DAO factory.
     */
    public final void setDaoFactory(final DAOFactory daoFactory) {
        this.daoFactory = daoFactory;
    }

    /**
     * 通常メンテナンスログデータを取得する。
     * @param request メンテナンスログ取得リクエスト {@link MaintenanceLogRequest}
     * @return 通常メンテナンスログデータ {@link MaitnenanceLogResponse}
     */
    @POST
    @Path("/maintenance")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "通常メンテナンスログ取得", response = MaintenanceLogResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = ResultBase.RES_OK, message = "Success"),
            @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "データベースエラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_DAO, message = "DAOエラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効なパラメータ"),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー")
    })
    public final MaintenanceLogResponse getNormalMaintenanceLog(MaintenanceLogRequest request) {
        String functionName = "getNormalMaintenanceLog";

        tp.methodEnter(functionName).println("companyId", request.getCompanyId()).println("storeId", request.getStoreId())
            .println("bizCatId", request.getBizCatId()).println("maintenanceId", request.getMaintenanceId())
            .println("syncRecordCount", request.getSyncRecordCount()).println("dataFiles", request.getDataFiles());

        MaintenanceLogResponse response = new MaintenanceLogResponse();

        if (!request.isValid()) {
            // リクエスト内容が無効な場合はメンテナンスデータなしとして返す
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            response.setMessage("Invalid request parameter.");
            response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
            return response;
        }

        try {
            IMasterSyncDAO dao = daoFactory.getMasterSyncDAO();

            // 配信ファイルバージョンチェック
            List<DataFile> clientDataFiles = request.getDataFiles();
            List<DataFile> serverDataFiles = dao.getDataFiles(request.getCompanyId(), request.getStoreId());

            DataFileVersionMatchingResult matchingResult = verifyDataFileVersionMatching(clientDataFiles, serverDataFiles);
            if (matchingResult != DataFileVersionMatchingResult.DATA_FILE_VERSION_MATCH) {
                // 配信ファイルバージョンが一致しない場合は、一致しない原因に応じたエラーレスポンスを返す
                return createDataFileVersionErrorResponse(matchingResult);
            }

            // メンテナンスデータ取得処理
            long maintenanceId = decideUsingMaintenanceId(request.getMaintenanceId(), serverDataFiles);
            List<MaintenanceLog> maintenanceLogs = dao.getNormalMaintenanceLogs(request.getCompanyId(), request.getStoreId(), request.getBizCatId(), maintenanceId, request.getSyncRecordCount());
            if (maintenanceLogs.isEmpty()) {
                response.setNCRWSSResultCode(ResultBase.RES_OK);
                response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
                return response;
            }

            // メンテナンスログに対応するマスタデータを抽出
            response.setMaintenanceLogs(getMaintenanceLogsWithMasterRecords(request.getCompanyId(), request.getStoreId(), maintenanceLogs));

            if (response.getMaintenanceLogs().isEmpty()) {
                response.setNCRWSSResultCode(ResultBase.RES_OK);
                response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
                return response;
            }

            response.setNCRWSSResultCode(ResultBase.RES_OK);
            response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_EXISTS);
            response.setDataCount(response.getMaintenanceLogs().size());
        } catch (DaoException daoEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO, "Failed to get the maintenance logs.\n" + daoEx.getMessage());
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            if (daoEx.getCause() instanceof SQLException) {
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            }
            response.setMessage(daoEx.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL, ex.getMessage());
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            response.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(response);
        }

        return response;
    }

    /**
     * 緊急売変メンテナンスログデータを取得する。
     * @param request メンテナンスログ取得リクエスト {@link MaintenanceLogRequest}
     * @return 緊急売変メンテナンスログデータ {@link MaintenanceLogResponse}
     */
    @POST
    @Path("/maintenance_urgent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "緊急売変メンテナンスログ取得", response = MaintenanceLogResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = ResultBase.RES_OK, message = "Success"),
            @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "データベースエラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_DAO, message = "DAOエラー"),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "無効なパラメータ"),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "汎用エラー")
    })
    public final MaintenanceLogResponse getUrgentMaintenanceLog(MaintenanceLogRequest request) {
        String functionName = "getUrgentMaintenanceLog";

        tp.methodEnter(functionName).println("companyId", request.getCompanyId()).println("storeId", request.getStoreId())
            .println("bizCatId", request.getBizCatId()).println("maintenanceId", request.getMaintenanceId())
            .println("syncRecordCount", request.getSyncRecordCount()).println("dataFiles", request.getDataFiles());

        MaintenanceLogResponse response = new MaintenanceLogResponse();

        if (!request.isValid()) {
            // リクエスト内容が無効な場合はメンテナンスデータなしとして返す
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            response.setMessage("Invalid request parameter.");
            response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
            return response;
        }

        try {
            IMasterSyncDAO dao = daoFactory.getMasterSyncDAO();

            // 配信ファイルバージョンチェック
            List<DataFile> clientDataFiles = request.getDataFiles();
            List<DataFile> serverDataFiles = dao.getDataFiles(request.getCompanyId(), request.getStoreId());

            DataFileVersionMatchingResult matchingResult = verifyDataFileVersionMatching(clientDataFiles, serverDataFiles);
            if (matchingResult != DataFileVersionMatchingResult.DATA_FILE_VERSION_MATCH) {
                // 配信ファイルバージョンが一致しない場合は、一致しない原因に応じたエラーレスポンスを返す
                return createDataFileVersionErrorResponse(matchingResult);
            }

            // メンテナンスデータ取得処理
            long maintenanceId = decideUsingMaintenanceId(request.getMaintenanceId(), serverDataFiles);
            List<MaintenanceLog> maintenanceLogs = dao.getUrgentMaintenanceLogs(request.getCompanyId(), request.getStoreId(), request.getBizCatId(), maintenanceId, request.getSyncRecordCount());

            // メンテナンスログに対応するマスタデータを抽出
            response.setMaintenanceLogs(getMaintenanceLogsWithMasterRecords(request.getCompanyId(), request.getStoreId(), maintenanceLogs));

            if (response.getMaintenanceLogs().isEmpty()) {
                response.setNCRWSSResultCode(ResultBase.RES_OK);
                response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
                return response;
            }

            response.setNCRWSSResultCode(ResultBase.RES_OK);
            response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_EXISTS);
            response.setDataCount(response.getMaintenanceLogs().size());
        } catch (DaoException daoEx) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_DAO, "Failed to get the maintenance logs.\n" + daoEx.getMessage());
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            if (daoEx.getCause() instanceof SQLException) {
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
            }
            response.setMessage(daoEx.getMessage());
        } catch (Exception ex) {
            LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL, ex.getMessage());
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            response.setMessage(ex.getMessage());
        } finally {
            tp.methodExit(response);
        }

        return response;
    }

    /**
     * リクエスト元の配信ファイルのバージョンとサーバ側管理の配信ファイルバージョンが一致するかどうかを確認する。
     * @param client
     * @param server
     * @return
     */
    private DataFileVersionMatchingResult verifyDataFileVersionMatching(List<DataFile> client, List<DataFile> server) {
        // 比較時にリスト内の順序違いで誤判定しないようにTypeでソートしておく
        Comparator<DataFile> comparator = new Comparator<DataFile>() {
            @Override
            public int compare(DataFile df1, DataFile df2) {
                return df1.getType() - df2.getType();
            }
        };
        Collections.sort(client, comparator);
        Collections.sort(server, comparator);

        if (server.isEmpty()) {
            // 配信ファイルバージョンの取得に失敗
            return DataFileVersionMatchingResult.SERVER_DATA_FILE_GET_FAILED;
        }

        if (server.size() != client.size()) {
            // 配信ファイルの数が一致しない
            return DataFileVersionMatchingResult.DATA_FILE_COUNT_NOT_MATCH;
        }

        if (!server.equals(client)) {
            // 配信ファイルバージョンが一致しない
            return DataFileVersionMatchingResult.DATA_FILE_VERSION_NOT_MATCH;
        }

        return DataFileVersionMatchingResult.DATA_FILE_VERSION_MATCH;
    }

    /**
     * 配信ファイルバージョンチェックエラーに対応するレスポンスデータを作成する。
     * @param result
     * @return
     */
    private MaintenanceLogResponse createDataFileVersionErrorResponse(DataFileVersionMatchingResult result) {
        MaintenanceLogResponse response = new MaintenanceLogResponse();
        switch (result) {
            case SERVER_DATA_FILE_GET_FAILED:
                // 配信ファイルバージョンの取得に失敗
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setMessage("Failed to get the DataFile version from database.");
                response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
                return response;
            case DATA_FILE_COUNT_NOT_MATCH:
                // 配信ファイル数が一致しない
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setMessage("DataFile count is not matching.");
                response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
                return response;
            case DATA_FILE_VERSION_NOT_MATCH:
            default:
                // 配信ファイルバージョンが一致しない
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setMessage("DataFile version is not matching.");
                response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
                return response;
        }
    }

    /**
     * メンテナンスログ取得に使用するメンテナンスIDを決定する。
     * @param clientMaintenanceId
     * @param serverDataFiles
     * @return
     */
    private long decideUsingMaintenanceId(long clientMaintenanceId, List<DataFile> serverDataFiles) {
        long maintenanceId = clientMaintenanceId;
        long serverMaintenanceId = 0;

        for (DataFile dataFile : serverDataFiles) {
            if (serverMaintenanceId < dataFile.getMaintenanceId()) {
                // 複数の配信ファイルが存在する場合、最大のメンテナンスIDを設定する
                serverMaintenanceId = dataFile.getMaintenanceId();
            }
        }

        if (clientMaintenanceId < serverMaintenanceId) {
            // 配信ファイル作成時のメンテナンスIDの方が新しい場合は
            // 配信ファイル作成時のメンテナンスIDを使用する
            maintenanceId = serverMaintenanceId;
        }

        return maintenanceId;
    }

    /**
     * マスタデータを紐付けたメンテナンスログを取得する。
     * @param maintenanceLogs
     * @return
     */
    private List<MaintenanceLog> getMaintenanceLogsWithMasterRecords(String companyId, String storeId, List<MaintenanceLog> maintenanceLogs) throws DaoException {
        IMasterSyncDAO dao = daoFactory.getMasterSyncDAO();
        List<MaintenanceLog> logs = new LinkedList<MaintenanceLog>();

        for (MaintenanceLog log : maintenanceLogs) {
            // マスタデータ抽出パラメータを取得
            List<MasterSyncParameter> parameters = dao.getMasterSyncParameters(log.getSyncGroupId());
            for (MasterSyncParameter parameter : parameters) {
                // マスタデータを抽出
                List<Record> records = dao.getMasterTableRecords(companyId, storeId, parameter, log);
                if (records.isEmpty()) {
                    continue;
                }
                MasterTable table = new MasterTable();
                table.setTableName(parameter.getDatabaseName() + "." + parameter.getSchemaName() + "." + parameter.getTableName());
                table.setRecordCount(records.size());
                table.setRecords(records);
                log.addMasterTable(table);
            }
            if (log.getMaintenanceType() != 3 && log.getMasterTables().isEmpty()) {
                // メンテナンス区分が登録または更新の場合、紐付くマスタデータが存在しなければ連携対象外とする
                continue;
            }
            logs.add(log);
        }

        return logs;
    }
}
