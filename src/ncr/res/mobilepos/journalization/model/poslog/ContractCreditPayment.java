package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ContractCreditPayment")
public class ContractCreditPayment {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	@XmlElement(name = "Sales")
	private Sales sales;
	
	@XmlElement(name = "ShoppingLoan")
	private ShoppingLoan shoppingLoan;
	
	@XmlElement(name = "Edy")
	private Edy edy;
	
	@XmlElement(name = "SuicaPASMO")
	private SuicaPASMO suicaPASMO;
	
	@XmlElement(name = "WAON")
	private WAON wAON;
	
	@XmlElement(name = "CUP")
	private CUP cUP;
	
	@XmlElement(name = "TenantCredit1")
	private TenantCredit1 tenantCredit1;
	
	public final TenantCredit1 getTenantCredit1() {
		return this.tenantCredit1;
	}
	public final void setTenantCredit1(TenantCredit1 tc) {
		this.tenantCredit1 = tc;
	}
	
	public final CUP getCUP() {
		return this.cUP;
	}
	public final void setCUP(CUP c) {
		this.cUP = c;
	}
	
	public final WAON getWAON() {
		return this.wAON;
	}
	public final void setWAON(WAON w) {
		this.wAON = w;
	}
	
	public final SuicaPASMO getSuicaPASMO() {
		return this.suicaPASMO;
	}
	public final void setSuicaPASMO(SuicaPASMO sp) {
		this.suicaPASMO = sp;
	}
	
	public final Edy getEdy() {
		return this.edy;
	}
	public final void setEdy(Edy e) {
		this.edy = e;
	}
	
	public final ShoppingLoan getShoppingLoan() {
		return this.shoppingLoan;
	}
	public final void setShoppingLoan(ShoppingLoan sl) {
		this.shoppingLoan = sl;
	}
	
	public final Sales getSales() {
		return this.sales;
	}
	public final void setSales(Sales sl) {
		this.sales = sl;
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
	public final void setCount(String val) {
		this.count = val;
	}
}
