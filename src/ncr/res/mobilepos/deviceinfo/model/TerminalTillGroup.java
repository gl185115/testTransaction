package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.ArrayList;
import java.util.List;

/**
 * TillGroup
 */
@XmlRootElement(name = "tillgroup")
@XmlAccessorType(XmlAccessType.NONE)
public class TerminalTillGroup {
    @XmlElement(name = "tillid")
    private String tillId;

    @XmlElement(name = "terminals")
    private List<TerminalStatus> terminals;

    public TerminalTillGroup(String tillId) {
        this.tillId = tillId;
        this.terminals = new ArrayList<>();
    }

    public List<TerminalStatus> getTerminals() {
        return terminals;
    }

    public void setTerminals(List<TerminalStatus> terminals) {
        this.terminals = terminals;
    }

    public boolean add(TerminalStatus terminal) {
        return terminals.add(terminal);
    }
}
