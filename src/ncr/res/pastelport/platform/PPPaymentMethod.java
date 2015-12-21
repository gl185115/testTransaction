package ncr.res.pastelport.platform;

import ncr.realgate.util.Trace;
import ncr.res.mobilepos.helper.DebugLogger;
import ncr.res.mobilepos.helper.Logger;
import atg.taglib.json.util.JSONException;

/**
 * @author zx
 */
public class PPPaymentMethod {
    /** The Service Subtract. */
    public static final String SERVICE_SUBTRACT = "SUBTRACT";
    /** The IOWriter Program Name.*/
    private static final String PROGNAME = "AuthPP";
    /** The Debug Trace Printer. */
     private static Trace.Printer tp;

    /**
     * A private member variable used for logging the class implementations.
     */
    private static final Logger LOGGER = (Logger) Logger.getInstance(); // Get logger
    /** The Default Constructor. */
    public PPPaymentMethod() {
        tp = DebugLogger.getDbgPrinter(Thread.currentThread().getId(),
                this.getClass());
    }

    /**
     * The PAyment Method.
     * @param ppSendTx  The Pastel Port Send Base.
     * @param tx        The Common Transaction.
     * @return          The Payment method.
     * @throws JSONException The exception thrown when error occur.
     */
    public final String paymentMethod(final PastelPortTxSendBase ppSendTx,
            final CommonTx tx)
            throws JSONException {
        tp.methodEnter("PastelPortTxRecvBase")
            .println("PastelPortTxSendBase", ppSendTx.toString())
            .println("CommonTx", tx.toString());

        String resultString = "";
        String paymentMethodString = tx.getFieldValue("paymentmethod");
        String servicetype = tx.getFieldValue("service");
        if (SERVICE_SUBTRACT.equals(servicetype)) { // SUBTRACT
            ppSendTx.setHandledivision("1" + tx.getFieldValue("paymentmethod"));
        } else { // VOID OR refund
            ppSendTx.setHandledivision("4" + tx.getFieldValue("paymentmethod"));
        }

        if ("10".equals(paymentMethodString)) {
            resultString = disposablePayment(ppSendTx, tx);
        } else if ("80".equals(paymentMethodString)) {
            resultString = revolvingPayment(ppSendTx, tx);
        } else if ("21".equals(paymentMethodString)) {
            resultString = bonusPaymentPatternOne(ppSendTx, tx);
        } else if ("22".equals(paymentMethodString)) {
            resultString = bonusPaymentPatternTwo(ppSendTx, tx);
        } else if ("23".equals(paymentMethodString)) {
            resultString = bonusPaymentPatternThree(ppSendTx, tx);
        } else if ("24".equals(paymentMethodString)) {
            resultString = bonusPaymentPatternFour(ppSendTx, tx);
        } else if ("25".equals(paymentMethodString)) {
            resultString = bonusPaymentPatternFive(ppSendTx, tx);
        } else if ("61".equals(paymentMethodString)) {
            resultString = installmentsPaymentPatternOne(ppSendTx, tx);
        } else if ("62".equals(paymentMethodString)) {
            resultString = installmentsPaymentPatternTwo(ppSendTx, tx);
        } else if ("63".equals(paymentMethodString)) {
            resultString = installmentsPaymentPatternThree(ppSendTx, tx);
        } else if ("31".equals(paymentMethodString)) {
            resultString = extraPaymentAtBonusTimePatternOne(ppSendTx, tx);
        } else if ("32".equals(paymentMethodString)) {
            resultString = extraPaymentAtBonusTimePatternTwo(ppSendTx, tx);
        } else if ("33".equals(paymentMethodString)) {
            resultString = extraPaymentAtBonusTimePatternThree(ppSendTx, tx);
        } else if ("34".equals(paymentMethodString)) {
            resultString = extraPaymentAtBonusTimePatternFour(ppSendTx, tx);
        }
       tp.methodExit(resultString);
        return resultString;
    }

    /**
     * Get The disposable Payment. append string when paymentmethod =10.
     * @param ppSendTx      The Pastel Port Send Tx BAse.
     * @param tx            The Common Transaction.
     * @return  The Disposable Payment.
     */
    private String disposablePayment(
            final PastelPortTxSendBase ppSendTx, final CommonTx tx) {
        StringBuilder sbuilder = new StringBuilder();
        // start payment month
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        // firFirst
        sbuilder.append(appString(CommonBase.DIGIT_NINE));

        sbuilder.append(appString(CommonBase.DIGIT_THREE)); // splitnum
        // amountdivide1
        // amountdivide2
        // amountdivide3
        // amountdivide4
        // amountdivide5
        // amountdivide6
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        // bonusnum
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        // Monbonus1
        // Monbonus2
        // Monbonus3
        // Monbonus4
        // Monbonus5
        // Monbonus6
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setStartpayment(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setFirfirst(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setSplitnum(appString(CommonBase.DIGIT_THREE));
        ppSendTx.setAmountdivide1(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setAmountdivide2(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setAmountdivide3(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setAmountdivide4(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setAmountdivide5(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setAmountdivide6(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setBonusnum(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus1(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus2(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus3(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus4(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus5(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus6(appString(CommonBase.DIGIT_TWO));

        return sbuilder.toString();
    }

    /**
     * Get The revolving Payment. append string when paymentmethod =80.
     * @param ppSendTx      The Pastel Port Send Tx BAse.
     * @param tx            The Common Transaction.
     * @return  The Disposable Payment.
     */
    private String revolvingPayment(
            final PastelPortTxSendBase ppSendTx, final CommonTx tx) {

        return disposablePayment(ppSendTx, tx);
    }

    /**
     * Get the bonus Payment pattern one. append string when paymentmethod =21.
     * @param ppSendTx      The Pastel Port Send Tx BAse.
     * @param tx            The Common Transaction.
     * @return  The Disposable Payment.
     */
    private String bonusPaymentPatternOne(
            final PastelPortTxSendBase ppSendTx,
            final CommonTx tx) {

        return disposablePayment(ppSendTx, tx);
    }


    /**
     * Get the bonus Payment pattern two. append string when paymentmethod =22.
     * @param ppSendTx      The Pastel Port Send Tx BAse.
     * @param tx            The Common Transaction.
     * @return  The Disposable Payment.
     * @exception JSONException The exception thrown when error occur.
     */
    private String bonusPaymentPatternTwo(
            final PastelPortTxSendBase ppSendTx,
            final CommonTx tx) throws JSONException {
        StringBuilder sbuilder = new StringBuilder();
        // startpayment
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        // firfirst
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        // splitnum
        sbuilder.append(appString(CommonBase.DIGIT_THREE));
        // amountdivide1
        // amountdivide2
        // amountdivide3
        // amountdivide4
        // amountdivide5
        // amountdivide6
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        // bonusnum
        String bonusnum = "";
        try {
            bonusnum = tx.getFieldValue("paymentinfo").substring(1);
            if (bonusnum.length() != 2) {
                LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                        "PPPaymentMethod.bonusPaymentPatternTwo()",
                        Logger.RES_EXCEP_GENERAL,
                        "bonusnum  length shoud be 2!");
                throw new JSONException("bonusnum  length shoud be 2");
            }
            sbuilder.append(bonusnum);

        } catch (IndexOutOfBoundsException indexOutE) {
            LOGGER.logAlert(
                    PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.bonusPaymentPatternTwo()",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get subString to bonusnum"
                            + indexOutE.getMessage());
            throw new IndexOutOfBoundsException();
        }
        // Monbonus1
        // Monbonus2
        // Monbonus3
        // Monbonus4
        // Monbonus5
        // Monbonus6
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setStartpayment(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setFirfirst(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setSplitnum(appString(CommonBase.DIGIT_THREE));
        ppSendTx.setAmountdivide1(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setAmountdivide2(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setAmountdivide3(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setAmountdivide4(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setAmountdivide5(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setAmountdivide6(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setBonusnum(bonusnum);
        ppSendTx.setMonbonus1(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus2(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus3(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus4(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus5(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus6(appString(CommonBase.DIGIT_TWO));
        return sbuilder.toString();
    }

    /**
     * Get the bonus Payment pattern three.
     * append string when paymentmethod =23.
     * @param ppSendTx      The Pastel Port Send Tx BAse.
     * @param tx            The Common Transaction.
     * @return  The Disposable Payment.
     * @exception JSONException The exception thrown when error occur.
     */
    private String bonusPaymentPatternThree(
            final PastelPortTxSendBase ppSendTx,
            final CommonTx tx) throws JSONException {
        StringBuilder sbuilder = new StringBuilder();
        // startpayment
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        // firfirst
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        // splitnum
        sbuilder.append(appString(CommonBase.DIGIT_THREE));
        // amountdivide1
        // amountdivide2
        // amountdivide3
        // amountdivide4
        // amountdivide5
        // amountdivide6
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        sbuilder.append(appString(CommonBase.DIGIT_NINE));
        // bonusnum
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        String monbonus1 = "";
        try {
            monbonus1 = tx.getFieldValue("paymentinfo").substring(1);
            if (monbonus1.length() != 2) {
                LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                        "PPPaymentMethod.bonusPaymentPatternThree()",
                        Logger.RES_EXCEP_GENERAL,
                        "monbonus1  length shoud be 2!");
                throw new JSONException("monbonus1  length shoud be 2");
            }

        } catch (IndexOutOfBoundsException indexOutE) {
            LOGGER.logAlert(
                    PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.bonusPaymentPatternThree()",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get subString to monbonus1"
                            + indexOutE.getMessage());
            throw new IndexOutOfBoundsException();
        }
        sbuilder.append(monbonus1);
        // monbonus1
        // monbonus2
        // monbonus3
        // monbonus4
        // monbonus5
        // monbonus6
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        sbuilder.append(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setStartpayment(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setFirfirst(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setSplitnum(appString(CommonBase.DIGIT_THREE));
        ppSendTx.setAmountdivide1(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setAmountdivide2(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setAmountdivide3(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setAmountdivide4(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setAmountdivide5(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setAmountdivide6(appString(CommonBase.DIGIT_NINE));
        ppSendTx.setBonusnum(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus1(monbonus1);
        ppSendTx.setMonbonus2(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus3(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus4(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus5(appString(CommonBase.DIGIT_TWO));
        ppSendTx.setMonbonus6(appString(CommonBase.DIGIT_TWO));
        return sbuilder.toString();
    }

    /**
     * Get the bonus Payment pattern four.
     * append string when paymentmethod =24.
     * @param ppSendTx      The Pastel Port Send Tx BAse.
     * @param tx            The Common Transaction.
     * @return  The Disposable Payment.
     * @exception JSONException The exception thrown when error occur.
     */
    private String bonusPaymentPatternFour(final PastelPortTxSendBase ppSendTx,
            final CommonTx tx) throws JSONException {
        StringBuilder sbuilder = new StringBuilder();
        String paymentinfo = tx.getFieldValue("paymentinfo");
        int indexF = paymentinfo.indexOf("F");
        if (indexF == -1) {
            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.bonusPaymentPatternFour()",
                    Logger.RES_EXCEP_GENERAL,
                    "value of paymentinfo didn't right");
            throw new JSONException("paymentinfo");
        }
        try {
            // startpayment
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            // firfirst
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            // splitnum
            sbuilder.append(appString(CommonBase.DIGIT_THREE));
            // amountdivide1
            // amountdivide2
            // amountdivide3
            // amountdivide4
            // amountdivide5
            // amountdivide6
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setStartpayment(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setFirfirst(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setSplitnum(appString(CommonBase.DIGIT_THREE));
            ppSendTx.setAmountdivide1(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide2(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide3(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide4(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide5(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide6(appString(CommonBase.DIGIT_NINE));
            // Eee[Fff]
            String eeeString = "";

            if (indexF != -1) {
                eeeString = paymentinfo.substring(1, CommonBase.DIGIT_THREE);
            } else {
                eeeString = paymentinfo.substring(1);
            }
            // ee count
            int countinfo = Integer.valueOf(eeeString);
            // bonusnum
            sbuilder.append(eeeString);
            ppSendTx.setBonusnum(eeeString);
            // Monbonus1
            // Monbonus2
            // Monbonus3
            // Monbonus4
            // Monbonus5
            // Monbonus6
            if (indexF != -1) {
                String[] arraystr = new String[CommonBase.DIGIT_SIX];
                String tempString =
                    paymentinfo.substring(CommonBase.DIGIT_THREE);
                for (int i = 0; i < CommonBase.DIGIT_SIX; i++) {
                    String varStr = "";
                    if (i + 1 < countinfo) {
                        // ff
                        sbuilder.append(tempString.substring((i
                                    * CommonBase.DIGIT_THREE) + 1,
                                (i * CommonBase.DIGIT_THREE)
                                + CommonBase.DIGIT_THREE));
                        varStr = tempString.substring((i
                                * CommonBase.DIGIT_THREE) + 1,
                                (i * CommonBase.DIGIT_THREE)
                                + CommonBase.DIGIT_THREE);
                    } else if (i + 1 == countinfo && countinfo != 1) {
                        sbuilder.append(tempString.substring((i
                                * CommonBase.DIGIT_THREE) + 1));
                        varStr = tempString.substring((i
                                * CommonBase.DIGIT_THREE) + 1);
                    } else if (i == 0 && countinfo == 1) {
                        sbuilder.append(tempString.substring(1));
                        varStr = tempString.substring(1);
                    } else {
                        sbuilder.append(appString(CommonBase.DIGIT_TWO));
                        varStr = appString(CommonBase.DIGIT_TWO);
                    }
                    arraystr[i] = varStr;
                }
                ppSendTx.setMonbonus1(arraystr[0]);
                ppSendTx.setMonbonus2(arraystr[CommonBase.DIGIT_ONE]);
                ppSendTx.setMonbonus3(arraystr[CommonBase.DIGIT_TWO]);
                ppSendTx.setMonbonus4(arraystr[CommonBase.DIGIT_THREE]);
                ppSendTx.setMonbonus5(arraystr[CommonBase.DIGIT_FOUR]);
                ppSendTx.setMonbonus6(arraystr[CommonBase.DIGIT_FIVE]);
            } else {
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));

                ppSendTx.setMonbonus2(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus3(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus4(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus5(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus6(appString(CommonBase.DIGIT_TWO));

            }
        } catch (IndexOutOfBoundsException indexOutE) {

            LOGGER.logAlert(
                    PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.bonusPaymentPatternFour()",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get subString to monbonus"
                            + indexOutE.getMessage());
            throw new IndexOutOfBoundsException();
        } catch (NumberFormatException nFE) {
            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.bonusPaymentPatternFour()",
                    Logger.RES_EXCEP_GENERAL, nFE.getMessage());
            throw new NumberFormatException();

        }
        return sbuilder.toString();
    }

    /**
     * Get the bonus Payment pattern five.
     * append string when paymentmethod =25.
     * @param ppSendTx      The Pastel Port Send Tx BAse.
     * @param tx            The Common Transaction.
     * @return  The Disposable Payment.
     * @exception JSONException The exception thrown when error occur.
     */
    private String bonusPaymentPatternFive(final PastelPortTxSendBase ppSendTx,
            final CommonTx tx) throws JSONException {
        StringBuilder sbuilder = new StringBuilder();
        // Eee[FffGgggggggg]
        String paymentinfo = tx.getFieldValue("paymentinfo");
        int indexF = paymentinfo.indexOf("F");
        if (indexF == -1) {
            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.bonusPaymentPatternFive()",
                    Logger.RES_EXCEP_GENERAL,
                    "value of paymentinfo didn't right");
            throw new JSONException("paymentinfo");
        }
        try {
            // startpayment
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            // firfirst
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            // splitnum
            sbuilder.append(appString(CommonBase.DIGIT_THREE));
            ppSendTx.setStartpayment(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setFirfirst(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setSplitnum(appString(CommonBase.DIGIT_THREE));
            String eeeString = paymentinfo.substring(1, CommonBase.DIGIT_THREE);
            int countinfo = Integer.valueOf(eeeString);
            if (indexF != -1) {
                String[] arrayStr = new String[CommonBase.DIGIT_SIX];
                String tempString =
                    paymentinfo.substring(CommonBase.DIGIT_THREE);
                for (int i = 0; i < CommonBase.DIGIT_SIX; i++) {
                    String varStr = "";
                    if (i + 1 < countinfo) {
                        sbuilder.append("0"
                          + tempString.substring((CommonBase.DIGIT_TWELVE * i)
                                 + CommonBase.DIGIT_FOUR,
                                 CommonBase.DIGIT_TWELVE * i
                                 + CommonBase.DIGIT_EIGHT));
                        varStr = "0"
                          + tempString.substring((CommonBase.DIGIT_TWELVE * i)
                                  + CommonBase.DIGIT_FOUR,
                                  CommonBase.DIGIT_TWELVE * i
                                  + CommonBase.DIGIT_EIGHT);
                    } else if (i + 1 == countinfo && countinfo != 1) {
                        sbuilder.append("0"
                                + tempString.substring((CommonBase.DIGIT_TWELVE
                                        * i) - CommonBase.DIGIT_EIGHT));
                        varStr = "0"
                            + tempString.substring((CommonBase.DIGIT_TWELVE * i)
                                    - CommonBase.DIGIT_EIGHT);
                    } else if (countinfo == 1 && i == 0) {
                        sbuilder.append("0"
                                + tempString.substring(CommonBase.DIGIT_FOUR));
                        varStr = "0"
                            + tempString.substring(CommonBase.DIGIT_FOUR);
                    } else {
                        sbuilder.append(appString(CommonBase.DIGIT_NINE));
                        varStr = appString(CommonBase.DIGIT_NINE);
                    }
                    arrayStr[i] = varStr;
                }
                ppSendTx.setAmountdivide1(arrayStr[0]);
                ppSendTx.setAmountdivide2(arrayStr[CommonBase.DIGIT_ONE]);
                ppSendTx.setAmountdivide3(arrayStr[CommonBase.DIGIT_TWO]);
                ppSendTx.setAmountdivide4(arrayStr[CommonBase.DIGIT_THREE]);
                ppSendTx.setAmountdivide5(arrayStr[CommonBase.DIGIT_FOUR]);
                ppSendTx.setAmountdivide6(arrayStr[CommonBase.DIGIT_FIVE]);
                sbuilder.append(eeeString);
                ppSendTx.setBonusnum(eeeString);
                String[] arrayStrM = new String[CommonBase.DIGIT_SIX];
                for (int i = 0; i < CommonBase.DIGIT_SIX; i++) {
                    String tempStr = "";
                    if (i + 1 < countinfo) {
                        sbuilder.append(tempString.substring(
                                (i * CommonBase.DIGIT_TWELVE) + 1,
                                (i * CommonBase.DIGIT_TWELVE)
                                + CommonBase.DIGIT_THREE));
                        tempStr = tempString.substring(
                                (i * CommonBase.DIGIT_TWELVE) + 1,
                                (i * CommonBase.DIGIT_TWELVE)
                                + CommonBase.DIGIT_THREE);
                    } else if (i + 1 == countinfo && countinfo != 1) {
                        sbuilder.append(tempString.substring(
                                (i * CommonBase.DIGIT_TWELVE) + 1,
                                (i * CommonBase.DIGIT_TWELVE)
                                + CommonBase.DIGIT_THREE));
                        tempStr = tempString.substring(
                                (i * CommonBase.DIGIT_TWELVE) + 1,
                                (i * CommonBase.DIGIT_TWELVE)
                                + CommonBase.DIGIT_THREE);
                    } else if (countinfo == 1 && i == 0) {
                        sbuilder.append(tempString.substring(1,
                                CommonBase.DIGIT_THREE));
                        tempStr = tempString.substring(1,
                                CommonBase.DIGIT_THREE);
                    } else {
                        sbuilder.append(appString(CommonBase.DIGIT_TWO));
                        tempStr = appString(CommonBase.DIGIT_TWO);
                    }
                    arrayStrM[i] = tempStr;
                }
                ppSendTx.setMonbonus1(arrayStrM[0]);
                ppSendTx.setMonbonus2(arrayStrM[CommonBase.DIGIT_ONE]);
                ppSendTx.setMonbonus3(arrayStrM[CommonBase.DIGIT_TWO]);
                ppSendTx.setMonbonus4(arrayStrM[CommonBase.DIGIT_THREE]);
                ppSendTx.setMonbonus5(arrayStrM[CommonBase.DIGIT_FOUR]);
                ppSendTx.setMonbonus6(arrayStrM[CommonBase.DIGIT_FIVE]);
            } else {
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                // bonusnum
                sbuilder.append(eeeString);
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setAmountdivide1(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide2(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide3(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide4(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide5(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide6(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setBonusnum(eeeString);
                ppSendTx.setMonbonus1(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus2(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus3(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus4(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus5(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus6(appString(CommonBase.DIGIT_TWO));
            }
        } catch (IndexOutOfBoundsException indexOutE) {
            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.bonusPaymentPatternFive()",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get subString to monbonus"
                            + indexOutE.getMessage());
            throw new IndexOutOfBoundsException();
        } catch (NumberFormatException nFE) {
            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.bonusPaymentPatternFive()",
                    Logger.RES_EXCEP_GENERAL, nFE.getMessage());
            throw new NumberFormatException();
        }
        return sbuilder.toString();
    }

    /**
     * Get the intallment Payment pattern one.
     * append string when paymentmethod =61.
     * @param ppSendTx      The Pastel Port Send Tx BAse.
     * @param tx            The Common Transaction.
     * @return  The Disposable Payment.
     * @exception JSONException The exception thrown when error occur.
     */
    private String installmentsPaymentPatternOne(
            final PastelPortTxSendBase ppSendTx,
            final CommonTx tx) throws JSONException {
        StringBuilder sbuilder = new StringBuilder();
        try {
            // [Aaa]Ccc
            String paymentinfo = tx.getFieldValue("paymentinfo");
            int paymentinfolength = paymentinfo.length();
            int indexofC = paymentinfo.indexOf("C");
            // startpayment
            if (paymentinfolength > CommonBase.DIGIT_THREE) {
                sbuilder.append(paymentinfo.substring(1, indexofC));
                ppSendTx.setStartpayment(paymentinfo.substring(1, indexofC));
            } else {
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setStartpayment(appString(CommonBase.DIGIT_TWO));
            }

            // firfirst
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            // splitnum
            sbuilder.append("0" + paymentinfo.substring(paymentinfolength - 2));

            ppSendTx.setFirfirst(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setSplitnum("0"
                    + paymentinfo.substring(paymentinfolength - 2));
            // amountdivide1
            // amountdivide2
            // amountdivide3
            // amountdivide4
            // amountdivide5
            // amountdivide6
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            // bonusnum
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            // Monbonus1
            // Monbonus2
            // Monbonus3
            // Monbonus4
            // Monbonus5
            // Monbonus6
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));

            ppSendTx.setAmountdivide1(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide2(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide3(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide4(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide5(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide6(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setBonusnum(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus1(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus2(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus3(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus4(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus5(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus6(appString(CommonBase.DIGIT_TWO));
        } catch (IndexOutOfBoundsException indexOutE) {

            LOGGER.logAlert(
                    PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.bonusPaymentPatternFour()",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get subString to monbonus"
                            + indexOutE.getMessage());
            throw new IndexOutOfBoundsException();
        }
        return sbuilder.toString();
    }

    /**
     * Get the intallment Payment pattern two.
     * append string when paymentmethod =62.
     * @param ppSendTx      The Pastel Port Send Tx BAse.
     * @param tx            The Common Transaction.
     * @return  The Disposable Payment.
     * @exception JSONException The exception thrown when error occur.
     */
    private String installmentsPaymentPatternTwo(
            final PastelPortTxSendBase ppSendTx,
            final CommonTx tx) throws JSONException {
        // [Aaa]Ccc[Ddddddddd]
        StringBuilder sbuilder = new StringBuilder();
        String paymentinfo = tx.getFieldValue("paymentinfo");

        int indexofA = paymentinfo.indexOf("A");
        int indexofC = paymentinfo.indexOf("C");
        int indexofD = paymentinfo.indexOf("D");

        if (indexofC == -1) {
            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.extraPaymentAtBonusTimePatternThree()",
                    Logger.RES_EXCEP_GENERAL,
                    "value of paymentinfo didn't right");
            throw new JSONException("paymentinfo");
        }

        try {
            String tempString = "";

            if (indexofD != -1) {
                // [Aaa]Ccc[Ddddddddd]
                tempString = paymentinfo.substring(indexofC + 1,
                        indexofC + CommonBase.DIGIT_THREE);
            } else {
                // [Aaa]Ccc
                tempString = paymentinfo.substring(indexofC + 1);
            }
            int countemp = Integer.valueOf(tempString);
            // startpayment
            if (indexofA == 0) {
                sbuilder.append(
                        paymentinfo.substring(1, CommonBase.DIGIT_THREE));
                ppSendTx.setStartpayment(
                        paymentinfo.substring(1, CommonBase.DIGIT_THREE));

            } else {
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setStartpayment(appString(CommonBase.DIGIT_TWO));
            }
            // firfirst
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            // splitnum
            sbuilder.append("0" + tempString);

            ppSendTx.setFirfirst(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setSplitnum("0" + tempString);
            // [Ddddddddd]
            String endString = paymentinfo.substring(indexofD);
            // amountdivide1
            // amountdivide2
            // amountdivide3
            // amountdivide4
            // amountdivide5
            // amountdivide6
            if (indexofD != -1) {
                String[] arrayStr = new String[CommonBase.DIGIT_SIX];
                for (int i = 0; i < CommonBase.DIGIT_SIX; i++) {
                    String tempStr = "";
                    if (i < countemp) {
                        sbuilder.append("0"
                                + endString.substring(
                                        (i * CommonBase.DIGIT_NINE)
                                        + 1, (i * CommonBase.DIGIT_NINE)
                                        + CommonBase.DIGIT_NINE));
                        tempStr = "0"
                                + endString.substring(
                                        (i * CommonBase.DIGIT_NINE)
                                        + 1, (i * CommonBase.DIGIT_NINE)
                                        + CommonBase.DIGIT_NINE);
                    } else if (i == countemp && countemp != 1) {
                        sbuilder.append("0"
                                + endString.substring(
                                        (i * CommonBase.DIGIT_NINE) + 1));
                        tempStr = "0" + endString.substring(
                                (i * CommonBase.DIGIT_NINE) + 1);
                    } else if (countemp == 1 && i == 0) {
                        sbuilder.append("0" + endString.substring(1));
                        tempStr = "0" + endString.substring(1);
                    } else {
                        sbuilder.append(appString(CommonBase.DIGIT_NINE));
                        tempStr = appString(CommonBase.DIGIT_NINE);
                    }
                    arrayStr[i] = tempStr;
                }
                ppSendTx.setAmountdivide1(arrayStr[0]);
                ppSendTx.setAmountdivide2(arrayStr[CommonBase.DIGIT_ONE]);
                ppSendTx.setAmountdivide3(arrayStr[CommonBase.DIGIT_TWO]);
                ppSendTx.setAmountdivide4(arrayStr[CommonBase.DIGIT_THREE]);
                ppSendTx.setAmountdivide5(arrayStr[CommonBase.DIGIT_FOUR]);
                ppSendTx.setAmountdivide6(arrayStr[CommonBase.DIGIT_FIVE]);
            } else {
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                sbuilder.append(appString(CommonBase.DIGIT_NINE));

                ppSendTx.setAmountdivide1(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide2(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide3(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide4(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide5(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide6(appString(CommonBase.DIGIT_NINE));
            }
            // bonusnum
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            // Monbonus1
            // Monbonus2
            // Monbonus3
            // Monbonus4
            // Monbonus5
            // Monbonus6
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));

            ppSendTx.setBonusnum(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus1(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus2(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus3(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus4(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus5(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus6(appString(CommonBase.DIGIT_TWO));
        } catch (IndexOutOfBoundsException indexOutE) {

            LOGGER.logAlert(
                    PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.installmentsPaymentPatternTwo()",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get subString to amountdivide"
                            + indexOutE.getMessage());
            throw new IndexOutOfBoundsException();
        } catch (NumberFormatException nFE) {
            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.installmentsPaymentPatternTwo()",
                    Logger.RES_EXCEP_GENERAL, nFE.getMessage());
            throw new NumberFormatException();

        }
        return sbuilder.toString();
    }

    /**
     * Get the intallment Payment pattern three.
     * append string when paymentmethod =63.
     * @param ppSendTx      The Pastel Port Send Tx BAse.
     * @param tx            The Common Transaction.
     * @return  The Disposable Payment.
     * @exception JSONException The exception thrown when error occur.
     */
    private String installmentsPaymentPatternThree(
            final PastelPortTxSendBase ppSendTx,
            final CommonTx tx) throws JSONException {
        // [Aaa]BbbbbbbbbCcc
        StringBuilder sbuilder = new StringBuilder();
        String paymentinfo = tx.getFieldValue("paymentinfo");
        int indexofA = paymentinfo.indexOf("A");
        int indexofC = paymentinfo.indexOf("C");
        int indexofB = paymentinfo.indexOf("B");

        if (indexofC == -1 || indexofB == -1) {
            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.extraPaymentAtBonusTimePatternThree()",
                    Logger.RES_EXCEP_GENERAL,
                    "value of paymentinfo didn't right");
            throw new JSONException("paymentinfo");
        }

        try {
            String tempString = "";
            // cc
            tempString = paymentinfo.substring(indexofC + 1);

            // [Aaa]//startpayment
            if (indexofA == 0) {
                sbuilder.append(paymentinfo.substring(1,
                        CommonBase.DIGIT_THREE));
                ppSendTx.setStartpayment(paymentinfo.substring(1,
                        CommonBase.DIGIT_THREE));

            } else {
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setStartpayment(appString(CommonBase.DIGIT_TWO));
            }
            // Bbbbbbbbb//firfirst
            sbuilder.append("0"
                    + paymentinfo.substring(indexofB + 1, indexofC));
            // splitnum
            sbuilder.append("0" + tempString);

            ppSendTx.setFirfirst("0"
                    + paymentinfo.substring(indexofB + 1, indexofC));
            ppSendTx.setSplitnum("0" + tempString);
            // amountdivide1
            // amountdivide2
            // amountdivide3
            // amountdivide4
            // amountdivide5
            // amountdivide6

            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            // bonusnum
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            // monbonus1
            // monbonus2
            // monbonus3
            // monbonus4
            // monbonus5
            // monbonus6
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));

            ppSendTx.setAmountdivide1(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide2(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide3(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide4(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide5(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide6(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setBonusnum(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus1(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus2(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus3(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus4(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus5(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus6(appString(CommonBase.DIGIT_TWO));
        } catch (IndexOutOfBoundsException indexOutE) {
            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.extraPaymentAtBonusTimePatternThree()",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get subString to bonusnum or monbonus"
                            + indexOutE.getMessage());
            throw new IndexOutOfBoundsException();
        }
        return sbuilder.toString();
    }

    /**
     * Get the extra Payment at bonus pattern one.
     * append string when paymentmethod =31.
     * @param ppSendTx      The Pastel Port Send Tx BAse.
     * @param tx            The Common Transaction.
     * @return  The Disposable Payment.
     * @exception JSONException The exception thrown when error occur.
     */
    private String extraPaymentAtBonusTimePatternOne(
            final PastelPortTxSendBase ppSendTx, final CommonTx tx)
    throws JSONException {
        return installmentsPaymentPatternOne(ppSendTx, tx);
    }

    /**
     * Get the extra Payment at bonus pattern one.
     * append string when paymentmethod =32.
     * @param ppSendTx      The Pastel Port Send Tx BAse.
     * @param tx            The Common Transaction.
     * @return  The Disposable Payment.
     * @exception JSONException The exception thrown when error occur.
     */
    private String extraPaymentAtBonusTimePatternTwo(
            final PastelPortTxSendBase ppSendTx, final CommonTx tx)
    throws JSONException {
        // [Aaa]CccGgggggggg
        StringBuilder sbuilder = new StringBuilder();
        String paymentinfo = tx.getFieldValue("paymentinfo");

        int indexofA = paymentinfo.indexOf("A");
        int indexofC = paymentinfo.indexOf("C");
        int indexofG = paymentinfo.indexOf("G");
        if (indexofC == -1 || indexofG == -1) {
            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.extraPaymentAtBonusTimePatternThree()",
                    Logger.RES_EXCEP_GENERAL,
                    "value of paymentinfo didn't right");
            throw new JSONException("paymentinfo");
        }

        try {
            // [Aaa]Ccc
            if (indexofA == 0) {
                sbuilder.append(paymentinfo.substring(indexofA + 1, indexofC));
                ppSendTx.setStartpayment(paymentinfo.substring(indexofA + 1,
                        indexofC));
            } else {
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setStartpayment(appString(CommonBase.DIGIT_TWO));
            }
            // firfirst
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            // splitnum
            sbuilder.append("0"
                    + paymentinfo.substring(indexofC + 1, indexofG));

            ppSendTx.setFirfirst(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setSplitnum("0"
                    + paymentinfo.substring(indexofC + 1, indexofG));

            // amountdivide1
            // amountdivide2
            // amountdivide3
            // amountdivide4
            // amountdivide5
            // amountdivide6
            sbuilder.append("0" + paymentinfo.substring(indexofG + 1));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            // Monbonus1
            // Monbonus2
            // Monbonus3
            // Monbonus4
            // Monbonus5
            // Monbonus6
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));
            sbuilder.append(appString(CommonBase.DIGIT_TWO));

            ppSendTx.setAmountdivide1("0"
                    + paymentinfo.substring(indexofG + 1));
            ppSendTx.setAmountdivide2(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide3(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide4(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide5(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide6(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setBonusnum(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus1(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus2(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus3(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus4(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus5(appString(CommonBase.DIGIT_TWO));
            ppSendTx.setMonbonus6(appString(CommonBase.DIGIT_TWO));
        } catch (IndexOutOfBoundsException indexOutE) {

            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.extraPaymentAtBonusTimePatternOne()",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get subString to splitnum or amountdivide1"
                            + indexOutE.getMessage());
            throw new IndexOutOfBoundsException();
        }
        return sbuilder.toString();
    }

    /**
     * Get the extra Payment at bonus pattern three.
     * append string when paymentmethod =33.
     * @param ppSendTx      The Pastel Port Send Tx BAse.
     * @param tx            The Common Transaction.
     * @return  The Disposable Payment.
     * @exception JSONException The exception thrown when error occur.
     */
    private String extraPaymentAtBonusTimePatternThree(
            final PastelPortTxSendBase ppSendTx, final CommonTx tx)
    throws JSONException {
        // [Aaa]CccEee[Fff]
        StringBuilder sbuilder = new StringBuilder();
        String paymentinfo = tx.getFieldValue("paymentinfo");
        int indexofA = paymentinfo.indexOf("A");
        int indexofC = paymentinfo.indexOf("C");
        int indexofE = paymentinfo.indexOf("E");
        int indexofF = paymentinfo.indexOf("F");

        if (indexofC == -1 || indexofE == -1) {
            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.extraPaymentAtBonusTimePatternThree()",
                    Logger.RES_EXCEP_GENERAL,
                    "value of paymentinfo didn't right");
            throw new JSONException("paymentinfo");
        }

        try {
            String eeString = "";
            if (indexofA != -1) { // startpayment
                sbuilder.append(paymentinfo.substring(indexofA + 1, indexofC));
                ppSendTx.setStartpayment(paymentinfo.substring(indexofA + 1,
                        indexofC));
            } else {
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setStartpayment(appString(CommonBase.DIGIT_TWO));
            }
            // firfirst
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            // splitnum
            sbuilder.append("0"
                    + paymentinfo.substring(indexofC + 1, indexofE));

            ppSendTx.setFirfirst(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setSplitnum("0"
                    + paymentinfo.substring(indexofC + 1, indexofE));

            // amountdivide1
            // amountdivide2
            // amountdivide3
            // amountdivide4
            // amountdivide5
            // amountdivide6
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            sbuilder.append(appString(CommonBase.DIGIT_NINE));

            ppSendTx.setAmountdivide1(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide2(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide3(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide4(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide5(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setAmountdivide6(appString(CommonBase.DIGIT_NINE));

            if (indexofF != -1) {
                eeString = paymentinfo.substring(indexofE + 1, indexofF);
            } else {
                eeString = paymentinfo.substring(indexofE + 1);
            }
            int countff = Integer.valueOf(eeString);
            sbuilder.append(eeString); // bonusnum
            ppSendTx.setBonusnum(eeString);
            String ffString = paymentinfo.substring(indexofF);
            // Monbonus1
            // Monbonus2
            // Monbonus3
            // Monbonus4
            // Monbonus5
            // Monbonus6
            if (indexofF != -1) {
                String[] arrayStr = new String[CommonBase.DIGIT_SIX];
                for (int i = 0; i < CommonBase.DIGIT_SIX; i++) {
                    String tempStr = "";
                    if (i + 1 < countff) {
                        sbuilder.append(ffString
                                .substring(i * CommonBase.DIGIT_THREE + 1,
                                        i * CommonBase.DIGIT_THREE
                                        + CommonBase.DIGIT_THREE));
                        tempStr = ffString.substring(
                                i * CommonBase.DIGIT_THREE + 1,
                                i * CommonBase.DIGIT_THREE
                                + CommonBase.DIGIT_THREE);
                    } else if (i + 1 == countff && countff != 1) {
                        sbuilder.append(ffString.substring(
                                i * CommonBase.DIGIT_THREE + 1));
                        tempStr = ffString.substring(
                                i * CommonBase.DIGIT_THREE + 1);
                    } else if (i == 0 && countff == 1) {
                        sbuilder.append(ffString.substring(1));
                        tempStr = ffString.substring(1);
                    } else {
                        sbuilder.append(appString(CommonBase.DIGIT_TWO));
                        tempStr = appString(CommonBase.DIGIT_TWO);

                    }
                    arrayStr[i] = tempStr;
                }

                ppSendTx.setMonbonus1(arrayStr[0]);
                ppSendTx.setMonbonus2(arrayStr[CommonBase.DIGIT_ONE]);
                ppSendTx.setMonbonus3(arrayStr[CommonBase.DIGIT_TWO]);
                ppSendTx.setMonbonus4(arrayStr[CommonBase.DIGIT_THREE]);
                ppSendTx.setMonbonus5(arrayStr[CommonBase.DIGIT_FOUR]);
                ppSendTx.setMonbonus6(arrayStr[CommonBase.DIGIT_FIVE]);
            } else {
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));

                ppSendTx.setMonbonus1(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus2(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus3(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus4(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus5(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus6(appString(CommonBase.DIGIT_TWO));
            }
        } catch (IndexOutOfBoundsException indexOutE) {

            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.extraPaymentAtBonusTimePatternThree()",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get subString to bonusnum or monbonus"
                            + indexOutE.getMessage());
            throw new IndexOutOfBoundsException();
        } catch (NumberFormatException nFE) {
            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.extraPaymentAtBonusTimePatternThree()",
                    Logger.RES_EXCEP_GENERAL, nFE.getMessage());
            throw new NumberFormatException();

        }
        return sbuilder.toString();
    }

    /**
     * Get the extra Payment at bonus pattern four.
     * append string when paymentmethod =34.
     * @param ppSendTx      The Pastel Port Send Tx BAse.
     * @param tx            The Common Transaction.
     * @return  The Disposable Payment.
     * @exception JSONException The exception thrown when error occur.
     */
    private String extraPaymentAtBonusTimePatternFour(
            final PastelPortTxSendBase ppSendTx, final CommonTx tx)
    throws JSONException {
        // [Aaa]CccEee[FffGgggggggg]
        StringBuilder sbuilder = new StringBuilder();
        String paymentinfo = tx.getFieldValue("paymentinfo");
        int indexofA = paymentinfo.indexOf("A");
        int indexofC = paymentinfo.indexOf("C");
        int indexofE = paymentinfo.indexOf("E");
        int indexofF = paymentinfo.indexOf("F");
        if (indexofC == -1 || indexofE == -1) {
            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.extraPaymentAtBonusTimePatternThree()",
                    Logger.RES_EXCEP_GENERAL,
                    "value of paymentinfo didn't right");
            throw new JSONException("paymentinfo");
        }
        try {
            String ffString;
            String eeString = "";
            if (indexofF != -1) {
                eeString = paymentinfo.substring(indexofE + 1, indexofF);
            } else {
                eeString = paymentinfo.substring(indexofE + 1);
            }
            int countff = Integer.valueOf(eeString);

            if (indexofA != -1) { // startpayment
                sbuilder.append(paymentinfo.substring(indexofA + 1, indexofC));
                ppSendTx.setStartpayment(paymentinfo.substring(indexofA + 1,
                        indexofC));
            } else {
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setStartpayment(appString(CommonBase.DIGIT_TWO));
            }
            // firfirst
            sbuilder.append(appString(CommonBase.DIGIT_NINE));
            // splitnum
            sbuilder.append("0"
                    + paymentinfo.substring(indexofC + 1, indexofE));
            ppSendTx.setFirfirst(appString(CommonBase.DIGIT_NINE));
            ppSendTx.setSplitnum("0"
                    + paymentinfo.substring(indexofC + 1, indexofE));
            if (indexofF != -1) {
                ffString = paymentinfo.substring(indexofF); // [FffGgggggggg]
                for (int i = 0; i < CommonBase.DIGIT_SIX; i++) {
                    if (i + 1 < countff) {
                        sbuilder.append("0"
                         + ffString.substring(i * CommonBase.DIGIT_TWELVE
                                 + CommonBase.DIGIT_FOUR,
                                 i * CommonBase.DIGIT_TWELVE
                                 + CommonBase.DIGIT_TWELVE));
                    } else if (i + 1 == countff && countff != 1) {
                        sbuilder.append("0"
                                + ffString.substring(
                                        i * CommonBase.DIGIT_TWELVE
                                        + CommonBase.DIGIT_FOUR));
                    } else if (i == 0 && countff == 1) {
                        sbuilder.append("0"
                                + ffString.substring(CommonBase.DIGIT_FOUR));
                    } else {
                        sbuilder.append(appString(CommonBase.DIGIT_NINE));
                    }
                }
            } else {
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                sbuilder.append(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide1(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide2(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide3(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide4(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide5(appString(CommonBase.DIGIT_NINE));
                ppSendTx.setAmountdivide6(appString(CommonBase.DIGIT_NINE));
            }
            // bonusnum
            sbuilder.append(eeString);
            ppSendTx.setBonusnum(eeString);
            if (indexofF != -1) {
                String[] arrayStrM = new String[CommonBase.DIGIT_SIX];
                ffString = paymentinfo.substring(indexofF);
                for (int i = 0; i < CommonBase.DIGIT_SIX; i++) {
                    String temp = "";
                    if (i + 1 < countff) {
                        sbuilder.append(ffString.substring(
                                i * CommonBase.DIGIT_TWELVE + 1,
                                i * CommonBase.DIGIT_TWELVE
                                + CommonBase.DIGIT_THREE));
                        temp = ffString.substring(
                                i * CommonBase.DIGIT_TWELVE + 1,
                                i * CommonBase.DIGIT_TWELVE
                                + CommonBase.DIGIT_THREE);
                    } else if (i + 1 == countff && countff != 1) {
                        sbuilder.append(ffString.substring(
                                i * CommonBase.DIGIT_TWELVE + 1,
                                i * CommonBase.DIGIT_TWELVE
                                + CommonBase.DIGIT_THREE));
                        temp = ffString.substring(
                                i * CommonBase.DIGIT_TWELVE + 1,
                                i * CommonBase.DIGIT_TWELVE
                                + CommonBase.DIGIT_THREE);
                    } else if (countff == 1 && i == 0) {
                        sbuilder.append(ffString.substring(1,
                                CommonBase.DIGIT_THREE));
                        temp = ffString.substring(1,
                                CommonBase.DIGIT_THREE);
                    } else {
                        sbuilder.append(appString(CommonBase.DIGIT_TWO));
                        temp = appString(CommonBase.DIGIT_TWO);
                    }
                    arrayStrM[i] = temp;
                }
                ppSendTx.setMonbonus1(arrayStrM[0]);
                ppSendTx.setMonbonus2(arrayStrM[CommonBase.DIGIT_ONE]);
                ppSendTx.setMonbonus3(arrayStrM[CommonBase.DIGIT_TWO]);
                ppSendTx.setMonbonus4(arrayStrM[CommonBase.DIGIT_THREE]);
                ppSendTx.setMonbonus5(arrayStrM[CommonBase.DIGIT_FOUR]);
                ppSendTx.setMonbonus6(arrayStrM[CommonBase.DIGIT_FIVE]);
            } else {
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                sbuilder.append(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus1(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus2(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus3(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus4(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus5(appString(CommonBase.DIGIT_TWO));
                ppSendTx.setMonbonus6(appString(CommonBase.DIGIT_TWO));
            }
        } catch (IndexOutOfBoundsException indexOutE) {
            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.extraPaymentAtBonusTimePatternThree()",
                    Logger.RES_EXCEP_GENERAL,
                    "Failed to get subString to"
                    + " bonusnum or monbonus or splitnum "
                            + indexOutE.getMessage());
            throw new IndexOutOfBoundsException();
        } catch (NumberFormatException nFE) {
            LOGGER.logAlert(PPPaymentMethod.PROGNAME,
                    "PPPaymentMethod.extraPaymentAtBonusTimePatternThree()",
                    Logger.RES_EXCEP_GENERAL, nFE.getMessage());
            throw new NumberFormatException();

        }
        return sbuilder.toString();
    }

    /**
     * Get an AppString to Pattern.
     * @param b The index.
     * @return The AppString to Pattern.
     */
    private String appString(final int b) {
        StringBuilder sbuilBuilder = new StringBuilder();
        for (int i = 0; i < b; i++) {
            sbuilBuilder.append("0");
        }
        return sbuilBuilder.toString();
    }

}
