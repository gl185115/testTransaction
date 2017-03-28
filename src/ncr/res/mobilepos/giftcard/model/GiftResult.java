/**
 * Copyright (c) 2015 NCR Japan Ltd. 
 */
package ncr.res.mobilepos.giftcard.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

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
     * setup result by the response message.
     * @param resp server response
     */
    public void transfer(Message resp) {
        if (resp.getPriorAmount().trim().length() > 0) {
            priorAmount = Long.parseLong(resp.getPriorAmount());
        }
        if (resp.getCurrentAmount().trim().length() > 0) {
            currentAmount = Long.parseLong(resp.getCurrentAmount());
        }
        errorCode = resp.getErrorCode();
        subErrorCode = resp.getSubErrorCode();
        authorizationNumber = resp.getAuthNumber();
        expirationDate = resp.getExpiration();
        String status = resp.getCardStatus();
        assert(status.length() == 4);
        activationStatus = status.charAt(0) - '0';
        expirationStatus = status.charAt(1) - '0';
        lostStatus = status.charAt(2) - '0';
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
