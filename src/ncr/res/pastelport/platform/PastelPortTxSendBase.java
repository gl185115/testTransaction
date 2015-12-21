package ncr.res.pastelport.platform;

/**
 * @author issuser
 *
 */
public interface PastelPortTxSendBase extends PastelPortHeadBase {
    /**
     * @param line1
     *            of digits the line to set
     */
    void setLine1(String line1);

    /**
     * @param identification1
     *            identification the identification to set
     */
    void setIdentification1(String identification1);

    /**
     * @param blankArea1
     *            area system the Reserved area to set
     */
    void setBlankarea1(String blankArea1);

    /**
     * @param blankArea2
     *            area the Reserved area to set
     */
    void setBlankarea2(String blankArea2);

    /**
     * @param blankArea3
     *            area the Reserved areato set
     */
    void setBlankarea3(String blankArea3);

    /**
     * @param blankArea4
     *            area the Reserved area to set
     */
    void setBlankarea4(String blankArea4);

    /**
     * @param blankArea5
     *            area the Reserved area to set
     */
    void setBlankarea5(String blankArea5);

    /**
     * @param classification
     *            of transaction the classification to set
     */
    void setClassification(String classification);

    /**
     * @param description
     *            qualified the description to set
     */
    void setDescription(String description);

    /**
     * @param blankArea6
     *            area the blank area to set
     */
    void setBlankarea6(String blankArea6);

    /**
     * @param blankArea7
     *            area the blank area to set
     */
    void setBlankarea7(String blankArea7);

    /**
     * @param blankArea8
     *            area the blank area to set
     */
    void setBlankarea8(String blankArea8);

    /**
     * @param blankArea9
     *            area the blank area to set
     */
    void setBlankarea9(String blankArea9);

    /**
     * @param line2
     *            of digits the Number of digits to set
     */
    void setLine2(String line2);

    /**
     * @param identification2
     *            identification the identification2 to set
     */
    void setIdentification2(String identification2);

    /**
     * @param determine
     *            how to determine card the determine to set
     */
    void setDetermine(String determine);

    /**
     * @param type
     *            Type of Card the type to set
     */
    void setType(String type);

    /**
     * @param info
     *            card the Information to set
     */
    void setInfo(String info);

    /**
     * @param jis1Info
     *            the JIS1 to set
     */
    void setJis1Info(String jis1Info);

    /**
     * @param jis2Info
     *            the JIS2 to set
     */
    void setJis2Info(String jis2Info);

    /**
     * @param blankArea10
     *            area the blank area to set
     */
    void setBlankarea10(String blankArea10);

    /**
     * @param blankArea11
     *            area the blank area to set
     */
    void setBlankarea11(String blankArea11);

    /**
     * @param code
     *            code the Private code to set
     */
    void setCode(String code);

    /**
     * @param blankArea12
     *            area the blank area to set
     */
    void setBlankarea12(String blankArea12);

    /**
     * @param expirationDate
     *            date the expirationDate to set
     */
    void setExpirationdate(String expirationDate);

    /**
     * @param paymentseq
     *            . the paymentseq to set
     */
    void setPaymentseq(String paymentseq);

    /**
     * @param lastPaymentseq
     *            . the lastPaymentseq to set
     */
    void setLastpaymentseq(String lastPaymentseq);

    /**
     * @param goodsCode
     *            the goodsCode to set
     */
    void setGoodscode(String goodsCode);

    /**
     * @param blankArea13
     *            area the blank area to set
     */
    void setBlankarea13(String blankArea13);

    /**
     * @param taxShipping
     *            the taxShipping to set
     */
    void setTaxshipping(String taxShipping);

    /**
     * @param blankArea14
     *            area the blank area to set
     */
    void setBlankarea14(String blankArea14);

    /**
     * @param blankArea15
     *            area the blank area to set
     */
    void setBlankarea15(String blankArea15);

    /**
     * @param handleDivision
     *            division the handleDivision to set
     */
    void setHandledivision(String handleDivision);

    /**
     * @param startPayment
     *            month payment the Starting month payment to set
     */
    void setStartpayment(String startPayment);

    /**
     * @param firFirst
     *            first the firFirst to set
     */
    void setFirfirst(String firFirst);

    /**
     * @param splitNum
     *            of split the splitNum to set
     */
    void setSplitnum(String splitNum);

    /**
     * @param amountDivide1
     *            split the amountDivide1 to set
     */
    void setAmountdivide1(String amountDivide1);

    /**
     * @param amountDivide2
     *            the amountDivide2 to set
     */
    void setAmountdivide2(String amountDivide2);

    /**
     * @param amountDivide3
     *            the amountDivide3 to set
     */
    void setAmountdivide3(String amountDivide3);

    /**
     * @param amountDivide4
     *            the amountDivide4 to set
     */
    void setAmountdivide4(String amountDivide4);

    /**
     * @param amountDivide5
     *            the amountDivide5 to set
     */
    void setAmountdivide5(String amountDivide5);

    /**
     * @param amountDivide6
     *            the amountDivide6 to set
     */
    void setAmountdivide6(String amountDivide6);

    /**
     * @param bonusNum
     *            Number of bonus the bonusNum to set
     */
    void setBonusnum(String bonusNum);

    /**
     * @param monBonus1
     *            January bonus the monBonus1 to set
     */
    void setMonbonus1(String monBonus1);

    /**
     * @param monBonus2
     *            the monBonus2 to set
     */
    void setMonbonus2(String monBonus2);

    /**
     * @param monBonus3
     *            the monBonus3 to set
     */
    void setMonbonus3(String monBonus3);

    /**
     * @param monBonus4
     *            the monBonus4 to set
     */
    void setMonbonus4(String monBonus4);

    /**
     * @param monBonus5
     *            the monBonus5 to set
     */
    void setMonbonus5(String monBonus5);

    /**
     * @param monBonus6
     *            the monBonus6 to set
     */
    void setMonbonus6(String monBonus6);

    /**
     * @param transferAmount
     *            amount the transferAmount to set
     */
    void setTransferamount(String transferAmount);

    /**
     * @param blankArea16
     *            area the blank area to set
     */
    void setBlankarea16(String blankArea16);

    /**
     * @param errorCode
     *            code the errorCode to set
     */
    void setErrorcode(String errorCode);

    /**
     * @param blankArea17
     *            area the blank area to set
     */
    void setBlankarea17(String blankArea17);

    /**
     * @param approvalNum
     *            Number the Approval Number to set
     */
    void setApprovalnum(String approvalNum);

    /**
     * @param blankArea18
     *            area the blank area to set
     */
    void setBlankarea18(String blankArea18);

    /**
     * @param blankArea19
     *            area the blank area to set
     */
    void setBlankarea19(String blankArea19);

    /**
     * @param blankArea20
     *            area the blank area to set
     */
    void setBlankarea20(String blankArea20);

    /**
     * @param blankArea21
     *            area the blank area to set
     */
    void setBlankarea21(String blankArea21);

    /**
     * @param requestCode
     *            code the Request code to set
     */
    void setRequestcode(String requestCode);

    /**
     * @param messageCode
     *            reason code the messageCode to set
     */
    void setMessagecode(String messageCode);

    /**
     * @param terminalInfo The Terminal Information.
     */
    void setTerminalinfo(String terminalInfo);

    /**
     * @param subCode
     *            the company code of destination
     */
    void setSubcode(String subCode);

    /**
     * @param secuInfo
     *            security information
     */
    void setSecuinfo(String secuInfo);

    /**
     * @param secuCode
     *            security Code.
     */
    void setSecucode(String secuCode);

    /**
     * @param jis2SecuInfo The Security Information in JIS2.
     */
    void setJis2Secuinfo(String jis2SecuInfo);

    /**
     * @param alliance
     *            non-alliance/alliance
     *
     */
    void setAlliance(String alliance);

    /**
     * @param locationDecision The Local Decision.
     */
    void setLocationdecision(String locationDecision);

    /**
     * @param responseData The Response Data.
     */
    void setResponsedata(String responseData);

    /**
     * @param allianceCode The Alliance Code.
     */
    void setAlliancecode(String allianceCode);

    /**
     * @param identificationAP
     *            the AP Identification.
     */
    void setIdentificationap(String identificationAP);

    /**
     * @param serialNum
     *            Card serial number
     */
    void setSerialnum(String serialNum);

    /**
     * @param affiliatedStoreBizCode
     *            Merchant classification code
     */
    void setAffiliatedstorebizcode(String affiliatedStoreBizCode);

    /**
     * @param processICCard
     *            card processing level
     */
    void setProcessiccard(String processICCard);

    /**
     * @param reserve2 The reserved to set.
     */
    void setReserve2(String reserve2);

    /**
     * @param iCRelatedNum
     *           The number of digits in the data related to IC
     */
    void setIcrelatednum(String iCRelatedNum);

    /**
     * @param iCRelatedData
     *            The IC related data
     */
    void setIcrelateddata(String iCRelatedData);

    /**
     * @return Number of digits
     */
    String getLine1();

    /**
     * @return Item identification
     */
    String getIdentification1();

    /**
     * @return the blank area Reserved area system
     */
    String getBlankarea1();

    /**
     * @return the blank area
     */
    String getBlankarea2();

    /**
     * @return the blank area
     */
    String getBlankarea3();

    /**
     * @return the blank area
     */
    String getBlankarea4();

    /**
     * @return the blank area
     */
    String getBlankarea5();

    /**
     * @return Trade classification / type of transaction
     */
    String getClassification();

    /**
     * @return Trade qualified
     */
    String getDescription();

    /**
     * @return the blank area Reserved area system
     */
    String getBlankarea6();

    /**
     * @return the blank area
     */
    String getBlankarea7();

    /**
     * @return the blank area
     */
    String getBlankarea8();

    /**
     * @return the blank area
     */
    String getBlankarea9();

    /**
     * @return Number of digits
     */
    String getLine2();

    /**
     * @return Item identification
     */
    String getIdentification2();

    /**
     * @return How to determine card
     */
    String getDetermine();

    /**
     * @return Type of Card
     */
    String getType();

    /**
     * @return Card information (manual)
     */
    String getInfo();

    /**
     * @return JIS1
     */
    String getJis1Info();

    /**
     * @return JIS2
     */
    String getJis2Info();

    /**
     * @return the blank area Reserved area system
     */
    String getBlankarea10();

    /**
     * @return the blank area
     */
    String getBlankarea11();

    /**
     * @return Private code
     */
    String getCode();

    /**
     * @return the blank area
     */
    String getBlankarea12();

    /**
     * @return Expiration date
     */
    String getExpirationdate();

    /**
     * @return Paymentseq
     */
    String getPaymentseq();

    /**
     * @return Lastpaymentseq
     */
    String getLastpaymentseq();

    /**
     * @return Product code
     */
    String getGoodscode();

    /**
     * @return Reserved area system
     */
    String getBlankarea13();

    /**
     * @return Tax and shipping
     */
    String getTaxshipping();

    /**
     * @return the blank area
     */
    String getBlankarea14();

    /**
     * @return the blank area
     */
    String getBlankarea15();

    /**
     * @return Handling division
     */
    String getHandledivision();

    /**
     * @return Starting month payment
     */
    String getStartpayment();

    /**
     * @return Fri first
     */
    String getFirfirst();

    /**
     * @return Number of split
     */
    String getSplitnum();

    /**
     * @return Amount / bonus amount divided (first time)
     */
    String getAmountdivide1();

    /**
     * @return the Amountdivide2
     */
    String getAmountdivide2();

    /**
     * @return the Amountdivide3
     */
    String getAmountdivide3();

    /**
     * @return Amountdivide4
     */
    String getAmountdivide4();

    /**
     * @return Amountdivide5
     */
    String getAmountdivide5();

    /**
     * @return Amountdivide6
     */
    String getAmountdivide6();

    /**
     * @return Number of bonus
     */
    String getBonusnum();

    /**
     * @return January bonus
     */
    String getMonbonus1();

    /**
     * @return the monbonus2
     */
    String getMonbonus2();

    /**
     * @return monbonus3
     */
    String getMonbonus3();

    /**
     * @return monbonus4
     */
    String getMonbonus4();

    /**
     * @return monbonus5
     */
    String getMonbonus5();

    /**
     * @return monbonus6
     */
    String getMonbonus6();

    /**
     * @return Transfer amount
     */
    String getTransferamount();

    /**
     * @return Reserved area system
     */
    String getBlankarea16();

    /**
     * @return Error code
     */
    String getErrorcode();

    /**
     * @return the blank area
     */
    String getBlankarea17();

    /**
     * @return Approval Number
     */
    String getApprovalnum();

    /**
     * @return Reserved area system
     */
    String getBlankarea18();

    /**
     * @return the blank area
     */
    String getBlankarea19();

    /**
     * @return the blank area
     */
    String getBlankarea20();

    /**
     * @return the blank area
     */
    String getBlankarea21();

    /**
     * @return Request code
     */
    String getRequestcode();

    /**
     * @return allianceCode
     */
    String getAlliancecode();

    /**
     * @return the message code
     */
    String getMessagecode();

    /**
     * @return the terminal infomation
     */
    String getTerminalinfo();

    /**
     * @return Under the company code of destination
     */
    String getSubcode();

    /**
     * @return Information security
     */
    String getSecuinfo();

    /**
     * @return secucode
     */
    String getSecucode();

    /**
     * @return Jis2Secuinfo
     */
    String getJis2Secuinfo();

    /**
     * @return And non-alliance alliance
     */
    String getAlliance();

    /**
     * @return Locationdecision
     */
    String getLocationdecision();

    /**
     * @return Responsedata
     */
    String getResponsedata();

    /**
     * @return AP identification
     */
    String getIdentificationap();

    /**
     * @return the serial num
     */
    String getSerialnum();

    /**
     * @return Merchant classification code
     */
    String getAffiliatedstorebizcode();

    /**
     * @return IC card processing level
     */
    String getProcessiccard();

    /**
     * @return the reserve
     */
    String getReserve2();

    /**
     * @return the Icrelatednum
     */
    String getIcrelatednum();

    /**
     * @return IC-related data
     */
    String getIcrelateddata();

    /**
     * Get Pastel Port Send Request implementation.
     * @return The PastelPortTxSendImpl instance.
     */
    PastelPortTxSendImpl getPastelPortTxSendImpl();

    /*
     * (non-Javadoc)
     *
     * @see ncr.res.pastelport.platform.PastelPortHeadBase#getStoreid()
     */
    @Override
    String getStoreid();

    /*
     * (non-Javadoc)
     *
     * @see ncr.res.pastelport.platform.PastelPortHeadBase#getPosno()
     */
    @Override
    String getPosno();

    /**
     * @return string
     */
    @Override
    String toString();

    /**
     * @return boolean
     */
    Boolean check();
}
