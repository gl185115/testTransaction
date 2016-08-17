/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.networkreceipt.model;

/**
 * PaperReceiptCredit Class is a Model representation of the
 * information which identifies the credit information of paper receipt.
 */
public class PaperReceiptCredit {
    /**
     * credit card company.
     */
    private String creditCompany;
    /**
     * jcb.
     */
    private String jcb;
    /**
     * credit card number.
     */
    private String creditNum;
    /**
     * credit card expiration.
     */
    private String creditExpiration;
    /**
     * credit transaction slip number.
     */
    private String creditSlipNum;
    /**
     * credit amount.
     */
    private String creditAmount;
    /**
     * approval number.
     */
    private String approvalNum;
    /**
     * credit transaction process number.
     */
    private String creditProcessNum;
    /**
     * terminal identification number.
     */
    private String terminalIdentificationNum;
    /**
     * settlement manage number.
     */
    private String settlementManageNum;

    /**
     * @return creditCompany
     */
    public final String getCreditCompany() {
        return creditCompany;
    }
    /**
     * @param creditCompanyToSet  creditCompany
     */
    public final void setCreditCompany(final String creditCompanyToSet) {
        this.creditCompany = creditCompanyToSet;
    }
    /**
     * @return jcb
     */
    public final String getJcb() {
        return jcb;
    }
    /**
     * @param jcbToSet  jcb
     */
    public final void setJcb(final String jcbToSet) {
        this.jcb = jcbToSet;
    }
    /**
     * @return creditNum
     */
    public final String getCreditNum() {
        return creditNum;
    }
    /**
     * @param creditNumToSet  creditNum
     */
    public final void setCreditNum(final String creditNumToSet) {
        this.creditNum = creditNumToSet;
    }
    /**
     * @return creditExpiration
     */
    public final String getCreditExpiration() {
        return creditExpiration;
    }
    /**
     * @param creditExpirationToSet  creditExpiration
     */
    public final void setCreditExpiration(final String creditExpirationToSet) {
        this.creditExpiration = creditExpirationToSet;
    }
    /**
     * @return creditSlipNum
     */
    public final String getCreditSlipNum() {
        return creditSlipNum;
    }
    /**
     * @param creditSlipNumToSet  creditSlipNum
     */
    public final void setCreditSlipNum(final String creditSlipNumToSet) {
        this.creditSlipNum = creditSlipNumToSet;
    }
    /**
     * @return creditAmount
     */
    public final String getCreditAmount() {
        return creditAmount;
    }
    /**
     * @param creditAmountToSet  creditAmount
     */
    public final void setCreditAmount(final String creditAmountToSet) {
        this.creditAmount = creditAmountToSet;
    }
    /**
     * @return approvalNum
     */
    public final String getApprovalNum() {
        return approvalNum;
    }
    /**
     * @param approvalNumToSet  approvalNum
     */
    public final void setApprovalNum(final String approvalNumToSet) {
        this.approvalNum = approvalNumToSet;
    }
    /**
     * @return creditProcessNum
     */
    public final String getCreditProcessNum() {
        return creditProcessNum;
    }
    /**
     * @param creditProcessNumToSet  creditProcessNum
     */
    public final void setCreditProcessNum(final String creditProcessNumToSet) {
        this.creditProcessNum = creditProcessNumToSet;
    }
    /**
     * @return terminalIdentificationNum
     */
    public final String getTerminalIdentificationNum() {
        return terminalIdentificationNum;
    }
    /**
     * @param terminalIdentificationNumToSet  terminalIdentificationNum
     */
    public final void setTerminalIdentificationNum(
            final String terminalIdentificationNumToSet) {
        this.terminalIdentificationNum = terminalIdentificationNumToSet;
    }
    /**
     * @return settlementManageNum
     */
    public final String getSettlementManageNum() {
        return settlementManageNum;
    }
    /**
     * @param settlementManageNumToSet  settlementManageNum
     */
    public final void setSettlementManageNum(
            final String settlementManageNumToSet) {
        this.settlementManageNum = settlementManageNumToSet;
    }
}
