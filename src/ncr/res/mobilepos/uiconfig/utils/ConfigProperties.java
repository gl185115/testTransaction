package ncr.res.mobilepos.uiconfig.utils;

import java.util.ResourceBundle;

public class ConfigProperties {

	private ConfigProperties() {
		resource = ResourceBundle.getBundle(this.getResourceFile());
	}

	public static ConfigProperties getInstance() {
		if (instance == null) {
			synchronized (ConfigProperties.class) {
				instance = new ConfigProperties();
			}
		}
		return instance;
	}

	/*
	 * Custom Directory is relative to the
	 */
	public void setCustomDirectory(String customPath) {
		installationDrive = customPath;
	}

	public String getCustomDirectory() {
		return installationDrive;
	}

	public String getValue(String key) {
		if (("customDir").equals(key)) {
			return getCustomDirectory();
		}
		return (resource.containsKey(key)) ? resource.getString(key) : null;
	}

	public void setResourceFile(String fileName) {
		resource = ResourceBundle.getBundle(fileName);
		resourceBundleFile = fileName;
	}

	public String getResourceFile() {
		return resourceBundleFile;
	}

	private volatile ResourceBundle resource;
	private static ConfigProperties instance = null;
	private String installationDrive;
	private String resourceBundleFile = "resconfig";
}
