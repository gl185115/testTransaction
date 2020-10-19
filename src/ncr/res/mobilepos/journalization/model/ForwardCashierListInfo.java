package ncr.res.mobilepos.journalization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlRootElement(name = "ForwardCashierListInfo")
@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="ForwardCashierListInfo")
public class ForwardCashierListInfo {

    @XmlElement(name = "DeviceName")
    private String DeviceName;

    @XmlElement(name = "TerminalId")
    private String TerminalId;

    @XmlElement(name = "IPAddress")
    private String IPAddress;

    @XmlElement(name = "TerminalType")
    private String TerminalType;

    @ApiModelProperty(value="ê∏éZã@ñºèÃ", notes="ê∏éZã@ñºèÃ")
    public String getDeviceName() {
		return DeviceName;
	}

	public void setDeviceName(String deviceName) {
		DeviceName = deviceName;
	}

    @ApiModelProperty(value="ê∏éZã@ÇÃí[ññî‘çÜ", notes="ê∏éZã@ÇÃí[ññî‘çÜ")
	public String getTerminalId() {
		return TerminalId;
	}

	public void setTerminalId(String terminalId) {
		TerminalId = terminalId;
	}

    @ApiModelProperty(value="ê∏éZã@ÇÃIpaddress", notes="ê∏éZã@ÇÃIpaddress")
	public String getIPAddress() {
		return IPAddress;
	}

	public void setIPAddress(String iPAddress) {
		IPAddress = iPAddress;
	}

    @ApiModelProperty(value="ê∏éZã@ÇÃTerminalType", notes="ê∏éZã@ÇÃTerminalType")
	public String getTerminalType() {
		return TerminalType;
	}

	public void setTerminalType(String terminalType) {
		TerminalType = terminalType;
	}
}
