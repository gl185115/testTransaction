package ncr.res.mobilepos.mujiPassport.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.NONE)
public class PassportPaymentInfoResponse extends ResultBase  {

    @XmlElement(name = "resultCode")
    private int resultCode;

    @XmlElement(name = "paymentInfoCode")
    private String paymentInfoCode;

    @XmlElement(name = "prepaidPin")
    private String prepaidPin;

    @XmlElement(name = "paymentType")
    private String paymentType;

    @XmlElement(name = "errorMessage")
    private String errorMessage;

    /**
     * @return the resultCode
     */
    public final int getResultCode() {
        return resultCode;
    }

    /**
     * @param resultCode the resultCode to set
     */
    public final void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    /**
	 * @return the paymentInfoCode
	 */
	public String getPaymentInfoCode() {
		return paymentInfoCode;
	}

	/**
	 * @param paymentInfoCode the paymentInfoCode to set
	 */
	public void setPaymentInfoCode(String paymentInfoCode) {
		this.paymentInfoCode = paymentInfoCode;
	}

	/**
	 * @return the prepaidPin
	 */
	public String getPrepaidPin() {
		return prepaidPin;
	}

	/**
	 * @param prepaidPin the prepaidPin to set
	 */
	public void setPrepaidPin(String prepaidPin) {
		this.prepaidPin = prepaidPin;
	}

	/**
	 * @return the paymentType
	 */
	public String getPaymentType() {
		return paymentType;
	}

	/**
	 * @param paymentType the paymentType to set
	 */
	public void setPaymentType(String paymentType) {
		this.paymentType = paymentType;
	}

	/**
     * @return the errorMessage
     */
    public final String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public final void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    @Override
	public String toString() {
		return "PassportPaymentInfoResponse [resultCode=" + resultCode + ", paymentInfoCode=" + paymentInfoCode
				+ ", prepaidPin=" + prepaidPin + ", paymentType=" + paymentType + ", errorMessage=" + errorMessage
				+ "]";
	}
}
