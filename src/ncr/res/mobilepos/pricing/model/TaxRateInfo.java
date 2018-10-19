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

    @ApiModelProperty(value="�ŗ��敪", notes="�ŗ��敪")
    public String getTaxId() {
        return taxId;
    }

    public void setTaxId(String taxId) {
        this.taxId = taxId;
    }

    @ApiModelProperty(value="�J�n��", notes="�J�n��")
    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    @ApiModelProperty(value="�I����", notes="�I����")
    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    @ApiModelProperty(value="����ŗ�", notes="����ŗ�")
    public int getTaxRate() {
        return taxRate;
    }

    public void setTaxRate(int taxRate) {
        this.taxRate = taxRate;
    }

    @ApiModelProperty(value="���V�[�g�Ɉ󎚂���}�[�N", notes="���V�[�g�Ɉ󎚂���}�[�N")
    public String getSubCode1() {
        return subCode1;
    }

    public void setSubCode1(String subCode1) {
        this.subCode1 = subCode1;
    }

    @ApiModelProperty(value="�y���ŗ��t���O", notes="�y���ŗ��t���O")
    public int getSubNum1() {
        return subNum1;
    }

    public void setSubNum1(int subNum1) {
        this.subNum1 = subNum1;
    }

    @ApiModelProperty(value="�f�t�H���g�ŗ��t���O", notes="�f�t�H���g�ŗ��t���O")
    public int getSubNum2() {
        return subNum2;
    }

    public void setSubNum2(int subNum2) {
        this.subNum2 = subNum2;
    }

}
