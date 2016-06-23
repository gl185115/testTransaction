package ncr.res.mobilepos.softwareinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
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
@ApiModel(value="SoftwareComponents")
public class SoftwareComponents extends ResultBase {

    @XmlElement(name = "ServerInfo")
    private SoftwareVersion serverVersionInfo;

    @XmlElement(name = "JavaInfo")
    private SoftwareVersion javaVersionInfo;

    @XmlElement(name = "ServiceInfo")
    private SoftwareVersion serviceVersionInfo;

    @XmlElement(name = "OperatingSystemInfo")
    private SoftwareVersion operatingSystemVersionInfo;

    @ApiModelProperty(value="�T�[�o�̃o�[�W�������", notes="�T�[�o�̃o�[�W�������")
	public SoftwareVersion getServerVersionInfo() {
		return serverVersionInfo;
	}

	public void setServerVersionInfo(SoftwareVersion serverVersionInfo) {
		this.serverVersionInfo = serverVersionInfo;
	}

	@ApiModelProperty(value="�T�[�o��java���", notes="�T�[�o��java���")
	public SoftwareVersion getJavaVersionInfo() {
		return javaVersionInfo;
	}

	public void setJavaVersionInfo(SoftwareVersion javaVersionInfo) {
		this.javaVersionInfo = javaVersionInfo;
	}

	@ApiModelProperty(value="�T�[�r�X�̃o�[�W�������", notes="�T�[�r�X�̃o�[�W�������")
	public SoftwareVersion getServiceVersionInfo() {
		return serviceVersionInfo;
	}

	public void setServiceVersionInfo(SoftwareVersion serviceVersionInfo) {
		this.serviceVersionInfo = serviceVersionInfo;
	}

	@ApiModelProperty(value="�I�y���[�e�B���O�V�X�e���̃o�[�W�������", notes="�I�y���[�e�B���O�V�X�e���̃o�[�W�������")
	public SoftwareVersion getOperatingSystemVersionInfo() {
		return operatingSystemVersionInfo;
	}

	public void setOperatingSystemVersionInfo(
			SoftwareVersion operatingSystemVersionInfo) {
		this.operatingSystemVersionInfo = operatingSystemVersionInfo;
	}

}
