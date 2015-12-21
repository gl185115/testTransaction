/**
 * Copyright (c) 2015 NCR Japan Ltd. 
 */
package ncr.res.mobilepos.giftcard.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GiftCard")
public class GiftCard {
    @XmlElement(name = "JIS1")
    String jis1;
    @XmlElement(name = "JIS2")
    String jis2;
    @XmlElement(name = "Amount")
    String amount;
    /** original transactionID for cancel transaction */
    @XmlElement(name = "TransactionId")
    String transactionId;

    public GiftCard() {
    }

    public GiftCard(final GiftCard g) {
        jis1 = g.jis1;
        jis2 = g.jis2;
        amount = g.amount;
    }

    public String getJis1() {
        return jis1;
    }
    public String getJis2() {
        return jis2;
    }
    public String getAmount() {
        return amount;
    }
    public String getTransactionId() {
        return transactionId;
    }
}

