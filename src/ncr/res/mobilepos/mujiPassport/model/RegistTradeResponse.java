package ncr.res.mobilepos.mujiPassport.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlRootElement(name = "Response")
@XmlAccessorType(XmlAccessType.NONE)
public class RegistTradeResponse extends ResultBase {

    @XmlElement(name = "resultCode")
    private int resultCode;

    @XmlElement(name = "couponTradeNo")
    private String couponTradeNo;

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
     * @return the couponTradeNo
     */
    public final String getCouponTradeNo() {
        return couponTradeNo;
    }

    /**
     * @param couponTradeNo the couponTradeNo to set
     */
    public final void setCouponTradeNo(String couponTradeNo) {
        this.couponTradeNo = couponTradeNo;
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
        return "RegistTradeResponse [resultCode=" + resultCode + ", "
                + (couponTradeNo != null ? "couponTradeNo=" + couponTradeNo + ", " : "")
                + (errorMessage != null ? "errorMessage=" + errorMessage : "") + "]";
    }
}
