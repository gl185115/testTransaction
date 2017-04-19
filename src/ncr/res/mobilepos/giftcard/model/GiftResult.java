/**
 * Copyright (c) 2015 NCR Japan Ltd. 
 */
package ncr.res.mobilepos.giftcard.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.giftcard.toppan.model.Message;
import ncr.res.mobilepos.model.ResultBase;

/**
 * Gift (Toppan) center response object.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GiftResult")
@ApiModel(value="GiftResult")
public class GiftResult extends ResultBase {
    @XmlElement(name = "PriorAmount")
    long priorAmount;
    @XmlElement(name = "CurrentAmount")
    long currentAmount;
    @XmlElement(name = "ErrorCode")
    String errorCode;
    @XmlElement(name = "SubErrorCode")
    String subErrorCode;
    @XmlElement(name = "AuthorizationNumber")
    String authorizationNumber;
    @XmlElement(name = "ExpirationDate")
    String expirationDate;
    @XmlElement(name = "ActivationStatus")
    int activationStatus;
    @XmlElement(name = "ExpirationStatus")
    int expirationStatus;
    @XmlElement(name = "LostStatus")
    int lostStatus;

    /**
     * Default success instance.
     */
    public GiftResult() {
    }

    /**
     * failure instance.
     */
    public GiftResult(int errorCode) {
        super(errorCode);
    }

    /**
     * @return priorAmount
     */
    @ApiModelProperty(value="PriorAmount", notes="PriorAmount")
    public long getPriorAmount() {
        return priorAmount;
    }
    
    /**
     * @param priorAmount セットする priorAmount
     */
    public void setPriorAmount(long priorAmount) {
        this.priorAmount = priorAmount;
    }
    
    /**
     * @return currentAmount
     */
    @ApiModelProperty(value="CurrentAmount", notes="CurrentAmount")
    public long getCurrentAmount() {
        return currentAmount;
    }
    
    /**
     * @param currentAmount セットする currentAmount
     */
    public void setCurrentAmount(long currentAmount) {
        this.currentAmount = currentAmount;
    }
    
    /**
     * @return errorCode
     */
    @ApiModelProperty(value="ErrorCode", notes="ErrorCode")
    public String getErrorCode() {
        return errorCode;
    }
    
    /**
     * @param errorCode セットする errorCode
     */
    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
    
    /**
     * @return subErrorCode
     */
    @ApiModelProperty(value="SubErrorCode", notes="SubErrorCode")
    public String getSubErrorCode() {
        return subErrorCode;
    }
    
    /**
     * @param subErrorCode セットする subErrorCode
     */
    public void setSubErrorCode(String subErrorCode) {
        this.subErrorCode = subErrorCode;
    }
    
    /**
     * @return authorizationNumber
     */
    @ApiModelProperty(value="AuthorizationNumber", notes="AuthorizationNumber")
    public String getAuthorizationNumber() {
        return authorizationNumber;
    }
    
    /**
     * @param authorizationNumber セットする authorizationNumber
     */
    public void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }
    
    /**
     * @return expirationDate
     */
    @ApiModelProperty(value="ExpirationDate", notes="ExpirationDate")
    public String getExpirationDate() {
        return expirationDate;
    }
    
    /**
     * @param expirationDate セットする expirationDate
     */
    public void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }
    
    /**
     * @return activationStatus
     */
    @ApiModelProperty(value="ActivationStatus", notes="ActivationStatus")
    public int getActivationStatus() {
        return activationStatus;
    }
    
    /**
     * @param activationStatus セットする activationStatus
     */
    public void setActivationStatus(int activationStatus) {
        this.activationStatus = activationStatus;
    }
    
    /**
     * @return expirationStatus
     */
    @ApiModelProperty(value="ExpirationStatus", notes="ExpirationStatus")
    public int getExpirationStatus() {
        return expirationStatus;
    }
    
    /**
     * @param expirationStatus セットする expirationStatus
     */
    public void setExpirationStatus(int expirationStatus) {
        this.expirationStatus = expirationStatus;
    }
    
    /**
     * @return lostStatus
     */
    @ApiModelProperty(value="LostStatus", notes="LostStatus")
    public int getLostStatus() {
        return lostStatus;
    }
    
    /**
     * @param lostStatus セットする lostStatus
     */
    public void setLostStatus(int lostStatus) {
        this.lostStatus = lostStatus;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder(super.toString());
        sb.append('(');
        sb.append("priorAmount:").append(priorAmount).append(',');
        sb.append("currentAmount:").append(currentAmount).append(',');
        sb.append("errorCode:").append(errorCode).append(',');
        sb.append("subErrorCode:").append(subErrorCode).append(',');
        sb.append("authorizationNumber:").append(authorizationNumber).append(',');
        sb.append("expirationDate:").append(expirationDate).append(',');
        sb.append("activationStatus:").append(activationStatus).append(',');
        sb.append("expirationStatus:").append(expirationStatus).append(',');
        sb.append("lostStatus:").append(lostStatus);
        sb.append(')');
        return sb.toString();
    }
}
