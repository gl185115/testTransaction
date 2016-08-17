package ncr.res.mobilepos.classinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import ncr.res.mobilepos.model.ResultBase;

/**
 * Class that encapsulates list of ClassInfo.
 */
@XmlRootElement(name = "ViewClassInfo")
@XmlAccessorType(XmlAccessType.NONE)
public class ViewClassInfo extends ResultBase {
    /**
     * ClassInfo object.
     */
    @XmlElement(name = "Class")
    private ClassInfo classInfo;
    /**
     * Store id.
     */
    @XmlElement(name = "RetailStoreID")
    private String retailStoreID;

    /**
     * Gets ClassInfo object
     *
     * @return an ClassInfo.
     */
    public final ClassInfo getClassInfo() {
        return classInfo;
    }

    /**
     * Sets ClassInfo object
     *
     * @param classInfoToSet list of ClassInfo object.
     */
    public final void setClassInfo(final ClassInfo classInfoToSet) {
        this.classInfo = classInfoToSet;
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
