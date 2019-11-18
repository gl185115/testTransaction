package ncr.res.mobilepos.poslogstatus.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

/**
 * UnsendTransaction Model Object.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "UnsendTransaction")
@ApiModel(value = "UnsendTransaction")
public class UnsendTransaction {
	/**
	 * BusinessDayDate.
	 */
	@XmlElement(name = "BusinessDayDate")
	private String businessDayDate;
	/**
	 * SequenceNumber.
	 */
	@XmlElement(name = "SequenceNumber")
	private String sequenceNumber;
	/**
	 * SendStatus.
	 */
	@XmlElement(name = "SendStatus")
	private String sendStatus;

    public String getBusinessDayDate() {
        return businessDayDate;
    }

    public void setBusinessDayDate(String businessDayDate) {
        this.businessDayDate = businessDayDate;
    }

    public String getSequenceNumber() {
        return sequenceNumber;
    }

    public void setSequenceNumber(String sequenceNumber) {
        this.sequenceNumber = sequenceNumber;
    }

    public String getSendStatus() {
        return sendStatus;
    }

    public void setSendStatus(String sendStatus) {
        this.sendStatus = sendStatus;
    }

    @Override
    public String toString() {
        return "UnsendTransaction [businessDayDate=" + businessDayDate + ", sequenceNumber=" + sequenceNumber
                + ", sendStatus=" + sendStatus + "]";
    }
}