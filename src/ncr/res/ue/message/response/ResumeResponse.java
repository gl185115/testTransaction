package ncr.res.ue.message.response;

import ncr.res.ue.exception.MessageException;

/**
 * ResumeResponse
 * Response Message entity for ResumeResponse operation.
 * @author RD185102
 *
 */
public class ResumeResponse extends UEResponseBase {
    /**
     * Expected message length for ResumeResponse.
     */
    private static final int EXPECTED_MESSAGE_LENGTH = 22;

    @Override
    protected final void parseResponse(final String responseString)
        throws MessageException {
        setResponseFlag(checkResponse(
                responseString, EXPECTED_MESSAGE_LENGTH));
    }

    /**
     * Constructor.
     * @param msgType       Message type.
     * @param responseString  Response from UE.
     * @throws MessageException Thrown when passing error occurs.
     */
    public ResumeResponse(final int msgType, final String responseString)
        throws MessageException {
            super(msgType);
            try {
                parseResponse(responseString);              
            } catch (IndexOutOfBoundsException ie) {
                throw new MessageException(
                        "parse fail - index out of bounds:\r\n"
                        + ie.getMessage());
            } catch (Exception e) {
                throw new MessageException("parse fail - exception:\r\n"
                        + e.getMessage());
            }
    }
    /**
     * constructor.
     * @param responseFlag - the response flag
     */
    public ResumeResponse(
            final int responseFlag) {
        setResponseFlag(responseFlag);
    }
}
