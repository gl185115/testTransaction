package ncr.res.mobilepos.creditauthorization.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Jis2Param.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Jis2Param")
public class Jis2Param {

    /** The jis2s. */
    @XmlElement(name = "Jis2")
    private List<Jis2> jis2s;

    /**
     * Gets the jis2s.
     *
     * @return jis2
     */
    public final List<Jis2> getJis2s() {
        return jis2s;
    }

    /**
     * Sets the jis2.
     *
     * @param jis2List
     *            the new jis2
     */
    public final void setJis2(final List<Jis2> jis2List) {
        this.jis2s = jis2List;
    }
}
