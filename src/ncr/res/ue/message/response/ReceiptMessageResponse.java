package ncr.res.ue.message.response;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.response.rewards.Discount;
import ncr.res.ue.message.response.rewards.ReceiptMessageLine;


/**
 * class that holds the response for Receipt Message.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ReceiptMessageResponse")
@XmlSeeAlso({ Discount.class })
public class ReceiptMessageResponse extends UEResponseBase {

    /**
     * The length of the message length.
     */
    private static final int MESSAGE_LENGTH_LENGTH = 4;

    /**
     * Index of print length.
     */
    private static final int PRINT_LENGTH_INDEX = 7;

    /**
     * The length of the print length.
     */
    private static final int PRINT_LENGTH_LENGTH = 2;

    /**
     * The known length of the print line.
     */
    private static final int KNOWN_PRINT_LINE_LENGTH = 11;

    /**
     * The length of the reward id.
     */
    private static final int REWARD_ID_LENGTH = 6;

    /**
     * reward id.
     */
    @XmlElement(name = "RewardId")
    private String rewardId;

    /**
     * returns the reward id.
     * @return the reward id
     */
    public final String getRewardId() {
        return this.rewardId;
    }

    /**
     * The length of the earned reward id.
     */
    private static final int EARNED_REWARD_ID_LENGTH = 6;

    /**
     * earned reward id.
     */
    @XmlElement(name = "EarnedRewardId")
    private String earnedRewardId;

    /**
     * returns the earned reward id.
     * @return the earned reward id
     */
    public final String getEarnedRewardId() {
        return this.earnedRewardId;
    }

    /**
     * The length of the location.
     */
    private static final int LOCATION_LENGTH = 1;

    /**
     * Location.
     */
    @XmlElement(name = "Location")
    private String location;

    /**
     * returns the location.
     * @return the location
     */
    public final String getLocation() {
        return this.location;
    }

    /**
     * The length of the message type.
     */
    private static final int RCPT_MESSAGE_TYPE_LENGTH = 1;

    /**
     * message type.
     */
    @XmlElement(name = "MessageType")
    private String messageType;

    /**
     * returns the receipt message type.
     * @return the message type
     */
    public final String getReceiptMessageType() {
        return this.messageType;
    }

    /**
     * The length of the sort id.
     */
    private static final int SORT_ID_LENGTH = 3;

    /**
     * Sort Id.
     */
    @XmlElement(name = "SortId")
    private String sortId;

    /**
     * returns the sort id.
     * @return the sort id
     */
    public final String getSortId() {
        return this.sortId;
    }

    /**
     * list of receipt message lines.
     */
    @XmlElementWrapper(name = "ReceiptLineList")
    @XmlElementRef()
    private List<ReceiptMessageLine> receiptLineList
        = new ArrayList<ReceiptMessageLine>();

    /**
     * Gets the receipt lines.
     *
     * @return the receipt lines
     */
    public final List<ReceiptMessageLine> getReceiptLines() {
        return receiptLineList;
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
        int endIndex = startIndex + REWARD_ID_LENGTH;

        rewardId = responseString.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + EARNED_REWARD_ID_LENGTH;

        earnedRewardId = responseString.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + LOCATION_LENGTH;

        location = responseString.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + RCPT_MESSAGE_TYPE_LENGTH;

        messageType = responseString.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + SORT_ID_LENGTH;

        sortId = responseString.substring(startIndex, endIndex);

        int printLineLength = Integer.parseInt(
                responseString.substring(
                        endIndex + PRINT_LENGTH_INDEX,
                        endIndex + PRINT_LENGTH_INDEX + PRINT_LENGTH_LENGTH));

        startIndex = endIndex;
        endIndex = startIndex + printLineLength + KNOWN_PRINT_LINE_LENGTH;

        while (endIndex <= (messageLength
                + MESSAGE_LENGTH_LENGTH - 2)) {

            ReceiptMessageLine receiptLine = new ReceiptMessageLine(
                    responseString.substring(startIndex, endIndex));

            receiptLineList.add(receiptLine);

            if ((endIndex + PRINT_LENGTH_INDEX
                    + PRINT_LENGTH_LENGTH) > messageLength) {
                break;
            }

            printLineLength = Integer.parseInt(
                    responseString.substring(
                            endIndex + PRINT_LENGTH_INDEX,
                            endIndex + PRINT_LENGTH_INDEX
                            + PRINT_LENGTH_LENGTH));

            startIndex = endIndex;
            endIndex = startIndex + printLineLength + KNOWN_PRINT_LINE_LENGTH;
        }
    }

    /**
     * constructor.
     * @param msgType - message type
     * @param responseString - response from UE
     * @throws MessageException - thrown when parsing error occurs
     */
    public ReceiptMessageResponse(final int msgType,
            final String responseString) throws MessageException {
        super(msgType);

        receiptLineList = new ArrayList<ReceiptMessageLine>();

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
    public ReceiptMessageResponse() {
    }
}
