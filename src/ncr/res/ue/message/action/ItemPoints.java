package ncr.res.ue.message.action;


import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.TransportHeader;

/**
 * class that creates item points messages.
 *
 */
public class ItemPoints extends OutgoingMessage {

    /**
     * Item entry id.
     */
    private int itemEntryId = -1;
    /**
     * Item quantity.
     */
    private int itemQuantity = -1;
    /**
     * Program Id.
     */
    private int programId = -1;
    /**
     * Quantity.
     */
    private int quantity = -1;

    @Override
    public final String createMessage(final String termId,
            final String transactionId) throws MessageException {
        String messageBody = String.format("%02d", this.getMessageHeader());
        messageBody += String.format("%06d", itemEntryId);
        messageBody += String.format("%06d", itemQuantity);
        messageBody += String.format("%06d", programId);
        messageBody += String.format("%06d", quantity);
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
     * @param itemEntryIdToSet - item entry id
     * @param itemQuantityToSet - item quantity flag
     * @param programIdToSet - program id
     * @param quantityToSet - quantity
     */
    public ItemPoints(final int itemEntryIdToSet,
            final int itemQuantityToSet,
            final int programIdToSet,
            final int quantityToSet) {
        super(MessageTypes.ITEM_POINTS);

        this.itemEntryId = itemEntryIdToSet;
        this.itemQuantity = itemQuantityToSet;
        this.programId = programIdToSet;
        this.quantity = quantityToSet;

    }
}
