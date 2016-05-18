package ncr.res.mobilepos.uiconfig.utils;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.List;

import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;

import org.json.JSONObject;

import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.uiconfig.model.schedule.Company;
import ncr.res.mobilepos.uiconfig.model.schedule.Config;
import ncr.res.mobilepos.uiconfig.model.schedule.Deploy;
import ncr.res.mobilepos.uiconfig.model.schedule.Schedule;
import ncr.res.mobilepos.uiconfig.model.schedule.Target;
import ncr.res.mobilepos.uiconfig.model.schedule.Task;

public class ScheduleXmlUtil {

	public static Schedule getSchedule(File pSchedulePath) {

		String scheduleStr = "";

		try {

			if (pSchedulePath.exists()) {
				scheduleStr = FileUtil.fileRead(pSchedulePath);
				return (Schedule) XmlToObjectConverter.parseXml(scheduleStr, Schedule.class);
			} else {
			}

		} catch (Exception e) {
		}

		return null;
	}

	public static String getSchedule(File pScheduleFile, String pResource, String companyID) {

		StringBuilder scheduleJson = new StringBuilder("\n{\"schedule\":");

		Schedule schedule = getSchedule(pScheduleFile);
		if (schedule == null) {
			schedule = getEmptyScheduleModel(pResource, companyID);
		}

		scheduleJson.append(schedule.toString()).append("}");
		return scheduleJson.toString();
	}

	public static Deploy getDeploy(File pSchedulePath, String pCompanyID) {

		Deploy deploy = null;
		Schedule scheduleModel = null;

		scheduleModel = getSchedule(pSchedulePath);
		if (scheduleModel == null) {
			return null;
		}

		deploy = getDeploy(scheduleModel, pCompanyID);
		if (deploy == null) {
			return null;
		}

		return deploy;
	}

	public static Deploy getDeploy(Schedule pScheduleModel, String pCompanyID) {

		boolean isFindFlg = false;

		try {
			if (pScheduleModel != null && pScheduleModel.getDeploy() != null) {
				for (Deploy deploy : pScheduleModel.getDeploy()) {
					if (deploy == null || deploy.getCompany() == null) continue;
					if (pCompanyID.equals(deploy.getCompany().getId())) {
						isFindFlg = true;
						return deploy;
					}
				}

				if (!isFindFlg) {
				}
			} else {
			}
		} catch (Exception e) {
		}

		return null;
	}

//	public static List<Config> getConfigList(Schedule pScheduleModel, String pCompanyID) {
//
//		String method = LogUtil.getCurrentMethodName();
//		boolean isFindFlg = false;
//
//		try {
//			if (pScheduleModel != null && pScheduleModel.getDeploy() != null) {
//				for (Deploy deploy : pScheduleModel.getDeploy()) {
//					if (deploy == null || deploy.getCompany() == null) continue;
//					if (pCompanyID.equals(deploy.getCompany().getId())) {
//						isFindFlg = true;
//						return deploy.getConfig();
//					}
//				}
//
//				if (!isFindFlg) {
//					LOG.error(method + "Can not find the schedule config element by companyID : " + pCompanyID);
//				}
//			} else {
//				LOG.error(method + "Schedule model or Schedule config element is null or empty!");
//			}
//		} catch (Exception e) {
//			LOG.error(method + LogUtil.RES_EXCEPTION + e.toString());
//		}
//
//		return null;
//	}

	public static Config getConfig(Deploy pDeploy, String pResource) {

		Config config = null;
		boolean isFindFlg = false;

		try {
			if (pDeploy != null && pDeploy.getConfig().size() > 0) {
				for (Config con : pDeploy.getConfig()) {
					if (con == null || StringUtility.isNullOrEmpty(con.getResource())) continue;
					if (pResource.equalsIgnoreCase(con.getResource())) {
						config = con;
						isFindFlg = true;
						break;
					}
				}
			}

			if (!isFindFlg) {
			}
		} catch (Exception e) {
		}

		return config;
	}

	public static Config getConfig(List<Config> pConfigList, String pConfigType) {

		boolean isFindFlg = false;

		try {
			if (pConfigList != null && pConfigList.size() != 0) {
				for (Config config : pConfigList) {
					if (config != null) {
						if (pConfigType.equalsIgnoreCase(config.getResource())) {
							isFindFlg = true;
							return config;
						}
					}
				}

				if (!isFindFlg) {
				}
			} else {
			}
		} catch (Exception e) {
		}

		return null;
	}

//	public static List<Task> getTaskList(String pResource) {
//
//		String method = LogUtil.getCurrentMethodName();
//		List<Task> taskList = null;
//		File xml_schedule = null;
//
//		try {
//			if (StaticParameter.key_pickList.equalsIgnoreCase(pResource)) {
//				xml_schedule = new File(StaticParameter.dir_pickList, StaticParameter.xml_schedule);
//			} else if (StaticParameter.key_notices.equalsIgnoreCase(pResource)) {
//				xml_schedule = new File(StaticParameter.dir_notices, StaticParameter.xml_schedule);
//			} else if (StaticParameter.key_usability.equalsIgnoreCase(pResource)) {
//				xml_schedule = new File(StaticParameter.dir_usability, StaticParameter.xml_schedule);
//			} else if (StaticParameter.key_options.equalsIgnoreCase(pResource)) {
//				xml_schedule = new File(StaticParameter.dir_options, StaticParameter.xml_schedule);
//			}
//
//			taskList = getTaskList(xml_schedule, pResource);
//		} catch (Exception e) {
//			LOG.error(method + LogUtil.RES_EXCEPTION + " : " + pResource + " : " + e.toString());
//			return null;
//		}
//
//		return taskList;
//	}

//	public static List<Task> getTaskList(File pSchedulePath, String pResource) {
//
//		String method = LogUtil.getCurrentMethodName();
//		List<Config> configList = null;
//		List<Task> taskList = null;
//
//		Schedule scheduleModel = getSchedule(pSchedulePath);
//		if (scheduleModel == null) {
//			LOG.error(method + "Schedule file cannot be parsed into object. Xml format might be incorrect.");
//			return null;
//		}
//
//		configList = getConfigList(scheduleModel, StaticParameter.companyID);
//		if (configList == null) {
//			LOG.info(method + "There is no configuration list set for company : " + StaticParameter.companyID);
//			return null;
//		}
//
//		taskList = getTaskList(configList, pResource);
//		if (taskList == null || taskList.size() == 0) {
//			LOG.info(method + "There is no configuration list set for config : " + pResource);
//			return null;
//		}
//
//		return taskList;
//	}

//	public static List<Task> getTaskList(List<Config> pConfigList, String pConfigType) {
//
//		String method = LogUtil.getCurrentMethodName();
//		boolean isFindFlg = false;
//
//		try {
//			if (pConfigList != null && pConfigList.size() != 0) {
//				for (Config config : pConfigList) {
//					if (config != null) {
//						if (pConfigType.equalsIgnoreCase(config.getResource())) {
//							isFindFlg = true;
//							return config.getTask();
//						}
//					}
//				}
//
//				if (!isFindFlg) {
//					LOG.error(method + "Can not find the schedule config task by config type : " + pConfigType);
//				}
//			} else {
//				LOG.error(method + "Schedule config element is null or empty!");
//			}
//		} catch (Exception e) {
//			LOG.error(method + e.toString());
//		}
//
//		return null;
//	}

//	public static Task getTask(File pScheduleXml, String pResource, String pFilename, String pEffective) {
//
//		String method = LogUtil.getCurrentMethodName();
//		LOG.debug(LogUtil.methodStart(method));
//
//		List<Task> taskList = null;
//		Task task = null;
//
//		taskList = getTaskList(pScheduleXml, pResource);
//		if (taskList == null || taskList.size() == 0) {
//			LOG.info(method + "Schedule task element is null or empty by config type " + pResource);
//			return null;
//		}
//
//
//		task = getTask(taskList, pFilename, pEffective);
//
//		LOG.debug(LogUtil.methodExit(method));
//		return task;
//	}

//	public static Task getTask(List<Task> pTaskList, String pFilename, String pEffective) {
//
//		if (pTaskList != null && pTaskList.size() > 0) {
//			for (Task task : pTaskList) {
//				if (task.getTarget() == null) continue;
//				if (task.getEffective().equals(pEffective) && task.getFilename().equals(pFilename)) {
//					return task;
//				}
//			}
//		}
//
//		return null;
//	}

	public static boolean saveSchedule(Schedule pSchedule, File pScheduleXml) {

		String xmlString = null;
		XmlSerializer<Schedule> xmlSer = new XmlSerializer<Schedule>();
		boolean retFlg = false;

		try {
			if (pSchedule != null) {
				FileUtil.fileBackupByCmdCopy(pScheduleXml, false);

				xmlString = xmlSer.marshallObj(Schedule.class, pSchedule, StaticParameter.code_UTF8);
				xmlString = prettyFormat(xmlString);

				if (FileUtil.fileSave(pScheduleXml, xmlString, false, StaticParameter.code_UTF8)) {
					retFlg = true;

				} else {
					FileUtil.fileRollback(pScheduleXml);
				}
			} else {
			}
		} catch (Exception e) {
			retFlg = false;
		}

		return retFlg;
	}

	public static String saveScheduleByJSON(String pScheduleJson, File pSchedulePath) {

		String xmlString = null;
		XmlSerializer<Schedule> xmlSer = new XmlSerializer<Schedule>();

		try {
			if (!StringUtility.isNullOrEmpty(pScheduleJson)) {
				if (!pSchedulePath.exists()) {
					//LOG.error(method + LogUtil.RES_NOTEXIST + pSchedulePath.getPath());
				}

				JSONObject json = new JSONObject(pScheduleJson);
				xmlString = org.json.XML.toString(json);
				Schedule scheduleModel = xmlSer.unMarshallXml(xmlString, Schedule.class);

				if (scheduleModel != null) {
					xmlString = xmlSer.marshallObj(Schedule.class, scheduleModel, StaticParameter.code_MS932);
					xmlString = xmlSer.marshallObj(Schedule.class, scheduleModel, StaticParameter.code_UTF8);
					xmlString = prettyFormat(xmlString);

					if (FileUtil.fileSave(pSchedulePath, xmlString, false, StaticParameter.code_UTF8)) {
						//LOG.info(method + "Save schedule XML [SUCCESS] : " + pSchedulePath.getPath());
					} else {
						//LOG.info(method + "Save schedule XML [FAILED]");
					}
				} else {
					//LOG.info(method + "Save schedule XML [FAILED] Target schedule model is null!");
				}
			}

		} catch (Exception e) {
			//LOG.error(method + LogUtil.RES_EXCEPTION + e.toString());
			//LOG.error(method + pScheduleJson);
			//LOG.error(method + "Error in write schedule file : " + pSchedulePath.getPath());
			return null;
		}

		return xmlString;
	}


//	public static boolean isTaskMatched(List<Task> pTaskList, String pEffective, String pFilename) {
//
//		if (pTaskList != null && pTaskList.size() > 0) {
//			for (Task task : pTaskList) {
//				if (task == null) continue;
//				if (task.getEffective().equals(pEffective) && task.getFilename().equals(pFilename)) {
//					return true;
//				}
//			}
//		}
//
//		return false;
//	}

	public static Schedule getEmptyScheduleModel(String pResource,
			String companyID) {

		Schedule schedule = null;
		Company company = null;
		Deploy deploy = null;
		List<Deploy> deployList = new ArrayList<Deploy>();
		Config config = null;
		List<Config> configList = new ArrayList<Config>();
		Task task = null;
		Target target = null;
		List<Task> taskList = new ArrayList<Task>();

		try {
			target = new Target();
//			target.setGroup(StaticParameter.str_empty);
			target.setStore(StaticParameter.str_empty);
			target.setWorkstation(StaticParameter.str_empty);

			task = new Task();
			task.setEffective(StaticParameter.str_empty);
			task.setFilename(StaticParameter.str_empty);
			task.setTarget(target);
			taskList.add(task);

			config = new Config();
			config.setResource(pResource);
			config.setTask(taskList);
			configList.add(config);

			company = new Company();
			company.setId(companyID);
			company.setName(StaticParameter.str_empty);

			deploy = new Deploy();
			deploy.setCompany(company);
			deploy.setConfig(configList);
			deployList.add(deploy);

			schedule = new Schedule();
			schedule.setDeploy(deployList);

		} catch (Exception e) {
		}

		return schedule;
	}

//	public static void getScheduleAtInit() {
//
//		File xml_schedule = null;
//		Schedule schedule = null;
//
//		for (String resource : StaticParameter.resconfigArr) {
//			xml_schedule = new File(StaticParameter.dir_custom, resource);
//			xml_schedule = new File(xml_schedule, StaticParameter.xml_schedule);
//			if (!xml_schedule.exists() || !xml_schedule.isFile()) {
//				xml_schedule = new File(StaticParameter.dir_schedule, String.format(StaticParameter.format_scheduleXml, resource));
//				if (!xml_schedule.exists() || !xml_schedule.isFile()) {
//					xml_schedule = StaticParameter.file_xml_schedule;
//					if (!xml_schedule.exists() || !xml_schedule.isFile()) {
//						continue;
//					}
//				}
//			}
//
//			schedule = ScheduleXmlUtil.getSchedule(xml_schedule);
//			if (StaticParameter.key_notices.equalsIgnoreCase(resource)) {
//				scheduleNoticesModel = schedule;
//
//			} else if (StaticParameter.key_pickList.equalsIgnoreCase(resource)) {
//				schedulePickListModel = schedule;
//
//			} else if (StaticParameter.key_usability.equalsIgnoreCase(resource)) {
//				scheduleUsabilityModel = schedule;
//
////			} else if (StaticParameter.key_options.equalsIgnoreCase(resource)) {
////				scheduleOptionsModel = schedule;
//			}
//		}
//	}

//	public static void getScheduleTaskListAtInit() {
//
//		File xml_schedule = null;
//		List<Task> taskList = null;
//
//		for (String resource : StaticParameter.resconfigArr) {
//			xml_schedule = new File(StaticParameter.dir_custom, resource);
//			xml_schedule = new File(xml_schedule, StaticParameter.xml_schedule);
//
//			if (!xml_schedule.exists() || !xml_schedule.isFile()) {
//				xml_schedule = new File(StaticParameter.dir_schedule,
//						String.format(StaticParameter.format_scheduleXml, resource));
//
//				if (!xml_schedule.exists() || !xml_schedule.isFile()) {
//					xml_schedule = StaticParameter.file_xml_schedule;
//					if (!xml_schedule.exists() || !xml_schedule.isFile()) {
//						continue;
//					}
//				}
//			}
//
//			taskList = ScheduleXmlUtil.getTaskList(xml_schedule, resource);
//			if (StaticParameter.key_notices.equalsIgnoreCase(resource)) {
//				schedule_noticesTaskList = taskList;
//
//			} else if (StaticParameter.key_pickList.equalsIgnoreCase(resource)) {
//				schedule_pickListTaskList = taskList;
//
//			} else if (StaticParameter.key_usability.equalsIgnoreCase(resource)) {
//				schedule_usabilityTaskList = taskList;
//
////			} else if (StaticParameter.key_options.equalsIgnoreCase(resource)) {
////				schedule_optionsTaskList = taskList;
//			}
//		}
//	}

//	public static boolean isDeployLogFileExistInSchedule(String pEffective, String pFilename) {
//
//		List<Task> taskList = null;
//
//		for (String resource : StaticParameter.resconfigArr) {
//			if (StaticParameter.key_notices.equalsIgnoreCase(resource)) {
//				taskList = schedule_noticesTaskList;
//
//			} else if (StaticParameter.key_pickList.equalsIgnoreCase(resource)) {
//				taskList = schedule_pickListTaskList;
//
//			} else if (StaticParameter.key_usability.equalsIgnoreCase(resource)) {
//				taskList = schedule_usabilityTaskList;
//
////			} else if (StaticParameter.key_options.equalsIgnoreCase(resource)) {
////				taskList = schedule_optionsTaskList;
//			}
//
//			if (isTaskMatched(taskList, pEffective, pFilename)) {
//				return true;
//			}
//		}
//
//		return false;
//	}

//	public static boolean isDeployLogFileExistInSchedule(String pEffective, String pFilename, String pResource) {
//
//		List<Task> taskList = null;
//
//		if (StaticParameter.key_notices.equalsIgnoreCase(pResource)) {
//			taskList = schedule_noticesTaskList;
//
//		} else if (StaticParameter.key_pickList.equalsIgnoreCase(pResource)) {
//			taskList = schedule_pickListTaskList;
//
//		} else if (StaticParameter.key_usability.equalsIgnoreCase(pResource)) {
//			taskList = schedule_usabilityTaskList;
//
////		} else if (StaticParameter.key_options.equalsIgnoreCase(pResource)) {
////			taskList = schedule_optionsTaskList;
//		}
//
//		return isTaskMatched(taskList, pEffective, pFilename);
//	}


//	public static boolean isExistScheduleDeployTaskLogOrNot(String pEffective, String pFilename, String pResource) {
//
//		List<Task> taskList = null;
//
//		if (StaticParameter.key_notices.equalsIgnoreCase(pResource)) {
//			taskList = schedule_noticesTaskList;
//
//		} else if (StaticParameter.key_pickList.equalsIgnoreCase(pResource)) {
//			taskList = schedule_pickListTaskList;
//		}
//
//		return ScheduleXmlUtil.isTaskMatched(taskList, pEffective, pFilename);
//	}

//	public static boolean isDeployLogFileExistInSchedule(String pEffective, String pFilename, String pResource) {
//
//		File xml_schedule = null;
//		List<Task> taskList = null;
//
//		xml_schedule = new File(StaticParameter.dir_custom, pResource);
//		xml_schedule = new File(xml_schedule, StaticParameter.xml_schedule);
//
//		if (!xml_schedule.exists() || !xml_schedule.isFile()) {
//			xml_schedule = new File(StaticParameter.dir_schedule, String.format(StaticParameter.format_scheduleXml, pResource));
//			if (!xml_schedule.exists() || !xml_schedule.isFile()) {
//				xml_schedule = StaticParameter.file_xml_schedule;
//				if (!xml_schedule.exists() || !xml_schedule.isFile()) {
//					return false;
//				}
//			}
//		}
//
//		taskList = ScheduleXmlUtil.getTaskListFromScheduleXml(xml_schedule, pResource, StaticParameter.companyID);
//		if (taskList != null && taskList.size() > 0) {
//			for (Task task : taskList) {
//				if (task == null) continue;
//				if (task.getEffective().equals(pEffective) && task.getFilename().equals(pFilename)) {
//					return true;
//				}
//			}
//		}
//
//		return false;
//	}

	/**
	 *  Set XML file pretty.
	 */
	public static String prettyFormat(String input) {

		StringWriter stringWriter = null;
		StringReader stringReader = null;

		try {
			stringReader = new StringReader(input);
			Source xmlInput = new StreamSource(stringReader);
			stringWriter = new StringWriter();
			StreamResult xmlOutput = new StreamResult(stringWriter);
			TransformerFactory transformerFactory = TransformerFactory.newInstance();
			transformerFactory.setAttribute("indent-number", 2);
			Transformer transformer = transformerFactory.newTransformer();
			transformer.setOutputProperty(OutputKeys.INDENT, "yes");
			transformer.transform(xmlInput, xmlOutput);
			return xmlOutput.getWriter().toString();
		} catch (Exception e) {
			return "";
		} finally {
			try {
				if (stringReader != null) {
					stringReader.close();
				}
				if (stringWriter != null) {
					stringWriter.close();
				}
			} catch (IOException e) {
			}
		}
	}

//	public static Schedule scheduleObject = null;
//	public static Schedule scheduleNoticesModel = null;
//	public static Schedule schedulePickListModel = null;
//	public static Schedule scheduleUsabilityModel = null;
//	public static Schedule scheduleOptionsModel = null;
//
//	public static List<Task> schedule_noticesTaskList = null;
//	public static List<Task> schedule_pickListTaskList = null;
//	public static List<Task> schedule_usabilityTaskList = null;
//	public static List<Task> schedule_optionsTaskList = null;
//
//	private static final Logger LOG = LogManager.getLogger(ScheduleXmlUtil.class.getName());
}
