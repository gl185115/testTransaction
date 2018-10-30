package ncr.res.mobilepos.taxrate.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;
import ncr.res.mobilepos.pricing.model.ChangeableTaxRate;
import ncr.res.mobilepos.pricing.model.DefaultTaxRate;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TaxRateInfo")
@ApiModel(value="TaxRateInfo")
public class DptTaxRate extends ResultBase{

    @XmlElement(name = "TaxId")
    private Integer taxId;

    @XmlElement(name = "DepartmentId")
    private String departmentId;

    @XmlElement(name = "ChangeableTaxRate")
    private ChangeableTaxRate changeableTaxRate;

    @XmlElement(name = "DefaultTaxRate")
    private DefaultTaxRate defaultTaxRate;

    @ApiModelProperty(value="�ŗ��敪", notes="�ŗ��敪")
    public Integer getTaxId() {
        return taxId;
    }

    public void setTaxId(Integer taxId) {
        this.taxId = taxId;
    }

    @ApiModelProperty(value="����R�[�h", notes="����R�[�h")
    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    @ApiModelProperty(value="�ύX�\�Ȑŗ�", notes="�ύX�\�Ȑŗ�")
    public final ChangeableTaxRate getChangeableTaxRate() {
        return this.changeableTaxRate;
    }

    public final void setChangeableTaxRate(final ChangeableTaxRate changeableTaxRate) {
        this.changeableTaxRate = changeableTaxRate;
    }

    @ApiModelProperty(value="�f�t�H���g�Ŏg���ŗ�", notes="�f�t�H���g�Ŏg���ŗ�")
    public final DefaultTaxRate getDefaultTaxRate() {
        return this.defaultTaxRate;
    }

    public final void setDefaultTaxRate(final DefaultTaxRate defaultTaxRate) {
        this.defaultTaxRate = defaultTaxRate;
    }

    @Override
    public final String toString() {
      StringBuilder str = new StringBuilder();
      String clrf = "; ";
      str.append("TaxId: ").append(taxId).append(clrf)
         .append("DepartmentId: ").append(departmentId).append(clrf)
         .append("ChangeableTaxRate: ").append(changeableTaxRate)
         .append(clrf)
         .append("DefaultTaxRate: ").append(defaultTaxRate);
      return str.toString();
    }
}