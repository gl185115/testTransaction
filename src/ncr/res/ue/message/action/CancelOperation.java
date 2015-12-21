package ncr.res.ue.message.action;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.TransportHeader;

/**
 * Cancel Operation action message generator.
 * @author cc185102
 *
 */
public class CancelOperation extends OutgoingMessage {
    /**
     * The Reason Flag.
     */
    private int reasonFlag;
    /**
     * The default constructor.
     */
    private CancelOperation() {    	
    }
    /**
     * The Custom Constructor.
     * @param reasonFlagToSet The Reason flag to set.
     */
    public CancelOperation(final int reasonFlagToSet) {
        this.reasonFlag = reasonFlagToSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String createMessage(final String termId,
            final String transactionId)
            throws MessageException {
        String messageBody = String.format("%2d", this.getMessageHeader());
            messageBody += String.format("%d", this.reasonFlag);
            messageBody += OutgoingMessage.MESSAGE_TERMINATOR;
            String fullMessage = TransportHeader.create(
                                    messageBody,
                                    termId,
                                    transactionId);
            fullMessage += messageBody;
            return fullMessage;
    }
}
