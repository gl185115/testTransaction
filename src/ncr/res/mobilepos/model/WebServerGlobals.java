package ncr.res.mobilepos.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.helper.StringUtility;
import ncr.res.mobilepos.systemconfiguration.model.MemberServer;
import ncr.res.mobilepos.systemconfiguration.model.UrlConfig;

import org.codehaus.jackson.map.ObjectMapper;



/**
 * WebServerGlobals is a model class that encapsulates
 * all the necessary System Configuration for the
 * Web Servers that used this Web Service
 */
/**
 * @author NCRP
 *
 */
@XmlRootElement
@XmlAccessorType(XmlAccessType.NONE)
public class WebServerGlobals extends ResultBase {
    /** The default constructor. */
    public WebServerGlobals() {
        badgeCountType = -1;
        maxSearchResults = -1;
        maxSuspendable = DEFAULT_MAX_SUSPENDABLE;
    }
    /**
     * TaxRate.
     */
    @XmlElement(name = "TaxRate")
    private String taxRate;
    /**
     * TaxRounding.
     */
    @XmlElement(name = "TaxRounding")
    /**
     * The Rounding flag for computing Tax(1:RoundUp / 2:RoundDown / 3:RoundOff)
     */
    private String roundingConsumptionTax;
    /**
     * CredentialCookiesExpiry.
     * Deprecated.
     */
    @XmlElement(name = "CredentialCookiesExpiry")
    private String credentialCookiesExpiry;
    /**
     * CredentialExpiry.
     * Deprecated.
     */
    @XmlElement(name = "CredentialExpiry")
    private String credentialExpiry;
    /**
     * NumberOfPasscodeDigits.
     * Exact Passcode digits at operator signon.
     */
    @XmlElement(name = "NumberOfPasscodeDigits")
    private String numberOfPasscodeDigits;
    /**
     * Maximum number for Operator digits.
     */
    @XmlElement(name = "MaxLengthOfOperatorNo")
    private String maxLengthOfOperatorNo;
    /**
     * WebUI default language to use.
     */
    @XmlElement(name = "DefaultLanguage")
    private String defaultLanguage;
    /**
     * The border line of sales reports.
     */
    @XmlElement(name = "StoreOpenTime")
    private String storeOpenTime;
    /**
     * The Maximum Transaction Number to be generated.
     */
    @XmlElement(name = "MaxTransactionCount")
    private String maxTransactionCount;
    /**
     * One of parameters for signature capture.
     */
    @XmlElement(name = "MerchantPassword")
    private String merchantPassword;
    /**
     * One of parameters for signature capture.
     */
    @XmlElement(name = "MerchatnId")
    private String merchatnId;
    /**
     * One of parameters for signature capture.
     */
    @XmlElement(name = "PassPhrase")
    private String passPhrase;
    /**
     * One of parameters for signature capture.
     */
    @XmlElement(name = "SecurityPhrase")
    private String securityPhrase;
    /**
     * Signless credit card payment threshold amount.
     */
    @XmlElement(name = "SignlessThreshold")
    /**
     * Prefix, SKU-Digits; The parameter of in-store label.
     */
    private String signlessThreshold;
    /**
     * Prefix, SKU-Digits; The parameter of in-store label.
     */
    @XmlElement(name = "InStoreParam1")
    private String inStoreParam1;
    /**
     * Prefix, SKU-Digits; The parameter of in-store label.
     */
    @XmlElement(name = "InStoreParam2")
    private String inStoreParam2;
    /**
     * Prefix, SKU-Digits; The parameter of in-store label.
     */
    @XmlElement(name = "InStoreParam3")
    private String inStoreParam3;
    /**
     * Prefix, SKU-Digits; The parameter of in-store label.
     */
    @XmlElement(name = "InStoreParam4")
    private String inStoreParam4;
    /**
     * Prefix, SKU-Digits; The parameter of in-store label.
     */
    @XmlElement(name = "InStoreParam5")
    private String inStoreParam5;
    /**
     * Prefix, SKU-Digits; The parameter of in-store label.
     */
    @XmlElement(name = "InStoreParam6")
    private String inStoreParam6;
    /**
     * Prefix, SKU-Digits; The parameter of in-store label.
     */
    @XmlElement(name = "InStoreParam7")
    private String inStoreParam7;
    /**
     * Prefix, SKU-Digits; The parameter of in-store label.
     */
    @XmlElement(name = "InStoreParam8")
    private String inStoreParam8;
    /**
     * Prefix, SKU-Digits; The parameter of in-store label.
     */
    @XmlElement(name = "InStoreParam9")
    private String inStoreParam9;
    /**
     * Prefix, SKU-Digits; The parameter of in-store label.
     */
    @XmlElement(name = "InStoreParam10")
    private String inStoreParam10;
    /**
     * Prefix, SKU-Digits; The parameter of in-store label.
     */
    @XmlElement(name = "InStoreParam11")
    private String inStoreParam11;
    /**
     * Flag to disable daily change of AES key
     * (if true, disable AESKey daily change).
     */
    @XmlElement(name = "Flag")
    private String flag;
    /**
     * AES Key when AESKey.Flag is true.
     */
    @XmlElement(name = "KeyValue")
    private String keyValue;
    /** The Badge Count Type. */
    @XmlElement(name = "BadgeCountType")
    private int badgeCountType;
    /**
     * Option to enable/disable same items
     * consolidation in shopping cart.
     */
    @XmlElement(name = "DisableItemConsolidation")
    private boolean disableItemConsolidation;
    /** The Maximum Search Results. */
    @XmlElement(name = "MaxSearchResults")
    private int maxSearchResults;
    /** The range of documentary tax. */
    @XmlElement(name = "Range1")
    private String range1;
    @XmlElement(name = "Range2")
    private String range2;
	@XmlElement(name = "Range3")
    private String range3;
    @XmlElement(name = "Range4")
    private String range4;
    @XmlElement(name = "Range5")
    private String range5;
    @XmlElement(name = "Range6")
    private String range6;
    @XmlElement(name = "Range7")
    private String range7;
    @XmlElement(name = "Range8")
    private String range8;
    @XmlElement(name = "Range9")
    private String range9;
    @XmlElement(name = "Range10")
    private String range10;
    /** The Price Include Tax. */
    private String priceIncludeTax;
    /** The LogIn Behavior. */
    @XmlElement(name = "LoginBehavior")
    private int loginBehavior;
    /** The Credential Days Left Warning. */
    @XmlElement(name = "CredentialDayLeftWarning")
    private int credentialDayLeftWarning;
    /** The Discount Input Method.*/
    @XmlElement(name = "DiscountInputMethod")
    private String discountInputMethod;
    /** The pricing type **/
    @XmlElement(name = "PricingType")
    private int pricingType;
    /** The payment method **/
    @XmlElement(name = "PaymentMethod")
    private String method;
    /** The fantamiliar **/
    @XmlElement(name = "MemberServer")
    private MemberServer memberInfoConfig;
    /** The server URI */
    @XmlElement(name = "Url")
    private UrlConfig urlConfig;
    /** The Category points system **/
    @XmlElement(name = "Category")
    private String category;
    /** The Open Drawer key **/
    @XmlElement(name = "Drawer")
    private String drawer;
    /** The Open Drawer Auto Open key **/
    @XmlElement(name = "DrawerAutoOpen")
    private String drawerAutoOpen;
    @XmlElement(name="Synchronization")
    private String synchronization;   
    @XmlElement(name="OpenDrawer")
    private String openDrawer;
    @XmlElement(name="MaxSuspendable")
    private String maxSuspendable;
    public static final String DEFAULT_MAX_SUSPENDABLE = "20";
    @XmlElement(name="TimeDifference")
    private String timeDifference;
    /** Automatic premium discount rate **/
    @XmlElement(name="PremierDiscountRate")
    private int premierDiscountRate;
    @XmlElement(name="CompanyId")
    private String companyId;
    @XmlElement(name="TodUri")
    private String todUri;
    @XmlElement(name="TodConnectionTimeout")
    private int todConnectionTimeout = 1000;
    @XmlElement(name="TodReadTimeout")
    private int todReadTimeout = 1000;
    @XmlElement(name="VictoriaNumSales")
    private int victoriaNumSales;
    @XmlElement(name="VictoriaNumChargePayment")
    private int victoriaNumChargePayment;
    @XmlElement(name="VictoriaNumChargePaymentVoid")
    private int victoriaNumChargePaymentVoid;
    @XmlElement(name="VictoriaNumChargeSale")
    private int victoriaNumChargeSale;
    @XmlElement(name="VictoriaNumExchange")
    private int victoriaNumExchange;
    @XmlElement(name="VictoriaNumPreserveCancel")
    private int victoriaNumPreserveCancel;
    @XmlElement(name="VictoriaNumProcessingSlip")
    private int victoriaNumProcessingSlip;
    @XmlElement(name="VictoriaNumPurchase")
    private int victoriaNumPurchase;
    @XmlElement(name="VictoriaNumPurchaseReturn")
    private int victoriaNumPurchaseReturn;
    @XmlElement(name="VictoriaNumReturn")
    private int victoriaNumReturn;
    @XmlElement(name="VictoriaNumTaxFree")
    private int victoriaNumTaxFree;
    @XmlElement(name="VictoriaNumVoid")
    private int victoriaNumVoid;
    @XmlElement(name="XebioNumChargePayment")
    private int xebioNumChargePayment;
    @XmlElement(name="XebioNumChargePaymentVoid")
    private int xebioNumChargePaymentVoid;
    @XmlElement(name="XebioNumChargeSale")
    private int xebioNumChargeSale;
    @XmlElement(name="XebioNumExchange")
    private int xebioNumExchange;
    @XmlElement(name="XebioNumPreserveCancel")
    private int xebioNumPreserveCancel;
    @XmlElement(name="XebioNumProcessingSlip")
    private int xebioNumProcessingSlip;
    @XmlElement(name="XebioNumPurchase")
    private int xebioNumPurchase;
    @XmlElement(name="XebioNumPurchaseReturn")
    private int xebioNumPurchaseReturn;
    @XmlElement(name="XebioNumReturn")
    private int xebioNumReturn;
    @XmlElement(name="XebioNumSales")
    private int xebioNumSales ;
    @XmlElement(name="XebioNumTaxFree")
    private int xebioNumTaxFree;
    @XmlElement(name="XebioNumVoid")
    private int xebioNumVoid;
    
    
    public int getVictoriaNumSales() {
        return victoriaNumSales;
    }
    public void setVictoriaNumSales(int victoriaNumSales) {
        this.victoriaNumSales = victoriaNumSales;
    }
    public int getVictoriaNumChargePayment() {
    	return victoriaNumChargePayment;
    }
    public void setVictoriaNumChargePayment(int victoriaNumChargePayment) {
		this.victoriaNumChargePayment = victoriaNumChargePayment;
	}
    
    public int getVictoriaNumChargePaymentVoid() {
    	return victoriaNumChargePaymentVoid;
    }
    public void setVictoriaNumChargePaymentVoid(int victoriaNumChargePaymentVoid) {
		this.victoriaNumChargePaymentVoid = victoriaNumChargePaymentVoid;
	}
    
    public int getVictoriaNumChargeSale() {
    	return victoriaNumChargeSale;
    }
    public void setVictoriaNumChargeSale(int victoriaNumChargeSale) {
		this.victoriaNumChargeSale = victoriaNumChargeSale;
	}
    
    public int getVictoriaNumExchange() {
    	return victoriaNumExchange;
    }
    public void setVictoriaNumExchange(int victoriaNumExchange) {
		this.victoriaNumExchange = victoriaNumExchange;
	}
    
    public int getVictoriaNumPreserveCancel() {
    	return victoriaNumPreserveCancel;
    }
    public void setVictoriaNumPreserveCancel(int victoriaNumPreserveCancel) {
		this.victoriaNumPreserveCancel = victoriaNumPreserveCancel;
	}
    
    public int getVictoriaNumProcessingSlip() {
    	return victoriaNumProcessingSlip;
    }
    public void setVictoriaNumProcessingSlip(int victoriaNumProcessingSlip) {
		this.victoriaNumProcessingSlip = victoriaNumProcessingSlip;
	}
    
    public int getVictoriaNumPurchase() {
		return victoriaNumPurchase;
	}
	public void setVictoriaNumPurchase(int victoriaNumPurchase) {
		this.victoriaNumPurchase = victoriaNumPurchase;
	}
	public int getVictoriaNumPurchaseReturn() {
		return victoriaNumPurchaseReturn;
	}
	public void setVictoriaNumPurchaseReturn(int victoriaNumPurchaseReturn) {
		this.victoriaNumPurchaseReturn = victoriaNumPurchaseReturn;
	}
	public int getVictoriaNumReturn() {
		return victoriaNumReturn;
	}
	public void setVictoriaNumReturn(int victoriaNumReturn) {
		this.victoriaNumReturn = victoriaNumReturn;
	}
	public int getVictoriaNumTaxFree() {
		return victoriaNumTaxFree;
	}
	public void setVictoriaNumTaxFree(int victoriaNumTaxFree) {
		this.victoriaNumTaxFree = victoriaNumTaxFree;
	}
	public int getVictoriaNumVoid() {
		return victoriaNumVoid;
	}
	public void setVictoriaNumVoid(int victoriaNumVoid) {
		this.victoriaNumVoid = victoriaNumVoid;
	}
	public int getXebioNumChargePayment() {
		return xebioNumChargePayment;
	}
	public void setXebioNumChargePayment(int xebioNumChargePayment) {
		this.xebioNumChargePayment = xebioNumChargePayment;
	}
	public int getXebioNumChargePaymentVoid() {
		return xebioNumChargePaymentVoid;
	}
	public void setXebioNumChargePaymentVoid(int xebioNumChargePaymentVoid) {
		this.xebioNumChargePaymentVoid = xebioNumChargePaymentVoid;
	}
	public int getXebioNumChargeSale() {
		return xebioNumChargeSale;
	}
	public void setXebioNumChargeSale(int xebioNumChargeSale) {
		this.xebioNumChargeSale = xebioNumChargeSale;
	}
	public int getXebioNumExchange() {
		return xebioNumExchange;
	}
	public void setXebioNumExchange(int xebioNumExchange) {
		this.xebioNumExchange = xebioNumExchange;
	}
	public int getXebioNumPreserveCancel() {
		return xebioNumPreserveCancel;
	}
	public void setXebioNumPreserveCancel(int xebioNumPreserveCancel) {
		this.xebioNumPreserveCancel = xebioNumPreserveCancel;
	}
	public int getXebioNumProcessingSlip() {
		return xebioNumProcessingSlip;
	}
	public void setXebioNumProcessingSlip(int xebioNumProcessingSlip) {
		this.xebioNumProcessingSlip = xebioNumProcessingSlip;
	}
	public int getXebioNumPurchase() {
		return xebioNumPurchase;
	}
	public void setXebioNumPurchase(int xebioNumPurchase) {
		this.xebioNumPurchase = xebioNumPurchase;
	}
	public int getXebioNumPurchaseReturn() {
		return xebioNumPurchaseReturn;
	}
	public void setXebioNumPurchaseReturn(int xebioNumPurchaseReturn) {
		this.xebioNumPurchaseReturn = xebioNumPurchaseReturn;
	}
	public int getXebioNumReturn() {
		return xebioNumReturn;
	}
	public void setXebioNumReturn(int xebioNumReturn) {
		this.xebioNumReturn = xebioNumReturn;
	}
	public int getXebioNumSales() {
		return xebioNumSales;
	}
	public void setXebioNumSales(int xebioNumSales) {
		this.xebioNumSales = xebioNumSales;
	}
	public int getXebioNumTaxFree() {
		return xebioNumTaxFree;
	}
	public void setXebioNumTaxFree(int xebioNumTaxFree) {
		this.xebioNumTaxFree = xebioNumTaxFree;
	}
	public int getXebioNumVoid() {
		return xebioNumVoid;
	}
	public void setXebioNumVoid(int xebioNumVoid) {
		this.xebioNumVoid = xebioNumVoid;
	}
	public String getDocTaxRange2() {
		return range2;
	}
	public void setDocTaxRange2(String range2) {
		this.range2 = range2;
	}
	public String getDocTaxRange3() {
		return range3;
	}
	public void setDocTaxRange3(String range3) {
		this.range3 = range3;
	}
	public String getDocTaxRange4() {
		return range4;
	}
	public void setDocTaxRange4(String range4) {
		this.range4 = range4;
	}
	public String getDocTaxRange5() {
		return range5;
	}
	public void setDocTaxRange5(String range5) {
		this.range5 = range5;
	}
	public String getDocTaxRange6() {
		return range6;
	}
	public void setDocTaxRange6(String range6) {
		this.range6 = range6;
	}
	public String getDocTaxRange7() {
		return range7;
	}
	public void setDocTaxRange7(String range7) {
		this.range7 = range7;
	}
	public String getDocTaxRange8() {
		return range8;
	}
	public void setDocTaxRange8(String range8) {
		this.range8 = range8;
	}
	public String getDocTaxRange9() {
		return range9;
	}
	public void setDocTaxRange9(String range9) {
		this.range9 = range9;
	}
	public String getDocTaxRange10() {
		return range10;
	}
	public void setDocTaxRange10(String range10) {
		this.range10 = range10;
	}    
   	public String getTodSynchronization() {
		return synchronization;
	}
	public void setTodSynchronization(String synchronization) {
		this.synchronization = synchronization;
	}
	public String getTodTimeDifference() {
		return timeDifference;
	}
	public void setTodTimeDifference(String timeDifference) {
		this.timeDifference = timeDifference;
	}
    public int getPremierDiscountRate(){
        return premierDiscountRate;
    }
    public void setPremierDiscountRate(int premierDiscountRate) {
        this.premierDiscountRate = premierDiscountRate;
    }
	public String getCompanyId() {
        return companyId;
    }
    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }
    /**
     * return Enterprise Server's TOD service URI.
     */
    public String getTodUri() {
        return todUri;
    }
    /**
     * set Enterprise Server's TOD service URI.
     */
    public void setTodUri(String uri) {
        todUri = uri;
    }
    /**
     * return connection timeout value of Enterprise Server's TOD service.
     */
    public int getTodConnectionTimeout() {
        return todConnectionTimeout;
    }
    /**
     * set connection timeout value of Enterprise Server's TOD service.
     */
    public void setTodConnectionTimeout(int val) {
        todConnectionTimeout = val;
    }
    /**
     * return read timeout value of Enterprise Server's TOD service.
     */
    public int getTodReadTimeout() {
        return todReadTimeout;
    }
    /**
     * set read timeout value of Enterprise Server's TOD service.
     */
    public void setTodReadTimeout(int val) {
        todReadTimeout = val;
    }
    /**
     * @return the Discount Input Method
     */
    public final String getDiscountInputMethod() {
        return discountInputMethod;
    }
    /**
     * @param discountInputMethodToSet
     *  the Discount Input Method to set
     */
    public final void setDiscountInputMethod(
            final String discountInputMethodToSet) {
        this.discountInputMethod = discountInputMethodToSet;
    }

    /**
     * @return the Credential Days Left Warning
     */
    public final int getCredentialDayLeftWarning() {
        return credentialDayLeftWarning;
    }
    /**
     * @param credentialDayLeftWarningToSet
     *  the Credential Days Left Warning to set
     */
    public final void setCredentialDayLeftWarning(
            final int credentialDayLeftWarningToSet) {
        this.credentialDayLeftWarning = credentialDayLeftWarningToSet;
    }
    /**
     * @return the Login Behavior
     */
    public final int getLoginBehavior() {
        return loginBehavior;
    }
    /**
     * @param loginBehaviorToSet the loginBehavior to set
     */
    public final void setLoginBehavior(final int loginBehaviorToSet) {
        this.loginBehavior = loginBehaviorToSet;
    }
    /**
     * @return the priceIncludeTax
     */
    public final String getPriceIncludeTax() {
        return priceIncludeTax;
    }
    /**
     * @param priceIncludeTaxToSet the priceIncludeTax to set
     */
    public final void setPriceIncludeTax(final String priceIncludeTaxToSet) {
        this.priceIncludeTax = priceIncludeTaxToSet;
    }
    /**
     * Getters for the Badge Count Type.
     * @return The Badge Count Type
     */
    public final int getBadgeCountType() {
        return badgeCountType;
    }
    /**
     * Setters for Badge Count Type.
     * @param badgeCountTypeToSet The new BadgeCountType
     */
     public final void setBadgeCountType(final int badgeCountTypeToSet) {
        this.badgeCountType = badgeCountTypeToSet;
     }
    /**
     * Getter for KeyValue.
     *
     * @return String
     */
    public final String getKeyValue() {
        return keyValue;
    }
    /**
     * Setter for KeyValue.
     *
     * @param keyValueToSet string key value
     */
    public final void setKeyValue(final String keyValueToSet) {
        this.keyValue = keyValueToSet;
    }
    /**
     * Getter for Flag.
     *
     * @return String
     */
    public final String getFlag() {
        return flag;
    }
    /**
     * Setter for KeyValue.
     *
     * @param flagToSet flag
     */
    public final void setFlag(final String flagToSet) {
        this.flag = flagToSet;
    }
    /**
     * Getter for inStoreParam1.
     *
     * @return String
     */
    public final String getInStoreParam1() {
        return inStoreParam1;
    }
    /**
     * Setter for inStoreParam1.
     *
     * @param inStoreParam1ToSet inStoreParam1
     */
    public final void setInStoreParam1(final String inStoreParam1ToSet) {
        this.inStoreParam1 = inStoreParam1ToSet;
    }
    /**
     * Getter for inStoreParam2.
     *
     * @return String
     */
    public final String getInStoreParam2() {
        return inStoreParam2;
    }
    /**
     * Setter for inStoreParam2.
     *
     * @param inStoreParam2ToSet data to set
     */
    public final void setInStoreParam2(final String inStoreParam2ToSet) {
        this.inStoreParam2 = inStoreParam2ToSet;
    }
    /**
     * Getter for inStoreParam3.
     *
     * @return String
     */
    public final String getInStoreParam3() {
        return inStoreParam3;
    }
    /**
     * Setter for inStoreParam3.
     *
     * @param inStoreParam3ToSet data to set
     */
    public final void setInStoreParam3(final String inStoreParam3ToSet) {
        this.inStoreParam3 = inStoreParam3ToSet;
    }
    /**
     * Getter for inStoreParam4.
     *
     * @return String
     */
    public final String getInStoreParam4() {
        return inStoreParam4;
    }
    /**
     * Setter for inStoreParam4.
     *
     * @param inStoreParam4ToSet data to set
     */
    public final void setInStoreParam4(final String inStoreParam4ToSet) {
        this.inStoreParam4 = inStoreParam4ToSet;
    }
    /**
     * Getter for inStoreParam5.
     *
     * @return String
     */
    public final String getInStoreParam5() {
        return inStoreParam5;
    }
    /**
     * Setter for inStoreParam5.
     *
     * @param inStoreParam5ToSet data to set
     */
    public final void setInStoreParam5(final String inStoreParam5ToSet) {
        this.inStoreParam5 = inStoreParam5ToSet;
    }
    /**
     * Getter for inStoreParam6.
     *
     * @return String
     */
    public final String getInStoreParam6() {
        return inStoreParam6;
    }
    /**
     * Setter for inStoreParam6.
     *
     * @param inStoreParam6ToSet data to set
     */
    public final void setInStoreParam6(final String inStoreParam6ToSet) {
        this.inStoreParam6 = inStoreParam6ToSet;
    }
    /**
     * Getter for inStoreParam7.
     *
     * @return String
     */
    public final String getInStoreParam7() {
        return inStoreParam7;
    }
    /**
     * Setter for inStoreParam7.
     *
     * @param inStoreParam7ToSet data to set
     */
    public final void setInStoreParam7(final String inStoreParam7ToSet) {
        this.inStoreParam7 = inStoreParam7ToSet;
    }
    /**
     * Getter for inStoreParam8.
     *
     * @return String
     */
    public final String getInStoreParam8() {
        return inStoreParam8;
    }
    /**
     * Setter for inStoreParam8.
     *
     * @param inStoreParam8ToSet data to set
     */
    public final void setInStoreParam8(final String inStoreParam8ToSet) {
        this.inStoreParam8 = inStoreParam8ToSet;
    }
    /**
     * Getter for inStoreParam9.
     *
     * @return String
     */
    public final String getInStoreParam9() {
        return inStoreParam9;
    }
    /**
     * Setter for inStoreParam9.
     *
     * @param inStoreParam9ToSet data to set
     */
    public final void setInStoreParam9(final String inStoreParam9ToSet) {
        this.inStoreParam9 = inStoreParam9ToSet;
    }
    /**
     * Getter for inStoreParam10.
     *
     * @return String
     */
    public final String getInStoreParam10() {
        return inStoreParam10;
    }
    /**
     * Setter for inStoreParam10.
     *
     * @param inStoreParam10ToSet data to set
     */
    public final void setInStoreParam10(final String inStoreParam10ToSet) {
        this.inStoreParam10 = inStoreParam10ToSet;
    }
    /**
     * Getter for inStoreParam11.
     *
     * @return String
     */
    public final String getInStoreParam11() {
        return inStoreParam11;
    }
    /**
     * Setter for inStoreParam11.
     *
     * @param inStoreParam11ToSet data to set
     */
    public final void setInStoreParam11(final String inStoreParam11ToSet) {
        this.inStoreParam11 = inStoreParam11ToSet;
    }
    /**
     * Getter for threshold.
     *
     * @return String
     */
    public final String getThreshold() {
        return signlessThreshold;
    }
    /**
     * Setter for threshold.
     *
     * @param thresholdToSet data to set
     */
    public final void setThreshold(final String thresholdToSet) {
        this.signlessThreshold = thresholdToSet;
    }
    /**
     * Getter for securityPhrase.
     *
     * @return String
     */
    public final String getSecurityphrase() {
        return securityPhrase;
    }
    /**
     * Setter for securityPhrase.
     *
     * @param securityPhraseToSet data to set
     */
    public final void setSecurityphrase(final String securityPhraseToSet) {
        this.securityPhrase = securityPhraseToSet;
    }
    /**
     * Getter for passPhrase.
     *
     * @return String
     */
    public final String getPassphrase() {
        return passPhrase;
    }
    /**
     * Setter for passPhrase.
     *
     * @param passPhraseToSet data to set
     */
    public final void setPassphrase(final String passPhraseToSet) {
        this.passPhrase = passPhraseToSet;
    }
    /**
     * Getter for forMerchantId.
     *
     * @return String
     */
    public final String getMerchatnid() {
        return merchatnId;
    }
    /**
     * Setter for merchatnId.
     *
     * @param merchatnIdToSet data to set
     */
    public final void setMerchatnid(final String merchatnIdToSet) {
        this.merchatnId = merchatnIdToSet;
    }
    /**
     * Getter for MerchantPassword.
     *
     * @return String
     */
    public final String getMerchantpassword() {
        return merchantPassword;
    }
    /**
     * Setter for merchantPassword.
     *
     * @param merchantPasswordToSet data to set
     */
    public final void setMerchantpassword(final String merchantPasswordToSet) {
        this.merchantPassword = merchantPasswordToSet;
    }
    /**
     * Getter for maxTransactionCount.
     *
     * @return String
     */
    public final String getMaxtransactioncount() {
        return maxTransactionCount;
    }
    /**
     * Setter for maxTransactionCount.
     *
     * @param maxTransactionCountToSet data to set
     */
    public final void setMaxtransactioncount(
            final String maxTransactionCountToSet) {
        this.maxTransactionCount = maxTransactionCountToSet;
    }
    /**
     * Getter for numberOfPasscodeDigits.
     *
     * @return String
     */
    public final String getNumberofpasscodedigits() {
        return numberOfPasscodeDigits;
    }
    /**
     * Setter for numberOfPasscodeDigits.
     *
     * @param numberOfPasscodeDigitsToSet data to set
     */
    public final void setNumberofpasscodedigits(
            final String numberOfPasscodeDigitsToSet) {
        this.numberOfPasscodeDigits = numberOfPasscodeDigitsToSet;
    }
    /**
     * Getter for maxLengthOfOperatorNo.
     *
     * @return String
     */
    public final String getMaxlengthofoperatorno() {
        return maxLengthOfOperatorNo;
    }
    /**
     * Setter for maxLengthOfOperatorNo.
     *
     * @param maxLengthOfOperatorNoToSet data to set
     */
    public final void setMaxlengthofoperatorno(
            final String maxLengthOfOperatorNoToSet) {
        this.maxLengthOfOperatorNo = maxLengthOfOperatorNoToSet;
    }
    /**
     * Getter for defaultLanguage.
     *
     * @return String
     */
    public final String getDefaultLanguage() {
        return defaultLanguage;
    }
    /**
     * Setter for defaultLanguage.
     *
     * @param defaultLanguageToSet data to set
     */
    public final void setDefaultLanguage(final String defaultLanguageToSet) {
        this.defaultLanguage = defaultLanguageToSet;
    }
    /**
     * Getter for taxRate.
     *
     * @return String
     */
    public final String getTaxrate() {
        return taxRate;
    }
    /**
     * Setter for taxRate.
     *
     * @param taxRateToSet data to set
     */
    public final void setTaxrate(final String taxRateToSet) {
        this.taxRate = taxRateToSet;
    }
    /**
     * Getter for taxrounding.
     *
     * @return String
     */
    public final String getTaxrounding() {
        return roundingConsumptionTax;
    }
    /**
     * Setter for taxrounding.
     *
     * @param taxroundingToSet data to set
     */
    public final void setTaxrounding(final String taxroundingToSet) {
        this.roundingConsumptionTax = taxroundingToSet;
    }
    /**
     * Getter for credentialExpiry.
     *
     * @return String
     */
    public final String getCredentialexpiry() {
        return credentialExpiry;
    }
    /**
     * Setter for credentialExpiry.
     *
     * @param credentialExpiryToSet data to set
     */
    public final void setCredentialexpiry(
            final String credentialExpiryToSet) {
        this.credentialExpiry = credentialExpiryToSet;
    }
    /**
     * Getter for numberOfPasscodeDigits.
     *
     * @return String
     */
    public final String getNumberOfPasscodeDigits() {
        return numberOfPasscodeDigits;
    }
    /**
     * Setter for numberOfPasscodeDigits.
     *
     * @param numberOfPasscodeDigits data to set
     */
    public final void setNumberOfPasscodeDigits(
            final String numberOfPasscodeDigits) {
        this.numberOfPasscodeDigits = numberOfPasscodeDigits;
    }
    /**
     * Getter for maxLengthOfOperatorNo.
     *
     * @return String
     */
    public final String getMaxLengthOfOperatorNo() {
        return maxLengthOfOperatorNo;
    }
    /**
     * Setter for maxLengthOfOperatorNo.
     *
     * @param maxLengthOfOperatorNo data to set
     */
    public final void setMaxLengthOfOperatorNo(
            final String maxLengthOfOperatorNo) {
        this.maxLengthOfOperatorNo = maxLengthOfOperatorNo;
    }
    /**
     * Setter for credentialCookiesExpiry.
     *
     * @param credentialCookiesExpiryToSet data to set
     */
    public final void setCredentialcookiesexpiry(
            final String credentialCookiesExpiryToSet) {
        this.credentialCookiesExpiry = credentialCookiesExpiryToSet;
    }
    /**
     * Getter for credentialCookiesExpiry.
     *
     * @return String
     */
    public final String getCredentialcookiesexpiry() {
        return credentialCookiesExpiry;
    }
    /**
     * Setter for storeOpenTime.
     *
     * @param storeOpenTimeToSet data to set
     */
    public final void setStoreOpenTime(final String storeOpenTimeToSet) {
        this.storeOpenTime = storeOpenTimeToSet;
    }
    /**
     * Getter for storeOpenTime.
     *
     * @return String
     */
    public final String getStoreOpenTime() {
        return storeOpenTime;
    }

    /**
     * @param bDisableItemConsolidation the disableItemConsolidation to set
     */
    public final void setDisableItemConsolidation(
            final boolean bDisableItemConsolidation) {
        this.disableItemConsolidation = bDisableItemConsolidation;
    }
    /**
     * @return the disableItemConsolidation
     */
    public final boolean isDisableItemConsolidation() {
        return disableItemConsolidation;
    }
    /**
     * @return the maxSearchResults
     */
    public final int getMaxSearchResults() {
        return maxSearchResults;
    }
    /**
     * @param maxSearchResultsToSet the maxSearchResults to set
     */
    public final void setMaxSearchResults(final int maxSearchResultsToSet) {
        this.maxSearchResults = maxSearchResultsToSet;
    }
    public final String getDocTaxRange1() {
        return range1;
    }
    public final void setDocTaxRange1(final String range1) {
        this.range1 = range1;
    }
    
    /**
     * @return pricing type
     */
    public int getPricingType() {
    	return pricingType;
    }
    /**
     * @param pricingType the setPricingType to set
     */
    public final void setPricingType(final int pricingType) {
    	this.pricingType = pricingType;
    }

    /**
     * @return method
     */
    public String getPaymentMethod() {
    	return method;
    }
    /**
     * @param methodType the setPaymentMethod to set
     */
    public final void setPaymentMethod(final String methodType) {
    	this.method = methodType;
    }

    /**
     * @return category
     */
    public String setPointCategory() {
    	return category;
    }
    /**
     * @param pointCategoryType the pointCategory to set
     */
    public final void setPointCategory(final String pointCategoryType) {
    	this.category = pointCategoryType;
    }
    /**
     * @return pointServerUri
     */
    public MemberServer getMemberInfoConfig() {
    	return memberInfoConfig;
    }
    /**
     * @param serverUri the setPointServerUri to set
     */
    public final void setMemberInfoConfig(final MemberServer memberInfoConfig) {
    	this.memberInfoConfig = memberInfoConfig;
    }
    /**
     * @return Uri configuration.
     */
    public UrlConfig getUrlConfig() {
    	return urlConfig;
    }
    /**
     * @param argUrlConfig the Uri configuration.
     */
    public final void setUrlConfig(final UrlConfig argUrlConfig) {
    	urlConfig = argUrlConfig;
    }
    /**
     * @return drawer
     */
    public String getDrawer() {
    	return drawer;
    }
    /**
     * @param drawer the setDrawer to set
     */
    public final void setDrawer(final String drawer) {
    	this.drawer = drawer;
    }
    /**
     * @return drawerAutoOpen
     */
    public String getDrawerAutoOpen() {
    	return drawerAutoOpen;
    }
    /**
     * @param drawerautopen the setDrawerAutoOpen to set
     */
    public final void setDrawerAutoOpen(final String drawerAutoOpen) {
    	this.drawerAutoOpen = drawerAutoOpen;
    }
    
    /**
     * return the value of openDrawer status
     * @return String
     */
    public String getOpenDrawer() {
		return openDrawer;
	}
    
    /**
     * 
     * @param openDrawer
     */
	public void setOpenDrawer(String openDrawer) {
		this.openDrawer = openDrawer;
	}

    /** get max queue able transactions per a store */
    public String getMaxSuspendable() {
        return maxSuspendable;
    }
    /** set max queue able transactions per a store */
	public void setMaxSuspendable(String val) {
        if (!StringUtility.isNullOrEmpty(val) && !("null").equals(val)) {
            maxSuspendable = val.trim();
        } else {
            maxSuspendable = DEFAULT_MAX_SUSPENDABLE;
        }
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