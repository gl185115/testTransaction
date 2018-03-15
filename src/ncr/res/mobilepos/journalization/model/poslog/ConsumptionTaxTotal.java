package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ConsumptionTaxTotal")
public class ConsumptionTaxTotal {
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	@XmlElement(name = "DepositsReceived")
	private String depositsReceived;
	
	@XmlElement(name = "Returned")
	private String returned;
	
	public final String getReturned() {
		return this.returned;
	}
	
	public final void setReturned(String r) {
		this.returned = r;
	}
	
	public final String getDepositsReceived() {
		return this.depositsReceived;
	}
	
	public final void setDepositsReceived(String dr) {
		this.depositsReceived = dr;
	}
	
	public final Amount getAmount() {
		return this.amount;
	}
	
	public final void setAmount(Amount amt) {
		this.amount = amt;
	}
}