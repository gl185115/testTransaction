package ncr.res.ue.message;

import ncr.res.ue.exception.MessageException;

/**
 * Handles creation of the.
 * UE Transport Header
 * @author jg185106
 */
public final class TransportHeader {
    /**
     * Fixed length of the transport header.
     */
    public static final int TRANSPORT_HEADER_LENGTH = 20;
    /**
     * Fixed length of Terminator characters.
     */
    public static final int TERMINATOR_LENGTH = 4;
    /**
     * Fixed length of Data Message Header characters.
     */
    public static final int DATA_MESSAGE_HEADER_LENGTH = 2;
    /**
     * The length allocated to hold the message length.
     */
    public static final int TH_MESSAGELENGTH_LENGTH = 4;
    /**
     * The default constructor.
     */
    private TransportHeader() {
    }
    /**
     * Creation of the UE Message TransportHeader.
     * @param message the UE Message
     * @param termId the id of the terminal performing
     *          the transaction
     * @param transactionNumber the transaction number
     * @return String the header data created
     * @throws MessageException the exception
     */
    public static String create(final String message, final String termId,
            final String transactionNumber) throws MessageException {
        String header = "";
        if (!message.isEmpty() && !termId.isEmpty()
                && !transactionNumber.isEmpty()) {
            //MessageLength section should have 4 characters
            String messageLength = String.format("%04d",
                    TransportHeader.TRANSPORT_HEADER_LENGTH + message.length()
                            - TransportHeader.TERMINATOR_LENGTH);
            //TermId section should have 8 characters
            String headerTermId = String.format("%8s", termId)
                                    .replace(" ", "0");
            //TransId section should have 8 characters
            String headerTransactionNumber =
                String.format("%8s", transactionNumber).replace(" ", "0");
            header = messageLength + headerTermId + headerTransactionNumber;
        } else {
            throw new MessageException("Transport Header parameter invalid!");
        }
        return header;
    }
}
