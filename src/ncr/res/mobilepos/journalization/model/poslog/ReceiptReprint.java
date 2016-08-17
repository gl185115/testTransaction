package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ReceiptReprint")
public class ReceiptReprint {
    /**
     * TypeCode
     */
    @XmlAttribute(name = "TypeCode")
    private String typeCode;
    /**
     * The TransactionLink object.
     */
    @XmlElement(name = "TransactionLink")
    private TransactionLink transactionLink;

    /**
     * @return transactionLink
     */
    public TransactionLink getTransactionLink() {
        return transactionLink;
    }

    /**
     * @param transactionLink セットする transactionLink
     */
    public void setTransactionLink(TransactionLink transactionLink) {
        this.transactionLink = transactionLink;
    }

    /**
     * @return typeCode
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * @param typeCode セットする typeCode
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

}
