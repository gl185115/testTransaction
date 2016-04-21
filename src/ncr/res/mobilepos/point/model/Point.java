package ncr.res.mobilepos.point.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Point")
public class Point extends ResultBase {
	@XmlElement(name = "ItemPointRate")
	private ItemPointRate itemPointRate;
	@XmlElement(name = "TranPointRate")
	private TranPointRate tranPointRate;
	
	public final void setItemPointRate(ItemPointRate itemPointRate) {
		this.itemPointRate = itemPointRate;
	}
	
	public final ItemPointRate getItemPointRate() {
		return itemPointRate;
	}
	
	public final void setTranPointRate(TranPointRate tranPointRate) {
		this.tranPointRate = tranPointRate;
	}
	
	public final TranPointRate getTranPointRate() {
		return tranPointRate;
	}
	
	@Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("ItemPointRate: ").append(itemPointRate).append("; ");
        sb.append("TranPointRate: ").append(tranPointRate).append("; ");
        return sb.toString();
    }
}
