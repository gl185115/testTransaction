package ncr.res.mobilepos.point.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ItemPointRate")
public class ItemPointRate {
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
	@XmlElement(name = "BasePoint")
	private String basepoint;
	@XmlElement(name = "DptSettingFlag")
	private String dptsettingflag;		
	@XmlElement(name = "ItemSettingFlag")
	private String itemsettingflag;	
	@XmlElement(name = "CardSettingFlag")
	private String cardsettingflag;
	@XmlElement(name = "StoreSettingFlag")
	private String storesettingflag;
	@XmlElement(name = "CardClassId")
	private String cardclassid;
	@XmlElement(name = "Type")
	private String type;
	@XmlElement(name = "BasePointCash")
	private String basepointcash;
	@XmlElement(name = "BasePointAffiliate")
	private String basepointaffiliate;
	@XmlElement(name = "BasePointNonAffiliate")
	private String basepointnonaffiliate;
	
	
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
	
	public final void setBasePoint(String basepoint) {
		this.basepoint = basepoint;
	}
	
	public final String getBasePoint() {
		return basepoint;
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

	public final void setCardSettingFlag(String cardsettingflag) {
		this.cardsettingflag = cardsettingflag;
	}
	
	public final String getCardSettingFlag() {
		return cardsettingflag;
	}
	
	public final void setStoreSettingFlag(String storesettingflag) {
		this.storesettingflag = storesettingflag;
	}
	
	public final String getStoreSettingFlag() {
		return storesettingflag;
	}
	
	public final void setCardClassId(String cardclassid) {
		this.cardclassid = cardclassid;
	}
	
	public final String getCardClassId() {
		return cardclassid;
	}

	public final void setType(String type) {
		this.type = type;
	}
	
	public final String getType() {
		return type;
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
}
