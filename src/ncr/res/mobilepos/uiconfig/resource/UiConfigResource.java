package ncr.res.mobilepos.uiconfig.resource;

import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.uiconfig.constants.UiConfigProperties;
import ncr.res.mobilepos.uiconfig.dao.IUiConfigCommonDAO;
import ncr.res.mobilepos.uiconfig.dao.SQLServerUiConfigCommonDAO;
import ncr.res.mobilepos.uiconfig.model.UiConfigType;
import ncr.res.mobilepos.uiconfig.model.schedule.Config;
import ncr.res.mobilepos.uiconfig.model.schedule.Deploy;
import ncr.res.mobilepos.uiconfig.model.schedule.Schedule;
import ncr.res.mobilepos.uiconfig.model.schedule.Task;
import ncr.res.mobilepos.uiconfig.model.store.CSVStore;
import ncr.res.mobilepos.uiconfig.model.store.StoreEntry;
import ncr.res.mobilepos.uiconfig.utils.StaticParameter;
import ncr.res.mobilepos.uiconfig.utils.UiConfigHelper;

import javax.ws.rs.*;
import javax.ws.rs.core.Response;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.List;

@Path("/uiconfig")
public class UiConfigResource {
    // Extensions for custom images.
    private static final String EXTENSION_CUSTOM_IMAGE_JPG = ".jpg";
    private static final String EXTENSION_CUSTOM_IMAGE_PNG = ".png";

    /**
     * UiConfigProperties
     */
    private static final UiConfigProperties configProperties = UiConfigProperties.getInstance();
    /**
     * the instance of the logger.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();
    /**
     * Trace Printer.
     */
    private Trace.Printer tp;

    /**
     * Constructor.
     */
    public UiConfigResource() {
        // Initialize trace printer, Constructor is called by each request.
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    @Path("/custom/{typeParam}")
    @GET
    @Produces({"application/javascript;charset=UTF-8"})
    /**
     * Finds appropriate config file and returns the content as String.
     */
    public final Response requestConfigFile(
            @PathParam("typeParam") final String typeParam,
            @QueryParam("companyID") final String companyID,
            @QueryParam("storeID") final String storeID,
            @QueryParam("workstationID") final String workstationID) {
        // Logs given parameters.
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
        tp.methodEnter("/uiconfig/custom/" + typeParam);
        tp.println("typeParam", typeParam)
                .println("companyID", companyID)
                .println("storeID", storeID)
                .println("workstationID", workstationID);

        // 1, Defines config type from typeParam.
        UiConfigType configType = UiConfigType.toEnum(typeParam);

        // 2, Validates parameters.
        if (UiConfigType.UNKNOWN == configType
                || UiConfigHelper.isNullOrEmpty(companyID)
                || UiConfigHelper.isNullOrEmpty(storeID)
                || UiConfigHelper.isNullOrEmpty(workstationID)) {
            tp.methodExit("Invalid parameters");
            LOGGER.logAlert(
                    this.getClass().getSimpleName(),
                    "requestConfigFile",
                    Logger.RES_EXCEP_GENERAL,
                    "Invalid parameters. configType:" + configType
                            + " companyID:" + companyID
                            + " storeID:" + storeID
                            + " workstationID:" + workstationID);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // 3, Marshals schedule.xml. This file should be provided with UTF-8 encoding.
        Schedule schedule;
        try {
            schedule = UiConfigHelper.marshallScheduleXml(configProperties.getCustomResourceBasePath() + 
            		configType.toString() + 
            		configProperties.getScheduleFilePath());
        } catch (IOException ioe) {
            tp.methodExit("schedule.xml: IOException while reading");
            LOGGER.logAlert(this.getClass().getSimpleName(),
                    "requestConfigFile",
                    Logger.RES_EXCEP_IO,
                    "schedule.xml: IOException while reading", ioe);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }
        if (schedule == null) {
            tp.methodExit("schedule.xml: Cannot be parsed");
            LOGGER.logAlert(
                    this.getClass().getSimpleName(),
                    "requestConfigFile",
                    Logger.RES_EXCEP_PARSE,
                    "schedule.xml: Cannot be parsed");
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        // 4, Gets 'Deploy' for the specific company ID.
        Deploy companyDeploy = schedule.getCompanyDeploy(companyID);
        if (companyDeploy == null) {
            tp.methodExit("schedule.xml: No <deploy> with <company><id>:" + companyID);
            LOGGER.logAlert(
                    this.getClass().getSimpleName(),
                    "requestConfigFile",
                    Logger.RES_EXCEP_FILENOTFOUND,
                    "schedule.xml: No <deploy> with <company><id>:" + companyID);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // 5, Gets 'Config' for the type. e.g. usability, options....
        Config typeConfig = companyDeploy.getResourceConfig(configType);
        if (typeConfig == null) {
            tp.methodExit("schedule.xml: No <config> with <resource>:" + configType.getValue());
            LOGGER.logAlert(
                    this.getClass().getSimpleName(),
                    "requestConfigFile",
                    Logger.RES_EXCEP_FILENOTFOUND,
                    "schedule.xml: No <config> with <resource>:" + configType.getValue());
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // 6, Loads store.csv into the memory. This should be provided with UTF-8 encoding.
//        List<CSVStore> csvStores;
//        try {
//            csvStores = UiConfigHelper.loadStoresCSV(configProperties.getStoresCsvFileFullPath());
//        } catch (IOException ioe) {
//            tp.methodExit("stores.csv: IOException while reading");
//            LOGGER.logAlert(this.getClass().getSimpleName(),
//                    "requestConfigFile",
//                    Logger.RES_EXCEP_IO,
//                    "stores.csv: IOException while reading", ioe);
//            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
//        }
        List<StoreEntry> storeEntryList;
        try {
            IUiConfigCommonDAO icmyInfoDao = new SQLServerUiConfigCommonDAO();
            storeEntryList = icmyInfoDao.getStoreEntryList(companyID);
        } catch (DaoException doe) {
        	tp.methodExit("stores.csv: DaoException while reading");
            LOGGER.logAlert(this.getClass().getSimpleName(),
                    "requestConfigFile",
                    Logger.RES_EXCEP_IO,
                    "stores.csv: DaoException while reading", doe);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
		}

        // 7, Filters Config.Tasks by StoreID, WorkStationID, and EffectiveDate.
//        List<Task> effectiveTasks = typeConfig.getValidTasks(storeID, workstationID, csvStores);
        List<Task> effectiveTasks = typeConfig.getValidTasksByDB(storeID, workstationID, storeEntryList);
        if (effectiveTasks.isEmpty()) {
            tp.methodExit("schedule.xml: No valid <task> "
                    + "configType:" + configType
                    + " companyID:" + companyID
                    + " storeID:" + storeID
                    + " workstationID:" + workstationID);
            LOGGER.logAlert(
                    this.getClass().getSimpleName(),
                    "requestConfigFile",
                    Logger.RES_EXCEP_FILENOTFOUND,
                    "schedule.xml: No valid <task>"
                            + "configType:" + configType
                            + " companyID:" + companyID
                            + " storeID:" + storeID
                            + " workstationID:" + workstationID);
            return Response.status(Response.Status.NOT_FOUND).build();
        }
        Task effectiveTask = effectiveTasks.get(0);
        if (effectiveTask.getFilename() == null) {
            tp.methodExit("schedule.xml: No <filename> in effective <task><target>");
            LOGGER.logAlert(
                    this.getClass().getSimpleName(),
                    "requestConfigFile",
                    Logger.RES_EXCEP_FILENOTFOUND,
                    "schedule.xml: No <filename> in effective <task><target>");
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // 8, Creates deploy status file. Keeps it as it is for original source, UiConfig, compatibility.
        String deployStatusPath = configProperties.getCustomResourceBasePath() + configType.toString() + configProperties.getDeployStatusFileName();
        UiConfigHelper.doDeployStatusCustomRequest(configType, companyID, storeID, workstationID, effectiveTask, deployStatusPath);

        // 9, Joins resource file path.
        String configFilePath = configProperties.getCustomResourceBasePath()
                + configType.getValue()
                + File.separator
                + effectiveTask.getFilename();
        final File configFile = new File(configFilePath);
        if (!configFile.exists()) {
            tp.methodExit("schedule.xml: Config file not found:" + configFilePath);
            LOGGER.logAlert(
                    this.getClass().getSimpleName(),
                    "requestConfigFile",
                    Logger.RES_EXCEP_FILENOTFOUND,
                    "schedule.xml: Config file not found:" + configFilePath);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // 10, Reads all the file content into String. Contents files should be provided as UTF-8 encoding.
        String configContent;
        try {
            configContent = UiConfigHelper.readFileToString(configFile, UiConfigHelper.ENCODING_CONFIG_FILE);
        } catch (IOException ioe) {
            tp.methodExit("IOException while reading custom config:" + configFilePath);
            LOGGER.logAlert(this.getClass().getSimpleName(),
                    "requestConfigFile",
                    Logger.RES_EXCEP_IO,
                    "IOException while reading custom config:" + configFilePath, ioe);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR).build();
        }

        // Successfully makes response.
        tp.methodExit("Returns " + configFilePath);
        return Response.status(Response.Status.OK).entity(configContent).build();
    }

    /**
     * Returns custom image files.
     *
     * @param filenameParam
     * @return
     */
    @Path("/custom/{typeParam}/images/{filename}")
    @GET
    @Produces({"image/png", "image/jpg"})
    public final Response requestCustomImage(
    		@PathParam("typeParam") final String typeParam,
    		@PathParam("filename") final String filenameParam) {
        // Logs given parameters.
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
        tp.methodEnter("/uiconfig/custom/images/" + filenameParam);
        tp.println("typeParam", typeParam);
        tp.println("filename", filenameParam);

        // 1, Decodes filename.
        String filename = null;
        try {
            filename = URLDecoder.decode(filenameParam, UiConfigHelper.URL_ENCODING_CHARSET);
        } catch (UnsupportedEncodingException e) {
            // This exception is never thrown.
        }

        // 2, Validates file extension, it should be png or jpg.
        if (!filename.endsWith(EXTENSION_CUSTOM_IMAGE_PNG) && !filename.endsWith(EXTENSION_CUSTOM_IMAGE_JPG)) {
            tp.methodExit("Invalid filename:" + filename);
            LOGGER.logAlert(
                    this.getClass().getSimpleName(),
                    "requestCustomImage",
                    Logger.RES_EXCEP_GENERAL,
                    "Invalid filename:" + filename);
            return Response.status(Response.Status.BAD_REQUEST).build();
        }

        // 3, Searches a file with the given name in custom base path.
        UiConfigType configType = UiConfigType.toEnum(typeParam);
        String customBasePath = configProperties.getCustomResourceBasePath() + configType.toString();
        File file = UiConfigHelper.searchImageFile(customBasePath, filename);
        if (file == null) {
            tp.methodExit("Custom image not found:" + filename);
            LOGGER.logAlert(
                    this.getClass().getSimpleName(),
                    "requestCustomImage",
                    Logger.RES_EXCEP_FILENOTFOUND,
                    "Custom image not found:" + filename);
            return Response.status(Response.Status.NOT_FOUND).build();
        }

        // 4, Returns file for the response.
        Response.ResponseBuilder rb = new ResponseBuilderImpl();
        rb.header("Content-Disposition", "attachment; filename=\"" + filename + "\"");
        rb.status(Response.Status.OK);
        rb.entity(file);
        tp.methodExit("Returns " + file.getAbsolutePath());
        return rb.build();
    }
}
