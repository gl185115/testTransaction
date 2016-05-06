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

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONObject;

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
import ncr.res.mobilepos.uiconfig.model.fileInfo.FileDownLoadInfo;
import ncr.res.mobilepos.uiconfig.model.fileInfo.FileInfo;
import ncr.res.mobilepos.uiconfig.model.fileInfo.FileInfoList;
import ncr.res.mobilepos.uiconfig.model.fileInfo.FileRemoveInfo;
import ncr.res.mobilepos.uiconfig.model.fileInfo.PictureInfoList;
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
public class UiConfigMaintenanceResource {

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
			if(StringUtility.isNullOrEmpty(cmpList)) {
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
					functionName + ":Failed to get company information.",
					ex);
		} catch(Exception ex) {
			tp.println("Failed to get company information.");
			LOGGER.logAlert(PROG_NAME,
					Logger.RES_EXCEP_GENERAL,
					functionName + ":Failed to get company information.",
					ex);
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
			@FormParam("title2") final String title2) {

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/fileUpload");
		tp.println("folder", folder).println("contents", contents).println("desfilename", desfilename)
				.println("overwrite", overwrite).println("picturename", picturename).println("expire", expire)
				.println("title", title).println("title2", title2);

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
			File uploadDir = new File(configProperties.getCustomResourceBasePath(), folder);
			if (!uploadDir.exists()) {
				tp.println(uploadDir.getPath() + ":" + "directory does not exist");
				if (uploadDir.mkdirs()) {
					tp.println("Create a" + uploadDir.getPath() + "directory");
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
				result.setNCRWSSResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
				result.setNCRWSSExtendedResultCode(ResultBase.RES_ERROR_IOEXCEPTION);
				result.setMessage(ResultBase.RES_FAILED_MSG);
				return result;
			}

		} catch (Exception e) {
			tp.println("Failed to requestFileUpload.");
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ":Failed to requestFileUpload.", e);
		} finally {
			tp.methodExit(result.toString());
		}
		return result;
	}

	@Path("/fileList")
	@POST
	@Produces({"application/json;charset=UTF-8"})
	public final FileInfoList requestConfigFileList(
			@FormParam("folder") final String folder){

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/fileList");
		tp.println("folder", folder);

		FileInfoList result = new FileInfoList();
		try {
			File resourceDir = new File(configProperties.getCustomResourceBasePath(), folder);
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
		} finally {
			tp.methodExit(result.toString());
		}
		
		return result;
	}
    
	@Path("/fileDownload")
	@POST
	@Produces({"application/json;charset=UTF-8"})
	public final FileDownLoadInfo requestConfigFileDownload(
			@FormParam("folder") final String folder,
			@FormParam("filename") final String filename){

		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/fileDownload");
		tp.println("folder", folder)
		  .println("filename", filename);

		FileDownLoadInfo result = new FileDownLoadInfo();
		try {
			File loadFolder = new File(configProperties.getCustomResourceBasePath(), folder);
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
			if(StringUtility.isNullOrEmpty(storeEntryList)) {
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
					functionName + ":Failed to get StoreAndGroup information.",
					ex);
		} catch(Exception ex) {
			tp.println("Failed to get StoreAndGroup information.");
			LOGGER.logAlert(PROG_NAME,
					Logger.RES_EXCEP_GENERAL,
					functionName + ":Failed to get StoreAndGroup information.",
					ex);
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
	public final PictureInfoList PictureListServlet(@FormParam("folder") final String folder) {
		// Logs given parameters.
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/pictureList");
		tp.println("folder", folder);

		PictureInfoList result = new PictureInfoList();

		try {
			File dir_resource = new File(configProperties.getCustomResourceBasePath(), folder);
			
			if (dir_resource.exists()) {
				String baseDir = dir_resource.getPath();
				List<String> filelist = new ArrayList<String>();
				GetFolderList(baseDir, filelist, true);
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
                return result;
			}

		} catch (Exception e) {
			tp.println("Failed to requestPictureList.");
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ":Failed to requestPictureList.", e);
		} finally {
			tp.methodExit(result.toString());
		}
		return result;

	}

	private void GetFolderList(String sDir, List<String> sList, boolean bRecurs) {

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
						GetFolderList(sDir + "\\" + name, sList, true);
					}
				} else if (subFileDir.isFile() && (name.toLowerCase().endsWith(".png")
						|| name.toLowerCase().endsWith(".jpg") || name.toLowerCase().endsWith(".bmp")
						|| name.toLowerCase().endsWith(".gif") || name.toLowerCase().endsWith(".jpeg"))) {
					sList.add(subFileDir.getPath());
				}
			}
		}
	}

	@Path("/getSchedule")
	@POST
	@Produces({"application/json;charset=UTF-8"})
	public final GetScheduleInfo getSchedule (@FormParam("resource") final String resource){
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/getSchedule");
		
		File xml_schedule = null;
		GetScheduleInfo result = null;

		try {
			result = new GetScheduleInfo();
			if (!StringUtility.isNullOrEmpty(resource)) {
				xml_schedule = new File(configProperties.getCustomResourceBasePath(), resource);
				xml_schedule = new File(xml_schedule, StaticParameter.xml_schedule);

				if(!xml_schedule.exists() || !xml_schedule.isFile()){
					xml_schedule = new File(configProperties.getCustomResourceBasePath() + StaticParameter.key_schedule, String.format(StaticParameter.format_scheduleXml, resource));
				}
				
				result.setSchedule(ScheduleXmlUtil.getSchedule(xml_schedule));
				if (result.getSchedule() == null) {
					result.setSchedule(ScheduleXmlUtil.getEmptyScheduleModel(resource));
				}
			}
			else {
				tp.println("The config resource is null or empty!");
				LOGGER.logAlert(PROG_NAME,
						functionName,
						Logger.RES_EXCEP_NODATAFOUND,
						"The config resource is null or empty!");
			}
		} catch (Exception ex) {
			tp.println("Failed to requestGetSchedule.");
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ":Failed to requestGetSchedule.", ex);
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
			@FormParam("resource") final String resource) {
    	
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/setSchedule");
		tp.println("filename", filename).println("schedulejson", schedulejson).println("resource", resource);

		ResultBase result = new ResultBase();
		try {
			File scheduleXml = null;
			File setScheduleDir = new File(configProperties.getCustomResourceBasePath() + StaticParameter.key_schedule);
			if (!setScheduleDir.exists()) {
				if (setScheduleDir.mkdirs()) {
					tp.println( "Directory is not find. And create at : " + setScheduleDir.getPath());
				}
			}
			if (!StringUtility.isNullOrEmpty(resource)) {
				scheduleXml = new File(configProperties.getCustomResourceBasePath(), resource);
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
		} finally {
			tp.methodExit(result.toString());
		}
		return result;
	}
	
	@SuppressWarnings("unchecked")
	@Path("/fileRemove")
	@POST
	@Produces({"application/json;charset=UTF-8"})
	public final FileRemoveInfo requestConfigFileRemove(
			@FormParam("folder") final String folder,
			
			@FormParam("filename") final String filename,
			@FormParam("confirmDel") final String confirmDel,
			@FormParam("delFileList") final String delFileList){
		String functionName = DebugLogger.getCurrentMethodName();
		tp.methodEnter("/fileRemove");
		tp.println("folder", folder)
		  .println("filename",filename)
		  .println("confirmDel",confirmDel)
		  .println("delFileList",delFileList);

		imageFileArr = new JSONArray();
		FileRemoveInfo result = new FileRemoveInfo();

		try {
			if (!StringUtility.isNullOrEmpty(folder)) {
				if (Arrays.asList(StaticParameter.resourceArr).contains(folder)) {
					if (removeResourceDirFile(folder, filename)) {
						tp.println("File remove successfully");
					} else {
						tp.println("File remove successfully ");
						LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_FILESAVEFAILED, "File remove successfully");
						result.setStatus(StaticParameter.res_failed);
						return result;
					}

				} else if (folder.startsWith(StaticParameter.key_images + StaticParameter.str_separator)) {
					if (StaticParameter.res_false.equalsIgnoreCase(confirmDel)) {
						if (isExistingPickListImage(folder, filename)) {
							tp.println(filename + ":" + "Picture is being used");
							LOGGER.logAlert(PROG_NAME, functionName, Logger.RES_EXCEP_FILENOTFOUND, "Picture is being used");
							result.setStatus(StaticParameter.res_exist);
							result.setDescription(imageFileArr);
							return result;
						} else {
							if (removeResourceDirImageFile(folder, filename, delFileList)) {
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
						if (removeResourceDirImageFile(folder, filename, delFileList)) {
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
			tp.println("Failed to requestFileRemove.");
			LOGGER.logAlert(PROG_NAME, Logger.RES_EXCEP_GENERAL, functionName + ":Failed to requestFileRemove.", e);
		} finally{
			tp.methodExit(result.toString());
		}
		return result;
	}
	
	/**
	 * �t�@�C���폜�i�t�@�C���p�j
	 * @param pResource : pickList/notices
	 * @param pFileName : �t�@�C�����O
	 * @return true(����) / false(���s)
	 */
	private boolean removeResourceDirFile(String pResource, String pFileName) {

		boolean isDeleteFlg = false;
		try {
			File delFileDir = new File(configProperties.getCustomResourceBasePath(), pResource);
			File delFile = new File(delFileDir, pFileName);

			if (removeFile(delFile)) {
				tp.println(":" + "file already exists");
			} else {
				tp.println("");
			}

			if (isDeleteFlg) {
				File xml_schedule = new File(configProperties.getCustomResourceBasePath(), pResource);
				xml_schedule = new File(xml_schedule, configProperties.getScheduleFilePath());

				deleteScheduleTask(xml_schedule, pResource, pFileName);
			}
		} catch (Exception e) {
			tp.println("The message id of SQL Exception.");
		}
		return isDeleteFlg;
	}

	private void deleteScheduleTask(File pScheduleXml, String pResource, String pFileName) {

		Config config = null;
		Deploy deploy = null;
		Schedule schedule = null;
		List<Task> taskList = new ArrayList<Task>();
		List<Task> taskTemp = new ArrayList<Task>();
		boolean isNeedUpdate = false;

		try {
			schedule = ScheduleXmlUtil.getSchedule(pScheduleXml);
			deploy = ScheduleXmlUtil.getDeploy(schedule, StaticParameter.companyID);
			config = ScheduleXmlUtil.getConfig(deploy, pResource);

			if (StringUtility.isNullOrEmpty(config)) {
				tp.println("Can not find the file in Schedule Xml : " + pFileName);
				return;
			}
			taskList = config.getTask();
			if (StringUtility.isNullOrEmpty(taskList)) {
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
	 * �t�@�C���폜�i�摜�p�j
	 *
	 * @param pResource : images�f�B���N�g����pickList/notices
	 * @param pFileName : �t�@�C�����O
	 * @param pDelFileList : �폜�����s����A���s�t�@�C�����X�g�ɕۑ�����
	 * @return true(����) / false(���s)
	 */
	private boolean removeResourceDirImageFile(String pResource, String pFileName, String pDelFileList) {

		String resource = "";
		String content = "";
		String pictureName = "";
		String filename = "";
		File dir_resource = null;
		File img_picture = null;
		JSONArray delFileList = null;
		JSONObject lineData = null;
		boolean retFlg = false;

		try {
			dir_resource = new File(configProperties.getCustomResourceBasePath(), pResource);
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

			imageFileArr = new JSONArray();

			if (pResource.startsWith(configProperties.getCustomResourceBasePath() + StaticParameter.str_separator)) {
				resource = pResource.split(StaticParameter.str_separator)[1];
				dir_resource = new File(configProperties.getCustomResourceBasePath(), resource);
				img_picture = new File(dir_resource, pResource);
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

					if (configProperties.getCustomResourceBasePath().equalsIgnoreCase(resource)) {
						content = chargeFileSeparator(content);
					} else if (configProperties.getCustomResourceBasePath().equalsIgnoreCase(resource)) {
						pictureName = pFileName;
					}

					if (!StringUtility.isNullOrEmpty(content) && content.contains(pictureName)) {
						content = content.replaceAll(pFileName, StaticParameter.str_empty);

						if (!FileUtil.fileSave(img_picture, content, false, UiConfigHelper.ENCODING_DEPLOY_STATUS_FILE)) {

							lineData = new JSONObject();
							filename = img_picture.getName();
							lineData.put(KEY_FULLNAME, filename);

							filename = filename.substring(0, filename.length() - StaticParameter.file_js.length());
							lineData.put(KEY_FILENAME, filename);

							imageFileArr.put(lineData);
						}
					}
				}

				if (imageFileArr.length() == 0) {
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

	private boolean isExistingPickListImage(String pResource, String pFileName) {

		String resource = "";
		String content = "";
		String pictureName = "";
		String filename = "";
		File dir_resource = null;
		File img_picture = null;
		JSONObject lineData = null;
		boolean existFlg = false;

		try {

			if (pResource.startsWith(configProperties.getCustomResourceBasePath() + StaticParameter.str_separator)) {
				resource = pResource.split(StaticParameter.str_separator)[1];
				dir_resource = new File(configProperties.getCustomResourceBasePath(), resource);
				img_picture = new File(dir_resource, pResource);
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

					if (configProperties.getCustomResourceBasePath().equalsIgnoreCase(resource)) {
						content = chargeFileSeparator(content);
					} else if (configProperties.getCustomResourceBasePath().equalsIgnoreCase(resource)) {
						pictureName = pFileName;
					}

					if (!StringUtility.isNullOrEmpty(content) && content.contains(pictureName)) {
						tp.println( "The picture [ "+ pFileName + " ] is existing in file : " + file.getPath());

						lineData = new JSONObject();
						filename = file.getName();
						lineData.put(KEY_FULLNAME, filename);

						filename = filename.substring(0, filename.length() - StaticParameter.file_js.length());
						lineData.put(KEY_FILENAME, filename);

						imageFileArr.put(lineData);

					}
				}

				if (imageFileArr.length() == 0) {
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

	private JSONArray imageFileArr = null;
	private static final String KEY_FILENAME = "fileName";
	private static final String KEY_FULLNAME = "fullName";
}

