/*
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 *
 * RetailTransaction
 *
 * Model Class for RetailTransaction
 *
 * De la Cerna, Jessel G.
 * Carlos G. Campos
 */

package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Premium Model Object.
 *
 * <P>A Premium Node in POSLog XML.
 *
 * <P>The Premium node is under Sale Node.
 * And mainly holds the information of the Premium
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Premium")
public class Premium {
    /**
     * The private member variable that will hold the flag of Premium.
     */
    @XmlElement(name = "PremiumItemFlag")
    private String premiumItemFlag;

    /**
     * PremiumItemPromotionCode
     */
    @XmlElement(name="PremiumItemPromotionCode")
    private String premiumItemPromotionCode;

    /**
     * PremiumItemPromotionName
     */
    @XmlElement(name="PremiumItemPromotionName")
    private String premiumItemPromotionName;

    /**
     * PremiumItemTargetPrice
     */
    @XmlElement(name="PremiumItemTargetPrice")
    private String premiumItemTargetPrice;

    /**
     * @return the premiumItemFlag
     */
    public final String getPremiumItemFlag() {
        return premiumItemFlag;
    }

    /**
     * @param premiumItemFlag the premiumItemFlag to set
     */
    public final void setPremiumItemFlag(final String premiumItemFlag) {
        this.premiumItemFlag = premiumItemFlag;
    }

    /**
     * @return the premiumItemPromotionCode
     */
    public final String getPremiumItemPromotionCode() {
        return premiumItemPromotionCode;
    }

    /**
     * @param premiumItemPromotionCode the premiumItemPromotionCode to set
     */
    public final void setPremiumItemPromotionCode(final String premiumItemPromotionCode) {
        this.premiumItemPromotionCode = premiumItemPromotionCode;
    }

    /**
     * @return the premiumItemPromotionName
     */
    public final String getPremiumItemPromotionName() {
        return premiumItemPromotionName;
    }

    /**
     * @param premiumItemPromotionName the premiumItemPromotionName to set
     */
    public final void setPremiumItemPromotionName(final String premiumItemPromotionName) {
        this.premiumItemPromotionName = premiumItemPromotionName;
    }

    /**
     * @return the premiumItemTargetPrice
     */
    public final String getPremiumItemTargetPrice() {
        return premiumItemTargetPrice;
    }

    /**
     * @param premiumItemTargetPrice the premiumItemTargetPrice to set
     */
    public final void setPremiumItemTargetPrice(final String premiumItemTargetPrice) {
        this.premiumItemTargetPrice = premiumItemTargetPrice;
    }
}
