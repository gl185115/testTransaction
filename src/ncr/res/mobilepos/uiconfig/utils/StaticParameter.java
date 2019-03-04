package ncr.res.mobilepos.uiconfig.utils;

import java.io.File;
import java.util.Map;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.Semaphore;

public class StaticParameter {

	/*
	 * プログラム変数
	 */
	public static String company = "";
	public static String companyID = RESConfigEnum.COMPANYID.getValue();
	public static String primaryServe = "";
	public static String secondaryServe = "";
	public static String smbSerUrlMain = "";
	public static String smbSerUrlSub = "";
	public static String smbActiveSerMeX = null;
	public static String format_scheduleXml = "schedule_%s.xml";
	public static String format_zipFileName = "%s.zip";
	public static String format_updVersionFile = "%s.ver";
	public static String userID = RESConfigEnum.USERID.getValue();
	public static String password = RESConfigEnum.PASSWORD.getValue();

	public static Semaphore semp = new Semaphore(1);
//	public static ExistingEnvironementBalancer envBalancer = null;
	public static ScheduledExecutorService customDirUpdateScheduler = null;
	public static ScheduledExecutorService collectAccessLogFromPosToStoreSerScheduler = null;
	public static Map<String, String> serverMap = null;

	public static File dirWorkspace = null;
	public static File dir_custom = null;
	public static File dir_advertise = null;
	public static File dir_images = null;
	public static File dir_imagesNotices = null;
	public static File dir_imagesPickList = null;
	public static File dir_notices = null;
	public static File dir_options = null;
	public static File dir_phrases = null;
	public static File dir_pickList = null;
	public static File dir_schedule = null;
	public static File dir_usability = null;
	public static File dir_receipt = null;
	public static File dir_log = null;
	public static File dir_logs = null;

	public static File dir_ImagesNotices_bk = null;
	public static File dir_ImagesPickList_bk = null;
	public static File dir_Notices_bk = null;
	public static File dir_PiclList_bk = null;
	public static File dir_Schedule_bk = null;
	public static File dir_usability_bk = null;

	public static File file_csv_rule = null;
	public static File file_csv_stores = null;
	public static File file_csv_storesDetail = null;
	public static File file_csv_tableStore = null;
	public static File file_csv_tableGroup = null;
	public static File file_csv_accesslog = null;
	public static File file_xml_resProfile = null;
	public static File file_xml_schedule = null;
	public static File file_xml_notices = null;
	public static File file_xml_pickList = null;
	public static File file_xml_schedule_notices = null;
	public static File file_xml_schedule_pickList = null;
	public static File file_ver_custom = null;
	public static File file_ver_server = null;
	public static File file_ver_notices = null;
	public static File file_ver_pickList = null;
	public static File file_config_resTransPath = null;
	public static File file_dst_default = null;
	public static File file_trn_com = null;
	public static File file_trn_default = null;

	/*
	 * 固定
	 */
	public static final String company_MYB = "MYB";
	public static final String company_GMS = "GMS";
	public static final String code_UTF8 = "UTF-8";
	public static final String code_MS932 = "MS932";

	public static final String format_yyyyMMddHHmm = "yyyy-MM-dd HH:mm";
	public static final String format_yyyyMMddTHHmm = "yyyy-MM-dd'T'HH:mm";
	public static final String format_yyyyMMddHHmmss = "yyyy-MM-dd HH:mm:ss";
	public static final String format_yyyyMMddHHmmssSSS = "yyyy-MM-dd HH:mm:ss:SSS";

	public static final String res_zero = "0";
	public static final String res_one = "1";

	public static final String ret_exist = "0";
	public static final String ret_normal = "1";
	public static final String ret_error = "-1";

	public static final String res_true = "true";
	public static final String res_false = "false";
	public static final String res_create = "create";
	public static final String res_delete = "delete";
	public static final String res_update = "update";

	public static final String res_result = "result";
	public static final String res_status = "status";
	public static final String res_success = "success";
	public static final String res_failed = "failed";
	public static final String res_error = "error";
	public static final String res_exist = "exist";
//	public static final String res_existNot = "existNot";
//	public static final String res_image = "image";
	public static final String res_description = "description";

	public static final String key_primary = "primary";
	public static final String key_secondary = "secondary";
	public static final String key_custom = "custom";
	public static final String key_advertise = RESConfigEnum.DIR_ADVERTISE.getValue();
	public static final String key_images = RESConfigEnum.DIR_IMAGES.getValue();
	public static final String key_notices = RESConfigEnum.DIR_NOTICES.getValue();
	public static final String key_options = RESConfigEnum.DIR_OPTIONS.getValue();
	public static final String key_phrases = RESConfigEnum.DIR_PHRASES.getValue();
	public static final String key_pickList = RESConfigEnum.DIR_PICKLIST.getValue();
	public static final String key_schedule = RESConfigEnum.DIR_SCHEDULE.getValue();
	public static final String key_usability = RESConfigEnum.DIR_USABILITY.getValue();
	public static final String key_receipt = RESConfigEnum.DIR_RECEIPT.getValue();
	public static final String key_log = RESConfigEnum.DIR_LOG.getValue();
	public static final String key_logs = RESConfigEnum.DIR_LOGS.getValue();
	public static final String key_all = "all";
	public static final String key_store = "store";
	public static final String key_group = "group";
	public static final String key_all_str = "全店舗";
	public static final String key_store_str = "個別店舗";
	public static final String key_group_str = "店舗グループ";
	public static final String key_resource = RESConfigEnum.RESOURCE.getValue();

	public static final String str_empty = "";
	public static final String str_space = " ";
	public static final String str_comma = ",";
	public static final String str_equal = "=";
	public static final String str_semicolon = ";";
	public static final String str_separator = "/";
	public static final String str_dot = "\\.";
	public static final String str_enter = "\r\n";
	public static final String str_smb = "smb://";
	public static final String str_underline = "_";
	public static final String str_Ip21 = "21";
//	public static final String str_scoDataMeX = "/ScoData/MeX";
//	public static final String str_scoDataMeX = "/MYB_Shared/ScoData/MeX";
//	public static final String str_scoDataMeX = "/R&D/ScoData/MeX";
	public static final String str_deployTitle = "ファイル名, 適用時刻, 開封, 店舗番号, 店舗名, 電話番号\r\n";
	public static final String str_deployLine = "%s, %s, %s, %s, %s, %s\r\n";

	public static final String file_bk = "_bk";
	public static final String file_js = ".js";
	public static final String file_log = ".log";
	public static final String file_csv = ".csv";
	public static final String file_xml = ".xml";
	public static final String file_ver = ".ver";
	public static final String file_version = "version.log";

	public static final String zip_customize = RESConfigEnum.ZIP_CUSTOMIZE.getValue();

	public static final String csv_rule = RESConfigEnum.FILE_RULE.getValue();
	public static final String csv_stores = RESConfigEnum.FILE_STORESCSV.getValue();
	public static final String csv_storesDetail = RESConfigEnum.FILE_STORESDETAILCSV.getValue();
	public static final String csv_tableStore = RESConfigEnum.FILE_TABLESTORE.getValue();
	public static final String csv_tableGroup = RESConfigEnum.FILE_TABLEGROUP.getValue();
	public static final String csv_accesslog = RESConfigEnum.FILE_ACCESSLOGSFILE.getValue();

	public static final String xml_schedule = RESConfigEnum.FILE_SCHEDULE.getValue();
	public static final String xml_schedule_notices = RESConfigEnum.FILE_NOTICESXML.getValue();
	public static final String xml_schedule_pickList = RESConfigEnum.FILE_PICKLISTXML.getValue();
	public static final String xml_resProfile = RESConfigEnum.FILE_RESPROFILE.getValue();

	public static final String trn_default = RESConfigEnum.FILE_DEFAULTTRN.getValue();
	public static final String trn_com = RESConfigEnum.FILE_COMTRN.getValue();
	public static final String dst_default = RESConfigEnum.FILE_DEFAULTDST.getValue();

	public static final String ver_server = "server.ver";
	public static final String ver_custom = "custom.ver";
	public static final String ver_notices = "notices.ver";
	public static final String ver_pickList = "pickList.ver";

	public static final String listenTimer = RESConfigEnum.REQUESTTIMER.toString();

	public static final String sys_windows = "Windows";
	public static final String sys_osname = System.getProperties().getProperty("os.name");
	public static final String sys_UserHome = System.getProperty("user.home").split(":")[0];
	public static final String sys_computerName = System.getenv("COMPUTERNAME");

	public static final String config_resTransPath = RESConfigEnum.FILE_RESTRANSPATH.getValue();

	public static final String[] resourceArr = RESConfigEnum.RESOURCE.getValue().split(",");
	public static final String[] resconfigArr = RESConfigEnum.RESCONFIG.getValue().split(",");

}
