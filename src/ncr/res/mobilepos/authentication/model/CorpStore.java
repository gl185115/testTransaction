package ncr.res.mobilepos.authentication.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * represents a corpstore.
 * @author AP185142
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Store")
public class CorpStore {

    /**
     * the storeid.
     */
    @XmlElement(name = "RetailStoreID")
    private String storeid;

    /**
     * the storename.
     */
    @XmlElement(name = "StoreName")
    private String storename;

    /**
     * the passcode.
     */
    @XmlElement(name = "Passcode")
    private String passcode;

   /**
    * the registration permission.
    */
    @XmlElement(name = "Permission")
    private int permission;

    /**
     * the companyname.
     */
    @XmlElement(name = "CompanyName")
    private String companyname;
    
    /**
     * gets the passcode.
     * @return passcode
     */
    public final String getPasscode() {
        return passcode;
    }

    /**
     * sets the passcode.
     * @param passcodeToSet the passcode
     */
    public final void setPasscode(final String passcodeToSet) {
        this.passcode = passcodeToSet;
    }

    /**
     * gets the permission.
     * @return permission
     */
    public final int getPermission() {
        return permission;
    }

    /**
     * sets the permission.
     * @param permissionToSet the permission
     */
    public final void setPermission(final int permissionToSet) {
        this.permission = permissionToSet;
    }

    /**
     * gets the storename.
     * @return storename
     */
    public final String getStorename() {
        return storename;
    }

    /**
     * sets the storename.
     * @param storenameToSet the storename
     */
    public final void setStorename(final String storenameToSet) {
        this.storename = storenameToSet;
    }

    /**
     * sets the storeid.
     * @param storeidToSet the storeid
     */
    public final void setStoreid(final String storeidToSet) {
        this.storeid = storeidToSet;
    }

    /**
     * gets the storeid.
     * @return the storeid
     */
    public final String getStoreid() {
        return storeid;
    }
    
    /**
     * gets the companyname.
     * @return companyname
     */
    public final String getCompanyName() {
        return companyname;
    }

    /**
     * sets the companyname.
     * @param companynameToSet the companyname
     */
    public final void setCompanyName(final String companynameToSet) {
        this.companyname = companynameToSet;
    }
}
