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
 * The Class SuspendTransaction.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SuspendTransaction")
public class SuspendTransaction extends OutgoingMessage {

    /**
     * The suspend flag. Holds the suspension status. 0 = Suspend Transaction,
     * ResumeID Request; 1 = Suspend Transaction, Use ResumeID provided or fail;
     * 2 = Suspend Transaction, Use ResumeID provided if possible.
     */
    @XmlElement(name = "SuspendFlag")
    private int suspendFlag = -1;

    /**
     * The resume identifier. The Unique identifier that The Engine should use
     * to identify the suspended transaction when it is resumed.
     */
    @XmlElement(name = "ResumeID")
    private int resumeID;

    /**
     * Instantiates a new suspend transaction.
     *
     * @param newSuspendFlag
     *            the new suspend flag
     * @param newResumeID
     *            the new resume id
     */
    public SuspendTransaction(final int newSuspendFlag,
            final int newResumeID) {
        super(MessageTypes.SUSPEND_TRANSACTION);
        this.setSuspendFlag(newSuspendFlag);
        this.setResumeID(newResumeID);
    }

    /**
     * Instantiates a new suspend transaction.
     */
    public SuspendTransaction() {
        super(MessageTypes.SUSPEND_TRANSACTION);
    }

    /*
     * (non-Javadoc)
     *
     * @see ncr.res.ue.message.OutgoingMessage#createMessage(java.lang.String,
     * java.lang.String)
     */
    @Override
    public final String createMessage(final String termId,
            final String transactionId) throws MessageException {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("%02d", this.getMessageHeader()))
                .append(String.format("%1d", this.getSuspendFlag()))
                .append(String.format("%030d", this.getResumeID()))
                .append(OutgoingMessage.MESSAGE_TERMINATOR);

        String fullMessage = TransportHeader.create(sb.toString(), termId,
                transactionId);
        fullMessage += sb.toString();

        return fullMessage;
    }

    /**
     * Sets the suspend flag.
     *
     * @param newSuspendFlag
     *            the new suspend flag
     */
    public final void setSuspendFlag(final int newSuspendFlag) {
        this.suspendFlag = newSuspendFlag;
    }

    /**
     * Gets the suspend flag.
     *
     * @return the suspend flag
     */
    public final int getSuspendFlag() {
        return suspendFlag;
    }

    /**
     * Sets the resume id.
     *
     * @param newResumeID
     *            the new resume id
     */
    public final void setResumeID(final int newResumeID) {
        this.resumeID = newResumeID;
    }

    /**
     * Gets the resume id.
     *
     * @return the resume id
     */
    public final int getResumeID() {
        return resumeID;
    }

}
