package ncr.res.mobilepos.credential.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

/**
 * holds the data for a User Group.
 * @author AP185142
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Group")
public class UserGroup {
    /**
     * The GroupCode.
     */
    @XmlElement(name = "GroupCode")
    private int groupcode;

    /**
     * gets the GroupCode.
     *
     * @return GroupCode
     */
    public final int getGroupCode() {
        return groupcode;
    }

    /**
     * sets the GroupCode.
     *
     * @param groupCode
     *            - the GroupCode
     */
    public final void setGroupCode(final int groupCode) {
        this.groupcode = groupCode;
    }

    /**
     * The GroupName.
     */
    @XmlElement(name = "GroupName")
    private String groupname;

    /**
     * gets the GroupName.
     *
     * @return GroupName
     */
    public final String getGroupName() {
        return groupname;
    }

    /**
     * sets the GroupName.
     *
     * @param groupName
     *            - the groupName
     */
    public final void setGroupName(final String groupName) {
        this.groupname = groupName;
    }

    /**
     * The Transaction.
     */
    @XmlElement(name = "Transaction")
    private boolean transaction;

    /**
     * gets the Transaction.
     *
     * @return Transaction
     */
    public final boolean isTransaction() {
        return transaction;
    }

    /**
     * sets the Transaction.
     *
     * @param transactionToSet
     *            - the transaction
     */
    public final void setTransaction(final boolean transactionToSet) {
        this.transaction = transactionToSet;
    }

    /**
     * The Reports.
     */
    @XmlElement(name = "Reports")
    private boolean reports;

    /**
     * gets the Reports.
     *
     * @return Reports
     */
    public final boolean isReports() {
        return reports;
    }

    /**
     * sets the Reports.
     *
     * @param reportsToSet
     *            - the reports
     */
    public final void setReports(final boolean reportsToSet) {
        this.reports = reportsToSet;
    }

    /**
     * The Settings.
     */
    @XmlElement(name = "Settings")
    private boolean settings;

    /**
     * gets the Settings.
     *
     * @return Settings
     */
    public final boolean isSettings() {
        return settings;
    }

    /**
     * sets the Settings.
     *
     * @param settingsToSet
     *            - the settings
     */
    public final void setSettings(final boolean settingsToSet) {
        this.settings = settingsToSet;
    }

    /**
     * The Merchandise.
     */
    @XmlElement(name = "Merchandise")
    private boolean merchandise;

    /**
     * gets the Merchandise.
     *
     * @return Merchandise
     */
    public final boolean isMerchandise() {
        return merchandise;
    }

    /**
     * sets the Settings.
     *
     * @param merchandiseToSet
     *            - the merchandise
     */
    public final void setMerchandise(final boolean merchandiseToSet) {
        this.merchandise = merchandiseToSet;
    }

    /**
     * The Administration.
     */
    @XmlElement(name = "Administration")
    private boolean administration;

    /**
     * gets the Administration.
     *
     * @return Administration
     */
    public final boolean isAdministration() {
        return administration;
    }

    /**
     * sets the Administration.
     *
     * @param administrationToSet
     *            - the administration
     */
    public final void setAdministration(final boolean administrationToSet) {
        this.administration = administrationToSet;
    }

    /**
     * The Drawer.
     */
    @XmlElement(name = "Drawer")
    private boolean drawer;

    /**
     * gets the Drawer.
     *
     * @return Drawer
     */
    public final boolean isDrawer() {
        return drawer;
    }

    /**
     * sets the Drawer.
     *
     * @param drawerToSet
     *            - the drawer
     */
    public final void setDrawer(final boolean drawerToSet) {
        this.drawer = drawerToSet;
    }

    /**
     * Convert to string.
     * @return String
     */
    @Override
    public final String toString() {
        ObjectMapper mapper = new ObjectMapper();
        String ret = "";
        try {
            ret += mapper.writeValueAsString(this);
        } catch (Exception ex) {
            ret = super.toString();
        }
        return ret;
    }
}
