package ncr.res.ue.message.action;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.TransportHeader;

/**
 * class that creates a message for ITEM_QUANTITY.
 * @author cc185102
 *
 */
public class ItemQuantity extends OutgoingMessage {
    /**
     * The Entry Flag.
     */
    private int entryFlag = -1;
    /**
     * The Item Entry ID.
     */
    private int itemEntryID = -1;
    /**
     * The Reward Check Sum.
     */
    private int rewardCheckSum = -1;
    /**
     * The Quantity.
     */
    private int quantity = -1;

    /**
     * The default constructor.
     */
    private ItemQuantity() {    	
    }

    /**
     * The custom constructor for Item Quantity.
     * @param entryFlagToSet The new Entry Flag.
     * @param itemEntryIDToSet  The new Item Entry.
     * @param rewardCheckSumToSet   The new Reward Check Sum.
     * @param quantityToSet The new quantity.
     */
    public ItemQuantity(final int entryFlagToSet, final int itemEntryIDToSet,
            final int rewardCheckSumToSet, final int quantityToSet) {
        super(MessageTypes.ITEM_QUANTITY);
        this.entryFlag = entryFlagToSet;
        this.itemEntryID = itemEntryIDToSet;
        this.quantity = quantityToSet;
        this.rewardCheckSum = rewardCheckSumToSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String createMessage(final String termId,
            final String transactionId)
            throws MessageException {
        String messageBody = String.format("%02d", this.getMessageHeader());
        messageBody += String.format("%1d", this.entryFlag);
        messageBody += String.format("%06d", this.itemEntryID);
        messageBody += String.format("%032d", this.rewardCheckSum);
        messageBody += String.format("%06d", this.quantity);
        messageBody += OutgoingMessage.MESSAGE_TERMINATOR;

        String fullMessage = TransportHeader.create(
                                messageBody,
                                termId,
                                transactionId);
        fullMessage += messageBody;
        return fullMessage;
    }
}
