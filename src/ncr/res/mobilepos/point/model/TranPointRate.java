package ncr.res.mobilepos.point.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TranPointRate")
public class TranPointRate {
	@XmlElement(name = "CompanyId")
	private String companyId;
	@XmlElement(name = "StoreId")
	private String storeId;
	@XmlElement(name = "PointFlag")
	private String pointflag;
	@XmlElement(name = "BasePrice")
	private String baseprice;
	@XmlElement(name = "RecordId")
	private String recordid;
	@XmlElement(name = "BasePointCash")
	private String basepointcash;
	@XmlElement(name = "BasePointAffiliate")
	private String basepointaffiliate;
	@XmlElement(name = "BasePointNonAffiliate")
	private String basepointnonaffiliate;
	@XmlElement(name = "PointCalcType")
	private String pointcalctype;
	@XmlElement(name = "TaxCalcType")
	private String taxcalctype;
	@XmlElement(name = "RoundType")
	private String roundtype;
	@XmlElement(name = "CardSettingFlag")
	private String cardsettingflag;
	@XmlElement(name = "DptSettingFlag")
	private String dptsettingflag;
	@XmlElement(name = "ItemSettingFlag")
	private String itemsettingflag;
	@XmlElement(name = "TargetStoreType")
	private String targetstoretype;
	@XmlElement(name = "Type")
	private String type;
	
	
	public final void setCompanyId(String companyId) {
		this.companyId = companyId;
	}
	
	public final String getCompanyId() {
		return companyId;
	}
	
	public final void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	
	public final String getStoreId() {
		return storeId;
	}
	
	public final void setPointFlag(String pointflag) {
		this.pointflag = pointflag;
	}
	
	public final String getPointFlag() {
		return pointflag;
	}
	
	public final void setBasePrice(String baseprice) {
		this.baseprice = baseprice;
	}
	
	public final String getBasePrice() {
		return baseprice;
	}
	
	public final void setRecordId(String recordid) {
		this.recordid = recordid;
	}
	
	public final String getRecordId() {
		return recordid;
	}
	
	public final void setBasePointCash(String basepointcash) {
		this.basepointcash = basepointcash;
	}
	
	public final String getBasePointCash() {
		return basepointcash;
	}

		public final void setBasePointAffiliate(String basepointaffiliate) {
		this.basepointaffiliate = basepointaffiliate;
	}
	
	public final String getBasePointAffiliate() {
		return basepointaffiliate;
	}

	public final void setBasePointNonAffiliate(String basepointnonaffiliate) {
		this.basepointnonaffiliate = basepointnonaffiliate;
	}
	
	public final String getBasePointNonAffiliate() {
		return basepointnonaffiliate;
	}

	public final void setPointCalcType(String pointcalctype) {
		this.pointcalctype = pointcalctype;
	}
	
	public final String getPointCalcType() {
		return pointcalctype;
	}

	public final void setTaxCalcType(String taxcalctype) {
		this.taxcalctype = taxcalctype;
	}
	
	public final String getTaxCalcType() {
		return taxcalctype;
	}

	public final void setRoundType(String roundtype) {
		this.roundtype = roundtype;
	}
	
	public final String getRoundType() {
		return roundtype;
	}

	public final void setCardSettingFlag(String cardsettingflag) {
		this.cardsettingflag = cardsettingflag;
	}
	
	public final String getCardSettingFlag() {
		return cardsettingflag;
	}
	
	public final void setDptSettingFlag(String dptsettingflag) {
		this.dptsettingflag = dptsettingflag;
	}
	
	public final String getDptSettingFlag() {
		return dptsettingflag;
	}

	public final void setItemSettingFlag(String itemsettingflag) {
		this.itemsettingflag = itemsettingflag;
	}
	
	public final String getItemSettingFlag() {
		return itemsettingflag;
	}

	public final void setTargetStoreType(String targetstoretype) {
		this.targetstoretype = targetstoretype;
	}
	
	public final String getTargetStoreType() {
		return targetstoretype;
	}

	public final void setType(String type) {
		this.type = type;
	}
	
	public final String getType() {
		return type;
	}

}
