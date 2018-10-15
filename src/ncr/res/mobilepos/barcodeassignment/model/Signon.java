package ncr.res.mobilepos.barcodeassignment.model;

import java.util.ArrayList;
import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlElementWrapper;
import javax.xml.bind.annotation.XmlRootElement;

import com.wordnik.swagger.annotations.ApiModel;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "signon")
@ApiModel(value="signon")
public class Signon {

    @XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<ItemSale> signons = new ArrayList<ItemSale>();

    public List<ItemSale> getSignons() {
        return signons;
    }

    public void setSignons(List<ItemSale> signons) {
        this.signons = signons;
    }
}
