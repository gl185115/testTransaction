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

	@ApiModelProperty(value="��ЃR�[�h", notes="��ЃR�[�h")
	public final String getCompanyId() {
		return companyId;
	}

	public final void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	@ApiModelProperty(value="�X�܃R�[�h", notes="�X�܃R�[�h")
	public final String getStoreId() {
		return storeId;
	}

	public final void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	@ApiModelProperty(value="���̋敪", notes="���̋敪")
	public final String getNameCategory() {
		return nameCategory;
	}

	public final void setNameCategory(String nameCategory) {
		this.nameCategory = nameCategory;
	}

	@ApiModelProperty(value="���̃R�[�h", notes="���̃R�[�h")
	public final String getNameId() {
		return nameId;
	}

	public final void setNameId(String nameId) {
		this.nameId = nameId;
	}

	@ApiModelProperty(value="����", notes="����")
	public final String getNameText() {
		return nameText;
	}

	public final void setNameText(String nameText) {
		this.nameText = nameText;
	}

	@ApiModelProperty(value="���̃R�[�h���^���l", notes="���̃R�[�h���^���l")
	public final String getNameIdName() {
		return nameIdName;
	}

	public final void setNameIdName(String nameIdName) {
		this.nameIdName = nameIdName;
	}
}
