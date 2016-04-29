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
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import javax.ws.rs.FormParam;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.exception.DaoException;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.uiconfig.constants.UiConfigProperties;
import ncr.res.mobilepos.uiconfig.dao.IUiConfigCommonDAO;
import ncr.res.mobilepos.uiconfig.dao.SQLServerUiConfigCommonDAO;
import ncr.res.mobilepos.uiconfig.model.fileInfo.FileInfo;
import ncr.res.mobilepos.uiconfig.model.fileInfo.FileInfoList;
import ncr.res.mobilepos.uiconfig.model.schedule.CompanyInfo;
import ncr.res.mobilepos.uiconfig.model.schedule.CompanyInfoList;
import ncr.res.mobilepos.uiconfig.model.store.StoreEntry;
import ncr.res.mobilepos.uiconfig.model.store.TableStore;
import ncr.res.mobilepos.uiconfig.model.store.TableStoreList;
import ncr.res.mobilepos.uiconfig.utils.FileUtil;
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
					tp.methodExit("The message id of file Not found Exception.");
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
				tp.methodExit("The message id of Not found file Exception.");
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
			tp.methodExit("Failed to requestFileList.");
            LOGGER.logAlert(PROG_NAME,
            		functionName,
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to requestFileList.");
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
}

