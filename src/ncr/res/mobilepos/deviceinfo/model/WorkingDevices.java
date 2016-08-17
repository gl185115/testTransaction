package ncr.res.mobilepos.deviceinfo.model;

import ncr.res.mobilepos.model.ResultBase;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.List;

/**
 * WorkingDevices, this is used to store TerminalInfo for getWorkingDevices return.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "workingdevices")
public class WorkingDevices extends ResultBase{

    @XmlElement(name = "terminals")
    private List<TerminalStatus> activeTerminals;

    @XmlElement(name = "group")
    private TerminalTillGroup ownTillGroup;

    @XmlElement(name = "groups")
    private List<TerminalTillGroup> tillGroups;

    public List<TerminalStatus> getActiveTerminals() {
        return activeTerminals;
    }

    public void setActiveTerminals(List<TerminalStatus> activeTerminals) {
        this.activeTerminals = activeTerminals;
    }

    public TerminalTillGroup getOwnTillGroup() {
        return ownTillGroup;
    }

    public void setOwnTillGroup(TerminalTillGroup ownTillGroup) {
        this.ownTillGroup = ownTillGroup;
    }

    public List<TerminalTillGroup> getTillGroups() {
        return tillGroups;
    }

    public void setTillGroups(List<TerminalTillGroup> tillGroups) {
        this.tillGroups = tillGroups;
    }

}
