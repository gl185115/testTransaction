package ncr.res.ue.message.action;

import java.math.BigInteger;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.TransportHeader;

/**
 * class that creates member id messages.
 *
 */
public class MemberId extends OutgoingMessage {

    /**
     * Customer identifier.
     */
    private BigInteger memberId = new BigInteger("-1");
    /**
     * Flag that describes what to do with the id.
     */
    private int entryFlag = -1;
    /**
     * dictates the type of the memberid.
     */
    private int type = -1;

    @Override
    public final String createMessage(final String termId,
            final String transactionId) throws MessageException {
        String messageBody = String.format("%02d", this.getMessageHeader());
        messageBody += String.format("%020d", memberId);
        messageBody += String.format("%1d", entryFlag);
        messageBody += String.format("%02d", type);
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
     * @param memberIdToSet - member id
     * @param entryFlagToSet - entry flag
     * @param typeToSet - type
     */
    public MemberId(final BigInteger memberIdToSet,
            final int entryFlagToSet,
            final int typeToSet) {
        super(MessageTypes.MEMBER_ID);

        this.memberId = memberIdToSet;
        this.entryFlag = entryFlagToSet;
        this.type = typeToSet;

    }
}
