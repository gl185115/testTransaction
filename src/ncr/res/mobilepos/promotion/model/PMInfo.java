package ncr.res.mobilepos.promotion.model;

import java.sql.Date;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PricePromInfo")
@ApiModel(value="PricePromInfo")
public class PMInfo {
	
	@XmlElement(name = "pmNo")
	private int pmNo;
	
	@XmlElement(name = "pMGroupNo")
	private int pMGroupNo;
	
	@XmlElement(name = "pmPrice")
	private double pmPrice;
	
	@XmlElement(name = "pMCnt")
	private int pMCnt;
	
	@XmlElement(name = "pMName")
	private String pMName;
	
	@XmlElement(name = "pMDisplayNo")
	private String pMDisplayNo;
	
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
	
	@XmlElement(name = "DecisionPMPrice")
	private Double decisionPMPrice;
	
	@XmlElement(name = "PMRate")
	private double pmRate;
	
	@XmlElement(name = "DecisionPMRate")
	private Double decisionPMRate;
	
	@XmlElement(name = "SubNum1")
	private int subNum1;


	public int getSubNum1() {
		return subNum1;
	}

	public void setSubNum1(int subNum1) {
		this.subNum1 = subNum1;
	}

	public Double getDecisionPMPrice() {
		return decisionPMPrice;
	}

	public void setDecisionPMPrice(Double decisionPMPrice) {
		this.decisionPMPrice = decisionPMPrice;
	}

	public double getPmRate() {
		return pmRate;
	}

	public void setPmRate(double pMRate) {
		this.pmRate = pMRate;
	}

	public Double getDecisionPMRate() {
		return decisionPMRate;
	}

	public void setDecisionPMRate(Double decisionPMRate) {
		this.decisionPMRate = decisionPMRate;
	}

	public String getDiscountClass() {
		return discountClass;
	}

	public void setDiscountClass(String discountClass) {
		this.discountClass = discountClass;
	}

	public int getPmNo() {
		return pmNo;
	}

	public void setPmNo(int pmNo) {
		this.pmNo = pmNo;
	}

	public int getPMGroupNo() {
		return pMGroupNo;
	}

	public void setPMGroupNo(int pMGroupNo) {
		this.pMGroupNo = pMGroupNo;
	}

	public double getPmPrice() {
		return pmPrice;
	}

	public void setPmPrice(double pmPrice) {
		this.pmPrice = pmPrice;
	}

	public int getpMCnt() {
		return pMCnt;
	}

	public void setpMCnt(int pMCnt) {
		this.pMCnt = pMCnt;
	}

	public String getpMName() {
		return pMName;
	}

	public void setpMName(String pMName) {
		this.pMName = pMName;
	}

	public String getpMDisplayNo() {
		return pMDisplayNo;
	}

	public void setpMDisplayNo(String pMDisplayNo) {
		this.pMDisplayNo = pMDisplayNo;
	}

	public int getpMGroupNo() {
		return pMGroupNo;
	}

	public void setpMGroupNo(int pMGroupNo) {
		this.pMGroupNo = pMGroupNo;
	}

	public Date getStartDate() {
		return startDate;
	}

	public void setStartDate(Date startDate) {
		this.startDate = startDate;
	}

	public Date getEndDate() {
		return endDate;
	}

	public void setEndDate(Date endDate) {
		this.endDate = endDate;
	}

	public String getStartTime() {
		return startTime;
	}

	public void setStartTime(String startTime) {
		this.startTime = startTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getSaleStartTime() {
		return saleStartTime;
	}

	public void setSaleStartTime(String saleStartTime) {
		this.saleStartTime = saleStartTime;
	}

	public String getSaleEndTime() {
		return saleEndTime;
	}

	public void setSaleEndTime(String saleEndTime) {
		this.saleEndTime = saleEndTime;
	}

	public int getDayOfWeekSettingFlag() {
		return dayOfWeekSettingFlag;
	}

	public void setDayOfWeekSettingFlag(int dayOfWeekSettingFlag) {
		this.dayOfWeekSettingFlag = dayOfWeekSettingFlag;
	}

	public int getDayOfWeekMonFlag() {
		return dayOfWeekMonFlag;
	}

	public void setDayOfWeekMonFlag(int dayOfWeekMonFlag) {
		this.dayOfWeekMonFlag = dayOfWeekMonFlag;
	}

	public int getDayOfWeekTueFlag() {
		return dayOfWeekTueFlag;
	}

	public void setDayOfWeekTueFlag(int dayOfWeekTueFlag) {
		this.dayOfWeekTueFlag = dayOfWeekTueFlag;
	}

	public int getDayOfWeekWedFlag() {
		return dayOfWeekWedFlag;
	}

	public void setDayOfWeekWedFlag(int dayOfWeekWedFlag) {
		this.dayOfWeekWedFlag = dayOfWeekWedFlag;
	}

	public int getDayOfWeekThuFlag() {
		return dayOfWeekThuFlag;
	}

	public void setDayOfWeekThuFlag(int dayOfWeekThuFlag) {
		this.dayOfWeekThuFlag = dayOfWeekThuFlag;
	}

	public int getDayOfWeekFriFlag() {
		return dayOfWeekFriFlag;
	}

	public void setDayOfWeekFriFlag(int dayOfWeekFriFlag) {
		this.dayOfWeekFriFlag = dayOfWeekFriFlag;
	}

	public int getDayOfWeekSatFlag() {
		return dayOfWeekSatFlag;
	}

	public void setDayOfWeekSatFlag(int dayOfWeekSatFlag) {
		this.dayOfWeekSatFlag = dayOfWeekSatFlag;
	}

	public int getDayOfWeekSunFlag() {
		return dayOfWeekSunFlag;
	}

	public void setDayOfWeekSunFlag(int dayOfWeekSunFlag) {
		this.dayOfWeekSunFlag = dayOfWeekSunFlag;
	}
	
	public PMInfo() {
	}
	 
	public PMInfo(final PMInfo info)
	{
	    this.pmNo = info.getPmNo();
	    this.pMGroupNo = info.getPMGroupNo();
	    this.pmPrice = info.getPmPrice();
	    this.pMCnt = info.getpMCnt();
	    this.pMName = info.getpMName();
	    this.pMDisplayNo = info.getpMDisplayNo();
	    this.startDate   = info.getStartDate();
	    this.endDate = info.getEndDate();
	    this.startTime = info.getStartTime();
	    this.endTime     = info.getEndTime();
	    this.saleStartTime = info.getSaleStartTime();
	    this.saleEndTime = info.getSaleEndTime();
	    this.dayOfWeekSettingFlag = info.getDayOfWeekSettingFlag();
	    this.dayOfWeekMonFlag = info.getDayOfWeekMonFlag();
	    this.dayOfWeekTueFlag = info.getDayOfWeekTueFlag();
	    this.dayOfWeekWedFlag = info.getDayOfWeekWedFlag();
	    this.dayOfWeekThuFlag = info.getDayOfWeekThuFlag();
	    this.dayOfWeekFriFlag = info.getDayOfWeekFriFlag();
	    this.dayOfWeekSatFlag = info.getDayOfWeekSatFlag();
	    this.dayOfWeekSunFlag = info.getDayOfWeekSunFlag();
	    this.discountClass = info.getDiscountClass();
	    this.decisionPMPrice = info.getDecisionPMPrice();
	    this.pmRate = info.getPmRate();
	    this.decisionPMRate = info.getDecisionPMRate();
	    this.subNum1 = info.getSubNum1();
	}
}
