package ncr.res.mobilepos.uiconfig.model.deploystatus;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DeployEffective")
public class DeployEffective {

    @XmlElement(name = "PickList")
    private EPickList pickList;

    @XmlElement(name = "Options")
    private EOptions options;

    @XmlElement(name = "Usability")
    private EUsability usability;

    @XmlElement(name = "Notices")
    private ENotices notices;

    public EPickList getPickList() {
        return pickList;
    }

    public void setPickList(EPickList pickList) {
        this.pickList = pickList;
    }

    public EOptions getOptions() {
        return options;
    }

    public void setOptions(EOptions options) {
        this.options = options;
    }

    public EUsability getUsability() {
        return usability;
    }

    public void setUsability(EUsability usability) {
        this.usability = usability;
    }

    public ENotices getNotices() {
        return notices;
    }

    public void setNotices(ENotices notices) {
        this.notices = notices;
    }
}
