package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SalesReturnGeneratedTotal")
public class SalesReturnGeneratedTotal {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	@XmlElement(name = "CashReturn")
	private CashReturn cashReturn;
	
	public final CashReturn getCashReturn() {
		return this.cashReturn;
	}
	
	public final void setCashReturn(CashReturn cr) {
		this.cashReturn = cr;
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
	
	public final void setCount(String count) {
		this.count =  count;
	}
}