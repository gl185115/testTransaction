package ncr.res.pastelport.platform;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.xml.bind.annotation.XmlRootElement;

import org.apache.commons.io.output.FileWriterWithEncoding;
import org.codehaus.jackson.map.ObjectMapper;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;

/**
 * @author issuser
 *
 */
@XmlRootElement(name = "PastelPortTxRecvImpl")
public class PastelPortTxRecvImpl implements PastelPortTxRecvBase {
    /** The Length of the Message. */
    private String legth = "002579";
    /** The System reservation territory 2. */
    private String systemReservationTerritory2 = "3202";
    /** The Item Discrimination. */
    private String itemDiscrimination1 = "U1";
    /** The System Reservation territory 4. */
    private String systemReservationTerritory4 = "000130000";
    /** The System Reservation Territory 5. */
    private String systemReservationTerritory5 = "0000";
    /** The Count Check flag. */
    private String countCheckFlag = "0"; // Scrutiny flag counter
    /** The System Reservation territory 7. */
    private String systemReservationTerritory7 = "000";
    /** The Inquiry Message. */
    private String inqMessage = "0"; // INQ telegram
    /** The Source Flag. */
    private String sourceFlag = "0"; // Flag originated
    /** The Apllication Error Flag. */
    private String applicationErrorFlag = "0";
    /** The Final Result Flag. */
    private String resultFlag = "0";
    /** The Reserved Field. */
    private String reserveField = "0000100001"; // Preliminary fixed part
    /** The System Reservation Territory. */
    private String systemReservationTerritory13 = "0000";
    /** The Automatic revocation Flag. */
    private String cancelNoticeFlag = "0";
    /** The Store ID. */
    private String storeid; // Shop code
    /** The POS Number. */
    private String posNo; // POSNoÅD
    /** The Transaction Sequence Number. */
    private String txid; // Transaction sequence number
    /** The POS Operate Time. */
    private String posOperateTime; // POS
    /** The System Reservation Territory. */
    private String systemReservationTerritory14 = "000";
    /** The Finish Flag. */
    private String finishFlag = "   "; // End determination
    /** The Number of Digits. */
    private String digit; // Number of digits
    /** The Item Identification. */
    private String itemDiscrimination2; // Item identification
    /**  The Card Judge Method. */
    private String cardJudgeMethod; // How to determine card
    /** The CArd Sort. */
    private String cardSort; // Type of Card
    /** The Card Information. */
    private String cardInfo; // Card information (manual)
    /** The Card Information JSI1. */
    private String cardInfoJIS1; // Card information (JIS1)
    /** The Card Information JSI2. */
    private String cardInfoJIS2; // Card information (JIS2)
    /** The System Reservation territory 8.*/
    private String systemReservationTerritory8; // Reserved area system
    /** The System Reservation Teritorry 9. */
    private String systemReservationTerritory9; // Reserved area system
    /** The Password. */
    private String password; // Private code
    /** The System Reservation Territory 11. */
    private String systemReservationTerritory11;
    /** The Term Validity. */
    private String termValidity; // Expiration date
    /** The Payment Sequence. */
    private String paymentseq;
    /** The Original Voucher Number. */
    private String originalVoucherNo;
    /** The Product Code. */
    private String productCode;
    /** The Product Total. */
    private String productTotal;
    /** The Tax Carriage. */
    private String taxCarriage; // Tax and shipping
    /** The System Reservation Territory. */
    private String systemReservationTerritory18;
    /** The System Reservation Territory. */
    private String systemReservationTerritory19;
    /** The Payment Method ID. */
    private String paymentMethodId; // Handling division
    /** The Beginning Date. (Start Month Payment.) */
    private String beginningDate; // Starting month payment
    /** The initial Amount. */
    private String initialAmount; // Fri first
    /** The Payment Count. (Number of split) */
    private String paymentCount;
    /** The Payment Amount 1.
     * (Bonus payment amount split amount (first time)).
     */
    private String payAmount1;
    /** Bonus payment amount split amount (second time). */
    private String payAmount2;
    /** Bonus payment amount split amount (third time). */
    private String payAmount3;
    /** Bonus payment amount split amount (fourth time). */
    private String payAmount4;
    /** Bonus payment amount split amount (fifth time). */
    private String payAmount5;
    /** Bonus payment amount split amount (sixth time). */
    private String payAmount6;
    /** The Number of Bonus. */
    private String bonusCount; // Number of bonus
    /** The January Bonus. */
    private String bonus1;
    /** The February Bonus. */
    private String bonus2;
    /** The March Bonus. */
    private String bonus3;
    /** The April Bonus. */
    private String bonus4;
    /** The May Bonus. */
    private String bonus5;
    /** The June Bonus. */
    private String bonus6;
    /** The Transfer Fund Amount. */
    private String transferFund; // Transfer amount
    /** The System Reservation Territory 38. */
    private String systemReservationTerritory38;
    /** The Error Code. */
    private String errorCode;
    /** The System reservation Territory 40. */
    private String systemReservationTerritory40;
    /** The Approval code. */
    private String approvalcode; // Approval Number
    /** The System Reservation Territory 42. */
    private String systemReservationTerritory42;
    /** The System Reservation Territory 43. */
    private String systemReservationTerritory43;
    /** The System Reservation Territory 44. */
    private String systemReservationTerritory44;
    /** The Reservation. */
    private String reservation1; // Reserve
    /** The Request Code. */
    private String requestCode; // Request code
    /** The Message Type ID. */
    private String messageTypeId; // Message type ID
    /** The Connect Company. */
    private String connectCompany; // Connection company name
    /** The Cafis Sequence Number. */
    private String cafisSeq; // Processing sequence number CAFIS
    /** The Cafis processing date. */
    private String cafisDate; // Years CAFIS processing
    /** The Cafis approval code. */
    private String cafisApprovalcode; // (First digit above)
    // approval number CAFIS
    /** The System Trace Audit Number. */
    private String systemTraceAuditNumber; // System trace
    // audit number
    /** The Center Receipt Date. */
    private String centerReceiptDate; // Reception center date
    /** The System Reservation Territory. */
    private String systemReservationTerritory54;
    /** The Permission Code. */
    private String permissionCode; // Authorization code
    /** The Authorization Agency ID. */
    private String authorizationAgencyID; // Agency ID number
    // authorization is
    /** The MemberShip printed Member ID. */
    private String printMemberID; // Membership number printed data
    // transmission)
    /** The Print Validity Date.  */
    private String printValidityDate; // Expiration date printed
    // data transmission)
    /** The Display Message. */
    private String displayMessage; // display the
    // message transmission
    /** The Message Transmission. */
    private String printMessage; // Message data printing)
    // printer transmission
    /** The connect company code. */
    private String connectCompanyCode; // Company code connection
    /** The Cooperation Code. */
    private String cooperationCode; // Code and non-alliance alliance
    /** The Reservation. */
    private String reservation2; // Reserve
    /** The IC Related Validity Digit. */
    private String iCRelatedDataValidityDigit; // The number of
    // digits in the data related to
    // IC
    /** The IC Related data. */
    private String iCRelatedData; // IC-related data
    /** The Debug Trace Printer. */
    private Trace.Printer tp;

    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get logger;

    /**
     * Constructor.
     */
    public PastelPortTxRecvImpl() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                this.getClass());
    }

    /**
     * @param legthToSet the MEssage length.
     */
    @Override
    public final void setLegth(final String legthToSet) {
        this.legth = legthToSet;
    }

    /**
     * @param systemReservationTerritory2ToSet The System Reservation 2.
     */
    @Override
    public final void setSystemreservationterritory2(
            final String systemReservationTerritory2ToSet) {
        this.systemReservationTerritory2 = systemReservationTerritory2ToSet;
    }

    /**
     * @param itemDiscrimination1ToSet The Item Descrimination 1.
     */
    @Override
    public final void setItemdiscrimination1(
            final String itemDiscrimination1ToSet) {
        this.itemDiscrimination1 = itemDiscrimination1ToSet;
    }

    /**
     * @param systemReservationTerritory4ToSet
     *                       The System Reservation Territory 4.
     */
    @Override
    public final void setSystemreservationterritory4(
            final String systemReservationTerritory4ToSet) {
        this.systemReservationTerritory4 = systemReservationTerritory4ToSet;
    }

    /**
     * @param systemReservationTerritory5ToSet
     *               The System Reservation territory 5.
     */
    @Override
    public final void setSystemreservationterritory5(
            final String systemReservationTerritory5ToSet) {
        this.systemReservationTerritory5 = systemReservationTerritory5ToSet;
    }

    /**
     * @param countCheckFlagToSet The Count Check Flag.
     */
    @Override
    public final void setCountcheckflag(final String countCheckFlagToSet) {
        this.countCheckFlag = countCheckFlagToSet;
    }

    /**
     * @param systemReservationTerritory7ToSet
     *                      The System reservation territory 7.
     */
    @Override
    public final void setSystemreservationterritory7(
            final String systemReservationTerritory7ToSet) {
        this.systemReservationTerritory7 = systemReservationTerritory7ToSet;
    }

    /**
     * @param inqMessageToSet The Message to set.
     */
    @Override
    public final void setInqmessage(final String inqMessageToSet) {
        this.inqMessage = inqMessageToSet;
    }

    /**
     * @param sourceFlagToSet The Source Flag to Set.
     */
    @Override
    public final void setSourceflag(final String sourceFlagToSet) {
        this.sourceFlag = sourceFlagToSet;
    }

    /**
     * @param applicationErrorFlagToSet The Application Flag Error to set.
     */
    @Override
    public final void setApplicationerrorflag(
            final String applicationErrorFlagToSet) {
        this.applicationErrorFlag = applicationErrorFlagToSet;
    }

    /**
     * @param resultFlagToSet The Result Flag to set.
     */
    @Override
    public final void setResultflag(final String resultFlagToSet) {
        this.resultFlag = resultFlagToSet;
    }

    /**
     * @param reserveFieldToSet The Reserve Field to Set.
     */
    @Override
    public final void setReservefield(final String reserveFieldToSet) {
        this.reserveField = reserveFieldToSet;
    }

    /**
     * @param systemReservationTerritory13ToSet
     *          The System Reservation Territory to Set.
     */
    @Override
    public final void setSystemreservationterritory13(
            final String systemReservationTerritory13ToSet) {
        this.systemReservationTerritory13 = systemReservationTerritory13ToSet;
    }

    /**
     * @param cancelNoticeFlagToSet The Cancel notice Flag to Set.
     */
    @Override
    public final void setCancelnoticeflag(final String cancelNoticeFlagToSet) {
        this.cancelNoticeFlag = cancelNoticeFlagToSet;
    }

    /**
     * @param storeidToSet The Store ID to  Set.
     */
    @Override
    public final void setStoreid(final String storeidToSet) {
        this.storeid = storeidToSet;
    }

    /**
     * @param posNoToSet the POSNoÅD to set
     */
    @Override
    public final void setPosno(final String posNoToSet) {
        this.posNo = posNoToSet;
    }

    /**
     * @param txidToSet The Transaction ID to Set.
     */
    @Override
    public final void setTxid(final String txidToSet) {
        this.txid = txidToSet;
    }

    /**
     * @param posOperateTimeToSet The POS Operate time To set.
     */
    @Override
    public final void setPosoperatetime(final String posOperateTimeToSet) {
        this.posOperateTime = posOperateTimeToSet;
    }

    /**
     * @param systemReservationTerritory14ToSet The System Reservation
     *                  Territory to set.
     */
    @Override
    public final void setSystemreservationterritory14(
            final String systemReservationTerritory14ToSet) {
        this.systemReservationTerritory14 = systemReservationTerritory14ToSet;
    }

    /**
     * @param finishFlagToSet The Finish Flag to Set.
     */
    @Override
    public final void setFinishflag(final String finishFlagToSet) {
        this.finishFlag = finishFlagToSet;
    }

    /**
     * @return legth
     */
    @Override
    public final String getLegth() {
        return legth;
    }

    /**
     * @return systemReservationTerritory2
     */
    @Override
    public final String getSystemreservationterritory2() {
        return systemReservationTerritory2;
    }

    /**
     * @return itemDiscrimination1
     */
    @Override
    public final String getItemdiscrimination1() {
        return itemDiscrimination1;
    }

    /**
     * @return systemReservationTerritory4
     */
    @Override
    public final String getSystemreservationterritory4() {
        return systemReservationTerritory4;
    }

    /**
     * @return systemReservationTerritory5
     */
    @Override
    public final String getSystemreservationterritory5() {
        return systemReservationTerritory5;
    }

    /**
     * @return countCheckFlag
     */
    @Override
    public final String getCountcheckflag() {
        return countCheckFlag;
    }

    /**
     * @return systemReservationTerritory7
     */
    @Override
    public final String getSystemreservationterritory7() {
        return systemReservationTerritory7;
    }

    /**
     * @return inqMessage
     */
    @Override
    public final String getInqmessage() {
        return inqMessage;
    }

    /**
     * @return sourceFlag
     */
    @Override
    public final String getSourceflag() {
        return sourceFlag;
    }

    /**
     * @return applicationErrorFlag
     */
    @Override
    public final String getApplicationerrorflag() {
        return applicationErrorFlag;
    }

    /**
     * @return resultFlag
     */
    @Override
    public final String getResultflag() {
        return resultFlag;
    }

    /**
     * @return reserveField
     */
    @Override
    public final String getReservefield() {
        return reserveField;
    }

    /**
     * @return systemReservationTerritory13
     */
    @Override
    public final String getSystemreservationterritory13() {
        return systemReservationTerritory13;
    }

    /**
     * @return cancelNoticeFlag
     */
    @Override
    public final String getCancelnoticeflag() {
        return cancelNoticeFlag;
    }

    /**
     * @return storeid
     */
    @Override
    public final String getStoreid() {
        return storeid;
    }

    /**
     * @return the POSNoÅD
     */
    @Override
    public final String getPosno() {
        return posNo;
    }

    /**
     * @return txid
     */
    @Override
    public final String getTxid() {
        return txid;
    }

    /**
     * @return posOperateTime
     */
    @Override
    public final String getPosoperatetime() {
        return posOperateTime;
    }

    /**
     * @return systemReservationTerritory14
     */
    @Override
    public final String getSystemreservationterritory14() {
        return systemReservationTerritory14;
    }

    /**
     * @return finishFlag
     */
    @Override
    public final String getFinishflag() {
        return finishFlag;
    }

    /**
     * @return digit
     */
    @Override
    public final String getDigit() {
        return digit;
    }

    /**
     * @return ItemDiscrimination2
     */
    @Override
    public final String getItemdiscrimination2() {
        return itemDiscrimination2;
    }

    /**
     * @return cardJudgeMethod
     */
    @Override
    public final String getCardjudgemethod() {
        return cardJudgeMethod;
    }

    /**
     * @return cardSort
     */
    @Override
    public final String getCardsort() {
        return cardSort;
    }

    /**
     * @return cardInfo
     */
    @Override
    public final String getCardinfo() {
        return cardInfo;
    }

    /**
     * @return cardInfoJIS1
     */

    @Override
    public final String getCardinfojis1() {
        return cardInfoJIS1;
    }

    /**
     * @return cardInfoJIS2
     */
    @Override
    public final String getCardinfojis2() {
        return cardInfoJIS2;
    }

    /**
     * @return systemReservationTerritory8
     */
    @Override
    public final String getSystemreservationterritory8() {
        return systemReservationTerritory8;
    }

    /**
     * @return systemReservationTerritory9
     */
    @Override
    public final String getSystemreservationterritory9() {
        return systemReservationTerritory9;
    }

    /**
     * @return password
     */
    @Override
    public final String getPassword() {
        return password;
    }

    /**
     * @return systemReservationTerritory11
     */
    @Override
    public final String getSystemreservationterritory11() {
        return systemReservationTerritory11;
    }

    /**
     * @return termValidity
     */
    @Override
    public final String getTermvalidity() {
        return termValidity;
    }

    /**
     * @return paymentseq
     */
    @Override
    public final String getPaymentseq() {
        return paymentseq;
    }

    /**
     * @return originalVoucherNo
     */
    @Override
    public final String getOriginalvoucherno() {
        return originalVoucherNo;
    }

    /**
     * @return productCode
     */
    @Override
    public final String getProductcode() {
        return productCode;
    }

    /**
     * @return productTotal
     */
    @Override
    public final String getProducttotal() {
        return productTotal;
    }

    /**
     * @return taxCarriage
     */
    @Override
    public final String getTaxcarriage() {
        return taxCarriage;
    }

    /**
     * @return systemReservationTerritory18
     */
    @Override
    public final String getSystemreservationterritory18() {
        return systemReservationTerritory18;
    }

    /**
     * @return systemReservationTerritory19
     */
    @Override
    public final String getSystemreservationterritory19() {
        return systemReservationTerritory19;
    }

    /**
     * @return paymentMethodId
     */
    @Override
    public final String getPaymentmethodid() {
        return paymentMethodId;
    }

    /**
     * @return beginningDate
     */
    @Override
    public final String getBeginningdate() {
        return beginningDate;
    }

    /**
     * @return initialAmount
     */
    @Override
    public final String getInitialamount() {
        return initialAmount;
    }

    /**
     * @return paymentCount
     */
    @Override
    public final String getPaymentcount() {
        return paymentCount;
    }

    /**
     * @return payAmount1
     */
    @Override
    public final String getPayamount1() {
        return payAmount1;
    }

    /**
     * @return payAmount2
     */
    @Override
    public final String getPayamount2() {
        return payAmount2;
    }

    /**
     * @return payAmount3
     */
    @Override
    public final String getPayamount3() {
        return payAmount3;
    }

    /**
     * @return payAmount4
     */
    @Override
    public final String getPayamount4() {
        return payAmount4;
    }

    /**
     * @return payAmount5
     */
    @Override
    public final String getPayamount5() {
        return payAmount5;
    }

    /**
     * @return payAmount6
     */
    @Override
    public final String getPayamount6() {
        return payAmount6;
    }

    /**
     * @return bonusCount
     */
    @Override
    public final String getBonuscount() {
        return bonusCount;
    }

    /**
     * @return bonus1
     */
    @Override
    public final String getBonus1() {
        return bonus1;
    }

    /**
     * @return bonus1
     */
    @Override
    public final String getBonus2() {
        return bonus2;
    }

    /**
     * @return bonus3
     */
    @Override
    public final String getBonus3() {
        return bonus3;
    }

    /**
     * @return bonus4
     */
    @Override
    public final String getBonus4() {
        return bonus4;
    }

    /**
     * @return bonus5
     */
    @Override
    public final String getBonus5() {
        return bonus5;
    }

    /**
     * @return bonus6
     */
    @Override
    public final String getBonus6() {
        return bonus6;
    }

    /**
     * @return transferFund
     */
    @Override
    public final String getTransferfund() {
        return transferFund;
    }

    /**
     * @return systemReservationTerritory38
     */
    @Override
    public final String getSystemreservationterritory38() {
        return systemReservationTerritory38;
    }

    /**
     * @return errorCode
     */
    @Override
    public final String getErrorcode() {
        return errorCode;
    }

    /**
     * @return systemReservationTerritory40
     */
    @Override
    public final String getSystemreservationterritory40() {
        return systemReservationTerritory40;
    }

    /**
     * @return approvalcode
     */
    @Override
    public final String getApprovalcode() {
        return approvalcode;
    }

    /**
     * @return systemReservationTerritory42
     */
    @Override
    public final String getSystemreservationterritory42() {
        return systemReservationTerritory42;
    }

    /**
     * @return systemReservationTerritory43
     */
    @Override
    public final String getSystemreservationterritory43() {
        return systemReservationTerritory43;
    }

    /**
     * @return systemReservationTerritory44
     */
    @Override
    public final String getSystemreservationterritory44() {
        return systemReservationTerritory44;
    }

    /**
     * @return reservation1
     */
    @Override
    public final String getReservation1() {
        return reservation1;
    }

    /**
     * @return requestCode
     */
    @Override
    public final String getRequestcode() {
        return requestCode;
    }

    /**
     * @return messageTypeId
     */
    @Override
    public final String getMessagetypeid() {
        return messageTypeId;
    }

    /**
     * @return connectCompany
     */
    @Override
    public final String getConnectcompany() {
        return connectCompany;
    }

    /**
     * @return cafisSeq
     */
    @Override
    public final String getCafisseq() {
        return cafisSeq;
    }

    /**
     * @return cafisDate
     */
    @Override
    public final String getCafisdate() {
        return cafisDate;
    }

    /**
     * @return cafisApprovalcode
     */
    @Override
    public final String getCafisapprovalcode() {
        return cafisApprovalcode;
    }

    /**
     * @return systemTraceAuditNumber
     */
    @Override
    public final String getSystemtraceauditnumber() {
        return systemTraceAuditNumber;
    }

    /**
     * @return centerReceiptDate
     */
    @Override
    public final String getCenterreceiptdate() {
        return centerReceiptDate;
    }

    /**
     * @return systemReservationTerritory54
     */
    @Override
    public final String getSystemreservationterritory54() {
        return systemReservationTerritory54;
    }

    /**
     * @return permissionCode
     */
    @Override
    public final String getPermissioncode() {
        return permissionCode;
    }

    /**
     * @return authorizationAgencyID
     */
    @Override
    public final String getAuthorizationagencyid() {
        return authorizationAgencyID;
    }

    /**
     * @return printMemberID
     */
    @Override
    public final String getPrintmemberid() {
        return printMemberID;
    }

    /**
     * @return printValidityDate
     */
    @Override
    public final String getPrintvaliditydate() {
        return printValidityDate;
    }

    /**
     * @return displayMessage
     */
    @Override
    public final String getDisplaymessage() {
        return displayMessage;
    }

    /**
     * @return printMessage
     */
    @Override
    public final String getPrintmessage() {
        return printMessage;
    }

    /**
     * @return connectCompanyCode
     */
    @Override
    public final String getConnectcompanycode() {
        return connectCompanyCode;
    }

    /**
     * @return cooperationCode
     */
    @Override
    public final String getCooperationcode() {
        return cooperationCode;
    }

    /**
     * @return reservation2
     */
    @Override
    public final String getReservation2() {
        return reservation2;
    }

    /**
     * @return iCRelatedDataValidityDigit
     */
    @Override
    public final String getIcrelateddatavaliditydigit() {
        return iCRelatedDataValidityDigit;
    }

    /**
     * @return iCRelatedData
     */
    @Override
    public final String getIcrelateddata() {
        return iCRelatedData;
    }

    /**
     * @param digitToSet The Number of Digit to set.
     */
    @Override
    public final void setDigit(final String digitToSet) {
        this.digit = digitToSet;
    }

    /**
     * @param itemDiscrimination2ToSet The Item Descrimination to Set.
     */
    @Override
    public final void setItemdiscrimination2(
            final String itemDiscrimination2ToSet) {
        this.itemDiscrimination2 = itemDiscrimination2ToSet;
    }

    /**
     * @param cardJudgeMethodToSet the Card Judge Method to Set.
     */
    @Override
    public final void setCardjudgemethod(final String cardJudgeMethodToSet) {
        this.cardJudgeMethod = cardJudgeMethodToSet;
    }

    /**
     * @param cardSortToSet The Card Sort to Set.
     */

    @Override
    public final void setCardsort(final String cardSortToSet) {
        this.cardSort = cardSortToSet;
    }

    /**
     * @param cardInfoToSet The Card Info to set.
     */
    @Override
    public final void setCardinfo(final String cardInfoToSet) {
        this.cardInfo = cardInfoToSet;
    }

    /**
     * @param cardInfoJIS1ToSet The CardInfo JSI to Set.
     */
    @Override
    public final void setCardinfojis1(final String cardInfoJIS1ToSet) {
        this.cardInfoJIS1 = cardInfoJIS1ToSet;
    }

    /**
     * @param cardInfoJIS2ToSet The Card Info JIS2 to Set.
     */
    @Override
    public final void setCardinfojis2(final String cardInfoJIS2ToSet) {
        this.cardInfoJIS2 = cardInfoJIS2ToSet;
    }

    /**
     * @param systemReservationTerritory8ToSet The System Reservation Teritory.
     */
    @Override
    public final void setSystemreservationterritory8(
            final String systemReservationTerritory8ToSet) {
        this.systemReservationTerritory8 = systemReservationTerritory8ToSet;
    }

    /**
     * @param systemReservationTerritory9ToSet The System Reservation Territory.
     */
    @Override
    public final void setSystemreservationterritory9(
            final String systemReservationTerritory9ToSet) {
        this.systemReservationTerritory9 = systemReservationTerritory9ToSet;
    }

    /**
     * @param passwordToSet The Password.
     */
    @Override
    public final void setPassword(final String passwordToSet) {
        this.password = passwordToSet;
    }

    /**
     * @param systemReservationTerritory11ToSet
     *                       The System Reservation territory.
     */
    @Override
    public final void setSystemreservationterritory11(
            final String systemReservationTerritory11ToSet) {
        this.systemReservationTerritory11 = systemReservationTerritory11ToSet;
    }

    /**
     * @param termValidityToSet The Term validity.
     */
    @Override
    public final void setTermvalidity(final String termValidityToSet) {
        this.termValidity = termValidityToSet;
    }

    /**
     * @param paymentseqToSet The payment Sequence to set.
     */
    @Override
    public final void setPaymentseq(final String paymentseqToSet) {
        this.paymentseq = paymentseqToSet;
    }

    /**
     * @param originalVoucherNoToSet The Original Voucher.
     */
    @Override
    public final void setOriginalvoucherno(
            final String originalVoucherNoToSet) {
        this.originalVoucherNo = originalVoucherNoToSet;
    }

    /**
     * @param productCodeToSet The Product code to set.
     */
    @Override
    public final void setProductcode(final String productCodeToSet) {
        this.productCode = productCodeToSet;
    }

    /**
     * @param productTotalToSet The Product total to set.
     */
    @Override
    public final void setProducttotal(final String productTotalToSet) {
        this.productTotal = productTotalToSet;
    }

    /**
     * @param taxCarriageToSet The Tax Carriage to set.
     */
    @Override
    public final void setTaxcarriage(final String taxCarriageToSet) {
        this.taxCarriage = taxCarriageToSet;
    }

    /**
     * @param systemReservationTerritory18ToSet
     *                       The System Reservation Territory.
     */
    @Override
    public final void setSystemreservationterritory18(
            final String systemReservationTerritory18ToSet) {
        this.systemReservationTerritory18 = systemReservationTerritory18ToSet;
    }

    /**
     * @param systemReservationTerritory19ToSet
     *                           The System Reservation Territory.
     */
    @Override
    public final void setSystemreservationterritory19(
            final String systemReservationTerritory19ToSet) {
        this.systemReservationTerritory19 = systemReservationTerritory19ToSet;
    }

    /**
     * @param paymentMethodIdToSet The Payment Method ID to Set.
     */
    @Override
    public final void setPaymentmethodid(final String paymentMethodIdToSet) {
        this.paymentMethodId = paymentMethodIdToSet;
    }

    /**
     * @param beginningDateToSet The Beginning Date to set.
     */
    @Override
    public final void setBeginningdate(final String beginningDateToSet) {
        this.beginningDate = beginningDateToSet;
    }

    /**
     * @param initialAmountToSet The Initial Amount to set.
     */
    @Override
    public final void setInitialamount(final String initialAmountToSet) {
        this.initialAmount = initialAmountToSet;
    }

    /**
     * @param paymentCountToSet The Payment Count to set.
     */
    @Override
    public final void setPaymentcount(final String paymentCountToSet) {
        this.paymentCount = paymentCountToSet;
    }

    /**
     * @param payAmount1ToSet The Payment Amount to set.
     */
    @Override
    public final void setPayamount1(final String payAmount1ToSet) {
        this.payAmount1 = payAmount1ToSet;
    }

    /**
     * @param payAmount2ToSet The Payment Amount.
     */
    @Override
    public final void setPayamount2(final String payAmount2ToSet) {
        this.payAmount2 = payAmount2ToSet;
    }

    /**
     * @param payAmount3ToSet The Payment Amount to Set.
     */
    @Override
    public final void setPayamount3(final String payAmount3ToSet) {
        this.payAmount3 = payAmount3ToSet;
    }

    /**
     * @param payAmount4ToSet The payment Amount to set.
     */
    @Override
    public final void setPayamount4(final String payAmount4ToSet) {
        this.payAmount4 = payAmount4ToSet;
    }

    /**
     * @param payAmount5ToSet The Payment Amount to set.
     */
    @Override
    public final void setPayamount5(final String payAmount5ToSet) {
        this.payAmount5 = payAmount5ToSet;
    }

    /**
     * @param payAmount6ToSet The Payment Amount to set.
     */
    @Override
    public final void setPayamount6(final String payAmount6ToSet) {
        this.payAmount6 = payAmount6ToSet;
    }

    /**
     * @param bonusCountToSet The Bonus Count to set.
     */
    @Override
    public final void setBonuscount(final String bonusCountToSet) {
        this.bonusCount = bonusCountToSet;
    }

    /**
     * @param bonus1ToSet The January Bonus.
     */
    @Override
    public final void setBonus1(final String bonus1ToSet) {
        this.bonus1 = bonus1ToSet;
    }

    /**
     * @param bonus2ToSet The February Bonus.
     */

    @Override
    public final void setBonus2(final String bonus2ToSet) {
        this.bonus2 = bonus2ToSet;
    }

    /**
     * @param bonus3ToSet The March Bonus.
     */
    @Override
    public final void setBonus3(final String bonus3ToSet) {
        this.bonus3 = bonus3ToSet;
    }

    /**
     * @param bonus4ToSet The April Bonus.
     */
    @Override
    public final void setBonus4(final String bonus4ToSet) {
        this.bonus4 = bonus4ToSet;
    }

    /**
     * @param bonus5ToSet The May Bonus.
     */
    @Override
    public final void setBonus5(final String bonus5ToSet) {
        this.bonus5 = bonus5ToSet;
    }

    /**
     * @param bonus6ToSet The June Bonus.
     */
    @Override
    public final void setBonus6(final String bonus6ToSet) {
        this.bonus6 = bonus6ToSet;
    }

    /**
     * @param transferFundToSet The Transfer Fund to Set.
     */
    @Override
    public final void setTransferfund(final String transferFundToSet) {
        this.transferFund = transferFundToSet;
    }

    /**
     * @param systemReservationTerritory38ToSet
     *   The System Reservation Territory.
     */
    @Override
    public final void setSystemreservationterritory38(
            final String systemReservationTerritory38ToSet) {
        this.systemReservationTerritory38 = systemReservationTerritory38ToSet;
    }

    /**
     * @param errorCodeToSet    The Error Code to Set.
     */
    @Override
    public final void setErrorcode(final String errorCodeToSet) {
        this.errorCode = errorCodeToSet;
    }

    /**
     * @param systemReservationTerritory40ToSet
     *                   The System Reservation Territory to set.
     */
    @Override
    public final void setSystemreservationterritory40(
            final String systemReservationTerritory40ToSet) {
        this.systemReservationTerritory40 = systemReservationTerritory40ToSet;
    }

    /**
     * @param approvalcodeToSet The Approval Code to set.
     */
    @Override
    public final void setApprovalcode(final String approvalcodeToSet) {
        this.approvalcode = approvalcodeToSet;
    }

    /**
     * @param systemReservationTerritory42ToSet
     *           The System Reservation Territory to set.
     */
    @Override
    public final void setSystemreservationterritory42(
            final String systemReservationTerritory42ToSet) {
        this.systemReservationTerritory42 = systemReservationTerritory42ToSet;
    }

    /**
     * @param systemReservationTerritory43ToSet
     *           The System Reservation Territory to set.
     */
    @Override
    public final void setSystemreservationterritory43(
            final String systemReservationTerritory43ToSet) {
        this.systemReservationTerritory43 = systemReservationTerritory43ToSet;
    }

    /**
     * @param systemReservationTerritory44ToSet
     *  The System Reservation territory to set.
     */
    @Override
    public final void setSystemreservationterritory44(
            final String systemReservationTerritory44ToSet) {
        this.systemReservationTerritory44 = systemReservationTerritory44ToSet;
    }

    /**
     * @param reservation1ToSet The Reservation to set.
     */
    @Override
    public final void setReservation1(final String reservation1ToSet) {
        this.reservation1 = reservation1ToSet;
    }

    /**
     * @param requestCodeToSet The Request Code to set.
     */
    @Override
    public final void setRequestcode(final String requestCodeToSet) {
        this.requestCode = requestCodeToSet;
    }

    /**
     * @param messageTypeIdToSet The Message Type ID to set.
     */
    @Override
    public final void setMessagetypeid(final String messageTypeIdToSet) {
        this.messageTypeId = messageTypeIdToSet;
    }

    /**
     * @param connectCompanyToSet The connect Company to set.
     */
    @Override
    public final void setConnectcompany(final String connectCompanyToSet) {
        this.connectCompany = connectCompanyToSet;
    }

    /**
     * @param cafisSeqToSet The CAFIS sequence number to set.
     */
    @Override
    public final void setCafisseq(final String cafisSeqToSet) {
        this.cafisSeq = cafisSeqToSet;
    }

    /**
     * @param cafisDateToSet The CAFIS Date.
     */
    @Override
    public final void setCafisdate(final String cafisDateToSet) {
        this.cafisDate = cafisDateToSet;
    }

    /**
     * @param cafisApprovalcodeToSet The CAFIS Approval code to set.
     */
    @Override
    public final void setCafisapprovalcode(
            final String cafisApprovalcodeToSet) {
        this.cafisApprovalcode = cafisApprovalcodeToSet;
    }

    /**
     * @param systemTraceAuditNumberToSet The System Trace Audit Number to set.
     */
    @Override
    public final void setSystemtraceauditnumber(
            final String systemTraceAuditNumberToSet) {
        this.systemTraceAuditNumber = systemTraceAuditNumberToSet;
    }

    /**
     * @param centerReceiptDateToSet The Center Receipt Date to set.
     */
    @Override
    public final void setCenterreceiptdate(
            final String centerReceiptDateToSet) {
        this.centerReceiptDate = centerReceiptDateToSet;
    }

    /**
     * @param systemReservationTerritory54ToSet
     *       The System reservation Territory to set.
     */
    @Override
    public final void setSystemreservationterritory54(
            final String systemReservationTerritory54ToSet) {
        this.systemReservationTerritory54 = systemReservationTerritory54ToSet;
    }

    /**
     * @param permissionCodeToSet The Permission code to set.
     */
    @Override
    public final void setPermissioncode(final String permissionCodeToSet) {
        this.permissionCode = permissionCodeToSet;
    }

    /**
     * @param authorizationAgencyIDToSet The Authorization Agency ID to set.
     */
    @Override
    public final void setAuthorizationagencyid(
            final String authorizationAgencyIDToSet) {
        this.authorizationAgencyID = authorizationAgencyIDToSet;
    }

    /**
     * @param printMemberIDToSet The print Member ID to set.
     */
    @Override
    public final void setPrintmemberid(final String printMemberIDToSet) {
        this.printMemberID = printMemberIDToSet;
    }

    /**
     * @param printValidityDateToSet The Print Validity ID to set.
     */
    @Override
    public final void setPrintvaliditydate(
            final String printValidityDateToSet) {
        this.printValidityDate = printValidityDateToSet;
    }

    /**
     * @param displayMessageToSet The Display Message to set.
     */
    @Override
    public final void setDisplaymessage(final String displayMessageToSet) {
        this.displayMessage = displayMessageToSet;
    }

    /**
     * @param printMessageToSet The print Message to set.
     */
    @Override
    public final void setPrintmessage(final String printMessageToSet) {
        this.printMessage = printMessageToSet;
    }

    /**
     * @param connectCompanyCodeToSet The Connect Company Code to set.
     */
    @Override
    public final void setConnectcompanycode(
            final String connectCompanyCodeToSet) {
        this.connectCompanyCode = connectCompanyCodeToSet;
    }

    /**
     * @param cooperationCodeToSet The Cooperation Code to set.
     */
    @Override
    public final void setCooperationcode(final String cooperationCodeToSet) {
        this.cooperationCode = cooperationCodeToSet;
    }

    /**
     * @param reservation2ToSet The Reservation to set.
     */
    @Override
    public final void setReservation2(final String reservation2ToSet) {
        this.reservation2 = reservation2ToSet;
    }

    /**
     * @param iCRelatedDataValidityDigitToSet
     *                      The IC Related Validity digit to set.
     */
    @Override
    public final void setIcrelateddatavaliditydigit(
            final String iCRelatedDataValidityDigitToSet) {
        this.iCRelatedDataValidityDigit = iCRelatedDataValidityDigitToSet;
    }

    /**
     * @param iCRelatedDataToSet The IC Related Data to set.
     */
    @Override
    public final void setIcrelateddata(final String iCRelatedDataToSet) {
        this.iCRelatedData = iCRelatedDataToSet;
    }

    /**
     * Check Pastel Port Transaction Receive.
     * @return True if checked, else, false.
     */
    @SuppressWarnings("deprecation")
    @Override
    public final boolean check() {
        tp.methodEnter("check");
        boolean isSuccessful = true;
        FileWriterWithEncoding fileWriter = null;
        String classNameString = "";
        try {

            Context env = (Context) new InitialContext()
                    .lookup("java:comp/env");

            fileWriter = new FileWriterWithEncoding(env.lookup("iowPath")
                    + "\\ppLog.txt", Charset.forName("UTF-8"), true);
            Date date = new Date();
            fileWriter.write(date.toLocaleString() + "\t\t");
            fileWriter.write("PastelPortTxRecvImpl\t\n");
            Field[] fds = Class.forName(
                    "ncr.res.pastelport.platform.PastelPortTxRecvImpl")
                    .getDeclaredFields();
            for (int i = 0; i < fds.length - 1; i++) {
                String filedname = fds[i].getName();
                if (!"cardInfoJIS1".equals(filedname)
                        && !"cardInfoJIS2".equals(filedname)
                        && !"termValidity".equals(filedname)) {
                classNameString = toPatternString(fds[i].getName(),
                        CommonBase.DIGIT_THIRTY_FIVE);
                fileWriter.write(" \t" + toPatternString(i + "",
                        CommonBase.DIGIT_TEN)
                        + toPatternString(fds[i].getName(),
                                CommonBase.DIGIT_THIRTY_FIVE)
                        + "Value:"
                        + toPatternString(fds[i].get(this).toString(),
                                CommonBase.DIGIT_TWENTY_FIVE)
                        + "\n");
                }
            }
            fileWriter.flush();

        } catch (Exception e) {
            LOGGER.logError("Exception", e.getClass().toString(), "",
                    "Message:" + e.getMessage());
            isSuccessful = false;
            try {
                fileWriter.write(toPatternString("Exception",
                        CommonBase.DIGIT_TEN)
                        + classNameString + "IsNull\n");
                fileWriter.flush();
            } catch (IOException e1) {
            	LOGGER.logError("Exception", e.getClass().toString(), "",
                        "Message:" + e.getMessage());
            }
            
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (final IOException e) {
                    LOGGER.logError("IOException", e.getClass().toString(),
                            "", "Message:" + e.getMessage());
                }
            }
            tp.methodExit(isSuccessful);
        }

        return isSuccessful;
    }

    /**
     * Make A pattern String.
     * @param initString The initial String.
     * @param lenth The length of the Patrtern String.
     * @return The Patterned String.
     */
    private String toPatternString(final String initString, final int lenth) {
        StringBuilder resultString = new StringBuilder(initString);
        if (resultString.length() < lenth) {
            int tempLenth = lenth - resultString.length();
            for (int i = 0; i < tempLenth; i++) {
                resultString.append(" ");
            }
        }
        return resultString.toString();
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
