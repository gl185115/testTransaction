package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "NetSalesTotal")
public class NetSalesTotal {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	public final String getCount() {
		return this.count;
	}
	
	public final void setCount(String cnt) {
		this.count = cnt;
	}
	
	public final Amount getAmount() {
		return this.amount;
	}
	
	public void setAmount(Amount amt) {
		this.amount = amt;
	}
}