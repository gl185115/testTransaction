package ncr.res.mobilepos.buyadditionalinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "BuyadditionalInfo")
public class BuyadditionalInfo {
	@XmlElement(name = "CompanyId")
	private String companyId;
	@XmlElement(name = "StoreId")
	private String storeId;
	@XmlElement(name = "BuyAdditionalInfoId")
	private String id;
	@XmlElement(name = "BuyAdditionalInfoName")
	private String name;
	@XmlElement(name = "BuyAdditionalInfoKanaName")
	private String kanaName;
	@XmlElement(name = "BuyAdditionalInfoShortName")
	private String shortName;
	@XmlElement(name = "BuyAdditionalInfoShortKanaName")
	private String shortKanaName;

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

	public final String getShortName() {
		return shortName;
	}

	public final void setShortName(String shortName) {
		this.shortName = shortName;
	}

	public final String getShortKanaName() {
		return shortKanaName;
	}

	public final void setShortKanaName(String shortKanaName) {
		this.shortKanaName = shortKanaName;
	}

}
