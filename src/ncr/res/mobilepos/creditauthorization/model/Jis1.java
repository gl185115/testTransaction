package ncr.res.mobilepos.creditauthorization.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Jis1.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Jis1")
public class Jis1 {

    /** The company information. */
    @XmlElement(name = "companyinfo")
    private String companyinfo;

    /** The position. */
    @XmlElement(name = "position")
    private String position;

    /** The digits. */
    @XmlElement(name = "digits")
    private String digits;

    /** The company code begin. */
    @XmlElement(name = "companycodebegin")
    private String companycodebegin;

    /** The company code end. */
    @XmlElement(name = "companycodeend")
    private String companycodeend;

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
     * @param companyInformation
     *            the new company information
     */
    public final void setCompanyinfo(final String companyInformation) {
        this.companyinfo = companyInformation;
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
     * @param positionToSet
     *            the new position
     */
    public final void setPosition(final String positionToSet) {
        this.position = positionToSet;
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
     * Gets the company code begin.
     *
     * @return company code begin
     */
    public final String getCompanycodebegin() {
        return companycodebegin;
    }

    /**
     * Sets the company code begin.
     *
     * @param companyCodeBegin
     *            the new company code begin
     */
    public final void setCompanycodebegin(final String companyCodeBegin) {
        this.companycodebegin = companyCodeBegin;
    }

    /**
     * Gets the company code end.
     *
     * @return company code end
     */
    public final String getCompanycodeend() {
        return companycodeend;
    }

    /**
     * Sets the company code end.
     *
     * @param companyCodeEnd
     *            the new company code end
     */
    public final void setCompanycodeend(final String companyCodeEnd) {
        this.companycodeend = companyCodeEnd;
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
     * @param position1ToSet
     *            the new position1
     */
    public final void setPosition1(final String position1ToSet) {
        this.position1 = position1ToSet;
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
