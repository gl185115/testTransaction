package ncr.res.ue.message.response;


import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.ue.exception.MessageException;

/**
 * handles Total Response from UE.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TotalResponse")
public class TotalResponse extends UEResponseBase {
    /**
     * the total precision.
     */
    @XmlElement(name = "TotalPrecision")
    private String totalPrecision = "";
    /**
     * the net total.
     */
    @XmlElement(name = "NetTotal")
    private String netTotal = "";
    /**
     * the gross total.
     */
    @XmlElement(name = "GrossTotal")
    private String grossTotal = "";

    /**
     * the message length expected for a
     * Connection Initialization Response.
     */
    public static final int EXPECTED_MESSAGE_LENGTH = 43; //20(TH) + 23(Msg)
    /**
     * length of total precision.
     */
    public static final int TOTAL_PRECISION_LENGTH = 1;
    /**
     * length of net total.
     */
    public static final int NET_TOTAL_LENGTH = 10;
    /**
     * length of gross total.
     */
    public static final int GROSS_TOTAL_LENGTH = 10;
    /**
     * length of response flag.
     */
    public static final int RESPONSE_FLAG_LENGTH = 2;


    /**
     * gets the total precision.
     * @return String - the total precision
     */
    public final String getTotalPrecision() {
        return totalPrecision;
    }
    /**
     * gets the net total.
     * @return String - the net total
     */
    public final String getNetTotal() {
        return netTotal;
    }
    /**
     * gets the gross total.
     * @return String - the gross total
     */
    public final String getGrossTotal() {
        return grossTotal;
    }

    /**
     * constructor.
     * @param msgType - message type
     * @param responseString - response from UE
     * @throws MessageException - thrown when parsing error occurs
     */
    public TotalResponse(final int msgType,
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

    @Override
    protected final void parseResponse(final String responseString)
        throws MessageException {

        checkResponse(
                responseString,
                TotalResponse.EXPECTED_MESSAGE_LENGTH);

        //get needed data
        //start from index 22 and
        //end in index 22 + 2 (length of status indicator)
        int startIndex = UEResponseBase.IX_MESSAGE_START;
        int endIndex = startIndex + RESPONSE_FLAG_LENGTH;
        setResponseFlag(Integer.parseInt(responseString.substring(
                startIndex, endIndex)));

        startIndex = endIndex;
        endIndex += TOTAL_PRECISION_LENGTH;
        totalPrecision = responseString.substring(
                startIndex, endIndex).trim();

        startIndex = endIndex;
        endIndex += NET_TOTAL_LENGTH;
        netTotal = responseString.substring(
                startIndex, endIndex).trim();

        startIndex = endIndex;
        endIndex += GROSS_TOTAL_LENGTH;
        grossTotal = responseString.substring(
                startIndex, endIndex).trim();
    }
    /**
     * constructor.
     * @param responseFlag - the response flag
     */
    public TotalResponse(
            final int responseFlag) {
        setResponseFlag(responseFlag);
    }
}
