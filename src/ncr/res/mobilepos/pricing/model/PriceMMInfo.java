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
public class PriceMMInfo implements Cloneable {

	@XmlElement(name = "MMNo")
	private String MMNo;

	@XmlElement(name = "conditionCount1")
	private int conditionCount1;

	@XmlElement(name = "conditionCount2")
	private int conditionCount2;

	@XmlElement(name = "conditionCount3")
	private int conditionCount3;

	@XmlElement(name = "conditionPrice3")
	private double conditionPrice3;

	@XmlElement(name = "conditionPrice2")
	private double conditionPrice2;

	@XmlElement(name = "conditionPrice1")
	private double conditionPrice1;

	@XmlElement(name = "decisionPrice1")
	private Double decisionPrice1;

	@XmlElement(name = "decisionPrice2")
	private Double decisionPrice2;

	@XmlElement(name = "decisionPrice3")
	private Double decisionPrice3;

	@XmlElement(name = "averagePrice1")
	private double averagePrice1;

	@XmlElement(name = "averagePrice2")
	private double averagePrice2;
	
	@XmlElement(name = "averagePrice3")
	private double averagePrice3;
	
	@XmlElement(name = "note")
	private String note;
	
	@XmlElement(name = "sku")
	private String sku;
	
	@XmlElement(name = "targetStoreType")
	private String targetStoreType;
	
	@XmlElement(name = "mmDisplayNo")
    private String mmDisplayNo;
	
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
	
	@XmlElement(name = "DiscountClass")
	private String discountClass;
	
	@XmlElement(name = "ConditionRate1")
	private Double conditionRate1;
	
	@XmlElement(name = "DecisionRate1")
	private Double decisionRate1;
	
	@XmlElement(name = "ConditionRate2")
	private Double conditionRate2;
	
	@XmlElement(name = "DecisionRate2")
	private Double decisionRate2;
	
	@XmlElement(name = "ConditionRate3")
	private Double conditionRate3;
	
	@XmlElement(name = "DecisionRate3")
	private Double decisionRate3;
	
	@XmlElement(name = "SubNum1")
	private int subNum1;

	public int getSubNum1() {
		return subNum1;
	}

	public void setSubNum1(int subNum1) {
		this.subNum1 = subNum1;
	}

	public String getDiscountClass() {
		return discountClass;
	}

	public void setDiscountClass(String discountClass) {
		this.discountClass = discountClass;
	}

	public Double getConditionRate1() {
		return conditionRate1;
	}

	public void setConditionRate1(Double conditionRate1) {
		this.conditionRate1 = conditionRate1;
	}

	public Double getDecisionRate1() {
		return decisionRate1;
	}

	public void setDecisionRate1(Double decisionRate1) {
		this.decisionRate1 = decisionRate1;
	}

	public Double getConditionRate2() {
		return conditionRate2;
	}

	public void setConditionRate2(Double conditionRate2) {
		this.conditionRate2 = conditionRate2;
	}

	public Double getDecisionRate2() {
		return decisionRate2;
	}

	public void setDecisionRate2(Double decisionRate2) {
		this.decisionRate2 = decisionRate2;
	}

	public Double getConditionRate3() {
		return conditionRate3;
	}

	public void setConditionRate3(Double conditionRate3) {
		this.conditionRate3 = conditionRate3;
	}

	public Double getDecisionRate3() {
		return decisionRate3;
	}

	public void setDecisionRate3(Double decisionRate3) {
		this.decisionRate3 = decisionRate3;
	}

	@ApiModelProperty(value="BMNO", notes="BMNO")
	public String getMMNo() {
		return MMNo;
	}

	public void setMMNo(String MMNo) {
		this.MMNo = MMNo;
	}

	@ApiModelProperty(value="������1", notes="������1")
	public int getConditionCount1() {
		return conditionCount1;
	}

	public void setConditionCount1(int conditionCount1) {
		this.conditionCount1 = conditionCount1;
	}

	@ApiModelProperty(value="������2", notes="������2")
	public int getConditionCount2() {
		return conditionCount2;
	}

	public void setConditionCount2(int conditionCount2) {
		this.conditionCount2 = conditionCount2;
	}

	@ApiModelProperty(value="������3", notes="������3")
	public int getConditionCount3() {
		return conditionCount3;
	}

	public void setConditionCount3(int conditionCount3) {
		this.conditionCount3 = conditionCount3;
	}

	@ApiModelProperty(value="�������i3", notes="�������i3")
	public double getConditionPrice3() {
		return conditionPrice3;
	}

	public void setConditionPrice3(double conditionPrice3) {
		this.conditionPrice3 = conditionPrice3;
	}

	@ApiModelProperty(value="�������i2", notes="�������i2")
	public double getConditionPrice2() {
		return conditionPrice2;
	}

	public void setConditionPrice2(double conditionPrice2) {
		this.conditionPrice2 = conditionPrice2;
	}

	@ApiModelProperty(value="�������i1", notes="�������i1")
	public double getConditionPrice1() {
		return conditionPrice1;
	}

	public void setConditionPrice1(double conditionPrice1) {
		this.conditionPrice1 = conditionPrice1;
	}

	@ApiModelProperty(value="�����㉿�i1", notes="�����㉿�i1")
	public Double getDecisionPrice1() {
		return decisionPrice1;
	}

	public void setDecisionPrice1(Double decisionPrice1) {
		this.decisionPrice1 = decisionPrice1;
	}

	@ApiModelProperty(value="�����㉿�i2", notes="�����㉿�i2")
	public Double getDecisionPrice2() {
		return decisionPrice2;
	}

	public void setDecisionPrice2(Double decisionPrice2) {
		this.decisionPrice2 = decisionPrice2;
	}

	@ApiModelProperty(value="�����㉿�i3", notes="�����㉿�i3")
	public Double getDecisionPrice3() {
		return decisionPrice3;
	}

	public void setDecisionPrice3(Double decisionPrice3) {
		this.decisionPrice3 = decisionPrice3;
	}

	@ApiModelProperty(value="���ω��i1", notes="���ω��i1")
	public double getAveragePrice1() {
		return averagePrice1;
	}

	public void setAveragePrice1(double averagePrice1) {
		this.averagePrice1 = averagePrice1;
	}

	@ApiModelProperty(value="���ω��i2", notes="���ω��i2")
	public double getAveragePrice2() {
		return averagePrice2;
	}

	public void setAveragePrice2(double averagePrice2) {
		this.averagePrice2 = averagePrice2;
	}

	@ApiModelProperty(value="���ω��i3", notes="���ω��i3")
	public double getAveragePrice3() {
		return averagePrice3;
	}

	public void setAveragePrice3(double averagePrice3) {
		this.averagePrice3 = averagePrice3;
	}

	@ApiModelProperty(value="����", notes="����")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@ApiModelProperty(value="���i�R�[�h", notes="���i�R�[�h")
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	
	@ApiModelProperty(value="���i�R�[�h", notes="���i�R�[�h")
	public String getTargetStoreType() {
		return targetStoreType;
	}

	public void setTargetStoreType(String targetStoreType) {
		this.targetStoreType = targetStoreType;
	}
	
	@ApiModelProperty(value="MM����FROM", notes="MM����FROM")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@ApiModelProperty(value="MM����TO", notes="MM����TO")
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

	@ApiModelProperty(value="MM�\���pNO", notes="MM�\���pNO")	
	public String getMmDisplayNo() {
		return mmDisplayNo;
	}

	public void setMmDisplayNo(String mmDisplayNo) {
		this.mmDisplayNo = mmDisplayNo;
	}
	
	@Override  
    public Object clone() {  
		PriceMMInfo pmi = null;  
        try{  
        	pmi = (PriceMMInfo)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return pmi;  
    }  
}