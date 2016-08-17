package ncr.res.mobilepos.cardinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@ApiModel(value="StatusInfoResponse")
public class StatusInfoResponse extends ResultBase {

    @XmlElement(name = "StatusInfo")
    StatusInfo statusInfo;

    /**
     * @return the statusInfo
     */
    @ApiModelProperty(value="‰ïˆõó‘Ôî•ñ", notes="‰ïˆõó‘Ôî•ñ")
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
