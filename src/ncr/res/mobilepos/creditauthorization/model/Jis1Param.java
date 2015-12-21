package ncr.res.mobilepos.creditauthorization.model;

import java.util.List;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class Jis1Param.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Jis1Param")
public class Jis1Param {

    /** The list of Jis1. */
    @XmlElement(name = "Jis1")
    private List<Jis1> jis1s;

    /**
     * Gets the jis1s.
     *
     * @return jis1s
     */
    public final List<Jis1> getJis1s() {
        return jis1s;
    }

    /**
     * Sets the jis1s.
     *
     * @param jis1List
     *            the new jis1s
     */
    public final void setJis1s(final List<Jis1> jis1List) {
        this.jis1s = jis1List;
    }

}
