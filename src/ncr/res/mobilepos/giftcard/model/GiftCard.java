/**
 * Copyright (c) 2015 NCR Japan Ltd. 
 */
package ncr.res.mobilepos.giftcard.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GiftCard")
@ApiModel(value="GiftCard")
public class GiftCard {
    @XmlElement(name = "JIS1")
    String jis1;
    @XmlElement(name = "JIS2")
    String jis2;
    @XmlElement(name = "Amount")
    String amount;
    @XmlElement(name = "PIN")
    String pin;
    /** original transactionID for cancel transaction */
    @XmlElement(name = "TransactionId")
    String transactionId;

    public GiftCard() {
    }

    public GiftCard(final GiftCard g) {
        jis1 = g.jis1;
        jis2 = g.jis2;
        pin = g.pin;
        amount = g.amount;
    }

    @ApiModelProperty(value="JIS1", notes="JIS1")
    public String getJis1() {
        return jis1;
    }
    @ApiModelProperty(value="JIS2", notes="JIS2")
    public String getJis2() {
        return jis2;
    }
    @ApiModelProperty(value="PIN", notes="PIN")
    public String getPin() {
        return pin;
    }
    @ApiModelProperty(value="Amount", notes="Amount")
    public String getAmount() {
        return amount;
    }
    @ApiModelProperty(value="取引コード", notes="取引コード")
    public String getTransactionId() {
        return transactionId;
    }
}

