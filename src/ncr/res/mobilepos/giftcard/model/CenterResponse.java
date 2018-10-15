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

import ncr.res.mobilepos.model.ResultBase;

/**
 * Toppan Giftcenter response data transfer object.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CenterResponse")
@ApiModel(value="CenterResponse")
public class CenterResponse extends ResultBase {

    @XmlElement(name = "ErrorCode")
    String errorCode;
    @XmlElement(name = "SubErrorCode")
    String subErrorCode;
    @XmlElement(name = "AuthorizedNumber")
    String authorizedNumber;
    @XmlElement(name = "PriorAmount")
    long priorAmount;
    @XmlElement(name = "CurrentAmount")
    long currentAmount;
    /** ActivationStatus: 0: not activated, 1: activated, 2: activated (first use) */
    @XmlElement(name = "ActivationStatus")
    int activationStatus;
    /** DateValidity: 0: valid, 1: expired, 2: not started */
    @XmlElement(name = "DateValidity")
    int dateValidity;
    /** CardValidity: 0: valid, 1: lost */
    @XmlElement(name = "CardValidity")
    int cardValidity;

    @ApiModelProperty(value="�G���[�R�[�h", notes="�G���[�R�[�h")
    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String val) {
        errorCode = val;
    }

    @ApiModelProperty(value="�T�u�G���[�R�[�h", notes="�T�u�G���[�R�[�h")
    public String getSubErrorCode() {
        return subErrorCode;
    }

    public void setSubErrorCode(String val) {
        subErrorCode = val;
    }

    @ApiModelProperty(value="�����i���o�[", notes="�����i���o�[")
    public String getAuthorizedNumber() {
        return authorizedNumber;
    }

    public void setAuthorizedNumber(String val) {
        authorizedNumber = val;
    }

    @ApiModelProperty(value="�v���C�I���e�B�[���z", notes="�v���C�I���e�B�[���z")
    public long getPriorAmount() {
        return priorAmount;
    }

    public void setPriorAmount(long val) {
        priorAmount = val;
    }

    @ApiModelProperty(value="�J�����g���z", notes="�J�����g���z")
    public long getCurrentAmount() {
        return currentAmount;
    }

    public void setCurrentAmount(long val) {
        currentAmount = val;
    }

    @ApiModelProperty(value="�A�N�e�B�u�X�e�[�^�X", notes="�A�N�e�B�u�X�e�[�^�X")
    public int getActivationStatus() {
        return activationStatus;
    }

    public void setActivationStatus(int val) {
        activationStatus = val;
    }

    @ApiModelProperty(value="�L�����t", notes="�L�����t")
    public int getDateValidity() {
        return dateValidity;
    }

    public void setDateValidity(int val) {
        dateValidity = val;
    }

    @ApiModelProperty(value="�L���J�[�h", notes="�L���J�[�h")
    public int getCardValidity() {
        return cardValidity;
    }

    public void setCardValidity(int val) {
        cardValidity = val;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append('(');
        sb.append("ErrorCode").append(errorCode).append(',');
        sb.append("SubErrorCode").append(subErrorCode).append(',');
        sb.append("AuthorizedNumber").append(authorizedNumber).append(',');
        sb.append("PriorAmount").append(priorAmount).append(',');
        sb.append("CurrentAmount").append(currentAmount).append(',');
        sb.append("ActivationStatus").append(activationStatus).append(',');
        sb.append("DateValidity").append(dateValidity).append(',');
        sb.append("CardValidity").append(cardValidity).append(')');
        return sb.toString();
    }
}
