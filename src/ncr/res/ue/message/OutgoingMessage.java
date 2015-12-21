package ncr.res.ue.message;

import ncr.res.ue.exception.MessageException;

/**
 * Messages base class.
 * @author jg185106
 */
public abstract class OutgoingMessage {
    /**
     * Message terminator.
     */
    public static final String MESSAGE_TERMINATOR = "\r\n";
    /**
     * Message header container.
     */
    private int messageHeader = 0;
    /**
     * Message header getter.
     * @return int
     */
    public final int getMessageHeader() {
        return this.messageHeader;
    }
    /**
     * Default Constructor.
     */
    public OutgoingMessage() {
    }
    /**
     * Constructor to set message header.
     * @param messageType type of message.
     */
    public OutgoingMessage(final int messageType) {
        messageHeader = messageType;
    }
    /**
     * Create message.
     * @param termId the terminal id performing this
     *              transaction
     * @param transactionId the transactionId for this transaction
     * @return String
     * @throws MessageException exception thrown when Message
     *              is not created properly.
     */
    public abstract String createMessage(
                                String termId,
                                String transactionId) throws MessageException;
}
