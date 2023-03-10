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
@XmlRootElement(name = "sale")
@ApiModel(value="sale")
public class Sale {

	@XmlElementWrapper(name = "items")
    @XmlElement(name = "item")
    private List<ItemSale> sales = new ArrayList<ItemSale>();

    public List<ItemSale> getSales() {
        return sales;
    }

    public void setSales(List<ItemSale> sales) {
        this.sales = sales;
    }
}
