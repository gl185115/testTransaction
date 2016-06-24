package ncr.res.mobilepos.authentication.model;

import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;

import ncr.res.mobilepos.model.ResultBase;

/**
 * model of the device status.
 *
 */
@XmlRootElement
@ApiModel(value="DeviceStatus")
public class DeviceStatus extends ResultBase {
	/**
     * status code for offline device.
     */
    public static final int STATUS_DEVICEOFFLINE = 0x00;
    /**
     * status code for online device.
     */
    public static final int STATUS_DEVICEONLINE = 0x01;
    /**
     * status code for invalid device.
     */
    public static final int STATUS_DEVICEINVALID = 0x02;
    /**
     * status code for device not found.
     */
    public static final int STATUS_DEVICENOTFOUND = 0x03;
    /**
     * status code for device status no change.
     */
    public static final int STATUS_DEVICESTATUSNOCHANGE = 0x04;
    /**
     * the id of the store.
     */
    private String storeID = "";
    /**
     * the id of the company.
     */
    private String corpID = "";
    /**
     * the name of the company.
     */
    private String corpName = "";
    /**
     * the name of the company.
     */
    private String corpCompany = "";
    /**
     * the name of the store.
     */
    private String storeName = "";
    /**
     * the id of the terminal.
     */
    private String terminalID = "";
    /**
     * the name of the device.
     */
    private String deviceName = "";
    /**
     * the status of the terminal.
     */
    private int terminalStatus = 0;
    /**
     * the url for the web application to access.
     */
    private String webAppUrl = "";
    /**
     * the expiry date of the device status.
     */
    private String activeValidUntil = null;
    /**
     * the signature activation key.
     */
    private ActivationKey activationKey = null;
    /**
     * the status of the signature.
     */
    private int signStatus = 0;    

    /**
     * constructor.
     */
    public DeviceStatus() {
    }

    /**
     * constructor.
     * @param terminalid - the id of the terminal
     * @param terminalstatus - the status of the terminal
     */
    public DeviceStatus(final String terminalid, final int terminalstatus) {
        terminalID = terminalid;
        terminalStatus = terminalstatus;
    }

    /**
     * constructor.
     * @param terminalid - the id of the terminal
     * @param terminalstatus - the status of the terminal
     * @param activevaliduntil - the expiry date of the device status
     */
    public DeviceStatus(final String terminalid, final int terminalstatus,
            final String activevaliduntil) {
        terminalID = terminalid;
        terminalStatus = terminalstatus;
        activeValidUntil = activevaliduntil;
    }

    /**
     * constructor.
     * @param resultcode - the result of the request
     * @param message - the message of the request
     */
    public DeviceStatus(final int resultcode, final String message) {
        super(resultcode, message);
    }

    /**
     * gets the store id.
     * @return String - the store id
     */
    @ApiModelProperty(value="店舗コード", notes="店舗コード")
    public final String getStoreID() {
        return storeID;
    }
    /**
     * sets the store id.
     * @param storeid - the store id to set
     */
    public final void setStoreID(final String storeid) {
        storeID = storeid;
    }
    /**
     * gets the company id.
     * @return String - the company id
     */
    @ApiModelProperty(value="会社コード", notes="会社コード")
    public final String getCorpID() {
        return corpID;
    }

    /**
     * sets the company id.
     * @param corpid - the company id to set
     */
    public final void setCorpID(final String corpid) {
        corpID = corpid;
    }
    /**
     * gets the url for the web application to use.
     * @return String - url of the web application
     */
    @ApiModelProperty(value="アプリケーションURL", notes="アプリケーションURL")
    public final String getWebAppUrl() {
        return webAppUrl;
    }
    /**
     * sets the url of the web application.
     * @param webappurl  - the url of the web application to set
     */
    public final void setWebAppUrl(final String webappurl) {
        webAppUrl = webappurl;
    }
    /**
     * gets the device name.
     * @return String - the name of the device
     */
    @ApiModelProperty(value="デバイス名称", notes="デバイス名称")
    public final String getDeviceName() {
        return deviceName;
    }
    /**
     * sets the device name.
     * @param devicename - the name of the device to set
     */
    public final void setDeviceName(final String devicename) {
        deviceName = devicename;
    }
    /**
     * gets the store name.
     * @return String - the store name
     */
    @ApiModelProperty(value="店舗名称", notes="店舗名称")
    public final String getStoreName() {
        return storeName;
    }
    /**
     * sets the store name.
     * @param storename - the store name to set
     */
    public final void setStoreName(final String storename) {
        storeName = storename;
    }
    /**
     * gets the company name.
     * @return String - the company name
     */
    @ApiModelProperty(value="会社名称", notes="会社名称")
    public final String getCorpName() {
        return corpName;
    }
    /**
     * sets the company name.
     * @param corpname - the company name to set
     */
    public final void setCorpName(final String corpname) {
        corpName = corpname;
    }
    /**
     * gets the company name.
     * @return String - the company name
     */
    @ApiModelProperty(value="会社名称", notes="会社名称")
    public final String getCorpCompany() {
        return corpCompany;
    }
    /**
     * sets the company name.
     * @param corpCompany - the company name to set
     */
    public final void setCorpCompany(final String corpcompany) {
        corpCompany = corpcompany;
    }
    /**
     * gets the terminal id.
     * @return String - the terminal id
     */
    @ApiModelProperty(value="端末コード", notes="端末コード")
    public final String getTerminalID() {
        return terminalID;
    }
    /**
     * sets the terminal id.
     * @param terminalid - the terminal id to set
     */
    public final void setTerminalID(final String terminalid) {
        terminalID = terminalid;
    }
    /**
     * gets the terminal status.
     * @return int - the status of the terminal
     */
    @ApiModelProperty(value="端末ステータス", notes="端末ステータス")
    public final int getTerminalStatus() {
        return terminalStatus;
    }
    /**
     * sets the terminal status.
     * @param terminalstatus - the terminal status to set
     */
    public final void setTerminalStatus(final int terminalstatus) {
        terminalStatus = terminalstatus;
    }
    /**
     * sets the expiry date.
     * @param activevaliduntil - the date of the expiry to set
     */
    public final void setActiveValidUntil(final String activevaliduntil) {
        activeValidUntil = activevaliduntil;
    }
    /**
     * gets the expiry date.
     * @return String - the date of the expiry
     */
    @ApiModelProperty(value="有効期間", notes="有効期間")
    public final String getActiveValidUntil() {
        return activeValidUntil;
    }

    /**
     * sets the activation key.
     * @param activationKeyToSet - the activation key to set
     */
    public final void setActivationKey(final ActivationKey activationKeyToSet) {
        this.activationKey = activationKeyToSet;
    }
    /**
     * gets the activation key.
     * @return ActivationKey  - the activation key model
     */
    @ApiModelProperty(value="アクティベーションキー", notes="アクティベーションキー")
    public final ActivationKey getActivationKey() {
        return activationKey;
    }
    /**
     * gets the sign status.
     * @return int - the status of signature
     */
    @ApiModelProperty(value="サインステータス", notes="サインステータス")
    public final int getSignStatus() {
        return signStatus;
    }
    /**
     * sets the sign status.
     * @param signStatusToSet - the status of the signature to set
     */
    public final void setSignStatus(final int signStatusToSet) {
        this.signStatus = signStatusToSet;
    }

    /**
     * Convert to string.
     * @return String
     */
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
