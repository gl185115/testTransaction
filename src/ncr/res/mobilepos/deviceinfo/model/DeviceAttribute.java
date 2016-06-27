package ncr.res.mobilepos.deviceinfo.model;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import ncr.res.mobilepos.model.ResultBase;


@XmlAccessorType(XmlAccessType.NONE)
@XmlRootElement(name = "DeviceAttribute")
@ApiModel(value="DeviceAttribute")
public class DeviceAttribute extends ResultBase {
    @XmlElement(name = "CompanyId")
    private String companyId;

	@XmlElement(name = "StoreId")
    private String storeId;

	@XmlElement(name = "TerminalId")
	private String terminalId;

	@XmlElement(name = "Training")
    private int training;

	@XmlElement(name = "DeviceName")
	private String deviceName;

	@XmlElement(name = "AttributeId")
	private String attributeId;

	@XmlElement(name = "PrinterId")
	private String printerId;

	@XmlElement(name = "TillId")
	private String tillId;

	@XmlElement(name= "LinkQueueBuster")
	private String linkQueueBuster;

	@XmlElement(name = "PrintDes")
	private String printDes;

	@XmlElement(name = "DrawerId")
	private String drawerId;

	@XmlElement(name = "DisplayName")
	private String displayName;

	@XmlElement(name = "AttributeDes")
	private String attributeDes;

	/**
     * @return the companyId
     */
	@ApiModelProperty(value="会社コード", notes="会社コード")
    public final String getCompanyId() {
        return companyId;
    }

    /**
     * @param companyId the companyId to set
     */
    public final void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

	/**
	 * @return the storeId
	 */
    @ApiModelProperty(value="店舗コード", notes="店舗コード")
	public final String getStoreId() {
		return storeId;
	}

	/**
	 * @param storeId the storeId to set
	 */
	public final void setStoreId(String storeId) {
		this.storeId = storeId;
	}

	/**
	 * @return the terminalId
	 */
	@ApiModelProperty(value="端末番号", notes="端末番号")
	public final String getTerminalId() {
		return terminalId;
	}

	/**
	 * @param terminalId the terminalId to set
	 */
	public final void setTerminalId(final String terminalId) {
		this.terminalId = terminalId;
	}

	/**
     * @return the training mode
     */
	@ApiModelProperty(value="トレーニングフラグ", notes="トレーニングフラグ")
    public final int getTrainingMode() {
        return training;
    }

    /**
     * @param trainingMode to set
     */
    public final void setTrainingMode(final int trainingMode) {
        this.training = trainingMode;
    }

	/**
	 * @return the deviceName
	 */
    @ApiModelProperty(value="端末名称", notes="端末名称")
	public final String getDeviceName() {
		return deviceName;
	}

	/**
	 * @param deviceName the deviceName to set
	 */
	public final void setDeviceName(final String deviceName) {
		this.deviceName = deviceName;
	}

	/**
	 * @return the attributeId
	 */
	@ApiModelProperty(value="属性コード", notes="属性コード")
	public final String getAttributeId() {
		return attributeId;
	}

	/**
	 * @param attributeId the attributeId to set
	 */
	public final void setAttributeId(final String attributeId) {
		this.attributeId = attributeId;
	}

	/**
	 * @return the printerId
	 */
	@ApiModelProperty(value="プリンターID", notes="プリンターID")
	public final String getPrinterId() {
		return printerId;
	}

	/**
	 * @param printerId the printerId to set
	 */
	public final void setPrinterId(final String printerId) {
		this.printerId = printerId;
	}

	/**
	 * @return the tillId
	 */
	@ApiModelProperty(value="ドロワーID", notes="ドロワーID")
	public final String getTillId() {
		return tillId;
	}

	/**
	 * @param tillId the tillId to set
	 */
	public final void setTillId(final String tillId) {
		this.tillId = tillId;
	}

	/**
	 * @return the linkQueueBuster
	 */
	@ApiModelProperty(value="BluetoothプリンターID", notes="BluetoothプリンターID")
	public final String getLinkQueueBuster() {
		return linkQueueBuster;
	}

	/**
	 * @param linkQueueBuster the linkQueueBuster to set
	 */
	public final void setLinkQueueBuster(final String linkQueueBuster) {
		this.linkQueueBuster = linkQueueBuster;
	}

	/**
	 * @return the printDes
	 */
	@ApiModelProperty(value="備考", notes="備考")
	public final String getPrintDes() {
		return printDes;
	}

	/**
	 * @param printDes the printDes to set
	 */
	public final void setPrintDes(final String printDes) {
		this.printDes = printDes;
	}

	/**
	 * @return the drawerId
	 */
	@ApiModelProperty(value="ドロワーコード", notes="ドロワーコード")
	public final String getDrawerId() {
		return drawerId;
	}

	/**
	 * @param drawerId the drawerId to set
	 */
	public final void setDrawerId(final String drawerId) {
		this.drawerId = drawerId;
	}

	/**
	 * @return the displayName
	 */
	@ApiModelProperty(value="iphoneの画面上に表示された名前", notes="iphoneの画面上に表示された名前")
	public final String getDisplayName() {
		return displayName;
	}

	/**
	 * @param displayName the displayName to set
	 */
	public final void setDisplayName(final String displayName) {
		this.displayName = displayName;
	}

	/**
	 * @return the attributeDes
	 */
	@ApiModelProperty(value="記述", notes="記述")
	public final String getAttributeDes() {
		return attributeDes;
	}

	/**
	 * @param attributeDes the attributeDes to set
	 */
	public final void setAttributeDes(final String attributeDes) {
		this.attributeDes = attributeDes;
	}

	@Override
	public String toString() {
		return "DeviceAttribute [storeId=" + storeId + ", terminalId="
				+ terminalId + ", deviceName=" + deviceName + ", attributeId="
				+ attributeId + ", printerId=" + printerId + ", tillId="
				+ tillId + ", linkQueueBuster=" + linkQueueBuster
				+ ", printDes=" + printDes + ", drawerId=" + drawerId
				+ ", displayName=" + displayName + ", attributeDes="
				+ attributeDes + "]";
	}

}
