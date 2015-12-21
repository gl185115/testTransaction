package ncr.res.ue.message.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.ue.exception.MessageException;

/**
 * class that holds the response for Item Entry Message.
 * @author cc185102
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ItemQuantityResponse")
public class ItemQuantityResponse extends UEResponseBase {
    /**
     * The expected message length.
     */
    public static final int EXPECTED_MESSAGE_LENGTH = 22;
    /**
     * The default Item Quantity Response constructor.
     */
    public ItemQuantityResponse() {
        super();
    }
    /**
     * The custom constructor.
     * @param responseFlag The Response flag.
     */
    public ItemQuantityResponse(final int responseFlag) {
        setResponseFlag(responseFlag);
    }
    /**
     * The custom ItemQuantityResponse constructor.
     * @param msgType The message Type.
     * @param responseString The Response String.
     * @throws MessageException The exception thrown when error fail.
     */
    public ItemQuantityResponse(final int msgType,
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
     * {@inheritDoc}
     */
    @Override
    protected final void parseResponse(final String responseString)
    throws MessageException {
        setResponseFlag(checkResponse(
                responseString, TriggerCodeResponse.EXPECTED_MESSAGE_LENGTH));
    }
}
