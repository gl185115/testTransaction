package ncr.res.ue.message.response.rewards;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

/**
 * Base class for Reward responses.
 * @author jg185106
 *
 */
@XmlRootElement(name = "Reward")
@XmlSeeAlso({ CashierNotification.class,
    CashierNotificationImmediate.class,
    StoredValue.class })
public abstract class RewardBase {
    /**
     * the length of the reward ID.
     */
    protected static final int REWARD_ID_LENGTH = 6;

    /**
     * Uniquely identifies each cashier
     * notification for the transaction.
     */
    @XmlElement(name = "RewardID")
    private String rewardID;

    /**
     * gets the reward id.
     * @return String - the reward id
     */
    public final String getRewardID() {
        return rewardID;
    }
    /**
     * Setter for the Reward ID.
     * @param id - the reward id
     */
    protected final void setRewardID(
            final String id) {
        this.rewardID = id;
    }

    /**
     * the length of the earned reward id.
     */
    protected static final int EARNED_REWARD_ID_LENGTH = 6;

    /**
     * Used to identify all of the rewards
     * associated to a single instance of a promotion.
     */
    @XmlElement(name = "EarnedRewardID")
    private String earnedRewardID;

    /**
     * gets the earned reward id.
     * @return String - the earned reward id
     */
    public final String getEarnedRewardID() {
        return earnedRewardID;
    }
    /**
     * Private setter for the EarnedRewardID.
     * @param id - the earned reward id
     */
    protected final void setEarnedRewardID(
            final String id) {
        this.earnedRewardID = id;
    }
    /**
     * Parser for the RewardID
     * and EarnedRewardID.
     * Returns the remaining string to parse
     * in message.
     * @param message - Message String to parse.
     * @return String
     */
    protected final String parseRewardIds(
            final String message) {
        int startIndex = 0;
        int endIndex = REWARD_ID_LENGTH;
        this.setRewardID(message.substring(startIndex, endIndex));
        startIndex = endIndex;
        endIndex = startIndex + EARNED_REWARD_ID_LENGTH;
        this.setEarnedRewardID(message.substring(startIndex, endIndex));
        startIndex = endIndex;
        return message.substring(startIndex);
    }
}
