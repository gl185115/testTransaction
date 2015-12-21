package ncr.res.mobilepos.line.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * Class that encapsulates list of Lines.
 */
@XmlRootElement(name = "ViewLine")
@XmlAccessorType(XmlAccessType.NONE)
public class ViewLine extends ResultBase {
    /**
     * List of Lines.
     */
    @XmlElement(name = "Line")
    private Line line;
    /**
     * Store id.
     */
    @XmlElement(name = "RetailStoreID")
    private String retailStoreID;

    /**
     * Gets the list of Lines.
     *
     * @return an array list of Lines.
     */
    public final Line getLine() {
        return line;
    }

    /**
     * Sets list of Lines.
     *
     * @param lineToSet list of Lines.
     */
    public final void setLine(final Line lineToSet) {
        this.line = lineToSet;
    }

    /**
     * Gets the retail storeid.
     *
     * @return retailStoreID
     */
    public final String getRetailStoreID() {
        return retailStoreID;
    }

    /**
     * Sets the retails storeid.
     *
     * @param retailStoreIDToSet storeid.
     */
    public final void setRetailStoreID(final String retailStoreIDToSet) {
        this.retailStoreID = retailStoreIDToSet;
    }

}
