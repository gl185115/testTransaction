package ncr.res.mobilepos.mujiPassport.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.NONE)
public class PayPassportResponse extends ResultBase {
    @XmlElement(name = "resultCode")
    private int resultCode;
    
    @XmlElement(name = "errorMessage")
    private String errorMessage;

    @XmlElement(name = "paymentType")
    private String paymentType;
    
    @XmlElement(name = "paymentStatus")
    private int paymentStatus;
    
    @XmlElement(name = "paymentCode")
    private String paymentCode;
    
    @XmlElement(name = "nativeResponse")
    private List<NativeResponse> nativeResponse;

    /**
     * @return the resultCode
     */
    public int getResultCode() {
        return resultCode;
    }

    /**
     * @param resultCode the resultCode to set
     */
    public void setResultCode(int resultCode) {
        this.resultCode = resultCode;
    }

    /**
     * @return the errorMessage
     */
    public String getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param errorMessage the errorMessage to set
     */
    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
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
     * @return the paymentStatus
     */
    public int getPaymentStatus() {
        return paymentStatus;
    }

    /**
     * @param paymentStatus the paymentStatus to set
     */
    public void setPaymentStatus(int paymentStatus) {
        this.paymentStatus = paymentStatus;
    }

    /**
     * @return the paymentCode
     */
    public String getPaymentCode() {
        return paymentCode;
    }

    /**
     * @param paymentCode the paymentCode to set
     */
    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }

    /**
     * @return the nativeResponse
     */
    public List<NativeResponse> getNativeResponse() {
        return nativeResponse;
    }

    /**
     * @param nativeResponse the nativeResponse to set
     */
    public void setNativeResponse(List<NativeResponse> nativeResponse) {
        this.nativeResponse = nativeResponse;
    }

    @Override
    public String toString() {
        return "PayPassportResponse [resultCode=" + resultCode + ", errorMessage=" + errorMessage + ", paymentType="
                + paymentType + ", paymentStatus=" + paymentStatus + ", paymentCode=" + paymentCode
                + ", nativeResponse=" + nativeResponse + "]";
    }
    
}
