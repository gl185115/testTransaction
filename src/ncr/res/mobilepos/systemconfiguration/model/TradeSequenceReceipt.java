package ncr.res.mobilepos.systemconfiguration.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "tradeSequenceReceipt")
@ApiModel(value="tradeSequenceReceipt")
public class TradeSequenceReceipt {

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<ItemSale> receipts = new ArrayList<ItemSale>();

    public List<ItemSale> getReceipts() {
        return receipts;
    }

    public void setReceipts(List<ItemSale> receipts) {
        this.receipts = receipts;
    }
}
