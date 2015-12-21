package ncr.res.ue.message.action;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.TransportHeader;

/**
 * Class that creates Trigger Code message.
 * @author cc185102
 *
 */
public class TriggerCode extends OutgoingMessage {
    /**
     * The Entry ID.
     */
    private int entryID =  -1;
    /**
     * The Entry Flag.
     */
    private int entryFlag = -1;
    /**
     * The Trigger Code.
     */
    private int triggerCode;
    /**
     * The Default Constructor.
     */
    private TriggerCode() {    	
    }
    /**
     * The Custom constructor for Trigger Code.
     * @param entryIDToSet      The Entry ID to set.
     * @param entryFlagToSet    The Entry Flag to set.
     * @param triggerCodeToSet  The Trigger Code to Set.
     */
    public TriggerCode(final int entryIDToSet, final int entryFlagToSet,
            final int triggerCodeToSet) {
        super(MessageTypes.TRIGGER_CODE);
        this.entryID = entryIDToSet;
        this.entryFlag = entryFlagToSet;
        this.triggerCode = triggerCodeToSet;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public final String createMessage(final String termId,
            final String transactionId)
            throws MessageException {
        String messageBody = String.format("%02d", this.getMessageHeader());
        messageBody += String.format("T%05d", this.entryID);
        messageBody += String.format("%1d", this.entryFlag);
        messageBody += String.format("%016d", this.triggerCode);
        messageBody += OutgoingMessage.MESSAGE_TERMINATOR;

        String fullMessage = TransportHeader.create(
                                messageBody,
                                termId,
                                transactionId);
        fullMessage += messageBody;

        return fullMessage;
    }
}
