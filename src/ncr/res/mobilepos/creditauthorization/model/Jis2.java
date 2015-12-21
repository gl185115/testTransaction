package ncr.res.mobilepos.creditauthorization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Jis2.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Jis2")
public class Jis2 {

    /** The company information. */
    @XmlElement(name = "companyinfo")
    private String companyinfo;

    /** The id mark. */
    @XmlElement(name = "idmark")
    private String idmark;

    /** The category code. */
    @XmlElement(name = "categorycode")
    private String categorycode;

    /** The corporate code. */
    @XmlElement(name = "corpcode")
    private String corpcode;

    /** The position. */
    @XmlElement(name = "position")
    private String position;

    /** The digits. */
    @XmlElement(name = "digits")
    private String digits;

    /** The company code. */
    @XmlElement(name = "companycode")
    private String companycode;

    /** The account number digits. */
    @XmlElement(name = "accountnumberdigits")
    private String accountnumberdigits;

    /** The position1. */
    @XmlElement(name = "position1")
    private String position1;

    /** The digits1. */
    @XmlElement(name = "digits1")
    private String digits1;

    /** The expiration position. */
    @XmlElement(name = "expirationposition")
    private String expirationposition;

    /** The crcompany code. */
    @XmlElement(name = "crcompanycode")
    private String crcompanycode;

    /**
     * Gets the company information.
     *
     * @return company information
     */
    public final String getCompanyinfo() {
        return companyinfo;
    }

    /**
     * Sets the company information.
     *
     * @param companyInfo
     *            the new company information
     */
    public final void setCompanyinfo(final String companyInfo) {
        this.companyinfo = companyInfo;
    }

    /**
     * Gets the id mark.
     *
     * @return id mark
     */
    public final String getIdmark() {
        return idmark;
    }

    /**
     * Sets the id mark.
     *
     * @param idMark
     *            the new id mark
     */
    public final void setIdmark(final String idMark) {
        this.idmark = idMark;
    }

    /**
     * Gets the category code.
     *
     * @return category code
     */
    public final String getCategorycode() {
        return categorycode;
    }

    /**
     * Sets the category code.
     *
     * @param categoryCode
     *            the new category code
     */
    public final void setCategorycode(final String categoryCode) {
        this.categorycode = categoryCode;
    }

    /**
     * Gets the corporate code.
     *
     * @return corporate code
     */
    public final String getCorpcode() {
        return corpcode;
    }

    /**
     * Sets the corporate code.
     *
     * @param corpCode
     *            the new corporate code
     */
    public final void setCorpcode(final String corpCode) {
        this.corpcode = corpCode;
    }

    /**
     * Gets the position.
     *
     * @return position
     */
    public final String getPosition() {
        return position;
    }

    /**
     * Sets the position.
     *
     * @param pos
     *            the new position
     */
    public final void setPosition(final String pos) {
        this.position = pos;
    }

    /**
     * Gets the digits.
     *
     * @return digits
     */
    public final String getDigits() {
        return digits;
    }

    /**
     * Sets the digits.
     *
     * @param digitsToSet
     *            the new digits
     */
    public final void setDigits(final String digitsToSet) {
        this.digits = digitsToSet;
    }

    /**
     * Gets the company code.
     *
     * @return company code
     */
    public final String getCompanycode() {
        return companycode;
    }

    /**
     * Sets the company code.
     *
     * @param companyCode
     *            the new company code
     */
    public final void setCompanycode(final String companyCode) {
        this.companycode = companyCode;
    }

    /**
     * Gets the account number digits.
     *
     * @return account number digits
     */
    public final String getAccountnumberdigits() {
        return accountnumberdigits;
    }

    /**
     * Sets the account number digits.
     *
     * @param accountNumberDigits
     *            the new account number digits
     */
    public final void setAccountnumberdigits(final String accountNumberDigits) {
        this.accountnumberdigits = accountNumberDigits;
    }

    /**
     * Gets the position1.
     *
     * @return position1
     */
    public final String getPosition1() {
        return position1;
    }

    /**
     * Sets the position1.
     *
     * @param pos1
     *            the new position1
     */
    public final void setPosition1(final String pos1) {
        this.position1 = pos1;
    }

    /**
     * Gets the digits1.
     *
     * @return digits1
     */
    public final String getDigits1() {
        return digits1;
    }

    /**
     * Sets the digits1.
     *
     * @param digits1ToSet
     *            the new digits1
     */
    public final void setDigits1(final String digits1ToSet) {
        this.digits1 = digits1ToSet;
    }

    /**
     * Gets the expiration position.
     *
     * @return expiration position
     */
    public final String getExpirationposition() {
        return expirationposition;
    }

    /**
     * Sets the expiration position.
     *
     * @param expirationPosition
     *            the new expiration position
     */
    public final void setExpirationposition(final String expirationPosition) {
        this.expirationposition = expirationPosition;
    }

    /**
     * Gets the crcompany code.
     *
     * @return crcompany code
     */
    public final String getCrcompanycode() {
        return crcompanycode;
    }

    /**
     * Sets the crcompany code.
     *
     * @param crCompanyCode
     *            the new crcompany code
     */
    public final void setCrcompanycode(final String crCompanyCode) {
        this.crcompanycode = crCompanyCode;
    }
}
