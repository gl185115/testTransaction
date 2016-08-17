package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TransactionDailyReport")
public class TransactionDailyReport {
	
	@XmlElement(name = "NetSalesTotal")
	private NetSalesTotal netSalesTotal;
	
	@XmlElement(name = "SalesGeneratedTotal")
	private SalesGeneratedTotal salesGeneratedTotal;
	
	@XmlElement(name = "SalesReturnGeneratedTotal")
	private SalesReturnGeneratedTotal salesReturnGeneratedTotal;
	
	@XmlElement(name = "ConsumptionTaxTotal")
	private ConsumptionTaxTotal consumptionTaxTotal;
	
	@XmlElement(name = "CreditPayment")
	private CreditPayment creditPayment;
	
	@XmlElement(name = "ContractCreditPayment")
	private ContractCreditPayment contractCreditpayment;
	
	@XmlElement(name = "GiftCertificatePayment")
	private GiftCertificatePayment giftCertificatePayment;
	
	@XmlElement(name = "OtherPayment")
	private OtherPayment otherPayment;
	
	@XmlElement(name = "PurchaseTotal")
	private PurchaseTotal purchaseTotal;
	
	@XmlElement(name = "DepositsReceivedTotal")
	private DepositsReceivedTotal depositsReceivedTotal;
	
	@XmlElement(name = "DepositsCashReturnTotal")
	private DepositsCashReturnTotal depositsCashReturnTotal;
	
	@XmlElement(name = "OtherDepositsTotal")
	private OtherDepositsTotal otherDepositsTotal;
	
	@XmlElement(name = "OtherWithdrawalsTotal")
	private OtherWithdrawalsTotal otherWithdrawalsTotal;
	
	@XmlElement(name = "BankDepositsTotal")
	private BankDepositsTotal bankDepositsTotal;
	
	@XmlElement(name = "CashOnHand")
	private String cashOnHand;
	
	@XmlElement(name = "ChangeReserve")
	private String changeReserve;
	
	@XmlElement(name = "ReceiptCount")
	private String receiptCount;
	
	@XmlElement(name = "ExchangeCount")
	private String exchangeCount;
	
	@XmlElement(name = "CancellationCount")
	private String cancellationCount;
	
	@XmlElement(name = "DutyFreeTransaction")
	private DutyFreeTransaction dutyFreeTransaction;
	
	@XmlElement(name = "SalesReturn")
	private SalesReturn salesReturn;
	
	@XmlElement(name = "SalesVoided")
	private SalesVoided salesVoided;
	
	@XmlElement(name = "SalesVoidedReturn")
	private SalesVoidedReturn salesVoidedReturn;
	
	@XmlElement(name = "DepositsCancellation")
	private DepositsCancellation depositsCancellation;
	
	@XmlElement(name = "ExchangeReturn")
	private ExchangeReturn exchangeReturn;
	
	@XmlElement(name = "CashIn")
	private CashIn cashIn;
	
	@XmlElement(name = "CashInVoid")
	private CashInVoid cashInVoid;
	
	@XmlElement(name = "CashOut")
	private CashOut cashOut;
	
	@XmlElement(name = "CashOutVoid")
	private CashOutVoid cashOutVoid;
	
	@XmlElement(name = "GroupTicketBalance")
	private GroupTicketBalance groupTicketBalance;
	
	@XmlElement(name = "DiscountTicketIssue")
	private DiscountTicketIssue discountTicketIssue;
	
	@XmlElement(name = "PremiumTicketIssue")
	private PremiumTicketIssue premiumTicketIssue;
	
	@XmlElement(name = "DetailsDiscountTotal")
	private DetailsDiscountTotal detailsDiscountTotal;
	
	@XmlElement(name = "SubtotalDiscountTotal")
	private SubtotalDiscountTotal subtotalDiscountTotal;
	
	public final SubtotalDiscountTotal getSubtotalDiscountTotal() {
		return this.subtotalDiscountTotal;
	}
	public final void setSubtotalDiscountTotal(SubtotalDiscountTotal val) {
		this.subtotalDiscountTotal = val;
	}
	
	public final DetailsDiscountTotal getDetailsDiscountTotal() {
		return this.detailsDiscountTotal;
	}
	public final void setDetailsDiscountTotal(DetailsDiscountTotal val) {
		this.detailsDiscountTotal = val;
	}
	
	public final PremiumTicketIssue getPremiumTicketIssue() {
		return this.premiumTicketIssue;
	}
	public final void setPremiumTicketIssue(PremiumTicketIssue val) {
		this.premiumTicketIssue = val;
	}
	
	public final DiscountTicketIssue getDiscountTicketIssue() {
		return this.discountTicketIssue;
	}
	public final void setDiscountTicketissue(DiscountTicketIssue val) {
		this.discountTicketIssue = val;
	}
	
	public final GroupTicketBalance getGroupTicketBalance() {
		return this.groupTicketBalance;
	}
	public final void setGroupTicketBalance(GroupTicketBalance val) {
		this.groupTicketBalance = val;
	}
	
	public final CashOutVoid getCashOutVoid() {
		return this.cashOutVoid;
	}
	public final void setCashOutVoid(CashOutVoid val) {
		this.cashOutVoid = val;
	}
	
	public final CashOut getCashOut() {
		return this.cashOut;
	}
	public final void setCashOut(CashOut val) {
		this.cashOut = val;
	}
	
	public final CashInVoid getCashInVoid() {
		return this.cashInVoid;
	}
	public final void setCashInVoid(CashInVoid civ) {
		this.cashInVoid = civ;
	}
	
	public final CashIn getCashIn() {
		return this.cashIn;
	}
	public final void setCashIn(CashIn val) {
		this.cashIn = val;
	}
	
	public final ExchangeReturn getExchangeReturn() {
		return this.exchangeReturn;
	}
	public final void setExchangeReturn(ExchangeReturn val) {
		this.exchangeReturn = val;
	}
	
	public final DepositsCancellation getDepositsCancellation() {
		return this.depositsCancellation;
	}
	public final void setDepositsCancellation(DepositsCancellation val) {
		this.depositsCancellation = val;
	}
	
	public final SalesVoidedReturn getSalesVoidedReturn() {
		return this.salesVoidedReturn;
	}
	public final void setSalesVoidedReturn(SalesVoidedReturn val) {
		this.salesVoidedReturn = val;
	}
	
	public final SalesVoided getSalesVoided() {
		return this.salesVoided;
	}
	public final void setSalesVoided(SalesVoided val) {
		this.salesVoided = val;
	}
	
	public final SalesReturn getSalesReturn() {
		return this.salesReturn;
	}
	public final void setSalesReturn(SalesReturn val) {
		this.salesReturn = val;
	}
	
	public final DutyFreeTransaction getDutyFreeTransaction() {
		return this.dutyFreeTransaction;
	}
	public final void setDutyFreeTransaction(DutyFreeTransaction val) {
		this.dutyFreeTransaction = val;
	}
	
	public final String getCancellationCount() {
		return this.cancellationCount;
	}
	public final void setCancellationCount(String val) {
		this.cancellationCount = val;
	}
	
	public final String getExchangeCount() {
		return this.exchangeCount;
	}
	public final void setExchangeCount(String val) {
		this.exchangeCount = val;
	}
	
	public final String getReceiptCount() {
		return this.receiptCount;
	}
	public final void setReceiptCount(String val) {
		this.receiptCount = val;
	}
	
	public final String getChangeReserve() {
		return this.changeReserve;
	}
	public final void setChangeReserve(String val) {
		this.changeReserve = val;
	}
	
	public final String getCashOnHand() {
		return this.cashOnHand;
	}
	public final void setCashOnHand(String val) {
		this.cashOnHand = val;
	}
	
	public final BankDepositsTotal getBankDepositsTotal() {
		return this.bankDepositsTotal;
	}
	public final void setBankDepositsTotal(BankDepositsTotal val) {
		this.bankDepositsTotal = val;
	}
	
	public final OtherWithdrawalsTotal getOtherWithdrawalsTotal() {
		return this.otherWithdrawalsTotal;
	}
	public final void setOtherWithdrawalsTotal(OtherWithdrawalsTotal val) {
		this.otherWithdrawalsTotal = val;
	}
	
	public final OtherDepositsTotal getOtherDepositsTotal() {
		return this.otherDepositsTotal;
	}
	public final void setOtherDepositsTotal(OtherDepositsTotal val) {
		this.otherDepositsTotal = val;
	}
	
	public final DepositsCashReturnTotal getDepositsCashReturnTotal() {
		return this.depositsCashReturnTotal;
	}
	public final void setDepositsCashReturnTotal(DepositsCashReturnTotal val) {
		this.depositsCashReturnTotal = val;
	}
	
	public final DepositsReceivedTotal getDepositsReceivedTotal() {
		return this.depositsReceivedTotal;
	}
	public final void setDepositsReceivedTotal(DepositsReceivedTotal val) {
		this.depositsReceivedTotal = val;
	}
	
	public final PurchaseTotal getPurchaseTotal() {
		return this.purchaseTotal;
	}
	public final void setPurchaseTotal(PurchaseTotal val) {
		this.purchaseTotal = val;
	}
	
	public final OtherPayment getOtherPayment() {
		return this.otherPayment;
	}
	public final void setOtherPayment(OtherPayment val) {
		this.otherPayment = val;
	}
	
	public final GiftCertificatePayment getGiftCertificatePayment() {
		return this.giftCertificatePayment;
	}
	public final void setGiftCertificatePayment(GiftCertificatePayment val) {
		this.giftCertificatePayment = val;
	}
	
	public final ContractCreditPayment getContractCreditPayment() {
		return this.contractCreditpayment;
	}
	public final void setContractCreditPayment(ContractCreditPayment val) {
		this.contractCreditpayment = val;
	}
	
	public final CreditPayment getCreditPayment() {
		return this.creditPayment;
	}
	public final void setCreditPayment(CreditPayment val) {
		this.creditPayment = val;
	}
	
	public final ConsumptionTaxTotal getConsumptionTaxTotal() {
		return this.consumptionTaxTotal;
	}
	public final void setConsumptionTaxTotal(ConsumptionTaxTotal val) {
		this.consumptionTaxTotal = val;
	}
	
	public final SalesReturnGeneratedTotal getSalesReturnGeneratedTotal() {
		return this.salesReturnGeneratedTotal;
	}
	public final void setSalesReturnGeneratedTotal(SalesReturnGeneratedTotal val) {
		this.salesReturnGeneratedTotal = val;
	}
	
	public final SalesGeneratedTotal getSalesGeneratedTotal() {
		return this.salesGeneratedTotal;
	}
	public final void setSalesGeneratedtotal(SalesGeneratedTotal val) {
		this.salesGeneratedTotal = val;
	}
	
	public final NetSalesTotal getNetSalesTotal() {
		return this.netSalesTotal;
	}
	public final void setNetSalesTotal(NetSalesTotal val) {
		this.netSalesTotal = val;
	}
	
}