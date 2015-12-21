package ncr.res.ue.message.action;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.TransportHeader;
/**
 * ConnectionInitialization action message generator.
 * @author jg185106
 *
 */
public class ConnectionInitialization extends OutgoingMessage {
    /**
     * Location code for initialization.
     */
    private String locationCode;
    /**
     * Protocol version for UE Messaging.
     */
    private String protocolVersion;
    /**
     * Protocol build for UE Messaging.
     */
    private String protocolBuild;
    /**
     * ConnectionInitialization main constructor.
     * @param thisLocationCode Location code of requestor.
     * @param thisProtocolVersion Version number of AMS Reward
     *          Engine Integration Protocol.
     * @param thisProtocolBuild Build number of AMS Reward
     *          Engine Integration Protocol.
     */
    public ConnectionInitialization(
            final String thisLocationCode,
            final String thisProtocolVersion,
            final String thisProtocolBuild) {
        super(MessageTypes.CONNECTION_INITIALIZE);
        this.locationCode = thisLocationCode;
        this.protocolVersion = thisProtocolVersion;
        this.protocolBuild = thisProtocolBuild;
    }
    /*
     * (non-Javadoc)
     * @see ncr.res.ue.message.OutgoingMessage#
     * createMessage(java.lang.String, java.lang.String)
     */
    @Override
    public final String createMessage(
            final String termId,
            final String transactionId) throws MessageException {
        String messageBody = String.format("%2d", this.getMessageHeader());
        messageBody += String.format("%-12s", locationCode);
        messageBody += String.format("%8s", protocolVersion);
        messageBody += String.format("%4s", protocolBuild);
        messageBody += OutgoingMessage.MESSAGE_TERMINATOR;
        String fullMessage = TransportHeader.create(
                                messageBody,
                                termId,
                                transactionId);
        fullMessage += messageBody;
        return fullMessage;
    }
}
