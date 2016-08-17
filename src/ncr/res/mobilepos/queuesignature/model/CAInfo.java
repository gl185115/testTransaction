package ncr.res.mobilepos.queuesignature.model;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Model Class that represents CA Information.
 */
@XmlRootElement(name = "CAInfo")
public class CAInfo {
    /** The Shop Name. */
    private String shopName;
    /**
     * Get the Shop Name.
     * @return  The Shop Name.
     */
    @XmlElement(name = "ShopName")
    public final String getShopName() {
         return shopName;
     }
     /**
      * Set the Shop Name.
      * @param shopnameToSet The Shop Name.
      */
     public final void setShopName(final String shopnameToSet) {
         this.shopName = shopnameToSet;
     }
     /** The Terminal ID. */
     private String terminalId;
     /**
      * Get the Terminal ID.
      * @return The Terminal ID.
      */
     @XmlElement(name = "TerminalId")
     public final String getTerminalId() {
         return terminalId;
     }
     /**
      * Set the Terminal ID.
      * @param terminalidToSet  The Terminal ID.
      */
     public final void setTerminalId(final String terminalidToSet) {
         this.terminalId = terminalidToSet;
     }
     /**
      * The Time of use.
      */
    private String timeOfUse;
    /**
     * Get the Time of use.
     * @return The Time of use.
     */
    @XmlElement(name = "TimeOfUse")
    public final String getTimeOfUse() {
         return timeOfUse;
     }
     /**
      * Set the Time of use.
      * @param timeofuse    The Time of use.
      */
     public final void setTimeOfUse(final String timeofuse) {
         this.timeOfUse = timeofuse;
     }
     /**
     * The Slip Number.
     */
    private int slipNo;
    /**
     * Get the Slip number.
     * @return The Slip Number.
     */
    @XmlElement(name = "SlipNo")
    public final int getSlipNo() {
         return slipNo;
     }
     /**
      * Set the Slip Number.
      * @param slipno   The Slip Number.
      */
     public final void setSlipNo(final int slipno) {
         this.slipNo = slipno;
     }
     /**
      * The Cancel Slip Number.
      */
     private int cancelSlipNo;
     /**
      * Get the Cancel Slip Number.
      * @return The Cancel Slip Number
      */
     @XmlElement(name = "CancelSlipNo")
     public final int getCancelSlipNo() {
         return cancelSlipNo;
     }
     /**
      * Set the Cancel Slip Number.
      * @param cancelslipno The Cancel Slip Number.
      */
     public final void setCancelSlipNo(final int cancelslipno) {
         this.cancelSlipNo = cancelslipno;
     }
     /** The Card number. */
     private String cardNo;
     /**
      * Get the Card Number.
      * @return The Card Number.
      */
     @XmlElement(name = "CardNo")
     public final String getCardNo() {
         return cardNo;
     }
     /**
      * Set the Card Number.
      * @param cardno   The Card Number.
      */
     public final void setCardNo(final String cardno) {
         this.cardNo = cardno;
     }
     /**
      * The Expiration Date.
      */
     private String expireDate;
     /**
      * Get the Expiration Date.
      * @return The Expiration Date.
      */
     @XmlElement(name = "ExpireDate")
     public final String getExpireDate() {
         return expireDate;
     }
     /**
      * Set The Expire Date.
      * @param expiredate   The expire Date.
      */
     public final void setExpireDate(final String expiredate) {
         this.expireDate = expiredate;
     }
     /**
      * Set the Approval Number.
      */
     private String approvalNo;
     /**
      * Get the Approval Number.
      * @return The Approval Number.
      */
     @XmlElement(name = "ApprovalNo")
    public final String getApprovalNo() {
         return this.approvalNo;
     }
     /**
      * Set the Approval Number.
      * @param approvalno   The Approval Number.
      */
     public final void setApprovalNo(final String approvalno) {
         this.approvalNo = approvalno;
     }
     /**
      * The Cancel Division.
      */
     private String cancelDivision;
     /**
      * Get the cancel Division.
      * @return The Cancel Division.
      */
     @XmlElement(name = "CancelDivision")
     public final String getCancelDivision() {
         return cancelDivision;
     }
     /**
      * Set the Cancel Division.
      * @param canceldivision   The Cancel Division.
      */
     public final void setCancelDivision(final String canceldivision) {
         this.cancelDivision = canceldivision;
     }
     /**
      * The Business Division.
      */
     private String businessDivision;
     /**
      * The Business Division.
      * @return The Business Division.
      */
     @XmlElement(name = "BusinessDivision")
    public final String getBusinessDivision() {
         return businessDivision;
     }
     /**
      * Set the Business Division.
      * @param businessdivision The Business Divison.
      */
     public final void setBusinessDivision(final String businessdivision) {
         this.businessDivision = businessdivision;
     }
     /**
      * The Merchandise Code.
      */
     private String merchandiseCode;
     /**
      * The Merchandise code.
      * @return The Merchandise code.
      */
     @XmlElement(name = "MerchandiseCode")
     public final String getMerchandiseCode() {
         return merchandiseCode;
     }
     /**
      *  Set the Merchandise Code.
      * @param merchandisecode The Merchandise Code.
      */
     public final void setMerchandiseCode(final String merchandisecode) {
         this.merchandiseCode = merchandisecode;
     }
     /**
      * The Card Company Code.
      */
     private String cardCompanyCode;
     /**
      * Get the Card Company Code.
      * @return The Card Company Code.
      */
     @XmlElement(name = "CardCompanyCode")
     public final String getCardCompanyCode() {
         return cardCompanyCode;
     }
     /**
      * Set the Card Company Code.
      * @param cardcompanycode  The Card Company Code.
      */
     public final void setCardCompanyCode(final String cardcompanycode) {
         this.cardCompanyCode = cardcompanycode;
     }
     /**
      * The Card Company Name.
      */
     private String cardCompanyName;
     /**
      * Get the Card Company name.
      * @return The Card Company Name.
      */
     @XmlElement(name = "CardCompanyName")
     public final String getCardCompanyName() {
         return cardCompanyName;
     }
     /**
      * Set the Card Company Name.
      * @param cardcompanyname  The Card Company Name.
      */
     public final void setCardCompanyName(final String cardcompanyname) {
         this.cardCompanyName = cardcompanyname;
     }
     /**
      * The Payment Division.
      */
     private int paymentDivision;
     /**
      * Get the Payment Division.
      * @return The Payment Division.
      */
     @XmlElement(name = "PaymentDivision")
     public final int getPaymentDivision() {
         return paymentDivision;
     }
     /**
      * Set the Payment Division.
      * @param paymentdivision The Payment Division.
      */
     public final void setPaymentDivision(final int paymentdivision) {
         this.paymentDivision = paymentdivision;
     }
     /**
      * The InstallmentAmount.
      */
     private double installmentAmount;
     /**
      * Get the Installment Amount.
      * @return The Installment Amount.
      */
     @XmlElement(name = "InstallmentAmount")
     public final double getInstallmentAmount() {
         return this.installmentAmount;
     }
     /**
      * Set the Installment Amount.
      * @param installmentamount The Installment Amount.
      */
     public final void setInstallmentAmount(final double installmentamount) {
         this.installmentAmount = installmentamount;
     }
     /**
      * The Start month.
      */
     private int startMonth;
    /**
     * Get The Start month.
     * @return The Start Month
     */
    @XmlElement(name = "StartMonth")
    public final int getStartMonth() {
         return startMonth;
     }
     /**
      * Set the Start month.
      * @param startmonthToSet The Start Month.
      */
     public final void setStartMonth(final int startmonthToSet) {
         this.startMonth = startmonthToSet;
     }
     /**
      * The Number of bonuses.
      */
     private int numberOfBonuses;
    /**
     * Get the number of Bonuses.
     * @return The Number of Bonuses.
     */
    @XmlElement(name = "NumberOfBonuses")
    public final int getNumberOfBonuses() {
         return numberOfBonuses;
     }
     /**
      * Set the number of bonuses.
      * @param numberofbonusesToSet The number of Bonuses.
      */
     public final void setNumberOfBonuses(final int numberofbonusesToSet) {
         this.numberOfBonuses = numberofbonusesToSet;
     }
     /**
      * The Sum.
      */
     private double sum;
    /**
     * Get the Sum.
     * @return Sum to Set.
     */
    @XmlElement(name = "Sum")
    public final double getSum() {
         return sum;
     }
     /**
      * Set the Sum.
      * @param sumToSet The Sum to set.
      */
     public final void setSum(final double sumToSet) {
         this.sum = sumToSet;
     }
    /**
     * The Tax and Postage.
     */
    private double taxAndPostage;
    /**
     * The Tax and Postage.
     * @return  The Tax and Postage.
     */
    @XmlElement(name = "TaxAndPostage")
    public final double getTaxAndPostage() {
         return taxAndPostage;
     }
     /**
      * Set the Tax and Postage.
      * @param taxandpostageToSet    The Tax and Postage
      */
     public final void setTaxAndPostage(final double taxandpostageToSet) {
         this.taxAndPostage = taxandpostageToSet;
     }
    /**
     * The Roman Name.
     */
    private String romanName;
    /**
     * Get the Roman Name.
     * @return The Roman Name.
     */
    @XmlElement(name = "RomanName")
    public final String getRomanName() {
         return romanName;
     }
     /**
      * Set the Roman Name.
      * @param romanname    The Roman Name
      */
     public final void setRomanName(final String romanname) {
         this.romanName = romanname;
     }
     /**
      * The Cup Number.
      */
     private int cupNo;
    /**
     * Get the Cup Number.
     * @return  The Cup Number.
     */
    @XmlElement(name = "CupNo")
    public final int getCupNo() {
         return this.cupNo;
     }
     /**
      * Set the Cup Number.
      * @param cupno    The Cup Number
      */
     public final void setCupNo(final int cupno) {
         this.cupNo = cupno;
     }
     /**
      * The CupSendDate.
      */
     private String cupSendDate;
    /**
     * Get the CupSendDate.
     * @return  The CupSendDate
     */
    @XmlElement(name = "CupSendDate")
    public final String getCupSendDate() {
         return this.cupSendDate;
     }
     /**
      * Set the Cup Send Date.
      * @param cupsenddateToSet The CupSendDate
      */
     public final void setCupSendDate(final String cupsenddateToSet) {
         this.cupSendDate = cupsenddateToSet;
     }
    /** The Sign. */
    private String sign;
    /**
     * Get the Sign.
     * @return  The sign
     */
    @XmlElement(name = "Sign")
    public final String getSign() {
         return sign;
     }
     /**
      * Set the sign.
      * @param signToSet The sign.
      */
     public final void setSign(final String signToSet) {
         this.sign = signToSet;
     }
     /**
      * The status.
      */
     private int status = 0;
     /**
      * Get the Status.
      * @return The Status.
      */
     public final int getStatus() {
         return status;
     }
     /**
      * Set the Status.
      * @param statusToSet The Status
      */
     public final void setStatus(final int statusToSet) {
          this.status = statusToSet;
     }

     @Override
     public final String toString() {
        StringBuilder str = new StringBuilder();
        String dlmtr = "; ";
        str.append("ApprovalNumber: ").append(this.approvalNo).append(dlmtr)
           .append("BusinessDivision: ").append(this.businessDivision)
           .append(dlmtr)
           .append("CancelDivision: ").append(this.cancelDivision)
           .append(dlmtr)
           .append("CancelSlipNumber: ").append(this.cancelSlipNo).append(dlmtr)
           .append("CardCompanyCode: ").append(this.cardCompanyCode)
           .append(dlmtr)
           .append("CardCompanyName: ").append(this.cardCompanyName)
           .append(dlmtr)
           .append("CardNumber: ").append(this.cardNo).append(dlmtr)
           .append("CupNumber: ").append(this.cupNo).append(dlmtr)
           .append("CupSendDate: ").append(this.cupSendDate).append(dlmtr)
           .append("ExpireDate: ").append(this.expireDate).append(dlmtr)
           .append("InstallmentAmount: ").append(this.installmentAmount)
           .append(dlmtr)
           .append("MerchandiseCode: ").append(this.merchandiseCode)
           .append(dlmtr)
           .append("NumberOfBonuses: ").append(this.numberOfBonuses)
           .append(dlmtr)
           .append("PaymentDivision: ").append(this.paymentDivision)
           .append(dlmtr)
           .append("RomanName: ").append(this.romanName).append(dlmtr)
           .append("ShopName: ").append(this.shopName).append(dlmtr)
           .append("Sign: ").append(this.sign).append(dlmtr)
           .append("SlipNumber: ").append(this.slipNo).append(dlmtr)
           .append("StartMonth: ").append(this.startMonth).append(dlmtr)
           .append("Status: ").append(this.status).append(dlmtr)
           .append("Sum: ").append(this.sum).append(dlmtr)
           .append("TaxAndPostage: ").append(this.taxAndPostage).append(dlmtr)
           .append("TerminalID: ").append(this.terminalId).append(dlmtr)
           .append("TimeOfUse: ").append(this.timeOfUse).append(dlmtr);
        return str.toString();
     }
}
