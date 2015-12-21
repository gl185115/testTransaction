package ncr.res.mobilepos.creditauthorization.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import ncr.res.mobilepos.helper.Logger;
import ncr.res.mobilepos.model.ResultBase;

/**
 * The Class PastelPortResp.
 */
@XmlRootElement(name = "PastelPortResp")
public class PastelPortResp extends ResultBase {

    /** The result code. */
    private String resultCode; // 暫定として、statusをセット
                              // As provisional, set the status
    /** The extended result code. */
    private String extendedResultCode; // 暫定として、creditstatus As provisional,

    /** The corpid. */
    private String corpid; // 受信データをセット Set the received data

    /** The storeid. */
    private String storeid; // 受信データをセット Set the received data

    /** The terminalid. */
    private String terminalid; // 受信データをセット Set the received data

    /** The txid. */
    private String txid; // 受信データをセット Set the received data

    /** The amount. */
    private String amount; // 受信データをセット Set the received data

    /** The status. */
    private String status; // 0(正常)、1(異常再送可)、9(異常再送不可) (Normal) 0, (s
                           // retransmission abnormal) 1, 9 (non-retransmission
                           // abnormal)
    /** The creditstatus. */
    private String creditstatus; // 00(正常)、07(取扱不可)、08(承認番号要)10
                                 // (リミット以下折り返し)、18(リミット超オフライン)
                                 // (Normal) 00, (not handling) 07, 10 (requires
                                 // authorization number) 08 (folded below
                                 // limit), (off-line ultra-limit) 18
    /** The errorcode. */
    private String errorcode; // 正常時はスペース4文字、異常時は"G30 "など
                              // Normal 4-space
                              // characters, such as the "G30" when abnormal
    /** The errormessage. */
    private String errormessage; // 正常時はタグなし Normal when no tag
                                 //正常時はタグなし Normal when no tag
    /** The approvalcode. */
    private String approvalcode; // 内部折り返しの場合はタグなしの場合あり
                                 // If the internal wrapping
                                 // is an untagged
    /** The tracenum. */
    private String tracenum; // 内部折り返しの場合はタグなしの場合あり
                             // If the internal wrapping is
                             // an untagged
    /** The pan4. */
    private String pan4; // ************9999下4桁開示
                         // PAN 4 digit PAN disclosure under
    /** The pan6. */
    private String pan6; // 999999******9999先頭6桁+下4桁開示
                         // PAN The first six digits +
                         // PAN disclosure last four digits
    /** The expdate. */
    private String expdate; // 有効期限

    /** The paymentmethod. */
    private String paymentmethod; // 受信データをセット Set the received data

    /** The creditcompanyname. */
    private String creditcompanyname; // 復号時のデータをセット Set the received data

    /** The creditcompanycode. */
    private String creditcompanycode; // 復号時のデータをセット Set the received data

    /** The cancelnoticeflag. */
    private String cancelnoticeflag; // 自動取消(取消通知)フラグ
                                     // Automatic revocation flag
                                     // (notice of revocation)
    /** The tillid. */
    private String tillid;

    /** The paymentseq. */
    private String paymentseq;

    /** The recvcompanycode. */
    private String recvcompanycode;
    
    private String settlementnum;

    public final String getSettlementnum() {
        return settlementnum;
    }

    public final void setSettlementnum(final String settlementnum) {
        this.settlementnum = settlementnum;
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
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance();

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
     * Gets the tillid.
     *
     * @return the tillid
     */
    public final String getTillid() {
        return tillid;
    }

    /**
     * Sets the tillid.
     *
     * @param tillID
     *            the new tillid
     */
    public final void setTillid(final String tillID) {
        this.tillid = tillID;
    }

    /**
     * Communication is failed.
     *
     * @param resp
     *            the resp
     * @param status
     *            the status
     * @param creditStatus
     *            the credit status
     * @param errorCode
     *            the error code
     * @param errorMessage
     *            the error message
     */
    public static void communicationIsFailed(final PastelPortResp resp,
            final String status, final String creditStatus,
            final String errorCode, final String errorMessage) {

        String progName = "PastelPortResp";
        String className = "communicationIsFailed";
        String functionName = className + "communicationIsFailed";
        LOGGER.logFunctionEntry(progName, functionName + " - ", "");

        resp.setStatus(status);
        resp.setCreditstatus(creditStatus);
        resp.setErrormessage(errorMessage);
        resp.setErrorcode(errorCode);
        LOGGER.logAlert(progName, functionName, Logger.RES_EXCEP_GENERAL,
                "通信ラー");
    }

    /**
     * Communication is successful.
     */
    public final void communicationIsSuccessful() {
        this.setStatus("0");
        this.setCreditstatus("00");
        this.setErrorcode("    ");
    }

    /**
     * Gets the cancelnoticeflag.
     *
     * @return the cancelnoticeflag
     */
    public final String getCancelnoticeflag() {
        return cancelnoticeflag;
    }

    /**
     * Sets the cancelnoticeflag.
     *
     * @param cancelNoticeFlag
     *            the new cancelnoticeflag
     */
    public final void setCancelnoticeflag(final String cancelNoticeFlag) {
        this.cancelnoticeflag = cancelNoticeFlag;
    }

    /** The posreturn. */
    private boolean posreturn = false; // POS返送フラグ need to return directly

    /**
     * Gets the result code.
     *
     * @return the result code
     */
    public final String getResultCode() {
        return resultCode;
    }

    /**
     * Sets the result code.
     *
     * @param resCode
     *            the new result code
     */
    public final void setResultCode(final String resCode) {
        this.resultCode = resCode;
    }

    /**
     * Gets the extended result code.
     *
     * @return the extended result code
     */
    public final String getExtendedResultCode() {
        return extendedResultCode;
    }

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
     * Gets the terminalid.
     *
     * @return the terminalid
     */
    public final String getTerminalid() {
        return terminalid;
    }

    /**
     * Sets the terminalid.
     *
     * @param terminalID
     *            the new terminalid
     */
    public final void setTerminalid(final String terminalID) {
        this.terminalid = terminalID;
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
     * Sets the extended result code.
     *
     * @param extResultCode
     *            the new extended result code
     */
    public final void setExtendedResultCode(final String extResultCode) {
        this.extendedResultCode = extResultCode;
    }

    /**
     * Gets the status.
     *
     * @return the status
     */
    public final String getStatus() {
        return status;
    }

    /**
     * Sets the status.
     *
     * @param stat
     *            the new status
     */
    public final void setStatus(final String stat) {
        this.status = stat;
    }

    /**
     * Gets the creditstatus.
     *
     * @return the creditstatus
     */
    public final String getCreditstatus() {
        return creditstatus;
    }

    /**
     * Sets the creditstatus.
     *
     * @param creditStatus
     *            the new creditstatus
     */
    public final void setCreditstatus(final String creditStatus) {
        this.creditstatus = creditStatus;
    }

    /**
     * Gets the errorcode.
     *
     * @return the errorcode
     */
    public final String getErrorcode() {
        return errorcode;
    }

    /**
     * Sets the errorcode.
     *
     * @param errorCode
     *            the new errorcode
     */
    public final void setErrorcode(final String errorCode) {
        this.errorcode = errorCode;
    }

    /**
     * Gets the errormessage.
     *
     * @return the errormessage
     */
    public final String getErrormessage() {
        return errormessage;
    }

    /**
     * Sets the errormessage.
     *
     * @param errorMessage
     *            the new errormessage
     */
    public final void setErrormessage(final String errorMessage) {
        this.errormessage = errorMessage;
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
     * Gets the pan4.
     *
     * @return the pan4
     */
    public final String getPan4() {
        return pan4;
    }

    /**
     * Sets the pan4.
     *
     * @param pan
     *            the new pan4
     */
    public final void setPan4(final String pan) {
        this.pan4 = pan;
    }

    /**
     * Gets the pan6.
     *
     * @return the pan6
     */
    public final String getPan6() {
        return pan6;
    }

    /**
     * Sets the pan6.
     *
     * @param pan
     *            the new pan6
     */
    public final void setPan6(final String pan) {
        this.pan6 = pan;
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
     * Gets the creditcompanyname.
     *
     * @return the creditcompanyname
     */
    public final String getCreditcompanyname() {
        return creditcompanyname;
    }

    /**
     * Sets the creditcompanyname.
     *
     * @param creditCompanyName
     *            the new creditcompanyname
     */
    public final void setCreditcompanyname(final String creditCompanyName) {
        this.creditcompanyname = creditCompanyName;
    }

    /**
     * Gets the creditcompanycode.
     *
     * @return the creditcompanycode
     */
    public final String getCreditcompanycode() {
        return creditcompanycode;
    }

    /**
     * Sets the creditcompanycode.
     *
     * @param creditCompanyCode
     *            the new creditcompanycode
     */
    public final void setCreditcompanycode(final String creditCompanyCode) {
        this.creditcompanycode = creditCompanyCode;
    }

    /**
     * is need to return immediately.
     *
     * @return the pos return
     */
    public final boolean getPosReturn() {
        return posreturn;
    }

    /**
     * set the value to true when need to return immediately.
     *
     * @param posReturn
     *            the new pos return
     */
    public final void setPosReturn(final boolean posReturn) {
        this.posreturn = posReturn;
    }

    @Override
    public final String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String ret = "";
        try {
            ret += mapper.writeValueAsString(this);
        } catch (Exception ex) {
            ret = super.toString();
        }
        return ret;
    }
}
