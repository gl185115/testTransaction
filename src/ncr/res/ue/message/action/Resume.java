package ncr.res.ue.message.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.TransportHeader;
/**
 * Resume Transaction action generator.
 * @author RD185102
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ResumeTransaction")
public class Resume extends OutgoingMessage {

    /**
     * The transaction resume flag.
     */
    @XmlElement(name = "ResumeFlag")
    private int thisResumeFlag = 0;
    /**
     * Identifier from suspend transaction response.
     */
    @XmlElement(name = "ResumeID")
    private String thisResumeId = "";

    /*
     * (non-Javadoc)
     * @see ncr.res.ue.message.OutgoingMessage#
     * createMessage(java.lang.String, java.lang.String)
     */
    @Override
    public final String createMessage(final String termId,
            final String transactionId) throws MessageException {

        String messageBody = String.format("%02d" , this.getMessageHeader());
        messageBody += String.format("%1d", this.thisResumeFlag);
        messageBody += String.format("%30s" , this.thisResumeId);
        messageBody += OutgoingMessage.MESSAGE_TERMINATOR;

        String fullMessage = TransportHeader.create(messageBody,
                termId, transactionId);
        fullMessage += messageBody;

        return fullMessage;
    }

    /**
     * The main constructor for Resume transaction.
     * @param resumeFlag    Transaction resume flag.
     * @param resumeId      Identifier from suspend transaction response.
     */
    public Resume(final int resumeFlag, final String resumeId) {
        super(MessageTypes.RESUME);
        this.setThisResumeFlag(resumeFlag);
        this.setThisResumeId(resumeId);
    }

    /**
     * Resume ID Setter.
     * @param newResumeId  Resume ID
     */
    public final void setThisResumeId(final String newResumeId) {
        this.thisResumeId = newResumeId;
    }
    /**
     * Resume ID Getter.
     * @return resume id
     */
    public final String getThisResumeId() {
        return thisResumeId;
    }
    /**
     * Resume Flag Setter.
     * @param newResumeFlag  Resume Flag
     */
    public final void setThisResumeFlag(final int newResumeFlag) {
        this.thisResumeFlag = newResumeFlag;
    }
    /**
     * Resume Flag Getter.
     * @return  Resume Flag
     */
    public final int getThisResumeFlag() {
        return thisResumeFlag;
    }
}
