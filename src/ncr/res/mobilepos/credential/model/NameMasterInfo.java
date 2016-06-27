package ncr.res.mobilepos.credential.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "NameMasterInfo")
@ApiModel(value="NameMasterInfo")
public class NameMasterInfo {
	@XmlElement(name = "CompanyId")
	private String companyId;

	@XmlElement(name = "StoreId")
	private String storeId;

	@XmlElement(name = "NameCategory")
	private String nameCategory;

	@XmlElement(name = "NameId")
	private String nameId;

	@XmlElement(name = "NameText")
	private String nameText;

	@XmlElement(name = "NameIdName")
	private String nameIdName;

	@ApiModelProperty(value="会社コード", notes="会社コード")
	public final String getCompanyId() {
		return companyId;
	}

	public final void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@ApiModelProperty(value="店舗コード", notes="店舗コード")
	public final String getStoreId() {
		return storeId;
	}

	public final void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@ApiModelProperty(value="名称区分", notes="名称区分")
	public final String getNameCategory() {
		return nameCategory;
	}

	public final void setNameCategory(String nameCategory) {
		this.nameCategory = nameCategory;
	}

	@ApiModelProperty(value="名称コード", notes="名称コード")
	public final String getNameId() {
		return nameId;
	}

	public final void setNameId(String nameId) {
		this.nameId = nameId;
	}

	@ApiModelProperty(value="名称", notes="名称")
	public final String getNameText() {
		return nameText;
	}

	public final void setNameText(String nameText) {
		this.nameText = nameText;
	}

	@ApiModelProperty(value="名称コード名／備考", notes="名称コード名／備考")
	public final String getNameIdName() {
		return nameIdName;
	}

	public final void setNameIdName(String nameIdName) {
		this.nameIdName = nameIdName;
	}
}
