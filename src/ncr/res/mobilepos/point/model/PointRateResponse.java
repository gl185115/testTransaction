package ncr.res.mobilepos.point.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
public class PointRateResponse extends ResultBase{

    @XmlElement(name = "ItemPointRate")
    List<ItemPointRate> itemPointRateList;

    @XmlElement(name = "TranPointRate")
    List<TranPointRate> tranPointRateList;

    /**
     * @return the item point rate
     */
    public List<ItemPointRate> getItemPointRateList() {
        return itemPointRateList;
    }

    /**
     * @param item point rates the itempointlist to set
     */
    public void setItemPointRateList(List<ItemPointRate> itemPointRateList) {
        this.itemPointRateList = itemPointRateList;
    }
    
    /**
     * @return the tran point rate
     */
    public List<TranPointRate> getTranPointRateList() {
        return tranPointRateList;
    }

    /**
     * @param tran point rates the itempointlist to set
     */
    public void setTranPointRateList(List<TranPointRate> tranPointRateList) {
        this.tranPointRateList = tranPointRateList;
    }
}
