package ncr.res.ue.message.response;

import java.util.List;
import java.util.ArrayList;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.response.rewards.CashierNotification;
import ncr.res.ue.message.response.rewards.CashierNotificationImmediate;
import ncr.res.ue.message.response.rewards.RewardBase;
import ncr.res.ue.message.response.rewards.StoredValue;
/**
 * Class to handle Reward responses.
 * @author jg185106
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "RewardResponse")
public class RewardsResponse extends UEResponseBase {
    /**
     * List of rewards.
     */
    @XmlElementRef
    private RewardBase reward;
    /**
     * Accessor of the rewards list.
     * @return {@link RewardBase}
     */
    public final RewardBase getReward() {
        return this.reward;
    }
    /**
     * Reward type.
     */
    @XmlElement(name = "RewardType")
    private String rewardType;
    /**
     * Accessor for the rewardType.
     * @return String
     */
    public final String getRewardType() {
        return this.rewardType;
    }
    /**
     * StoredValue rewards.
     * Applicable only to StoredValue.
     */
    @XmlElement(name = "Reward")
    private List<RewardBase> rewards = null;
    /**
     * Getter for the storedValue.
     * @return ArrayList<{@link StoredValue}>
     */
    public final List<RewardBase> getStoredValue() {
        return this.rewards;
    }
    /**
     * Default constructor.
     */
    public RewardsResponse() {
    }
    /**
     * Constructor for Rewards.
     * @param msgType - message type
     * @param responseString - response from UE
     * @throws MessageException - thrown when parsing error occurs
     */
    public RewardsResponse(final int msgType,
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
        switch (this.getMessageType()) {
            case MessageTypes.IM_CASHIER_NOTIFICATION_REWARD:
                reward = new CashierNotification(responseString);
                break;
            case MessageTypes.IM_CASHIER_NOTIFICATION_IMMEDIATE_REWARD:
                reward = new CashierNotificationImmediate(responseString);
                break;
            case MessageTypes.IM_STORED_VALUE_REWARD:
                rewards = StoredValue.parseStoredValue(responseString);
                rewardType = StoredValue.class.getSimpleName();
                break;
            default:
                break;
        }
        if (reward != null) {
            rewardType = reward.getClass().getSimpleName();
        }
    }
}
