package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Sale Model Object.
 *
 * <P>A LoyaltyAccount Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "LoyaltyAccount")
public class LoyaltyAccount {

    /**
     * ConnCode.
     */
    @XmlElement(name = "ConnCode")
    private String connCode;

    /**
     * @param connCode the connCode to set
     */
    public final void setConnCode(final String connCode) {
        this.connCode = connCode;
    }
    /**
     * @return the connCode
     */
    public final String getConnCode() {
        return connCode;
    }

    /**
     * ConnName.
     */
    @XmlElement(name = "ConnName")
    private String connName;

    /**
     * @param connName the connName to set
     */
    public final void setConnName(final String connName) {
        this.connName = connName;
    }
    /**
     * @return the connName
     */
    public final String getConnName() {
        return connName;
    }

	/**
     * ConnTel.
     */
    @XmlElement(name = "ConnKanaName")
    private String connKanaName;

    /**
     * @param connKanaName the connKanaName to set
     */
    public final void setConnKanaName(final String connKanaName) {
        this.connKanaName = connKanaName;
    }
    /**
     * @return the connKanaName
     */
    public final String getConnKanaName() {
        return connKanaName;
    }

    /**
     * ConnTel.
     */
    @XmlElement(name = "ConnTel")
    private String connTel;

    /**
     * @param connTel the connTel to set
     */
    public final void setConnTel(final String connTel) {
        this.connTel = connTel;
    }
    /**
     * @return the connTel
     */
    public final String getConnTel() {
        return connTel;
    }
}
