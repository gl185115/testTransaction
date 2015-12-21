package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PurchaseTotal")
public class PurchaseTotal {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	@XmlElement(name = "PurchaseStocks")
	private PurchaseStocks purchaseStocks;
	
	@XmlElement(name = "PurchaseConsumptionTax")
	private String purchaseConsumptionTax;
	
	public final String getPurchaseConsumptionTax() {
		return this.purchaseConsumptionTax;
	}
	public final void setPurchaseConsumptionTax(String str) {
		this.purchaseConsumptionTax = str;
	}
	
	public final PurchaseStocks getPurchaseStocks() {
		return this.purchaseStocks;
	}
	public final void setPurchaseStocks(PurchaseStocks ps) {
		this.purchaseStocks = ps;
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