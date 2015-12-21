package ncr.res.mobilepos.softwareinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * The Model Class for the searched SoftwareComponents.
 *<br>
 * Members:<br>
 * ResultCode   : Result code of the operation.<br>
 * SoftwareVersion serverVersionInfo
 * SoftwareVersion javaVersionInfo
 * SoftwareVersion serviceVersionInfo
 * SoftwareVersion operatingSystemVersionInfo
 * 
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SoftwareComponents")
public class SoftwareComponents extends ResultBase {
    
    @XmlElement(name = "ServerInfo")
    private SoftwareVersion serverVersionInfo;
    
    @XmlElement(name = "JavaInfo")
    private SoftwareVersion javaVersionInfo;
    
    @XmlElement(name = "ServiceInfo")
    private SoftwareVersion serviceVersionInfo;
    
    @XmlElement(name = "OperatingSystemInfo")
    private SoftwareVersion operatingSystemVersionInfo;

	public SoftwareVersion getServerVersionInfo() {
		return serverVersionInfo;
	}

	public void setServerVersionInfo(SoftwareVersion serverVersionInfo) {
		this.serverVersionInfo = serverVersionInfo;
	}

	public SoftwareVersion getJavaVersionInfo() {
		return javaVersionInfo;
	}

	public void setJavaVersionInfo(SoftwareVersion javaVersionInfo) {
		this.javaVersionInfo = javaVersionInfo;
	}

	public SoftwareVersion getServiceVersionInfo() {
		return serviceVersionInfo;
	}

	public void setServiceVersionInfo(SoftwareVersion serviceVersionInfo) {
		this.serviceVersionInfo = serviceVersionInfo;
	}

	public SoftwareVersion getOperatingSystemVersionInfo() {
		return operatingSystemVersionInfo;
	}

	public void setOperatingSystemVersionInfo(
			SoftwareVersion operatingSystemVersionInfo) {
		this.operatingSystemVersionInfo = operatingSystemVersionInfo;
	}   
	
}
