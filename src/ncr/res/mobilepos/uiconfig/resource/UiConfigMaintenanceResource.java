/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * JournalizationResource
 *
 * Resource which provides Web Service for journalizing transaction
 *
 * Campos, Carlos
 */

package ncr.res.mobilepos.uiconfig.resource;

import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletRequest;
import javax.ws.rs.Consumes;
import javax.ws.rs.FormParam;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Response;

import org.apache.tomcat.util.http.fileupload.FileItemIterator;
import org.apache.tomcat.util.http.fileupload.FileItemStream;
import org.apache.tomcat.util.http.fileupload.disk.DiskFileItemFactory;
import org.apache.tomcat.util.http.fileupload.servlet.ServletFileUpload;
import org.apache.tomcat.util.http.fileupload.util.Streams;

import com.sun.jersey.core.spi.factory.ResponseBuilderImpl;
import com.wordnik.swagger.annotations.Api;
import com.wordnik.swagger.annotations.ApiOperation;
import com.wordnik.swagger.annotations.ApiParam;
import com.wordnik.swagger.annotations.ApiResponse;
import com.wordnik.swagger.annotations.ApiResponses;

import atg.taglib.json.util.JSONArray;
import ncr.realgate.util.Trace;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.uiconfig.constants.UiConfigProperties;
import ncr.res.mobilepos.uiconfig.dao.IUiConfigCommonDAO;
import ncr.res.mobilepos.uiconfig.dao.SQLServerUiConfigCommonDAO;
import ncr.res.mobilepos.uiconfig.model.UiConfigType;
import ncr.res.mobilepos.uiconfig.model.fileInfo.FileDownLoadInfo;
import ncr.res.mobilepos.uiconfig.model.fileInfo.FileInfo;
import ncr.res.mobilepos.uiconfig.model.fileInfo.FileInfoList;
import ncr.res.mobilepos.uiconfig.model.fileInfo.FileRemove;
import ncr.res.mobilepos.uiconfig.model.fileInfo.FileRemoveInfo;
import ncr.res.mobilepos.uiconfig.model.fileInfo.PictureInfoList;
import ncr.res.mobilepos.uiconfig.model.fileInfo.PictureInfoUpload;
import ncr.res.mobilepos.uiconfig.model.schedule.CompanyInfo;
import ncr.res.mobilepos.uiconfig.model.schedule.CompanyInfoList;
import ncr.res.mobilepos.uiconfig.model.schedule.Config;
import ncr.res.mobilepos.uiconfig.model.schedule.Deploy;
import ncr.res.mobilepos.uiconfig.model.schedule.GetScheduleInfo;
import ncr.res.mobilepos.uiconfig.model.schedule.Schedule;
import ncr.res.mobilepos.uiconfig.model.schedule.Task;
import ncr.res.mobilepos.uiconfig.model.store.StoreEntry;
import ncr.res.mobilepos.uiconfig.model.store.TableStore;
import ncr.res.mobilepos.uiconfig.model.store.TableStoreList;
import ncr.res.mobilepos.uiconfig.utils.FileUtil;
import ncr.res.mobilepos.uiconfig.utils.ImageScalr;
import ncr.res.mobilepos.uiconfig.utils.ScheduleXmlUtil;
import ncr.res.mobilepos.uiconfig.utils.StaticParameter;
import ncr.res.mobilepos.uiconfig.utils.UiConfigHelper;


/**
 * uiconfigMaintenance Web Resource Class.
 *
 * <P>uiconfigMaintenance.
 *
 */
@Path("/uiconfigMaintenance")
@Api(value="/uiconfigMaintenance", description="カスタムメンテナンスリソースAPI")
public class UiConfigMaintenanceResource {
    // Extensions for custom images.
    private static final String EXTENSION_CUSTOM_IMAGE_JPG = ".jpg";
    private static final String EXTENSION_CUSTOM_IMAGE_PNG = ".png";

	/**
     * ClassSimpleName
     */
	private final String PROG_NAME = "UCMT";
	
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
    private Trace.Printer tp = null;

    /**
     * Default Constructor for JournalizationResource.
     *
     * <P>Initializes the logger object.
     */
    public UiConfigMaintenanceResource() {
        // Initialize trace printer, Constructor is called by each request.
        this.tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
    }

    /**
     * Returns custom image files.
     *
     * @param filenameParam
     * @return
     */
    @Path("/custom/{companyID}/{typeParam}/images/{filename}")
    @GET
    @Produces({"image/png", "image/jpg"})
	@ApiOperation(value="カスタムイメージファイル取得", response=Response.class)
	public final Response requestCustomImage(
			@ApiParam(name="companyID", value="企業コード") @PathParam("companyID") final String companyID,
			@ApiParam(name="typeParam", value="リソースタイプ") @PathParam("typeParam") final String typeParam,
			@ApiParam(name="filename", value="ファイル名") @PathParam("filename") final String filenameParam) {
        // Logs given parameters.
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(), getClass());
        tp.methodEnter("/uiconfigMaintenance/custom/" + companyID + "/" + typeParam + "/images/" + filenameParam);
        tp.println("companyID", companyID);
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
        String customBasePath = configProperties.getCustomMaintenanceBasePath() +
        		companyID +
        		StaticParameter.str_separator +
        		configType.toString();
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
    
    @Path("/getcompanyinfo")
    @POST
    @Produces({"application/json;charset=UTF-8"})
	public final CompanyInfoList getCompanyInfo() {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/getcompanyinfo/");
		
		CompanyInfoList companyInfo = null;
		List<CompanyInfo> cmpList = null;
		try {
			companyInfo = new CompanyInfoList();
			IUiConfigCommonDAO icmyInfoDao = new SQLServerUiConfigCommonDAO();
			cmpList = icmyInfoDao.getCompanyInfo();
			if(cmpList == null) {
				tp.println("Failed to No Data Found.");
				LOGGER.logAlert(PROG_NAME,
						functionName,
						Logger.RES_EXCEP_NODATAFOUND,
						"Failed to get company information.");
				companyInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
				companyInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
				companyInfo.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return companyInfo;
			} else {
				companyInfo.setCompanyInfo(cmpList);
			}
		} catch (DaoException ex) {
			tp.println("Failed to get company information.");
			LOGGER.logAlert(PROG_NAME,
					Logger.RES_EXCEP_DAO,
					functionName + ":Failed to get company information.",ex);
			companyInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			companyInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
			companyInfo.setMessage(ex.getMessage());
		} catch(Exception ex) {
			tp.println("Failed to get company information.");
			LOGGER.logAlert(PROG_NAME,
					Logger.RES_EXCEP_GENERAL,
					functionName + ":Failed to get company information.",
					ex);
			companyInfo.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			companyInfo.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			companyInfo.setMessage(ex.getMessage());
        } finally {
        	tp.methodExit(companyInfo.toString());
        }
		return companyInfo;
    }

    @Path("/fileUpload")
    @POST
    @Produces({"application/json;charset=UTF-8"})
	public final ResultBase requestFileUpload(@FormParam("folder") final String folder,
			@FormParam("contents") final String contents,
			@FormParam("desfilename") final String desfilename,
			@FormParam("overwrite") final String overwrite,
			@FormParam("picturename") final String picturename,
			@FormParam("expire") final String expire,
			@FormParam("title") final String title,
			@FormParam("title2") final String title2,
			@FormParam("companyID") final String companyID) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/fileUpload");
		tp.println("folder", folder).println("contents", contents).println("desfilename", desfilename)
				.println("overwrite", overwrite).println("picturename", picturename).println("expire", expire)
				.println("title", title).println("title2", title2).println("companyID", companyID);

		HashMap<String, String> ref = new HashMap<String, String>();
		ref.put("title", title);
		ref.put("title2", title2);
		ref.put("expire", expire);
		ref.put("contents", contents);
		ref.put("picturename", picturename);
		ref.put("folder", folder);
		ref.put("desfilename", desfilename);
		ref.put("overwrite", overwrite);

		ResultBase result = new ResultBase();
		try {
			String url = configProperties.getCustomMaintenanceBasePath() + companyID + StaticParameter.str_separator;
			File uploadDir = new File(url, folder);
			if (!uploadDir.exists()) {
				tp.println(uploadDir.getPath() + ":" + "directory does not exist");
				if (uploadDir.mkdirs()) {
					tp.println("Create a" + uploadDir.getPath() + "directory");
					LOGGER.logAlert(PROG_NAME,
							functionName,
							Logger.RES_EXCEP_GENERAL,
							"Create a" + uploadDir.getPath() + "directory");
					result.setNCRWSSResultCode(ResultBase.RES_ERROR_TXNOTFOUND);
					result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_TXNOTFOUND);
					result.setMessage(ResultBase.RES_FAILED_MSG);
					return result;
				}
			}

			String fname = "";
			if (StaticParameter.res_false.equalsIgnoreCase(overwrite)) {
				for (File file : uploadDir.listFiles()) {
					if (file.isFile()) {
						fname = file.getName();
						if ((fname.equalsIgnoreCase(desfilename))) {
							tp.println(desfilename + ":" + "file already exists");
							LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_EXISTS, "file already exists");
							result.setNCRWSSResultCode(ResultBase.RES_FILE_ALREADY_EXIST);
							result.setNCRWSSExtendedResultCode(ResultBase.RES_FILE_ALREADY_EXIST);
							result.setMessage(ResultBase.RES_EXISTS_MSG);
							return result;
						}
					}
				}
			}

			File saveFile = new File(uploadDir, desfilename);
			if (saveFile.exists()) {
				if (saveFile.isFile()) {
					if (!saveFile.canWrite()) {
						if (!saveFile.setWritable(true)) {
							tp.println("Set file to writable failed ");
						}
					}
				}
			}

			String fileContent = ref.get("contents");
			if (StaticParameter.key_notices.equalsIgnoreCase(folder)) {
				fileContent = getNoticeJson(ref);
			} else if (StaticParameter.key_pickList.equalsIgnoreCase(folder)) {
				if (fileContent.contains("\\\\\\\\")) {
					fileContent = fileContent.replace("\\\\\\\\", StaticParameter.str_separator);
				}

				if (fileContent.contains("\\\\")) {
					fileContent = fileContent.replace("\\\\", StaticParameter.str_separator);
				}
			} else if (StaticParameter.key_advertise.equalsIgnoreCase(folder)) {
				if (fileContent.contains("\\\\\\\\")) {
					fileContent = fileContent.replace("\\\\\\\\", StaticParameter.str_separator);
				}

				if (fileContent.contains("\\\\")) {
					fileContent = fileContent.replace("\\\\", StaticParameter.str_separator);
				}
			}

			if (FileUtil.fileSave(saveFile, fileContent, false, UiConfigHelper.ENCODING_CONFIG_FILE)) {
				tp.println("File saved successfully");
				LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_SUCCESS, "File saved successfully");
				result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
				result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
				result.setMessage(ResultBase.RES_SUCCESSFULL_MSG);
				return result;
			} else {
				tp.println("File save failed ");
				LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_FILESAVEFAILED, "File saved failed ");
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
				result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
				result.setMessage(ResultBase.RES_FAILED_MSG);
				return result;
			}

		} catch (Exception e) {
			tp.println("Failed to requestFileUpload.");
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ":Failed to requestFileUpload.", e);
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setMessage(e.getMessage());
		} finally {
			tp.methodExit(result.toString());
		}
		return result;
	}

	@Path("/fileList")
	@POST
	@Produces({"application/json;charset=UTF-8"})
	@ApiOperation(value="カスタムファイルリスト取得", response=FileInfoList.class)
	@ApiResponses(value={
			@ApiResponse(code=ResultBase.RES_ERROR_FILENOTFOUND, message="ファイル未検出エラー"),
			@ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
	})
	public final FileInfoList requestConfigFileList(
			@ApiParam(name="companyID", value="企業コード") @FormParam("companyID") final String companyID,
			@ApiParam(name="folder", value="フォルダ") @FormParam("folder") final String folder){

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/fileList");
		tp.println("companyID", companyID);
		tp.println("folder", folder);

		FileInfoList result = new FileInfoList();
		try {
			String url = configProperties.getCustomMaintenanceBasePath() + companyID + StaticParameter.str_separator;
			File resourceDir = new File(url, folder);
			File[] fileList = null;

			if (resourceDir.exists()) {
				fileList = resourceDir.listFiles();

				if (fileList.length == 0) {
					tp.println("The message id of file Not found Exception.");
		            LOGGER.logAlert(PROG_NAME,
		            		functionName,
		                    Logger.RES_EXCEP_FILEEMPTY,
		                    "The message id of file is empty.");
		            result.setNCRWSSResultCode(ResultBase.RESRPT_EMPTY);
					result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_EMPTY);
					result.setMessage(ResultBase.RES_FAILED_MSG);
					return result;
				}
				
				List<FileInfo> fileInfoList = new ArrayList<FileInfo>();
				FileInfo fileInfo = null;
				for (File file : fileList) {
					if (file.isFile()) {
						String filename = file.getName();
						if (filename.toLowerCase().endsWith(StaticParameter.file_js)) {
							fileInfo = new FileInfo();
							fileInfo.setFileName(filename);
							fileInfoList.add(fileInfo);
						}
					}
				}
				
				result.setFileInfoList(fileInfoList);
			} else {
				tp.println("The message id of Not found file Exception.");
	            LOGGER.logAlert(PROG_NAME,
	                    functionName,
	                    Logger.RES_EXCEP_FILENOTFOUND,
	                    "The message id of Not found file Exception.");
	            result.setNCRWSSResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
				result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
				result.setMessage(ResultBase.RES_FAILED_MSG);
				return result;
			}

		} catch (Exception e) {
			tp.println("Failed to requestFileList.");
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ":Failed to requestFileList.", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setMessage(e.getMessage());
		} finally {
			tp.methodExit(result.toString());
		}
		
		return result;
	}
    
	@Path("/fileDownload")
	@POST
	@Produces({"application/json;charset=UTF-8"})
	@ApiOperation(value="リソースファイルダウンロード", response=FileDownLoadInfo.class)
	@ApiResponses(value={
			@ApiResponse(code=ResultBase.RES_ERROR_FILENOTFOUND, message="ファイル未検出エラー"),
			@ApiResponse(code=ResultBase.RES_ERROR_GENERAL, message="汎用エラー"),
	})
	public final FileDownLoadInfo requestConfigFileDownload(
			@ApiParam(name="folder", value="フォルダ") @FormParam("folder") final String folder,
			@ApiParam(name="filename", value="ファイル名") @FormParam("filename") final String filename,
			@ApiParam(name="companyID", value="企業コード") @FormParam("companyID") final String companyID){

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/fileDownload");
		tp.println("folder", folder)
		  .println("filename", filename)
		  .println("companyID", companyID);
		
		FileDownLoadInfo result = new FileDownLoadInfo();
		try {
			String url = configProperties.getCustomMaintenanceBasePath() + companyID + StaticParameter.str_separator;
			File loadFolder = new File(url, folder);
			File loadFile = new File(loadFolder, filename);

			if (loadFile.exists()) {
				if (loadFile.isDirectory()) {
					tp.println("The message id of Not file Exception.");
		            LOGGER.logAlert(PROG_NAME,
		            		functionName,
		                    Logger.RES_EXCEP_FILENOTFOUND,
		                    "The message id of Not file Exception.");
		            result.setNCRWSSResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
					result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
					result.setMessage(ResultBase.RES_FAILED_MSG);
					return result;
				} else {
					String fileContent = FileUtil.fileRead(loadFile);
					result.setStatus(StaticParameter.res_success);
					result.setResult(fileContent);
					return result;
				}
			} else {
				tp.println("The message id of file Not found Exception.");
	            LOGGER.logAlert(PROG_NAME,
	            		functionName,
	                    Logger.RES_EXCEP_FILENOTFOUND,
	                    "The message id of file Not found Exception.");
	            result.setNCRWSSResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
				result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_FILENOTFOUND);
				result.setMessage(ResultBase.RES_FAILED_MSG);
				return result;
			}

		} catch (Exception e) {
			tp.println("Failed to requestFileDownload.");
            LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ":Failed to requestFileDownload.", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setMessage(e.getMessage());
		} finally {
			tp.methodExit(result.toString());
		}

		return result;
	}
	
    @Path("/getDeployStoreAndGroup")
    @POST
    @Produces({"application/json;charset=UTF-8"})
	public final TableStoreList getDeployStoreAndGroup(@FormParam("companyID") final String companyID) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/getDeployStoreAndGroup/");
		tp.println("companyID", companyID);
		
		TableStoreList result = null;
		try {
			result = new TableStoreList();
			IUiConfigCommonDAO icmyInfoDao = new SQLServerUiConfigCommonDAO();
			List<StoreEntry> storeEntryList = icmyInfoDao.getStoreEntryList(companyID);
			if(storeEntryList == null) {
				tp.println("Failed to No Data Found.");
				LOGGER.logAlert(PROG_NAME,
						functionName,
						Logger.RES_EXCEP_NODATAFOUND,
						"Failed to get StoreAndGroup information.");
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_NODATAFOUND);
				result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_NODATAFOUND);
				result.setMessage(ResultBase.RES_NODATAFOUND_MSG);
                return result;
			} else {
				TableStore tableStore = null;
				List<TableStore> tableStoreList = new ArrayList<TableStore>();
				
				tableStore = new TableStore();
				tableStore.setLevelKey(StaticParameter.key_all);
				tableStore.setCategory(StaticParameter.key_all_str);
				tableStore.setStoreEntries(storeEntryList);
				tableStoreList.add(tableStore);
				
				tableStore = new TableStore();
				tableStore.setLevelKey(StaticParameter.key_store);
				tableStore.setCategory(StaticParameter.key_store_str);
				tableStore.setStoreEntries(storeEntryList);
				tableStoreList.add(tableStore);
				
				result.setTableStore(tableStoreList);
			}
		} catch (DaoException ex) {
			tp.println("Failed to get StoreAndGroup information.");
			LOGGER.logAlert(PROG_NAME,
					Logger.RES_EXCEP_DAO,
					functionName + ":Failed to get StoreAndGroup information.",ex);
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_DB);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_DB);
			result.setMessage(ex.getMessage());
		} catch(Exception ex) {
			tp.println("Failed to get StoreAndGroup information.");
			LOGGER.logAlert(PROG_NAME,
					Logger.RES_EXCEP_GENERAL,
					functionName + ":Failed to get StoreAndGroup information.",ex);
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setMessage(ex.getMessage());
        } finally {
        	tp.methodExit(result.toString());
        }
		return result;
    }
    
	private String getNoticeJson(HashMap<String, String> ref) {

		StringBuilder builder = new StringBuilder();
		builder.append("/**").append(StaticParameter.str_enter);
		builder.append(" * template for operational notices").append(StaticParameter.str_enter);
		builder.append(" */").append(StaticParameter.str_enter);
		builder.append("res.config.notices[0] = ").append(StaticParameter.str_enter);
		builder.append("\t{").append(StaticParameter.str_enter);
		builder.append("\t\tdate : \"" + new SimpleDateFormat(StaticParameter.format_yyyyMMddHHmmss).format(new Date())
				+ "\",").append(StaticParameter.str_enter);
		builder.append("\t\tfilename : \"" + ref.get("filename") + "\",").append(StaticParameter.str_enter);
		builder.append("\t\ttitle : \"" + ref.get("title") + "\",").append(StaticParameter.str_enter);
		builder.append("\t\ttitle2 : \"" + ref.get("title2") + "\",").append(StaticParameter.str_enter);
		builder.append("\t\texpire : \"" + ref.get("expire") + "\",").append(StaticParameter.str_enter);
		builder.append("\t\tbody : \"" + ref.get("contents") + "\",").append(StaticParameter.str_enter);
		builder.append("\t\tattachment : \"" + ref.get("picturename") + "\",").append(StaticParameter.str_enter);
		builder.append("\t};").append(StaticParameter.str_enter);

		return builder.toString();
	}

	@Path("/pictureList")
	@POST
	@Produces({ "application/json;charset=UTF-8" })
	public final PictureInfoList PictureListServlet(
			@FormParam("folder") final String folder,
			@FormParam("sizeType") final int sizeType,
			@FormParam("companyID") final String companyID) {
		// Logs given parameters.
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/pictureList");
		tp.println("folder", folder).
		println("sizeType", sizeType).
		println("companyID", companyID);

		PictureInfoList result = new PictureInfoList();

		try {
			String url = configProperties.getCustomMaintenanceBasePath() + companyID + StaticParameter.str_separator;
			File dir_resource = new File(url, folder);
			
			if (dir_resource.exists()) {
				String baseDir = dir_resource.getPath();
				List<String> filelist = new ArrayList<String>();
				GetFolderList(baseDir, filelist, true, sizeType);
				if (filelist.isEmpty()) {
					tp.println(functionName + "There is no files in " + folder);
					LOGGER.logAlert(PROG_NAME,
							functionName,
							Logger.RES_EXCEP_EXISTS,
							functionName + "There is no files in " + folder);
	                return result;
				} else {
					List<String> list = new ArrayList<String>();
					for (String path : filelist) {
						path = path.substring(baseDir.length() + 1);
						path = path.replace("\\", StaticParameter.str_separator);
						list.add(path);
					}
					
					result.setResult(list);
					return result;
				}
			} else {
				dir_resource.mkdirs();
				tp.println("create folder :" + folder);
				LOGGER.logAlert(PROG_NAME,
						functionName,
						Logger.RES_EXCEP_GENERAL,
						"create folder :" + folder);
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_TXNOTFOUND);
				result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_TXNOTFOUND);
				result.setMessage(ResultBase.RES_FAILED_MSG);
				return result;
			}

		} catch (Exception e) {
			tp.println("Failed to requestPictureList.");
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ":Failed to requestPictureList.", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setMessage(e.getMessage());
		} finally {
			tp.methodExit(result.toString());
		}
		return result;

	}

	private void GetFolderList(String sDir, List<String> sList, boolean bRecurs, int sizeType) {
		File subFileDir = null;
		File fileDir = new File(sDir);
		if (!fileDir.exists()) {
			tp.println("Failed to requestPictureList.");
		} else {
			String[] names = fileDir.list();
			for (String name : names) {
				subFileDir = new File(sDir, name);
				if (subFileDir.isDirectory()) {
					if (bRecurs) {
						GetFolderList(sDir + "\\" + name, sList, true, sizeType);
					}
				} else if (subFileDir.isFile() && (name.toLowerCase().endsWith(".png")
						|| name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".bmp")
						|| name.toLowerCase().endsWith(".gif") || name.toLowerCase().endsWith(".jpeg"))) {
					if (sizeType == 0) {
						sList.add(subFileDir.getPath());
					} else {
						BufferedImage sourceImg = null;
						int pictureWidth = -1;
						try {
							sourceImg = ImageIO.read(subFileDir);
							pictureWidth = sourceImg.getWidth();
							sourceImg = null;
							if(sizeType == 1){
								if(pictureWidth < 1020){
									sList.add(subFileDir.getPath());
								} else {
									continue;
								}
							} else {
								if(pictureWidth >= 1020){
									sList.add(subFileDir.getPath());
								} else {
									continue;
								}
							}
						} catch (FileNotFoundException e) {
							tp.println("Failed to GetFolderList.");
							sourceImg = null;
							continue;
						} catch (IOException e) {
							tp.println("Failed to GetFolderList.");
							sourceImg = null;
							continue;
						}
					}
				}
			}
		}
	}

	@Path("/getSchedule")
	@POST
	@Produces({"application/json;charset=UTF-8"})
	public final GetScheduleInfo getSchedule (@FormParam("resource") final String resource,
			@FormParam("companyID") final String companyID){
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/getSchedule");
		tp.println("resource", resource).
		println("companyID", companyID);
		
		File xml_schedule = null;
		GetScheduleInfo result = null;

		try {
			result = new GetScheduleInfo();
			if (!StringUtility.isNullOrEmpty(resource)) {
				String url = configProperties.getCustomMaintenanceBasePath() + companyID + StaticParameter.str_separator;
				xml_schedule = new File(url, resource);
				xml_schedule = new File(xml_schedule, StaticParameter.xml_schedule);

				result.setSchedule(ScheduleXmlUtil.getSchedule(xml_schedule));
				if (result.getSchedule() == null) {
					result.setSchedule(ScheduleXmlUtil.getEmptyScheduleModel(resource, companyID));
				}
			}
			else {
				tp.println("The config resource is null or empty!");
				LOGGER.logAlert(PROG_NAME,
						functionName,
						Logger.RES_EXCEP_NODATAFOUND,
						"The config resource is null or empty!");
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_INVALIDPARAMETER);
				result.setMessage(ResultBase.RES_INVALIDPARAMETER_MSG);
			}
		} catch (Exception ex) {
			tp.println("Failed to requestGetSchedule.");
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ":Failed to requestGetSchedule.", ex);
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setMessage(ex.getMessage());
		} finally {
			tp.methodExit(result.toString());
		}
		return result;
	}
	
	@Path("/setSchedule")
    @POST
    @Produces({"application/json;charset=UTF-8"})
	public final ResultBase requestSetSchedule(@FormParam("filename") final String filename,
			@FormParam("schedulejson") final String schedulejson, 
			@FormParam("resource") final String resource,
			@FormParam("companyID") final String companyID) {
    	
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/setSchedule");
		tp.println("filename", filename).
		println("schedulejson", schedulejson).
		println("resource", resource).
		println("companyID", companyID);

		ResultBase result = new ResultBase();
		try {
			File scheduleXml = null;
			String url = configProperties.getCustomMaintenanceBasePath() + companyID + StaticParameter.str_separator;
			File setScheduleDir = new File(url + resource);
			if (!setScheduleDir.exists()) {
				if (setScheduleDir.mkdirs()) {
					tp.println( "Directory is not find. And create at : " + setScheduleDir.getPath());
				}
			}
			if (!StringUtility.isNullOrEmpty(resource)) {
				scheduleXml = new File(url, resource);
				scheduleXml = new File(scheduleXml, StaticParameter.xml_schedule);

				ScheduleXmlUtil.saveScheduleByJSON(schedulejson, scheduleXml);
				tp.println("xml set successfully");
				LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_SUCCESS, "xml set successfully");
				result.setNCRWSSResultCode(ResultBase.RESRPT_OK);
				result.setNCRWSSExtendedResultCode(ResultBase.RESRPT_OK);
				result.setMessage(ResultBase.RES_SUCCESSFULL_MSG);
				return result;
			}else {
				tp.println("xml set failed ");
				LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_FILESAVEFAILED, "xml set failed ");
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
				result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
				result.setMessage(ResultBase.RES_FAILED_MSG);
				return result;
			}

		} catch (Exception e) {
			tp.println("Failed to requestSetSchedule.");
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ":Failed to setSchedule.", e);
			result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
			result.setMessage(e.getMessage());
		} finally {
			tp.methodExit(result.toString());
		}
		return result;
	}
	
	@Path("/fileRemove")
	@POST
	@Produces({"application/json;charset=UTF-8"})
	@ApiOperation(value="カスタムファイル削除", response=FileRemoveInfo.class)
	public final FileRemoveInfo requestConfigFileRemove(
			@ApiParam(name="companyID", value="企業コード") @FormParam("companyID") final String companyID,
			@ApiParam(name="folder", value="フォルダ") @FormParam("folder") final String folder,
			@ApiParam(name="filename", value="ファイル名") @FormParam("filename") final String filename,
			@ApiParam(name="confirmDel", value="利用中確認フラグ") @FormParam("confirmDel") final String confirmDel,
			@ApiParam(name="delFileList", value="削除失敗ファイルリスト") @FormParam("delFileList") final String delFileList){
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/fileRemove");
		tp.println("companyID",companyID)
		  .println("folder", folder)
		  .println("filename",filename)
		  .println("confirmDel",confirmDel)
		  .println("delFileList",delFileList);

		imageFileArr = new ArrayList<FileRemove>();
		FileRemoveInfo result = new FileRemoveInfo();

		try {
			if (!StringUtility.isNullOrEmpty(folder)) {
				if (Arrays.asList(StaticParameter.resourceArr).contains(folder)) {
					if (removeResourceDirFile(folder, filename, companyID)) {
						tp.println("File remove successfully");
					} else {
						tp.println("File remove successfully ");
						LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_FILESAVEFAILED, "File remove successfully");
						result.setStatus(StaticParameter.res_failed);
						return result;
					}

				} else if (folder.endsWith(StaticParameter.str_separator+ StaticParameter.key_images )) {
					if (StaticParameter.res_false.equalsIgnoreCase(confirmDel)) {
						if (isExistingPickListImage(folder, filename, companyID)) {
							tp.println(filename + ":" + "Picture is being used");
							LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_FILENOTFOUND, "Picture is being used");
							result.setStatus(StaticParameter.res_exist);
							result.setDescription(imageFileArr);
							return result;
						} else {
							if (removeResourceDirImageFile(folder, filename, delFileList, companyID)) {
								tp.println("File remove successfully");
								LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_SUCCESS, "File remove successfully");
								result.setStatus(StaticParameter.res_success);
								return result;
							} else {
								tp.println("File remove failed ");
								LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_FILESAVEFAILED, "File remove failed ");
								result.setStatus(StaticParameter.res_failed);
								result.setDescription(imageFileArr);
								return result;
							}
						}
					} else if (StaticParameter.res_true.equalsIgnoreCase(confirmDel)) {
						if (removeResourceDirImageFile(folder, filename, delFileList, companyID)) {
							tp.println("File remove successfully");
							LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_SUCCESS, "File remove successfully");
							result.setStatus(StaticParameter.res_success);
							return result;
						} else {
							tp.println("File remove failed ");
							LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_FILESAVEFAILED, "File remove failed ");
							result.setStatus(StaticParameter.res_failed);
							result.setDescription(imageFileArr);
							return result;
						}
					}
				}
			}
		} catch (Exception e) {
			tp.println("Failed to requestfileRemove.");
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ":Failed to requestfileRemove.", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(e.getMessage());
		} finally{
			tp.methodExit(result.toString());
		}
		return result;
	}
	
	/**
	 * ファイル削除（ファイル用）
	 * @param pResource : pickList/notices
	 * @param pFileName : ファイル名前
	 * @return true(成功) / false(失敗)
	 */
	private boolean removeResourceDirFile(String pResource, String pFileName, String companyID) {

		boolean isDeleteFlg = false;
		try {
			String url = configProperties.getCustomMaintenanceBasePath() + companyID + StaticParameter.str_separator;
			File delFileDir = new File(url, pResource);
			File delFile = new File(delFileDir, pFileName);

			if (removeFile(delFile)) {
				tp.println(":" + "file already exists");
			} else {
				tp.println("");
			}

			if (isDeleteFlg) {
				File xml_schedule = new File(url, pResource);
				xml_schedule = new File(xml_schedule, configProperties.getScheduleFilePath());

				deleteScheduleTask(xml_schedule, pResource, pFileName,companyID);
			}
		} catch (Exception e) {
			tp.println("The message id of SQL Exception.");
		}
		return isDeleteFlg;
	}

	private void deleteScheduleTask(File pScheduleXml, String pResource, String pFileName, String companyID) {

		Config config = null;
		Deploy deploy = null;
		Schedule schedule = null;
		List<Task> taskList = new ArrayList<Task>();
		List<Task> taskTemp = new ArrayList<Task>();
		boolean isNeedUpdate = false;

		try {
			schedule = ScheduleXmlUtil.getSchedule(pScheduleXml);
			deploy = ScheduleXmlUtil.getDeploy(schedule, companyID);
			config = ScheduleXmlUtil.getConfig(deploy, pResource);

			if (config == null) {
				tp.println("Can not find the file in Schedule Xml : " + pFileName);
				return;
			}
			taskList = config.getTask();
			if (taskList == null) {
				tp.println("Can not find the task in Schedule Xml");
				return;
			}

			taskTemp = new ArrayList<Task>();
			taskTemp.addAll(taskList);

			for (Task task : taskList) {
				if (pFileName.equalsIgnoreCase(task.getFilename())) {
					if (taskTemp.remove(task)) {
						isNeedUpdate = true;
						tp.println( "Removed the schedule task");
					}
				}
			}

			if (isNeedUpdate) {
				config.setTask(taskTemp);
				ScheduleXmlUtil.saveSchedule(schedule, pScheduleXml);
			}
		} catch (Exception e) {
			tp.println("The message id of SQL Exception.");
		}
	}

	/**
	 * ファイル削除（画像用）
	 *
	 * @param pResource : imagesディレクトリのpickList/notices
	 * @param pFileName : ファイル名前
	 * @param pDelFileList : 削除を失敗たら、失敗ファイルリストに保存する
	 * @return true(成功) / false(失敗)
	 */
	private boolean removeResourceDirImageFile(String pResource, String pFileName, String pDelFileList, String companyID) {

		String resource = "";
		String content = "";
		String pictureName = "";
		String filename = "";
		File dir_resource = null;
		File img_picture = null;
		JSONArray delFileList = null;
		FileRemove lineData = null;
		boolean retFlg = false;

		try {
			String url = configProperties.getCustomMaintenanceBasePath() + companyID + StaticParameter.str_separator;
			dir_resource = new File(url, pResource);
			img_picture = new File(dir_resource, pFileName);

			if (removeFile(img_picture)) {
				retFlg = true;
				tp.println("File removal success");
			} else {
				tp.println("File removal failed");
				return retFlg;
			}

			if (StringUtility.isNullOrEmpty(pDelFileList)) {
				tp.println("The list of image delete is null or empty !");
				retFlg = true;
				return retFlg;
			}

			delFileList = new JSONArray(pDelFileList);
			if (delFileList.length() == 0) {
				tp.println("The list of image delete is null or empty !");
				retFlg = true;
				return retFlg;
			}

			imageFileArr = new ArrayList<FileRemove>();

			if (pResource.endsWith(StaticParameter.str_separator + StaticParameter.key_images)) {
				resource = pResource.split(StaticParameter.str_separator)[0];
				dir_resource = new File(url, resource);
				img_picture = new File(dir_resource, StaticParameter.str_separator + StaticParameter.key_images);
				img_picture = new File(img_picture, pFileName);

				if (pFileName.contains(StaticParameter.str_separator)) {
					pictureName = img_picture.getParentFile().getName() + StaticParameter.str_separator + img_picture.getName();
				} else {
					pictureName = pFileName;
				}

				for (int i = 0; i < delFileList.length(); i++) {
					filename = delFileList.getJSONObject(i).getString(KEY_FULLNAME);
					if (StringUtility.isNullOrEmpty(filename)) {
						continue;
					}

					img_picture = new File(dir_resource, filename);
					content = FileUtil.fileRead(img_picture);

					if (StaticParameter.key_pickList.equalsIgnoreCase(resource)) {
						content = chargeFileSeparator(content);
					} else if (StaticParameter.key_notices.equalsIgnoreCase(resource)) {
						pictureName = pFileName;
					} else if(StaticParameter.key_advertise.equalsIgnoreCase(resource)){
						content = chargeFileSeparator(content);
					}

					if (!StringUtility.isNullOrEmpty(content) && content.contains(pictureName)) {
						content = content.replaceAll(pFileName, StaticParameter.str_empty);

						if (!FileUtil.fileSave(img_picture, content, false, UiConfigHelper.ENCODING_DEPLOY_STATUS_FILE)) {

							lineData = new FileRemove();
							filename = img_picture.getName();
							lineData.setFullName(filename);

							filename = filename.substring(0, filename.length() - StaticParameter.file_js.length());
							lineData.setFileName(filename);

							imageFileArr.add(lineData);
						}
					}
				}

				if (imageFileArr.size() == 0) {
					retFlg = true;
				} else {
					retFlg = false;
				}
			}

		} catch (Exception e) {
			tp.println("The message id of SQL Exception.");
		} 
		return retFlg;
	}

	private boolean isExistingPickListImage(String pResource, String pFileName, String companyID) {

		String resource = "";
		String content = "";
		String pictureName = "";
		String filename = "";
		File dir_resource = null;
		File img_picture = null;
		FileRemove lineData = null;
		boolean existFlg = false;

		try {

			imageFileArr = new ArrayList<FileRemove>();
			if (pResource.endsWith(StaticParameter.str_separator + StaticParameter.key_images)) {
				resource = pResource.split(StaticParameter.str_separator)[0];
				String url = configProperties.getCustomMaintenanceBasePath() + companyID + StaticParameter.str_separator;
				dir_resource = new File(url, resource);
				img_picture = new File(dir_resource, StaticParameter.str_separator + StaticParameter.key_images);
				img_picture = new File(img_picture, pFileName);

				if (pFileName.contains(StaticParameter.str_separator)) {
					pictureName = img_picture.getParentFile().getName() + StaticParameter.str_separator + img_picture.getName();
				} else {
					pictureName = pFileName;
				}

				for (File file : dir_resource.listFiles()) {
					if (!file.getName().endsWith(StaticParameter.file_js)) {
						continue;
					}

					content = FileUtil.fileRead(file);

					if (StaticParameter.key_pickList.equalsIgnoreCase(resource)) {
						content = chargeFileSeparator(content);
					} else if (StaticParameter.key_notices.equalsIgnoreCase(resource)) {
						pictureName = pFileName;
					} else if(StaticParameter.key_advertise.equalsIgnoreCase(resource)){
						content = chargeFileSeparator(content);
					}

					if (!StringUtility.isNullOrEmpty(content) && content.contains(pictureName)) {
						tp.println( "The picture [ "+ pFileName + " ] is existing in file : " + file.getPath());

						lineData = new FileRemove();
						filename = file.getName();
						lineData.setFullName(filename);

						filename = filename.substring(0, filename.length() - StaticParameter.file_js.length());
						lineData.setFileName(filename);

						imageFileArr.add(lineData);

					}
				}

				if (imageFileArr.size() == 0) {
					existFlg = false;
					tp.println( "The picture [ "+ pFileName + " ] is not exist in each of " + resource);
				} else {
					existFlg = true;
				}
			}

		} catch (Exception e) {
			tp.println( "The message id of SQL Exception.");
		}
		return existFlg;
	}

	private String chargeFileSeparator(String pChargeContent) {

		String content = "";

		try {
			content = pChargeContent;
			if (!StringUtility.isNullOrEmpty(content)) {
				if (content.contains("\\\\\\\\")) {
					content = content.replace("\\\\\\\\", StaticParameter.str_separator);
				}

				if (content.contains("\\\\")) {
					content = content.replace("\\\\", StaticParameter.str_separator);
				}
			}
		} catch (Exception e) {
			tp.println( "The message id of SQL Exception.");
		}

		return content;
	}

	private boolean removeFile(File pDelFile) {

		boolean isDeleteFlg = false;

		try {
			if (pDelFile.exists()) {
				if (pDelFile.isFile()) {
					if (pDelFile.delete()) {
						isDeleteFlg = true;
						tp.println("File removal success");

					} else {
						if (FileUtil.fileDeleteByCmdDel(pDelFile)) {
							isDeleteFlg = true;
							tp.println("File removal success");
						}
					}
				} else {
					isDeleteFlg = false;
					tp.println("The message id of Not found file Exception.");
				}
			} else {
				isDeleteFlg = true;
				tp.println("The message id of Not found file Exception.");
			}

		} catch (Exception e) {
			tp.println( "The message id of SQL Exception.");
		}
		return isDeleteFlg;
	}

	private List<FileRemove> imageFileArr = null;
	private static final String KEY_FULLNAME = "fullName";
	
	@Path("/pictureUpload")
	@POST
    @Produces({"application/json;charset=UTF-8"})
    @Consumes({"multipart/form-data"})
	public final PictureInfoUpload requestConfigPictureUpload(
			@Context final HttpServletRequest request){

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/pictureUpload");

		String companyID = "";
		String filename = "";
		String folder = "";
		int sizeType = 0;
		File imgFolder = null;

		PictureInfoUpload result = new PictureInfoUpload();
		InputStream isFormField = null;
		InputStream isFormFile = null;
		FileOutputStream stream = null;
		DiskFileItemFactory factory = new DiskFileItemFactory();
		ServletFileUpload sfu = new ServletFileUpload(factory);

		try {
			factory.setSizeThreshold(1024);
			sfu.setSizeMax(-1);
			sfu.setHeaderEncoding("UTF-8");

			FileItemIterator list = sfu.getItemIterator(request);
			while(list.hasNext()) {
				FileItemStream fis = list.next();
				if (fis.isFormField()) {
					isFormField = fis.openStream();
					if ("filename".equals(fis.getFieldName())) {
						filename = Streams.asString(isFormField, StaticParameter.code_UTF8);
					} else if ("folder".equals(fis.getFieldName())) {
						folder = Streams.asString(isFormField, StaticParameter.code_UTF8);
					}else if("sizeType".equals(fis.getFieldName())){
						sizeType = Integer.parseInt(Streams.asString(isFormField, StaticParameter.code_UTF8));
					}else if("companyID".equals(fis.getFieldName())){
						companyID = Streams.asString(isFormField, StaticParameter.code_UTF8);
					}
				} else {
					if ("form-file".equals(fis.getFieldName())) {
						isFormFile = fis.openStream();
						imgFolder = new File(configProperties.getCustomMaintenanceBasePath() + companyID + StaticParameter.str_separator, folder);
						File imageFile = new File(imgFolder, filename);

						stream = new FileOutputStream(imageFile);
						int len = 0;
						byte[] buf = new byte[1024];
						while ((len = isFormFile.read(buf)) > 0) {
							stream.write(buf, 0, len);
						}

						stream.flush();
						stream.close();
					}
				}
			}

			if ("notices/images".equals(folder)) {
				stdtargetWidth = 256;
				stdtargetHeight = 384;
			} else if ("pickList/images".equals(folder)) {
				stdtargetWidth = 114;
				stdtargetHeight = 76;
			}else if("advertise/images".equals(folder)){
				if(sizeType == 1){
					stdtargetWidth = 600;
					stdtargetHeight = 640;
				}else if(sizeType == 2){
					stdtargetWidth = 1020;
					stdtargetHeight = 640;
				}else{
					stdtargetWidth = 114;
					stdtargetHeight = 76;
				}
			}

			result.setImage(filename);
			ImageCopyWithFormat(imgFolder, imgFolder, filename, "png");
			ImageCopyWithFormat(imgFolder, imgFolder, filename, "jpg");
			ImageCopyWithFormat(imgFolder, imgFolder, filename, "bmp");
			ImageCopyWithFormat(imgFolder, imgFolder, filename, "gif");
			ImageCopyWithFormat(imgFolder, imgFolder, filename, "jpeg");
		}catch(Exception e){
			tp.println("Failed to requestpictureUpload.");
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ":Failed to requestpictureUpload.", e);
            result.setNCRWSSResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_GENERAL);
            result.setMessage(e.getMessage());
		}finally{
			if(stream != null){
				try {
					stream.close();
				} catch (IOException e) {
					tp.println("Stream IO exception occured. ");
					LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_TILL, "Stream IO exception occured.");
					result.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
					result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
					result.setMessage(e.getMessage());
				}
			}
			if (isFormField != null) {
				try {
					isFormField.close();
				} catch (IOException e) {
					tp.println("Stream IO exception occured. ");
					LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_TILL, "Stream IO exception occured.");
					result.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
					result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
					result.setMessage(e.getMessage());
				}
			}
			if (isFormFile != null) {
				try {
					isFormFile.close();
				} catch (IOException e) {
					tp.println("Stream IO exception occured. ");
					LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_TILL, "Stream IO exception occured.");
					result.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
					result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
					result.setMessage(e.getMessage());
				}
			}
			tp.methodExit(result.toString());
		}
		return result;
	}

	/**
	 * Process of Resize Image files to specified folder.
	 */
	private void ImageCopyWithFormat(File sDir, File sToDir,
									String fileName, String format) throws IOException {

		String matchString = "(.*)\\." + format + "$";
		if (fileName.toLowerCase().matches(matchString)) {
			File subFileDir = new File(sDir, fileName);
			BufferedImage originalImage = ImageIO.read(subFileDir);
			int targetWidth = stdtargetWidth;
			int targetHeight = stdtargetHeight;

			// Get current image's Width and Height,calculate standard size.
			int currentWidth = originalImage.getWidth();
			int currentHeight = originalImage.getHeight();

			if (currentHeight < currentWidth) {
				targetHeight = (int) (((stdtargetWidth * 1.0) / currentWidth) * currentHeight);
				targetHeight = targetHeight > stdtargetHeight ? stdtargetHeight : targetHeight;
			} else {
				targetWidth = (int) (((stdtargetHeight * 1.0) / currentHeight) * currentWidth);
				targetWidth = targetWidth > stdtargetWidth ? stdtargetWidth : targetWidth;
			}

			// Call resize method of image file resize class
			BufferedImage scaledImage = ImageScalr.resize(originalImage, targetWidth, targetHeight);
			originalImage.flush();

			// If folders or file is not exist then create
			if (!sToDir.exists()) {
				sToDir.mkdirs();
			}

			File destFile = new File(sToDir, fileName);
			if (!destFile.exists()) {
				destFile.createNewFile();
			}

			ImageIO.write(scaledImage, format, destFile);
			scaledImage.flush();
		}
	}
	private int stdtargetWidth = 76;
	private int stdtargetHeight = 114;
}

