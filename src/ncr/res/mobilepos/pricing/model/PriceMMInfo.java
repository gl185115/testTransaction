package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PricePromInfo")
@ApiModel(value="PricePromInfo")
public class PriceMMInfo {

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

	@ApiModelProperty(value="BMNO", notes="BMNO")
	public String getMMNo() {
		return MMNo;
	}

	public void setMMNo(String MMNo) {
		this.MMNo = MMNo;
	}

	@ApiModelProperty(value="ê¨óßêî1", notes="ê¨óßêî1")
	public int getConditionCount1() {
		return conditionCount1;
	}

	public void setConditionCount1(int conditionCount1) {
		this.conditionCount1 = conditionCount1;
	}

	@ApiModelProperty(value="ê¨óßêî2", notes="ê¨óßêî2")
	public int getConditionCount2() {
		return conditionCount2;
	}

	public void setConditionCount2(int conditionCount2) {
		this.conditionCount2 = conditionCount2;
	}

	@ApiModelProperty(value="ê¨óßêî3", notes="ê¨óßêî3")
	public int getConditionCount3() {
		return conditionCount3;
	}

	public void setConditionCount3(int conditionCount3) {
		this.conditionCount3 = conditionCount3;
	}

	@ApiModelProperty(value="ê¨óßâøäi3", notes="ê¨óßâøäi3")
	public double getConditionPrice3() {
		return conditionPrice3;
	}

	public void setConditionPrice3(double conditionPrice3) {
		this.conditionPrice3 = conditionPrice3;
	}

	@ApiModelProperty(value="ê¨óßâøäi2", notes="ê¨óßâøäi2")
	public double getConditionPrice2() {
		return conditionPrice2;
	}

	public void setConditionPrice2(double conditionPrice2) {
		this.conditionPrice2 = conditionPrice2;
	}

	@ApiModelProperty(value="ê¨óßâøäi1", notes="ê¨óßâøäi1")
	public double getConditionPrice1() {
		return conditionPrice1;
	}

	public void setConditionPrice1(double conditionPrice1) {
		this.conditionPrice1 = conditionPrice1;
	}

	@ApiModelProperty(value="ê¨óßå„âøäi1", notes="ê¨óßå„âøäi1")
	public Double getDecisionPrice1() {
		return decisionPrice1;
	}

	public void setDecisionPrice1(Double decisionPrice1) {
		this.decisionPrice1 = decisionPrice1;
	}

	@ApiModelProperty(value="ê¨óßå„âøäi2", notes="ê¨óßå„âøäi2")
	public Double getDecisionPrice2() {
		return decisionPrice2;
	}

	public void setDecisionPrice2(Double decisionPrice2) {
		this.decisionPrice2 = decisionPrice2;
	}

	@ApiModelProperty(value="ê¨óßå„âøäi3", notes="ê¨óßå„âøäi3")
	public Double getDecisionPrice3() {
		return decisionPrice3;
	}

	public void setDecisionPrice3(Double decisionPrice3) {
		this.decisionPrice3 = decisionPrice3;
	}

	@ApiModelProperty(value="ïΩãœâøäi1", notes="ïΩãœâøäi1")
	public double getAveragePrice1() {
		return averagePrice1;
	}

	public void setAveragePrice1(double averagePrice1) {
		this.averagePrice1 = averagePrice1;
	}

	@ApiModelProperty(value="ïΩãœâøäi2", notes="ïΩãœâøäi2")
	public double getAveragePrice2() {
		return averagePrice2;
	}

	public void setAveragePrice2(double averagePrice2) {
		this.averagePrice2 = averagePrice2;
	}

	@ApiModelProperty(value="ïΩãœâøäi3", notes="ïΩãœâøäi3")
	public double getAveragePrice3() {
		return averagePrice3;
	}

	public void setAveragePrice3(double averagePrice3) {
		this.averagePrice3 = averagePrice3;
	}

	@ApiModelProperty(value="ÉÅÉÇ", notes="ÉÅÉÇ")
	public String getNote() {
		return note;
	}

	public void setNote(String note) {
		this.note = note;
	}

	@ApiModelProperty(value="è§ïiÉRÅ[Éh", notes="è§ïiÉRÅ[Éh")
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}
}