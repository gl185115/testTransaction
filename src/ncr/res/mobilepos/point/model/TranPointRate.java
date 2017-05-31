package ncr.res.mobilepos.point.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TranPointRate")
@ApiModel(value="TranPointRate")
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
	@XmlElement(name = "BasePoint")
	private String basepoint;
	@XmlElement(name = "CashingUnit")
	private String cashingUnit;
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
	
    @ApiModelProperty(value="会社コード", notes="会社コード")
	public final String getCompanyId() {
		return companyId;
	}
	
	public final void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	
    @ApiModelProperty(value="店舗コード", notes="店舗コード")
	public final String getStoreId() {
		return storeId;
	}
	
	public final void setPointFlag(String pointflag) {
		this.pointflag = pointflag;
	}
	
    @ApiModelProperty(value="ポイント運用フラグ", notes="ポイント運用フラグ")
	public final String getPointFlag() {
		return pointflag;
	}
	
	public final void setBasePrice(String baseprice) {
		this.baseprice = baseprice;
	}
	
    @ApiModelProperty(value="基準額", notes="基準額")
	public final String getBasePrice() {
		return baseprice;
	}
	
	public final void setBasePoint(String basepoint) {
		this.basepoint = basepoint;
	}
	
    @ApiModelProperty(value="基準ポイント", notes="基準ポイント")
	public final String getBasePoint() {
		return basepoint;
	}

	public final void setRecordId(String recordid) {
		this.recordid = recordid;
	}
	
    @ApiModelProperty(value="管理NO", notes="管理NO")
	public final String getRecordId() {
		return recordid;
	}
	
	public final void setCashingUnit(String cashingUnit) {
		this.cashingUnit = cashingUnit;
	}
	
	@ApiModelProperty(value="KPC計算単位", notes="KPC計算単位")
	public final String getCashingUnit() {
		return cashingUnit;
	}
	
	public final void setBasePointCash(String basepointcash) {
		this.basepointcash = basepointcash;
	}
	
    @ApiModelProperty(value="基準ポイント(現金)", notes="基準ポイント(現金)")
	public final String getBasePointCash() {
		return basepointcash;
	}

	public final void setBasePointAffiliate(String basepointaffiliate) {
		this.basepointaffiliate = basepointaffiliate;
	}
	
	@ApiModelProperty(value="基準ポイント(提携)", notes="基準ポイント(提携)")
	public final String getBasePointAffiliate() {
		return basepointaffiliate;
	}

	public final void setBasePointNonAffiliate(String basepointnonaffiliate) {
		this.basepointnonaffiliate = basepointnonaffiliate;
	}
	
	@ApiModelProperty(value="基準ポイント(提携外)", notes="基準ポイント(提携外)")
	public final String getBasePointNonAffiliate() {
		return basepointnonaffiliate;
	}

	public final void setPointCalcType(String pointcalctype) {
		this.pointcalctype = pointcalctype;
	}
	
	@ApiModelProperty(value="計算単位区分", notes="計算単位区分")
	public final String getPointCalcType() {
		return pointcalctype;
	}

	public final void setTaxCalcType(String taxcalctype) {
		this.taxcalctype = taxcalctype;
	}
	
	@ApiModelProperty(value="税計算区分", notes="税計算区分")
	public final String getTaxCalcType() {
		return taxcalctype;
	}

	public final void setRoundType(String roundtype) {
		this.roundtype = roundtype;
	}
	
	@ApiModelProperty(value="丸め方法区分", notes="丸め方法区分")
	public final String getRoundType() {
		return roundtype;
	}

	public final void setCardSettingFlag(String cardsettingflag) {
		this.cardsettingflag = cardsettingflag;
	}
	
	@ApiModelProperty(value="カード別設定フラグ", notes="カード別設定フラグ")
	public final String getCardSettingFlag() {
		return cardsettingflag;
	}
	
	public final void setDptSettingFlag(String dptsettingflag) {
		this.dptsettingflag = dptsettingflag;
	}
	
	@ApiModelProperty(value="部門別設定フラグ", notes="部門別設定フラグ")
	public final String getDptSettingFlag() {
		return dptsettingflag;
	}

	public final void setItemSettingFlag(String itemsettingflag) {
		this.itemsettingflag = itemsettingflag;
	}
	
	@ApiModelProperty(value="プロジェクト別設定フラグ", notes="プロジェクト別設定フラグ")
	public final String getItemSettingFlag() {
		return itemsettingflag;
	}

	public final void setTargetStoreType(String targetstoretype) {
		this.targetstoretype = targetstoretype;
	}
	
	@ApiModelProperty(value="設定店舗区分", notes="設定店舗区分")
	public final String getTargetStoreType() {
		return targetstoretype;
	}

	public final void setType(String type) {
		this.type = type;
	}
	
    @ApiModelProperty(value="タイプ", notes="タイプ")
	public final String getType() {
		return type;
	}

}
