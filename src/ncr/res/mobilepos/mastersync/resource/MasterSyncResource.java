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
@Api(value = "mastersync", description = "�����}�X�^�A�gAPI")
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
     * MaintenanceResource������������B
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
     * �ʏ탁���e�i���X���O�f�[�^���擾����B
     * @param request �����e�i���X���O�擾���N�G�X�g {@link MaintenanceLogRequest}
     * @return �ʏ탁���e�i���X���O�f�[�^ {@link MaitnenanceLogResponse}
     */
    @POST
    @Path("/maintenance")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "�ʏ탁���e�i���X���O�擾", response = MaintenanceLogResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = ResultBase.RES_OK, message = "Success"),
            @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "�f�[�^�x�[�X�G���["),
            @ApiResponse(code = ResultBase.RES_ERROR_DAO, message = "DAO�G���["),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "�����ȃp�����[�^"),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "�ėp�G���[")
    })
    public final MaintenanceLogResponse getNormalMaintenanceLog(MaintenanceLogRequest request) {
        String functionName = "getNormalMaintenanceLog";

        tp.methodEnter(functionName).println("companyId", request.getCompanyId()).println("storeId", request.getStoreId())
            .println("bizCatId", request.getBizCatId()).println("maintenanceId", request.getMaintenanceId())
            .println("syncRecordCount", request.getSyncRecordCount()).println("dataFiles", request.getDataFiles());

        MaintenanceLogResponse response = new MaintenanceLogResponse();

        if (!request.isValid()) {
            // ���N�G�X�g���e�������ȏꍇ�̓����e�i���X�f�[�^�Ȃ��Ƃ��ĕԂ�
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            response.setMessage("Invalid request parameter.");
            response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
            return response;
        }

        try {
            IMasterSyncDAO dao = daoFactory.getMasterSyncDAO();

            // �z�M�t�@�C���o�[�W�����`�F�b�N
            List<DataFile> clientDataFiles = request.getDataFiles();
            List<DataFile> serverDataFiles = dao.getDataFiles(request.getCompanyId(), request.getStoreId());

            DataFileVersionMatchingResult matchingResult = verifyDataFileVersionMatching(clientDataFiles, serverDataFiles);
            if (matchingResult != DataFileVersionMatchingResult.DATA_FILE_VERSION_MATCH) {
                // �z�M�t�@�C���o�[�W��������v���Ȃ��ꍇ�́A��v���Ȃ������ɉ������G���[���X�|���X��Ԃ�
                return createDataFileVersionErrorResponse(matchingResult);
            }

            // �����e�i���X�f�[�^�擾����
            long maintenanceId = decideUsingMaintenanceId(request.getMaintenanceId(), serverDataFiles);
            List<MaintenanceLog> maintenanceLogs = dao.getNormalMaintenanceLogs(request.getCompanyId(), request.getStoreId(), request.getBizCatId(), maintenanceId, request.getSyncRecordCount());
            if (maintenanceLogs.isEmpty()) {
                response.setNCRWSSResultCode(ResultBase.RES_OK);
                response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
                return response;
            }

            // �����e�i���X���O�ɑΉ�����}�X�^�f�[�^�𒊏o
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
     * �ً}���σ����e�i���X���O�f�[�^���擾����B
     * @param request �����e�i���X���O�擾���N�G�X�g {@link MaintenanceLogRequest}
     * @return �ً}���σ����e�i���X���O�f�[�^ {@link MaintenanceLogResponse}
     */
    @POST
    @Path("/maintenance_urgent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "�ً}���σ����e�i���X���O�擾", response = MaintenanceLogResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = ResultBase.RES_OK, message = "Success"),
            @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "�f�[�^�x�[�X�G���["),
            @ApiResponse(code = ResultBase.RES_ERROR_DAO, message = "DAO�G���["),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "�����ȃp�����[�^"),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "�ėp�G���[")
    })
    public final MaintenanceLogResponse getUrgentMaintenanceLog(MaintenanceLogRequest request) {
        String functionName = "getUrgentMaintenanceLog";

        tp.methodEnter(functionName).println("companyId", request.getCompanyId()).println("storeId", request.getStoreId())
            .println("bizCatId", request.getBizCatId()).println("maintenanceId", request.getMaintenanceId())
            .println("syncRecordCount", request.getSyncRecordCount()).println("dataFiles", request.getDataFiles());

        MaintenanceLogResponse response = new MaintenanceLogResponse();

        if (!request.isValid()) {
            // ���N�G�X�g���e�������ȏꍇ�̓����e�i���X�f�[�^�Ȃ��Ƃ��ĕԂ�
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            response.setMessage("Invalid request parameter.");
            response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
            return response;
        }

        try {
            IMasterSyncDAO dao = daoFactory.getMasterSyncDAO();

            // �z�M�t�@�C���o�[�W�����`�F�b�N
            List<DataFile> clientDataFiles = request.getDataFiles();
            List<DataFile> serverDataFiles = dao.getDataFiles(request.getCompanyId(), request.getStoreId());

            DataFileVersionMatchingResult matchingResult = verifyDataFileVersionMatching(clientDataFiles, serverDataFiles);
            if (matchingResult != DataFileVersionMatchingResult.DATA_FILE_VERSION_MATCH) {
                // �z�M�t�@�C���o�[�W��������v���Ȃ��ꍇ�́A��v���Ȃ������ɉ������G���[���X�|���X��Ԃ�
                return createDataFileVersionErrorResponse(matchingResult);
            }

            // �����e�i���X�f�[�^�擾����
            long maintenanceId = decideUsingMaintenanceId(request.getMaintenanceId(), serverDataFiles);
            List<MaintenanceLog> maintenanceLogs = dao.getUrgentMaintenanceLogs(request.getCompanyId(), request.getStoreId(), request.getBizCatId(), maintenanceId, request.getSyncRecordCount());

            // �����e�i���X���O�ɑΉ�����}�X�^�f�[�^�𒊏o
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
     * ���N�G�X�g���̔z�M�t�@�C���̃o�[�W�����ƃT�[�o���Ǘ��̔z�M�t�@�C���o�[�W��������v���邩�ǂ������m�F����B
     * @param client
     * @param server
     * @return
     */
    private DataFileVersionMatchingResult verifyDataFileVersionMatching(List<DataFile> client, List<DataFile> server) {
        // ��r���Ƀ��X�g���̏����Ⴂ�Ō딻�肵�Ȃ��悤��Type�Ń\�[�g���Ă���
        Comparator<DataFile> comparator = new Comparator<DataFile>() {
            @Override
            public int compare(DataFile df1, DataFile df2) {
                return df1.getType() - df2.getType();
            }
        };
        Collections.sort(client, comparator);
        Collections.sort(server, comparator);

        if (server.isEmpty()) {
            // �z�M�t�@�C���o�[�W�����̎擾�Ɏ��s
            return DataFileVersionMatchingResult.SERVER_DATA_FILE_GET_FAILED;
        }

        if (server.size() != client.size()) {
            // �z�M�t�@�C���̐�����v���Ȃ�
            return DataFileVersionMatchingResult.DATA_FILE_COUNT_NOT_MATCH;
        }

        if (!server.equals(client)) {
            // �z�M�t�@�C���o�[�W��������v���Ȃ�
            return DataFileVersionMatchingResult.DATA_FILE_VERSION_NOT_MATCH;
        }

        return DataFileVersionMatchingResult.DATA_FILE_VERSION_MATCH;
    }

    /**
     * �z�M�t�@�C���o�[�W�����`�F�b�N�G���[�ɑΉ����郌�X�|���X�f�[�^���쐬����B
     * @param result
     * @return
     */
    private MaintenanceLogResponse createDataFileVersionErrorResponse(DataFileVersionMatchingResult result) {
        MaintenanceLogResponse response = new MaintenanceLogResponse();
        switch (result) {
            case SERVER_DATA_FILE_GET_FAILED:
                // �z�M�t�@�C���o�[�W�����̎擾�Ɏ��s
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setMessage("Failed to get the DataFile version from database.");
                response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
                return response;
            case DATA_FILE_COUNT_NOT_MATCH:
                // �z�M�t�@�C��������v���Ȃ�
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setMessage("DataFile count is not matching.");
                response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
                return response;
            case DATA_FILE_VERSION_NOT_MATCH:
            default:
                // �z�M�t�@�C���o�[�W��������v���Ȃ�
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setMessage("DataFile version is not matching.");
                response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
                return response;
        }
    }

    /**
     * �����e�i���X���O�擾�Ɏg�p���郁���e�i���XID�����肷��B
     * @param clientMaintenanceId
     * @param serverDataFiles
     * @return
     */
    private long decideUsingMaintenanceId(long clientMaintenanceId, List<DataFile> serverDataFiles) {
        long maintenanceId = clientMaintenanceId;
        long serverMaintenanceId = 0;

        for (DataFile dataFile : serverDataFiles) {
            if (serverMaintenanceId < dataFile.getMaintenanceId()) {
                // �����̔z�M�t�@�C�������݂���ꍇ�A�ő�̃����e�i���XID��ݒ肷��
                serverMaintenanceId = dataFile.getMaintenanceId();
            }
        }

        if (clientMaintenanceId < serverMaintenanceId) {
            // �z�M�t�@�C���쐬���̃����e�i���XID�̕����V�����ꍇ��
            // �z�M�t�@�C���쐬���̃����e�i���XID���g�p����
            maintenanceId = serverMaintenanceId;
        }

        return maintenanceId;
    }

    /**
     * �}�X�^�f�[�^��R�t���������e�i���X���O���擾����B
     * @param maintenanceLogs
     * @return
     */
    private List<MaintenanceLog> getMaintenanceLogsWithMasterRecords(String companyId, String storeId, List<MaintenanceLog> maintenanceLogs) throws DaoException {
        IMasterSyncDAO dao = daoFactory.getMasterSyncDAO();
        List<MaintenanceLog> logs = new LinkedList<MaintenanceLog>();

        for (MaintenanceLog log : maintenanceLogs) {
            // �}�X�^�f�[�^���o�p�����[�^���擾
            List<MasterSyncParameter> parameters = dao.getMasterSyncParameters(log.getSyncGroupId());
            for (MasterSyncParameter parameter : parameters) {
                // �}�X�^�f�[�^�𒊏o
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
                // �����e�i���X�敪���o�^�܂��͍X�V�̏ꍇ�A�R�t���}�X�^�f�[�^�����݂��Ȃ���ΘA�g�ΏۊO�Ƃ���
                continue;
            }
            logs.add(log);
        }

        return logs;
    }
}
