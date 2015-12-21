package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "CreditPayment")
public class CreditPayment {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	@XmlElement(name = "Sales")
	private Sales sales;
	
	@XmlElement(name = "XebioCard")
	private XebioCard xebioCard;
	
	public final XebioCard getXebioCard() {
		return this.xebioCard;
	}
	
	public final void setXebioCard(XebioCard xc) {
		this.xebioCard = xc;
	}
	
	public final Sales getSales() {
		return this.sales;
	}
	
	public final void setSales(Sales sales) {
		this.sales = sales;
	}
	
	public final Amount getAmount() {
		return this.amount;
	}
	
	public final void setAmount(Amount amt) {
		this.amount = amt;
	}
	
	public final String getCount() {
		return this.count;
	}
	
	public final void setCount(String cnt) {
		this.count = cnt;
	}
}