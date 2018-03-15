package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DetailsDiscountTotal")
public class DetailsDiscountTotal {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	@XmlElement(name = "ChobiTagDiscount")
	private ChobiTagDiscount chobiTagDiscount;
	
	@XmlElement(name = "TradeInDiscountRate")
	private TradeInDiscountRate tradeInDiscountRate;
	
	@XmlElement(name = "TradeInDiscountAmount")
	private TradeInDiscountAmount tradeInDiscountAmount;
	
	@XmlElement(name = "PurchaseReplacementSupport")
	private PurchaseReplacementSupport purchaseReplacementSupport;
	
	@XmlElement(name = "LimitedDiscount")
	private LimitedDiscount limitedDiscount;
	
	@XmlElement(name = "DiscountDetails")
	private DiscountDetails discountDetails;
		
	public final DiscountDetails getDiscountDetails() {
		return this.discountDetails;
	}
	public final void setDiscountDetails(DiscountDetails dd) {
		this.discountDetails = dd;
	}
	
	public final LimitedDiscount getLimitedDiscount() {
		return this.limitedDiscount;
	}
	public final void setLimitedDiscount(LimitedDiscount ld) {
		this.limitedDiscount = ld;
	}
	
	public final PurchaseReplacementSupport getPurchaseReplacementSupport() {
		return this.purchaseReplacementSupport;
	}
	public final void setPurchaseReplacementSupport(PurchaseReplacementSupport val) {
		this.purchaseReplacementSupport = val;
	}
	
	public final TradeInDiscountAmount getTradeInDiscountAmount() {
		return this.tradeInDiscountAmount;
	}
	public final void setTradeInDiscountAmount(TradeInDiscountAmount val) {
		this.tradeInDiscountAmount = val;
	}
	
	public final TradeInDiscountRate getTradeInDiscountRate() {
		return this.tradeInDiscountRate;
	}
	public final void setTradeInDiscountRate(TradeInDiscountRate val) {
		this.tradeInDiscountRate = val;
	}
	
	public final ChobiTagDiscount getChobiTagDiscount() {
		return this.chobiTagDiscount;
	}
	public final void setChobiTagDiscount(ChobiTagDiscount val) {
		this.chobiTagDiscount = val;
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