package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author      mlwang      <mlwangi @ isoftstone.com>
 * 
 * barLoyaltyReward Model Object.
 *
 * <P>An barLoyaltyReward Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "barLoyaltyReward")
public class BarLoyaltyReward {
    /**
     * barPromotionID Promotion id
     * ※ イベントコード
     */
    @XmlElement(name = "barPromotionID")
    private String barPromotionID;

    /**
     * barPromotionName Promotion Name
     * ※ イベント名称
     */
    @XmlElement(name = "barPromotionName")
    private String barPromotionName;
    
    /**
     * @return the barPromotionID
     */
    public String getBarPromotionID() {
        return barPromotionID;
    }

    /**
     * @param barPromotionID the barPromotionID to set
     */
    public void setBarPromotionID(String barPromotionID) {
        this.barPromotionID = barPromotionID;
    }
    
    /**
     * @return the barPromotionName
     */
    public String getBarPromotionName() {
        return barPromotionName;
    }

    /**
     * @param barPromotionName the barPromotionName to set
     */
    public void setBarPromotionName(String barPromotionName) {
        this.barPromotionName = barPromotionName;
    }
    
}
