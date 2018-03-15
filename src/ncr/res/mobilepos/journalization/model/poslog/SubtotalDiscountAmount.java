package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SubtotalDiscountAmount")
public class SubtotalDiscountAmount {
	
	@XmlAttribute(name = "Code")
	private String code;
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	@XmlElement(name = "DiscountAmount")
	private DiscountAmount discountAmount;
	
	public final DiscountAmount getDiscountAmount() {
		return this.discountAmount;
	}
	public final void setDiscountAmount(DiscountAmount da) {
		this.discountAmount = da;
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
	public final void setCount(String str) {
		this.count = str;
	}
	
	public final String getCode() {
		return this.code;
	}
	public final void setCode(String str) {
		this.code = str;
	}
	
}