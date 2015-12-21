package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;



/**
 * DeviceInfo
 * Model for representing devices
 * associations for a terminal.
 */
//please note all members are small letters
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DeviceInfo")
public class DeviceInfo {
	 /**
     * The id of the company.
     */
    @XmlElement(name = "CompanyId")
    private String companyId;
    /**
     * The retail store id where the device
     * is registered.
     */
    @XmlElement(name = "RetailStoreID")
    private String storeid;
    /**
     * The id of the device.
     */
    @XmlElement(name = "DeviceID")
    private String deviceid;
    /**
     * The name of the device.m.mm,
     */
    @XmlElement(name = "Name")
    private String devicename;    
    /**
     * The training flag where the device
     * is registered.
     */
    @XmlElement(name = "Training")
    private int training;
    /**
     * The flag for device logs auto upload.
     */
    @XmlElement(name = "LogAutoUpload")
    private String logautoupload;
    /**
     * The log size in day unit.
     */
    @XmlElement(name = "LogSize")
    private String logsize;
    /**
     * The number of past logs saved from device.
     */
    @XmlElement(name = "SaveLogFile")
    private String savelogfile;
    /**
     * The posterminal link identifier is set here.
     */
    @XmlElement(name = "POSLink")
    private String linkposterminalid;
    /**
     * The pricingtype. Holds the method of pricing.
     */
    @XmlElement(name = "PricingType")
    private String pricingtype;
    /**
     * The id of the printer.
     */
    @XmlElement(name = "Printer")
    private String printerid;
    /**
     * The printer associated with the device is set here.
     */
    @XmlElement(name = "PrinterInfo")
    private PrinterInfo printerinfo;
    /**
     * The tillid of the device.
     */
    @XmlElement(name = "TillID")
    private String tillid;
    /**
     * The authorization link of the device.
     */
    /**
     * The queuebuster link of the device.
     */
    @XmlElement(name = "QueueBusterLink")
    private String queuebusterlink;
    /**
     * The authorization link of the device.
     */
    @XmlElement(name = "AuthorizationLink")
    private String authorizationlink;
    /**
     * The signature link of the device.
     */
    @XmlElement(name = "SignatureLink")
    private String signaturelink;
    /**
     * The last transaction number.
     */
    @XmlElement(name = "Txid")
    private  String txid;
    /**
     * The last suspend transaction number.
     */
    @XmlElement(name = "SuspendTxid")
    private  String suspendtxid;
    
    @XmlElement(name = "EjSequence")
    private int ejSequence;
    
    @XmlElement(name = "Status")
	private String status;
    
    @XmlElement(name = "AppId")
    private String appId;
    
    @XmlElement(name = "OpeCode")
    private String opeCode;

    private String updAppId;
    
    private String updOpeCode;
    
    @XmlElement(name = "AttributeId")
    private String attributeId;

    /**
     * 
     * Setter for the device name.
     * @param devicenameToSet the name of the device
     */
    public final void setDeviceName(final String devicenameToSet) {
        devicename = devicenameToSet;
    }
    /**
     * Getter for the device name.
     * @return String the device name
     */
    public final String getDeviceName() {
        return devicename;
    }
    /**
     * Setter for the printer id.
     * @param printeridToSet the id of the printer
     */
    public final void setPrinterId(final String printeridToSet) {
        printerid = printeridToSet;
    }
    /**
     * Getter for the printer id.
     * @return String the printer id
     */
    public final String getPrinterId() {
        return printerid;
    }
    /**
     * Setter for the retail store id.
     * @param retailstoreidToSet the id of the store
     */
    public final void setRetailStoreId(final String retailstoreidToSet) {
        storeid = retailstoreidToSet;
    }
    /**
     * Getter for the retail store id.
     * @return String the retail store id
     */
    public final String getRetailStoreId() {
        return storeid;
    }
    /**
     * Setter for the training flag.
     * @param trainingFlagToSet to set for device
     */
    public final void setTrainingMode(final int trainingFlagToSet) {
        training = trainingFlagToSet;
    }
    /**
     * Getter for the training flag.
     * @return int the training flag
     */
    public final int getTrainingMode() {
        return training;
    }
    /**
     * Setter for the log auto upload flag.
     * @param logautouploadToSet the flag of the log auto upload
     */
    public final void setLogAutoUpload(final String logautouploadToSet) {
        logautoupload = logautouploadToSet;
    }
    /**
     * Getter for the log auto upload flag.
     * @return String the log auto upload flag
     */
    public final String getLogAutoUpload() {
        return logautoupload;
    }
    /**
     * Setter for the log size.
     * @param logsizeToSet the size of the log
     */
    public final void setLogSize(final String logsizeToSet) {
        logsize = logsizeToSet;
    }
    /**
     * Getter for the log size.
     * @return String the log size
     */
    public final String getLogSize() {
        return logsize;
    }
    /**
     * Setter for the save log file.
     * @param savelogfileToSet the size of the log
     */
    public final void setSaveLogFile(final String savelogfileToSet) {
        savelogfile = savelogfileToSet;
    }
    /**
     * Getter for the save log file.
     * @return String the save log file
     */
    public final String getSaveLogFile() {
        return savelogfile;
    }
    /**
     * Setter for the device id.
     * @param deviceidToSet the id of the device
     */
    public final void setDeviceId(final String deviceidToSet) {
        deviceid = deviceidToSet;
    }
    /**
     * Getter for the device id.
     * @return String the device id
     */
    public final String getDeviceId() {
        return deviceid;
    }
    /**
     * Setter for the pos terminal link.
     * @param linkPosTerminalId the posterminal to link
     *                            to the device
     */
    public final void setLinkPOSTerminalId(final String linkPosTerminalId) {
        String link = linkPosTerminalId;
        if (link != null) {
            link = link.trim();
        }
        linkposterminalid = link;
    }
    /**
     * Getter for the pos terminal link.
     * @return String the pos terminal link id
     */
    public final String getLinkPOSTerminalId() {
        return linkposterminalid;
    }
    /**
     * Setter for the printer information.
     * @param printerInfo printer to associate
     *                            to the device
     */
    public final void setPrinterInfo(final PrinterInfo printerInfo) {
        printerinfo = printerInfo;
    }
    /**
     * Getter for the printer info.
     * @return PrinterInfo printer info
     */
    public final PrinterInfo getPrinterInfo() {
        return printerinfo;
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
    /**
     * Sets the pricingtype.
     *
     * @param pricingType the new pricingtype
     */
    public final void setPricingType(final String pricingType) {
        this.pricingtype = pricingType;
    }
    /**
     * Gets the pricingtype.
     *
     * @return the pricingtype
     */
    public final String getPricingType() {
        return pricingtype;
    }
    /**
     * Gets the authorization link.
     *
     * @return the authorization link
     */
    public final String getAuthorizationlink() {
        return authorizationlink;
    }
    /**
     * Sets the authorization link.
     *
     * @param authorizationlinkToSet the new authorization link
     */
    public final void setAuthorizationlink(
            final String authorizationlinkToSet) {
        String link = authorizationlinkToSet;
        if (link != null) {
            link = link.trim();
        }
        this.authorizationlink = link;
    }
    /**
     * Gets the queuebuster link.
     *
     * @return the queuebuster link
     */
    public final String getQueuebusterlink() {
        return queuebusterlink;
    }
    /**
     * Sets the queuebuster link.
     *
     * @param queuebusterlinkToSet the new queuebuster link
     */
    public final void setQueuebusterlink(
            final String queuebusterlinkToSet) {
        String link = queuebusterlinkToSet;
        if (link != null) {
            link = link.trim();
        }
        this.queuebusterlink = link;
    }
    /**
     * Gets the signature link.
     *
     * @return the signature link
     */
    public final String getSignaturelink() {
        return signaturelink;
    }
    /**
     * Sets the signature link.
     *
     * @param signaturelinkToSet the new signature link
     */
    public final void setSignaturelink(
            final String signaturelinkToSet) {
        String link = signaturelinkToSet;
        if (link != null) {
            link = link.trim();
        }
        this.signaturelink = link;
    }
    public final String getTxid() {
        return txid;
    }
    public final void setTxid(final String txid) {
        this.txid = txid;
    }
    public final String getSuspendtxid() {
		return suspendtxid;
	}
	public final void setSuspendtxid(final String suspendtxid) {
		this.suspendtxid = suspendtxid;
	}
	public final int getEjSequence() {
        return ejSequence;
    }
    public final void setEjSequence(final int ejSequence) {
        this.ejSequence = ejSequence;
    }
	public void setStatus(String status) {
		this.status = status;
	}
	/**
     * Getter for the device status.
     * @return String the device name
     */
    public final String getStatus() {
        return status;
    }
    public final void setTillId(final String tillId) {
        this.tillid = tillId;
    }
    public final String getTillId() {
    	return tillid;
    }
    public final String getUpdAppId() {
        return updAppId;
    }
    public final void setUpdAppId(final String updAppId) {
        this.updAppId = updAppId;
    }
    public final String getUpdOpeCode() {
        return updOpeCode;
    }
    public final void setUpdOpeCode(final String updOpeCode) {
        this.updOpeCode = updOpeCode;
    }
	public final String getCompanyId() {
		return companyId;
	}
	public final void setCompanyId(final String companyIdToSet) {
		this.companyId = companyIdToSet;
	}
	public final String getAppId() {
        return appId;
    }
    public final void setAppId(final String appIdToSet) {
        this.appId = appIdToSet;
    }
    public final String getOpeCode() {
        return opeCode;
    }
    public final void setOpeCode(final String opeCodeToSet) {
        this.opeCode = opeCodeToSet;
    }
    public final String getAttributeId() {
    	return attributeId;
    }
    public final void setAttributeId(final String attributeId) {
    	this.attributeId = attributeId;
    }
}
