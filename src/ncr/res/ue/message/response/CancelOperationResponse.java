package ncr.res.ue.message.response;

import ncr.res.ue.exception.MessageException;

/**
 * The Cancel Operation Response Base.
 * @author cc185102
 */
public class CancelOperationResponse extends UEResponseBase {
    /**
     * The expected message length.
     */
    public static final int EXPECTED_MESSAGE_LENGTH = 22;

    /**
     * The Default Constructor.
     */
    public CancelOperationResponse() {
        super();
    }

    /**
     * constructor.
     *
     * @param msgType
     *            - message type
     * @param responseString
     *            - response from UE
     * @throws MessageException
     *             - thrown when parsing error occurs
     */
    public CancelOperationResponse(final int msgType,
            final String responseString)
            throws MessageException {
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
     * {@inheritDoc}
     */
    @Override
    protected final void parseResponse(final String responseString)
            throws MessageException {
        setResponseFlag(checkResponse(responseString, EXPECTED_MESSAGE_LENGTH));
    }
    /**
     * The custom constructor.
     * @param responseFlag The Response flag.
     */
    public CancelOperationResponse(final int responseFlag) {
        setResponseFlag(responseFlag);
    }
}
