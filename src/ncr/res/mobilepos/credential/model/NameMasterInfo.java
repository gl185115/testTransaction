package ncr.res.mobilepos.credential.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "NameMasterInfo")
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

	public final String getCompanyId() {
		return companyId;
	}

	public final void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public final String getStoreId() {
		return storeId;
	}

	public final void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	public final String getNameCategory() {
		return nameCategory;
	}

	public final void setNameCategory(String nameCategory) {
		this.nameCategory = nameCategory;
	}

	public final String getNameId() {
		return nameId;
	}

	public final void setNameId(String nameId) {
		this.nameId = nameId;
	}

	public final String getNameText() {
		return nameText;
	}

	public final void setNameText(String nameText) {
		this.nameText = nameText;
	}

	public final String getNameIdName() {
		return nameIdName;
	}

	public final void setNameIdName(String nameIdName) {
		this.nameIdName = nameIdName;
	}
}
