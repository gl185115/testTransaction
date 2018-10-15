package ncr.res.mobilepos.point.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PointInfo")
@ApiModel(value="PointInfo")
public class PointInfo {
	@XmlElement(name = "PointFlag")
	private String pointFlag;
	@XmlElement(name = "PointType")
	private String pointType;
	@XmlElement(name = "BasePrice")
	private String basePrice;
	@XmlElement(name = "CashingUnit")
	private String cashingUnit;
	@XmlElement(name = "RecordId")
	private String recordId;
	@XmlElement(name = "BasePoint")
	private String basePoint;
	@XmlElement(name = "PointTender")
	private String pointTender;
	@XmlElement(name = "PointCalcType")
	private String pointCalcType;
	@XmlElement(name = "TaxCalcType")
	private String taxCalcType;
	@XmlElement(name = "RoundType")
	private String roundType;
	@XmlElement(name = "TenderSettingFlag")
	private String tenderSettingFlag;
	@XmlElement(name = "BasePointCash")
	private String basePointCash;
	@XmlElement(name = "BasePointAffiliate")
	private String basePointAffiliate;
	@XmlElement(name = "BasePointNonAffiliate")
	private String basePointNonAffiliate;
	@XmlElement(name = "CardSettingFlag")
	private String cardSettingFlag;
	@XmlElement(name = "TargetStoreType")
	private String targetStoreType;
	@XmlElement(name = "ItemSettingFlag")
	private String itemSettingFlag;
	@XmlElement(name = "DptSettingFlag")
	private String dptSettingFlag;
	@XmlElement(name = "CardClassId")
	private String cardClassId;
	@XmlElement(name = "TenderId")
	private String tenderId;
	@XmlElement(name = "TenderType")
	private String tenderType;
	@XmlElement(name = "GroupTargetType")
	private String groupTargetType;
	@XmlElement(name = "GroupIdStart")
	private String groupIdStart;
	@XmlElement(name = "GroupIdEnd")
	private String groupIdEnd;
	@XmlElement(name = "DptTargetType")
	private String dptTargetType;
	@XmlElement(name = "DptStart")
	private String dptStart;
	@XmlElement(name = "DptEnd")
	private String dptEnd;
	@XmlElement(name = "BrandTargetType")
	private String brandTargetType;
	@XmlElement(name = "BrandIdStart")
	private String brandIdStart;
	@XmlElement(name = "BrandIdEnd")
	private String brandIdEnd;
	@XmlElement(name = "SkuTargetType")
	private String skuTargetType;
	@XmlElement(name = "SkuStart")
	private String skuStart;
	@XmlElement(name = "SkuEnd")
	private String skuEnd;
	@XmlElement(name = "InfoBasePoint")
	private String infoBasePoint;
	@XmlElement(name = "DetailBasePoint")
	private String detailBasePoint;
	@XmlElement(name = "TargetType")
	private String targetType;
	@XmlElement(name = "TargetId")
	private String targetId;
	
	
	public final void setPointFlag(String pointFlag) {
		this.pointFlag = pointFlag;
	}
	
    @ApiModelProperty(value="ポイント運用フラグ", notes="ポイント運用フラグ")
	public final String getPointFlag() {
		return pointFlag;
	}
	
	public final void setPointType(String pointType) {
		this.pointType = pointType;
	}
	
    @ApiModelProperty(value="ポイント区分", notes="ポイント区分")
	public final String getPointType() {
		return pointType;
	}
	public final void setBasePrice(String basePrice) {
		this.basePrice = basePrice;
	}
	
    @ApiModelProperty(value="基準額", notes="基準額")
	public final String getBasePrice() {
		return basePrice;
	}
	public final void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	
    @ApiModelProperty(value="管理NO", notes="管理NO")
	public final String getRecordId() {
		return recordId;
	}
	public final void setBasePoint(String basePoint) {
		this.basePoint = basePoint;
	}
	
    @ApiModelProperty(value="基準ポイント", notes="基準ポイント")
	public final String getBasePoint() {
		return basePoint;
	}
	public final void setCashingUnit(String cashingUnit) {
		this.cashingUnit = cashingUnit;
	}
	
    @ApiModelProperty(value="還元額(1P)", notes="還元額(1P)")
	public final String getCashingUnit() {
		return cashingUnit;
	}
	public final void setPointTender(String pointTender) {
		this.pointTender = pointTender;
	}
	
    @ApiModelProperty(value="ポイント種別", notes="ポイント種別")
	public final String getPointTender() {
		return pointTender;
	}
	public final void setPointCalcType(String pointCalcType) {
		this.pointCalcType = pointCalcType;
	}
	
    @ApiModelProperty(value="計算単位区分", notes="計算単位区分")
	public final String getPointCalcType() {
		return pointCalcType;
	}
	public final void setTaxCalcType(String taxCalcType) {
		this.taxCalcType = taxCalcType;
	}
	
    @ApiModelProperty(value="税計算区分", notes="税計算区分")
	public final String getTaxCalcType() {
		return taxCalcType;
	}
	public final void setRoundType(String roundType) {
		this.roundType = roundType;
	}
	
    @ApiModelProperty(value="丸め方法区分", notes="丸め方法区分")
	public final String getRoundType() {
		return roundType;
	}
	public final void setTenderSettingFlag(String tenderSettingFlag) {
		this.tenderSettingFlag = tenderSettingFlag;
	}
	
    @ApiModelProperty(value="金種設定フラグ", notes="金種設定フラグ")
	public final String getTenderSettingFlag() {
		return tenderSettingFlag;
	}
	public final void setBasePointCash(String basePointCash) {
		this.basePointCash = basePointCash;
	}
	
    @ApiModelProperty(value="基準ポイント(現金)", notes="基準ポイント(現金)")
	public final String getBasePointCash() {
		return basePointCash;
	}
	public final void setBasePointAffiliate(String basePointAffiliate) {
		this.basePointAffiliate = basePointAffiliate;
	}
	
    @ApiModelProperty(value="基準ポイント(提携)", notes="基準ポイント(提携)")
	public final String getBasePointAffiliate() {
		return basePointAffiliate;
	}
	public final void setBasePointNonAffiliate(String basePointNonAffiliate) {
		this.basePointNonAffiliate = basePointNonAffiliate;
	}
	
    @ApiModelProperty(value="基準ポイント(提携外)", notes="基準ポイント(提携外)")
	public final String getBasePointNonAffiliate() {
		return basePointNonAffiliate;
	}
	public final void setCardSettingFlag(String cardSettingFlag) {
		this.cardSettingFlag = cardSettingFlag;
	}
	
    @ApiModelProperty(value="カード設定フラグ", notes="カード設定フラグ")
	public final String getCardSettingFlag() {
		return cardSettingFlag;
	}
	public final void setTargetStoreType(String targetStoreType) {
		this.targetStoreType = targetStoreType;
	}
	
    @ApiModelProperty(value="店設定区分", notes="店設定区分")
	public final String getTargetStoreType() {
		return targetStoreType;
	}
	public final void setItemSettingFlag(String itemSettingFlag) {
		this.itemSettingFlag = itemSettingFlag;
	}
	
    @ApiModelProperty(value="商品別設定フラグ", notes="商品別設定フラグ")
	public final String getItemSettingFlag() {
		return itemSettingFlag;
	}
	public final void setDptSettingFlag(String dptSettingFlag) {
		this.dptSettingFlag = dptSettingFlag;
	}
	
    @ApiModelProperty(value="部門別設定フラグ", notes="部門別設定フラグ")
	public final String getDptSettingFlag() {
		return dptSettingFlag;
	}

	public final void setCardClassId(String cardClassId) {
		this.cardClassId = cardClassId;
	}
	
    @ApiModelProperty(value="カード区分", notes="カード区分")
	public final String getCardClassId() {
		return cardClassId;
	}
	public final void setTenderId(String tenderId) {
		this.tenderId = tenderId;
	}
	
    @ApiModelProperty(value="種別コード", notes="種別コード")
	public final String getTenderId() {
		return tenderId;
	}
	public final void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}
	
    @ApiModelProperty(value="支払種別", notes="支払種別")
	public final String getTenderType() {
		return tenderType;
	}
	public final void setGroupTargetType(String groupTargetType) {
		this.groupTargetType = groupTargetType;
	}
	
    @ApiModelProperty(value="グループ複数設定フラグ", notes="グループ複数設定フラグ")
	public final String getGroupTargetType() {
		return groupTargetType;
	}
	public final void setGroupIdStart(String groupIdStart) {
		this.groupIdStart = groupIdStart;
	}
	
    @ApiModelProperty(value="グループFROM", notes="グループFROM")
	public final String getGroupIdStart() {
		return groupIdStart;
	}
	public final void setGroupIdEnd(String groupIdEnd) {
		this.groupIdEnd = groupIdEnd;
	}
	
    @ApiModelProperty(value="グループTO", notes="グループTO")
	public final String getGroupIdEnd() {
		return groupIdEnd;
	}
	public final void setDptTargetType(String dptTargetType) {
		this.dptTargetType = dptTargetType;
	}
	
    @ApiModelProperty(value="部門複数設定フラグ", notes="部門複数設定フラグ")
	public final String getDptTargetType() {
		return dptTargetType;
	}
	public final void setDptStart(String dptStart) {
		this.dptStart = dptStart;
	}
	
    @ApiModelProperty(value="部門FROM", notes="部門FROM")
	public final String getDptStart() {
		return dptStart;
	}
	public final void setDptEnd(String dptEnd) {
		this.dptEnd = dptEnd;
	}
	
    @ApiModelProperty(value="部門TO", notes="部門TO")
	public final String getDptEnd() {
		return dptEnd;
	}
	public final void setBrandTargetType(String brandTargetType) {
		this.brandTargetType = brandTargetType;
	}
	
    @ApiModelProperty(value="ブランド複数設定フラグ", notes="ブランド複数設定フラグ")
	public final String getBrandTargetType() {
		return brandTargetType;
	}
	public final void setBrandIdStart(String brandIdStart) {
		this.brandIdStart = brandIdStart;
	}
	
    @ApiModelProperty(value="ブランドFROM", notes="ブランドFROM")
	public final String getBrandIdStart() {
		return brandIdStart;
	}
	public final void setBrandIdEnd(String brandIdEnd) {
		this.brandIdEnd = brandIdEnd;
	}
	
    @ApiModelProperty(value="ブランドTO", notes="ブランドTO")
	public final String getBrandIdEnd() {
		return brandIdEnd;
	}
	public final void setSkuTargetType(String skuTargetType) {
		this.skuTargetType = skuTargetType;
	}
	
    @ApiModelProperty(value="商品コード複数設定フラグ", notes="商品コード複数設定フラグ")
	public final String getSkuTargetType() {
		return skuTargetType;
	}
	public final void setSkuStart(String skuStart) {
		this.skuStart = skuStart;
	}
	
    @ApiModelProperty(value="商品コードFROM", notes="商品コードFROM")
	public final String getSkuStart() {
		return skuStart;
	}
	public final void setSkuEnd(String skuEnd) {
		this.skuEnd = skuEnd;
	}
	
    @ApiModelProperty(value="商品コードTO", notes="商品コードTO")
	public final String getSkuEnd() {
		return skuEnd;
	}
	public final void setInfoBasePoint(String infoBasePoint) {
		this.infoBasePoint = infoBasePoint;
	}
	
    @ApiModelProperty(value="キャンペーン商品マスタ-基準ポイント", notes="キャンペーン商品マスタ基準ポイント")
	public final String getInfoBasePoint() {
		return infoBasePoint;
	}
	public final void setDetailBasePoint(String detailBasePoint) {
		this.detailBasePoint = detailBasePoint;
	}
	
    @ApiModelProperty(value="キャンペーン商品明細マスタ-基準ポイント", notes="キャンペーン商品明細マスタ-基準ポイント")
	public final String getDetailBasePoint() {
		return detailBasePoint;
	}
	public final void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	
    @ApiModelProperty(value="設定区分", notes="設定区分")
	public final String getTargetType() {
		return targetType;
	}
	public final void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	
    @ApiModelProperty(value="設定コード", notes="設定コード")
	public final String getTargetId() {
		return targetId;
	}
}
