package ncr.res.pastelport.platform;

/**
 * @author issuser.
 *
 */
public interface PastelPortTxRecvBase extends PastelPortHeadBase {
    /**
     * @return the number of the line
     */
    String getDigit();

    /**
     * @return Item identification
     */
    String getItemdiscrimination2();

    /**
     * @return How to determine card
     */
    String getCardjudgemethod();

    /**
     * @return Type of Card
     */
    String getCardsort();

    /**
     * @return Card information (manual)
     */
    String getCardinfo();

    /**
     * @return Card information (JIS1)
     */
    String getCardinfojis1();

    /**
     * @return Card information (JIS2)
     */
    String getCardinfojis2();

    /**
     * @return Reserved area system
     */
    String getSystemreservationterritory8();

    /**
     * @return Reserved area system
     */
    String getSystemreservationterritory9();

    /**
     * @return Private code
     */
    String getPassword();

    /**
     * @return Reserved area system
     */
    String getSystemreservationterritory11();

    /**
     * @return Expiration date
     */
    String getTermvalidity();

    /**
     * @return paymentseq
     */
    String getPaymentseq();

    /**
     * @return originalvoucherno
     */
    String getOriginalvoucherno();

    /**
     * @return the product code
     */
    String getProductcode();

    /**
     * @return the product total
     */
    String getProducttotal();

    /**
     * @return Tax and shipping
     */
    String getTaxcarriage();

    /**
     * @return Reserved area system
     */
    String getSystemreservationterritory18();

    /**
     * @return Reserved area system
     */
    String getSystemreservationterritory19();

    /**
     * @return Handling division
     */
    String getPaymentmethodid();

    /**
     * @return Starting month payment
     */
    String getBeginningdate();

    /**
     * @return Fri first
     */
    String getInitialamount();

    /**
     * @return Number of split
     */
    String getPaymentcount();

    /**
     * @return Bonus payment amount split amount (first time)
     */
    String getPayamount1();

    /**
     * @return Bonus payment amount split amount
     */
    String getPayamount2();

    /**
     * @return Bonus payment amount split amount
     */
    String getPayamount3();

    /**
     * @return Bonus payment amount split amount
     */
    String getPayamount4();

    /**
     * @return Bonus payment amount split amount
     */
    String getPayamount5();

    /**
     * @return Bonus payment amount split amount
     */
    String getPayamount6();

    /**
     * @return the number of bonus
     */
    String getBonuscount();

    /**
     * @return january bonus
     */
    String getBonus1();

    /**
     * @return february bonus
     */
    String getBonus2();

    /**
     * @return march bonus
     */
    String getBonus3();

    /**
     * @return April bonus.
     */
    String getBonus4();

    /**
     * @return May bonus.
     */
    String getBonus5();

    /**
     * @return June bonus.
     */
    String getBonus6();

    /**
     * @return transfer amount
     */
    String getTransferfund();

    /**
     * @return Systemreservationterritory38.
     */
    String getSystemreservationterritory38();

    /**
     * @return errorcode
     */
    String getErrorcode();

    /**
     * @return Systemreservationterritory40.
     */
    String getSystemreservationterritory40();

    /**
     * @return the approvalcode
     */
    String getApprovalcode();

    /**
     * @return the Systemreservationterritory42
     */
    String getSystemreservationterritory42();

    /**
     * @return the Systemreservationterritory43
     */
    String getSystemreservationterritory43();

    /**
     * @return the Systemreservationterritory44
     */
    String getSystemreservationterritory44();

    /**
     * @return Reserve
     */
    String getReservation1();

    /**
     * @return Request code
     */
    String getRequestcode();

    /**
     * @return Message type ID
     */
    String getMessagetypeid();

    /**
     * @return Connection company name
     */
    String getConnectcompany();

    /**
     * @return Processing sequence number CAFIS
     */
    String getCafisseq();

    /**
     * @return Years CAFIS processing
     */
    String getCafisdate();

    /**
     * @return (First digit above) approval number CAFIS
     */
    String getCafisapprovalcode();

    /**
     * @return System trace audit number
     */
    String getSystemtraceauditnumber();

    /**
     * @return Reception center date
     */
    String getCenterreceiptdate();

    /**
     * @return Systemreservationterritory54.
     */
    String getSystemreservationterritory54();

    /**
     * @return Authorization code
     */
    String getPermissioncode();

    /**
     * @return Agency ID number authorization is
     */
    String getAuthorizationagencyid();

    /**
     * @return Membership number printed data transmission)
     */
    String getPrintmemberid();

    /**
     * @return Expiration date printed data transmission)
     */
    String getPrintvaliditydate();

    /**
     * @return the Data) display the message transmission
     */
    String getDisplaymessage();

    /**
     * @return the Message data printing) printer transmission
     */
    String getPrintmessage();

    /**
     * @return the Company code connection
     */
    String getConnectcompanycode();

    /**
     * @return the Code and non-alliance alliance
     */
    String getCooperationcode();

    /**
     * @return the Reserve
     */
    String getReservation2();

    /**
     * @return the The number of digits in the data related to IC
     */
    String getIcrelateddatavaliditydigit();

    /**
     * @return the IC-related data
     */
    String getIcrelateddata();

    /**
     * @param digit The Digit.
     */
    void setDigit(String digit);

    /**
     * @param itemDiscrimination2 The item descrimination.
     */
    void setItemdiscrimination2(String itemDiscrimination2);

    /**
     * @param cardJudgeMethod The Card Judge Method.
     */
    void setCardjudgemethod(String cardJudgeMethod);

    /**
     * @param cardSort The Card Sort.
     */
    void setCardsort(String cardSort);

    /**
     * @param cardInfo  The Card Information.
     */
    void setCardinfo(String cardInfo);

    /**
     * @param cardInfoJIS1 The Card Info in JIS1.
     */
    void setCardinfojis1(String cardInfoJIS1);

    /**
     * @param cardInfoJIS2 The Card Info in JIS2.
     */
    void setCardinfojis2(String cardInfoJIS2);

    /**
     * @param systemReservationTerritory8  The System Reservation territory.
     */
    void setSystemreservationterritory8(
            String systemReservationTerritory8);

    /**
     * @param systemReservationTerritory9 The System Reservation territory.
     */
    void setSystemreservationterritory9(
            String systemReservationTerritory9);

    /**
     * @param password The Password.
     */
    void setPassword(String password);

    /**
     * @param systemReservationTerritory11 The System Reservation territory.
     */
    void setSystemreservationterritory11(
            String systemReservationTerritory11);

    /**
     * @param termValidity The Term validity.
     */
    void setTermvalidity(String termValidity);

    /**
     * @param paymentseq The Payment Sequence.
     */
    void setPaymentseq(String paymentseq);

    /**
     * @param originalVoucherNo The Original Voucher Number.
     */
    void setOriginalvoucherno(String originalVoucherNo);

    /**
     * @param productCode The Product code.
     */
    void setProductcode(String productCode);

    /**
     * @param productTotal The Product total.
     */
    void setProducttotal(String productTotal);

    /**
     * @param taxCarriage The Tax Carriage.
     */
    void setTaxcarriage(String taxCarriage);

    /**
     * @param systemReservationTerritory18 The System Reservation teritorry.
     */
    void setSystemreservationterritory18(
            String systemReservationTerritory18);

    /**
     * @param systemReservationTerritory19 The System reservation Territory.
     */
    void setSystemreservationterritory19(
            String systemReservationTerritory19);

    /**
     * @param paymentMethodId The Payment Method ID.
     */
    void setPaymentmethodid(String paymentMethodId);

    /**
     * @param beginningDate Thje Beginning date.
     * */
    void setBeginningdate(String beginningDate);

    /**
     * @param initialAmount
     *            the Initial Amount to set
     */
    void setInitialamount(String initialAmount);

    /**
     * @param paymentCount The Payment count.
     */
    void setPaymentcount(String paymentCount);

    /**
     * @param payAmount1 The Pay Amount.
     */
    void setPayamount1(String payAmount1);

    /**
     * @param payAmount2 The Pay Amount.
     */
    void setPayamount2(String payAmount2);

    /**
     * @param payAmount3 The Pay Amount.
     */
    void setPayamount3(String payAmount3);

    /**
     * @param payAmount4 The Pay Amount.
     */
    void setPayamount4(String payAmount4);

    /**
     * @param payAmount5 The Pay Amount.
     */
    void setPayamount5(String payAmount5);

    /**
     * @param payAmount6 The Pay Amount.
     */
    void setPayamount6(String payAmount6);

    /**
     * @param bonusCount The Bonus Count.
     */
    void setBonuscount(String bonusCount);

    /**
     * @param bonus1 The Bonus.
     */
    void setBonus1(String bonus1);

    /**
     * @param bonus2 The Bonus.
     */
    void setBonus2(String bonus2);

    /**
     * @param bonus3 The Bonus.
     */
    void setBonus3(String bonus3);

    /**
     * @param bonus4 The Bonus.
     */
    void setBonus4(String bonus4);

    /**
     * @param bonus5 The Bonus.
     */
    void setBonus5(String bonus5);

    /**
     * @param bonus6 The Bonus.
     */
    void setBonus6(String bonus6);

    /**
     * @param transferFund The Transfer fund.
     */
    void setTransferfund(String transferFund);

    /**
     * @param systemReservationTerritory38 The System Reservation territory.
     */
    void setSystemreservationterritory38(
            String systemReservationTerritory38);

    /**
     * @param errorCode The Error Code.
     */
    void setErrorcode(String errorCode);

    /**
     * @param systemReservationTerritory40 The System reservation territory.
     */
    void setSystemreservationterritory40(
            String systemReservationTerritory40);

    /**
     * @param approvalcode The Approval Code.
     */
    void setApprovalcode(String approvalcode);

    /**
     * @param systemReservationTerritory42 The System Reservation territory.
     */
    void setSystemreservationterritory42(
            String systemReservationTerritory42);

    /**
     * @param systemReservationTerritory43 The System reservation territory.
     */
    void setSystemreservationterritory43(
            String systemReservationTerritory43);

    /**
     * @param systemReservationTerritory44 The System reservation territory.
     */
    void setSystemreservationterritory44(
            String systemReservationTerritory44);

    /**
     * @param reservation1 The Reservation.
     */
    void setReservation1(String reservation1);

    /**
     * @param requestCode The Request Code.
     */
    void setRequestcode(String requestCode);

    /**
     * @param messageTypeId The Message Type ID.
     */
    void setMessagetypeid(String messageTypeId);

    /**
     * @param connectCompany The Connect Company.
     */
    void setConnectcompany(String connectCompany);

    /**
     * @param cafisSeq The CafisSeq.
     */
    void setCafisseq(String cafisSeq);

    /**
     * @param cafisDate The CafisDate.
     */
    void setCafisdate(String cafisDate);

    /**
     * @param cafisApprovalcode The Cafis Approval Code.
     */
    void setCafisapprovalcode(String cafisApprovalcode);

    /**
     * @param systemTraceAuditNumber The System Trace Audit Number.
     */
    void setSystemtraceauditnumber(String systemTraceAuditNumber);

    /**
     * @param centerReceiptDate The Center Receipt Date.
     */
    void setCenterreceiptdate(String centerReceiptDate);

    /**
     * @param systemReservationTerritory54 The System Reservation territory.
     */
    void setSystemreservationterritory54(
            String systemReservationTerritory54);

    /**
     * @param permissionCode The Permission code.
     */
    void setPermissioncode(String permissionCode);

    /**
     * @param authorizationAgencyID The Authorization Agency ID.
     */
    void setAuthorizationagencyid(String authorizationAgencyID);

    /**
     * @param printMemberID The Print Member ID.
     */
    void setPrintmemberid(String printMemberID);

    /**
     * @param printValidityDate The Print Validity Date.
     */
    void setPrintvaliditydate(String printValidityDate);

    /**
     * @param displayMessage The Display Message.
     */
    void setDisplaymessage(String displayMessage);

    /**
     * @param printMessage The Print Message.
     */
    void setPrintmessage(String printMessage);

    /**
     * @param connectCompanyCode The Connect Company Code.
     */
    void setConnectcompanycode(String connectCompanyCode);

    /**
     * @param cooperationCode The Cooperation Code.
     */
    void setCooperationcode(String cooperationCode);

    /**
     * @param reservation2 The Reservation.
     */
    void setReservation2(String reservation2);

    /**
     * @param iCRelatedDataValidityDigit The IC related Data Validity Digit.
     */
    void setIcrelateddatavaliditydigit(
            String iCRelatedDataValidityDigit);

    /**
     * @param iCRelatedData The IC Related Data.
     */
    void setIcrelateddata(String iCRelatedData);

    /**
     * @return True if Checked else false.
     */
    boolean check();
}
