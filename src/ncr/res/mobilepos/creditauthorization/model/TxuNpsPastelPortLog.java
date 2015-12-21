package ncr.res.mobilepos.creditauthorization.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class TxuNpsPastelPortLog.
 */
@XmlRootElement(name = "TxuNpsPastelPortLog")
public class TxuNpsPastelPortLog {

    /** The corpid. */
    private String corpid;

    /** The storeid. */
    private String storeid;

    /** The termid. */
    private String termid;

    /** The paymentseq. */
    private String paymentseq;

    /** The txdate. */
    private String txdate;

    /** The txid. */
    private String txid;

    /** The brand. */
    private String brand;

    /** The service. */
    private String service;

    /** The txtype. */
    private String txtype;

    /** The txstatus. */
    private String txstatus;

    /** The crcompanycode. */
    private String crcompanycode;

    /** The recvcompanycode. */
    private String recvcompanycode;

    /** The paymentmethod. */
    private String paymentmethod;

    /** The pan. */
    private String pan;

    /** The expdate. */
    private String expdate;

    /** The amount. */
    private String amount;

    /** The castatus. */
    private String castatus;

    /** The caerrorcode. */
    private String caerrorcode;

    /** The dcstatus. */
    private String dcstatus;

    /** The dcerrorcode. */
    private String dcerrorcode;

    /** The approvalcode. */
    private String approvalcode;

    /** The tracenum. */
    private String tracenum;

    /** The alertflag. */
    private String alertflag;

    /** The voidflag. */
    private String voidflag;

    /** The voidcorpid. */
    private String voidcorpid;

    /** The voidstoreid. */
    private String voidstoreid;

    /** The voidtermid. */
    private String voidtermid;

    /** The voidpaymentseq. */
    private String voidpaymentseq;

    /** The voidtxid. */
    private String voidtxid;

    /** The voidtxdate. */
    private String voidtxdate;

    /** The settlementnum. */
    private String settlementnum;
    // RES 3.1 ’Ç‰Á‚·‚é START
    /** The txdate2. */
    private String txdate2;

    /** The serverpaymentseq. */
    private String serverpaymentseq;
    // RES 3.1 ’Ç‰Á‚·‚é END

    /**
     * Gets the corpid.
     *
     * @return the corpid
     */
    public final String getCorpid() {
        return corpid;
    }

    /**
     * Sets the corpid.
     *
     * @param corpID
     *            the new corpid
     */
    public final void setCorpid(final String corpID) {
        this.corpid = corpID;
    }

    /**
     * Gets the storeid.
     *
     * @return the storeid
     */
    public final String getStoreid() {
        return storeid;
    }

    /**
     * Sets the storeid.
     *
     * @param storeID
     *            the new storeid
     */
    public final void setStoreid(final String storeID) {
        this.storeid = storeID;
    }

    /**
     * Gets the termid.
     *
     * @return the termid
     */
    public final String getTermid() {
        return termid;
    }

    /**
     * Sets the termid.
     *
     * @param terminalID
     *            the new termid
     */
    public final void setTermid(final String terminalID) {
        this.termid = terminalID;
    }

    /**
     * Gets the paymentseq.
     *
     * @return the paymentseq
     */
    public final String getPaymentseq() {
        return paymentseq;
    }

    /**
     * Sets the paymentseq.
     *
     * @param paymentSeq
     *            the new paymentseq
     */
    public final void setPaymentseq(final String paymentSeq) {
        this.paymentseq = paymentSeq;
    }

    /**
     * Gets the txdate.
     *
     * @return the txdate
     */
    public final String getTxdate() {
        return txdate;
    }

    /**
     * Sets the txdate.
     *
     * @param txDate
     *            the new txdate
     */
    public final void setTxdate(final String txDate) {
        this.txdate = txDate;
    }

    /**
     * Gets the txid.
     *
     * @return the txid
     */
    public final String getTxid() {
        return txid;
    }

    /**
     * Sets the txid.
     *
     * @param txID
     *            the new txid
     */
    public final void setTxid(final String txID) {
        this.txid = txID;
    }

    /**
     * Gets the brand.
     *
     * @return the brand
     */
    public final String getBrand() {
        return brand;
    }

    /**
     * Sets the brand.
     *
     * @param brnd
     *            the new brand
     */
    public final void setBrand(final String brnd) {
        this.brand = brnd;
    }

    /**
     * Gets the service.
     *
     * @return the service
     */
    public final String getService() {
        return service;
    }

    /**
     * Sets the service.
     *
     * @param servc
     *            the new service
     */
    public final void setService(final String servc) {
        this.service = servc;
    }

    /**
     * Gets the txtype.
     *
     * @return the txtype
     */
    public final String getTxtype() {
        return txtype;
    }

    /**
     * Sets the txtype.
     *
     * @param txType
     *            the new txtype
     */
    public final void setTxtype(final String txType) {
        this.txtype = txType;
    }

    /**
     * Gets the txstatus.
     *
     * @return the txstatus
     */
    public final String getTxstatus() {
        return txstatus;
    }

    /**
     * Sets the txstatus.
     *
     * @param txStatus
     *            the new txstatus
     */
    public final void setTxstatus(final String txStatus) {
        this.txstatus = txStatus;
    }

    /**
     * Gets the crcompanycode.
     *
     * @return the crcompanycode
     */
    public final String getCrcompanycode() {
        return crcompanycode;
    }

    /**
     * Sets the crcompanycode.
     *
     * @param crCompanyCode
     *            the new crcompanycode
     */
    public final void setCrcompanycode(final String crCompanyCode) {
        this.crcompanycode = crCompanyCode;
    }

    /**
     * Gets the recvcompanycode.
     *
     * @return the recvcompanycode
     */
    public final String getRecvcompanycode() {
        return recvcompanycode;
    }

    /**
     * Sets the recvcompanycode.
     *
     * @param recvCompanyCode
     *            the new recvcompanycode
     */
    public final void setRecvcompanycode(final String recvCompanyCode) {
        this.recvcompanycode = recvCompanyCode;
    }

    /**
     * Gets the paymentmethod.
     *
     * @return the paymentmethod
     */
    public final String getPaymentmethod() {
        return paymentmethod;
    }

    /**
     * Sets the paymentmethod.
     *
     * @param paymentMethod
     *            the new paymentmethod
     */
    public final void setPaymentmethod(final String paymentMethod) {
        this.paymentmethod = paymentMethod;
    }

    /**
     * Gets the pan.
     *
     * @return the pan
     */
    public final String getPan() {
        return pan;
    }

    /**
     * Sets the pan.
     *
     * @param panToSet
     *            the new pan
     */
    public final void setPan(final String panToSet) {
        this.pan = panToSet;
    }

    /**
     * Gets the expdate.
     *
     * @return the expdate
     */
    public final String getExpdate() {
        return expdate;
    }

    /**
     * Sets the expdate.
     *
     * @param expDate
     *            the new expdate
     */
    public final void setExpdate(final String expDate) {
        this.expdate = expDate;
    }

    /**
     * Gets the amount.
     *
     * @return the amount
     */
    public final String getAmount() {
        return amount;
    }

    /**
     * Sets the amount.
     *
     * @param amt
     *            the new amount
     */
    public final void setAmount(final String amt) {
        this.amount = amt;
    }

    /**
     * Gets the castatus.
     *
     * @return the castatus
     */
    public final String getCastatus() {
        return castatus;
    }

    /**
     * Sets the castatus.
     *
     * @param caStatus
     *            the new castatus
     */
    public final void setCastatus(final String caStatus) {
        this.castatus = caStatus;
    }

    /**
     * Gets the caerrorcode.
     *
     * @return the caerrorcode
     */
    public final String getCaerrorcode() {
        return caerrorcode;
    }

    /**
     * Sets the caerrorcode.
     *
     * @param caErrorCode
     *            the new caerrorcode
     */
    public final void setCaerrorcode(final String caErrorCode) {
        this.caerrorcode = caErrorCode;
    }

    /**
     * Gets the dcstatus.
     *
     * @return the dcstatus
     */
    public final String getDcstatus() {
        return dcstatus;
    }

    /**
     * Sets the dcstatus.
     *
     * @param dcStatus
     *            the new dcstatus
     */
    public final void setDcstatus(final String dcStatus) {
        this.dcstatus = dcStatus;
    }

    /**
     * Gets the dcerrorcode.
     *
     * @return the dcerrorcode
     */
    public final String getDcerrorcode() {
        return dcerrorcode;
    }

    /**
     * Sets the dcerrorcode.
     *
     * @param dcErrorCode
     *            the new dcerrorcode
     */
    public final void setDcerrorcode(final String dcErrorCode) {
        this.dcerrorcode = dcErrorCode;
    }

    /**
     * Gets the approvalcode.
     *
     * @return the approvalcode
     */
    public final String getApprovalcode() {
        return approvalcode;
    }

    /**
     * Sets the approvalcode.
     *
     * @param approvalCode
     *            the new approvalcode
     */
    public final void setApprovalcode(final String approvalCode) {
        this.approvalcode = approvalCode;
    }

    /**
     * Gets the tracenum.
     *
     * @return the tracenum
     */
    public final String getTracenum() {
        return tracenum;
    }

    /**
     * Sets the tracenum.
     *
     * @param traceNum
     *            the new tracenum
     */
    public final void setTracenum(final String traceNum) {
        this.tracenum = traceNum;
    }

    /**
     * Gets the alertflag.
     *
     * @return the alertflag
     */
    public final String getAlertflag() {
        return alertflag;
    }

    /**
     * Sets the alertflag.
     *
     * @param alertFlag
     *            the new alertflag
     */
    public final void setAlertflag(final String alertFlag) {
        this.alertflag = alertFlag;
    }

    /**
     * Gets the voidflag.
     *
     * @return the voidflag
     */
    public final String getVoidflag() {
        return voidflag;
    }

    /**
     * Sets the voidflag.
     *
     * @param voidFlag
     *            the new voidflag
     */
    public final void setVoidflag(final String voidFlag) {
        this.voidflag = voidFlag;
    }

    /**
     * Gets the voidcorpid.
     *
     * @return the voidcorpid
     */
    public final String getVoidcorpid() {
        return voidcorpid;
    }

    /**
     * Sets the voidcorpid.
     *
     * @param voidCorpID
     *            the new voidcorpid
     */
    public final void setVoidcorpid(final String voidCorpID) {
        this.voidcorpid = voidCorpID;
    }

    /**
     * Gets the voidstoreid.
     *
     * @return the voidstoreid
     */
    public final String getVoidstoreid() {
        return voidstoreid;
    }

    /**
     * Sets the voidstoreid.
     *
     * @param voidStoreID
     *            the new voidstoreid
     */
    public final void setVoidstoreid(final String voidStoreID) {
        this.voidstoreid = voidStoreID;
    }

    /**
     * Gets the voidtermid.
     *
     * @return the voidtermid
     */
    public final String getVoidtermid() {
        return voidtermid;
    }

    /**
     * Sets the voidtermid.
     *
     * @param voidTermID
     *            the new voidtermid
     */
    public final void setVoidtermid(final String voidTermID) {
        this.voidtermid = voidTermID;
    }

    /**
     * Gets the voidpaymentseq.
     *
     * @return the voidpaymentseq
     */
    public final String getVoidpaymentseq() {
        return voidpaymentseq;
    }

    /**
     * Sets the voidpaymentseq.
     *
     * @param voidPaymentSeq
     *            the new voidpaymentseq
     */
    public final void setVoidpaymentseq(final String voidPaymentSeq) {
        this.voidpaymentseq = voidPaymentSeq;
    }

    /**
     * Gets the voidtxid.
     *
     * @return the voidtxid
     */
    public final String getVoidtxid() {
        return voidtxid;
    }

    /**
     * Sets the voidtxid.
     *
     * @param voidTxID
     *            the new voidtxid
     */
    public final void setVoidtxid(final String voidTxID) {
        this.voidtxid = voidTxID;
    }

    /**
     * Gets the voidtxdate.
     *
     * @return the voidtxdate
     */
    public final String getVoidtxdate() {
        return voidtxdate;
    }

    /**
     * Sets the voidtxdate.
     *
     * @param voidTxDate
     *            the new voidtxdate
     */
    public final void setVoidtxdate(final String voidTxDate) {
        this.voidtxdate = voidTxDate;
    }

    /**
     * Gets the settlementnum.
     *
     * @return the settlementnum
     */
    public final String getSettlementnum() {
        return settlementnum;
    }

    /**
     * Sets the settlementnum.
     *
     * @param settlementNum
     *            the new settlementnum
     */
    public final void setSettlementnum(final String settlementNum) {
        this.settlementnum = settlementNum;
    }
    // RES 3.1 ’Ç‰Á‚·‚é START
    /**
     * Gets the txdate2.
     *
     * @return the txdate2
     */
    public final String getTxdate2() {
        return txdate2;
    }

    /**
     * Sets the txdate2.
     *
     * @param txDate2
     *            the new txDate2
     */
    public final void setTxdate2(final String txDate2) {
        this.txdate2 = txDate2;
    }

    /**
     * Gets the serverpaymentseq.
     *
     * @return the serverpaymentseq
     */
    public final String getServerpaymentseq() {
        return serverpaymentseq;
    }

    /**
     * Sets the serverpaymentseq.
     *
     * @param serverPaymentSeq
     *            the new serverPaymentSeq
     */
    public final void setServerpaymentseq(final String serverPaymentSeq) {
        this.serverpaymentseq = serverPaymentSeq;
    }
    // RES 3.1 ’Ç‰Á‚·‚é END
}
