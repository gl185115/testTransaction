package ncr.res.mobilepos.promotion.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PmDiscountInfo")
@ApiModel(value="PmDiscountInfo")
public class PmDiscountInfo {
	
	@XmlElement(name = "SumTruePrice")
	private double sumTruePrice;
	
	@XmlElement(name = "DiscountPrice")
	private double discountPrice;
	
	@XmlElement(name = "SumNowPrice")
	private double sumNowPrice;
	
	@XmlElement(name = "PmKey")
	private String pmKey;
	
	@XmlElement(name = "PmList")
	private List<PmItemInfo> pmList;
	
	@XmlElement(name = "Times")
	private int times;
	
	@XmlElement(name = "SubNum1")
	private int subNum1;
	
	public int getSubNum1() {
		return subNum1;
	}

	public void setSubNum1(int subNum1) {
		this.subNum1 = subNum1;
	}

	public int getTimes() {
		return times;
	}

	public void setTimes(int times) {
		this.times = times;
	}

	public String getPmKey() {
		return pmKey;
	}

	public void setPmKey(String pmKey) {
		this.pmKey = pmKey;
	}

	public double getSumTruePrice() {
		return sumTruePrice;
	}

	public void setSumTruePrice(double sumTruePrice) {
		this.sumTruePrice = sumTruePrice;
	}

	public double getDiscountPrice() {
		return discountPrice;
	}

	public void setDiscountPrice(double discountPrice) {
		this.discountPrice = discountPrice;
	}

	public double getSumNowPrice() {
		return sumNowPrice;
	}

	public void setSumNowPrice(double sumNowPrice) {
		this.sumNowPrice = sumNowPrice;
	}

	public List<PmItemInfo> getPmList() {
		return pmList;
	}

	public void setPmList(List<PmItemInfo> pmList) {
		this.pmList = pmList;
	}
}
