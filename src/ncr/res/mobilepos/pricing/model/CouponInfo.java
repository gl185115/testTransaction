package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CouponInfo")
@ApiModel(value="CouponInfo")
public class CouponInfo {
    
    @XmlElement(name = "CouponNo")
    private String couponNo;
    
    @XmlElement(name = "EvenetName")
    private String evenetName;
    
    @XmlElement(name = "ReceiptName")
    private String receiptName;
    
    @XmlElement(name = "IssueType")
    private String IssueType;
    
    @XmlElement(name = "IssueCount")
    private int issueCount;
    
    @XmlElement(name = "UnitPrice")
    private Double UnitPrice;

    @ApiModelProperty(value="������NO", notes="������NO")
    public String getCouponNo() {
        return couponNo;
    }

    public void setCouponNo(String couponNo) {
        this.couponNo = couponNo;
    }

    @ApiModelProperty(value="�C�x���g����", notes="�C�x���g����")
    public String getEvenetName() {
        return evenetName;
    }

    public void setEvenetName(String evenetName) {
        this.evenetName = evenetName;
    }

    @ApiModelProperty(value="���V�[�g����", notes="���V�[�g����")
    public String getReceiptName() {
        return receiptName;
    }

    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }

    @ApiModelProperty(value="���s���@", notes="���s���@")
    public String getIssueType() {
        return IssueType;
    }

    public void setIssueType(String issueType) {
        IssueType = issueType;
    }

    @ApiModelProperty(value="���s����", notes="���s����")
    public int getIssueCount() {
        return issueCount;
    }

    public void setIssueCount(int issueCount) {
        this.issueCount = issueCount;
    }

    @ApiModelProperty(value="�P�ʋ��z(�ō�)", notes="�P�ʋ��z(�ō�)")
    public Double getUnitPrice() {
        return UnitPrice;
    }

    public void setUnitPrice(Double unitPrice) {
        UnitPrice = unitPrice;
    }

}
