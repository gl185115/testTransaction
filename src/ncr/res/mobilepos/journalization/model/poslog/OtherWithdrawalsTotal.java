package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "OtherWithdrawalsTotal")
public class OtherWithdrawalsTotal {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	@XmlElement(name = "TenantDeposits")
	private TenantDeposits tenantDeposits;
	
	@XmlElement(name = "GCampaign")
	private GCampaign gCampaign;
	
	public final GCampaign getGCampaign() {
		return this.gCampaign;
	}
	public final void setGCampaign(GCampaign gc) {
		this.gCampaign = gc;
	}
	
	public final TenantDeposits getTenantDeposits() {
		return this.tenantDeposits;
	}
	public final void setTenantDeposits(TenantDeposits val) {
		this.tenantDeposits = val;
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