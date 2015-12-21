package ncr.res.ue.message.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.response.rewards.Discount;
import ncr.res.ue.message.response.rewards.Points;


/**
 * class that holds the response for Points Message.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PointsResponse")
@XmlSeeAlso({ Discount.class })
public class PointsResponse extends UEResponseBase {

    /**
     * The length of the message length.
     */
    private static final int MESSAGE_LENGTH_LENGTH = 4;

    /**
     * The length of the of one point message.
     */
    private static final int EXPECTED_POINT_MESSAGE_LENGTH = 35; //20(TH) + 2(M)

    /**
     * list of points.
     */
    @XmlElementWrapper(name = "PointsList")
    @XmlElementRef()
    private List<Points> points = new ArrayList<Points>();

    /**
     * Gets the points.
     *
     * @return the points
     */
    public final List<Points> getPoints() {
        return points;
    }

    @Override
    protected final void parseResponse(final String responseString)
            throws MessageException {

        int messageLength = Integer.parseInt(
                responseString.substring(0, MESSAGE_LENGTH_LENGTH));

        if (messageLength
                != (responseString.length() - MESSAGE_LENGTH_LENGTH + 2)) {
            throw new MessageException("Length is wrong for "
                    + this.getClass() + " "
                    + Integer.toString(messageLength));
        }

        int startIndex = UEResponseBase.IX_MESSAGE_START;
        int endIndex = startIndex + EXPECTED_POINT_MESSAGE_LENGTH;

        while (endIndex <= (messageLength
                + MESSAGE_LENGTH_LENGTH - 2)) {

            Points discount = new Points(
                    responseString.substring(startIndex, endIndex),
                    EXPECTED_POINT_MESSAGE_LENGTH);

            points.add(discount);

            startIndex = endIndex;
            endIndex += EXPECTED_POINT_MESSAGE_LENGTH;
        }
    }

    /**
     * constructor.
     * @param msgType - message type
     * @param responseString - response from UE
     * @throws MessageException - thrown when parsing error occurs
     */
    public PointsResponse(final int msgType,
            final String responseString) throws MessageException {
        super(msgType);

        points = new ArrayList<Points>();

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
     * Instantiates a new discount response.
     */
    public PointsResponse() {
    }
}
