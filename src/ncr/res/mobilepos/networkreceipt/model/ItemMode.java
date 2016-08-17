package ncr.res.mobilepos.networkreceipt.model;

import ncr.res.mobilepos.journalization.model.poslog.ItemID;
import ncr.res.mobilepos.helper.StringUtility;

public class ItemMode {

    /**
     *
     */
    private String sequenceNumber;

    /**
     *
     */
    private String itemID;
    
    public enum ITEM_TYPE { Codabar };

    /**
     *
     */
    private String itemName;

    /**
     * 品番
     */
    private String itemNote;

    /**
     *
     */
    private double regularSalesUnitPrice;

    /**
     *
     */
    private double actualSalesUnitPrice;

    /**
     *
     */
    private double extendedAmount;

    /**
     *
     */
    private double extendedDiscountAmount;

    /**
     *
     */
    private int quantity;

    /**
     *
     */
    private String reasonCode;

    /**
     *
     */
    private double percent;

    /**
     *
     */
    private double discountAmount;

    /**
     *
     */
    private int mmSequence;

    /**
     *
     */
    private int mmQuantity;

    /**
     *
     */
    private int mmSize;

    /**
     *
     */
    private String mmID;

    /**
     *
     */
    private String mmName;

    /**
     *
     */
    private String mmReasonCode;

    /**
     *
     */
    private double mmAmount;

    /**
     *
     */
    private double mmPreviousPrice;

    /**
     *
     */
    private double mmSellingPrice;

    /**
     *
     */
    private String nonSalesFlag;

    /**
     *
     */
    private String discountableFlag;

    /**
     * language (ja or en)
     */
    private String language;

    /**
     * allMmQuantity
     */
    private String allMmQuantity;
    /**
     * voucherTypeCode
     */
    private String voucherTypeCode;
    private double voucherAmount;
    private double voucherFaceValueAmount;
    private String voucherSerialNumber;
    private String voucherExpirationDate;
    private double voucherUnspentAmount;
    /**返品の取消Flag */
    private String voidreturnFlag;
    private double exemptTaxAmount;
  //==================== 前受金等精算情報 ==================== START
    
    /**
     * ID for
     * -Unspecified   (In case of "Donation")
     * -Layaway   (In case of "Layaway")
     * -RainCheck   ( In case of "RainCheck")
    */
    private String paymentReservationID;

    /**
     * -Unspecified   (In case of "Donation")
     * -Layaway   (In case of "Layaway")
     * -RainCheck   ( In case of "RainCheck")
     */
    private String paymentCode;

    /** 金額 */
    private double paymentAmount;

    /** お釣り */
    private double paymentChange;

    //==================== 前受金等精算情報 ====================  E N D

    /** itemType value **/
    //private String itemType;

    /** discount item mark 'S' */
    private String itemMark;

    /** B対象獲得ポイント */
    private double itemPoints;

    /** 社員販売 */
    private String employeeFlag;
    
    private String isAdvancedFlag;
    //TODO
    private String priceItemDerivationRuleID;
    private String priceTotalDerivationRuleID;
    private String priceDerivationRuleType;
    
    private String barReservationStatus;
    private String barReservationTypeReasonCode;
    private String rainCheckReasonCode;
    // Inventory Reservation ID
    private String inventoryReservationID;
    
    private String denomination="";
	
	private String changeMachineAmount="";
	
	private String cashDrawerAmount="";
	
	private String changeMachineDrawerTotal="";
	
	private String tenderCtrlTypePayInOutList = "false";
	
	private String tenderCtrlBalType = "Cash";
	
	private String tenderCtrlType;
	
	private String denominationQty;
	
	private String changeMachineDenomQty;
	
	private String cashDrawerDenomQty;
	
	private String groupLabel;
	
	private String groupTicketLabel;
	
	private String realAmount;
	
	private String calculatedAmount;
	
	private String gapAmount;
	
	private String differenceAmount;
	
	private String calculatedAmt2;
	
	private String payInPlanDenomination;
	
	private String payInPlanDenomQty;
	
	private String payInPlanDrawerAmount;
	
	private String detailsDiscountAmountKind;
	private String detailsDiscountAmountCnt;
	private String detailsDiscountAmountAmt;
	private String detailsDiscountRateKind;
	private String detailsDiscountRateCnt;
	private String detailsDiscountRateAmt;
	private String subTotDiscountRateRateKind;
	private String subTotDiscountRateRateCnt;
	private String subTotDiscountRateRateAmt;
	private String subTotDiscountAmountAmountKind;
	private String subTotDiscountAmountAmountCnt;
	private String subTotDiscountAmountAmountAmt;
	
    /**
     * @return employeeFlag
     */
    public String getEmployeeFlag() {
        return employeeFlag;
    }
    /**
     * @param employeeFlag セットする employeeFlag
     */
    public void setEmployeeFlag(String employeeFlag) {
        this.employeeFlag = employeeFlag;
    }
    /**
     * showTaxKanji
     */
    private String showTaxKanji;

    /**
     * @return showTaxKanji
     */
    public String getShowTaxKanji() {
        return showTaxKanji;
    }
    /**
     * @param showTaxKanji showTaxKanji
     */
    public void setShowTaxKanji(String showTaxKanji) {
        this.showTaxKanji = showTaxKanji;
    }

    /**
     * @return sequenceNumber
     */
    public String getSequenceNumber() {

        return sequenceNumber;
    }

    /**
     * @param sequenceNumber sequenceNumber
     */
    public void setSequenceNumber(String sequenceNumber) {

        this.sequenceNumber = sequenceNumber;
    }

    /**
     * @return itemID
     */
    public String getItemID() {

        return itemID;
    }

    /**
     * @param itemID itemID
     */
    public void setItemID(String itemID) {

        this.itemID = itemID;
    }
    /**
     * Sets item id or plucode.
     * <p>If item type is Codabar, format is {plucode}-{size}-{color}</p>
     * 
     * @param itemId ItemID
     */
    public void setItemID(ItemID itemId) {	
	this.itemID = itemId.getPluCode();	
	if (itemId.getType().equalsIgnoreCase(ITEM_TYPE.Codabar.name())) {
	    StringBuilder sb = new StringBuilder();
	    sb.append(this.itemID.substring(2, 16)).append("-")
		    .append(this.itemID.substring(16, 19)).append("-")
		    .append(this.itemID.substring(19, 22));
	    this.itemID = sb.toString();
	}
    }

    /**
     * @return itemName
     */
    public String getItemName() {

        return itemName;
    }

    /**
     * @param itemName itemName
     */
    public void setItemName(String itemName) {

        this.itemName = itemName;
    }

    /**
     * @return regularSalesUnitPrice
     */
    public double getRegularSalesUnitPrice() {

        return regularSalesUnitPrice;
    }

    /**
     * @param regularSalesUnitPrice regularSalesUnitPrice
     */
    public void setRegularSalesUnitPrice(double regularSalesUnitPrice) {

        this.regularSalesUnitPrice = regularSalesUnitPrice;
    }

    /**
     * @return actualSalesUnitPrice
     */
    public double getActualSalesUnitPrice() {

        return actualSalesUnitPrice;
    }

    /**
     * @param actualSalesUnitPrice actualSalesUnitPrice
     */
    public void setActualSalesUnitPrice(double actualSalesUnitPrice) {

        this.actualSalesUnitPrice = actualSalesUnitPrice;
    }

    /**
     * @return extendedAmount
     */
    public double getExtendedAmount() {

        return extendedAmount;
    }

    /**
     * @param extendedAmount extendedAmount
     */
    public void setExtendedAmount(double extendedAmount) {

        this.extendedAmount = extendedAmount;
    }

    /**
     * @return extendedDiscountAmount
     */
    public double getExtendedDiscountAmount() {

        return extendedDiscountAmount;
    }

    /**
     * @param extendedDiscountAmount extendedDiscountAmount
     */
    public void setExtendedDiscountAmount(double extendedDiscountAmount) {

        this.extendedDiscountAmount = extendedDiscountAmount;
    }

    /**
     * @return quantity
     */
    public int getQuantity() {

        return quantity;
    }

    /**
     * @param quantity quantity
     */
    public void setQuantity(int quantity) {

        this.quantity = quantity;
    }

    /**
     * @return reasonCode
     */
    public String getReasonCode() {

        return reasonCode;
    }

    /**
     * @param reasonCode reasonCode
     */
    public void setReasonCode(String reasonCode) {

        this.reasonCode = reasonCode;
    }

    /**
     * @return percent
     */
    public double getPercent() {

        return percent;
    }

    /**
     * @param percent percent
     */
    public void setPercent(double percent) {

        this.percent = percent;
    }

    /**
     * @return mmID
     */
    public String getMmID() {

        return mmID;
    }

    /**
     * @param mmID mmID
     */
    public void setMmID(String mmID) {

        this.mmID = mmID;
    }

    /**
     * @return mmName
     */
    public String getMmName() {

        return mmName;
    }

    /**
     * @param mmName mmName
     */
    public void setMmName(String mmName) {

        this.mmName = mmName;
    }

    /**
     * @return mmReasonCode
     */
    public String getMmReasonCode() {

        return mmReasonCode;
    }

    /**
     * @param mmReasonCode mmReasonCode
     */
    public void setMmReasonCode(String mmReasonCode) {

        this.mmReasonCode = mmReasonCode;
    }

    /**
     * @return mmAmount
     */
    public double getMmAmount() {

        return mmAmount;
    }

    /**
     * @param mmAmount mmAmount
     */
    public void setMmAmount(double mmAmount) {

        this.mmAmount = mmAmount;
    }

    /**
     * @return mmSellingPrice
     */
    public double getMmSellingPrice() {

        return mmSellingPrice;
    }

    /**
     * @param mmSellingPrice mmSellingPrice
     */
    public void setMmSellingPrice(double mmSellingPrice) {

        this.mmSellingPrice = mmSellingPrice;
    }

    /**
     * @return mmPreviousPrice
     */
    public double getMmPreviousPrice() {

        return mmPreviousPrice;
    }

    /**
     * @param mmPreviousPrice mmPreviousPrice
     */
    public void setMmPreviousPrice(double mmPreviousPrice) {

        this.mmPreviousPrice = mmPreviousPrice;
    }

    /**
     * @return discountAmount
     */
    public double getDiscountAmount() {

        return discountAmount;
    }

    /**
     * @param discountAmount discountAmount
     */
    public void setDiscountAmount(double discountAmount) {

        this.discountAmount = discountAmount;
    }

    /**
     * @return mmSequence
     */
    public int getMmSequence() {

        return mmSequence;
    }

    /**
     * @param mmSequence mmSequence
     */
    public void setMmSequence(int mmSequence) {

        this.mmSequence = mmSequence;
    }

    /**
     * @return nonSalesFlag
     */
    public String getNonSalesFlag() {

        return nonSalesFlag;
    }

    /**
     * @param nonSalesFlag nonSalesFlag
     */
    public void setNonSalesFlag(String nonSalesFlag) {

        this.nonSalesFlag = nonSalesFlag;
    }

    /**
     * @return discountableFlag
     */
    public String getDiscountableFlag() {

        return discountableFlag;
    }

    /**
     * @param discountableFlag discountableFlag
     */
    public void setDiscountableFlag(String discountableFlag) {

        this.discountableFlag = discountableFlag;
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
     * @return mmSize
     */
    public int getMmSize() {

        return mmSize;
    }

    /**
     * @param mmSize mmSize
     */
    public void setMmSize(int mmSize) {

        this.mmSize = mmSize;
    }

    /**
     * language
     * @return
     */
    public String getLanguage() {
        return language;
    }

    /**
     * language
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * allMmQuantity
     * @return
     */
    public String getAllMmQuantity() {
        return allMmQuantity;
    }

    /**
     * allMmQuantity
     */
    public void setAllMmQuantity(String allMmQuantity) {
        this.allMmQuantity = allMmQuantity;
    }
    /**
     * voucherTypeCode
     * @return
     */
    public String getVoucherTypeCode() {
        return voucherTypeCode;
    }
    /**
     * voucherTypeCode
     */
    public void setVoucherTypeCode(String voucherTypeCode) {
        this.voucherTypeCode = voucherTypeCode;
    }
    /**
     * @return voucherAmount
     */
    public double getVoucherAmount() {
        return voucherAmount;
    }
    /**
     * @param voucherAmount セットする voucherAmount
     */
    public void setVoucherAmount(double voucherAmount) {
        this.voucherAmount = voucherAmount;
    }
    /**
     * @return voucherFaceValueAmount
     */
    public double getVoucherFaceValueAmount() {
        return voucherFaceValueAmount;
    }
    /**
     * @param voucherFaceValueAmount セットする voucherFaceValueAmount
     */
    public void setVoucherFaceValueAmount(double voucherFaceValueAmount) {
        this.voucherFaceValueAmount = voucherFaceValueAmount;
    }
    /**
     * @return voucherSerialNumber
     */
    public String getVoucherSerialNumber() {
        return voucherSerialNumber;
    }
    /**
     *
     */
    public String getVoucherSerialNumberEncrypt(){
        StringBuilder cNo = null;
        if(!StringUtility.isNullOrEmpty(this.getVoucherSerialNumber()) &&
                this.getVoucherSerialNumber().length() > 4){
            int length = this.getVoucherSerialNumber().length();
            cNo = new StringBuilder(this.getVoucherSerialNumber().substring(length - 4, length));
            for(int i = 0 ; i < length - 4 ; i++){
                cNo.insert(0, "X");
            }
        }else {
            cNo = new StringBuilder("XXXXXXXXXXXXXXXX");
        }
        return cNo.toString();
    }
    /**
     * @param voucherSerialNumber セットする voucherSerialNumber
     */
    public void setVoucherSerialNumber(String voucherSerialNumber) {
        this.voucherSerialNumber = voucherSerialNumber;
    }
    /**
     * @return voucherExpirationDate
     */
    public String getVoucherExpirationDate() {
        return voucherExpirationDate;
    }
    /**
     * @param voucherExpirationDate セットする voucherExpirationDate
     */
    public void setVoucherExpirationDate(String voucherExpirationDate) {
        this.voucherExpirationDate = voucherExpirationDate;
    }
    /**
     * @return voucherUnspentAmount
     */
    public double getVoucherUnspentAmount() {
        return voucherUnspentAmount;
    }
    /**
     * @param voucherUnspentAmount セットする voucherUnspentAmount
     */
    public void setVoucherUnspentAmount(double voucherUnspentAmount) {
        this.voucherUnspentAmount = voucherUnspentAmount;
    }
    /**
     * @return paymentReservationID
     */
    public String getPaymentReservationID() {
        return paymentReservationID;
    }
    /**
     * @param paymentReservationID セットする paymentReservationID
     */
    public void setPaymentReservationID(String paymentReservationID) {
        this.paymentReservationID = paymentReservationID;
    }
    /**
     * @return paymentCode
     */
    public String getPaymentCode() {
        return paymentCode;
    }
    /**
     * @param paymentCode セットする paymentCode
     */
    public void setPaymentCode(String paymentCode) {
        this.paymentCode = paymentCode;
    }
    /**
     * @return paymentAmount
     */
    public double getPaymentAmount() {
        return paymentAmount;
    }
    /**
     * @param paymentAmount セットする paymentAmount
     */
    public void setPaymentAmount(double paymentAmount) {
        this.paymentAmount = paymentAmount;
    }
    /**
     * @return paymentChange
     */
    public double getPaymentChange() {
        return paymentChange;
    }
    /**
     * @param paymentChange セットする paymentChange
     */
    public void setPaymentChange(double paymentChange) {
        this.paymentChange = paymentChange;
    }
    /**
    /*
     * @return itemMark
     */
    public String getItemMark() {
        return itemMark;
    }
    /**
     * @param itemMark セットする itemMark
     */
    public void setItemMark(String itemMark) {
        this.itemMark = itemMark;
    }
    /**
     * @return itemNote
     */
    public String getItemNote() {
        return itemNote;
    }
    /**
     * @param itemNote セットする itemNote
     */
    public void setItemNote(String itemNote) {
        this.itemNote = itemNote;
    }
    /**
     * @return itemPoints
     */
    public double getItemPoints() {
        return itemPoints;
    }
    /**
     * @param itemPoints セットする itemPoints
     */
    public void setItemPoints(double itemPoints) {
        this.itemPoints = itemPoints;
    }
    //FENGSHA ADD START
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
  //FENGSHA ADD END
    //1.02 FENGSHA 2015.01.30 ADD START
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
	public String getIsAdvancedFlag() {
		return isAdvancedFlag;
	}
	public void setIsAdvancedFlag(String isAdvancedFlag) {
		this.isAdvancedFlag = isAdvancedFlag;
	}
	/**
	 * @return the rainCheckReasonCode
	 */
	public String getRainCheckReasonCode() {
		return rainCheckReasonCode;
	}
	/**
	 * @param rainCheckReasonCode the rainCheckReasonCode to set
	 */
	public void setRainCheckReasonCode(String rainCheckReasonCode) {
		this.rainCheckReasonCode = rainCheckReasonCode;
	}
	/**
	 * @return the inventoryReservationID
	 */
	public String getInventoryReservationID() {
		return inventoryReservationID;
	}
	/**
	 * @param inventoryReservationID the inventoryReservationID to set
	 */
	public void setInventoryReservationID(String inventoryReservationID) {
		this.inventoryReservationID = inventoryReservationID;
	}
	/**
	 * @return the barReservationStatus
	 */
	public String getBarReservationStatus() {
		return barReservationStatus;
	}
	/**
	 * @param barReservationStatus the barReservationStatus to set
	 */
	public void setBarReservationStatus(String barReservationStatus) {
		this.barReservationStatus = barReservationStatus;
	}
	/**
	 * @return the barReservationTypeReasonCode
	 */
	public String getBarReservationTypeReasonCode() {
		return barReservationTypeReasonCode;
	}
	/**
	 * @param barReservationTypeReasonCode the barReservationTypeReasonCode to set
	 */
	public void setBarReservationTypeReasonCode(String barReservationTypeReasonCode) {
		this.barReservationTypeReasonCode = barReservationTypeReasonCode;
	}
	
	public String getPriceItemDerivationRuleID() {
        return priceItemDerivationRuleID;
    }
    public void setPriceItemDerivationRuleID(String priceItemDerivationRuleID) {
        this.priceItemDerivationRuleID = priceItemDerivationRuleID;
    }
    public String getPriceTotalDerivationRuleID() {
        return priceTotalDerivationRuleID;
    }
    public void setPriceTotalDerivationRuleID(String priceTotalDerivationRuleID) {
        this.priceTotalDerivationRuleID = priceTotalDerivationRuleID;
    }
    public String getPriceDerivationRuleType() {
		return priceDerivationRuleType;
	}
	public void setPriceDerivationRuleType(String priceDerivationRuleType) {
		this.priceDerivationRuleType = priceDerivationRuleType;
	}
	public void setDenomination(String denomValue) {
		this.denomination = denomValue;
	}
	
	public String getDenomination() {
		return this.denomination;
	}
	
	public void setChangeMachineAmount(String cmAmount) {
		this.changeMachineAmount = cmAmount;
	}
	
	public String getChangeMachineAmount() {
		return this.changeMachineAmount;
	}
	
	public void setCashDrawerAmount(String cdAmount) {
		this.cashDrawerAmount = cdAmount;
	}
	
	public String getCashDrawerAmount() {
		return this.cashDrawerAmount;
	}
	
	public void setChangeMachineDrawerTotal(String cmdTotal) {
		this.changeMachineDrawerTotal = cmdTotal;
	}
	
	public String getChangeMachineDrawerTotal() {
		return this.changeMachineDrawerTotal;
	}
	
	public void setTenderCtrlTypePayInOutList(String value) {
		this.tenderCtrlTypePayInOutList = value;
	}
	
	public String getTenderCtrlTypePayInOutList() {
		return this.tenderCtrlTypePayInOutList;
	}
	
	public void setTenderCtrlBalType(String value) {
		this.tenderCtrlBalType = value;
	}
	
	public String getTenderCtrlBalType() {
		return this.tenderCtrlBalType;
	}
	
	public void setDenominationQty(String value) {
		this.denominationQty = value;
	}
	
	public String getDenominationQty() {
		return this.denominationQty;
	}
	
	public void setChangeMachineDenomQty(String qty) {
		this.changeMachineDenomQty = qty;
	}
	
	public String getChangeMachineDenomQty() {
		return this.changeMachineDenomQty;
	}
	
	public void setCashDrawerDenomQty(String qty) {
		this.cashDrawerDenomQty = qty;
	}
	
	public String getCashDrawerDenomQty() {
		return this.cashDrawerDenomQty;
	}
	
	public void setGroupLabel(String lbl) {
		this.groupLabel = lbl;
	}
	
	public String getGroupLabel() {
		return this.groupLabel;
	}
	
	public void setRealAmount(String amt) {
		this.realAmount = amt;
	}
	
	public String getRealAmount() {
		return this.realAmount;
	}
	
	public void setCalculatedAmount(String amt) {
		this.calculatedAmount = amt;
	}
	
	public String getCalculatedAmount() {
		return this.calculatedAmount;
	}
	
	public void setGapAmount(String amt) {
		this.gapAmount = amt;
	}
	
	public String getGapAmount() {
		return this.gapAmount;
	}
	
	public void setDifferenceAmount(String amt) {
		this.differenceAmount = amt;
	}
	
	public String getDifferenceAmount() {
		return this.differenceAmount;
	}
	public void setTenderCtrlType(String type) {
		this.tenderCtrlType = type;
	}
	public String getTenderCtrlType() {
		return this.tenderCtrlType;
	}
	public void setCalculatedAmt2(String amt) {
		this.calculatedAmt2 = amt;
	}
	public String getCalculatedAmt2() {
		return this.calculatedAmt2;
	}
	public String getPayInPlanDenomination() {
		return this.payInPlanDenomination;
	}
	public void setPayInPlanDenomination(String value) {
		this.payInPlanDenomination = value;
	}
	public String getPayInPlanDenomQty() {
		return this.payInPlanDenomQty;
	}
	public void setPayInPlanDenomQty(String value) {
		this.payInPlanDenomQty = value;
	}
	public String getPayInPlanDrawerAmount() {
		return this.payInPlanDrawerAmount;
	}
	public void setPayInPlanDrawerAmount(String value) {
		this.payInPlanDrawerAmount = value;
	}
	
	public String getDetailsDiscountAmountKind() {
	    return this.detailsDiscountAmountKind;
	}
	public void setDetailsDiscountAmountKind(String val) {
	    this.detailsDiscountAmountKind=val;
	}
	public String getDetailsDiscountAmountCnt() {
	    return this.detailsDiscountAmountCnt;
	}
	public void setDetailsDiscountAmountCnt(String val) {
	    this.detailsDiscountAmountCnt=val;
	}
	public String getDetailsDiscountAmountAmt() {
	    return this.detailsDiscountAmountAmt;
	}
	public void setDetailsDiscountAmountAmt(String val) {
	    this.detailsDiscountAmountAmt=val;
	}
	public String getDetailsDiscountRateKind() {
	    return this.detailsDiscountRateKind;
	}
	public void setDetailsDiscountRateKind(String val) {
	    this.detailsDiscountRateKind=val;
	}
	public String getDetailsDiscountRateCnt() {
	    return this.detailsDiscountRateCnt;
	}
	public void setDetailsDiscountRateCnt(String val) {
	    this.detailsDiscountRateCnt=val;
	}
	public String getDetailsDiscountRateAmt() {
	    return this.detailsDiscountRateAmt;
	}
	public void setDetailsDiscountRateAmt(String val) {
	    this.detailsDiscountRateAmt=val;
	}
	public String getSubTotDiscountRateRateKind() {
	    return this.subTotDiscountRateRateKind;
	}
	public void setSubTotDiscountRateRateKind(String val) {
	    this.subTotDiscountRateRateKind=val;
	}
	public String getSubTotDiscountRateRateCnt() {
	    return this.subTotDiscountRateRateCnt;
	}
	public void setSubTotDiscountRateRateCnt(String val) {
	    this.subTotDiscountRateRateCnt=val;
	}
	public String getSubTotDiscountRateRateAmt() {
	    return this.subTotDiscountRateRateAmt;
	}
	public void setSubTotDiscountRateRateAmt(String val) {
	    this.subTotDiscountRateRateAmt=val;
	}
	public String getSubTotDiscountAmountAmountKind() {
	    return this.subTotDiscountAmountAmountKind;
	}
	public void setSubTotDiscountAmountAmountKind(String val) {
	    this.subTotDiscountAmountAmountKind=val;
	}
	public String getSubTotDiscountAmountAmountCnt() {
	    return this.subTotDiscountAmountAmountCnt;
	}
	public void setSubTotDiscountAmountAmountCnt(String val) {
	    this.subTotDiscountAmountAmountCnt=val;
	}
	public String getSubTotDiscountAmountAmountAmt() {
	    return this.subTotDiscountAmountAmountAmt;
	}
	public void setSubTotDiscountAmountAmountAmt(String val) {
	    this.subTotDiscountAmountAmountAmt=val;
	}
	public String getGroupTicketLabel() {
		return this.groupTicketLabel;
	}
	public void setGroupTicketLabel(String str) {
		this.groupTicketLabel = str;
	}
}
