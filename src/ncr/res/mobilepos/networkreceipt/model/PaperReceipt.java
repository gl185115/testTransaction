/**
 * Copyright (c) 2011 NCR/JAPAN Corporation SW-R&D
 */
package ncr.res.mobilepos.networkreceipt.model;

import ncr.res.mobilepos.journalization.model.poslog.MemberInfo;
/**
 * PaperReceipt Class is a Model representation of the
 * information which identifies the paper receipt.
 */
public class PaperReceipt {
    /**
     * class instance of PaperReceiptHeader.
     */
    private PaperReceiptHeader receiptHeader;
    /**
     * class instance of PaperReceiptContent.
     */
    private PaperReceiptContent receiptContent;
    /**
     * class instance of PaperReceiptPayment.
     */
    private PaperReceiptPayment receiptPayment;
    /**
     * class instance of PaperReceiptFooter.
     */
    private PaperReceiptFooter receiptFooter;
    /**
     * for fantamiliar points system
     */
    private MemberInfo memberInfo;
    /**
     * for fantamiliar points system
     */
    private String businessDate;
    
    /**
     * default constructor.
     */
    public PaperReceipt() {
        this.receiptHeader = new PaperReceiptHeader();
        this.receiptContent = new PaperReceiptContent();
        this.receiptPayment = new PaperReceiptPayment();
        this.receiptFooter = new PaperReceiptFooter();
    }

    /**
     * constructor.
     * @param receiptHeaderToSet - PaperReceiptHeader to set
     * @param receiptContentToSet - PaperReceiptContent to set
     * @param receiptFooterToSet - PaperReceiptFooter to set
     */
    public PaperReceipt(final PaperReceiptHeader receiptHeaderToSet,
                final PaperReceiptContent receiptContentToSet,
                final PaperReceiptFooter receiptFooterToSet) {
        this.receiptHeader = receiptHeaderToSet;
        this.receiptContent = receiptContentToSet;
        this.receiptPayment = new PaperReceiptPayment();
        this.receiptFooter = receiptFooterToSet;
    }

    /**
     * gets receipt header.
     * @return receiptHeader
     */
    public final PaperReceiptHeader getReceiptHeader() {
        return receiptHeader;
    }

    /**
     * sets receipt header.
     * @param receiptHeaderToSet  receiptHeader
     */
    public final void setReceiptHeader(
            final PaperReceiptHeader receiptHeaderToSet) {
        this.receiptHeader = receiptHeaderToSet;
    }

    /**
     * @return receiptContent
     */
    public final PaperReceiptContent getReceiptContent() {
        return receiptContent;
    }

    /**
     * @param receiptContentToSet  receiptContent
     */
    public final void setReceiptContent(
            final PaperReceiptContent receiptContentToSet) {
        this.receiptContent = receiptContentToSet;
    }

    /**
     * @return receiptPayment
     */
    public final PaperReceiptPayment getReceiptPayment() {
        return receiptPayment;
    }

    /**
     * @param receiptPaymentToSet receiptPayment
     */
    public final void setReceiptPayment(
            final PaperReceiptPayment receiptPaymentToSet) {
        this.receiptPayment = receiptPaymentToSet;
    }

    /**
     * @return receiptFooter
     */
    public final PaperReceiptFooter getReceiptFooter() {
        return receiptFooter;
    }

    /**
     * @param receiptFooterToSet  receiptFooter
     */
    public final void setReceiptFooter(
            final PaperReceiptFooter receiptFooterToSet) {
        this.receiptFooter = receiptFooterToSet;
    }

    /**
     * @return memberinfo
     */
    public final MemberInfo getMemberInfo() {
        return memberInfo;
    }

    /**
     * @param memberinfoToSet  MemberInfo
     */
    public final void setMemberInfo(
            final MemberInfo memberinfoToSet) {
        this.memberInfo = memberinfoToSet;
    }
    /**
     * @return businessDate
     */
    public final String getBusinessDate() {
        return businessDate;
    }

    /**
     * @param businessdateToSet
     */
    public final void setBusinessDate(
            final String businessdateToSet) {
        this.businessDate = businessdateToSet;
    }
    
    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("Header : \n" + receiptHeader);
        sb.append("\nContent : \n" + receiptContent);
        sb.append("\nPayment : \n" + receiptPayment);
        sb.append("\nFooter : \n" + receiptFooter);
        sb.append("\nMemberInfo : \n" + memberInfo);
        return sb.toString();
    }

}
