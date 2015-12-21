package ncr.res.mobilepos.systemconfiguration.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *  MemberInfoConfig is a model class representing
 *  the display of Inputs for Member information.
 *  Please see: http://jira.ncr.com/browse/RES-4306
 * @author VR185019
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Fantamiliar")
public class FantamiliarConfig extends MemberServer {
    /**
     * The Rate 1.
     */
    @XmlElement(name = "Rate1")
    private String rate1;
    /**
     * The Rate 2.
     */
    @XmlElement(name = "Rate2")
    private String rate2;
    /**
     * The Rate 3.
     */
    @XmlElement(name = "Rate3")
    private String rate3;
    /**
     * @return the Rate1
     */
    public final String getRate1() {
        return rate1;
    }
    /**
     * @param rate1ToSet
     *  the rate1 to set
     */
    public final void setRate1(
            final String rate1ToSet) {
        this.rate1 = rate1ToSet;
    }
    /**
     * @return the Rate2
     */
    public final String getRate2() {
        return rate2;
    }
    /**
     * @param rate2ToSet
     *  the rate2 to set
     */
    public final void setRate2(
            final String rate2ToSet) {
        this.rate2 = rate2ToSet;
    }
    /**
     * @return the Rate3
     */
    public final String getRate3() {
        return rate3;
    }
    /**
     * @param rate3ToSet
     *  the rate3 to set
     */
    public final void setRate3(
            final String rate3ToSet) {
        this.rate3 = rate3ToSet;
    }
}
