package ncr.res.mobilepos.store.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PresetSroreInfo")
public class PresetSroreInfo extends StoreInfo {

    @XmlElement(name = "TerminalId")
    private String terminalId = "";

    @XmlElement(name = "ReceiptStoreName")
    private String receiptStoreName = "";

    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    public String getReceiptStoreName() {
        return receiptStoreName;
    }

    public void setReceiptStoreName(String receiptStoreName) {
        this.receiptStoreName = receiptStoreName;
    }

    public String getReceiptTelNo() {
        return receiptTelNo;
    }

    public void setReceiptTelNo(String receiptTelNo) {
        this.receiptTelNo = receiptTelNo;
    }

    public String getFormalReceiptStoreName() {
        return formalReceiptStoreName;
    }

    public void setFormalReceiptStoreName(String formalReceiptStoreName) {
        this.formalReceiptStoreName = formalReceiptStoreName;
    }

    public String getFormalReceiptTelNo() {
        return formalReceiptTelNo;
    }

    public void setFormalReceiptTelNo(String formalReceiptTelNo) {
        this.formalReceiptTelNo = formalReceiptTelNo;
    }

    @XmlElement(name = "ReceiptTelNo")
    private String receiptTelNo = "";

    @XmlElement(name = "FormalReceiptStoreName")
    private String formalReceiptStoreName = "";

    @XmlElement(name = "FormalReceiptTelNo")
    private String formalReceiptTelNo = "";
}
