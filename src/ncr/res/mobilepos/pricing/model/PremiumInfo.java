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

package ncr.res.mobilepos.pricing.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * Premium Model Object.
 *
 * <P>A Premium Node in POSLog XML.
 *
 * <P>The Premium node is under Sale Node.
 * And mainly holds the information of the Premium
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PremiumInfo")
@ApiModel(value="PremiumInfo")
public class PremiumInfo {
    @XmlElement(name = "PremiumItemNo")
    private String premiumItemNo;
    
    @XmlElement(name = "PremiumItemName")
    private String premiumItemName;
    
    @XmlElement(name = "TargetPrice")
    private String targetPrice;
    
    @XmlElement(name = "TargetCount")
    private int targetCount;

    @ApiModelProperty(value="v~A€iNO", notes="v~A€iNO")
    public String getPremiumItemNo() {
        return premiumItemNo;
    }

    public void setPremiumItemNo(String premiumItemNo) {
        this.premiumItemNo = premiumItemNo;
    }

    @ApiModelProperty(value="v~A€iΌΜ", notes="v~A€iΌΜ")
    public String getPremiumItemName() {
        return premiumItemName;
    }

    public void setPremiumItemName(String premiumItemName) {
        this.premiumItemName = premiumItemName;
    }

    @ApiModelProperty(value="ΞΫΰz", notes="ΞΫΰz")
    public String getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(String targetPrice) {
        this.targetPrice = targetPrice;
    }

    @ApiModelProperty(value="€iΚ", notes="€iΚ")
    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }
    
    public PremiumInfo() {
    }

    //copy costructor
    public PremiumInfo(final PremiumInfo info)
    {
        this.premiumItemNo = info.getPremiumItemNo();
        this.premiumItemName = info.getPremiumItemName();
        this.targetPrice = info.getTargetPrice();
        this.targetCount = info.getTargetCount();
    }
}
