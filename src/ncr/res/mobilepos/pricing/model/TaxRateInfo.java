package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TaxRateInfo")
@ApiModel(value="TaxRateInfo")
public class TaxRateInfo {

    @XmlElement(name = "taxId")
    private String taxId;

    @XmlElement(name = "startDate")
    private String startDate;

    @XmlElement(name = "endDate")
    private String endDate;

    @XmlElement(name = "taxRate")
    private int taxRate;

    @XmlElement(name = "subCode1")
    private String subCode1;

    @XmlElement(name = "subNum1")
    private int subNum1;

    @XmlElement(name = "subNum2")
    private int subNum2;

    @ApiModelProperty(value="税率区分", notes="税率区分")
    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    @ApiModelProperty(value="開始日", notes="開始日")
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @ApiModelProperty(value="終了日", notes="終了日")
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @ApiModelProperty(value="消費税率", notes="消費税率")
    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    @ApiModelProperty(value="レシートに印字するマーク", notes="レシートに印字するマーク")
    public String getSubCode1() {
        return subCode1;
    }

    public void setSubCode1(String subCode1) {
        this.subCode1 = subCode1;
    }

    @ApiModelProperty(value="軽減税率フラグ", notes="軽減税率フラグ")
    public int getSubNum1() {
        return subNum1;
    }

    public void setSubNum1(int subNum1) {
        this.subNum1 = subNum1;
    }

    @ApiModelProperty(value="デフォルト税率フラグ", notes="デフォルト税率フラグ")
    public int getSubNum2() {
        return subNum2;
    }

    public void setSubNum2(int subNum2) {
        this.subNum2 = subNum2;
    }

}
