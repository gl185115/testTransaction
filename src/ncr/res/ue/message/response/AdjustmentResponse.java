package ncr.res.ue.message.response;


import ncr.res.ue.exception.MessageException;

/**
 * class that holds the response for Adjustment Message.
 *
 */
public class AdjustmentResponse extends UEResponseBase {

    /**
     * the expected message length of the response.
     */
    private static final int EXPECTED_MESSAGE_LENGTH = 22; //20(TH) + 2(M)

    @Override
    protected final void parseResponse(final String responseString)
            throws MessageException {

        setResponseFlag(checkResponse(
                responseString, EXPECTED_MESSAGE_LENGTH));
    }

    /**
     * constructor.
     * @param msgType - message type
     * @param responseString - response from UE
     * @throws MessageException - thrown when parsing error occurs
     */
    public AdjustmentResponse(final int msgType,
            final String responseString) throws MessageException {
        super(msgType);

        try {
            parseResponse(responseString);
        } catch (IndexOutOfBoundsException ie) {
            throw new MessageException("parse fail - index out of bounds: "
                    + ie.getMessage());
        } catch (Exception e) {
            throw new MessageException("parse fail - exception: "
                    + e.getMessage());
        }
    }

    /**
     * Custom constructor.
     * @param responseFlag - the response flag
     */
    public AdjustmentResponse(final int responseFlag) {
        setResponseFlag(responseFlag);
    }
}
