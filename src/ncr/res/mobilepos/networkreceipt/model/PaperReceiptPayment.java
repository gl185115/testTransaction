package ncr.res.mobilepos.networkreceipt.model;

/**
 * class that contains details about
 * Payment.
 *
 */
public class PaperReceiptPayment {

    /**
     * credit company code.
     */
    private String crCompanyCode;
    /**
     * credit company name.
     */
    private String companyName;
    /**
     * receiving company code.
     */
    private String recvCompanyCode;
    /**
     * last 4 digits of credit card number.
     */
    private String panLast4;
    /**
     * status of credit authorization.
     */
    private String caStatus;
    /**
     * expiry date of credit card.
     */
    private String expiryMaster;
    /**
     * payment sequence number.
     */
    private String paymentSeq;
    /**
     * paayment approval code.
     */
    private String approvalCode;
    /**
     * trace number.
     */
    private String traceNum;
    /**
     * settlement number.
     */
    private String settlementNum;
    /**
     * amount of credit due.
     */
    private String creditAmount;
    /**
     * @return creditAmount
     */
    public final String getCreditAmount() {
        return creditAmount;
    }
    /**
     * @param creditAmountToSet creditAmount
     */
    public final void setCreditAmount(final String creditAmountToSet) {
        this.creditAmount = creditAmountToSet;
    }
    /**
     * @return crCompanyCode
     */
    public final String getCrCompanyCode() {
        return crCompanyCode;
    }
    /**
     * @param crCompanyCodeToSet crCompanyCode
     */
    public final void setCrCompanyCode(final String crCompanyCodeToSet) {
        this.crCompanyCode = crCompanyCodeToSet;
    }
    /**
     * @return companyName
     */
    public final String getCompanyName() {
        return companyName;
    }
    /**
     * @param companyNameToSet companyName
     */
    public final void setCompanyName(final String companyNameToSet) {
        this.companyName = companyNameToSet;
    }
    /**
     * @return recvCompanyCode
     */
    public final String getRecvCompanyCode() {
        return recvCompanyCode;
    }
    /**
     * @param recvCompanyCodeToSet recvCompanyCode
     */
    public final void setRecvCompanyCode(final String recvCompanyCodeToSet) {
        this.recvCompanyCode = recvCompanyCodeToSet;
    }
    /**
     * @return panLast4
     */
    public final String getPanLast4() {
        return panLast4;
    }
    /**
     * @param panLast4ToSet panLast4
     */
    public final void setPanLast4(final String panLast4ToSet) {
        this.panLast4 = panLast4ToSet;
    }
    /**
     * @return caStatus
     */
    public final String getCaStatus() {
        return caStatus;
    }
    /**
     * @param caStatusToSet caStatus
     */
    public final void setCaStatus(final String caStatusToSet) {
        this.caStatus = caStatusToSet;
    }
    /**
     * @return expiryMaster
     */
    public final String getExpiryMaster() {
        return expiryMaster;
    }
    /**
     * @param expiryMasterToSet expiryMaster
     */
    public final void setExpiryMaster(final String expiryMasterToSet) {
        this.expiryMaster = expiryMasterToSet;
    }
    /**
     * @return paymentSeq
     */
    public final String getPaymentSeq() {
        return paymentSeq;
    }
    /**
     * @param paymentSeqToSet paymentSeq
     */
    public final void setPaymentSeq(final String paymentSeqToSet) {
        this.paymentSeq = paymentSeqToSet;
    }
    /**
     * @return approvalCode
     */
    public final String getApprovalCode() {
        return approvalCode;
    }
    /**
     * @param approvalCodeToSet approvalCode
     */
    public final void setApprovalCode(final String approvalCodeToSet) {
        this.approvalCode = approvalCodeToSet;
    }
    /**
     * @return traceNum
     */
    public final String getTraceNum() {
        return traceNum;
    }
    /**
     * @param traceNumToSet traceNum
     */
    public final void setTraceNum(final String traceNumToSet) {
        this.traceNum = traceNumToSet;
    }
    /**
     * @return settlementNum
     */
    public final String getSettlementNum() {
        return settlementNum;
    }
    /**
     * @param settlementNumToSet settlementNum
     */
    public final void setSettlementNum(final String settlementNumToSet) {
        this.settlementNum = settlementNumToSet;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Approval=" + approvalCode);
        sb.append("\nCA Status=" + caStatus);
        sb.append("\nCompany Name=" + companyName);
        sb.append("\nCompany Code=" + crCompanyCode);
        sb.append("\nCredit Amount=" + creditAmount);
        sb.append("\nExpiry=" + expiryMaster);
        sb.append("\nPAN last 4=" + panLast4);
        sb.append("\nPayment Seq=" + paymentSeq);
        sb.append("\nReceiving Company Code=" + recvCompanyCode);
        sb.append("\nSettlement Number=" + settlementNum);
        sb.append("\nTrace Number=" + traceNum);
        return sb.toString();
    }
}
