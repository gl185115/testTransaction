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
    @ApiModelProperty(value="����t���O", notes="����t���O")
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
    @ApiModelProperty(value="���|�[�g�t���O", notes="���|�[�g�t���O")
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
    @ApiModelProperty(value="�Z�b�g�t���O", notes="�Z�b�g�t���O")
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
    @ApiModelProperty(value="���i�̔��t���O", notes="���i�̔��t���O")
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
    @ApiModelProperty(value="�Ǘ��t���O", notes="�Ǘ��t���O")
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
    @ApiModelProperty(value="�h���A�t���O", notes="�h���A�t���O")
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
