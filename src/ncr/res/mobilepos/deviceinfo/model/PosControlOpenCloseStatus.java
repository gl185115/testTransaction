package ncr.res.mobilepos.deviceinfo.model;

import ncr.res.mobilepos.model.ResultBase;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DeviceStatus")
public class PosControlOpenCloseStatus extends ResultBase {

    @XmlElement(name = "OpenCloseStatus")
	private short openCloseStat;

    public short getOpenCloseStat() {
        return openCloseStat;
    }

    public void setOpenCloseStat(short openCloseStat) {
        this.openCloseStat = openCloseStat;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("OpenCloseStatus: ").append(openCloseStat).append("; ");
        return sb.toString();
    }
}
