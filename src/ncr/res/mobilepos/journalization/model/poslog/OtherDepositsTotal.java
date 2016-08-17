package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "OtherDepositsTotal")
public class OtherDepositsTotal {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	@XmlElement(name = "XebioGroupTicketSales")
	private XebioGroupTicketSales xebioGroupTicketSales;
	
	@XmlElement(name = "TaxFreeMiscIncome")
	private TaxFreeMiscIncome taxFreeMiscIncome;
	
	@XmlElement(name = "MiscIncomeTicketBalance")
	private MiscIncomeTicketBalance miscIncomeTicketBalance;
	
	@XmlElement(name = "Postage")
	private Postage postage;
	
	@XmlElement(name = "CashOnDelivery")
	private CashOnDelivery cashOnDelivery;
	
	@XmlElement(name = "BoxCharges")
	private BoxCharges boxCharges;
	
	public final BoxCharges getBoxCharges() {
		return this.boxCharges;
	}
	
	public final void setBoxCharges(BoxCharges val) {
		this.boxCharges = val;
	}
	
	public final CashOnDelivery getCashOnDelivery() {
		return this.cashOnDelivery;
	}
	public final void setCashOnDelivery(CashOnDelivery cod) {
		this.cashOnDelivery = cod;
	}
	
	public final Postage getPostage() {
		return this.postage;
	}
	public final void setPostage(Postage val) {
		this.postage = val;
	}
	
	public final MiscIncomeTicketBalance getMiscIncomeTicketBalance() {
		return this.miscIncomeTicketBalance;
	}
	public final void setMiscIncomeTicketBalance(MiscIncomeTicketBalance val) {
		this.miscIncomeTicketBalance = val;
	}
	
	public final TaxFreeMiscIncome getTaxFreeMiscIncome() {
		return this.taxFreeMiscIncome;
	}
	public final void setTaxFreeMiscIncome(TaxFreeMiscIncome val) {
		this.taxFreeMiscIncome = val;
	}
	
	public final XebioGroupTicketSales getXebioGroupTicketSales() {
		return this.xebioGroupTicketSales;
	}
	public final void setXebioGroupTicketSales(XebioGroupTicketSales xb) {
		this.xebioGroupTicketSales = xb;
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