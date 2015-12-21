package ncr.res.ue.message.response;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.TransportHeader;

/**
 * base class for responses from UE.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "UEResponseBase")
public abstract class UEResponseBase {
    /**
     * the status indicator.
     */
    @XmlElement(name = "ResponseFlag")
    private int responseFlag;
    /**
     * holds the message type of the response.
     */
    @XmlElement(name = "MessageType")
    private int messageType;

    /**
     * result constant for successful parsing.
     */
    public static final int PARSE_OK = 0;
    /**
     * result constant for index access failure.
     */
    public static final int ERROR_PARSE_INDEX_FAIL = 1100;
    /**
     * result constant for incompatible type.
     */
    public static final int ERROR_PARSE_WRONG_TYPE = 1101;
    /**
     * result constant for wrong string length.
     */
    public static final int ERROR_PARSE_WRONG_LENGTH = 1102;
    /**
     * index where the actual message starts.
     */
    public static final int IX_MESSAGE_START = 22;
    /**
     * Length of the response flag.
     */
    public static final int LN_RESPONSE_FLAG = 2;
    /**
     * sets the messageType.
     * @param newMessageType - the messageType to set
     */
    public final void setMessageType(final int newMessageType) {
        this.messageType = newMessageType;
    }
    /**
     * sets the responseFlag.
     * @param newResponseFlag - the responseFlag to set
     */
    public final void setResponseFlag(final int newResponseFlag) {
        this.responseFlag = newResponseFlag;
    }
    /**
     * gets messageType.
     * @return int - the messageType
     */
    public final int getMessageType() {
        return this.messageType;
    }
    /**
     * gets the status indicator.
     * @return String - the status indicator
     */
    public final int getResponseFlag() {
        return responseFlag;
    }

    /**
     * Default constructor.
     */
    public UEResponseBase() {
    }

    /**
     * constructor for setting messageType.
     * @param newMessageType - the messageType to set
     */
    public UEResponseBase(final int newMessageType) {
        this.messageType = newMessageType;
    }

    /**
     * Checks if response data is valid.
     * @param message the response message to be checked.
     * @param expectedLength the expected length of message.
     * @return int
     * @throws MessageException thrown when message is invalid.
     */
    protected final int checkResponse(
            final String message,
            final int expectedLength) throws MessageException {
      //check validity of messageType
        int msgType = Integer.parseInt(
                message.substring(
                        TransportHeader.TRANSPORT_HEADER_LENGTH,
                        TransportHeader.TRANSPORT_HEADER_LENGTH
                            + TransportHeader.DATA_MESSAGE_HEADER_LENGTH));
        if (getMessageType() != msgType) {
            throw new MessageException("Incompatible message type for "
                    + this.getClass() + " "
                    + Integer.toString(msgType));
        }

        //check validity of Length
        int messageLength = Integer.parseInt(
                message.substring(0,
                            TransportHeader.TH_MESSAGELENGTH_LENGTH));
        if (messageLength != expectedLength) {
            throw new MessageException("Length is wrong for "
                    + this.getClass() + " "
                    + Integer.toString(messageLength));
        }

        return Integer.parseInt(message.substring(
                IX_MESSAGE_START, IX_MESSAGE_START + LN_RESPONSE_FLAG));
    }

    /**
     * parses the response from UE and gets the data.
     * @param responseString - response from UE
     * @throws MessageException - thrown when parsing fails
     */
    protected abstract void parseResponse(String responseString)
        throws MessageException;
}
