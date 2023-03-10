package ncr.res.mobilepos.mastersync.resource;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.sql.SQLException;
import java.util.Base64;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import jcifs.CIFSContext;
import jcifs.CIFSException;
import jcifs.config.PropertyConfiguration;
import jcifs.context.BaseContext;
import jcifs.smb.NtlmPasswordAuthenticator;
import jcifs.smb.SmbException;
import jcifs.smb.SmbFile;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.WindowsEnvironmentVariables;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.mastersync.dao.IMasterSyncDAO;
import ncr.res.mobilepos.mastersync.model.DataFile;
import ncr.res.mobilepos.mastersync.model.DataFileVersionMatchingResult;
import ncr.res.mobilepos.mastersync.model.Field;
import ncr.res.mobilepos.mastersync.model.MaintenanceLog;
import ncr.res.mobilepos.mastersync.model.MaintenanceLogRequest;
import ncr.res.mobilepos.mastersync.model.MaintenanceLogResponse;
import ncr.res.mobilepos.mastersync.model.MasterSyncParameter;
import ncr.res.mobilepos.mastersync.model.MasterTable;
import ncr.res.mobilepos.mastersync.model.PickListImage;
import ncr.res.mobilepos.mastersync.model.Record;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.systemconfiguration.dao.SQLServerSystemConfigDAO;

@Path("/mastersync")
@Api(value = "mastersync", description = "?????}?X?^?A?gAPI")
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
     * MaintenanceResource?????????????B
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
     * ?????????e?i???X???O?f?[?^???????????B
     * @param request ?????e?i???X???O???????N?G?X?g {@link MaintenanceLogRequest}
     * @return ?????????e?i???X???O?f?[?^ {@link MaitnenanceLogResponse}
     */
    @POST
    @Path("/maintenance")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "?????????e?i???X???O????", response = MaintenanceLogResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = ResultBase.RES_OK, message = "Success"),
            @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "?f?[?^?x?[?X?G???["),
            @ApiResponse(code = ResultBase.RES_ERROR_DAO, message = "DAO?G???["),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "???????p?????[?^"),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "???p?G???[")
    })
    public final MaintenanceLogResponse getNormalMaintenanceLog(MaintenanceLogRequest request) {
        String functionName = "getNormalMaintenanceLog";

        tp.methodEnter(functionName).println("companyId", request.getCompanyId()).println("storeId", request.getStoreId())
            .println("bizCatId", request.getBizCatId()).println("maintenanceId", request.getMaintenanceId())
            .println("syncRecordCount", request.getSyncRecordCount()).println("dataFiles", request.getDataFiles());

        MaintenanceLogResponse response = new MaintenanceLogResponse();

        if (!request.isValid()) {
            // ???N?G?X?g???e???????????????????e?i???X?f?[?^??????????????
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            response.setMessage("Invalid request parameter.");
            response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
            return response;
        }

        IMasterSyncDAO dao = null;
        try {
            dao = daoFactory.getMasterSyncDAO();

            // ?z?M?t?@?C???o?[?W?????`?F?b?N
            List<DataFile> clientDataFiles = request.getDataFiles();
            List<DataFile> serverDataFiles = dao.getDataFiles(request.getCompanyId(), request.getStoreId());

            DataFileVersionMatchingResult matchingResult = verifyDataFileVersionMatching(clientDataFiles, serverDataFiles);
            if (matchingResult != DataFileVersionMatchingResult.DATA_FILE_VERSION_MATCH) {
                // ?z?M?t?@?C???o?[?W?????????v?????????????A???v???????????????????G???[???X?|???X??????
                return createDataFileVersionErrorResponse(matchingResult);
            }

            // ?????e?i???X?f?[?^????????
            long maintenanceId = decideUsingMaintenanceId(request.getMaintenanceId(), serverDataFiles);
            List<MaintenanceLog> maintenanceLogs = dao.getNormalMaintenanceLogs(request.getCompanyId(), request.getStoreId(), request.getBizCatId(), maintenanceId, request.getSyncRecordCount());
            tp.println("MaintenanceId:", maintenanceId).println("maintenanceLogs size:", maintenanceLogs.size());
            if (maintenanceLogs.isEmpty()) {
                response.setNCRWSSResultCode(ResultBase.RES_OK);
                response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
                return response;
            }

            // ?????e?i???X???O???????????}?X?^?f?[?^?????o
            response.setMaintenanceLogs(getMaintenanceLogsWithMasterRecords(dao, request.getCompanyId(), request.getStoreId(), maintenanceLogs));

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
            dao.closeConnection();
            tp.methodExit(response);
        }

        return response;
    }

    /**
     * ???}?????????e?i???X???O?f?[?^???????????B
     * @param request ?????e?i???X???O???????N?G?X?g {@link MaintenanceLogRequest}
     * @return ???}?????????e?i???X???O?f?[?^ {@link MaintenanceLogResponse}
     */
    @POST
    @Path("/maintenance_urgent")
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    @ApiOperation(value = "???}?????????e?i???X???O????", response = MaintenanceLogResponse.class)
    @ApiResponses(value = {
            @ApiResponse(code = ResultBase.RES_OK, message = "Success"),
            @ApiResponse(code = ResultBase.RES_ERROR_DB, message = "?f?[?^?x?[?X?G???["),
            @ApiResponse(code = ResultBase.RES_ERROR_DAO, message = "DAO?G???["),
            @ApiResponse(code = ResultBase.RES_ERROR_INVALIDPARAMETER, message = "???????p?????[?^"),
            @ApiResponse(code = ResultBase.RES_ERROR_GENERAL, message = "???p?G???[")
    })
    public final MaintenanceLogResponse getUrgentMaintenanceLog(MaintenanceLogRequest request) {
        String functionName = "getUrgentMaintenanceLog";

        tp.methodEnter(functionName).println("companyId", request.getCompanyId()).println("storeId", request.getStoreId())
            .println("bizCatId", request.getBizCatId()).println("maintenanceId", request.getMaintenanceId())
            .println("syncRecordCount", request.getSyncRecordCount()).println("dataFiles", request.getDataFiles());

        MaintenanceLogResponse response = new MaintenanceLogResponse();

        if (!request.isValid()) {
            // ???N?G?X?g???e???????????????????e?i???X?f?[?^??????????????
            response.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
            response.setMessage("Invalid request parameter.");
            response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
            return response;
        }

        IMasterSyncDAO dao = null;
        try {
            dao = daoFactory.getMasterSyncDAO();

            // ?z?M?t?@?C???o?[?W?????`?F?b?N
            List<DataFile> clientDataFiles = request.getDataFiles();
            List<DataFile> serverDataFiles = dao.getDataFiles(request.getCompanyId(), request.getStoreId());

            DataFileVersionMatchingResult matchingResult = verifyDataFileVersionMatching(clientDataFiles, serverDataFiles);
            if (matchingResult != DataFileVersionMatchingResult.DATA_FILE_VERSION_MATCH) {
                // ?z?M?t?@?C???o?[?W?????????v?????????????A???v???????????????????G???[???X?|???X??????
                return createDataFileVersionErrorResponse(matchingResult);
            }

            // ?????e?i???X?f?[?^????????
            long maintenanceId = decideUsingMaintenanceId(request.getMaintenanceId(), serverDataFiles);
            List<MaintenanceLog> maintenanceLogs = dao.getUrgentMaintenanceLogs(request.getCompanyId(), request.getStoreId(), request.getBizCatId(), maintenanceId, request.getSyncRecordCount());

            // ?????e?i???X???O???????????}?X?^?f?[?^?????o
            response.setMaintenanceLogs(getMaintenanceLogsWithMasterRecords(dao, request.getCompanyId(), request.getStoreId(), maintenanceLogs));

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
            dao.closeConnection();
            tp.methodExit(response);
        }

        return response;
    }

    /**
     * ???N?G?X?g?????z?M?t?@?C?????o?[?W???????T?[?o?????????z?M?t?@?C???o?[?W?????????v???????????????m?F?????B
     * @param client
     * @param server
     * @return
     */
    private DataFileVersionMatchingResult verifyDataFileVersionMatching(List<DataFile> client, List<DataFile> server) {
        // ???r???????X?g????????????????????????????????Type???\?[?g????????
        Comparator<DataFile> comparator = new Comparator<DataFile>() {
            @Override
            public int compare(DataFile df1, DataFile df2) {
                return df1.getType() - df2.getType();
            }
        };
        Collections.sort(client, comparator);
        Collections.sort(server, comparator);

        if (server.isEmpty()) {
            // ?z?M?t?@?C???o?[?W???????????????s
            return DataFileVersionMatchingResult.SERVER_DATA_FILE_GET_FAILED;
        }

        if (server.size() != client.size()) {
            // ?z?M?t?@?C???????????v??????
            return DataFileVersionMatchingResult.DATA_FILE_COUNT_NOT_MATCH;
        }

        if (!server.equals(client)) {
            // ?z?M?t?@?C???o?[?W?????????v??????
            return DataFileVersionMatchingResult.DATA_FILE_VERSION_NOT_MATCH;
        }

        return DataFileVersionMatchingResult.DATA_FILE_VERSION_MATCH;
    }

    /**
     * ?z?M?t?@?C???o?[?W?????`?F?b?N?G???[?????????????X?|???X?f?[?^???????????B
     * @param result
     * @return
     */
    private MaintenanceLogResponse createDataFileVersionErrorResponse(DataFileVersionMatchingResult result) {
        MaintenanceLogResponse response = new MaintenanceLogResponse();
        switch (result) {
            case SERVER_DATA_FILE_GET_FAILED:
                // ?z?M?t?@?C???o?[?W???????????????s
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setMessage("Failed to get the DataFile version from database.");
                response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
                return response;
            case DATA_FILE_COUNT_NOT_MATCH:
                // ?z?M?t?@?C?????????v??????
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setMessage("DataFile count is not matching.");
                response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
                return response;
            case DATA_FILE_VERSION_NOT_MATCH:
            default:
                // ?z?M?t?@?C???o?[?W?????????v??????
                response.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                response.setMessage("DataFile version is not matching.");
                response.setResult(MaintenanceLogResponse.RESULT_MAINTENANCE_DATA_NOT_EXISTS);
                return response;
        }
    }

    /**
     * ?????e?i???X???O???????g?p?????????e?i???XID???????????B
     * @param clientMaintenanceId
     * @param serverDataFiles
     * @return
     */
    private long decideUsingMaintenanceId(long clientMaintenanceId, List<DataFile> serverDataFiles) {
        long maintenanceId = clientMaintenanceId;
        long serverMaintenanceId = 0;

        for (DataFile dataFile : serverDataFiles) {
            if (serverMaintenanceId < dataFile.getMaintenanceId()) {
                // ???????z?M?t?@?C?????????????????A???????????e?i???XID??????????
                serverMaintenanceId = dataFile.getMaintenanceId();
            }
        }

        if (clientMaintenanceId < serverMaintenanceId) {
            // ?z?M?t?@?C???????????????e?i???XID???????V??????????
            // ?z?M?t?@?C???????????????e?i???XID???g?p????
            maintenanceId = serverMaintenanceId;
        }

        return maintenanceId;
    }

    /**
     * ?}?X?^?f?[?^???R?t?????????e?i???X???O???????????B
     * @param maintenanceLogs
     * @return
     */
    private List<MaintenanceLog> getMaintenanceLogsWithMasterRecords(IMasterSyncDAO dao, String companyId, String storeId, List<MaintenanceLog> maintenanceLogs) throws CIFSException, DaoException, IOException, MalformedURLException, SmbException {
        List<MaintenanceLog> logs = new LinkedList<MaintenanceLog>();

        for (MaintenanceLog log : maintenanceLogs) {
            // ?}?X?^?f?[?^???o?p?????[?^??????
            List<MasterSyncParameter> parameters = dao.getMasterSyncParameters(log.getSyncGroupId());
            for (MasterSyncParameter parameter : parameters) {
            	tp.println("getMaintenanceLogsWithMasterRecords getMaintenanceType",log.getMaintenanceType()).println("getOutputType",parameter.getOutputType());
                // ?}?X?^?f?[?^?????o
                List<Record> records = dao.getMasterTableRecords(companyId, storeId, parameter, log);
                if (log.getMaintenanceType() != 3 && records.isEmpty()) {
                    // ?????e?i???X???????o?^???????X?V?????o???R?[?h0?????????????~?????????X?L?b?v
                    continue;
                }
                MasterTable table = new MasterTable();
                // ?}?X?^?f?[?^?o???^?C?v??1(DB)??????
                if (parameter.getOutputType() == 1) {
                    // ?}?X?^?f?[?^?????????????e?[?u??????????
                    table.setTableName(parameter.getDatabaseName() + "." + parameter.getSchemaName() + "." + parameter.getTableName());
                    tp.println("getMaintenanceLogsWithMasterRecords setTableName",parameter.getDatabaseName() + "." + parameter.getSchemaName() + "." + parameter.getTableName());
                }
                // ?}?X?^?f?[?^?o???^?C?v??2(?s?b?N???X?g)??????
                if (parameter.getOutputType() == 2) {
                    // ?s?b?N???X?g???\???????????t?@?C?????????f?B???N?g????????
                    String imageDirType = "";
                    String imageDir = "";
                    String user = "";
                    String password = "";
                    if (WindowsEnvironmentVariables.getInstance().isServerTypeEnterprise()) {
                        // Enterprise????????PRM_SYSTEM_CONFIG????????
                        SQLServerSystemConfigDAO systemDao = daoFactory.getSystemConfigDAO();
                        Map<String, String> syncConfig = systemDao.getPrmSystemConfigValue("MasterSync");
                        if (!syncConfig.isEmpty()) {
                            imageDirType = syncConfig.containsKey("ImageFileDirectoryType") ? syncConfig.get("ImageFileDirectoryType") : "";
                            imageDir     = syncConfig.containsKey("ImageFileDirectoryPath") ? syncConfig.get("ImageFileDirectoryPath") : "";
                            user         = syncConfig.containsKey("ImageFileDirectoryUser") ? syncConfig.get("ImageFileDirectoryUser") : "";
                            password     = syncConfig.containsKey("ImageFileDirectoryPassword") ? syncConfig.get("ImageFileDirectoryPassword") : "";
                        }
                    } else {
                        // HOST?????????s?b?N???X?g?z?u???f?B???N?g????????images?f?B???N?g??
                        imageDirType = "Local";
                        imageDir = parameter.getOutputPath() + "\\images";
                    }

                    // ?s?b?N???X?g???\????????????????????
                    List<PickListImage> pickListImages = getPickListImages(records, imageDirType, imageDir, user, password);
                    table.setPickListImages(pickListImages);
                    tp.println("getMaintenanceLogsWithMasterRecords setPickListImages");
                }
                table.setOutputType(parameter.getOutputType());
                table.setOutputPath(parameter.getOutputPath());
                table.setRecordCount(records.size());
                if (log.getMaintenanceType() != 3) {
                    // ?????e?i???X?????????????????????o???R?[?h???Z?b?g??????
                    table.setRecords(records);
                }
                log.addMasterTable(table);
            }
            if (log.getMasterTables().isEmpty()) {
                // ?A?g?f?[?^???S?????????????????e?i???X???O???A?g?????O??????
                continue;
            }
            logs.add(log);
        }

        return logs;
    }

    /**
     * ?s?b?N???X?g???\?????????????????????B
     * @param records
     * @return
     */
    private List<PickListImage> getPickListImages(List<Record> records, String imageDirectoryType, String imageDirectory, String user, String password) throws CIFSException, IOException, MalformedURLException, SmbException {
        tp.println("getPickListImages imageDirectoryType",imageDirectoryType).println("imageDirectory",imageDirectory).println("user",user).println("password",password);
        // ?s?b?N???X?g???\???????????t?@?C???????????s?b?N???X?g?????p???R?[?h????????
        // ?P?????S?????????????d???????P?[?X??????????HashSet???d????????
        HashSet<String> imageFiles = new HashSet<String>();
        for (Record record : records) {
            for (Field field : record.getFields()) {
                if (field.getName().equals("ImageFileName") && field.getValue() != null && !field.getValue().toString().isEmpty()) {
                    imageFiles.add(field.getValue().toString());
                }
            }
        }

        List<PickListImage> images = new LinkedList<PickListImage>();
        switch (imageDirectoryType.toLowerCase()) {
        case "local":
            // ???[?J?????f?B???N?g????????????????????????
            for (String imageFile : imageFiles) {
                // ?????t?@?C????????JSON??????????????????Base64????????????
                File file = new File(imageDirectory + "\\" + imageFile);
                byte[] contents = Files.readAllBytes(file.toPath());
                String encoded = Base64.getEncoder().encodeToString(contents);

                PickListImage image = new PickListImage();
                image.setFileName(imageFile);
                image.setContents(encoded);
                images.add(image);
            }
            break;
        case "remote":
            // ?????[?g???f?B???N?g????????????????????????
            // ?g?p????SMB???o?[?W??????????
            Properties properties = new Properties();
            properties.setProperty("jcifs.smb.client.minVersion", "SMB202");
            properties.setProperty("jcifs.smb.client.maxVersion", "SMB311");

            SmbFile remoteDir = null;

            try {
                // ?????p???F????????????
                BaseContext baseContext = new BaseContext(new PropertyConfiguration(properties));
                NtlmPasswordAuthenticator authenticator = new NtlmPasswordAuthenticator(user, password);
                CIFSContext cifsContext = baseContext.withCredentials(authenticator);

                // ?????[?g?f?B???N?g????SMB??????
                String url = ("smb:" + imageDirectory + "\\").replace("\\", "/");
                remoteDir = new SmbFile(url, cifsContext);

                // ?????[?g???f?B???N?g???????????????t?@?C??????????????
                SmbFile[] remoteFiles = remoteDir.listFiles();

                for (SmbFile remoteFile : remoteFiles) {
                    InputStream input = null;
                    ByteArrayOutputStream output = new ByteArrayOutputStream();

                    try {
                        if (remoteFile.isDirectory()) {
                            // ?f?B???N?g????????
                            continue;
                        }

                        String remoteFileName = remoteFile.getName();
                        if (!imageFiles.contains(remoteFileName)) {
                            // ?A?g???????O???t?@?C????????
                            continue;
                        }

                        // ?????t?@?C????????JSON??????????????????Base64????????????
                        input = remoteFile.getInputStream();
                        byte[] buffer = new byte[1024];
                        while (input.read(buffer) >= 0) {
                            output.write(buffer);
                        }
                        String encoded = Base64.getEncoder().encodeToString(output.toByteArray());

                        PickListImage image = new PickListImage();
                        image.setFileName(remoteFileName);
                        image.setContents(encoded);
                        images.add(image);
                    } finally {
                        if (remoteFile != null) {
                            remoteFile.close();
                        }
                        if (input != null) {
                            input.close();
                        }
                        if (output != null) {
                            output.close();
                        }
                    }
                }
            } finally {
                if (remoteDir != null) {
                    remoteDir.close();
                }
            }

            break;
        default:
            throw new IllegalArgumentException("Unknown imageDirectoryType: " + imageDirectoryType);
        }

        return images;
    }
}
