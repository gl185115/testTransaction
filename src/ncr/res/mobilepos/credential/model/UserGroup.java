package ncr.res.mobilepos.credential.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

/**
 * holds the data for a User Group.
 * @author AP185142
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Group")
@ApiModel(value="UserGroup")
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
    @ApiModelProperty(value="グループコード", notes="グループコード")
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
    @ApiModelProperty(value="グループ名称", notes="グループ名称")
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
    @ApiModelProperty(value="取引フラグ", notes="取引フラグ")
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
    @ApiModelProperty(value="レポートフラグ", notes="レポートフラグ")
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
    @ApiModelProperty(value="セットフラグ", notes="セットフラグ")
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
    @ApiModelProperty(value="商品販売フラグ", notes="商品販売フラグ")
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
    @ApiModelProperty(value="管理フラグ", notes="管理フラグ")
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
    @ApiModelProperty(value="ドロアフラグ", notes="ドロアフラグ")
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
