package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Tender Model Object.
 *
 * <P>A Tender Node in POSLog XML.
 *
 * <P>The Tender node is under LineItem Node.
 * And holds the tender type information from the transaction.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Voucher")
public class Voucher {

    /**
     * The private member variable that will hold the type code of Voucher.
     */
    @XmlAttribute(name = "TypeCode")
    private String typeCode;

     /**
     * The private member variable that will hold the voucher name of the voucher.
     */
    @XmlElement(name = "VoucherName")
    private String voucherName;

    /**
    * The private member variable that will hold the change type of the voucher.
    */
   @XmlElement(name = "ChangeType")
   private String changeType;

    /**
    * The private member variable that will hold the voucher id of the voucher.
    */
   @XmlElement(name = "VoucherID")
   private String voucherID;

    /**
     * The private member variable that will hold the voucher id of the voucher.
     */
    @XmlElement(name = "OriginalVoucherID")
    private String originalVoucherID;

    /**
     * The private member variable which holds the Expiration Date information
     */
    @XmlElement(name = "ExpirationDate")
    private String expirationDate;

    /**
     * ID of brand.
     */
    @XmlElement(name = "SerialNumber")
    private String serialNumber;

    /**
     * Previous FaceValueAmount.
     */
    @XmlElement(name = "FaceValueAmount")
    private int faceValueAmount;

    /**
     * Previous StampType.
     */
    @XmlElement(name = "StampType")
    private String stampType;

    /**
     * Previous PointType.
     */
    @XmlElement(name = "PointType")
    private String pointType;

    /**
     * ID of UnspentAmount.
     */
    @XmlElement(name = "UnspentAmount")
    private UnspentAmount unspentAmount;

    /**
     * TenderType
     */
    @XmlAttribute(name = "TenderType")
    private String tenderType;

    /**
     * SubTenderType
     */
    @XmlAttribute(name = "SubTenderType")
    private String subTenderType;
    /**
     * TenderName
     */
    @XmlAttribute(name = "TenderName")
    private String tenderName;
    /**
     * barSubTypeCode
     */
    @XmlElement(name = "barSubTypeCode")
    private String barSubTypeCode;
    /**
     * Count
     */
    @XmlElement(name = "Count")
    private String count;
    /**
     * amount.
     */
    @XmlElement(name = "Amount")
    private String amount;
    /***
     * registeredAmount;
     */
    @XmlElement(name = "RegisteredAmount")
    private String registeredAmount;
    /***
     * currentAmount;
     */
    @XmlElement(name = "CurrentAmount")
    private String currentAmount;
    /***
     * difference;
     */
    @XmlElement(name = "Difference")
    private String difference;

    /***
     * cardNumber;
     */
    @XmlElement(name = "CardNumber")
    private String cardNumber;

    /***
     * authorizationNumber;
     */
    @XmlElement(name = "AuthorizationNumber")
    private String authorizationNumber;

    /***
     * priorFaceValueAmount;
     */
    @XmlElement(name = "PriorFaceValueAmount")
    private Integer priorFaceValueAmount;

    /***
     * currentFaceValueAmount;
     */
    @XmlElement(name = "CurrentFaceValueAmount")
    private Integer currentFaceValueAmount;

    /***
     * jis1;
     */
    @XmlElement(name = "JIS1")
    private String jis1;
    /***
     * jis2;
     */
    @XmlElement(name = "JIS2")
    private String jis2;

    /***
     * pin;
     */
    @XmlElement(name = "PIN")
    private String pin;

    /***
    * ChargeAmount.
    */
   @XmlElement(name = "ChargeAmount")
   private int chargeAmount;

   /***
    * Brand.
    */
   @XmlElement(name = "Brand")
   private String brand;

   /***
    * ErrorCode.
    */
   @XmlElement(name = "ErrorCode")
   private String errorCode;

    /**
     * @return the amount
     */
    public String getAmount() {
        return amount;
    }

    /**
     * @param amount the amount to set
     */
    public void setAmount(String amount) {
        this.amount = amount;
    }
    /**
     * @return the count
     */
    public String getCount() {
        return count;
    }

    /**
     * @param count the count to set
     */
    public void setCount(String count) {
        this.count = count;
    }
    /**
     * @return the TenderType
     */
    public final String getTenderType() {
        return tenderType;
    }
    /**
     * @param tenderType the tenderType to set
     */
    public  final void setTenderType(String tenderType) {
        this.tenderType = tenderType;
    }
    /**
     * @return the subTenderType
     */
    public final String getTenderName() {
        return tenderName;
    }
    /**
     * @param subTenderType the subTenderType to set
     */
    public final void setTenderName(String tenderName) {
        this.tenderName = tenderName;
    }
    /**
     * @return the subTenderType
     */
    public final String getSubTenderType() {
        return subTenderType;
    }
    /**
     * @param subTenderType the subTenderType to set
     */
    public final void setSubTenderType(String subTenderType) {
        this.subTenderType = subTenderType;
    }
    /**
     * @return the barSubTypeCode
     */
    public final String getBarSubTypeCode() {
        return barSubTypeCode;
    }
    /**
     * @param barSubTypeCode the barSubTypeCode to set
     */
    public final void setBarSubTypeCode(String barSubTypeCode) {
        this.barSubTypeCode = barSubTypeCode;
    }

    /**
     * @return the voucherID
     */
    public final String getVoucherID() {
        return voucherID;
    }

    /**
     * @param voucherID the voucherID to set
     */
    public final void setVoucherID(String voucherID) {
        this.voucherID = voucherID;
    }

    /**
     * @return the originalVoucherID
     */
    public final String getOriginalVoucherID() {
        return originalVoucherID;
    }

    /**
     * @param originalVoucherID the originalVoucherID to set
     */
    public final void setOriginalVoucherID(String originalVoucherID) {
        this.originalVoucherID = originalVoucherID;
    }

    /**
     * @return the expirationDate
     */
    public final String getExpirationDate() {
        return expirationDate;
    }

    /**
     * @param ExpirationDate
     *            the ExpirationDate to set
     */
    public final void setExpirationDate(String expirationDate) {
        this.expirationDate = expirationDate;
    }

    /**
     * @return the serialNumber
     */
    public final String getSerialNumber() {
        return serialNumber;
    }

    /**
     * @param serialNumber
     *            the serialNumber to set
     */
    public final void setSerialNumber(String serialNumber) {
        this.serialNumber = serialNumber;
    }
    /**
     * @return faceValueAmount
     */
    public int getFaceValueAmount() {
        return faceValueAmount;
    }

    /**
     * @param faceValueAmount セットする faceValueAmount
     */
    public void setFaceValueAmount(int faceValueAmount) {
        this.faceValueAmount = faceValueAmount;
    }
    /**
     * @return unspentAmount
     */
    public final UnspentAmount getUnspentAmount() {
        return unspentAmount;
    }

    /**
     * @param unspentAmount セットする unspentAmount
     */
    public final void setUnspentAmount( UnspentAmount unspentAmount) {
        this.unspentAmount = unspentAmount;
    }

    /**
     * @return typeCode
     */
    public String getTypeCode() {
        return typeCode;
    }

    /**
     * @param typeCode セットする typeCode
     */
    public void setTypeCode(String typeCode) {
        this.typeCode = typeCode;
    }

    /**
     * Overrides the toString() Method.
     * @return The string representation of tender.
     */
    public final String toString() {
        StringBuilder str = new StringBuilder();
        String crlf = "\r\n";

        str.append("VoucherID : ").append(this.voucherID).append(crlf)
                .append("OriginalVoucherID : ").append(this.originalVoucherID)
                .append(crlf).append("ExpirationDate : ")
                .append(this.expirationDate).append(crlf)
                .append("SerialNumber : ").append(this.serialNumber).append(crlf)
                .append("FaceValueAmount : ").append(this.faceValueAmount).append(crlf);

        return str.toString();
    }

    /***
     * @param regAmount
     */
    public final void setRegisteredAmount(String regAmount) {
    	this.registeredAmount = regAmount;
    }

    /***
     * @return the registeredAmount.
     */
    public final String getRegisteredAmount() {
    	return this.registeredAmount;
    }

    public final void setCurrentAmount(String curAmt) {
    	this.currentAmount = curAmt;
    }

    public final String getCurrentAmount() {
    	return this.currentAmount;
    }

    public final void setDifference(String diff) {
    	this.difference = diff;
    }

    public final String getDifference() {
    	return this.difference;
    }

    /**
     * @return the voucherName
     */
    public final String getVoucherName() {
        return voucherName;
    }

    /**
     * @param voucherName the voucherName to set
     */
    public final void setVoucherName(final String voucherName) {
        this.voucherName = voucherName;
    }

    /**
     * @return the changeType
     */
    public final String getChangeType() {
        return changeType;
    }

    /**
     * @param changeType the changeType to set
     */
    public final void setChangeType(final String changeType) {
        this.changeType = changeType;
    }

    /**
     * @return the stampType
     */
    public final String getStampType() {
        return stampType;
    }

    /**
     * @param stampType the stampType to set
     */
    public final void setStampType(String stampType) {
        this.stampType = stampType;
    }

    /**
     * @return the pointType
     */
    public final String getPointType() {
        return pointType;
    }

    /**
     * @param pointType the pointType to set
     */
    public final void setPointType(String pointType) {
        this.pointType = pointType;
    }

    /**
     * @return the CardNumber
     */
    public final String getCardNumber() {
        return cardNumber;
    }

    /**
     * @param cardNumber the cardNumber to set
     */
    public final void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    /**
     * @return the authorizationNumber
     */
    public final String getAuthorizationNumber() {
        return authorizationNumber;
    }

    /**
     * @param authorizationNumber the authorizationNumber to set
     */
    public final void setAuthorizationNumber(String authorizationNumber) {
        this.authorizationNumber = authorizationNumber;
    }

    /**
     * @return the priorFaceValueAmount
     */
    public final Integer getPriorFaceValueAmount() {
        return priorFaceValueAmount;
    }

    /**
     * @param priorFaceValueAmount the priorFaceValueAmount to set
     */
    public final void setPriorFaceValueAmount(Integer priorFaceValueAmount) {
        this.priorFaceValueAmount = priorFaceValueAmount;
    }

    /**
     * @return the currentFaceValueAmount
     */
    public final Integer getCurrentFaceValueAmount() {
        return currentFaceValueAmount;
    }

    /**
     * @param currentFaceValueAmount the currentFaceValueAmount to set
     */
    public final void setCurrentFaceValueAmount(Integer currentFaceValueAmount) {
        this.currentFaceValueAmount = currentFaceValueAmount;
    }

    /**
     * @return the jis1
     */
    public final String getJis1() {
        return jis1;
    }

    /**
     * @param jis1 the jis1 to set
     */
    public final void setJis1(String jis1) {
        this.jis1 = jis1;
    }

    /**
     * @return the jis2
     */
    public final String getJis2() {
        return jis2;
    }

    /**
     * @param jis2 the jis2 to set
     */
    public final void setJis2(String jis2) {
        this.jis2 = jis2;
    }

    /**
     * @return the pin
     */
    public final String getPin() {
        return pin;
    }

    /**
     * @param pin the pin to set
     */
    public final void setPin(String pin) {
        this.pin = pin;
    }
    /**
     * @return the chargeAmount
     */
    public final int getChargeAmount() {
        return chargeAmount;
    }

    /**
     * @param chargeAmount
     *            the chargeAmount to set
     */
    public final void setChargeAmount(int chargeAmount) {
        this.chargeAmount = chargeAmount;
    }

    /**
     * @return the brand
     */
    public final String getBrand() {
        return brand;
    }

    /**
     * @param brand
     *            the brand to set
     */
    public final void setBrand(String brand) {
        this.brand = brand;
    }

    /**
     * @return the errorCode
     */
    public final String getErrorCode() {
        return errorCode;
    }

    /**
     * @param errorCode
     *            the errorCode to set
     */
    public final void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }
}
