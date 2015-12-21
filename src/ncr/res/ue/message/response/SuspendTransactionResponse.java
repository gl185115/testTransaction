package ncr.res.ue.message.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.ue.exception.MessageException;

/**
 * The Class SuspendTransactionResponse.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SuspendTransactionResponse")
public class SuspendTransactionResponse extends UEResponseBase {
    /**
     * The expected message length of the response. The Message including
     * terminator minus TransportHeader's MessageLength. TH(20 - 4) + DMH(2) +
     * DMB(32) + T(2)
     */
    private static final int EXPECTED_MESSAGE_LENGTH = 52;
    /**
     * Resume ID data length.
     */
    private static final int RESUME_ID_LENGTH = 30;
    /**
     * Resume ID.
     */
    @XmlElement(name = "ResumeID")
    private String resumeID;
    /**
     * Resume ID getter.
     * @return The ResumeID.
     */
    public final String getResumeID() {
        return resumeID;
    }
    /**
     * Instantiates a new suspend transaction response.
     *
     * @param msgType
     *            the msg type
     * @param responseString
     *            the response string
     * @throws MessageException
     *             the message exception
     */
    public SuspendTransactionResponse(final int msgType,
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

    /*
     * (non-Javadoc)
     *
     * @see
     * ncr.res.ue.message.response.UEResponseBase#parseResponse(java.lang.String
     * )
     */
    @Override
    protected final void parseResponse(final String responseString)
            throws MessageException {
        setResponseFlag(checkResponse(responseString, EXPECTED_MESSAGE_LENGTH));
        int startIndex = UEResponseBase.IX_MESSAGE_START
                            + UEResponseBase.LN_RESPONSE_FLAG;
        int endIndex = startIndex + RESUME_ID_LENGTH;
        resumeID = responseString.substring(startIndex, endIndex);    
    }

    /**
     * Custom constructor.
     * @param responseFlag - the response flag
     */
    public SuspendTransactionResponse(final int responseFlag) {
        setResponseFlag(responseFlag);
    }
}
