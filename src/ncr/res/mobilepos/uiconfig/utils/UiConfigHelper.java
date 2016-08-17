package ncr.res.mobilepos.uiconfig.utils;

import ncr.res.mobilepos.helper.XmlSerializer;
import ncr.res.mobilepos.uiconfig.model.UiConfigType;
import ncr.res.mobilepos.uiconfig.model.deploystatus.*;
import ncr.res.mobilepos.uiconfig.model.schedule.Schedule;
import ncr.res.mobilepos.uiconfig.model.schedule.Task;
import ncr.res.mobilepos.uiconfig.model.store.CSVStore;

import javax.xml.bind.JAXBException;
import javax.xml.transform.OutputKeys;
import javax.xml.transform.Source;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.stream.StreamResult;
import javax.xml.transform.stream.StreamSource;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.*;

public class UiConfigHelper {
    // Encodings for files.
    private static final String ENCODING_UTF8 = "UTF-8";
    public static final String ENCODING_SCHEDULE_XML = ENCODING_UTF8;
    public static final String ENCODING_STORES_CSV = ENCODING_UTF8;
    public static final String ENCODING_CONFIG_FILE = ENCODING_UTF8;
    public static final String ENCODING_DEPLOY_STATUS_FILE = ENCODING_UTF8;
    // URL encoding charset.
    public static final String URL_ENCODING_CHARSET = ENCODING_UTF8;

    /**
     * @param stringVar
     * @return
     */
    public static boolean isNullOrEmpty(String stringVar) {
        return stringVar == null || stringVar.isEmpty();
    }

    /**
     * Marshall schedule.xml into Schedule.
     *
     * @return
     */
    public static Schedule marshallScheduleXml(String scheduleXmlFilePath) throws IOException {
        File scheduleFile = new File(scheduleXmlFilePath);
        if (!scheduleFile.exists()) {
            return null;
        }
        String xmlString = readFileToString(scheduleFile, ENCODING_SCHEDULE_XML);
        return (Schedule) XmlToObjectConverter.parseXml(xmlString, Schedule.class);
    }

    /**
     * Reads the file into String.
     *
     * @param scheduleFile
     * @return
     */
    public static String readFileToString(File scheduleFile, String encoding) throws IOException {
        byte[] content = Files.readAllBytes(Paths.get(scheduleFile.getAbsolutePath()));
        return new String(content, encoding);
    }

    /**
     * Loads stores.csv into CSVStore.
     *
     * @return a list of CSVStore.
     */
    public static List<CSVStore> loadStoresCSV(String storesCsvFilePath) throws IOException {
        File storeCsvFile = new File(storesCsvFilePath);

        List<CSVStore> csvStores = new ArrayList<>();
        try (
                BufferedReader reader = new BufferedReader(
                        new InputStreamReader(new FileInputStream(storeCsvFile), ENCODING_STORES_CSV))) {
            String line;
            while ((line = reader.readLine()) != null) {
                CSVStore store = new CSVStore(line.replace("\uFEFF", "").trim());
                csvStores.add(store);
            }
        }
        return csvStores;
    }


    /**
     * Searches image file with given name under base path.
     *
     * @param basePath      base path to start searching.
     * @param imageFileName file name to match.
     * @return File object for found file. Null for not found,
     */
    public static File searchImageFile(String basePath, String imageFileName) {
        return findFile(new File(basePath), imageFileName);
    }

    /**
     * It recursively searches a file under its subdirectories.
     *
     * @param path     path to search.
     * @param fileName file name to search.
     * @return File object for found file. Null for not found,
     */
    private static File findFile(File path, String fileName) {
        // path should be directory.
        if (!path.isDirectory()) {
            return null;
        }

        // Check if the file is in the directory.
        File fileToFind = new File(path.getAbsolutePath() + File.separator + fileName);
        if (fileToFind.exists() && fileToFind.isFile()) {
            // Found the file and returns.
            return fileToFind;
        }

        // File is not found in the directory.
        File[] files = path.listFiles();
        for (File anotherFile : files) {
            File foundFile = findFile(anotherFile, fileName);
            if (foundFile != null) {
                return foundFile;
            }
        }
        // Returns null for not found after searching all the subdirectories.
        return null;
    }

    /********
     * Below methods are for doDeployStatusCustomRequest.
     *********/


    public static synchronized void doDeployStatusCustomRequest(UiConfigType deployType,
                                                                String companyID, String storeID, String workstationID, Task task,
                                                                String deployStatusFilePath) {

        DeployStatus deployStatus = serializerDeployStatusXmlToObject(deployStatusFilePath);
        deployStatus = serializerDeployStatusFile(deployType, companyID,
                storeID, workstationID, deployStatus, task);

        if (deployStatus != null) {
            serializerDeployStatusObjectToXml(deployStatus, deployStatusFilePath);
        }
    }

    /**
     * Set XML file pretty.
     */
    private static String prettyFormat(String input) {
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
            throw new RuntimeException(e); // simple exception handling, please review it
        } finally {
            try {
                if (stringReader != null) {
                    stringReader.close();
                }
                if (stringWriter != null) {
                    stringWriter.close();
                }
            } catch (IOException e) {
                // LOG.info("Error in closing in finally : " +e.getMessage());
            }
        }
    }

    private static DeployStatus serializerDeployStatusXmlToObject(String deployStatusFilePath) {

        DeployStatus deployStatus = new DeployStatus();
        XmlSerializer<DeployStatus> xmlSer = new XmlSerializer<>();

        File deployStatusFile = new File(deployStatusFilePath);

        try {
            if (!deployStatusFile.exists()) {
                deployStatusFile.getParentFile().mkdirs();
                deployStatusFile.createNewFile();
            }

            String xmlString = readFileToString(deployStatusFile, ENCODING_DEPLOY_STATUS_FILE);
            deployStatus = xmlSer.unMarshallXml(xmlString, DeployStatus.class);
        } catch (IOException e) {
            // LOG.error("Error in create config files : " + deployStatusFile.getPath());
            return null;
        } catch (JAXBException e) {
            // LOG.error("Error in Serializer config files : " + deployStatusFile.getPath());
            return null;
        }

        return deployStatus;
    }

    private static DeployStatus serializerDeployStatusFile(UiConfigType deployType, String companyID,
                                                           String storeID, String workstationID, DeployStatus deployStatus, Task task) {

        DeployConfig dConfig = new DeployConfig();
        List<DeployConfig> dConfigList = new ArrayList<>();
        DeployEffective dEffective = new DeployEffective();
        EPickList ePickList = new EPickList();
        EAdvertise eAdvertise = new EAdvertise();
        EOptions eOptions = new EOptions();
        EUsability eUsability = new EUsability();
        ENotices eNotices = new ENotices();
        DeployApplied dApplied = new DeployApplied();
        List<DeployApplied> dAppliedList = new ArrayList<>();
        DeployStoreID dStoreID = new DeployStoreID();
        List<String> storeIDList = new ArrayList<>();

        boolean needDeploy = false;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        StringBuilder logInfo = new StringBuilder();
        logInfo.append(sdf.format(new Date()) + " ");
        logInfo.append("companyID=" + companyID + ", ");
        logInfo.append("storeID=" + storeID + ", ");
        logInfo.append("workstationID=" + workstationID + " : ");
        logInfo.append("applied " + deployType + File.separator + task.getFilename());

        storeIDList.add(storeID);
        dStoreID.setStoreID(storeIDList);
        dApplied.setDeployStoreID(dStoreID);
        dApplied.setStore(task.getTarget().getStore());
        dApplied.setWorkstation(task.getTarget().getWorkstation());
        dApplied.setFilename(task.getFilename());
        dApplied.setEffective(task.getEffective());
        dAppliedList.add(dApplied);

        switch (deployType.getValue()) {
            case "pickList":
                ePickList = new EPickList();
                ePickList.setDeployAppliedList(dAppliedList);
                dEffective.setPickList(ePickList);
                break;
            case "advertise":
            	eAdvertise = new EAdvertise();
            	eAdvertise.setDeployAppliedList(dAppliedList);
                dEffective.setAdvertise(eAdvertise);
                break;
            case "options":
                eOptions = new EOptions();
                eOptions.setDeployAppliedList(dAppliedList);
                dEffective.setOptions(eOptions);
                break;
            case "usability":
                eUsability = new EUsability();
                eUsability.setDeployAppliedList(dAppliedList);
                dEffective.setUsability(eUsability);
                break;
            case "notices":
                eNotices = new ENotices();
                eNotices.setDeployAppliedList(dAppliedList);
                dEffective.setNotices(eNotices);
                break;
            default:
                break;
        }

        dConfig.setCompanyId(companyID);
        dConfig.setDeployEffective(dEffective);
        dConfigList.add(dConfig);

        if (deployStatus == null) {
            deployStatus = new DeployStatus();
            deployStatus.setDeployConfig(dConfigList);
            // LOG.debug(logInfo.toString());
            return deployStatus;
        }

        List<DeployConfig> dcList = deployStatus.getDeployConfig();
        if (dcList == null || dcList.size() == 0) {
            deployStatus.setDeployConfig(dConfigList);
            // LOG.debug(logInfo.toString());
            return deployStatus;
        }

        DeployConfig dc = null;
        for (int i = 0; i < dcList.size(); i++) {
            dc = dcList.get(i);
            if (dc.getCompanyId().equals(companyID)) {
                needDeploy = false;
                break;
            } else {
                needDeploy = true;
            }
        }

        if (needDeploy) {
            deployStatus.getDeployConfig().add(dConfig);
            dcList = deployStatus.getDeployConfig();
            Collections.sort(dcList, new Comparator<DeployConfig>() {
                public int compare(DeployConfig arg0, DeployConfig arg1) {
                    return arg0.getCompanyId().compareTo(arg1.getCompanyId());
                }
            });
            // LOG.debug(logInfo.toString());
            return deployStatus;
        }

        for (int i = 0; i < dcList.size(); i++) {
            dc = dcList.get(i);
            if (dc.getCompanyId().equals(companyID)) {
                if (dc.getDeployEffective() == null) {
                    deployStatus.getDeployConfig().get(i).setDeployEffective(dEffective);
                    // LOG.debug(logInfo.toString());
                    return deployStatus;
                }

                List<DeployApplied> daList = null;
                switch (deployType.getValue()) {
                    case "pickList":
                        EPickList ep = dc.getDeployEffective().getPickList();
                        if (ep != null) {
                            daList = ep.getDeployAppliedList();
                            break;
                        } else {
                            deployStatus.getDeployConfig().get(i).
                                    getDeployEffective().setPickList(ePickList);
                            // LOG.debug(logInfo.toString());
                            return deployStatus;
                        }
                    case "advertise":
                    	EAdvertise ea = dc.getDeployEffective().getAdvertise();
                        if (ea != null) {
                            daList = ea.getDeployAppliedList();
                            break;
                        } else {
                            deployStatus.getDeployConfig().get(i).
                                    getDeployEffective().setAdvertise(eAdvertise);
                            // LOG.debug(logInfo.toString());
                            return deployStatus;
                        }
                    case "options":
                        EOptions eo = dc.getDeployEffective().getOptions();
                        if (eo != null) {
                            daList = eo.getDeployAppliedList();
                            break;
                        } else {
                            deployStatus.getDeployConfig().get(i).
                                    getDeployEffective().setOptions(eOptions);
                            // LOG.debug(logInfo.toString());
                            return deployStatus;
                        }
                    case "usability":
                        EUsability eu = dc.getDeployEffective().getUsability();
                        if (eu != null) {
                            daList = eu.getDeployAppliedList();
                            break;
                        } else {
                            deployStatus.getDeployConfig().get(i).
                                    getDeployEffective().setUsability(eUsability);
                            // LOG.debug(logInfo.toString());
                            return deployStatus;
                        }
                    case "notices":
                        ENotices en = dc.getDeployEffective().getNotices();
                        if (en != null) {
                            daList = en.getDeployAppliedList();
                            break;
                        } else {
                            deployStatus.getDeployConfig().get(i).
                                    getDeployEffective().setNotices(eNotices);
                            // LOG.debug(logInfo.toString());
                            return deployStatus;
                        }
                    default:
                        break;
                }

                if (daList.size() != 0) {
                    for (DeployApplied da : daList) {
                        List<String> idList = da.getDeployStoreID().getStoreID();
                        if (task.getEffective().equals(da.getEffective())
                                && task.getFilename().equals(da.getFilename())
                                && task.getTarget().getStore().equals(da.getStore())) {
                            if (idList.contains(storeID)) {
                                needDeploy = false;
                                continue;
                            } else {
                                idList.add(storeID);
                                Collections.sort(idList);
                                // LOG.debug(logInfo.toString());
                                return deployStatus;
                            }
                        } else {
                            needDeploy = true;

                        }
                    }

                    if (needDeploy) {
                        switch (deployType.getValue()) {
                            case "pickList":
                                deployStatus.getDeployConfig().get(i).getDeployEffective()
                                        .getPickList().getDeployAppliedList().add(dApplied);
                                // LOG.debug(logInfo.toString());
                                return deployStatus;
                            case "advertise":
                                deployStatus.getDeployConfig().get(i).getDeployEffective()
                                        .getAdvertise().getDeployAppliedList().add(dApplied);
                                // LOG.debug(logInfo.toString());
                                return deployStatus;
                            case "options":
                                deployStatus.getDeployConfig().get(i).getDeployEffective()
                                        .getOptions().getDeployAppliedList().add(dApplied);
                                // LOG.debug(logInfo.toString());
                                return deployStatus;
                            case "usability":
                                deployStatus.getDeployConfig().get(i).getDeployEffective()
                                        .getUsability().getDeployAppliedList().add(dApplied);
                                // LOG.debug(logInfo.toString());
                                return deployStatus;
                            case "notices":
                                deployStatus.getDeployConfig().get(i).getDeployEffective()
                                        .getNotices().getDeployAppliedList().add(dApplied);
                                // LOG.debug(logInfo.toString());
                                return deployStatus;
                            default:
                                break;
                        }
                    }
                } else {
                    deployStatus.getDeployConfig().get(i)
                            .getDeployEffective().getPickList().setDeployAppliedList(dAppliedList);
                    // LOG.debug(logInfo.toString());
                    return deployStatus;
                }
            }
        }

        return null;
    }

    private static void serializerDeployStatusObjectToXml(DeployStatus deployStatus, String deployStatusFilePath) {

        String xmlString = null;
        FileWriter fileWriter = null;
        XmlSerializer<DeployStatus> xmlSer = new XmlSerializer<>();

        File deployStatusFile = new File(deployStatusFilePath);

        try {
            if (deployStatus != null) {
                xmlString = xmlSer.marshallObj(DeployStatus.class, deployStatus, "utf-8");
                fileWriter = new FileWriter(deployStatusFile);
                fileWriter.write(prettyFormat(xmlString));
                fileWriter.flush();
                fileWriter.close();
            }
        } catch (IOException e) {
            // LOG.error("Error in write config files : " + deployStatusFile.getPath());
        } catch (JAXBException e) {
            // LOG.error("Error in serializer config files : " + deployStatusFile.getPath());
        }
    }

}
