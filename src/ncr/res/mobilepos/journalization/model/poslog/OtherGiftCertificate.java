package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "OtherGiftCertificate")
public class OtherGiftCertificate {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	@XmlElement(name = "GiftCertificate")
	private GiftCertificate giftCertificate;
	
	@XmlElement(name = "Other")
	private Other other;
	
	public final Other getOther() {
		return this.other;
	}
	public final void setOther(Other o) {
		this.other = o;
	}
	
	public final GiftCertificate getGiftCertificate() {
		return this.giftCertificate;
	}
	public final void setGiftCertificate(GiftCertificate gc) {
		this.giftCertificate = gc;
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
	
}