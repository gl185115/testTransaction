package ncr.res.mobilepos.creditauthorization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * The Class PastelPortEnv.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PastelPortEnv")
public class PastelPortEnv {

    /** The Constant PASTELPORTENVIDINUSE. */
    public static final int PASTELPORTENVIDINUSE = 0;

    /** The connection port no. */
    @XmlElement(name = "connectionPortNO")
    private int connectionPortNO;

    /** The handled company code. */
    @XmlElement(name = "handledCompanyCode")
    private String handledCompanyCode;

    /** The mscedit. */
    @XmlElement(name = "mscedit")
    private String mscedit;

    /** The company code. */
    @XmlElement(name = "companyCode")
    private String companyCode;

    /** The time out. */
    @XmlElement(name = "timeOut")
    private int timeOut;

    /** The payment classification. */
    @XmlElement(name = "paymentClassification")
    private String paymentClassification;

    /** The sign less receipt word. */
    @XmlElement(name = "signLessReceiptWord")
    private String signLessReceiptWord;

    /** The trading unfinished receipt word. */
    @XmlElement(name = "tradingUnfinishedReceiptWord")
    private String tradingUnfinishedReceiptWord;

    /** The pastel port multi server ip. */
    @XmlElement(name = "pastelPortMultiServerIP")
    private String pastelPortMultiServerIP;

    /** The handled company sub code. */
    @XmlElement(name = "handledCompanySubCode")
    private String handledCompanySubCode;

    /** The retry count. */
    @XmlElement(name = "retryCount")
    private int retryCount;

    /** The print pp log. */
    @XmlElement(name = "printPPLog")
    private boolean printPPLog;

    /**
     * Gets the retry count.
     *
     * @return the retry count
     */
    public final int getRetryCount() {
        return retryCount;
    }

    /**
     * Sets the retry count.
     *
     * @param retCount
     *            the new retry count
     */
    public final void setRetryCount(final int retCount) {
        this.retryCount = retCount;
    }

    /**
     * Checks if is prints the pp log.
     *
     * @return true, if is prints the pp log
     */
    public final boolean isPrintPPLog() {
        return printPPLog;
    }

    /**
     * Sets the prints the pp log.
     *
     * @param prntPPLog
     *            the new prints the pp log
     */
    public final void setPrintPPLog(final boolean prntPPLog) {
        this.printPPLog = prntPPLog;
    }

    /**
     * Gets the connection port no.
     *
     * @return the connection port no
     */
    public final int getConnectionPortNO() {
        return connectionPortNO;
    }

    /**
     * Sets the connection port no.
     *
     * @param connPortNo
     *            the new connection port no
     */
    public final void setConnectionPortNO(final int connPortNo) {
        this.connectionPortNO = connPortNo;
    }

    /**
     * Gets the handled company code.
     *
     * @return the handled company code
     */
    public final String getHandledCompanyCode() {
        return handledCompanyCode;
    }

    /**
     * Sets the handled company code.
     *
     * @param handledCompCode
     *            the new handled company code
     */
    public final void setHandledCompanyCode(final String handledCompCode) {
        this.handledCompanyCode = handledCompCode;
    }

    /**
     * Gets the mscedit.
     *
     * @return the mscedit
     */
    public final String getMscedit() {
        return mscedit;
    }

    /**
     * Sets the mscedit.
     *
     * @param mscEdit
     *            the new mscedit
     */
    public final void setMscedit(final String mscEdit) {
        this.mscedit = mscEdit;
    }

    /**
     * Gets the company code.
     *
     * @return the company code
     */
    public final String getCompanyCode() {
        return companyCode;
    }

    /**
     * Sets the company code.
     *
     * @param compCode
     *            the new company code
     */
    public final void setCompanyCode(final String compCode) {
        this.companyCode = compCode;
    }

    /**
     * Gets the time out.
     *
     * @return the time out
     */
    public final int getTimeOut() {
        return timeOut;
    }

    /**
     * Sets the time out.
     *
     * @param time
     *            the new time out
     */
    public final void setTimeOut(final int time) {
        this.timeOut = time;
    }

    /**
     * Gets the payment classification.
     *
     * @return the payment classification
     */
    public final String getPaymentClassification() {
        return paymentClassification;
    }

    /**
     * Sets the payment classification.
     *
     * @param paymentClass
     *            the new payment classification
     */
    public final void setPaymentClassification(
            final String paymentClass) {
        this.paymentClassification = paymentClass;
    }

    /**
     * Gets the sign less receipt word.
     *
     * @return the sign less receipt word
     */
    public final String getSignLessReceiptWord() {
        return signLessReceiptWord;
    }

    /**
     * Sets the sign less receipt word.
     *
     * @param signlessRecptWord
     *            the new sign less receipt word
     */
    public final void setSignLessReceiptWord(final String signlessRecptWord) {
        this.signLessReceiptWord = signlessRecptWord;
    }

    /**
     * Gets the trading unfinished receipt word.
     *
     * @return the trading unfinished receipt word
     */
    public final String getTradingUnfinishedReceiptWord() {
        return tradingUnfinishedReceiptWord;
    }

    /**
     * Sets the trading unfinished receipt word.
     *
     * @param tradingUnfnshedRecptWord
     *            the new trading unfinished receipt word
     */
    public final void setTradingUnfinishedReceiptWord(
            final String tradingUnfnshedRecptWord) {
        this.tradingUnfinishedReceiptWord = tradingUnfnshedRecptWord;
    }

    /**
     * Gets the pastel port multi server ip.
     *
     * @return the pastel port multi server ip
     */
    public final String getPastelPortMultiServerIP() {
        return pastelPortMultiServerIP;
    }

    /**
     * Sets the pastel port multi server ip.
     *
     * @param ppMultiServerIP
     *            the new pastel port multi server ip
     */
    public final void setPastelPortMultiServerIP(
            final String ppMultiServerIP) {
        this.pastelPortMultiServerIP = ppMultiServerIP;
    }

    /**
     * Gets the handled company sub code.
     *
     * @return the handled company sub code
     */
    public final String getHandledCompanySubCode() {
        return handledCompanySubCode;
    }

    /**
     * Sets the handled company sub code.
     *
     * @param handledCompSubCode
     *            the new handled company sub code
     */
    public final void setHandledCompanySubCode(
            final String handledCompSubCode) {
        this.handledCompanySubCode = handledCompSubCode;
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
