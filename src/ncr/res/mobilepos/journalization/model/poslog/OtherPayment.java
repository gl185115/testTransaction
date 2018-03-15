package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "OtherPayment")
public class OtherPayment {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	@XmlElement(name = "OtherGiftCertificate")
	private OtherGiftCertificate otherGiftCertificate;
	
	@XmlElement(name = "TransferPayment")
	private TransferPayment transferPayment;
	
	@XmlElement(name = "CashOnDelivery")
	private CashOnDelivery cashOnDelivery;
	
	public final CashOnDelivery getCashOnDelivery() {
		return this.cashOnDelivery;
	}
	public final void setCashOnDelivery(CashOnDelivery cod) {
		this.cashOnDelivery = cod;
	}
	
	public final TransferPayment getTransferPayment() {
		return this.transferPayment;
	}
	public final void setTransferPayment(TransferPayment tp) {
		this.transferPayment = tp;
	}
	
	public final OtherGiftCertificate getOtherGiftCertificate() {
		return this.otherGiftCertificate;
	}
	public final void setOtherGiftCertificate(OtherGiftCertificate ogc) {
		this.otherGiftCertificate = ogc;
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