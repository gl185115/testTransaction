package ncr.res.mobilepos.networkreceipt.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ncr.res.mobilepos.constant.TransactionVariable;
import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.journalization.model.poslog.MemberInfo;
/**
 * 改定履歴
 * バージョン      改定日付        担当者名      改定内容
 * 1.01            2014.09.09      LIUKAI        "haveDocTax"を加える対応.
 * 1.02            2014.12.15      mlwang
 * 1.03            2015.01.14      FENGSHA       返品の取消を対応
 * 1.04            2015.01.21      FENGSHA       前受金の取消レシートを対応.
 * 1.05            2015.01.23      FENGSHA       前回前受金出力の新規を対応
 * 1.06            2015.02.25      FENGSHA       旧税率新規を対応
 * Receipt Model Object.
 *
 * <P>
 * A Receipt in POSLog .
 */
public class ReceiptMode {
	
	public static final String RECEIPTTYPE_NORMAL = "Normal";
	
	public static final String RECEIPTTYPE_VOID = "Voided";
	
	public static final String RECEIPTTYPE_RETURN = "Returned";
	
	public static final String RECEIPTTYPE_VOID_RETURN = "VoidedReturned";
	
	public static final String RECEIPTTYPE_CANCEL = "Cancel";
	
    /**
     * receipt type: Normal, Cancled, Voided, Returned
     */
    private String receiptType;
    
    private String voidedType = "";
    /**
     * ReturnedLink，Returned
     */
    private String returnedType;
    /**
     * train mode flag
     */
    private String trainModeFlag;
    /**
     * receipt print direction
     */
    private String direction;
    /**
     * store id
     */
    private String storeID;
    /**
     *
     */
    private int storeType;
    /**
     * store name
     */
    private String storeName;
    /**
     * storeAddress
     */
    private String storeAddress;
    /**
     * telephone number
     */
    private String telNo;

    /**
     * ads
     */
    private String ads;

    /**
     * operator id
     */
    private String operatorID;
    /**
     * sales clerk name
     */
    private String clerkName;

    /**
     * begin date time
     */
    private String beginDateTime;

    /**
     * business day date
     */
    private String businessDayDate;

    /**
     * workstation id
     */
    private String workStationID;


    /**
     * poslog sequence number
     */
    private String sequenceNo;

    /**
     * cash payment amount
     */
    private double cashPament;

    /**
     *
     */
    private double miscPament;

    /**
     *
     */
    private double subtotal;
    
    /**
     * 値引前合計
     */
    private double subtotalWithoutDiscount;
    /**
     *
     */
    private double grandAmount;

    /**
     *
     */
    private double taxAmount;

    /**
     *
     */
    private String taxPercent;

    /**
     *
     */
    private double tenderChange;

    /**
     *
     */
    private String tenderType;

    /**
     *
     */
    private String totalReasonCode;

    /**
     *
     */
    private int totalPercent;

    /**
     *
     */
    private double totalDiscount;
    
    /***/
    private int mmQuantity;
    /***/
    private double vouchersAmount;
    /***/
    private String originalOperatorID;
    /***/
    private String originalClerkName;
    /***/
    private String originalWorkStationID;
    /***/
    private String originalSequenceNo;
    /***/
    private String originalRetailStoreID;
    /***/
    private String originalBusinessDayDate;
    /***/
    private String originalBeginDateTime;
    /***/
    private String publishedWorkStationID;
    /***/
    private String publishedSequenceNo;
    private String publishedBeginDateTime;

    /***/
    private String voiderRetailStoreID;
    /***/
    private String voiderWorkstationID;
    /***/
    private String voiderSequenceNumber;
    /***/
    private String voiderBusinessDayDate;
    /***/
    private String voiderBeginDateTime;
    /***/
    private String voiderOperatorID;
    /***/
    private String voiderWorkstationSubID;
    /**
     * start add by mlwang 2014/09/03
     * suica information
     */
    private String suicaSerialNumber;
    /****/
    private double suicaFaceValueAmount;
    /****/
    private double suicaUnspentAmount;
    /****/
    private String suicaReferenceNumber;
    /**
     * Suica payment amount.
     * In POSLog, Tender(TenderType='Voucher')->Amount.
     */
    private double suicaAmount;
    //end add  2014/09/03
    /**
     * sales void clerk name
     */
    //start add by mlwang 2014/09/02
    private String voiderClerkName;
    //end add 2014/09/02
    private String voidClerkName;
    /***/
    private String custGraphicId;
    /***/
    private String custGraphicName;
    /***/
    private String nDivided;
    /***/
    private String origReferenceNo;
    /***/
    private String paymentMethodCode;
    /**
     * item list
     */
    private List<ItemMode> itemList = new ArrayList<ItemMode>();
    /**
     * Member information
     */
    private MemberInfo memberinfo;
    /**
     * language (jp or en)
     */
    private String language;
    /**
     * レシート発行済みフラグ
     */
    private String retryflag;
    /**
     * To print doc tax stamp.
     */
    private boolean taxStampIncluded = false;
    /* 1.01 2014.09.09 "DocTax"を加える対応 ADD START */
    /**
     * To print doc tax stamp.
     */
    private String haveDocTax;

    /** */
    private String customerId;

    /** */
    private String customerPoints;

    /** */
    private int giftCardCount;

    private double giftCardsAmount;
    /** */
    //private boolean advanceFlag;
    private String isAdvanceFlag;
    private String isRainCheckFlag;
    /** InventoryReservationID */
    private String inventoryReservationID;

    /** 免税金額 */
    private double exemptTaxAmount;

    /** 返品の取消 Flag*/
    private String voidreturnFlag;

    /**前受金の取消Type */
    private String advanceVoidType;
    private String holdVoidType;
    private String customrOrderVoidType;
    /**前回前受金 */
    private double previousAmount;

    /**旧税率 */
    private String oldTaxRate;
    
    /**社員販売フラグ */
    private String barEmployeeFlag;
    // =============== クレジット情報 =============== START

    /** カード種類 */
    private String creditType;

    /** カード会社 */
    private String cardCompany;

    /** カード番号 */
    private String creditNo;

    /** 伝票番号 */
    private String slipNo;

    /** 元クレジット**/
    private String orislipNo;

    /** payment method name */
    private String paymentMethod;

    /** カード払い金額 */
    private double creditPament;

    /** 承認番号 */
    private String approvalNo;

    /** クレジットカード処理通番 */
    private String issueSequence;

    /** 端末識別番号 */
    private String creditTerminalID;

    /** 決済管理番号 */
    private String referenceNo;

    /** カード署名フラグ */
    private String signatureFlag;

    /** ICカードAID */
    private String icAID;

    /** ICカードATC */
    private String icATC;

    /** 銀聯番号 */
    private String unionPayNo;

    /** カード送信日時*/
    private String creditIssueDateTime;

    // =============== クレジット情報 =============== END

    // =============== 出金と入金と両替 =============== START

    /** PickUp Loan Exchange*/
    private String tenderControlType;

    /** 入金 */
    private double tenderControlLoan;

    /** 出金 */
    private double tenderControlPickup;

    /** 両替 */
    private double tenderControlExchange;

    private String receiptTitle = "normal";

    private double voucherWithoutChange;
    
    // =============== 出金と入金と両替 =============== END
    
    private String tenderCtrlTypePayInOut = "false";
    
    private String tenderControlPayOut;
    
    private String tenderControlPayIn;
    
    private String tenderCtrlPayInPlanDrawerAmt;
    
    private String tenderCtrlBeginningAmount;
    
    private double bankTransferAmount;
    
    private String tenderCtrlBalanceType;

    private String tenderBalCashNetTotalAmt;
    private String tenderBalCashGrossTotalAmt;
    private String tenderBalCashDiffAmt;
    private String tenderBalCashChangeReserveAmt;
    private String tenderBalCashCMCDTotalAmt;
    
    private String tenderBalNetTotal;
    private String tenderBalGrossTotal;
    private String tenderBalDiff;
    private String tenderBalChangeReserve;
    
    private String tenderCtrlBalanceTotal;
    
    private String tenderCtrlSODTopMsg1;
    private String tenderCtrlSODTopMsg2;
    private String tenderCtrlSODTopMsg3;
    private String tenderCtrlSODTopMsg4;
    private String tenderCtrlSODTopMsg5;
    private String tenderCtrlSODBottomMsg1;
    private String tenderCtrlSODBottomMsg2;
    private String tenderCtrlSODBottomMsg3;
    private String tenderCtrlSODBottomMsg4;
    private String tenderCtrlSODBottomMsg5;
    
    private String weatherNameId;
    private String eODNumCustomers;
    private String eOdNumGuests;
    
    // EOD
    private String eodCnt;
    private String authorizationNumCnt;
    private String netSalesTotalCnt;
    private String netSalesTotalAmt;
    private String salesGenTotCnt;
    private String salesGenTotAmt;
    private String salesGenTotCashCnt;
    private String salesGenTotCashAmt;
    private String salesGenTotCoDCnt;
    private String salesGenTotCoDAmt;
    private String salesGenTotCreditCnt;
    private String salesGenTotCreditAmt;
    private String salesGenTotDepositsReserveCnt;
    private String salesGenTotDepositsReserveAmt;
    private String salesRetGenTotalCnt;
    private String salesRetGenTotalAmt;
    private String salesRetGenCashRetCnt;
    private String salesRetGenCashRetAmt;
    private String consumptionTaxAmt;
    private String consumptionTaxDepositsRecAmt;
    private String consumptionTaxReturnedAmt;
    private String creditPaymentCnt;
    private String creditPaymentAmt;
    private String creditPaymentSalesCnt;
    private String creditPaymentSalesAmt;
    private String xebioCardCnt;
    private String xebioCardAmt;
    private String contractCreditPaymentCnt;
    private String contractCreditPaymentAmt;
    private String contractCreditPaymentSalesCnt;
    private String contractCreditPaymentSalesAmt;
    private String contractCreditPaymentShoppingLoanCnt;
    private String contractCreditPaymentShoppingLoanAmt;
    private String contractCreditPaymentEdyCnt;
    private String contractCreditPaymentEdyAmt;
    private String contractCreditPaymentSuicaCnt;
    private String contractCreditPaymentSuicaAmt;
    private String contractCreditPaymentWaonCnt;
    private String contractCreditPaymentWaonAmt;
    private String contractCreditPaymentCupCnt;
    private String contractCreditPaymentCupAmt;
    private String contractCreditPaymentTenant1Cnt;
    private String contractCreditPaymentTenant1Amt;
    private String giftCertPaymentCnt;
    private String giftCertPaymentAmt;
    private String giftCertPaymentSalesCnt;
    private String giftCertPaymentSalesAmt;
    private String giftCertPaymentXebioGrpCnt;
    private String giftCertPaymentXebioGrpAmt;
    private String otherPaymentCnt;
    private String otherPaymentAmt;
    private String otherOtherGiftCertCnt;
    private String otherOtherGiftCertAmt;
    private String otherOtherGiftCertGiftCertCnt;
    private String otherOtherGiftCertGiftCertAmt;
    private String otherOtherGiftCertOtherCnt;
    private String otherOtherGiftCertOtherAmt;
    private String otherTransferPaymentCnt;
    private String otherTransferPaymentAmt;
    private String otherCoDCnt;
    private String otherCoDAmt;
    private String purchaseTotalCnt;
    private String purchaseTotalAmt;
    private String purchaseTotStocksCnt;
    private String purchaseTotStocksAmt;
    private String purchaseTotConsumptionTaxAmt;
    private String depositsReceivedTotalCnt;
    private String depositsReceivedTotalAmt;
    private String depositsReceivedTotalResDownPaymentCnt;
    private String depositsReceivedTotalResDownPaymentAmt;
    private String depositsCashRetTotalCnt;
    private String depositsCashRetTotalAmt;
    private String depositsCashRetTotResDownPaymentRetCnt;
    private String depositsCashRetTotResDownPaymentRetAmt;
    private String otherDepositsTotalCnt;
    private String otherDepositsTotalAmt;
    private String otherDepositsTotXebioGrpTicketSalesCnt;
    private String otherDepositsTotXebioGrpTicketSalesAmt;
    private String otherDepositsTotTaxFreeMiscIncomeCnt;
    private String otherDepositsTotTaxFreeMiscIncomeAmt;
    private String otherDepositsTotMiscIncomeTicketBalCnt;
    private String otherDepositsTotMiscIncomeTicketBalAmt;
    private String otherDepositsTotPostageCnt;
    private String otherDepositsTotPostageAmt;
    private String otherDepositsTotCoDCnt;
    private String otherDepositsTotCoDAmt;
    private String otherDepositsTotBoxChangeCnt;
    private String otherDepositsTotBoxChangeAmt;
    private String otherWithdrawalsTotCnt;
    private String otherWithdrawalsTotAmt;
    private String otherWithdrawalsTotTenantDepositsCnt;
    private String otherWithdrawalsTotTenantDepositsAmt;
    private String otherWithdrawalsTotGCampainCnt;
    private String otherWithdrawalsTotGCampainAmt;
    private String bankDepositsTotCnt;
    private String bankDepositsTotAmt;
    private String bankDepositsTotMiddleCollectionCnt;
    private String bankDepositsTotMiddleCollectionAmt;
    private String bankDepositsTotDepAdjustPlusCnt;
    private String bankDepositsTotDepAdjustPlusAmt;
    private String bankDepositsTotDepAdjustMinusCnt;
    private String bankDepositsTotDepAdjustMinusAmt;
    private String bankDepositsTotCashinAmtCnt;
    private String bankDepositsTotCashinAmtAmt;
    private String cashOnHandAmt;
    private String changeReserveAmt;
    private String receiptCountCnt;
    private String exchangeCountCnt;
    private String cancellationCountCnt;
    private String dutyFreeTransactionCnt;
    private String dutyFreeTransactionAmt;
    private String salesRetCnt;
    private String salesRetAmt;
    private String salesVoidedCnt;
    private String salesVoidedAmt;
    private String salesVoidedRetCnt;
    private String salesVoidedRetAmt;
    private String depositsCancellationCnt;
    private String depositsCancellationAmt;
    private String exchangeRetCnt;
    private String exchangeRetAmt;
    private String cashInCnt;
    private String cashInAmt;
    private String cashInVoidCnt;
    private String cashInVoidAmt;
    private String cashOutCnt;
    private String cashOutAmt;
    private String cashOutVoidCnt;
    private String cashOutVoidAmt;
    private String groupTicketBalCnt;
    private String groupTicketBalPOSRegistrationCnt;
    private String groupTicketBalActualRemainingCnt;
    private String discountTicketIssueCnt;
    private String discountTicketIssueTestCnt;
    private String premiumTicketIssueCnt;
    private String premiumTicketIssueBSSHeadKaCnt;
    private String detailsDiscountTotalCnt;
    private String detailsDiscountTotalAmt;
    private String detailsDiscountTotChobiTag18Cnt;
    private String detailsDiscountTotChobiTag18Amt;
    private String detailsDiscountTotTradeInDiscountRate19Cnt;
    private String detailsDiscountTotTradeInDiscountRate19Amt;
    private String detailsDiscountTotTradeInDiscountAmt19Cnt;
    private String detailsDiscountTotTradeInDiscountAmt19Amt;
    private String detailsDiscountTotPurchaseReplaceSupport21Cnt;
    private String detailsDiscountTotPurchaseReplaceSupport21Amt;
    private String detailsDiscountTotLimited91Cnt;
    private String detailsDiscountTotLimited91Amt;
    private String detailsDiscountDetCnt;
    private String detailsDiscountDetAmt;
    private String subTotDiscountCnt;
    private String subTotDiscountAmt;
    private String subTotDiscountCoupon11Cnt;
    private String subTotDiscountCoupon11Amt;
    private String subTotDiscountPointTicketXebio13Cnt;
    private String subTotDiscountPointTicketXebio13Amt;
    private String subTotDiscountPointMortgage16Cnt;
    private String subTotDiscountPointMortgage16Amt;
    private String subTotDiscountGCoupon32Cnt;
    private String subTotDiscountGCoupon32Amt;
    private String subTotDiscountPremiumDiscnt61Cnt;
    private String subTotDiscountPremiumDiscnt61Amt;
    private String subTotDiscount71Cnt;
    private String subTotDiscount71Amt;
    private String subTotDiscountCompanyEmployeeSpecial72Cnt;
    private String subTotDiscountCompanyEmployeeSpecial72Amt;
    private String subTotDiscountSpecialDiscountComp73Cnt;
    private String subTotDiscountSpecialDiscountComp73Amt;
    private String subTotDiscountDMDiscnt74Cnt;
    private String subTotDiscountDMDiscnt74Amt;
    private String subTotDiscountCorpDiscnt75Cnt;
    private String subTotDiscountCorpDiscnt75Amt;
    private String subTotDiscountDMDiscount76Cnt;
    private String subTotDiscountDMDiscount76Amt;
    private String subTotDiscountDMDiscount77Cnt;
    private String subTotDiscountDMDiscount77Amt;
    private String subTotDiscountTicket78Cnt;
    private String subTotDiscountTicket78Amt;
    private String subTotDiscountDMDiscount79Cnt;
    private String subTotDiscountDMDiscount79Amt;
    private String subTotDiscountLimitedSales80Cnt;
    private String subTotDiscountLimitedSales80Amt;
    private String subTotDiscountStockholderPreferential81Cnt;
    private String subTotDiscountStockholderPreferential81Amt;
    private String subTotDiscountXebio82Cnt;
    private String subTotDiscountXebio82Amt;
    private String subTotDiscount83Cnt;
    private String subTotDiscount83Amt;
    private String subTotDiscountStudent84Cnt;
    private String subTotDiscountStudent84Amt;
    private String subTotDiscountSpecial85Cnt;
    private String subTotDiscountSpecial85Amt;
    private String subTotDiscount86Cnt;
    private String subTotDiscount86Amt;
    private String subTotDiscount87Cnt;
    private String subTotDiscount87Amt;
    private String subTotDiscount88Cnt;
    private String subTotDiscount88Amt;
    private String subTotDiscount89Cnt;
    private String subTotDiscount89Amt;
    private String subTotDiscountTicket92Cnt;
    private String subTotDiscountTicket92Amt;
    private String subTotDiscountRateCnt;
    private String subTotDiscountRateAmt;
    
    private String transactionType;
    private String companyId;
    private String transactionId;
    private int salesAmount;
    private int amountForPts;
    private String basicPts;
    private String ptsGeneratedTotal="";
    private String ptsThisTime;
    
    private String ptCardDiv;
    private String ptCardNo;
    private String prePtTotal;
    private String cumulativePtTotal;
    
    private String ptTicketDate;
    private String ptTicketHdr1;
    private String ptTicketHdr2;
    private String ptTicketMsg1;
    private String ptTicketValue;
    private String ptTicketFtr1;
    private String ptTicketFtr2;
    private String ptTicketIssueNumber;
    private String ptTicketBarcodeIdentifier;
    private String ptTicketExpDate;
    private String ptTicketValueBarcode;
    private String ptTicketFixedValue;
    
    // Promotion info
    private String barPromotionID;
    private String barPromotionName;
    private String operatorName;
    
    /**
     * @return advanceAmount 前受金登録金額/前受金一括取消の前受金金額
     */
    public double getAdvanceAmount() {
        double advanceAmount = 0.0;
        if(this.isAdvanceFlag == "true" || TransactionVariable
                                .ADVANCEVOIDTYPE.equals(this.getAdvanceVoidType())){
            advanceAmount = Double.parseDouble(this.getCashCreditMiscPament()) -
                    this.getTenderChange() - this.getVoucherWithoutChange();
        }
        return advanceAmount;
    }
    /**
     * @return nonoAdvanceAmount 前受金登録未納金
     */
    public double getNonoAdvanceAmount() {
        double nonoAdvanceAmount = 0.0;
        if(this.isAdvanceFlag == "true"){
            nonoAdvanceAmount = this.getGrandAmount() - this.getAdvanceAmount();
        }
        if(nonoAdvanceAmount < 0){
            nonoAdvanceAmount = 0.0 ;
        }
        return nonoAdvanceAmount;
    }
    /**
     * Checks to print doc tax stamp.
     * @return
     */
    public String getHaveDocTax() {
        return haveDocTax;
    }
    /**
     * Sets to print doc tax stamp.
     * @param haveDocTax
     */
    public void setHaveDocTax(String haveDocTax) {
        this.haveDocTax = haveDocTax;
    }
    /**
     * Checks to print doc tax stamp.
     * @return
     */
    public boolean isTaxStampIncluded() {
        return taxStampIncluded;
    }
    /**
     * Sets to print doc tax stamp.
     * @param haveDocTax
     */
    public void setTaxStampIncluded(boolean haveDocTax) {
        this.taxStampIncluded = haveDocTax;
    }
    /**
     * @return paymentMethodCode
     */
    public String getPaymentMethodCode() {
        return paymentMethodCode;
    }

    /**
     * @param paymentMethodCodeToSet
     */
    public void setPaymentMethodCode(String paymentMethodCodeToSet) {

        this.paymentMethodCode = paymentMethodCodeToSet;
    }
    /**
     * @return nDivided
     */
    public String getNDivided() {

        return nDivided;
    }

    /**
     * @param nDivided
     */
    public void setNDivided(String nDivided) {

        this.nDivided = nDivided;
    }
    /**
     * @return origReferenceNo
     */
    public String getOriginalReferenceNo() {

        return origReferenceNo;
    }

    /**
     * @param referenceNoToSet OriginalReferenceNo
     */
    public void setOriginalReferenceNo(String referenceNoToSet) {

        this.origReferenceNo = referenceNoToSet;
    }
    /**
     * @return storeID
     */
    public String getStoreID() {

        return storeID;
    }

    /**
     * @param storeID storeID
     */
    public void setStoreID(String storeID) {

        this.storeID = storeID;
    }

    /**
     * @return storeName
     */
    public String getStoreName() {
        return storeName;
    }

    /**
     * @param storeName storeName
     */
    public void setStoreName(String storeName) {

        this.storeName = storeName;
    }

    /**
     * @return telNo
     */
    public String getTelNo() {

        return telNo;
    }

    /**
     * @param telNo telNo
     */
    public void setTelNo(String telNo) {

        this.telNo = telNo;
    }

    /**
     * @return ads
     */
    public String getAds() {

        return ads;
    }

    /**
     * @param ads ads
     */
    public void setAds(String ads) {

        this.ads = ads;
    }

    /**
     * @return operatorID
     */
    public String getOperatorID() {

        return operatorID;
    }

    /**
     * @param operatorID operatorID
     */
    public void setOperatorID(String operatorID) {

        this.operatorID = operatorID;
    }

    /**
     * @return clerkName
     */
    public String getClerkName() {

        return clerkName;
    }

    /**
     * @param clerkName clerkName
     */
    public void setClerkName(String clerkName) {

        this.clerkName = clerkName;
    }

    /**
     * @return beginDateTime
     */
    public String getBeginDateTime() {

        return beginDateTime;
    }

    /**
     * @param beginDateTime beginDateTime
     */
    public void setBeginDateTime(String beginDateTime) {

        this.beginDateTime = beginDateTime;
    }

    /**
     * @return businessDayDate
     */
    public String getBusinessDayDate() {

        return businessDayDate;
    }

    /**
     * @param businessDayDate businessDayDate
     */
    public void setBusinessDayDate(String businessDayDate) {

        this.businessDayDate = businessDayDate;
    }

    /**
     * @return workStationID
     */
    public String getWorkStationID() {

        return workStationID;
    }

    /**
     * @param workStationID workStationID
     */
    public void setWorkStationID(String workStationID) {

        this.workStationID = workStationID;
    }

    /**
     * @return sequenceNo
     */
    public String getSequenceNo() {

        return sequenceNo;
    }

    /**
     * @param sequenceNo sequenceNo
     */
    public void setSequenceNo(String sequenceNo) {

        this.sequenceNo = sequenceNo;
    }

    /**
     * @return cashPament
     */
    public double getCashPament() {
        double previousLayaway = 0.0;
        for(int i = 0 ; i < this.getItemList().size() ; i++){
            previousLayaway += this.getItemList().get(i).getPaymentChange();
        }
        return cashPament - previousLayaway;
    }

    /**
     * @param cashPament cashPament
     */
    public void setCashPament(double cashPament) {

        this.cashPament = cashPament;
    }

    /**
     * @return cashPayment
     */
    public double getCashPayment() {

        return cashPament;
    }

    /**
     * @param cashPayment cashPayment
     */
    public void setCashPayment(double cashPayment) {

        this.cashPament = cashPayment;
    }

    /**
     * @return miscPament
     */
    public double getMiscPament() {

        return miscPament;
    }

    /**
     * @param miscPament miscPament
     */
    public void setMiscPament(double miscPament) {

        this.miscPament = miscPament;
    }

    /**
     * @return miscPayment
     */
    public double getMiscPayment() {

        return miscPament;
    }

    /**
     * @param miscPayment miscPayment
     */
    public void setMiscPayment(double miscPayment) {

        this.miscPament = miscPayment;
    }

    /**
     * @return subtotal
     */
    public double getSubtotal() {

        return subtotal;
    }

    /**
     * @param subtotal subtotal
     */
    public void setSubtotal(double subtotal) {

        this.subtotal = subtotal;
    }

    /**
     * @return grandAmount
     */
    public double getGrandAmount() {

        return grandAmount;
    }

    /**
     * @param grandAmount grandAmount
     */
    public void setGrandAmount(double grandAmount) {

        this.grandAmount = grandAmount;
    }

    /**
     * @return taxAmount
     */
    public double getTaxAmount() {

        return taxAmount;
    }

    /**
     * @param taxAmount taxAmount
     */
    public void setTaxAmount(double taxAmount) {

        this.taxAmount = taxAmount;
    }

    /**
     * @return taxPercent
     */
    public String getTaxPercent() {

        return taxPercent;
    }

    /**
     * @param taxPercent taxPercent
     */
    public void setTaxPercent(String taxPercent) {

        this.taxPercent = taxPercent;
    }

    /**
     * @return tenderChange
     */
    public double getTenderChange() {

        return tenderChange;
    }

    /**
     * @param tenderChange tenderChange
     */
    public void setTenderChange(double tenderChange) {

        this.tenderChange = tenderChange;
    }

    /**
     * @return cardCompany
     */
    public String getCardCompany() {

        return cardCompany;
    }

    /**
     * @param cardCompany cardCompany
     */
    public void setCardCompany(String cardCompany) {

        this.cardCompany = cardCompany;
    }

    /**
     * @return creditNo
     */
    public String getCreditNo() {

        return creditNo;
    }
    /**
     *
     */
    public String getCreditNoEncrypt(){
        StringBuilder cNo = null;
        if(!StringUtility.isNullOrEmpty(this.getCreditNo()) &&
                this.getCreditNo().length() > 4){
            int length = this.getCreditNo().length();
            cNo = new StringBuilder(this.getCreditNo().substring(length - 4, length));
            for(int i = 0 ; i < length - 4 ; i++){
                cNo.insert(0, "X");
            }
        }else {
            cNo = new StringBuilder("XXXXXXXXXXXXXXXX");
        }
        return cNo.toString();
    }
    /**
     * @param creditNo creditNo
     */
    public void setCreditNo(String creditNo) {

        this.creditNo = creditNo;
    }

    /**
     * @return slipNo
     */
    public String getSlipNo() {

        return slipNo;
    }

    /**
     * @param slipNo slipNo
     */
    public void setSlipNo(String slipNo) {

        this.slipNo = slipNo;
    }

    /**
     * @return paymentMethod
     */
    public String getPaymentMethod() {

        return paymentMethod;
    }

    /**
     * @param paymentMethod paymentMethod
     */
    public void setPaymentMethod(String paymentMethod) {

        this.paymentMethod = paymentMethod;
    }

    /**
     * @return creditPament
     */
    public double getCreditPament() {

        return creditPament;
    }

    /**
     * @param creditPament creditPament
     */
    public void setCreditPament(double creditPament) {

        this.creditPament = creditPament;
    }

    /**
     * @return creditPayment
     */
    public double getCreditPayment() {

        return creditPament;
    }

    /**
     * @param creditPayment creditPayment
     */
    public void setCreditPayment(double creditPayment) {

        this.creditPament = creditPayment;
    }

    /**
     * @return approvalNo
     */
    public String getApprovalNo() {

        return approvalNo;
    }

    /**
     * @param approvalNo approvalNo
     */
    public void setApprovalNo(String approvalNo) {

        this.approvalNo = approvalNo;
    }

    /**
     * @return issueSequence
     */
    public String getIssueSequence() {

        return issueSequence;
    }

    /**
     * @param issueSequence issueSequence
     */
    public void setIssueSequence(String issueSequence) {

        this.issueSequence = issueSequence;
    }

    /**
     * @return creditTerminalID
     */
    public String getCreditTerminalID() {

        return creditTerminalID;
    }

    /**
     * creditTerminalID を暗号化
     */
    public String getCreditTerminalIDXX() {
        StringBuilder builder = new StringBuilder("");
        if(!StringUtility.isNullOrEmpty(this.creditTerminalID)){
            int length = this.creditTerminalID.length();
            for(int i = 0 ; i < length ; i++){
                builder.insert(0, "X");
            }
        }else{
            builder.append("XXXXXXXXXXXXX");
        }
        return builder.toString();
    }
    /**
     * @param creditTerminalID creditTerminalID
     */
    public void setCreditTerminalID(String creditTerminalID) {

        this.creditTerminalID = creditTerminalID;
    }

    /**
     * @return referenceNo
     */
    public String getReferenceNo() {

        return referenceNo;
    }

    /**
     * @param referenceNo referenceNo
     */
    public void setReferenceNo(String referenceNo) {

        this.referenceNo = referenceNo;
    }

    /**
     * @return totalReasonCode
     */
    public String getTotalReasonCode() {

        return totalReasonCode;
    }

    /**
     * @param totalReasonCode totalReasonCode
     */
    public void setTotalReasonCode(String totalReasonCode) {

        this.totalReasonCode = totalReasonCode;
    }

    /**
     * @return totalPercent
     */
    public int getTotalPercent() {

        return totalPercent;
    }

    /**
     * @param totalPercent totalPercent
     */
    public void setTotalPercent(int totalPercent) {

        this.totalPercent = totalPercent;
    }

    /**
     * @return itemList
     */
    public List<ItemMode> getItemList() {

        return itemList;
    }

    /**
     * @param itemList itemList
     */
    public void setItemList(List<ItemMode> itemList) {

        this.itemList = itemList;
    }

    /**
     * @return receiptType
     */
    public String getReceiptType() {

        return receiptType;
    }

    /**
     * @param receiptType receiptType
     */
    public void setReceiptType(String receiptType) {

        this.receiptType = receiptType;
    }

    /**
     * @return totalDiscount
     */
    public double getTotalDiscount() {

        return totalDiscount;
    }

    /**
     * @param totalDiscount totalDiscount
     */
    public void setTotalDiscount(double totalDiscount) {

        this.totalDiscount = totalDiscount;
    }

    /**
     * @return mmQuantity
     */
    public int getMmQuantity() {

        return mmQuantity;
    }

    /**
     * @param mmQuantity mmQuantity
     */
    public void setMmQuantity(int mmQuantity) {

        this.mmQuantity = mmQuantity;
    }

    /**
     * @return tenderType
     */
    public String getTenderType() {

        return tenderType;
    }

    /**
     * @param tenderType tenderType
     */
    public void setTenderType(String tenderType) {

        this.tenderType = tenderType;
    }

    /**
     * @return signatureFlag
     */
    public String getSignatureFlag() {

        return signatureFlag;
    }

    /**
     * @param signatureFlag signatureFlag
     */
    public void setSignatureFlag(String signatureFlag) {

        this.signatureFlag = signatureFlag;
    }

    /**
     * @return voiderRetailStoreID
     */
    public String getVoiderRetailStoreID() {

        return voiderRetailStoreID;
    }

    /**
     * @param voiderRetailStoreID voiderRetailStoreID
     */
    public void setVoiderRetailStoreID(String voiderRetailStoreID) {

        this.voiderRetailStoreID = voiderRetailStoreID;
    }

    /**
     * @return voiderWorkstationID
     */
    public String getVoiderWorkstationID() {

        return voiderWorkstationID;
    }

    /**
     * @param voiderWorkstationID voiderWorkstationID
     */
    public void setVoiderWorkstationID(String voiderWorkstationID) {

        this.voiderWorkstationID = voiderWorkstationID;
    }

    /**
     * @return voiderSequenceNumber
     */
    public String getVoiderSequenceNumber() {

        return voiderSequenceNumber;
    }

    /**
     * @param voiderSequenceNumber voiderSequenceNumber
     */
    public void setVoiderSequenceNumber(String voiderSequenceNumber) {

        this.voiderSequenceNumber = voiderSequenceNumber;
    }

    /**
     * @return voiderBusinessDayDate
     */
    public String getVoiderBusinessDayDate() {

        return voiderBusinessDayDate;
    }

    /**
     * @param voiderBusinessDayDate voiderBusinessDayDate
     */
    public void setVoiderBusinessDayDate(String voiderBusinessDayDate) {

        this.voiderBusinessDayDate = voiderBusinessDayDate;
    }

    /**
     * @return voiderBeginDateTime
     */
    public String getVoiderBeginDateTime() {

        return voiderBeginDateTime;
    }

    /**
     * @param voiderBeginDateTime voiderBeginDateTime
     */
    public void setVoiderBeginDateTime(String voiderBeginDateTime) {

        this.voiderBeginDateTime = voiderBeginDateTime;
    }

    /**
     * @return voiderOperatorID
     */
    public String getVoiderOperatorID() {

        return voiderOperatorID;
    }

    /**
     * @param voiderOperatorID voiderOperatorID
     */
    public void setVoiderOperatorID(String voiderOperatorID) {

        this.voiderOperatorID = voiderOperatorID;
    }

    /**
     * @return voiderWorkstationSubID
     */
    public String getVoiderWorkstationSubID() {

        return voiderWorkstationSubID;
    }

    /**
     * @param voiderWorkstationSubID voiderWorkstationSubID
     */
    public void setVoiderWorkstationSubID(String voiderWorkstationSubID) {

        this.voiderWorkstationSubID = voiderWorkstationSubID;
    }

    /**
     * @return voidClerkName
     */
    public String getVoidClerkName() {

        return voidClerkName;
    }

    /**
     * @param voidClerkName voidClerkName
     */
    public void setVoidClerkName(String voidClerkName) {

        this.voidClerkName = voidClerkName;
    }

    /**
     * @return custGraphicId
     */
    public String getCustGraphicId() {

        return custGraphicId;
    }

    /**
     * @param custGraphicId custGraphicId
     */
    public void setCustGraphicId(String custGraphicId) {

        this.custGraphicId = custGraphicId;
    }

    /**
     * @return custGraphicName
     */
    public String getCustGraphicName() {

        return custGraphicName;
    }

    /**
     * @param custGraphicName custGraphicName
     */
    public void setCustGraphicName(String custGraphicName) {

        this.custGraphicName = custGraphicName;
    }

    /**
     * @return memberinfo
     */
    public MemberInfo getMemberInfo() {

        return memberinfo;
    }

    // /**
    //  * @param member memberinfo
    //  */
    // public void setMemberInfo(MemberInfo member) {

    //     this.memberinfo = member;
    // }

    // public final String toString() {
    //     StringBuilder sb = new StringBuilder();
    //     return sb.toString();
    // }

    // /**
    //  * Gets the AmountForPoints under LineItem.
    //  *
    //  * @return        The AmountForPoints under LineItem.
    //  */
    // public final String getMiAmountForPoints() {
    //     return this.memberinfo != null ? this.memberinfo.getAmountForPoints() : null;
    // }
    // /**
    //  * Gets the PointRate under LineItem.
    //  *
    //  * @return        The PointRate under LineItem.
    //  */
    // public final String getMiPointRate() {
    //     return this.memberinfo != null ? this.memberinfo.getPointRate() : null;
    // }
    // /**
    //  * Gets the CorrectionPoints under LineItem.
    //  *
    //  * @return        The CorrectionPoints under LineItem.
    //  */
    // public final String getMiCorrectionPoints() {
    //     return this.memberinfo != null ? this.memberinfo.getCorrectionPoints() : null;
    // }
    // /**
    //  * Gets the ExpirationDate under LineItem.
    //  *
    //  * @return        The ExpirationDate under LineItem.
    //  */
    // public final String getMiExpirationDate() {
    //     String expirationDate = null;
    //     if(this.memberinfo != null){
    //         expirationDate = this.memberinfo.getExpirationDate();
    //         if(!StringUtility.isNullOrEmpty(expirationDate) && expirationDate.length() == 8){
    //             expirationDate = expirationDate.substring(0,4) + "/" + expirationDate.substring(4,6) +
    //                      "/" + expirationDate.substring(6,8);
    //         }
    //     }
    //     return expirationDate;
    // }
    // /**
    //  * Gets the MemberShipId under LineItem.
    //  *
    //  * @return        The MemberShipId under LineItem.
    //  */
    // public final String getMiMemberShipId() {
    //     return this.memberinfo != null ? this.memberinfo.getInputtedMembershipId() : null;
    // }
    // /**
    //  * Gets the tMediaId under LineItem.
    //  *
    //  * @return        The tMediaId under LineItem.
    //  */
    // public final String getMiMediaId() {
    //     return this.memberinfo != null ? this.memberinfo.getMediaId() : null;
    // }
    // /**
    //  * Gets the PointsAcknowledgeId under LineItem.
    //  *
    //  * @return        The PointsAcknowledgeId under LineItem.
    //  */
    // public final String getMiPointsAcknowledgeId() {
    //     return this.memberinfo != null ? this.memberinfo.getPointsAcknowledgeId() : null;
    // }
    // /**
    //  * Gets the PiontsTransactionId under LineItem.
    //  *
    //  * @return        The PiontsTransactionIdId under LineItem.
    //  */
    // public final String getMiPiontsTransactionId() {
    //     return this.memberinfo != null ? this.memberinfo.getPiontsTransactionId() : null;
    // }
    // /**
    //  * Gets the PointsMethod under LineItem.
    //  *
    //  * @return        The PointsMethod under LineItem.
    //  */
    // public final String getMiPointsMethod() {
    //     return this.memberinfo != null ? this.memberinfo.getPointsMethod() : null;
    // }
    // /**
    //  * Gets the StatusCode under LineItem.
    //  *
    //  * @return        The StatusCode under LineItem.
    //  */
    // public final String getMiStatusCode() {
    //     return this.memberinfo != null ? this.memberinfo.getStatusCode() : null;
    // }
    // /**
    //  * Gets the ServerStatusCode under LineItem.
    //  *
    //  * @return        The ServerStatusCode under LineItem.
    //  */
    // public final String getMiServerStatusCode() {
    //     return this.memberinfo != null ? this.memberinfo.getServerStatusCode() : null;
    // }
    // /**
    //  * Gets the PointsPrior under LineItem.
    //  *
    //  * @return        The PointsPrior under LineItem.
    //  */
    // public final String getMiPointsPrior() {
    //     return this.memberinfo != null ? this.memberinfo.getPointsPrior() : null;
    // }
    // /**
    //  * Gets the TotalPoints under LineItem.
    //  *
    //  * @return        The TotalPoints under LineItem.
    //  */
    // public final String getMiTotalPoints() {
    //     return this.memberinfo != null ? this.memberinfo.getTotalPoints() : null;
    // }
    // /**
    //  * Gets the LostPoints under LineItem.
    //  *
    //  * @return        The LostPoints under LineItem.
    //  */
    // public final String getMiLostPoints() {
    //     return this.memberinfo != null ? this.memberinfo.getLostPoints() : null;
    // }

    // /**
    //  * Gets the PointsPrior add the CorrectionPoints.
    //  *
    //  * @return
    //  */
    // public final String getPointsPriorAddCorrectionPoints() {

    //     String strValue = null;

    //     if (this.memberinfo != null) {
    //         String strPointsPrior = this.memberinfo.getPointsPrior();
    //         String strCorrectionPoints = this.memberinfo.getCorrectionPoints();

    //         if (StringUtility
    //                 .isNullOrEmpty(strPointsPrior, strCorrectionPoints)) {
    //             strValue = "0";
    //         } else if (StringUtility.isNullOrEmpty(strPointsPrior)) {
    //             strValue = String
    //                     .valueOf(Long.valueOf(strCorrectionPoints) < 0 ? 0
    //                             : Long.valueOf(strCorrectionPoints));
    //         } else if (StringUtility.isNullOrEmpty(strCorrectionPoints)) {
    //             strValue = String.valueOf(Long.valueOf(strPointsPrior) < 0 ? 0
    //                     : Long.valueOf(strPointsPrior));
    //         } else {
    //             strValue = String.valueOf((Long.valueOf(strPointsPrior) + Long
    //                     .valueOf(strCorrectionPoints)) < 0 ? 0 : (Long
    //                     .valueOf(strPointsPrior) + Long
    //                     .valueOf(strCorrectionPoints)));
    //         }
    //     }
    //     return strValue;
    // }

    /**
     *
     * @return
     */
    public final String getAllQuantity(){
        int itemQuantity = 0;
        if(this.getItemList() != null){
            for (ItemMode item : this.getItemList()) {
                itemQuantity += item.getQuantity();
            }
        }
        return String.valueOf(itemQuantity);
    }

    /**
     *
     * @return
     */
    public String getLanguage() {
        return language;
    }
    /**
     *
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }
    /**
     *
     * @return
     */
    public String getRetryflag() {
        return retryflag;
    }
    /**
     *
     * @param retryflag
     */
    public void setRetryflag(String retryflag) {
        this.retryflag = retryflag;
    }
    /**
     * @return slipNo
     */
    public String getReSlipNo() {

        String strReslipNo = null;
        if (this.slipNo != null) {
            strReslipNo = String.format("%05d", Integer.valueOf(this.slipNo));
        }
        return strReslipNo;
    }

    /**
     * @return issueSeq
     */
    public String getIssueSeq() {

        return this.getIssueSequence() == null ? " " : this.getIssueSequence();
    }

    /**
     * @return cashCreditMiscPament
     */
    public String getCashCreditMiscPament() {
            return String.valueOf(this.getCashPament() + this.getCreditPament()
                    + this.getSuicaAmount() + this.getVouchersAmount()
                    + this.getPreviousLaywayAmount() + this.getPreviousAmount()
                    + this.getGiftCardsAmount() + this.getBankTransferAmount());
    }

    public double getVoucherWithoutChange() {
        return voucherWithoutChange;
    }
    public void setVoucherWithoutChange(double voucherWithoutChange) {
        this.voucherWithoutChange = voucherWithoutChange;
    }
    /**
     * @return previousLaywayAmount 精算金額
     */
    public double getPreviousLaywayAmount(){
        double total = 0.0;
        List<ItemMode> items = this.getItemList();
        if(!items.isEmpty()){
            for(int i = 0 ; i < items.size() ; i++){
                total += items.get(i).getPaymentAmount();
            }
        }
        return total;
    }
    public double getPreviousLaywayAmountAndReLayway(){
    	return this.getPreviousLaywayAmount() + this.getPreviousAmount();
    }
    /**
     * @return totalAdvanceAmount 前受金登録と前受金合計
     */
    //public double getTotalAdvanceAmount() {
    //    return this.getAdvanceAmount() + this.getPreviousLaywayAmount();
    //}
    /**
     * @return barNo
     */
    public String getBarNo() {
        return this.getStoreID() + "/" + this.getWorkStationID();
    }
    /**
     * @return OriginalBarNo
     */
    public String getOriginalBarNo(){
        return this.getOriginalRetailStoreID() + "/"
                + this.getOriginalWorkStationID();
    }
    /**
     * @return VoiderRetailWorkstationID
     */
    public String getVoiderRetailWorkstationID() {
        return this.getVoiderRetailStoreID() + "/" + this.getVoiderWorkstationID();
    }

    public String getStoreAddress() {
        return storeAddress;
    }

    public void setStoreAddress(String storeAddress) {
        this.storeAddress = storeAddress;
    }

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
    public double getAmountWithoutTenderChange(){
        return this.cashPament - this.tenderChange;
    }
    /**
     * @return vouchersAmount
     */
    public double getVouchersAmount() {
        return vouchersAmount;
    }
    /**
     * @param vouchersAmount セットする vouchersAmount
     */
    public void setVouchersAmount(double vouchersAmount) {
        this.vouchersAmount = vouchersAmount;
    }
    /**
     * @return originalOperatorID
     */
    public String getOriginalOperatorID() {
        return originalOperatorID;
    }
    /**
     * @param originalOperatorID セットする originalOperatorID
     */
    public void setOriginalOperatorID(String originalOperatorID) {
        this.originalOperatorID = originalOperatorID;
    }
    /**
     * @return originalClerkName
     */
    public String getOriginalClerkName() {
        return originalClerkName;
    }
    /**
     * @param originalClerkName セットする originalClerkName
     */
    public void setOriginalClerkName(String originalClerkName) {
        this.originalClerkName = originalClerkName;
    }
    /**
     * @return originalWorkStationID
     */
    public String getOriginalWorkStationID() {
        return originalWorkStationID;
    }
    /**
     * @param originalWorkStationID セットする originalWorkStationID
     */
    public void setOriginalWorkStationID(String originalWorkStationID) {
        this.originalWorkStationID = originalWorkStationID;
    }
    /**
     * @return originalSequenceNo
     */
    public String getOriginalSequenceNo() {
        return originalSequenceNo;
    }
    /**
     * @param originalSequenceNo セットする originalSequenceNo
     */
    public void setOriginalSequenceNo(String originalSequenceNo) {
        this.originalSequenceNo = originalSequenceNo;
    }
    /**
     * @return originalRetailStoreID
     */
    public String getOriginalRetailStoreID() {
        return originalRetailStoreID;
    }
    /**
     * @param originalRetailStoreID セットする originalRetailStoreID
     */
    public void setOriginalRetailStoreID(String originalRetailStoreID) {
        this.originalRetailStoreID = originalRetailStoreID;
    }
    /**
     * @return originalBusinessDayDate
     */
    public String getOriginalBusinessDayDate() {
        return originalBusinessDayDate;
    }
    /**
     * @param originalBusinessDayDate セットする originalBusinessDayDate
     */
    public void setOriginalBusinessDayDate(String originalBusinessDayDate) {
        this.originalBusinessDayDate = originalBusinessDayDate;
    }
    /**
     * @return originalBeginDateTime
     */
    public String getOriginalBeginDateTime() {
        return originalBeginDateTime;
    }
    /**
     * @param originalBeginDateTime セットする originalBeginDateTime
     */
    public void setOriginalBeginDateTime(String originalBeginDateTime) {
        this.originalBeginDateTime = originalBeginDateTime;
    }
    /**
     * @return publishedWorkStationID
     */
    public String getPublishedWorkStationID() {
        return publishedWorkStationID;
    }
    /**
     * @param publishedWorkStationID セットする publishedWorkStationID
     */
    public void setPublishedWorkStationID(String publishedWorkStationID) {
        this.publishedWorkStationID = publishedWorkStationID;
    }
    /**
     * @return publishedSequenceNo
     */
    public String getPublishedSequenceNo() {
        return publishedSequenceNo;
    }
    /**
     * @param publishedSequenceNo セットする publishedSequenceNo
     */
    public void setPublishedSequenceNo(String publishedSequenceNo) {
        this.publishedSequenceNo = publishedSequenceNo;
    }
    /**
     * @return publishedBeginDateTime
     */
    public String getPublishedBeginDateTime() {
        return publishedBeginDateTime;
    }
    /**
     * @param publishedBeginDateTime セットする publishedBeginDateTime
     */
    public void setPublishedBeginDateTime(String publishedBeginDateTime) {
        this.publishedBeginDateTime = publishedBeginDateTime;
    }
    /**
     * @return suicaSerialNumber
     */
    public String getSuicaSerialNumber() {
        return suicaSerialNumber;
    }
    /**
     * @param suicaSerialNumber セットする suicaSerialNumber
     */
    public void setSuicaSerialNumber(String suicaSerialNumber) {
        this.suicaSerialNumber = suicaSerialNumber;
    }
    /**
     * @return suicaFaceValueAmount
     */
    public double getSuicaFaceValueAmount() {
        return suicaFaceValueAmount;
    }
    /**
     * @param suicaFaceValueAmount セットする suicaFaceValueAmount
     */
    public void setSuicaFaceValueAmount(double suicaFaceValueAmount) {
        this.suicaFaceValueAmount = suicaFaceValueAmount;
    }
    /**
     * @return suicaUnspentAmount
     */
    public double getSuicaUnspentAmount() {
        return suicaUnspentAmount;
    }
    /**
     * @param suicaUnspentAmount セットする suicaUnspentAmount
     */
    public void setSuicaUnspentAmount(double suicaUnspentAmount) {
        this.suicaUnspentAmount = suicaUnspentAmount;
    }
    /**
     * @return suicaReferenceNumber
     */
    public String getSuicaReferenceNumber() {
        return suicaReferenceNumber;
    }
    /**
     * @param suicaReferenceNumber セットする suicaReferenceNumber
     */
    public void setSuicaReferenceNumber(String suicaReferenceNumber) {
        this.suicaReferenceNumber = suicaReferenceNumber;
    }
    /**
     * @return suicaAmount
     */
    public double getSuicaAmount() {
        return suicaAmount;
    }
    /**
     * @param suicaAmount セットする suicaAmount
     */
    public void setSuicaAmount(double suicaAmount) {
        this.suicaAmount = suicaAmount;
    }
    /**
     * @return voiderClerkName
     */
    public String getVoiderClerkName() {
        return voiderClerkName;
    }
    /**
     * @param voiderClerkName セットする voiderClerkName
     */
    public void setVoiderClerkName(String voiderClerkName) {
        this.voiderClerkName = voiderClerkName;
    }
    /**
     * @return nDivided
     */
    public String getnDivided() {
        return nDivided;
    }
    /**
     * @param nDivided セットする nDivided
     */
    public void setnDivided(String nDivided) {
        this.nDivided = nDivided;
    }
    /**
     * @return origReferenceNo
     */
    public String getOrigReferenceNo() {
        return origReferenceNo;
    }
    /**
     * @param origReferenceNo セットする origReferenceNo
     */
    public void setOrigReferenceNo(String origReferenceNo) {
        this.origReferenceNo = origReferenceNo;
    }
    /**
     * @return memberinfo
     */
    public MemberInfo getMemberinfo() {
        return memberinfo;
    }
    /**
     * @param memberinfo セットする memberinfo
     */
    public void setMemberinfo(MemberInfo memberinfo) {
        this.memberinfo = memberinfo;
    }
    /**
     * @return customerId
     */
    public String getCustomerId() {
        return customerId;
    }
    /**
     * @param customerId セットする customerId
     */
    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }
    /**
     * @return customerPoints
     */
    public String getCustomerPoints() {
        return customerPoints;
    }
    /**
     * @param customerPoints セットする customerPoints
     */
    public void setCustomerPoints(String customerPoints) {
        this.customerPoints = customerPoints;
    }
    /**
     * @return giftCardCount
     */
    public int getGiftCardCount() {
        return giftCardCount;
    }
    /**
     * @param giftCardCount セットする giftCardCount
     */
    public void setGiftCardCount(int giftCardCount) {
        this.giftCardCount = giftCardCount;
    }
    /**
     * @return inventoryReservationID
     */
    public String getInventoryReservationID() {
    	if(null == this.inventoryReservationID){
    		Iterator<ItemMode> iter = this.getItemList().iterator();
    		while(iter.hasNext()){
    			String code = iter.next().getInventoryReservationID();
    			if(!StringUtility.isNullOrEmpty(code)){
    				this.inventoryReservationID = code;
    				break;
    			}
    		}
    	}
        return inventoryReservationID;
    }
    /**
     * @param inventoryReservationID セットする inventoryReservationID
     */
    public void setInventoryReservationID(String inventoryReservationID) {
        this.inventoryReservationID = inventoryReservationID;
    }

    /**
     * @return exemptTaxAmount
     */
    public double getExemptTaxAmount() {
        return exemptTaxAmount;
    }
    /**
     * @param exemptTaxAmount セットする exemptTaxAmount
     */
    public void setExemptTaxAmount(double exemptTaxAmount) {
        this.exemptTaxAmount = exemptTaxAmount;
    }
    /**
     * @return creditType
     */
    public String getCreditType() {
        return creditType;
    }
    /**
     * @param creditType セットする creditType
     */
    public void setCreditType(String creditType) {
        this.creditType = creditType;
    }
    /**
     * @return orislipNo
     */
    public String getOrislipNo() {
        String strOriReslipNo = null;
        if (this.orislipNo != null) {
            strOriReslipNo = String.format("%05d", Integer.valueOf(this.orislipNo));
        }
        return strOriReslipNo;
    }
    /**
     * @param orislipNo セットする orislipNo
     */
    public void setOrislipNo(String orislipNo) {
        this.orislipNo = orislipNo;
    }
    /**
     * @return icAID
     */
    public String getIcAID() {
        return icAID;
    }
    /**
     * @param icAID セットする icAID
     */
    public void setIcAID(String icAID) {
        this.icAID = icAID;
    }
    /**
     * @return icATC
     */
    public String getIcATC() {
        return icATC;
    }
    /**
     * @param icATC セットする icATC
     */
    public void setIcATC(String icATC) {
        this.icATC = icATC;
    }
    /**
     * @return unionPayNo
     */
    public String getUnionPayNo() {
        return unionPayNo;
    }
    /**
     * @param unionPayNo セットする unionPayNo
     */
    public void setUnionPayNo(String unionPayNo) {
        this.unionPayNo = unionPayNo;
    }
    /**
     * @return creditIssueDateTime
     */
    public String getCreditIssueDateTime() {
        return creditIssueDateTime;
    }
    /**
     * @param creditIssueDateTime セットする creditIssueDateTime
     */
    public void setCreditIssueDateTime(String creditIssueDateTime) {
        this.creditIssueDateTime = creditIssueDateTime;
    }
    /**
     * @return storeType
     */
    public int getStoreType() {
        return storeType;
    }
    /**
     * @param storeType セットする storeType
     */
    public void setStoreType(int storeType) {
        this.storeType = storeType;
    }

    /**
     * @return tenderControlLoan
     */
    public double getTenderControlLoan() {
        return tenderControlLoan;
    }
    /**
     * @param tenderControlLoan セットする tenderControlLoan
     */
    public void setTenderControlLoan(double tenderControlLoan) {
        this.tenderControlLoan = tenderControlLoan;
    }
    /**
     * @return tenderControlPickup
     */
    public double getTenderControlPickup() {
        return tenderControlPickup;
    }
    /**
     * @param tenderControlPickup セットする tenderControlPickup
     */
    public void setTenderControlPickup(double tenderControlPickup) {
        this.tenderControlPickup = tenderControlPickup;
    }
    /**
     * @return tenderControlExchange
     */
    public double getTenderControlExchange() {
        return tenderControlExchange;
    }
    /**
     * @param tenderControlExchange セットする tenderControlExchange
     */
    public void setTenderControlExchange(double tenderControlExchange) {
        this.tenderControlExchange = tenderControlExchange;
    }
    /**
     * @return tenderControlType
     */
    public String getTenderControlType() {
        return tenderControlType;
    }
    /**
     * @param tenderControlType セットする tenderControlType
     */
    public void setTenderControlType(String tenderControlType) {
        this.tenderControlType = tenderControlType;
    }
    /**
     * @return returnedType
     */
    public String getReturnedType() {
        return returnedType;
    }
    /**
     * @param returnedType セットする returnedType
     */
    public void setReturnedType(String returnedType) {
        this.returnedType = returnedType;
    }
    /**
     * @return trainModeFlag
     */
    public String getTrainModeFlag() {
        return trainModeFlag;
    }
    /**
     * @param trainModeFlag セットする trainModeFlag
     */
    public void setTrainModeFlag(String trainModeFlag) {
        this.trainModeFlag = trainModeFlag;
    }
//    /**
//     * @return advanceFlag
//     */
//    public boolean isAdvanceFlag() {
//        return advanceFlag;
//    }
//    /**
//     * @param advanceFlag セットする advanceFlag
//     */
//    public void setAdvanceFlag(boolean advanceFlag) {
//        this.advanceFlag = advanceFlag;
//    }
    /**
     * @return isAdvanceFlag
     */
    public String getIsAdvanceFlag() {
        return isAdvanceFlag;
    }
    /**
     * @param isAdvanceFlag セットする isAdvanceFlag
     */
    public void setIsAdvanceFlag(String isAdvanceFlag) {
        this.isAdvanceFlag = isAdvanceFlag;
    }

  //1.03 2015.01.14 FENGSHA ADD START
    /**
    * @return voidreturnFlag
    */
    public String getVoidreturnFlag() {
    return voidreturnFlag;
    }
    /**
    * @param voidreturnFlag セットする voidreturnFlag
    */
    public void setVoidreturnFlag(String voidreturnFlag) {
    this.voidreturnFlag = voidreturnFlag;
    }
  //1.03 2015.01.14 FENGSHA ADD END
  //1.04 2015.01.21 FENGSHA ADD START
    /**
     * @return advanceVoidType
     */
    public String getAdvanceVoidType() {
        return advanceVoidType;
    }
    /**
     * @param advanceVoidType セットする advanceVoidType
     */
    public void setAdvanceVoidType(String advanceVoidType) {
        this.advanceVoidType = advanceVoidType;
    }
  //1.04 2015.01.21 FENGSHA ADD END
  //1.05 2015.01.23 FENGSHA ADD START
    /**
     * @return previousAmount
     */
    public double getPreviousAmount() {
        return previousAmount;
    }
    /**
     * @param previousAmount セットする previousAmount
     */
    public void setPreviousAmount(double previousAmount) {
        this.previousAmount = previousAmount;
    }

    public String getReceiptTitle() {
        return receiptTitle;
    }

    public void setReceiptTitle(String receiptTitle) {
        this.receiptTitle = receiptTitle;
    }

    public double getGiftCardsAmount() {
        return giftCardsAmount;
    }

    public void setGiftCardsAmount(double giftCardsAmount) {
        this.giftCardsAmount = giftCardsAmount;
    }

  //1.05 2015.01.23 FENGSHA ADD END

 //1.06 2015.02.25 FENGSHA ADD START
    /**
     * @return oldTaxRate
     */
    public String getOldTaxRate() {
        return oldTaxRate;
    }
    /**
     * @param oldTaxRate セットする oldTaxRate
     */
    public void setOldTaxRate(String oldTaxRate) {
        this.oldTaxRate = oldTaxRate;
    }
    //1.06 2015.02.25 FENGSHA ADD END
	public String getVoidedType() {
		return voidedType;
	}
	public void setVoidedType(String voidedType) {
		this.voidedType = voidedType;
	}
	
	/**
	 * @return the barEmployeeFlag
	 */
	public String getBarEmployeeFlag() {
		return barEmployeeFlag;
	}
	/**
	 * @param barEmployeeFlag the barEmployeeFlag to set
	 */
	public void setBarEmployeeFlag(String barEmployeeFlag) {
		this.barEmployeeFlag = barEmployeeFlag;
	}
	/**
	 * @return the rainCheckReasonCode
	 * hold 、Reservation(予約) 、CustomerOrder(客注)
	 */
	public String getRainCheckReasonCode() {
		String reasonCode = null;
		Iterator<ItemMode> iter = this.getItemList().iterator();
		while(iter.hasNext()){
			reasonCode = iter.next().getRainCheckReasonCode();
			break;
		}
		return reasonCode;
	}
	public String getBarPromotionID() {
		return barPromotionID;
	}
	public void setBarPromotionID(String barPromotionID) {
		this.barPromotionID = barPromotionID;
	}
	public String getBarPromotionName() {
		return barPromotionName;
	}
	public void setBarPromotionName(String barPromotionName) {
		this.barPromotionName = barPromotionName;
	}
    public String getIsRainCheckFlag() {
        return isRainCheckFlag;
    }
    public void setIsRainCheckFlag(String isRainCheckFlag) {
        this.isRainCheckFlag = isRainCheckFlag;
    }
    public double getBankTransferAmount() {
        return bankTransferAmount;
    }
    public void setBankTransferAmount(double bankTransferAmount) {
        this.bankTransferAmount = bankTransferAmount;
    }
    public String getHoldVoidType() {
        return holdVoidType;
    }
    public void setHoldVoidType(String holdVoidType) {
        this.holdVoidType = holdVoidType;
    }
    public String getCustomrOrderVoidType() {
        return customrOrderVoidType;
    }
    public void setCustomrOrderVoidType(String customrOrderVoidType) {
        this.customrOrderVoidType = customrOrderVoidType;
    }
    public double getSubtotalWithoutDiscount() {
        return subtotalWithoutDiscount;
    }
    public void setSubtotalWithoutDiscount(double subtotalWithoutDiscount) {
        this.subtotalWithoutDiscount = subtotalWithoutDiscount;
    }
    public void setTenderCtrlTypePayInOut(String value) {
    	this.tenderCtrlTypePayInOut = value;
    }
    public String getTenderCtrlTypePayInOut() {
    	return tenderCtrlTypePayInOut;
    }
    public void setTenderControlPayOut(String payoutAmount) {
    	this.tenderControlPayOut = payoutAmount;
    }
    public String getTenderControlPayOut() {
    	return tenderControlPayOut;
    }
    public void setTenderControlPayIn(String payinAmount) {
    	this.tenderControlPayIn = payinAmount;
    }
    public String getTenderControlPayIn() {
    	return tenderControlPayIn;
    }
    public void setTenderCtrlBeginningAmount(String beginAmount) {
    	this.tenderCtrlBeginningAmount = beginAmount;
    }
    public String getTenderCtrlBeginningAmount() {
    	return tenderCtrlBeginningAmount;
    }
	public void setTenderCtrlBalanceType(String balanceType) {
		this.tenderCtrlBalanceType = balanceType;
	}
	public String getTenderCtrlBalanceType() {
		return tenderCtrlBalanceType;
	}
	public void setOperatorName(String name) {
		this.operatorName = name;
	}
	public String getOperatorName() {
		return operatorName;
	}
    public void setTenderCtrlBalanceTotal(String amount) {
    	this.tenderCtrlBalanceTotal = amount;
    }
    public String getTenderCtrlBalanceTotal() {
    	return this.tenderCtrlBalanceTotal;
    }
    public void setTenderBalCashNetTotalAmt(String amt) {
    	this.tenderBalCashNetTotalAmt = amt;
    }
    public String getTenderBalCashNetTotalAmt() {
    	return this.tenderBalCashNetTotalAmt;
    }
    public void setTenderBalCashGrossTotalAmt(String amt) {
    	this.tenderBalCashGrossTotalAmt = amt;
    }
    public String getTenderBalCashGrossTotalAmt() {
    	return this.tenderBalCashGrossTotalAmt;
    }
    public void setTenderBalCashDiffAmt(String amt) {
    	this.tenderBalCashDiffAmt = amt;
    }
    public String getTenderBalCashDiffAmt() {
    	return this.tenderBalCashDiffAmt;
    }
    public void setTenderBalCashChangeReserveAmt(String amt) {
    	this.tenderBalCashChangeReserveAmt = amt;
    }
    public String getTenderBalCashChangeReserveAmt() {
    	return this.tenderBalCashChangeReserveAmt;
    }
    public void setTenderBalCashCMCDTotalAmt(String amt) {
    	this.tenderBalCashCMCDTotalAmt = amt;
    }
    public String getTenderBalCashCMCDTotalAmt() {
    	return this.tenderBalCashCMCDTotalAmt;
    }
    public void setWeatherNameId(String weathernameid) {
    	this.weatherNameId = weathernameid;
    }
    public String getWeatherNameId() {
    	return this.weatherNameId;
    }
    public String getEODNumCustomers() {
    	return this.eODNumCustomers;
    }
    public void setEODNumCustomers(String numCustomers) {
    	this.eODNumCustomers = numCustomers;
    }
    public String getEODNumGuests() {
    	return this.eOdNumGuests;
    }
    public void setEODNumGuests(String numGuests) {
    	this.eOdNumGuests = numGuests;
    }
    public String getTenderCtrlPayInPlanDrawerAmt() {
    	return this.tenderCtrlPayInPlanDrawerAmt;
    }
    public void setTenderCtrlPayInPlanDrawerAmt(String value) {
    	this.tenderCtrlPayInPlanDrawerAmt = value;
    }
    public String getTransactionType() {
    	return this.transactionType;
    }
    public void setTransactionType(String txType) {
    	this.transactionType = txType;
    }
    public String getCompanyId() {
    	return this.companyId;
    }
    public void setCompanyId(String compId) {
    	this.companyId = compId;
    }
    public String getTransactionId() {
    	return this.transactionId;
    }
    public void setTransactionId(String txId) {
    	this.transactionId = txId;
    }
    public int getSalesAmount() {
    	return this.salesAmount;
    }
    public void setSalesAmount(int amt) {
    	this.salesAmount = amt;
    }
    public int getAmountForPts() {
    	return this.amountForPts;
    }
    public void setAmountForPts(int amtforpts) {
    	this.amountForPts = amtforpts;
    }
    public String getBasicPts() {
    	return this.basicPts;
    }
    public void setBasicPts(String basicpts) {
    	this.basicPts = basicpts;
    }
    public String getPtsGeneratedTotal() {
    	return this.ptsGeneratedTotal;
    }
    public void setPtsGeneratedTotal(String ptsgeneratedtotal) {
    	this.ptsGeneratedTotal = ptsgeneratedtotal;
    }
    public String getPtCardDiv() {
    	return this.ptCardDiv;
    }
    public void setPtCardDiv(String carddiv) {
    	this.ptCardDiv = carddiv;
    }
    public String getPtCardNo() {
    	return this.ptCardNo;
    }
    public void setPtCardNo(String cardno) {
    	this.ptCardNo = cardno;
    }
    public String getPrePtTotal() {
    	return this.prePtTotal;
    }
    public void setPrePtTotal(String prepttotal) {
    	this.prePtTotal = prepttotal;
    }
    public String getCumulativePtTotal() {
    	return this.cumulativePtTotal;
    }
    public void setCumulativePtTotal(String cumulativepttotal) {
    	this.cumulativePtTotal = cumulativepttotal;
    }
    public String getPtTicketDate() {
    	return this.ptTicketDate;
    }
    public void setPtTicketDate(String dt) {
    	this.ptTicketDate = dt;
    }
    public String getPtTicketHdr1() {
    	return this.ptTicketHdr1;
    }
    public void setPtTicketHdr1(String hdr1) {
    	this.ptTicketHdr1 = hdr1;
    }
    public String getPtTicketHdr2() {
    	return this.ptTicketHdr2;
    }
    public void setPtTicketHdr2(String hdr2) {
    	this.ptTicketHdr2 = hdr2;
    }
    public String getPtTicketMsg1() {
    	return this.ptTicketMsg1;
    }
    public void setPtTicketMsg1(String msg) {
    	this.ptTicketMsg1 = msg;
    }
    public String getPtTicketValue() {
    	return this.ptTicketValue;
    }
    public void setPtTicketValue(String value) {
    	this.ptTicketValue = value;
    }
    public String getPtTicketFtr1() {
    	return this.ptTicketFtr1;
    }
    public void setPtTicketFtr1(String ftr) {
    	this.ptTicketFtr1 = ftr;
    }
    public String getPtTicketFtr2() {
    	return this.ptTicketFtr2;
    }
    public void setPtTicketFtr2(String ftr) {
    	this.ptTicketFtr2 = ftr;
    }
    public String getTenderBalNetTotal(){
    	return this.tenderBalNetTotal;
    }
    public void setTenderBalNetTotal(String nettotal) {
    	this.tenderBalNetTotal = nettotal;
    }
    public String getTenderBalGrossTotal() {
    	return this.tenderBalGrossTotal;
    }
    public void setTenderBalGrossTotal(String grstotal) {
    	this.tenderBalGrossTotal = grstotal;
    }
    public String getTenderBalDiff() {
    	return this.tenderBalDiff;
    }
    public void setTenderBalDiff(String diff) {
    	this.tenderBalDiff = diff;
    }
    public String getTenderBalChangeReserve() {
    	return this.tenderBalChangeReserve;
    }
    public void setTenderBalChangeReserve(String value) {
    	this.tenderBalChangeReserve = value;
    }
    public String getPtTicketIssueNumber() {
    	return this.ptTicketIssueNumber;
    }
    public void setPtTicketIssueNumber(String value) {
    	this.ptTicketIssueNumber = value;
    }
    public String getPtTicketBarcodeIdentifier() {
    	return this.ptTicketBarcodeIdentifier;
    }
    public void setPtTicketBarcodeIdentifier(String val) {
    	this.ptTicketBarcodeIdentifier = val;
    }
    public String getPtTicketValueBarcode() {
    	return this.ptTicketValueBarcode;
    }
    public void setPtTicketValueBarcode(String val) {
    	this.ptTicketValueBarcode = val;
    }
    public String getPtTicketExpDate() {
    	return this.ptTicketExpDate;
    }
    public void setPtTicketExpDate(String val) {
    	this.ptTicketExpDate = val;
    }
    public String getTenderCtrlSODTopMsg1() {
    	return this.tenderCtrlSODTopMsg1;
    }
    public void setTenderCtrlSODTopMsg1(String val) {
    	this.tenderCtrlSODTopMsg1 = val;
    }
    public String getTenderCtrlSODMsg2() {
    	return this.tenderCtrlSODTopMsg2;
    }
    public void setTenderCtrlSODTopMsg2(String val) {
    	this.tenderCtrlSODTopMsg2 = val;
    }
    public String getTenderCtrlSODTopMsg3() {
        return this.tenderCtrlSODTopMsg3;
    }
    public void setTenderCtrlSODTopMsg3(String val) {
        this.tenderCtrlSODTopMsg3 = val;
    }
    public String getTenderCtrlSODTopMsg4() {
        return this.tenderCtrlSODTopMsg4;
    }
    public void setTenderCtrlSODTopMsg4(String val) {
        this.tenderCtrlSODTopMsg4 = val;
    }
    public String getTenderCtrlSODTopMsg5() {
        return this.tenderCtrlSODTopMsg5;
    }
    public void setTenderCtrlSODTopMsg5(String val) {
        this.tenderCtrlSODTopMsg5 = val;
    }
    public String getTenderCtrlBottomMsg1() {
    	return this.tenderCtrlSODBottomMsg1;
    }
    public void setTenderCtrlSODBottomMsg1(String val) {
    	this.tenderCtrlSODBottomMsg1 = val;
    }
    public String getTenderCtrlSODBottomMsg2() {
    	return this.tenderCtrlSODBottomMsg2;
    }
    public void setTenderCtrlSODBottomMsg2(String val) {
    	this.tenderCtrlSODBottomMsg2 = val;
    }
    public String getTenderCtrlSODBottomMsg3() {
    	return this.tenderCtrlSODBottomMsg3;
    }
    public void setTenderCtrlSODBottomMsg3(String val) {
    	this.tenderCtrlSODBottomMsg3 = val;
    }
    public String getTenderCtrlSODBottomMsg4() {
    	return this.tenderCtrlSODBottomMsg4;
    }
    public void setTenderCtrlSODBottomMsg4(String val) {
    	this.tenderCtrlSODBottomMsg4 = val;
    }
    public String getTenderCtrlSODBottomMsg5() {
    	return this.tenderCtrlSODBottomMsg5;
    }
    public void setTenderCtrlSODBottomMsg5(String val) {
    	this.tenderCtrlSODBottomMsg5 = val;
    }
    public String getPtsThisTime() {
    	return this.ptsThisTime;
    }
    public void setPtsThisTime(String val) {
    	this.ptsThisTime = val;
    }
    public String getPtTicketFixedValue() {
    	return this.ptTicketFixedValue;
    }
    public void setPtTicketFixedValue(String val) {
    	this.ptTicketFixedValue = val;
    }
    
    public String getEodCnt() {
        return this.eodCnt;
    }
    public void setEodCnt(String val) {
        this.eodCnt=val;
    }
    public String getAuthorizationNumCnt() {
        return this.authorizationNumCnt;
    }
    public void setAuthorizationNumCnt(String val) {
        this.authorizationNumCnt=val;
    }
    public String getNetSalesTotalCnt() {
        return this.netSalesTotalCnt;
    }
    public void setNetSalesTotalCnt(String val) {
        this.netSalesTotalCnt=val;
    }
    public String getNetSalesTotalAmt() {
        return this.netSalesTotalAmt;
    }
    public void setNetSalesTotalAmt(String val) {
        this.netSalesTotalAmt=val;
    }
    public String getSalesGenTotCnt() {
        return this.salesGenTotCnt;
    }
    public void setSalesGenTotCnt(String val) {
        this.salesGenTotCnt=val;
    }
    public String getSalesGenTotAmt() {
        return this.salesGenTotAmt;
    }
    public void setSalesGenTotAmt(String val) {
        this.salesGenTotAmt=val;
    }
    public String getSalesGenTotCashCnt() {
        return this.salesGenTotCashCnt;
    }
    public void setSalesGenTotCashCnt(String val) {
        this.salesGenTotCashCnt=val;
    }
    public String getSalesGenTotCashAmt() {
        return this.salesGenTotCashAmt;
    }
    public void setSalesGenTotCashAmt(String val) {
        this.salesGenTotCashAmt=val;
    }
    public String getSalesGenTotCoDCnt() {
        return this.salesGenTotCoDCnt;
    }
    public void setSalesGenTotCoDCnt(String val) {
        this.salesGenTotCoDCnt=val;
    }
    public String getSalesGenTotCoDAmt() {
        return this.salesGenTotCoDAmt;
    }
    public void setSalesGenTotCoDAmt(String val) {
        this.salesGenTotCoDAmt=val;
    }
    public String getSalesGenTotCreditCnt() {
        return this.salesGenTotCreditCnt;
    }
    public void setSalesGenTotCreditCnt(String val) {
        this.salesGenTotCreditCnt=val;
    }
    public String getSalesGenTotCreditAmt() {
        return this.salesGenTotCreditAmt;
    }
    public void setSalesGenTotCreditAmt(String val) {
        this.salesGenTotCreditAmt=val;
    }
    public String getSalesGenTotDepositsReserveCnt() {
        return this.salesGenTotDepositsReserveCnt;
    }
    public void setSalesGenTotDepositsReserveCnt(String val) {
        this.salesGenTotDepositsReserveCnt=val;
    }
    public String getSalesGenTotDepositsReserveAmt() {
        return this.salesGenTotDepositsReserveAmt;
    }
    public void setSalesGenTotDepositsReserveAmt(String val) {
        this.salesGenTotDepositsReserveAmt=val;
    }
    public String getSalesRetGenTotalCnt() {
        return this.salesRetGenTotalCnt;
    }
    public void setSalesRetGenTotalCnt(String val) {
        this.salesRetGenTotalCnt=val;
    }
    public String getSalesRetGenTotalAmt() {
        return this.salesRetGenTotalAmt;
    }
    public void setSalesRetGenTotalAmt(String val) {
        this.salesRetGenTotalAmt=val;
    }
    public String getSalesRetGenCashRetCnt() {
        return this.salesRetGenCashRetCnt;
    }
    public void setSalesRetGenCashRetCnt(String val) {
        this.salesRetGenCashRetCnt=val;
    }
    public String getSalesRetGenCashRetAmt() {
        return this.salesRetGenCashRetAmt;
    }
    public void setSalesRetGenCashRetAmt(String val) {
        this.salesRetGenCashRetAmt=val;
    }
    public String getConsumptionTaxAmt() {
        return this.consumptionTaxAmt;
    }
    public void setConsumptionTaxAmt(String val) {
        this.consumptionTaxAmt=val;
    }
    public String getConsumptionTaxDepositsRecAmt() {
        return this.consumptionTaxDepositsRecAmt;
    }
    public void setConsumptionTaxDepositsRecAmt(String val) {
        this.consumptionTaxDepositsRecAmt=val;
    }
    public String getConsumptionTaxReturnedAmt() {
        return this.consumptionTaxReturnedAmt;
    }
    public void setConsumptionTaxReturnedAmt(String val) {
        this.consumptionTaxReturnedAmt=val;
    }
    public String getCreditPaymentCnt() {
        return this.creditPaymentCnt;
    }
    public void setCreditPaymentCnt(String val) {
        this.creditPaymentCnt=val;
    }
    public String getCreditPaymentAmt() {
        return this.creditPaymentAmt;
    }
    public void setCreditPaymentAmt(String val) {
        this.creditPaymentAmt=val;
    }
    public String getCreditPaymentSalesCnt() {
        return this.creditPaymentSalesCnt;
    }
    public void setCreditPaymentSalesCnt(String val) {
        this.creditPaymentSalesCnt=val;
    }
    public String getCreditPaymentSalesAmt() {
        return this.creditPaymentSalesAmt;
    }
    public void setCreditPaymentSalesAmt(String val) {
        this.creditPaymentSalesAmt=val;
    }
    public String getXebioCardCnt() {
        return this.xebioCardCnt;
    }
    public void setXebioCardCnt(String val) {
        this.xebioCardCnt=val;
    }
    public String getXebioCardAmt() {
        return this.xebioCardAmt;
    }
    public void setXebioCardAmt(String val) {
        this.xebioCardAmt=val;
    }
    public String getContractCreditPaymentCnt() {
        return this.contractCreditPaymentCnt;
    }
    public void setContractCreditPaymentCnt(String val) {
        this.contractCreditPaymentCnt=val;
    }
    public String getContractCreditPaymentAmt() {
        return this.contractCreditPaymentAmt;
    }
    public void setContractCreditPaymentAmt(String val) {
        this.contractCreditPaymentAmt=val;
    }
    public String getContractCreditPaymentSalesCnt() {
        return this.contractCreditPaymentSalesCnt;
    }
    public void setContractCreditPaymentSalesCnt(String val) {
        this.contractCreditPaymentSalesCnt=val;
    }
    public String getContractCreditPaymentSalesAmt() {
        return this.contractCreditPaymentSalesAmt;
    }
    public void setContractCreditPaymentSalesAmt(String val) {
        this.contractCreditPaymentSalesAmt=val;
    }
    public String getContractCreditPaymentShoppingLoanCnt() {
        return this.contractCreditPaymentShoppingLoanCnt;
    }
    public void setContractCreditPaymentShoppingLoanCnt(String val) {
        this.contractCreditPaymentShoppingLoanCnt=val;
    }
    public String getContractCreditPaymentShoppingLoanAmt() {
        return this.contractCreditPaymentShoppingLoanAmt;
    }
    public void setContractCreditPaymentShoppingLoanAmt(String val) {
        this.contractCreditPaymentShoppingLoanAmt=val;
    }
    public String getContractCreditPaymentEdyCnt() {
        return this.contractCreditPaymentEdyCnt;
    }
    public void setContractCreditPaymentEdyCnt(String val) {
        this.contractCreditPaymentEdyCnt=val;
    }
    public String getContractCreditPaymentEdyAmt() {
        return this.contractCreditPaymentEdyAmt;
    }
    public void setContractCreditPaymentEdyAmt(String val) {
        this.contractCreditPaymentEdyAmt=val;
    }
    public String getContractCreditPaymentSuicaCnt() {
        return this.contractCreditPaymentSuicaCnt;
    }
    public void setContractCreditPaymentSuicaCnt(String val) {
        this.contractCreditPaymentSuicaCnt=val;
    }
    public String getContractCreditPaymentSuicaAmt() {
        return this.contractCreditPaymentSuicaAmt;
    }
    public void setContractCreditPaymentSuicaAmt(String val) {
        this.contractCreditPaymentSuicaAmt=val;
    }
    public String getContractCreditPaymentWaonCnt() {
        return this.contractCreditPaymentWaonCnt;
    }
    public void setContractCreditPaymentWaonCnt(String val) {
        this.contractCreditPaymentWaonCnt=val;
    }
    public String getContractCreditPaymentWaonAmt() {
        return this.contractCreditPaymentWaonAmt;
    }
    public void setContractCreditPaymentWaonAmt(String val) {
        this.contractCreditPaymentWaonAmt=val;
    }
    public String getContractCreditPaymentCupCnt() {
        return this.contractCreditPaymentCupCnt;
    }
    public void setContractCreditPaymentCupCnt(String val) {
        this.contractCreditPaymentCupCnt=val;
    }
    public String getContractCreditPaymentCupAmt() {
        return this.contractCreditPaymentCupAmt;
    }
    public void setContractCreditPaymentCupAmt(String val) {
        this.contractCreditPaymentCupAmt=val;
    }
    public String getContractCreditPaymentTenant1Cnt() {
        return this.contractCreditPaymentTenant1Cnt;
    }
    public void setContractCreditPaymentTenant1Cnt(String val) {
        this.contractCreditPaymentTenant1Cnt=val;
    }
    public String getContractCreditPaymentTenant1Amt() {
        return this.contractCreditPaymentTenant1Amt;
    }
    public void setContractCreditPaymentTenant1Amt(String val) {
        this.contractCreditPaymentTenant1Amt=val;
    }
    public String getGiftCertPaymentCnt() {
        return this.giftCertPaymentCnt;
    }
    public void setGiftCertPaymentCnt(String val) {
        this.giftCertPaymentCnt=val;
    }
    public String getGiftCertPaymentAmt() {
        return this.giftCertPaymentAmt;
    }
    public void setGiftCertPaymentAmt(String val) {
        this.giftCertPaymentAmt=val;
    }
    public String getGiftCertPaymentSalesCnt() {
        return this.giftCertPaymentSalesCnt;
    }
    public void setGiftCertPaymentSalesCnt(String val) {
        this.giftCertPaymentSalesCnt=val;
    }
    public String getGiftCertPaymentSalesAmt() {
        return this.giftCertPaymentSalesAmt;
    }
    public void setGiftCertPaymentSalesAmt(String val) {
        this.giftCertPaymentSalesAmt=val;
    }
    public String getGiftCertPaymentXebioGrpCnt() {
        return this.giftCertPaymentXebioGrpCnt;
    }
    public void setGiftCertPaymentXebioGrpCnt(String val) {
        this.giftCertPaymentXebioGrpCnt=val;
    }
    public String getGiftCertPaymentXebioGrpAmt() {
        return this.giftCertPaymentXebioGrpAmt;
    }
    public void setGiftCertPaymentXebioGrpAmt(String val) {
        this.giftCertPaymentXebioGrpAmt=val;
    }
    public String getOtherPaymentCnt() {
        return this.otherPaymentCnt;
    }
    public void setOtherPaymentCnt(String val) {
        this.otherPaymentCnt=val;
    }
    public String getOtherPaymentAmt() {
        return this.otherPaymentAmt;
    }
    public void setOtherPaymentAmt(String val) {
        this.otherPaymentAmt=val;
    }
    public String getOtherOtherGiftCertCnt() {
        return this.otherOtherGiftCertCnt;
    }
    public void setOtherOtherGiftCertCnt(String val) {
        this.otherOtherGiftCertCnt=val;
    }
    public String getOtherOtherGiftCertAmt() {
        return this.otherOtherGiftCertAmt;
    }
    public void setOtherOtherGiftCertAmt(String val) {
        this.otherOtherGiftCertAmt=val;
    }
    public String getOtherOtherGiftCertGiftCertCnt() {
        return this.otherOtherGiftCertGiftCertCnt;
    }
    public void setOtherOtherGiftCertGiftCertCnt(String val) {
        this.otherOtherGiftCertGiftCertCnt=val;
    }
    public String getOtherOtherGiftCertGiftCertAmt() {
        return this.otherOtherGiftCertGiftCertAmt;
    }
    public void setOtherOtherGiftCertGiftCertAmt(String val) {
        this.otherOtherGiftCertGiftCertAmt=val;
    }
    public String getOtherOtherGiftCertOtherCnt() {
        return this.otherOtherGiftCertOtherCnt;
    }
    public void setOtherOtherGiftCertOtherCnt(String val) {
        this.otherOtherGiftCertOtherCnt=val;
    }
    public String getOtherOtherGiftCertOtherAmt() {
        return this.otherOtherGiftCertOtherAmt;
    }
    public void setOtherOtherGiftCertOtherAmt(String val) {
        this.otherOtherGiftCertOtherAmt=val;
    }
    public String getOtherTransferPaymentCnt() {
        return this.otherTransferPaymentCnt;
    }
    public void setOtherTransferPaymentCnt(String val) {
        this.otherTransferPaymentCnt=val;
    }
    public String getOtherTransferPaymentAmt() {
        return this.otherTransferPaymentAmt;
    }
    public void setOtherTransferPaymentAmt(String val) {
        this.otherTransferPaymentAmt=val;
    }
    public String getOtherCoDCnt() {
        return this.otherCoDCnt;
    }
    public void setOtherCoDCnt(String val) {
        this.otherCoDCnt=val;
    }
    public String getOtherCoDAmt() {
        return this.otherCoDAmt;
    }
    public void setOtherCoDAmt(String val) {
        this.otherCoDAmt=val;
    }
    public String getPurchaseTotalCnt() {
        return this.purchaseTotalCnt;
    }
    public void setPurchaseTotalCnt(String val) {
        this.purchaseTotalCnt=val;
    }
    public String getPurchaseTotalAmt() {
        return this.purchaseTotalAmt;
    }
    public void setPurchaseTotalAmt(String val) {
        this.purchaseTotalAmt=val;
    }
    public String getPurchaseTotStocksCnt() {
        return this.purchaseTotStocksCnt;
    }
    public void setPurchaseTotStocksCnt(String val) {
        this.purchaseTotStocksCnt=val;
    }
    public String getPurchaseTotStocksAmt() {
        return this.purchaseTotStocksAmt;
    }
    public void setPurchaseTotStocksAmt(String val) {
        this.purchaseTotStocksAmt=val;
    }
    public String getPurchaseTotConsumptionTaxAmt() {
        return this.purchaseTotConsumptionTaxAmt;
    }
    public void setPurchaseTotConsumptionTaxAmt(String val) {
        this.purchaseTotConsumptionTaxAmt=val;
    }
    public String getDepositsReceivedTotalCnt() {
        return this.depositsReceivedTotalCnt;
    }
    public void setDepositsReceivedTotalCnt(String val) {
        this.depositsReceivedTotalCnt=val;
    }
    public String getDepositsReceivedTotalAmt() {
        return this.depositsReceivedTotalAmt;
    }
    public void setDepositsReceivedTotalAmt(String val) {
        this.depositsReceivedTotalAmt=val;
    }
    public String getDepositsReceivedTotalResDownPaymentCnt() {
        return this.depositsReceivedTotalResDownPaymentCnt;
    }
    public void setDepositsReceivedTotalResDownPaymentCnt(String val) {
        this.depositsReceivedTotalResDownPaymentCnt=val;
    }
    public String getDepositsReceivedTotalResDownPaymentAmt() {
        return this.depositsReceivedTotalResDownPaymentAmt;
    }
    public void setDepositsReceivedTotalResDownPaymentAmt(String val) {
        this.depositsReceivedTotalResDownPaymentAmt=val;
    }
    public String getDepositsCashRetTotalCnt() {
        return this.depositsCashRetTotalCnt;
    }
    public void setDepositsCashRetTotalCnt(String val) {
        this.depositsCashRetTotalCnt=val;
    }
    public String getDepositsCashRetTotalAmt() {
        return this.depositsCashRetTotalAmt;
    }
    public void setDepositsCashRetTotalAmt(String val) {
        this.depositsCashRetTotalAmt=val;
    }
    public String getDepositsCashRetTotResDownPaymentRetCnt() {
        return this.depositsCashRetTotResDownPaymentRetCnt;
    }
    public void setDepositsCashRetTotResDownPaymentRetCnt(String val) {
        this.depositsCashRetTotResDownPaymentRetCnt=val;
    }
    public String getDepositsCashRetTotResDownPaymentRetAmt() {
        return this.depositsCashRetTotResDownPaymentRetAmt;
    }
    public void setDepositsCashRetTotResDownPaymentRetAmt(String val) {
        this.depositsCashRetTotResDownPaymentRetAmt=val;
    }
    public String getOtherDepositsTotalCnt() {
        return this.otherDepositsTotalCnt;
    }
    public void setOtherDepositsTotalCnt(String val) {
        this.otherDepositsTotalCnt=val;
    }
    public String getOtherDepositsTotalAmt() {
        return this.otherDepositsTotalAmt;
    }
    public void setOtherDepositsTotalAmt(String val) {
        this.otherDepositsTotalAmt=val;
    }
    public String getOtherDepositsTotXebioGrpTicketSalesCnt() {
        return this.otherDepositsTotXebioGrpTicketSalesCnt;
    }
    public void setOtherDepositsTotXebioGrpTicketSalesCnt(String val) {
        this.otherDepositsTotXebioGrpTicketSalesCnt=val;
    }
    public String getOtherDepositsTotXebioGrpTicketSalesAmt() {
        return this.otherDepositsTotXebioGrpTicketSalesAmt;
    }
    public void setOtherDepositsTotXebioGrpTicketSalesAmt(String val) {
        this.otherDepositsTotXebioGrpTicketSalesAmt=val;
    }
    public String getOtherDepositsTotTaxFreeMiscIncomeCnt() {
        return this.otherDepositsTotTaxFreeMiscIncomeCnt;
    }
    public void setOtherDepositsTotTaxFreeMiscIncomeCnt(String val) {
        this.otherDepositsTotTaxFreeMiscIncomeCnt=val;
    }
    public String getOtherDepositsTotTaxFreeMiscIncomeAmt() {
        return this.otherDepositsTotTaxFreeMiscIncomeAmt;
    }
    public void setOtherDepositsTotTaxFreeMiscIncomeAmt(String val) {
        this.otherDepositsTotTaxFreeMiscIncomeAmt=val;
    }
    public String getOtherDepositsTotMiscIncomeTicketBalCnt() {
        return this.otherDepositsTotMiscIncomeTicketBalCnt;
    }
    public void setOtherDepositsTotMiscIncomeTicketBalCnt(String val) {
        this.otherDepositsTotMiscIncomeTicketBalCnt=val;
    }
    public String getOtherDepositsTotMiscIncomeTicketBalAmt() {
        return this.otherDepositsTotMiscIncomeTicketBalAmt;
    }
    public void setOtherDepositsTotMiscIncomeTicketBalAmt(String val) {
        this.otherDepositsTotMiscIncomeTicketBalAmt=val;
    }
    public String getOtherDepositsTotPostageCnt() {
        return this.otherDepositsTotPostageCnt;
    }
    public void setOtherDepositsTotPostageCnt(String val) {
        this.otherDepositsTotPostageCnt=val;
    }
    public String getOtherDepositsTotPostageAmt() {
        return this.otherDepositsTotPostageAmt;
    }
    public void setOtherDepositsTotPostageAmt(String val) {
        this.otherDepositsTotPostageAmt=val;
    }
    public String getOtherDepositsTotCoDCnt() {
        return this.otherDepositsTotCoDCnt;
    }
    public void setOtherDepositsTotCoDCnt(String val) {
        this.otherDepositsTotCoDCnt=val;
    }
    public String getOtherDepositsTotCoDAmt() {
        return this.otherDepositsTotCoDAmt;
    }
    public void setOtherDepositsTotCoDAmt(String val) {
        this.otherDepositsTotCoDAmt=val;
    }
    public String getOtherDepositsTotBoxChangeCnt() {
        return this.otherDepositsTotBoxChangeCnt;
    }
    public void setOtherDepositsTotBoxChangeCnt(String val) {
        this.otherDepositsTotBoxChangeCnt=val;
    }
    public String getOtherDepositsTotBoxChangeAmt() {
        return this.otherDepositsTotBoxChangeAmt;
    }
    public void setOtherDepositsTotBoxChangeAmt(String val) {
        this.otherDepositsTotBoxChangeAmt=val;
    }
    public String getOtherWithdrawalsTotCnt() {
        return this.otherWithdrawalsTotCnt;
    }
    public void setOtherWithdrawalsTotCnt(String val) {
        this.otherWithdrawalsTotCnt=val;
    }
    public String getOtherWithdrawalsTotAmt() {
        return this.otherWithdrawalsTotAmt;
    }
    public void setOtherWithdrawalsTotAmt(String val) {
        this.otherWithdrawalsTotAmt=val;
    }
    public String getOtherWithdrawalsTotTenantDepositsCnt() {
        return this.otherWithdrawalsTotTenantDepositsCnt;
    }
    public void setOtherWithdrawalsTotTenantDepositsCnt(String val) {
        this.otherWithdrawalsTotTenantDepositsCnt=val;
    }
    public String getOtherWithdrawalsTotTenantDepositsAmt() {
        return this.otherWithdrawalsTotTenantDepositsAmt;
    }
    public void setOtherWithdrawalsTotTenantDepositsAmt(String val) {
        this.otherWithdrawalsTotTenantDepositsAmt=val;
    }
    public String getOtherWithdrawalsTotGCampainCnt() {
        return this.otherWithdrawalsTotGCampainCnt;
    }
    public void setOtherWithdrawalsTotGCampainCnt(String val) {
        this.otherWithdrawalsTotGCampainCnt=val;
    }
    public String getOtherWithdrawalsTotGCampainAmt() {
        return this.otherWithdrawalsTotGCampainAmt;
    }
    public void setOtherWithdrawalsTotGCampainAmt(String val) {
        this.otherWithdrawalsTotGCampainAmt=val;
    }
    public String getBankDepositsTotCnt() {
        return this.bankDepositsTotCnt;
    }
    public void setBankDepositsTotCnt(String val) {
        this.bankDepositsTotCnt=val;
    }
    public String getBankDepositsTotAmt() {
        return this.bankDepositsTotAmt;
    }
    public void setBankDepositsTotAmt(String val) {
        this.bankDepositsTotAmt=val;
    }
    public String getBankDepositsTotMiddleCollectionCnt() {
        return this.bankDepositsTotMiddleCollectionCnt;
    }
    public void setBankDepositsTotMiddleCollectionCnt(String val) {
        this.bankDepositsTotMiddleCollectionCnt=val;
    }
    public String getBankDepositsTotMiddleCollectionAmt() {
        return this.bankDepositsTotMiddleCollectionAmt;
    }
    public void setBankDepositsTotMiddleCollectionAmt(String val) {
        this.bankDepositsTotMiddleCollectionAmt=val;
    }
    public String getBankDepositsTotDepAdjustPlusCnt() {
        return this.bankDepositsTotDepAdjustPlusCnt;
    }
    public void setBankDepositsTotDepAdjustPlusCnt(String val) {
        this.bankDepositsTotDepAdjustPlusCnt=val;
    }
    public String getBankDepositsTotDepAdjustPlusAmt() {
        return this.bankDepositsTotDepAdjustPlusAmt;
    }
    public void setBankDepositsTotDepAdjustPlusAmt(String val) {
        this.bankDepositsTotDepAdjustPlusAmt=val;
    }
    public String getBankDepositsTotDepAdjustMinusCnt() {
        return this.bankDepositsTotDepAdjustMinusCnt;
    }
    public void setBankDepositsTotDepAdjustMinusCnt(String val) {
        this.bankDepositsTotDepAdjustMinusCnt=val;
    }
    public String getBankDepositsTotDepAdjustMinusAmt() {
        return this.bankDepositsTotDepAdjustMinusAmt;
    }
    public void setBankDepositsTotDepAdjustMinusAmt(String val) {
        this.bankDepositsTotDepAdjustMinusAmt=val;
    }
    public String getBankDepositsTotCashinAmtCnt() {
        return this.bankDepositsTotCashinAmtCnt;
    }
    public void setBankDepositsTotCashinAmtCnt(String val) {
        this.bankDepositsTotCashinAmtCnt=val;
    }
    public String getBankDepositsTotCashinAmtAmt() {
        return this.bankDepositsTotCashinAmtAmt;
    }
    public void setBankDepositsTotCashinAmtAmt(String val) {
        this.bankDepositsTotCashinAmtAmt=val;
    }
    public String getCashOnHandAmt() {
        return this.cashOnHandAmt;
    }
    public void setCashOnHandAmt(String val) {
        this.cashOnHandAmt=val;
    }
    public String getChangeReserveAmt() {
        return this.changeReserveAmt;
    }
    public void setChangeReserveAmt(String val) {
        this.changeReserveAmt=val;
    }
    public String getReceiptCountCnt() {
        return this.receiptCountCnt;
    }
    public void setReceiptCountCnt(String val) {
        this.receiptCountCnt=val;
    }
    public String getExchangeCountCnt() {
        return this.exchangeCountCnt;
    }
    public void setExchangeCountCnt(String val) {
        this.exchangeCountCnt=val;
    }
    public String getCancellationCountCnt() {
        return this.cancellationCountCnt;
    }
    public void setCancellationCountCnt(String val) {
        this.cancellationCountCnt=val;
    }
    public String getDutyFreeTransactionCnt() {
        return this.dutyFreeTransactionCnt;
    }
    public void setDutyFreeTransactionCnt(String val) {
        this.dutyFreeTransactionCnt=val;
    }
    public String getDutyFreeTransactionAmt() {
        return this.dutyFreeTransactionAmt;
    }
    public void setDutyFreeTransactionAmt(String val) {
        this.dutyFreeTransactionAmt=val;
    }
    public String getSalesRetCnt() {
        return this.salesRetCnt;
    }
    public void setSalesRetCnt(String val) {
        this.salesRetCnt=val;
    }
    public String getSalesRetAmt() {
        return this.salesRetAmt;
    }
    public void setSalesRetAmt(String val) {
        this.salesRetAmt=val;
    }
    public String getSalesVoidedCnt() {
        return this.salesVoidedCnt;
    }
    public void setSalesVoidedCnt(String val) {
        this.salesVoidedCnt=val;
    }
    public String getSalesVoidedAmt() {
        return this.salesVoidedAmt;
    }
    public void setSalesVoidedAmt(String val) {
        this.salesVoidedAmt=val;
    }
    public String getSalesVoidedRetCnt() {
        return this.salesVoidedRetCnt;
    }
    public void setSalesVoidedRetCnt(String val) {
        this.salesVoidedRetCnt=val;
    }
    public String getSalesVoidedRetAmt() {
        return this.salesVoidedRetAmt;
    }
    public void setSalesVoidedRetAmt(String val) {
        this.salesVoidedRetAmt=val;
    }
    public String getDepositsCancellationCnt() {
        return this.depositsCancellationCnt;
    }
    public void setDepositsCancellationCnt(String val) {
        this.depositsCancellationCnt=val;
    }
    public String getDepositsCancellationAmt() {
        return this.depositsCancellationAmt;
    }
    public void setDepositsCancellationAmt(String val) {
        this.depositsCancellationAmt=val;
    }
    public String getExchangeRetCnt() {
        return this.exchangeRetCnt;
    }
    public void setExchangeRetCnt(String val) {
        this.exchangeRetCnt=val;
    }
    public String getExchangeRetAmt() {
        return this.exchangeRetAmt;
    }
    public void setExchangeRetAmt(String val) {
        this.exchangeRetAmt=val;
    }
    public String getCashInCnt() {
        return this.cashInCnt;
    }
    public void setCashInCnt(String val) {
        this.cashInCnt=val;
    }
    public String getCashInAmt() {
        return this.cashInAmt;
    }
    public void setCashInAmt(String val) {
        this.cashInAmt=val;
    }
    public String getCashInVoidCnt() {
        return this.cashInVoidCnt;
    }
    public void setCashInVoidCnt(String val) {
        this.cashInVoidCnt=val;
    }
    public String getCashInVoidAmt() {
        return this.cashInVoidAmt;
    }
    public void setCashInVoidAmt(String val) {
        this.cashInVoidAmt=val;
    }
    public String getCashOutCnt() {
        return this.cashOutCnt;
    }
    public void setCashOutCnt(String val) {
        this.cashOutCnt=val;
    }
    public String getCashOutAmt() {
        return this.cashOutAmt;
    }
    public void setCashOutAmt(String val) {
        this.cashOutAmt=val;
    }
    public String getCashOutVoidCnt() {
        return this.cashOutVoidCnt;
    }
    public void setCashOutVoidCnt(String val) {
        this.cashOutVoidCnt=val;
    }
    public String getCashOutVoidAmt() {
        return this.cashOutVoidAmt;
    }
    public void setCashOutVoidAmt(String val) {
        this.cashOutVoidAmt=val;
    }
    public String getGroupTicketBalCnt() {
        return this.groupTicketBalCnt;
    }
    public void setGroupTicketBalCnt(String val) {
        this.groupTicketBalCnt=val;
    }
    public String getGroupTicketBalPOSRegistrationCnt() {
        return this.groupTicketBalPOSRegistrationCnt;
    }
    public void setGroupTicketBalPOSRegistrationCnt(String val) {
        this.groupTicketBalPOSRegistrationCnt=val;
    }
    public String getGroupTicketBalActualRemainingCnt() {
        return this.groupTicketBalActualRemainingCnt;
    }
    public void setGroupTicketBalActualRemainingCnt(String val) {
        this.groupTicketBalActualRemainingCnt=val;
    }
    public String getDiscountTicketIssueCnt() {
        return this.discountTicketIssueCnt;
    }
    public void setDiscountTicketIssueCnt(String val) {
        this.discountTicketIssueCnt=val;
    }
    public String getDiscountTicketIssueTestCnt() {
        return this.discountTicketIssueTestCnt;
    }
    public void setDiscountTicketIssueTestCnt(String val) {
        this.discountTicketIssueTestCnt=val;
    }
    public String getPremiumTicketIssueCnt() {
        return this.premiumTicketIssueCnt;
    }
    public void setPremiumTicketIssueCnt(String val) {
        this.premiumTicketIssueCnt=val;
    }
    public String getPremiumTicketIssueBSSHeadKaCnt() {
        return this.premiumTicketIssueBSSHeadKaCnt;
    }
    public void setPremiumTicketIssueBSSHeadKaCnt(String val) {
        this.premiumTicketIssueBSSHeadKaCnt=val;
    }
    public String getDetailsDiscountTotalCnt() {
        return this.detailsDiscountTotalCnt;
    }
    public void setDetailsDiscountTotalCnt(String val) {
        this.detailsDiscountTotalCnt=val;
    }
    public String getDetailsDiscountTotalAmt() {
        return this.detailsDiscountTotalAmt;
    }
    public void setDetailsDiscountTotalAmt(String val) {
        this.detailsDiscountTotalAmt=val;
    }
    public String getDetailsDiscountTotChobiTag18Cnt() {
        return this.detailsDiscountTotChobiTag18Cnt;
    }
    public void setDetailsDiscountTotChobiTag18Cnt(String val) {
        this.detailsDiscountTotChobiTag18Cnt=val;
    }
    public String getDetailsDiscountTotChobiTag18Amt() {
        return this.detailsDiscountTotChobiTag18Amt;
    }
    public void setDetailsDiscountTotChobiTag18Amt(String val) {
        this.detailsDiscountTotChobiTag18Amt=val;
    }
    public String getDetailsDiscountTotTradeInDiscountRate19Cnt() {
        return this.detailsDiscountTotTradeInDiscountRate19Cnt;
    }
    public void setDetailsDiscountTotTradeInDiscountRate19Cnt(String val) {
        this.detailsDiscountTotTradeInDiscountRate19Cnt=val;
    }
    public String getDetailsDiscountTotTradeInDiscountRate19Amt() {
        return this.detailsDiscountTotTradeInDiscountRate19Amt;
    }
    public void setDetailsDiscountTotTradeInDiscountRate19Amt(String val) {
        this.detailsDiscountTotTradeInDiscountRate19Amt=val;
    }
    public String getDetailsDiscountTotTradeInDiscountAmt19Cnt() {
        return this.detailsDiscountTotTradeInDiscountAmt19Cnt;
    }
    public void setDetailsDiscountTotTradeInDiscountAmt19Cnt(String val) {
        this.detailsDiscountTotTradeInDiscountAmt19Cnt=val;
    }
    public String getDetailsDiscountTotTradeInDiscountAmt19Amt() {
        return this.detailsDiscountTotTradeInDiscountAmt19Amt;
    }
    public void setDetailsDiscountTotTradeInDiscountAmt19Amt(String val) {
        this.detailsDiscountTotTradeInDiscountAmt19Amt=val;
    }
    public String getDetailsDiscountTotPurchaseReplaceSupport21Cnt() {
        return this.detailsDiscountTotPurchaseReplaceSupport21Cnt;
    }
    public void setDetailsDiscountTotPurchaseReplaceSupport21Cnt(String val) {
        this.detailsDiscountTotPurchaseReplaceSupport21Cnt=val;
    }
    public String getDetailsDiscountTotPurchaseReplaceSupport21Amt() {
        return this.detailsDiscountTotPurchaseReplaceSupport21Amt;
    }
    public void setDetailsDiscountTotPurchaseReplaceSupport21Amt(String val) {
        this.detailsDiscountTotPurchaseReplaceSupport21Amt=val;
    }
    public String getDetailsDiscountTotLimited91Cnt() {
        return this.detailsDiscountTotLimited91Cnt;
    }
    public void setDetailsDiscountTotLimited91Cnt(String val) {
        this.detailsDiscountTotLimited91Cnt=val;
    }
    public String getDetailsDiscountTotLimited91Amt() {
        return this.detailsDiscountTotLimited91Amt;
    }
    public void setDetailsDiscountTotLimited91Amt(String val) {
        this.detailsDiscountTotLimited91Amt=val;
    }
    public String getDetailsDiscountDetCnt() {
        return this.detailsDiscountDetCnt;
    }
    public void setDetailsDiscountDetCnt(String val) {
        this.detailsDiscountDetCnt=val;
    }
    public String getDetailsDiscountDetAmt() {
        return this.detailsDiscountDetAmt;
    }
    public void setDetailsDiscountDetAmt(String val) {
        this.detailsDiscountDetAmt=val;
    }
    public String getSubTotDiscountCnt() {
        return this.subTotDiscountCnt;
    }
    public void setSubTotDiscountCnt(String val) {
        this.subTotDiscountCnt=val;
    }
    public String getSubTotDiscountAmt() {
        return this.subTotDiscountAmt;
    }
    public void setSubTotDiscountAmt(String val) {
        this.subTotDiscountAmt=val;
    }
    public String getSubTotDiscountCoupon11Cnt() {
        return this.subTotDiscountCoupon11Cnt;
    }
    public void setSubTotDiscountCoupon11Cnt(String val) {
        this.subTotDiscountCoupon11Cnt=val;
    }
    public String getSubTotDiscountCoupon11Amt() {
        return this.subTotDiscountCoupon11Amt;
    }
    public void setSubTotDiscountCoupon11Amt(String val) {
        this.subTotDiscountCoupon11Amt=val;
    }
    public String getSubTotDiscountPointTicketXebio13Cnt() {
        return this.subTotDiscountPointTicketXebio13Cnt;
    }
    public void setSubTotDiscountPointTicketXebio13Cnt(String val) {
        this.subTotDiscountPointTicketXebio13Cnt=val;
    }
    public String getSubTotDiscountPointTicketXebio13Amt() {
        return this.subTotDiscountPointTicketXebio13Amt;
    }
    public void setSubTotDiscountPointTicketXebio13Amt(String val) {
        this.subTotDiscountPointTicketXebio13Amt=val;
    }
    public String getSubTotDiscountPointMortgage16Cnt() {
        return this.subTotDiscountPointMortgage16Cnt;
    }
    public void setSubTotDiscountPointMortgage16Cnt(String val) {
        this.subTotDiscountPointMortgage16Cnt=val;
    }
    public String getSubTotDiscountPointMortgage16Amt() {
        return this.subTotDiscountPointMortgage16Amt;
    }
    public void setSubTotDiscountPointMortgage16Amt(String val) {
        this.subTotDiscountPointMortgage16Amt=val;
    }
    public String getSubTotDiscountGCoupon32Cnt() {
        return this.subTotDiscountGCoupon32Cnt;
    }
    public void setSubTotDiscountGCoupon32Cnt(String val) {
        this.subTotDiscountGCoupon32Cnt=val;
    }
    public String getSubTotDiscountGCoupon32Amt() {
        return this.subTotDiscountGCoupon32Amt;
    }
    public void setSubTotDiscountGCoupon32Amt(String val) {
        this.subTotDiscountGCoupon32Amt=val;
    }
    public String getSubTotDiscountPremiumDiscnt61Cnt() {
        return this.subTotDiscountPremiumDiscnt61Cnt;
    }
    public void setSubTotDiscountPremiumDiscnt61Cnt(String val) {
        this.subTotDiscountPremiumDiscnt61Cnt=val;
    }
    public String getSubTotDiscountPremiumDiscnt61Amt() {
        return this.subTotDiscountPremiumDiscnt61Amt;
    }
    public void setSubTotDiscountPremiumDiscnt61Amt(String val) {
        this.subTotDiscountPremiumDiscnt61Amt=val;
    }
    public String getSubTotDiscount71Cnt() {
        return this.subTotDiscount71Cnt;
    }
    public void setSubTotDiscount71Cnt(String val) {
        this.subTotDiscount71Cnt=val;
    }
    public String getSubTotDiscount71Amt() {
        return this.subTotDiscount71Amt;
    }
    public void setSubTotDiscount71Amt(String val) {
        this.subTotDiscount71Amt=val;
    }
    public String getSubTotDiscountCompanyEmployeeSpecial72Cnt() {
        return this.subTotDiscountCompanyEmployeeSpecial72Cnt;
    }
    public void setSubTotDiscountCompanyEmployeeSpecial72Cnt(String val) {
        this.subTotDiscountCompanyEmployeeSpecial72Cnt=val;
    }
    public String getSubTotDiscountCompanyEmployeeSpecial72Amt() {
        return this.subTotDiscountCompanyEmployeeSpecial72Amt;
    }
    public void setSubTotDiscountCompanyEmployeeSpecial72Amt(String val) {
        this.subTotDiscountCompanyEmployeeSpecial72Amt=val;
    }
    public String getSubTotDiscountSpecialDiscountComp73Cnt() {
        return this.subTotDiscountSpecialDiscountComp73Cnt;
    }
    public void setSubTotDiscountSpecialDiscountComp73Cnt(String val) {
        this.subTotDiscountSpecialDiscountComp73Cnt=val;
    }
    public String getSubTotDiscountSpecialDiscountComp73Amt() {
        return this.subTotDiscountSpecialDiscountComp73Amt;
    }
    public void setSubTotDiscountSpecialDiscountComp73Amt(String val) {
        this.subTotDiscountSpecialDiscountComp73Amt=val;
    }
    public String getSubTotDiscountDMDiscnt74Cnt() {
        return this.subTotDiscountDMDiscnt74Cnt;
    }
    public void setSubTotDiscountDMDiscnt74Cnt(String val) {
        this.subTotDiscountDMDiscnt74Cnt=val;
    }
    public String getSubTotDiscountDMDiscnt74Amt() {
        return this.subTotDiscountDMDiscnt74Amt;
    }
    public void setSubTotDiscountDMDiscnt74Amt(String val) {
        this.subTotDiscountDMDiscnt74Amt=val;
    }
    public String getSubTotDiscountCorpDiscnt75Cnt() {
        return this.subTotDiscountCorpDiscnt75Cnt;
    }
    public void setSubTotDiscountCorpDiscnt75Cnt(String val) {
        this.subTotDiscountCorpDiscnt75Cnt=val;
    }
    public String getSubTotDiscountCorpDiscnt75Amt() {
        return this.subTotDiscountCorpDiscnt75Amt;
    }
    public void setSubTotDiscountCorpDiscnt75Amt(String val) {
        this.subTotDiscountCorpDiscnt75Amt=val;
    }
    public String getSubTotDiscountDMDiscount76Cnt() {
        return this.subTotDiscountDMDiscount76Cnt;
    }
    public void setSubTotDiscountDMDiscount76Cnt(String val) {
        this.subTotDiscountDMDiscount76Cnt=val;
    }
    public String getSubTotDiscountDMDiscount76Amt() {
        return this.subTotDiscountDMDiscount76Amt;
    }
    public void setSubTotDiscountDMDiscount76Amt(String val) {
        this.subTotDiscountDMDiscount76Amt=val;
    }
    public String getSubTotDiscountDMDiscount77Cnt() {
        return this.subTotDiscountDMDiscount77Cnt;
    }
    public void setSubTotDiscountDMDiscount77Cnt(String val) {
        this.subTotDiscountDMDiscount77Cnt=val;
    }
    public String getSubTotDiscountDMDiscount77Amt() {
        return this.subTotDiscountDMDiscount77Amt;
    }
    public void setSubTotDiscountDMDiscount77Amt(String val) {
        this.subTotDiscountDMDiscount77Amt=val;
    }
    public String getSubTotDiscountTicket78Cnt() {
        return this.subTotDiscountTicket78Cnt;
    }
    public void setSubTotDiscountTicket78Cnt(String val) {
        this.subTotDiscountTicket78Cnt=val;
    }
    public String getSubTotDiscountTicket78Amt() {
        return this.subTotDiscountTicket78Amt;
    }
    public void setSubTotDiscountTicket78Amt(String val) {
        this.subTotDiscountTicket78Amt=val;
    }
    public String getSubTotDiscountDMDiscount79Cnt() {
        return this.subTotDiscountDMDiscount79Cnt;
    }
    public void setSubTotDiscountDMDiscount79Cnt(String val) {
        this.subTotDiscountDMDiscount79Cnt=val;
    }
    public String getSubTotDiscountDMDiscount79Amt() {
        return this.subTotDiscountDMDiscount79Amt;
    }
    public void setSubTotDiscountDMDiscount79Amt(String val) {
        this.subTotDiscountDMDiscount79Amt=val;
    }
    public String getSubTotDiscountLimitedSales80Cnt() {
        return this.subTotDiscountLimitedSales80Cnt;
    }
    public void setSubTotDiscountLimitedSales80Cnt(String val) {
        this.subTotDiscountLimitedSales80Cnt=val;
    }
    public String getSubTotDiscountLimitedSales80Amt() {
        return this.subTotDiscountLimitedSales80Amt;
    }
    public void setSubTotDiscountLimitedSales80Amt(String val) {
        this.subTotDiscountLimitedSales80Amt=val;
    }
    public String getSubTotDiscountStockholderPreferential81Cnt() {
        return this.subTotDiscountStockholderPreferential81Cnt;
    }
    public void setSubTotDiscountStockholderPreferential81Cnt(String val) {
        this.subTotDiscountStockholderPreferential81Cnt=val;
    }
    public String getSubTotDiscountStockholderPreferential81Amt() {
        return this.subTotDiscountStockholderPreferential81Amt;
    }
    public void setSubTotDiscountStockholderPreferential81Amt(String val) {
        this.subTotDiscountStockholderPreferential81Amt=val;
    }
    public String getSubTotDiscountXebio82Cnt() {
        return this.subTotDiscountXebio82Cnt;
    }
    public void setSubTotDiscountXebio82Cnt(String val) {
        this.subTotDiscountXebio82Cnt=val;
    }
    public String getSubTotDiscountXebio82Amt() {
        return this.subTotDiscountXebio82Amt;
    }
    public void setSubTotDiscountXebio82Amt(String val) {
        this.subTotDiscountXebio82Amt=val;
    }
    public String getSubTotDiscount83Cnt() {
        return this.subTotDiscount83Cnt;
    }
    public void setSubTotDiscount83Cnt(String val) {
        this.subTotDiscount83Cnt=val;
    }
    public String getSubTotDiscount83Amt() {
        return this.subTotDiscount83Amt;
    }
    public void setSubTotDiscount83Amt(String val) {
        this.subTotDiscount83Amt=val;
    }
    public String getSubTotDiscountStudent84Cnt() {
        return this.subTotDiscountStudent84Cnt;
    }
    public void setSubTotDiscountStudent84Cnt(String val) {
        this.subTotDiscountStudent84Cnt=val;
    }
    public String getSubTotDiscountStudent84Amt() {
        return this.subTotDiscountStudent84Amt;
    }
    public void setSubTotDiscountStudent84Amt(String val) {
        this.subTotDiscountStudent84Amt=val;
    }
    public String getSubTotDiscountSpecial85Cnt() {
        return this.subTotDiscountSpecial85Cnt;
    }
    public void setSubTotDiscountSpecial85Cnt(String val) {
        this.subTotDiscountSpecial85Cnt=val;
    }
    public String getSubTotDiscountSpecial85Amt() {
        return this.subTotDiscountSpecial85Amt;
    }
    public void setSubTotDiscountSpecial85Amt(String val) {
        this.subTotDiscountSpecial85Amt=val;
    }
    public String getSubTotDiscount86Cnt() {
        return this.subTotDiscount86Cnt;
    }
    public void setSubTotDiscount86Cnt(String val) {
        this.subTotDiscount86Cnt=val;
    }
    public String getSubTotDiscount86Amt() {
        return this.subTotDiscount86Amt;
    }
    public void setSubTotDiscount86Amt(String val) {
        this.subTotDiscount86Amt=val;
    }
    public String getSubTotDiscount87Cnt() {
        return this.subTotDiscount87Cnt;
    }
    public void setSubTotDiscount87Cnt(String val) {
        this.subTotDiscount87Cnt=val;
    }
    public String getSubTotDiscount87Amt() {
        return this.subTotDiscount87Amt;
    }
    public void setSubTotDiscount87Amt(String val) {
        this.subTotDiscount87Amt=val;
    }
    public String getSubTotDiscount88Cnt() {
        return this.subTotDiscount88Cnt;
    }
    public void setSubTotDiscount88Cnt(String val) {
        this.subTotDiscount88Cnt=val;
    }
    public String getSubTotDiscount88Amt() {
        return this.subTotDiscount88Amt;
    }
    public void setSubTotDiscount88Amt(String val) {
        this.subTotDiscount88Amt=val;
    }
    public String getSubTotDiscount89Cnt() {
        return this.subTotDiscount89Cnt;
    }
    public void setSubTotDiscount89Cnt(String val) {
        this.subTotDiscount89Cnt=val;
    }
    public String getSubTotDiscount89Amt() {
        return this.subTotDiscount89Amt;
    }
    public void setSubTotDiscount89Amt(String val) {
        this.subTotDiscount89Amt=val;
    }
    public String getSubTotDiscountTicket92Cnt() {
        return this.subTotDiscountTicket92Cnt;
    }
    public void setSubTotDiscountTicket92Cnt(String val) {
        this.subTotDiscountTicket92Cnt=val;
    }
    public String getSubTotDiscountTicket92Amt() {
        return this.subTotDiscountTicket92Amt;
    }
    public void setSubTotDiscountTicket92Amt(String val) {
        this.subTotDiscountTicket92Amt=val;
    }
    public String getSubTotDiscountRateCnt() {
        return this.subTotDiscountRateCnt;
    }
    public void setSubTotDiscountRateCnt(String val) {
        this.subTotDiscountRateCnt=val;
    }
    public String getSubTotDiscountRateAmt() {
        return this.subTotDiscountRateAmt;
    }
    public void setSubTotDiscountRateAmt(String val) {
        this.subTotDiscountRateAmt=val;
    }
}
