package ncr.res.websocket.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "OldPosGroup")
public class RESMobileParamOldPOS {
    @XmlElement(name = "deviceid")
    private List<String> deviceidList;

    public List<String> getDeviceidList() {
        return deviceidList;
    }

    public void setDeviceidList(List<String> deviceidList) {
        this.deviceidList = deviceidList;
    }
}
