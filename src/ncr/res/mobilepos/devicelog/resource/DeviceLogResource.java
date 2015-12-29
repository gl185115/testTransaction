/**
 *
 */
package ncr.res.mobilepos.devicelog.resource;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Iterator;
import java.util.List;

import javax.naming.InitialContext;
import javax.naming.NamingException;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.constant.GlobalConstant;
import ncr.res.mobilepos.daofactory.DAOFactory;
import ncr.res.mobilepos.devicelog.dao.IDeviceLogDAO;
import ncr.res.mobilepos.devicelog.model.DeviceLog;
import ncr.res.mobilepos.devicelog.model.DeviceLogs;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DateFormatUtility;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;

/**
 * @author PB185094
 *
 */

@Path("/devicelog")
public class DeviceLogResource {


    /**
     * constant for maximum count for device log list.
     */
    private static final int TO_MAX_COUNT = 4;
    /**
     * instance of trace debug printer.
     */
    private Trace.Printer tp = null;
    /**
     * instance of IOWriter.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

    /**
     * DeviceLogPath, path to store device log files, being initialized only once.
     */
    private final String deviceLogPath = loadDeviceLogPath();

    /**
     * FastDateFormat, which is used by uploadLog() to add date to its filename.
     */
    public static final DateTimeFormatter MOBILE_DEVICE_LOG_DATEFORMAT
    						= DateTimeFormatter.ofPattern("yyyyMMddHHmmss");
    
    /**
     * MobileShop Device Log file-path template.
     * "<deviceLogPath>/MobileShop_<storeId>_<termId>_<receivedTime>.log"
     */
    public static final String MOBILE_FILE_PATH_TEMPLATE = "%s" + File.separator + "MobileShop_%s_%s_%s.log";
    
    /**
     * Clock to generate current time, which is used for filename. 
     */
    private final Clock currentTimeClock;
    
    /**
     * constructor.
     */
    public DeviceLogResource() {
	this(Clock.systemDefaultZone());
    }
    
    /**
     * constructor with given Clock.
     */
    public DeviceLogResource(Clock clock) {
	currentTimeClock = clock;
	tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }
    
    /**
     * Loads deviceLogPath from web.xml via InitialContext and lookup. If it
     * fails by throwing NamingException, it should be recorded in log.
     */
    private String loadDeviceLogPath() {
	try {
	    InitialContext initialContext = new InitialContext();
	    javax.naming.Context contextEnv = (javax.naming.Context) initialContext.lookup("java:comp/env");
	    return (String) contextEnv.lookup("deviceLogPath");
	} catch (NamingException exception) {
	    LOGGER.logError("DeviceLogResource", "DeviceLogResource", Logger.RES_EXCEP_GENERAL,
		    exception.getMessage());
	    exception.printStackTrace();
	    return "";
	}
    }

    /**
     * uploads logs from device to server
     * 
     * @param request - the servlet request
     * @param storeid - store id
     * @param termid - terminal id
     * @param data - contents of log itself
     * @return ResultBase, which contains: NCRWSSResultCode,
     *         NCRWSSExtendedResultCode and Message.
     */
    @Path("/upload")
    @POST
    @Produces({ MediaType.APPLICATION_JSON + ";charset=UTF-8" })
    public final ResultBase uploadLog(@Context final HttpServletRequest request,
	    @FormParam("storeid") final String storeId,
	    @FormParam("termid") final String termId,
	    @FormParam("data") final String data) {
	// Logs given parameters for Debug.
	tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
	tp.methodEnter("uploadLog");
	tp.println("storeid", storeId).println("termid", termId).println("data", data);

	// Instantiates return object having successful state as default.
	ResultBase result = new ResultBase();

	// Validates if given parameters are not empty.
	if ((storeId == null || termId == null || data == null) ||
		(storeId.length() == 0 || termId.length() == 0 || data.length() == 0)) {
	    // Returns as invalid parameters.
	    result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
	    result.setMessage("Invalid parameters. Parameters must not be empty.");
	    return result;
	}

	// Formats current server time.
	String currentTimeString = LocalDateTime.now(currentTimeClock).format(MOBILE_DEVICE_LOG_DATEFORMAT);
	// Combines parameters to make device-log  file path.
	String deviceLogFilePath = String.format(MOBILE_FILE_PATH_TEMPLATE,
							deviceLogPath, storeId, termId, currentTimeString);

	// Opens file output stream with append mode in case the file already exists.
	try (FileOutputStream fos = new FileOutputStream(new File(deviceLogFilePath), true);
		BufferedOutputStream logFile = new BufferedOutputStream(fos)) {
	    logFile.write(data.getBytes());
	} catch (IOException e) {
	    // Records error while writing file.
	    result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
	    result.setMessage("Failed to write file, File I/O eror.");
	    tp.println("Failed to write file, File I/O eror.");
	    e.printStackTrace();
	}

	tp.methodExit(result.getNCRWSSResultCode());
	return result;
    }

    /**
     * uploads from device to server.
     * @param request - the servlet request
     * @param udid - udid of the terminal
     * @param logDate - date of the logs that are
     * desired to be updloaded
     * @param rowId - the row id
     * @return DeviceLog model - holds details of the device log
     */

    @Path("/upload1/{udid}/{logDate}")
    @POST
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public final DeviceLog upload(
            @Context final HttpServletRequest request,
            @PathParam("udid") final String udid,
            @PathParam("logDate") final String logDate,
            @QueryParam("rowId") final String rowId) {

        DeviceLog result = new DeviceLog();

        tp.methodEnter("upload");
        tp.println("rowId", rowId)
            .println("udid", udid)
            .println("logDate", logDate)
            .println("request", (request != null)
                    ? request.toString() : null);

        // Check if request is multipart/form-data
        if (null == request || null == request.getContentType()
                || !ServletFileUpload.isMultipartContent(request)) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage("Invalid request."
                    + " Requires 'multipart/form-data' media type.");
            tp.methodExit("Invalid request."
                    + " Requires 'multipart/form-data' media type.");
            return result;
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Create a new file upload handler
        ServletFileUpload upload = new ServletFileUpload(factory);

        try {
            @SuppressWarnings("unchecked")
            List<FileItem> items = upload.parseRequest(request);
            Iterator<FileItem> iter = items.iterator();

            FileItem logFileItem = null;

            // get log_file field
            while (iter.hasNext()) {
                FileItem item = iter.next();
                if (!item.isFormField()
                        && "log_file".equalsIgnoreCase(item.getFieldName())) {
                    logFileItem = item;
                    break;
                }
            }

            // if 'log_file' file field is not in the request, return error
            if (null == logFileItem) {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                result.setMessage("Invalid request."
                        + " Requires 'log_file' file field.");
                tp.println("Invalid request."
                        + " Requires 'log_file' file field.");
			} else if ((null == udid || null == logDate || udid.isEmpty() || logDate
					.isEmpty()) && (null == rowId || rowId.isEmpty())) {
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
				result.setMessage("Invalid request."
						+ " Requires universal device ID and log date.");
				tp.println("Invalid request."
						+ " Requires universal device ID and log date.");
			} else {
				DAOFactory daoFactory = DAOFactory
						.getDAOFactory(DAOFactory.SQLSERVER);
				IDeviceLogDAO deviceLog = daoFactory.getDeviceLogDao();

				// If rowId is supplied, ignore udid
				// and logDate, update the log file pointed by rowId.
				SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
				result = (null == rowId || rowId.isEmpty()) ? deviceLog
						.saveLogFile(udid, dateFormat.parse(logDate),
								logFileItem) : deviceLog.updateLogFile(rowId,
						logFileItem);
			}

        } catch (DaoException e) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            result.setMessage(e.getMessage());
        } catch (FileUploadException e) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(e.getMessage());
        } catch (ParseException e) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(e.getMessage()
                    + ": Invalid log date format. Expected format: yyyyMMdd.");
        } finally {
            tp.methodExit(result.toString());
        }
        return result;
    }

    /**
     * uploads from device to server.
     * @param request - the servlet request
     * @param storeid - store id of terminal
     * @param termid - id of terminal
     * @param logDate - date of the logs that are
     * @param seqnum - sign log sequence number
     * @return DeviceLog model - holds details of the device log
     */
    @Path("/upload/{storeid}/{termid}/{logDate}")
    @POST
    @Produces(MediaType.APPLICATION_ATOM_XML)
    @Consumes(MediaType.MULTIPART_FORM_DATA)
    public final ResultBase uploadFile(
            @Context final HttpServletRequest request,
            @PathParam("storeid") final String storeid,
            @PathParam("termid") final String termid,
            @PathParam("logDate") final String logDate,
            @QueryParam("seqnum") final String seqnum) {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                getClass());
        tp.methodEnter("uploadFile");
        tp.println("storeid", storeid).println("termid", termid)
        .println("logDate", logDate).println("seqnum", seqnum);

        ResultBase result = new ResultBase();

        do {
            // Check if request is multipart/form-data
            if (null == request.getContentType()
                    || !ServletFileUpload.isMultipartContent(request)) {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                result.setMessage("Invalid request."
                        + " Requires 'multipart/form-data' media type.");
                break;
            }

            // Check the parameters
            if (storeid == null || "".equals(storeid)
                    || termid == null || "".equals(termid)
                    || logDate == null || "".equals(logDate)) {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                result.setMessage("Invalid parameters.");
                break;
            }

            // Check the format of parameter Log date
            if (!DateFormatUtility.isLegalFormat(logDate, "yyyyMMdd")) {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                result.setMessage("Invalid parameters. LogDate is not in format"
                        + " yyyyMMdd");
                break;
            }

            DiskFileItemFactory factory = new DiskFileItemFactory();
            ServletFileUpload upload = new ServletFileUpload(factory);

            try {
                @SuppressWarnings("unchecked")
                List<FileItem> items = upload.parseRequest(request);
                Iterator<FileItem> iter = items.iterator();

                FileItem logFileItem = null;
                StringBuilder fileName = new StringBuilder();
                    fileName.append(GlobalConstant.getCorpid())
                    .append("_").append(storeid).append("_").append(termid)
                    .append("_").append(logDate);

                // get log_file field
                while (iter.hasNext()) {
                    FileItem item = iter.next();
                    if (!item.isFormField()) {
                        if ("log_file".equalsIgnoreCase(item.getFieldName())) {
                            logFileItem = item;
                            fileName.append(".txt");
                            break;
                        } else if (("sign_log_file".equalsIgnoreCase(item.getFieldName()))
                                && (seqnum != null) 
                                && (!"".equals(seqnum))) {
                                logFileItem = item;
                                fileName.append("_")
                                .append(seqnum).append("_SIGN.txt");
                                break;

                        }
                    }
                }

                if (null == logFileItem) {
                    result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                    result.setMessage("Invalid request."
                            + "Requires 'log_file' or + "
                            + "'sign_log_file' file field.");
                } else {
                    // Get device log path
                    javax.naming.Context env =
                            (javax.naming.Context) new InitialContext()
                            .lookup("java:comp/env");
                    String path = (String) env.lookup("deviceLogPath");
                    String target = path + "\\" + fileName;
                    File directory = new File(path);
                    if (!directory.exists() && (!directory.mkdirs())) {
                            result.setNCRWSSResultCode(
                                    ResultBase.RES_ERROR_GENERAL);
                            result.setMessage("It's failed to make directory.");
                            tp.println("It is failed to make directory.");
                    }
                    logFileItem.write(new File(target));
                }
            } catch (FileUploadException e) {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                result.setMessage(e.getMessage());
                LOGGER.logAlert("DeviceLogResource", "upload",
                        Logger.RES_EXCEP_GENERAL, e.getMessage());
            } catch (Exception e) {
                result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
                result.setMessage(e.getMessage());
                LOGGER.logAlert("DeviceLogResource", "upload",
                        Logger.RES_EXCEP_GENERAL, e.getMessage());
            }
        } while (false);
        tp.methodExit(result.getNCRWSSResultCode());
        return result;
    }

    /**
     * gets the list of logs.
     * @param udid - udid of the terminal
     * @param startIndex - the index to start returning
     * @param count - the number of indexes that are to be
     * returned
     * @return DeviceLogs - holds an array of DeviceLog
     * models
     */
    @Path("/getloglist/{udid}")
    @GET
    @Produces(MediaType.APPLICATION_ATOM_XML)
    public final DeviceLogs getDeviceLogs(
            @PathParam("udid") final String udid,
            @QueryParam("startIndex") final int startIndex,
            @QueryParam("count") final int count) {

        tp.methodEnter("getDeviceLogs");
        tp.println("udid", udid)
            .println("startIndex", Integer.toString(startIndex))
            .println("count", Integer.toString(count));

        DeviceLogs result = new DeviceLogs();
        DeviceLog[] logs = null;

        int starti = (0 > startIndex) ? 0 :  startIndex;
        int endi =  (0 >= count) ? starti + TO_MAX_COUNT
                : starti + count - 1;

        if (null == udid || udid.isEmpty()) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage("Universal device ID is empty.");
            tp.methodExit("Universal device ID is empty.");
            return result;
        }

        try {
            DAOFactory daoFactory =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IDeviceLogDAO deviceLog = daoFactory.getDeviceLogDao();

            logs = deviceLog.getLogsOfDevice(udid, starti, endi);

            result.setUdid(udid);
            result.setCount(logs.length);
            result.setStartIndex(starti);
            result.setEndIndex((0 == logs.length)
                    ? starti : (starti + logs.length - 1));
            result.setLogs(logs);

        } catch (DaoException e) {
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            result.setMessage(e.getMessage());
        } finally {
            tp.methodExit(result.toString());
        }

        return result;
    }

    /**
     * downloads a log file.
     * @param rowId - the row id
     * @param asPlainText - flag to tell whether
     * the data would be returned as plain text or not
     * @return Response - holds the response. part of
     * javax.core
     */
    @Path("/download/{rowId}")
    @GET
    public final Response downloadLogFile(
            @PathParam("rowId") final String rowId,
            @QueryParam("asPlainText") final boolean asPlainText) {

        tp.methodEnter("downloadLogFile");
        tp.println("rowId", rowId)
            .println("asPlainText", asPlainText);

        /**
         * class for device log output
         *
         */
        class DeviceLogSteamingOutput implements StreamingOutput {
            /**
             * class instance of the row id
             */
            private String rowId;
            /**
             * constructor.
             * @param rowid - the row id to set
             */
            public DeviceLogSteamingOutput(final String rowid) {
                this.rowId = rowid;
            }

            @Override
            public void write(final OutputStream arg0)
            throws IOException {
                try {
                    DAOFactory daoFactory =
                        DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
                    IDeviceLogDAO deviceLog = daoFactory.getDeviceLogDao();
                    deviceLog.readBinaryData(this.rowId, arg0);
                } catch (DaoException e) {
                    arg0.write(e.getMessage()
                            .getBytes(Charset.forName("UTF-8")));
                }
            }
        }

        Response res = null;
        DeviceLog deviceLogInfo = null;
        StreamingOutput stream = null;

        try {
            DAOFactory daoFactory =
                DAOFactory.getDAOFactory(DAOFactory.SQLSERVER);
            IDeviceLogDAO deviceLog = daoFactory.getDeviceLogDao();
            deviceLogInfo = deviceLog.getDeviceLogInfo(rowId);
            if (null == deviceLogInfo.getRowId()
                    || deviceLogInfo.getRowId().isEmpty()) {
                res = (asPlainText)
                        ? Response.ok(deviceLogInfo.toString(),
                                MediaType.TEXT_PLAIN).build()
                        : Response.ok(deviceLogInfo,
                                MediaType.APPLICATION_ATOM_XML).build();
            } else {
                SimpleDateFormat dateFormat =
                    new SimpleDateFormat("yyyyMMdd");
                String logFileName = deviceLogInfo.getUdid()
                    + "_" + dateFormat.format(deviceLogInfo.getLogDate())
                    + ".log";
                stream = new DeviceLogSteamingOutput(rowId);
                res = (asPlainText)
                        ? Response
                            .ok(stream)
                            .header("content-type", "text/plain; charset=utf-8")
                            .build()
                        : Response
                            .ok(stream, MediaType.APPLICATION_OCTET_STREAM)
                            .header("content-disposition",
                                    "attachment; filename = " + logFileName)
                            .build();
            }

        } catch (DaoException e) {
            deviceLogInfo = new DeviceLog();
            deviceLogInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DAO);
            deviceLogInfo.setMessage(e.getMessage());
            res = (asPlainText)
                    ? Response.ok(deviceLogInfo.toString(),
                            MediaType.TEXT_PLAIN).build()
                    : Response.ok(deviceLogInfo,
                            MediaType.APPLICATION_ATOM_XML).build();
        } finally {
            String responseString = "";

            if (res != null) {
                responseString = res.toString();
            }

            tp.methodExit(responseString);
        }
        return res;
    }

}
