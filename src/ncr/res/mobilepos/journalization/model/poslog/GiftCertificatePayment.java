package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GiftCertificatePayment")
public class GiftCertificatePayment {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Sales")
	private Sales sales;
	
	@XmlElement(name = "XebioGroup")
	private XebioGroup xebioGroup;
	
	public final XebioGroup getXebioGroup() {
		return this.xebioGroup;
	}
	public final void setXebioGroup(XebioGroup xg) {
		this.xebioGroup = xg;
	}
	
	public final Sales getSales() {
		return this.sales;
	}
	public final void setSales(Sales s) {
		this.sales = s;
	}
	
	public final String getCount() {
		return this.count;
	}
	public final void setCount(String s) {
		this.count = s;
	}
}