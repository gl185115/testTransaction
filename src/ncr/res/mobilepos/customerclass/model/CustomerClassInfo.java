package ncr.res.mobilepos.customerclass.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModelProperty;

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

	@ApiModelProperty(value="客層コード", notes="客層コード")
	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	@ApiModelProperty(value="客層名称(漢字)", notes="客層名称(漢字)")
	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(value="客層名称(カナ)", notes="客層名称(カナ)")
	public final String getKanaName() {
		return kanaName;
	}

	public final void setKanaName(String kanaName) {
		this.kanaName = kanaName;
	}

	@ApiModelProperty(value="世代区分", notes="世代区分")
	public final String getGenerationType() {
		return generationType;
	}

	public final void setGenerationType(String generationType) {
		this.generationType = generationType;
	}

	@ApiModelProperty(value="性別区分", notes="性別区分")
	public final String getSexType() {
		return sexType;
	}

	public final void setSexType(String sexType) {
		this.sexType = sexType;
	}

	@ApiModelProperty(value="その他区分", notes="その他区分")
	public final String getOtherType() {
		return otherType;
	}

	public final void setOtherType(String otherType) {
		this.otherType = otherType;
	}

}
