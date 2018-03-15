package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TenderExchange")
public class TenderExchange {

    /**
     * The private member variable that will hold the ExchangeDetail.
     */
    @XmlElement(name = "ExchangeDetail")
    private ExchangeDetail exchangeDetail;

    /**
     * Gets the Exchange Detail information of the returned item.
     *
     * @return        Returns the Item Exchange Detail information of returned item.
     */
    public final ExchangeDetail getExchangeDetail() {
        return exchangeDetail;
    }

    /**
     * Sets the Exchange Detail information of the returned item.
     *
     * @param exchangeDetail        The new value for Item Exchange Detail information
     */
    public final void setExchangeDetail(ExchangeDetail exchangeDetail) {
        this.exchangeDetail = exchangeDetail;
    }
    
    public TenderExchange(){
        this.exchangeDetail =new ExchangeDetail();
    }

    /**
     * Overrides the toString() Method.
     * @return The String representation of Return.
     */
    public final String toString() {
        StringBuffer str = new StringBuffer();
        if (null != this.getExchangeDetail()) {
            str.append("ExchangeDetail : ").append(this.exchangeDetail.toString());
        }

        return str.toString();
    }
}
