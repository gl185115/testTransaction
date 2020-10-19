package ncr.res.mobilepos.mujiPassport.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlRootElement(name = "nativeResponse")
@XmlAccessorType(XmlAccessType.NONE)
public class NativeResponse {
    @XmlElement(name = "serviceType")
    private String serviceType;
    
    @XmlElement(name = "mstatus")
    private String mstatus;
    
    @XmlElement(name = "vResultCode")
    private String vResultCode;
    
    @XmlElement(name = "merrMsg")
    private String merrMsg;
    
    @XmlElement(name = "marchTxn")
    private String marchTxn;
    
    @XmlElement(name = "orderId")
    private String orderId;
    
    @XmlElement(name = "custTxn")
    private String custTxn;
    
    @XmlElement(name = "txnVersion")
    private String txnVersion;
    
    @XmlElement(name = "cardTransactiontype")
    private String cardTransactiontype;

    @XmlElement(name = "gatewayRequestDate")
    private String gatewayRequestDate;

    @XmlElement(name = "gatewayResponseDate")
    private String gatewayResponseDate;

    @XmlElement(name = "centerRequestDate")
    private String centerRequestDate;

    @XmlElement(name = "centerResponseDate")
    private String centerResponseDate;

    @XmlElement(name = "pending")
    private String pending;

    @XmlElement(name = "loopback")
    private String loopback;

    @XmlElement(name = "connectedCenterId")
    private String connectedCenterId;

    @XmlElement(name = "centerRequestNumber")
    private String centerRequestNumber;

    @XmlElement(name = "centerReferenceNumber")
    private String centerReferenceNumber;

    @XmlElement(name = "reqCardNumber")
    private String reqCardNumber;

    @XmlElement(name = "reqCardExpire")
    private String reqCardExpire;

    @XmlElement(name = "reqCardOptionType")
    private String reqCardOptionType;

    @XmlElement(name = "reqAmount")
    private String reqAmount;

    @XmlElement(name = "reqAcquirerCode")
    private String reqAcquirerCode;

    @XmlElement(name = "reqJpoInformation")
    private String reqJpoInformation;

    @XmlElement(name = "reqWithCapture")
    private String reqWithCapture;

    @XmlElement(name = "req3dMessageVersion")
    private String req3dMessageVersion;

    @XmlElement(name = "req3dTransactionId")
    private String req3dTransactionId;

    @XmlElement(name = "req3dTransactionStatus")
    private String req3dTransactionStatus;

    @XmlElement(name = "req3dCavvAlgorithm")
    private String req3dCavvAlgorithm;

    @XmlElement(name = "req3dCavv")
    private String req3dCavv;

    @XmlElement(name = "req3dEci")
    private String req3dEci;

    @XmlElement(name = "reqSecurityCode")
    private String reqSecurityCode;

    @XmlElement(name = "resReturnReferenceNumber")
    private String resReturnReferenceNumber;

    @XmlElement(name = "resAuthCode")
    private String resAuthCode;

    @XmlElement(name = "resActionCode")
    private String resActionCode;

    @XmlElement(name = "resCenterErrorCode")
    private String resCenterErrorCode;

    @XmlElement(name = "acquirerCode")
    private String acquirerCode;

    /**
     * @return the serviceType
     */
    public String getServiceType() {
        return serviceType;
    }

    /**
     * @param serviceType the serviceType to set
     */
    public void setServiceType(String serviceType) {
        this.serviceType = serviceType;
    }

    /**
     * @return the mstatus
     */
    public String getMstatus() {
        return mstatus;
    }

    /**
     * @param mstatus the mstatus to set
     */
    public void setMstatus(String mstatus) {
        this.mstatus = mstatus;
    }

    /**
     * @return the vResultCode
     */
    public String getvResultCode() {
        return vResultCode;
    }

    /**
     * @param vResultCode the vResultCode to set
     */
    public void setvResultCode(String vResultCode) {
        this.vResultCode = vResultCode;
    }

    /**
     * @return the merrMsg
     */
    public String getMerrMsg() {
        return merrMsg;
    }

    /**
     * @param merrMsg the merrMsg to set
     */
    public void setMerrMsg(String merrMsg) {
        this.merrMsg = merrMsg;
    }

    /**
     * @return the marchTxn
     */
    public String getMarchTxn() {
        return marchTxn;
    }

    /**
     * @param marchTxn the marchTxn to set
     */
    public void setMarchTxn(String marchTxn) {
        this.marchTxn = marchTxn;
    }

    /**
     * @return the orderId
     */
    public String getOrderId() {
        return orderId;
    }

    /**
     * @param orderId the orderId to set
     */
    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    /**
     * @return the custTxn
     */
    public String getCustTxn() {
        return custTxn;
    }

    /**
     * @param custTxn the custTxn to set
     */
    public void setCustTxn(String custTxn) {
        this.custTxn = custTxn;
    }

    /**
     * @return the txnVersion
     */
    public String getTxnVersion() {
        return txnVersion;
    }

    /**
     * @param txnVersion the txnVersion to set
     */
    public void setTxnVersion(String txnVersion) {
        this.txnVersion = txnVersion;
    }

    /**
     * @return the cardTransactiontype
     */
    public String getCardTransactiontype() {
        return cardTransactiontype;
    }

    /**
     * @param cardTransactiontype the cardTransactiontype to set
     */
    public void setCardTransactiontype(String cardTransactiontype) {
        this.cardTransactiontype = cardTransactiontype;
    }

    /**
     * @return the gatewayRequestDate
     */
    public String getGatewayRequestDate() {
        return gatewayRequestDate;
    }

    /**
     * @param gatewayRequestDate the gatewayRequestDate to set
     */
    public void setGatewayRequestDate(String gatewayRequestDate) {
        this.gatewayRequestDate = gatewayRequestDate;
    }

    /**
     * @return the gatewayResponseDate
     */
    public String getGatewayResponseDate() {
        return gatewayResponseDate;
    }

    /**
     * @param gatewayResponseDate the gatewayResponseDate to set
     */
    public void setGatewayResponseDate(String gatewayResponseDate) {
        this.gatewayResponseDate = gatewayResponseDate;
    }

    /**
     * @return the centerRequestDate
     */
    public String getCenterRequestDate() {
        return centerRequestDate;
    }

    /**
     * @param centerRequestDate the centerRequestDate to set
     */
    public void setCenterRequestDate(String centerRequestDate) {
        this.centerRequestDate = centerRequestDate;
    }

    /**
     * @return the centerResponseDate
     */
    public String getCenterResponseDate() {
        return centerResponseDate;
    }

    /**
     * @param centerResponseDate the centerResponseDate to set
     */
    public void setCenterResponseDate(String centerResponseDate) {
        this.centerResponseDate = centerResponseDate;
    }

    /**
     * @return the pending
     */
    public String getPending() {
        return pending;
    }

    /**
     * @param pending the pending to set
     */
    public void setPending(String pending) {
        this.pending = pending;
    }

    /**
     * @return the loopback
     */
    public String getLoopback() {
        return loopback;
    }

    /**
     * @param loopback the loopback to set
     */
    public void setLoopback(String loopback) {
        this.loopback = loopback;
    }

    /**
     * @return the connectedCenterId
     */
    public String getConnectedCenterId() {
        return connectedCenterId;
    }

    /**
     * @param connectedCenterId the connectedCenterId to set
     */
    public void setConnectedCenterId(String connectedCenterId) {
        this.connectedCenterId = connectedCenterId;
    }

    /**
     * @return the centerRequestNumber
     */
    public String getCenterRequestNumber() {
        return centerRequestNumber;
    }

    /**
     * @param centerRequestNumber the centerRequestNumber to set
     */
    public void setCenterRequestNumber(String centerRequestNumber) {
        this.centerRequestNumber = centerRequestNumber;
    }

    /**
     * @return the centerReferenceNumber
     */
    public String getCenterReferenceNumber() {
        return centerReferenceNumber;
    }

    /**
     * @param centerReferenceNumber the centerReferenceNumber to set
     */
    public void setCenterReferenceNumber(String centerReferenceNumber) {
        this.centerReferenceNumber = centerReferenceNumber;
    }

    /**
     * @return the reqCardNumber
     */
    public String getReqCardNumber() {
        return reqCardNumber;
    }

    /**
     * @param reqCardNumber the reqCardNumber to set
     */
    public void setReqCardNumber(String reqCardNumber) {
        this.reqCardNumber = reqCardNumber;
    }

    /**
     * @return the reqCardExpire
     */
    public String getReqCardExpire() {
        return reqCardExpire;
    }

    /**
     * @param reqCardExpire the reqCardExpire to set
     */
    public void setReqCardExpire(String reqCardExpire) {
        this.reqCardExpire = reqCardExpire;
    }

    /**
     * @return the reqCardOptionType
     */
    public String getReqCardOptionType() {
        return reqCardOptionType;
    }

    /**
     * @param reqCardOptionType the reqCardOptionType to set
     */
    public void setReqCardOptionType(String reqCardOptionType) {
        this.reqCardOptionType = reqCardOptionType;
    }

    /**
     * @return the reqAmount
     */
    public String getReqAmount() {
        return reqAmount;
    }

    /**
     * @param reqAmount the reqAmount to set
     */
    public void setReqAmount(String reqAmount) {
        this.reqAmount = reqAmount;
    }

    /**
     * @return the reqAcquirerCode
     */
    public String getReqAcquirerCode() {
        return reqAcquirerCode;
    }

    /**
     * @param reqAcquirerCode the reqAcquirerCode to set
     */
    public void setReqAcquirerCode(String reqAcquirerCode) {
        this.reqAcquirerCode = reqAcquirerCode;
    }

    /**
     * @return the reqJpoInformation
     */
    public String getReqJpoInformation() {
        return reqJpoInformation;
    }

    /**
     * @param reqJpoInformation the reqJpoInformation to set
     */
    public void setReqJpoInformation(String reqJpoInformation) {
        this.reqJpoInformation = reqJpoInformation;
    }

    /**
     * @return the reqWithCapture
     */
    public String getReqWithCapture() {
        return reqWithCapture;
    }

    /**
     * @param reqWithCapture the reqWithCapture to set
     */
    public void setReqWithCapture(String reqWithCapture) {
        this.reqWithCapture = reqWithCapture;
    }

    /**
     * @return the req3dMessageVersion
     */
    public String getReq3dMessageVersion() {
        return req3dMessageVersion;
    }

    /**
     * @param req3dMessageVersion the req3dMessageVersion to set
     */
    public void setReq3dMessageVersion(String req3dMessageVersion) {
        this.req3dMessageVersion = req3dMessageVersion;
    }

    /**
     * @return the req3dTransactionId
     */
    public String getReq3dTransactionId() {
        return req3dTransactionId;
    }

    /**
     * @param req3dTransactionId the req3dTransactionId to set
     */
    public void setReq3dTransactionId(String req3dTransactionId) {
        this.req3dTransactionId = req3dTransactionId;
    }

    /**
     * @return the req3dTransactionStatus
     */
    public String getReq3dTransactionStatus() {
        return req3dTransactionStatus;
    }

    /**
     * @param req3dTransactionStatus the req3dTransactionStatus to set
     */
    public void setReq3dTransactionStatus(String req3dTransactionStatus) {
        this.req3dTransactionStatus = req3dTransactionStatus;
    }

    /**
     * @return the req3dCavvAlgorithm
     */
    public String getReq3dCavvAlgorithm() {
        return req3dCavvAlgorithm;
    }

    /**
     * @param req3dCavvAlgorithm the req3dCavvAlgorithm to set
     */
    public void setReq3dCavvAlgorithm(String req3dCavvAlgorithm) {
        this.req3dCavvAlgorithm = req3dCavvAlgorithm;
    }

    /**
     * @return the req3dCavv
     */
    public String getReq3dCavv() {
        return req3dCavv;
    }

    /**
     * @param req3dCavv the req3dCavv to set
     */
    public void setReq3dCavv(String req3dCavv) {
        this.req3dCavv = req3dCavv;
    }

    /**
     * @return the req3dEci
     */
    public String getReq3dEci() {
        return req3dEci;
    }

    /**
     * @param req3dEci the req3dEci to set
     */
    public void setReq3dEci(String req3dEci) {
        this.req3dEci = req3dEci;
    }

    /**
     * @return the reqSecurityCode
     */
    public String getReqSecurityCode() {
        return reqSecurityCode;
    }

    /**
     * @param reqSecurityCode the reqSecurityCode to set
     */
    public void setReqSecurityCode(String reqSecurityCode) {
        this.reqSecurityCode = reqSecurityCode;
    }

    /**
     * @return the resReturnReferenceNumber
     */
    public String getResReturnReferenceNumber() {
        return resReturnReferenceNumber;
    }

    /**
     * @param resReturnReferenceNumber the resReturnReferenceNumber to set
     */
    public void setResReturnReferenceNumber(String resReturnReferenceNumber) {
        this.resReturnReferenceNumber = resReturnReferenceNumber;
    }

    /**
     * @return the resAuthCode
     */
    public String getResAuthCode() {
        return resAuthCode;
    }

    /**
     * @param resAuthCode the resAuthCode to set
     */
    public void setResAuthCode(String resAuthCode) {
        this.resAuthCode = resAuthCode;
    }

    /**
     * @return the resActionCode
     */
    public String getResActionCode() {
        return resActionCode;
    }

    /**
     * @param resActionCode the resActionCode to set
     */
    public void setResActionCode(String resActionCode) {
        this.resActionCode = resActionCode;
    }

    /**
     * @return the resCenterErrorCode
     */
    public String getResCenterErrorCode() {
        return resCenterErrorCode;
    }

    /**
     * @param resCenterErrorCode the resCenterErrorCode to set
     */
    public void setResCenterErrorCode(String resCenterErrorCode) {
        this.resCenterErrorCode = resCenterErrorCode;
    }

    /**
     * @return the acquirerCode
     */
    public String getAcquirerCode() {
        return acquirerCode;
    }

    /**
     * @param acquirerCode the acquirerCode to set
     */
    public void setAcquirerCode(String acquirerCode) {
        this.acquirerCode = acquirerCode;
    }

    @Override
    public String toString() {
        return "NativeResponse [serviceType=" + serviceType + ", mstatus=" + mstatus + ", vResultCode=" + vResultCode
                + ", merrMsg=" + merrMsg + ", marchTxn=" + marchTxn + ", orderId=" + orderId + ", custTxn=" + custTxn
                + ", txnVersion=" + txnVersion + ", cardTransactiontype=" + cardTransactiontype
                + ", gatewayRequestDate=" + gatewayRequestDate + ", gatewayResponseDate=" + gatewayResponseDate
                + ", centerRequestDate=" + centerRequestDate + ", centerResponseDate=" + centerResponseDate
                + ", pending=" + pending + ", loopback=" + loopback + ", connectedCenterId=" + connectedCenterId
                + ", centerRequestNumber=" + centerRequestNumber + ", centerReferenceNumber=" + centerReferenceNumber
                + ", reqCardNumber=" + reqCardNumber + ", reqCardExpire=" + reqCardExpire + ", reqCardOptionType="
                + reqCardOptionType + ", reqAmount=" + reqAmount + ", reqAcquirerCode=" + reqAcquirerCode
                + ", reqJpoInformation=" + reqJpoInformation + ", reqWithCapture=" + reqWithCapture
                + ", req3dMessageVersion=" + req3dMessageVersion + ", req3dTransactionId=" + req3dTransactionId
                + ", req3dTransactionStatus=" + req3dTransactionStatus + ", req3dCavvAlgorithm=" + req3dCavvAlgorithm
                + ", req3dCavv=" + req3dCavv + ", req3dEci=" + req3dEci + ", reqSecurityCode=" + reqSecurityCode
                + ", resReturnReferenceNumber=" + resReturnReferenceNumber + ", resAuthCode=" + resAuthCode
                + ", resActionCode=" + resActionCode + ", resCenterErrorCode=" + resCenterErrorCode + ", acquirerCode="
                + acquirerCode + "]";
    }

}
