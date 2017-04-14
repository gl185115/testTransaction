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
@XmlRootElement(name = "payment")
@ApiModel(value="payment")
public class Payment {

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<ItemSale> payments = new ArrayList<ItemSale>();

    public List<ItemSale> getPayments() {
        return payments;
    }

    public void setPayments(List<ItemSale> payments) {
        this.payments = payments;
    }
}
