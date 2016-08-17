package ncr.res.mobilepos.journalization.model.poslog;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;

/**
 * ItemID Model Object.
 *
 * <P>An ItemID Node in POSLog XML.
 *
 * <P>The ItemID node is under Sale Node.
 * And holds the value that is PLU Code, its length and type
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "ItemID")
public class ItemID {
   /**
    * The PLU type.
    */
    @XmlAttribute(name = "Type")
    private String type;
    /**
     * The PLU code.
     */
    @XmlValue
    private String pluCode;
    /**
     * The Getter method for the PLU type.
     * @return The PLU type.
     */
    public final String getType() {
        return type;
    }
    /**
     * The Setter method for the PLU type.
     * @param typeToSet The type to set.
     */
    public final void setType(final String typeToSet) {
        this.type = typeToSet;
    }
    /**
     * The Getter method for the PLU code.
     * @return The PLU code.
     */
    public final String getPluCode() {
        return pluCode;
    }
    /**
     * The Setter method for the PLU code.
     * @param pluCodeToSet The PLU code to set.
     */
    public final void setPluCode(final String pluCodeToSet) {
        this.pluCode = pluCodeToSet;
    }
}
