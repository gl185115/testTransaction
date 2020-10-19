package ncr.res.mobilepos.pricing.model;

import java.sql.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PricePromInfo")
@ApiModel(value="PricePromInfo")
public class PricePromInfo {

	@XmlElement(name = "promotionNo")
    private String promotionNo;

	@XmlElement(name = "promotionName")
    private String promotionName;

	@XmlElement(name = "memberFlag")
	private String memberFlag;
	
	@XmlElement(name = "reasonCode")
	private String reasonCode;
		
	@XmlElement(name = "promotionDetailNo")
	private String promotionDetailNo;
	
	@XmlElement(name = "promotionType")
    private String promotionType;

	@XmlElement(name = "DptNameLocal")
    private String dptNameLocal;
	
	// qianqiang add start
	@XmlElement(name = "promotionDisplayNo")
    private String promotionDisplayNo;
	// qianqiang add end
	
	@XmlElement(name = "brandFlag")
    private int brandFlag;

	@XmlElement(name = "dpt")
    private String dpt;

	@XmlElement(name = "line")
    private String line;

	@XmlElement(name = "class")
    private String clas;

	@XmlElement(name = "sku")
    private String sku;

	@XmlElement(name = "discountClass")
    private String discountClass;

	@XmlElement(name = "diacountRate")
    private double diacountRate;

	@XmlElement(name = "discountAmt")
    private long discountAmt;
	
	// lilx 20191126 add start
	@XmlElement(name = "salesPrice")
    private long salesPrice;
	
	@XmlElement(name = "startDate")
    private Date startDate;	
	
	@XmlElement(name = "endDate")
    private Date endDate;
	
	@XmlElement(name = "startTime")
    private String startTime;
	
	@XmlElement(name = "endTime")
    private String endTime;	

	@XmlElement(name = "saleStartTime")
    private String saleStartTime;
	
	@XmlElement(name = "saleEndTime")
    private String saleEndTime;
	// lilx 20191126 add end

	// qianqiang add start
	@XmlElement(name = "dayOfWeekSettingFlag")
    private int dayOfWeekSettingFlag;

	@XmlElement(name = "dayOfWeekMonFlag")
    private int dayOfWeekMonFlag;
	
	@XmlElement(name = "dayOfWeekTueFlag")
    private int dayOfWeekTueFlag;

	@XmlElement(name = "dayOfWeekWedFlag")
    private int dayOfWeekWedFlag;

	@XmlElement(name = "dayOfWeekThuFlag")
    private int dayOfWeekThuFlag;

	@XmlElement(name = "dayOfWeekFriFlag")
    private int dayOfWeekFriFlag;

	@XmlElement(name = "dayOfWeekSatFlag")
    private int dayOfWeekSatFlag;

	@XmlElement(name = "dayOfWeekSunFlag")
    private int dayOfWeekSunFlag;
	// qianqiang add end
	
	@XmlElement(name = "brandId")
    private String brandId;
	
	@XmlElement(name = "SubNum1")
	private int SubNum1;
	@XmlElement(name = "SubNum2")
	private int subNum2;
	@XmlElement(name = "SubCode1")
	private int subCode1;
	
	@XmlElement(name = "SubCode2")
	private int subCode2;
	
	@XmlElement(name = "SubCode3")
	private String subCode3;
	
	@XmlElement(name = "SubCode4")
	private int subCode4;
	
	@XmlElement(name = "SubCode5")
	private int subCode5;

	public int getSubNum2() {
		return subNum2;
	}

	public void setSubNum2(int subNum2) {
		this.subNum2 = subNum2;
	}

	public int getSubCode1() {
		return subCode1;
	}

	/**
	 * @return ���W����t���O
	 */
	public void setSubCode1(int subCode1) {
		this.subCode1 = subCode1;
	}

	/**
	 * @return �l���敪
	 */
	public int getSubCode2() {
		return subCode2;
	}

	public void setSubCode2(int subCode2) {
		this.subCode2 = subCode2;
	}

	public String getSubCode3() {
		return subCode3;
	}

	public void setSubCode3(String subCode3) {
		this.subCode3 = subCode3;
	}

	public int getSubCode4() {
		return subCode4;
	}

	public void setSubCode4(int subCode4) {
		this.subCode4 = subCode4;
	}

	public int getSubCode5() {
		return subCode5;
	}

	public void setSubCode5(int subCode5) {
		this.subCode5 = subCode5;
	}

	public int getSubNum1() {
		return SubNum1;
	}

	public void setSubNum1(int subNum1) {
		SubNum1 = subNum1;
	}

	@ApiModelProperty(value="���No", notes="���No")
    public String getPromotionNo() {
        return promotionNo;
    }

    public void setPromotionNo(String promotionNo) {
        this.promotionNo = promotionNo;
    }

	@ApiModelProperty(value="���\���pNo", notes="���\���pNo")
	public String getPromotionDisplayNo() {
		return promotionDisplayNo;
	}

	public void setPromotionDisplayNo(String promotionDisplayNo) {
		this.promotionDisplayNo = promotionDisplayNo;
	}

	@ApiModelProperty(value="��於��", notes="��於��")
    public String getPromotionName() {
        return promotionName;
    }

    public void setPromotionName(String promotionName) {
        this.promotionName = promotionName;
    }
	public String getMemberFlag() {
		return memberFlag;
	}
	
	public void setMemberFlag(String memberFlag) {
		this.memberFlag = memberFlag;
	}

	public String getReasonCode() {
		return reasonCode;
	}

	public void setReasonCode(String reasonCode) {
		this.reasonCode = reasonCode;
	}

	public String getPromotionDetailNo() {
		return promotionDetailNo;
	}

	public void setPromotionDetailNo(String promotionDetailNo) {
		this.promotionDetailNo = promotionDetailNo;
	}

	public double getDiacountRate() {
		return diacountRate;
	}

	public void setDiacountRate(double diacountRate) {
		this.diacountRate = diacountRate;
	}

	@ApiModelProperty(value="�ݒ�敪", notes="�ݒ�敪")
    public String getPromotionType() {
        return promotionType;
    }

    public void setPromotionType(String promotionType) {
        this.promotionType = promotionType;
    }
    public String getDptNameLocal() {
		return dptNameLocal;
	}

	public void setDptNameLocal(String dptNameLocal) {
		this.dptNameLocal = dptNameLocal;
	}

	@ApiModelProperty(value="�u�����h�ݒ�t���O", notes="�u�����h�ݒ�t���O")
    public int getBrandFlag() {
        return brandFlag;
    }

    public void setBrandFlag(int brandFlag) {
        this.brandFlag = brandFlag;
    }

	@ApiModelProperty(value="����R�[�h", notes="����R�[�h")
    public String getDpt() {
        return dpt;
    }

    public void setDpt(String dpt) {
        this.dpt = dpt;
    }

	@ApiModelProperty(value="�i��R�[�h", notes="�i��R�[�h")
    public String getLine() {
        return line;
    }

    public void setLine(String line) {
        this.line = line;
    }

	@ApiModelProperty(value="�N���X�R�[�h", notes="�N���X�R�[�h")
    public String getClas() {
        return clas;
    }

    public void setClas(String clas) {
        this.clas = clas;
    }

	@ApiModelProperty(value="���i�R�[�h", notes="���i�R�[�h")
    public String getSku() {
        return sku;
    }

    public void setSku(String sku) {
        this.sku = sku;
    }

	@ApiModelProperty(value="�����敪", notes="�����敪")
    public String getDiscountClass() {
        return discountClass;
    }

    public void setDiscountClass(String discountClass) {
        this.discountClass = discountClass;
    }

	@ApiModelProperty(value="������", notes="������")
    public double getDiscountRate() {
        return diacountRate;
    }

    public void setDiscountRate(double diacountRate) {
        this.diacountRate = diacountRate;
    }

	@ApiModelProperty(value="�l���z", notes="�l���z")
    public long getDiscountAmt() {
        return discountAmt;
    }

    public void setDiscountAmt(long discountAmt) {
        this.discountAmt = discountAmt;
    }

    @ApiModelProperty(value="���P��", notes="���P��")
	public long getSalesPrice() {
		return salesPrice;
	}

	public void setSalesPrice(long salesPrice) {
		this.salesPrice = salesPrice;
	}
	
	@ApiModelProperty(value="��������FROM", notes="��������FROM")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@ApiModelProperty(value="��������TO", notes="��������TO")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@ApiModelProperty(value="�J�n����", notes="�J�n����")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@ApiModelProperty(value="�I������", notes="�I������")	
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@ApiModelProperty(value="�^�C���Z�[���J�n����", notes="�^�C���Z�[���J�n����")
	public String getSaleStartTime() {
		return saleStartTime;
	}

	public void setSaleStartTime(String saleStartTime) {
		this.saleStartTime = saleStartTime;
	}

	@ApiModelProperty(value="�^�C���Z�[���I������", notes="�^�C���Z�[���I������")
	public String getSaleEndTime() {
		return saleEndTime;
	}

	public void setSaleEndTime(String saleEndTime) {
		this.saleEndTime = saleEndTime;
	}

	@ApiModelProperty(value="�j���ݒ�t���O", notes="�j���ݒ�t���O")	
	public int getDayOfWeekSettingFlag() {
		return dayOfWeekSettingFlag;
	}

	public void setDayOfWeekSettingFlag(int dayOfWeekSettingFlag) {
		this.dayOfWeekSettingFlag = dayOfWeekSettingFlag;
	}

	@ApiModelProperty(value="�j���ݒ�(��)�t���O", notes="�j���ݒ�(��)�t���O")	
	public int getDayOfWeekMonFlag() {
		return dayOfWeekMonFlag;
	}

	public void setDayOfWeekMonFlag(int dayOfWeekMonFlag) {
		this.dayOfWeekMonFlag = dayOfWeekMonFlag;
	}

	@ApiModelProperty(value="�j���ݒ�(��)�t���O", notes="�j���ݒ�(��)�t���O")	
	public int getDayOfWeekTueFlag() {
		return dayOfWeekTueFlag;
	}

	public void setDayOfWeekTueFlag(int dayOfWeekTueFlag) {
		this.dayOfWeekTueFlag = dayOfWeekTueFlag;
	}

	@ApiModelProperty(value="�j���ݒ�(��)�t���O", notes="�j���ݒ�(��)�t���O")	
	public int getDayOfWeekWedFlag() {
		return dayOfWeekWedFlag;
	}

	public void setDayOfWeekWedFlag(int dayOfWeekWedFlag) {
		this.dayOfWeekWedFlag = dayOfWeekWedFlag;
	}

	@ApiModelProperty(value="�j���ݒ�(��)�t���O", notes="�j���ݒ�(��)�t���O")	
	public int getDayOfWeekThuFlag() {
		return dayOfWeekThuFlag;
	}

	public void setDayOfWeekThuFlag(int dayOfWeekThuFlag) {
		this.dayOfWeekThuFlag = dayOfWeekThuFlag;
	}

	@ApiModelProperty(value="�j���ݒ�(��)�t���O", notes="�j���ݒ�(��)�t���O")	
	public int getDayOfWeekFriFlag() {
		return dayOfWeekFriFlag;
	}

	public void setDayOfWeekFriFlag(int dayOfWeekFriFlag) {
		this.dayOfWeekFriFlag = dayOfWeekFriFlag;
	}

	@ApiModelProperty(value="�j���ݒ�(�y)�t���O", notes="�j���ݒ�(�y)�t���O")	
	public int getDayOfWeekSatFlag() {
		return dayOfWeekSatFlag;
	}

	public void setDayOfWeekSatFlag(int dayOfWeekSatFlag) {
		this.dayOfWeekSatFlag = dayOfWeekSatFlag;
	}

	@ApiModelProperty(value="�j���ݒ�(��)�t���O", notes="�j���ݒ�(��)�t���O")	
	public int getDayOfWeekSunFlag() {
		return dayOfWeekSunFlag;
	}

	public void setDayOfWeekSunFlag(int dayOfWeekSunFlag) {
		this.dayOfWeekSunFlag = dayOfWeekSunFlag;
	}

	@ApiModelProperty(value="�u�����h�R�[�h", notes="�u�����h�R�[�h")
    public String getBrandId() {
        return brandId;
    }

    public void setBrandId(String brandId) {
        this.brandId = brandId;
    }

	@Override
	public String toString() {
		return "PricePromInfo [promotionNo=" + promotionNo + ", promotionName=" + promotionName + ", memberFlag="
				+ memberFlag + ", reasonCode=" + reasonCode + ", promotionDetailNo=" + promotionDetailNo
				+ ", promotionType=" + promotionType + ", dptNameLocal=" + dptNameLocal + ", promotionDisplayNo="
				+ promotionDisplayNo + ", brandFlag=" + brandFlag + ", dpt=" + dpt + ", line=" + line + ", clas=" + clas
				+ ", sku=" + sku + ", discountClass=" + discountClass + ", diacountRate=" + diacountRate
				+ ", discountAmt=" + discountAmt + ", salesPrice=" + salesPrice + ", startDate=" + startDate
				+ ", endDate=" + endDate + ", startTime=" + startTime + ", endTime=" + endTime + ", saleStartTime="
				+ saleStartTime + ", saleEndTime=" + saleEndTime + ", dayOfWeekSettingFlag=" + dayOfWeekSettingFlag
				+ ", dayOfWeekMonFlag=" + dayOfWeekMonFlag + ", dayOfWeekTueFlag=" + dayOfWeekTueFlag
				+ ", dayOfWeekWedFlag=" + dayOfWeekWedFlag + ", dayOfWeekThuFlag=" + dayOfWeekThuFlag
				+ ", dayOfWeekFriFlag=" + dayOfWeekFriFlag + ", dayOfWeekSatFlag=" + dayOfWeekSatFlag
				+ ", dayOfWeekSunFlag=" + dayOfWeekSunFlag + ", brandId=" + brandId + ", SubNum1=" + SubNum1
				+ ", subNum2=" + subNum2 + ", subCode1=" + subCode1 + ", subCode2=" + subCode2 + ", subCode3="
				+ subCode3 + ", subCode4=" + subCode4 + ", subCode5=" + subCode5 + "]";
	}
}

