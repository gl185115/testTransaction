package ncr.res.mobilepos.deviceinfo.model;

import ncr.res.mobilepos.model.ResultBase;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Device Attribute Info.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "AttributeInfo")
public class AttributeInfo extends ResultBase {
	
	@XmlElement(name = "AttributeId")
    private String attributeId;
	
	@XmlElement(name = "Printer")
	private String printer;
	
	@XmlElement(name = "Till")
	private String till;
	
	@XmlElement(name = "CreditTerminal")
	private String creditTerminal;
	
	@XmlElement(name = "MSR")
	private String MSR;
	
	@XmlElement(name = "CashChanger")
	private String cashChanger;
	
	@XmlElement(name = "Attribute1")
	private String attribute1;
	
	@XmlElement(name = "Attribute2")
	private String attribute2;
	
	@XmlElement(name = "Attribute3")
	private String attribute3;
	
	@XmlElement(name = "Attribute4")
	private String attribute4;
	
	@XmlElement(name = "Attribute5")
	private String attribute5;
	
	@XmlElement(name = "Attribute6")
	private String attribute6;
	
	@XmlElement(name = "Attribute7")
	private String attribute7;
	
	@XmlElement(name = "Attribute8", nillable = true)
	private String attribute8;
	
	@XmlElement(name = "Attribute9", nillable = true)
	private String attribute9;
	
	@XmlElement(name = "Attribute10", nillable = true)
	private String attribute10;
	
	@XmlElement(name = "Attribute11", nillable = true)
	private String attribute11;
	
	@XmlElement(name = "Attribute12", nillable = true)
	private String attribute12;
	
	@XmlElement(name = "Attribute13", nillable = true)
	private String attribute13;
	
	@XmlElement(name = "Attribute14", nillable = true)
	private String attribute14;
	
	@XmlElement(name = "Attribute15", nillable = true)
	private String attribute15;
	
	@XmlElement(name = "Attribute16", nillable = true)
	private String attribute16;
	
	@XmlElement(name = "Attribute17", nillable = true)
	private String attribute17;
	
	@XmlElement(name = "Attribute18", nillable = true)
	private String attribute18;
	
	@XmlElement(name = "Attribute19", nillable = true)
	private String attribute19;
	
	@XmlElement(name = "Attribute20", nillable = true)
	private String attribute20;
	
	@XmlElement(name = "Attribute21", nillable = true)
	private String attribute21;
	
	@XmlElement(name = "Attribute22", nillable = true)
	private String attribute22;
	
	@XmlElement(name = "Attribute23", nillable = true)
	private String attribute23;
	
	@XmlElement(name = "Attribute24", nillable = true)
	private String attribute24;
	
	@XmlElement(name = "Attribute25", nillable = true)
	private String attribute25;
	
	@XmlElement(name = "Attribute26", nillable = true)
	private String attribute26;
	
	@XmlElement(name = "Attribute27", nillable = true)
	private String attribute27;
	
	@XmlElement(name = "Attribute28", nillable = true)
	private String attribute28;
	
	@XmlElement(name = "Attribute29", nillable = true)
	private String attribute29;
	
	@XmlElement(name = "Attribute30", nillable = true)
	private String attribute30;
	
	@XmlElement(name = "Training")
    private Integer training;

	/**
	 * @return the attributeId
	 */
	public final String getAttributeId() {
		return attributeId;
	}

	/**
	 * @param attributeId the attributeId to set
	 */
	public final void setAttributeId(String attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * @return the printer
	 */
	public final String getPrinter() {
		return printer;
	}

	/**
	 * @param printer the printer to set
	 */
	public final void setPrinter(String printer) {
		this.printer = printer;
	}

	/**
	 * @return the till
	 */
	public final String getTill() {
		return till;
	}

	/**
	 * @param till the till to set
	 */
	public final void setTill(String till) {
		this.till = till;
	}

	/**
	 * @return the creditTerminal
	 */
	public final String getCreditTerminal() {
		return creditTerminal;
	}

	/**
	 * @param creditTerminal the creditTerminal to set
	 */
	public final void setCreditTerminal(String creditTerminal) {
		this.creditTerminal = creditTerminal;
	}

	/**
	 * @return the mSR
	 */
	public final String getMSR() {
		return MSR;
	}

	/**
	 * @param mSR the mSR to set
	 */
	public final void setMSR(String mSR) {
		MSR = mSR;
	}

	/**
	 * @return the cashChanger
	 */
	public final String getCashChanger() {
		return cashChanger;
	}

	/**
	 * @param cashChanger the cashChanger to set
	 */
	public final void setCashChanger(String cashChanger) {
		this.cashChanger = cashChanger;
	}

	/**
	 * @return the attribute1
	 */
	public final String getAttribute1() {
		return attribute1;
	}

	/**
	 * @param attribute1 the attribute1 to set
	 */
	public final void setAttribute1(String attribute1) {
		this.attribute1 = attribute1;
	}

	/**
	 * @return the attribute2
	 */
	public final String getAttribute2() {
		return attribute2;
	}

	/**
	 * @param attribute2 the attribute2 to set
	 */
	public final void setAttribute2(String attribute2) {
		this.attribute2 = attribute2;
	}

	/**
	 * @return the attribute3
	 */
	public final String getAttribute3() {
		return attribute3;
	}

	/**
	 * @param attribute3 the attribute3 to set
	 */
	public final void setAttribute3(String attribute3) {
		this.attribute3 = attribute3;
	}

	/**
	 * @return the attribute4
	 */
	public final String getAttribute4() {
		return attribute4;
	}

	/**
	 * @param attribute4 the attribute4 to set
	 */
	public final void setAttribute4(String attribute4) {
		this.attribute4 = attribute4;
	}

	/**
	 * @return the attribute5
	 */
	public final String getAttribute5() {
		return attribute5;
	}

	/**
	 * @param attribute5 the attribute5 to set
	 */
	public final void setAttribute5(String attribute5) {
		this.attribute5 = attribute5;
	}

	/**
	 * @return the attribute6
	 */
	public final String getAttribute6() {
		return attribute6;
	}

	/**
	 * @param attribute6 the attribute6 to set
	 */
	public final void setAttribute6(String attribute6) {
		this.attribute6 = attribute6;
	}

	/**
	 * @return the attribute7
	 */
	public final String getAttribute7() {
		return attribute7;
	}

	/**
	 * @param attribute7 the attribute7 to set
	 */
	public final void setAttribute7(String attribute7) {
		this.attribute7 = attribute7;
	}

	/**
	 * @return the attribute8
	 */
	public final String getAttribute8() {
		return attribute8;
	}

	/**
	 * @param attribute8 the attribute8 to set
	 */
	public final void setAttribute8(String attribute8) {
		this.attribute8 = attribute8;
	}

	/**
	 * @return the attribute9
	 */
	public final String getAttribute9() {
		return attribute9;
	}

	/**
	 * @param attribute9 the attribute9 to set
	 */
	public final void setAttribute9(String attribute9) {
		this.attribute9 = attribute9;
	}

	/**
	 * @return the attribute10
	 */
	public final String getAttribute10() {
		return attribute10;
	}

	/**
	 * @param attribute10 the attribute10 to set
	 */
	public final void setAttribute10(String attribute10) {
		this.attribute10 = attribute10;
	}

	/**
	 * @return the attribute11
	 */
	public final String getAttribute11() {
		return attribute11;
	}

	/**
	 * @param attribute11 the attribute11 to set
	 */
	public final void setAttribute11(String attribute11) {
		this.attribute11 = attribute11;
	}

	/**
	 * @return the attribute12
	 */
	public final String getAttribute12() {
		return attribute12;
	}

	/**
	 * @param attribute12 the attribute12 to set
	 */
	public final void setAttribute12(String attribute12) {
		this.attribute12 = attribute12;
	}

	/**
	 * @return the attribute13
	 */
	public final String getAttribute13() {
		return attribute13;
	}

	/**
	 * @param attribute13 the attribute13 to set
	 */
	public final void setAttribute13(String attribute13) {
		this.attribute13 = attribute13;
	}

	/**
	 * @return the attribute14
	 */
	public final String getAttribute14() {
		return attribute14;
	}

	/**
	 * @param attribute14 the attribute14 to set
	 */
	public final void setAttribute14(String attribute14) {
		this.attribute14 = attribute14;
	}

	/**
	 * @return the attribute15
	 */
	public final String getAttribute15() {
		return attribute15;
	}

	/**
	 * @param attribute15 the attribute15 to set
	 */
	public final void setAttribute15(String attribute15) {
		this.attribute15 = attribute15;
	}

	/**
	 * @return the attribute16
	 */
	public final String getAttribute16() {
		return attribute16;
	}

	/**
	 * @param attribute16 the attribute16 to set
	 */
	public final void setAttribute16(String attribute16) {
		this.attribute16 = attribute16;
	}

	/**
	 * @return the attribute17
	 */
	public final String getAttribute17() {
		return attribute17;
	}

	/**
	 * @param attribute17 the attribute17 to set
	 */
	public final void setAttribute17(String attribute17) {
		this.attribute17 = attribute17;
	}

	/**
	 * @return the attribute18
	 */
	public final String getAttribute18() {
		return attribute18;
	}

	/**
	 * @param attribute18 the attribute18 to set
	 */
	public final void setAttribute18(String attribute18) {
		this.attribute18 = attribute18;
	}

	/**
	 * @return the attribute19
	 */
	public final String getAttribute19() {
		return attribute19;
	}

	/**
	 * @param attribute19 the attribute19 to set
	 */
	public final void setAttribute19(String attribute19) {
		this.attribute19 = attribute19;
	}

	/**
	 * @return the attribute20
	 */
	public final String getAttribute20() {
		return attribute20;
	}

	/**
	 * @param attribute20 the attribute20 to set
	 */
	public final void setAttribute20(String attribute20) {
		this.attribute20 = attribute20;
	}

	/**
	 * @return the attribute21
	 */
	public final String getAttribute21() {
		return attribute21;
	}

	/**
	 * @param attribute21 the attribute21 to set
	 */
	public final void setAttribute21(String attribute21) {
		this.attribute21 = attribute21;
	}

	/**
	 * @return the attribute22
	 */
	public final String getAttribute22() {
		return attribute22;
	}

	/**
	 * @param attribute22 the attribute22 to set
	 */
	public final void setAttribute22(String attribute22) {
		this.attribute22 = attribute22;
	}

	/**
	 * @return the attribute23
	 */
	public final String getAttribute23() {
		return attribute23;
	}

	/**
	 * @param attribute23 the attribute23 to set
	 */
	public final void setAttribute23(String attribute23) {
		this.attribute23 = attribute23;
	}

	/**
	 * @return the attribute24
	 */
	public final String getAttribute24() {
		return attribute24;
	}

	/**
	 * @param attribute24 the attribute24 to set
	 */
	public final void setAttribute24(String attribute24) {
		this.attribute24 = attribute24;
	}

	/**
	 * @return the attribute25
	 */
	public final String getAttribute25() {
		return attribute25;
	}

	/**
	 * @param attribute25 the attribute25 to set
	 */
	public final void setAttribute25(String attribute25) {
		this.attribute25 = attribute25;
	}

	/**
	 * @return the attribute26
	 */
	public final String getAttribute26() {
		return attribute26;
	}

	/**
	 * @param attribute26 the attribute26 to set
	 */
	public final void setAttribute26(String attribute26) {
		this.attribute26 = attribute26;
	}

	/**
	 * @return the attribute27
	 */
	public final String getAttribute27() {
		return attribute27;
	}

	/**
	 * @param attribute27 the attribute27 to set
	 */
	public final void setAttribute27(String attribute27) {
		this.attribute27 = attribute27;
	}

	/**
	 * @return the attribute28
	 */
	public final String getAttribute28() {
		return attribute28;
	}

	/**
	 * @param attribute28 the attribute28 to set
	 */
	public final void setAttribute28(String attribute28) {
		this.attribute28 = attribute28;
	}

	/**
	 * @return the attribute29
	 */
	public final String getAttribute29() {
		return attribute29;
	}

	/**
	 * @param attribute29 the attribute29 to set
	 */
	public final void setAttribute29(String attribute29) {
		this.attribute29 = attribute29;
	}

	/**
	 * @return the attribute30
	 */
	public final String getAttribute30() {
		return attribute30;
	}

	/**
	 * @param attribute30 the attribute30 to set
	 */
	public final void setAttribute30(String attribute30) {
		this.attribute30 = attribute30;
	}
	
	/**
     * @return the training mode
     */
    public final int getTrainingMode() {
        return training;
    }

    /**
     * @param trainingMode the training mode to set
     */
    public final void setTrainingMode(int trainingMode) {
        this.training = trainingMode;
    }

	@Override
	public String toString() {
		return "AttributeInfo [attributeId=" + attributeId + ", printer="
				+ printer + ", till=" + till + ", creditTerminal="
				+ creditTerminal + ", MSR=" + MSR + ", cashChanger="
				+ cashChanger + ", attribute1=" + attribute1 + ", attribute2="
				+ attribute2 + ", attribute3=" + attribute3 + ", attribute4="
				+ attribute4 + ", attribute5=" + attribute5 + ", attribute6="
				+ attribute6 + ", attribute7=" + attribute7 + ", attribute8="
				+ attribute8 + ", attribute9=" + attribute9 + ", attribute10="
				+ attribute10 + "training" + "]";
	}

}
