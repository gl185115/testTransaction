package ncr.res.pastelport.platform;

import java.io.IOException;
import java.lang.reflect.Field;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;

import javax.naming.Context;
import javax.naming.InitialContext;

import org.apache.commons.io.output.FileWriterWithEncoding;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;

/**
 * PastelPortTxSendImpl is a class implementation for
 *  Sending Pastel Port Transaction.
 *
 */
public class PastelPortTxSendImpl implements PastelPortTxSendBase {
    /** The Message Length. */
    private String legth = "002579"; // the length of the message
    /** The System Reservation Territory 2. */
    private String systemReservationTerritory2 = "3202";
    /** The Item Discrimination 1. */
    private String itemDiscrimination1 = "U1"; // Item identification
    /** The System Reservation 4. */
    private String systemReservationTerritory4 = "000130000";
    /** The System Reservation 5. */
    private String systemReservationTerritory5 = "0000";
    /** The Count Check flag. */
    private String countCheckFlag = "0"; // the count check flag
    /** The System Reservation 7. */
    private String systemReservationTerritory7 = "000";
    /** The inquiry Message. */
    private String inqMessage = "0"; // message
    /** The Source Flag. */
    private String sourceFlag = "0"; // the source flag
    /** The Application Error flag. */
    private String applicationErrorFlag = "0"; // application error flag
    /** The Result flag. */
    private String resultFlag = "0"; // result flag
    /** The reserved  Field. */
    private String reserveField = "0000100001"; // reserve field
    /** The System Reservation Territory 13. */
    private String systemReservationTerritory13 = "0000";
    /** The Cancel Notice Flag. */
    private String cancelNoticeFlag = "0"; // cancel notice flag
    /** The Sotre ID. */
    private String storeid; // storeid
    /** The POS Number. */
    private String posNo; // POSNoÅD
    /** The Transaction Number. */
    private String txid; // the tracationNo
    /** The POS Operate Time. */
    private String posOperateTime; // POS operate time
    /** The System Reservation Territory 14. */
    private String systemReservationTerritory14 = "000"; // system reservation
    /** The Finish flag. */
    private String finishFlag = "   "; // finish flag
    /** The Line 1. */
    private String line1; // line
    /** The Identification 1. */
    private String identification1; // identification
    /** The Blank Area 1. */
    private String blankArea1; // blankArea1
    /** The Blank Area 2. */
    private String blankArea2; // blankArea2
    /** The Blank Area 3. */
    private String blankArea3; // blankArea3
    /** The Blank Area 4. */
    private String blankArea4; // blankArea4
    /** The Blank Area 5. */
    private String blankArea5; // blankArea5
    /** The Classification. */
    private String classification; // calssification
    /** The Description. */
    private String description; // description
    /** The Blank Area 6. */
    private String blankArea6; // blankArea6
    /** The Blank Area 7. */
    private String blankArea7; // blankArea7
    /** The Blank Area 8. */
    private String blankArea8; // blankArea8
    /** The Blank Area 9.  */
    private String blankArea9; // blankArea9
    /** The Line 2. */
    private String line2; // line2
    /** The Identification 2. */
    private String identification2; // indentification2
    /** The Determine. */
    private String determine; // determine
    /** The Type. */
    private String type; // type
    /** The Info. */
    private String info; // info
    /** The JIS1 info. */
    private String jis1Info; // JIS1
    /** The JIS2 info. */
    private String jis2Info; // JIS2
    /** The Blank Area 10. */
    private String blankArea10; // blankArea10
    /** The Blank Area 11. */
    private String blankArea11; // blankArea11
    /** The Code. */
    private String code; // code
    /** The Blank Area 12. */
    private String blankArea12; // blankArea12
    /** The expiration date.  */
    private String expirationDate; // expirationDate
    /** The Payment Sequence. */
    private String paymentseq; // paymentseq
    /** The Last Payment Sequence. */
    private String lastPaymentseq; // lastPaymentseq
    /** The Goods Code. */
    private String goodsCode; // goodsCode
    /** The Blank Area 13. */
    private String blankArea13; // blankArea13
    /** The TAx Shipping.  */
    private String taxShipping; // taxShipping
    /** The Blank Area 14. */
    private String blankArea14; // blankArea14
    /** The Blank Area 15. */
    private String blankArea15; // blankArea15
    /** The Handle Division. */
    private String handleDivision; // handing division
    /** The Start Payment. */
    private String startPayment; // startpayment
    /** The FIR first. */
    private String firFirst; // fir first
    /** The Split Number. */
    private String splitNum; // split num
    /** The Amount Divide (First Time). */
    private String amountDivide1; // Amount split
    /** The Amount Divide (Second Time). */
    private String amountDivide2;
    /** The Amount Divide (Third Time). */
    private String amountDivide3;
    /** The Amount Divide (Fourth Time). */
    private String amountDivide4;
    /** The Amount Divide (Fifth Time). */
    private String amountDivide5;
    /** The Amount Divide (Sixth Time). */
    private String amountDivide6;
    /** The Number of bonus. */
    private String bonusNum; // Number of bonus
    /** The January Bonus. */
    private String monBonus1;
    /** The February Bonus. */
    private String monBonus2;
    /** The March Bonus. */
    private String monBonus3;
    /** The April Bonus. */
    private String monBonus4;
    /** The May Bonus. */
    private String monBonus5;
    /** The June Bonus. */
    private String monBonus6;
    /** The Transfer Amount. */
    private String transferAmount; // transfer amount
    /** The Blank Area 16. */
    private String blankArea16; // blankArea16
    /** The Error Code. */
    private String errorCode; // error code
    /** The Blank Area 17. */
    private String blankArea17; // blankArea17
    /** The Approval Number. */
    private String approvalNum; // approval num
    /** The Blank Area 18. */
    private String blankArea18; // blankArea18
    /** The Blank Area 19. */
    private String blankArea19; // blankArea19
    /** The Blank Area 20. */
    private String blankArea20; // blankArea20
    /** The Blank Area 21. */
    private String blankArea21; // blankArea21
    /** The Request Code. */
    private String requestCode; // request code
    /** The Message Code. */
    private String messageCode; // message code
    /** The Terminal info. */
    private String terminalInfo; // terminal infomation\
    /** The Sub Code. */
    private String subCode; // sub code
    /** The Security Info. */
    private String secuInfo; // secuinfo
    /** The Security Code. */
    private String secuCode; // secucode
    /** The JIS2 Security Info. */
    private String jis2SecuInfo; // jis2SecuInfo
    /** The Alliance. */
    private String alliance; // And non-alliance alliance
    /** The Location Decision. */
    private String locationDecision; // LocationDecision
    /** The Response Data. */
    private String responseData; // responseData
    /** The Alliance Code. */
    private String allianceCode; // allianceCode
    /** The Ap Identification. */
    private String identificationAP; // the AP identification
    /** The Serial Number. */
    private String serialNum; // the serial num
    /** The Affiliated Store Business Code. */
    private String affiliatedStoreBizCode;
    /** The Process IC Card. */
    private String processICCard; // the IC processed card
    /** The Reserved 2.*/
    private String reserve2; // reserve
    /** The Number of IC Related. */
    private String iCRelatedNum; // IC Related Num
    /** The Data of IC Related. */
    private String iCRelatedData; // IC Related Data
    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get logger
    /** The Debug Trace Printer. */
    private Trace.Printer tp;

    /** Default Constructor. */
    public PastelPortTxSendImpl() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                this.getClass());
    }
    
    @Override
    public final Boolean check() {
        tp.methodEnter("check");
        int[] fieldDigit = {
        CommonBase.DIGIT_SIX, CommonBase.DIGIT_FOUR, CommonBase.DIGIT_TWO,
        CommonBase.DIGIT_NINE, CommonBase.DIGIT_FOUR, CommonBase.DIGIT_ONE,
        CommonBase.DIGIT_THREE, CommonBase.DIGIT_ONE, CommonBase.DIGIT_ONE,
        CommonBase.DIGIT_ONE, CommonBase.DIGIT_ONE, CommonBase.DIGIT_TEN,
        CommonBase.DIGIT_FOUR, CommonBase.DIGIT_ONE, CommonBase.DIGIT_FOUR,
        CommonBase.DIGIT_FOUR, CommonBase.DIGIT_FOUR, CommonBase.DIGIT_FOURTEN,
        CommonBase.DIGIT_THREE, CommonBase.DIGIT_THREE, CommonBase.DIGIT_FOUR,
        CommonBase.DIGIT_TWO, CommonBase.DIGIT_FOUR, CommonBase.DIGIT_TWO,
        CommonBase.DIGIT_TWO, CommonBase.DIGIT_ONE, CommonBase.DIGIT_FOUR,
        CommonBase.DIGIT_FOUR, CommonBase.DIGIT_SIXTEN, CommonBase.DIGIT_EIGHT,
        CommonBase.DIGIT_FOUR, CommonBase.DIGIT_EIGHT, CommonBase.DIGIT_EIGHT,
        CommonBase.DIGIT_FOUR, CommonBase.DIGIT_TWO, CommonBase.DIGIT_ONE,
        CommonBase.DIGIT_THREE, CommonBase.DIGIT_TWENTY,
        CommonBase.DIGIT_THIRTY_SEVEN,
        CommonBase.DIGIT_SIXTY_NINE, CommonBase.DIGIT_FOUR,
        CommonBase.DIGIT_ONE,
        CommonBase.DIGIT_FOUR, CommonBase.DIGIT_FOUR, CommonBase.DIGIT_SIX,
        CommonBase.DIGIT_THIRTEN, CommonBase.DIGIT_THIRTEN,
        CommonBase.DIGIT_SEVEN,
        CommonBase.DIGIT_NINE, CommonBase.DIGIT_SEVEN, CommonBase.DIGIT_NINE,
        CommonBase.DIGIT_NINE, CommonBase.DIGIT_THREE, CommonBase.DIGIT_TWO,
        CommonBase.DIGIT_NINE, CommonBase.DIGIT_THREE, CommonBase.DIGIT_NINE,
        CommonBase.DIGIT_NINE, CommonBase.DIGIT_NINE, CommonBase.DIGIT_NINE,
        CommonBase.DIGIT_NINE, CommonBase.DIGIT_NINE, CommonBase.DIGIT_TWO,
        CommonBase.DIGIT_TWO, CommonBase.DIGIT_TWO, CommonBase.DIGIT_TWO,
        CommonBase.DIGIT_TWO, CommonBase.DIGIT_TWO, CommonBase.DIGIT_TWO,
        CommonBase.DIGIT_NINE, CommonBase.DIGIT_TWO, CommonBase.DIGIT_FOUR,
        CommonBase.DIGIT_TWO, CommonBase.DIGIT_SEVEN, CommonBase.DIGIT_TWO,
        CommonBase.DIGIT_TWO, CommonBase.DIGIT_TWENTY_TWO, CommonBase.DIGIT_TWO,
        CommonBase.DIGIT_FOUR, CommonBase.DIGIT_FOUR, CommonBase.DIGIT_THIRTEN,
        CommonBase.DIGIT_ELEVEN, CommonBase.DIGIT_ONE, CommonBase.DIGIT_FOUR,
        CommonBase.DIGIT_SIX, CommonBase.DIGIT_ONE, CommonBase.DIGIT_ONE,
        CommonBase.DIGIT_ONE, CommonBase.DIGIT_TWO, CommonBase.DIGIT_THREE,
        CommonBase.DIGIT_TWO, CommonBase.DIGIT_FOUR, CommonBase.DIGIT_ONE,
        CommonBase.DIGIT_THIRTEN, CommonBase.DIGIT_FOUR,
        CommonBase.DIGIT_NINETEN_NINTY_EIGHT,
        CommonBase.DIGIT_SIX };
        Boolean isSuccessful = true;

        FileWriterWithEncoding fileWriter = null;
        String classNameString = "";
        try {

            Context env = (Context) new InitialContext()
                    .lookup("java:comp/env");
            fileWriter = new FileWriterWithEncoding(env.lookup("iowPath")
                    + "\\ppLog.txt", Charset.forName("UTF-8"),
                    true);
            PastelPortTxSendImpl obj = getPastelPortTxSendImpl();
            Field[] fds = Class.forName(
                    "ncr.res.pastelport.platform.PastelPortTxSendImpl")
                    .getDeclaredFields();
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            fileWriter.write(fmt.format(new Date()) + "\t\t");
            fileWriter.write("PastelPortTxSendImpl\t\n");
            // Loop is the count of fields fds.length minus 2.
            // 2 is IOW and Trace Printer.
            for (int i = 0; i < fds.length - 2; i++) {
                classNameString = toPatternString(fds[i].getName(),
                        CommonBase.DIGIT_THIRTY_FIVE);
                String result = "Error";
                if (null == fds[i].get(obj)
                        || fds[i].get(this).toString().length()
                        != fieldDigit[i]) {
                    result = "Error";

                    isSuccessful = false;
                } else {
                    result = "OK";
                }
                String filedname = fds[i].getName();
                if (!"jis1Info".equals(filedname)
                        && !"jis2Info".equals(filedname)
                        && !"expirationDate".equals(filedname)) {
                fileWriter.write(toPatternString(result, CommonBase.DIGIT_TEN)
                        + toPatternString(i + "", CommonBase.DIGIT_TEN)
                        + toPatternString(fds[i].getName(),
                                CommonBase.DIGIT_THIRTY_FIVE
                                )
                        + "Value:"
                        + toPatternString(fds[i].get(this).toString(),
                                CommonBase.DIGIT_TWENTY_FIVE)
                        + "\n");
                }
            }
            fileWriter.flush();
        } catch (Exception e) {
            isSuccessful = false;
            LOGGER.logError("Exception", e.getClass().toString(),
                    Logger.RES_EXCEP_GENERAL, "Message:" + e.getMessage());
            try {
                fileWriter.write(toPatternString("Exception",
                        CommonBase.DIGIT_TEN)
                        + classNameString + "IsNull\n");
                fileWriter.flush();
            } catch (IOException e1) {
                // TODO Auto-generated catch block
            	LOGGER.logError("Exception", e.getClass().toString(),
                        Logger.RES_EXCEP_IO, "Message:" + e.getMessage());
            }
            
        } finally {
            if (fileWriter != null) {
                try {
                    fileWriter.close();
                } catch (final Exception e) {
                    LOGGER.logError("IOException", e.getClass().toString(),
                            "", "Message:" + e.getMessage());
                }
            }
            tp.methodExit(isSuccessful); // 9 ìØÇ∂
        }

        return isSuccessful;
    }

    /** {@inheritDoc} */
    @Override
    public final void setLegth(final String legthToSet) {
        this.legth = legthToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setSystemreservationterritory2(
            final String systemReservationTerritory2ToSet) {
        this.systemReservationTerritory2 = systemReservationTerritory2ToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setItemdiscrimination1(
            final String itemDiscrimination1ToSet) {
        this.itemDiscrimination1 = itemDiscrimination1ToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setSystemreservationterritory4(
            final String systemReservationTerritory4ToSet) {
        this.systemReservationTerritory4 = systemReservationTerritory4ToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setSystemreservationterritory5(
            final String systemReservationTerritory5ToSet) {
        this.systemReservationTerritory5 = systemReservationTerritory5ToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setCountcheckflag(final String countCheckFlagToSet) {
        this.countCheckFlag = countCheckFlagToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setSystemreservationterritory7(
            final String systemReservationTerritory7ToSet) {
        this.systemReservationTerritory7 = systemReservationTerritory7ToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setInqmessage(final String inqMessageToSet) {
        this.inqMessage = inqMessageToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setSourceflag(final String sourceFlagToSet) {
        this.sourceFlag = sourceFlagToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setApplicationerrorflag(
            final String applicationErrorFlagToSet) {
        this.applicationErrorFlag = applicationErrorFlagToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setResultflag(final String resultFlagToSet) {
        this.resultFlag = resultFlagToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setReservefield(final String reserveFieldToSet) {
        this.reserveField = reserveFieldToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setSystemreservationterritory13(
            final String systemReservationTerritory13ToSet) {
        this.systemReservationTerritory13 = systemReservationTerritory13ToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setCancelnoticeflag(final String cancelNoticeFlagToSet) {
        this.cancelNoticeFlag = cancelNoticeFlagToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setStoreid(final String storeidToSet) {
        this.storeid = storeidToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setPosno(final String posNoToSet) {
        this.posNo = posNoToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setTxid(final String txidToSet) {
        this.txid = txidToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setPosoperatetime(final String posOperateTimeToSet) {
        this.posOperateTime = posOperateTimeToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setSystemreservationterritory14(
            final String systemReservationTerritory14ToSet) {
        this.systemReservationTerritory14 = systemReservationTerritory14ToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final void setFinishflag(final String finishFlagToSet) {
        this.finishFlag = finishFlagToSet;
    }

    /** {@inheritDoc} */
    @Override
    public final String getLegth() {
        return legth;
    }

    /** {@inheritDoc} */
    @Override
    public final String getSystemreservationterritory2() {
        return systemReservationTerritory2;
    }

    /** {@inheritDoc} */
    @Override
    public final String getItemdiscrimination1() {
        return itemDiscrimination1;
    }

    /** {@inheritDoc} */
    @Override
    public final String getSystemreservationterritory4() {
        return systemReservationTerritory4;
    }

    /** {@inheritDoc} */
    @Override
    public final String getSystemreservationterritory5() {
        return systemReservationTerritory5;
    }

    /** {@inheritDoc} */
    @Override
    public final String getCountcheckflag() {
        return countCheckFlag;
    }

    /** {@inheritDoc} */
    @Override
    public final String getSystemreservationterritory7() {
        return systemReservationTerritory7;
    }

    /** {@inheritDoc} */
    @Override
    public final String getInqmessage() {
        return inqMessage;
    }

    /** {@inheritDoc} */
    @Override
    public final String getSourceflag() {
        return sourceFlag;
    }

    /** {@inheritDoc} */
    @Override
    public final String getApplicationerrorflag() {
        return applicationErrorFlag;
    }

    /** {@inheritDoc} */
    @Override
    public final String getResultflag() {
        return resultFlag;
    }

    /** {@inheritDoc} */
    @Override
    public final String getReservefield() {
        return reserveField;
    }

    /** {@inheritDoc} */
    @Override
    public final String getSystemreservationterritory13() {
        return systemReservationTerritory13;
    }

    /** {@inheritDoc} */
    @Override
    public final String getCancelnoticeflag() {
        return cancelNoticeFlag;
    }

    /** {@inheritDoc} */
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

    /** {@inheritDoc} */
    @Override
    public final String getTxid() {
        return txid;
    }

    /** {@inheritDoc} */
    @Override
    public final String getPosoperatetime() {
        return posOperateTime;
    }

    /** {@inheritDoc} */
    @Override
    public final String getSystemreservationterritory14() {
        return systemReservationTerritory14;
    }

    /** {@inheritDoc} */
    @Override
    public final String getFinishflag() {
        return finishFlag;
    }

    /** {@inheritDoc} */
    @Override
    public final String getLine1() {
        return line1;
    }

    /** {@inheritDoc} */
    @Override
    public final String getIdentification1() {
        return identification1;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea1() {
        return blankArea1;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea2() {
        return blankArea2;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea3() {
        return blankArea3;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea4() {
        return blankArea4;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea5() {
        return blankArea5;
    }

    /** {@inheritDoc} */
    @Override
    public final String getClassification() {
        return classification;
    }

    /** {@inheritDoc} */
    @Override
    public final String getDescription() {
        return description;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea6() {
        return blankArea6;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea7() {
        return blankArea7;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea8() {
        return blankArea8;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea9() {
        return blankArea9;
    }

    /** {@inheritDoc} */
    @Override
    public final String getLine2() {
        return line2;
    }

    /** {@inheritDoc} */
    @Override
    public final String getIdentification2() {
        return identification2;
    }

    /** {@inheritDoc} */
    @Override
    public final String getDetermine() {
        return determine;
    }

    /** {@inheritDoc} */
    @Override
    public final String getType() {
        return type;
    }

    /** {@inheritDoc} */
    @Override
    public final String getInfo() {
        return info;
    }

    /** {@inheritDoc} */
    @Override
    public final String getJis1Info() {
        return jis1Info;
    }

    /** {@inheritDoc} */
    @Override
    public final String getJis2Info() {
        return jis2Info;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea10() {
        return blankArea10;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea11() {
        return blankArea11;
    }

    /** {@inheritDoc} */
    @Override
    public final String getCode() {
        return code;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea12() {
        return blankArea12;
    }

    /** {@inheritDoc} */
    @Override
    public final String getExpirationdate() {
        return expirationDate;
    }

    /** {@inheritDoc} */
    @Override
    public final String getPaymentseq() {
        return paymentseq;
    }

    /** {@inheritDoc} */
    @Override
    public final String getLastpaymentseq() {
        return lastPaymentseq;
    }

    /** {@inheritDoc} */
    @Override
    public final String getGoodscode() {
        return goodsCode;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea13() {
        return blankArea13;
    }

    /** {@inheritDoc} */
    @Override
    public final String getTaxshipping() {
        return taxShipping;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea14() {
        return blankArea14;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea15() {
        return blankArea15;
    }

    /** {@inheritDoc} */
    @Override
    public final String getHandledivision() {
        return handleDivision;
    }

    /** {@inheritDoc} */
    @Override
    public final String getStartpayment() {
        return startPayment;
    }

    /** {@inheritDoc} */
    @Override
    public final String getFirfirst() {
        return firFirst;
    }

    /** {@inheritDoc} */
    @Override
    public final String getSplitnum() {
        return splitNum;
    }

    /** {@inheritDoc} */
    @Override
    public final String getAmountdivide1() {
        return amountDivide1;
    }

    /** {@inheritDoc} */
    @Override
    public final String getAmountdivide2() {
        return amountDivide2;
    }

    /** {@inheritDoc} */
    @Override
    public final String getAmountdivide3() {
        return amountDivide3;
    }

    /** {@inheritDoc} */
    @Override
    public final String getAmountdivide4() {
        return amountDivide4;
    }

    /** {@inheritDoc} */
    @Override
    public final String getAmountdivide5() {
        return amountDivide5;
    }

    /** {@inheritDoc} */
    @Override
    public final String getAmountdivide6() {
        return amountDivide6;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBonusnum() {
        return bonusNum;
    }

    /** {@inheritDoc} */
    @Override
    public final String getMonbonus1() {
        return monBonus1;
    }

    /** {@inheritDoc} */
    @Override
    public final String getMonbonus2() {
        return monBonus2;
    }
    /** {@inheritDoc} */
    @Override
    public final String getMonbonus3() {
        return monBonus3;
    }
    /** {@inheritDoc} */
    @Override
    public final String getMonbonus4() {
        return monBonus4;
    }
    /** {@inheritDoc} */
    @Override
    public final String getMonbonus5() {
        return monBonus5;
    }
    /** {@inheritDoc} */
    @Override
    public final String getMonbonus6() {
        return monBonus6;
    }
    /** {@inheritDoc} */
    @Override
    public final String getTransferamount() {
        return transferAmount;
    }
    /** {@inheritDoc} */
    @Override
    public final String getBlankarea16() {
        return blankArea16;
    }
    /** {@inheritDoc} */
    @Override
    public final String getErrorcode() {
        return errorCode;
    }
    /** {@inheritDoc} */
    @Override
    public final String getBlankarea17() {
        return blankArea17;
    }
    /** {@inheritDoc} */
    @Override
    public final String getApprovalnum() {
        return approvalNum;
    }

    /** {@inheritDoc} */
    @Override
    public final String getBlankarea18() {
        return blankArea18;
    }
    /** {@inheritDoc} */
    @Override
    public final String getBlankarea19() {
        return blankArea19;
    }
    /** {@inheritDoc} */
    @Override
    public final String getBlankarea20() {
        return blankArea20;
    }
    /** {@inheritDoc} */
    @Override
    public final String getBlankarea21() {
        return blankArea21;
    }
    /** {@inheritDoc} */
    @Override
    public final String getRequestcode() {
        return requestCode;
    }
    /** {@inheritDoc} */
    @Override
    public final String getMessagecode() {
        return messageCode;
    }
    /** {@inheritDoc} */
    @Override
    public final String getTerminalinfo() {
        return terminalInfo;
    }
    /** {@inheritDoc} */
    @Override
    public final String getSubcode() {
        return subCode;
    }
    /** {@inheritDoc} */
    @Override
    public final String getSecuinfo() {
        return secuInfo;
    }
    /** {@inheritDoc} */
    @Override
    public final String getSecucode() {
        return secuCode;
    }
    /** {@inheritDoc} */
    @Override
    public final String getJis2Secuinfo() {
        return jis2SecuInfo;
    }
    /** {@inheritDoc} */
    @Override
    public final String getAlliance() {
        return alliance;
    }
    /** {@inheritDoc} */
    @Override
    public final String getLocationdecision() {
        return locationDecision;
    }
    /** {@inheritDoc} */
    @Override
    public final String getResponsedata() {
        return responseData;
    }
    /** {@inheritDoc} */
    @Override
    public final String getAlliancecode() {
        return allianceCode;
    }
    /** {@inheritDoc} */
    @Override
    public final String getIdentificationap() {
        return identificationAP;
    }
    /** {@inheritDoc} */
    @Override
    public final String getSerialnum() {
        return serialNum;
    }
    /** {@inheritDoc} */
    @Override
    public final String getAffiliatedstorebizcode() {
        return affiliatedStoreBizCode;
    }
    /** {@inheritDoc} */
    @Override
    public final String getProcessiccard() {
        return processICCard;
    }
    /** {@inheritDoc} */
    @Override
    public final String getReserve2() {
        return reserve2;
    }
    /** {@inheritDoc} */
    @Override
    public final String getIcrelatednum() {
        return iCRelatedNum;
    }
    /** {@inheritDoc} */
    @Override
    public final String getIcrelateddata() {
        return iCRelatedData;
    }
    /** {@inheritDoc} */
    @Override
    public final void setLine1(final String line1ToSet) {
        this.line1 = line1ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setIdentification1(final String identification1ToSet) {
        this.identification1 = identification1ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setBlankarea1(final String blankArea1ToSet) {
        this.blankArea1 = blankArea1ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setBlankarea2(final String blankArea2ToSet) {
        this.blankArea2 = blankArea2ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setBlankarea3(final String blankArea3ToSet) {
        this.blankArea3 = blankArea3ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setBlankarea4(final String blankArea4ToSet) {
        this.blankArea4 = blankArea4ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setBlankarea5(final String blankArea5ToSet) {
        this.blankArea5 = blankArea5ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setClassification(final String classificationToSet) {
        this.classification = classificationToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setDescription(final String descriptionToSet) {
        this.description = descriptionToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setBlankarea6(final String blankArea6ToSet) {
        this.blankArea6 = blankArea6ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setBlankarea7(final String blankArea7ToSet) {
        this.blankArea7 = blankArea7ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setBlankarea8(final String blankArea8ToSet) {
        this.blankArea8 = blankArea8ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setBlankarea9(final String blankArea9ToSet) {
        this.blankArea9 = blankArea9ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setLine2(final String line2ToSet) {
        this.line2 = line2ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setIdentification2(final String identification2ToSet) {
        this.identification2 = identification2ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setDetermine(final String determineToSet) {
        this.determine = determineToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setType(final String typeToSet) {
        this.type = typeToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setInfo(final String infoToSet) {
        this.info = infoToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setJis1Info(final String jis1InfoToSet) {
        this.jis1Info = jis1InfoToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setJis2Info(final String jis2InfoToSet) {
        this.jis2Info = jis2InfoToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setBlankarea10(final String blankArea10ToSet) {
        this.blankArea10 = blankArea10ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setBlankarea11(final String blankArea11ToSet) {
        this.blankArea11 = blankArea11ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setCode(final String codeToSet) {
        this.code = codeToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setBlankarea12(final String blankArea12ToSet) {
        this.blankArea12 = blankArea12ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setExpirationdate(final String expirationDateToSet) {
        this.expirationDate = expirationDateToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setPaymentseq(final String paymentseqToSet) {
        this.paymentseq = paymentseqToSet;
    }
    /**
     * @param lastPaymentseqToSet
     *             the lastPaymentseq to set
     */
    @Override
    public final void setLastpaymentseq(final String lastPaymentseqToSet) {
        this.lastPaymentseq = lastPaymentseqToSet;
    }
    /**
     * @param goodsCodeToSet
     *            the goodsCode to set
     */
    @Override
    public final void setGoodscode(final String goodsCodeToSet) {
        this.goodsCode = goodsCodeToSet;
    }
    /**
     * @param blankArea13ToSet
     *            the blankArea13 to set
     */
    @Override
    public final void setBlankarea13(final String blankArea13ToSet) {
        this.blankArea13 = blankArea13ToSet;
    }
    /**
     * @param taxShippingToSet
     *            the taxShipping to set
     */
    @Override
    public final void setTaxshipping(final String taxShippingToSet) {
        this.taxShipping = taxShippingToSet;
    }
    /**
     * @param blankArea14ToSet
     *            the blankArea14 to set
     */
    @Override
    public final void setBlankarea14(final String blankArea14ToSet) {
        this.blankArea14 = blankArea14ToSet;
    }
    /**
     * @param blankArea15ToSet
     *            the blankArea15 to set
     */
    @Override
    public final void setBlankarea15(final String blankArea15ToSet) {
        this.blankArea15 = blankArea15ToSet;
    }
    /**
     * @param handleDivisionToSet
     *            the handleDivision to set
     */
    @Override
    public final void setHandledivision(final String handleDivisionToSet) {
        this.handleDivision = handleDivisionToSet;
    }
    /**
     * @param startPaymentToSet
     *            the startPayment to set
     */
    @Override
    public final void setStartpayment(final String startPaymentToSet) {
        this.startPayment = startPaymentToSet;
    }
    /**
     * @param firFirstToSet The FIR first to set.
     */
    @Override
    public final void setFirfirst(final String firFirstToSet) {
        this.firFirst = firFirstToSet;
    }
    /**
     * @param splitNumToSet The number of splits to set.
     */
    @Override
    public final void setSplitnum(final String splitNumToSet) {
        this.splitNum = splitNumToSet;
    }
    /**
     * @param amountDivide1ToSet The Amount divided (First Time)
     */
    @Override
    public final void setAmountdivide1(final String amountDivide1ToSet) {
        this.amountDivide1 = amountDivide1ToSet;
    }
    /**
     * @param amountDivide2ToSet The Amount divided (second Time)
     */
    @Override
    public final void setAmountdivide2(final String amountDivide2ToSet) {
        this.amountDivide2 = amountDivide2ToSet;
    }
    /**
     * @param amountDivide3ToSet The Amount divided (third Time)
     */
    @Override
    public final void setAmountdivide3(final String amountDivide3ToSet) {
        this.amountDivide3 = amountDivide3ToSet;
    }
    /**
     * @param amountDivide4ToSet The Amount divided (Fourth Time)
     */
    @Override
    public final void setAmountdivide4(final String amountDivide4ToSet) {
        this.amountDivide4 = amountDivide4ToSet;
    }
    /**
     * @param amountDivide5ToSet The Amount divided (Fifth Time)
     */
    @Override
    public final void setAmountdivide5(final String amountDivide5ToSet) {
        this.amountDivide5 = amountDivide5ToSet;
    }
    /**
     * @param amountDivide6ToSet The Amount divided (Sixth Time)
     */
    @Override
    public final void setAmountdivide6(final String amountDivide6ToSet) {
        this.amountDivide6 = amountDivide6ToSet;
    }
    /**
     * @param bonusNumToSet The Number of Bonus.
     */
    @Override
    public final void setBonusnum(final String bonusNumToSet) {
        this.bonusNum = bonusNumToSet;
    }
    /**
     * @param monBonus1ToSet The January Bonus.
     */
    @Override
    public final void setMonbonus1(final String monBonus1ToSet) {
        this.monBonus1 = monBonus1ToSet;
    }
    /**
     * @param monBonus2ToSet The February Bonus.
     */
    @Override
    public final void setMonbonus2(final String monBonus2ToSet) {
        this.monBonus2 = monBonus2ToSet;
    }
    /**
     * @param monBonus3ToSet The MArch Bonus.
     */
    @Override
    public final void setMonbonus3(final String monBonus3ToSet) {
        this.monBonus3 = monBonus3ToSet;
    }
    /**
     * @param monBonus4ToSet The April Bonus.
     */
    @Override
    public final void setMonbonus4(final String monBonus4ToSet) {
        this.monBonus4 = monBonus4ToSet;
    }
    /**
     * @param monBonus5ToSet The May Bonus.
     */
    @Override
    public final void setMonbonus5(final String monBonus5ToSet) {
        this.monBonus5 = monBonus5ToSet;
    }
    /**
     * @param monBonus6ToSet The June Bonus.
     */
    @Override
    public final void setMonbonus6(final String monBonus6ToSet) {
        this.monBonus6 = monBonus6ToSet;
    }
    /**
     * @param transferAmountToSet The Transfer Amount to set.
     */
    @Override
    public final void setTransferamount(final String transferAmountToSet) {
        this.transferAmount = transferAmountToSet;
    }
    /**
     * @param blankArea16ToSet The Blank Area.
     */
    @Override
    public final void setBlankarea16(final String blankArea16ToSet) {
        this.blankArea16 = blankArea16ToSet;
    }
    /**
     * @param errorCodeToSet The Error code to set.
     */
    @Override
    public final void setErrorcode(final String errorCodeToSet) {
        this.errorCode = errorCodeToSet;
    }
    /**
     * @param blankArea17ToSet The Blank Area.
     */
    @Override
    public final void setBlankarea17(final String blankArea17ToSet) {
        this.blankArea17 = blankArea17ToSet;
    }
    /**
     * @param approvalNumToSet The Approval Number to set.
     */
    @Override
    public final void setApprovalnum(final String approvalNumToSet) {
        this.approvalNum = approvalNumToSet;
    }
    /**
     * @param blankArea18ToSet The Blank Area to set.
     */
    @Override
    public final void setBlankarea18(final String blankArea18ToSet) {
        this.blankArea18 = blankArea18ToSet;
    }
    /**
     * @param blankArea19ToSet The Blank Area to set.
     */
    @Override
    public final void setBlankarea19(final String blankArea19ToSet) {
        this.blankArea19 = blankArea19ToSet;
    }
    /**
     * @param blankArea20ToSet The Blank Area.
     */
    @Override
    public final void setBlankarea20(final String blankArea20ToSet) {
        this.blankArea20 = blankArea20ToSet;
    }
    /**
     * @param blankArea21ToSet The blank area.
     */
    @Override
    public final void setBlankarea21(final String blankArea21ToSet) {
        this.blankArea21 = blankArea21ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setRequestcode(final String requestCodeToSet) {
        this.requestCode = requestCodeToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setMessagecode(final String messageCodeToSet) {
        this.messageCode = messageCodeToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setTerminalinfo(final String terminalInfoToSet) {
        this.terminalInfo = terminalInfoToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setSubcode(final String subCodeToSet) {
        this.subCode = subCodeToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setSecuinfo(final String secuInfoToSet) {
        this.secuInfo = secuInfoToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setSecucode(final String secuCodeToSet) {
        this.secuCode = secuCodeToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setJis2Secuinfo(final String jis2SecuInfoToSet) {
        this.jis2SecuInfo = jis2SecuInfoToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setAlliance(final String allianceToSet) {
        this.alliance = allianceToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setLocationdecision(final String locationDecisionToSet) {
        this.locationDecision = locationDecisionToSet;
    }
    /**
     * @param responseDataToSet
     *            íÒågÅEîÒíÒågÅFâûìöÉfÅ[É^ the íÒågÅEîÒíÒågÅFâûìöÉfÅ[É^ to set
     */
    @Override
    public final void setResponsedata(final String responseDataToSet) {
        this.responseData = responseDataToSet;
    }
    /**
     * @param allianceCodeToSet
     *          íÒågÅEîÒíÒågÅFíÒågÅEîÒíÒågÉRÅ[Éh the íÒågÅEîÒíÒågÅFíÒågÅEîÒíÒågÉRÅ[Éh to set
     */
    @Override
    public final void setAlliancecode(final String allianceCodeToSet) {
        this.allianceCode = allianceCodeToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setIdentificationap(final String identificationAPToSet) {
        this.identificationAP = identificationAPToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setSerialnum(final String serialNumToSet) {
        this.serialNum = serialNumToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setAffiliatedstorebizcode(
            final String affiliatedStoreBizCodeToSet) {
        this.affiliatedStoreBizCode = affiliatedStoreBizCodeToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setProcessiccard(final String processICCardToSet) {
        this.processICCard = processICCardToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setReserve2(final String reserve2ToSet) {
        this.reserve2 = reserve2ToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final void setIcrelatednum(final String iCRelatedNumToSet) {
        this.iCRelatedNum = iCRelatedNumToSet;
    }
    /**
     * @param iCRelatedDataToSet
     *            The related IC to set
     */
    @Override
    public final void setIcrelateddata(final String iCRelatedDataToSet) {
        this.iCRelatedData = iCRelatedDataToSet;
    }
    /** {@inheritDoc} */
    @Override
    public final PastelPortTxSendImpl getPastelPortTxSendImpl() {
        return this;
    }
    /** {@inheritDoc} */
    @Override
    public final String toString() {
        tp.methodEnter("toString");
        StringBuilder sb = new StringBuilder(CommonBase.DIGIT_TWO_THOUSAND);
        // POS data
        sb.append(legth).append(systemReservationTerritory2)
          .append(itemDiscrimination1).append(systemReservationTerritory4)
          .append(systemReservationTerritory5).append(countCheckFlag)
          .append(systemReservationTerritory7).append(inqMessage)
          .append(sourceFlag).append(applicationErrorFlag).append(resultFlag)
          .append(reserveField).append(systemReservationTerritory13)
          .append(cancelNoticeFlag).append(storeid).append(posNo)
          .append(txid).append(posOperateTime)
          .append(systemReservationTerritory14).append(finishFlag)
          .append(line1).append(identification1).append(blankArea1)
          .append(blankArea2).append(blankArea3).append(blankArea4)
          .append(blankArea5).append(classification).append(description)
          .append(blankArea6).append(blankArea7).append(blankArea8)
          .append(blankArea9).append(line2).append(identification2)
          .append(determine).append(type).append(info).append(jis1Info)
          .append(jis2Info).append(blankArea10).append(blankArea11)
          .append(code).append(blankArea12).append(expirationDate)
          .append(paymentseq).append(lastPaymentseq).append(goodsCode)
          .append(blankArea13).append(taxShipping).append(blankArea14)
          .append(blankArea15).append(handleDivision).append(startPayment)
          .append(firFirst).append(splitNum).append(amountDivide1)
          .append(amountDivide2).append(amountDivide3).append(amountDivide4)
          .append(amountDivide5).append(amountDivide6).append(bonusNum)
          .append(monBonus1).append(monBonus2).append(monBonus3)
          .append(monBonus4).append(monBonus5).append(monBonus6)
          .append(transferAmount).append(blankArea16).append(errorCode)
          .append(blankArea17).append(approvalNum).append(blankArea18)
          .append(blankArea19).append(blankArea20).append(blankArea21)
          .append(requestCode).append(messageCode).append(terminalInfo)
          .append(subCode).append(secuInfo).append(secuCode)
          .append(jis2SecuInfo).append(alliance).append(locationDecision)
          .append(responseData).append(allianceCode)
          .append(identificationAP).append(serialNum)
          .append(affiliatedStoreBizCode).append(processICCard)
          .append(reserve2).append(iCRelatedNum).append(iCRelatedData);
        tp.methodExit();
        return sb.toString();
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
}
