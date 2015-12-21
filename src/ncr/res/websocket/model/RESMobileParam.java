package ncr.res.websocket.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import javax.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "RESMobileParam")
public class RESMobileParam {
    @XmlElement(name = "CheckFlag")
    private String checkFlag;

    @XmlElement(name = "NewPosGroup")
    private RESMobileParamNewPOS newPosGroup;
    
    @XmlElement(name = "OldPosGroup")
    private RESMobileParamOldPOS oldPosGroup;

    public String getCheckFlag() {
        return checkFlag;
    }

    public void setCheckFlag(String checkFlag) {
        this.checkFlag = checkFlag;
    }

    public RESMobileParamNewPOS getNewPosGroup() {
        return newPosGroup;
    }

    public void setNewPosGroup(RESMobileParamNewPOS newPosGroup) {
        this.newPosGroup = newPosGroup;
    }

    public RESMobileParamOldPOS getOldPosGroup() {
        return oldPosGroup;
    }

    public void setOldPosGroup(RESMobileParamOldPOS oldPosGroup) {
        this.oldPosGroup = oldPosGroup;
    }
}
