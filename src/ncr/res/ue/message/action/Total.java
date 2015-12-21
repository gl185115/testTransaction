package ncr.res.ue.message.action;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.TransportHeader;

/**
 * class that creates total messages.
 *
 */
public class Total extends OutgoingMessage {

    /**
     * Precision indicator for the Total and UserTotal field.
     */
    private int totalPrecision = -1;
    /**
     * Sum of gross price of all items sold in the transaction,
     * not including taxes, at the time the message is sent.
     */
    private int total = -1;
    /**
     * Sum of net price of all items sold
     * in the transaction (at the time the message is sent).
     */
    private int userTotal = -1;
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
        messageBody += String.format("%1d", totalPrecision);
        messageBody += String.format("%010d", total);
        messageBody += String.format("%010d", userTotal);
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
     * @param totalPrecisionToSet - totalPrecision
     * @param totalToSet - total
     * @param userTotalToSet - userTotal
     * @param dateToSet - date
     * @param timeToSet - time
     */
    public Total(final int totalPrecisionToSet,
            final int totalToSet,
            final int userTotalToSet,
            final String dateToSet,
            final String timeToSet) {
        super(MessageTypes.TOTAL);

        this.totalPrecision = totalPrecisionToSet;
        this.total = totalToSet;
        this.userTotal = userTotalToSet;
        this.date = dateToSet;
        this.time = timeToSet;

    }
}
