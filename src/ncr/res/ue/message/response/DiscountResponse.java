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


/**
 * class that holds the response for Item Entry Message.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DiscountResponse")
@XmlSeeAlso({ Discount.class })
public class DiscountResponse extends UEResponseBase {

    /**
     * The length of the message length.
     */
    private static final int MESSAGE_LENGTH_LENGTH = 4;

    /**
     * The length of the reward length.
     */
    private static final int REWARD_LENGTH_LENGTH = 3; //20(TH) + 2(M)

    /**
     * list of discounts.
     */
    @XmlElementWrapper(name = "DiscountList")
    @XmlElementRef()
    private List<Discount> discounts = new ArrayList<Discount>();

    /**
     * Gets the discounts.
     *
     * @return the discounts
     */
    public final List<Discount> getDiscounts() {
        return discounts;
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
        int endIndex = startIndex + REWARD_LENGTH_LENGTH;

        while (endIndex < messageLength) {
            int rewardLength = Integer.parseInt(
                    responseString.substring(startIndex, endIndex));
            startIndex = endIndex;
            endIndex += rewardLength;

            // add the message length length
            //to the gotten message length and subtract 2
            // because the terminating char should not count
            // this gets the total of the message
            // to be checked against endIndex
            if (endIndex > (messageLength
                                + MESSAGE_LENGTH_LENGTH - 2)) {
                endIndex = messageLength;
            }

            Discount discount = new Discount(
                    responseString.substring(startIndex, endIndex),
                    rewardLength);

            discounts.add(discount);

            startIndex = endIndex;
            endIndex += REWARD_LENGTH_LENGTH;
        }
    }

    /**
     * constructor.
     * @param msgType - message type
     * @param responseString - response from UE
     * @throws MessageException - thrown when parsing error occurs
     */
    public DiscountResponse(final int msgType,
            final String responseString) throws MessageException {
        super(msgType);

        discounts = new ArrayList<Discount>();

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
    public DiscountResponse() {
    }
}
