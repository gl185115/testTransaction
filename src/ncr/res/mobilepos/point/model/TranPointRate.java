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
	
    @ApiModelProperty(value="��ЃR�[�h", notes="��ЃR�[�h")
	public final String getCompanyId() {
		return companyId;
	}
	
	public final void setStoreId(String storeId) {
		this.storeId = storeId;
	}
	
    @ApiModelProperty(value="�X�܃R�[�h", notes="�X�܃R�[�h")
	public final String getStoreId() {
		return storeId;
	}
	
	public final void setPointFlag(String pointflag) {
		this.pointflag = pointflag;
	}
	
    @ApiModelProperty(value="�|�C���g�^�p�t���O", notes="�|�C���g�^�p�t���O")
	public final String getPointFlag() {
		return pointflag;
	}
	
	public final void setBasePrice(String baseprice) {
		this.baseprice = baseprice;
	}
	
    @ApiModelProperty(value="��z", notes="��z")
	public final String getBasePrice() {
		return baseprice;
	}
	
	public final void setBasePoint(String basepoint) {
		this.basepoint = basepoint;
	}
	
    @ApiModelProperty(value="��|�C���g", notes="��|�C���g")
	public final String getBasePoint() {
		return basepoint;
	}

	public final void setRecordId(String recordid) {
		this.recordid = recordid;
	}
	
    @ApiModelProperty(value="�Ǘ�NO", notes="�Ǘ�NO")
	public final String getRecordId() {
		return recordid;
	}
	
	public final void setCashingUnit(String cashingUnit) {
		this.cashingUnit = cashingUnit;
	}
	
	@ApiModelProperty(value="KPC�v�Z�P��", notes="KPC�v�Z�P��")
	public final String getCashingUnit() {
		return cashingUnit;
	}
	
	public final void setBasePointCash(String basepointcash) {
		this.basepointcash = basepointcash;
	}
	
    @ApiModelProperty(value="��|�C���g(����)", notes="��|�C���g(����)")
	public final String getBasePointCash() {
		return basepointcash;
	}

	public final void setBasePointAffiliate(String basepointaffiliate) {
		this.basepointaffiliate = basepointaffiliate;
	}
	
	@ApiModelProperty(value="��|�C���g(��g)", notes="��|�C���g(��g)")
	public final String getBasePointAffiliate() {
		return basepointaffiliate;
	}

	public final void setBasePointNonAffiliate(String basepointnonaffiliate) {
		this.basepointnonaffiliate = basepointnonaffiliate;
	}
	
	@ApiModelProperty(value="��|�C���g(��g�O)", notes="��|�C���g(��g�O)")
	public final String getBasePointNonAffiliate() {
		return basepointnonaffiliate;
	}

	public final void setPointCalcType(String pointcalctype) {
		this.pointcalctype = pointcalctype;
	}
	
	@ApiModelProperty(value="�v�Z�P�ʋ敪", notes="�v�Z�P�ʋ敪")
	public final String getPointCalcType() {
		return pointcalctype;
	}

	public final void setTaxCalcType(String taxcalctype) {
		this.taxcalctype = taxcalctype;
	}
	
	@ApiModelProperty(value="�Ōv�Z�敪", notes="�Ōv�Z�敪")
	public final String getTaxCalcType() {
		return taxcalctype;
	}

	public final void setRoundType(String roundtype) {
		this.roundtype = roundtype;
	}
	
	@ApiModelProperty(value="�ۂߕ��@�敪", notes="�ۂߕ��@�敪")
	public final String getRoundType() {
		return roundtype;
	}

	public final void setCardSettingFlag(String cardsettingflag) {
		this.cardsettingflag = cardsettingflag;
	}
	
	@ApiModelProperty(value="�J�[�h�ʐݒ�t���O", notes="�J�[�h�ʐݒ�t���O")
	public final String getCardSettingFlag() {
		return cardsettingflag;
	}
	
	public final void setDptSettingFlag(String dptsettingflag) {
		this.dptsettingflag = dptsettingflag;
	}
	
	@ApiModelProperty(value="����ʐݒ�t���O", notes="����ʐݒ�t���O")
	public final String getDptSettingFlag() {
		return dptsettingflag;
	}

	public final void setItemSettingFlag(String itemsettingflag) {
		this.itemsettingflag = itemsettingflag;
	}
	
	@ApiModelProperty(value="�v���W�F�N�g�ʐݒ�t���O", notes="�v���W�F�N�g�ʐݒ�t���O")
	public final String getItemSettingFlag() {
		return itemsettingflag;
	}

	public final void setTargetStoreType(String targetstoretype) {
		this.targetstoretype = targetstoretype;
	}
	
	@ApiModelProperty(value="�ݒ�X�܋敪", notes="�ݒ�X�܋敪")
	public final String getTargetStoreType() {
		return targetstoretype;
	}

	public final void setType(String type) {
		this.type = type;
	}
	
    @ApiModelProperty(value="�^�C�v", notes="�^�C�v")
	public final String getType() {
		return type;
	}

}
