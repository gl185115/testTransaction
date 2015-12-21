package ncr.res.mobilepos.report.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * The Class ItemLabel.
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ItemLabel")
public class ItemLabel {

    /** The code. */
    private String code = "";

    /** The name. */
    private String name = "";

    /**
     * Sets the code.
     *
     * @param codeString
     *            the new code
     */
    public final void setCode(final String codeString) {
        this.code = codeString;
    }

    /**
     * Sets the name.
     *
     * @param nameString
     *            the new name
     */
    public final void setName(final String nameString) {
        this.name = nameString;
    }

    /**
     * Gets the code.
     *
     * @return the code
     */
    @XmlElement(name = "Code")
    public final String getCode() {
        return this.code;
    }

    /**
     * Gets the name.
     *
     * @return the name
     */
    @XmlElement(name = "Name")
    public final String getName() {
        return this.name;
    }
}
