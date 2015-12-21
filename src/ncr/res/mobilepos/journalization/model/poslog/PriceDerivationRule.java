package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Price Derivation Rule Model Object.
 *
 * @author CC185102
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PriceDerivationRule")
public class PriceDerivationRule {
	/** The ApplicationType. */
	@XmlAttribute(name = "ApplicationType")
    private String applicationType;

    /** The MixSubNo. */
    @XmlElement(name = "MixSubNo")
    private int mixSubNo;

	/** The Price Derivation Rule. */
    @XmlElement(name = "PriceDerivationRuleID")
    private String priceDerivationRuleID;

    /** The PromotionCode. */
    @XmlElement(name = "PromotionCode")
    private String PromotionCode;
    
    /** The PromotionName. */
    @XmlElement(name = "PromotionName")
    private String PromotionName;
    
    /** The Description. */
    @XmlElement(name = "Description")
    private String description;
    
    /** The MSQuantity. */
    @XmlElement(name = "MSQuantity")
    private String mSQuantity;

    /** The SetNo. */
    @XmlElement(name = "SetNo")
    private String setNo;

    /** The SetDetailNo. */
    @XmlElement(name = "SetDetailNo")
    private String setDetailNo;
    
    /** The Eligibility. */
    @XmlElement(name = "Eligibility")
    private Eligibility eligibility;
    
    /** The Amount. */
    @XmlElement(name = "Amount")
    private Amount amount;
    
    /** The MMQuantity. */
    @XmlElement(name = "MMQuantity")
    private String mmQuantity;

    /** ReasonCode */
    @XmlElement(name = "ReasonCode")
    private String reasonCode;

    /** ReceiptName */
    @XmlElement(name = "ReceiptName")
    private String receiptName;

    /** MSNo */
    @XmlElement(name = "MSNo")
    private String mSNo;

    /** MSDetailNo */
    @XmlElement(name = "MSDetailNo")
    private String mSDetailNo;
    
    /**
     * @return the mixSubNo
     */
    public final int getMixSubNo() {
        return mixSubNo;
    }
    /**
     * @param mixSubNo the mixSubNo to set
     */
    public final void setMixSubNo(final int mixSubNo) {
        this.mixSubNo = mixSubNo;
    }

    /**
     * @return the priceDerivationRuleID
     */
    public final String getPriceDerivationRuleID() {
        return priceDerivationRuleID;
    }
    /**
     * @param priceDerivationRuleIDToSet the priceDerivationRuleID to set
     */
    public final void setPriceDerivationRuleID(
            final String priceDerivationRuleIDToSet) {
        this.priceDerivationRuleID = priceDerivationRuleIDToSet;
    }
    /**
     * @return the description
     */
    public final String getDescription() {
        return description;
    }
    /**
     * @param descriptionToSet the description to set
     */
    public final void setDescription(
            final String descriptionToSet) {
        this.description = descriptionToSet;
    }
	/**
	 * @return the eligibility
	 */
	public final Eligibility getEligibility() {
		return eligibility;
	}
	/**
	 * @param eligibility the eligibility to set
	 */
	public final void setEligibility(
			final Eligibility eligibility) {
		this.eligibility = eligibility;
	}
	/**
	 * @return the amount
	 */
	public final Amount getAmount() {
		return amount;
	}
	/**
	 * @param amount the amount to set
	 */
	public final void setAmount(
			final Amount amount) {
		this.amount = amount;
	}
	/**
	 * @return the applicationType
	 */
	public final String getApplicationType() {
		return applicationType;
	}
	/**
	 * @param applicationType the applicationType to set
	 */
	public final void setApplicationType(String 
			applicationType) {
		this.applicationType = applicationType;
	}
	/**
	 * @return the mmQuantity
	 */
	public final String getMmQuantity() {
		return mmQuantity;
	}
	/**
	 * @param mmQuantity the mmQuantity to set
	 */
	public final void setMmQuantity(String mmQuantity) {
		this.mmQuantity = mmQuantity;
	}
    /**
     * @return the reasonCode
     */
    public final String getReasonCode() {
       return reasonCode;
    }
    /**
     * @param reasonCode the reasonCode to set
     */
     public final void setReasonCode(String reasonCode) {
        this.reasonCode = reasonCode;
     }
    /**
     * @return the receiptName
     */
    public String getReceiptName() {
        return receiptName;
    }
    /**
     * @param receiptName the receiptName to set
     */
    public void setReceiptName(String receiptName) {
        this.receiptName = receiptName;
    }
    /**
     * @return the promotionCode
     */
    public final String getPromotionCode() {
        return PromotionCode;
    }
    /**
     * @param promotionCode the promotionCode to set
     */
    public final void setPromotionCode(final String promotionCode) {
        PromotionCode = promotionCode;
    }
    /**
     * @return the promotionName
     */
    public final String getPromotionName() {
        return PromotionName;
    }
    /**
     * @param promotionName the promotionName to set
     */
    public final void setPromotionName(final String promotionName) {
        PromotionName = promotionName;
    }
    /**
     * @return the mSQuantity
     */
    public final String getmSQuantity() {
        return mSQuantity;
    }
    /**
     * @param mSQuantity the mSQuantity to set
     */
    public final void setmSQuantity(final String mSQuantity) {
        this.mSQuantity = mSQuantity;
    }
    /**
     * @return the setNo
     */
    public final String getSetNo() {
        return setNo;
    }
    /**
     * @param setNo the setNo to set
     */
    public final void setSetNo(final String setNo) {
        this.setNo = setNo;
    }
    /**
     * @return the setDetailNo
     */
    public final String getSetDetailNo() {
        return setDetailNo;
    }
    /**
     * @param setDetailNo the setDetailNo to set
     */
    public final void setSetDetailNo(final String setDetailNo) {
        this.setDetailNo = setDetailNo;
    }
    /**
     * @return the mSNo
     */
    public String getmSNo() {
        return mSNo;
    }
    /**
     * @param mSNo the mSNo to set
     */
    public void setmSNo(final String mSNo) {
        this.mSNo = mSNo;
    }
    /**
     * @return the mSDetailNo
     */
    public String getmSDetailNo() {
        return mSDetailNo;
    }
    /**
     * @param mSDetailNo the mSDetailNo to set
     */
    public void setmSDetailNo(final String mSDetailNo) {
        this.mSDetailNo = mSDetailNo;
    }
}
