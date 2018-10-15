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
	
    @ApiModelProperty(value="�|�C���g�^�p�t���O", notes="�|�C���g�^�p�t���O")
	public final String getPointFlag() {
		return pointFlag;
	}
	
	public final void setPointType(String pointType) {
		this.pointType = pointType;
	}
	
    @ApiModelProperty(value="�|�C���g�敪", notes="�|�C���g�敪")
	public final String getPointType() {
		return pointType;
	}
	public final void setBasePrice(String basePrice) {
		this.basePrice = basePrice;
	}
	
    @ApiModelProperty(value="��z", notes="��z")
	public final String getBasePrice() {
		return basePrice;
	}
	public final void setRecordId(String recordId) {
		this.recordId = recordId;
	}
	
    @ApiModelProperty(value="�Ǘ�NO", notes="�Ǘ�NO")
	public final String getRecordId() {
		return recordId;
	}
	public final void setBasePoint(String basePoint) {
		this.basePoint = basePoint;
	}
	
    @ApiModelProperty(value="��|�C���g", notes="��|�C���g")
	public final String getBasePoint() {
		return basePoint;
	}
	public final void setCashingUnit(String cashingUnit) {
		this.cashingUnit = cashingUnit;
	}
	
    @ApiModelProperty(value="�Ҍ��z(1P)", notes="�Ҍ��z(1P)")
	public final String getCashingUnit() {
		return cashingUnit;
	}
	public final void setPointTender(String pointTender) {
		this.pointTender = pointTender;
	}
	
    @ApiModelProperty(value="�|�C���g���", notes="�|�C���g���")
	public final String getPointTender() {
		return pointTender;
	}
	public final void setPointCalcType(String pointCalcType) {
		this.pointCalcType = pointCalcType;
	}
	
    @ApiModelProperty(value="�v�Z�P�ʋ敪", notes="�v�Z�P�ʋ敪")
	public final String getPointCalcType() {
		return pointCalcType;
	}
	public final void setTaxCalcType(String taxCalcType) {
		this.taxCalcType = taxCalcType;
	}
	
    @ApiModelProperty(value="�Ōv�Z�敪", notes="�Ōv�Z�敪")
	public final String getTaxCalcType() {
		return taxCalcType;
	}
	public final void setRoundType(String roundType) {
		this.roundType = roundType;
	}
	
    @ApiModelProperty(value="�ۂߕ��@�敪", notes="�ۂߕ��@�敪")
	public final String getRoundType() {
		return roundType;
	}
	public final void setTenderSettingFlag(String tenderSettingFlag) {
		this.tenderSettingFlag = tenderSettingFlag;
	}
	
    @ApiModelProperty(value="����ݒ�t���O", notes="����ݒ�t���O")
	public final String getTenderSettingFlag() {
		return tenderSettingFlag;
	}
	public final void setBasePointCash(String basePointCash) {
		this.basePointCash = basePointCash;
	}
	
    @ApiModelProperty(value="��|�C���g(����)", notes="��|�C���g(����)")
	public final String getBasePointCash() {
		return basePointCash;
	}
	public final void setBasePointAffiliate(String basePointAffiliate) {
		this.basePointAffiliate = basePointAffiliate;
	}
	
    @ApiModelProperty(value="��|�C���g(��g)", notes="��|�C���g(��g)")
	public final String getBasePointAffiliate() {
		return basePointAffiliate;
	}
	public final void setBasePointNonAffiliate(String basePointNonAffiliate) {
		this.basePointNonAffiliate = basePointNonAffiliate;
	}
	
    @ApiModelProperty(value="��|�C���g(��g�O)", notes="��|�C���g(��g�O)")
	public final String getBasePointNonAffiliate() {
		return basePointNonAffiliate;
	}
	public final void setCardSettingFlag(String cardSettingFlag) {
		this.cardSettingFlag = cardSettingFlag;
	}
	
    @ApiModelProperty(value="�J�[�h�ݒ�t���O", notes="�J�[�h�ݒ�t���O")
	public final String getCardSettingFlag() {
		return cardSettingFlag;
	}
	public final void setTargetStoreType(String targetStoreType) {
		this.targetStoreType = targetStoreType;
	}
	
    @ApiModelProperty(value="�X�ݒ�敪", notes="�X�ݒ�敪")
	public final String getTargetStoreType() {
		return targetStoreType;
	}
	public final void setItemSettingFlag(String itemSettingFlag) {
		this.itemSettingFlag = itemSettingFlag;
	}
	
    @ApiModelProperty(value="���i�ʐݒ�t���O", notes="���i�ʐݒ�t���O")
	public final String getItemSettingFlag() {
		return itemSettingFlag;
	}
	public final void setDptSettingFlag(String dptSettingFlag) {
		this.dptSettingFlag = dptSettingFlag;
	}
	
    @ApiModelProperty(value="����ʐݒ�t���O", notes="����ʐݒ�t���O")
	public final String getDptSettingFlag() {
		return dptSettingFlag;
	}

	public final void setCardClassId(String cardClassId) {
		this.cardClassId = cardClassId;
	}
	
    @ApiModelProperty(value="�J�[�h�敪", notes="�J�[�h�敪")
	public final String getCardClassId() {
		return cardClassId;
	}
	public final void setTenderId(String tenderId) {
		this.tenderId = tenderId;
	}
	
    @ApiModelProperty(value="��ʃR�[�h", notes="��ʃR�[�h")
	public final String getTenderId() {
		return tenderId;
	}
	public final void setTenderType(String tenderType) {
		this.tenderType = tenderType;
	}
	
    @ApiModelProperty(value="�x�����", notes="�x�����")
	public final String getTenderType() {
		return tenderType;
	}
	public final void setGroupTargetType(String groupTargetType) {
		this.groupTargetType = groupTargetType;
	}
	
    @ApiModelProperty(value="�O���[�v�����ݒ�t���O", notes="�O���[�v�����ݒ�t���O")
	public final String getGroupTargetType() {
		return groupTargetType;
	}
	public final void setGroupIdStart(String groupIdStart) {
		this.groupIdStart = groupIdStart;
	}
	
    @ApiModelProperty(value="�O���[�vFROM", notes="�O���[�vFROM")
	public final String getGroupIdStart() {
		return groupIdStart;
	}
	public final void setGroupIdEnd(String groupIdEnd) {
		this.groupIdEnd = groupIdEnd;
	}
	
    @ApiModelProperty(value="�O���[�vTO", notes="�O���[�vTO")
	public final String getGroupIdEnd() {
		return groupIdEnd;
	}
	public final void setDptTargetType(String dptTargetType) {
		this.dptTargetType = dptTargetType;
	}
	
    @ApiModelProperty(value="���啡���ݒ�t���O", notes="���啡���ݒ�t���O")
	public final String getDptTargetType() {
		return dptTargetType;
	}
	public final void setDptStart(String dptStart) {
		this.dptStart = dptStart;
	}
	
    @ApiModelProperty(value="����FROM", notes="����FROM")
	public final String getDptStart() {
		return dptStart;
	}
	public final void setDptEnd(String dptEnd) {
		this.dptEnd = dptEnd;
	}
	
    @ApiModelProperty(value="����TO", notes="����TO")
	public final String getDptEnd() {
		return dptEnd;
	}
	public final void setBrandTargetType(String brandTargetType) {
		this.brandTargetType = brandTargetType;
	}
	
    @ApiModelProperty(value="�u�����h�����ݒ�t���O", notes="�u�����h�����ݒ�t���O")
	public final String getBrandTargetType() {
		return brandTargetType;
	}
	public final void setBrandIdStart(String brandIdStart) {
		this.brandIdStart = brandIdStart;
	}
	
    @ApiModelProperty(value="�u�����hFROM", notes="�u�����hFROM")
	public final String getBrandIdStart() {
		return brandIdStart;
	}
	public final void setBrandIdEnd(String brandIdEnd) {
		this.brandIdEnd = brandIdEnd;
	}
	
    @ApiModelProperty(value="�u�����hTO", notes="�u�����hTO")
	public final String getBrandIdEnd() {
		return brandIdEnd;
	}
	public final void setSkuTargetType(String skuTargetType) {
		this.skuTargetType = skuTargetType;
	}
	
    @ApiModelProperty(value="���i�R�[�h�����ݒ�t���O", notes="���i�R�[�h�����ݒ�t���O")
	public final String getSkuTargetType() {
		return skuTargetType;
	}
	public final void setSkuStart(String skuStart) {
		this.skuStart = skuStart;
	}
	
    @ApiModelProperty(value="���i�R�[�hFROM", notes="���i�R�[�hFROM")
	public final String getSkuStart() {
		return skuStart;
	}
	public final void setSkuEnd(String skuEnd) {
		this.skuEnd = skuEnd;
	}
	
    @ApiModelProperty(value="���i�R�[�hTO", notes="���i�R�[�hTO")
	public final String getSkuEnd() {
		return skuEnd;
	}
	public final void setInfoBasePoint(String infoBasePoint) {
		this.infoBasePoint = infoBasePoint;
	}
	
    @ApiModelProperty(value="�L�����y�[�����i�}�X�^-��|�C���g", notes="�L�����y�[�����i�}�X�^��|�C���g")
	public final String getInfoBasePoint() {
		return infoBasePoint;
	}
	public final void setDetailBasePoint(String detailBasePoint) {
		this.detailBasePoint = detailBasePoint;
	}
	
    @ApiModelProperty(value="�L�����y�[�����i���׃}�X�^-��|�C���g", notes="�L�����y�[�����i���׃}�X�^-��|�C���g")
	public final String getDetailBasePoint() {
		return detailBasePoint;
	}
	public final void setTargetType(String targetType) {
		this.targetType = targetType;
	}
	
    @ApiModelProperty(value="�ݒ�敪", notes="�ݒ�敪")
	public final String getTargetType() {
		return targetType;
	}
	public final void setTargetId(String targetId) {
		this.targetId = targetId;
	}
	
    @ApiModelProperty(value="�ݒ�R�[�h", notes="�ݒ�R�[�h")
	public final String getTargetId() {
		return targetId;
	}
}
