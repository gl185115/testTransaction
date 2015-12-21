package ncr.res.ue.message.response.rewards;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Points model.
 * @author AP185142
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Points")
public class Points extends RewardBase {
    /**
     * the length of the item entry id.
     */
    private static final int ITEM_ENTRY_ID_LENGTH = 6;

    /**
     * the item entry id.
     */
    @XmlElement(name = "ItemEntryId")
    private String itemEntryId;

    /**
     * gets the item entry id.
     * @return String - the item entry id
     */
    public final String getItemEntryid() {
        return itemEntryId;
    }

    /**
     * the length of the price precision.
     */
    private static final int PRICE_PRECISION_LENGTH = 1;

    /**
     * the price precision.
     */
    @XmlElement(name = "PricePrecision")
    private String pricePrecision;

    /**
     * gets the price precision.
     * @return String - the price precision
     */
    public final String getPricePrecision() {
        return pricePrecision;
    }

    /**
     * the length of match net unit price.
     */
    private static final int MATCH_NET_UNIT_PRICE_LENGTH = 10;

    /**
     * the match net unit price.
     */
    @XmlElement(name = "MatchNetUnitPrice")
    private String matchNetUnitPrice;

    /**
     * gets the match net unit price.
     * @return String - the match net unit price
     */
    public final String getMatchNetUnitPrice() {
        return matchNetUnitPrice;
    }

    /**
     * the length of item quantity.
     */
    private static final int ITEM_QUANTITY_LENGTH = 6;

    /**
     * the item quantity.
     */
    @XmlElement(name = "ItemQuantity")
    private String itemQuantity;

    /**
     * gets the item quantity.
     * @return String - the item quantity
     */
    public final String getItemQuantity() {
        return itemQuantity;
    }

    /**
     * the length of the program id.
     */
    private static final int PROGRAM_ID_LENGTH = 6;

    /**
     * the program id.
     */
    @XmlElement(name = "ProgramId")
    private String programId;

    /**
     * gets the program id.
     * @return String - the program id
     */
    public final String getProgramId() {
        return programId;
    }

    /**
     * the length of the quantity.
     */
    private static final int QUANTITY_LENGTH = 6;

    /**
     * the quantity.
     */
    @XmlElement(name = "Quantity")
    private String quantity;

    /**
     * gets the quantity.
     * @return String - the quantity
     */
    public final String getQuantity() {
        return quantity;
    }

    /**
     * Constructor.
     * @param pointsMessage - the message corresponding to a certain
     * @param length - the length of the passed message
     * reward sent by UE.
     */
    public Points(final String pointsMessage, final int length) {
        if (length
                != (pointsMessage.length())) {
            return;
        }

        int startIndex = 0;
        int endIndex = ITEM_ENTRY_ID_LENGTH;

        itemEntryId = pointsMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + PRICE_PRECISION_LENGTH;

        pricePrecision = pointsMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + MATCH_NET_UNIT_PRICE_LENGTH;

        matchNetUnitPrice = pointsMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + ITEM_QUANTITY_LENGTH;

        itemQuantity = pointsMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + PROGRAM_ID_LENGTH;

        programId = pointsMessage.substring(startIndex, endIndex);

        startIndex = endIndex;
        endIndex = startIndex + QUANTITY_LENGTH;

        quantity = pointsMessage.substring(startIndex, endIndex);

    }
}
