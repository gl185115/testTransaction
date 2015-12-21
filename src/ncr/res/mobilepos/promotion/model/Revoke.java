package ncr.res.mobilepos.promotion.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Revoke Model Object.
 *
 * Encapsulates the Revoke Promotion information.
 * @author RD185102
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Revoke")
public class Revoke {
    /** The custom constructor for Revoke.
     * @param earnedRewardIDToSet The Earned Reward ID to set.
     */
    public Revoke(final String earnedRewardIDToSet) {
        this.setEarnedRewardID(earnedRewardIDToSet);
    }
    /**
     * The Earned Reward ID.
     */
    @XmlElement(name = "EarnedRewardID")
    private String earnedRewardID;

    /**
     * @return The earnedRewardID
     */
    public final String getEarnedRewardID() {
        return earnedRewardID;
    }
    /**
     * @param earnedRewardIDToSet the earnedRewardID to set
     */
    public final void setEarnedRewardID(final String earnedRewardIDToSet) {
        this.earnedRewardID = earnedRewardIDToSet;
    }
}
