package ncr.res.ue.message.action;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.ue.exception.MessageException;
import ncr.res.ue.message.MessageTypes;
import ncr.res.ue.message.OutgoingMessage;
import ncr.res.ue.message.TransportHeader;

/**
 * GS1_Coupon action message generator.
 * @author RD185102
 */

@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "GSOneCoupon")
public class GSOneCoupon extends OutgoingMessage {
    /**
     * Unique identifier for the GS1_COUPON within the transaction.
     */
    @XmlElement(name = "EntryID")
    private String entryId = "";
    /**
     * Apply Flag.
     */
    @XmlElement(name = "ApplyFlag")
    private int applyFlag;
    /**
     * Application Identifier. The id (always 8110).
     */
    @XmlElement(name = "ApplicationID")
    private String applicationId = "8110";
    /**
     * Variable length indicator have a value in range of 0-6.
     */
    @XmlElement(name = "PrimaryGs1CompanyPrefixVli")
    private int primGs1CompPrefVli;
    /**
     *    Primary GS1 Company Prefix.
     */
    @XmlElement(name = "PrimaryGs1CompanyPrefix")
    private String primGs1CompPref = "";
    /**
     * Offer Code.
     */
    @XmlElement(name = "OfferCode")
    private String offerCode = "";
    /**
     * Save Value VLI.
     */
    @XmlElement(name = "SaveValueVli")
    private int saveValueVli;
    /**
     * Save Value.
     */
    @XmlElement(name = "SaveValue")
    private String saveValue = "";
    /**
     * Primary Purchase Requirement VLI.
     */
    @XmlElement(name = "PrimaryPurchaseRequirementVli")
    private int prmPrchseReqVli;
    /**
     * Primary Purchase Requirement.
     */
    @XmlElement(name = "PrimaryPurchaseRequirement")
    private String primPrchseReq = "";
    /**
     * Primary Purchase Requirement Code.
     */
    @XmlElement(name = "PrimaryPurchaseRequirementCode")
    private int primPrchseReqCode;
    /**
     * Primary Purchase Family Code.
     */
    @XmlElement(name = "PrimaryPurchaseFamilyCode")
    private String primPrchseFmlyCode = "";
    /**
     * Second Qualifying Purchase.
     */
    @XmlElement(name = "SecondQualifyingPurchase")
    private int scndQualifyingPrchse;
    /**
     * Additional Purchase Rules Code.
     */
    @XmlElement(name = "AdditionalPurchaseRulesCode")
    private int additionalPrchseRulesCode;
    /**
     * 2nd Purchase Requirement VLI.
     */
    @XmlElement(name = "SecondPurchaseRequirementVli")
    private int scndPrchseReqVli;
    /**
     * 2nd Purchase Requirement.
     */
    @XmlElement(name = "SecondPurchaseRequirement")
    private String scndPrchseReq = "";
    /**
     * 2nd Purchase Requirement Code.
     */
    @XmlElement(name = "SecondPurchaseRequirementCode")
    private int scndPrchseReqCode;
    /**
     * 2nd purchase family code.
     */
    @XmlElement(name = "SecondPurchaseFamilyCode")
    private String scndPurchseFmlyCode = "";
    /**
     * 2nd purchase gs1 company prefix vli.
     */
    @XmlElement(name = "SecondPurchaseGs1CompanyPrefixVli")
    private int scndPrchseGs1CmpnyPrefVli;
    /**
     * 2nd GS1 Company Prefix.
     */
    @XmlElement(name = "SecondGs1CompanyPrefix")
    private String scndGs1CmpnyPref = "";
    /**
     * 3rd qualifying purchase.
     */
    @XmlElement(name = "ThirdQualifyingPurchase")
    private int thrdQualifyingPrchse;
    /**
     * 3rd Purchase requirement VLI.
     */
    @XmlElement(name = "ThirdPurchaseRequirementVli")
    private int thrdPrchseReqVli;
    /**
     * 3rd Purchase requirement.
     */
    @XmlElement(name = "ThirdPurchaseRequirement")
    private String thrdPrchseReq = "";
    /**
     * 3rd purchase requirement code.
     */
    @XmlElement(name = "ThirdPurchaseRequirementCode")
    private int thrdPrchseReqCode;
    /**
     * 3rd purchase family code.
     */
    @XmlElement(name = "ThirdPurchaseFamilyCode")
    private String thrdPrchseFmlyCode = "";
    /**
     * 3rd purchase gs1 company prefix vli.
     */
    @XmlElement(name = "ThirdPurchaseGs1CompanyPrefixVli")
    private int thrdPrchseGs1CmpnyPrefVli;
    /**
     * 3rd GS1 Company Prefix.
     */
    @XmlElement(name = "ThirdGs1CompanyPrefix")
    private String thrdGs1CmpnyPref = "";
    /**
     * Expiration date indicator.
     */
    @XmlElement(name = "ExpirationDateIndicator")
    private int expDateIndicator;
    /**
     * Expiration date.
     */
    @XmlElement(name = "ExpirationDate")
    private String expDate;
    /**
     * Start Date Indicator.
     */
    @XmlElement(name = "StartDateIndicator")
    private int startDateIndicator;
    /**
     * Start Date.
     */
    @XmlElement(name = "StartDate")
    private String startDate;
    /**
     * Serial Number Indicator.
     */
    @XmlElement(name = "SerialNumberIndicator")
    private int serialNumIndicator;
    /**
     * Serial Number VLI.
     */
    @XmlElement(name = "SerialNumberVli")
    private int serialNumVli;
    /**
     * Serial Number.
     */
    @XmlElement(name = "SerialNumber")
    private String serialNum = "";
    /**
     * Retailer Identification.
     */
    @XmlElement(name = "RetailerID")
    private int retailerId;
    /**
     * Retailer GS1 Company Prefix or GLN VLI.
     */
    @XmlElement(name = "RetailerGs1CompanyPrefixGlnVli")
    private int retailerGs1CmpnyPrefGlnVli;
    /**
     * Retailer GS1 Company Prefix or GLN.
     */
    @XmlElement(name = "RetailerGs1CompanyPrefixGln")
    private String retailerGs1CmpnyPrefGln = "";
    /**
     * Miscellaneous Elements.
     */
    @XmlElement(name = "MiscellaneousElements")
    private int miscElements;
    /**
     * Save Value Code.
     */
    @XmlElement(name = "SaveValueCode")
    private int saveValueCode;
    /**
     * Save Value Applies To Which Item.
     */
    @XmlElement(name = "SaveValue")
    private int saveValAppliesToWchItem;
    /**
     * Store Coupon Flag.
     */
    @XmlElement(name = "StoreCouponFlag")
    private int storeCouponFlag;
    /**
     * Do Not Multiply Flag.
     */
    @XmlElement(name = "DoNotMultiplyFlag")
    private int doNotMultiplyFlag;
    /**
     * Priority used by store coupons.
     */
    @XmlElement(name = "Priority")
    private String priority = "";
      /**
       * The main constructor for GS1 Coupon.
       * @param newEntryId     Unique identifier for the
       *                    GS1_COUPON within the transaction.
       * @param newApplyFlag   Apply Flag.
       * @param newApplicationId   Application Identifier.
       * @param newPrimGs1CompPrefVli  Primary GS1 Company Prefix VLI.
       * @param newPrimGs1CompPref     Primary GS1 Company Prefix.
       * @param newOfferCode           Offer Code.
       * @param newSaveValueVli        Save Value VLI.
       * @param newSaveValue           Save Value.
       * @param newPrimPrchseReqVli    Primary Purchase Requirement VLI.
       * @param newPrimPrchseReq       Primary Purchase Requirement.
       * @param newPrimPrchseReqCode   Primary Purchase Requirement Code.
       * @param newPrimPrchseFmlyCode  Primary Purchase Family Code.
       * @param newScndQualifyingPrchse    Second Qualifying Purchase.
       * @param newAdditionalPrchseRulesCode   Additional Purchase Rules Code.
       * @param newScndPrchseReqVli    Second Purchase Requirement VLI.
       * @param newScndPrchseReq       Second Purchase Requirement.
       * @param newScndPrchseReqCode   Second Purchase Requirement Code.
       * @param newScndPurchseFmlyCode Second Purchase Family Code.
       * @param newScndPrchseGs1CmpnyPrefVli
       *            Second Purchase GS1 Company Prefix VLI.
       * @param newScndGs1CmpnyPref    Second Purchase GS1 Company Prefix.
       * @param newThrdQualifyingPrchse    Third Qualifying Purchase.
       * @param newThrdPrchseReqVli    Third Purchase Requirement VLI.
       * @param newThrdPrchseReq   Third Purchase Requirement.
       * @param newThrdPrchseReqCode   Third Purchase Requirement Code.
       * @param newThrdPrchseFmlyCode  Third Purchase Family Code.
       * @param newThrdPrchseGs1CmpnyPrefVli
       *            Third Purchase GS1 Company Prefix VLI.
       * @param newThrdGs1CmpnyPref    Third GS1 Company Prefix.
       * @param newExpDateIndicator    Expiration Date Indicator.
       * @param newExpDate     Expiration Date.
       * @param newStartDateIndicator  Start Date Indicator.
       * @param newStartDate   Start Date.
       * @param newSerialNumIndicator  Serial Number Indicator.
       * @param newSerialNumVli    Serial Number VLI.
       * @param newSerialNum   Serial Number.
       * @param newRetailerId  Retailer Identification.
       * @param newRetailerGs1CmpnyPrefGlnVli
       *            Retailer GS1 Company Prefix GLN VLI.
       * @param newRetailerGs1CmpnyPrefGln     Retailer GS1 Company Prefix GLN.
       * @param newMiscElements    Miscellaneous Elements.
       * @param newSaveValueCode   Save Value Code.
       * @param newSaveValAppliesToWchItem
       *            Save Value  Applies to Which Item.
       * @param newStoreCouponFlag    Store Coupon Flag.
       * @param newDoNotMultiplyFlag  Multiply Flag.
       * @param newPriority        Priority used by store coupons.
       */
      public GSOneCoupon(final String newEntryId, final int newApplyFlag,
            final String newApplicationId, final int newPrimGs1CompPrefVli,
            final String newPrimGs1CompPref, final String newOfferCode,
            final int newSaveValueVli, final String newSaveValue,
            final int newPrimPrchseReqVli, final String newPrimPrchseReq,
            final int newPrimPrchseReqCode, final String newPrimPrchseFmlyCode,
            final int newScndQualifyingPrchse,
            final int newAdditionalPrchseRulesCode,
            final int newScndPrchseReqVli,
            final String newScndPrchseReq, final int newScndPrchseReqCode,
            final String newScndPurchseFmlyCode,
            final int newScndPrchseGs1CmpnyPrefVli,
            final String newScndGs1CmpnyPref,
            final int newThrdQualifyingPrchse, final int newThrdPrchseReqVli,
            final String newThrdPrchseReq, final int newThrdPrchseReqCode,
            final String newThrdPrchseFmlyCode,
            final int newThrdPrchseGs1CmpnyPrefVli,
            final String newThrdGs1CmpnyPref,
            final int newExpDateIndicator, final String newExpDate,
            final int newStartDateIndicator, final String newStartDate,
            final int newSerialNumIndicator, final int newSerialNumVli,
            final String newSerialNum, final int newRetailerId,
            final int newRetailerGs1CmpnyPrefGlnVli,
            final String newRetailerGs1CmpnyPrefGln, final int newMiscElements,
            final int newSaveValueCode, final int newSaveValAppliesToWchItem,
            final int newStoreCouponFlag, final int newDoNotMultiplyFlag,
            final String newPriority) {

          super(MessageTypes.GS1_COUPON);
          this.entryId = newEntryId;
          this.applyFlag  = newApplyFlag;
          this.applicationId = newApplicationId;
          this.primGs1CompPrefVli = newPrimGs1CompPrefVli;
          this.primGs1CompPref = newPrimGs1CompPref;
          this.offerCode = newOfferCode;
          this.saveValueVli = newSaveValueVli;
          this.saveValue = newSaveValue;
          this.prmPrchseReqVli = newPrimPrchseReqVli;
          this.primPrchseReq = newPrimPrchseReq;
          this.primPrchseReqCode = newPrimPrchseReqCode;
          this.primPrchseFmlyCode = newPrimPrchseFmlyCode;
          this.scndQualifyingPrchse = newScndQualifyingPrchse;
          this.additionalPrchseRulesCode = newAdditionalPrchseRulesCode;
          this.scndPrchseReqVli = newScndPrchseReqVli;
          this.scndPrchseReq = newScndPrchseReq;
          this.scndPrchseReqCode = newScndPrchseReqCode;
          this.scndPurchseFmlyCode = newScndPurchseFmlyCode;
          this.scndPrchseGs1CmpnyPrefVli = newScndPrchseGs1CmpnyPrefVli;
          this.scndGs1CmpnyPref = newScndGs1CmpnyPref;
          this.thrdQualifyingPrchse = newThrdQualifyingPrchse;
          this.thrdPrchseReqVli = newThrdPrchseReqVli;
          this.thrdPrchseReq = newThrdPrchseReq;
          this.thrdPrchseReqCode = newThrdPrchseReqCode;
          this.thrdPrchseFmlyCode = newThrdPrchseFmlyCode;
          this.thrdPrchseGs1CmpnyPrefVli = newThrdPrchseGs1CmpnyPrefVli;
          this.thrdGs1CmpnyPref = newThrdGs1CmpnyPref;
          this.expDateIndicator = newExpDateIndicator;
          this.expDate = newExpDate;
          this.startDateIndicator = newStartDateIndicator;
          this.startDate = newStartDate;
          this.serialNumIndicator = newSerialNumIndicator;
          this.serialNumVli = newSerialNumVli;
          this.serialNum = newSerialNum;
          this.retailerId = newRetailerId;
          this.retailerGs1CmpnyPrefGlnVli = newRetailerGs1CmpnyPrefGlnVli;
          this.retailerGs1CmpnyPrefGln = newRetailerGs1CmpnyPrefGln;
          this.miscElements = newMiscElements;
          this.saveValueCode = newSaveValueCode;
          this.saveValAppliesToWchItem = newSaveValAppliesToWchItem;
          this.storeCouponFlag = newStoreCouponFlag;
          this.doNotMultiplyFlag = newDoNotMultiplyFlag;
          this.priority = newPriority;
      }

    @Override
    public final String createMessage(final String termId,
            final String transactionId)
            throws MessageException {
        StringBuffer sb = new StringBuffer();
        sb.append(String.format("%02d", this.getMessageHeader()))
        .append(String.format("%6s" , entryId))
        .append(String.format("%1d" , applyFlag))
        .append(String.format("%4s" , applicationId))
        .append(String.format("%1d" , primGs1CompPrefVli))
        .append(String.format("%12s" , primGs1CompPref))
        .append(String.format("%6s" , offerCode))
        .append(String.format("%1d", saveValueVli))
        .append(String.format("%5s", saveValue))
        .append(String.format("%1d", prmPrchseReqVli))
        .append(String.format("%5s", primPrchseReq))
        .append(String.format("%1d", primPrchseReqCode))
        .append(String.format("%3s", primPrchseFmlyCode))
        .append(String.format("%1d", scndQualifyingPrchse))
        .append(String.format("%1d", additionalPrchseRulesCode))
        .append(String.format("%1d", scndPrchseReqVli))
        .append(String.format("%5s", scndPrchseReq))
        .append(String.format("%1d", scndPrchseReqCode))
        .append(String.format("%3s", scndPurchseFmlyCode))
        .append(String.format("%1d", scndPrchseGs1CmpnyPrefVli))
        .append(String.format("%12s", scndGs1CmpnyPref))
        .append(String.format("%1d", thrdQualifyingPrchse))
        .append(String.format("%1d", thrdPrchseReqVli))
        .append(String.format("%5s", thrdPrchseReq))
        .append(String.format("%1d", thrdPrchseReqCode))
        .append(String.format("%3s", thrdPrchseFmlyCode))
        .append(String.format("%1d", thrdPrchseGs1CmpnyPrefVli))
        .append(String.format("%12s", thrdGs1CmpnyPref))
        .append(String.format("%1d", expDateIndicator))
        .append(String.format("%6s", expDate))
        .append(String.format("%1d", startDateIndicator))
        .append(String.format("%6s", startDate))
        .append(String.format("%1d" , serialNumIndicator))
        .append(String.format("%1d", serialNumVli))
        .append(String.format("%15s", serialNum))
        .append(String.format("%1d", retailerId))
        .append(String.format("%1d", retailerGs1CmpnyPrefGlnVli))
        .append(String.format("%13s", retailerGs1CmpnyPrefGln))
        .append(String.format("%1d", miscElements))
        .append(String.format("%1d", saveValueCode))
        .append(String.format("%1d", saveValAppliesToWchItem))
        .append(String.format("%1d", storeCouponFlag))
        .append(String.format("%1d", doNotMultiplyFlag))
        .append(String.format("%4s", priority))
        .append(OutgoingMessage.MESSAGE_TERMINATOR);

        String fullMessage = TransportHeader.create(sb.toString(), termId,
                transactionId);
        fullMessage += sb.toString();

        return fullMessage;
    }
    /**
     * Setter for Entry ID.
     * @param newEntryId    Entry ID.
     */
    public final void setEntryId(final String newEntryId) {
        this.entryId = newEntryId;
    }
    /**
     * Getter for Entry ID.
     * @return The entry ID.
     */
    public final String getEntryId() {
        return entryId;
    }
    /**
     * Setter for Apply Flag.
     * @param newApplyFlag  Apply flag to set.
     */
    public final void setApplyFlag(final int newApplyFlag) {
        this.applyFlag  = newApplyFlag;
    }
    /**
     * Getter for Apply flag.
     * @return The apply flag.
     */
    public final int getApplyFlag() {
        return applyFlag;
    }
    /**
     * Setter for Application ID.
     * @param newApplicationId Application ID to set.
     */
    public final void setApplicationId(final String newApplicationId) {
        this.applicationId = newApplicationId;
    }
    /**
     * Getter for Application ID.
     * @return The Application ID.
     */
    public final String getApplicationId() {
        return applicationId;
    }
    /**
     * Setter for Primary GS Company Prefix Vli.
     * @param newPrimGs1CompPrefVli Primary GS1 Company Prefix Vli to set.
     */
    public final void setPrimGs1CompPrefVli(final int newPrimGs1CompPrefVli) {
        this.primGs1CompPrefVli = newPrimGs1CompPrefVli;
    }
    /**
     * Getter for Primary Gs1 Company Prefix Vli.
     * @return The Primary Gs1 Company Prefix Vli.
     */
    public final int getPrimGs1CompPrefVli() {
        return primGs1CompPrefVli;
    }
    /**
     * Setter for Primary GS1 Company Prefix.
     * @param newPrimGs1CompPref    Primary GS1 Company Prefix to set.
     */
    public final void setPrimGs1CompPref(final String newPrimGs1CompPref) {
        this.primGs1CompPref = newPrimGs1CompPref;
    }
    /**
     * Getter for Primary GS1 Company Prefix.
     * @return The Primary GS1 Company Prefix.
     */
    public final String getPrimGs1CompPref() {
        return primGs1CompPref;
    }
    /**
     * Setter for Offer Code.
     * @param newOfferCode  The Offer Code to set.
     */
    public final void setOfferCode(final String newOfferCode) {
        this.offerCode = newOfferCode;
    }
    /**
     * Getter for Offer Code.
     * @return The Offer Code.
     */
    public final String getOfferCode() {
        return offerCode;
    }
    /**
     * Setter for Save Value Vli.
     * @param newSaveValueVli The Save Value Vli to set.
     */
    public final void setSaveValueVli(final int newSaveValueVli) {
        this.saveValueVli = newSaveValueVli;
    }
    /**
     * Getter for Save Value Vli.
     * @return The Save Value Vli.
     */
    public final int getSaveValueVli() {
        return saveValueVli;
    }
    /**
     * Setter for Save Value.
     * @param newSaveValue The Save Value to set.
     */
    public final void setSaveValue(final String newSaveValue) {
        this.saveValue = newSaveValue;
    }
    /**
     * Getter for Save Value.
     * @return The Save Value.
     */
    public final String getSaveValue() {
        return saveValue;
    }
    /**
     * Setter for Primary Purchase Requirement Vli.
     * @param newPrimPrchseReqVli The Primary Purchase Requirement Vli to set.
     */
    public final void setPrimPrchseReqVli(final int newPrimPrchseReqVli) {
        this.prmPrchseReqVli = newPrimPrchseReqVli;
    }
    /**
     * Getter for Primary Purchase Requirement Vli.
     * @return  The Primary Purchase Requirement Vli.
     */
    public final int getPrimPrchseReqVli() {
        return prmPrchseReqVli;
    }
    /**
     * Setter for Primary Purchase Requirement.
     * @param newPrimPrchseReq The Primary Purchase Requirement to set.
     */
    public final void setPrimPrchseReq(final String newPrimPrchseReq) {
        this.primPrchseReq = newPrimPrchseReq;
    }
    /**
     * Getter for Primary Purchase Requirement.
     * @return The Primary Purchase Requirement.
     */
    public final String getPrimPrchseReq() {
        return primPrchseReq;
    }
    /**
     * Setter for Primary Purchase Requirement Code.
     * @param newPrimPrchseReqCode The Primary Purchase Requirement Code to set.
     */
    public final void setPrimPrchseReqCode(final int newPrimPrchseReqCode) {
        this.primPrchseReqCode = newPrimPrchseReqCode;
    }
    /**
     * Getter for Primary Purchase Requirement Code.
     * @return The Primary Purchase Requirement Code.
     */
    public final int getPrimPrchseReqCode() {
        return primPrchseReqCode;
    }
    /**
     * Setter for Primary Purchase Family Code.
     * @param newPrimPrchseFmlyCode The Primary Purchase Family Code to set.
     */
    public final void setPrimPrchseFmlyCode(
            final String newPrimPrchseFmlyCode) {
        this.primPrchseFmlyCode = newPrimPrchseFmlyCode;
    }
    /**
     * Getter for Primary Purchase Family Code.
     * @return The Primary Purchase Family Code.
     */
    public final String getPrimPrchseFmlyCode() {
        return primPrchseFmlyCode;
    }
    /**
     * Setter for Second Qualifying Purchase.
     * @param newScndQualifyingPrchse The Second Qualifying Purchase to set.
     */
    public final void setScndQualifyingPrchse(
            final int newScndQualifyingPrchse) {
        this.scndQualifyingPrchse = newScndQualifyingPrchse;
    }
    /**
     * Getter for Second Qualifying Purchase.
     * @return The Second Qualifying Purchase.
     */
    public final int getScndQualifyingPrchse() {
        return scndQualifyingPrchse;
    }
    /**
     * Setter for Additional Purchase Rules Code.
     * @param newAdditionalPrchseRulesCode
     *          The Additional Purchase Rules Code to set.
     */
    public final void setAdditionalPrchseRulesCode(
            final int newAdditionalPrchseRulesCode) {
        this.additionalPrchseRulesCode = newAdditionalPrchseRulesCode;
    }
    /**
     * Getter for Additional Purchase Rules Code.
     * @return The Additional Purchase Rules Code.
     */
    public final int getAdditionalPrchseRulesCode() {
        return additionalPrchseRulesCode;
    }
    /**
     * Setter for Second Purchase Requirement Vli.
     * @param newScndPrchseReqVli   The Second Purchase Requirement Vli to set.
     */
    public final void setScndPrchseReqVli(final int newScndPrchseReqVli) {
        this.scndPrchseReqVli = newScndPrchseReqVli;
    }
    /**
     * Getter for Second Purchase Requirement Vli.
     * @return The Second Purchase Requirement Vli.
     */
    public final int getScndPrchseReqVli() {
        return scndPrchseReqVli;
    }
    /**
     * Setter for Second Purchase Requirement.
     * @param newScndPrchseReq  The Second Purchase Requirement to set.
     */
    public final void setScndPrchseReq(final String newScndPrchseReq) {
        this.scndPrchseReq = newScndPrchseReq;
    }
    /**
     * Getter for Second Purchase Requirement.
     * @return The Second Purchase Requirement.
     */
    public final String getScndPrchseReq() {
        return scndPrchseReq;
    }
    /**
     * Setter for Second Purchase Requirement Code.
     * @param newScndPrchseReqCode  The Second Purchase Requirement Code to set.
     */
    public final void setScndPrchseReqCode(final int newScndPrchseReqCode) {
        this.scndPrchseReqCode = newScndPrchseReqCode;
    }
    /**
     * Getter for Second Purchase Requirement Code.
     * @return The Second Purchase Requirement Code.
     */
    public final int getScndPrchseReqCode() {
        return scndPrchseReqCode;
    }
    /**
     * Setter for Second Purchase Family Code.
     * @param newScndPurchseFmlyCode The Second Purchase Family Code to set.
     */
    public final void setScndPurchseFmlyCode(
            final String newScndPurchseFmlyCode) {
        this.scndPurchseFmlyCode = newScndPurchseFmlyCode;
    }
    /**
     * Getter for Second Purchase Family Code.
     * @return The Second Purchase Family Code.
     */
    public final String getScndPurchseFmlyCode() {
        return scndPurchseFmlyCode;
    }
    /**
     * Setter for Second Purchase GS1 Company Prefix Vli.
     * @param newScndPrchseGs1CmpnyPrefVli
     *      The Second Purchase GS1 Company Prefix Vli to set.
     */
    public final void setScndPrchseGs1CmpnyPrefVli(
            final int newScndPrchseGs1CmpnyPrefVli) {
        this.scndPrchseGs1CmpnyPrefVli = newScndPrchseGs1CmpnyPrefVli;
    }
    /**
     * Getter for Second Purchase GS1 Company Prefix Vli.
     * @return The Second Purchase GS1 Company Prefix Vli to set.
     */
    public final int getScndPrchseGs1CmpnyPrefVli() {
        return scndPrchseGs1CmpnyPrefVli;
    }
    /**
     * Setter for Second GS1 Company Prefix.
     * @param newScndGs1CmpnyPref The Second GS1 Company Prefix to set.
     */
    public final void setScndGs1CmpnyPref(final String newScndGs1CmpnyPref) {
        this.scndGs1CmpnyPref = newScndGs1CmpnyPref;
    }
    /**
     * Getter for Second GS1 Company Prefix.
     * @return The Second GS1 Company Prefix.
     */
    public final String getScndGs1CmpnyPref() {
        return scndGs1CmpnyPref;
    }
    /**
     * Setter for Third Qualifying Purchase.
     * @param newThrdQualifyingPrchse The Third Qualifying Purchase to set.
     */
    public final void setThrdQualifyingPrchse(
            final int newThrdQualifyingPrchse) {
        this.thrdQualifyingPrchse = newThrdQualifyingPrchse;
    }
    /**
     * Getter for Third Qualifying Purchase.
     * @return The Third Qualifying Purchase.
     */
    public final int getThrdQualifyingPrchse() {
        return thrdQualifyingPrchse;
    }
    /**
     * Setter for Third Purchase Requirement Vli.
     * @param newThrdPrchseReqVli   The Third Purchase Requirement Vli to set.
     */
    public final void setThrdPrchseReqVli(final int newThrdPrchseReqVli) {
        this.thrdPrchseReqVli = newThrdPrchseReqVli;
    }
    /**
     * Getter for Third Purchase Requirement Vli.
     * @return The Third Purchase Requirement Vli.
     */
    public final int getThrdPrchseReqVli() {
        return thrdPrchseReqVli;
    }
    /**
     * Setter for Third Purchase Requirement.
     * @param newThrdPrchseReq  The Third Purchase Requirement to set.
     */
    public final void setThrdPrchseReq(final String newThrdPrchseReq) {
        this.thrdPrchseReq = newThrdPrchseReq;
    }
    /**
     * Getter for Third Purchase Requirement.
     * @return The Third Purchase Requirement.
     */
    public final String getThrdPrchseReq() {
        return thrdPrchseReq;
    }
    /**
     * Setter for Third Purchase Requirement Code.
     * @param newThrdPrchseReqCode  The Third Purchase Requirement Code to set.
     */
    public final void setThrdPrchseReqCode(final int newThrdPrchseReqCode) {
        this.thrdPrchseReqCode = newThrdPrchseReqCode;
    }
    /**
     * Getter for Third Purchase Requirement Code.
     * @return The Third Purchase Requirement Code.
     */
    public final int getThrdPrchseReqCode() {
        return thrdPrchseReqCode;
    }
    /**
     * Setter for Third Purchase Family Code.
     * @param newThrdPrchseFmlyCode The Third Purchase Family Code to set.
     */
    public final void setThrdPrchseFmlyCode(
            final String newThrdPrchseFmlyCode) {
        this.thrdPrchseFmlyCode = newThrdPrchseFmlyCode;
    }
    /**
     * Getter for Third Purchase Family Code.
     * @return The Third Purchase Family Code.
     */
    public final String getThrdPrchseFmlyCode() {
        return thrdPrchseFmlyCode;
    }
    /**
     * Setter for Third Purchase GS1 Company Prefix Vli.
     * @param newThrdPrchseGs1CmpnyPrefVli
     *          The Third Purchase GS1 Company Prefix Vli to set.
     */
    public final void setThrdPrchseGs1CmpnyPrefVli(
            final int newThrdPrchseGs1CmpnyPrefVli) {
        this.thrdPrchseGs1CmpnyPrefVli = newThrdPrchseGs1CmpnyPrefVli;
    }
    /**
     * Getter for Third Purchase GS1 Company Prefix Vli.
     * @return The Third Purchase GS1 Company Prefix Vli.
     */
    public final int getThrdPrchseGs1CmpnyPrefVli() {
        return thrdPrchseGs1CmpnyPrefVli;
    }
    /**
     * Setter for Third Purchase GS1 Company Prefix.
     * @param newThrdGs1CmpnyPref
     *          The Third Purchase GS1 Company Prefix to set.
     */
    public final void setThrdGs1CmpnyPref(final String newThrdGs1CmpnyPref) {
        this.thrdGs1CmpnyPref = newThrdGs1CmpnyPref;
    }
    /**
     * Getter for Third Purchase GS1 Company Prefix.
     * @return The Third Purchase GS1 Company Prefix.
     */
    public final String getThrdGs1CmpnyPref() {
        return thrdGs1CmpnyPref;
    }
    /**
     * Setter for Expiration Date Indicator.
     * @param newExpDateIndicator   The Expiration Date Indicator to set.
     */
    public final void setExpDateIndicator(final int newExpDateIndicator) {
        this.expDateIndicator = newExpDateIndicator;
    }
    /**
     * Getter for Expiration Date Indicator.
     * @return The Expiration Date Indicator.
     */
    public final int getExpDateIndicator() {
        return expDateIndicator;
    }
    /**
     * Setter for Expiration Date.
     * @param newExpDate    Expiration Date to set.
     */
    public final void setExpDate(final String newExpDate) {
        this.expDate = newExpDate;
    }
    /**
     * Getter for Expiration Date.
     * @return The Expiration Date.
     */
    public final String getExpDate() {
        return expDate;
    }
    /**
     * Setter for Start Date Indicator.
     * @param newStartDateIndicator The Start Date Indicator to set.
     */
    public final void setStartDateIndicator(final int newStartDateIndicator) {
        this.startDateIndicator = newStartDateIndicator;
    }
    /**
     * Getter for Start Date Indicator.
     * @return The Start Date Indicator.
     */
    public final int getStartDateIndicator() {
        return startDateIndicator;
    }
    /**
     * Setter for Start Date.
     * @param newStartDate  Start Date to set.
     */
    public final void setStartDate(final String newStartDate) {
        this.startDate = newStartDate;
    }
    /**
     * Getter for Start Date.
     * @return The Start Date.
     */
    public final String getStartDate() {
        return startDate;
    }
    /**
     * Setter for Serial Number Indicator.
     * @param newSerialNumIndicator The Serial Number Indicator to set.
     */
    public final void setSerialNumIndicator(final int newSerialNumIndicator) {
        this.serialNumIndicator = newSerialNumIndicator;
    }
    /**
     * Getter for Serial Number Indicator.
     * @return The Serial Number Indicator.
     */
    public final int getSerialNumIndicator() {
        return serialNumIndicator;
    }
    /**
     * Setter for Serial Number Vli.
     * @param newSerialNumVli   The Serial Number Vli to set.
     */
    public final void setSerialNumVli(final int newSerialNumVli) {
        this.serialNumVli = newSerialNumVli;
    }
    /**
     * Getter for Serial Number Vli.
     * @return The Serial Number Vli.
     */
    public final int getSerialNumVli() {
        return serialNumVli;
    }
    /**
     * Setter for Serial Number.
     * @param newSerialNum  The Serial Number to set.
     */
    public final void setSerialNum(final String newSerialNum) {
        this.serialNum = newSerialNum;
    }
    /**
     * Getter for Serial Number.
     * @return The Serial Number.
     */
    public final String getSerialNum() {
        return serialNum;
    }
    /**
     * Setter for Retailer ID.
     * @param newRetailerId The Retailer ID to set.
     */
    public final void setRetailerId(final int newRetailerId) {
        this.retailerId = newRetailerId;
    }
    /**
     * Getter for Retailer ID.
     * @return The Retailer ID.
     */
    public final int getRetailerId() {
        return retailerId;
    }
    /**
     * Setter for Retailer GS1 Company Prefix Gln Vli.
     * @param newRetailerGs1CmpnyPrefGlnVli
     *          The Retailer GS1 Company Prefix Gln Vli to set.
     */
    public final void setRetailerGs1CmpnyPrefGlnVli(
            final int newRetailerGs1CmpnyPrefGlnVli) {
        this.retailerGs1CmpnyPrefGlnVli = newRetailerGs1CmpnyPrefGlnVli;
    }
    /**
     * Getter for Retailer GS1 Company Prefix Gln Vli.
     * @return The Retailer GS1 Company Prefix Gln Vli.
     */
    public final int getRetailerGs1CmpnyPrefGlnVli() {
        return retailerGs1CmpnyPrefGlnVli;
    }
    /**
     * Setter for Retailer GS1 Company Prefix Gln.
     * @param newRetailerGs1CmpnyPrefGln
     *          The Retailer GS1 Company Prefix Gln to set.
     */
    public final void setRetailerGs1CmpnyPrefGln(
            final String newRetailerGs1CmpnyPrefGln) {
        this.retailerGs1CmpnyPrefGln = newRetailerGs1CmpnyPrefGln;
    }
    /**
     * Getter for Retailer GS1 Company Prefix Gln.
     * @return The Retailer GS1 Company Prefix Gln.
     */
    public final String getRetailerGs1CmpnyPrefGln() {
        return retailerGs1CmpnyPrefGln;
    }
    /**
     * Setter for Miscellaneous Elements.
     * @param newMiscElements   The Miscellaneous Elements to set.
     */
    public final void setMiscElements(final int newMiscElements) {
        this.miscElements = newMiscElements;
    }
    /**
     * Getter for Miscellaneous Elements.
     * @return The Miscellaneous Elements.
     */
    public final int getMiscElements() {
        return miscElements;
    }
    /**
     * Setter for Save Value Code.
     * @param newSaveValueCode  The Save Value Code to set.
     */
    public final void setSaveValueCode(final int newSaveValueCode) {
        this.saveValueCode = newSaveValueCode;
    }
    /**
     * Getter for Save Value Code.
     * @return The Save Value Code.
     */
    public final int getSaveValueCode() {
        return saveValueCode;
    }
    /**
     * Setter for Save Value Applies to Which Item.
     * @param newSaveValAppliesToWchItem
     *          The Save Value Applies to Which Item to set.
     */
    public final void setSaveValAppliesToWchItem(
            final int newSaveValAppliesToWchItem) {
        this.saveValAppliesToWchItem = newSaveValAppliesToWchItem;
    }
    /**
     * Getter for Save Value Applies to Which Item.
     * @return The Save Value Applies to Which Item.
     */
    public final int getSaveValAppliesToWchItem() {
        return saveValAppliesToWchItem;
    }
    /**
     * Setter for Store Coupon Flag.
     * @param newStoreCouponFlag    The Store Coupon Flag to set.
     */
    public final void setStoreCouponFlag(final int newStoreCouponFlag) {
        this.storeCouponFlag = newStoreCouponFlag;
    }
    /**
     * Getter for Store Coupon Flag.
     * @return The Store Coupon Flag.
     */
    public final int getStoreCouponFlag() {
        return storeCouponFlag;
    }
    /**
     * Setter for doNotMultiplyFlag.
     * @param newDoNotMultiplyFlag  The doNotMultiplyFlag to set.
     */
    public final void setDoNotMultiplyFlag(final int newDoNotMultiplyFlag) {
        this.doNotMultiplyFlag = newDoNotMultiplyFlag;
    }
    /**
     * Getter for doNotMultiplyFlag.
     * @return The doNotMultiplyFlag.
     */
    public final int getDoNotMultiplyFlag() {
        return doNotMultiplyFlag;
    }
    /**
     * Setter for Priority.
     * @param newPriority   The Priority to set.
     */
    public final void setPriority(final String newPriority) {
        this.priority = newPriority;
    }
    /**
     * Getter for Priority.
     * @return  The Priority.
     */
    public final String getPriority() {
        return priority;
    }
}
