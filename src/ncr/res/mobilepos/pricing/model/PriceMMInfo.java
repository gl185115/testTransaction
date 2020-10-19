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

	@ApiModelProperty(value="成立数1", notes="成立数1")
	public int getConditionCount1() {
		return conditionCount1;
	}

	public void setConditionCount1(int conditionCount1) {
		this.conditionCount1 = conditionCount1;
	}

	@ApiModelProperty(value="成立数2", notes="成立数2")
	public int getConditionCount2() {
		return conditionCount2;
	}

	public void setConditionCount2(int conditionCount2) {
		this.conditionCount2 = conditionCount2;
	}

	@ApiModelProperty(value="成立数3", notes="成立数3")
	public int getConditionCount3() {
		return conditionCount3;
	}

	public void setConditionCount3(int conditionCount3) {
		this.conditionCount3 = conditionCount3;
	}

	@ApiModelProperty(value="成立価格3", notes="成立価格3")
	public double getConditionPrice3() {
		return conditionPrice3;
	}

	public void setConditionPrice3(double conditionPrice3) {
		this.conditionPrice3 = conditionPrice3;
	}

	@ApiModelProperty(value="成立価格2", notes="成立価格2")
	public double getConditionPrice2() {
		return conditionPrice2;
	}

	public void setConditionPrice2(double conditionPrice2) {
		this.conditionPrice2 = conditionPrice2;
	}

	@ApiModelProperty(value="成立価格1", notes="成立価格1")
	public double getConditionPrice1() {
		return conditionPrice1;
	}

	public void setConditionPrice1(double conditionPrice1) {
		this.conditionPrice1 = conditionPrice1;
	}

	@ApiModelProperty(value="成立後価格1", notes="成立後価格1")
	public Double getDecisionPrice1() {
		return decisionPrice1;
	}

	public void setDecisionPrice1(Double decisionPrice1) {
		this.decisionPrice1 = decisionPrice1;
	}

	@ApiModelProperty(value="成立後価格2", notes="成立後価格2")
	public Double getDecisionPrice2() {
		return decisionPrice2;
	}

	public void setDecisionPrice2(Double decisionPrice2) {
		this.decisionPrice2 = decisionPrice2;
	}

	@ApiModelProperty(value="成立後価格3", notes="成立後価格3")
	public Double getDecisionPrice3() {
		return decisionPrice3;
	}

	public void setDecisionPrice3(Double decisionPrice3) {
		this.decisionPrice3 = decisionPrice3;
	}

	@ApiModelProperty(value="平均価格1", notes="平均価格1")
	public double getAveragePrice1() {
		return averagePrice1;
	}

	public void setAveragePrice1(double averagePrice1) {
		this.averagePrice1 = averagePrice1;
	}

	@ApiModelProperty(value="平均価格2", notes="平均価格2")
	public double getAveragePrice2() {
		return averagePrice2;
	}

	public void setAveragePrice2(double averagePrice2) {
		this.averagePrice2 = averagePrice2;
	}

	@ApiModelProperty(value="平均価格3", notes="平均価格3")
	public double getAveragePrice3() {
		return averagePrice3;
	}

	public void setAveragePrice3(double averagePrice3) {
		this.averagePrice3 = averagePrice3;
	}

	@ApiModelProperty(value="メモ", notes="メモ")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@ApiModelProperty(value="商品コード", notes="商品コード")
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
	
	@ApiModelProperty(value="商品コード", notes="商品コード")
	public String getTargetStoreType() {
		return targetStoreType;
	}

	public void setTargetStoreType(String targetStoreType) {
		this.targetStoreType = targetStoreType;
	}
	
	@ApiModelProperty(value="MM期間FROM", notes="MM期間FROM")
	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	@ApiModelProperty(value="MM期間TO", notes="MM期間TO")
	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	@ApiModelProperty(value="開始時刻", notes="開始時刻")
	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	@ApiModelProperty(value="終了時刻", notes="終了時刻")	
	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	@ApiModelProperty(value="タイムセール開始時刻", notes="タイムセール開始時刻")
	public String getSaleStartTime() {
		return saleStartTime;
	}

	public void setSaleStartTime(String saleStartTime) {
		this.saleStartTime = saleStartTime;
	}

	@ApiModelProperty(value="タイムセール終了時刻", notes="タイムセール終了時刻")
	public String getSaleEndTime() {
		return saleEndTime;
	}

	public void setSaleEndTime(String saleEndTime) {
		this.saleEndTime = saleEndTime;
	}

	@ApiModelProperty(value="曜日設定フラグ", notes="曜日設定フラグ")	
	public int getDayOfWeekSettingFlag() {
		return dayOfWeekSettingFlag;
	}

	public void setDayOfWeekSettingFlag(int dayOfWeekSettingFlag) {
		this.dayOfWeekSettingFlag = dayOfWeekSettingFlag;
	}

	@ApiModelProperty(value="曜日設定(月)フラグ", notes="曜日設定(月)フラグ")	
	public int getDayOfWeekMonFlag() {
		return dayOfWeekMonFlag;
	}

	public void setDayOfWeekMonFlag(int dayOfWeekMonFlag) {
		this.dayOfWeekMonFlag = dayOfWeekMonFlag;
	}

	@ApiModelProperty(value="曜日設定(火)フラグ", notes="曜日設定(火)フラグ")	
	public int getDayOfWeekTueFlag() {
		return dayOfWeekTueFlag;
	}

	public void setDayOfWeekTueFlag(int dayOfWeekTueFlag) {
		this.dayOfWeekTueFlag = dayOfWeekTueFlag;
	}

	@ApiModelProperty(value="曜日設定(水)フラグ", notes="曜日設定(水)フラグ")	
	public int getDayOfWeekWedFlag() {
		return dayOfWeekWedFlag;
	}

	public void setDayOfWeekWedFlag(int dayOfWeekWedFlag) {
		this.dayOfWeekWedFlag = dayOfWeekWedFlag;
	}

	@ApiModelProperty(value="曜日設定(木)フラグ", notes="曜日設定(木)フラグ")	
	public int getDayOfWeekThuFlag() {
		return dayOfWeekThuFlag;
	}

	public void setDayOfWeekThuFlag(int dayOfWeekThuFlag) {
		this.dayOfWeekThuFlag = dayOfWeekThuFlag;
	}

	@ApiModelProperty(value="曜日設定(金)フラグ", notes="曜日設定(金)フラグ")	
	public int getDayOfWeekFriFlag() {
		return dayOfWeekFriFlag;
	}

	public void setDayOfWeekFriFlag(int dayOfWeekFriFlag) {
		this.dayOfWeekFriFlag = dayOfWeekFriFlag;
	}

	@ApiModelProperty(value="曜日設定(土)フラグ", notes="曜日設定(土)フラグ")	
	public int getDayOfWeekSatFlag() {
		return dayOfWeekSatFlag;
	}

	public void setDayOfWeekSatFlag(int dayOfWeekSatFlag) {
		this.dayOfWeekSatFlag = dayOfWeekSatFlag;
	}

	@ApiModelProperty(value="曜日設定(日)フラグ", notes="曜日設定(日)フラグ")	
	public int getDayOfWeekSunFlag() {
		return dayOfWeekSunFlag;
	}

	public void setDayOfWeekSunFlag(int dayOfWeekSunFlag) {
		this.dayOfWeekSunFlag = dayOfWeekSunFlag;
	}

	@ApiModelProperty(value="MM表示用NO", notes="MM表示用NO")	
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