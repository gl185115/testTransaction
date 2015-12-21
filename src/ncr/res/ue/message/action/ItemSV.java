package ncr.res.ue.message.action;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.TransportHeader;

/**
 * class that creates Item Stored Value.
 * @author cc185102
 */
public class ItemSV extends OutgoingMessage {
    /**
     * The Item Entry ID.
     */
    private int itemEntryID = -1;
    /**
     * The Item Quantity.
     */
    private int itemQuantity = -1;
    /**
     * The program ID.
     */
    private int programID = -1;
    /**
     * The Quantity.
     */
    private int quantity = -1;
    /**
     * The Expiration Date.
     */
    private String expiration = "";
    /**
     * The Default constructor.
     */
    private ItemSV() {    	
    }

    /**
     * Custom Constructor.
     * @param itemEntryIDToSet The Item Entry ID.
     * @param itemQuantityToSet The Item Quantity.
     * @param programIDToSet The Program ID.
     * @param quantityToSet The Quantity.
     * @param expirationToSet The Expiration.
     */
    public ItemSV(final int itemEntryIDToSet, final int itemQuantityToSet,
            final int programIDToSet, final int quantityToSet,
            final String expirationToSet) {
        super(MessageTypes.ITEM_SV);
        this.expiration = expirationToSet;
        this.itemQuantity = itemQuantityToSet;
        this.programID = programIDToSet;
        this.quantity = quantityToSet;
        this.itemEntryID = itemEntryIDToSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String createMessage(final String termId,
            final String transactionId)
            throws MessageException {
        String messageBody = String.format("%02d", this.getMessageHeader());
        messageBody += String.format("%06d", this.itemEntryID);
        messageBody += String.format("%06d", this.itemQuantity);
        messageBody += String.format("%06d", this.programID);
        messageBody += String.format("%06d", this.quantity);
        messageBody += String.format("%16s", this.expiration)
                             .replace(" ", "0");
        messageBody += OutgoingMessage.MESSAGE_TERMINATOR;

        String fullMessage = TransportHeader.create(
                                messageBody,
                                termId,
                                transactionId);
        fullMessage += messageBody;
        return fullMessage;
    }
}
