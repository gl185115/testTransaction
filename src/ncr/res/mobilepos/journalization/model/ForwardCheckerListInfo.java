package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "ForwardCheckerListInfo")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="ForwardCheckerListInfo")
public class ForwardCheckerListInfo {

    @XmlElement(name = "DeviceName")
    private String DeviceName;

    @XmlElement(name = "TerminalId")
    private String TerminalId;

    @XmlElement(name = "IPAddress")
    private String IPAddress;

    @ApiModelProperty(value="���i�o�^�@����", notes="���i�o�^�@����")
    public String getDeviceName() {
		return DeviceName;
	}

	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}

    @ApiModelProperty(value="���i�o�^�@�̒[���ԍ�", notes="���i�o�^�@�̒[���ԍ�")
	public String getTerminalId() {
		return TerminalId;
	}

	public void setTerminalId(String terminalId) {
		TerminalId = terminalId;
	}

    @ApiModelProperty(value="���i�o�^�@��Ipaddress", notes="���i�o�^�@��Ipaddress")
	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}
}
