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
    private List<TerminalStatus> terminals;

    public WorkingDevices(int returnCode, List<TerminalStatus> terminals) {
        setNCRWSSResultCode(returnCode);
        this.terminals = terminals;
    }
}
