package ncr.res.mobilepos.promotion.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * ItemList Model Object.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ItemList")
@ApiModel(value="ItemList")
public class ItemList {

    @XmlElement(name = "itemcode")
    private String itemcode;

    @XmlElement(name = "Dpt")
    private String dpt;

    @XmlElement(name = "Line")
    private String line;

    @XmlElement(name = "Class")
    private String classCode;

    @XmlElement(name = "ConnCode")
    private String connCode;

    @XmlElement(name = "BrandId")
    private String brandId;

    @XmlElement(name = "Qty")
    private int quantity;

    @XmlElement(name = "Amount")
    private int amount;

    @XmlElement(name = "MdName")
    private String mdName;

    @ApiModelProperty(value="���i�R�[�h", notes="���i�R�[�h")
    public final String getItemcode() {
		return itemcode;
	}

	public final void setItemcode(String itemcode) {
		this.itemcode = itemcode;
	}

    @ApiModelProperty(value="����R�[�h", notes="����R�[�h")
    public final String getDpt() {
        return dpt;
    }

    public final void setDpt(String dpt) {
        this.dpt = dpt;
    }

    @ApiModelProperty(value="�i��R�[�h", notes="�i��R�[�h")
    public final String getLine() {
        return line;
    }

    public final void setLine(String line) {
        this.line = line;
    }

    @ApiModelProperty(value="�N���X�R�[�h", notes="�N���X�R�[�h")
    public final String getClassCode() {
        return classCode;
    }

    public final void setClassCode(String classCode) {
        this.classCode = classCode;
    }

    @ApiModelProperty(value="�d�����R�[�h", notes="�d�����R�[�h")
    public final String getConnCode() {
        return connCode;
    }

    public final void setConnCode(String connCode) {
        this.connCode = connCode;
    }

    @ApiModelProperty(value="�u�����h�R�[�h", notes="�u�����h�R�[�h")
    public final String getBrandId() {
        return brandId;
    }

    public final void setBrandId(String brandId) {
        this.brandId = brandId;
    }

    @ApiModelProperty(value="����", notes="����")
    public final int getQty() {
        return quantity;
    }

    public final void setQty(final int quantity) {
        this.quantity = quantity;
    }

    @ApiModelProperty(value="���z", notes="���z")
    public final int getAmount() {
        return this.amount;
    }

    public final void setAmount(final int amount) {
        this.amount = amount;
    }

    @ApiModelProperty(value="���i��", notes="���i��")
    public final String getMdName() {
        return mdName;
    }

    public final void setMdName(String mdName) {
        this.mdName = mdName;
    }
}
