package ncr.res.mobilepos.store.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PresetSroreInfo")
@ApiModel(value="PresetSroreInfo")
public class PresetSroreInfo extends StoreInfo {

    @XmlElement(name = "TerminalId")
    private String terminalId = "";

    @XmlElement(name = "ReceiptStoreName")
    private String receiptStoreName = "";

    @ApiModelProperty(value="端末番号", notes="端末番号")
    public String getTerminalId() {
        return terminalId;
    }

    public void setTerminalId(String terminalId) {
        this.terminalId = terminalId;
    }

    @ApiModelProperty(value="レシート店舗名", notes="レシート店舗名")
    public String getReceiptStoreName() {
        return receiptStoreName;
    }

    public void setReceiptStoreName(String receiptStoreName) {
        this.receiptStoreName = receiptStoreName;
    }

    @ApiModelProperty(value="レシート電話番号", notes="レシート電話番号")
    public String getReceiptTelNo() {
        return receiptTelNo;
    }

    public void setReceiptTelNo(String receiptTelNo) {
        this.receiptTelNo = receiptTelNo;
    }

    @ApiModelProperty(value="領収書店舗名", notes="領収書店舗名")
    public String getFormalReceiptStoreName() {
        return formalReceiptStoreName;
    }

    public void setFormalReceiptStoreName(String formalReceiptStoreName) {
        this.formalReceiptStoreName = formalReceiptStoreName;
    }

    @ApiModelProperty(value="領収書電話番号", notes="領収書電話番号")
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
