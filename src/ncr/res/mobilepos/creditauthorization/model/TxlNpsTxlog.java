package ncr.res.mobilepos.creditauthorization.model;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class TxlNpsTxlog.
 */
@XmlRootElement(name = "TxlNpsTxlog")
public class TxlNpsTxlog {

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
     * @param termID
     *            the new termid
     */
    public final void setTermid(final String termID) {
        this.termid = termID;
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
     * Gets the guid.
     *
     * @return the guid
     */
    public final String getGuid() {
        return guid;
    }

    /**
     * Sets the guid.
     *
     * @param guID
     *            the new guid
     */
    public final void setGuid(final String guID) {
        this.guid = guID;
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
     * @param servce
     *            the new service
     */
    public final void setService(final String servce) {
        this.service = servce;
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
     * Gets the txdatetime.
     *
     * @return the txdatetime
     */
    public final String getTxdatetime() {
        return txdatetime;
    }

    /**
     * Sets the txdatetime.
     *
     * @param txDateTime
     *            the new txdatetime
     */
    public final void setTxdatetime(final String txDateTime) {
        this.txdatetime = txDateTime;
    }

    /**
     * Gets the rwid.
     *
     * @return the rwid
     */
    public final String getRwid() {
        return rwid;
    }

    /**
     * Sets the rwid.
     *
     * @param rwID
     *            the new rwid
     */
    public final void setRwid(final String rwID) {
        this.rwid = rwID;
    }

    /**
     * Gets the rwkind.
     *
     * @return the rwkind
     */
    public final String getRwkind() {
        return rwkind;
    }

    /**
     * Sets the rwkind.
     *
     * @param rwKind
     *            the new rwkind
     */
    public final void setRwkind(final String rwKind) {
        this.rwkind = rwKind;
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
     * Gets the xmldocument.
     *
     * @return the xmldocument
     */
    public final String getXmldocument() {
        return xmldocument;
    }

    /**
     * Sets the xmldocument.
     *
     * @param xmlDoc
     *            the new xmldocument
     */
    public final void setXmldocument(final String xmlDoc) {
        this.xmldocument = xmlDoc;
    }

    /** The corpid. */
    private String corpid;

    /** The storeid. */
    private String storeid;

    /** The termid. */
    private String termid;

    /** The txid. */
    private String txid;

    /** The paymentseq. */
    private String paymentseq;

    /** The guid. */
    private String guid;

    /** The brand. */
    private String brand;

    /** The service. */
    private String service;

    /** The amount. */
    private String amount;

    /** The txdatetime. */
    private String txdatetime;

    /** The rwid. */
    private String rwid;

    /** The rwkind. */
    private String rwkind;

    /** The txtype. */
    private String txtype;

    /** The xmldocument. */
    private String xmldocument;
}
