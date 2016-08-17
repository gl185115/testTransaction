package ncr.res.mobilepos.store.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlSeeAlso;

import ncr.res.mobilepos.model.ResultBase;

/**
 *  RES-5500
 * Model Class containing the list of stores.
 * @author VR185019
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "StoreInternSys")
@XmlSeeAlso(Store.class)
public class StoreInternSys extends ResultBase {
    /**
     * the Value of the Store Internal System table
     */
    @XmlElement(name = "Value")
    private String value;
    /**
     * gets the Store Internal System table
     *
     * @return value
     */
    
    public final String getValue() {
        return value;
    }

    /**
     * sets the Store Internal System table
     *
     * @param valueToSet
     *            - the Store Internal System table
     */
    public final void setValue(final String valueToSet) {
        this.value = valueToSet;
    }

    @Override
    public final String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(super.toString()).append("; ");
        sb.append("Value: ").append(value);
        return sb.toString();
    }
}
