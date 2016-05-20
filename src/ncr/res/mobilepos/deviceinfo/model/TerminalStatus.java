package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;
import java.sql.Timestamp;

/**
 * TerminalStatus, this stores open close status for the terminal.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "terminal")
public class TerminalStatus {

    /**
     * CompanyId
     */
    @XmlTransient
    private String companyId;
    /**
     * StoreId
     */
    @XmlTransient
    private String storeId;
    /**
     * TerminalId
     */
    @XmlElement(name = "terminalid")
    private String terminalId;
    /**
     * TillId
     */
    @XmlTransient
    private String tillId;
    /**
     * TerminalName
     */
    @XmlElement(name = "terminalName", nillable = true)
    private String terminalName;
    /**
     * OpenCloseStat
     */
    @XmlTransient
    private short openCloseStat;
    /**
     * SodTime
     */
    @XmlTransient
    private Timestamp sodTime;
    /**
     * EodTime
     */
    @XmlTransient
    private Timestamp eodTime;


    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getStoreId() {
        return storeId;
    }

    public void setStoreId(String storeId) {
        this.storeId = storeId;
    }

    public String getTerminalName() {
        return terminalName;
    }

    public void setTerminalName(String terminalName) {
        this.terminalName = terminalName;
    }

    public short getOpenCloseStat() {
        return openCloseStat;
    }

    public void setOpenCloseStat(short openCloseStat) {
        this.openCloseStat = openCloseStat;
    }

    public Timestamp getSodTime() {
        return sodTime;
    }

    public void setSodTime(Timestamp sodTime) {
        this.sodTime = sodTime;
    }

    public Timestamp getEodTime() {
        return eodTime;
    }

    public void setEodTime(Timestamp eodTime) {
        this.eodTime = eodTime;
    }

    public String getTillId() {
        return tillId;
    }

    public void setTillId(String tillId) {
        this.tillId = tillId;
    }

}
