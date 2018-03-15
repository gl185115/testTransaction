package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SalesGeneratedTotal")
public class SalesGeneratedTotal {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	@XmlElement(name = "Cash")
	private Cash cash;
	
	@XmlElement(name = "CashOnDelivery")
	private CashOnDelivery cashOnDelivery;
	
	@XmlElement(name = "Credit")
	private Credit credit;
	
	@XmlElement(name = "DepositsReservation")
	private DepositsReservation depositsReservation;
	
	public final DepositsReservation getDepositsReservation() {
		return this.depositsReservation;
	}
	
	public final void setDepositsReservation(DepositsReservation dr) {
		this.depositsReservation = dr;
	}
	
	public final Credit getCredit() {
		return this.credit;
	}
	
	public final void setCredit(Credit c) {
		this.credit = c;
	}
	
	public final CashOnDelivery getCashOnDelivery() {
		return this.cashOnDelivery;
	}
	
	public final void setCashOnDelivery(CashOnDelivery cod) {
		this.cashOnDelivery = cod;
	}
	
	public final Cash getCash() {
		return this.cash;
	}
	
	public final void setCash(Cash cash) {
		this.cash = cash;
	}
	
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