package ncr.res.ue.message.response.rewards;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * This message is used to associate
 * a number of stored value units to
 * an item in the transaction for the
 * purposes of returns and deferred
 * sales completions.
 * @author jg185106
 *
 */
@XmlRootElement(name = "StoredValue")
public class StoredValue extends RewardBase {
    /**
     * ItemEntryID length constant.
     */
    public static final int ITEM_ENTRY_ID_LENGTH = 6;
    /**
     * PricePrecision length constant.
     */
    public static final int PRICE_PRECISION_LENGTH = 1;
    /**
     * MatchNetUnitPrice length constant.
     */
    public static final int MATCH_NET_UNIT_PRICE_LENGTH = 10;
    /**
     * ItemQuantity length constant.
     */
    public static final int ITEM_QUANTITY_LENGTH = 6;
    /**
     * ProgramID length constant.
     */
    public static final int PROGRAM_ID_LENGTH = 6;
    /**
     * Quantity length constant.
     */
    public static final int QUANTITY_LENGTH = 6;
    /**
     * Expiration length constant.
     */
    public static final int EXPIRATION_LENGTH = 16;
    /**
     * Item to associate the sv to.
     */
    @XmlElement(name = "ItemEntryID")
    private String itemEntryId;
    /**
     * Precision indicator for MatchNetUnitPrice field.
     */
    @XmlElement(name = "PricePrecision")
    private int pricePrecision;
    /**
     * Net price of item to associate the sv to.
     */
    @XmlElement(name = "MatchNetUnitPrice")
    private String matchNetUnitPrice;
    /**
     * How many of the matching item should
     * have sv associated to them.
     */
    @XmlElement(name = "ItemQuantity")
    private String itemQuantity;
    /**
     * The identifier of the sv program
     * into which units are earned.
     * Right-justified, zero-filled.
     */
    @XmlElement(name = "ProgramID")
    private String programID;
    /**
     * The number of units earned
     * into the program.
     * Right-justified, zero-filled.
     */
    @XmlElement(name = "Quantity")
    private String quantity;
    /**
     * Expiration data that should be
     * echoed back to The Engine when
     * the sv units come into play in
     * a return or deferred sales completion.
     */
    @XmlElement(name = "Expiration")
    private String expiration;
    /**
     * Getter for ItemEntryID.
     * @return String
     */
    public final String getItemEntryID() {
        return this.itemEntryId;
    }
    /**
     * Getter for PricePrecision.
     * @return int
     */
    public final int getPricePrecision() {
        return this.pricePrecision;
    }
    /**
     * Getter for ItemQuantity.
     * @return String
     */
    public final String getItemQuantity() {
        return this.itemQuantity;
    }
    /**
     * Getter for ProgramID.
     * @return String
     */
    public final String getProgramID() {
        return this.programID;
    }
    /**
     * Getter for ProgramID.
     * @return String
     */
    public final String getQuantity() {
        return this.quantity;
    }
    /**
     * Getter for PricePrecision.
     * @return String
     */
    public final String getExpiration() {
        return this.expiration;
    }
    /**
     * Getter for PricePrecision.
     * @return String
     */
    public final String getMatchNetUnitPrice() {
        return this.matchNetUnitPrice;
    }
    /**
     * {@link StoredValue} default constructor.
     */
    public StoredValue() {
    }
    /**
     * Parse a StoredValue from the message string.
     * @param message - the reward message to parse.
     * @return StoredValue - StoredValue created from the
     *                       parsed message.
     */
    public static final List<RewardBase>
    parseStoredValue(final String message) {
        List<RewardBase> storedValues = new ArrayList<RewardBase>();
        String svMessage = message;
        while (!"\r\n".equals(svMessage) && !svMessage.isEmpty()) {
            int beginIndex = 0;
            int endIndex = ITEM_ENTRY_ID_LENGTH;
            StoredValue storedValue = new StoredValue();
            storedValue.itemEntryId = svMessage.substring(beginIndex, endIndex);
            beginIndex = endIndex;
            endIndex = beginIndex + PRICE_PRECISION_LENGTH;
            storedValue.pricePrecision = Integer.parseInt(
                    svMessage.substring(beginIndex, endIndex));
            beginIndex = endIndex;
            endIndex = beginIndex + MATCH_NET_UNIT_PRICE_LENGTH;
            storedValue.matchNetUnitPrice =
                svMessage.substring(beginIndex, endIndex);
            beginIndex = endIndex;
            endIndex = beginIndex + ITEM_QUANTITY_LENGTH;
            storedValue.itemQuantity =
                svMessage.substring(beginIndex, endIndex);
            beginIndex = endIndex;
            endIndex = beginIndex + PROGRAM_ID_LENGTH;
            storedValue.programID = svMessage.substring(beginIndex, endIndex);
            beginIndex = endIndex;
            endIndex = beginIndex + QUANTITY_LENGTH;
            storedValue.quantity = svMessage.substring(beginIndex, endIndex);
            beginIndex = endIndex;
            endIndex = beginIndex + EXPIRATION_LENGTH;
            storedValue.expiration = svMessage.substring(beginIndex, endIndex);
            beginIndex = endIndex;
            svMessage = svMessage.substring(beginIndex);
            storedValues.add(storedValue);
        }
        return storedValues;
    }
}
