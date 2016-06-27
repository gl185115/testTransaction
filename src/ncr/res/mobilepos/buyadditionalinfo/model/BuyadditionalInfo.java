package ncr.res.mobilepos.buyadditionalinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "BuyadditionalInfo")
@ApiModel(value="BuyadditionalInfo")
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

	@ApiModelProperty(value="購買補助コード", notes="購買補助コード")
	public final String getId() {
		return id;
	}

	public final void setId(String id) {
		this.id = id;
	}

	@ApiModelProperty(value="購買補助名称", notes="購買補助名称")
	public final String getName() {
		return name;
	}

	public final void setName(String name) {
		this.name = name;
	}

	@ApiModelProperty(value="購買補助名称(カナ)", notes="購買補助名称(カナ)")
	public final String getKanaName() {
		return kanaName;
	}

	public final void setKanaName(String kanaName) {
		this.kanaName = kanaName;
	}

	@ApiModelProperty(value="購買補助略称", notes="購買補助略称")
	public final String getShortName() {
		return shortName;
	}

	public final void setShortName(String shortName) {
		this.shortName = shortName;
	}

	@ApiModelProperty(value="購買補助略称(カナ)", notes="購買補助略称(カナ)")
	public final String getShortKanaName() {
		return shortKanaName;
	}

	public final void setShortKanaName(String shortKanaName) {
		this.shortKanaName = shortKanaName;
	}

}
