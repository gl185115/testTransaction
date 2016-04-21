package ncr.res.mobilepos.cardinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
public class StatusInfoResponse extends ResultBase {

    @XmlElement(name = "StatusInfo")
    StatusInfo statusInfo;

    /**
     * @return the statusInfo
     */
    public StatusInfo getStatusInfo() {
        return statusInfo;
    }

    /**
     * @param statusInfo
     *            the statusInfo to set
     */
    public void setStatusInfo(StatusInfo statusInfo) {
        this.statusInfo = statusInfo;
    }

}
