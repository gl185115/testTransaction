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
