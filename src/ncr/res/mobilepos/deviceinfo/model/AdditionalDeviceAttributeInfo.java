package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * Additional Device Attribute Info.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "AdditionalDeviceAttributeInfo")
public class AdditionalDeviceAttributeInfo extends ResultBase {
	
	@XmlElement(name = "CompanyId")
    private String companyId;
	
	@XmlElement(name = "RetailStoreID")
	private String retailStoreId;
	
	@XmlElement(name = "DeviceID")
	private String deviceId;
	
	@XmlElement(name = "Training")
	private Integer training;
	
	@XmlElement(name = "ExtraCode1")
	private String extraCode1;
	
	@XmlElement(name = "ExtraCode2")
	private String extraCode2;
	
	@XmlElement(name = "ExtraCode3")
	private String extraCode3;
	
	@XmlElement(name = "ExtraCode4")
	private String extraCode4;
	
	@XmlElement(name = "ExtraCode5")
	private String extraCode5;
	
	@XmlElement(name = "ExtraCode6")
	private String extraCode6;
	
	@XmlElement(name = "ExtraCode7")
	private String extraCode7;
	
	@XmlElement(name = "ExtraCode8")
	private String extraCode8;
	
	@XmlElement(name = "ExtraCode9")
	private String extraCode9;
	
	@XmlElement(name = "ExtraCode10")
	private String extraCode10;
	
	@XmlElement(name = "ExtraNum1")
	private Integer extraNum1;
	
	@XmlElement(name = "ExtraNum2")
	private Integer extraNum2;
	
	@XmlElement(name = "ExtraNum3")
	private Integer extraNum3;
	
	@XmlElement(name = "ExtraNum4")
	private Integer extraNum4;
	
	@XmlElement(name = "ExtraNum5")
	private Integer extraNum5;
	
	@XmlElement(name = "ExtraNum6")
	private Integer extraNum6;
	
	@XmlElement(name = "ExtraNum7")
	private Integer extraNum7;
	
	@XmlElement(name = "ExtraNum8")
	private Integer extraNum8;
	
	@XmlElement(name = "ExtraNum9")
	private Integer extraNum9;
	
	@XmlElement(name = "ExtraNum10")
	private Integer extraNum10;
	
	@XmlElement(name = "ExtraFlag1")
	private Integer extraFlag1;
	
	@XmlElement(name = "ExtraFlag2")
	private Integer extraFlag2;
	
	@XmlElement(name = "ExtraFlag3")
	private Integer extraFlag3;
	
	@XmlElement(name = "ExtraFlag4")
	private Integer extraFlag4;
	
	@XmlElement(name = "ExtraFlag5")
	private Integer extraFlag5;
	
	@XmlElement(name = "ExtraFlag6")
	private Integer extraFlag6;
	
	@XmlElement(name = "ExtraFlag7")
	private Integer extraFlag7;
	
	@XmlElement(name = "ExtraFlag8")
	private Integer extraFlag8;
	
	@XmlElement(name = "ExtraFlag9")
	private Integer extraFlag9;
	
	@XmlElement(name = "ExtraFlag10")
	private Integer extraFlag10;
	
	@XmlElement(name = "ExtraFlag11")
	private Integer extraFlag11;
	
	@XmlElement(name = "ExtraFlag12")
	private Integer extraFlag12;
	
	@XmlElement(name = "ExtraFlag13")
	private Integer extraFlag13;
	
	@XmlElement(name = "ExtraFlag14")
	private Integer extraFlag14;
	
	@XmlElement(name = "ExtraFlag15")
	private Integer extraFlag15;
	
	@XmlElement(name = "ExtraFlag16")
	private Integer extraFlag16;
	
	@XmlElement(name = "ExtraFlag17")
	private Integer extraFlag17;
	
	@XmlElement(name = "ExtraFlag18")
	private Integer extraFlag18;
	
	@XmlElement(name = "ExtraFlag19")
	private Integer extraFlag19;
	
	@XmlElement(name = "ExtraFlag20")
	private Integer extraFlag20;

	public String getCompanyId() {
		return companyId;
	}

	public void setCompanyId(String companyId) {
		this.companyId = companyId;
	}

	public String getRetailStoreId() {
		return retailStoreId;
	}

	public void setRetailStoreId(String retailStoreId) {
		this.retailStoreId = retailStoreId;
	}

	public String getDeviceId() {
		return deviceId;
	}

	public void setDeviceId(String deviceId) {
		this.deviceId = deviceId;
	}

	public Integer getTraining() {
		return training;
	}

	public void setTraining(Integer training) {
		this.training = training;
	}

	public String getExtraCode1() {
		return extraCode1;
	}

	public void setExtraCode1(String extraCode1) {
		this.extraCode1 = extraCode1;
	}

	public String getExtraCode2() {
		return extraCode2;
	}

	public void setExtraCode2(String extraCode2) {
		this.extraCode2 = extraCode2;
	}

	public String getExtraCode3() {
		return extraCode3;
	}

	public void setExtraCode3(String extraCode3) {
		this.extraCode3 = extraCode3;
	}

	public String getExtraCode4() {
		return extraCode4;
	}

	public void setExtraCode4(String extraCode4) {
		this.extraCode4 = extraCode4;
	}

	public String getExtraCode5() {
		return extraCode5;
	}

	public void setExtraCode5(String extraCode5) {
		this.extraCode5 = extraCode5;
	}

	public String getExtraCode6() {
		return extraCode6;
	}

	public void setExtraCode6(String extraCode6) {
		this.extraCode6 = extraCode6;
	}

	public String getExtraCode7() {
		return extraCode7;
	}

	public void setExtraCode7(String extraCode7) {
		this.extraCode7 = extraCode7;
	}

	public String getExtraCode8() {
		return extraCode8;
	}

	public void setExtraCode8(String extraCode8) {
		this.extraCode8 = extraCode8;
	}

	public String getExtraCode9() {
		return extraCode9;
	}

	public void setExtraCode9(String extraCode9) {
		this.extraCode9 = extraCode9;
	}

	public Integer getExtraNum1() {
		return extraNum1;
	}

	public void setExtraNum1(Integer extraNum1) {
		this.extraNum1 = extraNum1;
	}

	public Integer getExtraNum2() {
		return extraNum2;
	}

	public void setExtraNum2(Integer extraNum2) {
		this.extraNum2 = extraNum2;
	}

	public Integer getExtraNum3() {
		return extraNum3;
	}

	public void setExtraNum3(Integer extraNum3) {
		this.extraNum3 = extraNum3;
	}

	public Integer getExtraNum4() {
		return extraNum4;
	}

	public void setExtraNum4(Integer extraNum4) {
		this.extraNum4 = extraNum4;
	}

	public Integer getExtraNum5() {
		return extraNum5;
	}

	public void setExtraNum5(Integer extraNum5) {
		this.extraNum5 = extraNum5;
	}

	public Integer getExtraNum6() {
		return extraNum6;
	}

	public void setExtraNum6(Integer extraNum6) {
		this.extraNum6 = extraNum6;
	}

	public Integer getExtraNum7() {
		return extraNum7;
	}

	public void setExtraNum7(Integer extraNum7) {
		this.extraNum7 = extraNum7;
	}

	public Integer getExtraNum8() {
		return extraNum8;
	}

	public void setExtraNum8(Integer extraNum8) {
		this.extraNum8 = extraNum8;
	}

	public Integer getExtraNum9() {
		return extraNum9;
	}

	public void setExtraNum9(Integer extraNum9) {
		this.extraNum9 = extraNum9;
	}

	public Integer getExtraNum10() {
		return extraNum10;
	}

	public void setExtraNum10(Integer extraNum10) {
		this.extraNum10 = extraNum10;
	}

	public Integer getExtraFlag1() {
		return extraFlag1;
	}

	public void setExtraFlag1(Integer extraFlag1) {
		this.extraFlag1 = extraFlag1;
	}

	public Integer getExtraFlag2() {
		return extraFlag2;
	}

	public void setExtraFlag2(Integer extraFlag2) {
		this.extraFlag2 = extraFlag2;
	}

	public Integer getExtraFlag3() {
		return extraFlag3;
	}

	public void setExtraFlag3(Integer extraFlag3) {
		this.extraFlag3 = extraFlag3;
	}

	public Integer getExtraFlag4() {
		return extraFlag4;
	}

	public void setExtraFlag4(Integer extraFlag4) {
		this.extraFlag4 = extraFlag4;
	}

	public Integer getExtraFlag5() {
		return extraFlag5;
	}

	public void setExtraFlag5(Integer extraFlag5) {
		this.extraFlag5 = extraFlag5;
	}

	public Integer getExtraFlag6() {
		return extraFlag6;
	}

	public void setExtraFlag6(Integer extraFlag6) {
		this.extraFlag6 = extraFlag6;
	}

	public Integer getExtraFlag7() {
		return extraFlag7;
	}

	public void setExtraFlag7(Integer extraFlag7) {
		this.extraFlag7 = extraFlag7;
	}

	public Integer getExtraFlag8() {
		return extraFlag8;
	}

	public void setExtraFlag8(Integer extraFlag8) {
		this.extraFlag8 = extraFlag8;
	}

	public Integer getExtraFlag9() {
		return extraFlag9;
	}

	public void setExtraFlag9(Integer extraFlag9) {
		this.extraFlag9 = extraFlag9;
	}

	public Integer getExtraFlag10() {
		return extraFlag10;
	}

	public void setExtraFlag10(Integer extraFlag10) {
		this.extraFlag10 = extraFlag10;
	}

	public Integer getExtraFlag11() {
		return extraFlag11;
	}

	public void setExtraFlag11(Integer extraFlag11) {
		this.extraFlag11 = extraFlag11;
	}

	public Integer getExtraFlag12() {
		return extraFlag12;
	}

	public void setExtraFlag12(Integer extraFlag12) {
		this.extraFlag12 = extraFlag12;
	}

	public Integer getExtraFlag13() {
		return extraFlag13;
	}

	public void setExtraFlag13(Integer extraFlag13) {
		this.extraFlag13 = extraFlag13;
	}

	public Integer getExtraFlag14() {
		return extraFlag14;
	}

	public void setExtraFlag14(Integer extraFlag14) {
		this.extraFlag14 = extraFlag14;
	}

	public Integer getExtraFlag15() {
		return extraFlag15;
	}

	public void setExtraFlag15(Integer extraFlag15) {
		this.extraFlag15 = extraFlag15;
	}

	public Integer getExtraFlag16() {
		return extraFlag16;
	}

	public void setExtraFlag16(Integer extraFlag16) {
		this.extraFlag16 = extraFlag16;
	}

	public Integer getExtraFlag17() {
		return extraFlag17;
	}

	public void setExtraFlag17(Integer extraFlag17) {
		this.extraFlag17 = extraFlag17;
	}

	public Integer getExtraFlag18() {
		return extraFlag18;
	}

	public void setExtraFlag18(Integer extraFlag18) {
		this.extraFlag18 = extraFlag18;
	}

	public Integer getExtraFlag19() {
		return extraFlag19;
	}

	public void setExtraFlag19(Integer extraFlag19) {
		this.extraFlag19 = extraFlag19;
	}

	public Integer getExtraFlag20() {
		return extraFlag20;
	}

	public void setExtraFlag20(Integer extraFlag20) {
		this.extraFlag20 = extraFlag20;
	}

	public String getExtraCode10() {
		return extraCode10;
	}

	public void setExtraCode10(String extraCode10) {
		this.extraCode10 = extraCode10;
	}

	
}
