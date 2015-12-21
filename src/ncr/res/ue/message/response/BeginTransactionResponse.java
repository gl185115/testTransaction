package ncr.res.ue.message.response;


import ncr.res.ue.exception.MessageException;
/**
 * BeginTransactionResponse
 * Response message entity for BeginTransaction
 * operation.
 * @author jg185106
 *
 */
public class BeginTransactionResponse extends UEResponseBase {
    /**
     * Expected message length for BeginTransactionResponse.
     */
    public static final int EXPECTED_MESSAGE_LENGTH = 22;
    /**
     * constructor.
     * @param msgType - message type
     * @param responseString -  response from UE
     * @throws MessageException - thrown when parsing error occurs
     */
    public BeginTransactionResponse(final int msgType,
            final String responseString) throws MessageException {
        super(msgType);

        try {
            parseResponse(responseString);
        } catch (IndexOutOfBoundsException ie) {
            throw new MessageException("parse fail - index out of bounds:\r\n"
                    + ie.getMessage());
        } catch (Exception e) {
            throw new MessageException("parse fail - exception:\r\n"
                    + e.getMessage());
        }
    }
    @Override
    protected final void parseResponse(final String responseString)
        throws MessageException {        
        setResponseFlag(checkResponse(
                responseString,
                BeginTransactionResponse.EXPECTED_MESSAGE_LENGTH));
    }

    /**
     * constructor.
     * @param responseFlag - the response flag
     */
    public BeginTransactionResponse(
            final int responseFlag) {
        setResponseFlag(responseFlag);
    }
}
