package ncr.res.mobilepos.customerclass.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CustomerClassInfo")
public class CustomerClassInfo {
	@XmlElement(name = "CompanyId")
	private String companyId;
	@XmlElement(name = "StoreId")
	private String storeId;
	@XmlElement(name = "CustomerClassId")
	private String id;
	@XmlElement(name = "CustomerClassName")
	private String name;
	@XmlElement(name = "CustomerClassKanaName")
	private String kanaName;
	@XmlElement(name = "GenerationType")
	private String generationType;
	@XmlElement(name = "SexType")
	private String sexType;
	@XmlElement(name = "OtherType")
	private String otherType;

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

	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	public final String getKanaName() {
		return kanaName;
	}

	public final void setKanaName(String kanaName) {
		this.kanaName = kanaName;
	}

	public final String getGenerationType() {
		return generationType;
	}

	public final void setGenerationType(String generationType) {
		this.generationType = generationType;
	}

	public final String getSexType() {
		return sexType;
	}

	public final void setSexType(String sexType) {
		this.sexType = sexType;
	}

	public final String getOtherType() {
		return otherType;
	}

	public final void setOtherType(String otherType) {
		this.otherType = otherType;
	}

}
