package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "BankDepositsTotal")
public class BankDepositsTotal {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	@XmlElement(name = "MiddleCollection")
	private MiddleCollection middleCollection;
	
	@XmlElement(name = "DepoistsAdsjustmentPlus")
	private DepositsAdjustmentPlus depositsAdjustmentPlus;
	
	@XmlElement(name = "DepositsAdjustmentMinus")
	private DepositsAdjustmentMinus depositsAdjustmentMinus;
	
	@XmlElement(name = "CashInAmount")
	private CashInAmount cashInAmount;
	
	public final CashInAmount getCashInAmount() {
		return this.cashInAmount;
	}
	public final void setCashInAmount(CashInAmount val) {
		this.cashInAmount = val;
	}
	
	public final DepositsAdjustmentMinus getDepositsAdjustmentMinus() {
		return this.depositsAdjustmentMinus;
	}
	public final void setDepositsAdjustmentMinus(DepositsAdjustmentMinus val) {
		this.depositsAdjustmentMinus = val;
	}
	
	public final DepositsAdjustmentPlus getDepositsAdjustmentPlus() {
		return this.depositsAdjustmentPlus;
	}
	public final void setDepositsAdjustmentPlus(DepositsAdjustmentPlus val) {
		this.depositsAdjustmentPlus = val;
	}
	
	public final MiddleCollection getMiddleCollection() {
		return this.middleCollection;
	}
	public final void setMiddleCollection(MiddleCollection val) {
		this.middleCollection = val;
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