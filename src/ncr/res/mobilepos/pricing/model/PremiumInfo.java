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

    @ApiModelProperty(value="プレミアム商品NO", notes="プレミアム商品NO")
    public String getPremiumItemNo() {
        return premiumItemNo;
    }

    public void setPremiumItemNo(String premiumItemNo) {
        this.premiumItemNo = premiumItemNo;
    }

    @ApiModelProperty(value="プレミアム商品名称", notes="プレミアム商品名称")
    public String getPremiumItemName() {
        return premiumItemName;
    }

    public void setPremiumItemName(String premiumItemName) {
        this.premiumItemName = premiumItemName;
    }

    @ApiModelProperty(value="対象金額", notes="対象金額")
    public String getTargetPrice() {
        return targetPrice;
    }

    public void setTargetPrice(String targetPrice) {
        this.targetPrice = targetPrice;
    }

    @ApiModelProperty(value="商品数量", notes="商品数量")
    public int getTargetCount() {
        return targetCount;
    }

    public void setTargetCount(int targetCount) {
        this.targetCount = targetCount;
    }
}
