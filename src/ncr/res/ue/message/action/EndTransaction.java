package ncr.res.ue.message.action;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.TransportHeader;

/**
 * class that creates end transaction messages.
 *
 */
public class EndTransaction extends OutgoingMessage {

    /**
     * the status of the transaction (0-2 and 10 only).
     */
    private int status = -1;
    /**
     * the date of the transaction.
     */
    private String date = "";
    /**
     * the time.
     */
    private String time = "";

    @Override
    public final String createMessage(final String termId,
            final String transactionId) throws MessageException {
        String messageBody = String.format("%02d", this.getMessageHeader());
        messageBody += String.format("%02d", status);
        messageBody += String.format("%6s", date);
        messageBody += String.format("%6s", time);
        messageBody += OutgoingMessage.MESSAGE_TERMINATOR;

        String fullMessage = TransportHeader.create(
                                messageBody,
                                termId,
                                transactionId);
        fullMessage += messageBody;

        return fullMessage;
    }

    /**
     * constructor.
     * @param statusToSet - status
     * @param dateToSet - date
     * @param timeToSet - time
     */
    public EndTransaction(final int statusToSet,
            final String dateToSet,
            final String timeToSet) {
        super(MessageTypes.END_TRANSACTION);

        this.status = statusToSet;
        this.date = dateToSet;
        this.time = timeToSet;

    }
}
