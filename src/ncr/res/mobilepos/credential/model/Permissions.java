package ncr.res.mobilepos.credential.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;

/**
 * The Model Class containing the User Group Permissions.
 * @author cc185102
 *
 */
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "Permissions")
@ApiModel(value="Permissions")
public class Permissions {
    /** The Transactions flag. */
    @XmlElement(name = "Transactions")
    private boolean transactions;
    /** The Reports Flag. */
    @XmlElement(name = "Reports")
    private boolean reports;
    /** The Settings Flag. */
    @XmlElement(name = "Settings")
    private boolean settings;
    /** The Merchandise Flag. */
    @XmlElement(name = "Merchandise")
    private boolean merchandise;
    /** The Administration Flag. */
    @XmlElement(name = "Administration")
    private boolean administration;
    /** The Drawer Flag. */
    @XmlElement(name = "Drawer")
    private boolean drawer;
    /**
     * @return the transactions
     */
    @ApiModelProperty(value="取引フラグ", notes="取引フラグ")
    public final boolean isTransactions() {
        return transactions;
    }
    /**
     * @param transactionsToSet the transactions to set
     */
    public final void setTransactions(final boolean transactionsToSet) {
        this.transactions = transactionsToSet;
    }
    /**
     * @return the reports
     */
    @ApiModelProperty(value="レポートフラグ", notes="レポートフラグ")
    public final boolean isReports() {
        return reports;
    }
    /**
     * @param reportsToSet the reports to set
     */
    public final void setReports(final boolean reportsToSet) {
        this.reports = reportsToSet;
    }
    /**
     * @return the settings
     */
    @ApiModelProperty(value="セットフラグ", notes="セットフラグ")
    public final boolean isSettings() {
        return settings;
    }
    /**
     * @param settingsToSet the settings to set
     */
    public final void setSettings(final boolean settingsToSet) {
        this.settings = settingsToSet;
    }
    /**
     * @return the merchandise
     */
    @ApiModelProperty(value="商品販売フラグ", notes="商品販売フラグ")
    public final boolean isMerchandise() {
        return merchandise;
    }
    /**
     * @param merchandiseToSet the merchandise to set
     */
    public final void setMerchandise(final boolean merchandiseToSet) {
        this.merchandise = merchandiseToSet;
    }
    /**
     * @return the administration
     */
    @ApiModelProperty(value="管理フラグ", notes="管理フラグ")
    public final boolean isAdministration() {
        return administration;
    }
    /**
     * @param administrationToSet the administration to set
     */
    public final void setAdministration(final boolean administrationToSet) {
        this.administration = administrationToSet;
    }
    /**
     * @return the drawer
     */
    @ApiModelProperty(value="ドロアフラグ", notes="ドロアフラグ")
    public final boolean isDrawer() {
        return drawer;
    }
    /**
     * @param drawerToSet the drawer to set
     */
    public final void setDrawer(final boolean drawerToSet) {
        this.drawer = drawerToSet;
    }

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
