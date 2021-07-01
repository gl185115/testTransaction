package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import org.codehaus.jackson.map.ObjectMapper;

import com.wordnik.swagger.annotations.ApiModel;
import com.wordnik.swagger.annotations.ApiModelProperty;



/**
 * DeviceInfo
 * Model for representing devices
 * associations for a terminal.
 */
//please note all members are small letters
@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DeviceInfo")
@ApiModel(value="DeviceInfo")
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

    @XmlElement(name = "WsPortNumber")
    private String wsPortNumber;
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
    @ApiModelProperty(value="�[������", notes="�[������")
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
    @ApiModelProperty(value="�v�����^�[ID", notes="�v�����^�[ID")
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
    @ApiModelProperty(value="�X�܃R�[�h", notes="�X�܃R�[�h")
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
    @ApiModelProperty(value="�g���[�j���O�t���O", notes="�g���[�j���O�t���O")
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
    @ApiModelProperty(value="�������O�C���t���O", notes="�������O�C���t���O")
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
    @ApiModelProperty(value="���O�T�C�Y", notes="���O�T�C�Y")
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
    @ApiModelProperty(value="���O�t�@�C���ێ����㐔", notes="���O�t�@�C���ێ����㐔")
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
    @ApiModelProperty(value="�[���R�[�h", notes="�[���R�[�h")
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
    @ApiModelProperty(value="�ڑ���POS�[���ԍ�", notes="�ڑ���POS�[���ԍ�")
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
    @ApiModelProperty(value="�v�����^�[���", notes="�v�����^�[���")
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
    @ApiModelProperty(value="���i�^�C�v", notes="���i�^�C�v")
    public final String getPricingType() {
        return pricingtype;
    }
    /**
     * Gets the authorization link.
     *
     * @return the authorization link
     */
    @ApiModelProperty(value="�F�����N", notes="�F�����N")
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
    @ApiModelProperty(value="Bluetooth�v�����^�[�����N", notes="Bluetooth�v�����^�[�����N")
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
    @ApiModelProperty(value="���������N", notes="���������N")
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
    @ApiModelProperty(value="����ԍ�", notes="����ԍ�")
    public final String getTxid() {
        return txid;
    }
    public final void setTxid(final String txid) {
        this.txid = txid;
    }
    @ApiModelProperty(value="�ۗ�����ԍ�", notes="�ۗ�����ԍ�")
    public final String getSuspendtxid() {
		return suspendtxid;
	}
	public final void setSuspendtxid(final String suspendtxid) {
		this.suspendtxid = suspendtxid;
	}
	@ApiModelProperty(value="�ŏIEJ�V�[�P���X�ԍ�", notes="�ŏIEJ�V�[�P���X�ԍ�")
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
	@ApiModelProperty(value="���R�[�h�X�e�[�^�X", notes="���R�[�h�X�e�[�^�X")
    public final String getStatus() {
        return status;
    }
    public final void setTillId(final String tillId) {
        this.tillid = tillId;
    }
    @ApiModelProperty(value="�h�����[ID", notes="�h�����[ID")
    public final String getTillId() {
    	return tillid;
    }
    @ApiModelProperty(value="�ŏI�X�V�v���O����ID", notes="�ŏI�X�V�v���O����ID")
    public final String getUpdAppId() {
        return updAppId;
    }
    public final void setUpdAppId(final String updAppId) {
        this.updAppId = updAppId;
    }
    @ApiModelProperty(value="�ŏI�X�V���[�U�[ID", notes="�ŏI�X�V���[�U�[ID")
    public final String getUpdOpeCode() {
        return updOpeCode;
    }
    public final void setUpdOpeCode(final String updOpeCode) {
        this.updOpeCode = updOpeCode;
    }
    @ApiModelProperty(value="��ЃR�[�h", notes="��ЃR�[�h")
	public final String getCompanyId() {
		return companyId;
	}
	public final void setCompanyId(final String companyIdToSet) {
		this.companyId = companyIdToSet;
	}
	@ApiModelProperty(value="�v���O�����h�c", notes="�v���O�����h�c")
	public final String getAppId() {
        return appId;
    }
    public final void setAppId(final String appIdToSet) {
        this.appId = appIdToSet;
    }
    @ApiModelProperty(value="���[�U�h�c", notes="���[�U�h�c")
    public final String getOpeCode() {
        return opeCode;
    }
    public final void setOpeCode(final String opeCodeToSet) {
        this.opeCode = opeCodeToSet;
    }
    @ApiModelProperty(value="�����R�[�h", notes="�����R�[�h")
    public final String getAttributeId() {
    	return attributeId;
    }
    public final void setAttributeId(final String attributeId) {
    	this.attributeId = attributeId;
    }
    @ApiModelProperty(value="websocket�ڑ��|�[�g�ԍ�", notes="websocket�ڑ��|�[�g�ԍ�")
    public final String getWsPortNumber() {
    	return wsPortNumber;
    }
    public final void setWsPortNumber(final String wsPortNumber) {
    	this.wsPortNumber = wsPortNumber;
    }
}
