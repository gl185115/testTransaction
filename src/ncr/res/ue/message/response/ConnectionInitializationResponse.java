package ncr.res.ue.message.response;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.ue.exception.MessageException;

/**
 * handles Connection Initialization Response from UE.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ConnectionInitializationResponse")
public class ConnectionInitializationResponse extends UEResponseBase {
    /**
     * the status indicator.
     */
    @XmlElement(name = "StatusIndicator")
    private String statusIndicator = "";
    /**
     * the location code.
     */
    @XmlElement(name = "LocationCode")
    private String locationCode = "";
    /**
     * the protocol version.
     */
    @XmlElement(name = "ProtocolVersion")
    private String protocolVersion = "";
    /**
     * the protocol build.
     */
    @XmlElement(name = "ProtocolBuild")
    private String protocolBuild = "";

    /**
     * the message length expected for a
     * Connection Initialization Response.
     */
    public static final int EXPECTED_MESSAGE_LENGTH = 46; //20(TH) + 26(Msg)
    /**
     * length of status indicator.
     */
    public static final int STATUS_INDICATOR_LENGTH = 2;
    /**
     * length of location code.
     */
    public static final int LOCATION_CODE_LENGTH = 12;
    /**
     * length of protocol version.
     */
    public static final int PROTOCOL_VERSION_LENGTH = 8;
    /**
     * length of protocol build.
     */
    public static final int PROTOCOL_BUILD_LENGTH = 4;

    /**
     * gets the status indicator.
     * @return String
     */
    public final String getStatusIndicator() {
        return statusIndicator;
    }
    /**
     * gets the location code.
     * @return String - the location code
     */
    public final String getLocationCode() {
        return locationCode;
    }
    /**
     * gets the protocol version.
     * @return String - the protocol version
     */
    public final String getProtocolVersion() {
        return protocolVersion;
    }
    /**
     * gets the protocol build.
     * @return String - the protocol build
     */
    public final String getProtocolBuild() {
        return protocolBuild;
    }

    /**
     * constructor.
     * @param msgType - message type
     * @param responseString - response from UE
     * @throws MessageException - thrown when parsing error occurs
     */
    public ConnectionInitializationResponse(final int msgType,
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
     * constructor.
     * @param responseFlag - the response flag
     */
    public ConnectionInitializationResponse(
            final int responseFlag) {
        setResponseFlag(responseFlag);
    }

    @Override
    protected final void parseResponse(final String responseString)
        throws MessageException {

        checkResponse(
                responseString,
                ConnectionInitializationResponse.EXPECTED_MESSAGE_LENGTH);

        //get needed data
        //start from index 22 and
        //end in index 22 + 2 (length of status indicator)
        int startIndex = UEResponseBase.IX_MESSAGE_START;
        int endIndex = startIndex + STATUS_INDICATOR_LENGTH;
        setResponseFlag(Integer.parseInt(responseString.substring(
                startIndex, endIndex)));

        statusIndicator = "" + getResponseFlag();

        startIndex = endIndex;
        endIndex += LOCATION_CODE_LENGTH;
        locationCode = responseString.substring(
                startIndex, endIndex).trim();

        startIndex = endIndex;
        endIndex += PROTOCOL_VERSION_LENGTH;
        protocolVersion = responseString.substring(
                startIndex, endIndex).trim();

        startIndex = endIndex;
        endIndex += PROTOCOL_BUILD_LENGTH;
        protocolBuild = responseString.substring(
                startIndex, endIndex).trim();
    }
}
