package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Detail Model Object.
 *
 * <P>An Detail Node in POSLog XML.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Detail")
public class Detail {
    /**
     * Basic
     */
    @XmlElement(name = "Basic")
    private Basic basic;
    /**
     * Additional
     */
    @XmlElement(name = "Additional")
    private Additional additional;
    /**
     * @return the basic
     */
    public final Basic getBasic() {
        return basic;
    }
    /**
     * @param basic the basic to set
     */
    public final void setBasic(final Basic basic) {
        this.basic = basic;
    }
    /**
     * @return the additional
     */
    public final Additional getAdditional() {
        return additional;
    }
    /**
     * @param additional the additional to set
     */
    public final void setAdditional(final Additional additional) {
        this.additional = additional;
    }
}
