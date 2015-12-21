package ncr.res.mobilepos.journalization.model.poslog;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * TillSettle Model Object.
 *
 * <P>A TillSettle Node in POSLog XML.
 *
 * <P>The TillSettle node is under TenderControlTransaction Node.
 * And holds the start of day and end of day transaction details
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "TillSettle")
public class TillSettle {

    /**
     * Element for start of day and end of day
     */
    @XmlElement(name = "TenderSummary")
    private TenderSummary tenderSummary;

    /**
     * The private member variable that will hold the Tender.
     */
    @XmlElement(name = "Tender")
    private Tender tender;

    /**
     * Transaction count (取引件数)
     */
    @XmlElement(name = "TransactionCount")
    private String transactionCount;

    /**
     * TotalNetSalesAmount
     */
    @XmlElement(name = "TotalNetSalesAmount")
    private String totalNetSalesAmount;

    /**
     * TotalTaxAmount
     */
    @XmlElement(name = "TotalTaxAmount")
    private String totalTaxAmount;

    /**
     * totalGrossSalesExemptTaxAmount
     */
    @XmlElement(name = "TotalGrossSalesExemptTaxAmount")
    private String totalGrossSalesExemptTaxAmount;

    /**
     * GrossPositiveAmount
     */
    @XmlElement(name = "GrossPositiveAmount")
    private String grossPositiveAmount;

    /**
     * barWeather
     */
    @XmlElement(name = "barWeather")
    private String barWeather;

    /**
     * barCustomerTraffic
     */
    @XmlElement(name = "barCustomerTraffic")
    private String barCustomerTraffic;

    /**
     * GrossNegativeAmount
     */
    @XmlElement(name = "GrossNegativeAmount")
    private String grossNegativeAmount;

    /**
     *  Tax exempt (免税)
     */
    @XmlElement(name = "TaxExempt")
    private TaxExempt taxExempt;

    /**
     * Loans (両替持参金)
     */
    @XmlElement(name = "Loans")
    private Loans loans;
    
    @XmlElement(name = "TenderLoan")
    private TenderLoan tenderLoan;

    /**
     * Paid in (募金)
     */
    @XmlElement(name = "paidIn")
    private PaidIn paidIn;

    /**
     * Line item void (取消)
     */
    @XmlElement(name = "LineItemVoids")
    private LineItemVoids lineItemVoids;

    /**
     * Transaction voids (取引取消)
     */
    @XmlElement(name = "TransactionVoids")
    private TransactionVoids transactionVoids;

    /**
     * Tender pickup (釣銭補充)
     */
    @XmlElement(name = "TenderPickup")
    private TenderPickup tenderPickup;

    /**
     * Returns (返品)
     */
    @XmlElement(name = "Returns")
    private Returns returns;

    /**
     * Employee discounts (社員割引)
     */
    @XmlElement(name = "EmployeeDiscounts")
    private EmployeeDiscounts employeeDiscounts;

    /**
     * Miscellaneous discounts (その他割引)
     */
    @XmlElement(name = "MiscellaneousDiscounts")
    private MiscellaneousDiscounts miscellaneousDiscounts;

    /**
     * Store coupons (クーポン)
     */
    @XmlElement(name = "StoreCoupons")
    private StoreCoupons storeCoupons;

    /**
     * Markdowns (値下げ)
     */
    @XmlElement(name = "Markdowns")
    private Markdowns markdowns;

    /**
     * Layaway payments (お取り置き)
     */
    @XmlElement(name = "LayawayPayments")
    private LayawayPayments layawayPayments;

    /**
     * CreditDebit (クレジット)
     */
    @XmlElement(name = "CreditDebit")
    private CreditDebit creditDebit;

    /**
     * UnionPay (銀聯)
     */
    @XmlElement(name = "UnionPay")
    private UnionPay unionPay;
    
    /**
     * Voucher (商品券、金券、ギフトカード)
     */
    @XmlElement(name = "Voucher")
    private List<Voucher> voucher;
    /**
     * Stored value (電子マネー)
     */
    @XmlElement(name = "StoredValue")
    private StoredValue storedValue;

    /**
     * Documentary stamp (印紙)
     */
    @XmlElement(name = "DocumentaryStamp")
    private DocumentaryStamp documentaryStamp;

    /**
     * Return Line item void (返品時取消)
     */
    @XmlElement(name = "ReturnLineItemVoids")
    private ReturnLineItemVoids returnLineItemVoids;

    /**
     * Return Employee discounts (返品時社員割引)
     */
    @XmlElement(name = "ReturnEmployeeDiscounts")
    private ReturnEmployeeDiscounts returnEmployeeDiscounts;

    /**
     * Return Miscellaneous discounts (返品時その他割引)
     */
    @XmlElement(name = "ReturnMiscellaneousDiscounts")
    private ReturnMiscellaneousDiscounts returnMiscellaneousDiscounts;

    /**
     * Container deposits (前受金、内金)
     */
    @XmlElement(name = "ContainerDeposits")
    private ContainerDeposits containerDeposits;

    /**
     * TotalMeasures
     */
    @XmlElement(name = "TotalMeasures")
    private TotalMeasures totalMeasures;

    /**
     * Element that tells the PayOut details
     */
    @XmlElement(name = "PayOut")
    private PayOut payOut;
    
    /**
     * Element that tells the PayIn details
     */
    @XmlElement(name = "PayIn")
    private PayIn payIn;
    
    @XmlElement(name = "PayInPlan")
    private PayInPlan payInPlan;
    
    @XmlElement(name = "TransactionDailyReport")
    private TransactionDailyReport transactionDailyReport;
    
    public final TransactionDailyReport getTransactionDailyReport() {
    	return this.transactionDailyReport;
    }
    
    public final void setTransactionDailyReport(TransactionDailyReport tdr) {
    	this.transactionDailyReport = tdr;
    }
    
    /**
     * voucher
     *
     * @return       voucher
     */
    public final List<Voucher> getVoucher() {
        return voucher;
    }
    /**
     * Voucher (商品券、金券、ギフトカード)
     *
     * @param voucher
     */
    public final void setVoucher(List<Voucher> voucher) {
        this.voucher = voucher;
    }

    /**
     * Gets the tender summary of the till settle
     *
     * @return          		The tender summary of the till settle
     */
    public final TenderSummary getTenderSummary() {
        return tenderSummary;
    }

    /**
     * Sets the tender summary of the till settle
     *
     * @param tenderSummary		The new tender summary to set
     */
    public final void setTenderSummary(TenderSummary tenderSummary) {
        this.tenderSummary = tenderSummary;
    }

    /**
     * Gets the tender of the till settle
     *
     * @return                  The tender of the till settle
     */
    public final Tender getTender() {
        return tender;
    }

    /**
     * Sets the tender  of the till settle
     *
     * @param tender   The new tender  to set
     */
    public final void setTender(Tender tender) {
        this.tender = tender;
    }

    /**
     * @return transactionCount
     */
    public String getTransactionCount() {
        return transactionCount;
    }

    /**
     * @param transactionCount セットする transactionCount
     */
    public void setTransactionCount(String transactionCount) {
        this.transactionCount = transactionCount;
    }

    /**
     * @return totalNetSalesAmount
     */
    public String getTotalNetSalesAmount() {
        return totalNetSalesAmount;
    }

    /**
     * @param totalNetSalesAmount セットする totalNetSalesAmount
     */
    public void setTotalNetSalesAmount(String totalNetSalesAmount) {
        this.totalNetSalesAmount = totalNetSalesAmount;
    }

    /**
     * @return totalTaxAmount
     */
    public String getTotalTaxAmount() {
        return totalTaxAmount;
    }

    /**
     * @param totalTaxAmount セットする totalTaxAmount
     */
    public void setTotalTaxAmount(String totalTaxAmount) {
        this.totalTaxAmount = totalTaxAmount;
    }

    /**
     * @return totalGrossSalesExemptTaxAmount
     */
    public String getTotalGrossSalesExemptTaxAmount() {
        return totalGrossSalesExemptTaxAmount;
    }

    /**
     * @param totalGrossSalesExemptTaxAmount セットする totalGrossSalesExemptTaxAmount
     */
    public void setTotalGrossSalesExemptTaxAmount(
            String totalGrossSalesExemptTaxAmount) {
        this.totalGrossSalesExemptTaxAmount = totalGrossSalesExemptTaxAmount;
    }

    /**
     * @return grossPositiveAmount
     */
    public String getGrossPositiveAmount() {
        return grossPositiveAmount;
    }

    /**
     * @param grossPositiveAmount セットする grossPositiveAmount
     */
    public void setGrossPositiveAmount(String grossPositiveAmount) {
        this.grossPositiveAmount = grossPositiveAmount;
    }

    /**
     * @return barWeather
     */
    public String getBarWeather() {
        return barWeather;
    }

    /**
     * @param barWeather セットする barWeather
     */
    public void setBarWeather(String barWeather) {
        this.barWeather = barWeather;
    }

    /**
     * @return barCustomerTraffic
     */
    public String getBarCustomerTraffic() {
        return barCustomerTraffic;
    }

    /**
     * @param barCustomerTraffic セットする barCustomerTraffic
     */
    public void setBarCustomerTraffic(String barCustomerTraffic) {
        this.barCustomerTraffic = barCustomerTraffic;
    }

    /**
     * @return grossNegativeAmount
     */
    public String getGrossNegativeAmount() {
        return grossNegativeAmount;
    }

    /**
     * @param grossNegativeAmount セットする grossNegativeAmount
     */
    public void setGrossNegativeAmount(String grossNegativeAmount) {
        this.grossNegativeAmount = grossNegativeAmount;
    }

    /**
     * @return taxExempt
     */
    public TaxExempt getTaxExempt() {
        return taxExempt;
    }

    /**
     * @param taxExempt セットする taxExempt
     */
    public void setTaxExempt(TaxExempt taxExempt) {
        this.taxExempt = taxExempt;
    }

    /**
     * @return loans
     */
    public Loans getLoans() {
        return loans;
    }

    /**
     * @param loans セットする loans
     */
    public void setLoans(Loans loans) {
        this.loans = loans;
    }
    
    public final TenderLoan getTenderLoan() {
    	return this.tenderLoan;
    }
    public final void setTenderLoan(TenderLoan tl) {
    	this.tenderLoan = tl;
    }

    /**
     * @return paidIn
     */
    public PaidIn getPaidIn() {
        return paidIn;
    }

    /**
     * @param paidIn セットする paidIn
     */
    public void setPaidIn(PaidIn paidIn) {
        this.paidIn = paidIn;
    }

    /**
     * @return lineItemVoids
     */
    public LineItemVoids getLineItemVoids() {
        return lineItemVoids;
    }

    /**
     * @param lineItemVoids セットする lineItemVoids
     */
    public void setLineItemVoids(LineItemVoids lineItemVoids) {
        this.lineItemVoids = lineItemVoids;
    }

    /**
     * @return transactionVoids
     */
    public TransactionVoids getTransactionVoids() {
        return transactionVoids;
    }

    /**
     * @param transactionVoids セットする transactionVoids
     */
    public void setTransactionVoids(TransactionVoids transactionVoids) {
        this.transactionVoids = transactionVoids;
    }

    /**
     * @return tenderPickup
     */
    public TenderPickup getTenderPickup() {
        return tenderPickup;
    }

    /**
     * @param tenderPickup セットする tenderPickup
     */
    public void setTenderPickup(TenderPickup tenderPickup) {
        this.tenderPickup = tenderPickup;
    }

    /**
     * @return returns
     */
    public Returns getReturns() {
        return returns;
    }

    /**
     * @param returns セットする returns
     */
    public void setReturns(Returns returns) {
        this.returns = returns;
    }

    /**
     * @return employeeDiscounts
     */
    public EmployeeDiscounts getEmployeeDiscounts() {
        return employeeDiscounts;
    }

    /**
     * @param employeeDiscounts セットする employeeDiscounts
     */
    public void setEmployeeDiscounts(EmployeeDiscounts employeeDiscounts) {
        this.employeeDiscounts = employeeDiscounts;
    }

    /**
     * @return miscellaneousDiscounts
     */
    public MiscellaneousDiscounts getMiscellaneousDiscounts() {
        return miscellaneousDiscounts;
    }

    /**
     * @param miscellaneousDiscounts セットする miscellaneousDiscounts
     */
    public void setMiscellaneousDiscounts(
            MiscellaneousDiscounts miscellaneousDiscounts) {
        this.miscellaneousDiscounts = miscellaneousDiscounts;
    }

    /**
     * @return storeCoupons
     */
    public StoreCoupons getStoreCoupons() {
        return storeCoupons;
    }

    /**
     * @param storeCoupons セットする storeCoupons
     */
    public void setStoreCoupons(StoreCoupons storeCoupons) {
        this.storeCoupons = storeCoupons;
    }

    /**
     * @return markdowns
     */
    public Markdowns getMarkdowns() {
        return markdowns;
    }

    /**
     * @param markdowns セットする markdowns
     */
    public void setMarkdowns(Markdowns markdowns) {
        this.markdowns = markdowns;
    }

    /**
     * @return layawayPayments
     */
    public LayawayPayments getLayawayPayments() {
        return layawayPayments;
    }

    /**
     * @param layawayPayments セットする layawayPayments
     */
    public void setLayawayPayments(LayawayPayments layawayPayments) {
        this.layawayPayments = layawayPayments;
    }

    /**
     * @return creditDebit
     */
    public CreditDebit getCreditDebit() {
        return creditDebit;
    }

    /**
     * @param creditDebit セットする creditDebit
     */
    public void setCreditDebit(CreditDebit creditDebit) {
        this.creditDebit = creditDebit;
    }

    /**
     * @return unionPay
     */
    public UnionPay getUnionPay() {
        return unionPay;
    }

    /**
     * @param unionPay セットする unionPay
     */
    public void setUnionPay(UnionPay unionPay) {
        this.unionPay = unionPay;
    }
    
    /**
     * @return storedValue
     */
    public StoredValue getStoredValue() {
        return storedValue;
    }

    /**
     * @param storedValue セットする storedValue
     */
    public void setStoredValue(StoredValue storedValue) {
        this.storedValue = storedValue;
    }

    /**
     * @return documentaryStamp
     */
    public DocumentaryStamp getDocumentaryStamp() {
        return documentaryStamp;
    }

    /**
     * @param documentaryStamp セットする documentaryStamp
     */
    public void setDocumentaryStamp(DocumentaryStamp documentaryStamp) {
        this.documentaryStamp = documentaryStamp;
    }

    /**
     * @return returnLineItemVoids
     */
    public ReturnLineItemVoids getReturnLineItemVoids() {
        return returnLineItemVoids;
    }

    /**
     * @param returnLineItemVoids セットする returnLineItemVoids
     */
    public void setReturnLineItemVoids(ReturnLineItemVoids returnLineItemVoids) {
        this.returnLineItemVoids = returnLineItemVoids;
    }

    /**
     * @return returnEmployeeDiscounts
     */
    public ReturnEmployeeDiscounts getReturnEmployeeDiscounts() {
        return returnEmployeeDiscounts;
    }

    /**
     * @param returnEmployeeDiscounts セットする returnEmployeeDiscounts
     */
    public void setReturnEmployeeDiscounts(
            ReturnEmployeeDiscounts returnEmployeeDiscounts) {
        this.returnEmployeeDiscounts = returnEmployeeDiscounts;
    }

    /**
     * @return returnMiscellaneousDiscounts
     */
    public ReturnMiscellaneousDiscounts getReturnMiscellaneousDiscounts() {
        return returnMiscellaneousDiscounts;
    }

    /**
     * @param returnMiscellaneousDiscounts セットする returnMiscellaneousDiscounts
     */
    public void setReturnMiscellaneousDiscounts(
            ReturnMiscellaneousDiscounts returnMiscellaneousDiscounts) {
        this.returnMiscellaneousDiscounts = returnMiscellaneousDiscounts;
    }

    /**
     * @return containerDeposits
     */
    public ContainerDeposits getContainerDeposits() {
        return containerDeposits;
    }

    /**
     * @param containerDeposits セットする containerDeposits
     */
    public void setContainerDeposits(ContainerDeposits containerDeposits) {
        this.containerDeposits = containerDeposits;
    }

    /**
     * @return totalMeasures
     */
    public TotalMeasures getTotalMeasures() {
        return totalMeasures;
    }

    /**
     * @param totalMeasures セットする totalMeasures
     */
    public void setTotalMeasures(TotalMeasures totalMeasures) {
        this.totalMeasures = totalMeasures;
    }

    /**
     * Gets the payOut details of the tender control transaction
     * @return     The payOut details of the tender control transaction
     */
    public final PayOut getPayOut() {
    	return payOut;
    }
    
    /**
     * Sets the payOut details of the tender control transaction
     * @param payOutToSet     The payOut details of the tender control transaction
     */
    public final void setPayOut(PayOut payOutToSet) {
    	this.payOut = payOutToSet;
    }
    
    /**
     * Gets the payIn details of the tender control transaction
     * @return     The payIn details of the tender control transaction
     */
    public final PayIn getPayIn() {
    	return payIn;
    }
    
    /**
     * Sets the payIn details of the tender control transaction
     * @param payInToSet     The payIn details of the tender control transaction
     */
    public final void setPayIn(PayIn payInToSet) {
    	this.payIn = payInToSet;
    }
    
    public final PayInPlan getPayInPlan() {
    	return payInPlan;
    }
    
    public final void setPayInPlan(PayInPlan payInPlan) {
    	this.payInPlan = payInPlan;
    }
}
