package ncr.res.mobilepos.journalization.model.poslog;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * PremiumInfo Model Object.
 *
 * <P>An PremiumInfo Node in POSLog XML.
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "PremiumInfo")
public class PremiumInfo {
    /**
     * Premium
     */
    @XmlElement(name="Premium")
    private List<Premium> premium;
    
    /**
     * @return the premium
     */
    public final List<Premium> getPremium() {
        return premium;
    }
    
    /**
     * @param premium the premium to set
     */
    public final void setPremium(final List<Premium> premium) {
        this.premium = premium;
    }
}
