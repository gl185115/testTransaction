package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "SubtotalDiscountTotal")
public class SubtotalDiscountTotal {
	
	@XmlElement(name = "Count")
	private String count;
	
	@XmlElement(name = "Amount")
	private Amount amount;
	
	@XmlElement(name = "CouponDiscount")
	private CouponDiscount couponDiscount;
	
	@XmlElement(name = "PointTicketXebio")
	private PointTicketXebio pointTicketXebio;
	
	@XmlElement(name = "PointMortgage")
	private PointMortgage pointMortgage;
	
	@XmlElement(name = "GPCoupon")
	private GPCoupon gPCoupon;
	
	@XmlElement(name = "PremiumDiscount")
	private PremiumDiscount premiumDiscount;
	
	@XmlElement(name = "Discount_71")
	private Discount_71 discount_71;
	
	@XmlElement(name = "CompanyEmployeeSpecialDiscountVictoria")
	private CompanyEmployeeSpecialDiscountVictoria companyEmployeeSpecialDiscountVictoria;
	
	@XmlElement(name = "SpecialDiscountComplimentaryTickets")
	private SpecialDiscountComplimentaryTickets specialDicountComplimentaryTickets;
	
	@XmlElement(name = "DMDiscount_74")
	private DMDiscount_74 dMDiscount_74;
	
	@XmlElement(name = "CorporateDiscount")
	private CorporateDiscount corporateDiscount;
	
	@XmlElement(name = "DMDiscount_76")
	private DMDiscount_76 dMDiscount_76;
	
	@XmlElement(name = "DMDiscount_77")
	private DMDiscount_77 dMDiscount_77;
	
	@XmlElement(name = "DiscountTicket_78")
	private DiscountTicket_78 discountTicket_78;
	
	@XmlElement(name = "DMDiscount_79")
	private DMDiscount_79 dMDiscount_79;
	
	@XmlElement(name = "LimitedSalesPromotion")
	private LimitedSalesPromotion limitedSalesPromotion;
	
	@XmlElement(name = "StockholderPreferentialDiscountVictoria")
	private StockholderPreferentialDiscountVictoria stockholderPreferentialDiscountVictoria;
	
	@XmlElement(name = "XebioDiscount")
	private XebioDiscount xebioDiscount;
	
	@XmlElement(name = "Discount_83")
	private Discount_83 discount_83;
	
	@XmlElement(name = "StudentDiscount")
	private StudentDiscount studentDiscount;
	
	@XmlElement(name = "SpecialDiscount")
	private SpecialDiscount specialDiscount;
	
	@XmlElement(name = "Discount_86")
	private Discount_86 discount_86;
	
	@XmlElement(name = "Discount_87")
	private Discount_87 discount_87;
	
	@XmlElement(name = "Discount_88")
	private Discount_88 discount_88;
	
	@XmlElement(name = "Discount_89")
	private Discount_89 discount_89;
	
	@XmlElement(name = "DiscountTicket_92")
	private DiscountTicket_92 discountTicket_92;
	
	@XmlElement(name = "SubtotalDiscountRate")
	private SubtotalDiscountRate subtotalDiscountRate;
	
	@XmlElement(name = "SubtotalDiscountAmount")
	private SubtotalDiscountAmount subtotalDiscountAmount;
	
	public final SubtotalDiscountAmount getSubtotalDiscountAmount() {
		return this.subtotalDiscountAmount;
	}
	
	public final SubtotalDiscountRate getSubtotalDiscountRate() {
		return this.subtotalDiscountRate;
	}
	public final void setSubtotalDiscountRate(SubtotalDiscountRate val) {
		this.subtotalDiscountRate = val;
	}
	
	public final DiscountTicket_92 getDiscountTicket_92() {
		return this.discountTicket_92;
	}
	public final void setDiscountTicket_92(DiscountTicket_92 val) {
		this.discountTicket_92 = val;
	}
	
	public final Discount_89 getDiscount_89() {
		return this.discount_89;
	}
	public final void setDiscount_89(Discount_89 val) {
		this.discount_89 = val;
	}
	
	public final Discount_88 getDiscount_88() {
		return this.discount_88;
	}
	public final void setDiscount_88(Discount_88 val) {
		this.discount_88 = val;
	}
	
	public final Discount_87 getDiscount_87() {
		return this.discount_87;
	}
	public final void setDiscount_87(Discount_87 val) {
		this.discount_87 = val;
	}
	
	public final Discount_86 getDiscount_86() {
		return this.discount_86;
	}
	public final void setDiscount_86(Discount_86 val) {
		this.discount_86 = val;
	}
	
	public final SpecialDiscount getSpecialDiscount() {
		return this.specialDiscount;
	}
	public final void setSpecialDiscount(SpecialDiscount val) {
		this.specialDiscount = val;
	}
	
	public final StudentDiscount getStudentDiscount() {
		return this.studentDiscount;
	}
	public final void setStudentDiscount(StudentDiscount val) {
		this.studentDiscount = val;
	}
	
	public final Discount_83 getDiscount_83() {
		return this.discount_83;
	}
	public final void setDiscount_83(Discount_83 val) {
		this.discount_83 = val;
	}
	
	public final XebioDiscount getXebioDiscount() {
		return this.xebioDiscount;
	}
	public final void setXebioDiscount(XebioDiscount val) {
		this.xebioDiscount = val;
	}
	
	public final StockholderPreferentialDiscountVictoria getStockholderPreferentialDiscountVictoria() {
		return this.stockholderPreferentialDiscountVictoria;
	}
	public final void setStockholderPreferentialDiscountVictoria(StockholderPreferentialDiscountVictoria val) {
		this.stockholderPreferentialDiscountVictoria = val;
	}
	
	public final LimitedSalesPromotion getLimitedSalesPromotion() {
		return this.limitedSalesPromotion;
	}
	public final void setLimittedSalesPromotion(LimitedSalesPromotion val) {
		this.limitedSalesPromotion = val;
	}
	
	public final DMDiscount_79 getDMDiscount_79() {
		return this.dMDiscount_79;
	}
	public final void setDMDiscount_79(DMDiscount_79 val) {
		this.dMDiscount_79 = val;
	}
	
	public final DiscountTicket_78 getDiscountTicket_78() {
		return this.discountTicket_78;
	}
	public final void setDiscountTicket_78(DiscountTicket_78 val) {
		this.discountTicket_78 = val;
	}
	
	public final DMDiscount_77 getDMDiscount_77() {
		return this.dMDiscount_77;
	}
	public final void setDMDicount_77(DMDiscount_77 val) {
		this.dMDiscount_77 = val;
	}
	
	public final DMDiscount_76 getDMDiscount_76() {
		return this.dMDiscount_76;
	}
	public final void setDMDiscount_76(DMDiscount_76 val) {
		this.dMDiscount_76 = val; 
	}
	
	public final CorporateDiscount getCorporateDiscount() {
		return this.corporateDiscount;
	}
	public final void setCorporateDiscount(CorporateDiscount val) {
		this.corporateDiscount = val;
	}
	
	
	public final DMDiscount_74 getDMDiscount_74() {
		return this.dMDiscount_74;
	}
	public final void setDMDiscount_74(DMDiscount_74 val) {
		this.dMDiscount_74 = val;
	}
	
	public final SpecialDiscountComplimentaryTickets getSpecialDiscountComplimentaryTickets() {
		return this.specialDicountComplimentaryTickets;
	}
	public final void setSpecialDiscountComplimentaryTickets(SpecialDiscountComplimentaryTickets val) {
		this.specialDicountComplimentaryTickets = val;
	}
	
	public final CompanyEmployeeSpecialDiscountVictoria getCompanyEmployeeSpecialDiscountVictoria() {
		return this.companyEmployeeSpecialDiscountVictoria;
	}
	public final void setCompanyEmployeeSpcialDiscountVictoria(CompanyEmployeeSpecialDiscountVictoria val) {
		this.companyEmployeeSpecialDiscountVictoria = val;
	}
	
	public final Discount_71 getDiscount_71() {
		return this.discount_71;
	}
	public final void setDiscount_71(Discount_71 val) {
		this.discount_71 = val;
	}
	
	public final PremiumDiscount getPremiumDiscount() {
		return this.premiumDiscount;
	}
	public final void setPremiumDiscount(PremiumDiscount val) {
		this.premiumDiscount = val;
	}
	
	public final GPCoupon getGPCoupon() {
		return this.gPCoupon;
	}
	public final void setGPCoupon(GPCoupon val) {
		this.gPCoupon = val;
	}
	
	public final PointMortgage getPointMortgage() {
		return this.pointMortgage;
	}
	public final void setPointMortgage(PointMortgage val) {
		this.pointMortgage = val;
	}
	
	public final PointTicketXebio getPointTicketXebio() {
		return this.pointTicketXebio;
	}
	public final void setPointTicketXebio(PointTicketXebio val) {
		this.pointTicketXebio = val;
	}
	
	public final CouponDiscount getCouponDiscount() {
		return this.couponDiscount;
	}
	public final void setCouponDiscount(CouponDiscount val) {
		this.couponDiscount = val;
	}
	
	public final Amount getAmount() {
		return this.amount;
	}
	public final void setAmount(Amount amt) {
		this.amount = amt;
	}
	
	public final String getcount() {
		return this.count;
	}
	public final void setCount(String str) {
		this.count = str;
	}
	
}